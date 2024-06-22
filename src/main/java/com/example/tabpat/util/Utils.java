package com.example.tabpat.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class Utils {
    /**
     * 时间转时间戳
     *
     * @param time 输入时间
     * @return 返回时间戳
     * @throws ParseException
     */
    public static Long createTimestamp(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);
        return date.getTime();
    }

    /**
     * 时间戳转时间
     *
     * @param timestamp 时间戳
     * @param type      时间类型
     * @return 返回时间
     */

    public static String timestampToTime(Long timestamp, String type) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        //部署服务器时区未知，转换为东八区上海时间
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);
        DateTimeFormatter formatter = switch (type) {
            case "day" -> DateTimeFormatter.ofPattern("yyyy-MM-dd");
            case "month" -> DateTimeFormatter.ofPattern("yyyy-MM");
            case "year" -> DateTimeFormatter.ofPattern("yyyy");
            default -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        };
        return dateTime.format(formatter);
    }

    //时间戳方法重载，可只接受一个参数
    public static String timestampToTime(Long timestamp) {
        return timestampToTime(timestamp, "default");
    }
}
