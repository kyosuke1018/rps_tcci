/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Gilbert.Lin
 */
public class DateUtil {

    public static Map<String, String> getLeaveTimeList() {
        Map<String, String> leaveTimeList = new LinkedHashMap<String, String>();
        Calendar calendar = Calendar.getInstance();
        //initial
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        for (int i = 0; i < 48; i++) {
            //進行轉換
            java.text.SimpleDateFormat simple = new java.text.SimpleDateFormat();
            simple.applyPattern("HH:mm");
            String dateString = simple.format(calendar.getTime());
//            System.out.println("dateString="+dateString);
            leaveTimeList.put(dateString, dateString);
            // Add 30 minutes to the calendar time
            calendar.add(Calendar.MINUTE, 30);
        }
        return leaveTimeList;
    }

    public static String convertHours(BigDecimal totalHours) {
        BigDecimal per8Hours = new BigDecimal("8");
        BigDecimal days;
        BigDecimal hours;
        days = totalHours.divideToIntegralValue(per8Hours).setScale(0,RoundingMode.UNNECESSARY);
        hours = totalHours.subtract(days.multiply(per8Hours));
        StringBuilder sb = new StringBuilder();
        sb.append(days);
        sb.append("天");
        sb.append(hours);
        sb.append("小時(");
        sb.append(totalHours);
        sb.append(")");
//        System.out.println(sb.toString());
        return sb.toString();
    }

    public static BigDecimal betweenHours(java.util.Date startDate, java.util.Date endDate, boolean isNormalClass) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.SECOND, 0);
        // Date end = sdf.parse(enddate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.SECOND, 0);
        BigDecimal leaveHours_Total = new BigDecimal(0);
        BigDecimal leaveHours_Day = new BigDecimal(0);
        int hour_start = 8;
        int hour_end = 17;
        int minute_start = 0;
        int minute_end = 0;
        int leave_hours = 8;
        while (start.before(end)) {
            int hour = start.get(Calendar.HOUR_OF_DAY);
            int minute = start.get(Calendar.MINUTE);
//				System.out.println("hour="+hour);
//				System.out.println("minute="+minute);
            int day = start.get(Calendar.DAY_OF_WEEK);
            //holiday
            if(isNormalClass){
                if ((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY)) {
                    start.add(Calendar.DAY_OF_MONTH, 1);
                    start.set(Calendar.HOUR_OF_DAY, 0);
                    continue;
                }
            }
            if (leaveHours_Day.intValue() == 8) {
                leaveHours_Day = new BigDecimal(0);
                start.add(Calendar.DATE, 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                continue;
            }

            leaveHours_Total = leaveHours_Total.add(new BigDecimal("0.5"));
            leaveHours_Day = leaveHours_Day.add(new BigDecimal("0.5"));
//				System.out.println("workingHours=" + leaveHours_Total);

            start.add(Calendar.MINUTE, 30);
        }
        return leaveHours_Total;
    }

    public static BigDecimal betweenHours(String classDef, java.util.Date startDate, java.util.Date endDate) {
        String[] classDefList = {"NORMAL", "SHIFT"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.SECOND, 0);
        // Date end = sdf.parse(enddate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.SECOND, 0);
        BigDecimal leaveHours_Total = new BigDecimal(0);
        BigDecimal leaveHours_Day = new BigDecimal(0);
        int hour_start = 8;
        int hour_end = 17;
        int minute_start = 0;
        int minute_end = 0;
        int leave_hours = 8;
        while (start.before(end)) {
            int hour = start.get(Calendar.HOUR_OF_DAY);
            int minute = start.get(Calendar.MINUTE);
//				System.out.println("hour="+hour);
//				System.out.println("minute="+minute);
            if (classDefList[1].equalsIgnoreCase(classDef)) {
                //holiday
                int day = start.get(Calendar.DAY_OF_WEEK);
                if ((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY)) {
                    start.add(Calendar.DAY_OF_MONTH, 1);
                    start.set(Calendar.HOUR_OF_DAY, 0);
                    start.set(Calendar.MINUTE, 0);
                    continue;
                }
            }
            if (leaveHours_Day.intValue() == 8) {
                leaveHours_Day = new BigDecimal(0);
                start.add(Calendar.DATE, 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                continue;
            }

            leaveHours_Total = leaveHours_Total.add(new BigDecimal("0.5"));
            leaveHours_Day = leaveHours_Day.add(new BigDecimal("0.5"));
            System.out.println("workingHours=" + leaveHours_Total);

            start.add(Calendar.MINUTE, 30);
        }
        return leaveHours_Total;
    }

    public static java.util.Date mergeDateTime(java.util.Date date, String dateTime) {
        String[] hhMM = dateTime.split(":");
        Calendar cal2 = Calendar.getInstance();       // get calendar instance 
        cal2.setTime(date);                           // set cal to endDate 
        cal2.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hhMM[0]));            // set hour to midnight 
        cal2.set(Calendar.MINUTE, Integer.valueOf(hhMM[1]));                 // set minute in hour            
        return cal2.getTime();
    }
    /**
     * 
     * @param date java.util.Date date
     * @param hour 00~23
     * @return date java.util.Date date
     */
    public static java.util.Date mergeDateHour(java.util.Date date, String hour) {
        Calendar cal2 = Calendar.getInstance();       // get calendar instance 
        cal2.setTime(date);                           // set cal to endDate 
        cal2.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));            // set hour to midnight 
        cal2.set(Calendar.MINUTE, 0);                 // set minute in hour            
        return cal2.getTime();
    }    
    public static String mergeHourMinute(String hour, String minute) {           
        return hour+":"+minute;
    }    

    public static String getDateTime(java.util.Date date) {
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("HH:mm");
        StringBuilder nowHHmm = new StringBuilder(dateformatYYYYMMDD.format(date));
        return nowHHmm.toString();
    }
    public static String getHour(java.util.Date date) {
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("HH");
        StringBuilder nowHHmm = new StringBuilder(dateformatYYYYMMDD.format(date));
        return nowHHmm.toString();
    }    
    public static String getMinute(java.util.Date date) {
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("mm");
        StringBuilder nowHHmm = new StringBuilder(dateformatYYYYMMDD.format(date));
        return nowHHmm.toString();
    } 
    public static void main(String[] args) {
        getLeaveTimeList();
    }

    /**
     * DateUtils.getISODateStr(new Date(), "/") = "yyyy/MM/dd"
     * 
     * @param dt
     *            , sLink
     * @return
     */
    public static String getISODateStr(java.util.Date dt, String sLink) {
        if (null == dt) {
            return "";
        }

        Calendar ctime = Calendar.getInstance();
        ctime.setTime(dt);

        StringBuffer sb = new StringBuffer();
        sb.append(ctime.get(Calendar.YEAR));

        sb.append(sLink);

        String sMonth = String.valueOf(ctime.get(Calendar.MONTH) + 1);
        if (sMonth.length() == 1) {
            sb.append("0");
        }
        sb.append(sMonth);

        sb.append(sLink);

        String sDay = String.valueOf(ctime.get(Calendar.DAY_OF_MONTH));
        if (sDay.length() == 1) {
            sb.append("0");
        }
        sb.append(sDay);

        return sb.toString();

    }

    /**
     * DateUtils.getSimpleISODateStr(new Date()) = "yyyyMMdd"
     */
    public static String getSimpleISODateStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_ISO_DATE_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }

    /**
     * DateUtils.getSimpleISOTimeStr(new Date()) = "HHmmss"
     */
    public static String getSimpleISOTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_ISO_TIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }
    
    /**
     * DateUtils.getSimpleISOTimeStr(new Date()) = "HHmm"
     */
    public static String getSimpleTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_TIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }    

    public static java.util.Date getDate(int iYear, int iMonth, int iDay) {

        if (iYear <= 0 || iMonth <= 0 || iDay <= 0) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.set(Calendar.YEAR, iYear);
        calendar.set(Calendar.MONTH, iMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, iDay);

        return calendar.getTime();
    }

    /**
     * 將ISO Date字串轉換成 java.util.Date
     * <ul>
     * <li>DateUtils.getISODate("2005-10-10", "-") = Date(2005-10-10)</li>
     * </ul>
     * 
     * @param sISODate
     * @param sLink
     * @return
     */
    public static java.util.Date getISODate(String sISODate, String sLink) {
        if (StringUtils.isBlank(sISODate)) {
            return null;
        }
        java.util.Date dt = null;
        // 有分隔符號
        if (StringUtils.isNotBlank(sLink)) {
            String[] tokens = sISODate.split(sLink);

            if (tokens.length == 3) {

                int iYear = str2Int(tokens[0]);
                int iMonth = str2Int(tokens[1]);
                int iDay = str2Int(tokens[2]);

                dt = DateUtil.getDate(iYear, iMonth, iDay);
            }
        } // 沒有分隔符號 YYYYMMDD
        else {

            if (sISODate.length() == 8) {
                int iYear = str2Int(sISODate.substring(0, 4));
                int iMonth = str2Int(sISODate.substring(4, 6));
                int iDay = str2Int(sISODate.substring(6, 8));
                dt = getDate(iYear, iMonth, iDay);
            }
        }
        return dt;
    }

   /**
     * DateUtils.getISODateTimeStr(new Date()) = "yyyy-MM-ddTHH:mm:ss"
     * 
     * @param dt
     * @return
     */
    public static String getISODateTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.ISO_DATETIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }    
    
    public static java.util.Date getDateTime(int iYear, int iMonth, int iDay, int iHour, int iMin, int iSec) {

        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.set(Calendar.YEAR, iYear);
        calendar.set(Calendar.MONTH, iMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, iDay);
        calendar.set(Calendar.HOUR_OF_DAY, iHour);
        calendar.set(Calendar.MINUTE, iMin);
        calendar.set(Calendar.SECOND, iSec);

        return calendar.getTime();
    }    
    
    
    /**
     * 
     * @param sValue
     * @return
     */
    private static int str2Int(String sValue, int iDefaultValue) {
        int iValue = iDefaultValue;
        try {
            iValue = Integer.parseInt(sValue);
        } catch (Exception e) {
            iValue = iDefaultValue;
        }

        return iValue;
    }

    private static int str2Int(String sValue) {
        return str2Int(sValue, 0);
    }
    public static Map<String, String> getHoursList() {
        Map<String, String> hoursList = new LinkedHashMap<String, String>();
        for (int i=0; i<24; i++) {
            String hour = (i<10) ? "0" + i : String.valueOf(i);
            hoursList.put(hour, hour);
        }        
        return hoursList;        
    }
    public static Map<String, String> getMinutesList() {
        Map<String, String> minutesList = new LinkedHashMap<String, String>();
        for (int i = 0; i < 60; i++) {
            String minute = (i<10) ? "0" + i : String.valueOf(i);            
            minutesList.put(minute, minute);
        }
        return minutesList;        
    }  
    public static String getDateTimeString(java.util.Date date) {
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        StringBuilder nowHHmm = new StringBuilder(dateformatYYYYMMDD.format(date));
        return nowHHmm.toString();
    }    
    
  /**
     * 依輸入的日期取得月底的日期 (2007-01-12 = 2007-01-31)
     * 
     * @param Date
     *            傳入的日期
     * @return Date 月底
     */
    public static Date getLastDayOfMonth(Date dt) {

        if (null == dt) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);

        // 取得本月最大日數(月底日)
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 天數

        Calendar rtnCalendar = Calendar.getInstance();
        rtnCalendar.clear();
        rtnCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        rtnCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        rtnCalendar.set(Calendar.DAY_OF_MONTH, day);

        return rtnCalendar.getTime();
    }

    /**
     * 依輸入的日期取得該年年底的日期 (12/31) (2007-01-12 = 2007-12-31)
     * 
     * @param Date
     *            傳入的日期
     * @return Date 月底
     */
    public static Date getLastDayOfYear(Date dt) {

        if (null == dt) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);

        Calendar rtnCalendar = Calendar.getInstance();
        rtnCalendar.clear();
        rtnCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        rtnCalendar.set(Calendar.MONTH, 11);
        rtnCalendar.set(Calendar.DAY_OF_MONTH, 31);

        return rtnCalendar.getTime();
    }
    
    /**
     *  取得當天最後一個Milliseconds
     * @param date
     * @return date
     */
    public static Date getLastMilliSecondsOfTheDay(Date date){
        date = DateUtil.getFirstMinuteOfDay(date);
        date = DateUtils.addDays(date, 1);
        date = DateUtils.addMilliseconds(date, -1);
        return date;
    }
    
    
    /**
     *  取得當年度最後一個Milliseconds
     * @param date
     * @return date
     */
    public static Date getLastMilliSecondsOfTheYear(Date date){
        date = DateUtil.getLastDayOfYear(date);
        date = DateUtils.addDays(date, 1);
        date = DateUtils.addMilliseconds(date, -1);
        return date;
    }    
    
   /**
     * 依輸入的日期，取得當天的零時零分(00:00)日期物件
     * 
     * @param Date
     * @return Date
     */
    public static Date getFirstMinuteOfDay(Date dt) {

        if (null == dt) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);

        Calendar rtnCalendar = Calendar.getInstance();
        rtnCalendar.clear();
        rtnCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        rtnCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        rtnCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        return rtnCalendar.getTime();
    }    
    public static String getStringByDate(java.util.Date date,String pattern) {
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat(pattern);
        StringBuilder nowHHmm = new StringBuilder(dateformatYYYYMMDD.format(date));
        return nowHHmm.toString();
    }

}
