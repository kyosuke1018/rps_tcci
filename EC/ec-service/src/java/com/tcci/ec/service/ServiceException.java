/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ServiceException extends WebApplicationException {

    private final static Logger logger = LoggerFactory.getLogger(ServiceException.class);
    private final static String ERROR_PREFIX = "ERR:";

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST)
                      .entity(ERROR_PREFIX + message)
                      .type(MediaType.TEXT_PLAIN)
                      .build());
        logger.error(message, this);
    }

}
