package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.Role;
import com.bruce.security.model.po.UserRole;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户主键获取所有角色
     *
     * @param userId 用户主键
     * @return 角色
     */
    List<Role> getByUserId(Long userId);

}
