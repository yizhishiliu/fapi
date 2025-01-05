package com.shiliu.fapicommon.model.vo;

import com.shiliu.fapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口调用统计VO
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InvokeInterfaceVO extends InterfaceInfo {

    /**
     * 接口调用次数
     */
    private Integer totalCount;

    private static final long serialVersionUID = 1L;
}
