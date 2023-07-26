package com.example.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
1.逻辑删除
mybatis-plus官网：https://baomidou.com/pages/6b03c5/
1)配置全局的逻辑删除规则（省略）
2）配置逻辑删除的组件Bean（省略）
3）给Bean加上逻辑删除注解@TableLogic
 */  //
/*
JSR303校验
1.给bean添加校验注解:javax.validation.constraints 并定义自己的message提示
2.开启校验功能@Valid
效果：校验错误后会有默认的响应
3.给校验的bean后紧跟一个bingdingresult。就可以获取到校验结果
4.统一的异常处理
@ControllerAdvice



 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.gulimall.product.dao")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
