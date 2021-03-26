package com.bruce.security.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class JwtToken {

    /**
     * 用户主键
     */
    private String uid;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户角色
     */
    private Set<String> roles;
    /**
     * 过期时间
     */
    private Long exp;
    /**
     * 唯一身份标识
     */
    private String jti;
    /**
     * 此处可能是拼写错误 iat: jwt的签发时间
     */
    private String ati;

    @JsonIgnore
    boolean isRefreshToken() {
        return ati != null;
    }

    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() >= (exp * 1000);
    }
}
