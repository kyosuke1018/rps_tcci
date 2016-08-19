package com.tcci.fc.util;

//import com.tcci.fc.util.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;


public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    public final static String ISO_LINK = "-";
    public final static String ROC_LINK = "/";

    /**
     * DateUtils.getSimpleISODateStr(new Date()) = "yyyyMMdd"
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
     * DateUtils.getSimpleISODateTimeStr(new Date()) = "yyyyMMddTHHmmss"
     * 
     * @param dt
     * @return
     */
    public static String getSimpleISODateTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_ISO_DATETIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }
    
    
    

    /**
     * DateUtils.getISODateStr(new Date()) = "yyyy-MM-dd"
     * 
     * @param dt
     * @return
     */
    public static String getISODateStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.ISO_DATE_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);

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
     * DateUtils.getISOTimeStr(new Date()) = "HH:mm:ss"
     * 
     * @param dt
     * @return
     */
    public static String getISOTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        // String sFormat = DateFormatUtils.ISO_TIME_FORMAT.getPattern();
        String sFormat = DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }

    /**
     * DateUtils.getISOTimeStr(new Date()) = "hh:mm a"
     * 
     * @author Eustace 2009/05/21
     * @param dt
     * @return
     */
    public static String getISOTimeStr2(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_ISO_TIME_FORMAT_IN_12.getPattern();

        return DateFormatUtils.format(dt, sFormat);
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

                dt = DateUtils.getDate(iYear, iMonth, iDay);
            }
        }
        // 沒有分隔符號 YYYYMMDD
        else {

            if (sISODate.length() == 8) {
                int iYear = str2Int(sISODate.substring(0, 4));
                int iMonth =str2Int(sISODate.substring(4, 6));
                int iDay = str2Int(sISODate.substring(6, 8));
                dt = DateUtils.getDate(iYear, iMonth, iDay);
            }
        }
        return dt;
    }

    /**
     * 將ISO DateTime字串轉換成 java.util.Date
     * <ul>
     * <li>DateUtils.getISODate("2005-10-10T12:34:56", "-") =
     * Date(2005-10-10T12:34:56)</li>
     * </ul>
     * 
     * @param sISODate
     * @param sLink
     * @return
     */
    public static java.util.Date getISODateTime(String sISODateTime) {
        if (StringUtils.isBlank(sISODateTime)) {
            return null;
        }

        String sDateTimeLink = "T";
        String sDateLink = "-";
        String sTimeLink = ":";

        boolean isParseOK = true;

        java.util.Date dt = null;

        int iYear = 0;
        int iMonth = 0;
        int iDay = 0;
        int iHour = 0;
        int iMin = 0;
        int iSec = 0;

        String[] tokens = sISODateTime.split(sDateTimeLink);

        if (tokens.length == 2) {

            String sDate = tokens[0];
            String sTime = tokens[1];

            String[] dateTokens = sDate.split(sDateLink);

            if (dateTokens.length == 3) {

                iYear = str2Int(dateTokens[0]);
                iMonth = str2Int(dateTokens[1]);
                iDay = str2Int(dateTokens[2]);

            } else {
                isParseOK = false;
            }

            String[] timeTokens = sTime.split(sTimeLink);

            if (isParseOK && timeTokens.length == 3) {

                iHour = str2Int(timeTokens[0]);
                iMin = str2Int(timeTokens[1]);
                iSec = str2Int(timeTokens[2]);

            } else {
                isParseOK = false;
            }

        }

        if (isParseOK) {

            dt = DateUtils.getDateTime(iYear, iMonth, iDay, iHour, iMin, iSec);
        }

        return dt;
    }

    /**
     * 將ISO Date字串轉換成 java.util.Date
     * <ul>
     * <li>DateUtils.getISODate("2005-10-10") = Date(2005-10-10)</li>
     * </ul>
     * 
     * @param sISODate
     * @return
     */
    public static java.util.Date getISODate(String sISODate) {
        return getISODate(sISODate, ISO_LINK);
    }

    /**
     * DateUtils.getBriefISODateStr(new Date()) = "yyMMdd"
     */
    public static String getBriefISODateStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.BRIEF_ISO_DATE_FORMAT.getPattern();

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
     * 將日期字串(yyyymmdd)轉為Date物件
     * 
     * @param dateString
     *            日期字串
     * @return
     */
    public static Date simpleISODateStringToDate(String dateString) {
        SimpleDateFormat sdm = new SimpleDateFormat("yyyymmdd");
        Date date = null;
        try {
            date = sdm.parse(dateString);
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        return date;
    }

    /**
     * 取得常用日期時間格式(12小時制)
     * "yyyy/MM/dd hh:mm:ss
     */
    public static String getCommonDateTimeStrIn12(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.COMMON_DATETIME_FORMAT_IN_12.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }
    
    /**
     * 取得常用日期時間格式(24小時制)
     * "yyyy/MM/dd hh:mm:ss
     */
    public static String getCommonDateTimeStr(java.util.Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.COMMON_DATETIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
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

    /**
     * 依輸入的日期，取得當天的零時零分(00:00)日期物件
     * 
     * @param Calendar
     * @return Calendar
     */
    public static Calendar getFirstMinuteOfDay(Calendar dt) {

        if (null == dt) {
            return null;
        }

        Calendar calendar = dt;

        Calendar rtnCalendar = Calendar.getInstance();
        rtnCalendar.clear();
        rtnCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        rtnCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        rtnCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        return rtnCalendar;
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
     * 轉換日期為民國年(年份最多三碼) DateUtils.getROCDateStr(new Date()) = "yyy-MM-dd"
     * 
     * @param dt
     * @return
     */
    public static String getROCDateStr(java.util.Date dt, boolean isSevenDigits) {
        return DateUtils.getROCDateStr(dt, DateUtils.ROC_LINK, isSevenDigits);
    }

    /**
     * 轉換日期為民國年 DateUtils.getROCDateStr(new Date(), "-") = "yyy-MM-dd"
     * 
     * @param String
     *            dt
     * @param String
     *            sLink
     * @param boolean 是否回傳yyy-MM-dd,若否則回傳yy-MM-dd
     * @return
     */
    public static String getROCDateStr(java.util.Date dt, String sLink, boolean isSevenDigits) {

        if (dt == null) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dt);

        // Date
        int iYear = calendar.get(Calendar.YEAR) - 1911;

        if (!isSevenDigits && iYear > 99) { // 2011-01-04 修正 如果是回傳 6位則須mode 100, 回傳7位則不需
            iYear = iYear % 100;
        }

        String sYear = String.valueOf(iYear);
        if (isSevenDigits) {
            sYear = StringUtils.leftPad(sYear, 3, "0");
        }

        int iMonth = calendar.get(Calendar.MONTH) + 1;
        String sMonth = String.valueOf(iMonth);
        sMonth = StringUtils.leftPad(sMonth, 2, "0");

        int iDay = calendar.get(Calendar.DAY_OF_MONTH);
        String sDay = String.valueOf(iDay);
        sDay = StringUtils.leftPad(sDay, 2, "0");

        StringBuffer sb = new StringBuffer();
        sb.append(sYear).append(sLink).append(sMonth).append(sLink).append(sDay);

        return sb.toString();

    }

    /**
     * 轉換日期為七位民國年月日 DateUtils.getRocDateStr7Digits(new Date(), "-") =
     * "yyy-MM-dd"
     * 
     * @param dt
     *            日期
     * @param sLink
     *            分隔符號
     * @return 七位民國年月日
     */
    public static String getRocDateStr7Digits(java.util.Date dt, String sLink) {
        String rocDateStr = getROCDateStr(dt, sLink, true);
        return StringUtils.leftPad(rocDateStr, 7, "0");
    }

    /**
     * 由取得的民國年月日字串，轉換為 Calendar 物件。
     * 
     * @param rocStr
     *            民國年月日字串, 格式:"YYYMMdd" or "YYMMdd"
     * @return
     */
    

    /**
     * 轉換Calendar 格式的日期成為民國年(String)
     * 
     * @param calendar
     * @return
     */
    public static String getRocDateStrByCalendar(Calendar calendar) {
        return StringUtils.leftPad(DateUtils.getROCDateStr(calendar.getTime(), "", true), 7, "0");
    }

    /**
     * 取得西元年月字串
     * <p>
     * 
     * @param calendar
     * @return yyyyMM
     */
    public static String getYeayMonth(Calendar calendar) {

        if (null == calendar) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_ISO_YEAR_MONTH_FORMAT.getPattern();

        return DateFormatUtils.format(calendar.getTime(), sFormat);
    }

    /**
     * 取得日期範圍 getRange("20090101", 1) => Calendar[2] = {
     * CalendarStart("20090101 00:00:00") , CalendarEnd("20090102 00:00:00") }
     * 
     * @param date
     *            日期
     * @param i
     *            天數範圍
     * @return
     */
    public static Calendar[] getCalendarRange(Calendar date, int i) {
        if (null == date) {
            return null;
        }
        Calendar dateStart = getFirstMinuteOfDay(date);

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date.getTime());
        dateEnd.add(Calendar.DAY_OF_WEEK, i);
        dateEnd = getFirstMinuteOfDay(dateEnd);

        Calendar[] range = { dateStart, dateEnd };
        return range;
    }

    /**
     * 取得日期範圍
     * 
     * <pre>
     * getRange(&quot;20090101&quot;,Calendar.DAY_OF_WEEK, 1) =&gt; Calendar[2] = { CalendarStart(&quot;20090101 00:00:00&quot;)  , CalendarEnd(&quot;20090102 00:00:00&quot;) }
     * getRange(&quot;20090101&quot;,Calendar.MONTH, 1)=&gt; Calendar[2] = { CalendarStart(&quot;20090101 00:00:00&quot;)  , CalendarEnd(&quot;20090201 00:00:00&quot;) }
     * getRange(&quot;20090101&quot;,Calendar.YEAR, 1) =&gt; Calendar[2] = { CalendarStart(&quot;20090101 00:00:00&quot;)  , CalendarEnd(&quot;20100101 00:00:00&quot;) }
     * &#064;param date 日期
     * &#064;param field Calendar參數
     * &#064;param amount
     * @return
     */
    public static Calendar[] getCalendarRange(Calendar date, int field, int amount) {
        if (null == date) {
            return null;
        }
        Calendar dateStart = getFirstMinuteOfDay(date);
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date.getTime());
        dateEnd.add(field, amount);
        dateEnd = getFirstMinuteOfDay(dateEnd);

        Calendar[] range = { dateStart, dateEnd };
        return range;
    }

    /**
     * 將西元年月(YYYYMM) 轉成Calendar
     * 
     * <pre>
     * 日期以當月1號為起始
     * getCalendarByYYYYMM(&quot;200901&quot;) = CalendarStart(&quot;20090101 00:00:00&quot;)
     * &#064;param yearMonth 西元年月(YYYYMM)
     * @return
     */
    public static Calendar getCalendarByYYYYMM(String yearMonth) {
        if (StringUtils.isBlank(yearMonth) || yearMonth.length() != 6) {
            return null;
        }

        // 西元年(YYYY)
        Integer sYear = Integer.parseInt(yearMonth.substring(0, 4));
        // 月份(MM)
        Integer sMonth = Integer.parseInt(yearMonth.substring(4, 6));

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, sYear);
        calendar.set(Calendar.MONTH, sMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

  

   

    public static int getDays(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return 0;
        } else if (date2.compareTo(date1) < 0) {
            return 0;
        }
        // date2應大於date1
        int days = 0;
        days = (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
        return days;
    }

    /**
     *  取得當天最後一個Milliseconds
     * @param date
     * @return date
     */
    public static Date getLastMilliSecondsOfTheDay(Date date){
        date = DateUtils.getFirstMinuteOfDay(date);
        date = DateUtils.addDays(date, 1);
        date = DateUtils.addMilliseconds(date, -1);
        return date;
    }
    
   /**
     * 取得指定年度的第一個星期X.
     * @param year
     * @param dayOfWeek 星期一~星期日，如: Calendar.SATURDAY, Calendar.SUNDAY
     */
    public static Calendar getFirstDayOfWeekInYear(int year, int dayOfWeek) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, 0, 1, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        cal.set(Calendar.WEEK_OF_MONTH, 1);
        if (cal.get(Calendar.YEAR) != year) {
            cal.add(Calendar.DATE, 7);
        }
        return cal;
    }    
}