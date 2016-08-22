package com.tcci.storegateway.responsehandler;

import com.tcci.storegateway.requesthandler.RequestHandlerBase;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.LogFactory;

public abstract class ResponseHandlerBase implements ResponseHandler {

    protected HttpMethod method;

    public ResponseHandlerBase(HttpMethod method) {
        this.method = method;
    }

    @Override
    public abstract void process(HttpServletResponse response) throws IOException;

    @Override
    public void close() {
        method.releaseConnection();
    }

    @Override
    public int getStatusCode() {
        return method.getStatusCode();
    }

    protected void sendStreamToClient(ServletResponse response) throws IOException {
        InputStream streamFromServer = method.getResponseBodyAsStream();
        OutputStream responseStream = response.getOutputStream();

        if (streamFromServer != null) {
            byte[] buffer = new byte[1024];
            int read = streamFromServer.read(buffer);
            while (read > 0) {
                responseStream.write(buffer, 0, read);
                read = streamFromServer.read(buffer);
            }
            streamFromServer.close();
        }
        responseStream.flush();
        responseStream.close();
    }

    protected void setHeaders(HttpServletResponse response) {
        Header[] headers = method.getResponseHeaders();

        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            String name = header.getName();
            boolean contentLength = name.equalsIgnoreCase("content-length");
            boolean connection = name.equalsIgnoreCase("connection");

            if (!contentLength && !connection) {
                response.addHeader(name, header.getValue());
            }
        }

        setViaHeader(response);
    }

    private void setViaHeader(HttpServletResponse response) {
        String serverHostName = "jEasyReverseProxy";
        try {
            serverHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LogFactory.getLog(RequestHandlerBase.class).error("Couldn't get the hostname needed for header Via", e);
        }

        Header originalVia = method.getResponseHeader("via");
        StringBuilder via = new StringBuilder("");
        if (originalVia != null) {
            via.append(originalVia.getValue()).append(", ");
        }
        via.append(method.getStatusLine().getHttpVersion()).append(" ").append(serverHostName);

        response.setHeader("via", via.toString());
    }

}
