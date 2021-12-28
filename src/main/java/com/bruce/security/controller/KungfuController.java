package com.bruce.security.controller;

import com.bruce.security.model.enums.MethodEnum;
import com.bruce.security.scanner.SecurityResource;
import com.bruce.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KungfuController {

    @Autowired
    private UserService userService;

    private final String PREFIX = "pages/";

    /**
     * level1页面映射
     */
    @GetMapping("/level1/{path}")
    @SecurityResource(name = "level1", code = "level1", parentCode = "kungfu", method = MethodEnum.GET, order = 1)
    public String level1(@PathVariable("path") String path) {
        return PREFIX + "level1/" + path;
    }

    /**
     * level2页面映射
     */
    @GetMapping("/level2/{path}")
    @SecurityResource(name = "level2", code = "level2", parentCode = "kungfu", method = MethodEnum.GET, order = 2)
    public String level2(@PathVariable("path") String path) {
        return PREFIX + "level2/" + path;
    }

    /**
     * level3页面映射
     */
    @GetMapping("/level3/{path}")
    @SecurityResource(name = "level3", code = "level3", parentCode = "kungfu", method = MethodEnum.GET, order = 3)
    public String level3(@PathVariable("path") String path) {
        return PREFIX + "level3/" + path;
    }

    /**
     * level4页面映射
     */
    @GetMapping("/level4/{path}")
    @SecurityResource(name = "level4", code = "level4", parentCode = "kungfu", method = MethodEnum.GET, order = 4)
    public String level4(@PathVariable("path") String path) {
        return PREFIX + "level4/" + path;
    }


}
