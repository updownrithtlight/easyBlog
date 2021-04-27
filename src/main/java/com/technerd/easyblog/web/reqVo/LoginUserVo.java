package com.technerd.easyblog.web.reqVo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Data
public class LoginUserVo {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    /**
     * 密码
     */
    private String userPass;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String userEmail;
}
