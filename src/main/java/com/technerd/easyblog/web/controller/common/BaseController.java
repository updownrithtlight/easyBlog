package com.technerd.easyblog.web.controller.common;

import com.technerd.easyblog.common.base.BaseEntity;
import com.technerd.easyblog.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <pre>
 *     Controller抽象类
 * </pre>
 *
 * @author : technerd
 */
public abstract class BaseController<T extends BaseEntity> {

    @Autowired
    private HttpServletRequest request;

    public long getUserId(){
        Claims claims = getClaims();
        return Long.parseLong(claims.getId());
    }

    private Claims getClaims() {
        Claims claims = null;
        claims = (Claims)request.getAttribute("admin_claims");
        if(claims==null){
            claims= (Claims)request.getAttribute("user_claims");
        }
        return claims;
    }

    public boolean isAdmin(){
        return "admin".equals(getClaims().get("role").toString());

    }

    public void save(T t){
        Date date = new Date();
        if(t.getId()==null){
            t.setCreateTime(date);
            t.setCreateBy(getUserId()+"");
        }else {
            t.setUpdateTime(date);
            t.setUpdateBy(getUserId()+"");
        }
    }

}
