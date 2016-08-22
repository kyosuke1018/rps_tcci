/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Jason.Yu
 */
public class DateUtil {

    //public static String DATE_FORMAT_YYMMDD = "yyMMdd";
    //public static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static String DATE_FORMAT_YYYYMM = "yyyyMM";

    public static Date convertDateToStartTime(Date d) {
        return convertDateToStartOrEnd(d, true);
    }

    public static Date convertDateToEndTime(Date d) {
        return convertDateToStartOrEnd(d, false);
    }

    public static Date convertDateToStartOrEnd(Date d, boolean isStart) {
        Date date = null;
        if (d == null) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (!isStart) {
            hour = 23;
            minute = 59;
            second = 59;
        }
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }

    public static String getDateFormat(Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String s = sdf.format(d);
        //System.out.println("getDateFormat=" + s);
        return s;
    }

    public static Date getChineseDateFormat(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.YEAR, -1911);
        return c.getTime();
    }

    public static String formatDateString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
