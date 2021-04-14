package com.technerd.easyblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.entity.PostCategoryRef;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostCategoryRefMapper extends BaseMapper<PostCategoryRef> {
    int deleteByPrimaryKey(Long id);

    int insert(PostCategoryRef record);

    int insertSelective(PostCategoryRef record);

    PostCategoryRef selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PostCategoryRef record);

    int updateByPrimaryKey(PostCategoryRef record);
}