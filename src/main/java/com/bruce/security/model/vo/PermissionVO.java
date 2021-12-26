package com.bruce.security.model.vo;

import com.bruce.security.model.po.Permission;
import lombok.Data;

import java.util.List;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/26 16:49
 * @Author fzh
 */
@Data
public class PermissionVO extends Permission {

    private List<PermissionVO> children;

    private boolean hasChild;

}
