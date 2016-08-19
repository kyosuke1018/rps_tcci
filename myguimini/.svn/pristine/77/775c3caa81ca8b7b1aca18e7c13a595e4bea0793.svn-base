/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.servlet;

import com.tcci.myguimini.facade.AccessLogFacade;
import com.tcci.myguimini.facade.MyServiceFacade;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@WebServlet(name = "ExecuteServiceServlet", urlPatterns = {"/servlet/executeService"})
public class ExecuteServiceServlet extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(ExecuteServiceServlet.class);
    
    @EJB
    private AccessLogFacade logFacade;
    
    @Resource(mappedName = "jndi/myguimini.config")
    private Properties jndiAppConfig;

    private String server;
    private HttpClient httpClient;
    
    @EJB
    private MyServiceFacade myServiceFacade;

    @Override
    public void init(ServletConfig config) {
        server = jndiAppConfig.getProperty("ssoTicketURL");
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*取得服務*/
        String service = request.getParameter("service");
        /*ticket*/
        String tgt = request.getParameter("tgt");
        if (null == service || null == tgt) {
            throw new ServletException("Invalid parameters!");
        }
        service = service.trim();
        tgt = tgt.trim();
        /*取的對映服務認證的位置*/
        String serviceUrl = myServiceFacade.findServiceUrl(service);
        if (null == serviceUrl) {
            throw new ServletException("Service not found! service:" + service);
        }
        
        // httpClient.getState().clearCookies();
        
        /*取得該系統的認證ticket*/
        String scTicket = getServiceTicket(tgt, serviceUrl);
        if (null == scTicket) {
            sessionExpired(response);
            return;
        }

        serviceUrl = serviceUrl.contains("?") ? serviceUrl + "&ticket=" + scTicket
                                              : serviceUrl + "?ticket=" + scTicket;
        GetMethod method = new GetMethod(serviceUrl);
        try {
            // int status = httpClient.executeMethod(method);
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            setHeaders(response, method);
            response.setStatus(status);
            sendStreamToClient(response, method);
            logFacade.addServiceLog(service, tgt);
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private String getServiceTicket(String tgt, String service) {
        PostMethod method = new PostMethod(server + "/" + tgt);
        method.addParameter("service", service);
        try {
            // int status = httpClient.executeMethod(method);
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            if (200 == status) {
                return method.getResponseBodyAsString();
            }
        } catch (IOException ex) {
            logger.error("tgt:{} getServiceTicket IOException", tgt, ex);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    private void sessionExpired(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("session expired!");
    }

    protected void setHeaders(HttpServletResponse response, HttpMethod method) {
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
    }

    protected void sendStreamToClient(ServletResponse response, HttpMethod method) throws IOException {
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
}
