package com.bruce.security.exceptions;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/23 9:42
 * @Author fzh
 */
public class ServiceExeption extends RuntimeException {

    public ServiceExeption() {
        super();
    }

    public ServiceExeption(String message) {
        super(message);
    }

    public ServiceExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceExeption(Throwable cause) {
        super(cause);
    }
}
