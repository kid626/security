package com.bruce.security.controller;

import com.bruce.security.component.TokenComponent;
import com.bruce.security.config.SecurityProperty;
import com.bruce.security.model.common.Result;
import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.model.vo.PermissionVO;
import com.bruce.security.service.PermissionService;
import com.bruce.security.service.UserService;
import com.bruce.security.util.CookieUtil;
import com.bruce.security.util.UserUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/26 11:33
 * @Author fzh
 */
@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SecurityProperty property;
    @Autowired
    private TokenComponent tokenComponent;


    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public Result<UserAuthentication> login(@RequestBody LoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        UserAuthentication user = userService.login(dto);
        //cookie
        response.addCookie(CookieUtil.createCookie(property.getToken().getName(), user.getToken()));
        return Result.success(user);
    }

    @ApiOperation("单点登录跳转")
    @GetMapping(value = "/redirect")
    public void redirect(String homePage, HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = tokenComponent.getToken(request);
            response.addCookie(CookieUtil.createCookie(property.getToken().getName(), token));
            response.sendRedirect(homePage);
        } catch (IOException e) {
            log.error("重定向失败:{}", e.getMessage(), e);
        }
    }

    @ApiOperation("全局登出")
    @GetMapping(value = "/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success();
    }

    @ApiOperation("获取登录用户信息")
    @GetMapping(value = {"/info", "user/info"})
    public Result<UserAuthentication> info() {
        UserAuthentication user = UserUtil.getCurrentUser();
        return Result.success(user);
    }

    @ApiOperation("用户资源权限")
    @GetMapping(value = "/perm/list")
    public Result<List<String>> permList() {
        List<String> list = userService.permList();
        return Result.success(list);
    }

    @ApiOperation("获取登录密钥")
    @GetMapping(value = "/secretKey")
    public Result<String> loginSecretKey(String username) {
        String secretKey = userService.getLoginSecretKey(username);
        return Result.success(secretKey);
    }

    @ApiOperation("获取菜单树")
    @GetMapping(value = "/menuTree")
    public Result<PermissionVO> menuTree() {
        PermissionVO result = permissionService.tree();
        return Result.success(result);
    }

    @GetMapping(value = "/showResScript", produces = "text/html;charset=utf-8")
    public String showResScript(@RequestParam(defaultValue = "false") boolean autoIncrement) {
        // if (AuthContextUtil.isActiveProfile("prod")) {
        //     return StringUtils.EMPTY;
        // }
        // StringBuilder sb = new StringBuilder();
        // sb.append("# 菜单初始化脚本</br>");
        // List<Resource> menuList = AuthResourceScanner.getMenuList();
        // for (Resource res : menuList) {
        //     sb.append(AuthResourceUtil.genDDL(res, autoIncrement) + "</br>");
        // }
        // sb.append("# 操作初始化脚本</br>");
        // List<Resource> resourceList = AuthResourceScanner.getResourceList();
        // for (Resource res : resourceList) {
        //     sb.append(AuthResourceUtil.genDDL(res, autoIncrement) + "</br>");
        // }
        // return sb.toString();
        return StringUtils.EMPTY;
    }

    @ApiOperation("获取图形验证码")
    @GetMapping(value = "/verifyCode")
    public Result<String> verifyCode(@RequestParam String key) {
        // String verifyCode = userService.verifyCode(key);
        return Result.success();
    }


}
