/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.exception;

/**
 *
 * @author Peter.pan
 */
public class CmCustomException extends Exception {

    public CmCustomException(String message) {
        super(message);
    }

    public CmCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CmCustomException(Throwable ex) {
        super(ex);
    }
    
}
