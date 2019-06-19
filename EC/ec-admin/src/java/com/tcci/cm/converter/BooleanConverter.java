/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.converter;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class BooleanConverter implements Converter {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public Object convert(Class arg0, Object arg1) {
        //logger.debug("BooleanConverter convert ... arg0 = "+arg0+"; arg1="+arg1);
        if( arg1==null ) {
            return null;
        }
        
        return arg1;
    }
    
}
