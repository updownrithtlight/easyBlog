package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Article {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String title;

    private String subtitle;

    private Boolean status;

    private String context;

}