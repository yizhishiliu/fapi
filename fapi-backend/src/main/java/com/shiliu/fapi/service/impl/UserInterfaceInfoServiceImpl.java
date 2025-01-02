package com.shiliu.fapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.exception.ThrowUtils;
import com.shiliu.fapi.mapper.UserInterfaceInfoMapper;
import com.shiliu.fapi.service.UserInterfaceInfoService;
import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户调用接口信息服务实现
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Service
@Slf4j
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    /**
     * 校验数据
     *
     * @param userInterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer remainingTimes = userInterfaceInfo.getRemainingTimes();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(id <= 0 || userId <= 0 || interfaceInfoId <= 0, ErrorCode.PARAMS_ERROR, "接口或用户不存在！");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        ThrowUtils.throwIf(remainingTimes < 0, ErrorCode.PARAMS_ERROR, "剩余接口调用次数不能小于0！");
    }

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(Long interfaceInfoId, Long userId) {
        // 校验
        ThrowUtils.throwIf(interfaceInfoId <= 0 || userId <= 0, ErrorCode.PARAMS_ERROR, "接口或用户不存在！");
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("totalTimes = totalTimes + 1, remainingTimes = remainingTimes - 1");
        return this.update(updateWrapper);
    }
}
