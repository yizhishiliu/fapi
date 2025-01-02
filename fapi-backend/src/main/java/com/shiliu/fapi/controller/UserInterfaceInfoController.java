package com.shiliu.fapi.controller;

import com.shiliu.fapi.service.UserInterfaceInfoService;
import com.shiliu.fapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户调用接口信息接口
 *
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

}
