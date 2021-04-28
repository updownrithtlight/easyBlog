package com.technerd.easyblog.utils;

import io.jsonwebtoken.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

/**
 * @program: tensquare61
 * @description: 创建token 解析token
 **/
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    private long ttl;

    private String secretKey;

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 当用户/管理员登陆成功后根据用户信息创建jwt
     * @param id
     * @param name
     * @param role
     * @return
     */
    public String createToken(String id, String name, String role){
        long now = System.currentTimeMillis();
        long timeout = now+ttl;
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(id) //jwt的唯一身份标识
                .setSubject(name) //jwt所面向的用户
                .setIssuer("technerd") //签发者
                .setIssuedAt(new Date(now))//设置签发日期
                .setExpiration(new Date(timeout))  //有效
                .claim("role", role)//自定义信息
                .signWith(SignatureAlgorithm.HS256, secretKey);//指定签名 以及加密方式
       return jwtBuilder.compact();
    }

    /**
     * 验证token是否有效
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        Claims claims = null;//获取载荷信息
        try {
            claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
