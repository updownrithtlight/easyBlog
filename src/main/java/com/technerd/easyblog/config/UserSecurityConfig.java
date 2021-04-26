package com.technerd.easyblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @program: tensquare61
 * @description: security框架配置类-将一些请求放行
 **/
@Configuration
public class UserSecurityConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()  //放行所有请求
                .and().csrf().disable();
//        super.configure(http);
    }

}
