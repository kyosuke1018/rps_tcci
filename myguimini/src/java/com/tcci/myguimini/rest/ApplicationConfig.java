/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Egg.chen
 */
@javax.ws.rs.ApplicationPath("service")
public class ApplicationConfig extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
//        // following code can be used to customize Jersey 1.x JSON provider:
//        try {
//            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
//            resources.add(jacksonProvider);
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        addRestResourceClasses(resources);
//        return resources;
//    }
    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * re-generated by NetBeans REST support to populate given list with all
     * resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tcci.myguimini.facade.MyServiceFacade.class);
    }
}
