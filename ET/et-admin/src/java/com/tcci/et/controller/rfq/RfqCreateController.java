/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.CmFactoryController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.model.TenderVO;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.rfq.RfqVO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "rfqCreate")
@ViewScoped
public class RfqCreateController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 32;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    
    @ManagedProperty(value="#{selFactory}")
    protected CmFactoryController selFactory;
    
    public List<CmCompanyVO> companys;
    public List<CmFactoryVO> factorys;
    
    public List<SelectItem> companyOps;
    public List<SelectItem> factoryOps;

    public List<SelectItem> purOrgOps;
    public List<SelectItem> purGroupOps;
    
    public TenderVO tenderVO = new TenderVO();
    public RfqVO rfqVO = new RfqVO();
    public String queryResultMsg;
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        rfqVO = new RfqVO();
        
        initCompany();// 
        getInitParams();// query string params
    }
    
    /**
     * query string params 初始處理
     */
    public void getInitParams(){
        try{
            String tenderIdStr = JsfUtils.getRequestParameter("tenderId");
            if( tenderIdStr!=null ){
                Long tenderId = Long.parseLong(tenderIdStr);
                tenderVO = tenderFacade.findById(tenderId, false);
                logger.info("getInitParams tenderVO = "+tenderVO);
            }
        }catch(Exception e){
            logger.error("getInitParams Exception:\n", e);
        }
    }
    
    /**
     * 依指定標案初始資料
     * @param tenderVO 
     */
    public void initByTender(TenderVO tenderVO){
        if( tenderVO==null ){
            logger.error("initByTender  tenderVO==null");
            return;
        }
        
        rfqVO.setCompanyId(tenderVO.getCompanyId());
        onChangeCompany(true);
        selFactory.setNowFactory(tenderVO.getFactoryId(), false);
        rfqVO.setFactory(selFactory.getSelFactory());
        
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+rfqVO.getFactoryId());
    }
    
    //<editor-fold defaultstate="collapsed" desc="for 輸入詢價單資訊">
    /**
     * 初始公司別
     */
    public void initCompany(){
        //companys = companyFacade.findAllAndSort();
        //companyOps = rfqCommonFacade.buildCompanyOptions(companys);
        // 考慮權限
        selFactory.initConfig(this.getMyCompanyIds(), this.getMyFactoryIds(), null, null, true);
        rfqVO.setCompany(selFactory.getSelCompany());
    }
    
    /**
     * 變更公司別
     * @param confirm 
     */
    public void onChangeCompany(boolean confirm){
        try{
            logger.debug("onChangeCompany companyId = "+rfqVO.getCompanyId());
            confirm = selFactory.onChangeCompany(rfqVO.getCompanyId(), confirm);
            logger.debug("onChangeCompany companyId = "+rfqVO.getCompanyId()+", confirm = "+confirm);
            if( !confirm ){
                // 需再確認變更 (因會清除原全部設定)
                JsfUtils.buildSuccessCallback("changeCompany", true);
                return;
            }
            
            rfqVO.setCompany(rfqCommonFacade.getSelectedCompany(companys, rfqVO.getCompanyId()));
            // TODO 清除所有設定
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeCompany", e, true);
        }
    }

    /**
     * 回覆公司別
     */
    public void onCancelCompany(){
        try{
            logger.debug("onCancelCompany ... ");
            selFactory.onCancelCompany();

            rfqVO.setCompanyId(selFactory.getCompanyId());

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onCancelCompany", e, true);
        }
    }
    
    /**
     * 開啟廠別選取對話框
     */
    public void onOpenSelFactoryDlg(){
        try{
            logger.debug("onOpenSelFactoryDlg ... ");
            //selFactory.setNowCompany(rfqVO.getCompany().getId(), true);

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeCompany", e, true);
        }

    }
    
    /**
     * 初始廠別
     */
    /*public void initFactory(CmCompanyVO company){
        factorys = factoryFacade.findByCompanyCode(company.getSapClientCode());
        factoryOps = rfqCommonFacade.buildFactoryOptions(factorys);
    }*/  
    
    /**
     * 選取廠別
     */
    public void selectFactorys(){
        try{
            logger.debug("selectFactorys "+rfqVO.getCompanyId());
            selFactory.setMultiFactorys();
            
            rfqVO.setFactorys(selFactory.getSelFactorys());
            rfqVO.setFactoryIds(selFactory.getSelFactoryIds());

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "selectFactorys", e, true);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    } 
    
    public List<CmCompanyVO> getCompanys() {
        return companys;
    }

    public void setCompanys(List<CmCompanyVO> companys) {
        this.companys = companys;
    }

    public List<CmFactoryVO> getFactorys() {
        return factorys;
    }

    public void setFactorys(List<CmFactoryVO> factorys) {
        this.factorys = factorys;
    }

    public TenderVO getTenderVO() {
        return tenderVO;
    }

    public void setTenderVO(TenderVO tenderVO) {
        this.tenderVO = tenderVO;
    }

    public List<SelectItem> getCompanyOps() {
        return companyOps;
    }

    public void setCompanyOps(List<SelectItem> companyOps) {
        this.companyOps = companyOps;
    }

    public List<SelectItem> getFactoryOps() {
        return factoryOps;
    }

    public void setFactoryOps(List<SelectItem> factoryOps) {
        this.factoryOps = factoryOps;
    }

    public List<SelectItem> getPurOrgOps() {
        return purOrgOps;
    }

    public void setPurOrgOps(List<SelectItem> purOrgOps) {
        this.purOrgOps = purOrgOps;
    }

    public RfqVO getRfqVO() {
        return rfqVO;
    }

    public void setRfqVO(RfqVO rfqVO) {
        this.rfqVO = rfqVO;
    }

    public String getQueryResultMsg() {
        return queryResultMsg;
    }

    public void setQueryResultMsg(String queryResultMsg) {
        this.queryResultMsg = queryResultMsg;
    }

    public CmFactoryController getSelFactory() {
        return selFactory;
    }

    public void setSelFactory(CmFactoryController selFactory) {
        this.selFactory = selFactory;
    }

    public List<SelectItem> getPurGroupOps() {
        return purGroupOps;
    }

    public void setPurGroupOps(List<SelectItem> purGroupOps) {
        this.purGroupOps = purGroupOps;
    }
    //</editor-fold>

}
