/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.servlet;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.proxy.TcSolrQueryProxy;
import com.tcci.solr.server.util.SolrServerUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://localhost:8080/solr/reindex?source=SolrWebDemo&start=0&rows=10
 * 
 * @author Peter
 */
@WebServlet(urlPatterns = {"/reindex"})
public class ReIndexServlet extends HttpServlet {
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
        
        // 輸入資訊
        String source = (request.getParameter("source")!=null? request.getParameter("source"):"");
        String startStr = (request.getParameter("start")!=null? request.getParameter("start"):"");
        String rowsStr = (request.getParameter("rows")!=null? request.getParameter("rows"):"");
  
        String res = "";
        int start = 0;
        int rows = 0;
        try{
            start = Integer.parseInt(startStr);
            rows = Integer.parseInt(rowsStr);
        }catch(Exception e){
            res = "輸入錯誤 startStr = "+startStr+"; rowsStr = "+rowsStr;
            logger.error("error : startStr = "+startStr+"; rowsStr = "+rowsStr);
        }
        
        if( res.isEmpty() ){
            // 執行 ReIndex
            try{
                TcSolrQueryProxy queryProxy = new TcSolrQueryProxy();
                // 限制每次處理筆數
                queryProxy.setStart(start);
                queryProxy.setRows(rows);

                long num = 0;
                if( source.isEmpty() ){
                    // 查詢條件
                    queryProxy.setQuery(TcSolrConfig.SOLR_QUERY_ALL);// TcSolrConfig.SOLR_QUERY_ALL or TcSolrConfig.FIELD_SOURCE_AP+TcSolrConfig.OP_SOLR_EQUALS+source
                    // 依 ID 排序
                    queryProxy.setSort(TcSolrConfig.FIELD_SOLR_KEY, SolrQuery.ORDER.asc);
                    // num = SolrServerUtils.reIndexAll(ConfigManager.DEF_INDEX_NUM);
                }else{
                    // 查詢條件
                    queryProxy.setQuery(TcSolrConfig.FIELD_SOURCE_AP+TcSolrConfig.OP_SOLR_EQUALS+source);
                    // 依 ID 排序
                    queryProxy.setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.asc);
                    // num = SolrServerUtils.reIndexBySource(source, ConfigManager.DEF_INDEX_NUM);
                }

                num = SolrServerUtils.reIndexPeriod(queryProxy);

                res = "共處理 "+num+" 筆資料";
            }catch(Exception e){
                logger.debug("processRequest exception:\n", e);
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
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
