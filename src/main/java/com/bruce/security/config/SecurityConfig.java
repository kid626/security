package com.bruce.security.config;

import com.bruce.security.component.RedisTokenComponent;
import com.bruce.security.component.RedissonComponent;
import com.bruce.security.component.TokenComponent;
import com.bruce.security.filter.AuthenticationFilter;
import com.bruce.security.filter.CustomSecurityMetadataSource;
import com.bruce.security.handler.CustomAccessDeniedHandler;
import com.bruce.security.handler.CustomAuthenticationEntryPoint;
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

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/error",
            "/login",
            "/doc.html",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Autowired
    private UserService userService;
    @Autowired
    private CustomSecurityMetadataSource customSecurityMetadataSource;

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
                .addFilter(new AuthenticationFilter(authenticationManager(), userService))
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Bean
    public TokenComponent tokenComponent(UserService userService, RedissonComponent redissonComponent) {
        return new RedisTokenComponent(userService, redissonComponent);
        // return new JwtTokenComponent(userService);
    }

    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 5 * 60 * 1000)
    public void refreshPermission() {
        customSecurityMetadataSource.refreshPermission();
    }
}
