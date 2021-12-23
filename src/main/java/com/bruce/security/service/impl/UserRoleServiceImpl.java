package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.UserRoleMapper;
import com.bruce.security.model.po.Role;
import com.bruce.security.model.po.UserRole;
import com.bruce.security.service.RoleService;
import com.bruce.security.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


    @Autowired
    private UserRoleMapper mapper;

    @Autowired
    private RoleService roleService;


    @Override
    public List<Role> getByUserId(Long userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRole::getUserId, userId);
        List<UserRole> list = mapper.selectList(wrapper);
        Set<Long> roleSet = list.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<Role> roleList = new ArrayList<>();
        for (Long roleId : roleSet) {
            Role role = roleService.getById(roleId);
            roleList.add(role);
        }
        return roleList;
    }
}
