package com.bruce.security.controller;

import com.bruce.security.exceptions.CustomException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/8/13 11:25
 * @Author fzh
 */
public class CustomErrorController extends BasicErrorController {

    public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(errorAttributes, status);
    }

    @Override
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(getErrorAttributes(request));
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        // 获取异常参数
        ErrorAttributeOptions options = ErrorAttributeOptions.of(
                // 异常 message
                ErrorAttributeOptions.Include.MESSAGE,
                // 异常类型
                ErrorAttributeOptions.Include.EXCEPTION,
                // 异常堆栈，比较长
                // ErrorAttributeOptions.Include.STACK_TRACE,
                // 绑定的错误 error
                ErrorAttributeOptions.Include.BINDING_ERRORS
        );
        return getErrorAttributes(request, options);
    }

    public static void runtimeExTest() throws CustomException {
        throw new CustomException("test");
    }

    public static void checkExTest() throws IOException {
        throw new IOException("test");
    }

    public static void errorTest() throws Error {
        throw new Error("test");
    }

    public static void main(String[] args) {
        runtimeExTest();
        try {
            checkExTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        errorTest();
    }

}
