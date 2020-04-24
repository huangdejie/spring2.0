package com.cashbang.servlet;

import com.cashbang.annotations.DJRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
public class DJHandlerAdapter {

    public DJModelAndView handler(HttpServletRequest req, HttpServletResponse resp,DJHandlerMapping handlerMapping) throws UnsupportedEncodingException {
        //将参数名称和参数的位置保存起来
        Map<String,Integer> paramIndexMapping = new HashMap<String,Integer>();

        //获取参数列表中带DJRequestParam注解的名称并将其位置保存起来
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for(Annotation a:pa[i]){
                if(a instanceof DJRequestParam){
                    String paramName = ((DJRequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName,i);
                    }
                }
            }
        }

        //获取参数类型的class
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        //将req和resp形参类型名称的位置保存起来
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if(parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class){
                paramIndexMapping.put(parameterType.getName(),i);
            }
        }

        //获取实参列表String[]是因为参数中可能有数组
        Map<String,String[]> params = req.getParameterMap();

        Object [] paramValues = new Object[parameterTypes.length];
        //将值保存到paramValues中
        for (Map.Entry<String,String[]> entry : params.entrySet()) {
            String value = new String(Arrays.toString(params.get(entry.getKey()))
                    .replaceAll("\\[|\\]","")
                    .replaceAll("\\s+",",").getBytes("ISO-8859-1"),"UTF-8");
            //若之前的参数名位置对应关系中不包含此key则直接跳过
            if(!paramIndexMapping.containsKey(entry.getKey())){
                continue;
            }
            int index = paramIndexMapping.get(entry.getKey());
            paramValues[index] = castStringValues(value,parameterTypes[index]);
        }


        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }


        //执行方法
        try {
            Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);
            if(result == null || result instanceof Void){
                return null;
            }
            boolean isModelAndView = handlerMapping.getMethod().getReturnType() == DJModelAndView.class;
            if(isModelAndView){
                return (DJModelAndView) result;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }

    private Object castStringValues(String value, Class<?> parameterType) {
        if(String.class == parameterType){
            return value;
        }else if(Integer.class == parameterType){
            return Integer.valueOf(value);
        }else if(Double.class == parameterType){
            return Double.valueOf(value);
        }else{
            return value;
        }
    }

}
