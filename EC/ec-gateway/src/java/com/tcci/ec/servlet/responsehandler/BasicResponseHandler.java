package com.tcci.ec.servlet.responsehandler;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpMethod;

public class BasicResponseHandler extends ResponseHandlerBase {

    public BasicResponseHandler(HttpMethod method) {
        super(method);
    }

    @Override
    public void process(HttpServletResponse response) throws IOException {
        setHeaders(response);
        response.setStatus(getStatusCode());
        sendStreamToClient(response);
    }

}
