/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter Pan
 */
public class TCApplicationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TCApplicationListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // for SOLR
        //TcSolrConfig.init(GlobalConstant.JNDI_NAME_PRIVATE);
        //logger.info("contextInitialized TcSolrConfig.getSourceAP() = "+TcSolrConfig.getSourceAP());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
