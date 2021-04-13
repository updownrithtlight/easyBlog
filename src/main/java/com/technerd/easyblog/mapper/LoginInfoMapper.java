package com.technerd.easyblog.mapper;

import com.technerd.easyblog.entity.LoginInfo;

public interface LoginInfoMapper {
    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);
}