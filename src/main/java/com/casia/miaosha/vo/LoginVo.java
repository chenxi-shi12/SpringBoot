package com.casia.miaosha.vo;

import com.casia.miaosha.validator.IsMobile;

import javax.validation.constraints.NotNull;

public class LoginVo {
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';

    }
}