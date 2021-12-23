package com.bruce.security.controller;

import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class KungfuController {

    @Autowired
    private UserService userService;

    private final String PREFIX = "pages/";

    /**
     * 欢迎页
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "welcome";
    }

    @PostMapping("/login")
    public UserAuthentication login(@RequestBody @Validated LoginDTO dto) {
        return userService.login(dto);
    }


    /**
     * level1页面映射
     *
     * @param path
     * @return
     */
    @GetMapping("/level1/{path}")
    public String level1(@PathVariable("path") String path) {
        return PREFIX + "level1/" + path;
    }

    /**
     * level2页面映射
     *
     * @param path
     * @return
     */
    @GetMapping("/level2/{path}")
    public String level2(@PathVariable("path") String path) {
        return PREFIX + "level2/" + path;
    }

    /**
     * level3页面映射
     *
     * @param path
     * @return
     */
    @GetMapping("/level3/{path}")
    public String level3(@PathVariable("path") String path) {
        return PREFIX + "level3/" + path;
    }

    /**
     * level4页面映射
     *
     * @param path
     * @return
     */
    @GetMapping("/level4/{path}")
    @Secured("user")
    public String level4(@PathVariable("path") String path) {
        return PREFIX + "level4/" + path;
    }


}
