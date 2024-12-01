package com.shiliu.fapiinterface.controller;

import com.shiliu.fapiinterface.common.ErrorCode;
import com.shiliu.fapiinterface.exception.BusinessException;
import com.shiliu.fapiinterface.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.shiliu.fapiinterface.model.User;

import static com.shiliu.fapiinterface.utils.SignUtil.genSign;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private UserService userService;

    @GetMapping("/")
    public String helloByGet(String name) {
        return "hello" + name;
    }

    @PostMapping("/")
    public String helloByPost(@RequestParam String name) {
        return "hello" + name;
    }

    @PostMapping("/user")
    public String helloByPost2(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");

        // 查询数据库，验证accessKey是否已分配
        User accessUser = userService.checkAccessKey(accessKey);
        if (accessUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        // 验证nonce是否已使用
        // todo redis存储使用过的nonce，验证是否已使用(这里简单校验一下小于4位数）
        if (Long.parseLong(nonce) > 10000) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        // 验证timestamp是否过期（不超过当前时间5分钟）
        if (Long.parseLong(timestamp) > System.currentTimeMillis() + 5 * 60 * 1000) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        // 验证sign是否正确
        // 从数据库中查询accessKey对应的secretKey
        String secretKey = accessUser.getSecretKey();
        String gennedSign = genSign(body, secretKey);
        if (!sign.equals(gennedSign)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }
        return "hello" + user.getUserName();
    }
}
