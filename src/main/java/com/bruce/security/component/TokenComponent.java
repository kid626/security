package com.bruce.security.component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 9:36
 * @Author fzh
 */
public interface TokenComponent {

    /**
     * 根据用户名创建 token
     *
     * @param username 用户名
     * @return token
     */
    String createToken(String username);

    /**
     * 根据token字符串得到用户信息
     *
     * @param token token
     */
    String getFromToken(String token);

    /**
     * 删除 token
     *
     * @param token token
     */
    void removeToken(String token);

    /**
     * 获取 token
     *
     * @param request http 请求
     * @return  token
     */
    String getToken(HttpServletRequest request);

}
