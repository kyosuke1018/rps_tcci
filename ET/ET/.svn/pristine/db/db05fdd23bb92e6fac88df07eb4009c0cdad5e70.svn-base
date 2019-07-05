/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.FileController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.dw.facade.PrDwFacade;
import com.tcci.et.enums.CurrencyCodeEnum;
import com.tcci.et.enums.QuoteStatusEnum;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtVenderAllFacade;
import com.tcci.et.facade.rfq.EtQuotationFacade;
import com.tcci.et.facade.rfq.EtQuotationItemFacade;
import com.tcci.et.facade.rfq.EtQuotationPmFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.criteria.VenderCriteriaVO;
import com.tcci.et.model.rfq.QuotationItemVO;
import com.tcci.et.model.rfq.QuotationPmVO;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author peter.pan
 */
@ManagedBean(name = "quote")
@ViewScoped
public class OuoteInputController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 24;
    //public static final String DATATABLE_RESULT = "fmMain:dtResult";
    //public static final String DATATABLE_RESULT_PR = "fmMain:dtPrItemList";
    public static final String DATATABLE_RESULT_RFQ = "fmMain:dtRfqItemList";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    @EJB EtQuotationFacade quotationFacade;
    @EJB EtQuotationItemFacade quotationItemFacade;
    @EJB EtQuotationPmFacade quotationPmFacade;
    @EJB PrDwFacade prDwFacade;
    @EJB EtVenderAllFacade venderAllFacade;

    @ManagedProperty(value = "#{fileController}")
    private FileController fileController;
    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
    
    public TenderVO tenderVO = new TenderVO();
    public RfqVO rfqVO = new RfqVO();
    public QuotationVO quoVO;
    public boolean doNext = false;
    
    public List<VenderAllVO> venderList;
    public List<CurrencyCodeEnum> curList;
    
    //public VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
    public QuotationVO quoteVO = new QuotationVO();
    
    // for  QuotationItem datatable
    private List<QuotationItemVO> quoteList;
    private BaseLazyDataModel<QuotationItemVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<QuotationItemVO> filterResultList;
    
    public List<QuotationPmVO> pmList;
    
    public String queryResultMsg;
    public String queryRfqPmMsg;
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        getInitParams();// query string params
        initByTender(tenderVO);
        
        // for DEMO
        tenderVO = new TenderVO();
        tenderVO.setCode("GXHL-FC19001");
        
        // TODO currency
        curList = Arrays.asList(CurrencyCodeEnum.values());
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Tender">
    /**
     * query string params 初始處理
     */
    public void getInitParams(){
        try{
            // for TEST
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
            logger.error("initByTender  tenderVO==null");
            return;
        }
        
        rfqVO.setCompanyId(tenderVO.getCompanyId());
        //onChangeCompany(true);
        //selFactory.setNowFactory(tenderVO.getFactoryId(), false);
        //rfqVO.setFactory(selFactory.getSelFactory());
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+rfqVO.getFactoryId());
        
        // get rfq venders
        venderList = venderAllFacade.findByTenderId(tenderVO.getId());
        
        // TODO get rfq
        rfqVO = rfqCommonFacade.findRfqByTenderId(tenderVO.getId());
        if( rfqVO==null ){// 未設定過
            rfqVO = new RfqVO();
            // RFQ Default 
            rfqVO.setEkko(new RfqEkkoVO());
            rfqVO.getEkko().setTenderId(tenderVO.getId());
            rfqVO.getEkko().setBedat(DateUtils.getToday());// 詢價單日期
            rfqVO.getEkko().setAngdt(DateUtils.addDays(DateUtils.getToday(), 10));// 報價截止日        
        }

        // def currrency
        if( quoteVO.getId()==null ){
            quoteVO.setDisabled(false);
            quoteVO.setDiscount(null);// X
            quoteVO.setExRate(null);// X
            quoteVO.setExpiretime(null);// UI
            quoteVO.setInvoice(null);// UI
            quoteVO.setLast(null);
            quoteVO.setMemberId(null);
            quoteVO.setMemo(null);// UI
            quoteVO.setQuotetime(null);// UI
            quoteVO.setRfqId(rfqVO.getEkko().getId());
            quoteVO.setRfqVenderId(null);// UI
            quoteVO.setStatus(QuoteStatusEnum.TEMP.getCode());// 暫存
            quoteVO.setTaxRate(null);// UI
            quoteVO.setTenderId(tenderVO.getId());
            quoteVO.setCurQuo(rfqVO.getEkko().getWaers());// 預設
            quoteVO.setCurRfq(rfqVO.getEkko().getWaers());// 詢價單幣別

            quoteVO.setNetAmtQuo(null);
            quoteVO.setNetAmtRfq(null);
            quoteVO.setTaxQuo(null);
            quoteVO.setTaxRfq(null);
            quoteVO.setTotalAmtQuo(null);
            quoteVO.setTotalAmtRfq(null);

        }     
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
    
    /**
     * 選取供應商
     */
    public void onChangeVender(){
        logger.debug("onChangeVender ... "+quoteVO.getRfqVenderId());
        try{
            getVenderInfo(tenderVO.getId(), quoteVO.getRfqVenderId());
            getQuotationInfo(tenderVO.getId(), quoteVO.getRfqVenderId());
            doNext = sys.isId(quoteVO.getVenderId());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryVender", e, false);
        }
    }
    
    /**
     * 供應商資訊
     * @param tenderId
     * @param venderId 
     */
    public void getVenderInfo(Long tenderId, Long venderId){
        // TODO
    }
    
    /**
     * 報價資訊
     * @param tenderId
     * @param rfqVenderId 
     */
    public void getQuotationInfo(Long tenderId, Long rfqVenderId){
        logger.debug("getQuotationInfo tenderId = "+tenderId+", rfqVenderId = "+rfqVenderId);
        quoteList = quotationItemFacade.findByRfq(rfqVO.getTenderId(), rfqVO.getRfqId(), rfqVenderId, null);
        
        // for DEMO
        /*
            {id: 1, ebelp:1, txz01:"乙炔减压器 YQE-213", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:120, netwr:120, brtwr:1200, waers: 'RMB'},
            {id: 2, ebelp:2, txz01:"氧气减压器 YQY-12", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:115, netwr:115, brtwr:1150, waers: 'RMB'},
            {id: 3, ebelp:3, txz01:"乙炔管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 4, ebelp:4, txz01:"氧气管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 5, ebelp:5, txz01:"油石 200X50X25 420#", menge:2, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:25640, netwr:25640, brtwr:51.28, waers: 'RMB'},
            {id: 6, ebelp:6, txz01:"聚西先胺气管 FESTO:PAN 12x1.75  152702", menge:20, meins:"M", lfdat:"2018-02-28", peinh:1, netpr:45, netwr:45, brtwr:900, waers: 'RMB'},
        */
        if( sys.isEmpty(quoteList) ){
            String[] txz01Ary = new String[]{"乙炔减压器 YQE-213", "氧气减压器 YQY-12", "乙炔管 φ8mm 30m/RL" , "氧气管 φ8mm 30m/RL", "油石 200X50X25 420#", "聚西先胺气管 FESTO:PAN 12x1.75  152702"};
            quoteList = new ArrayList<QuotationItemVO>();
            for(int i=0; i<txz01Ary.length; i++){
                QuotationItemVO vo = new QuotationItemVO();
                vo.setEbelp(i+1L);
                vo.setMatnrUI("1000000"+Integer.toString(i));
                vo.setTxz01(txz01Ary[i]);
                vo.setMenge(BigDecimal.valueOf(10));
                vo.setPeinh(BigDecimal.valueOf(1));
                vo.setMeins("PC");
                vo.setEindt(DateUtils.addDate(DateUtils.getToday(), 10));

                quoteList.add(vo);
            }
        }
        //rfqVO.setEkpoList(ekpoList);
        logger.debug("initByTender quoteList = "+sys.size(quoteList));

        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT_RFQ);
        filterResultList = null; // filterValue 初始化
        lazyModel = new BaseLazyDataModel<QuotationItemVO>(quoteList);
    }
    
    /**
     * 提供報價項目
     * @param vo
     */
    public void onSelRfqItem(QuotationItemVO vo){
        logger.debug("onSelRfqItem ebelp = "+vo.getEbelp());
        if( vo.getMenge()==null ){// 預設
            vo.setMenge(vo.getRfqMenge());
        }
    }

    /**
     * 檢視服務類明細
     * @param selVO 
     */
    public void onViewRfqSrvItems(QuotationItemVO selVO){
        logger.debug("onViewRfqSrvItems ... "+selVO.getRfqId()+" : "+selVO.getEbelp());
        try{
            // 標示目前編輯中的服務類項目
            QuotationItemVO editItem = null;
            if( this.quoteList!=null ){
                for(QuotationItemVO vo : quoteList){
                    vo.setCurSrvItem(vo.getEbelp().equals(selVO.getEbelp()));
                    if( vo.isCurSrvItem()){
                        editItem = vo;
                    }
                }
            }
            
            if( editItem==null ){
                logger.error("onViewRfqSrvItems error editItem==null");
                JsfUtils.buildErrorCallback();
                return;
            }

            // 服務類明細
            queryRfqPmMsg = "["+selVO.getEbelp()+"]"+selVO.getTxz01()+"：";
            if( editItem.getPmList()==null ){// 未編輯過
                pmList = quotationPmFacade.findByRfqItem(selVO.getTenderId(), selVO.getRfqId(), selVO.getEbelp());
                editItem.setPmList(pmList);
            }else{
                pmList = new ArrayList<QuotationPmVO>();
                pmList.addAll(editItem.getPmList());
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewRfqSrvItems", e, false);
        }
    }
    
    /**
     * 關閉服務類明細
     */
    public void onCloseSrvItems(){
        logger.debug("onCloseSrvItems ...");
        pmList = null;
    }
    
    /**
     * 確定服務類明細報價
     */
    public void onConfirmSrvQuote(){
        logger.debug("onConfirmSrvQuote pmList = "+sys.size(pmList));
        try{
            // 目前編輯中的服務類項目
            if( this.quoteList!=null ){
                for(QuotationItemVO vo : quoteList){
                    if( vo.isCurSrvItem() ){
                        if( pmList!=null ){
                            BigDecimal total = BigDecimal.ZERO;
                            for(QuotationPmVO pm : pmList){
                                CurrencyCodeEnum cur = CurrencyCodeEnum.getFromCode(pm.getWaers());
                                if( cur==null ){
                                    logger.error("onConfirmSrvQuote error cur==null");
                                    JsfUtils.buildErrorCallback();
                                    return;
                                }
                                pm.setNetwr(pm.getTotalPrice().setScale(cur.getScale(), cur.getRoundingMode()));// 細目總價
                                logger.debug("onConfirmSrvQuote tbtwr = "+pm.getTbtwr()+", netwr = "+pm.getNetwr());
                                total = total.add(pm.getNetwr());
                            }
                            vo.setBrtwr(total);// 項目總價
                        }
                        vo.setPmList(pmList);
                        vo.setCurSrvItem(false);
                        onCloseSrvItems();
                        break;
                    }
                }
            }
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onConfirmSrvQuote", e, true);
        }
    }
    
    /**
     * 暫存報價
     */
    public void onSaveQuote(){
        logger.debug("onSaveQuote ... ");
        try{
            if( !checkQuotation() ){
                //quotationFacade.saveByRfq(rfqVO, quoteList, this.getLoginUser(), false);
                JsfUtils.buildErrorCallback();
                return;
            }
            
            logger.debug("onSaveQuote CurQuo = "+quoteVO.getCurQuo());
            for(QuotationItemVO vo : quoteList){
                logger.debug("onSaveQuote isSelected = "+vo.isSelected());
                logger.debug("onSaveQuote brtwr = "+vo.getBrtwr());
                logger.debug("onSaveQuote PmList = "+sys.size(vo.getPmList()));
            }
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSaveQuote", e, true);
        }
    }
    
    /**
     * 檢查報價輸入
     * @return 
     */
    public boolean checkQuotation(){
        // TODO
        return true;
    }
    
    /**
     * 確定送出報價
     */
    public void onSendQuote(){
        logger.debug("onSendQuote ... ");
        try{
            quoteVO.setStatus(QuoteStatusEnum.CONFIRM.getCode());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSendQuote", e, true);
        }
    }
    
    /**
     * 輸入下一供應商報價
     */
    public void onNextVender(){
        logger.debug("onNextVender ... ");
        try{
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onNextVender", e, true);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    public TenderVO getTenderVO() {
        return tenderVO;
    }

    public void setTenderVO(TenderVO tenderVO) {
        this.tenderVO = tenderVO;
    }

    public String getQueryResultMsg() {
        return queryResultMsg;
    }

    public void setQueryResultMsg(String queryResultMsg) {
        this.queryResultMsg = queryResultMsg;
    }

    public RfqVO getRfqVO() {
        return rfqVO;
    }

    public void setRfqVO(RfqVO rfqVO) {
        this.rfqVO = rfqVO;
    }

    public List<QuotationPmVO> getPmList() {
        return pmList;
    }

    public void setPmList(List<QuotationPmVO> pmList) {
        this.pmList = pmList;
    }

    public String getQueryRfqPmMsg() {
        return queryRfqPmMsg;
    }

    public void setQueryRfqPmMsg(String queryRfqPmMsg) {
        this.queryRfqPmMsg = queryRfqPmMsg;
    }

    public QuotationVO getQuoVO() {
        return quoVO;
    }

    public void setQuoVO(QuotationVO quoVO) {
        this.quoVO = quoVO;
    }

    public List<VenderAllVO> getVenderList() {
        return venderList;
    }

    public void setVenderList(List<VenderAllVO> venderList) {
        this.venderList = venderList;
    }

    public List<QuotationItemVO> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<QuotationItemVO> quoteList) {
        this.quoteList = quoteList;
    }

    public BaseLazyDataModel<QuotationItemVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<QuotationItemVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<QuotationItemVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<QuotationItemVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public List<CurrencyCodeEnum> getCurList() {
        return curList;
    }

    public void setCurList(List<CurrencyCodeEnum> curList) {
        this.curList = curList;
    }

    public QuotationVO getQuoteVO() {
        return quoteVO;
    }

    public void setQuoteVO(QuotationVO quoteVO) {
        this.quoteVO = quoteVO;
    }

    public boolean isDoNext() {
        return doNext;
    }

    public void setDoNext(boolean doNext) {
        this.doNext = doNext;
    }
    //</editor-fold>
}
