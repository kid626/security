package com.bruce.security.service;

import com.alibaba.fastjson.JSONObject;
import com.bruce.security.config.JwtToken;
import com.bruce.security.model.LoginResp;
import com.bruce.security.util.JwtUtil;
import java.util.HashSet;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenIssueService {


    public LoginResp issue(String username) {
        long now = System.currentTimeMillis() / 1000;
        HashSet<String> set = new HashSet<>();
        set.add(username);
        JwtToken access = new JwtToken();
        access.setJti(UUID.randomUUID().toString());
        access.setExp(now + 24 * 3600L);
        access.setRoles(set);
        access.setUid(username);
        access.setUserName(username);
        JwtToken refresh = new JwtToken();
        refresh.setJti(UUID.randomUUID().toString());
        refresh.setAti(access.getJti());
        refresh.setExp(now + 30 * 24 * 3600);
        refresh.setUserName(username);
        refresh.setUid(username);
        refresh.setRoles(set);
        LoginResp loginRsp = new LoginResp();
        loginRsp.setAccessToken(encode(access));
        loginRsp.setRefreshToken(encode(refresh));
        loginRsp.setExpire(24 * 3600L);
        loginRsp.setUid(username);
        return loginRsp;
    }

    private String encode(JwtToken token) {
        return JwtUtil.encode(JSONObject.toJSONString(token), token.getExp());
    }
}
