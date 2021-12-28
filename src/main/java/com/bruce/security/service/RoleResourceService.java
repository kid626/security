package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.RoleResource;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-27 21:22:57
 * @Author Bruce
 */
public interface RoleResourceService extends IService<RoleResource> {

    /**
     * 根据角色 id 获取
     *
     * @param roleId 角色 主键
     * @return 所有资源
     */
    List<Resource> getByRoleId(Long roleId);

}
