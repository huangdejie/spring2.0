package com.cashbang.servlet;

import java.io.File;

/**
 * @Author: huangdj
 * @Date: 2020/4/24
 */
public class DJViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    public DJViewResolver(String templateRoot){
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public String getDEFAULT_TEMPLATE_SUFFIX() {
        return DEFAULT_TEMPLATE_SUFFIX;
    }

    public File getTemplateRootDir() {
        return templateRootDir;
    }

    public void setTemplateRootDir(File templateRootDir) {
        this.templateRootDir = templateRootDir;
    }

    public DJView resolveViewName(String viewName){
        if(viewName == null || "".equals(viewName.trim())){
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" +viewName).replaceAll("/+","/"));
        return new DJView(templateFile);
    }
}
