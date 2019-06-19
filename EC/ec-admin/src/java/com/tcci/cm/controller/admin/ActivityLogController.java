/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.admin.ActivityLogCriteriaVO;
import com.tcci.cm.model.admin.ActivityLogVO;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.DateUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "activityLogController")
@ViewScoped
public class ActivityLogController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 80;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private UserFacade usersFacade;
    @EJB private CmActivityLogFacade cmActivityLogFacade;

    // 查詢條件
    private ActivityLogCriteriaVO criteriaVO;
    
    // 結果
    private BaseLazyDataModel<ActivityLogVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<ActivityLogVO> filterResultList; // datatable filter 後的結果
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();

        criteriaVO = new ActivityLogCriteriaVO();
        
        // default query
        defQuery();
    }
    
    /**
     * 預設查詢
     */
    private void defQuery(){
        criteriaVO.setDateStart(DateUtils.addDate(DateUtils.getToday(), -7));
        criteriaVO.setDateEnd(DateUtils.getToday());
        
        doQuery();    
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        
        resetDataTable();
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數

        try {
            List<ActivityLogVO> resList = cmActivityLogFacade.findByCriteria(criteriaVO);
            lazyModel = new BaseLazyDataModel<ActivityLogVO>(resList);
        }catch(Exception e){
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }
    
    /**
     * 重設表單、結果
     */
    public void doReset(){
        logger.debug("doReset ...");
        if( lazyModel!=null ){
            lazyModel.reset();
        }
        
        criteriaVO.reset();
        resetDataTable();
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public ActivityLogCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(ActivityLogCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public BaseLazyDataModel<ActivityLogVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<ActivityLogVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<ActivityLogVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<ActivityLogVO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    //</editor-fold>
}
