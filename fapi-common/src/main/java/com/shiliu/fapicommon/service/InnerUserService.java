package com.shiliu.fapicommon.service;

import com.shiliu.fapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface InnerUserService {

    /**
     * 根据accessKey和secretKey查询是否已分配密钥（accessKey和secretKey）
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    User getInvokeUser(String accessKey, String secretKey);
}
