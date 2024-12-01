package com.shiliu.fapiinterface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiliu.fapiinterface.service.UserService;
import com.shiliu.fapiinterface.model.User;
import com.shiliu.fapiinterface.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author <a href="https://github.com/yizhishiliu">一之十六</a>
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-12-01 13:51:15
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




