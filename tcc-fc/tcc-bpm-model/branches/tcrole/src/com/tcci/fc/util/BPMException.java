/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

/**
 *
 * @author Jason.Yu
 */
public class BPMException extends Exception{
    public BPMException() {
    }
    public BPMException(String message) {
        super(message);
    }
    public String getMessage() {
        return super.getMessage();
    }
    public BPMException(Throwable e) {
        super(e);
    }

    public BPMException(String message, Throwable e) {
        super(message, e);
    }    
}
