package com.tcci.storegateway.responsehandler;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.methods.HeadMethod;

public class HeadResponseHandler extends ResponseHandlerBase {
    
    public HeadResponseHandler(HeadMethod method) {
        super(method);
    }

    @Override
    public void process(HttpServletResponse response) {
        setHeaders(response);
        response.setStatus(getStatusCode());
    }

}
