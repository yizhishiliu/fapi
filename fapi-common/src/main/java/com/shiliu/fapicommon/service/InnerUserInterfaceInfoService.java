package com.shiliu.fapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;

/**
 * 用户调用接口信息服务
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);
}
