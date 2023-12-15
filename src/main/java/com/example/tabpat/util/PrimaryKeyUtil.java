package com.example.tabpat.util;

import java.util.UUID;

/**
 * 主键跑龙套
 *
 * @author 泡面ing
 * @date 2021/11/17
 */
public class PrimaryKeyUtil {

    public static String get() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
