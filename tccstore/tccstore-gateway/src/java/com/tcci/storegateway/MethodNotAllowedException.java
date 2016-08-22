package com.tcci.storegateway;

public class MethodNotAllowedException extends Exception {

    private static final long serialVersionUID = 1L;
    private String allowedMethods;

    public MethodNotAllowedException(String message, String allowedMethods) {
        super(message);
        this.allowedMethods = allowedMethods;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }

}
