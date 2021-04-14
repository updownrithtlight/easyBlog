package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.RolePermissionRef;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionRefMapper extends BaseMapper<RolePermissionRef> {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionRef record);

    int insertSelective(RolePermissionRef record);

    RolePermissionRef selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionRef record);

    int updateByPrimaryKey(RolePermissionRef record);
}