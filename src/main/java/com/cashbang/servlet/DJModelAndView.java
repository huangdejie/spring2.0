package com.cashbang.servlet;

import java.util.Map;

/**
 * @Author: huangdj
 * @Date: 2020/4/24
 */
public class DJModelAndView {

    private String viewName;
    private Map<String,?> model;

    public DJModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public DJModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
