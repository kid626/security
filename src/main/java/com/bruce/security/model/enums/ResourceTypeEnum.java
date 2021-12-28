package com.bruce.security.model.enums;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/28 14:53
 * @Author fzh
 */
public enum ResourceTypeEnum {

    /**
     * 资源类型枚举类 菜单 和 按钮
     */
    MENU(0),
    BUTTON(1);

    private int code;

    private ResourceTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
