/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;

/**
 *
 * @author gilbert
 */

/**
 * 
 * @author gilbert
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils{
    /**
     * BeanUtils.copyProperties(Form, mappingObj);
     * 出現No value specified for 'Date' Error
     * @param dest
     * @param orig
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void copyProperties(Object dest, Object orig) 
            throws IllegalAccessException, InvocationTargetException{
        ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
        org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
    }
}
