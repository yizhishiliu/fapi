package com.shiliu.fapicommon.service;

import com.shiliu.fapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface InnerUserService {

    /**
     * 根据accessKey查询是否已分配密钥
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
