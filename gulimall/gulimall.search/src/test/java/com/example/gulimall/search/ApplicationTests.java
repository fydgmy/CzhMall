package com.example.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.example.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
public class ApplicationTests {
	@Autowired
	private RestHighLevelClient client;
	/**测试存储数据到es
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
	class User{
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
