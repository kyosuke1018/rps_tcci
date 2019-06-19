/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.util;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class ConfigUtils {
    protected final static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    
    /**
     * get Properties From JNDI
     * @param jndiName
     * @return 
     */
    public static Properties getPropertiesFromJNDI(String jndiName){
        try{
            // 自 JNDI 讀取 source 與 實體檔案路徑 對應設定
            Context ctx = new InitialContext();
            Properties jndiProperties = (Properties)ctx.lookup(jndiName);
            /*if( jndiProperties==null ){
                logger.warn("getPropertiesFromJNDI warn: "+jndiName+" jndiProperties is null !");
            }*//*else{
                String name = FILE_DIRECTORY_PREFIX+source;
                String dir = jndiProperties.getProperty(name);
                logger.info("getDirBySource name="+name+"; dir="+dir);
                return dir;
            }*/
            
            return jndiProperties;
        }catch(NamingException e){
            logger.warn("prepareSolrConfig warn: no jndi = "+jndiName);
        }
        return null;
    }
    
    /**
     * get one Properties From JNDI
     * @param jndiName
     * @param propName
     * @param defValue
     * @param clazz
     * @return 
     */
    public static Object getPropFromJNDI(String jndiName, String propName, String defValue, Class clazz){
        Properties jndiProperties = getPropertiesFromJNDI(jndiName);
        String value = defValue;
        if( jndiProperties!=null ){
            value = jndiProperties.getProperty(propName);
        }
        
        if( value==null ){
            value = defValue;
        }
        return clazz.cast(value);
    }
    
    /**
     * 指定 JNDI 取出指定屬性
     * @param jndiName
     * @param propName
     * @param defValue
     * @return 
     */
    public static String getPropFromJNDI(String jndiName, String propName, String defValue){
        return (String) getPropFromJNDI(jndiName, propName, null, String.class);
    }
    
}
