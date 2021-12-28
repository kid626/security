package com.bruce.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.vo.ResourceVO;

import java.util.List;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 层
 * @ProjectName security
 * @Date 2021-12-27 21:22:57
 * @Author Bruce
 */
public interface ResourceService extends IService<Resource> {


    /**
     * 获取权限树
     *
     * @return 树状结构
     */
    List<ResourceVO> tree();

    /**
     * 获取所有按钮资源
     *
     * @return 所有按钮资源
     */
    List<Resource> getAvailableResources();

}
