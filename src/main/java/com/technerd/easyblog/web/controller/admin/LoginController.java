package com.technerd.easyblog.web.controller.admin;

import cn.hutool.core.lang.Validator;
import com.google.common.base.Strings;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.config.shiro.UserToken;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.entity.UserRoleRef;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.dto.SensConst;
import com.technerd.easyblog.model.enums.*;
import com.technerd.easyblog.service.*;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.Md5Util;
import com.technerd.easyblog.utils.RedisUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author technerd
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@Api(value = "登录模块")
public class LoginController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleRefService userRoleRefService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 处理跳转到登录页的请求
     *
     * @return 模板路径admin/admin_login
     */
    @GetMapping(value = "/login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        //如果已经登录，跳转到后台首页
        if (subject.isAuthenticated()) {
            return "redirect:/admin";
        }
        return "admin/admin_login";
    }

    /**
     * 验证登录信息
     *
     * @param account  登录名：邮箱／用户名
     * @param password password 密码
     * @return JsonResult JsonResult
     */
    @PostMapping(value = "/getLogin")
    @ResponseBody
    @SystemLog(description = "用户登录", type = LogTypeEnum.LOGIN)
    public JsonResult getLogin(String account,
                               String password) {

        Subject subject = SecurityUtils.getSubject();
        UserToken token = new UserToken(account, password, LoginTypeEnum.NORMAL.getValue());
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                //登录成功，修改登录错误次数为0
                User user = (User) subject.getPrincipal();
                userService.updateUserLoginNormal(user);
                //清除权限列表缓存，重新加载
                redisUtil.del(RedisKeys.USER_PERMISSION_URLS + user.getId());
                Set<String> permissionUrls = permissionService.findPermissionUrlsByUserId(user.getId());
                subject.getSession().setAttribute("permissionUrls", permissionUrls);
                return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.login.success"));
            }
        } catch (UnknownAccountException e) {
            log.info("UnknownAccountException -- > 账号不存在：");
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.login.user.not.found"));
        } catch (IncorrectCredentialsException e) {
            //更新失败次数
            User user;
            if (Validator.isEmail(account)) {
                user = userService.findByEmail(account);
            } else {
                user = userService.findByUserName(account);
            }
            if (user != null) {
                Integer errorCount = userService.updateUserLoginError(user);
                //超过五次禁用账户
                if (errorCount >= CommonParamsEnum.FIVE.getValue()) {
                    userService.updateUserLoginEnable(user, TrueFalseEnum.FALSE.getValue());
                }
                Object[] args = {(5 - errorCount) > 0 ? (5 - errorCount) : 0};
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.login.failed", args));
            }
        } catch (LockedAccountException e) {
            log.info("LockedAccountException -- > 账号被锁定");
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.query-failed"));
    }

    /**
     * 处理跳转到登录页的请求
     *
     * @return 模板路径admin/admin_login
     */
    @GetMapping(value = "/register")
    public String register() {
        Subject subject = SecurityUtils.getSubject();
        //如果已经登录，跳转到后台首页
        if (subject.isAuthenticated()) {
            return "redirect:/admin";
        }
        return "admin/admin_register";
    }


    /**
     * 验证注册信息
     *
     * @param user  用户
     * @return JsonResult JsonResult
     */
    @PostMapping(value = "/getRegister")
    @SystemLog(description = "用户注册", type = LogTypeEnum.REGISTER)
    @ApiOperation(value = "用户注册")
    public JsonResult getRegister(@RequestBody User user) {
        if (StringUtils.equals(SensConst.OPTIONS.get(BlogPropertiesEnum.OPEN_REGISTER.getProp()), TrueFalseEnum.FALSE.getValue())) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.register.close"));
        }
        //1.检查用户名
        User checkUser = userService.findByUserName(user.getUserName());
        if (checkUser != null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.user.user-name-exist"));
        }
        //2.检查用户名
        User checkEmail = userService.findByEmail(user.getUserEmail());
        if (checkEmail != null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.user.user-email-exist"));
        }
        //3.创建用户
        user.setEmailEnable(TrueFalseEnum.FALSE.getValue());
        user.setLoginEnable(TrueFalseEnum.TRUE.getValue());
        user.setLoginError(0);
        //加密用户的密码
        String salt = UUID.randomUUID().toString().substring(0, 24);
        String password = new SimpleHash("MD5", user.getUserPass(), salt, 32).toString();
//        user.setSalt(salt);
        user.setUserPass(password);
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userService.insert(user);

        //4.关联角色
        String defaultRole = SensConst.OPTIONS.get(BlogPropertiesEnum.DEFAULT_REGISTER_ROLE.getProp());
        Role role = null;
        if (!Strings.isNullOrEmpty(defaultRole)) {
            role = roleService.findByRoleName(defaultRole);
        }
        if (role == null) {
            role = roleService.findByRoleName(RoleEnum.SUBSCRIBER.getValue());
        }
        if (role != null) {
            userRoleRefService.insert(new UserRoleRef(user.getId(), role.getId()));
        }

        //4.发送激活验证码
//        if (StringUtils.equals(SensConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue())) {
//            Map<String, Object> map = new HashMap<>(8);
//            map.put("blogTitle", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
//            map.put("blogUrl", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
//            map.put("activeUrl", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
//            mailService.sendTemplateMail(
//                    userEmail,  SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "账户 - 电子邮箱激活", map, "common/mail_template/mail_active_email.ftl");
//        } else {
//            return new JsonResult(ResultCodeEnum.FAIL.getCode(), "本站没有启动SMTP，无法发送邮件！");
//        }

        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.register.success"));
    }

    /**
     * 处理跳转忘记密码的请求
     *
     * @return 模板路径admin/admin_login
     */
    @GetMapping(value = "/forget")
    public String forget() {
        Subject subject = SecurityUtils.getSubject();
        //如果已经登录，跳转到后台首页
        if (subject.isAuthenticated()) {
            return "redirect:/admin";
        }
        return "admin/admin_forget";
    }

    /**
     * 处理忘记密码
     *
     * @param userName  用户名
     * @param userEmail 邮箱
     * @return JsonResult
     */
    @PostMapping(value = "/getForget")
    @ResponseBody
    @SystemLog(description = "忘记密码", type = LogTypeEnum.FORGET)
    public JsonResult getForget(@ModelAttribute("userName") String userName,
                                @ModelAttribute("userEmail") String userEmail) {

        User user = userService.findByUserName(userName);
        if (user != null && Objects.equals(user.getUserEmail(), userEmail)) {
            //验证成功，将密码由邮件方法发送给对方
            //1.修改密码
            String password = RandomStringUtils.randomNumeric(8);
            userService.updatePassword(user.getId(), Md5Util.toMd5(password, "sens", 10));
            //2.发送邮件
            if (StringUtils.equals(SensConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                Map<String, Object> map = new HashMap<>(8);
                map.put("blogTitle", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                map.put("blogUrl", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                map.put("password", password);
                mailService.sendTemplateMail(
                        userEmail, SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "账户 - 找回密码", map, "common/mail_template/mail_forget.ftl");
            } else {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.smtp-not-enable"));
            }
        } else {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.forget.username-email-invalid"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.forget.password-send-mailbox"));
    }

    /**
     * 退出登录
     *
     * @return 重定向到/admin/login
     */
    @GetMapping(value = "/logOut")
    public String logOut() {
        User loginUser = getLoginUser();

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        log.info("用户[{}]退出登录", loginUser.getUserName());
        return "redirect:/admin/login";
    }
}
