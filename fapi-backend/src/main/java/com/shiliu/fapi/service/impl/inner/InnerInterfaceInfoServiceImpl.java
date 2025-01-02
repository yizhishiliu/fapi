package com.shiliu.fapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.exception.BusinessException;
import com.shiliu.fapi.mapper.InterfaceInfoMapper;
import com.shiliu.fapicommon.model.entity.InterfaceInfo;
import com.shiliu.fapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 根据接口路径和请求方式查询接口是否存在
     *
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url)
                .eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
