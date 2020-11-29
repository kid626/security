package com.bruce.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        // 定制请求的授权规则
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");

        // 开启登录功能,如果没有权限，则跳到登录页
        http.formLogin().loginPage("/userLogin")
                .usernameParameter("username")
                .passwordParameter("password");
        //1 /login 来到登录页
        //2 重定向到 /login?error 表示登录失败
        //3 更多详情规定
        //4 一旦定制 loginPage, post 请求就是登录

        // 开启自动配置的注销
        http.logout().logoutSuccessUrl("/");
        //1 访问 /logout 表示用户注销，清空 session
        //2 注销成功，会返回 /login?logout 页面

        // 开启记住我功能
        http.rememberMe();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth);
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("zhangsan").password(passwordEncoder().encode("123456")).roles("VIP1", "VIP2")
                .and()
                .withUser("lisi").password(passwordEncoder().encode("123456")).roles("VIP1", "VIP3")
                .and()
                .withUser("wangwu").password(passwordEncoder().encode("123456")).roles("VIP2", "VIP3");
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
