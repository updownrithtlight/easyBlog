package com.technerd.easyblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.technerd.easyblog.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <pre>
 *     文章分类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/30
 */
@Data
@TableName("category")
public class Category extends BaseEntity {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String cateName;

    /**
     * 分类父节点
     */
    private Long catePid;

    /**
     * 分类排序号
     */
    private Integer cateSort;

    /**
     * 分类层级
     */
    @ApiModelProperty(readOnly = true)
    private Integer cateLevel = 1;

    /**
     * 关系路径
     */
    @ApiModelProperty(readOnly = true)
    private String pathTrace;

    /**
     * 分类描述
     */
    private String cateDesc;

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
