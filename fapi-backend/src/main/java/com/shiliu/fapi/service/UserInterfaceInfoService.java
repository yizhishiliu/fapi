package com.shiliu.fapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.shiliu.fapi.model.entity.UserInterfaceInfo;
import com.shiliu.fapi.model.vo.UserInterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户调用接口信息服务
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验数据
     *
     * @param userInterfaceInfo
     * @param add 对创建的数据进行校验
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);

    /**
     * 获取查询条件
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);
    
    /**
     * 获取用户调用接口信息封装
     *
     * @param userInterfaceInfo
     * @param request
     * @return
     */
    UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request);

    /**
     * 分页获取用户调用接口信息封装
     *
     * @param userInterfaceInfoPage
     * @param request
     * @return
     */
    Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage, HttpServletRequest request);
}
