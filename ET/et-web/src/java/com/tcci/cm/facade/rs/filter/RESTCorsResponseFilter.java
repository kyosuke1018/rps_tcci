/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache HTTP Server .htaccess file add
 * @author Peter.pan 
 */
@Provider
@PreMatching
public class RESTCorsResponseFilter implements ContainerResponseFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {
        logger.info( "filter url = "+requestCtx.getUriInfo().getRequestUri().getPath());
        if( responseCtx.getStatus()!=200 ){
            logger.info( "filter response status = "+responseCtx.getStatus());
            logger.info( "filter response msg = "+((responseCtx.getStatusInfo()!=null)?responseCtx.getStatusInfo().getReasonPhrase():""));
        }
        
        responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*" );
        responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS" );
        responseCtx.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Accept, Authorization, Custom-Language");
        //responseCtx.getHeaders().add("Access-Control-Allow-Headers", "*");// 使用萬用字元 firefox 會有問題
        // How long the results of a request can be cached in a result cache.
        //responseCtx.getHeaders().add("Access-Control-Max-Age", "86400");
        responseCtx.getHeaders().add("Cache-Control", "no-cache");
        
        /*if( responseCtx.getHeaders()!=null ){
            for(String key : responseCtx.getHeaders().keySet()){
                logger.debug("filter "+key+" = "+responseCtx.getHeaders().getFirst(key));
            }
        }*/
    }
}