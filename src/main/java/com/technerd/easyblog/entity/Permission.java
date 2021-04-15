package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Permission {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private String url;

    private String icon;

    private Integer sort;

    private String resourceType;

    private Integer pid;

    private Integer level;

}