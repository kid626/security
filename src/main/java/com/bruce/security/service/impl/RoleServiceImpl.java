package com.bruce.security.service.impl;

import com.bruce.security.model.po.Role;
import com.bruce.security.mapper.RoleMapper;
import com.bruce.security.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
