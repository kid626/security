package com.bruce.security.handler;

import com.bruce.security.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/8/13 11:24
 * @Author fzh
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 捕捉 CustomException ，返回 response 的 status 设置为 HttpStatus.BAD_REQUEST
     *
     * @param e CustomException
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCustomException(CustomException e) {
        return e.getMessage();
    }

}
