package com.tcci.storegateway.requesthandler;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.TraceMethod;

public class MaxForwardRequestHandler extends RequestHandlerBase {

    @Override
    public HttpMethod process(HttpServletRequest request, String url) throws IOException {
        HttpMethodBase method = null;

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            method = new OptionsMethod(url);
        } else if (request.getMethod().equalsIgnoreCase("TRACE")) {
            method = new TraceMethod(url);
        } else {
            return null;
        }

        try {
            int max = request.getIntHeader("Max-Forwards");
            if (max == 0 || request.getRequestURI().equals("*")) {
                setAllHeaders(method, request);
                method.abort();
            } else if (max != -1) {
                setHeaders(method, request);
                method.setRequestHeader("Max-Forwards", "" + max--);
            } else {
                setHeaders(method, request);
            }
        } catch (NumberFormatException e) {
        }
        return method;
    }

    private void setAllHeaders(HttpMethod method, HttpServletRequest request) {
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            Enumeration value = request.getHeaders(name);
            while (value.hasMoreElements()) {
                method.addRequestHeader(name, (String) value.nextElement());
            }
        }
    }

}
