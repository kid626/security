package com.bruce.security.model.security;

import com.bruce.security.model.po.Resource;
import com.bruce.security.model.po.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/22 21:36
 * @Author fzh
 */
@Data
public class UserAuthentication extends User implements Authentication {

    private List<Resource> authorities;

    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return getUsername();
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return getUsername();
    }
}
