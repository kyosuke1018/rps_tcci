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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateConverter implements Converter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String DATE_FORMAT_STR = "yyyy/MM/dd";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    private static final String DATETIME_FORMAT_STR = DATE_FORMAT_STR + " HH:mm:ss";
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(DATETIME_FORMAT_STR);

    @Override
    public Object convert(Class arg0, Object arg1) {
        if( arg1==null ) {
            return null;
        }

        String className = arg1.getClass().getName();
        //java.sql.Timestamp   
        if ("java.sql.Timestamp".equalsIgnoreCase(className)) {
            try {
                return DATETIME_FORMAT.parse(DATETIME_FORMAT.format(arg1));
            } catch (ParseException e) {
                try {
                    return DATE_FORMAT.parse(DATE_FORMAT.format(arg1));
                } catch (ParseException ex) {
                    logger.error("convert 1 ParseException:\n", ex);
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
            if ( p.trim().length() == 0 ) {
                return null;
            }
            try {
                return DATETIME_FORMAT.parse(p.trim());
            } catch (ParseException e) {
                try {
                    return DATE_FORMAT.parse(p.trim());
                } catch (ParseException ex) {
                    logger.error("convert 2 ParseException:\n", ex);
                    return null;
                }
            }
        }
    }

    public static String formatDateTime(Object obj) {
        if (obj != null) {
            return DATE_FORMAT.format(obj);
        } else {
            return "";
        }
    }

    public static String formatLongDateTime(Object obj) {
        if (obj != null) {
            return DATETIME_FORMAT.format(obj);
        } else {
            return "";
        }
    }
}
