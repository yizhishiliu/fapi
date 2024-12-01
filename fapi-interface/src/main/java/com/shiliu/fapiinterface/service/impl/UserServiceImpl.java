package com.shiliu.fapiinterface.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiliu.fapiinterface.service.UserService;
import com.shiliu.fapiinterface.model.User;
import com.shiliu.fapiinterface.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-12-01 13:51:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 根据accessKey查询是否已分配给用户
     *
     * @param accessKey
     * @return
     */
    @Override
    public User checkAccessKey(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            return null;
        }
        // 查询是否已分配给用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return this.baseMapper.selectOne(queryWrapper);
    }
}




