package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.RolePermissionMapper;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.po.RolePermission;
import com.bruce.security.service.PermissionService;
import com.bruce.security.service.RolePermissionService;
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
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private RolePermissionMapper mapper;

    @Autowired
    private PermissionService permissionService;


    @Override
    public List<Permission> getByRoleId(Long roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        List<RolePermission> list = mapper.selectList(wrapper);
        Set<Long> permissionSet = list.stream().map(RolePermission::getPermissionId).collect(Collectors.toSet());
        List<Permission> permissionList = new ArrayList<>();
        for (Long permissionId : permissionSet) {
            Permission permission = permissionService.getById(permissionId);
            permissionList.add(permission);
        }
        return permissionList;
    }
}
