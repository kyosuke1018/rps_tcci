/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.CmFactoryController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.dw.facade.PrDwFacade;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.model.TenderVO;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.rfq.EtRfqEkkoFacade;
import com.tcci.et.facade.rfq.EtRfqEkpoFacade;
import com.tcci.et.facade.rfq.EtRfqPmFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.facade.sap.SapDataFacade;
import com.tcci.et.model.criteria.PrCriteriaVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.dw.PrEbanPmVO;
import com.tcci.et.model.dw.PrEbanVO;
import com.tcci.et.model.dw.T024VO;
import com.tcci.et.model.dw.T024eVO;
import com.tcci.et.model.dw.T052uVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.rfq.RfqPmVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    //public static final String DATATABLE_RESULT = "fmMain:dtResult";
    public static final String DATATABLE_RESULT_PR = "fmMain:dtPrItemList";
    public static final String DATATABLE_RESULT_RFQ = "fmMain:dtRfqItemList";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    @EJB SapDataFacade sapDataFacade;
    @EJB PrDwFacade prDwFacade;
    @EJB EtRfqEkkoFacade rfqEkkoFacade;
    @EJB EtRfqEkpoFacade rfqEkpoFacade;
    @EJB EtRfqPmFacade rfqPmFacade;
    
    @ManagedProperty(value="#{selFactory}")
    protected CmFactoryController selFactory;
    
    private List<CmCompanyVO> companys;
    private List<CmFactoryVO> factorys;
    
    private List<SelectItem> companyOps;
    private List<SelectItem> factoryOps;
    
    private List<SelectItem> purOrgOps;// 採購組織
    private List<SelectItem> purGroupOps;// 採購群組
    private List<SelectItem> payCondOps;// 付款條件
    
    private TenderVO tenderVO;
    private RfqVO rfqVO = new RfqVO();
    
    private PrCriteriaVO prCriteriaVO = new PrCriteriaVO();
    private List<PrEbanVO> prEbanList;// PR品項
    private List<PrEbanPmVO> prEbanPmList;// PR服務類品項細目
    //private List<PrEbantxHeadVO> prEbantxHeadList;
    //private List<PrEbantxItemVO> prEbantxItemList;
    private List<RfqPmVO> rfqPmList;// 詢價單服務類品項細目
    
    // for PR Items datatable
    private BaseLazyDataModel<PrEbanVO> lazyModelPrEban; // LazyModel for primefaces datatable lazy loading
    private List<PrEbanVO> filterResultListPrEban;
    // for RFQ Items datatable
    private BaseLazyDataModel<RfqEkpoVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<RfqEkpoVO> filterResultList;
    
    private List<String> prNoList = new ArrayList<String>();// 相關 PR 單號
    private String selPrNo;// 選取 PR 單號
    private String bukrs; // SAP 公司代碼
    private boolean selectAll;// 全選/全不選
    
    private String queryResultMsg;// 標案查詢結果訊息
    private String queryPrResultMsg;// PR查詢結果訊息
    private String queryPrPmMsg;//  PR PM查詢結果訊息
    private String queryRfqPmMsg;// RFQ PM查詢結果訊息
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        initCompany();
        getInitParams();// query string params
        initByTender(tenderVO);

        // for DEMO
        if( tenderVO==null ){
            tenderVO = new TenderVO();
            tenderVO.setCode("GXHL-FC19001");
        }
        // for TEST　1180000902, 1180002223, 1180002224, 1180004946
        prCriteriaVO.setBanfn("1180000261");
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
                logger.debug("getInitParams tenderVO = "+tenderVO);
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
        CmFactoryVO factory = factoryFacade.findById(tenderVO.getFactoryId());
        //onChangeCompany(true);
        //selFactory.setNowFactory(tenderVO.getFactoryId(), false);
        rfqVO.setFactory(factory); //selFactory.getSelFactory());
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+rfqVO.getFactoryId());
        //logger.info("initByTender Company = "+selFactory.getSelCompany()+", Factory = "+selFactory.getSelFactory());
        
        if( factory!=null ){
            initUIOptions(factory);
        }
        
        // for DEMO
        tenderVO.setStatus(TenderStatusEnum.NOT_SALE.getCode());
        // TODO get rfq
        rfqVO = rfqCommonFacade.findRfqByTenderId(tenderVO.getId());
        
        if( rfqVO==null || !rfqVO.isHasSet() ){// 未設定過
            rfqVO = new RfqVO();
            // RFQ Default
            rfqVO.setEkko(new RfqEkkoVO());
            rfqVO.getEkko().setTenderId(tenderVO.getId());
            rfqVO.getEkko().setBedat(DateUtils.getToday());// 詢價單日期
            rfqVO.getEkko().setAngdt(DateUtils.addDays(DateUtils.getToday(), 10));// 報價截止日
        }
        // 顯示RFQ明細
        renderRfqItems();

        // 關聯 PR
        prNoList = new ArrayList<String>();
        if( rfqVO!=null && rfqVO.getPrNos()!=null ){
            prNoList.addAll(rfqVO.getPrNos());
        }else{
            // for DEMO
            // prNoList.add("1180000261");
            prNoList.add("1180000902");
            prNoList.add("1180002223");
            prNoList.add("1180002224");
            prNoList.add("1180004946");
        }
        
        // TODO 已達報價起始日不可修改
        // TODO check readonly
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
    
    //<editor-fold defaultstate="collapsed" desc="for 公司/廠別">
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
            
            CmCompanyVO company = rfqCommonFacade.getSelectedCompany(companys, rfqVO.getCompanyId());
            logger.debug("onChangeCompany company = "+company);
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
            this.processUnknowException(this.getLoginUser(), "onOpenSelFactoryDlg", e, true);
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
    
    //<editor-fold defaultstate="collapsed" desc="for UI Options">
    /**
     *
     * @param factory
     */
    public void initUIOptions(CmFactoryVO factory){
        if( factory==null ){
            logger.error("factory==null");
            return;
        }
        bukrs = (factory!=null && factory.getCode()!=null)? factory.getCode().substring(0, 2)+"00":"";
        logger.debug("initUIOptions sapClientEnum="+factory.getSapClientCode()+", bukrs = "+bukrs);
        // 採購組織
        purOrgOps = new ArrayList<SelectItem>();
        List<T024eVO> purOrgList = sapDataFacade.findPurOrg(factory.getSapClientCode(), bukrs);
        if( purOrgList!=null ){
            for(T024eVO vo : purOrgList){
                purOrgOps.add(new SelectItem(vo.getEkorg(), vo.getEkotx()));
            }
        }
        // 採購群組
        purGroupOps = new ArrayList<SelectItem>();
        List<T024VO> purGroupList = sapDataFacade.findPurGroup(factory.getSapClientCode(), false);
        if( purGroupList!=null ){
            for(T024VO vo : purGroupList){
                purGroupOps.add(new SelectItem(vo.getEkgrp(), vo.getEknam()));
            }
        }
        // 付款條件
        payCondOps = new ArrayList<SelectItem>();
        List<T052uVO> payCondList = sapDataFacade.findPayCond(factory.getSapClientCode(), tenderVO.getLanguage());
        if( payCondList!=null ){
            for(T052uVO vo : payCondList){
                if( vo.getText1()!=null ){
                    payCondOps.add(new SelectItem(vo.getZterm(), vo.getText1()));
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for PR">
    /**
     * 請購單查詢
     */
    public void onQueryPr(){
        logger.debug("onQueryPr = "+this.prCriteriaVO.getBanfn());
        try{
            if( StringUtils.isBlank(prCriteriaVO.getBanfn()) ){
                JsfUtils.addErrorMessage("請輸入正確的請購單號!");
                return;
            }
            prCriteriaVO.setBanfn(prCriteriaVO.getBanfn().trim());
            prCriteriaVO.setMandt(this.tenderVO.getSapClient());
            // TODO 公司/廠別條件
            
            // TODO realtime sync PR
            
            prEbanList = prDwFacade.findEbanByCriteria(prCriteriaVO);// 未刪除、核發指示碼為R 或 不需簽核
            // prEbantxHeadList = prDwFacade.findEbantxHeadByCriteria(prCriteriaVO);
            // TODO 標示已勾選項目 & 數量
            selectAll = true;
            if( prEbanList!=null && !sys.isEmpty(rfqVO.getEkpoList()) ){
                for(PrEbanVO ebanVO : prEbanList){
                    ebanVO.setSelected(false);
                    for(RfqEkpoVO vo : rfqVO.getEkpoList()){
                        if( ebanVO.getBanfn().equals(vo.getBanfn()) && ebanVO.getBnfpo().equals(vo.getBnfpo()) ){
                            ebanVO.setSelected(true);
                            ebanVO.setQuantity(vo.getMenge());
                            break;
                        }
                    }
                    selectAll = selectAll && ebanVO.isSelected();
                }
            }
            
            // TODO check PR (物料類型)
            
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT_PR);
            filterResultListPrEban = null; // filterValue 初始化
            lazyModelPrEban = new BaseLazyDataModel<PrEbanVO>(prEbanList);
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryPr", e, false);
        }
    }
    
    /**
     * 選取已加入 PR
     */
    public void onSelectPr(){
        logger.debug("onSelectPr = "+this.selPrNo);
        prCriteriaVO.setBanfn(selPrNo);
        onQueryPr();
    }
    
    /**
     * 加入請購單
     */
    public void onAddPr(){
        logger.debug("onAddPr = "+this.prCriteriaVO.getBanfn());
        try{
            if( StringUtils.isBlank(prCriteriaVO.getBanfn()) ){
                JsfUtils.addErrorMessage("請輸入正確的請購單號!");
                return;
            }
            if( prNoList.contains(prCriteriaVO.getBanfn().trim()) ){
                JsfUtils.addErrorMessage("此請購單已加入過!");
                return;
            }
            prNoList.add(prCriteriaVO.getBanfn().trim());
            
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onAddPr", e, false);
        }
    }
    
    /**
     * 全選/全不選 PR ITEM
     */
    public void onSelAllPrItem(){
        logger.debug("onSelAllPrItem "+this.selectAll);
        try{
            if( prEbanList!=null ){
                for(PrEbanVO vo : prEbanList){
                    vo.setSelected(this.selectAll);
                    vo.setQuantity(vo.isSelected()?vo.getMenge():null);
                }
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSelAllPrItem", e, false);
        }
    }
    
    /**
     * 選取 PR ITEM
     * @param selVO
     */
    public void onSelPrItem(PrEbanVO selVO){
        logger.debug("onSelPrItem "+selVO.getBnfpo());
        try{
            selVO.setQuantity(selVO.isSelected()?selVO.getMenge():null);
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryPr", e, false);
        }
    }
    
    /**
     * 檢視服務類明細
     * @param selVO
     */
    public void onViewPrSrvItems(PrEbanVO selVO){
        logger.debug("onViewPrSrvItems ... "+selVO.getBanfn()+" : "+selVO.getBnfpo());
        try{
            queryPrPmMsg = "["+selVO.getBnfpo()+"]"+selVO.getTxz01()+"：";
            prEbanPmList = prDwFacade.findEbanPmByKey(selVO.getMandt(), selVO.getBanfn(), selVO.getBnfpo());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewPrSrvItems", e, false);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for RFQ">
    /**
     * 加入詢價項目
     */
    public void onAddRfqItems(){
        logger.debug("onAddRfqItems ... ");
        try{
            if( !sys.isEmpty(prEbanList) ){
                String banfn =  prEbanList.get(0).getBanfn();
                // 先加入 PR 選單
                if( !prNoList.contains(banfn) ){
                    prNoList.add(banfn);
                }
                // for debug
                for(PrEbanVO vo : prEbanList){
                    logger.debug("onAddRfqItems ... "+vo.getBnfpo()+":"+vo.isSelected()+":"+vo.getQuantity());
                }
                // TODO check input
                
                resetRfqItems(rfqVO, banfn, prEbanList);// 重設 RFQ 中指定 PR 的項目
                JsfUtils.buildSuccessCallback();
            }else{
                JsfUtils.addErrorMessage("無選取任何項目!");
                JsfUtils.buildErrorCallback();
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onAddRfqItems", e, true);
        }
    }
    
    /**
     * 重設 RFQ 中指定 PR 的項目
     * @param rfqVO
     * @param banfn
     * @param ebanList
     */
    public void resetRfqItems(RfqVO rfqVO, String banfn, List<PrEbanVO> ebanList){
        logger.debug("resetRfqItems banfn="+banfn+", ebanList="+sys.size(ebanList));
        if( rfqVO==null || banfn==null ){
            logger.error("resetRfqItems rfqVO==null || banfn==null");
            return;
        }
        List<RfqEkpoVO> newList = new ArrayList<RfqEkpoVO>();
        List<Long> existedBnfpos = new ArrayList<Long>();
        List<Long> selSrvBnfpos = new ArrayList<Long>();// 服務類
        Map<Long, Long> prRFqBnfposMap = new HashMap<Long, Long>();// for 重設 服務類明細
        Long maxEbelp = (rfqVO.getEkpoList()!=null)?rfqVO.getEkpoList().size():0L;
        
        long count = 1L;
        // 已存在 (變更數量 or 移除)
        if( rfqVO.getEkpoList()!=null ){
            for(RfqEkpoVO vo : rfqVO.getEkpoList()){
                if( !banfn.equals(vo.getBanfn()) ){// 非處理中 PR
                    vo.setEbelp(count);
                    newList.add(vo);
                    count++;
                    if( "9".equals(vo.getPstyp()) ){// 服務類
                        resetRfqSrvItems(rfqVO, vo.getBanfn(), vo.getBnfpo(), vo.getEbelp());// 變更 PM 中的 EBELP
                        selSrvBnfpos.add(vo.getBnfpo());
                        prRFqBnfposMap.put(vo.getBnfpo(), vo.getEbelp());// for 重設 服務類明細
                    }
                }else{// 處理中 PR
                    boolean selected = false;
                    for(PrEbanVO prEbanVO : ebanList){
                        if( prEbanVO.isSelected() && prEbanVO.getBnfpo()!=null && prEbanVO.getBnfpo().equals(vo.getBnfpo()) ){
                            if( (prEbanVO.getMatnr()==null && vo.getMatnr()==null) // 服務類 matnr = null
                                    || (prEbanVO.getMatnr()!=null && prEbanVO.getMatnr().equals(vo.getMatnr())) ){
                                vo.setMenge(prEbanVO.getQuantity());// 變更數量
                                logger.debug("resetRfqItems update "+vo.getBnfpo());
                                vo.setEbelp(count);
                                newList.add(vo);
                                count++;
                                existedBnfpos.add(vo.getBnfpo());
                                if( "9".equals(vo.getPstyp()) ){// 服務類
                                    resetRfqSrvItems(rfqVO, vo.getBanfn(), vo.getBnfpo(), vo.getEbelp());// 變更 PM 中的 EBELP
                                    selSrvBnfpos.add(vo.getBnfpo());
                                    prRFqBnfposMap.put(vo.getBnfpo(), vo.getEbelp());// for 重設 服務類明細
                                }
                                selected = true;
                            }else{
                                logger.error("resetRfqItems error bnfpo = "+prEbanVO.getBnfpo()+":"+prEbanVO.getMatnr()+"!="+vo.getMatnr());
                            }
                            break;
                        }
                    }// end of for
                    
                    // vo.setLoekz(selected?null:"X"); no used
                    //if( selected ){
                    //    maxEbelp = (maxEbelp < vo.getEbelp())?vo.getEbelp():maxEbelp;
                    //}
                }
            }
        }

        // 新加入
        if( ebanList!=null ){
            for(PrEbanVO prEbanVO : ebanList){
                if( prEbanVO.isSelected() ){
                    if( !existedBnfpos.contains(prEbanVO.getBnfpo()) ){
                        RfqEkpoVO vo = new RfqEkpoVO();
                        ExtBeanUtils.copyProperties(vo, prEbanVO);
                        vo.setNetpr(prEbanVO.getPreis());// 預算單價
                        vo.setMenge(prEbanVO.getQuantity());// 變更數量
                        //maxEbelp = maxEbelp + 1;
                        vo.setEbelp(count);
                        newList.add(vo);
                        count++;
                        logger.debug("resetRfqItems new "+vo.getBnfpo());
                        
                        if( "9".equals(vo.getPstyp()) ){// 服務類
                            selSrvBnfpos.add(vo.getBnfpo());
                            prRFqBnfposMap.put(vo.getBnfpo(), vo.getEbelp());// for 重設 服務類明細
                        }
                    }
                }
            }
        }
        
        rfqVO.setEkpoList(newList);// 詢價項目
        
        // 重設 服務類明細
        resetRfqSrvItems(rfqVO, banfn, selSrvBnfpos, prRFqBnfposMap);
        
        // 顯示RFQ明細
        renderRfqItems();
    }
    
    
    /**
     * 
     * @param rfqVO
     * @param banfn
     * @param bnfpo
     * @param newEbelp 
     */
    public void resetRfqSrvItems(RfqVO rfqVO, String banfn, Long bnfpo, Long newEbelp){
        if( rfqVO.getPmList()!=null ){
            for(RfqPmVO vo : rfqVO.getPmList()){
                if( vo.getBanfn().equals(banfn) && vo.getBnfpo().equals(bnfpo) ){
                    vo.setEbelp(newEbelp);
                }
            }
        }
    }
    
    /**
     * 重設 服務類明細
     * @param rfqVO
     * @param banfn
     * @param selSrvBnfpos 
     * @param prRFqBnfposMap 
     */
    public void resetRfqSrvItems(RfqVO rfqVO, String banfn, List<Long> selSrvBnfpos, Map<Long, Long> prRFqBnfposMap){
        logger.debug("resetRfqSrvItems banfn="+banfn+", ebanList="+sys.size(selSrvBnfpos));
        if( rfqVO==null || banfn==null ){
            logger.error("resetRfqSrvItems rfqVO==null || banfn==null");
            return;
        }
        
        List<RfqPmVO> newList = new ArrayList<RfqPmVO>();
        List<String> oriBanfnList = new ArrayList<String>();// 原有 PR 
        List<Long> oriBnfpoList = new ArrayList<Long>();// 此 PR 原有 PR 項目
        
        // 已存在 
        if( rfqVO.getPmList()!=null ){
            for(RfqPmVO vo : rfqVO.getPmList()){
                if( !banfn.equals(vo.getBanfn()) ){// 非處理中 PR
                    newList.add(vo);
                }else{// 處理中 PR
                    if( selSrvBnfpos!=null && selSrvBnfpos.contains(vo.getBnfpo()) ){// 有選取
                        newList.add(vo);
                    }
                    if( !oriBnfpoList.contains(vo.getBnfpo()) ){
                        oriBnfpoList.add(vo.getBnfpo());
                    }
                }

                if( !oriBanfnList.contains(vo.getBanfn()) ){
                    oriBanfnList.add(vo.getBanfn());
                }
            }
        }
        
        // 新增 (抓取服務類明細加入)
        // 1. 此 PR 是新增 - 加入所有選取的服務類明細
        if( !oriBanfnList.contains(banfn) ){
            if( selSrvBnfpos!=null ){
                for(Long bnfpo : selSrvBnfpos){
                    List<RfqPmVO> pmList = toRfqPmByBnfpo(rfqVO.getTenderId(), rfqVO.getRfqId(), prRFqBnfposMap.get(bnfpo), rfqVO.getEkko().getMandt(), banfn, bnfpo);
                    newList.addAll(pmList);
                }
            }
        }else{
            // 2. 此 PR 項目 是新增 - 加入選取新增項目的服務類明細
            if( selSrvBnfpos!=null ){
                for(Long bnfpo : selSrvBnfpos){
                    if( !oriBnfpoList.contains(bnfpo) ){
                        List<RfqPmVO> pmList = toRfqPmByBnfpo(rfqVO.getTenderId(), rfqVO.getRfqId(), prRFqBnfposMap.get(bnfpo), rfqVO.getEkko().getMandt(), banfn, bnfpo);
                        newList.addAll(pmList);
                    }
                }
            }
        }
        
        rfqVO.setPmList(newList);
    }
    
    /**
     * 轉指定 PR 服務類明細 至  RFQ 服務類明細
     * @param tenderId
     * @param rfqId
     * @param ebelp
     * @param mandt
     * @param banfn
     * @param bnfpo
     * @return 
     */
    public List<RfqPmVO> toRfqPmByBnfpo(Long tenderId, Long rfqId, Long ebelp, String mandt, String banfn, Long bnfpo){
        List<RfqPmVO> newList = new ArrayList<RfqPmVO>();
        
        List<PrEbanPmVO> pmList = prDwFacade.findEbanPmByKey(mandt, banfn, bnfpo);
        if( pmList!=null ){
            for(PrEbanPmVO pm : pmList ){
                RfqPmVO rfqPmVO = new RfqPmVO();
                ExtBeanUtils.copyProperties(rfqPmVO, pm);
                rfqPmVO.setTenderId(tenderId);
                rfqPmVO.setRfqId(rfqId);
                rfqPmVO.setEbelp(ebelp);
                //rfqPmVO.setEbeln(banfn);
                //rfqPmVO.setEbelp(bnfpo);

                newList.add(rfqPmVO);
            }
        }

        return newList;
    }
    
    /**
     * 顯示RFQ明細
     */
    public void renderRfqItems(){
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT_RFQ);
        filterResultList = null; // filterValue 初始化
        lazyModel = new BaseLazyDataModel<RfqEkpoVO>(rfqVO.getEkpoList());
    }
    
    /**
     * 檢視服務類明細
     * @param selVO
     */
    public void onViewRfqSrvItems(RfqEkpoVO selVO){
        logger.debug("onViewRfqSrvItems ... "+selVO.getBanfn()+" : "+selVO.getBnfpo());
        try{
            queryRfqPmMsg = "["+selVO.getEbelp()+"]"+selVO.getTxz01()+"：";
            // rfqPmList = prDwFacade.findByEbanPmByKey(selVO.getMandt(), selVO.getBanfn(), selVO.getBnfpo());
            rfqPmList = new ArrayList<RfqPmVO>();
            if( rfqVO.getPmList()!=null ){
                for(RfqPmVO vo : rfqVO.getPmList()){
                    if( vo.getEbelp().equals(selVO.getEbelp()) ){
                        rfqPmList.add(vo);
                    }
                }
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewRfqSrvItems", e, false);
        }
    }
    
    /**
     * 儲存 RFQ
     */
    public void onSaveRfq(){
        logger.debug("onSaveRfq ekpo size = "+sys.size(rfqVO.getEkpoList()));
        try{
            if( !checkRfqInput() ){
                JsfUtils.buildErrorCallback();
                return;
            }
            
            //  RFQ 主檔 EKKO
            rfqVO.setBukrs(bukrs);
            rfqCommonFacade.saveRfq(tenderVO, rfqVO, this.getLoginUser(), false);
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSaveRfq", e, true);
        }
    }
    
    /**
     * 檢查詢價單輸入
     * @return
     */
    public boolean checkRfqInput(){
        if( sys.isEmpty(rfqVO.getEkpoList()) ){
            JsfUtils.addErrorMessage("未加入任何詢價項目!");
            return false;
        }
        return true;
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

    public Boolean getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Boolean selectAll) {
        this.selectAll = selectAll;
    }
    
    public BaseLazyDataModel<RfqEkpoVO> getLazyModel() {
        return lazyModel;
    }
    
    public void setLazyModel(BaseLazyDataModel<RfqEkpoVO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public List<RfqEkpoVO> getFilterResultList() {
        return filterResultList;
    }
    
    public void setFilterResultList(List<RfqEkpoVO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    
    public BaseLazyDataModel<PrEbanVO> getLazyModelPrEban() {
        return lazyModelPrEban;
    }
    
    public void setLazyModelPrEban(BaseLazyDataModel<PrEbanVO> lazyModelPrEban) {
        this.lazyModelPrEban = lazyModelPrEban;
    }
    
    public List<PrEbanVO> getFilterResultListPrEban() {
        return filterResultListPrEban;
    }
    
    public void setFilterResultListPrEban(List<PrEbanVO> filterResultListPrEban) {
        this.filterResultListPrEban = filterResultListPrEban;
    }
    
    public List<String> getPrNoList() {
        return prNoList;
    }
    
    public void setPrNoList(List<String> prNoList) {
        this.prNoList = prNoList;
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
    
    public List<PrEbanVO> getPrEbanList() {
        return prEbanList;
    }
    
    public void setPrEbanList(List<PrEbanVO> prEbanList) {
        this.prEbanList = prEbanList;
    }
    
    public List<PrEbanPmVO> getPrEbanPmList() {
        return prEbanPmList;
    }
    
    public void setPrEbanPmList(List<PrEbanPmVO> prEbanPmList) {
        this.prEbanPmList = prEbanPmList;
    }
    
    public String getQueryPrResultMsg() {
        return queryPrResultMsg;
    }
    
    public void setQueryPrResultMsg(String queryPrResultMsg) {
        this.queryPrResultMsg = queryPrResultMsg;
    }
    
    public PrCriteriaVO getPrCriteriaVO() {
        return prCriteriaVO;
    }
    
    public void setPrCriteriaVO(PrCriteriaVO prCriteriaVO) {
        this.prCriteriaVO = prCriteriaVO;
    }
    
    public String getSelPrNo() {
        return selPrNo;
    }
    
    public void setSelPrNo(String selPrNo) {
        this.selPrNo = selPrNo;
    }

    public List<RfqPmVO> getRfqPmList() {
        return rfqPmList;
    }

    public void setRfqPmList(List<RfqPmVO> rfqPmList) {
        this.rfqPmList = rfqPmList;
    }
    
    public String getQueryPrPmMsg() {
        return queryPrPmMsg;
    }
    
    public void setQueryPrPmMsg(String queryPrPmMsg) {
        this.queryPrPmMsg = queryPrPmMsg;
    }
    
    public String getQueryRfqPmMsg() {
        return queryRfqPmMsg;
    }
    
    public void setQueryRfqPmMsg(String queryRfqPmMsg) {
        this.queryRfqPmMsg = queryRfqPmMsg;
    }
    
    public List<SelectItem> getPayCondOps() {
        return payCondOps;
    }
    
    public void setPayCondOps(List<SelectItem> payCondOps) {
        this.payCondOps = payCondOps;
    }
    
    public String getBukrs() {
        return bukrs;
    }
    
    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }
    
    public List<SelectItem> getPurGroupOps() {
        return purGroupOps;
    }
    
    public void setPurGroupOps(List<SelectItem> purGroupOps) {
        this.purGroupOps = purGroupOps;
    }
    //</editor-fold>
    
}
