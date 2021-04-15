package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Topic {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private String description;

    private Long parentId;

    private Boolean isLeaf;

    private Boolean status;

    private String pathTrace;

    private Integer cateSort;

    private Long userId;

}