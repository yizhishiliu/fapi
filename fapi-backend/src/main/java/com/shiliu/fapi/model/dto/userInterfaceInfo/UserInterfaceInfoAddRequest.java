package com.shiliu.fapi.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建用户调用接口信息请求
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalTimes;

    /**
     * 剩余调用次数
     */
    private Integer remainingTimes;

    private static final long serialVersionUID = 1L;
}