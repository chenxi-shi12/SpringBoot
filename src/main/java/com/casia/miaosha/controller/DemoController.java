package com.casia.miaosha.controller;

import com.casia.miaosha.domain.User;
import com.casia.miaosha.redis.RedisService;
import com.casia.miaosha.redis.UserKey;
import com.casia.miaosha.result.CodeMsg;
import com.casia.miaosha.result.Result;
import com.casia.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("")
    @ResponseBody
    String home() {
        return "Hello World";
    }

    @RequestMapping("/test")
    @ResponseBody
    Result<String> test() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("age", "100");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/insert")
    @ResponseBody
    public Result<Boolean> dbTest() {
        userService.test();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet() {
        Long v1 = redisService.get(UserKey.getById, "key1", Long.class);
        return Result.success(v1);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("2");
        boolean v1 = redisService.set(UserKey.getById, "key2", user);
        return Result.success(v1);
    }
}
