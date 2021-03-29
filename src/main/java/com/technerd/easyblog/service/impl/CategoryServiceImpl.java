package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.common.constant.RedisKeyExpire;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.entity.Category;
import com.technerd.easyblog.mapper.CategoryMapper;
import com.technerd.easyblog.service.CategoryService;
import com.technerd.easyblog.utils.CategoryUtil;
import com.technerd.easyblog.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <pre>
 *     分类业务逻辑实现类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/30
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;





    @Override
    public List<Category> findByUserId(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        return categoryMapper.selectList(queryWrapper);
    }



}
