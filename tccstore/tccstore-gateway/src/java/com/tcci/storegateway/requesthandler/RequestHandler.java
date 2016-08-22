package com.tcci.storegateway.requesthandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpMethod;

public interface RequestHandler {

    public HttpMethod process(HttpServletRequest request, String url) throws IOException;

}