package com.shiliu.fapiinterface.controller;

import com.shiliu.fapiinterface.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/yizhishiliu">一之十六</a>
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/")
    public String helloByGet(String name) {
        return "hello" + name;
    }

    @PostMapping("/")
    public String helloByPost(@RequestParam String name) {
        return "hello" + name;
    }

    @PostMapping("/")
    public String helloByPost2(@RequestBody User user) {
        return "hello" + user.getName();
    }
}
