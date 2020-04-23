package com.cashbang.config;

import com.cashbang.annotations.DJAutowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class DJApplicationContext {

    private BeanDefinitionReader beanDefinitionReader;
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String,Object> originalInstanceMap = new HashMap<>();
    private Map<String,BeanWrapper> beanWrapperMap = new HashMap<>();

    public Map<String,BeanWrapper> getBeanWrapper(){
        return beanWrapperMap;
    }

    public DJApplicationContext(String... configurations){
        beanDefinitionReader = new BeanDefinitionReader(configurations);
        List<BeanDefinition> beanDefinitionList = beanDefinitionReader.getBeanDefinition();
        try {
            doRegisterBeanDefinition(beanDefinitionList);
            doAutowired();
            String a = "";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doAutowired() {
        for (Map.Entry<String,BeanDefinition> entry:beanDefinitionMap.entrySet()){
            getBean(entry.getValue());
        }
        try {
            populateBean(beanWrapperMap);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getBean(BeanDefinition beanDefinition) {
        try {
            Class clazz = Class.forName(beanDefinition.getBeanClassName());
            Object instance = clazz.newInstance();
            originalInstanceMap.put(beanDefinition.getBeanName(),instance);
            BeanWrapper beanWrapper = new BeanWrapper(instance,clazz);
            beanWrapperMap.put(beanDefinition.getBeanName(),beanWrapper);
            for(Class cl:clazz.getInterfaces()){
                originalInstanceMap.put(cl.getName(),instance);
                beanWrapperMap.put(cl.getName(),beanWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBean(Map<String,BeanWrapper> beanWrapperMap) throws Exception{
        for (Map.Entry<String,BeanWrapper> entry:beanWrapperMap.entrySet()) {
            BeanWrapper beanWrapper = entry.getValue();
            Object instance = beanWrapper.getWrapperInstance();
            Class clzz = beanWrapper.getWrapperClass();
            for(Field field:clzz.getDeclaredFields()){
                if(!field.isAnnotationPresent(DJAutowired.class)){
                    continue;
                }
                DJAutowired djAutowired = field.getAnnotation(DJAutowired.class);
                String beanName = djAutowired.value();
                if("".equals(beanName)){
                    if(field.getType().isInterface()){
                        beanName = field.getType().getName();
                    }else{
                        beanName = toLowerCase(field.getType().getSimpleName());
                    }
                }
                field.setAccessible(true);
                if(!originalInstanceMap.keySet().contains(beanName)){
                    throw new Exception("暂未找到名称为"+beanName+"的实例");
                }
                field.set(instance,originalInstanceMap.get(beanName));

            }
        }

    }

    /**
     * 将字符串首字母大写
     * @param simpleName
     * @return
     */
    private String toLowerCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        if(chars[0] > 97 && chars[0] < 122){
            return simpleName;
        }
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitionList) throws Exception{
        for (BeanDefinition beanDefinition:beanDefinitionList) {
            if(beanDefinitionMap.keySet().contains(beanDefinition.getBeanName())){
                throw new Exception("The " + beanDefinition.getBeanName() + "is exists");
            }
            beanDefinitionMap.put(beanDefinition.getBeanName(),beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
        }
    }


}
