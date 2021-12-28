package com.bruce.security.model.security;

import com.bruce.security.model.enums.YesOrNoEnum;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/22 18:05
 * @Author fzh
 */
@Data
public class UserModel extends User implements UserDetails {

    public UserModel(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEnable(YesOrNoEnum.YES.getCode());
    }

    private List<Resource> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return YesOrNoEnum.YES.getCode().equals(this.getEnable());
    }
}
