package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.RoleResourceMapper;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.RoleResource;
import com.bruce.security.service.ResourceService;
import com.bruce.security.service.RoleResourceService;
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
 * @Date 2021-12-27 21:22:57
 * @Author Bruce
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

    @Autowired
    private RoleResourceMapper mapper;

    @Autowired
    private ResourceService resourceService;

    @Override
    public List<Resource> getByRoleId(Long roleId) {
        QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleResource::getRoleId, roleId);
        List<RoleResource> list = mapper.selectList(wrapper);
        Set<Long> resourceSet = list.stream().map(RoleResource::getResourceId).collect(Collectors.toSet());
        List<Resource> resourceList = new ArrayList<>();
        for (Long resourceId : resourceSet) {
            Resource resource = resourceService.getById(resourceId);
            resourceList.add(resource);
        }
        return resourceList;
    }
}
