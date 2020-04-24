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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.io.File;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class DJServlet extends HttpServlet {

    /**
     * 请求路径和方法关联
     */
    private List<DJHandlerMapping> handlerMappings = new ArrayList<DJHandlerMapping>();

    private Map<DJHandlerMapping,DJHandlerAdapter> handlerAdapters = new HashMap<DJHandlerMapping,DJHandlerAdapter>();

    private List<DJViewResolver> viewResolvers = new ArrayList<DJViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        DJHandlerMapping handlerMapping = getHandlerMapping(req);
        if(handlerMapping == null){
            processDispatcherResult(req,resp,new DJModelAndView("404"));
        }
        DJHandlerAdapter handlerAdapter = handlerAdapters.get(handlerMapping);
        DJModelAndView modelAndView = handlerAdapter.handler(req,resp,handlerMapping);
        processDispatcherResult(req,resp,modelAndView);

    }

    private void processDispatcherResult(HttpServletRequest req, HttpServletResponse resp, DJModelAndView modelAndView) {
        if(modelAndView == null){
            return;
        }
        if(viewResolvers.isEmpty()){
            return;
        }

        for (DJViewResolver viewResolver:viewResolvers){
            DJView view = viewResolver.resolveViewName(modelAndView.getViewName());
            try {
                view.render(modelAndView.getModel(),req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

    }


    private DJHandlerMapping getHandlerMapping(HttpServletRequest req) {
        if(handlerMappings.isEmpty()){
            return null;
        }
        String url = req.getRequestURI();
        System.out.println("*******请求的路径为"+url);
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");
        for (DJHandlerMapping handlerMapping : handlerMappings) {
            if(!handlerMapping.getPattern().matcher(url).matches()){
                continue;
            }
            return handlerMapping;
        }
        return null;
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
        initHandlerAdapter();
        initViewResolvers(context);
    }

    private void initViewResolvers(DJApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new DJViewResolver(templateRoot));
        }
    }

    /**
     * 初始化handlerAdapter
     */
    private void initHandlerAdapter() {
        for (DJHandlerMapping handlerMapping : handlerMappings) {
            handlerAdapters.put(handlerMapping,new DJHandlerAdapter());
        }
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
            String url = ("/"+requestMapping.value()).replaceAll("/+","/");
            Method[] methods = beanWrapper.getWrapperClass().getMethods();
            for (Method method : methods) {
                if(!method.isAnnotationPresent(DJRequestMapping.class)){
                    continue;
                }
                DJRequestMapping methodRequestMapping = method.getAnnotation(DJRequestMapping.class);
                String finalUrl = url+("/"+methodRequestMapping.value()).replaceAll("/+","/");
                Pattern pattern = Pattern.compile(finalUrl.replaceAll("\\*",".*"));
                DJHandlerMapping handlerMapping = new DJHandlerMapping(pattern,beanWrapper.getWrapperInstance(),method);
                handlerMappings.add(handlerMapping);

            }
        }

    }


}
