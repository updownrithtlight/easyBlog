package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Link {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String linkName;

    private String linkUrl;

    private String linkPic;

    private String linkDesc;

}