/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller;

import com.tcci.fc.controller.login.UserSession;
//import com.tcci.fc.facade.log.ActivityLogService;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.fileio.ExportUtil;
import com.tcci.fc.util.ExcelUtils;
import java.util.Date;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.primefaces.event.FlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilbert
 */
//Why is this class qualified enough as a "base"? 
abstract public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    //ResourceBundle
    protected transient ResourceBundle rb = ResourceBundle.getBundle("msgApp",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
//    @EJB
//    protected ActivityLogService activityLogService;     
    
    @ManagedProperty(value = "#{userSession}")
    protected UserSession userSession;
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    //
    //</editor-fold>
    
    final protected boolean valid = true;
    protected boolean isValid = valid;
    protected String pageTitle;
    protected String pageCode;
    
    /**
     * used for
     *
     * @PostConstruct
     */
    protected void init() {
        logger.debug("checkParameter ...");
        if (!checkParameter()) {
            return;
        }

        // 讀取資料 master
        if (!loadData()) {
            return;
        }
        logger.debug("init() finished");
    }

    /**
     * 檢核查詢條件或url參數
     * @return 
     */
    protected boolean checkParameter() {
        return valid;
    }
    
    protected boolean check4Query() throws Exception {
        return valid;
    }      

    /**
     * 查詢資料
     * @return 
     */    
    protected boolean loadData() {
        return valid;
    }

    /**
     * 查詢(query button)
     * @throws java.lang.Exception
     */
    public void query() throws Exception {
        logger.debug("query !");
//        if (!checkParameter()) {
//            return;
//        }
        if (!check4Query()) {
            queryAgain();
            return;
        }
        // 讀取資料 master
        if (!loadData()) {
            logger.debug("loadData Fail !");
            return;
        }
        logger.debug("query() finished");
    }
    protected void queryAgain() throws Exception {
        
    }
    /**
     * 儲存(save button)
     * @throws java.lang.Exception
     */
    public void save() throws Exception {      
        if (!check4Save()) {
            queryAgain();
            return;
        }
        if (!saveData()) {
            logger.debug("saveData Fail !");
            return;
        }
        logger.debug("save() finished");
    }  
    /**
     * 檢核查詢條件或url參數
     * @return 
     * @throws java.lang.Exception
     */
    protected boolean check4Save() throws Exception {
        return valid;
    }    
    protected boolean saveData() throws Exception{
        return valid;
    }
    
    //<editor-fold defaultstate="collapsed" desc="saveDtl1">
    protected void initDtl1(T detail) {
        
    }
    public void saveDtl1() throws Exception {
        if (!check4SaveDtl1()) {
            queryAgain();
            return;
        }
        if (!saveDataDtl1()) {
            logger.debug("saveDataDtl1 Fail !");
            return;
        }
        logger.debug("saveDtl1() finished");
    }
    protected boolean check4SaveDtl1() throws Exception {
        return valid;
    }
    protected boolean saveDataDtl1() throws Exception{
        return valid;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="saveDtl2">
    public void saveDtl2() throws Exception {
        if (!check4SaveDtl2()) {
            queryAgain();
            return;
        }
        if (!saveDataDtl2()) {
            logger.debug("saveDtl2 Fail !");
            return;
        }
        logger.debug("saveDtl2() finished");
    }
    protected boolean check4SaveDtl2() throws Exception {
        return valid;
    }
    protected boolean saveDataDtl2() throws Exception{
        return valid;
    }
    //</editor-fold>
  
    
    /**
     * 刪除(delete button)
     */
    public void delete() {
      
    }     

    /**
     * 重設(clean button)
     */
    public void reset() {
    }

    /**
     * 觸發PostConstruct用
     */
    public void triggerPostConstruct() {
    }
    
    public String onFlowProcess(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());
         
        return event.getNewStep();
    }      

    protected boolean checkStartDateEndDate(Date startDate, Date endDate) {
        //起日大於迄日，請重新輸入
        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) > 0) {
                String msg = "起日大於迄日，請重新輸入";
                JsfUtil.addErrorMessage(msg);
                return !valid;
            }
        }
        return valid;
    }    
    
    /**
     * 針對匯出Excel客製化處理
     *
     * @param document
     */
    public void postProcessXLS(Object document) {
        // Header 文字過濾
        ExportUtil.processHeader((HSSFWorkbook) document);
        
        // 數值欄位處理
        if (null!=getNumericColumns() && getNumericColumns().length>0){
            //數值欄位Pattern
            if (null!=getNumericPatterns() && getNumericPatterns().length>0){
                ExportUtil.translateXlsNumericFields((HSSFWorkbook) document, getNumericColumns(),getNumericPatterns());
            }else{
                ExportUtil.translateXlsNumericFields((HSSFWorkbook) document, getNumericColumns());
            } 
        }
           
        // 大額數值欄位處理
        if (null!=getBigAmountColumns() && getBigAmountColumns().length>0){
            ExportUtil.translateXlsBigAmountNumericFields((HSSFWorkbook) document, getBigAmountColumns());
        }
        
        //內文 換行文字過濾
        if (null!=getContentColumns() && getContentColumns().length>0){
            ExportUtil.processContentFields((HSSFWorkbook) document, getContentColumns());
        }

        // High Light 處理 (放在 postProcessXLS 最後一部)
        if (null!=getHighLightColumns() && getHighLightColumns().length>0){
            ExportUtil.highLightFields((HSSFWorkbook) document, getHighLightColumns(), 1);
        }
    }

    /**
     * 數值欄位序號(從零開始)
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
    protected String[] getNumericPatterns(){
        return null;
    }    
    
    /**
     * 大額數值欄位序號
     * @return 
     */
    protected int[] getBigAmountColumns(){
        return null;
    }

    /**
     * High Light 欄位處理
     * @return 
     */
    protected int[] getHighLightColumns(){
        return null;
    }
    
    /**
     * 最大查詢結果筆數
     * @return 
     */
//    public int getMaxResultSize(){
//        return SQLUtils.DEF_MAX_RESULT_SIZE; 
//    }
    
    /**
     * EXCEL 最大匯出筆數
     * @return 
     */
//    public int getMaxExportSize(){
//        return ExcelUtils.MAX_EXCEL_EXPORT_SIZE; 
//    }
        
    /**
     * 內文 換行文字過濾
     * @return 
     */
    protected int[] getContentColumns(){
        return null;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

}
