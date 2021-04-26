package com.technerd.easyblog.config.interceptor;

import com.technerd.easyblog.config.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: tensquare61
 * @description:
 **/
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return super.preHandle(request, response, handler);
        String header = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(header) && header.startsWith("Bearer ")){
            String token = header.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            if(claims!=null){
                String role = (String) claims.get("role");
                if("admin".equals(role)){
                    request.setAttribute("admin_claims", claims);
                }
                if("user".equals(role)){
                    request.setAttribute("user_claims", claims);
                }
            }
        }
        return true;
    }
}
