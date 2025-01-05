package com.shiliu.fapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @author ada
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
 * @createDate 2024-12-08 15:30:42
 * @Entity generator.domain.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listInvokeTopInterfaceInfo(int limit);
}




