package com.shiliu.fapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiliu.fapi.annotation.AuthCheck;
import com.shiliu.fapi.common.BaseResponse;
import com.shiliu.fapi.common.ErrorCode;
import com.shiliu.fapi.common.ResultUtils;
import com.shiliu.fapi.exception.BusinessException;
import com.shiliu.fapi.mapper.UserInterfaceInfoMapper;
import com.shiliu.fapi.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.shiliu.fapi.service.InterfaceInfoService;
import com.shiliu.fapicommon.model.entity.InterfaceInfo;
import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;
import com.shiliu.fapicommon.model.vo.InvokeInterfaceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析控制器
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取接口调用top3
     * @return
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InvokeInterfaceVO>> listTopInterfaceInvoke() {
        // 获取调用次数最多的接口调用信息
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listInvokeTopInterfaceInfo(3);
        // 根据接口信息id进行分组，方便后面关联查询接口信息
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList
                .stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        // 创建查询接口信息的条件构造器
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        // 设置id查询条件
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        // 查询接口信息
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        // 校验接口信息是否为空
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询接口信息失败");
        }
        // 将接口信息转换为调用信息
        List<InvokeInterfaceVO> invokeInterfaceVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InvokeInterfaceVO invokeInterfaceVO = new InvokeInterfaceVO();
            // 将接口信息复制到调用信息中
            BeanUtils.copyProperties(interfaceInfo, invokeInterfaceVO);
            // 获取接口信息id对应的调用次数
            int totalTimes = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalTimes();
            // 设置调用次数
            invokeInterfaceVO.setTotalTimes(totalTimes);
            // 返回调用信息VO对象
            return invokeInterfaceVO;
        }).collect(Collectors.toList());
        // 返回调用信息
        return ResultUtils.success(invokeInterfaceVOList);
    }
}
