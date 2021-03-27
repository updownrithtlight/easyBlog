package com.technerd.easyblog.service;

import com.technerd.easyblog.common.base.BaseService;
import com.technerd.easyblog.entity.Widget;

import java.util.List;

/**
 * <pre>
 *     小工具业务逻辑接口
 * </pre>
 *
 * @author : saysky
 * @date : 2018/1/24
 */
public interface WidgetService extends BaseService<Widget, Long> {


    /**
     * 根据类型查询，以树形展示，用于前台
     *
     * @param widgetType 小工具类型
     * @return List
     */
    List<Widget> findByWidgetType(Integer widgetType);
}
