package com.technerd.easyblog.utils;

import com.technerd.easyblog.model.dto.EasyConst;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *     OwO表情工具类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/22
 */
@Slf4j
public class OwoUtil {

    /**
     * 将表情标志转化为图片地址
     *
     * @param mark 表情标志
     * @return 表情图片地址
     */
    public static String markToImg(String mark) {
        for (String key : EasyConst.OWO.keySet()) {
            mark = mark.replace(key, EasyConst.OWO.get(key));
        }
        return mark;
    }
}
