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
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.enums.FormTypeEnum;
import com.tcci.et.facade.EtMemberFormFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "memberFormQuery")
@ViewScoped
public class MemberFormQueryController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 56;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtMemberFormFacade formFacade;
    
    // 查詢條件
    private MemberCriteriaVO criteriaVO;
    private List<SelectItem> statusList;
    private List<SelectItem> factoryOptions;
    
    private boolean isAdmin;
    private List<CmFactory> owenerfactoryList;
    
    // 結果
    private BaseLazyDataModel<MemberVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<MemberVO> filterResultList; // datatable filter 後的結果
    
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        if( functionDenied ){ return; }
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
//        isAdmin = false;
        
        criteriaVO = new MemberCriteriaVO();
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
                // 目前無CLOSE的case
//                if (s != FormStatusEnum.CLOSE) {
                    options.add(new SelectItem(s, msgApp.getString("status." + s.name())));
//                }
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
//        queryfactoryList = new ArrayList<>();
        if(this.isAdmin){
//        if(this.isPower){
            owenerfactoryList = cmUserfactoryFacade.findAllFactories();
//            queryfactoryList.addAll(owenerfactoryList);
        }else{
            owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(this.getLoginUser());
            if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
                logger.debug("owenerfactoryList :"+owenerfactoryList.size());
//                queryfactoryList.addAll(owenerfactoryList);
            }
//            List<CmFactory> owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(user);
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
        // criteriaVO.setGroup(GlobalConstant.UG_ADMINISTRATORS_ID); // ADMINISTRATORS
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
            List<MemberVO> resList = formFacade.findByCriteria(criteriaVO, GlobalConstant.DEF_LOCALE.getLocale());
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
//            JsfUtils.addWarningMessage("請選擇廠別");
//            return false;
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
        criteriaVO = new MemberCriteriaVO();
        criteriaVO.reset();
        resetDataTable();
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
//    public String formStatus(FormStatusEnum status) {
    public String formStatus(String status) {
        try {
            return null == status ? null : msgApp.getString("status." + status);
        } catch (Exception ex) {
            return status;
        }
    }
    
    public String formType(String type) {
        try {
            FormTypeEnum typeEnum = FormTypeEnum.getFromCode(type);
            return null == typeEnum ? null : typeEnum.getName();
        } catch (Exception ex) {
            return type;
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
    public BaseLazyDataModel<MemberVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<MemberVO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public List<MemberVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<MemberVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public MemberCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(MemberCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public List<SelectItem> getFactoryOptions() {
        return factoryOptions;
    }
    
    public List<SelectItem> getStatusList() {
        return statusList;
    }
    //</editor-fold>
}