package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author liuyanzhao
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {

    /**
     * 获得今日新增数量
     * @return
     */
    Integer getTodayCount();
}

