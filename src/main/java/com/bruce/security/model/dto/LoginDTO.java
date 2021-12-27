package com.bruce.security.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/23 9:40
 * @Author fzh
 */
@Data
public class LoginDTO {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码key")
    private String verifyKey;
    @ApiModelProperty("验证码code")
    private String verifyCode;

}
