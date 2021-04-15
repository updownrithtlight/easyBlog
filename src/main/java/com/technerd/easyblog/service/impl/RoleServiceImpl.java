package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.mapper.RoleMapper;
import com.technerd.easyblog.service.RolePermissionRefService;
import com.technerd.easyblog.service.RoleService;
import com.technerd.easyblog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     角色业务逻辑实现类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionRefService rolePermissionRefService;

//    @Autowired
//    private RedisUtil redisUtil;

    @Override
    public BaseMapper<Role> getRepository() {
        return roleMapper;
    }

    @Override
    public QueryWrapper<Role> getQueryWrapper(Role role) {
        //对指定字段查询
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (role != null) {
            if (StrUtil.isNotBlank(role.getRole())) {
                queryWrapper.eq("role", role.getRole());
            }
            if (StrUtil.isNotBlank(role.getDescription())) {
                queryWrapper.eq("description", role.getDescription());
            }
        }
        return queryWrapper;
    }

    @Override
    public void deleteByUserId(Long userId) {
//        roleMapper.deleteByUserId(userId);
    }

    @Override
    public Role findByRoleId(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public Role findByRoleName(String roleName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role", roleName);
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Role> listRolesByUserId(Long userId) {
//        return roleMapper.findByUserId(userId);
        return null;
    }

    @Override
    public Integer countUserByRoleId(Long roleId) {
//        return roleMapper.countUserByRoleId(roleId);
        return null;
    }

    @Override
    public List<Role> findAllWithCount() {
//        return roleMapper.findAllWithCount();
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role insert(Role role) {
//        roleMapper.insert(role);
//        if (role.getPermissions() != null && role.getPermissions().size() > 0) {
//            List<RolePermissionRef> rolePermissionRefs = new ArrayList<>(role.getPermissions().size());
//            for (Permission permission : role.getPermissions()) {
//                rolePermissionRefs.add(new RolePermissionRef(role.getId(), permission.getId()));
//            }
//            rolePermissionRefService.batchSaveByRolePermissionRef(rolePermissionRefs);
//        }
//        return role;
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role update(Role role) {
//        roleMapper.updateById(role);
//        if (role.getPermissions() != null && role.getPermissions().size() > 0) {
//            rolePermissionRefService.deleteRefByRoleId(role.getId());
//            List<RolePermissionRef> rolePermissionRefs = new ArrayList<>(role.getPermissions().size());
//            for (Permission permission : role.getPermissions()) {
//                rolePermissionRefs.add(new RolePermissionRef(role.getId(), permission.getId()));
//            }
//            rolePermissionRefService.batchSaveByRolePermissionRef(rolePermissionRefs);
//        }
        return role;
    }

    @Override
    public Role insertOrUpdate(Role entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }

//        redisUtil.delByKeys(RedisKeys.USER_PERMISSION_URLS);
        return entity;
    }

}
