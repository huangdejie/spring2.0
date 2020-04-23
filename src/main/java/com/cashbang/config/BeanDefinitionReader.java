package com.cashbang.config;

import com.cashbang.annotations.DJController;
import com.cashbang.annotations.DJService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件类
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class BeanDefinitionReader {

    /**
     * 所要扫描bean的class名称
     */
    private List<String> beanClassNameList = new ArrayList<>();
    /**
     * 配置文件
     */
    private Properties propertiesContext;

    public BeanDefinitionReader(String... configurations){
        doReader(configurations[0]);
        doInitBeanClassName(propertiesContext.getProperty("scan.package"));
        String a = "";
    }

    /**
     * 根据className封装成BeanDefinition
     * @return
     */
    public List<BeanDefinition> getBeanDefinition(){
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        List<String> beanNameList = new ArrayList<>();
        try {
            for (String className : beanClassNameList) {
                Class clazz = Class.forName(className);
                if(!clazz.isAnnotationPresent(DJController.class) && !clazz.isAnnotationPresent(DJService.class)){
                    //如果类上没有DJController或DJService注解则直接跳过
                    continue;
                }
                String beanName;
                //bean默认以注解上的值作为名称，若没有则默认以类名的首字母小写为名称
                if(clazz.isAnnotationPresent(DJController.class)){
                    DJController djController = (DJController) clazz.getAnnotation(DJController.class);
                    beanName = djController.value().equals("")?toLowerCase(clazz.getSimpleName()):djController.value();
                }else{
                    DJService djService = (DJService) clazz.getAnnotation(DJService.class);
                    beanName = djService.value().equals("")?toLowerCase(clazz.getSimpleName()):djService.value();
                }
                if(beanNameList.contains(beanName)){
                    throw new Exception("The beanName:"+beanName+" is existed!");
                }
                //默认以类名首字母小写作为bean的名称
                beanDefinitionList.add(new BeanDefinition(beanName,className));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return beanDefinitionList;
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

    /**
     * 根据配置文件得到要扫描的bean的className
     * @param scanPackage
     */
    private void doInitBeanClassName(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for(File file:classPath.listFiles()){
            if(file.isDirectory()){
                doInitBeanClassName(scanPackage + "." + file.getName());
                continue;
            }
            if(!file.getName().endsWith(".class")){
                //如果文件名不是以.class结尾则跳过
                continue;
            }
            String className = scanPackage+"."+file.getName().replace(".class", "");
            beanClassNameList.add(className);
        }
    }

    /**
     * 读取配置文件
     * @param contextConfiguration
     */
    private void doReader(String contextConfiguration){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfiguration.replaceAll("classpath:",""));
        propertiesContext = new Properties();
        try {
            propertiesContext.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getBeanClassNameList() {
        return beanClassNameList;
    }

    public void setBeanClassNameList(List<String> beanClassNameList) {
        this.beanClassNameList = beanClassNameList;
    }

    public Properties getPropertiesContext() {
        return propertiesContext;
    }

    public void setPropertiesContext(Properties propertiesContext) {
        this.propertiesContext = propertiesContext;
    }
}
