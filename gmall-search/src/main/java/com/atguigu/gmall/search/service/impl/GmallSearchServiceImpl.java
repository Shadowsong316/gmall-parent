package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.constant.EsConstant;
import com.atguigu.gmall.search.GmallSearchService;
import com.atguigu.gmall.to.es.EsProduct;
import com.atguigu.gmall.to.es.SearchParam;
import com.atguigu.gmall.to.es.SearchResponse;
import com.atguigu.gmall.to.es.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Component
@Service(version = "1.0")
public class GmallSearchServiceImpl implements GmallSearchService {
    @Autowired
    private JestClient jestClient;

    @Override
    public boolean saveProductInfoToEs(EsProduct esProduct) {
        Index build = new Index.Builder(esProduct).index(EsConstant.ES_PRODUCT_INDEX).type(EsConstant.ES_PRODUCT_TYPE)
                .id(esProduct.getId().toString()).build();
        DocumentResult execute = null;
        try {
            execute = jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return execute.isSucceeded();

    }

    @Override
    public SearchResponse searchProduct(SearchParam param) throws IOException {
        //1.根据页面传递的参数构建检索的DSL语句
        String queryDSL = buildSearchDsl(param);
        Search search = new Search.Builder(queryDSL).build();
        //2.执行查询
        SearchResult result = jestClient.execute(search);
        //3.封装和分析查询结果
        SearchResponse response = buildSearchResult(result);
        return response;
    }

    private SearchResponse buildSearchResult(SearchResult result) {
        System.out.println(result.getTotal() + "==>" + result.toString());
        SearchResponse response = new SearchResponse();
        //1、封装所有的商品信息
        List<SearchResult.Hit<EsProduct, Void>> hitList = result.getHits(EsProduct.class);
        for (SearchResult.Hit<EsProduct, Void> hit : hitList) {
            EsProduct source = hit.source;
            response.getProducts().add(source);
        }
        /**
         *  {name:"品牌",values:["小米","苹果"]}
         */
        //2、封装属性信息
        //2.1）、封装品牌进response
        MetricAggregation aggregations = result.getAggregations();
        SearchResponseAttrVo brandId = new SearchResponseAttrVo();
        brandId.setName("品牌");
        //2.2获取到品牌
        aggregations.getTermsAggregation("brandIdAgg").getBuckets().forEach(b -> {
            b.getTermsAggregation("brandNameAgg").getBuckets().forEach(bb -> {
                String key = bb.getKey();
                brandId.getValue().add(key);
            });
        });
        response.setBrand(brandId);
        //2.3 获取到分类
        SearchResponseAttrVo category = new SearchResponseAttrVo();
        category.setName("分类");
        aggregations.getTermsAggregation("categoryIdAgg").getBuckets().forEach(b -> {
            b.getTermsAggregation("productCategoryAgg").getBuckets().forEach(bb -> {
                String key = bb.getKey();
                category.getValue().add(key);
            });
        });
        response.setCatelog(category);
        //2.4）、获取到属性
        List<TermsAggregation.Entry> buckets = aggregations.getChildrenAggregation("productAttrAgg")
                .getChildrenAggregation("productAttrIdAgg")
                .getTermsAggregation("productAttrIdAgg")
                .getBuckets();
        buckets.forEach(b -> {
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            //第一层属性id
            attrVo.setProductAttributeId(Long.parseLong(b.getKey()));
            b.getTermsAggregation("prductAttrNameAgg").getBuckets().forEach(bb -> {
                //第二次是属性的名
                attrVo.setName(bb.getKey());
                bb.getTermsAggregation("productAttrValueAgg").getBuckets().forEach(bbb -> {
                    //第三层是属性的值
                    attrVo.getValue().add(bbb.getKey());
                });
            });
            response.getAttrVos().add(attrVo);
        });
        return response;
    }


    private String buildSearchDsl(SearchParam param) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1查询subTitle与keywords作为加分项
        if (!StringUtils.isEmpty(param.getKeyword())) {
            //must必须满足，match用来做模糊的,term是用来做精确的
            //filter和match一样但不计算相关性评分
            boolQuery.must(QueryBuilders.matchQuery("name", param.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("subTitle", param.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("keywords", param.getKeyword()));
        }
        //2过滤
        if (param.getCatelog3Id() != null) {
            //分类ID
            boolQuery.filter(QueryBuilders.termsQuery("productCategoryId", param.getCatelog3Id()));
        }
        if (param.getBrandId() != null) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        //传了属性
        if (param.getProps() != null && param.getProps().length > 0) {
            for (String prop : param.getProps()) {
                String productAttrId = prop.split(":")[0];
                String productAttrValue = prop.split(":")[1];
                BoolQueryBuilder must = boolQuery.filter(
                        QueryBuilders.nestedQuery("attrValueList", QueryBuilders.boolQuery()
                                .must(QueryBuilders.termsQuery("attrValueList.productAttributeId", productAttrId))
                                .must(QueryBuilders.termsQuery("attrValueList.value", productAttrValue)), ScoreMode.None));
                //过滤属性
                boolQuery.filter(QueryBuilders.nestedQuery("attrValueList",must, ScoreMode.None));
            }
        }
        String[] props = param.getProps();
        if (props!=null){
            for (String prop : props) {
                String values = prop.split(":")[1];
                String[] split = values.split("-");
                QueryBuilders.boolQuery().must(QueryBuilders.termQuery("attrValueList.productAttributeId",prop.split(":")[0]))
                        .must(QueryBuilders.termsQuery("attrValueList.value",split));
            }
        }
        //价格区间过滤
        if(param.getPriceFrom()!=null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(param.getPriceFrom()));
        }
        if(param.getPriceTo()!=null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").lte(param.getPriceTo()));
        }
        searchSource.query(boolQuery);
        //2聚合
//        searchSource.aggregation(AggregationBuilders.)
        //2.1聚合品牌信息
        TermsAggregationBuilder brandAggs = AggregationBuilders
                .terms("brandIdAgg").field("brandId").size(100)
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName").size(10));
        searchSource.aggregation(brandAggs);

        //2.2聚合分类信息
        TermsAggregationBuilder categoryAggs = AggregationBuilders.terms("categoryIdAgg")
                .field("productCategoryId").size(100)
                .subAggregation(AggregationBuilders.terms("productCategoryAgg")
                        .field("productCategoryName").size(100));
        searchSource.aggregation(categoryAggs);
        //2.3属性聚合
        FilterAggregationBuilder filter = AggregationBuilders.filter("productAttrIdAgg", QueryBuilders.termQuery("attrValueList.type", "1"));
        filter.subAggregation(AggregationBuilders.terms("productAttrIdAgg")
                .field("attrValueList.productAttributeId").size(100)
                .subAggregation(AggregationBuilders.terms("prductAttrNameAgg")
                        .field("attrValueList.name").size(100)
                        .subAggregation(AggregationBuilders.terms("productAttrValueAgg")
                                .field("attrValueList.value").size(100))));
        //2.3总的
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("productAttrAgg", "attrValueList")
                .subAggregation(filter);
        searchSource.aggregation(attrAgg);
        //3高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name").preTags("<b style='color:red'>").postTags("</b>");
        searchSource.highlighter(highlightBuilder);
        //4分页
        searchSource.from((param.getPageNum() - 1) * param.getPageSize());
        searchSource.size(param.getPageSize());
        //5 排序
        if (!StringUtils.isEmpty(param.getOrder())){
            String order = param.getOrder();
            String type = order.split(":")[0];
            String asc = order.split(":")[1];
            if ("0".equals(type)){
                searchSource.sort(SortBuilders.scoreSort().order(SortOrder.fromString(asc)));
            }
            if ("1".equals(type)){
                searchSource.sort(SortBuilders.fieldSort("sale").order(SortOrder.fromString(asc)));
            }
            if ("2".equals(type)){
                searchSource.sort(SortBuilders.fieldSort("price").order(SortOrder.fromString(asc)));
            }
//            if ("3".equals(type)){
//                String from = asc.split("-")[0];
//                String to = asc.split("-")[1];
//                //细节
//                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(from).lte(to));
//            }
        }
        searchSource.query(boolQuery);
        System.out.println(searchSource.toString());
        return searchSource.toString();
    }
}
