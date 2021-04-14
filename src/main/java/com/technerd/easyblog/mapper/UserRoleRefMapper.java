package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.UserRoleRef;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleRefMapper extends BaseMapper<UserRoleRef> {
    int deleteByPrimaryKey(Long id);

    int insert(UserRoleRef record);

    int insertSelective(UserRoleRef record);

    UserRoleRef selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRoleRef record);

    int updateByPrimaryKey(UserRoleRef record);
}