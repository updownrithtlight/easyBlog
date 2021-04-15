package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Category {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private Long parentId;

    private Integer sort;

    private Integer level;

    private String pathTrace;

    private String desc;

    private Long userId;

}