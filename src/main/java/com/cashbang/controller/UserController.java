package com.cashbang.controller;

import com.cashbang.annotations.DJAutowired;
import com.cashbang.annotations.DJController;
import com.cashbang.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
@DJController
public class UserController {

    @DJAutowired
    private UserService userService;

    public String sayHello(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           String name){
        return null;
    }

}
