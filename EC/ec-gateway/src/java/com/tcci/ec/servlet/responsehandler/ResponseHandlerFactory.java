package com.tcci.ec.servlet.responsehandler;

import com.tcci.ec.servlet.requesthandler.AllowedMethodHandler;
import com.tcci.ec.servlet.requesthandler.MethodNotAllowedException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.TraceMethod;

public class ResponseHandlerFactory {

    private static final String handledMethods = "OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE";

    public static ResponseHandler createResponseHandler(HttpMethod method) throws MethodNotAllowedException {
        if (!AllowedMethodHandler.methodAllowed(method)) {
            throw new MethodNotAllowedException("The method " + method.getName() + " is not in the AllowedHeaderHandler's list of allowed methods.", AllowedMethodHandler.getAllowHeader());
        }

        ResponseHandler handler = null;
        if (method.getName().equals("OPTIONS")) {
            handler = new OptionsResponseHandler((OptionsMethod) method);
        } else if (method.getName().equals("GET")) {
            handler = new GetResponseHandler((GetMethod) method);
        } else if (method.getName().equals("HEAD")) {
            handler = new HeadResponseHandler((HeadMethod) method);
        } else if (method.getName().equals("POST")) {
            handler = new PostResponseHandler((PostMethod) method);
        } else if (method.getName().equals("PUT")) {
            handler = new PutResponseHandler((PutMethod) method);
        } else if (method.getName().equals("DELETE")) {
            handler = new DeleteResponseHandler((DeleteMethod) method);
        } else if (method.getName().equals("TRACE")) {
            handler = new TraceResponseHandler((TraceMethod) method);
        } else {
            throw new MethodNotAllowedException("The method " + method.getName() + " was allowed by the AllowedMethodHandler, not by the factory.", handledMethods);
        }

        return handler;
    }

}
