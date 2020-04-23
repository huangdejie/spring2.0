package com.cashbang.config;

/**
 * bean的定义
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class BeanDefinition {

    /**
     * bean的名称
     */
    private String beanName;
    /**
     * bean的class名称
     */
    private String beanClassName;

    public BeanDefinition(String beanName,String beanClassName){
        this.beanName = beanName;
        this.beanClassName = beanClassName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
