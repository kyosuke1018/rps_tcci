/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.velocity.app.Velocity;
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
        // for Velocity
        initVelocityEngine(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    private void initVelocityEngine(ServletContext context) {
        try {
            String LOADER_PATH= "velocity.webapp.resource.loader.path";
            String loader_path = context.getInitParameter(LOADER_PATH);
            Velocity.setProperty(Velocity.RESOURCE_LOADER, "webapp");
            Velocity.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
            Velocity.setProperty("webapp.resource.loader.path", loader_path);
            Velocity.setProperty(Velocity.ENCODING_DEFAULT,"UTF-8");
            Velocity.setProperty(Velocity.INPUT_ENCODING,"UTF-8");
            Velocity.setProperty(Velocity.OUTPUT_ENCODING,"UTF-8");
            Velocity.setApplicationAttribute("javax.servlet.ServletContext", context);
            Velocity.init();
            logger.info("initVelocityEngine init ...");
        } catch (Exception ex) {
            logger.info("TCApplicationListener initVelocityEngine exception :\n", ex);
        }
    }
}
