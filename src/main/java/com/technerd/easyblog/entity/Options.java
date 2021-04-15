package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Options {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String optionName;

    private String optionValue;

    private Integer cateSort;

}