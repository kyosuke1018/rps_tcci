/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.tender;

import com.tcci.cm.controller.global.FileController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.dw.facade.PrDwFacade;
import com.tcci.et.entity.EtAward;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.enums.AwardStatusEnum;
import com.tcci.et.facade.EtSequenceFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.jco.JCoClientFacade;
import com.tcci.et.facade.rfq.EtAwardFacade;
import com.tcci.et.facade.rfq.EtAwardItemFacade;
import com.tcci.et.facade.rfq.EtQuotationFacade;
import com.tcci.et.facade.rfq.EtQuotationItemFacade;
import com.tcci.et.facade.rfq.EtQuotationPmFacade;
import com.tcci.et.facade.rfq.EtRfqPmFacade;
import com.tcci.et.facade.rfq.RfqCommonFacade;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rfq.AwardItemVO;
import com.tcci.et.model.rfq.AwardVO;
import com.tcci.et.model.rfq.QuotationItemVO;
import com.tcci.et.model.rfq.QuotationPmVO;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.rfq.RfqPmVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "tenderAward")
@ViewScoped
public class TenderAwardController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 38;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB EtTenderFacade tenderFacade;
    @EJB EtQuotationFacade quotationFacade;
    @EJB EtQuotationItemFacade quotationItemFacade;
    @EJB EtQuotationPmFacade quotationPmFacade;
    @EJB EtAwardFacade awardFacade;
    @EJB EtAwardItemFacade awardItemFacade;
    @EJB EtSequenceFacade sequenceFacade;
    
    @EJB EtRfqPmFacade rfqPmFacade;
    @EJB CmCompanyFacade companyFacade;
    @EJB CmFactoryFacade factoryFacade;
    @EJB RfqCommonFacade rfqCommonFacade;
    @EJB PrDwFacade prDwFacade;
    @EJB JCoClientFacade jCoClientFacade;

    @ManagedProperty(value = "#{fileController}")
    private FileController fileController;
    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
    
    private TenderVO tenderVO;
    private RfqVO rfqVO = new RfqVO();

    // for RFQ Items datatable
    private BaseLazyDataModel<RfqEkpoVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<RfqEkpoVO> filterResultList;
    private List<RfqPmVO> rfqPmList;
    private List<QuotationPmVO> quotePmList;
    private List<QuotationPmVO> awardPmList;
    
    private List<QuotationVO> venderQuoList;// 報價廠商與最後報價 ET_QUOTATION.ID
    private List<QuotationItemVO> quoItemList;// 所有相關報價明細
    private Map<Long, QuotationVO> venderQuoMap;// 廠商最後報價 MAP
    
    private QuotationItemVO[][] quoMatrix;//  RFQ項目 x 報價廠商  矩陣

    // 得標單
    private List<AwardVO> awardTimeList;// 得標時間
    private String awardCode;
    private List<VenderAllVO> awardVenderList;// 得標廠商
    private Long venderId;
    
    private AwardVO awardVO;// 得標主檔
    private List<AwardItemVO> awardItemList;// 得標項目
    private BaseLazyDataModel<AwardItemVO> lazyModelAward; // LazyModel for primefaces datatable lazy loading
    private List<AwardItemVO> filterResultListAward;
    
    private String otherInfo; // U: upload, C: cancel,  S: sap po
    
    private String queryResultMsg;
    private String queryRfqPmMsg;
    private String queryQuotePmMsg;
    private String queryAwardPmMsg;
    
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
        
        // for TEST 
        //jCoClientFacade.syncPurGroup("tcc_cn", null, this.getLoginUser());
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
        
        rfqPmList = null;
        quotePmList = null;
        awardPmList = null;
        venderQuoList = null;
        quoItemList = null;
        awardPmList = null;
        
        venderQuoList = null;// 報價廠商與最後報價 ET_QUOTATION.ID
        quoItemList = null;// 所有相關報價明細
        venderQuoMap = null;// 廠商最後報價 MAP
        quoMatrix = null;// RFQ項目 x 報價廠商  矩陣
        // 得標單
        awardTimeList = null; // 得標時間
        awardCode = null;
        awardVenderList = null;// 得標廠商
        venderId = null;
        
        awardVO = null;// 得標主檔
        awardItemList = null;// 得標項目 
        lazyModelAward = null;
        filterResultListAward = null;
        otherInfo = null;
        
        queryResultMsg = null;
        queryRfqPmMsg = null;
        queryQuotePmMsg = null;
        queryAwardPmMsg = null;

        rfqVO.setCompanyId(tenderVO.getCompanyId());
        //onChangeCompany(true);
        //selFactory.setNowFactory(tenderVO.getFactoryId(), false);
        //rfqVO.setFactory(selFactory.getSelFactory());
        logger.info("initByTender CompanyId = "+tenderVO.getCompanyId()+", FactoryId = "+tenderVO.getFactoryId());
        rfqVO = rfqCommonFacade.findRfqByTenderId(tenderVO.getId());
        
        // for DEMO //////////////
        /*
            {id: 1, ebelp:1, txz01:"乙炔减压器 YQE-213", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:120, netwr:120, brtwr:1200, waers: 'RMB'},
            {id: 2, ebelp:2, txz01:"氧气减压器 YQY-12", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:115, netwr:115, brtwr:1150, waers: 'RMB'},
            {id: 3, ebelp:3, txz01:"乙炔管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 4, ebelp:4, txz01:"氧气管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
            {id: 5, ebelp:5, txz01:"油石 200X50X25 420#", menge:2, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:25640, netwr:25640, brtwr:51.28, waers: 'RMB'},
            {id: 6, ebelp:6, txz01:"聚西先胺气管 FESTO:PAN 12x1.75  152702", menge:20, meins:"M", lfdat:"2018-02-28", peinh:1, netpr:45, netwr:45, brtwr:900, waers: 'RMB'},
        */     
        // RFQ Default 
        /*
        rfqVO.setEkko(new RfqEkkoVO());
        rfqVO.getEkko().setTenderId(tenderVO.getId());
        rfqVO.getEkko().setBedat(DateUtils.getToday());// 詢價單日期
        rfqVO.getEkko().setAngdt(DateUtils.addDays(DateUtils.getToday(), 10));// 報價截止日        
        String[]  txz01Ary = new String[]{"乙炔减压器 YQE-213", "乙炔管 φ8mm 30m/RL"
                , "氧气管 φ8mm 30m/RL", "油石 200X50X25 420#"
                , "聚西先胺气管 FESTO:PAN 12x1.75  152702", "预沉室入口膨胀节整修,具体详见附图 图号BT-C19-D110-504标识"};
        List<RfqEkpoVO> ekpoList = new ArrayList<RfqEkpoVO>();
        for(int i=0; i<20; i++){
            RfqEkpoVO vo = new RfqEkpoVO();
            vo.setEbelp(i+1L);
            vo.setMatnrUI("1000000"+Integer.toString(i));
            vo.setTxz01(txz01Ary[i % txz01Ary.length]);
            vo.setMenge(BigDecimal.valueOf(10));
            vo.setPeinh(BigDecimal.valueOf(1));
            
            if( 6L == vo.getEbelp() ){
                vo.setPstyp("9");
                vo.setTenderId(10L);
                vo.setRfqId(6L);
            }
            
            ekpoList.add(vo);
        }
        rfqVO.setEkpoList(ekpoList);
        rfqVO.setHasSet(true);
        */
        //////////////////////

        if( rfqVO.isHasSet() ){
            logger.debug("initByTender getEkpotxList = "+sys.size(rfqVO.getEkpoList()));
            initQuoteMatrix();// 初始報價矩陣 for UI
            renderQuoteMatrix();// 顯示
            
            getAwardInfo(); // 已決標資訊
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
     * 初始報價矩陣 for UI
     */
    public void initQuoteMatrix(){
        logger.debug("initQuoteMatrix ...");
        
        genVenderQuoList();// 報價廠商 與 最後報價 ET_QUOTATION.ID
        genVenderQuoMap();// 產出 - 廠商與報價明細資訊 MAP
        genQuotationMatrix();// 產出 - 詢價項目與廠商報價 陣列
    }
    
    /**
     * 報價矩陣顯示
     */
    public void renderQuoteMatrix(){
        logger.debug("renderQuoteMatrix ...");
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
        filterResultList = null; // filterValue 初始化
        lazyModel = new BaseLazyDataModel<RfqEkpoVO>(rfqVO.getEkpoList());
    }
    
    /**
     *  報價廠商 與 最後報價 ET_QUOTATION.ID
     */
    public void genVenderQuoList(){
        logger.debug("getVenderQuoList ...");
        venderQuoList = quotationFacade.findLastByRfq(tenderVO.getId(), rfqVO.getRfqId());
        quoItemList = quotationItemFacade.findByQuoteList(venderQuoList);
        logger.debug("venderQuoList = "+sys.size(venderQuoList)+", quoItemList = "+sys.size(quoItemList));
        
        // for DEMO
        /*
        venderQuoList  = new ArrayList<QuotationVO>();
        quoItemList = new ArrayList<QuotationItemVO>();// 所有相關報價明細
        
        String[]  venderAry = new String[]{
            "南宁市佳信安防工程有限公司",
            "梧州市闽桓建筑工程有限公司",
            "福建源榕交通工程有限公司",
            "广西第一地质工程公司",
            "东莞市华东电机维修有限公司",
            "广西南宁市传冠电气设备有限公司"
        };

        for(int i=0; i<venderAry.length; i++){
            QuotationVO vo = new QuotationVO();
            vo.setVenderId(Integer.toUnsignedLong(i));
            vo.setVenderName(venderAry[i]);
            vo.setId(0L);// 最後報價ID
            vo.setTimes(3);
            
            venderQuoList.add(vo);
        }
        */
    }
    
    /**
     *   產出 - 廠商與報價明細資訊 MAP
     */
    public void genVenderQuoMap(){
        logger.debug("genVenderQuoMap ...");
        venderQuoMap = new HashMap<Long, QuotationVO>();
        
        if( sys.isEmpty(venderQuoList) || quoItemList==null ){
            logger.error("genVenderQuoMap == null");
            return;
        }
        
        // quoList
        for(QuotationVO vo : venderQuoList){
            Long id = vo.getVenderId();
            List<QuotationItemVO> items = new ArrayList<QuotationItemVO>();
            for(QuotationItemVO itemVO : quoItemList){
                if( itemVO.getVenderId().equals(id) ){// 此廠商報價
                    items.add(itemVO);
                }
            }
            
            vo.setItemList(items);
            venderQuoMap.put(id, vo);
        }
    }
    
    /**
     *  產出 - 詢價項目與廠商報價 陣列
     */
    public void genQuotationMatrix(){
        logger.debug("genQuotationMatrix ...");
        logger.debug("genQuotationMatrix sys.isEmpty(venderQuoList) = "+sys.isEmpty(venderQuoList));
        logger.debug("genQuotationMatrix rfqVO = "+rfqVO);
        logger.debug("genQuotationMatrix sys.isEmpty(rfqVO.getEkpoList()) = "+sys.isEmpty(rfqVO.getEkpoList()));

        if( sys.isEmpty(venderQuoList) || rfqVO==null || sys.isEmpty(rfqVO.getEkpoList()) ){
            logger.error("genQuotationMatrix == null");
            quoMatrix = null;
            return;
        }
        int row = rfqVO.getEkpoList().size();
        int col = venderQuoList.size();
        logger.debug("genQuotationMatrix row = "+row+", col = "+col);
        
        quoMatrix = new QuotationItemVO[row][col];
        
        for(int r=0; r<row; r++){// rfq items
            RfqEkpoVO ekpo = rfqVO.getEkpoList().get(r);
            Long ebelp = ekpo.getEbelp();
            logger.debug("genQuotationMatrix ebelp = "+ebelp);
            for(int c=0; c<col; c++){// venders
                QuotationItemVO item = new QuotationItemVO();
                
                Long id = venderQuoList.get(c).getVenderId();
                QuotationVO quoVO = venderQuoMap.get(id);// 此廠商最後報價資訊
                
                if( quoVO!=null && !sys.isEmpty(quoVO.getItemList()) ){
                    for(QuotationItemVO qItem : quoVO.getItemList()){
                        if( ebelp.equals(qItem.getEbelp()) ){// 次項目報價
                            ExtBeanUtils.copyProperties(item, qItem);
                            item.setHasQuote(true);// 有報價
                        }
                    }
                }
                
                // for DEMO //////////////
                /*
                int n = NumberUtils.getRandomNumberInRange(1, 100);
                if( n>30 ){
                    item.setHasQuote(true);
                    item.setMenge(ekpo.getMenge());
                    item.setNetpr(BigDecimal.valueOf(n*100L));
                    if( ekpo.isSrvItem() ){
                        logger.debug("genQuotationMatrix srv item "+r+":"+c);
                        // selVO.getRfqId()+" : "+selVO.getEbelp()+" : "+selVO.getQuoteId()+" : "+selVO.getEbelp()
                        item.setTxz01("预沉室入口膨胀节整修,具体详见附图 图号BT-C19-D110-504标识");
                        item.setPstyp("9");
                        item.setTenderId(10L);
                        item.setRfqId(6L);
                        item.setQuoteId(6L);
                        item.setEbelp(4L);
                    }
                }
                */
                /////////////////////
                 
                quoMatrix[r][c] = item;
            }
        }
    }
    
    /**
     * 單項得標
     * @param row
     * @param col 
     */
    public void doSetAward(int row, int col){
        try{
            logger.debug("doSetAward row="+row+", col="+col+", isWinning="+quoMatrix[row][col].isWinning());
            if( quoMatrix[row][col].isWinning() ){
                if( quoMatrix[row][col].getWinningQuantity()==null ){
                    quoMatrix[row][col].setWinningQuantity(getRemaining(row, col));// 預設 = 剩餘數量
                    logger.debug("doSetAward WinningQuantity = "+quoMatrix[row][col].getWinningQuantity());
                }
            }
            
            if( BigDecimal.ZERO.equals(quoMatrix[row][col].getWinningQuantity()) ){
                JsfUtils.addWarningMessage("注意：已無剩餘量!請調整得標數量。");
            }
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewAwardSrvItems", e, false);
        }
    }
    
    /**
     * 剩餘數量
     * @param row 
     * @param ignoreCol 
     * @return  
     */
    public BigDecimal getRemaining(int row, int ignoreCol){
        BigDecimal ori = rfqVO.getEkpoList().get(row).getMenge();
        
        for(int col=0; col<venderQuoList.size(); col++){
            if( col != ignoreCol ){
                BigDecimal tmp = quoMatrix[row][col].getWinningQuantity();
                tmp = (tmp==null)?BigDecimal.ZERO:tmp;
                ori = ori.subtract(tmp);
            }
        }
        return ori;
    }
    
     /**
     * 檢視 RFQ 服務類明細
     * @param selVO 
     */
    public void onViewRfqSrvItems(RfqEkpoVO selVO){
        logger.debug("onViewRfqSrvItems ... "+selVO.getRfqId()+" : "+selVO.getEbelp());
        try{
            queryRfqPmMsg = "["+selVO.getEbelp()+"]"+selVO.getTxz01()+"：";
            rfqPmList = rfqPmFacade.findByEbelp(selVO.getTenderId(), selVO.getRfqId(), selVO.getEbelp());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewRfqSrvItems", e, false);
        }
    }
    
     /**
     * 檢視 報價 服務類明細
     * @param selVO 
     */
    public void onViewQuoteSrvItems(QuotationItemVO selVO){
        logger.debug("onViewQuoteSrvItems ... "+selVO.getRfqId()+" : "+selVO.getEbelp()+" : "+selVO.getQuoteId()+" : "+selVO.getEbelp());
        try{
            queryQuotePmMsg = "["+selVO.getEbelp()+"]"+selVO.getTxz01()+"：";
            quotePmList = quotationPmFacade.findByQuoteItem(selVO.getTenderId(), selVO.getRfqId(), selVO.getQuoteId(), selVO.getEbelp());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewQuoteSrvItems", e, false);
        }
    }
    
    /**
     * 關閉 RFQ 服務類明細
     */
    public void onCloseRfqPm(){
        queryRfqPmMsg = null;
        rfqPmList = null;
    }
    
     /**
     * 關閉 報價 服務類明細
     */
    public void onCloseQuotePm(){
        queryQuotePmMsg = null;
        quotePmList = null;
    }
    
    /////////////////// 以下為已得標部分的處理 ////////////////////////    
    /**
     * 變更得標時間
     */
    public void onChangeAwardTime(){
        logger.debug("onChangeAwardTime ... "+this.awardCode);
        try{
            // 得標廠商
            getAwardVenders();
            // 得標內容
            renderAwardItems();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeAwardTime", e, false);
        }
    }
    
    /**
     * 變更得標廠商
     */
    public void onChangeAwardVender(){
        logger.debug("onChangeAwardVender ... "+this.venderId);
        try{
            // 得標時間
            getAwardTimes();
            // 得標內容
            renderAwardItems();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onChangeAwardVender", e, false);
        }
    }

    /**
     * 已決標資訊
     */
    public void getAwardInfo(){
        // 得標時間
        getAwardTimes();
        // 得標廠商
        getAwardVenders();
    }    
    
    public void getAwardTimes(){
       // 得標時間
        awardTimeList = awardFacade.findAwardTimeList(rfqVO.getTenderId(), rfqVO.getRfqId(), venderId);
        // for DEMO
        /*
        awardTimeList = new ArrayList<AwardVO>();
        AwardVO awardVO = new AwardVO();
        awardVO.setAwardTime(DateUtils.addDate(new Date(), -5));
        awardVO.setCode("W2C190000002");
        awardTimeList.add(awardVO);
        awardVO = new AwardVO();
        awardVO.setAwardTime(DateUtils.addDate(new Date(), -7));
        awardVO.setCode("W2C190000001");
        awardTimeList.add(awardVO);
        */
    }
    
    public void getAwardVenders(){
        // 得標廠商
        awardVenderList = awardFacade.findAwardVenderList(rfqVO.getTenderId(), rfqVO.getRfqId(), awardCode);
        // for DEMO
        /*
        awardVenderList = new ArrayList<VenderAllVO>();
        VenderAllVO venderAllVO = new VenderAllVO();
        venderAllVO.setId(3L);
        venderAllVO.setLifnrUi("2006783");
        venderAllVO.setName("江苏茅山建设有限公司");
        awardVenderList.add(venderAllVO);
        venderAllVO = new VenderAllVO();
        venderAllVO.setId(2L);
        venderAllVO.setLifnrUi("2014809");
        venderAllVO.setName("安徽合力股份有限公司");
        awardVenderList.add(venderAllVO);
        venderAllVO = new VenderAllVO();
        venderAllVO.setId(1L);
        venderAllVO.setLifnrUi("2015004");
        venderAllVO.setName("沈阳峰玉祥商贸有限公司");
        awardVenderList.add(venderAllVO);
        */
    }
    
    /**
     * 得標項目列表
     */
    public void renderAwardItems(){
        logger.debug("onChangeAwardVender ... venderId="+this.venderId+", awardCode="+this.awardCode);
        
        awardVO = null;
        awardItemList = null;
        filterResultListAward = null; // filterValue 初始化
        lazyModelAward = null;
        
        if( venderId!=null && awardCode!=null ){
            awardVO = awardFacade.findByRfqAwardVender(tenderVO.getId(), rfqVO.getRfqId(), awardCode, venderId);
            awardItemList = awardItemFacade.findByRfq(tenderVO.getId(), rfqVO.getRfqId(), awardCode, null, venderId);
            
            lazyModelAward = new BaseLazyDataModel<AwardItemVO>(awardItemList);
            
            if( awardVO==null || sys.isEmpty(awardItemList) ){
                JsfUtils.addErrorMessage("查無相關得標資訊!");
            }
        }
        
        this.otherInfo = null;// 預設隱藏
        // 附件
        fileController.init();
    }
    
    /**
     * 顯示已得標資訊
     * @return 
     */
    public boolean showAwardItems(){
        return lazyModelAward!=null && lazyModelAward.getRowCount()>0;
    }
    
     /**
     * 檢視服務類得標明細
     * @param vo 
     */
    public void onViewAwardSrvItems(AwardItemVO vo){
        logger.debug("onViewAwardSrvItems ... "+vo.getRfqId()+" : "+vo.getEbelp()+" : "+vo.getQuoteId()+" : "+vo.getEbelp());
        try{
            queryAwardPmMsg = "["+vo.getEbelp()+"]"+vo.getTxz01()+"：";
            awardPmList = quotationPmFacade.findByQuoteItem(vo.getTenderId(), vo.getRfqId(), vo.getQuoteId(), vo.getEbelp());
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onViewAwardSrvItems", e, false);
        }
    }
    
     /**
     * 關閉 服務類 得標明細
     */
    public void onCloseAwardPm(){
        queryAwardPmMsg = null;
        awardPmList = null;
    }
    
    /**
     *  上傳附件
     */
    public void onUploadFiles(){
        logger.debug("onUploadFiles ...");
        try{
            this.otherInfo = "U";
            // 附件
            awardVO.setDocs(awardFacade.findFiles(awardVO));
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onUploadFiles", e, true);
        }
    }
    
    /**
     * 儲存附件
     */
    public void onSaveAttachments(){
        logger.debug("onSaveAttachments ... ");
        try{
            if( sys.isEmpty(fileController.getFiles()) ){
                JsfUtils.addErrorMessage("無上傳檔案!");
                return;
            }
            // 儲存附件
            saveAttachments(awardVO.getId());
            // 附件
            awardVO.setDocs(awardFacade.findFiles(awardVO));
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSaveAttachments", e, false);
        }
    }
    
    /**
     * 儲存附件
     * @param holderId
     */
    public void saveAttachments(Long holderId) throws Exception{
        logger.debug("saveAttachments ...holderId = "+holderId+", files=" + sys.size(fileController.getFiles()));
        EtAward entity = awardFacade.find(holderId);
        if( entity==null || awardVO==null ){
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
        if( !sys.isEmpty(awardVO.getRemovedDocs()) ){
            for(AttachmentVO vo : awardVO.getRemovedDocs()){
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
            if (awardVO.getDocs() != null) {
                // 未建立關聯直接刪除
                if (attachmentVO.getApplicationdata() == null
                        || attachmentVO.getApplicationdata().getId() == null) {
                    fileController.deleteAttachment(attachmentVO, this.isSimulated());
                } else {
                    // 註記刪除，於儲存時處理。
                    if (awardVO.getRemovedDocs() == null) {
                        awardVO.setRemovedDocs(new ArrayList<AttachmentVO>());
                    }
                    awardVO.getRemovedDocs().add(attachmentVO);
                }
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                awardVO.getDocs().remove(attachmentVO);// for UI
                //logger.debug("deleteFile remove "+attachmentVO.getFileName());
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                
                // 此處直接儲存刪除結果
                saveAttachments(awardVO.getId());
                
                cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), awardVO.getId(),
                        awardVO.toString(), null, true, this.getLoginUser(), this.isSimulated());
            }
            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "deleteFile", e, true);
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), awardVO.getId(),
                    awardVO.toString(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     *  確定決標
     */
    public void onSaveAward(){
        logger.debug("onSaveAward ...");
        try{
            if( rfqVO==null || sys.isEmpty(rfqVO.getEkpoList()) ||  sys.isEmpty(venderQuoList) || quoMatrix==null ){
                logger.error("onSaveAward error empty ...");
                return;
            }
            
            int row = rfqVO.getEkpoList().size();
            int col = venderQuoList.size();
            logger.debug("onSaveAward row = "+row+", col = "+col);
            
            String code = sequenceFacade.genAwardCode(tenderVO.getSapClientEnum(), this.getLoginUser());// 批號
            Date awardTime = new Date();
            logger.debug("onSaveAward code = "+code+", awardTime = "+DateUtils.format(awardTime));
            
            List<AwardVO> awardList = new ArrayList<AwardVO>();
            for(int c=0; c<col; c++){// 依廠商
                QuotationVO quoteVO = venderQuoList.get(c);
                List<AwardItemVO> awardItemList = new ArrayList<AwardItemVO>();
                for(int r=0; r<row; r++){// 依品項
                    QuotationItemVO quoteItemVO = quoMatrix[r][c];
                    if( quoteItemVO.isHasQuote() ){// 有報價
                        if( quoteItemVO.isWinning() ){// 得標
                            AwardItemVO itemVO = new AwardItemVO();
                            //itemVO.setAwardId();
                            itemVO.setDisabled(false);
                            itemVO.setMenge(quoteItemVO.getWinningQuantity());// UI
                            itemVO.setPstyp(quoteItemVO.getPstyp());
                            itemVO.setQuoteId(quoteItemVO.getQuoteId());
                            itemVO.setRfqId(quoteItemVO.getRfqId());
                            itemVO.setTenderId(quoteItemVO.getTenderId());
                            itemVO.setVenderId(quoteItemVO.getVenderId());
                            
                            awardItemList.add(itemVO);
                        }
                    }
                }
                
                if( !sys.isEmpty(awardItemList) ){
                    AwardVO awardVO = new AwardVO();
                    awardVO.setAwardItemList(awardItemList);           
                    awardVO.setAwardTime(awardTime);
                    awardVO.setCode(code);
                    awardVO.setDisabled(false);
                    awardVO.setStatus(AwardStatusEnum.AWARD.getCode());
                    awardVO.setQuoteId(quoteVO.getId());
                    awardVO.setRfqId(quoteVO.getRfqId());
                    awardVO.setTenderId(quoteVO.getTenderId());
                    awardVO.setVenderId(quoteVO.getVenderId());
                    
                    awardList.add(awardVO);
                }
            }
            
            logger.info("onSaveAward awardList = "+sys.size(awardList));
            if( sys.isEmpty(awardList) ){
                JsfUtils.addErrorMessage("未設定任何決標項目!");
                JsfUtils.buildErrorCallback();
                return;
            }
            
            // TODO save to DB
            awardFacade.saveAwardRecords(awardList, this.getLoginUser());
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onSaveAward", e, true);
        }
    }

    /**
     *  取消決標
     */
    public void onCancelAward(){
        logger.debug("onCancelAward ...");
        try{
            this.otherInfo = "C";
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onCancelAward", e, false);
        }
    }
    /**
     *  確定取消決標
     */
    public void doCancelAward(){
        logger.debug("onCancelAward ...");
        try{
            if( awardVO.getMemo()==null || awardVO.getMemo().trim().isEmpty() ){
                JsfUtils.addErrorMessage("請輸入撤銷原因說明!");
                return;
            }
            
            awardVO.setDisabled(true);
            awardFacade.saveVO(awardVO, this.getLoginUser(), false);
            
            // reload UI
            onQueryTender();
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onCancelAward", e, false);
        }
    }
    
    /**
     *  決標通知
     */
    public void onNotifyAward(){
        logger.debug("onNotifyAward ...");
        try{
            
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onNotifyAward", e, true);
        }
    }

    /**
     *  建立 SAP PO
     */
    public void onCreatePo(){
        logger.debug("onCreatePo ...");
        try{
            this.otherInfo = "S";
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onCreatePo", e, true);
        }
    }
    
    /**
     * 取消輸入補充資訊
     */
    public void onCancelOtherInfo(){
        logger.debug("onCancelOtherInfo ...");
        try{
            this.otherInfo = null;
        }catch(Exception e){
            this.processUnknowException(this.getLoginUser(), "onCancelOtherInfo", e, false);
        }
    } 
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public TenderVO getTenderVO() {
        return tenderVO;
    }

    public void setTenderVO(TenderVO tenderVO) {
        this.tenderVO = tenderVO;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }

    public List<AwardVO> getAwardTimeList() {
        return awardTimeList;
    }

    public void setAwardTimeList(List<AwardVO> awardTimeList) {
        this.awardTimeList = awardTimeList;
    }

    public List<VenderAllVO> getAwardVenderList() {
        return awardVenderList;
    }

    public void setAwardVenderList(List<VenderAllVO> awardVenderList) {
        this.awardVenderList = awardVenderList;
    }

    public List<QuotationItemVO> getQuoItemList() {
        return quoItemList;
    }

    public void setQuoItemList(List<QuotationItemVO> quoItemList) {
        this.quoItemList = quoItemList;
    }

    public Map<Long, QuotationVO> getVenderQuoMap() {
        return venderQuoMap;
    }

    public void setVenderQuoMap(Map<Long, QuotationVO> venderQuoMap) {
        this.venderQuoMap = venderQuoMap;
    }

    public QuotationItemVO[][] getQuoMatrix() {
        return quoMatrix;
    }

    public void setQuoMatrix(QuotationItemVO[][] quoMatrix) {
        this.quoMatrix = quoMatrix;
    }

    public List<QuotationVO> getVenderQuoList() {
        return venderQuoList;
    }

    public void setVenderQuoList(List<QuotationVO> venderQuoList) {
        this.venderQuoList = venderQuoList;
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

    public String getQueryRfqPmMsg() {
        return queryRfqPmMsg;
    }

    public void setQueryRfqPmMsg(String queryRfqPmMsg) {
        this.queryRfqPmMsg = queryRfqPmMsg;
    }

    public RfqVO getRfqVO() {
        return rfqVO;
    }

    public void setRfqVO(RfqVO rfqVO) {
        this.rfqVO = rfqVO;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<QuotationPmVO> getAwardPmList() {
        return awardPmList;
    }

    public void setAwardPmList(List<QuotationPmVO> awardPmList) {
        this.awardPmList = awardPmList;
    }

    public List<RfqPmVO> getRfqPmList() {
        return rfqPmList;
    }

    public void setRfqPmList(List<RfqPmVO> rfqPmList) {
        this.rfqPmList = rfqPmList;
    }

    public String getQueryAwardPmMsg() {
        return queryAwardPmMsg;
    }

    public void setQueryAwardPmMsg(String queryAwardPmMsg) {
        this.queryAwardPmMsg = queryAwardPmMsg;
    }

    public List<QuotationPmVO> getQuotePmList() {
        return quotePmList;
    }

    public void setQuotePmList(List<QuotationPmVO> quotePmList) {
        this.quotePmList = quotePmList;
    }

    public String getQueryQuotePmMsg() {
        return queryQuotePmMsg;
    }

    public void setQueryQuotePmMsg(String queryQuotePmMsg) {
        this.queryQuotePmMsg = queryQuotePmMsg;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public List<AwardItemVO> getAwardItemList() {
        return awardItemList;
    }

    public void setAwardItemList(List<AwardItemVO> awardItemList) {
        this.awardItemList = awardItemList;
    }

    public BaseLazyDataModel<AwardItemVO> getLazyModelAward() {
        return lazyModelAward;
    }

    public void setLazyModelAward(BaseLazyDataModel<AwardItemVO> lazyModelAward) {
        this.lazyModelAward = lazyModelAward;
    }

    public List<AwardItemVO> getFilterResultListAward() {
        return filterResultListAward;
    }

    public void setFilterResultListAward(List<AwardItemVO> filterResultListAward) {
        this.filterResultListAward = filterResultListAward;
    }

    public AwardVO getAwardVO() {
        return awardVO;
    }

    public void setAwardVO(AwardVO awardVO) {
        this.awardVO = awardVO;
    }

    public String getQueryResultMsg() {
        return queryResultMsg;
    }

    public void setQueryResultMsg(String queryResultMsg) {
        this.queryResultMsg = queryResultMsg;
    }
    //</editor-fold>
}
