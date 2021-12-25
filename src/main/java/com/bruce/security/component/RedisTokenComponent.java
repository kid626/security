package com.bruce.security.component;

import com.alibaba.fastjson.JSONObject;
import com.bruce.security.model.po.User;
import com.bruce.security.service.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 9:37
 * @Author fzh
 */
public class RedisTokenComponent implements TokenComponent {

    public RedisTokenComponent(UserService userService, RedissonComponent redissonComponent) {
        this.userService = userService;
        this.redissonComponent = redissonComponent;
    }

    private final UserService userService;

    private final RedissonComponent redissonComponent;

    /**
     * 有效时间
     */
    private final long tokenExpire = 24 * 60 * 60 * 1000;

    /**
     * token 名称
     */
    private final String tokenName = "securitytoken";

    @Override
    public String createToken(String username) {
        String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        User user = userService.getByUsername(username);
        redissonComponent.getRBucket(token).set(JSONObject.toJSONString(user), tokenExpire, TimeUnit.MILLISECONDS);
        return token;
    }

    @Override
    public String getFromToken(String token) {
        return redissonComponent.getRBucket(token).get();
    }

    @Override
    public void removeToken(String token) {
        redissonComponent.getRBucket(token).delete();
    }

    @Override
    public String getToken(HttpServletRequest request) {
        //step1:尝试从url参数获取
        String token = request.getParameter(tokenName);
        //step2:尝试从header获取
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(tokenName);
        }
        //step3:从cookie获取
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
