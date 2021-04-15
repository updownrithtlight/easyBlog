package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class UserRoleRef {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private Long userId;

    private Long roleId;

}