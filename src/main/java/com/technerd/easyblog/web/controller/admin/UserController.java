package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.config.shiro.UserToken;
import com.technerd.easyblog.entity.Category;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.LoginTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.UserVo;
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
    public JsonResult register(@RequestBody User user) {
         userService.insert(user);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.register.success"));
    }

    /**
     * 管理员给用户修改密码
     *
     * @param userVo 新密码
     * @return JsonResult
     */
    @PostMapping(value = "/proxy/changePass")
    @SystemLog(description = "管理员修改其他用户密码", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "管理员修改其他用户密码")
    public JsonResult adminChangePass(@RequestBody UserVo userVo) {
        User user = userService.get(userVo.getUserId());
        if (null != user) {
            userService.updatePassword(user.getId(), userVo.getPassword());
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.user.update-password-success"));
    }

    /**
     * 管理员修改用户资料
     *
     * @param user user
     * @return JsonResult
     */
    @PostMapping(value = "/profile/save/proxy")
    @SystemLog(description = "管理员修改其他用户信息", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "管理员修改其他用户信息")
    public JsonResult adminSaveProfile(@RequestBody User user) {
        userService.insertOrUpdate(user);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.edit-success"));
    }

}
