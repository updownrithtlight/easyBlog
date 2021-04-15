package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class PostCategoryRef {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private Long articleId;

    private Long cateId;

}