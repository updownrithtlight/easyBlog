package com.technerd.easyblog.model.vo;

/**
 * 常见的常数
 *
 * @author 枫叶
 * @date 2020/11/1
 */
@SuppressWarnings("unused")
public interface CommonConstant {
    /**
     * 成功的请求。
     */
    int SUCCESS = 2000;
    /**
     * 请求错误
     */
    int BAD_REQUEST = 4000;
    /**
     * 非法的参数
     */
    int INVALID_CHARACTER = 4003;
    /**
     * 资源未找到
     */
    int NOTFOUND = 4004;
    /**
     * 请求方式错误
     */
    int ERROR_METHOD = 4005;
    /**
     * 没有令牌
     */
    int NO_TOKEN = 4006;
    /**
     * 令牌过期
     */
    int  TOKEN_EXPIRED = 4007;
    /**
     * 错误的令牌
     */
    int  TOKEN_ERROR = 4008;
    /**
     * 插入失败
     */
    int INVALID_INSERT = 4010;
    /**
     * 查询失败
     */
    int INVALID_SELECT = 4011;
    /**
     * 删除失败
     */
    int INVALID_DELETE = 4012;
    /**
     * 更新失败
     */
    int INVALID_UPDATE = 4013;
    /**
     * 不支持的媒体格式
     */
    int ERROR_FORMAT = 4015;
    /**
     * 用户已被注册
     */
    int USER_EXIST = 4020;
    /**
     * 用户不存在
     */
    int NO_SUCH_USER = 4021;
    /**
     * 密码错误
     */
    int PASSWORD_ERROR = 4022;
    /**
     * 未知异常
     */
    int UNKNOWN_EXCEPTION = 5000;

    /**
     * 获取状态码
     * @return 状态码
     */
    int getCode();

    /**
     * 获取消息
     * @return 描述消息
     */
    String getMsg();
}
