package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Menu {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private String menuUrl;

    private String menuicon;

    private Integer cateSort;

    private Long parentId;

    private Boolean menuType;

}