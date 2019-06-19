/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.servlet;

import com.tcci.ec.facade.ServiceFacade;
import com.tcci.ec.servlet.requesthandler.AllowedMethodHandler;
import com.tcci.ec.servlet.requesthandler.MethodNotAllowedException;
import com.tcci.ec.servlet.requesthandler.RequestHandler;
import com.tcci.ec.servlet.requesthandler.RequestHandlerFactory;
import com.tcci.ec.servlet.responsehandler.ResponseHandler;
import com.tcci.ec.servlet.responsehandler.ResponseHandlerFactory;
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
import javax.ws.rs.core.HttpHeaders;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@WebServlet(name = "ExecuteServiceServlet", urlPatterns = {"/servlet/executeservice"})
public class ExecuteServiceServlet extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(ExecuteServiceServlet.class);

//    @Resource(mappedName = "jndi/mobile.config")
//    private Properties jndiAppConfig;

    private String server;
    private HttpClient httpClient;

    @EJB
    private ServiceFacade serviceFacade;

    @Override
    public void init(ServletConfig config) {
        AllowedMethodHandler.setAllowedMethods("OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE");
//        server = jndiAppConfig.getProperty("ssoTicketURL");

        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, false);
        // httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
        // timeout
//        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000); // 5秒內無法建立連線, timeout
         httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000); // 10秒內無法建立連線, timeout
//        httpClient.getHttpConnectionManager().getParams().setSoTimeout(15000); // 15秒內未收到response, timeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000); // 30秒內未收到response, timeout
        
        //thread
        httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(50);
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(50);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        String authStr = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("service={}", service);
        logger.debug("authStr={}", authStr);
        HttpMethod method = null;
        if (null == service) {
//        if (null == service || null == authStr) {
            service = request.getHeader("service");
            logger.debug("service={}", service);
            if (null == service) {
//            if (null == service || null == authStr) {
                throw new ServletException("Invalid parameters!");
            }
        }
        
//        if (request.getMethod().equals("GET")) {
//        } else if (request.getMethod().equals("POST")) {
//        }
        String serviceUrl = serviceFacade.findServiceUrl(service);
        logger.debug("serviceUrl={}", serviceUrl);
        if (null == serviceUrl) {
            throw new ServletException("Service not found! service:" + service);
        }
        //httpClient.getState().clearCookies();

        /*取得該系統的認證ticket*/
//        String scTicket = getServiceTicket(tgt, serviceUrl);
//        logger.debug("scTicket={}", scTicket);
//        if (null == scTicket) {
//            sessionExpired(response);
//            return;
//        }

//        serviceUrl = serviceUrl.contains("?") ? serviceUrl + "&ticket=" + scTicket
//                : serviceUrl + "?ticket=" + scTicket;

        ResponseHandler responseHandler = null;
        try {
            method = executeRequest(request, serviceUrl);
            logger.debug("method={}", method);
            if (method.getStatusCode() != 200) {
                logger.warn("storeService:{}, status:{}", serviceUrl, method.getStatusCode());
            }
            responseHandler = ResponseHandlerFactory.createResponseHandler(method);
            logger.debug("responseHandler={}", responseHandler);
            responseHandler.process(response);
        } catch (HttpException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("processRequest exception", e);
            e.printStackTrace();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("processRequest exception", e);
            e.printStackTrace();
        } catch (MethodNotAllowedException e) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.setHeader("Allow", e.getAllowedMethods());
            logger.error("processRequest exception", e);
            e.printStackTrace();
        } finally {
            if (responseHandler != null) {
                responseHandler.close();
            }
        }
    }

    private HttpMethod executeRequest(HttpServletRequest httpRequest, String url) throws MethodNotAllowedException, IOException {
        logger.debug("executeRequest,url={}", url);
        logger.debug("executeRequest,authStr={}", httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
        RequestHandler requestHandler = RequestHandlerFactory
                .createRequestMethod(httpRequest.getMethod());
        HttpMethod method = requestHandler.process(httpRequest, url);
        logger.debug("method={}", method);
        // method.setFollowRedirects(false);

        if (!((HttpMethodBase) method).isAborted()) {
            //httpClient.executeMethod(method);
            logger.debug("before httpClient.executeMethod");
            httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            logger.debug("after httpClient.executeMethod");
            if (method.getStatusCode() == 405) {
                Header allow = method.getResponseHeader("allow");
                String value = allow.getValue();
                throw new MethodNotAllowedException(
                        "Status code 405 from server", AllowedMethodHandler
                        .processAllowHeader(value));
            }
        }
        return method;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("doGet");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("doPost");
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    /*
    private String getServiceTicket(String tgt, String service) {
        PostMethod method = new PostMethod(server + "/" + tgt);
        method.addParameter("service", service);
        try {
            int status = httpClient.executeMethod(method);
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
        //android authentication error status code 401 改成403
        //20170407再改回401
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("session expired!");
    }
    */

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
