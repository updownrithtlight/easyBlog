package com.technerd.easyblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.technerd.easyblog.common.base.BaseEntity;
import lombok.Data;

/**
 * <pre>
 *     第三方应用绑定
 * </pre>
 *
 * @author : saysky
 * @date : 2018/1/19
 */
@Data
@TableName("sens_third_app_bind")
public class ThirdAppBind  extends BaseEntity {

    /**
     * OpenId
     */
    private String openId;

    /**
     * 绑定类型：qq/github
     */
    private String appType;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 状态：1启动，0禁用
     */
    private Integer status;

}
