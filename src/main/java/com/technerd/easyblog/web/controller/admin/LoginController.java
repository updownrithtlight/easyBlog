package com.technerd.easyblog.web.controller.admin;

import com.google.common.base.Strings;
import com.technerd.easyblog.utils.JwtUtil;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.entity.UserRoleRef;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.dto.SensConst;
import com.technerd.easyblog.model.enums.*;
import com.technerd.easyblog.model.vo.CommonEnum;
import com.technerd.easyblog.service.*;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.RedisUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import com.technerd.easyblog.web.reqVo.LoginUserVo;
import com.technerd.easyblog.web.reqVo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 用户登录
     *
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录")
    public JsonResult  login(@RequestBody LoginUserVo user, HttpServletResponse response) {
        String userName = user.getUserName();
        String password = user.getUserPass();
        if (org.springframework.util.StringUtils.isEmpty(userName) || org.springframework.util.StringUtils.isEmpty(password)) {
            return new JsonResult(CommonEnum.INVALID_CHARACTER.getCode(),"");
        }
        //查询是否有此用户
        User dbUser = userService.findByUserName(userName);
        if (dbUser == null) {
            return new JsonResult(CommonEnum.NO_SUCH_USER.getCode(),"");
        }
        //校验密码
        String o = (String)RedisUtil.get(userName);
        if (!encoder.matches(password, o)) {
            return new JsonResult(CommonEnum.PASSWORD_ERROR.getCode(),"");
        }
        //密码正确
        String token = jwtUtil.createToken(dbUser.getId()+"", dbUser.getUserName(),"admin");
        dbUser.setUserPass(null);
        //token放入redis
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return new JsonResult(CommonEnum.SUCCESS.getCode(), "");
    }


    /**
     * 验证注册信息
     *
     * @param user  用户
     * @return JsonResult JsonResult
     */
    @PostMapping(value = "/register")
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
        //2.检查邮箱
        User checkEmail = userService.findByEmail(user.getUserEmail());
        if (checkEmail != null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.user.user-email-exist"));
        }
        //3.创建用户
        user.setEmailEnable(TrueFalseEnum.FALSE.getValue());
        user.setLoginEnable(TrueFalseEnum.TRUE.getValue());
        user.setLoginError(0);
        //加密用户的密码
        String password = encoder.encode(user.getUserPass().trim());
        RedisUtil.set(user.getUserName(),password);
        System.out.println(password);
        user.setUserPass(password.trim());
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
     * 处理忘记密码
     *
     * @param userVo  用户
     * @return JsonResult
     */
    @PostMapping(value = "/forget")
    @ResponseBody
    @SystemLog(description = "忘记密码", type = LogTypeEnum.FORGET)
    @ApiOperation("处理忘记密码")
    public JsonResult forget(@RequestBody LoginUserVo userVo) {

       User  user = userService.findByUserName(userVo.getUserName());
        if (user != null && Objects.equals(user.getUserEmail(), userVo.getUserEmail())) {
            //验证成功，将密码由邮件方法发送给对方
            //1.修改密码
            String password = RandomStringUtils.randomNumeric(8);
            String encode = encoder.encode(user.getUserPass());
            userService.updatePassword(user.getId(), encode);
            //2.发送邮件
            if (StringUtils.equals(SensConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                Map<String, Object> map = new HashMap<>(8);
                map.put("blogTitle", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                map.put("blogUrl", SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                map.put("password", password);
                mailService.sendTemplateMail(
                        userVo.getUserEmail(), SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "账户 - 找回密码", map, "common/mail_template/mail_forget.ftl");
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
     * @return
     */
    @GetMapping(value = "/logOut")
    @ApiOperation(value = "退出登录")
    public JsonResult<Boolean> logout(HttpServletRequest request){
        /*
         * 清除redis中的RefreshToken即可
         */
        // TODO: 2021/4/28
        return new JsonResult<Boolean>(CommonEnum.SUCCESS.getCode(),"");
    }
}
