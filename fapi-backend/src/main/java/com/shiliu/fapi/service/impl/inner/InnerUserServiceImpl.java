package com.shiliu.fapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.exception.BusinessException;
import com.shiliu.fapi.mapper.UserMapper;
import com.shiliu.fapicommon.model.entity.User;
import com.shiliu.fapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据accessKey和secretKey查询是否已分配密钥（accessKey和secretKey）
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey, String secretKey) {
        if (StringUtils.isAnyBlank(accessKey, secretKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey)
                .eq("secretKey", secretKey);
        return userMapper.selectOne(queryWrapper);
    }
}
