package com.cashbang.servlet;

import com.cashbang.annotations.DJController;
import com.cashbang.annotations.DJRequestMapping;
import com.cashbang.config.BeanWrapper;
import com.cashbang.config.DJApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        for (Map.Entry<String,BeanWrapper> entry : context.getBeanWrapper().entrySet()) {
            BeanWrapper beanWrapper = entry.getValue();
            if(!beanWrapper.getWrapperClass().isAnnotationPresent(DJController.class) &&
                    !beanWrapper.getWrapperClass().isAnnotationPresent(DJRequestMapping.class)){
                continue;
            }
            DJRequestMapping requestMapping = (DJRequestMapping) beanWrapper.getWrapperClass().getAnnotation(DJRequestMapping.class);
            String url = ("\\"+requestMapping.value()).replaceAll("\\+","\\");
            Method[] methods = beanWrapper.getWrapperClass().getMethods();
            for (Method method : methods) {
                if(!method.isAnnotationPresent(DJRequestMapping.class)){
                    continue;
                }
                DJRequestMapping methodRequestMapping = method.getAnnotation(DJRequestMapping.class);
                String finalUrl = url+("\\"+methodRequestMapping.value()).replaceAll("\\+","\\");
                Pattern pattern = Pattern.compile(finalUrl.replaceAll("\\*",".*"));
                DJHandlerMapping handlerMapping = new DJHandlerMapping(pattern,beanWrapper,method);
                handlerMappins.add(handlerMapping);

            }
        }

    }


}
