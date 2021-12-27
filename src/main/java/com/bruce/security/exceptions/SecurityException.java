package com.bruce.security.exceptions;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/23 9:42
 * @Author fzh
 */
public class SecurityException extends RuntimeException {

    public SecurityException() {
        super();
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}
