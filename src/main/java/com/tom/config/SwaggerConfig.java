package com.tom.config;

/*
*
* Swagger的配置类
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2 // 手动开启Swagger
public class SwaggerConfig {

    /*
    *
    * 因为只有我一个人的开发，所以只需要一个Docket就可以了，也不需要创建多个实例对象
    * Swagger还可以配置各种开发环境下的显示情况，但是基于自己一个人开发，这里也就不写了
     */

    // 配置Swagger的Docket的Bean的实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Tom") // 设置实例的名称
                .enable(true) // 开启Swagger
                .apiInfo(apiInfo()) // 替换掉人家默认的信息
                .select()
                .apis(RequestHandlerSelectors.any()) // 扫描所有的
                .build();

    }


    // 配置Swagger页面显示的所有的文档信息---相当于替换成自己的（如果不配置的话）
    private ApiInfo apiInfo() {

        // 文档的作者信息
        Contact contact = new Contact("tom", "http://101.200.78.148:8888", "1755619015@qq.com");

        return new ApiInfo(
                "Tom的SwagerAPI文档", // 文档标题
                "这是这个文档的描述", // 文档描述
                "v1.0", // 版本
                "101.200.78.148:8888", // 我的Team
                contact, // 这里放入作者的信息
                "Apache 2.0",
                "http:www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }


















}
