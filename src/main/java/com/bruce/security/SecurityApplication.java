package com.bruce.security;

import com.bruce.security.controller.CustomErrorController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/8/13 11:25
 * @Author fzh
 */
@SpringBootApplication
@EnableWebSecurity
@MapperScan("com.bruce.security.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableScheduling
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    public CustomErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                                      ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        return new CustomErrorController(errorAttributes, serverProperties.getError(),
                errorViewResolversProvider.getIfAvailable());
    }

}
