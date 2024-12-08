package com.shiliu.fapi.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.constant.CommonConstant;
import com.shiliu.fapi.exception.ThrowUtils;
import com.shiliu.fapi.mapper.UserInterfaceInfoMapper;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.shiliu.fapi.model.entity.UserInterfaceInfo;
import com.shiliu.fapi.model.entity.User;
import com.shiliu.fapi.model.vo.UserInterfaceInfoVO;
import com.shiliu.fapi.model.vo.UserVO;
import com.shiliu.fapi.service.UserInterfaceInfoService;
import com.shiliu.fapi.service.UserService;
import com.shiliu.fapi.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户调用接口信息服务实现
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Service
@Slf4j
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param userInterfaceInfo
     * @param add               对创建的数据进行校验
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
        boolean updateResult = this.update(updateWrapper);
        return updateResult;
    }

    /**
     * 获取查询条件
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (userInterfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalTimes = userInterfaceInfoQueryRequest.getTotalTimes();
        Integer remainingTimes = userInterfaceInfoQueryRequest.getRemainingTimes();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(totalTimes), "totalTimes", totalTimes);
        queryWrapper.eq(ObjectUtils.isNotEmpty(interfaceInfoId), "interfaceInfoId", interfaceInfoId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(remainingTimes), "remainingTimes", remainingTimes);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取用户调用接口信息封装
     *
     * @param userInterfaceInfo
     * @param request
     * @return
     */
    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request) {
        // 对象转封装类
        UserInterfaceInfoVO userInterfaceInfoVO = UserInterfaceInfoVO.objToVo(userInterfaceInfo);

        // region 可选
        // 1. 关联查询用户信息
        Long userId = userInterfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        userInterfaceInfoVO.setUser(userVO);
        // endregion

        return userInterfaceInfoVO;
    }

    /**
     * 分页获取用户调用接口信息封装
     *
     * @param userInterfaceInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage, HttpServletRequest request) {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoPage.getRecords();
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>(userInterfaceInfoPage.getCurrent(), userInterfaceInfoPage.getSize(), userInterfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(userInterfaceInfoList)) {
            return userInterfaceInfoVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = userInterfaceInfoList.stream().map(userInterfaceInfo -> {
            return UserInterfaceInfoVO.objToVo(userInterfaceInfo);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userInterfaceInfoList.stream().map(UserInterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        userInterfaceInfoVOList.forEach(userInterfaceInfoVO -> {
            Long userId = userInterfaceInfoVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            userInterfaceInfoVO.setUser(userService.getUserVO(user));
        });
        // endregion

        userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
        return userInterfaceInfoVOPage;
    }

}
