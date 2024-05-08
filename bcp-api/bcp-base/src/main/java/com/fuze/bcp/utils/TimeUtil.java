package com.fuze.bcp.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${Liu} on 2018/3/22.
 * 时间类型转换
 */
public class TimeUtil {

    /**
     *时间转换 时分秒
     */
    public static String getTimeTransSecond(String data){
        if(data == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strData = formatter.parse(data, pos);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataString = formatter1.format(strData);
        return dataString;
    }

    /**
     * 年月日
     */
    public static String getTimeTransDay(String data){
        if(data == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strData = formatter.parse(data, pos);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String dataString = formatter1.format(strData);
        return dataString;
    }

    /**
     * 时  分
     */
    public static String getTimeTransMinutes(String data){
        if(data == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date strData = formatter.parse(data, pos);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataString = formatter1.format(strData);
        return dataString;
    }
}
