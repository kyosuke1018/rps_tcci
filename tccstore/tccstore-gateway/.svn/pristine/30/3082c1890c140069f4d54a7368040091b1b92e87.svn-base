package com.tcci.storegateway.responsehandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.TraceMethod;

public class TraceResponseHandler extends ResponseHandlerBase {

    private boolean proxyTargeted;

    public TraceResponseHandler(TraceMethod method) {
        super(method);
        proxyTargeted = !method.hasBeenUsed();
    }

    @Override
    public void process(HttpServletResponse response) throws IOException {

        if (proxyTargeted) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("content-type", "message/http");
            response.setHeader("Connection", "close");

            String path = method.getPath();
            String protocol = method.getParams().getVersion().toString();
            PrintWriter writer = response.getWriter();
            writer.println("TRACE " + path + " " + protocol);
            Header[] headers = method.getRequestHeaders();
            for (int i = 0; i < headers.length; i++) {
                writer.print(headers[i]);
            }
            writer.flush();
            writer.close();

        } else {
            setHeaders(response);
            response.setStatus(getStatusCode());
            sendStreamToClient(response);
        }
    }

    @Override
    public int getStatusCode() {
        if (proxyTargeted) {
            return 200;
        } else {
            return super.getStatusCode();
        }
    }

}
