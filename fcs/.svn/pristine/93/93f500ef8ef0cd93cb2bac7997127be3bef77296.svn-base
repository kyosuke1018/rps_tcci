/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.reconciling;


import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ListUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.irs.controller.sheetdata.SheetMstVO;
import com.tcci.irs.entity.AccountNode;
import com.tcci.irs.entity.reconciling.IrsSheetdataReconcilingD;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import com.tcci.irs.enums.SheetTypeEnum;
import com.tcci.irs.facade.IrsAccountNodeFacade;
import com.tcci.irs.facade.reconciling.IrsSheetdataReconcilingDFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "queryReconcilingController")
@ViewScoped
public class QueryReconcilingController extends BaseController {
    //<editor-fold defaultstate="collapsed" desc="Injects">
   
    @EJB
    protected IrsSheetdataReconcilingDFacade irsSheetdataReconcilingDFacade; 
    @EJB
    protected IrsSheetdataMFacade irsSheetdataMFacade; 
    @EJB
    protected FcCompanyFacade fcCompanyFacade; 
    @EJB
    protected IrsCompanyCloseFacade irsCompanyCloseFacade; 
    @EJB
    protected IrsAccountNodeFacade irsAccountNodeFacade;
    @EJB
    protected FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    //</editor-fold>    
    

    private IrsSheetdataReconcilingD condVO = null;

    //editMaster
    private IrsSheetdataM editMaster;
//    private BaseLazyDataModel<IrsSheetdataM> masterlazyModel; // LazyModel for primefaces datatable lazy loading
//    private List<IrsSheetdataM> masterfilterResultList; // datatable filter 後的結果
    private List<SheetMstVO> mstVOList;
    private FcCurrency announceCurr;//公告主體幣別
    private List<FcMonthlyExchangeRate> exchangeRateList;
    private boolean parentComp = false;//是否為母公司(ex 1000、8000) 影響UI欄位 調節作業
    private FcCompany individualCompany;
    String sheetdatamId_s;
    
    //editDetail
//    private BaseLazyDataModel<IrsSheetdataReconcilingD> lazyModel; // LazyModel for primefaces datatable lazy loading
//    private List<IrsSheetdataReconcilingD> filterResultList; // datatable filter 後的結果
    private List<IrsSheetdataReconcilingD> dtlVOList;
    private IrsSheetdataReconcilingD editDetail;
    //
    private List<SelectItem> companyOptions;
    private List<SelectItem> accountingOptions;
    private Long reconcilingAccNodeId;
    
    private boolean editLock;
    
    
    @PostConstruct
    @Override
    protected void init() {
        String controllerClass = getClass().getSimpleName();
        String controllerInstance = controllerClass.substring(0,1).toLowerCase()+controllerClass.substring(1);
        setPageTitle(rb.getString(controllerInstance+".pageTitle"));
        //
        condVO = new IrsSheetdataReconcilingD();
        //
        if (!checkParameter()) {
            return;
        }
        //
        loadData();
    }

    @Override
    protected boolean checkParameter() {
        sheetdatamId_s = JsfUtil.getRequestParameter("sheetdatamId");
        if(StringUtils.isNotBlank(sheetdatamId_s)){
            Long sheetdatamId = Long.valueOf(sheetdatamId_s);
            IrsSheetdataM entityMaster = irsSheetdataMFacade.find(sheetdatamId);
            //
            String individualCompanyId_s = JsfUtil.getRequestParameter("individualCompanyId");
            Long individualCompanyId = Long.valueOf(individualCompanyId_s);
            individualCompany = fcCompanyFacade.find(individualCompanyId);
            entityMaster.setIndividualCompany(individualCompany);
            // 
            condVO.setSheetdatam(entityMaster);
            this.setEditMaster(entityMaster);
            
            //編輯鎖定狀態 資料年月非開帳年月 或 被鎖定 ==>不可編輯
            String dataYM = entityMaster.getYMString();
            
            String compCode = individualCompany.getCode();
            parentComp = (GlobalConstant.PARENT_COMP_1000.equals(compCode) || GlobalConstant.PARENT_COMP_8000.equals(compCode));//個體公司是否為母公司
            //公告主體幣別
            announceCurr = individualCompany.getGroup().getCurrency();
            if(announceCurr!=null){
                exchangeRateList = fcMonthlyExchangeRateFacade.findByYMAndToCurrency(dataYM, announceCurr);
            }
//            IrsCompanyClose irsCompanyClose = irsCompanyCloseFacade.findByGroup(individualCompany.getGroup());
//            IrsCompanyClose irsCompanyClose = irsCompanyCloseFacade.findByGroup(CompanyGroupEnum.getFromCode(individualCompany.getGroup().getCode()));
            IrsCompanyClose irsCompanyClose = irsCompanyCloseFacade.findByGroup(null);
            
            //20160524 第三階段各廠陸續上線 調節表編輯鎖定功能 暫時取消(取消開關帳限制)
            //之後要改回來
//            if(!dataYM.equals(irsCompanyClose.getYearMonth()) || irsCompanyClose.isEditLock() ){
//                editLock = true;
//            }else{
                editLock = false;
//            }
        }else{
            editLock = true;
            return !valid;
        }

        return super.checkParameter(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    @Override
    protected boolean loadData() {
        //master
        mstVOList = new ArrayList();
        //設定VO
        SheetMstVO mstVO = new SheetMstVO();
        mstVO.setMaster(editMaster);
        mstVO.setReAnnounceAmount(this.getReAnnounceAmount(editMaster));
        mstVO.setPaAnnounceAmount(this.getPaAnnounceAmount(editMaster));
        mstVOList.add(mstVO);
//        masterlazyModel = new FilterLazyDataModel<>(masterVOList);
        //accountingOptions
//        accountingOptions = new ArrayList();
//        for (AccountTypeEnum accountTypetEnum : AccountTypeEnum.values()) {
//            String value = accountTypetEnum.getCode();
//            String label = editMaster.getAccountName(value);
//            accountingOptions.add(new SelectItem(value, label));
//        }
        //20151224 收支方改為公司選項
        //companyOptions 
        companyOptions = new ArrayList();
        for (AccountTypeEnum accountTypeEnum : AccountTypeEnum.values()) {
            String value = accountTypeEnum.getCode();
            String label = "";
            if(AccountTypeEnum.RE.equals(accountTypeEnum)){
                label = editMaster.getReCompanyDisPlay().toString();
            }else if(AccountTypeEnum.PA.equals(accountTypeEnum)){
                label = editMaster.getPaCompanyDisPlay().toString();
            }
            companyOptions.add(new SelectItem(value, label));
        }
        
        //setaccounting
        
        //detail
        JsfUtil.resetDataTable("qryForm:dataList");// 移除 datatable 目前排序、filter 效果

//        List<IrsSheetdataReconcilingD> list = Collections.list(Collections.enumeration(editMaster.getIrsSheetdataReconcilingDCollection()));
//        lazyModel = new FilterLazyDataModel<>(list);
//        List<IrsSheetdataReconcilingD> mstVOList = irsSheetdataReconcilingDFacade.find(condVO);
//        lazyModel = new FilterLazyDataModel<>(mstVOList);
        dtlVOList = irsSheetdataReconcilingDFacade.find(condVO);
        return super.loadData();
    }  
    
    public void removeMaster(IrsSheetdataReconcilingD dtlVO) {
        irsSheetdataReconcilingDFacade.remove(dtlVO);
//        long id_this = dtlVO.getId();
//        Collection<IrsSheetdataReconcilingD> irsSheetdataReconcilingDCollection = editMaster.getIrsSheetdataReconcilingDCollection();
//        Iterator<IrsSheetdataReconcilingD> collectionIterator =  irsSheetdataReconcilingDCollection.iterator();
//        while (collectionIterator.hasNext()) {
//            IrsSheetdataReconcilingD currentElement = collectionIterator.next();
//            long id_loop = currentElement.getId();
//            if(id_this == id_loop){
//                collectionIterator.remove();
//            }
//        }

        this.reloadData();
    }    
    
//    public BigDecimal getDiff() {
//        //RE+PA+reconcilingAmount_sum
//        BigDecimal reAmountOrig = editMaster.getReAmountOrig();
//        BigDecimal paAmountOrig = editMaster.getPaAmountOrig();
//        BigDecimal reconcilingAmount_sum = getReconcilingAmount();
//        return reAmountOrig.add(paAmountOrig).add(reconcilingAmount_sum);
//    }    
//    public BigDecimal getReconcilingAmount() {
//        //reconcilingAmount_sum=Reconciling.RE+Reconciling.PA
//        BigDecimal result = new BigDecimal("0");
////        List<IrsSheetdataReconcilingD> mstVOList = (List<IrsSheetdataReconcilingD>)lazyModel.getWrappedData();
////        for (IrsSheetdataReconcilingD entity : mstVOList) {
//        for (IrsSheetdataReconcilingD entity : dtlVOList) {
//            String accountType = entity.getAccountType();
//            BigDecimal amountAdjustments = entity.getAmountAdjustments();
//            if(null != amountAdjustments){
//                if("RE".equalsIgnoreCase(accountType)){
//                    result = result.add(amountAdjustments);
//                }else{//PA
//                    result = result.add(amountAdjustments);
//                }
//                
//            }
//
//        }
//        return result;
//    }
    
    public void initDtl1(IrsSheetdataReconcilingD detail) {
        if(null != detail){
            this.editDetail = detail;
            if(this.editDetail.getAccountNode() != null){
                this.reconcilingAccNodeId = this.editDetail.getAccountNode().getId();
            }
            logger.debug("initDtl1 RemarkOnly:"+editDetail.isRemarkOnly());
        }else{
            this.editDetail = new IrsSheetdataReconcilingD();
//            SelectItem selectItem = ListUtils.getFirstElement(accountingOptions);
            SelectItem selectItem = ListUtils.getFirstElement(companyOptions);
            String value = (String)selectItem.getValue();
            this.editDetail.setAccountType(value);
            this.reconcilingAccNodeId = null;
            this.editDetail.setRemarkOnly(false);
        }
        
        this.changeAccountType();
    }
    
    @Override
    protected boolean check4SaveDtl1() throws Exception {
        isValid = valid;
//        String value1 = editDetail.getYearMont;
//        //value1格式檢查
//        if(StringUtils.isNotBlank(value1)){
//            //init
//            String format = "yyyyMM";
//            String columnNmae1 = "已關帳年月" ;
//            
//            String checkvalue1 = DateUtils.checkDate(format, value1, columnNmae1);
//            if(StringUtils.isNotBlank(checkvalue1)){
//                JsfUtil.addErrorMessage(checkvalue1);
//                isValid = !valid;
//            }
//        }
        
        if(StringUtils.isBlank(this.editDetail.getRemark())){
            JsfUtil.addErrorMessage("[備註]必填");
            isValid = !valid;
        }

        if(!isValid){
//            loadData();
        }
        return isValid;
    }
    
    public void saveDataDtl() {
        logger.debug("saveDataDtl:");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if(!this.check4SaveDtl1()){
                return ;
            }
            
            TcUser user = userSession.getTcUser();
            editDetail.setCreator(user);
            editDetail.setCreatetimestamp(new Date());
            Long id =editDetail.getId();
            if(null == id){
                editDetail.setSheetdatam(condVO.getSheetdatam());
            }
            //20151228 金額 科目 非必填
            logger.debug("saveDataDtl RemarkOnly:"+editDetail.isRemarkOnly());
            if(this.editDetail.isRemarkOnly()){
                this.editDetail.setAmountAdjustments(BigDecimal.ZERO);
                this.editDetail.setAccountNode(null);
            }else{
                if(reconcilingAccNodeId != null){
                    AccountNode accountNode = irsAccountNodeFacade.find(reconcilingAccNodeId);
                    editDetail.setAccountNode(accountNode);
                }
                if(editDetail.getAmountAdjustments() == null){
                    editDetail.setAmountAdjustments(BigDecimal.ZERO);
                }
            }
            
            irsSheetdataReconcilingDFacade.edit(editDetail);
            //
            editMaster.setCreator(user);
            editMaster.setCreatetimestamp(new Date());
            irsSheetdataMFacade.edit(editMaster);
            
            JsfUtil.addSuccessMessage("儲存成功!");
            context.addCallbackParam("saved", true);
            
            this.reloadData();
        }catch (Exception ex) {
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("儲存失敗!");
            context.addCallbackParam("saved", false);
        }        
    }
    
//    @Override
//    protected boolean saveDataDtl1() throws Exception {
//        TcUser user = userSession.getTcUser();
//        editDetail.setCreator(user);
//        editDetail.setCreatetimestamp(new Date());
//        Long id =editDetail.getId();
//        if(null == id){
//            editDetail.setSheetdatam(condVO.getSheetdatam());
//        }
//        //20151228 金額 科目 非必填
//        if(reconcilingAccNodeId != null){
//            AccountNode accountNode = irsAccountNodeFacade.find(reconcilingAccNodeId);
//            editDetail.setAccountNode(accountNode);
//        }
//        if(editDetail.getAmountAdjustments() == null){
//            editDetail.setAmountAdjustments(BigDecimal.ZERO);
//        }
//        irsSheetdataReconcilingDFacade.edit(editDetail);
//        //
//        editMaster.setCreator(user);
//        editMaster.setCreatetimestamp(new Date());
//        irsSheetdataMFacade.edit(editMaster);
//        
//        String msg = "儲存成功!";
//        JsfUtil.addSuccessMessage(msg);
//        
//        loadData();
//        return valid;
//    }   
    
    public void changeAccountType() {
        logger.debug("changeAccountType:" + this.editDetail.getAccountType());
        String groupCode = ""; 
        AccountTypeEnum accountType = AccountTypeEnum.getFromCode(this.editDetail.getAccountType());
        if(AccountTypeEnum.RE.equals(accountType)){
            groupCode = editMaster.getReCompany().getGroup().getCode();
        }else if(AccountTypeEnum.PA.equals(accountType)){
            groupCode = editMaster.getPaCompany().getGroup().getCode();
        }
        CompanyGroupEnum group = CompanyGroupEnum.getFromCode(groupCode);
//        List<AccountNode> anList = irsAccountNodeFacade.findByGroupAndRole(group, accountType);
        List<AccountNode> anList = irsAccountNodeFacade.findByGroup(group);//20160309 開放所有會科類別 RE及PA方
        //20151224 開放所有會科類別
        //accountingOptions
        accountingOptions = new ArrayList();
        for (AccountNode accNode : anList) {
//            String value = accountTypetEnum.getCode();
            accountingOptions.add(new SelectItem(accNode.getId(), accNode.toString()));
        }
    }
    
    public void changeARemarkOnly(){
        logger.debug("changeARemarkOnly RemarkOnly:"+editDetail.isRemarkOnly());
        if(this.editDetail.isRemarkOnly()){
            this.editDetail.setAmountAdjustments(BigDecimal.ZERO);
            this.editDetail.setAccountNode(null);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    private void reloadData() {
        logger.debug("reloadData:");
        //master
        editMaster = irsSheetdataMFacade.find(editMaster.getId());
        editMaster.setIndividualCompany(individualCompany);
        List<IrsSheetdataReconcilingD> detailList = irsSheetdataReconcilingDFacade.findBySheetdataMId(editMaster.getId());
        editMaster.setIrsSheetdataReconcilingDCollection(detailList);
        
        mstVOList = new ArrayList();
        //設定VO
        SheetMstVO mstVO = new SheetMstVO();
        mstVO.setMaster(editMaster);
        mstVO.setReAnnounceAmount(this.getReAnnounceAmount(editMaster));
        mstVO.setPaAnnounceAmount(this.getPaAnnounceAmount(editMaster));
        mstVOList.add(mstVO);
        
        //detail
        JsfUtil.resetDataTable("qryForm:dataList");// 移除 datatable 目前排序、filter 效果
        dtlVOList = detailList;
    }
    
    //取得公告幣別金額
    private BigDecimal getReAnnounceAmount(IrsSheetdataM m){
//        logger.debug("getReAnnounceAmount! reAgreedAmount:"+m.getReAgreedAmountDisPlay());
        //若公司幣別 == 公告幣別
        if(m.getReCompanyDisPlay().getCurrency().getCode().equals(announceCurr.getCode())){
            return m.getReAgreedAmountDisPlay();
        }
        SheetTypeEnum sheetType = SheetTypeEnum.getByValue(m.getSheetType());
        if (CollectionUtils.isNotEmpty(exchangeRateList)) {
            for(FcMonthlyExchangeRate monthlyExchangeRate : exchangeRateList){
                //以公司幣別金額轉換為公告幣別金額
                if(m.getReCompanyDisPlay().getCurrency().getCode().equals(monthlyExchangeRate.getCurrency().getCode())){
                    BigDecimal rate;
                    if(SheetTypeEnum.BS.equals(sheetType)){
                        //ASET固定資產 年度累計值 使用平均匯率 例外處理
                        if("ASET".equals(m.getReAccountDisPlay().getCode())){
                            rate = monthlyExchangeRate.getAvgRate();
                        }else{
                            rate = monthlyExchangeRate.getRate();//期末匯率
                        }
                    }else{
                        rate = monthlyExchangeRate.getAvgRate();
                    }
                    return rate!=null?m.getReAgreedAmountDisPlay().multiply(rate).setScale(0, RoundingMode.HALF_UP):null;
                }
            }
        }
        return null;
    }
    private BigDecimal getPaAnnounceAmount(IrsSheetdataM m){
//        logger.debug("getPaAnnounceAmount! paAgreedAmount:"+m.getPaAgreedAmountDisPlay());
        //若公司幣別 == 公告幣別
        if(m.getPaCompanyDisPlay().getCurrency().getCode().equals(announceCurr.getCode())){
            return m.getPaAgreedAmountDisPlay();
        }
        SheetTypeEnum sheetType = SheetTypeEnum.getByValue(m.getSheetType());
        if (CollectionUtils.isNotEmpty(exchangeRateList)) {
            for(FcMonthlyExchangeRate monthlyExchangeRate : exchangeRateList){
                //以公司幣別金額轉換為公告幣別金額
                if(m.getPaCompanyDisPlay().getCurrency().getCode().equals(monthlyExchangeRate.getCurrency().getCode())){
                    BigDecimal rate;
                    if(SheetTypeEnum.BS.equals(sheetType)){
                        //ASET固定資產 年度累計值 使用平均匯率 例外處理
                        if("ASET".equals(m.getPaAccountDisPlay().getCode())){
                            rate = monthlyExchangeRate.getAvgRate();
                        }else{
                            rate = monthlyExchangeRate.getRate();//期末匯率
                        }
                    }else{
                        rate = monthlyExchangeRate.getAvgRate();
                    }
                    return rate!=null?m.getPaAgreedAmountDisPlay().multiply(rate).setScale(0, RoundingMode.HALF_UP):null;
                }
            }
        }
        return null;
    }
    //</editor-fold>       
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    
    public List<IrsSheetdataReconcilingD> getDtlVOList() {
        return dtlVOList;
    }

//    public BaseLazyDataModel<IrsSheetdataReconcilingD> getLazyModel() {
//        return lazyModel;
//    }
//
//    public void setLazyModel(BaseLazyDataModel<IrsSheetdataReconcilingD> lazyModel) {
//        this.lazyModel = lazyModel;
//    }
//
//    public List<IrsSheetdataReconcilingD> getFilterResultList() {
//        return filterResultList;
//    }
//
//    public void setFilterResultList(List<IrsSheetdataReconcilingD> filterResultList) {
//        this.filterResultList = filterResultList;
//    }
    public IrsSheetdataReconcilingD getCondVO() {
        return condVO;
    }

    public void setCondVO(IrsSheetdataReconcilingD condVO) {
        this.condVO = condVO;
    }

    public List<SelectItem> getCompanyOptions() {
        return companyOptions;
    }
    
    public List<SelectItem> getAccountingOptions() {
        return accountingOptions;
    }
    public void setAccountingList(List<SelectItem> accountingOptions) {
        this.accountingOptions = accountingOptions;
    }
    
    public IrsSheetdataM getEditMaster() {
        return editMaster;
    }
    public void setEditMaster(IrsSheetdataM editMaster) {
        this.editMaster = editMaster;
    }
    
//    public BaseLazyDataModel<IrsSheetdataM> getMasterlazyModel() {
//        return masterlazyModel;
//    }
//    public void setMasterlazyModel(BaseLazyDataModel<IrsSheetdataM> masterlazyModel) {
//        this.masterlazyModel = masterlazyModel;
//    }
    
//    public List<IrsSheetdataM> getMasterfilterResultList() {
//        return masterfilterResultList;
//    }
//    public void setMasterfilterResultList(List<IrsSheetdataM> masterfilterResultList) {
//        this.masterfilterResultList = masterfilterResultList;
//    }
    
    public List<SheetMstVO> getMstVOList() {
        return mstVOList;
    }
    
    public IrsSheetdataReconcilingD getEditDetail() {
        return editDetail;
    }
    public void setEditDetail(IrsSheetdataReconcilingD editDetail) {
        this.editDetail = editDetail;
    }

    public boolean isEditLock() {
        return editLock;
    }

    public Long getReconcilingAccNodeId() {
        return reconcilingAccNodeId;
    }

    public void setReconcilingAccNodeId(Long reconcilingAccNodeId) {
        this.reconcilingAccNodeId = reconcilingAccNodeId;
    }

    public FcCurrency getAnnounceCurr() {
        return announceCurr;
    }

    public boolean isParentComp() {
        return parentComp;
    }

    //</editor-fold>
}
