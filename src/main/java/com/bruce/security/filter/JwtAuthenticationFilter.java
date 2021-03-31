package com.bruce.security.filter;

import com.bruce.security.config.JwtToken;
import com.bruce.security.config.TokenAuthentication;
import com.bruce.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
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
}
