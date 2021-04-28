package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.entity.ThirdAppBind;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.RoleService;
import com.technerd.easyblog.service.ThirdAppBindService;
import com.technerd.easyblog.service.UserService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import com.technerd.easyblog.web.reqVo.PassChange;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *     后台用户管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/24
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/user")
@Api(value = "后台用户管理控制器")
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ThirdAppBindService thirdAppBindService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;


    /**
     *
     * @param bindId
     * @return
     */
    @GetMapping("/profile")
    @ApiOperation("获取用户详细信息")
    public JsonResult profile(@RequestParam("id") Long bindId) {
        //1.用户信息
        User user = new User();

        //2.第三方信息
        List<ThirdAppBind> thirdAppBinds = thirdAppBindService.findByUserId(user.getId());

        //3.角色列表
        List<Role> roles = roleService.listRolesByUserId(user.getId());
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.edit-success-login-again"));
    }


    /**
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/profile/save")
    @SystemLog(description = "修改个人资料", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "修改个人资料")
    public JsonResult saveProfile(@RequestBody User user) {
        User loginUser = new User();

        User saveUser = userService.get(loginUser.getId());
        saveUser.setUserPass(null);
        saveUser.setId(loginUser.getId());
        saveUser.setUserName(user.getUserName());
        saveUser.setUserDisplayName(user.getUserDisplayName());
        saveUser.setUserSite(user.getUserSite());
        saveUser.setUserAvatar(user.getUserAvatar());
        saveUser.setUserDesc(user.getUserDesc());
        saveUser.setUserEmail(user.getUserEmail());
        userService.insertOrUpdate(saveUser);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.edit-success-login-again"));
    }


    /**
     *
     * @param passChange
     * @return
     */
    @PostMapping(value = "/changePass")
    @ResponseBody
    @SystemLog(description = "修改密码", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "修改密码")
    public JsonResult changePass(@RequestBody PassChange passChange) {
//        User user = userService.get(loginUser.getId());
//        if (user != null && Objects.equals(user.getUserPass(), Md5Util.toMd5(passChange.getBeforePass(), "sens", 10))) {
//            userService.updatePassword(user.getId(), passChange.getNewPass());
//        } else {
//            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.user.old-password-error"));
//        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.user.update-password-success"));
    }

    /**
     * 删除第三方关联
     *
     * @param bindId 关联
     * @return JsonResult
     */
    @PostMapping(value = "/deleteBind")
    @SystemLog(description = "取消第三方关联", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "取消第三方关联")
    public JsonResult checkDelete(@RequestParam("id") Long bindId) {
        //1.判断是否存在
        ThirdAppBind thirdAppBind = thirdAppBindService.findByThirdAppBindId(bindId);
        if (thirdAppBind == null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.bind-not-exist"));
        }
        //2.判断是不是本人
        User user = new User();
        if (!Objects.equals(thirdAppBind.getUserId(), user.getId())) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
        }
        //3.删除
        thirdAppBindService.delete(bindId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


}
