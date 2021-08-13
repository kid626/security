package com.bruce.security.filter;

import com.bruce.security.config.JwtToken;
import com.bruce.security.config.TokenAuthentication;
import com.bruce.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    private OrRequestMatcher orRequestMatcher;

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

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        // 初始化忽略的url不走过此滤器
        List<RequestMatcher> matchers = Arrays.stream(AUTH_WHITELIST)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        orRequestMatcher = new OrRequestMatcher(matchers);

    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter:" + request.getServletPath());
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            chain.doFilter(request, response);
            return;
        }
        String token = extractJwtToken(authorization);
        if (token != null) {
            String decode = JwtUtil.decode(token);
            ObjectMapper mapper = new ObjectMapper();
            JwtToken jwtToken = mapper.readValue(decode, JwtToken.class);
            TokenAuthentication t = TokenAuthentication.of(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(t);
        }
        chain.doFilter(request, response);
    }

    private String extractJwtToken(String value) {
        if ((value.toLowerCase().startsWith("bearer"))) {
            String authHeaderValue = value.substring("bearer".length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }

    private Map<String, String> extractAccessMap(String value) throws UnsupportedEncodingException {
        if (!value.startsWith("accessKey=")) {
            return null;
        }
        String[] params = value.split("&");
        Map<String, String> paramsMap = new HashMap<>();
        for (String param : params) {
            String[] split = param.split("=");
            paramsMap.put(split[0], URLDecoder.decode(split[1], "UTF-8"));
        }
        if (paramsMap.containsKey("path")
                && paramsMap.containsKey("method")
                && paramsMap.containsKey("accessKey")
                && paramsMap.containsKey("sign")
                && paramsMap.containsKey("timestamp")
                && checkTimestamp(paramsMap.get("timestamp"))) {
            return paramsMap;
        }
        return null;
    }

    private boolean checkTimestamp(String timestamp) {
        Date date = new Date(Long.valueOf(timestamp));
        Date max = new Date(date.getTime() + 300000L);
        Date min = new Date(date.getTime() - 300000L);
        Date now = new Date();
        return (now.compareTo(max) <= 0 && now.compareTo(min) >= 0);
    }

    /**
     * 可以重写
     *
     * @param request
     * @return 返回为true时，则不过滤即不会执行doFilterInternal
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return orRequestMatcher.matches(request);
    }


}
