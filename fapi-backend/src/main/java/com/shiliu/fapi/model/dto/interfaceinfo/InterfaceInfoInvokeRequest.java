package com.shiliu.fapi.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口测试调用请求
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}