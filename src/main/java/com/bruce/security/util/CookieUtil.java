package com.bruce.security.util;

import javax.servlet.http.Cookie;
import java.time.Duration;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/26 11:35
 * @Author fzh
 */
public class CookieUtil {

    public static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge((int) Duration.ofDays(30).getSeconds());
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        return cookie;
    }

}
