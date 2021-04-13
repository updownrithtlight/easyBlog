package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

//    @Autowired
//    private PostCategoryRefMapper postCategoryRefMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public BaseMapper<Category> getRepository() {
        return categoryMapper;
    }

    @Override
    public QueryWrapper<Category> getQueryWrapper(Category category) {
        //对指定字段查询
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (category != null) {
            if (StrUtil.isNotBlank(category.getName())) {
                queryWrapper.like("cate_name", category.getName());
            }
            if (category.getLevel() != null) {
                queryWrapper.eq("cate_level", category.getLevel());
            }
        }
        return queryWrapper;
    }

    @Override
    public Category insert(Category category) {
        //1.设置CategoryLevel和pathTrace
        setLevelAndPathTrace(category);
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category update(Category category) {
//        //1.设置CategoryLevel和pathTrace
//        setLevelAndPathTrace(category);
//        categoryMapper.updateById(category);
        return null;
    }

    @Override
    public void delete(Long id) {
//        //1.删除分类和文章的关联
//        postCategoryRefMapper.deleteByCateId(id);
//        //2.删除分类
//        categoryMapper.deleteById(id);
    }

    /**
     * 设置CategoryLevel和pathTrace
     *
     * @param category
     * @return
     */
    private Category setLevelAndPathTrace(Category category) {
        if (category.getParentId() == 0 || category.getParentId() == null) {
            category.setLevel(1);
            category.setPathTrace("/");
        } else {
            Category parentCategory = this.get(category.getParentId());
            if (parentCategory != null && parentCategory.getLevel() != null) {
                category.setLevel(parentCategory.getLevel() + 1);
                category.setPathTrace(parentCategory.getPathTrace() + parentCategory.getId() + "/");
            }
        }
        return category;
    }

    @Override
    public List<Category> findByUserId(Long userId) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("user_id", userId);
//        return categoryMapper.selectList(queryWrapper);
        return null;
    }

    @Override
    public Page<Category> findByUserIdWithCountAndLevel(Long userId, Page<Category> page) {
//        List<Category> categories = categoryMapper.findByUserIdWithCount(userId, page);
//        categories.forEach(category -> {
//            String str = "";
//            for (int i = 1; i < category.getCateLevel(); i++) {
//                str += "——";
//            }
//            category.setCateName(str + category.getCateName());
//        });
//        return page.setRecords(CategoryUtil.getCategoryList(categories));
        return null;
    }

    @Override
    public List<Category> findByUserIdWithLevel(Long userId) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", userId);
//        List<Category> categories = categoryMapper.selectByMap(map);
//        return CategoryUtil.getCategoryList(categories);
        return null;
    }


    @Override
    public List<Category> findByPostId(Long postId) {
//        String value = redisUtil.get(RedisKeys.POST_CATEGORY + postId);
//        // 先从缓存取，缓存没有从数据库取
//        if (StringUtils.isNotEmpty(value)) {
//            return JSON.parseArray(value, Category.class);
//        }
//        List<Category> categoryList = categoryMapper.findByPostId(postId);
//        redisUtil.set(RedisKeys.POST_CATEGORY + postId, JSON.toJSONString(categoryList), RedisKeyExpire.POST_CATEGORY);
//        return categoryList;
        return null;
    }

    @Override
    public Integer countPostByCateId(Long cateId) {
//        return postCategoryRefMapper.countPostByCateId(cateId);
        return null;
    }


    @Override
    public List<Long> selectChildCateId(Long cateId) {
//        String pathTrace = "/" + cateId + "/";
//        return categoryMapper.selectChildCateIds(pathTrace);
        return null;
    }

    @Override
    public Category insertOrUpdate(Category entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
        return entity;
    }

    @Override
    public Integer getTodayCount() {
//        return categoryMapper.getTodayCount();
        return 0;
    }

    @Override
    public Integer deleteByUserId(Long userId) {
//        return categoryMapper.deleteByUserId(userId);
        return 0;
    }

    @Override
    public List<Category> cateIdsToCateList(List<Long> cateIds, Long userId) {
        List<Category> categoryList = this.findByUserId(userId);
        List<Long> allCateIds = categoryList.stream().map(Category::getId).collect(Collectors.toList());
        List<Category> result = new ArrayList<>();
        for(Long id : cateIds) {
            if(allCateIds.contains(id)) {
                Category category = new Category();
                category.setId(id);
                result.add(category);
            }
        }
        return result;
    }



}
