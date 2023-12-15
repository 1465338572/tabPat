package com.example.tabpat.util;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    /**
     * 时间转时间戳
     * @param time 输入时间
     * @return  返回时间戳
     * @throws ParseException
     */
    public static Long createTimestamp(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);
        return date.getTime();
    }

}
