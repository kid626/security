package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
public interface UserService extends IService<User>, UserDetailsService {

}
