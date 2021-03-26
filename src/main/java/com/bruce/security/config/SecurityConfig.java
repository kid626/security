package com.bruce.security.config;

import com.bruce.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Copyright Copyright © 2020 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2020/11/29 15:25
 * @Author Bruce
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/userLogin",
            "/level2/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .antMatchers("/**")
                .authenticated()
                .and()
                .addFilter(createJWTAuthenticationFilter());

    }


    @Bean
    public JwtAuthenticationFilter createJWTAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
