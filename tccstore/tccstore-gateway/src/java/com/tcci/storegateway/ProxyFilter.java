/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway;

import com.tcci.storegateway.requesthandler.RequestHandler;
import com.tcci.storegateway.requesthandler.RequestHandlerFactory;
import com.tcci.storegateway.responsehandler.ResponseHandler;
import com.tcci.storegateway.responsehandler.ResponseHandlerFactory;
import com.tcci.storegateway.responsehandler.SSOResponseHandler;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@WebFilter("/*")
public class ProxyFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(ProxyFilter.class);

    private String ssoTicketUrl;
    private String storeServiceUrl;
    private HttpClient httpClient;

    @Resource(mappedName = "jndi/storegateway.config")
    private Properties config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        AllowedMethodHandler.setAllowedMethods("OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE");
        ssoTicketUrl = config.getProperty("ssoTicketUrl");
        if (null == ssoTicketUrl) {
            String error = "ssoTicketUrl not configured!";
            logger.error(error);
            throw new ServletException(error);
        }
        storeServiceUrl = config.getProperty("storeServiceUrl");
        if (null == storeServiceUrl) {
            String error = "storeServiceUrl not configured!";
            logger.error(error);
            throw new ServletException(error);
        }
        logger.warn("ssoTicketUrl:{}", ssoTicketUrl);
        logger.warn("storeServiceUrl:{}", storeServiceUrl);
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, false);
        // httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = getURI(httpRequest);
        ResponseHandler responseHandler = null;
        // httpClient.getState().clearCookies();
        try {
            if ("/login".equals(uri)) {
                responseHandler = login(httpRequest, httpResponse);
            } else if (uri.startsWith("/tccstore/")) {
                responseHandler = storeService(httpRequest, httpResponse);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (HttpException e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("doFilter exception", e);
        } catch (IOException e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("doFilter exception", e);
        } catch (MethodNotAllowedException e) {
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            httpResponse.setHeader("Allow", e.getAllowedMethods());
            logger.error("doFilter exception", e);
        } catch (TGTExpiredException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("doFilter exception", e);
        } finally {
            if (responseHandler != null) {
                responseHandler.close();
            }
        }
        long endTime = System.currentTimeMillis();
        logger.warn("uri:{}, execution time(ms):{}", uri, (endTime-startTime));
    }

    @Override
    public void destroy() {
        httpClient = null;
    }

    private String getURI(HttpServletRequest httpRequest) {
        String contextPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI().substring(contextPath.length());
        if (httpRequest.getQueryString() != null) {
            uri += "?" + httpRequest.getQueryString();
        }
        return uri;
    }

    private ResponseHandler login(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws MethodNotAllowedException, IOException {
        // HttpMethod method = executeRequest(httpRequest, ssoTicketUrl);
        String username = httpRequest.getParameter("username");
        String password = httpRequest.getParameter("password");
        if (username != null) {
            username = username.toLowerCase().trim();
        }
        logger.warn("login account:{}", username);
        PostMethod method = new PostMethod(ssoTicketUrl);
        method.addParameter("username", username);
        method.addParameter("password", password);
        // httpClient.executeMethod(method);
        httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
        ResponseHandler responseHandler = (ResponseHandler) new SSOResponseHandler(method);
        responseHandler.process(httpResponse);
        return responseHandler;
    }

    private ResponseHandler storeService(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws MethodNotAllowedException, IOException, TGTExpiredException {
        String contextPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI().substring(contextPath.length() + 9);
        String serviceUrl = storeServiceUrl + uri;
        String query = httpRequest.getQueryString();
        if (query != null) {
            serviceUrl += "?" + query;
        }
        String ticket = getServiveTicket(serviceUrl, httpRequest.getHeader("TGT"));
        if (ticket != null) {
            serviceUrl += (query != null) ? "&" : "?";
            serviceUrl += "ticket=" + ticket;
        }
        HttpMethod method = executeRequest(httpRequest, serviceUrl);
        if (method.getStatusCode() != 200) {
            logger.warn("storeService:{}, status:{}", serviceUrl,method.getStatusCode());
        }
        ResponseHandler responseHandler = ResponseHandlerFactory.createResponseHandler(method);
        responseHandler.process(httpResponse);
        return responseHandler;
    }

    private HttpMethod executeRequest(HttpServletRequest httpRequest, String url) throws MethodNotAllowedException, IOException {
        RequestHandler requestHandler = RequestHandlerFactory
                .createRequestMethod(httpRequest.getMethod());
        HttpMethod method = requestHandler.process(httpRequest, url);
        // method.setFollowRedirects(false);

        if (!((HttpMethodBase) method).isAborted()) {
            // httpClient.executeMethod(method);
            httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
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

    private String getServiveTicket(String serviceUrl, String tgt) throws IOException, TGTExpiredException {
        if (null == tgt) {
            return null;
        }
        PostMethod method = new PostMethod(ssoTicketUrl + "/" + tgt);
        method.addParameter("service", serviceUrl);
        // httpClient.executeMethod(method);
        httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
        int statusCode = method.getStatusCode();
        String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
        if (200 == statusCode) {
            return output;
        } else {
            // TODO: TGT過期時, status 404, ... TicketGrantingTicket could not be found ..., 是否要判斷其它 case ?
            logger.error("getServiveTicket error! status:{}, output:{}", statusCode, output);
            throw new TGTExpiredException(output);
        }
    }

}
