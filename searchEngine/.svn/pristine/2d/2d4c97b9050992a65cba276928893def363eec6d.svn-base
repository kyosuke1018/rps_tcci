/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.servlet.listener;

import com.tcci.solr.client.conf.TcSolrConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.Pan
 */
public class TCApplicationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TCApplicationListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String solrSourceAP = sce.getServletContext().getContextPath().replace("/", "");
        TcSolrConfig.setSourceAP(solrSourceAP);
        logger.info("contextInitialized .. solrSourceAP = " + solrSourceAP);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
   
}
