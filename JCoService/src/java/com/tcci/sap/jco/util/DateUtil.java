/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Peter
 */
public class DateUtil {
    public final static String DATETIME_FORMAT_DEF = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_FORMAT_DEF = "yyyy-MM-dd";
    public final static String DATE_FORMAT_SAP = "yyyyMMdd";
    
    public static String formatDate(Date dateTime){
        return format(dateTime, DATE_FORMAT_DEF);
    }

    public static String format(Date dateTime){
        return format(dateTime, DATETIME_FORMAT_DEF);
    }

    public static String format(Date dateTime, String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(dateTime);
    }

    public static String formatDate(Calendar cal){
        return format(cal, DATE_FORMAT_DEF);
    }

    public static String format(Calendar cal){
        return format(cal, DATETIME_FORMAT_DEF);
    }

    public static String format(Calendar cal, String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(cal.getTime());
    }

    /**
     * 時間加減 
     * @param d
     * @param field
     * @param num
     * @return 
     */
    public static Date addDateTime(Date d, int field, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        //c.add(Calendar.DATE, day);
        c.add(field, num);
        return c.getTime();
    }
}
