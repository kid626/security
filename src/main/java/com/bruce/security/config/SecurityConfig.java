package com.bruce.security.config;

import com.bruce.security.component.JwtTokenComponent;
import com.bruce.security.component.RedisTokenComponent;
import com.bruce.security.component.RedissonComponent;
import com.bruce.security.component.TokenComponent;
import com.bruce.security.filter.AuthenticationFilter;
import com.bruce.security.filter.CustomSecurityMetadataSource;
import com.bruce.security.handler.CustomAccessDeniedHandler;
import com.bruce.security.handler.CustomAuthenticationEntryPoint;
import com.bruce.security.model.enums.TokenType;
import com.bruce.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @Copyright Copyright © 2020 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2020/11/29 15:25
 * @Author Bruce
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperty property;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomSecurityMetadataSource customSecurityMetadataSource;
    @Autowired
    private RedissonComponent redissonComponent;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), userService, tokenComponent(userService, redissonComponent)))
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] defaultExcludeUrls = property.getDefaultExcludeUrls();
        WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer = web.ignoring().antMatchers(defaultExcludeUrls);
        if (!isActiveProfile("prod")) {
            ignoredRequestConfigurer.antMatchers(property.getSwaggerUrls());
        }
        if (property.getExcludeUrls() != null) {
            ignoredRequestConfigurer.antMatchers(property.getExcludeUrls());
        }
    }

    @Bean
    public TokenComponent tokenComponent(UserService userService, RedissonComponent redissonComponent) {
        TokenType tokenType = property.getToken().getType();
        if (tokenType == null) {
            throw new IllegalArgumentException("tokenType 不能为空!");
        }
        if (tokenType == TokenType.JWT) {
            return new JwtTokenComponent(userService, property.getToken());
        }
        if (tokenType == TokenType.REDIS) {
            return new RedisTokenComponent(userService, redissonComponent, property.getToken());
        }
        throw new IllegalArgumentException("tokenType 不支持!");
    }

    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 5 * 60 * 1000)
    public void refreshPermission() {
        customSecurityMetadataSource.refreshPermission();
    }

    private boolean isActiveProfile(String profile) {
        for (String activeProfile : property.getActiveProfiles()) {
            if (activeProfile.equals(profile)) {
                return true;
            }
        }
        return false;
    }
}
