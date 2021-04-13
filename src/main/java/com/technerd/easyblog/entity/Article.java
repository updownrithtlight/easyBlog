package com.technerd.easyblog.entity;
import java.util.Date;

public class Article {
    private Integer id;

    private Boolean delFlag;

    private Integer createBy;

    private Integer updateBy;

    private Date createTime;

    private Date updateTime;

    private String title;

    private String subtitle;

    private Boolean status;

    private String context;

   public Integer getId() {
       return id;
   }

   public void setId(Integer id) {
       this.id = id;
   }

   public Boolean getDelFlag() {
       return delFlag;
   }

   public void setDelFlag(Boolean delFlag) {
       this.delFlag = delFlag;
   }

   public Integer getCreateBy() {
       return createBy;
   }

   public void setCreateBy(Integer createBy) {
       this.createBy = createBy;
   }

   public Integer getUpdateBy() {
       return updateBy;
   }

   public void setUpdateBy(Integer updateBy) {
       this.updateBy = updateBy;
   }

   public Date getCreateTime() {
       return createTime;
   }

   public void setCreateTime(Date createTime) {
       this.createTime = createTime;
   }

   public Date getUpdateTime() {
       return updateTime;
   }

   public void setUpdateTime(Date updateTime) {
       this.updateTime = updateTime;
   }

   public String getTitle() {
       return title;
   }

   public void setTitle(String title) {
       this.title = title == null ? null : title.trim();
   }

   public String getSubtitle() {
       return subtitle;
   }

   public void setSubtitle(String subtitle) {
       this.subtitle = subtitle == null ? null : subtitle.trim();
   }

   public Boolean getStatus() {
       return status;
   }

   public void setStatus(Boolean status) {
       this.status = status;
   }

   public String getContext() {
       return context;
   }

   public void setContext(String context) {
       this.context = context == null ? null : context.trim();
   }
}