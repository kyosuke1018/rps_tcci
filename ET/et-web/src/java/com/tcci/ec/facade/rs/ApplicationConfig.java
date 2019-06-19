/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author Peter.pan
 */
@javax.ws.rs.ApplicationPath("services")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        resources.add(MultiPartFeature.class);
        resources.add(JacksonFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.tcci.cm.exception.RsExceptionMapper.class);
        resources.add(com.tcci.cm.facade.rs.filter.JWTTokenNeededFilter.class);
        resources.add(com.tcci.cm.facade.rs.filter.RESTCorsRequestFilter.class);
        resources.add(com.tcci.cm.facade.rs.filter.RESTCorsResponseFilter.class);
        resources.add(com.tcci.cm.resolver.JacksonMapperProvider.class);
        resources.add(com.tcci.ec.facade.rs.AuthREST.class);
        resources.add(com.tcci.ec.facade.rs.JacksonConfigurator.class);
        resources.add(com.tcci.ec.facade.rs.MemberREST.class);
        resources.add(com.tcci.ec.facade.rs.OptionsREST.class);
        resources.add(com.tcci.ec.facade.rs.SystemREST.class);
        resources.add(com.tcci.ec.facade.rs.TenderREST.class);
        resources.add(com.tcci.ec.facade.rs.VenderREST.class);
    }
    
}
