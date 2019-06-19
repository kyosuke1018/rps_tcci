/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.servlet.requesthandler;

import java.util.HashMap;

/**
 *
 * @author Jimmy.Lee
 */
public class RequestHandlerFactory {

    private static HashMap requestHandlers;
    private static final String handledMethods = "OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE";
    private static final String bannedHeaders = "connection,accept-encoding,via,x-forwarded-for,x-forwarded-host,x-forwarded-server";

    static {
        RequestHandlerBase.addBannedHeaders(bannedHeaders);

        requestHandlers = new HashMap();
        MaxForwardRequestHandler optionsAndTrace = new MaxForwardRequestHandler();
        BasicRequestHandler basic = new BasicRequestHandler();
        EntityEnclosingRequestHandler entityEnclosing = new EntityEnclosingRequestHandler();

        requestHandlers.put("OPTIONS", optionsAndTrace);
        requestHandlers.put("GET", basic);
        requestHandlers.put("HEAD", basic);
        requestHandlers.put("POST", entityEnclosing);
        requestHandlers.put("PUT", entityEnclosing);
        requestHandlers.put("DELETE", basic);
        requestHandlers.put("TRACE", optionsAndTrace);
    }

    public static RequestHandler createRequestMethod(String method) throws MethodNotAllowedException {
        if (!AllowedMethodHandler.methodAllowed(method)) {
            throw new MethodNotAllowedException("The method " + method + " is not in the AllowedHeaderHandler's list of allowed methods.", AllowedMethodHandler.getAllowHeader());
        }

        RequestHandler handler = (RequestHandler) requestHandlers.get(method.toUpperCase());
        if (handler == null) {
            throw new MethodNotAllowedException("The method " + method + " was allowed by the AllowedMethodHandler, not by the factory.", handledMethods);
        } else {
            return handler;
        }
    }
}
