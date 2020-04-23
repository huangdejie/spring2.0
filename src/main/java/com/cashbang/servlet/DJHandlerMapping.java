package com.cashbang.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class DJHandlerMapping {

    /**
     * 请求路径的正则表达式
     */
    private Pattern pattern;
    /**
     * controller实例对象
     */
    private Object controller;
    /**
     * 请求路径所对应的方法
     */
    private Method method;

    public DJHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.method = method;
        this.controller = controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
