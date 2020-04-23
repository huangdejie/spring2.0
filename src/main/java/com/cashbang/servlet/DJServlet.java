package com.cashbang.servlet;

import com.cashbang.config.DJApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class DJServlet extends HttpServlet {

    private List<DJHandlerMapping> handlerMappins = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String configFileName = config.getInitParameter("configurations");
        DJApplicationContext applicationContext = new DJApplicationContext(configFileName);
        initStrategies(applicationContext);
    }

    /**
     * 初始化组件
     * @param context
     */
    private void initStrategies(DJApplicationContext context){
        initHandlerMapping(context);
    }

    /**
     * 初始化映射关系
     * @param context
     */
    private void initHandlerMapping(DJApplicationContext context) {



    }
}
