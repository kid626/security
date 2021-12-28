package com.bruce.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bruce.security.component.RedissonComponent;
import com.bruce.security.component.SecurityComponent;
import com.bruce.security.component.TokenComponent;
import com.bruce.security.config.SecurityProperty;
import com.bruce.security.mapper.UserMapper;
import com.bruce.security.model.constant.RedisConstant;
import com.bruce.security.model.dto.LoginDTO;
import com.bruce.security.model.enums.YesOrNoEnum;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.Role;
import com.bruce.security.model.po.User;
import com.bruce.security.model.security.UserAuthentication;
import com.bruce.security.service.RoleResourceService;
import com.bruce.security.service.UserRoleService;
import com.bruce.security.service.UserService;
import com.bruce.security.util.EncryptUtil;
import com.bruce.security.util.RandomUtil;
import com.bruce.security.util.UserUtil;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RedissonComponent redissonComponent;

    @Autowired
    private SecurityComponent securityComponent;

    @Autowired
    private SecurityProperty property;

    @Override
    public UserAuthentication login(LoginDTO loginDTO) {
        SecurityProperty.CaptchaManager captcha = property.getCaptcha();
        SecurityProperty.RetryManager retryManager = property.getRetry();
        if (captcha != null && !YesOrNoEnum.NO.getCode().equals(captcha.getEnable())) {
            securityComponent.checkCaptchaAndDelete(loginDTO.getRid(), loginDTO.getCode());
        }
        RAtomicLong retryNumAtomicLong = redissonComponent.getRAtomicLong(MessageFormat.format(RedisConstant.LOGIN_RETRY_NUM, loginDTO.getUsername()));
        User user = getByUsername(loginDTO.getUsername());
        if (user == null || !user.getPassword().equals(EncryptUtil.md5(loginDTO.getPassword(), user.getSalt()))) {
            securityComponent.incrExpireAndThrow(retryNumAtomicLong, retryManager);
        }
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(user, userAuthentication);
        String token = tokenComponent.createToken(loginDTO.getUsername());
        userAuthentication.setToken(token);
        List<Resource> list = getByUserId(user.getId());
        userAuthentication.setAuthorities(list);
        return userAuthentication;
    }

    @Override
    public UserAuthentication login(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String value = tokenComponent.getFromToken(token);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        User user = JSONObject.parseObject(value, User.class);
        // 刷新用户信息
        user = mapper.selectById(user.getId());
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(user, userAuthentication);
        List<Resource> list = getByUserId(user.getId());
        userAuthentication.setToken(token);
        userAuthentication.setAuthorities(list);
        return userAuthentication;
    }

    @Override
    public void logout() {
        String token = UserUtil.getCurrentUser().getToken();
        UserUtil.clearCurrentUser();
        tokenComponent.removeToken(token);
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(User::getUsername, username)
                .eq(User::getEnable, YesOrNoEnum.YES.getCode());
        return mapper.selectOne(wrapper);
    }

    @Override
    public List<Resource> getByUserId(Long userId) {
        List<Resource> result = new ArrayList<>();
        List<Role> roleList = userRoleService.getByUserId(userId);
        for (Role role : roleList) {
            List<Resource> resourceList = roleResourceService.getByRoleId(role.getId());
            result.addAll(resourceList);
        }
        return result;
    }

    @Override
    public List<String> permList() {
        List<Resource> list = (List<Resource>) UserUtil.getCurrentUser().getAuthorities();
        return list.stream().map(Resource::getCode).collect(Collectors.toList());
    }

    @Override
    public long save(User user) {
        Date now = new Date();
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(EncryptUtil.md5(user.getPassword(), user.getSalt()));
        user.setEnable(YesOrNoEnum.YES.getCode());
        user.setIsDelete(YesOrNoEnum.NO.getCode());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        mapper.insert(user);
        return user.getId();
    }
}
