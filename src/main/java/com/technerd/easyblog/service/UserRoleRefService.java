package com.technerd.easyblog.service;

import com.technerd.easyblog.common.base.BaseService;
import com.technerd.easyblog.entity.UserRoleRef;

/**
 * @author 言曌
 * @date 2019/1/25 下午8:07
 */

public interface UserRoleRefService extends BaseService<UserRoleRef, Long> {

    /**
     * 根据用户Id删除
     *
     * @param userId 用户Id
     */
    void deleteByUserId(Long userId);


}
