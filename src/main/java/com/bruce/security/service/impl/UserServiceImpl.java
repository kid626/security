package com.bruce.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.component.RedissonComponent;
import com.bruce.security.exceptions.ServiceExeption;
import com.bruce.security.mapper.UserMapper;
import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.po.Role;
import com.bruce.security.model.po.User;
import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.model.security.UserModel;
import com.bruce.security.service.RolePermissionService;
import com.bruce.security.service.UserRoleService;
import com.bruce.security.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private RedissonComponent redissonComponent;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        User user = mapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        return new UserModel(username, passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public UserAuthentication login(LoginDTO loginDTO) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(User::getUsername, loginDTO.getUsername())
                .eq(User::getEnable, 1);
        User user = mapper.selectOne(wrapper);
        if (user == null) {
            throw new ServiceExeption("用户名或密码错误!");
        }
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new ServiceExeption("用户名或密码错误!");
        }
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(user, userAuthentication);
        String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        userAuthentication.setToken(token);
        List<Permission> list = getByUserId(user.getId());
        userAuthentication.setAuthorities(list);
        redissonComponent.getRBucket(token).set(JSONObject.toJSONString(userAuthentication), 24, TimeUnit.HOURS);
        return userAuthentication;
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
