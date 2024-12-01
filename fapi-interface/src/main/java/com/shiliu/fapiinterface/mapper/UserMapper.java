package com.shiliu.fapiinterface.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiliu.fapiinterface.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author <a href="https://github.com/yizhishiliu">一之十六</a>
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-12-01 13:51:15
* @Entity com.shiliu.fapiinterface.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




