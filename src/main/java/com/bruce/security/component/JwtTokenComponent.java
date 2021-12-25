package com.bruce.security.component;

import com.alibaba.fastjson.JSONObject;
import com.bruce.security.config.SecurityProperty;
import com.bruce.security.model.po.User;
import com.bruce.security.service.UserService;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 10:11
 * @Author fzh
 */
public class JwtTokenComponent implements TokenComponent {

    private final UserService userService;

    private final SecurityProperty.TokenManager tokenManager;

    public JwtTokenComponent(UserService userService, SecurityProperty.TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
    }

    @Override
    public String createToken(String username) {
        User user = userService.getByUsername(username);
        String token = Jwts.builder().setSubject(JSONObject.toJSONString(user))
                .setExpiration(new Date(System.currentTimeMillis() + tokenManager.getExpire()))
                .signWith(SignatureAlgorithm.HS512, tokenManager.getSecret()).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    @Override
    public String getFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenManager.getSecret()).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public void removeToken(String token) {

    }

    @Override
    public String getToken(HttpServletRequest request) {
        String tokenName = tokenManager.getName();
        //step1:尝试从header获取
        String token = request.getHeader(tokenName);
        //step2:从cookie获取
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(tokenName)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }
}
