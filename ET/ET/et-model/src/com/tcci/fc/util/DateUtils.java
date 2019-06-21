package com.tcci.fc.util;

import com.tcci.cm.model.global.GlobalConstant;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jason.Yu
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public final static String ISO_LINK = "-";
    public final static String ROC_LINK = "/";

    /**
     * 取得今日 00:00:00
     * @return 
     */
    public static Date getToday(){
        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);// 本日
        
        return today.getTime();
    }
    
    public static String format(Date dt){
        return formatDateString(dt, GlobalConstant.FORMAT_DATETIME);
    }
    
    public static String formatDate(Date dt){
        return formatDateString(dt, GlobalConstant.FORMAT_DATE);
    }
    
    public static Date addDate(Date d, int day) {
        Date e;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, day);
        e = c.getTime();
        return e;
    }

    public static String formatDateString(Date date, String pattern) {
        if( date==null ){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    
    public static Map<String, Integer> getHoursList() {
        Map<String, Integer> hoursList = new LinkedHashMap<String, Integer>();
        for (int i=0; i<24; i++) {
            String hour = (i<10) ? "0" + i : String.valueOf(i);
            hoursList.put(hour, i);
        }        
        return hoursList;        
    }        

    public static Calendar convertDateToCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * DateUtil.getISODateStr(new Date()) = "yyyy-MM-dd"
     * 
     * @param dt
     * @return
     */
    public static String getISODateStr(Date dt) {

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
     * @param sLink
     * @return
     */
    public static String getISODateStr(Date dt, String sLink) {
        if (null == dt) {
            return "";
        }

        Calendar ctime = Calendar.getInstance();
        ctime.setTime(dt);

        StringBuilder sb = new StringBuilder();
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
     * DateUtil.getISODateTimeStr(new Date()) = "yyyy-MM-ddTHH:mm:ss"
     * 
     * @param dt
     * @return
     */
    public static String getISODateTimeStr(Date dt) {

        if (null == dt) {
            return "";
        }

        String sFormat = DateFormatUtils.ISO_DATETIME_FORMAT.getPattern();

        return DateFormatUtils.format(dt, sFormat);
    }

    /**
     * 將ISO Date字串轉換成 java.util.Date
     * <ul>
     * <li>DateUtil.getISODate("2005-10-10", "-") = Date(2005-10-10)</li>
     * </ul>
     * 
     * @param sISODate
     * @param sLink
     * @return
     */
    public static Date getISODate(String sISODate, String sLink) {
        if (StringUtils.isBlank(sISODate)) {
            return null;
        }
        Date dt = null;
        // 有分隔符號
        if (StringUtils.isNotBlank(sLink)) {
            String[] tokens = sISODate.split(sLink);

            if (tokens.length == 3) {

                int iYear = str2Int(tokens[0]);
                int iMonth = str2Int(tokens[1]);
                int iDay = str2Int(tokens[2]);

                dt = DateUtils.getDate(iYear, iMonth, iDay);
            }
        } // 沒有分隔符號 YYYYMMDD
        else {

            if (sISODate.length() == 8) {
                int iYear = str2Int(sISODate.substring(0, 4));
                int iMonth = str2Int(sISODate.substring(4, 6));
                int iDay = str2Int(sISODate.substring(6, 8));
                dt = DateUtils.getDate(iYear, iMonth, iDay);
            }
        }
        return dt;
    }

    /**
     * 將ISO DateTime字串轉換成 Date
     * <ul>
     * <li>DateUtil.getISODate("2005-10-10T12:34:56", "-") =
     * Date(2005-10-10T12:34:56)</li>
     * </ul>
     * 
     * @param sISODateTime
     * @return 
     */
    public static Date getISODateTime(String sISODateTime) {
        String sDateTimeLink = "T";
        return getCommonDateTime(sISODateTime, sDateTimeLink);
    }
    public static Date getCommonDateTime(String sISODateTime, String sDateTimeLink) {
        if (StringUtils.isBlank(sISODateTime)) {
            return null;
        }

        String sDateLink = "-";
        String sTimeLink = ":";

        boolean isParseOK = true;

        Date dt = null;

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
     * 將ISO Date字串轉換成 Date
     * <ul>
     * <li>DateUtil.getISODate("2005-10-10") = Date(2005-10-10)</li>
     * </ul>
     * 
     * @param sISODate
     * @return
     */
    public static Date getISODate(String sISODate) {
        return getISODate(sISODate, ISO_LINK);
    }

    public static Date getDate(int iYear, int iMonth, int iDay) {

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

    public static Date getDateTime(int iYear, int iMonth, int iDay, int iHour, int iMin, int iSec) {

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
     * 由取得的民國年月日字串，轉換為 Calendar 物件。
     * 
     * @param rocStr
     *            民國年月日字串, 格式:"YYYMMdd" or "YYMMdd"
     * @return
     */
    public static Calendar getCalendarByRocStr(String rocStr) {

        int strLen = StringUtils.length(rocStr);

        if (StringUtils.isBlank(rocStr) || strLen < 6 || strLen > 7) {
            return null;
        }

        Calendar c = Calendar.getInstance();

        int year;
        int month;
        int day;

        if (strLen == 6) {
            year = Integer.parseInt(StringUtils.substring(rocStr, 0, 2)) + 1911;
            month = Integer.parseInt(StringUtils.substring(rocStr, 2, 4));
            day = Integer.parseInt(StringUtils.substring(rocStr, 4, 6));
        } else { // strLen==7
            year = Integer.parseInt(StringUtils.substring(rocStr, 0, 3)) + 1911;
            month = Integer.parseInt(StringUtils.substring(rocStr, 3, 5));
            day = Integer.parseInt(StringUtils.substring(rocStr, 5, 7));

        }

        // 檢查年月日轉換成Calendar的時候是否正確。
        Date rocDate = getDate(year, month, day);
        if (strLen == 6) {
            if (!rocStr.equals(getROCDateStr(rocDate, "", false))) {
                return null;
            }
        } else { // strLen==7
            if (!rocStr.equals(StringUtils.leftPad(getROCDateStr(rocDate, "", true), 7, "0"))) {
                return null;
            }
        }
        c.setTime(rocDate);

        return c;
    }

    /**
     * 轉換Calendar 格式的日期成為民國年(String)
     * 
     * @param calendar
     * @return
     */
    public static String getRocDateStrByCalendar(Calendar calendar) {
        return StringUtils.leftPad(getROCDateStr(calendar.getTime(), "", true), 7, "0");
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
    public static String getROCDateStr(Date dt, String sLink, boolean isSevenDigits) {

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

        StringBuilder sb = new StringBuilder();
        sb.append(sYear).append(sLink).append(sMonth).append(sLink).append(sDay);

        return sb.toString();

    }    
    
/**
     * 取得常用日期時間格式(24小時制)
     * "yyyy/MM/dd hh:mm:ss
     */
    public static String getCommonDateTimeStr(Date dt) {

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
     * 
     * @param sValue
     * @return
     */
    private static int str2Int(String sValue, int iDefaultValue) {
        int iValue;
        try {
            iValue = Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            iValue = iDefaultValue;
        }

        return iValue;
    }

    private static int str2Int(String sValue) {
        return str2Int(sValue, 0);
    }
    
    /**
     * 依傳入的西元年、月份，取得六位數的年月。如: 201102
     * @param prplanMasterVO
     * @return 
     */
    public static String getYearMonth(String year, String month) {
        //年月
        StringBuilder sbYearMonth = new StringBuilder();
        sbYearMonth = sbYearMonth.append(year);
        if (StringUtils.length(month) < 2) {
            sbYearMonth = sbYearMonth.append("0").append(month);
        } else {
            sbYearMonth = sbYearMonth.append(month);
        }
        return sbYearMonth.toString();
    }    
    
 /**
     * 是否為系統年月。
     * @param year
     * @param month
     * @return 
     */
    public static boolean isSystemYearMonth(String year, String month) {
        try {
            Calendar c = Calendar.getInstance();
            int sysYear = c.get(Calendar.YEAR);
            if (!Integer.valueOf(year).equals(sysYear)) {
                return false;
            }

            int sysMonth = c.get(Calendar.MONTH);
            return Integer.valueOf(month).equals(sysMonth + 1);
        } catch (NumberFormatException e) {
            return false;
        }
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
    
 /**
     * 取得西元年月字串
     * <p>
     * 
     * @param calendar
     * @return yyyyMM
     */
    public static String getYearMonth(Calendar calendar) {

        if (null == calendar) {
            return "";
        }

        String sFormat = DateFormatUtils.SIMPLE_YEAR_MONTH_FORMAT.getPattern();

        return DateFormatUtils.format(calendar.getTime(), sFormat);
    }    
/**
     * 設定傳入日期的時間。
     * @param srcDate 原始時間
     * @param timeStr 要設定的時間字串，24小時制。如: 16:30:30
     * @return 
     */
    public static Date getDateTime(Date srcDate, String timeStr) {
        String sTimeLink = ":";
        String[] timeTokens = timeStr.split(sTimeLink);

        int iHour;
        int iMin;
        int iSec;

        if (timeTokens.length == 3) {

            iHour = StringUtils.str2Int(timeTokens[0]);
            iMin = StringUtils.str2Int(timeTokens[1]);
            iSec = StringUtils.str2Int(timeTokens[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(srcDate);
            calendar.set(Calendar.HOUR_OF_DAY, iHour);
            calendar.set(Calendar.MINUTE, iMin);
            calendar.set(Calendar.SECOND, iSec);
            return calendar.getTime();

        } else {
            throw new NumberFormatException("Parse time string error!");
        }
    }    
    
    /**
     * 傳換 yyyyMMddHHmmss => Date
     * @param dtStr
     * @return 
     */
    public static Date getDateTimeFromStr(String dtStr){
        if( dtStr==null || dtStr.length()!=14 ){
            logger.error("getDateTimeFromStr error dtStr = "+dtStr);
            return null;
        }
        try{
            int yyyy = Integer.parseInt(dtStr.substring(0, 4));
            int MM = Integer.parseInt(dtStr.substring(4, 6));
            int dd = Integer.parseInt(dtStr.substring(6, 8));
            int HH = Integer.parseInt(dtStr.substring(8, 10));
            int mm = Integer.parseInt(dtStr.substring(10, 12));
            int ss = Integer.parseInt(dtStr.substring(12));
            
            return getDateTime(yyyy, MM, dd, HH, mm, ss);
        }catch(Exception e){
            logger.error("getDateTimeFromStr exception = \n", e);
            return null;
        }
    }
    
    /**
     * 特定月份第一日
     * @param monthDiff
     * @return 
     */
    public static Date getMonthFirstDate(int monthDiff){
        Calendar cal = Calendar.getInstance();
        if( monthDiff!=0 ){
            cal.add(Calendar.MONTH, monthDiff);
        }
        
        Calendar newCal = Calendar.getInstance();
        newCal.clear();
        newCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        newCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        
        return newCal.getTime();
    }
    public static Date getMonthFirstDate(int monthDiff, Date speDate){
        Calendar cal = toCalendar(speDate);
        if( monthDiff!=0 ){
            cal.add(Calendar.MONTH, monthDiff);
        }
        
        Calendar newCal = Calendar.getInstance();
        newCal.clear();
        newCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        newCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        
        return newCal.getTime();
    }
    
    /**
     * 本月份第一日
     * @param monthDiff
     * @return 
     */
    public static Date getMonthFirstDate(){
        return getMonthFirstDate(0);
    }
    /**
     * 上月份第一日
     * @param monthDiff
     * @return 
     */
    public static Date getLastMonthFirstDate(){
        return getMonthFirstDate(-1);
    }
    
    /**
     * 依輸入的日期取得月底的日期 (2007-01-12 = 2007-01-31)
     * 
     * @param Date 傳入的日期
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
     * 計算工作日 (指考慮惕除六、日)
     * @param sdate
     * @param edate
     * @return 
     */
    public static int calWorkDays(Date sdate, Date edate){
        // 取得星期幾序號
        // Calendar.DAY_OF_WEEK: 日~六 = 1~7
        int s = DateUtils.toCalendar(sdate).get(Calendar.DAY_OF_WEEK);
        int f = DateUtils.toCalendar(edate).get(Calendar.DAY_OF_WEEK);
        
        int sw  = 7-s; // 開始日所在週，距週六天數
        int fw = f-1; // 結束日所在週，距週日天數

        Date ssDay = DateUtils.addDate(sdate, sw); // // 開始日所在週的週六
        Date esDay = DateUtils.addDate(edate, -fw); // // 結束日所在週的週日
        
        int workdays; // 工作日
        
        // 前提 Finishdate >= Startdate
        if( sdate.equals(edate) ){
            if( s==1 || s==7 ){// 週日或週六
                workdays = 0;
            }else{
                workdays = 1; // 同一天 = 1
            }
        }else{
            if( ssDay.equals(edate) || ssDay.after(edate) ){// 開始日、結束日在同一週
                int n = f-s+1; // 相距天數+1
                if( s==1 || s==7 ){ n = n-1; }// 開始日若週日或週六 減1
                if( f==1 || f==7 ){ n = (n>0)?n-1:0; }// 結束日若週日或週六 減1 (檢核重複扣)

                workdays = n;
            }else{// 開始日、結束日在不同週
                // 開始日所在週
                int ns = sw; // 週六不計，所以不需減1
                
                // 中間的包含週
                int days = calDaysBetween(ssDay, esDay) - 1;// 包含天數
                int weeks = days/7;// 包含週數
                int mWeekdays = days - (weeks*2); // // 包含工作日
                
                // 結束日所在週
                int nf = fw; // 週日不計，所以不需減1
                
                workdays = sw + mWeekdays + nf;
            }
        }
        
        return workdays;
    }
    
    /**
     * 計算兩日期間，計算間隔日期 (同一天 = 0)
     * @param sdate
     * @param edate
     * @return 
     */
    public static int calDaysBetween(Date sdate, Date edate){
        // 確保 sdate<=edate
        if( sdate.after(edate) ) {
            Date swap = edate;
            edate = sdate;
            sdate = swap;
        }
        
        // 只保留到年月日單位
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sdateStr = sdf.format(sdate);
        String edateStr = sdf.format(edate);
        try {
            Date newSdate = sdf.parse(sdateStr);
            Date newEdate = sdf.parse(edateStr);
            
            long days = TimeUnit.MILLISECONDS.toDays(newEdate.getTime() - newSdate.getTime());
            
            return (int)days;
        } catch (ParseException ex) {
            logger.error("calDaysBetween exception ...", ex);
        }
        
        return 0;
    }
}

