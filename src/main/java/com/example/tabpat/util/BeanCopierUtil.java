package com.example.tabpat.util;

import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;

/**
 * bean浅拷贝
 *
 * @author 泡面ing
 * @date 2021/11/17
 */
public class BeanCopierUtil {

    public static <T> T create(Object sourceObj, Class<T> targetClass) {
        BeanCopier copier = BeanCopier.create(sourceObj.getClass(), targetClass, false);
        T targetObj;
        try {
            targetObj = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate target object", e);
        }
        copier.copy(sourceObj, targetObj, null);
        return targetObj;
    }
}
