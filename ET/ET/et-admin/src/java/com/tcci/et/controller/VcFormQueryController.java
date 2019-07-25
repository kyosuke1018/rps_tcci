/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtVcFormFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "vcFormQuery")
@ViewScoped
public class VcFormQueryController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 58;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtVcFormFacade formFacade;
    
    // 查詢條件
    private BaseCriteriaVO criteriaVO;
    private List<SelectItem> statusList;
    private List<SelectItem> factoryOptions;
    
    private boolean isAdmin;
    private List<CmFactory> owenerfactoryList;
    
    // 結果
    private BaseLazyDataModel<MemberVenderVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<MemberVenderVO> filterResultList; // datatable filter 後的結果
    
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        if( functionDenied ){ return; }
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        
        criteriaVO = new BaseCriteriaVO();
        statusList = buildStatusOptions();
        factoryOptions = buildFactoryOptions();
        //預設查近一個月
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        criteriaVO.setStartAt(calendar.getTime());
        
        // Get view Id
        viewId = JsfUtils.getViewId();
        
        // default query
        defQuery();
    }
    
    /**
     * 狀態
     * @return 
     */
    List<SelectItem> buildStatusOptions(){
        List<SelectItem> options = new ArrayList<>();
        for (FormStatusEnum s : FormStatusEnum.values()) {
            options.add(new SelectItem(s, msgApp.getString("status." + s.name())));
        }
        return options;
    }
    
    /**
     * 廠別
     * @return 
     */
    List<SelectItem> buildFactoryOptions(){
        List<SelectItem> options = new ArrayList<>();
        owenerfactoryList = new ArrayList<>();
        if(this.isAdmin){
            owenerfactoryList = cmUserfactoryFacade.findAllFactories();
        }else{
            owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(this.getLoginUser());
            if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
                logger.debug("owenerfactoryList :"+owenerfactoryList.size());
            }
            if(CollectionUtils.isNotEmpty(owenerfactoryList)){
                logger.info("owenerfactoryList:"+owenerfactoryList.size());
                for(CmFactory cf :owenerfactoryList){
                    logger.info("cf:"+cf.getId());
                }
            }
        }
        logger.debug("buildFactoryOptions owenerfactoryList:"+owenerfactoryList.size());
        
        // 查詢工廠選單
        List<CmFactory> result = cmFactoryFacade.findByAreaCode(owenerfactoryList, null, null);
        if (result != null ) {
            logger.debug("buildFactoryOptions options:"+result.size());
            for (CmFactory g : result) {
                options.add(new SelectItem(g.getId(), g.getCode()+"-"+g.getName()));
            }
        }
        
        return options;
    }
    
    /**
     * default query
     */
    public void defQuery(){
        doQuery();
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        if( !doCheck() ){
            return;
        }
        
        resetDataTable();
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數

        try {
            List<MemberVenderVO> resList = formFacade.findByCriteria(criteriaVO);
            lazyModel = new BaseLazyDataModel<>(resList);
        }catch(Exception e){
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }
    
    /**
     * 查詢參數檢核
     * @return 
     */
    public boolean doCheck(){
        if( criteriaVO.getKeyword()!=null ){
            criteriaVO.setKeyword(criteriaVO.getKeyword().trim());
        }
        if( criteriaVO.getFactoryId()==null && !isAdmin ){
            List<Long> idList = new ArrayList<>();
            for(SelectItem item:factoryOptions){
                idList.add((Long)item.getValue());
            }
            criteriaVO.setIdList(idList);
        }
        return true;
    }
    
    /**
     * 重設表單、結果
     */
    public void doReset(){
        logger.debug("doReset ...");
        if( lazyModel!=null ){
            lazyModel.reset();
        }
        
        // 清除條件
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.reset();
        resetDataTable();
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    public String formStatus(String status) {
        try {
            return null == status ? null : msgApp.getString("status." + status);
        } catch (Exception ex) {
            return status;
        }
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
    public BaseLazyDataModel<MemberVenderVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<MemberVenderVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<MemberVenderVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<MemberVenderVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public BaseCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(BaseCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public List<SelectItem> getFactoryOptions() {
        return factoryOptions;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    //</editor-fold>
    
}
