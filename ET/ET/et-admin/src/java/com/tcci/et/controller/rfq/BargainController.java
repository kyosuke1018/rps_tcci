/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import static com.tcci.et.controller.rfq.RfqCreateController.DATATABLE_RESULT_PR;
import com.tcci.et.facade.EtBargainFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.facade.rfq.EtRfqVenderFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.dw.PrEbanVO;
import com.tcci.et.model.rfq.BargainVO;
import com.tcci.et.model.rfq.BargainVenderVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "bargain")
@ViewScoped
public class BargainController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 26;
    private static final String DATATABLE_RESULT = "fmMain:dtVenderList";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    @EJB EtVenderFacade venderFacade;
    @EJB EtRfqVenderFacade rfqVenderFacade;
    @EJB EtBargainFacade bargainFacade;
    
    private TenderVO tenderVO;
    private RfqVO rfqVO = new RfqVO();
    
    private BargainVO bargainVO = new BargainVO();
    private Integer nextTimes = 1;
    
    private List<VenderAllVO> rfqVenderList;// 已投標/邀標廠商
    private BaseLazyDataModel<VenderAllVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<VenderAllVO> filterResultList;
    private boolean selectAll;
    
    private List<BargainVO> bargainList;// 已設定議價紀錄
    
    private String queryVenderMsg;
    private String queryResultMsg;
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        getInitParams();// query string params
        initByTender(tenderVO);
        
        // for DEMO
        if( tenderVO==null ){
            tenderVO = new TenderVO();
            tenderVO.setCode("W2A190000010");
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Tender">
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
                logger.info("getInitParams SapClientEnum = "+(tenderVO!=null?tenderVO.getSapClientEnum():null));
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "getInitParams", e, false);
        }
    }

    /**
     * 依指定標案初始資料
     * @param tenderVO 
     */
    public void initByTender(TenderVO tenderVO){
        if( tenderVO==null || tenderVO.getId()==null ){
            logger.debug("initByTender tenderVO==null");
            return;
        }
        
        rfqVO.setCompanyId(tenderVO.getCompanyId());
        //onChangeCompany(true);
        //selFactory.setNowFactory(tenderVO.getFactoryId(), false);
        //rfqVO.setFactory(selFactory.getSelFactory());
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+tenderVO.getFactoryId());
        rfqVO = rfqCommonFacade.findRfqByTenderId(tenderVO.getId());
        
        // 已設定項目
        renderBargainSettings();
        // 初始議價設定
        initBargain();// 要再取得 bargainList 之後，才知 times 值
        
        // get rfq venders
        findRfqVenders();
        // 顯示關聯供應商
        renderRfqVenders();
    }
    
    /**
     * 查詢標案 by Code
     */
    public void onQueryTender(){
        logger.debug("onQueryTender = "+tenderVO.getCode());
        try{
            if( StringUtils.isBlank(tenderVO.getCode()) ){
                JsfUtils.addErrorMessage("請輸入標案編號!");
                return;
            }
            
            TenderCriteriaVO tenderCriteriaVO = new TenderCriteriaVO();
            tenderCriteriaVO.setCode(tenderVO.getCode());
            List<TenderVO> list = tenderFacade.findByCriteria(tenderCriteriaVO);
            if( sys.isEmpty(list) ){
                JsfUtils.addErrorMessage("請輸入正確的標案編號!");
                return;
            }
            if( list.size()>1 ){
                JsfUtils.addErrorMessage("有多筆相同的標案編號!");
                return;
            }
            tenderVO = list.get(0);
            initByTender(tenderVO);
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryTender", e, false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Rfq Vender">
    /**
     * 關聯供應商
     */
    public void findRfqVenders(){
        logger.debug("findRfqVenders ...");
        rfqVenderList = venderFacade.findByTenderId(tenderVO.getId());
    }
    
    /**
     * 顯示關聯供應商
     */
    public void renderRfqVenders(){
        selectAll = true;
        if( rfqVenderList!=null ){
            for(VenderAllVO vo : rfqVenderList){
                vo.setSelected(false);
                if( this.bargainVO!=null && this.bargainVO.getBargainVenderList()!=null ){
                    for(BargainVenderVO vender : bargainVO.getBargainVenderList()){
                        if( vo.getRfqVenderId().equals(vender.getRfqVenderId()) ){
                            vo.setSelected(true);
                            break;
                        }
                    }
                }
                selectAll = selectAll && vo.isSelected();
            }
        }

        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
        filterResultList = null; // filterValue 初始化
        lazyModel = new BaseLazyDataModel<VenderAllVO>(rfqVenderList);
    }
    
    /**
     * 全選/全不選
     */
    public void onSelAllVender(){
        logger.debug("onSelAllVender "+this.selectAll);
        try{
            if( rfqVenderList!=null ){
                for(VenderAllVO vo : rfqVenderList){
                    vo.setSelected(this.selectAll);
                }
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSelAllVender", e, false);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Bargain">
    /**
     * 查詢議價設定
     */
    public void findBargainSettings(){
        logger.debug("findBargainSettings ..."+tenderVO.getCode());
        bargainList = bargainFacade.findByRfqId(tenderVO.getId(), rfqVO.getRfqId());
        if( bargainList!=null ){
            Date now = new Date();
            for(BargainVO vo : bargainList){
                vo.setReadonly(vo.getEdate().compareTo(now)<0);// 已過日期不可改
                nextTimes = (!vo.getDisabled() && nextTimes>vo.getTimes())? nextTimes:vo.getTimes()+1;
            }
        }
    }
    
    /**
     * 顯示議價設定
     */
    public void renderBargainSettings(){
        logger.debug("renderBargainSettings ..."+tenderVO.getCode());
        try{
            findBargainSettings();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "renderBargainSettings", e, false);
        }
    }
    
    /**
     * 初始議價設定
     */
    public void initBargain(){
        bargainVO.setId(null);
        bargainVO.setTenderId(tenderVO.getId());
        bargainVO.setRfqId(rfqVO.getRfqId());
        bargainVO.setDisabled(false);
        bargainVO.setTimes(nextTimes);
        bargainVO.setSdate(DateUtils.getTodayToSecond(0, 0, 0));
        bargainVO.setEdate(DateUtils.addDate(DateUtils.getTodayToSecond(23, 59, 59), 10));
        bargainVO.setReadonly(false);
        
        bargainVO.setBargainVenderList(null);
    }
    
    /**
     * 編輯議價設定
     * @param vo 
     */
    public void doEditBargain(BargainVO vo){
        logger.debug("doEditBargain ..."+vo.getId());
        try{
            initBargain();
            ExtBeanUtils.copyProperties(bargainVO, vo);
            bargainVO.setBargainVenderList(vo.getBargainVenderList());
            
            // 顯示關聯供應商
            renderRfqVenders();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "doEditBargain", e, false);
        }
    }
    
    /**
     * 儲存議價設定
     */
    public void doSaveBargain(){
        logger.debug("doSaveBargain ..."+bargainVO.getId()+"; "+bargainVO.getEdate());
        try{
            if( !checkInput() ){
                return;
            }
            bargainFacade.saveFullBargain(bargainVO, this.getLoginUser(), false);
            
            // 已設定項目
            renderBargainSettings();
            // 初始議價設定
            initBargain();// 要再取得 bargainList 之後，才知 times 值
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "doSaveBargain", e, false);
        }
    }
    
    /**
     * 輸入檢查
     * @return 
     */
    public boolean checkInput(){
        if( bargainVO.getTimes()==null || bargainVO.getSdate()==null || bargainVO.getEdate()==null ){
            logger.error("checkInput "+bargainVO.getTimes()+" : "+bargainVO.getSdate()+" : "+bargainVO.getEdate());
            JsfUtils.addErrorMessage("不可輸入空白!");
            return false;
        }

        if( bargainVO.getEdate().compareTo(DateUtils.getToday())<0 ){
            JsfUtils.addErrorMessage("[開放報價截止時間] 不可小於今日日期!");
            return false;
        }
        
        if( this.bargainList!=null ){
            for(BargainVO vo : bargainList){
                if( bargainVO.getId()==null && !bargainVO.getId().equals(vo.getId()) ){
                    if( vo.getEdate()!=null &&  vo.getEdate().compareTo(bargainVO.getEdate()) >= 0 ){
                        JsfUtils.addErrorMessage("[開放報價截止時間] 不可小於前幾次議價設定!");
                        return false;
                    }
                }
            }
        }
        
        boolean hasSelVender = false;
        List<Long> newRfqVenderIds = new ArrayList<Long>();
        if( !sys.isEmpty(this.rfqVenderList) ){
            for(VenderAllVO vo : rfqVenderList){
                if( vo.isSelected() ){
                    newRfqVenderIds.add(vo.getRfqVenderId());
                    hasSelVender = true;
                }
            }
        }
        bargainVO.setNewRfqVenderIds(newRfqVenderIds);
        
        if( !hasSelVender ){
            JsfUtils.addErrorMessage("未選取任何廠商!");
            return false;
        }
        
        return true;
    }
    
    /**
     * 取消編輯
     */
    public void doCancelEdit(){
        logger.debug("doCancelEdit ..."+tenderVO.getCode());
        try{
            initBargain();
            
            renderRfqVenders();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "doCancelEdit", e, false);
        }
    }
    
    /**
     * 刪除議價設定
     * @param vo 
     */
    public void doDeleteBargain(BargainVO vo){
        logger.debug("doDeleteBargain ..."+vo.getId());
        try{
            vo.setDisabled(true);
            bargainFacade.saveVO(vo, this.getLoginUser(), false);
            
            // 已設定項目
            renderBargainSettings();
            // 初始議價設定
            initBargain();// 要再取得 bargainList 之後，才知 times 值
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "doDeleteBargain", e, false);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public TenderVO getTenderVO() {
        return tenderVO;
    }

    public void setTenderVO(TenderVO tenderVO) {
        this.tenderVO = tenderVO;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<VenderAllVO> getRfqVenderList() {
        return rfqVenderList;
    }

    public void setRfqVenderList(List<VenderAllVO> rfqVenderList) {
        this.rfqVenderList = rfqVenderList;
    }

    public BaseLazyDataModel<VenderAllVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<VenderAllVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<VenderAllVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<VenderAllVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public String getQueryVenderMsg() {
        return queryVenderMsg;
    }

    public void setQueryVenderMsg(String queryVenderMsg) {
        this.queryVenderMsg = queryVenderMsg;
    }

    public Integer getNextTimes() {
        return nextTimes;
    }

    public void setNextTimes(Integer nextTimes) {
        this.nextTimes = nextTimes;
    }

    public BargainVO getBargainVO() {
        return bargainVO;
    }

    public void setBargainVO(BargainVO bargainVO) {
        this.bargainVO = bargainVO;
    }

    public List<BargainVO> getBargainList() {
        return bargainList;
    }

    public void setBargainList(List<BargainVO> bargainList) {
        this.bargainList = bargainList;
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
    //</editor-fold>
}
