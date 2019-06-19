/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Provider
@PreMatching
public class RESTCorsRequestFilter implements ContainerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {
        logger.info( "Executing REST request filter Uri = " + requestCtx.getUriInfo().getPath() + ", method = "+requestCtx.getMethod());
        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            //logger.info( "HTTP Method (OPTIONS) - Detected!" );
            // Just send a OK signal back to the browser
            requestCtx.abortWith( Response.status( Response.Status.OK ).build() );
        }
    }
}