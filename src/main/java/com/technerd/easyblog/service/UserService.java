package com.technerd.easyblog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.common.base.BaseService;
import com.technerd.easyblog.entity.User;

import java.util.List;

/**
 * <pre>
 *     用户业务逻辑接口
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
public interface UserService  {

    /**
     * 根据用户名获得用户
     *
     * @param userName 用户名
     * @return 用户
     */
    User findByUserName(String userName);


    /**
     * 根据邮箱查找用户
     *
     * @param userEmail 邮箱
     * @return User
     */
    User findByEmail(String userEmail);

    /**
     * 更新密码
     *
     * @param userId 用户Id
     * @param password 密码
     */
    void updatePassword(Long userId, String password);

    /**
     *
     * @param user
     */
    User insert(User user) ;

    /**
     *
     * @param user
     * @return
     */
    User update(User user);

    /**
     *
     * @param id
     * @return
     */
    User get(Long id);

    /**
     *
     * @param entity
     * @return
     */
    User insertOrUpdate(User entity);

    /**
     *
     * @param userId
     */
    void delete(Long userId);

}
