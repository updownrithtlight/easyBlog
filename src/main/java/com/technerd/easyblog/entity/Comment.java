package com.technerd.easyblog.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Comment {
    private Long id;

    private Boolean delFlag;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private Integer postId;

    private String commentAuthor;

    private String commentAuthorIp;

    private String commentContent;

    private Long parentId;

    private Boolean commentStatus;

    private String pathTrace;

    private Boolean commentType;

    private Boolean isAdmin;

    private String commentAuthorAvatar;

}