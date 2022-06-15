package com.bruce.security.filter;

import com.bruce.security.model.po.Resource;
import com.bruce.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 14:24
 * @Author fzh
 */
@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ResourceService resourceService;

    /**
     * 每一个资源所需要的权限 Collection<ConfigAttribute>决策器会用到
     */
    private final static HashMap<String, Collection<ConfigAttribute>> RESOURCE_MAP = new HashMap<>(2);

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (String url : RESOURCE_MAP.keySet()) {
            if (new AntPathRequestMatcher(url).matches(request)) {
                return RESOURCE_MAP.get(url);
            }
        }
        return Arrays.asList(new SecurityConfig("default"));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // 初始化 所有资源 对应的角色
        refreshResources();
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }


    /**
     * 刷新所有资源
     */
    public void refreshResources() {
        // map 清空
        RESOURCE_MAP.clear();
        // 权限资源
        List<Resource> resourceList = resourceService.getAvailableResources();
        //某个资源 可以被哪些角色访问
        for (Resource re : resourceList) {
            String url = re.getUrl();
            String code = re.getCode();
            ConfigAttribute role = new SecurityConfig(code);
            Collection<ConfigAttribute> list = RESOURCE_MAP.getOrDefault(url, new ArrayList<>());
            list.add(role);
            RESOURCE_MAP.putIfAbsent(url, list);
        }
    }

}
