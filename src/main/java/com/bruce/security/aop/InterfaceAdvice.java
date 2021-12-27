package com.bruce.security.aop;

import com.bruce.security.exceptions.SecurityException;
import com.bruce.security.model.common.Error;
import com.bruce.security.model.common.Result;
import com.bruce.security.model.constant.CommonConstant;
import com.bruce.security.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 14:21
 * @Author fzh
 */
@Aspect
@Component
@Slf4j
public class InterfaceAdvice {

    @Around("execution(public * com.bruce.security.controller..*(..))")
    public Object ajaxResultProcess(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String traceId = RandomUtil.randomUUID();
        MDC.put(CommonConstant.TRACE_ID, traceId);

        Object obj;
        String fun = fetchFun(pjp);
        try {
            log.info("[op:trace],fun={},args={}", fun, pjp.getArgs());
            obj = pjp.proceed();
            log.info("[op:trace],fun={},result=success,elapse={}ms", fun, stopWatch.getTime());
            return obj;
        } catch (SecurityException e) {
            log.info("[op:trace],fun={},result=fail,elapse={}ms,err={}", fun, stopWatch.getTime(), e.getMessage());
            return Result.fail(Error.CUSTOM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[op:trace],fun={},result=fail,elapse={}ms", fun, stopWatch.getTime(), e);
            return Result.fail(Error.SYS_ERROR);
        } finally {
            stopWatch.stop();
            MDC.remove(CommonConstant.TRACE_ID);
            MDC.clear();
        }
    }

    private String fetchFun(ProceedingJoinPoint pjp) {
        return "["
                + pjp.getSignature().getDeclaringType().getSimpleName()
                + "."
                + pjp.getSignature().getName()
                + "]";
    }
}
