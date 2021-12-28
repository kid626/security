package com.bruce.security.scanner;

import com.bruce.security.model.enums.MethodEnum;

import java.lang.annotation.*;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/28 14:52
 * @Author fzh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SecurityResource {

    String name();

    String code();

    String parentCode();

    MethodEnum method() default MethodEnum.GET;

    int order() default 1;

}
