package com.bruce.security.service.impl;

import com.bruce.security.model.po.User;
import com.bruce.security.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 16:44
 * @Author fzh
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;


    @Test
    public void save() {
        User user = new User();
        user.setUsername("anon");
        user.setPassword("bruce");
        user.setCreateUser("admin");
        user.setUpdateUser("admin");
        long id = userService.save(user);
        System.out.println(id);
    }
}