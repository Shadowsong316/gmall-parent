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
import org.elasticsearch.index.query.TermsQueryBuilder;
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
/*************************************新增商品全部信息到ES 商品上架**********************************************/
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
    //TODO 重新做一遍
    /*************************************在ES中查询商品信息**********************************************/
    @Override
    public SearchResponse searchProduct(SearchParam param) throws IOException {
        String queryDSL=buildSearchDSL(param);
        Search search = new Search.Builder(queryDSL).addIndex("gulishop").addType("product").build();
        SearchResult result = jestClient.execute(search);
        SearchResponse response=buildSearchResult(result);
        //分装分页信息
        response.setTotal(result.getTotal());
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());
        return response;
    }
    /*************************************在ES中查询商品信息**********************************************/

    /*************************************根据前端传的参数创建DSL语句**********************************************/
    private String buildSearchDSL(SearchParam param) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        //1查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1关键字
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("name",param.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("subTitle",param.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("keywords",param.getKeyword()));
        }
        // 1.2过滤分类和品牌
        if (param.getCatelog3Id()!=null){
            boolQuery.filter(QueryBuilders.termsQuery("productCategoryId",param.getCatelog3Id()));
        }
        if (param.getBrandId()!=null){
            boolQuery.filter(QueryBuilders.termsQuery("brandId",param.getBrandId()));
        }
        //TODO 1.3过滤属性
        if (param.getProps()!=null&&param.getProps().length>0){
            String[] props = param.getProps();
            for (String prop : props) {
                String productAttrId = prop.split(":")[0];
                String productAttrValue = prop.split(":")[1];
                TermsQueryBuilder attrIdQ = QueryBuilders.termsQuery("attrValueList.productAttributeId", productAttrId);
                TermsQueryBuilder attrValueQ = QueryBuilders.termsQuery("attrValueList.value", productAttrValue);
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(attrIdQ).must(attrValueQ);
                boolQuery.filter(QueryBuilders.nestedQuery("attrValueList",boolQueryBuilder,ScoreMode.None));
            }
        }
        String[] props = param.getProps();
        if (props!=null){
            //props:a-b-c
            for (String prop : props) {
                String values = prop.split(":")[1];
                String[] split = values.split("-");
                BoolQueryBuilder must = QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("attrValueList.productAttributeId", prop.split(":")[0]))
                        .must(QueryBuilders.termQuery("attrValueList.value", split));
                boolQuery.filter(QueryBuilders.nestedQuery("attrValueList",must,ScoreMode.None));
            }
        }


        //1.4 过滤价格区间
        if (param.getPriceFrom()!=null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(param.getPriceFrom()));
        }
        if (param.getPriceTo()!=null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").lte(param.getPriceTo()));
        }
        //1过滤完成
        searchSource.query(boolQuery);
        //2聚合
        //2.1、聚合品牌信息
        TermsAggregationBuilder brandIdAgg = AggregationBuilders.terms("brandIdAgg").field("brandId").size(100)
                .subAggregation(AggregationBuilders.terms("brandName").field("brandName").size(100));
        searchSource.aggregation(brandIdAgg);//////////
        //2.2 聚合分类信息
        TermsAggregationBuilder categoryIdAgg = AggregationBuilders.terms("categoryId").field("productCategoryId").size(100)
                .subAggregation(AggregationBuilders.terms("productCategoryNameAgg").field("productCategoryName").size(100));
        searchSource.aggregation(categoryIdAgg);//////////
        //2.3.2 聚合基本属性信息-组合filter聚合
        FilterAggregationBuilder filter = AggregationBuilders.filter("productAttributeIdAgg", QueryBuilders.termQuery("attrValueList.type", "1"));
        filter.subAggregation(AggregationBuilders.terms("productAttributeIdAgg").field("attrValueList.productAttributeId").size(100)
                .subAggregation(AggregationBuilders.terms("productAttributeNameAgg").field("attrValueList.name").size(100)
                        .subAggregation(AggregationBuilders.terms("productAttributeValueAgg").field("attrValueList.value").size(100))));
        //2.3.1 聚合基本属性信息
        NestedAggregationBuilder productAttrIdAgg = AggregationBuilders.nested("productAttrIdAgg", "attrValueList")
                .subAggregation(filter);
        searchSource.aggregation(productAttrIdAgg);//////////
        //3高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags( "<b style='color:red'>").postTags("</b>").field("name");
        searchSource.highlighter(highlightBuilder);
        //4分页
        searchSource.from((param.getPageNum()-1)*param.getPageSize());
        searchSource.size(param.getPageSize());
        //5排序

        return searchSource.toString();
    }
    /*************************************根据前端传的参数创建DSL语句完成**********************************************/

    /*************************************封装和分析查询结果**********************************************/
    private SearchResponse buildSearchResult(SearchResult result) {
        //返回查询结果对象(品牌、分类、属性集合、商品集合、分页信息)
        SearchResponse response = new SearchResponse();
        List<SearchResult.Hit<EsProduct, Void>> hits = result.getHits(EsProduct.class);
        //1添加商品集合
        hits.forEach(hit->{
            EsProduct esProduct = hit.source;
            response.getProducts().add(esProduct);
        });
        //2添加属性集合
        MetricAggregation aggregations = result.getAggregations();
        System.out.println(result);
        //2.1获取品牌
        SearchResponseAttrVo brand = new SearchResponseAttrVo();
        brand.setName("品牌");
        aggregations.getTermsAggregation("brandIdAgg").getBuckets().forEach(b->{
            String brandId = b.getKey();
            brand.setProductAttributeId(Long.parseLong(brandId));
            b.getTermsAggregation("brandName").getBuckets().forEach(bb->{
                String brandName = bb.getKey();
                brand.getValue().add(brandName);
            });
        });
        response.setBrand(brand);
        //2.2获取分类
        SearchResponseAttrVo category = new SearchResponseAttrVo();
        category.setName("分类");
        aggregations.getTermsAggregation("categoryIdAgg").getBuckets().forEach(b->{
            String categoryId = b.getKey();
            category.setProductAttributeId(Long.parseLong(categoryId));
            b.getTermsAggregation("productCategoryNameAgg").getBuckets().forEach(bb->{
                String categoryValue = bb.getKey();
                category.getValue().add(categoryValue);
            });
        });
        response.setCatelog(category);
        //2.4获取属性

        aggregations.getChildrenAggregation("productAttrIdAgg").getChildrenAggregation("productAttributeIdAgg")
                .getTermsAggregation("productAttributeIdAgg").getBuckets().forEach(b->{
            SearchResponseAttrVo attribute = new SearchResponseAttrVo();
            String attributeId = b.getKey();
            attribute.setProductAttributeId(Long.parseLong(attributeId));
            b.getTermsAggregation("productAttributeNameAgg").getBuckets().forEach(bb->{
                String attributeName = bb.getKey();
                attribute.setName(attributeName);
                bb.getTermsAggregation("productAttributeValueAgg").getBuckets().forEach(bbb->{
                    String attributeValue = bbb.getKey();
                    attribute.getValue().add(attributeValue);
                });
            });
            response.getAttrVos().add(attribute);
        });
        return response;
    }
    /*************************************封装和分析查询结果完成**********************************************/

}
