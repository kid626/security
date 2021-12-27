package com.bruce.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.bruce.security.model.common.Error;
import com.bruce.security.model.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/23 20:59
 * @Author fzh
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.getWriter().write(JSONObject.toJSONString(Result.fail(Error.NO_LOGIN.getCode(), authException.getMessage())));
    }
}
