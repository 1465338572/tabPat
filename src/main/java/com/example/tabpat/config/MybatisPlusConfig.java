package com.example.tabpat.config;

import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * mybatis-plus配置
 * @author paomian
 */
public class MybatisPlusConfig {
    /**
     * sql 执行效率插件
     */
    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor(){
        return new PerformanceMonitorInterceptor();
    }
}
