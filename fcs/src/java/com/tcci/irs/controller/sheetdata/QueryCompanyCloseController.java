/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ListUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.irs.entity.IrsCompanyNoTrans;
import com.tcci.irs.entity.sheetdata.IrsCompanyClose;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.facade.IrsCompanyNoTransFacade;
import com.tcci.irs.facade.ReclMgmtFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcInvoFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "queryCompanyCloseController")
@ViewScoped
public class QueryCompanyCloseController extends BaseController {
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
   
    @EJB
    protected IrsCompanyCloseFacade irsCompanyCloseFacade; 
    @EJB
    protected FcCompanyFacade companyFacade;
    @EJB
    private ReclMgmtFacade reclMgmtFacade;
    @EJB
    private ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @EJB
    private ZtfiAfrcInvoFacade ztfiAfrcInvoFacade;
    @EJB
    private IrsCompanyNoTransFacade irsCompanyNoTransFacade;
    //</editor-fold>    
    

    private IrsCompanyClose condVO = null;
//    private BaseLazyDataModel<IrsCompanyClose> lazyModel; // LazyModel for primefaces datatable lazy loading
//    private List<IrsCompanyClose> filterResultList; // datatable filter 後的結果
    private List<IrsCompanyClose> closeList = new ArrayList<>();
    private IrsCompanyClose editDetail;
    //
    private List<SelectItem> companyOptions;//公司
    private List<SelectItem> groups;//企業團
    private List<CompanyGroupEnum> groupList;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isPowerUsers;
    private boolean isAdmin;
    private boolean noPermission = false;
    private String yearmonth;//開帳設定年月
    private List<IrsCompanyVO> companyVOList = new ArrayList<>();
    private List<IrsCompanyVO> filteredCompanyVOList = new ArrayList<>();//Filter
    private int dispMode = GlobalConstant.DISPMODE_ALL;
    private final int DISPMODE_NOT_UPLOADED_SAP = 4; // 顯示未上傳(SAP)
    private final int DISPMODE_NOT_UPLOADED_NONSAP = 5; // 顯示未上傳(非SAP)
    private final int DISPMODE_NOT_UPLOADED_UNDO = 6; // 顯示未上傳(無需處理)
    private List<DetailTabVO> detailTabs;
    private List<ZtfiAfrcTran> tranDetailList;
    private List<ZtfiAfrcInvo> invoDetailList;
//    private List<FcCompany> reconcilCompanyList;
    private FcCompany generateCompany;
    private FcCompany generateReconcilCompany;
    private List<SelectItem> reconcilCompanyOptions;//對帳公司選單
    private List<SelectItem> ymOptions;
    private String quoteYM;//查詢年月
    private List<IrsCompanyNoTrans> noTransList;
    
    @PostConstruct
    @Override
    protected void init() {
        try {
            String strDispMode = JsfUtil.getCookieValue(GlobalConstant.COOKIE_DISPMODE);
            if(StringUtils.isNotBlank(strDispMode)){
                dispMode = Integer.valueOf(strDispMode).intValue();
            }
            tranDetailList = new ArrayList<>();
            invoDetailList = new ArrayList<>();
        } catch (Exception ex) {
        }
        String controllerClass = getClass().getSimpleName();
        String controllerInstance = controllerClass.substring(0,1).toLowerCase()+controllerClass.substring(1);
        setPageTitle(rb.getString(controllerInstance+".pageTitle"));
        //
        condVO = new IrsCompanyClose();
        
        cgList =  userSession.getTcUser().getCompGroupList();
        isPowerUsers = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        //公司群組權限
        if (!isAdmin && cgList == null) {
            noPermission = true;
            return;
        }
//        groups = this.buildGroupOptions();
//        if (groups == null || groups.isEmpty()) {
//            noPermission = true;
//            return;
//        }
//        if(this.isAdmin){
//            group = CompanyGroupEnum.TCC;//預設台泥
//        }else{
//            if (this.cgList != null && !this.cgList.isEmpty()) {
//                group = this.cgList.get(0).getGroup();
//            }
//        }
        //依企業團查詢
        yearmonth = irsCompanyCloseFacade.findByGroup(null).getYearMonth();//20151223改為統一只有一組跨集團的設定
//        if(StringUtils.isNotBlank(yearmonth)){
//            companyVOList = companyFacade.findCompanyVO(null, yearmonth);
//        }
        quoteYM = yearmonth;
        ymOptions = this.fetchYMList();
        
        this.loadData();
    }
    
    private List<SelectItem> fetchYMList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date now =new Date();
        Calendar calendar = Calendar.getInstance();
        
        List<SelectItem> options = new ArrayList();
        for (int i = 0;i<5;i++) {
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, -i);
            options.add(new SelectItem(sdf.format(calendar.getTime()), sdf.format(calendar.getTime())));
        }
        
        return options;
    }
    
    
//    private List<SelectItem> buildGroupOptions() {
//        List<SelectItem> options = new ArrayList();
//        groupList = new ArrayList();
//        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
//            //admin有所有公司群組權限
//            if(this.isAdmin){
//                groupList.add(item);
//                options.add(new SelectItem(item, item.getName()));
//            }else{
//                if (this.cgList != null && !this.cgList.isEmpty()) {
//                    for(FcUserCompGroupR cg : this.cgList){
//                        if (item.equals(cg.getGroup())) {
//                             groupList.add(item);
//                             options.add(new SelectItem(item, item.getName()));
//                        }
//                    }
//                }
//            }
//        }
//        return options;
//    }
    
    public void selectCompany(){
        
    }
    
    @Override
    protected boolean loadData() {
        // 移除 datatable 目前排序、filter 效果
        JsfUtil.resetDataTable("qryForm:dataList");
//        irsCompanyCloseFacade.init(condVO);
//        List<IrsCompanyClose> mstVOList = irsCompanyCloseFacade.findAll();//20151223改為統一只有一組跨集團的設定
//        List<IrsCompanyClose> mstVOList = new ArrayList();
//        List<IrsCompanyClose> result = irsCompanyCloseFacade.findAll();
//        //依登入者公司群組權限filter
//        for(IrsCompanyClose icc : result){
//            for(CompanyGroupEnum cGroup : groupList){
//                if (cGroup.equals(icc.getGroup())) {
//                    mstVOList.add(icc);
//                }
//            }
//        }
//        lazyModel = new FilterLazyDataModel<>(mstVOList);
        closeList = irsCompanyCloseFacade.findAll();
        
        if(StringUtils.isNotBlank(yearmonth)){
//            companyVOList = companyFacade.findCompanyVO(null, yearmonth);
            this.dispModeChange();
        }
        
        return super.loadData();
    }  
    
    public void initDtl1(IrsCompanyClose detail) {
        this.editDetail = detail;
    }
    
    //<editor-fold defaultstate="collapsed" desc="generate sheet master">
    //產生對帳表 多對多
    public void generate(){
        logger.debug("generate sheetdata yearmonth:"+yearmonth);
        RequestContext context = RequestContext.getCurrentInstance();
        if(yearmonth==null){
            JsfUtil.addErrorMessage("未執行!");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date =sdf.parse(yearmonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;//0~11
            logger.debug("year: " + year + ", month: " + month );
            reclMgmtFacade.generateRecociling(year, month, null, null, true);
            
            JsfUtil.addSuccessMessage("執行完畢!");
        } catch (Exception ex) {
//        } catch (ReclException ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("執行失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    //產生對帳表 一對多
    public void generate(IrsCompanyVO vo){
        logger.debug("generate sheetdata yearmonth:"+yearmonth);
        RequestContext context = RequestContext.getCurrentInstance();
        if(yearmonth==null || vo==null){
            JsfUtil.addErrorMessage("未執行!");
            return;
        }
        logger.debug("generate sheetdata Company:"+vo.getCompany().getCode());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date =sdf.parse(yearmonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;//0~11
            logger.debug("year: " + year + ", month: " + month );
            reclMgmtFacade.generateRecociling(year, month, vo.getCompany(), null, true);
            
            JsfUtil.addSuccessMessage("執行完畢!");
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("執行失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    //產生對帳表 一對一
    public void generate1by1(){
        if(yearmonth==null 
                || generateCompany==null
                || generateReconcilCompany==null){
            JsfUtil.addErrorMessage("未執行!");
            return;
        }
        logger.debug("generate sheetdata yearmonth:"+yearmonth);
        logger.debug("generate sheetdata Company:"+generateCompany.getCode());
        logger.debug("generate sheetdata generateReconcilCompany:"+generateReconcilCompany.getCode());
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date =sdf.parse(yearmonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;//0~11
            logger.debug("year: " + year + ", month: " + month );
            reclMgmtFacade.generateRecociling(year, month, generateCompany, generateReconcilCompany, true);
            
            JsfUtil.addSuccessMessage("執行完畢!");
            context.addCallbackParam("saved", true);
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error("save exception", ex);
            JsfUtil.addErrorMessage("執行失敗!");
            context.addCallbackParam("saved", false);
        }
    }
    
    public void initGenerate(IrsCompanyVO vo){
        logger.debug("initGenerate yearmonth:"+yearmonth);
        logger.debug("initGenerate company:"+vo.getCompany().getCode());
        logger.debug("initGenerate user:"+userSession.getTcUser().getLoginAccount());
        generateCompany = vo.getCompany();
//        generateReconcilCompany = new FcCompany();
        if(generateCompany == null){
            JsfUtil.addErrorMessage("未設定對帳公司權限!");
            return;
        }
        List<FcCompany> reconcilCompanyList = companyFacade.findIrsReconcilCompany(userSession.getTcUser(), vo.getCompany());
        if (CollectionUtils.isNotEmpty(reconcilCompanyList)) {
            reconcilCompanyOptions = this.buildReconcilCompanyOptions(reconcilCompanyList);
//            logger.debug("initGenerate reconcilCompany:"+generateReconcilCompany.getCode());
        }else{
            JsfUtil.addErrorMessage("未設定對帳公司權限!");
        }
    }
    
    private List<SelectItem> buildReconcilCompanyOptions(List<FcCompany> companyList) {
        this.setGenerateReconcilCompany(ListUtils.getFirstElement(companyList));
        return ListUtils.getOptions(companyList);
    }
    
    //</editor-fold>
    
    @Override
    protected boolean check4SaveDtl1() throws Exception {
        isValid = valid;
        String value1 = editDetail.getYearMonth();
        //value1格式檢查
        if(StringUtils.isNotBlank(value1)){
            //init
            String format = "yyyyMM";
            String columnNmae1 = "已關帳年月" ;
            
            String checkvalue1 = DateUtils.checkDate(format, value1, columnNmae1);
            if(StringUtils.isNotBlank(checkvalue1)){
                JsfUtil.addErrorMessage(checkvalue1);
                isValid = !valid;
            }
        }

        if(!isValid){
            loadData();
        }
        return isValid;
    }
    
    @Override
    protected boolean saveDataDtl1() throws Exception {
        TcUser user = userSession.getTcUser();
        editDetail.setCreator(user);
        editDetail.setCreatetimestamp(new Date());
        
//        if(editDetail.getId() != null && editDetail.getId() > 0){
            irsCompanyCloseFacade.edit(editDetail);
//        }else{
//            irsCompanyCloseFacade.create(editDetail);
//        }
        yearmonth = editDetail.getYearMonth();

        String msg = "儲存成功!";
        JsfUtil.addSuccessMessage(msg);
        
        this.loadData();
        return valid;
    }   
    
//    public void changeGroup() {
//        logger.debug("changeGroup:" + this.group);
//        yearmonth = irsCompanyCloseFacade.findByGroup(group).getYearMonth();
//        companyVOList = companyFacade.findCompanyVO(group, yearmonth);
//    }
    
    public void changeYm() {
        logger.debug("changeYm:" + this.quoteYM);
        this.dispModeChange();
    }
    
    public void dispModeChange() {
        logger.debug("dispModeChange dispMode:"+dispMode);
        List<IrsCompanyVO> allCompanyVOList = companyFacade.findIrsCompanyVO(null, quoteYM);
        companyVOList.clear();
        if (GlobalConstant.DISPMODE_UPLOADED==dispMode) {//已上傳
            for (IrsCompanyVO vo : allCompanyVOList) {
                if (vo.getUploadRecord() != null) {
                    companyVOList.add(vo);
                }else if(vo.getNoTrans() != null){//本月無交易 歸類為已上傳
                    companyVOList.add(vo);
                }
            }
        } else if (GlobalConstant.DISPMODE_NOT_UPLOADED==dispMode) {
            for (IrsCompanyVO vo : allCompanyVOList) {
                if (vo.getUploadRecord() == null && vo.getNoTrans() == null) {
                    companyVOList.add(vo);
                }
            }
        } else if (this.DISPMODE_NOT_UPLOADED_SAP==dispMode) {
            for (IrsCompanyVO vo : allCompanyVOList) {
                if (vo.getUploadRecord() == null && vo.getNoTrans() == null) {
                    if (!vo.getCompany().isNonSap() && !vo.getCompany().isUndo()) {
                        companyVOList.add(vo);
                    }
                }
            }
        } else if (this.DISPMODE_NOT_UPLOADED_NONSAP==dispMode) {
            for (IrsCompanyVO vo : allCompanyVOList) {
                if (vo.getUploadRecord() == null && vo.getNoTrans() == null ) {
                    if (vo.getCompany().isNonSap() && !vo.getCompany().isUndo()) {
                        companyVOList.add(vo);
                    }
                }
            }
        } else if (this.DISPMODE_NOT_UPLOADED_UNDO==dispMode) {
            for (IrsCompanyVO vo : allCompanyVOList) {
                if (vo.getUploadRecord() == null && vo.getNoTrans() == null ) {
                    if (vo.getCompany().isUndo()) {
                        companyVOList.add(vo);
                    }
                }
            }
        } else {//ALL
            companyVOList.addAll(allCompanyVOList);
        }
        
        filteredCompanyVOList = companyVOList;//filter前 為剛查詢完的內容
        JsfUtil.saveCookie(GlobalConstant.COOKIE_DISPMODE, String.valueOf(dispMode), GlobalConstant.COOKIE_DISPMODE_MAXAGE);
    }
    
    //是否顯示對帳表功能聯結
    public boolean showLinkBtn(IrsCompanyVO vo){
        if(isPowerUsers){
            return true;
        }else{
            return companyFacade.isUploader(userSession.getTcUser(), vo.getCompany());
        }
    }
    //是否顯示非SAP上傳功能聯結
    public boolean showNonsapLinkBtn(IrsCompanyVO vo){
        if(vo.getCompany() == null){
            return false;
        }
        if(!this.yearmonth.equals(this.quoteYM)
                || !vo.getCompany().isNonSap()){
            return false;
        }
        if(isPowerUsers){
            return true;
        }else{
            return companyFacade.isUploader(userSession.getTcUser(), vo.getCompany());
        }
    }
    
    //是否可查詢對帳資料明細
    public boolean showDetail(IrsCompanyVO vo){
        if(vo.getUploadRecord() ==null){
            return false;
        }
        if(isAdmin){
            return true;
        }else{
            if(GlobalConstant.PARENT_COMP_1000.equals(vo.getCompany().getCode())){
                //有台泥對其他對帳公司的權限
                List<FcCompany> companyList = companyFacade.findIrsReconcilCompany(userSession.getTcUser(), vo.getCompany());
                return CollectionUtils.isNotEmpty(companyList);
            }else{
                return companyFacade.isUploader(userSession.getTcUser(), vo.getCompany());
            }
        }
    }
    
    //是否可顯示產生對帳表 一對一 (限1000台泥)
    public boolean showGenbyReconcilCompany(IrsCompanyVO vo){
        if(!this.yearmonth.equals(this.quoteYM)){
            return false;
        }
        if(vo.getUploadRecord() ==null){
            return false;
        }
        if(GlobalConstant.PARENT_COMP_1000.equals(vo.getCompany().getCode())){
        }else{
            return false;
        }
        if(isAdmin){
            return true;
        }else{
            //有台泥對其他對帳公司的權限
            List<FcCompany> companyList = companyFacade.findIrsReconcilCompany(userSession.getTcUser(), vo.getCompany());
            return CollectionUtils.isNotEmpty(companyList);
//            return companyFacade.isUploader(userSession.getTcUser(), vo.getCompany());
        }
    }
    //是否可顯示產生對帳表 一對多 (1000台泥以外)
    public boolean showGenbyCompany(IrsCompanyVO vo){
        if(!this.yearmonth.equals(this.quoteYM)){
            return false;
        }
        if(vo.getUploadRecord() ==null){
            return false;
        }
        if(GlobalConstant.PARENT_COMP_1000.equals(vo.getCompany().getCode())){
            return false;
        }
        if(isAdmin){
            return true;
        }else{
            return companyFacade.isUploader(userSession.getTcUser(), vo.getCompany());
        }
    }
    
    
    //對帳資料金額明細查詢
    public void fetchDetail(IrsCompanyVO vo){
//        logger.debug("fetchDetail Company,yearmonth:("+vo.getCompany().getCode()+"_"+yearmonth+")");
        logger.debug("fetchDetail Company,quoteYM:("+vo.getCompany().getCode()+"_"+quoteYM+")");
        detailTabs = new ArrayList<>();
        tranDetailList = ztfiAfrcTranFacade.find(vo.getCompany().getCode(), quoteYM);
        invoDetailList = ztfiAfrcInvoFacade.find(vo.getCompany().getCode(), quoteYM);
        
        DetailTabVO tranTabVO = new DetailTabVO();
        tranTabVO.setTranDetailList(tranDetailList);
        tranTabVO.setCode("TRAN");
        detailTabs.add(tranTabVO);
        
        DetailTabVO invoTabVO = new DetailTabVO();
        invoTabVO.setCode("INVO");
        invoTabVO.setInvoDetailList(invoDetailList);
        detailTabs.add(invoTabVO);
//        logger.debug("fetchDetail tranDetailList:"+tranDetailList.size());
//        logger.debug("fetchDetail tranDetailList:"+invoDetailList.size());
    }
    
    public String getCompanyNote(IrsCompanyVO vo){
        if(vo.getCompany().isUndo()){
            return "無需處理";
        }
        if(vo.getCompany().isNonSap()){
            return "非SAP";
        }else{
            return "SAP";
        }
    }
    
    // action
    public void noTransChange(IrsCompanyVO vo) {
        logger.debug("noTransChange Company,quoteYM:("+vo.getCompany().getCode()+"_"+quoteYM+")");
        if(vo.getNoTrans() != null){
            logger.debug("noTransChange remove Company,YM:("+vo.getNoTrans().getCompany().getCode()+"_"+vo.getNoTrans().getYearmonth()+")");
            irsCompanyNoTransFacade.remove(vo.getNoTrans().getId());
        }else{
            irsCompanyNoTransFacade.insert(quoteYM, vo.getCompany(), userSession.getTcUser());
        }
        this.dispModeChange();//reload
    }
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    
    //</editor-fold>       
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<IrsCompanyClose> getCloseList() {
        return closeList;
    }

    public void setCloseList(List<IrsCompanyClose> closeList) {
        this.closeList = closeList;
    }
    
    public IrsCompanyClose getCondVO() {
        return condVO;
    }

    public void setCondVO(IrsCompanyClose condVO) {
        this.condVO = condVO;
    }
    
    public IrsCompanyClose getEditDetail() {
        return editDetail;
    }

    public void setEditDetail(IrsCompanyClose editDetail) {
        this.editDetail = editDetail;
    }

    public List<SelectItem> getCompanyOptions() {
        return companyOptions;
    }

    public void setCompanyOptions(List<SelectItem> companyOptions) {
        this.companyOptions = companyOptions;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

//    public List<SelectItem> getGroups() {
//        return groups;
//    }

    public List<IrsCompanyVO> getCompanyVOList() {
        return companyVOList;
    }

    public List<IrsCompanyVO> getFilteredCompanyVOList() {
        return filteredCompanyVOList;
    }

    public void setFilteredCompanyVOList(List<IrsCompanyVO> filteredCompanyVOList) {
        this.filteredCompanyVOList = filteredCompanyVOList;
    }
    
    public int getDispMode() {
        return dispMode;
    }

    public void setDispMode(int dispMode) {
        this.dispMode = dispMode;
    }

    public List<DetailTabVO> getDetailTabs() {
        return detailTabs;
    }

    public FcCompany getGenerateReconcilCompany() {
        return generateReconcilCompany;
    }
    
    public void setGenerateReconcilCompany(FcCompany generateReconcilCompany) {
        this.generateReconcilCompany = generateReconcilCompany;
    }

    public FcCompany getGenerateCompany() {
        return generateCompany;
    }

    public List<SelectItem> getReconcilCompanyOptions() {
        return reconcilCompanyOptions;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public String getQuoteYM() {
        return quoteYM;
    }

    public void setQuoteYM(String quoteYM) {
        this.quoteYM = quoteYM;
    }

    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }
    //</editor-fold>

    public String getLinkUrl() {
        return "../sheetdata/querySheetdata.xhtml?yyyymm=" + quoteYM + "&";
    }
    public String getLinkUploadUrl() {
        return "../nonSAP/reportUpload.xhtml?yyyymm=" + quoteYM + "&";
    }
}
