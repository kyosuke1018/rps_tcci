/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.servlet;

import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.myguimini.entity.MyMobileApp;
import com.tcci.myguimini.facade.MyMobileAppFacade;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jimmy.Lee
 */
@WebServlet(name = "MobileAppServlet", urlPatterns = {"/servlet/mobileapp"})
public class MobileAppServlet extends HttpServlet {
    
    @EJB
    private MyMobileAppFacade appFacade;
    @EJB
    private ContentFacade contentFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String platform = request.getParameter("platform");
        MyMobileApp myApp = appFacade.findByPlatform(platform);
        if (null == myApp) {
            throw new ServletException("platform not found!");
        }
        String action = request.getParameter("action");
        if ("version".equals(action)) {
            response.getOutputStream().print(myApp.getAppVersion());
            return;
        } else if ("download".equals(action)){
            AttachmentVO vo = appFacade.getAttachmentVO(myApp);
            if (vo != null) {
                boolean showname = "1".equals(request.getParameter("showname"));
                downloadAttach(response, vo, showname);
                return;
            } else {
                throw new ServletException("attachment not found!");
            }
        }
        throw new ServletException("invalid action!");
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

    private void downloadAttach(HttpServletResponse response, AttachmentVO vo, boolean showname) {
        response.reset();
        //response.setContentType(vo.getContentType());
        response.setContentType("application/vnd.android.package-archive");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate");
        response.setHeader("Pragma", "public");
        // response.addHeader("Content-Type", vo.getContentType());
        response.addHeader("Content-Type", "application/vnd.android.package-archive");
        if (showname) {
            String filename = vo.getFileName();
            try {
                filename = URLEncoder.encode(filename, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            }
            response.setHeader("Content-disposition","attachment; filename=" + filename);
        }
        try {
            InputStream in = contentFacade.findContentStream(vo.getApplicationdata());
            ServletOutputStream output = response.getOutputStream();
            byte[] inBytes = new byte[10240];
            int len = 0;
            while ((len = in.read(inBytes)) != -1) {
                output.write(inBytes, 0, len);
            }
            output.flush();
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
