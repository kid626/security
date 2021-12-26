package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.vo.PermissionVO;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取权限树
     *
     * @return 树状结构
     */
    PermissionVO tree();

}
