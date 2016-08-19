/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.solr.server.util;

import com.tcci.solr.client.model.TcSolrSource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class JmsUtils {
    private static Logger logger = LoggerFactory.getLogger(JmsUtils.class);

    /**
     * 轉換 MQ message to TcSolrSource
     * @param msg
     * @return
     * @throws JMSException 
     */
    public static TcSolrSource convertMessage(Message msg) throws JMSException{
        if (msg !=null && msg instanceof ObjectMessage) {
            ObjectMessage obj = (ObjectMessage)msg;
            TcSolrSource data = (TcSolrSource) obj.getObject();

            return data;
        }
        
        return null;
    }
}
