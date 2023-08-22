package com.example.gulimall.search;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.example.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class ApplicationTests {
    @Autowired
    private RestHighLevelClient client;
    /**
     * Copyright 2023 bejson.com
     */

    /**
     * Auto-generated: 2023-08-19 18:25:56
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    @ToString
    @Data
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    /**
     * 优势：1）方便检索
     *
     * {
     *     skuId:1
     *     spuId:11
     *     skuTitle:华为xx
     *     price:998
     *     saleCount:99
     *     attrs:[
     *     {尺寸：5寸}
     *     {cpu：845}
     *     {分辨率：全高清}
     *     ]
     * }
     * 冗余字段：
     * 假设有100万商品有20个属性=1000000*2kb=2000mb=2gb
     * 2）
     * sku索引{
     *     skuId:1
     *     spuId:11
     *     xxxxx
     * }
     * attr索引{
     *     spuId：11
     *     attrs： attrs:[
     *       {尺寸：5寸}
     *       {cpu：845}
     *       {分辨率：全高清}
     *       ]
     * }
     *
     * @throws IOException
     */
    @Test
    public void searchData() throws IOException {
        //1.创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定DSL，检索条件
        //1.1构造检索条件
//		searchSourceBuilder.query();
//		searchSourceBuilder.from();
//		searchSourceBuilder.size();
//		searchSourceBuilder.aggregation();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        System.out.println(searchSourceBuilder.toString());
        //1.2按照年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(ageAgg);
        //1.3计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);
        System.out.println("检索条件" + searchSourceBuilder.toString());

        searchRequest.source(searchSourceBuilder);
        //2.执行检索
        SearchResponse search = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //3.分析结果
        System.out.println(search.toString());
        //Map map = JSON.parseObject(search.toString(), Map.class);
        //3.1获取所有查到的数据
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
//			hit.getIndex();
//			hit.getType();
//			hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account:" + account);
        }
        //3.2获取这次检索到的分析信息
        Aggregations aggregations = search.getAggregations();
//		for (Aggregation aggregation : aggregations.asList()) {
//			System.out.println("当前聚合："+aggregation.getName());
//			//aggregation.get
//			Terms ageAgg1 = aggregations.get("ageAgg");
//			ageAgg1.getBuckets();
//		}
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄：" + keyAsString + "==>" + bucket.getDocCount());
        }
        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资：" + balanceAvg1.getValue());
    }

    /**
     * 测试存储数据到es
     * 网络操作都会有异常，需要抛出来
     * 更新也可以
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");//数据的id
        //indexRequest.source("userName","zhangsan","age","18","gender","男");
        User user = new User();
        user.setUserName("zhangsan");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);//要保存的内容
        //执行操作
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println(index);
    }

    @Data
    class User {
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    public void contextLoads() {
        System.out.println("-------------------------");
        System.out.println(client);
        System.out.println("-------------------------");
    }

}
