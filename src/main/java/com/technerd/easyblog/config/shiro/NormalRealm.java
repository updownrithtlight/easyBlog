package com.technerd.easyblog.config.shiro;

import com.technerd.easyblog.entity.Permission;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.jwt.JwtToken;
import com.technerd.easyblog.service.PermissionService;
import com.technerd.easyblog.service.RoleService;
import com.technerd.easyblog.service.UserService;
import com.technerd.easyblog.utils.JwtUtil;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的realm
 *
 * @author 言曌
 * @date 2018/9/1 上午10:47
 */
@Slf4j
public class NormalRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private RoleService roleService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        Long userId = null;
        try {
            //这里工具类没有处理空指针等异常这里处理一下(这里处理科学一些)
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            throw new AuthenticationException("heard的token拼写错误或者值为空");
        }
        if (userId == null) {
            log.error("token无效(空''或者null都不行!)");
            throw new AuthenticationException("token无效");
        }
        User userBean = userService.get(userId);
        if (userBean == null) {
            log.error("用户不存在!)");
            throw new AuthenticationException("用户不存在!");
        }
        if (!JwtUtil.verify(token)) {
            log.error("用户名或密码错误(token无效或者与登录者不匹配)!)");
            throw new AuthenticationException("用户名或密码错误(token无效或者与登录者不匹配)!");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();

        List<Role> roles = roleService.listRolesByUserId(user.getId());
        for (Role role : roles) {
            authorizationInfo.addRole(role.getRole());
            List<Permission> permissions = permissionService.listPermissionsByRoleId(role.getId());
            //把权限的URL全部放到authorizationInfo中去
            Set<String> urls = permissions.stream().map(p -> p.getUrl()).collect(Collectors.toSet());
            authorizationInfo.addStringPermissions(urls);

        }
        return authorizationInfo;
    }


    /**
     * 必须重写此方法，不然Shiro会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


}
