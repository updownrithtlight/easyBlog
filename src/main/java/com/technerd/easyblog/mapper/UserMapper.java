package com.technerd.easyblog.mapper;

import com.technerd.easyblog.entity.User;

public interface UserMapper  {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    User findByUserName(String userName);

    User findByEmail(String userEmail);

}