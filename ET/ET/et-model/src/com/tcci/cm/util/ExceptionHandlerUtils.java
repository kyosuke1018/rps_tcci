/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import com.tcci.fc.util.DateUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class ExceptionHandlerUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtils.class);
    /**
     *	This method takes a exception as an input argument and returns the stacktrace as a string.
     * @param exception
     * @return 
     */    
    public static String getStackTrace(Throwable exception){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if( exception==null ){
            return "";
        }
        try{
            exception.printStackTrace(pw);
        }catch(Exception e){
            return "";
        }
        
        return sw.toString();
    }  
    
    /**
     * 
     * @param prefixMsg
     * @param exception
     * @return 
     */
    public static String getSimpleMessage(String prefixMsg, Throwable exception){
        StringBuilder sb = new StringBuilder().append(prefixMsg).append("(")
                .append(DateUtils.format(new Date())).append(exception.getMessage()).append(")");
        return sb.toString();
    }
    
    /**
     * 顯示 ConstraintViolationException 資訊
     * @param e
     * @return 
     */
    public static String printConstraintViolationException(EJBException e){
        logger.error("printConstraintViolationException ...");
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("ThrowableResultIgnored")
        Exception cause = e.getCausedByException();
        if (cause instanceof ConstraintViolationException) {
            @SuppressWarnings("ThrowableResultIgnored")
            ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
            for (Iterator<ConstraintViolation<?>> it = cve.getConstraintViolations().iterator(); it.hasNext();) {
                ConstraintViolation<? extends Object> v = it.next();
                sb.append(v).append("\n");
                logger.error("EJBException: \n" + v);
            }
        }
        return sb.toString();
    }
}
