package com.technerd.easyblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.technerd.easyblog.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuyanzhao
 */
@Data
@TableName("role")
public class Role  extends BaseEntity {

    /**
     * 角色名称：admin，author，subscriber
     */
    private String role;

    /**
     * 描述：管理员，作者，订阅者
     */
    private String description;

    /**
     * 级别
     */
    @ApiModelProperty(readOnly = true)
    private Integer level;

    /**
     * 该角色对应的用户数量，非数据库字段
     */
    @TableField(exist = false)
    @ApiModelProperty(readOnly = true)
    private Integer count;

    /**
     * 当前角色的权限列表
     */
    @TableField(exist = false)
    @ApiModelProperty(readOnly = true)
    private List<Permission> permissions;

}