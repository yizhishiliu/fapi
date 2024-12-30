package com.shiliu.fapicommon.model.vo;

import com.shiliu.fapicommon.model.entity.UserInterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户调用接口信息视图
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Data
public class UserInterfaceInfoVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param userInterfaceInfoVO
     * @return
     */
    public static UserInterfaceInfo voToObj(UserInterfaceInfoVO userInterfaceInfoVO) {
        if (userInterfaceInfoVO == null) {
            return null;
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoVO, userInterfaceInfo);
        return userInterfaceInfo;
    }

    /**
     * 对象转封装类
     *
     * @param userInterfaceInfo
     * @return
     */
    public static UserInterfaceInfoVO objToVo(UserInterfaceInfo userInterfaceInfo) {
        if (userInterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
        return userInterfaceInfoVO;
    }
}
