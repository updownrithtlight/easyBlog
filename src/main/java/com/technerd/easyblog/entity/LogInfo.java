package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class LogInfo {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private Boolean logType;

    private String requestUrl;

    private String requestType;

    private String requestParam;

    private String username;

    private String ip;

    private String ipinfo;

    private Integer costTime;

}