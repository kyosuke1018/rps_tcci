package com.tcci.storegateway.requesthandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class EntityEnclosingRequestHandler extends RequestHandlerBase {

    @Override
    public HttpMethod process(HttpServletRequest request, String url) throws IOException {

        EntityEnclosingMethod method = null;

        if (request.getMethod().equalsIgnoreCase("POST")) {
            method = new PostMethod(url);
        } else if (request.getMethod().equalsIgnoreCase("PUT")) {
            method = new PutMethod(url);
        }

        setHeaders(method, request);

        InputStreamRequestEntity stream;
        stream = new InputStreamRequestEntity(request.getInputStream());
        method.setRequestEntity(stream);
        method.setRequestHeader("Content-type", request.getContentType());

        return method;

    }

}
