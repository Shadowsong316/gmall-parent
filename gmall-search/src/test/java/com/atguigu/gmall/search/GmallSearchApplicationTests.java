package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Account;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.AvgAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchApplicationTests {
    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void searchAll() throws IOException {
        System.out.println(jestClient);
        Search search = new Search.Builder("").addIndex("bank").addType("account").build();
        SearchResult searchResult = jestClient.execute(search);
        System.out.println(searchResult.getJsonString());
    }

    @Test
    public void insert() throws IOException {
        //保存一个account
        Account account = new Account(99000L, 21000L, "lei", "feng", 32, "F", "mill road", "tong teacher", "lfy@atguigu.com", "BJ", "CP");
        Index index = new Index.Builder(account).index("bank")
                .type("accont")
                .id(account.getAccount_number() + "")
                .build();
        String s = index.toString();
        System.out.println("Index:String===>" + s);
        DocumentResult result = jestClient.execute(index);
        System.out.println(result.isSucceeded() + "===>" + result.getJsonString());
    }

    @Test
    public void delete() throws IOException {
        Delete delete = new Delete.Builder("99000")
                .index("bank")
                .type("account")
                .build();
        DocumentResult result = jestClient.execute(delete);

        System.out.println(result.isSucceeded() + "===>" + result.getJsonString());
    }

    @Test
    public void searchByDSL() throws IOException {
        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(query);
        System.out.println(sourceBuilder.toString());
        Search search = new Search.Builder(sourceBuilder.toString()).addIndex("bank")
                .addType("account")
                .build();
        SearchResult result = jestClient.execute(search);
        Long total = result.getTotal();
        System.out.println(total);
    }

    /**
     * GET bank/account/_search
     * {
     * "query": {
     * "bool": {
     * "must": [
     * {"match": {"address": "mill"}},
     * {"match": {"gender": "M"}}
     * ],
     * "must_not": [
     * {"match": { "age": "28" }}
     * ],
     * "should": [
     * {"match": {
     * "firstname": "Parker"
     * }}
     * ]
     * }
     * }
     * }
     */
    @Test
    public void searchFZ() throws IOException {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //构建两个must
        boolQuery.must(QueryBuilders.matchQuery("address", "mill"))
                .must(QueryBuilders.matchQuery("gender", "M"));
        //构建一个mustnot
        boolQuery.mustNot(QueryBuilders.matchQuery("age", 28));
        //构建should
        boolQuery.should(QueryBuilders.matchQuery("firstname", "Parker"));
        SearchSourceBuilder query = new SearchSourceBuilder().query(boolQuery);
        Search search = new Search.Builder(query.toString()).addIndex("bank")
                .addType("account")
                .build();
        SearchResult result = jestClient.execute(search);
        System.out.println(result.getTotal() + "==>" + result.getErrorMessage());
    }

	  /*GET bank/account/_search
      {
	    "query": {
	      "terms": {
	        "gender.keyword": [
	          "M",
	          "F"
	        ]
	      }
	    },
	    "aggs": {
	      "age_agg": {
	        "terms": {
	          "field": "age",
	          "size": 100
	        },
	        "aggs": {
	          "gender_agg": {
	            "terms": {
	              "field": "gender.keyword",
	              "size": 100
	            },
	            "aggs": {
	              "balance_avg": {
	                "avg": {
	                  "field": "balance"
	                }
	              }
	            }
	          },
	          "balance_avg":{
	            "avg": {
	              "field": "balance"
	            }
	          }
	        }
	      }
	    }
	    ,
	    "size": 1000
	  }*/

    //所有条件都在这里封装
    @Test
    public void aggs() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder());
        searchSourceBuilder.aggregation(aggregation());
        searchSourceBuilder.size(1000);
        String DSL = searchSourceBuilder.toString();
        System.out.println(DSL);
        Search build = new Search.Builder(DSL).addIndex("bank").addType("account").build();
        SearchResult result = jestClient.execute(build);
        System.out.println(result.getTotal()+"==>"+result.getErrorMessage());
        printResult(result);
    }
    //		打印结果

    private void printResult(SearchResult result){
        MetricAggregation aggregations = result.getAggregations();
        //获取命中的记录
        SearchResult.Hit<Account, Void> hit = result.getFirstHit(Account.class);
        Account source = hit.source;
        System.out.println(source);
        TermsAggregation age_agg = aggregations.getAggregation("age_agg", TermsAggregation.class);
        List<TermsAggregation.Entry> buckets = age_agg.getBuckets();
        buckets.forEach(b->{
            System.out.println("年龄："+b.getKey()+"；总共有："+b.getCount());
            AvgAggregation balance_avg = b.getAvgAggregation("balance_avg");
            System.out.println("平均薪资"+balance_avg.getAvg());
            TermsAggregation gender_agg = b.getAggregation("gender_agg", TermsAggregation.class);
            gender_agg.getBuckets().forEach((b2)->{
                System.out.println("性别："+b2.getKey()+"；有："+b2.getCount()+"人；平均薪资："+b2.getAvgAggregation("balance_avg").getAvg());
        }); });
    }

    private QueryBuilder queryBuilder() {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("gender.keyword", "M", "F");
        return queryBuilder;
    }

    private AggregationBuilder aggregation() {
        TermsAggregationBuilder age_agg = AggregationBuilders.terms("age_agg");

        age_agg.size(100).field("age");
        age_agg.subAggregation(subAggregation());
        age_agg.subAggregation(balanceAvgAgg());
        return age_agg;
    }

    //子聚合
    private AggregationBuilder subAggregation() {
        TermsAggregationBuilder gender_agg = AggregationBuilders.terms("gender_agg");
        gender_agg.field("gender.keyword").size(100);
        //继续有子聚合
        gender_agg.subAggregation(AggregationBuilders.avg("balance_avg").field("balance"));
        return gender_agg;
    }

    //子聚合
    private AggregationBuilder balanceAvgAgg() {
        AvgAggregationBuilder balance_avg = AggregationBuilders.avg("balance_avg").field("balance");
        return balance_avg;
    }
}
