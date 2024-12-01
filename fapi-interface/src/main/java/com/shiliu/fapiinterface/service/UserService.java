package com.shiliu.fapiinterface.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiliu.fapiinterface.model.User;

/**
* @author <a href="https://github.com/yizhishiliu">一之十六</a>
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-12-01 13:51:15
*/
public interface UserService extends IService<User> {

    User checkAccessKey(String accessKey);
}
