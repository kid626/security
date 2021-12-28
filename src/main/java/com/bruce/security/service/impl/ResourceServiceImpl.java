package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.ResourceMapper;
import com.bruce.security.model.po.Resource;
import com.bruce.security.model.vo.ResourceVO;
import com.bruce.security.service.ResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-27 21:22:57
 * @Author Bruce
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper mapper;

    @Override
    public List<ResourceVO> tree() {
        List<Resource> list = mapper.selectList(new QueryWrapper<>());
        List<ResourceVO> all = list.stream().map(resource -> {
            ResourceVO vo = new ResourceVO();
            BeanUtils.copyProperties(resource, vo);
            return vo;
        }).collect(Collectors.toList());

        //操作所有菜单数据
        Map<String, List<ResourceVO>> groupMap = all.stream().collect(Collectors.groupingBy(ResourceVO::getParentCode));
        all.forEach(resourceVO -> {
            resourceVO.setChildren(groupMap.get(resourceVO.getCode()));
            resourceVO.setHasChild(CollectionUtils.isEmpty(resourceVO.getChildren()));
        });
        return all.stream().filter(resourceVO -> StringUtils.isBlank(resourceVO.getParentCode())).collect(Collectors.toList());
    }

}
