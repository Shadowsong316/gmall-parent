package com.atguigu.gmall.admin.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class GmallSwagger2Config {

    @Bean("后台用户模块")
    public Docket userApis() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("后台用户模块")
//                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .paths(PathSelectors.regex("/admin.*"))
//                .paths(PathSelectors.regex("/productCategory.*"))
//                .paths(PathSelectors.regex("/productAttribute.*"))
//                .paths(PathSelectors.regex("/memberLevel.*"))
//                .paths(Predicates.and(PathSelectors.regex("/admin.*")))
//                .paths(Predicates.and(PathSelectors.regex("/productAttribute.*")))
//                .paths(Predicates.and(PathSelectors.regex("/memberLevel.*")))
//                .paths(Predicates.and(PathSelectors.regex("/productCategory/.*")))
//                .build()
//                .apiInfo(apiInfo())
//                .enable(true);
//                .groupName("adminApi")
//                .apiInfo(apiInfo())
//                .select()
////                .paths(Predicates.and(PathSelectors.regex("/admin.*")))
////                .paths(Predicates.and(PathSelectors.regex("/productAttribute.*")))
////                .paths(Predicates.and(PathSelectors.regex("/memberLevel.*")))
//                .paths(Predicates.and(PathSelectors.regex("/prefrenceArea.*")))
//                .build();
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.admin"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("谷粒商城-后台管理系统平台接口文档")
                .description("提供pms、oms、ums、cms、sms模块的文档")
                .termsOfServiceUrl("http://www.atguigu.com/")
                .version("1.0")
                .build();
    }


}
