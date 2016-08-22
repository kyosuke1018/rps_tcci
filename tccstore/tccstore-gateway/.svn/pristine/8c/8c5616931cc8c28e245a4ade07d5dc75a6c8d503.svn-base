package com.tcci.storegateway.requesthandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RequestHandlerBase implements RequestHandler {

    private static final Set bannedHeaders = new HashSet();
    private static Log log = LogFactory.getLog(RequestHandlerBase.class);

    @Override
    public abstract HttpMethod process(HttpServletRequest request, String url) throws IOException;

    protected void setHeaders(HttpMethod method, HttpServletRequest request) throws HttpException {
        Enumeration headers = request.getHeaderNames();
        String connectionToken = request.getHeader("connection");

        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            boolean isToken = (connectionToken != null && name.equalsIgnoreCase(connectionToken));

            if (!isToken && !bannedHeaders.contains(name.toLowerCase())) {
                Enumeration value = request.getHeaders(name);
                while (value.hasMoreElements()) {
                    method.addRequestHeader(name, (String) value.nextElement());
                }
            }
        }

        setProxySpecificHeaders(method, request);
    }

    private void setProxySpecificHeaders(HttpMethod method, HttpServletRequest request) throws HttpException {
        String serverHostName = "jEasyExtensibleProxy";
        try {
            serverHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Couldn't get the hostname needed for headers x-forwarded-server and Via", e);
        }

        String originalVia = request.getHeader("via");
        StringBuilder via = new StringBuilder("");
        if (originalVia != null) {
            if (originalVia.contains(serverHostName)) {
                log.error("This proxy has already handled the request, will abort.");
                throw new HttpException("Request has a cyclic dependency on this proxy.");
            }
            via.append(originalVia).append(", ");
        }
        via.append(request.getProtocol()).append(" ").append(serverHostName);

        method.setRequestHeader("via", via.toString());
        method.setRequestHeader("x-forwarded-for", request.getRemoteAddr());
        method.setRequestHeader("x-forwarded-host", request.getServerName());
        method.setRequestHeader("x-forwarded-server", serverHostName);

        method.setRequestHeader("accept-encoding", "");
    }

    public static void addBannedHeader(String header) {
        bannedHeaders.add(header);
    }

    public static void addBannedHeaders(String headers) {
        StringTokenizer tokenizer = new StringTokenizer(headers, ",");
        while (tokenizer.hasMoreTokens()) {
            bannedHeaders.add(tokenizer.nextToken().trim().toLowerCase());
        }
    }

}
