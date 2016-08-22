/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.tccweb.rs.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 實作 Cross-Origin Resource Sharing (CORS) 解決 Ajax 發送跨網域存取 Request
 * @author Jackson.Lee
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
    
    private final static Logger logger = LoggerFactory.getLogger(CORSFilter.class);
    
    @Override
    public void filter(ContainerRequestContext creq, ContainerResponseContext cresp) throws IOException {
        cresp.getHeaders().add("Access-Control-Allow-Origin", "*");
        cresp.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        cresp.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cresp.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cresp.getHeaders().add("Access-Control-Max-Age", "1209600");
        logger.debug("CORSFilter add header!!");
    }
}
