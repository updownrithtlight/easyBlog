package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.LogInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogInfoMapper extends BaseMapper<LogInfo> {
    int deleteByPrimaryKey(Long id);

    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    LogInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogInfo record);

    int updateByPrimaryKey(LogInfo record);
}