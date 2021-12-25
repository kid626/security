package com.bruce.security.config;

import com.bruce.security.model.enums.TokenType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/25 16:58
 * @Author fzh
 */
@Component
@ConfigurationProperties(prefix = SecurityProperty.PREFIX)
@Data
public class SecurityProperty implements EnvironmentAware {

    public static final String PREFIX = "security";

    private String[] excludeUrls;

    private String[] defaultExcludeUrls = new String[]{"/security/login", "/security/secretKey", "/security/showResScript",
            "/security/showResTree", "/security/verify/code/**", "/security/redirect", "/error"};

    private String[] swaggerUrls = new String[]{"/doc.html", "/v2/api-docs", "/v2/api-docs-ext",
            "/swagger-resources", "/webjars/**"};

    private String[] activeProfiles;

    private Environment env;

    private TokenManager token;


    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        this.activeProfiles = environment.getActiveProfiles();
    }


    @Data
    public class TokenManager {

        private TokenType type = TokenType.REDIS;

        private long expire = 24 * 60 * 60 * 1000;

        private String name = "token";

        private String secret;

    }
}