package com.cashbang.config;

import java.util.regex.Pattern;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class BeanWrapper {
    private Object wrapperInstance;

    private Class wrapperClass;

    public BeanWrapper(Object wrapperInstance,Class wrapperClass){
        this.wrapperInstance = wrapperInstance;
        this.wrapperClass = wrapperClass;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class wrapperClass) {
        this.wrapperClass = wrapperClass;
    }


}
