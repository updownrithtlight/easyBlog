package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginInfoMapper extends BaseMapper<LoginInfo> {
    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);
}