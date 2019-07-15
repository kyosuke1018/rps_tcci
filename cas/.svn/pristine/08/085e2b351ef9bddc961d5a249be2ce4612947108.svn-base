/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.servlet;

import java.io.IOException;
import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@WebServlet("/logout")
@DeclareRoles({ "ROLE_ADMIN", "ROLE_USER" })
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String CAS_LOGOUT_URL = "http://tcci-ap-qas.taiwancement.com/cas-server/logout";
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doGet ...");
        String authType = request.getParameter("authType");
        authType = (authType==null)?"CAS":authType;

        /*
        String username = null;
        if (request.getUserPrincipal() != null) {
            username = request.getUserPrincipal().getName();
        }
        response.getWriter().println("web username: " + username + "\n");
        response.getWriter().println("web user has role \"ROLE_ADMIN\": " + request.isUserInRole("ROLE_ADMIN") + "\n");
        response.getWriter().println("web user has role \"ROLE_USER\": " + request.isUserInRole("ROLE_USER") + "\n");
        */
        // 登出
        response.getWriter().println("logout ... \n");
        HttpSession session = request.getSession(false);
        if( session!=null ){
            logger.info("logout ...");
            session.invalidate();
            request.logout();
        }
        
        if( "CAS".equals(authType) ){
            response.sendRedirect(CAS_LOGOUT_URL);
        }else{
            response.sendRedirect(request.getContextPath()+"/html5/login.html");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doPost ...");
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
