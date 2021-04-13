package com.technerd.easyblog.model.dto;

import lombok.Data;

/**
 * <pre>
 *     七牛上传自定义凭证回调解析
 * </pre>
 *
 * @author : saysky
 * @date : 2018/12/3
 */
@Data
public class QiNiuPutSet {

    /**
     * 图片大小
     */
    private Long size;

    /**
     * 长
     */
    private Integer w;

    /**
     * 宽
     */
    private Integer h;
}
