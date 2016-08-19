/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service;

import java.io.Serializable;

/**
 *
 * @author Jimmy.Lee
 */
public class ServiceException extends RuntimeException implements Serializable {
    private final static String ERROR_PREFIX = "ERR:";
    
    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(ERROR_PREFIX + msg);
    }

    public ServiceException(String msg, Exception ex) {
        super(ERROR_PREFIX + msg, ex);
    }

}
