package com.technerd.easyblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * JWT工具类
 *
 * @author 枫叶
 * @date 2020/11/1
 */
@Slf4j
@SuppressWarnings("unused")
public class JwtUtil {

    /**
     * token到期时间，毫秒为单位
     */
    public static long EXPIRE_TIME;
    /**
     * RefreshToken到期时间为，秒为单位
     */
    public static long REFRESH_EXPIRE_TIME;
    /**
     * 密钥盐
     */
    private static String TOKEN_SECRET;

    /**
     * 设置token过期时间及密钥盐
     *
     * @param expireTime        客户端token过期时间
     * @param refreshExpireTime 服务器token过期时间
     * @param tokenSecret       token加密使用的盐值
     */
    public static void setProperties(long expireTime, long refreshExpireTime, String tokenSecret) {
        JwtUtil.EXPIRE_TIME = expireTime;
        JwtUtil.REFRESH_EXPIRE_TIME = refreshExpireTime;
        JwtUtil.TOKEN_SECRET = tokenSecret;
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        //创建token验证器
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        DecodedJWT decodedjwt = jwtVerifier.verify(token);
        log.info("认证通过");
        log.info("userId: [{}]", decodedjwt.getClaim("userId").asLong());
        log.info("过期时间：      [{}]", decodedjwt.getExpiresAt());
        return true;
    }

    /**
     * 获得token中的信息
     *
     * @return token中包含的用户id
     */
    public static long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return -1L;
        }
    }

    /**
     * 获取currentTime
     * @param token 密钥
     * @return currentTime
     */
    public static Long getCurrentTime(String token){
        try{
            DecodedJWT decodedjwt =JWT.decode(token);
            return decodedjwt .getClaim("currentTime").asLong();
        }catch (JWTCreationException e){
            return null;
        }
    }
    /**
     * 根据request中的token获取currentTime
     *
     * @param request request
     * @return CurrentTime
     */
    public static Long getCurrentTime(HttpServletRequest request) {
        String accessToken = request.getHeader("token");
        Long currentTime = getCurrentTime(accessToken);
        if (currentTime == null) {
            throw new RuntimeException("未获取到currentTime");
        }
        return currentTime;
    }

    /**
     * 生成签名,5min后过期
     *
     * @param userId 用户名
     * @return 加密的token
     */
    public static String sign(Long userId, Long currentTime) {
        String token = null;
        try {
            Date expireAt = new Date(currentTime + EXPIRE_TIME);
            token = JWT.create()
                    //存放数据
                    .withClaim("userId", userId)
                    .withClaim("currentTime", currentTime)
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return token;
    }


    /**
     * 根据request中的token获取账号
     *
     * @param request request
     * @return 用户的账号
     */
    public static Long getUserId(HttpServletRequest request) {
        String accessToken = request.getHeader("token");
        return getUserId(accessToken);
    }
}
