/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.et.controller;

import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.util.ExcelUtils;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.ExportUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.NetworkUtils;
import com.tcci.cm.util.NotificationUtils;
import com.tcci.cm.util.SQLUtils;
import com.tcci.cm.util.WebUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.DateUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
abstract public class AbstractController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @EJB protected SysResourcesFacade sys;
    
    protected String viewId;
    protected String globalMessage;
    
    public void showErrorMessage(){
        JsfUtils.addErrorMessage(globalMessage);
        globalMessage = "";// clear after show
    }
    
    public String getFuncTitle(){
        return "Show function title there ...";
    }
    /*
    public void processUnknowException(String methodName, Exception e, boolean isCallback){
        processUnknowException(methodName, e, isCallback, null);
    }
    public void processUnknowException(String methodName, Exception e, boolean isCallback, String errorCode){
        logger.error("processUnknowException ["+methodName+"] Exception:\n", e);
        String hostname = WebUtils.getHostName();
        String datetime = DateUtils.format(new Date());
        String errmsg = "系統發生錯誤，請提供此畫面給系統管理員，並告知執行操作! ("+hostname+") - "+datetime;
        if( isCallback ){
            RequestContext context = JsfUtils.buildErrorCallback();
            if( errorCode!=null && !errorCode.isEmpty() ){
                context.addCallbackParam("errorCode", errorCode);
            }
        }
        JsfUtils.addErrorMessage(errmsg);
    }
    */
    public void processUnknowException(TcUser operator, String methodName, Exception e, boolean isCallback){
        UUID uuid = UUID.randomUUID();
        String constraintViolations = "";
        if(e instanceof EJBException){
            constraintViolations = ExceptionHandlerUtils.printConstraintViolationException((EJBException)e);
        }
        processUnknowException(operator, methodName, e, isCallback, uuid.toString(), constraintViolations);
    }
    public void processUnknowException(TcUser operator, String methodName, Exception e, 
            boolean isCallback, String errorCode, String constraintViolations){
        logger.error("processUnknowException ["+methodName+"] ("+errorCode+") Exception:\n", e);
        String hostname = NetworkUtils.getHostIP(); // WebUtils.getHostName();
        String datetime = DateUtils.format(new Date());
        String errmsg = "系統發生錯誤，請提供此畫面 Email 給系統管理員，並告知執行操作! (" + hostname + ") - " + datetime;
        if( isCallback ){
            RequestContext context = JsfUtils.buildErrorCallback();
            if( errorCode!=null && !errorCode.isEmpty() ){
                context.addCallbackParam("errorCode", errorCode);
            }
        }
        NotificationUtils sender = new NotificationUtils();
        sender.notifyOnException(sys.getNotifyAdmins(), operator, methodName, e, errorCode, constraintViolations);
        JsfUtils.addErrorMessage(errmsg);
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Datatable Export">
    /**
     * 針對匯出Excel客製化處理
     * @param document
     */
    public void postProcessXLS(Object document) {
        logger.debug("postProcessXLS ...");
        try{
            ExportUtils.formatExcel((HSSFWorkbook) document, 
                    0, // sheetIndex
                    getColsWidth(),
                    getHighLightColumns(), 
                    getBigAmountColumns(), 
                    getContentColumns(), 
                    getNumericColumns(), 
                    getNumericPatterns(),
                    isRemoveLastRow());
        }catch(Exception e){
            logger.error("postProcessXLS Exception :\n", e);
        }
    }
    
    /**
     * 內文 換行文字過濾 (行數從零開始)
     * @return 
     */
    protected int[] getContentColumns(){
        return null;
    }

    /**
     * 數值欄位序號 (行數從零開始)
     * @return int[] 
     */
    protected int[] getNumericColumns(){
        return null;
    }
    
    /**
     * 數值欄位Pattern
     * 
     * @return 
     */
    protected Map<String, String> getNumericPatterns(){
        return null;
    }    
    
    /**
     * 大額數值欄位序號 (行數從零開始)
     * @return 
     */
    protected int[] getBigAmountColumns(){
        return null;
    }

    /**
     * High Light 欄位處理 (行數從零開始)
     * @return 
     */
    protected int[] getHighLightColumns(){
        return null;
    }
    
    /**
     * 指定欄位寬度
     * @return 
     */
    protected Map<Integer, Integer> getColsWidth(){
        return null;
    }
    
    /**
     * 移除最後一列 (例如有footer row時)
     * @return 
     */
    protected boolean isRemoveLastRow(){
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for get Constant">
    /**
     * 最大查詢結果筆數
     * @return 
     */
    public int getMaxResultSize(){
        return SQLUtils.DEF_MAX_RESULT_SIZE; 
    }
    
    /**
     * EXCEL 最大匯出筆數
     * @return 
     */
    public int getMaxExportSize(){
        return ExcelUtils.MAX_EXCEL_EXPORT_SIZE; 
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getGlobalMessage() {
        return globalMessage;
    }

    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }
    //</editor-fold>
    
}
