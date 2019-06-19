/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.converter;

/**
 *
 * @author Peter.pan
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.beanutils.Converter;

public class DateConverter implements Converter {
    private static final String dateFormatStr = "yyyy/MM/dd";
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateFormatStr);
    private static final String dateLongFormatStr = dateFormatStr + " HH:mm:ss";
    private static final SimpleDateFormat dateTimeLongFormat = new SimpleDateFormat(dateLongFormatStr);

    @Override
    public Object convert(Class arg0, Object arg1) {
        if( arg1==null ) {
            return null;
        }

        String className = arg1.getClass().getName();
        //java.sql.Timestamp   
        if ("java.sql.Timestamp".equalsIgnoreCase(className)) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(dateFormatStr + " HH:mm:ss");
                return df.parse(dateTimeLongFormat.format(arg1));
            } catch (Exception e) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat(dateFormatStr);
                    return df.parse(dateTimeFormat.format(arg1));
                } catch (ParseException ex) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else if ("java.util.Date".equalsIgnoreCase(className)) {
            return (java.util.Date)arg1;
        } else if ("java.lang.Long".equalsIgnoreCase(className)) {
            return new Date((Long)arg1);
        } else if ("java.lang.Integer".equalsIgnoreCase(className)) {
            Integer intValue = (Integer)arg1;
            Long longValue = Long.valueOf(intValue.toString());
            return new Date(longValue);
        } else {//java.util.Date,java.sql.Date   
            String p = (String) arg1;
            if (p == null || p.trim().length() == 0) {
                return null;
            }
            try {
                SimpleDateFormat df = new SimpleDateFormat(dateFormatStr + " HH:mm:ss");
                return df.parse(p.trim());
            } catch (Exception e) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat(dateFormatStr);
                    return df.parse(p.trim());
                } catch (ParseException ex) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String formatDateTime(Object obj) {
        if (obj != null) {
            return dateTimeFormat.format(obj);
        } else {
            return "";
        }
    }

    public static String formatLongDateTime(Object obj) {
        if (obj != null) {
            return dateTimeLongFormat.format(obj);
        } else {
            return "";
        }
    }
}
