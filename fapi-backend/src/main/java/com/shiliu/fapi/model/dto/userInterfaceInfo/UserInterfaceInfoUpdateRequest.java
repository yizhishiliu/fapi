package com.shiliu.fapi.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户调用接口信息请求
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalTimes;

    /**
     * 剩余调用次数
     */
    private Integer remainingTimes;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}