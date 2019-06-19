/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.sql.DataSource;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class SecurityUtils {
    private final static Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);
    
    /**
     * get caller from Assertion
     * @param assertion
     * @return 
     */
    public static String getCallerFromAssertion(Assertion assertion){
        return (assertion!=null && assertion.getPrincipal()!=null)?
                assertion.getPrincipal().getName():null;
    }
    
    /**
     * get CredentialValidationResult By Assertion
     * @param assertion
     * @return 
     */
    public static CredentialValidationResult getValidationResultByAssertion(Assertion assertion){
        String caller = getCallerFromAssertion(assertion);
        return new CredentialValidationResult(new CallerPrincipal(caller));
    }

    /**
     * get JDBC DataSource
     * @param jdbcName
     * @return 
     */
    public static DataSource getDataSource(String jdbcName){
        try{
            if( jdbcName!=null ){
                Context ctx = new InitialContext();
                return (DataSource)ctx.lookup(jdbcName);
            }
        }catch(Exception e){
            LOG.error("getDataSource Exception jdbcName = "+jdbcName+":\n", e);
        }
        return null;
    }
    
    /**
     * get system properties setting
     * @param name
     * @param defValue
     * @return 
     */
    public static String getFromSysProps(String name, String defValue){
        String conf = System.getProperty(name);
        LOG.info("getFromJNDI {} = {}", name, conf);
        return (conf==null || conf.trim().isEmpty())?defValue:conf;
    }
    
    /**
     * get JNDI setting
     * @param jndiConfig
     * @param name
     * @param defValue
     * @return
     */
    public static String getFromJNDI(Properties jndiConfig, String name, String defValue){
        if( jndiConfig==null ){
            return defValue;
        }
        String conf = jndiConfig.getProperty(name);
        LOG.info("getFromJNDI {} = {}", name, conf);
        return (conf==null || conf.trim().isEmpty())?defValue:conf;
    }
    
    /**
     * get JNDI Properties
     * @param jdbcName
     * @return 
     */
    public static Properties getJndiProperties(String jdbcName){
        try{
            if( jdbcName!=null ){
                Context ctx = new InitialContext();
                return (Properties)ctx.lookup(jdbcName);
            }
        }catch(Exception e){
            LOG.error("getJndiProperties Exception jdbcName = "+jdbcName+": {}", e.getMessage());
        }
        return null;
    }
}
