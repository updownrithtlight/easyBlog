package com.technerd.easyblog.service;

import com.technerd.easyblog.entity.Category;

import java.util.List;

/**
 * <pre>
 *     分类业务逻辑接口
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/30
 */
public interface CategoryService  {

    /**
     * 查询所有分类目录,带count和根据level封装name
     *
     * @return 返回List集合
     */
    List<Category> findByUserId(Long userId);


}
