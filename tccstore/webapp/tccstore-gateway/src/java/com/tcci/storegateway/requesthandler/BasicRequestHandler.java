package com.tcci.storegateway.requesthandler;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;

public class BasicRequestHandler extends RequestHandlerBase {

    @Override
    public HttpMethod process(HttpServletRequest request, String url) throws HttpException {
        HttpMethodBase method = null;
      
        if (request.getMethod().equalsIgnoreCase("GET")) {
            method = new GetMethod(url);
        } else if (request.getMethod().equalsIgnoreCase("HEAD")) {
            method = new HeadMethod(url);
        } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
            method = new DeleteMethod(url);
        } else {
            return null;
        }
        
        setHeaders(method, request);
        return method;
    }
      

}
