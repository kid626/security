package com.bruce.security.util;

import java.util.UUID;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 14:24
 * @Author fzh
 */
public class RandomUtil {

    private static final String CODE_GENERATOR = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    /**
     * 随机生成 n 位
     *
     * @param n 随机数长度
     * @return 随机数
     */
    public static String randomStr(int n) {
        StringBuilder sb = new StringBuilder();
        int len = CODE_GENERATOR.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            sb.append(CODE_GENERATOR.charAt((int) r));
        }
        return sb.toString();
    }

    /**
     * 随机 UUID
     *
     * @return uuid
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
