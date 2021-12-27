package com.bruce.security.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 14:29
 * @Author fzh
 */
@Data
@AllArgsConstructor
public class Error {

    private int code;
    private String message;

    /**
     * 公共异常
     */
    public static Error SYS_ERROR = new Error(500, "系统异常");
    public static Error CUSTOM_ERROR = new Error(400, "自定义错误");
    public static Error NO_AUTH = new Error(401, "无权限访问");
    public static Error NO_LOGIN = new Error(430, "未登录");
}
