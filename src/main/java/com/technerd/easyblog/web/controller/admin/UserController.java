package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.config.shiro.UserToken;
import com.technerd.easyblog.entity.Category;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.LoginTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.UserService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 *     后台用户管理控制器
 * </pre>
 *
 * @author : technerd
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;


    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    public static final String USER_NAME = "userName";
    public static final String USER_DISPLAY_NAME = "userDisplayName";
    public static final String EMAIL = "email";
    public static final String URL = "url";

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册")
    public User register(@RequestBody User user) {
        return userService.insert(user);
    }

}
