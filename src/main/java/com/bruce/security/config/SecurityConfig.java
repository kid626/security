package com.bruce.security.config;

import com.bruce.security.component.RedisTokenComponent;
import com.bruce.security.component.RedissonComponent;
import com.bruce.security.component.TokenComponent;
import com.bruce.security.filter.AuthenticationFilter;
import com.bruce.security.handler.UnAuthorizedEntryPoint;
import com.bruce.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            "/level2/**",
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(new UnAuthorizedEntryPoint())
                .and()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // TODO 改成动态配置的形式
                .antMatchers("/level1/**").hasAuthority("admin")
                .antMatchers("/level3/**").hasAnyAuthority("admin", "user")
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

}
