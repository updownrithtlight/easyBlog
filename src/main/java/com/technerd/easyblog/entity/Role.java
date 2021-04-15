package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Role {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String role;

    private String description;

    private Integer level;

}