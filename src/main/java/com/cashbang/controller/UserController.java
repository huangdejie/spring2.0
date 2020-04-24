package com.cashbang.controller;

import com.cashbang.annotations.DJAutowired;
import com.cashbang.annotations.DJController;
import com.cashbang.annotations.DJRequestMapping;
import com.cashbang.annotations.DJRequestParam;
import com.cashbang.service.UserService;
import com.cashbang.servlet.DJModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
@DJController
@DJRequestMapping("/user")
public class UserController {

    @DJAutowired
    private UserService userService;

    @DJRequestMapping("/first.html")
    public DJModelAndView first(HttpServletRequest request,HttpServletResponse response,
                                   @DJRequestParam("name") String name){
        String userName = userService.sayHello(name);
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("teacher",userName);
        model.put("data",userName+"ssss");
        model.put("token","66666666666666666666");
        DJModelAndView djModelAndView = new DJModelAndView("first.html",model);

        return djModelAndView;
    }

    @DJRequestMapping("syaHello")
    public DJModelAndView sayHello(HttpServletRequest request,HttpServletResponse response,@DJRequestParam("userName") String userName){
        out(response,"你好啊:"+userName);
        return null;
    }

    private DJModelAndView out(HttpServletResponse resp,String str){
        try {
            resp.setContentType("text/html;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
