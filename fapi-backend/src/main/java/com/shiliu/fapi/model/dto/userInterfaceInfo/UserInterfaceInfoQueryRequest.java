package com.shiliu.fapi.model.dto.userInterfaceInfo;

import com.shiliu.fapi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询用户调用接口信息请求
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}