package com.bruce.security.service;

import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.User;
import com.bruce.security.model.security.UserAuthentication;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface UserService {

    /**
     * 登录校验
     *
     * @param loginDTO LoginDTO
     * @return UserAuthentication
     */
    UserAuthentication login(LoginDTO loginDTO);

    /**
     * token登录校验
     *
     * @param token token
     * @return UserAuthentication
     */
    UserAuthentication login(String token);

    /**
     * 登出
     */
    void logout();

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return 用户
     */
    User getByUsername(String username);


    /**
     * 根据用户主键获取所有资源
     *
     * @param userId 用户主键
     * @return 所有权限资源
     */
    List<Resource> getByUserId(Long userId);

    /**
     * 获取资源列表
     *
     * @return 当前用户资源列表
     */
    List<String> permList();

    /**
     * 新增
     *
     * @param user User
     * @return 主键
     */
    long save(User user);

}
