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
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.dw.facade.PrDwFacade;
import com.tcci.et.entity.EtQuotation;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.enums.CurrencyCodeEnum;
import com.tcci.et.enums.QuoteStatusEnum;
import com.tcci.et.enums.TaxTypeEnum;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtVenderAllFacade;
import com.tcci.et.facade.rfq.EtQuotationFacade;
import com.tcci.et.facade.rfq.EtQuotationItemFacade;
import com.tcci.et.facade.rfq.EtQuotationPmFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rfq.QuotationItemVO;
import com.tcci.et.model.rfq.QuotationPmVO;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

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
    
    private TenderVO tenderVO;
    private RfqVO rfqVO = new RfqVO();
    private boolean doNext = false;
    
    private List<VenderAllVO> venderList;// 投標(受邀)廠商
    private VenderAllVO venderVO;// 選取報價廠商
    private List<CurrencyCodeEnum> curList;// 幣別選項
    private List<TaxTypeEnum> taxTypeList;// 稅別
    private List<SelectItem> taxRateOps;// 稅率
    
    private List<QuotationVO> quoteList;
    //private VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
    private QuotationVO quoteVO;
    private Integer nowTimes;
    
    // for  QuotationItem datatable
    private List<QuotationItemVO> quoteItemList;
    private BaseLazyDataModel<QuotationItemVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<QuotationItemVO> filterResultList;
    
    private List<QuotationPmVO> pmList;
    
    private String queryResultMsg;
    private String queryRfqPmMsg;
    
    @PostConstruct
    public void init(){
        logger.debug("init ...");
        
        getInitParams();// query string params
        initByTender(tenderVO);
        
        // for DEMO
        if( tenderVO==null ){
            tenderVO = new TenderVO();
            tenderVO.setCode("GXHL-FC19001");
        }
        
        // TODO
        curList = Arrays.asList(CurrencyCodeEnum.values());
        taxTypeList  = Arrays.asList(TaxTypeEnum.values());
        taxRateOps = new ArrayList<SelectItem>();
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(3L), "3%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(5L), "5%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(6L), "6%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(10L), "10%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(11L), "11%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(16L), "16%"));
        taxRateOps.add(new SelectItem(BigDecimal.valueOf(17L), "17%"));
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
                logger.debug("getInitParams SapClientEnum = "+(tenderVO!=null?tenderVO.getSapClientEnum():null));
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
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+tenderVO.getFactoryId());
        
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
        
        // init 報價
        quoteVO = new QuotationVO();
        nowTimes = null;// 預設顯示最後報價(或新報價)
        initQuote();
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
            initQuote();
            nowTimes = null;// 預設顯示最後報價(或新報價)
            quoteVO.setTimes(nowTimes);// 不指定
            if( quoteVO.getRfqVenderId()==null ){
                return;
            }
            
            getVenderInfo(quoteVO.getRfqVenderId());
            // 此供應商所有報價記錄
            quoteList = quotationFacade.findByRfqVender(tenderVO.getId(), rfqVO.getRfqId(), quoteVO.getRfqVenderId());
            // 預設顯示報價
            getQuotationInfo(tenderVO.getId(), rfqVO.getRfqId(), quoteList, quoteVO);
            
            /*if( quoteVO.getId()==null ){
                logger.debug("onChangeVender set quoteVO default ...");
                quoteVO.setDisabled(false);
                quoteVO.setDiscount(null);// X
                quoteVO.setExRate(null);// X
                quoteVO.setExpiretime(null);// UI
                quoteVO.setInvoice(null);// UI
                //quoteVO.setLast(null);
                quoteVO.setMemberId(null);
                quoteVO.setMemo(null);// UI
                quoteVO.setQuotetime(DateUtils.getToday());// UI
                quoteVO.setRfqId(rfqVO.getEkko().getId());
                //quoteVO.setRfqVenderId(null);// UI
                quoteVO.setStatus(QuoteStatusEnum.TEMP.getCode());// 暫存
                quoteVO.setTaxType(TaxTypeEnum.ZERO.getCode());// UI
                quoteVO.setTaxRate(null);// UI
                quoteVO.setTenderId(tenderVO.getId());
                quoteVO.setCurQuo(rfqVO.getEkko().getWaers());// 預設
                quoteVO.setCurRfq(rfqVO.getEkko().getWaers());// 詢價單幣別

                quoteVO.setNetAmtQuo(null);// UI
                quoteVO.setNetAmtRfq(null);
                quoteVO.setTaxQuo(null);// UI
                quoteVO.setTaxRfq(null);
                quoteVO.setTotalAmtQuo(null);// UI
                quoteVO.setTotalAmtRfq(null);
            }*/

            doNext = sys.isId(quoteVO.getRfqVenderId());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryVender", e, false);
        }
    }

    public void initQuote(){
        logger.debug("initQuote set quoteVO default ...");
        quoteVO.setDisabled(false);
        quoteVO.setDiscount(null);// X
        quoteVO.setExRate(null);// X
        quoteVO.setExpiretime(null);// UI
        quoteVO.setInvoice(null);// UI
        //quoteVO.setLast(null);
        quoteVO.setMemberId(null);
        quoteVO.setMemo(null);// UI
        quoteVO.setQuotetime(DateUtils.getToday());// UI
        quoteVO.setRfqId(rfqVO.getEkko().getId());
        //quoteVO.setRfqVenderId(null);// UI
        quoteVO.setStatus(QuoteStatusEnum.TEMP.getCode());// 暫存
        quoteVO.setTaxType(TaxTypeEnum.ZERO.getCode());// UI
        quoteVO.setTaxRate(null);// UI
        quoteVO.setTenderId(tenderVO.getId());
        quoteVO.setCurQuo(rfqVO.getEkko().getWaers());// 預設
        quoteVO.setCurRfq(rfqVO.getEkko().getWaers());// 詢價單幣別

        quoteVO.setNetAmtQuo(null);// UI
        quoteVO.setNetAmtRfq(null);
        quoteVO.setTaxQuo(null);// UI
        quoteVO.setTaxRfq(null);
        quoteVO.setTotalAmtQuo(null);// UI
        quoteVO.setTotalAmtRfq(null);
        
        // 附件
        fileController.init();
    }
    
    /**
     * 檢視第 ? 次報價
     * @param quoVO 
     */
    public void onSelectQuoteTimes(QuotationVO quoVO){
        logger.debug("onSelectQuoteTimes ... times = "+quoVO.getTimes());
        try{
            initQuote();
            nowTimes = quoVO.getTimes();
            ExtBeanUtils.copyProperties(quoteVO, quoVO);
            getQuotationInfo(tenderVO.getId(), rfqVO.getRfqId(), quoteList, quoteVO);
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onQueryVender", e, false);
        }
    }
    
    /**
     * 供應商資訊
     * @param rfqVenderId 
     */
    public void getVenderInfo(Long rfqVenderId){
        if( venderList!=null ){
            for(VenderAllVO vo : venderList){
                if( vo.getRfqVenderId().equals(rfqVenderId) ){
                    venderVO = new VenderAllVO();
                    ExtBeanUtils.copyProperties(venderVO, vo);
                    return;
                }
            }
        }
    }
    
    /**
     * 報價資訊
     * @param tenderId
     * @param rfqId
     * @param quoteList
     * @param quoteVO 
     */
    public void getQuotationInfo(Long tenderId, Long rfqId, List<QuotationVO> quoteList, QuotationVO quoteVO){
        logger.debug("getQuotationInfo tenderId = "+tenderId+", rfqVenderId = "+quoteVO.getRfqVenderId()+", quoteVO.getTimes() = "+quoteVO.getTimes());
        // ET_QUOTATION
        //quoteList = quotationFacade.findByRfqVender(tenderId, rfqId, quoteVO.getRfqVenderId());
        
        if( quoteVO.getTimes()==null ){// 未明確指定定幾次報價
            //QuotationVO quoVO = quotationFacade.findByRfqVenderLast(tenderId, rfqId, quoteVO.getRfqVenderId());
            QuotationVO quoVO = sys.isEmpty(quoteList)? null:quoteList.get(quoteList.size()-1);// 最後一次報價(不管有無確認)
            if( quoVO!=null ){
                ExtBeanUtils.copyProperties(quoteVO, quoVO);
                logger.debug("getQuotationInfo quoteVO = " + quoVO.getId());
            }
        }
        logger.info("getQuotationInfo quoteVO = " + quoteVO.getId());
        
        // ET_QUOTATION_ITEM
        quoteItemList = quotationItemFacade.findByRfq(tenderId, rfqId, quoteVO.getRfqVenderId(), null, true);
        logger.info("getQuotationInfo quoteItemList = " + sys.size(quoteItemList));
        
        // for DEMO
        /*
            {id: 1, ebelp:1, txz01:"乙炔减压器 YQE-213", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:120, netwr:120, brtwr:1200, waers: 'RMB'},
            {id: 2, ebelp:2, txz01:"氧气减压器 YQY-12", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:115, netwr:115, brtwr:1150, waers: 'RMB'},
            {id: 3, ebelp:3, txz01:"乙炔管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 4, ebelp:4, txz01:"氧气管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 5, ebelp:5, txz01:"油石 200X50X25 420#", menge:2, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:25640, netwr:25640, brtwr:51.28, waers: 'RMB'},
            {id: 6, ebelp:6, txz01:"聚西先胺气管 FESTO:PAN 12x1.75  152702", menge:20, meins:"M", lfdat:"2018-02-28", peinh:1, netpr:45, netwr:45, brtwr:900, waers: 'RMB'},
        *//*
        if( sys.isEmpty(quoteItemList) ){
            String[] txz01Ary = new String[]{"乙炔减压器 YQE-213", "氧气减压器 YQY-12", "乙炔管 φ8mm 30m/RL" , "氧气管 φ8mm 30m/RL", "油石 200X50X25 420#", "聚西先胺气管 FESTO:PAN 12x1.75  152702"};
            quoteItemList = new ArrayList<QuotationItemVO>();
            for(int i=0; i<txz01Ary.length; i++){
                QuotationItemVO vo = new QuotationItemVO();
                vo.setEbelp(i+1L);
                vo.setMatnrUI("1000000"+Integer.toString(i));
                vo.setTxz01(txz01Ary[i]);
                vo.setMenge(BigDecimal.valueOf(10));
                vo.setPeinh(BigDecimal.valueOf(1));
                vo.setMeins("PC");
                vo.setEindt(DateUtils.addDate(DateUtils.getToday(), 10));

                quoteItemList.add(vo);
            }
        }
        */
        //rfqVO.setEkpoList(ekpoList);
        logger.debug("initByTender quoteItemList = "+sys.size(quoteItemList));

        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT_RFQ);
        filterResultList = null; // filterValue 初始化
        lazyModel = new BaseLazyDataModel<QuotationItemVO>(quoteItemList);
        
        // 附件
        quoteVO.setDocs(quotationFacade.findFiles(quoteVO));
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
            //QuotationItemVO editItem = null;
            Integer index = null; 
            if( this.quoteItemList!=null ){
                int idx = 0;
                for(QuotationItemVO vo : quoteItemList){
                    vo.setCurSrvItem(vo.getEbelp().equals(selVO.getEbelp()));
                    if( vo.isCurSrvItem()){
                        index = idx;
                    }
                    idx++;
                }
            }
            
            if( index==null ){
                logger.error("onViewRfqSrvItems error index==null");
                JsfUtils.buildErrorCallback();
                return;
            }

            // 服務類明細
            queryRfqPmMsg = "["+selVO.getEbelp()+"]"+selVO.getTxz01()+"：";
            pmList = new ArrayList<QuotationPmVO>();
            if( quoteItemList.get(index).getPmList()==null ){// 未編輯過
                List<QuotationPmVO> curPmList = quotationPmFacade.findByQuoteItem(selVO.getTenderId(), selVO.getRfqId(), selVO.getQuoteId(), selVO.getEbelp());
                quoteItemList.get(index).setPmList(curPmList);
            }
            pmList.addAll(quoteItemList.get(index).getPmList());
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
            if( this.quoteItemList!=null ){
                for(QuotationItemVO vo : quoteItemList){
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
     * 檢查報價輸入
     * @return 
     */
    public boolean checkQuotation(){
        boolean hasError = false;
        logger.debug("onSaveQuote getQuotetime = "+quoteVO.getQuotetime());
        logger.debug("onSaveQuote getExpiretime = "+quoteVO.getExpiretime());
        logger.debug("onSaveQuote getTaxType = "+quoteVO.getTaxType());
        logger.debug("onSaveQuote getTaxRate = "+quoteVO.getTaxRate());
        logger.debug("onSaveQuote getMemo = "+quoteVO.getMemo());
        logger.debug("onSaveQuote getCurQuo = "+quoteVO.getCurQuo());
        CurrencyCodeEnum cur = CurrencyCodeEnum.getFromCode(quoteVO.getCurQuo());// 報價金額
        
        if( cur==null ){
            JsfUtils.addErrorMessage("未輸入報價幣別!");
            return false;
        }
        
        // 詢價項目 報價檢查
        //BigDecimal totalRfq = BigDecimal.ZERO;// 總金額(詢價單原幣)
        BigDecimal totalQuote = BigDecimal.ZERO;// 總金額(報價幣別)
        BigDecimal netQuote = BigDecimal.ZERO;// 淨總金額(報價幣別)
        for(QuotationItemVO vo : quoteItemList){
            logger.debug("onSaveQuote isSelected = "+vo.isSelected());
            if( vo.isSelected() ){
                if( !GlobalConstant.PSTYP_SERVICE.equals(vo.getPstyp()) ){// 非服務類，計算總額
                    logger.debug("onSaveQuote menge = "+vo.getMenge() + ", peinh = "+vo.getPeinh() + ", netpr = "+vo.getNetpr());
                    vo.setBrtwr(vo.getTotalPrice()!=null?vo.getTotalPrice().setScale(cur.getScale(), cur.getRoundingMode()):null);// 細目總價
                }else{
                    logger.debug("onSaveQuote PmList = "+sys.size(vo.getPmList()));
                }
                logger.debug("onSaveQuote brtwr = "+vo.getBrtwr());
                // 總價
                if( vo.getBrtwr()==null ){
                    hasError = true;
                    JsfUtils.addErrorMessage(MessageFormat.format("項目 {0} 有選取卻無提供報價!", vo.getEbelp()));
                }else{
                    totalQuote = totalQuote.add(vo.getBrtwr());
                }
                // 交貨日期
                if( vo.getEindt()==null ){
                    hasError = true;
                    JsfUtils.addErrorMessage(MessageFormat.format("項目 {0} 有選取卻無提供交貨日期!", vo.getEbelp()));
                }
            }
        }
        
        // 報價總金額
        quoteVO.setTotalAmtQuo(totalQuote);
        if( TaxTypeEnum.ZERO!=quoteVO.getTaxTypeEnum() && quoteVO.getTaxRate()!=null && quoteVO.getTaxRate().compareTo(BigDecimal.ZERO)>0 ){
            netQuote = totalQuote.subtract(totalQuote.multiply(quoteVO.getTaxRate().divide(BigDecimal.valueOf(100))));
            quoteVO.setNetAmtRfq(netQuote);
        }else{
            quoteVO.setNetAmtRfq(totalQuote);
        }

        return !hasError;
    }
    
    /**
     * 暫存報價
     */
    public void onSaveQuote(){
        logger.debug("onSaveQuote ... ");
        try{
            if( !checkQuotation() ){
                JsfUtils.buildErrorCallback();
                return;
            }
            
            quoteVO.setStatus(QuoteStatusEnum.TEMP.getCode());
            //quoteVO.setLast(true);// 只能編輯最新報價
            quotationFacade.saveQuote(rfqVO, quoteVO, quoteItemList, this.getLoginUser(), false);
            // 儲存附件
            saveAttachments(quoteVO.getId());
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSaveQuote", e, true);
        }
    }
    
    /**
     * 確定送出報價
     */
    public void onSendQuote(){
        logger.debug("onSendQuote ... ");
        try{
            if( !checkQuotation() ){
                JsfUtils.buildErrorCallback();
                return;
            }
            
            quoteVO.setStatus(QuoteStatusEnum.CONFIRM.getCode());
            //quoteVO.setLast(true);// 只能編輯最新報價
            quotationFacade.saveQuote(rfqVO, quoteVO, quoteItemList, this.getLoginUser(), false);
            // 儲存附件
            saveAttachments(quoteVO.getId());
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSendQuote", e, true);
        }
    }
    
    /**
     * 輸入下一供應商報價
     */
    public void onNextTender(){
        logger.debug("onNextTender ... ");
        try{
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onNextTender", e, true);
        }
    }
    
    /**
     * 儲存附件
     * @param holderId
     */
    public void saveAttachments(Long holderId) throws Exception{
        logger.debug("saveAttachments ...holderId = "+holderId+", files=" + sys.size(fileController.getFiles()));
        EtQuotation entity = quotationFacade.find(holderId);
        if( entity==null || quoteVO==null ){
            logger.error("saveAttachments error ...holderId = "+holderId+", files=" + sys.size(fileController.getFiles()));
            return;
        }
        
        if( !sys.isEmpty(fileController.getFiles()) ){
            fileController.saveUploadedFiles(entity, false, this.getLoginUser(), this.isSimulated());
        } else {
            // 取得並保留已存在 App Ids 至 existedAppIds，以便後續分辨哪些是本次上傳的
            fileController.findExistedAttachments(entity);
        }
        // 刪除註記刪除檔
        if( !sys.isEmpty(quoteVO.getRemovedDocs()) ){
            for(AttachmentVO vo : quoteVO.getRemovedDocs()){
                fileController.deleteAttachment(vo, this.isSimulated());
            }
        }
    }

    /**
     * 刪除上傳檔
     *
     * @param attachmentVO
     */
    public void deleteFile(AttachmentVO attachmentVO) {
        logger.debug("deleteFile ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_DOC_FILE;
        try {
            if (quoteVO.getDocs() != null) {
                // 未建立關聯直接刪除
                if (attachmentVO.getApplicationdata() == null
                        || attachmentVO.getApplicationdata().getId() == null) {
                    fileController.deleteAttachment(attachmentVO, this.isSimulated());
                } else {
                    // 註記刪除，於儲存時處理。
                    if (quoteVO.getRemovedDocs() == null) {
                        quoteVO.setRemovedDocs(new ArrayList<AttachmentVO>());
                    }
                    quoteVO.getRemovedDocs().add(attachmentVO);
                }
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                quoteVO.getDocs().remove(attachmentVO);// for UI
                //logger.debug("deleteFile remove "+attachmentVO.getFileName());
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                
                cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), quoteVO.getId(),
                        quoteVO.toString(), null, true, this.getLoginUser(), this.isSimulated());
            }
            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "deleteFile", e, true);
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), quoteVO.getId(),
                    quoteVO.toString(), null, false, this.getLoginUser(), this.isSimulated());
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

    public VenderAllVO getVenderVO() {
        return venderVO;
    }

    public void setVenderVO(VenderAllVO venderVO) {
        this.venderVO = venderVO;
    }

    public List<TaxTypeEnum> getTaxTypeList() {
        return taxTypeList;
    }

    public void setTaxTypeList(List<TaxTypeEnum> taxTypeList) {
        this.taxTypeList = taxTypeList;
    }

    public List<SelectItem> getTaxRateOps() {
        return taxRateOps;
    }

    public void setTaxRateOps(List<SelectItem> taxRateOps) {
        this.taxRateOps = taxRateOps;
    }

    public Integer getNowTimes() {
        return nowTimes;
    }

    public void setNowTimes(Integer nowTimes) {
        this.nowTimes = nowTimes;
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

    public List<VenderAllVO> getVenderList() {
        return venderList;
    }

    public void setVenderList(List<VenderAllVO> venderList) {
        this.venderList = venderList;
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

    public List<QuotationVO> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<QuotationVO> quoteList) {
        this.quoteList = quoteList;
    }

    public List<QuotationItemVO> getQuoteItemList() {
        return quoteItemList;
    }

    public void setQuoteItemList(List<QuotationItemVO> quoteItemList) {
        this.quoteItemList = quoteItemList;
    }

    public boolean isDoNext() {
        return doNext;
    }

    public void setDoNext(boolean doNext) {
        this.doNext = doNext;
    }
    //</editor-fold>
}
