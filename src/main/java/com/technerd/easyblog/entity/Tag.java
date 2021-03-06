package com.technerd.easyblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.technerd.easyblog.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <pre>
 *     文章标签
 * </pre>
 *
 * @author : saysky
 * @date : 2018/1/12
 */
@Data
@TableName("tag")
public class Tag  extends BaseEntity {

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 数量
     */
    @TableField(exist = false)
    @ApiModelProperty(readOnly = true)
    private Integer count;

}
