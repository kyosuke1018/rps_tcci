package com.tcci.ec.servlet.requesthandler;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityEnclosingRequestHandler extends RequestHandlerBase {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public HttpMethod process(HttpServletRequest request, String url) throws IOException {

        HttpMethod method = null;

        if (request.getMethod().equalsIgnoreCase("POST")) {
            method = new PostMethod(url);
        } else if (request.getMethod().equalsIgnoreCase("PUT")) {
            method = new PutMethod(url);
        }

        setHeaders(method, request);
            InputStreamRequestEntity stream;
            stream = new InputStreamRequestEntity(request.getInputStream());
            ((EntityEnclosingMethod) method).setRequestEntity(stream);
        method.setRequestHeader("Content-type", request.getContentType());

        return method;

    }

}
