/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.conf;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Peter
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    
    // for JNDI and System Properties
    private static final String PRIVATE_JNDI_NAME = "jndi/solrServer.config";
    private static final String FILE_DIRECTORY_PREFIX = "file.dir.";
    
    // Queue
    public static String QUEUE_FACTORY = "jms/SolrQueueConnectionFactory";
    public static String QUEUE_NAME = "jms/Solr.Queue.1"; 
    
    public static String SOURCE_DOC_ROOT = "D:/BAK/solr";
    
    public static int DEF_INDEX_NUM = 100; // 製作索引每次處理筆數
    
    /**
     * 依 Source 取得檔案實際存放位置
     */
    public static String getDirBySource(String source){
        try{
            // 自 JNDI 讀取 source 與 實體檔案路徑 對應設定
            Context ctx = new InitialContext();
            Properties jndiProperties = (Properties)ctx.lookup(PRIVATE_JNDI_NAME);
            if( jndiProperties==null ){
                logger.warn("prepareSolrConfig warn: "+PRIVATE_JNDI_NAME+" jndiProperties is null !");
            }else{
                String name = FILE_DIRECTORY_PREFIX+source;
                String dir = jndiProperties.getProperty(name);
                logger.info("getDirBySource name="+name+"; dir="+dir);
                return dir;
            }
        }catch(NamingException e){
            logger.warn("prepareSolrConfig warn: no jndi = "+PRIVATE_JNDI_NAME);
        }
        return null;
    }
}
