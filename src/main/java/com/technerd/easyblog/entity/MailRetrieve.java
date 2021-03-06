package com.technerd.easyblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.technerd.easyblog.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 邮件验证码
 * @author 言曌
 * @date 2018/2/23 上午10:24
 */

@Data
@TableName("mail_retrieve")
public class MailRetrieve extends BaseEntity  {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * Email
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private Date outTime;


}