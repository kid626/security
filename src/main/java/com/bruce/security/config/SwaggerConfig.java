package com.bruce.security.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName mybatisplus
 * @Date 2021/2/22 10:49
 * @Author Bruce
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     */
    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo("security", "security 相关接口", "1.0"))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.bruce"))
            .paths(PathSelectors.any())
            .build()
            .groupName("security-api")
            .pathMapping("/")
            .enable(true);
    }

    private ApiInfo apiInfo(String title, String description, String version) {
        return new ApiInfoBuilder()
            .title(title)
            .description(description)
            .contact(new Contact("Bruce", "", ""))
            .version(version)
            .build();
    }
}
