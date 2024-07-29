package com.example.tabpat.annotation;

import java.lang.annotation.*;

/**
 * AOP监控
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArticlesView {
    /**
     * 描述
     */
    String description() default "";
}
