package com.technerd.easyblog.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technerd.easyblog.common.constant.CommonConstant;
import com.technerd.easyblog.model.vo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 言曌
 * @date 2019-08-07 00:28
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * ID，自动生成
     */
    @TableId(type = IdType.AUTO,value = "id")
    private Long id;

    /**
     * 删除状态：1删除，0未删除
     */
    @TableField(value = "del_flag")
    @TableLogic
    private Integer delFlag = CommonConstant.STATUS_NORMAL;

    /**
     * 创建人用户名
     */
    @ApiModelProperty(readOnly = true)
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime=new Date();

    /**
     * 更新人
     */
    @ApiModelProperty(readOnly = true)
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime=new Date();


}
