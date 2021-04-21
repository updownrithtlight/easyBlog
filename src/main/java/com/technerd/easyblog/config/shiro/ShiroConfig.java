//package com.technerd.easyblog.config.shiro;
//
//import com.technerd.easyblog.config.filter.JwtFilter;
//import com.technerd.easyblog.config.properties.IgnoredUrlsProperties;
//import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
//import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
//import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
//import org.apache.shiro.mgt.DefaultSubjectDAO;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.realm.Realm;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.Filter;
//import java.util.*;
//
///**
// * @author 言曌
// * @date 2018/8/20 上午6:19
// */
//
//@Configuration
//public class ShiroConfig {
//
//    @Bean
//    IgnoredUrlsProperties getIgnoredUrlsProperties() {
//        return new IgnoredUrlsProperties();
//    }
//
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        //拦截器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/login/**", "anon");
//        //前后端带login登录的或者其他登录的通通放行
//        filterChainDefinitionMap.put("/**/login/**", "anon");
//        filterChainDefinitionMap.put("/**.js", "anon");
//        filterChainDefinitionMap.put("/druid/**", "anon");
//        filterChainDefinitionMap.put("/swagger**/**", "anon");
//        filterChainDefinitionMap.put("/**/swagger**/**", "anon");
//        filterChainDefinitionMap.put("/webjars/**", "anon");
//        filterChainDefinitionMap.put("/v2/**", "anon");
//        // 添加自己的过滤器并且取名为jwt
//        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
//        filterMap.put("jwt", new JwtFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);
//        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
//        filterChainDefinitionMap.put("/**", "jwt");
//        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
////        securityManager.setAuthenticator(modularRealmAuthenticator());
//        List<Realm> realms = new ArrayList<>();
//        //密码登录realm
//        realms.add(normalRealm());
//        //免密登录realm
////        realms.add(freeRealm());
//        securityManager.setRealms(realms);
//        /*
//         * 关闭shiro自带的session，详情见文档
//         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
//         */
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        securityManager.setSubjectDAO(subjectDAO);
//        return securityManager;
//    }
//
////    /**
////     * 系统自带的Realm管理，主要针对多realm
////     */
////    @Bean
////    public ModularRealmAuthenticator modularRealmAuthenticator() {
////        //自己重写的ModularRealmAuthenticator
////        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
////        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
////        return modularRealmAuthenticator;
////    }
//
//    /**
//     * 需要密码登录的realm
//     *
//     * @return MyShiroRealm
//     */
//    @Bean
//    public NormalRealm normalRealm() {
//        NormalRealm normalRealm = new NormalRealm();
//        normalRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return normalRealm;
//    }
//
//
////    /**
////     * 免密登录realm
////     *
////     * @return MyShiroRealm
////     */
////    @Bean
////    public FreeRealm freeRealm() {
////        FreeRealm realm = new FreeRealm();
////        //不验证账号密码
////        realm.setCredentialsMatcher(allowAllCredentialsMatcher());
////        return realm;
////    }
//
//    /**
//     * 访问 权限 拦截器
//     *
//     * @return
//     */
////    public URLPathMatchingFilter getURLPathMatchingFilter() {
////        return new URLPathMatchingFilter();
////    }
//
//    /**
//     * MD5加盐加密十次
//     *
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        //散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");
//        //散列的次数，md5("")
//        hashedCredentialsMatcher.setHashIterations(10);
//        return hashedCredentialsMatcher;
//    }
//
//    @Bean
//    public AllowAllCredentialsMatcher allowAllCredentialsMatcher() {
//        return new AllowAllCredentialsMatcher();
//    }
//
//}