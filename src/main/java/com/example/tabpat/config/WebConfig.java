package com.example.tabpat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源访问
 *
 * @author ABin
 * @date 2025/03/09
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前应用的工作目录
        String userDir = System.getProperty("user.dir");
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + userDir + "/");
    }
}
