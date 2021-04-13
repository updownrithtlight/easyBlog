package com.technerd.easyblog.mapper;

import com.technerd.easyblog.entity.Options;

public interface OptionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Options record);

    int insertSelective(Options record);

    Options selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Options record);

    int updateByPrimaryKey(Options record);
}