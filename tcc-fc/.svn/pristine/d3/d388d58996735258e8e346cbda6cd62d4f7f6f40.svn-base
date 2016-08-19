package com.tcci.fc.util.sap.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JCO connection
 *
 * @author Jackson.Lee
 */
public class JCoConnection {
    
    protected static final Logger logger = Logger.getLogger(JCoConnection.class.getCanonicalName());
    private static JCoDestination destination = null;
    SingltonSAPDestinationDataProvider provider = null;
    
    public JCoConnection() {
    }

    /**
     * Connect SAP
     */
    public JCoDestination connect(Properties jcoProps) {
        String destinationName = jcoProps.getProperty("jco.destinationName");
        
        try {
            //Get Destination
            destination = getConnection(destinationName, jcoProps);
        } catch (Exception ex) {
            throw new RuntimeException("Connect SAP system failed!", ex);
        }
        return destination;
    }

    /**
     * Disconnect SAP
     */
    public void closeConnection() {
        //TODO: 多Client連同一dest會關閉問題
        //if (Environment.isDestinationDataProviderRegistered() && provider != null) {
        //  Environment.unregisterDestinationDataProvider(provider);
        //}
    }

    /**
     * Connect SAP by JCO and get JCO Destination.
     *
     * @param destinationName
     * @param jcoProp
     * @return
     */
    private synchronized JCoDestination getConnection(String destinationName, Properties jcoProps) {
        JCoDestination dest = null;
        String msg;
        try {
            provider = SingltonSAPDestinationDataProvider.getSingltonSAPDestinationDataProvider();
            if( provider.getDestinationProperties(destinationName)==null ){
                provider.addDestination(destinationName, jcoProps);
            }

            if (!Environment.isDestinationDataProviderRegistered()) {
                //Register the CustomSAPDestinationDataProvider   
                Environment.registerDestinationDataProvider(provider);
                logger.log(Level.SEVERE, "Environment registerDestinationDataProvider ...");
            }

            //Get JCO settings and connect to SAP
            //logger.info("Establish connection to SAP....");
            //logger.info("Get SAP Destination....");
            //clearRepository(destinationName);
            dest = JCoDestinationManager.getDestination(destinationName);
            //clearRepository(dest);
            msg = "JCO connect success! destinationName=" + destinationName;
            logger.info(msg);
            //logger.info("JCO Attributes:");
            //logger.info(dest.getAttributes().toString());
        } catch (Exception ex) {
            msg = "getConnection => JCO connect Fail: name=" + destinationName + " => " + ex.getMessage();
            logger.log(Level.SEVERE, msg);
            // provider.removeDestination(destinationName);
            throw new RuntimeException("getConnection => Connect SAP system failed!", ex);
        } catch (Error err) {
            // provider.removeDestination(destinationName);
            throw new RuntimeException(err.getMessage(), err);
        }
        
        return dest;
    }
    
    /**
     * 重設特定 JCO destination
     * @param destinationName
     * @param jcoProps 
     */
    public synchronized void resetProperties(String destinationName, Properties jcoProps) {
        provider = SingltonSAPDestinationDataProvider.getSingltonSAPDestinationDataProvider();
        provider.addDestination(destinationName, jcoProps);

        Environment.registerDestinationDataProvider(provider);
    }
    
    /**
     * 清除 Destination 的 Repository (用於RFC有變更規格時)
     * @param destinationName
     * @throws JCoException 
     */
    public void clearRepository(String destinationName) throws JCoException{
        if( provider.getDestinationProperties(destinationName)!=null ){
            clearRepository(JCoDestinationManager.getDestination(destinationName));
        }
    }

    /**
     * 清除 Destination 的 Repository (用於RFC有變更規格時)
     * @param jcoDestination
     * @throws JCoException 
     */
    public void clearRepository(JCoDestination jcoDestination) throws JCoException{
        if( jcoDestination!=null ){
            logger.log(Level.SEVERE, "before clearRepository {0}...{1}", new Object[]{jcoDestination.getDestinationName(), (new Date()).toString()});
            jcoDestination.getRepository().clear();
            logger.log(Level.SEVERE, "after clearRepository {0}...{1}", new Object[]{jcoDestination.getDestinationName(), (new Date()).toString()});
        }
    }
}
