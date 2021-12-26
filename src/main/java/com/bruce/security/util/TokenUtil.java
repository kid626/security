package com.bruce.security.util;

import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/26 16:22
 * @Author fzh
 */
public class TokenUtil {


    private final static char A_LOWER = 'a';
    private final static char Z_LOWER = 'z';
    private final static char A_UPPER = 'A';
    private final static char Z_UPPER = 'Z';
    private final static char ZERO = '0';
    private final static char NINE = '9';

    /**
     * 获取token
     *
     * @param username key
     * @param length   长度
     * @return token
     */
    public static String getToken(String username, int length) {
        StringBuilder originString = new StringBuilder();
        originString.append(username);
        originString.append(new Date());
        char[] alpha = new char[62];
        for (int i = A_LOWER; i <= Z_LOWER; i++) {
            alpha[i - (int) A_LOWER] = (char) i;
        }

        for (int i = A_UPPER; i <= Z_UPPER; i++) {
            alpha[i - (int) A_UPPER + 26] = (char) i;
        }

        for (int i = ZERO; i <= NINE; i++) {
            alpha[i - (int) ZERO + 52] = (char) i;
        }

        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            originString.append(alpha[random.nextInt(61)]);
        }

        String token;
        token = Sha2Crypt.sha512Crypt(originString.toString().getBytes(StandardCharsets.UTF_8));
        token = Base64.encodeBase64URLSafeString(token.getBytes(StandardCharsets.UTF_8));
        return token.substring(0, length);
    }

}
