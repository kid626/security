package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.po.User;
import com.bruce.security.model.security.UserAuthentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface UserService extends IService<User>, UserDetailsService {

    /**
     * 登录校验
     *
     * @param loginDTO LoginDTO
     * @return UserAuthentication
     */
    UserAuthentication login(LoginDTO loginDTO);

    /**
     * token登录校验
     *
     * @param token token
     * @return UserAuthentication
     */
    UserAuthentication login(String token);


    /**
     * 根据用户主键获取所有资源
     *
     * @param userId 用户主键
     * @return 所有权限资源
     */
    List<Permission> getByUserId(Long userId);


}
