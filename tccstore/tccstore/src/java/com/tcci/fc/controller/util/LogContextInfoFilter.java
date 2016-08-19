/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.util;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.MDC;

/**
 *
 * @author Jimmy.Lee
 */
@WebFilter(filterName = "TimeOfDayFilter",
urlPatterns = {"/faces/*"}
)
public class LogContextInfoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Principal p = httpRequest.getUserPrincipal();
        String userAccount = (p != null) ? p.getName() : "";

        HttpSession session = httpRequest.getSession(false);
        String sessionId = (session != null) ? session.getId() : "";

        MDC.put("user", userAccount);
        MDC.put("sessionId", sessionId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("user");
            MDC.remove("sessionId");
        }
    }

    @Override
    public void destroy() {
    }
}
