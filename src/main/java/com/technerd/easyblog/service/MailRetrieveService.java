package com.technerd.easyblog.service;

import com.technerd.easyblog.common.base.BaseService;
import com.technerd.easyblog.entity.MailRetrieve;

/**
 * @author 言曌
 * @date 2019/1/30 上午11:20
 */

public interface MailRetrieveService extends BaseService<MailRetrieve, Long> {

    /**
     * 根据用户Id查询单个邮件记录
     *
     * @param userId 用户Id
     * @return 记录
     */
    MailRetrieve findLatestByUserId(Long userId);

    /**
     * 根据邮箱查询单个邮件记录
     *
     * @param email 邮箱
     * @return 记录
     */
    MailRetrieve findLatestByEmail(String email);

}
