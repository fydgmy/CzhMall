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
1).给bean添加校验注解:javax.validation.constraints 并定义自己的message提示
2).开启校验功能@Valid
效果：校验错误后会有默认的响应
3).给校验的bean后紧跟一个bingdingresult。就可以获取到校验结果
4).分组校验（多场景的复杂校验）
    1)@Null(message = "新增不能指定Id",groups ={UpdateGroup.class} )
    给校验注解标注什么情况下需要进行校验
    2）@Validated({AddGroup.class})
    3)默认没有指定分组的校验注解@NotBlank，在分组校验情况下不生效，只会在@Validated生效
5）自定义校验
    第一种方式：正则表达式
    第二种：自定义校验
    1）编写一个自定义的校验注解
    2）编写一个自定义的校验器 ConstraintValidator
    3）关联自定义的校验器和自定义的校验注解
    @Documented
@Constraint(
        validatedBy = {ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)

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
