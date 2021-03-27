package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.UserRoleRef;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuyanzhao
 */
@Mapper
public interface UserRoleRefMapper extends BaseMapper<UserRoleRef> {

    /**
     * 根据用户Id删除
     *
     * @param userId 用户Id
     * @return 影响行数
     */
    Integer deleteByUserId(Long userId);
}

