package com.bruce.security.scanner;

import com.bruce.security.model.enums.MethodEnum;
import com.bruce.security.model.enums.ResourceTypeEnum;
import com.bruce.security.model.po.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/28 14:56
 * @Author fzh
 */
@Slf4j
@Component
public class SecurityResourceScanner implements BeanPostProcessor, InitializingBean {


    private static List<Resource> menuList = new ArrayList<>();

    private static List<Resource> resourceList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream in = SecurityResourceScanner.class.getClassLoader().getResourceAsStream("menu.yaml");
            if (in == null) {
                log.info("skip scan menu because menu.yaml not exist");
                return;
            }
            Yaml yaml = new Yaml();
            LinkedHashMap<String, Object> map = yaml.load(in);
            processMenu((List) map.get("menu"), StringUtils.EMPTY);
        } catch (Exception e) {
            log.error("scan menu file fail,message={}", e.getMessage(), e);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            Class clazz = bean.getClass();
            //没有@Controller,@RestController
            if (!clazz.isAnnotationPresent(Controller.class) && !clazz.isAnnotationPresent(RestController.class)) {
                return bean;
            }
            for (String rootPath : parseMappingAnnotation(clazz.getAnnotations())) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(SecurityResource.class)) {
                        SecurityResource res = method.getAnnotation(SecurityResource.class);
                        for (String path : parseMappingAnnotation(method.getAnnotations())) {
                            if (StringUtils.isNotEmpty(path)) {
                                String mergePath = rootPath + path;
                                Resource resource = Resource.builder().name(res.name()).code(res.code()).parentCode(res.parentCode())
                                        .url(mergePath).method(res.method().getCode()).icon(StringUtils.EMPTY)
                                        .orderNum(res.order()).type(ResourceTypeEnum.BUTTON.getCode()).build();
                                resourceList.add(resource);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("scan resource fail,message={}", e.getMessage(), e);
        }
        return bean;
    }

    private void processMenu(List<LinkedHashMap<String, Object>> menus, String parentCode) {
        int order = 1;
        if (menus == null) {
            return;
        }
        for (LinkedHashMap<String, Object> menuMap : menus) {
            String name = (String) menuMap.get("name");
            String code = (String) menuMap.get("code");
            String path = (String) menuMap.get("path");
            String icon = menuMap.get("icon") != null ? (String) menuMap.get("icon") : StringUtils.EMPTY;
            Resource resource = Resource.builder().name(name).code(code).parentCode(parentCode).url(path)
                    .method(MethodEnum.GET.getCode()).orderNum(order).icon(icon)
                    .type(ResourceTypeEnum.MENU.getCode()).build();
            menuList.add(resource);
            if (menuMap.containsKey("menu")) {
                processMenu((List) menuMap.get("menu"), code);
            }
            order++;
        }
    }

    private List<String> parseMappingAnnotation(Annotation[] annotationList) {
        for (Annotation annotation : annotationList) {
            if (annotation instanceof RequestMapping) {
                return parseListValue(((RequestMapping) annotation).value());
            } else if (annotation instanceof GetMapping) {
                return parseListValue(((GetMapping) annotation).value());
            } else if (annotation instanceof PostMapping) {
                return parseListValue(((PostMapping) annotation).value());
            } else if (annotation instanceof DeleteMapping) {
                return parseListValue(((DeleteMapping) annotation).value());
            } else if (annotation instanceof PatchMapping) {
                return parseListValue(((PatchMapping) annotation).value());
            } else if (annotation instanceof PutMapping) {
                return parseListValue(((PutMapping) annotation).value());
            }
        }
        return Arrays.asList(StringUtils.EMPTY);
    }


    private List<String> parseListValue(String[]... pathList) {
        Set<String> resultSet = new LinkedHashSet<>();
        for (String[] paths : pathList) {
            for (String path : paths) {
                resultSet.add(path.replaceAll("\\{.*\\}", "*"));
            }
        }
        return new ArrayList<>(resultSet);
    }


    public static List<Resource> getMenuList() {
        return Collections.unmodifiableList(menuList);
    }

    public static List<Resource> getResourceList() {
        return Collections.unmodifiableList(resourceList);
    }


}
