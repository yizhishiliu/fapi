package com.shiliu.fapicommon.service;

import com.shiliu.fapicommon.model.entity.InterfaceInfo;

/**
 * 接口信息服务
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface InnerInterfaceInfoService {

    /**
     * 根据接口路径和请求方式查询接口是否存在
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
