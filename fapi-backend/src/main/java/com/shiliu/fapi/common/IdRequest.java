package com.shiliu.fapi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用Id请求
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}