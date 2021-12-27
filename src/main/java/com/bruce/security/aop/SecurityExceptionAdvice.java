package com.bruce.security.aop;

import com.bruce.security.exceptions.SecurityException;
import com.bruce.security.model.common.Error;
import com.bruce.security.model.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 20:13
 * @Author fzh
 */
@Slf4j
@RestControllerAdvice
@Order(0)
public class SecurityExceptionAdvice {

    @ExceptionHandler(value = SecurityException.class)
    public Result<String> handleSecurityException(SecurityException e) {
        return Result.fail(Error.CUSTOM_ERROR.getCode(), e.getMessage());
    }

}
