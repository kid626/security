package com.bruce.security.util;

import com.bruce.security.model.security.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/23 19:00
 * @Author fzh
 */
public class UserUtil {


    public static UserAuthentication getCurrentUser() {
        return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


    public static void setCurrentUser(UserAuthentication user) {
        SecurityContextHolder.getContext().setAuthentication(user);
    }

    public static void clearCurrentUser(){
        SecurityContextHolder.clearContext();
    }


}
