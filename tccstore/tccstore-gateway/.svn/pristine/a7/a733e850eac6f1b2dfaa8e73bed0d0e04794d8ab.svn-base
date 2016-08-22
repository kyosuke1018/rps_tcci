/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClientException extends Exception {

    private static final long serialVersionUID = 1L;

    private Exception origException;
    private int statusCode;

    public SSOClientException() {
        super();
    }

    public SSOClientException(Exception ex) {
        super(ex.getMessage());
        this.origException = ex;
    }

    public SSOClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Exception getOrigException() {
        return origException;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
