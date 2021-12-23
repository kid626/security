package com.bruce.security.filter;

import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.service.UserService;
import com.bruce.security.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        UserAuthentication authentication = userService.login(token);
        if (authentication != null) {
            UserUtil.setCurrentUser(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * 可以重写
     *
     * @param request HttpServletRequest
     * @return 返回为true时，则不过滤即不会执行doFilterInternal
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 初始化忽略的url不走过此滤器
        List<RequestMatcher> matchers = Arrays.stream(AUTH_WHITELIST)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        OrRequestMatcher orRequestMatcher = new OrRequestMatcher(matchers);
        return orRequestMatcher.matches(request);
    }


}
