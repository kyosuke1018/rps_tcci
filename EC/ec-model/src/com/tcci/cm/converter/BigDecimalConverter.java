/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.converter;

import java.math.BigDecimal;
import org.apache.commons.beanutils.Converter;

/**
 *
 * @author Peter.pan
 */
public class BigDecimalConverter implements Converter {

    @Override
    public Object convert(Class arg0, Object arg1) {
        if( arg1==null ) {
            return null;
        }
        
        String className = arg1.getClass().getName();

        if ("java.math.BigDecimal".equalsIgnoreCase(className)) {
            if( arg0.getName().equals(Double.class.getName()) ){
                return ((BigDecimal)arg1).doubleValue();
            }else if( arg0.getName().equals(Long.class.getName()) ){
                return ((BigDecimal)arg1).longValue();
            }
        }
        
        return arg1;
    }
    
}
