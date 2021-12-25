package com.bruce.security.component;

import com.alibaba.fastjson.JSONObject;
import com.bruce.security.model.po.User;
import com.bruce.security.service.UserService;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

    /**
     * token 有效期
     */
    private final long tokenExpire = 24 * 60 * 60 * 1000;

    /**
     * 密钥
     */
    private final String tokenSecret = "123456";

    public JwtTokenComponent(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String createToken(String username) {
        User user = userService.getByUsername(username);
        String token = Jwts.builder().setSubject(JSONObject.toJSONString(user))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpire))
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    @Override
    public String getFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public void removeToken(String token) {

    }
}
