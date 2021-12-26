package com.bruce.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.security.mapper.PermissionMapper;
import com.bruce.security.model.po.Permission;
import com.bruce.security.model.vo.PermissionVO;
import com.bruce.security.service.PermissionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Copyright Copyright © 2021 Bruce . All rights reserved.
 * @Desc service 实现类
 * @ProjectName security
 * @Date 2021-12-22 19:33:26
 * @Author Bruce
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper mapper;

    @Override
    public PermissionVO tree() {
        List<Permission> list = mapper.selectList(new QueryWrapper<>());
        List<PermissionVO> all = list.stream().map(permission -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(permission, vo);
            return vo;
        }).collect(Collectors.toList());
        return treeList(all);
    }

    /**
     * 获取 tree
     *
     * @param allList 需要生成 tree 的 list 集合
     * @return 根节点
     */
    private PermissionVO treeList(List<PermissionVO> allList) {
        PermissionVO vo = getTop();
        //迭代子节点信息
        List<PermissionVO> list = treeChild(vo.getId(), allList);
        vo.setChildren(list);
        vo.setHasChild(!CollectionUtils.isEmpty(list));
        return vo;
    }

    /**
     * 递归去获取子结点
     *
     * @param id      当前 id
     * @param allList 需要生成 tree 的 list 集合
     * @return 子结点集合
     */
    private List<PermissionVO> treeChild(Long id, List<PermissionVO> allList) {
        List<PermissionVO> mapArr = new ArrayList<>();
        for (PermissionVO x : allList) {
            if (x.getPid().equals(id)) {
                mapArr.add(x);
            }
        }
        for (PermissionVO y : mapArr) {
            List<PermissionVO> list = treeChild(y.getId(), allList);
            y.setChildren(list);
            y.setHasChild(!CollectionUtils.isEmpty(list));
        }
        return mapArr;
    }

    private PermissionVO getTop() {
        PermissionVO vo = new PermissionVO();
        vo.setId(0L);
        return vo;
    }

}
