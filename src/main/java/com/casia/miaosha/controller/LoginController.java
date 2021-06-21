package com.casia.miaosha.controller;

import com.casia.miaosha.domain.MiaoshaUser;
import com.casia.miaosha.result.Result;
import com.casia.miaosha.service.MiaoshaUserService;
import com.casia.miaosha.service.UserService;
import com.casia.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//        log.info(loginVo.toString());
        miaoshaUserService.login(response, loginVo);
        return Result.success(true);
    }
}
