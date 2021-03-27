package com.technerd.easyblog.service;

import com.technerd.easyblog.common.base.BaseService;
import com.technerd.easyblog.entity.Slide;

import java.util.List;

/**
 * <pre>
 *     菜单业务逻辑接口
 * </pre>
 *
 * @author : saysky
 * @date : 2018/1/24
 */
public interface SlideService extends BaseService<Slide, Long> {

    /**
     * 根据类型查询，以树形展示，用于前台
     *
     * @param slideType 菜单类型
     * @return List
     */
    List<Slide> findBySlideType(Integer slideType);

}
