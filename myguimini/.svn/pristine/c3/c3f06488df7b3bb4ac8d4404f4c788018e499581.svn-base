/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.servlet;

import com.tcci.myguimini.facade.AccessLogFacade;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/servlet/login"})
public class LoginServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    
    @EJB
    private AccessLogFacade logFacade;
    
    @Resource(mappedName = "jndi/myguimini.config")
    private Properties jndiAppConfig;

    private String server;
    private HttpClient httpClient;
    
    @Override
    public void init(ServletConfig config) {
        server = jndiAppConfig.getProperty("ssoTicketURL");

        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (null == username || null == password) {
            throw new ServletException("Invalid parameters!");
        }
        username = username.toLowerCase();
        String tgt = getTicketGrantingTicket(username, password);
        if (null == tgt) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("login failed!");
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(tgt);
            logFacade.addLoginLog(username, tgt);
        }
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

    private String getTicketGrantingTicket(String username, String password) {
        PostMethod method = new PostMethod(server);
        method.addParameter("username", username);
        method.addParameter("password", password);
        try {
            int status = httpClient.executeMethod(method);
            if (201 == status) { // 認證成功
                String output = method.getResponseBodyAsString();
                Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                                         .matcher(output);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
            logger.warn("{} login fail, status={}", username, status);
        } catch (IOException ex) {
            logger.error("{} login IOException", username, ex);
        } finally {
            method.releaseConnection();
        }
        return null;
    }
}
