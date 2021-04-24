package com.technerd.easyblog.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
