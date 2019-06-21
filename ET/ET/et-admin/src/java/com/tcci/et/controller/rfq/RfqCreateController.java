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
import com.tcci.et.model.TenderVO;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.criteria.PrCriteriaVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.dw.PrEbanPmVO;
import com.tcci.et.model.dw.PrEbanVO;
import com.tcci.et.model.dw.PrEbantxHeadVO;
import com.tcci.et.model.dw.PrEbantxItemVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
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
    //public static final String DATATABLE_RESULT = "fmMain:dtResult";
    public static final String DATATABLE_RESULT_PR = "fmMain:dtPrItemList";
    public static final String DATATABLE_RESULT_RFQ = "fmMain:dtRfqItemList";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    @EJB PrDwFacade prDwFacade;
    
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
    
    public PrCriteriaVO prCriteriaVO = new PrCriteriaVO();
    public List<PrEbanVO> prEbanList;
    public List<PrEbanPmVO> prEbanPmList;
    public List<PrEbantxHeadVO> prEbantxHeadList;
    public List<PrEbantxItemVO> prEbantxItemList;

    public List<PrEbanPmVO> rfqPmList;
    
    // for PR Items datatable
    private BaseLazyDataModel<PrEbanVO> lazyModelPrEban; // LazyModel for primefaces datatable lazy loading
    private List<PrEbanVO> filterResultListPrEban;
    // for RFQ Items datatable
    private BaseLazyDataModel<RfqEkpoVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<RfqEkpoVO> filterResultList;
    
    public List<String> prNoList = new ArrayList<String>();
    public String selPrNo;
    
    public String queryResultMsg;
    public String queryPrResultMsg;
    public String queryPrPmMsg;
    public String queryRfqPmMsg;
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        initCompany();
        getInitParams();// query string params
        initByTender(tenderVO);
        // for TEST　1180000902, 1180002223, 1180002224, 1180004946
        prCriteriaVO.setBanfn("1180000261");
        prNoList.add("1180000261");
        prNoList.add("1180000902");
        prNoList.add("1180002223");
        prNoList.add("1180002224");
        prNoList.add("1180004946");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Tender">
    /**
     * query string params 初始處理
     */
    public void getInitParams(){
        try{
            // for TEST
            String tenderIdStr = "10"; // JsfUtils.getRequestParameter("tenderId");
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
        
        // TODO get rfq
        rfqVO = rfqCommonFacade.findRfqByTenderId(tenderVO.getId());
        if( rfqVO==null ){// 未設定過
            rfqVO = new RfqVO();
            // RFQ Default 
            rfqVO.setEkko(new RfqEkkoVO());
            rfqVO.getEkko().setBedat(DateUtils.getToday());// 詢價單日期
            rfqVO.getEkko().setAngdt(DateUtils.addDays(DateUtils.getToday(), 10));// 報價截止日        
        }
    }

    public void onQueryTender(){
        logger.debug("onQueryTender = "+this.prCriteriaVO.getBanfn());
        try{
            if( StringUtils.isBlank(tenderVO.getCode()) ){
                JsfUtils.addErrorMessage("請輸入標案編號!");
                return;
            }
            
            TenderCriteriaVO tenderCriteriaVO = new TenderCriteriaVO();
            tenderCriteriaVO.setCode(tenderVO.getCode());
            List<TenderVO> list = tenderFacade.findByCriteria(tenderCriteriaVO);
            if( list==null ){
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
    
    //<editor-fold defaultstate="collapsed" desc="for PR">
    public void onQueryPr(){
        logger.debug("onQueryPr = "+this.prCriteriaVO.getBanfn());
        try{
            if( StringUtils.isBlank(prCriteriaVO.getBanfn()) ){
                JsfUtils.addErrorMessage("請輸入正確的請購單號!");
                return;
            }
            prCriteriaVO.setBanfn(prCriteriaVO.getBanfn().trim());
            prCriteriaVO.setMandt(this.tenderVO.getSapClient());
            
            prEbanList = prDwFacade.findByEbanByCriteria(prCriteriaVO);
            prEbantxHeadList = prDwFacade.findByEbantxHeadByCriteria(prCriteriaVO);
            
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT_PR);
            filterResultListPrEban = null; // filterValue 初始化
            lazyModelPrEban = new BaseLazyDataModel<PrEbanVO>(prEbanList);
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryPr", e, false);
        }
    }
    
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
            prEbanPmList = prDwFacade.findByEbanPmByKey(selVO.getMandt(), selVO.getBanfn(), selVO.getBnfpo());
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
        List<Integer> existedBnfpos = new ArrayList<Integer>();
        Integer maxEbelp = 0;
        
        // 已存在
        if( rfqVO.getEkpoList()!=null ){
            for(RfqEkpoVO vo : rfqVO.getEkpoList()){
                if( !banfn.equals(vo.getBanfn()) ){// 非處理中 PR
                    newList.add(vo);
                }else{// 處理中 PR
                    boolean selected = false;
                    for(PrEbanVO prEbanVO : ebanList){
                        if( prEbanVO.isSelected() && prEbanVO.getBnfpo()!=null && prEbanVO.getBnfpo().equals(vo.getBnfpo()) ){
                            if( prEbanVO.getMatnr()!=null && prEbanVO.getMatnr().equals(vo.getMatnr()) ){
                                vo.setMenge(prEbanVO.getQuantity());// 變更數量
                                logger.debug("resetRfqItems update "+vo.getBnfpo());
                                newList.add(vo);
                                existedBnfpos.add(vo.getBnfpo());
                                selected = true;
                            }else{
                                logger.error("resetRfqItems error bnfpo = "+prEbanVO.getBnfpo()+":"+prEbanVO.getMatnr()+"!="+vo.getMatnr());
                            }
                            break;
                        }
                    }
                    vo.setLoekz(selected?null:"X");
                    maxEbelp = (maxEbelp < vo.getEbelp())?vo.getEbelp():maxEbelp;
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
                        vo.setMenge(prEbanVO.getQuantity());// 變更數量
                        maxEbelp = maxEbelp + 1;
                        vo.setEbelp(maxEbelp);
                        
                        logger.debug("resetRfqItems new "+vo.getBnfpo());
                        newList.add(vo);
                    }
                }
            }
        }
        
        rfqVO.setEkpoList(newList);
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
            rfqPmList = prDwFacade.findByEbanPmByKey(selVO.getMandt(), selVO.getBanfn(), selVO.getBnfpo());
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
            
            prepareRfqFullData();
            
            // TODO SAVE
            
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
    
    /**
     * 完整儲存資料
     */
    public void prepareRfqFullData(){
        // TODO
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

    public List<PrEbantxHeadVO> getPrEbantxHeadList() {
        return prEbantxHeadList;
    }

    public void setPrEbantxHeadList(List<PrEbantxHeadVO> prEbantxHeadList) {
        this.prEbantxHeadList = prEbantxHeadList;
    }

    public List<PrEbantxItemVO> getPrEbantxItemList() {
        return prEbantxItemList;
    }

    public void setPrEbantxItemList(List<PrEbantxItemVO> prEbantxItemList) {
        this.prEbantxItemList = prEbantxItemList;
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

    public List<PrEbanPmVO> getRfqPmList() {
        return rfqPmList;
    }

    public void setRfqPmList(List<PrEbanPmVO> rfqPmList) {
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

    public List<SelectItem> getPurGroupOps() {
        return purGroupOps;
    }

    public void setPurGroupOps(List<SelectItem> purGroupOps) {
        this.purGroupOps = purGroupOps;
    }
    //</editor-fold>

}
