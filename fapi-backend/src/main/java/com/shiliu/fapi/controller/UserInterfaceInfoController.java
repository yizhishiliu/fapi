package com.shiliu.fapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiliu.fapi.annotation.AuthCheck;
import com.shiliu.fapi.common.BaseResponse;
import com.shiliu.fapi.common.DeleteRequest;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.common.ResultUtils;
import com.shiliu.fapi.constant.UserConstant;
import com.shiliu.fapi.exception.BusinessException;
import com.shiliu.fapi.exception.ThrowUtils;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoEditRequest;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.shiliu.fapi.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.shiliu.fapi.model.entity.UserInterfaceInfo;
import com.shiliu.fapi.model.entity.User;
import com.shiliu.fapi.model.vo.UserInterfaceInfoVO;
import com.shiliu.fapi.service.UserInterfaceInfoService;
import com.shiliu.fapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户调用接口信息接口
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建用户调用接口信息
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userInterfaceInfoAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 数据校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除用户调用接口信息
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userInterfaceInfoService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户调用接口信息（仅管理员可用）
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 数据校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        // 判断是否存在
        long id = userInterfaceInfoUpdateRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户调用接口信息（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfoVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVO(userInterfaceInfo, request));
    }

    /**
     * 分页获取用户调用接口信息列表（仅管理员可用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        // 查询数据库
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        return ResultUtils.success(userInterfaceInfoPage);
    }

    /**
     * 分页获取用户调用接口信息列表（封装类）
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfoVO>> listUserInterfaceInfoVOByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest,
                                                               HttpServletRequest request) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVOPage(userInterfaceInfoPage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户调用接口信息列表
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfoVO>> listMyUserInterfaceInfoVOByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(userInterfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceInfoVOPage(userInterfaceInfoPage, request));
    }

    /**
     * 编辑用户调用接口信息（给用户使用）
     *
     * @param userInterfaceInfoEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> editUserInterfaceInfo(@RequestBody UserInterfaceInfoEditRequest userInterfaceInfoEditRequest, HttpServletRequest request) {
        if (userInterfaceInfoEditRequest == null || userInterfaceInfoEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoEditRequest, userInterfaceInfo);
        // 数据校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = userInterfaceInfoEditRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
