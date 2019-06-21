/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.global;

import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.CmFunctionFacade;
import com.tcci.cm.util.ExportUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.model.global.HitMessageVO;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Peter
 */
abstract public class CommonQueryController extends SessionAwareController implements ICommonFunction {
    public static final String RESULT_DATATABLE = "fmMain:dtResult";

    @EJB protected CmFunctionFacade cmFunctionFacade;

    protected HitMessageVO hitMessageVO; // 提示訊息

    // for first time lazy load
    protected boolean firstTimeQuery;
    
    @PostConstruct
    private void init(){
        logger.debug("CommonQueryController init ...");
        // check Login
        if( !sessionController.isLogin() ){
            return;
        }
        // 首次執行頁面複雜的查詢(直接在PostConstruct跑無BlockUI效果，太早點擊會造成JS失效)
        firstTimeQuery = true;
    }
    
    /**
     * UI 直接觸發查詢 
     * don't override
     */
    public void doQuery(){
        if( beforeLoadData() ){
            // 依廠別權限檢核
            /*if( needCheckFactoryPermission ){
                logger.debug("checkFactoryPermission ...");
                if (!checkFactoryPermission(factoryListController.getSelectedFactory())) {
                    logger.error("checkFactoryPermission = false !");
                    return;
                }
            }*/

            // 載入DB資料
            loadData();
        }
    }
    
    /**
     * UI 重設條件 
     * don't override
     */
    public void doReset(){
        // factoryListController.resetQuery();
        resetUI();
    }
    
    /**
     * 載入 DB 資料 for override
     * @return 
     */
    protected boolean beforeLoadData(){ return true; }
    
    /**
     * 載入 DB 資料 for override
     */
    protected void loadData(){}
    
    /**
     * UI 重設
     */
    protected void resetUI(){}
    
    /**
     * 最大查詢結果筆數
     * @return 
     */
    @Override
    public int getMaxResultSize(){
        return GlobalConstant.DEF_MAX_RESULT_SIZE; 
    }
    
    /**
     * 功能ID
     * for Override
     * @return 
     */
    @Override
    public long getFunctionId(){
        // logger.debug(this.getClass().getName() + " not override getFunctionId() !");
        return 0;
    }
    
    /**
     * 功能標題
     * @return
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(this.getFunctionId());
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Excel Export">
    /**
     * transfer list data to Execel
     * @param dataList
     * @param outputFileFullName
     * @param templateFileName
     * @param headers
     * @throws Exception 
     */
//    public void listToExcel(List dataList, String outputFileFullName, String templateFileName, List<String> headers) throws Exception{
//        AnnotationExportUtils.gereateReport(
//                TruckScaleVO.class, 
//                dataList, 
//                outputFileFullName, 
//                templateFileName, 
//                headers);
//    }
    
    /**
     * 針對匯出Excel客製化處理
     * @param document
     */
    @Override
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
     * 是否移除最後一列 (datatable 有 footerText 時 override)
     * @return 
     */
    @Override
    protected boolean isRemoveLastRow(){
        return false; // 預設 datatable 無 footerText
    }
    
    /**
     * 設定欄寬
     * @return 
     */
    @Override
    protected Map<Integer, Integer> getColsWidth(){
        return null;
    }
    
    /**
     * 內文 換行文字過濾、HTML TAG
     * @return 
     */
    @Override
    protected int[] getContentColumns(){
        return null;
    }

    /**
     * 數值欄位序號(從零開始)
     * @return int[] 
     */
    @Override
    protected int[] getNumericColumns(){
        return null;
    }
    
    /**
     * 數值欄位Pattern
     * 
     * @return 
     */
    @Override
    protected Map<String, String> getNumericPatterns(){
        return null;
    }    
    
    /**
     * 大額數值欄位序號
     * @return 
     */
    @Override
    protected int[] getBigAmountColumns(){
        return null;
    }

    /**
     * High Light 欄位處理
     * @return 
     */
    @Override
    protected int[] getHighLightColumns(){
        return null;
    }
    
    /**
     * EXCEL 最大匯出筆數
     * @return 
     */
    @Override
    public int getMaxExportSize(){
        return GlobalConstant.MAX_EXCEL_EXPORT_SIZE; 
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public boolean isFirstTimeQuery() {
        return firstTimeQuery;
    }

    public void setFirstTimeQuery(boolean firstTimeQuery) {
        this.firstTimeQuery = firstTimeQuery;
    }

    public HitMessageVO getHitMessageVO() {
        return hitMessageVO;
    }

    public void setHitMessageVO(HitMessageVO hitMessageVO) {
        this.hitMessageVO = hitMessageVO;
    }
    //</editor-fold>
}
