package com.bruce.security.filter;

import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.service.UserService;
import com.bruce.security.util.UserUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Copyright Copyright © 2020 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2020/12/25 9:31
 * @Author Bruce
 */
public class AuthenticationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        UserAuthentication authentication = userService.login(token);
        if (authentication != null) {
            UserUtil.setCurrentUser(authentication);
        }
        chain.doFilter(request, response);
    }

}
