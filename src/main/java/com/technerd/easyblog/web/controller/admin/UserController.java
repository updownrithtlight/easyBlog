package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.*;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import com.technerd.easyblog.web.query.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 删除用户
     *
     * @param userId 用户Id
     * @return JsonResult
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除用户")
    public JsonResult removeUser(@RequestParam("id") Long userId) {
        //1.检查用户有没有文章
        Integer postCount = postService.countByUserId(userId);
        if (postCount > 0) {
            Object[] args = {postCount};
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.user.delete-failed", args));
        }
        userService.delete(userId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    /**
     * 查询用户详情
     *
     * @return
     */
    @GetMapping("/get")
    @ApiOperation(value = "查询用户详情")
    public JsonResult<User> get(@RequestParam("id") Long userId) {
        User user = userService.get(userId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"),user);
    }

    /**
     * 批量删除
     *
     * @param ids 用户ID列表
     * @return
     */
    @DeleteMapping(value = "/batchDelete")
    @SystemLog(description = "批量删除用户", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "批量删除用户")
    public JsonResult batchDelete(@RequestParam("ids") List<Long> ids) {
        //批量操作
        if (ids == null || ids.size() == 0 || ids.size() >= 100) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), "参数不合法!");
        }
        List<User> userList = userService.findByBatchIds(ids);
        for (User user : userList) {
            userService.delete(user.getId());
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    /**
     * 新增/修改用户
     *
     * @param user user
     * @return
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存用户", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "新增/修改用户")
    public JsonResult saveUser(@RequestBody User user) {
        //1.添加用户
        userService.insertOrUpdate(user);
       return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }




    /**
     * 管理员修改用户资料
     *
     * @param user user
     * @return JsonResult
     */
    @PostMapping(value = "/profile/save/proxy")
    @ResponseBody
    @SystemLog(description = "管理员修改其他用户信息", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "管理员修改用户资料")
    public JsonResult adminSaveProfile(@RequestBody User user) {
        userService.insertOrUpdate(user);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.edit-success"));
    }

    /**
     * 管理员给用户修改密码
     *
     * @param userVo
     * @return JsonResult
     */
    @PostMapping(value = "/proxy/changePass")
    @SystemLog(description = "管理员修改其他用户密码", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "管理员修改其他用户密码")
    public JsonResult adminChangePass(@RequestBody UserVo userVo) {
        User user = userService.get(userVo.getUserId());
        String password=null;
        if (null != user) {
            String salt = UUID.randomUUID().toString().substring(0, 24);
             password = RandomStringUtils.randomNumeric(8);
            String md5 = new SimpleHash("MD5", password, salt, 32).toString();
            userService.updatePassword(user.getId(), md5);
        }
        JsonResult result = new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.user.update-password-success"));
        result.setResult(password);
        return result;
    }
}
