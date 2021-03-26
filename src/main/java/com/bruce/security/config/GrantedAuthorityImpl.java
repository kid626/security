package com.bruce.security.config;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {
  private String authority;
  public GrantedAuthorityImpl(String authority) {
    this.authority = authority;
  }
  @Override
  public String getAuthority() {
    return this.authority;
  }
  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
