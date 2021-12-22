package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.UserMapper;
import com.bruce.security.model.entity.UserModel;
import com.bruce.security.model.po.User;
import com.bruce.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        User user = mapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        return new UserModel(username, passwordEncoder.encode(user.getPassword()));
    }
}
