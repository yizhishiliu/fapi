package com.shiliu.fapi.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.constant.CommonConstant;
import com.shiliu.fapi.exception.ThrowUtils;
import com.shiliu.fapi.mapper.InterfaceInfoMapper;
import com.shiliu.fapi.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.shiliu.fapi.service.InterfaceInfoService;
import com.shiliu.fapi.service.UserService;
import com.shiliu.fapi.utils.SqlUtils;
import com.shiliu.fapicommon.model.entity.InterfaceInfo;
import com.shiliu.fapicommon.model.entity.User;
import com.shiliu.fapicommon.model.vo.InterfaceInfoVO;
import com.shiliu.fapicommon.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 接口信息服务实现
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Service
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param interfaceInfo
     * @param add           对创建的数据进行校验
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, method), ErrorCode.PARAMS_ERROR);
        }

        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(name)) {
            ThrowUtils.throwIf(name.length() > 30, ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(description)) {
            ThrowUtils.throwIf(name.length() > 100, ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取接口信息封装
     *
     * @param interfaceInfo
     * @param request
     * @return
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        // 对象转封装类
        InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);

        // 可以根据需要为封装对象补充值
        // region 可选
        // 1. 关联查询用户信息
        Long userId = interfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceInfoVO.setUser(userVO);
        // endregion

        return interfaceInfoVO;
    }

    /**
     * 分页获取接口信息封装
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        // 对象列表 => 封装对象列表
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(InterfaceInfoVO::objToVo).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值
        // region 可选
        // 关联查询用户信息
        Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        interfaceInfoVOList.forEach(interfaceInfoVO -> {
            Long userId = interfaceInfoVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            interfaceInfoVO.setUser(userService.getUserVO(user));
        });
        // endregion

        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }
}
