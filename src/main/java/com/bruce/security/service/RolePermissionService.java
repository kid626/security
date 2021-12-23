package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.po.RolePermission;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 根据角色 id 获取
     *
     * @param roleId 角色 主键
     * @return 所有权限资源
     */
    List<Permission> getByRoleId(Long roleId);

}
