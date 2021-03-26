package com.bruce.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class TokenAuthentication implements Authentication {
  @Getter private final String uid;
  @Getter private final String username;
  @Getter private final Set<String> roles;
  protected TokenAuthentication(String uid, String username, Set<String> roles) {
    this.uid = uid;
    this.username = username;
    this.roles = roles;
  }
  public static TokenAuthentication of(JwtToken token) {
    return new TokenAuthentication(token.getUid(), token.getUserName(), token.getRoles());
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    for (String role : roles) {
      authorities.add(new GrantedAuthorityImpl(role));
    }
    return authorities;
  }
  @Override
  public Object getCredentials() {
    return null;
  }
  @Override
  public Object getDetails() {
    return username;
  }
  @Override
  public Object getPrincipal() {
    return username;
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
    return username;
  }
}
