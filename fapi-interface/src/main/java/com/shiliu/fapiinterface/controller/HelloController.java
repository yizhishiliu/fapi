package com.shiliu.fapiinterface.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.shiliu.fapiinterface.model.User;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/")
    public String helloByGet(String name) {
        return "hello" + name;
    }

//    @PostMapping("/")
//    public String helloByPost(@RequestParam String name) {
//        return "hello" + name;
//    }

    @PostMapping("/")
    public String helloByPost2(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String secretKey = request.getHeader("secretKey");
        return "hello" + user.getUserName();
    }
}
