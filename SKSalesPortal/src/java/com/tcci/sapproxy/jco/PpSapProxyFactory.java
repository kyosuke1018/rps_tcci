/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sapproxy.jco;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.util.GlobalConstant;
import com.tcci.sapproxy.PpProxy;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProxyFactory for SAPService
 * @author Peter.pan
 */
public class PpSapProxyFactory {
    private static Logger logger = LoggerFactory.getLogger(PpSapProxyFactory.class);
    public static PpProxy createProxy(String sapClientCode, String operator) {
        return createProxy(sapClientCode, operator, null);
    }
    public static PpProxy createProxy(String sapClientCode, String operator, List<TcUser> users) {
        String sapServiceUrl = getSapServiceUrlFromJndi();
        logger.debug("createProxy sapClientCode = " + sapClientCode + ", sapServiceUrl = "+sapServiceUrl);
        if (null == sapClientCode || null == sapServiceUrl) {
            throw new RuntimeException("sapClientCode is null || sapServiceUrl is null");
        }
        
        PpProxy ppProxy = new PpSapProxyImpl();
        ppProxy.init(sapClientCode, sapServiceUrl, operator);
        if( users!=null ){
            ppProxy.setNotifyUsers(users);
        }

        logger.debug("createProxy " + ppProxy.getClass().getName() + " object created!");
        return ppProxy;
    }

    public static String getSapServiceUrlFromJndi(){
        String sapServiceUrl = null;
        try {
            Context ctx = new InitialContext();
            Properties properties = (Properties)ctx.lookup(GlobalConstant.JNDI_GLOBAL);// jndi/global.config
            if( properties!=null ){
                sapServiceUrl = properties.getProperty(GlobalConstant.JNDI_SAP_SERVICE_REST);
            }
        }catch(Exception e){
            logger.error("getGlobalJndiProperties exception:\n");
        }
        return sapServiceUrl;
    }
}
