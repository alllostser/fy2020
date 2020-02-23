package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class TimeUtils {
    /**
     * 将日期格式转换为字符串格式利用JodaTime
     */
    public static String dateToStr(Date date){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.toString(dateTimeFormatter);
    }
    /**
     * 将日期格式转换为指定字符串格式利用JodaTime
     */
    public static String dateToStr(Date date,String formatter){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatter);
        return dateTime.toString(dateTimeFormatter);
    }

}
