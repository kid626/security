package com.bruce.security.model.enums;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/28 14:53
 * @Author fzh
 */
public enum MethodEnum {

    /**
     * HTTP 方法枚举类
     */
    ALL(0),
    POST(1),
    DELETE(2),
    PUT(3),
    GET(4);

    private int code;

    private MethodEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
