/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller.report;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.fileio.ExportUtil;
import com.tcci.fc.util.ListUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.fcs.util.ReportConfig;
import com.tcci.irs.entity.IrsCompanyType;
import com.tcci.irs.enums.SheetTypeEnum;
import com.tcci.irs.facade.IrsCompanyTypeFacade;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "irsReportController")
@ViewScoped
public class IrsReportController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IrsReportController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    protected IrsCompanyCloseFacade irsCompanyCloseFacade;
    @EJB
    private IrsCompanyTypeFacade companyTypeFacade;
    @EJB
    private ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @EJB
    protected FcCompanyFacade fcCompanyFacade; 
    @EJB
    protected IrsSheetdataMFacade irsSheetdataMFacade;
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    //</editor-fold>

    private List<SelectItem> reCompanyOptions;//個體公司選單
    private FcCompany reCompany;
    private List<SelectItem> ymOptions;
    private String yearmonth;
    private StreamedContent exportFile; // 匯出檔案
    
    
    // 企業團選項
    private List<SelectItem> groups;
    private CompanyGroupEnum group;
    private List<FcUserCompGroupR> cgList;
    private boolean isPowerUsers;
    private boolean isAdmin;
    private boolean noPermission = false;
    
    private List<IrsCompanyType> companyTypeList;
    private List<IrsReportVO> reportVOList;
    private List<FcMonthlyExchangeRate> rateList;
    private String bsisType;
    private boolean showMultiCurr = false;//顯示多幣別
    
    @PostConstruct
    @Override
    protected void init() {
         //20151119 增加多選公司群組
        cgList =  userSession.getTcUser().getCompGroupList();
        isPowerUsers = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        //公司群組權限
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
        
        List<String> ymList = irsSheetdataMFacade.findDataYMList();//依data產生YM選單
        ymOptions = ListUtils.getOptions(ymList);
        yearmonth = irsCompanyCloseFacade.findByGroup(null).getYearMonth();//20151223改為統一只有一組跨集團的設定
        this.fetchRateList();
    }
    
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
            //其他 不出報表
            if(CompanyGroupEnum.OTHER.equals(item)){
                continue;
            }
            //admin有所有公司群組權限
            if(this.isAdmin){
                options.add(new SelectItem(item, item.getName()));
            }else{
                if (this.cgList != null && !this.cgList.isEmpty()) {
                    for(FcUserCompGroupR cg : this.cgList){
                        if (item.equals(cg.getGroup())) {
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
        this.setReCompany(ListUtils.getFirstElement(reCompanyList));
        return ListUtils.getOptions(reCompanyList);
    }
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
        reCompanyOptions = this.buildReCompanyOptions();
        reportVOList = new ArrayList<>();
//        JsfUtil.resetDataTable("qryForm:dataList");
    }
    
    public void changeReCompany(){
        logger.debug("changeReCompany:" + this.reCompany);
    }
    
    public void changeYM(){
        logger.debug("changeYM:" + this.yearmonth);
        this.fetchRateList();
    }
    
    private void fetchRateList(){
        rateList = fcMonthlyExchangeRateFacade.findByYMAndToCurrencyCode(yearmonth, CurrencyEnum.TWD.name());
    }
    
    public void export(String sheetType) {
        this.queryDataTable(sheetType);
    }
    
    /**
     * 針對匯出Excel客製化處理
     * 交易明細 標準匯出
     *
     * @param document
     * @return 
     */
    @Override
    public void postProcessXLS(Object document) {
        logger.debug("postProcessXLS ...");
        //內文 換行文字過濾
        if (null!=getContentColumns() && getContentColumns().length>0){
            ExportUtil.processContentFields((HSSFWorkbook) document, getContentColumns());
        }

        // High Light 處理 (放在 postProcessXLS 最後一部)
        if (null!=getHighLightColumns() && getHighLightColumns().length>0){
            ExportUtil.highLightFields((HSSFWorkbook) document, getHighLightColumns(), 1);
        }
        
        if(getAutoSizeColumnLength()>0){
            ExportUtil.processAutoSize((HSSFWorkbook) document, getAutoSizeColumnLength());
        }
        
    }
    
    /**
     * 內文 換行文字過濾
     * @return 
     */
    //要顯示多幣別嗎?!
//    @Override
//    protected int[] getContentColumns(){
//        if(showMultiCurr){
//            if(SheetTypeEnum.BS.getValue().equals(bsisType)){
//                return new int[]{0,1,2,3,4,6,7,8,9,10,11};
//            }else{
//                return new int[]{0,1,2,4,5,6,7};
//            }
//        }
//        return null;
//    }
    
    /**
     * High Light 欄位處理
     * @return 
     */
    @Override
    protected int[] getHighLightColumns(){
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            return new int[]{0,6};
        }else{
            return new int[]{0,4};
        }
    }
    
    /**
     * AutoSize 欄位處理
     * @return 
     */
    private int getAutoSizeColumnLength(){
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            return 14;
        }else{
            return 10;
        }
    }
    
    private void queryDataTable(String sheetType) {
        bsisType = sheetType;
        logger.debug("queryDataTable bsisType:" + bsisType);
        reportVOList = new ArrayList<>();
        
        //顯示多幣別
        //20160321 多幣別顯示 原公司幣別金額 先不提供 拉長戰線
//        if(reCompany.getCurrency() !=null){
//            showMultiCurr = !CurrencyEnum.TWD.name().equals(reCompany.getCurrency().getCode());
//        }
        
        //process hostList company1:host;company2:guest
        //寫入左上host_RE 右下區塊host_PA
        List<ReportBaseVO> hostList = ztfiAfrcTranFacade.findIrsReportData(reCompany, yearmonth, bsisType, true);
        if (CollectionUtils.isEmpty(hostList)) {
            JsfUtil.addWarningMessage("查無資料!");
        }
        
        String hoststComp = reCompany.getCode();
        String lastGuestComp = "";
        IrsReportVO reportVO = new IrsReportVO();
        int hostIndex = 0;
        for(ReportBaseVO hostVO:hostList){
            String repaType = hostVO.getSheetCode();
            if(StringUtils.isNotBlank(lastGuestComp)){
                if(hostVO.getComp2Code().equals(lastGuestComp)){//同一組公司 保持同一個reportVO
                    //處理金額轉台幣 寫入指定會科欄位
                    this.processAccTwdAmount(reportVO, repaType, hostVO.getAccCode(), hostVO.getCurrCode(), 
                            hostVO.getAmount(), bsisType);
                }else{//與前一筆不同guest 
                    reportVOList.add(reportVO);//寫入前一個vo
                    
//                    logger.debug("add new row from hostList:{repaType,hostComp,guestComp}=={" + repaType + "," + hoststComp + "," + hostVO.getComp2Code() + "}");
                    reportVO = new IrsReportVO();
                    reportVO.setSheetType(bsisType);
                    if(AccountTypeEnum.RE.getCode().equals(repaType)){
                        reportVO.setCompCode(hoststComp);
                        reportVO.setCompName(hostVO.getCompName());
                        reportVO.setCompCurrCode(hostVO.getCurrCode());
                        reportVO.setComp2Code(hostVO.getComp2Code());
                        reportVO.setComp2Name(hostVO.getComp2Name());
                    }else{//PA
                        reportVO.setCompCode(hostVO.getComp2Code());
                        reportVO.setCompName(hostVO.getComp2Name());
                        reportVO.setComp2Code(hoststComp);
                        reportVO.setComp2Name(hostVO.getCompName());
                        reportVO.setCompCurrCode(hostVO.getCurrCode());
                    }
                    //處理金額轉台幣 寫入指定會科欄位
                    this.processAccTwdAmount(reportVO, repaType, hostVO.getAccCode(), 
                            hostVO.getCurrCode(), hostVO.getAmount(), bsisType);
                }
            }else{//first row
//                logger.debug("add new row from hostList:{repaType,hostComp,guestComp}=={" + repaType + "," + hoststComp + "," + hostVO.getComp2Code() + "}");
                reportVO = new IrsReportVO();
                reportVO.setSheetType(bsisType);
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    reportVO.setCompCode(hoststComp);
                    reportVO.setCompName(hostVO.getCompName());
                    reportVO.setCompCurrCode(hostVO.getCurrCode());
                    reportVO.setComp2Code(hostVO.getComp2Code());
                    reportVO.setComp2Name(hostVO.getComp2Name());
                }else{//PA
                    reportVO.setCompCode(hostVO.getComp2Code());
                    reportVO.setCompName(hostVO.getComp2Name());
                    reportVO.setComp2Code(hoststComp);
                    reportVO.setComp2Name(hostVO.getCompName());
                    reportVO.setComp2CurrCode(hostVO.getCurrCode());
                }
                
                //處理金額轉台幣 寫入指定會科欄位
                this.processAccTwdAmount(reportVO, repaType, hostVO.getAccCode(), hostVO.getCurrCode(), 
                        hostVO.getAmount(), bsisType);
            }
            
            if(hostList.size() == hostIndex+1){//最後一筆
                reportVOList.add(reportVO);//寫入vo
            }
            lastGuestComp = hostVO.getComp2Code();
            hostIndex++;
        }
        
        
        List<ReportBaseVO> guestList = ztfiAfrcTranFacade.findIrsReportData(reCompany, yearmonth, bsisType, false);
        if (CollectionUtils.isEmpty(guestList)) {
//            logger.debug("export guestList:" + guestList.size());
        }
        //process guestList company1:guest;company2:host
        //寫入右上區塊guest_PA, 左下guest_RE 
//        reportVOList;
//        reportVO = new IrsReportVO();
        for(ReportBaseVO guestVO:guestList){
            String repaType = guestVO.getSheetCode();
            String thisGuestComp = guestVO.getCompCode();
            boolean exist = false;
            for(IrsReportVO existReportVO : reportVOList){
                //寫入右上區塊guest_PA, 左下guest_RE 
                if((AccountTypeEnum.RE.getCode().equals(repaType) && existReportVO.getCompCode().equals(thisGuestComp))
                        || (AccountTypeEnum.PA.getCode().equals(repaType) && existReportVO.getComp2Code().equals(thisGuestComp))){
                    if(AccountTypeEnum.RE.getCode().equals(repaType)){
                        existReportVO.setCompCurrCode(guestVO.getCurrCode());
                    }else{
                        existReportVO.setComp2CurrCode(guestVO.getCurrCode());
                    }
                    //處理金額轉台幣 寫入指定會科欄位
                    this.processAccTwdAmount(existReportVO, repaType, guestVO.getAccCode(), guestVO.getCurrCode(),
                            guestVO.getAmount(), bsisType);
                    exist = true;
                    break;
                }
            }
            
            if(!exist){//add new row
                logger.debug("add new row from guestList:{repaType,hostComp,guestComp}=={" + repaType + "," + hoststComp + "," + thisGuestComp + "}");
                IrsReportVO newReportVO = new IrsReportVO();
                newReportVO.setSheetType(bsisType);
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    newReportVO.setCompCode(thisGuestComp);
                    newReportVO.setCompName(guestVO.getCompName());
                    newReportVO.setCompCurrCode(guestVO.getCurrCode());
                    newReportVO.setComp2Code(hoststComp);
                    newReportVO.setComp2Name(guestVO.getComp2Name());
                }else{//PA
                    newReportVO.setCompCode(hoststComp);
                    newReportVO.setCompName(guestVO.getComp2Name());
                    newReportVO.setComp2Code(thisGuestComp);
                    newReportVO.setComp2Name(guestVO.getCompName());
                    newReportVO.setComp2CurrCode(guestVO.getCurrCode());
                }
                //處理金額轉台幣 寫入指定會科欄位
                this.processAccTwdAmount(newReportVO, repaType, guestVO.getAccCode(), guestVO.getCurrCode(), 
                        guestVO.getAmount(), bsisType);
                
                reportVOList.add(newReportVO);//寫入vo
            }
        }
        
        //重新排序
        if (CollectionUtils.isNotEmpty(reportVOList)) {
            this.sortReportTable();
        }
        
        logger.debug("export end!");
    }
    
    //customer sort method
    private void sortReportTable(){
        Collections.sort(reportVOList, new Comparator<IrsReportVO>() {
            //先以compCode欄位排序, 再以comp2Code欄位排序
            //host 優先
            @Override
            public int compare(IrsReportVO c1, IrsReportVO c2) {
//                    return c1.getCompCode().compareTo(c2.getCompCode());
                int c = c1.getCompCode().compareTo(c2.getCompCode());
                String host = reCompany.getCode();
                if(0 == c){
//                        return c1.getComp2Code().compareTo(c2.getComp2Code());
                    c = c1.getComp2Code().compareTo(c2.getComp2Code());
                    if(0 != c){
                        if(host.equals(c1.getComp2Code())){
                            return 1;
                        }
                        if(host.equals(c2.getComp2Code())){
                            return -1;
                        }
                    }
                    return c;
                }else{
                    if(host.equals(c1.getCompCode())){
                        return -1;
                    }
                    if(host.equals(c2.getCompCode())){
                        return 1;
                    }
                    return c;
                }
            }
        });
    }
    
    //處理金額轉台幣 寫入指定會科欄位
    private IrsReportVO processAccTwdAmount(IrsReportVO vo, String repaType, String accCode, String currCode, 
            BigDecimal amount, String bsisType){
        if(null == amount){
            return vo;
        }
        BigDecimal twdAmount = BigDecimal.ZERO;
        if(!CurrencyEnum.TWD.name().equals(currCode)){
            for(FcMonthlyExchangeRate rateVO : rateList){
                if(rateVO.getCurrency().getCode().equals(currCode)){
                    BigDecimal rate;
                    if(SheetTypeEnum.BS.getValue().equals(bsisType)){
                        rate = rateVO.getRate();
                    }else{
                        rate = rateVO.getAvgRate();
                    }
                    if(rate == null){
                        logger.error("processAccTwdAmount 查無匯率! {currCode, yearmonth}:{" + currCode + "," + yearmonth + "}");
                        return vo;
                    }
                    twdAmount = amount.multiply(rate).setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
                    break;
                }
            }
        }else{
            twdAmount = amount;
        }
        
        //debug用
//        if("6800".equals(vo.getComp2Code())){
//            logger.debug("processAccTwdAmount vo:{accCode,amount,twdAmount}=={" + accCode + "," + amount + "," + twdAmount + "}");
//        }
        
        //寫入指定會科欄位
        //BS IS
        //RE PA
        //accCode
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "ARAP":
                        vo.setAmountReARAP(twdAmount);
                        vo.setAmountReARAP_Ori(amount);
                        break;
                    case "GLBS":
                        vo.setAmountReGLBS(twdAmount);
                        vo.setAmountReGLBS_Ori(amount);
                        break;
                    case "GLOT":
                        vo.setAmountReGLOT(twdAmount);
                        vo.setAmountReGLOT_Ori(amount);
                        break;
                    case "SGLI":
                        vo.setAmountReSGLI(twdAmount);
                        vo.setAmountReSGLI_Ori(amount);
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "ARAP":
                        vo.setAmountPaARAP(twdAmount);
                        vo.setAmountPaARAP_Ori(amount);
                        break;
                    case "GLBS":
                        vo.setAmountPaGLBS(twdAmount);
                        vo.setAmountPaGLBS_Ori(amount);
                        break;
                    case "GLOT":
                        vo.setAmountPaGLOT(twdAmount);
                        vo.setAmountPaGLOT_Ori(amount);
                        break;
                    case "SGLI":
                        vo.setAmountPaSGLI(twdAmount);
                        vo.setAmountPaSGLI_Ori(amount);
                        break;
                    case "ASET":
                        vo.setAmountPaASET(twdAmount);
                        vo.setAmountPaASET_Ori(amount);
                        break;
                }
            }
        }else{//IS
            if(AccountTypeEnum.RE.getCode().equals(repaType)){
                if(null != accCode)switch (accCode) {
                    case "SACO":
                        vo.setAmountReSACO(twdAmount);
                        vo.setAmountReSACO_Ori(amount);
                        break;
                    case "OTHE":
                        vo.setAmountReOTHE(twdAmount);
                        vo.setAmountReOTHE_Ori(amount);
                        break;
                }
            }else{//PA
                if(null != accCode)switch (accCode) {
                    case "SACO":
                        vo.setAmountPaSACO(twdAmount);
                        vo.setAmountPaSACO_Ori(amount);
                        break;
                    case "OTHE":
                        vo.setAmountPaOTHE(twdAmount);
                        vo.setAmountPaOTHE_Ori(amount);
                        break;
                    case "GLPL":
                        vo.setAmountPaGLPL(twdAmount);
                        vo.setAmountPaGLPL_Ori(amount);
                        break;
                }
            }
        }
        return vo;
    }
    
    //停用
    /**
    public void export1() {
	logger.debug("export:" + this.group);
        companyTypeList = companyTypeFacade.findAll(group);
        
        String path = ReportConfig.IRS_REPORT_PATH + "Report_Template_TCC.xlsx";//20160302只有台灣
	InputStream in = this.getClass().getResourceAsStream(path);
        try {
            Workbook workbook = WorkbookFactory.create(in);
            
            List<ReportBaseVO> resultList = ztfiAfrcTranFacade.findIrsReportData(group, yearmonth);
            if (CollectionUtils.isNotEmpty(companyTypeList) 
                    && CollectionUtils.isNotEmpty(resultList)) {
                //寫入公司資訊
                this.processCompany(workbook.getSheetAt(0), resultList);
            }
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
	    workbook.write(out);
            String filename = "IRS_Report_" + yearmonth + ".xlsx";
	    exportFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()),
		    "application/octet-stream", filename);
        } catch (Exception ex) {
	    logger.error("export exception!", ex);
	    JsfUtil.addErrorMessage(ex.getMessage());
	}
    }*/
    
    //寫入公司資訊
    private void processCompany(Sheet sheet, List<ReportBaseVO> resultList){
        int startRow = 5;
        int i = 0;
        Map<String,Integer> accColMap = this.getAccColMap(group);
        // 將公司寫入
        for(IrsCompanyType irsCompanyType : companyTypeList){
            Row row = sheet.getRow(startRow+i);
            if (null == row) {
		row = sheet.createRow(startRow+i);
	    }
            
            writeCell(row, 0, irsCompanyType.getCompany().toString());
	    writeCell(row, 1, irsCompanyType.getType());
            
            for(ReportBaseVO reportVO : resultList){
                if(irsCompanyType.getCompany().getCode().equals(reportVO.getCompCode())){
                    if(reportVO.getAmount()!=null){
                        if(reportVO.getAccCode()==null){
                            writeCell(row, 2, reportVO.getAmount());
                        }else{
                            //TODO 依會科欄位寫入指定column
                            //設計會科代碼與column mapping
                            Integer col = accColMap.get(reportVO.getAccCode());
                            if(col!=null){
                                writeCell(row, col, reportVO.getAmount());
                            }
                        }
                    }
                }
            }
            
            i++;
        }
    }
    
    //會科代碼與column mapping
    private Map<String,Integer> getAccColMap(CompanyGroupEnum group){
        Map<String, Integer> accColMap = new HashMap<>();
        if(CompanyGroupEnum.TCC.equals(group)){
            accColMap.put("1121", 3);
            accColMap.put("1131", 4);
            
            accColMap.put("1140", 6);
            accColMap.put("1143", 7);
            accColMap.put("1150", 8);
            accColMap.put("1153", 9);
        }else{
        }
        
        return accColMap;
    }
    
    private void writeCell(Row row, int col, String str) {
	Cell cell = row.getCell(col);
	if (null == cell) {
	    cell = row.createCell(col);
	}
	cell.setCellType(Cell.CELL_TYPE_STRING);
	cell.setCellValue(str);
    }
    
    private void writeCell(Row row, int col, BigDecimal amount) {
	Cell cell = row.getCell(col);
	if (null == cell) {
	    cell = row.createCell(col);
	}
	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	cell.setCellValue(amount.doubleValue());
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public StreamedContent getExportFile() {
        return exportFile;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public boolean isNoPermission() {
        return noPermission;
    }
    
    public FcCompany getReCompany() {
        return reCompany;
    }

    public void setReCompany(FcCompany reCompany) {
        this.reCompany = reCompany;
    }
    
    public List<SelectItem> getReCompanyOptions() {
        return reCompanyOptions;
    }
    
    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }

    public List<IrsReportVO> getReportVOList() {
        return reportVOList;
    }

    public String getBsisType() {
        return bsisType;
    }

    //</editor-fold>
    public boolean isShowMultiCurr() {
        return showMultiCurr;
    }
    
}
