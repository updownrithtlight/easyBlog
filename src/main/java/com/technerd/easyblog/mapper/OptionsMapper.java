package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.Options;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionsMapper extends BaseMapper<Options> {
    int deleteByPrimaryKey(Long id);

    int insert(Options record);

    int insertSelective(Options record);

    Options selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Options record);

    int updateByPrimaryKey(Options record);
}