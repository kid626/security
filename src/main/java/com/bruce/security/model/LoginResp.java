package com.bruce.security.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/3/31 13:50
 * @Author fzh
 */
@Data
public class LoginResp implements Serializable {

    private static final long serialVersionUID = 4021035770683221680L;
    private String accessToken;
    private String refreshToken;
    private Long expire;
    private String tokenType = "bearer";
    private String uid;

}
