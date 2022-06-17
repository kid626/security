package com.bruce.security.filter;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 14:56
 * @Author fzh
 */
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private CustomSecurityMetadataSource securityMetadataSource;
    private CustomAccessDecisionManager myAccessDecisionManager;

    public CustomFilterSecurityInterceptor(CustomSecurityMetadataSource securityMetadataSource, CustomAccessDecisionManager myAccessDecisionManager) {
        this.securityMetadataSource = securityMetadataSource;
        this.myAccessDecisionManager = myAccessDecisionManager;
        // 初始化
        securityMetadataSource.refreshResources();
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }


    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

}
