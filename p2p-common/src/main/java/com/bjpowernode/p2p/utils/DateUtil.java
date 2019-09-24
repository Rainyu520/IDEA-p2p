package com.bjpowernode.p2p.utils;/**
 * ClassName:DateUtil
 * Package:com.bjpowernode.p2p.utils
 * Description
 *
 * @date ：${Date}
 */

/**
 *Author：Rainyu
 *2019/9/21
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 给定时期加上给定时间
 */
public class DateUtil {
    public static Date getDateByAddDay(Date bidTime, Integer cycle) {
        Calendar calendar =Calendar.getInstance ();
        calendar.setTime (bidTime);
        calendar.add (Calendar.DATE, cycle);
        return calendar.getTime ();
    }




    public static Date getDateByAddMonth(Date bidTime, Integer cycle) {
        Calendar calendar =Calendar.getInstance ();
        calendar.setTime (bidTime);
        calendar.add (Calendar.MONTH, cycle);
        return calendar.getTime ();
    }

    public static String getDate() {
        return new SimpleDateFormat ("yyyyMMddHHmmssSSS").format (new Date ());
    }
}
