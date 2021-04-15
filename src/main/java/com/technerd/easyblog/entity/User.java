package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String name;

    private String nickName;

    private String password;

    private String email;

    private Boolean gender;

    private Boolean vipLevel;

    private Date registerTime;

    private String isAdmin;

    private Boolean emailEnable;

    private Boolean status;

    private String userDesc;

    private String userAvatar;

}