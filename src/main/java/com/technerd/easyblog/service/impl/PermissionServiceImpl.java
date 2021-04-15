package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.technerd.easyblog.entity.Permission;
import com.technerd.easyblog.mapper.PermissionMapper;
import com.technerd.easyblog.mapper.RolePermissionRefMapper;
import com.technerd.easyblog.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 *     角色业务逻辑实现类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionRefMapper rolePermissionRefMapper;



    @Override
    public List<Permission> listPermissionsByRoleId(Long roleId) {
//        return permissionMapper.findByRoleId(roleId);
        return null;
    }

    @Override
    public Set<String> findPermissionUrlsByUserId(Long userId) {
//        // 先从缓存取，缓存没有从数据库取
//        if (StringUtils.isNotEmpty(value)) {
//            return JSON.parseObject(value, Set.class);
//        }
//        List<Permission> permissions = permissionMapper.findPermissionByUserId(userId);
//        Set<String> urls = permissions.stream().map(p -> p.getUrl()).collect(Collectors.toSet());
        return null;
    }

    @Override
    public List<Permission> findPermissionTreeByUserIdAndResourceType(Long userId, String resourceType) {
//        List<Permission> permissions = permissionMapper.findPermissionByUserIdAndResourceType(userId, resourceType);
//        return PermissionUtil.getPermissionTree(permissions);
        return null;
    }

    @Override
    public List<Permission> findByResourceType(String resourceType) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("resource_type", resourceType);
        return permissionMapper.selectByMap(map);
    }



    @Override
    public void insertOrUpdate(Permission entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        permissionMapper.deleteById(id);
//        rolePermissionRefMapper.deleteByPermissionId(id);
        // 删除所有的用户的权限列表缓存
    }

    @Override
    public void insert(Permission entity) {

    }

    @Override
    public void update(Permission entity) {

    }
}
