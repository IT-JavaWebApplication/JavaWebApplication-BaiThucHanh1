package com.example.config;

import org.springframework.web.servlet.support.AbtractAnnotationConfigDispatcherServletInitializer;


public class AppInitializer extends AbstractAnnotationConfigDispatcherSerletInitializer {

    @Overrive
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Overrive
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]
                {WebConfig.class};
    }

    @Overrive
    protected String[]
    getSerletMappings() {
        return new String[]{"/"};
    }
}
