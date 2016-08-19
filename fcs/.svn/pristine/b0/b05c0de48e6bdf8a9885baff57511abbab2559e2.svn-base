/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.fileio.ExportUtil;
import com.tcci.fc.util.ListUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.util.time.DateFormatUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.irs.entity.reconciling.IrsSheetdataReconcilingD;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.enums.SheetTypeEnum;
import com.tcci.irs.facade.IrsAccountNodeFacade;
import com.tcci.irs.facade.reconciling.IrsSheetdataReconcilingDFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "querySheetdataController")
@ViewScoped
public class QuerySheetdataController extends BaseController {
//    extends BaseController
    //<editor-fold defaultstate="collapsed" desc="Injects">
   
    @EJB
    protected IrsSheetdataMFacade irsSheetdataMFacade; 
    @EJB
    protected IrsSheetdataReconcilingDFacade irsSheetdataReconcilingDFacade; 
    @EJB
    protected FcCompanyFacade fcCompanyFacade; 
    @EJB
    protected IrsCompanyCloseFacade irsCompanyCloseFacade;
    @EJB
    protected ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @EJB
    protected IrsAccountNodeFacade irsAccountNodeFacade;
    
    @EJB
    protected FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    //</editor-fold>    

    private IrsSheetdataM condVO = null;
//    private BaseLazyDataModel<IrsSheetdataM> lazyModel; // LazyModel for primefaces datatable lazy loading
//    private List<IrsSheetdataM> filterResultList; // datatable filter 後的結果
    private List<SheetMstVO> mstVOList;
//    private String closeYearMonth;
    
    //helper
    private List<SelectItem> reCompanyOptions;//個體公司選單
    private List<SelectItem> paCompanyOptions;//對帳公司選單
    private List<SelectItem> ymOptions;
    private List<SelectItem> groups;//企業團
    private List<CompanyGroupEnum> groupList;//企業團
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isPowerUsers;
    private boolean isAdmin;
    private boolean noPermission = false;
    private List<ZtfiAfrcTran> amountDetailList;
    private List<DetailTabVO> detailTabs;
    private BigDecimal totalAmount;
    private boolean isDiff = false;
    private FcCurrency announceCurr;//公告主體幣別
    private List<FcMonthlyExchangeRate> exchangeRateList;
    private boolean parentComp = false;//是否為母公司(ex 1000、8000) 影響UI欄位 調節作業
    private FcCompany linkReCompany;
    
    
    @PostConstruct
    @Override
    protected void init() {        
        logger.debug("init~!");
        String controllerClass = getClass().getSimpleName();
        String controllerInstance = controllerClass.substring(0,1).toLowerCase()+controllerClass.substring(1);
        setPageTitle(rb.getString(controllerInstance+".pageTitle"));
        
        cgList =  userSession.getTcUser().getCompGroupList();
        isPowerUsers = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        
        //
        condVO = new IrsSheetdataM();
        if (!checkParameter()) {
            return;
        }
        
        groups = this.buildGroupOptions();
        if (groups == null || groups.isEmpty()) {
            noPermission = true;
            return;
        }
        if(this.isAdmin){
            group = CompanyGroupEnum.TCC;//預設台泥
        }else{
            if (this.cgList != null && !this.cgList.isEmpty()) {
                group = this.cgList.get(0).getGroup();
            }
        }
        
        //依企業團查詢
        //reCompanyOptions
        reCompanyOptions = this.buildReCompanyOptions();
        
        //paCompanyOptions
        paCompanyOptions = this.buildPaCompanyOptions();
        
        List<String> ymList = irsSheetdataMFacade.findDataYMList();//依data產生YM選單
        ymOptions = ListUtils.getOptions(ymList);
        if(StringUtils.isBlank(condVO.getYearMonth())){
            condVO.setYearMonth(irsCompanyCloseFacade.findByGroup(null).getYearMonth());
        }
    }
    
    @Override
    protected boolean checkParameter() {
        String reCompanyId = JsfUtil.getRequestParameter("reCompanyId");
        String yyyymm = JsfUtil.getRequestParameter("yyyymm");
        if(StringUtils.isNotBlank(reCompanyId) && StringUtils.isNotBlank(yyyymm)){
            Long sheetdatamId = Long.valueOf(reCompanyId);
            linkReCompany = fcCompanyFacade.find(sheetdatamId);
            //
//            condVO.setReCompany(linkReCompany);
            // 
//            condVO.setYearMonth(irsCompanyCloseFacade.findByGroup(reCompany.getGroup()).getYearMonth());
//            condVO.setYearMonth(irsCompanyCloseFacade.findByGroup(CompanyGroupEnum.getFromCode(reCompany.getGroup().getCode())).getYearMonth());
//            condVO.setYearMonth(irsCompanyCloseFacade.findByGroup(null).getYearMonth());
            condVO.setYearMonth(yyyymm);
        }else{
//            return !valid;
        }

        return super.checkParameter(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        groupList = new ArrayList();
        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
            //admin有所有公司群組權限
            if(this.isAdmin){
                groupList.add(item);
                options.add(new SelectItem(item, item.getName()));
            }else{
                if (this.cgList != null && !this.cgList.isEmpty()) {
                    for(FcUserCompGroupR cg : this.cgList){
                        if (item.equals(cg.getGroup())) {
                            groupList.add(item);
                            options.add(new SelectItem(item, item.getName()));
                        }
                    }
                }
            }
        }
        return options;
    }
    
    private List<SelectItem> buildReCompanyOptions() {
        List<FcCompany> reCompanyList;
        if(isPowerUsers){
            reCompanyList = fcCompanyFacade.findAllActiveByGroup(group, false, false, true);
        }else{
            reCompanyList = fcCompanyFacade.findByUploaderR(userSession.getTcUser(), group);
            
            //台泥對帳公司權限
            List<FcCompany> irsReconcilCompanyList = fcCompanyFacade.findIrsReconcilCompanyRE(userSession.getTcUser());
            for(FcCompany com : irsReconcilCompanyList){
                if (CollectionUtils.isNotEmpty(reCompanyList)) {
                    if(!reCompanyList.contains(com)){
                        reCompanyList.add(com);
                    }
                }else{
                    reCompanyList = new ArrayList<>();
                    reCompanyList.add(com);
                }
            }
            //重新排序
            if (CollectionUtils.isNotEmpty(reCompanyList)) {
                Collections.sort(reCompanyList, new Comparator<FcCompany>() {
                    @Override
                    public int compare(FcCompany c1, FcCompany c2) {
                        return c1.getCode().compareTo(c2.getCode());
                    }
                });
            }
        }
//        reCompanyList = new ArrayList();
//        reCompanyList.add(fcCompanyFacade.find(Long.valueOf("1")));
//        reCompanyList.add(fcCompanyFacade.find(Long.valueOf("20")));
//        reCompanyList.add(fcCompanyFacade.find(Long.valueOf("30")));
//        reCompanyOptions = ListUtils.getOptions(reCompanyList);
        
        //setReCompany
        if(linkReCompany!= null && reCompanyList.contains(linkReCompany)){
            condVO.setReCompany(linkReCompany);
        }else{
            condVO.setReCompany(ListUtils.getFirstElement(reCompanyList));
        }
        
        return ListUtils.getOptions(reCompanyList);
    }
    
    private List<SelectItem> buildPaCompanyOptions() {
        List<FcCompany> paCompanyList = null;
        
        if(condVO.getReCompany() != null){
            //if個體公司 台泥, 對帳公司選單限 使用者對帳公司權限關聯表
            //admin 可全查 isAdmin ?!
            //admin HQ 可全查 isPowerUsers ?!
            if(GlobalConstant.PARENT_COMP_1000.equals(condVO.getReCompany().getCode()) 
                    && !isAdmin){
                paCompanyList = fcCompanyFacade.findIrsReconcilCompany(userSession.getTcUser(), condVO.getReCompany());
            }else{
                paCompanyList = fcCompanyFacade.findAllActiveByGroup(null, false, false, true);
                paCompanyList.remove(condVO.getReCompany());//移除相同選項
            }
        }
        ///setPaCompany
        if(condVO.getPaCompany() == null){
            condVO.setPaCompany(ListUtils.getFirstElement(paCompanyList));
        }
        
        if (CollectionUtils.isNotEmpty(paCompanyList)) {
            List<SelectItem> options = new ArrayList<>();
            //開放全對帳對象查詢 暫不開放使用
            if("kyle.cheng".equals(userSession.getTcUser().getLoginAccount())){
//            if(isAdmin){
                options.add(new SelectItem(null, "===="));
            }
            for (FcCompany comp : paCompanyList) {
                String label = comp.toString();
                options.add(new SelectItem(comp, label));
            }
            return options;
        }else{
            return null;
        }
//        return ListUtils.getOptions(paCompanyList);
    }
    
    @Override
    protected boolean check4Query() throws Exception {
        //檢核:年月
        String yearMonth = condVO.getYearMonth();
        String columnName = "年月" ;
        String format_yearMonth = "yyyyMM";
        String errMsg = DateUtils.checkDate(format_yearMonth, yearMonth, columnName);
        if(StringUtils.isNotBlank(errMsg)){
            JsfUtil.addErrorMessage(errMsg);
            return !valid;
        }
        //關帳
        
        return super.check4Query(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected boolean loadData() {
        // 移除 datatable 目前排序、filter 效果
        JsfUtil.resetDataTable("qryForm:dataList");
        String compCode = condVO.getReCompany().getCode();
        parentComp = (GlobalConstant.PARENT_COMP_1000.equals(compCode) || GlobalConstant.PARENT_COMP_8000.equals(compCode));//個體公司是否為母公司
        //公告主體幣別
        announceCurr = condVO.getReCompany().getGroup().getCurrency();
        if(announceCurr!=null){
            exchangeRateList = fcMonthlyExchangeRateFacade.findByYMAndToCurrency(condVO.getYearMonth(), announceCurr);
        }

//        mstVOList = irsSheetdataMFacade.find(condVO);
        mstVOList = new ArrayList<>();
        List<IrsSheetdataM> mstList = irsSheetdataMFacade.find(condVO);
        for (IrsSheetdataM entityMaster : mstList) {
            entityMaster.setIndividualCompany(condVO.getReCompany());
            Long sheetdataMId = entityMaster.getId();
            List<IrsSheetdataReconcilingD> detailList = irsSheetdataReconcilingDFacade.findBySheetdataMId(sheetdataMId);
            entityMaster.setIrsSheetdataReconcilingDCollection(detailList);
            //設定VO
            SheetMstVO mstVO = new SheetMstVO();
            mstVO.setMaster(entityMaster);
            mstVO.setReAnnounceAmount(this.getReAnnounceAmount(entityMaster));
            mstVO.setPaAnnounceAmount(this.getPaAnnounceAmount(entityMaster));
            mstVOList.add(mstVO);
        }
//        lazyModel = new FilterLazyDataModel<>(mstVOList);
        
        if (CollectionUtils.isEmpty(mstVOList)){
            JsfUtil.addWarningMessage("查無對帳資料!");
        }
        
        return super.loadData();
    }  
    
//    public void export(){
//        logger.debug("export!");
//    }
    
    /**
     * 內文 換行文字過濾
     * @return 
     */
//    @Override
//    protected int[] getContentColumns(){
//        return new int[]{4, 5};
//    }
    
    /**
     * 針對匯出Excel客製化處理
     * 交易明細 標準匯出
     *
     * @param document
     */
    @Override
    public void postProcessXLS(Object document) {
        logger.debug("postProcessXLS ...");
        try{
            // Header 文字過濾
            ExportUtil.processHeader((HSSFWorkbook) document);
            logger.debug("postProcessXLS after processHeader...");
            
            //內文 換行文字過濾
//            if (null!=getContentColumns() && getContentColumns().length>0){
//                ExportUtil.processContentFields((HSSFWorkbook) document, getContentColumns());
//            }
            
        }catch(Exception e){
            logger.error("postProcessXLS Exception :\n", e);
        }
    }
    
    /**
     * 針對匯出Excel客製化處理 對帳表 master detail
     *
     * @param document
     */
    public void postProcessMaster(Object document) {
        logger.debug("postProcessMaster ...");
        try{
            Workbook wb = (Workbook) document;
	    Sheet sheet0 = wb.getSheetAt(0);
	    
	    int rowNumber = sheet0.getLastRowNum();
	    
	    // 向下移動4行Row
	    sheet0.shiftRows(0, sheet0.getLastRowNum(), 4);
	    
	    sheet0.addMergedRegion(new CellRangeAddress(
		    0, //first row (0-based)
		    0, //last row  (0-based)
		    0, //first column (0-based)
		    2 //last column  (0-based)
	    ));
	    
	    // Header 文字過濾
            ExportUtil.processHeader((HSSFWorkbook) document);
	    
	    // 內文 框線
	    CellStyle cellStyle = ExportUtil.borderStyle(wb.createCellStyle(), CellStyle.BORDER_THIN);
	    rowNumber = sheet0.getLastRowNum();
	    
	    for (int i = 5; i <= rowNumber; i++) {
		Row row = sheet0.getRow(i);
		int colNumber = row.getLastCellNum();
		for (int j = 0; j < colNumber; j++) {
		    Cell textCell = row.getCell(j);
		    textCell.setCellStyle(cellStyle);
		}
	    }
	    
	    // 寫上標頭
	    IrsSheetdataM dataM;
	    String title0 = "";
	    String title1 = "";
	    String title2 = "";
	    String title3 = userSession.getTcUser().getName();
	    if (CollectionUtils.isNotEmpty(mstVOList)) {
		dataM = mstVOList.get(0).getMaster();
		title0 = dataM.getReCompanyDisPlay().getName();
		title1 = dataM.getPaCompanyDisPlay().getName();
		title2 = dataM.getYMString();
	    }
	    ExportUtil.writeCell(sheet0.getRow(0), 0, title0 + rb.getString("irs.msg.rpt01"));	    
	    ExportUtil.writeCell(sheet0.getRow(1), 0, rb.getString("irs.msg.rpt02"));
	    ExportUtil.writeCell(sheet0.getRow(2), 0, rb.getString("irs.msg.rpt03"));
	    ExportUtil.writeCell(sheet0.getRow(3), 0, rb.getString("irs.msg.rpt04"));
	    
	    ExportUtil.writeCell(sheet0.getRow(1), 1, title1);
	    ExportUtil.writeCell(sheet0.getRow(2), 1, title2);
	    ExportUtil.writeCell(sheet0.getRow(3), 1, title3);
	    
	    // 計算合併欄位
	    int startPos = 5;
	    int endPos = 5;
	    Row row5 = sheet0.getRow(5);
	    String value = row5.getCell(0).getStringCellValue();
	    for (int i = startPos + 1; i < sheet0.getPhysicalNumberOfRows(); i++) {
		Row row = sheet0.getRow(i);
		Cell cell = row.getCell(0);
		String valueTmp = cell.getStringCellValue();
		if (value.equals(valueTmp)) {
		    endPos = i;
		    continue;
		}
		sheet0.addMergedRegion(new CellRangeAddress(
			startPos, //first row (0-based)
			endPos, //last row  (0-based)
			0, //first column (0-based)
			0 //last column  (0-based)
		));
		cell = sheet0.getRow(startPos).getCell(0);
		CellStyle style = ExportUtil.borderStyle(wb.createCellStyle(), CellStyle.BORDER_THIN);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellValue(value);
		cell.setCellStyle(style);
		for (int j = startPos; j <= endPos; j++) {//highlight
		    cell = sheet0.getRow(j).getCell(2);//個體會科
		    cell.setCellStyle(style);
                    cell = sheet0.getRow(j).getCell(this.parentComp ? 6 : 5);//對帳會科
                    cell.setCellStyle(style);
		}
		value = valueTmp;
		startPos = i;
	    }
	    sheet0.addMergedRegion(new CellRangeAddress(
		    startPos, //first row (0-based)
		    endPos, //last row  (0-based)
		    0, //first column (0-based)
		    0 //last column  (0-based)
	    ));
	    Cell cell = sheet0.getRow(startPos).getCell(0);
	    CellStyle style = ExportUtil.borderStyle(wb.createCellStyle(), CellStyle.BORDER_THIN);
	    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    cell.setCellValue(value);
	    cell.setCellStyle(style);
            for (int j = startPos; j <= endPos; j++) {//highlight
                cell = sheet0.getRow(j).getCell(2);//個體會科
                cell.setCellStyle(style);
                
                cell = sheet0.getRow(j).getCell(this.parentComp ? 6 : 5);//對帳會科
                cell.setCellStyle(style);
            }
	    
	    ExportUtil.setSheetName((HSSFWorkbook) document, 0, "Master");//指定Sheet 指定名稱
	    //產生detail sheet
	    if (CollectionUtils.isNotEmpty(mstVOList)){
		this.postProcessXLSDetail((HSSFWorkbook) document);
	    }
            logger.debug("postProcessMaster after processHeader...");
        }catch(Exception e){
            logger.error("postProcessMaster Exception :\n", e);
        }
    }
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
        reCompanyOptions = this.buildReCompanyOptions();
        paCompanyOptions = this.buildPaCompanyOptions();
//        condVO.setYearMonth(irsCompanyCloseFacade.findByGroup(group).getYearMonth());
        
        mstVOList = null;
//        filterResultList = null; // filterValue 初始化
        // 移除 datatable 目前排序、filter 效果
        JsfUtil.resetDataTable("qryForm:dataList");
    }
    
    public void changeReCompany(){
        FcCompany reCompany = condVO.getReCompany();
        logger.debug("changeReCompany:" + reCompany.getCode());
        
        paCompanyOptions = this.buildPaCompanyOptions();
        
        mstVOList = null;
        // 移除 datatable 目前排序、filter 效果
        JsfUtil.resetDataTable("qryForm:dataList");
    }
    
    //個體方金額明細
    public void fetchReDetail(IrsSheetdataM m){
        logger.debug("fetchReDetail! accCode:"+m.getReAccountCodeDisPlay());
//        List<DetailTabVO> codeList = irsAccountNodeFacade.findCodeListByParent(
//                m.getReCompanyDisPlay().getGroup().getCode(), m.getReAccountCodeDisPlay(), null);//不傳AccountTypeEnum.RE
        //20160105 調整取得child code的方式
        List<DetailTabVO> codeList = irsAccountNodeFacade.findCodeListByParent(m.getReAccountDisPlay());
        detailTabs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(codeList)) {
            for(DetailTabVO tabVO : codeList){
                amountDetailList = ztfiAfrcTranFacade.findDetailByAccCode(m, AccountTypeEnum.RE, tabVO.getCode());
                tabVO.setTranDetailList(amountDetailList);
                totalAmount = BigDecimal.ZERO;
                for(ZtfiAfrcTran tran : amountDetailList){
                    totalAmount = totalAmount.add(tran.getWrbtr());
                }
                tabVO.setTotalAmount(totalAmount);
                detailTabs.add(tabVO);
            }
        }else{
            DetailTabVO tabVO = new DetailTabVO();
            tabVO.setCode(m.getReAccountCodeDisPlay());
            tabVO.setName(m.getReAccountNameDisPlay());
            amountDetailList = ztfiAfrcTranFacade.findDetail(m, AccountTypeEnum.RE);
            tabVO.setTranDetailList(amountDetailList);
            totalAmount = BigDecimal.ZERO;
            for(ZtfiAfrcTran tran : amountDetailList){
                totalAmount = totalAmount.add(tran.getWrbtr());
            }
            tabVO.setTotalAmount(totalAmount);
            detailTabs.add(tabVO);
        }
        
        isDiff = (totalAmount.compareTo(m.getReAmountOrigDisPlay()) != 0);
    }
    
    
    //對帳方金額明細
    public void fetchPaDetail(IrsSheetdataM m){
        logger.debug("fetchPaDetail! accCode:"+m.getPaAccountCodeDisPlay());
//        List<DetailTabVO> codeList = irsAccountNodeFacade.findCodeListByParent(
//                m.getPaCompanyDisPlay().getGroup().getCode(), m.getPaAccountCodeDisPlay(), null);//不傳AccountTypeEnum.PA
        //20160105 調整取得child code的方式
        List<DetailTabVO> codeList = irsAccountNodeFacade.findCodeListByParent(m.getPaAccountDisPlay());
        detailTabs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(codeList)) {
            logger.debug("fetchPaDetail! codeList:"+codeList.size());
            for(DetailTabVO tabVO : codeList){
                amountDetailList = ztfiAfrcTranFacade.findDetailByAccCode(m, AccountTypeEnum.PA, tabVO.getCode());
                tabVO.setTranDetailList(amountDetailList);
                totalAmount = BigDecimal.ZERO;
                for(ZtfiAfrcTran tran : amountDetailList){
                    totalAmount = totalAmount.add(tran.getWrbtr());
                }
                tabVO.setTotalAmount(totalAmount);
                detailTabs.add(tabVO);
            }
        }else{
            DetailTabVO tabVO = new DetailTabVO();
            tabVO.setCode(m.getPaAccountCodeDisPlay());
            tabVO.setName(m.getPaAccountNameDisPlay());
            amountDetailList = ztfiAfrcTranFacade.findDetail(m, AccountTypeEnum.PA);
            tabVO.setTranDetailList(amountDetailList);
            totalAmount = BigDecimal.ZERO;
            for(ZtfiAfrcTran tran : amountDetailList){
                totalAmount = totalAmount.add(tran.getWrbtr());
            }
            tabVO.setTotalAmount(totalAmount);
            detailTabs.add(tabVO);
        }
        
        isDiff = (totalAmount.compareTo(m.getPaAmountOrigDisPlay()) != 0);
    }
    
    //不調節備註
    public String getRemarkOnly(IrsSheetdataM master){
        List<IrsSheetdataReconcilingD> detailList = irsSheetdataReconcilingDFacade.findBySheetdataMId(master.getId());
        if (CollectionUtils.isNotEmpty(detailList)) {
            for(IrsSheetdataReconcilingD detail : detailList){
                if(detail.isRemarkOnly()){
                    return detail.getRemark();
                }
            }
        }
        return "";
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
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    //處理detail sheet
    private void postProcessXLSDetail(HSSFWorkbook wb) {
        logger.debug("postProcessXLSDetail start");
        for(SheetMstVO master : mstVOList){
            List<IrsSheetdataReconcilingD> detailList = (List)master.getMaster().getIrsSheetdataReconcilingDCollection();
            if (CollectionUtils.isNotEmpty(detailList)) {
                StringBuilder sheetName = new StringBuilder();//Detail_會科(RE)_幣別
                sheetName.append("Detail_").append(master.getMaster().getReAccountNameDisPlay()).append("_").append(master.getMaster().getCurrency().getCode());
                logger.debug("postProcessXLSDetail sheetName:"+sheetName);
                HSSFSheet dSheet = wb.createSheet(sheetName.toString());
                
                //headerRow
                Row headerRow = dSheet.createRow(0);
                String[] strTitle = dTitleLabel();
                int cols = 0;
                for (String title : strTitle) {
                    headerRow.createCell(cols).setCellValue(title);
                    cols++;
                }
                //contentRow
                int contentRowCount = 1;
                for(IrsSheetdataReconcilingD detail : detailList){
                    Row detailTitleRow = dSheet.createRow(contentRowCount);
                    String accountType = detail.getAccountType();
                    detailTitleRow.createCell(0).setCellValue(accountType);
                    AccountTypeEnum accountTypeEnum = AccountTypeEnum.getFromCode(accountType);
                    String compStr;
                    if(AccountTypeEnum.RE.equals(accountTypeEnum)){
                        compStr = detail.getSheetdatam().getReCompanyDisPlay().toString();
                    }else{
                        compStr = detail.getSheetdatam().getPaCompanyDisPlay().toString();
                    }
                    detailTitleRow.createCell(1).setCellValue(compStr);
                    String accNodeStr = "";
                    if(detail.getAccountNode()!=null){
                        accNodeStr = detail.getAccountNode().toString();
                    }
                    detailTitleRow.createCell(2).setCellValue(accNodeStr);
//                    detailTitleRow.createCell(1).setCellValue(detail.getAccountName());
                    detailTitleRow.createCell(3).setCellValue(detail.getRemark());
                    detailTitleRow.createCell(4).setCellValue(detail.getAmountAdjustments().toString());
                    detailTitleRow.createCell(5).setCellValue(detail.getCreator().getDisplayIdentifier());
                    if (null != detail.getCreatetimestamp()) {
                        String dtString = DateFormatUtils.format(detail.getCreatetimestamp(), DateFormatUtils.COMMON_DATETIME_FORMAT.getPattern());
                        detailTitleRow.createCell(6).setCellValue(dtString);
                    }
                    contentRowCount++;
                }
                
                // Header 文字過濾
                ExportUtil.processHeader(wb, dSheet);
                logger.debug("postProcessXLSDetail after processHeader...");
            }
        }
        logger.debug("postProcessXLSDetail end");
    }
    
    private String[] dTitleLabel(){
        String[] cols = new String[]{
            "對帳類別",
            "對帳公司",
            "會科名稱",
            "備註",
            "調整金額",
            "編輯人",
            "編輯時間"
        };
        return cols;
    }
    
    //</editor-fold>       
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
//    public BaseLazyDataModel<IrsSheetdataM> getLazyModel() {
//        return lazyModel;
//    }
//
//    public void setLazyModel(BaseLazyDataModel<IrsSheetdataM> lazyModel) {
//        this.lazyModel = lazyModel;
//    }
//
//    public List<IrsSheetdataM> getFilterResultList() {
//        return filterResultList;
//    }
//
//    public void setFilterResultList(List<IrsSheetdataM> filterResultList) {
//        this.filterResultList = filterResultList;
//    }
    
    public IrsSheetdataM getCondVO() {
        return condVO;
    }

    public void setCondVO(IrsSheetdataM condVO) {
        this.condVO = condVO;
    }

//    public String getCloseYearMonth() {
//        return closeYearMonth;
//    }
//    public void setCloseYearMonth(String closeYearMonth) {
//        this.closeYearMonth = closeYearMonth;
//    }
    
    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }
    
    public List<SelectItem> getReCompanyOptions() {
        return reCompanyOptions;
    }
    
    public List<SelectItem> getPaCompanyOptions() {
        return paCompanyOptions;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

    public List<SheetMstVO> getMstVOList() {
        return mstVOList;
    }

//    public List<ZtfiAfrcTran> getAmountDetailList() {
//        return amountDetailList;
//    }
//
//    public BigDecimal getTotalAmount() {
//        return totalAmount;
//    }
//
//    public boolean isIsDiff() {
//        return isDiff;
//    }

    public List<DetailTabVO> getDetailTabs() {
        return detailTabs;
    }

    public FcCurrency getAnnounceCurr() {
        return announceCurr;
    }

    public boolean isParentComp() {
        return parentComp;
    }
    //</editor-fold>
    
}
