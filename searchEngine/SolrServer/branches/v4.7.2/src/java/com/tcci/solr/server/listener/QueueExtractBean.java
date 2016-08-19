/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.listener;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.proxy.TcSolrUpdateProxy;
import com.tcci.solr.server.conf.ConfigManager;
import com.tcci.solr.server.util.JmsUtils;
import java.io.IOException;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessageListener implementation
 * @author Peter.pan
 */
@MessageDriven(mappedName="jms/Solr.Queue.1", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                                  propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                                  propertyValue = "javax.jms.Queue")
        })
public class QueueExtractBean implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(QueueExtractBean.class);  
    
    @Resource private MessageDrivenContext mdc;

    @Override
    public void onMessage(Message message) {
        logger.info("onMessage ...");
        int status = TcSolrConfig.RESPONSE_SOLR_FAIL;
        TcSolrSource tcSolrSource = null;
        try {
            // 將 Queue 傳送來的 Object 轉回 TcSolrSource 物件
            tcSolrSource = JmsUtils.convertMessage(message);
            logger.info(tcSolrSource.toString());
            
            // Solr 儲存檔案在 Solr Server 實體的路徑，方便日後 reindex
            //tcSolrSource.setPath(convertPath(tcSolrSource.getPath()));
            tcSolrSource.setPath(convertPath(tcSolrSource.getSource(), tcSolrSource.getPath()));// 傳換路徑 // 存放 Solr Server 實際路徑
            
            // Run Solr Extract
            TcSolrUpdateProxy proxy = new TcSolrUpdateProxy();
            status = proxy.extract(tcSolrSource);
            logger.info("onMessage extract status = "+status);
            
        } catch (JMSException ex) {
            logger.error("onMessage JMSException: \n", ex);
        } catch (IOException ex) {
            logger.error("onMessage IOException: \n", ex);
        } catch (SolrServerException ex) {
            logger.error("onMessage SolrServerException: \n", ex);
        } catch (SolrProxyException ex) {
            logger.error("onMessage SolrProxyException: \n", ex);
        }
        
        if( status != TcSolrConfig.RESPONSE_SOLR_OK ){
            // mdc.setRollbackOnly();
            logger.error("onMessage status ="+status);
            if( tcSolrSource != null ){
                logger.error("tcSolrSource ="+tcSolrSource);
            }
        }
    }
    
    /**
     * 轉換為 Solr Server 路徑
     * @param sourcePath
     * @return 
     */
    public String convertPath(String source, String srcpath){
        // String root = ConfigManager.SOURCE_DOC_ROOT;
        // String path = root + sourcePath.replace('\\', '/');
        
        String path = ConfigManager.getDirBySource(source);
        if( path==null ){
            path = srcpath;
        }
        
        return path;
    }
}
