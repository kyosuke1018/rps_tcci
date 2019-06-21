/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util.ssoclient;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClientException extends Exception {

    private int status; // http status

    public SSOClientException(String message, int status) {
        super(message);
        this.status = status;
    }

    public SSOClientException(String message) {
        super(message);
    }

    public SSOClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSOClientException(Throwable ex) {
        super(ex);
    }

    // getter, setter
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
