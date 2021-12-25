package com.bruce.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.component.TokenComponent;
import com.bruce.security.exceptions.ServiceExeption;
import com.bruce.security.mapper.UserMapper;
import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.po.Role;
import com.bruce.security.model.po.User;
import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.service.RolePermissionService;
import com.bruce.security.service.UserRoleService;
import com.bruce.security.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public UserAuthentication login(LoginDTO loginDTO) {
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new ServiceExeption("用户名或密码错误!");
        }
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new ServiceExeption("用户名或密码错误!");
        }
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(user, userAuthentication);
        String token = tokenComponent.createToken(loginDTO.getUsername());
        userAuthentication.setToken(token);
        List<Permission> list = getByUserId(user.getId());
        userAuthentication.setAuthorities(list);
        return userAuthentication;
    }

    @Override
    public UserAuthentication login(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String value = tokenComponent.getFromToken(token);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        User user = JSONObject.parseObject(value, User.class);
        // 刷新用户信息
        user = mapper.selectById(user.getId());
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(user, userAuthentication);
        List<Permission> list = getByUserId(user.getId());
        userAuthentication.setToken(token);
        userAuthentication.setAuthorities(list);
        return userAuthentication;
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(User::getUsername, username)
                .eq(User::getEnable, 1);
        return mapper.selectOne(wrapper);
    }

    @Override
    public List<Permission> getByUserId(Long userId) {
        List<Permission> result = new ArrayList<>();
        List<Role> roleList = userRoleService.getByUserId(userId);
        for (Role role : roleList) {
            List<Permission> permissionList = rolePermissionService.getByRoleId(role.getId());
            result.addAll(permissionList);
        }
        return result;
    }
}
