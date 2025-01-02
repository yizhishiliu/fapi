package com.shiliu.fapi.service;

import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface UserInterfaceInfoService {

    /**
     * 校验数据
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);
}
