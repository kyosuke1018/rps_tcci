/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.servlet;

import com.tcci.solr.client.enums.SolrTransactionEnum;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.server.util.SolrServerUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://localhost:8080/solr/safeDelete?op=D&source=PMIS&cid=10100
 * 
 * @author Peter
 */
@WebServlet(urlPatterns = {"/safeDelete"})
public class SafeDeleteServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(SafeDeleteServlet.class);
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        
        TcSolrSource tcSolrSource = new TcSolrSource();
        String res = genTcSolrSource(request, tcSolrSource);      
        
        PrintWriter out = response.getWriter();
        if( res.isEmpty() ){
            String op = (request.getParameter("op")!=null? request.getParameter("op"):"");
            
            if( SolrTransactionEnum.DELETE.getCode().equals(op) ){
                res = SolrServerUtils.runSolrDelete(tcSolrSource);
            }else{
                logger.error("processRequest error op = "+op);
            }
        }else{
            logger.error("processRequest ... " + res);
        }
        out.print(res);
        
        out.flush();
        out.close();
    }
    
    /**
     * 依輸入參數建立 TcSolrSource 物件
     * @param request
     * @return 
     */
    private String genTcSolrSource(HttpServletRequest request, TcSolrSource tcSolrSource){
        StringBuilder resSB = new StringBuilder();
        // get parameter
        String source = (request.getParameter("source")!=null? request.getParameter("source"):"");
        String cid = (request.getParameter("cid")!=null? request.getParameter("cid"):"");
        String title = (request.getParameter("title")!=null? request.getParameter("title"):"");
        String description = (request.getParameter("description")!=null? request.getParameter("description"):"");
        String filename = (request.getParameter("filename")!=null? request.getParameter("filename"):"");
        String path = (request.getParameter("path")!=null? request.getParameter("path"):"");
        
        //<editor-fold defaultstate="collapsed" desc="print parameter log"> 
        logger.info("processRequest ...");
        logger.info("request.getRemoteHost() = "+request.getRemoteHost());
        logger.info("source = "+source);
        logger.info("cid = "+cid);
        logger.info("title = "+title);
        logger.info("description = "+description);
        logger.info("filename = "+filename);
        logger.info("path = "+path);
        //</editor-fold>
        
        try{
            // check parameter
            if( source.isEmpty() 
                || cid.isEmpty() ){
                resSB.append("source、cid: 參數皆不可空白!");
            }else{
                long nCid = Long.parseLong(cid);

                // set TcSolrSource
                tcSolrSource.setSource(source);
                tcSolrSource.setCid(nCid);
                tcSolrSource.setTitle(title);
                tcSolrSource.setDescription(description);
                tcSolrSource.setFilename(filename);
                tcSolrSource.setPath(path);
            }
        }catch(NumberFormatException e){
            logger.error("processRequest cid="+cid, e);
            resSB.append("cid: 須為整數!");
        }
        
        return resSB.toString();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "SafeDeleteServlet : call solr deleteByQuery";
    }// </editor-fold>
    
}
