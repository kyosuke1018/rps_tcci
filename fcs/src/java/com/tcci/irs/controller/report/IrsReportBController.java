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
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.entity.FcUploaderR;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.facade.service.ReportService;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.enums.SheetTypeEnum;
import com.tcci.irs.facade.sheetdata.IrsCompanyCloseFacade;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import com.tcci.rpt.entity.RptCompanyOrg;
import com.tcci.rpt.facade.RptCompanyOrgFacade;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "irsReportB")
@ViewScoped
public class IrsReportBController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IrsReportBController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @EJB
    protected IrsCompanyCloseFacade irsCompanyCloseFacade;
    @EJB
    private ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    @EJB
    protected FcCompanyFacade fcCompanyFacade; 
    @EJB
    protected IrsSheetdataMFacade irsSheetdataMFacade;
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    @EJB
    private RptCompanyOrgFacade rptCompanyOrgFacade;
    @EJB
    private ReportService reportService;
    //</editor-fold>

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
    
    private List<IrsReportBVO> reportVOList;
    private List<FcMonthlyExchangeRate> rateList;
    private String bsisType;
    private boolean consolidation;
//    private boolean showMultiCurr = false;//顯示多幣別
    //勘誤 對應會科不符
    private List<ZtfiAfrcTran> tranDetailList;
    private List<ZtfiAfrcTran> consolidationCheckList;
    private List<ZtfiAfrcTran> nonConsolidationCheckList;
    private List<ZtfiAfrcTran> level1CheckList;
    //RptCompanyOrg
    private List<SelectItem> level1s;
    private String level1;
    private boolean multiSheet;//台泥1000XX報表 使用多sheet向下展示明細(明細sheet 為該報表公司之非合併)
    private String toCurrency;
    
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
        List<String> ymList = irsSheetdataMFacade.findDataYMList();//依data產生YM選單
        ymOptions = ListUtils.getOptions(ymList);
        yearmonth = irsCompanyCloseFacade.findByGroup(null).getYearMonth();//20151223改為統一只有一組跨集團的設定
        level1s = this.buildVirtualLevel1();
        if(!level1s.isEmpty()){
            level1 = (String)level1s.get(0).getValue();
        }
        
        this.fetchRateList();
        this.queryCheckList();//起始查詢 顯示勘誤筆數
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
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
//        reportVOList = new ArrayList<>();
        this.queryCheckList();
        level1s = this.buildVirtualLevel1();
//        JsfUtil.resetDataTable("qryForm:dataList");
    }
    
    public void changeYM(){
        logger.debug("changeYM:" + this.yearmonth);
        this.fetchRateList();
        this.queryCheckList();
    }
    
    public void changeLevel1() {
        logger.debug("changeLevel1:" + this.level1);
        if(this.level1 != null){
            RptCompanyOrg org = rptCompanyOrgFacade.findByCompany(this.group, this.level1);
            //起始查詢 顯示勘誤筆數
            level1CheckList = ztfiAfrcTranFacade.findIrsReportCheckList(group, yearmonth, org);
        }
        
        this.toCurrency = CurrencyEnum.TWD.name();//20160817 增加港幣轉換
    }
    
    //20160818 增加指定幣別金額轉換
    public void changeLevel1Rate() {
        logger.debug("changeLevel1Rate:" + this.toCurrency);
    }
    
    private void fetchRateList(){
        rateList = fcMonthlyExchangeRateFacade.findByYMAndToCurrencyCode(yearmonth, CurrencyEnum.TWD.name());
    }
    
    public void export(String sheetType, boolean consolidation) {
        this.queryDataTable(sheetType, consolidation);
    }
    
    //第一階 虛擬公司 合併/非合併
    //20160818 增加指定幣別金額轉換
    public void exportLevel1(String sheetType, boolean consolidation) {
        logger.debug("exportLevel1...:"+level1);
        logger.debug("exportLevel1...:"+yearmonth);
        bsisType = sheetType;
        this.multiSheet = false;
        logger.debug("exportLevel1 bsisType:" + bsisType);
        reportVOList = new ArrayList<>();
        tranDetailList = new ArrayList<>();
        
        RptCompanyOrg org = rptCompanyOrgFacade.findByCompany(group, level1);
        List<ReportBaseVO> resultList;
        if(consolidation){
            resultList = ztfiAfrcTranFacade.findIrsReportBDetail(group, yearmonth, bsisType, true, org);//合併:內部
        }else{
            resultList = ztfiAfrcTranFacade.findIrsReportBDetail(group, yearmonth, bsisType, false, org);//非合併:外部
        }
        if (CollectionUtils.isEmpty(resultList)) {
            JsfUtil.addWarningMessage("查無資料!");
            logger.warn("exportLevel1...查無資料!");
            return;
        }
        //20160818 增加指定幣別金額轉換
        logger.debug("exportLevel1...toCurrency:"+toCurrency);
        List<FcMonthlyExchangeRate> toCurrencyRateList;
        if(CurrencyEnum.TWD.name().equals(toCurrency)){
            toCurrencyRateList = rateList;
        }else{
            toCurrencyRateList = fcMonthlyExchangeRateFacade.findByYMAndToCurrencyCode(yearmonth, toCurrency);
        }
        if (CollectionUtils.isEmpty(toCurrencyRateList)) {
            JsfUtil.addWarningMessage("查無該年月之幣別匯率!");
            logger.warn("exportLevel1...查無該年月之幣別匯率!");
            return;
        }
        
        reportVOList = processDetailReportVO(resultList, toCurrency, toCurrencyRateList);//20160818 增加指定幣別金額轉換
    }
    
    //20160803
    //台泥關係人彙總表(收支、資產負債) 
    //只抓台泥上傳金額
    //對象公司 關係人公司關係設定(companyType)
    public void exportB1(String sheetType) {
        this.queryDataTable(sheetType, consolidation);
        bsisType = sheetType;
        this.consolidation = false;
        logger.debug("exportB1 bsisType:" + bsisType);
        reportVOList = new ArrayList<>();
        tranDetailList = new ArrayList<>();
        this.multiSheet = false;
        
        List<ReportBaseVO> resultList = ztfiAfrcTranFacade.findIrsReportB1Data(group, yearmonth, bsisType);
        if (CollectionUtils.isEmpty(resultList)) {
            JsfUtil.addWarningMessage("查無資料!");
            logger.warn("exportB1...查無資料!");
            return;
        }
        //process ReportBaseVO list to reportVOList
        for(ReportBaseVO baseVO:resultList){
            String guestComp = baseVO.getComp2Code();//只需比對comp2
            String repaType = baseVO.getSheetCode();
            boolean exist = false;
            
            for(IrsReportBVO reportVO:reportVOList){
                if(guestComp.equals(reportVO.getComp2Code())){
                    exist = true;
                    //處理金額轉台幣 寫入指定會科欄位
                    reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, rateList);
                    break;
                }
            }
            
            if(!exist){
                IrsReportBVO reportVO = new IrsReportBVO();
                reportVO.setSheetType(bsisType);
                reportVO.setCompCode(baseVO.getCompCode());
                reportVO.setCompCurrCode(baseVO.getCurrCode());
                reportVO.setComp2Code(guestComp);
                reportVO.setComp2Name(baseVO.getComp2Name());
                reportVO.setComp2Rtype(baseVO.getAbbreviation());
                //處理金額轉台幣 寫入指定會科欄位
                reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, rateList);
                reportVOList.add(reportVO);//寫入vo
            }
        }
        
        //重新排序
        if (CollectionUtils.isNotEmpty(reportVOList)) {
            this.sortReportTable(reportVOList);
        }
        
        //FOOTER TOTAL
//        this.footerTotal(reportVOList);
        //dataExport 不支援footer 寫入一筆
//        this.insertFooter(reportVOList);
        //改在ReportService 實作(initFooterMap==>footerTotal==>insertFooter)
        reportService.insertFooter(reportVOList, bsisType);
        
        logger.debug("exportB1 end!");
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
//        if (null!=getContentColumns() && getContentColumns().length>0){
//            ExportUtil.processContentFields((HSSFWorkbook) document, getContentColumns());
//        }

        // High Light 處理 (放在 postProcessXLS 最後一步)
        if (null!=getHighLightColumns() && getHighLightColumns().length>0){
            ExportUtil.highLightFields((HSSFWorkbook) document, getHighLightColumns(), 1);
        }
        
        if(getAutoSizeColumnLength()>0){
            ExportUtil.processAutoSize((HSSFWorkbook) document, getAutoSizeColumnLength());
        }
        
        //for合併公司各自報表需求 找出第一層(25家)中虛擬的 合併公司 ==> 7家
        if(this.multiSheet && this.consolidation){
            if(SheetTypeEnum.BS.getValue().equals(bsisType)
                    || SheetTypeEnum.IS.getValue().equals(bsisType)){
                List<RptCompanyOrg> detailList = rptCompanyOrgFacade.findVirtualCompany(group, 1);
                int sheetIndex = 1;
                for(RptCompanyOrg org : detailList){
                    //處理detail sheet
                    boolean result = this.postProcessXLSDetail((HSSFWorkbook) document, org);
                    if(result){//有資料產生sheet, 才對sheet style進行處理
                        // High Light 處理
                        if (null!=getHighLightColumns() && getHighLightColumns().length>0){
                            ExportUtil.highLightFields((HSSFWorkbook) document, getHighLightColumns(), 1, sheetIndex);
                        }
                        sheetIndex++;
                    }
                }
            }
        }
    }
    public void postProcessXLS_B1(Object document) {
        logger.debug("postProcessXLS_B1 ...");
        
        // High Light 處理 (放在 postProcessXLS 最後一步)
        ExportUtil.highLightFields((HSSFWorkbook) document, new int[]{0,1}, 1);//指定highlight欄位
        
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
            return new int[]{0,17};
        }else if(SheetTypeEnum.IS.getValue().equals(bsisType)){
            return new int[]{0,18};
        }else{
            return new int[]{12};
        }
    }
    
    /**
     * AutoSize 欄位處理
     * @return 
     */
    private int getAutoSizeColumnLength(){
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            return 31;
        }else if(SheetTypeEnum.IS.getValue().equals(bsisType)){
            return 26;
        }else{
            return 24;
        }
    }
    
    public void exportCheck(boolean consolidation) {
        if(consolidation){
            tranDetailList = consolidationCheckList;
        }else{
//            tranDetailList = nonConsolidationCheckList;//20160815非合併聯署 停用
        }
    }
    
    public void exportCheckLevel1() {
        tranDetailList = level1CheckList;
    }
    
    //<editor-fold defaultstate="collapsed" desc="private">
    private void queryDataTable(String sheetType, boolean consolidation) {
        bsisType = sheetType;
        this.consolidation = consolidation;
        logger.debug("queryDataTable bsisType:" + bsisType);
        reportVOList = new ArrayList<>();
        tranDetailList = new ArrayList<>();
        this.multiSheet = true;
        
        //顯示多幣別
        //20160321 多幣別顯示 原公司幣別金額 先不提供 拉長戰線
//        if(reCompany.getCurrency() !=null){
//            showMultiCurr = !CurrencyEnum.TWD.name().equals(reCompany.getCurrency().getCode());
//        }
        
        List<ReportBaseVO> resultList = ztfiAfrcTranFacade.findIrsReportBData(group, yearmonth, bsisType, this.consolidation);
        if (CollectionUtils.isEmpty(resultList)) {
            JsfUtil.addWarningMessage("查無資料!");
            return;
        }
        //process ReportBaseVO list to reportVOList
        for(ReportBaseVO baseVO:resultList){
            String hostComp = baseVO.getCompCode();
            String guestComp = baseVO.getComp2Code();
            String repaType = baseVO.getSheetCode();
            boolean exist = false;
            
            //hostComp, guestComp CompCode要替換成合併後的公司代碼
            //排除內部交易 hostComp = guestComp ex:5200 <==> 1H00
            if(hostComp.equals(guestComp)){
                continue;
            }
            
            for(IrsReportBVO reportVO:reportVOList){
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    if(hostComp.equals(reportVO.getCompCode()) && guestComp.equals(reportVO.getComp2Code())){
                        exist = true;
                        //處理金額轉台幣 寫入指定會科欄位
//                        this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(),
//                                baseVO.getAmount(), bsisType);
                        //改在ReportService 實作
                        reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, rateList);
                        break;
                    }
                }else{
                    if(hostComp.equals(reportVO.getComp2Code()) && guestComp.equals(reportVO.getCompCode())){
                        exist = true;
                        //處理金額轉台幣 寫入指定會科欄位
//                        this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(),
//                                baseVO.getAmount(), bsisType);
                        //改在ReportService 實作
                        reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, rateList);
                        break;
                    }
                }
            }
            
            if(!exist){
                IrsReportBVO reportVO = new IrsReportBVO();
                reportVO.setSheetType(bsisType);
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    reportVO.setCompCode(hostComp);
                    reportVO.setCompName(baseVO.getCompName());
                    reportVO.setCompCurrCode(baseVO.getCurrCode());
                    reportVO.setComp2Code(guestComp);
                    reportVO.setComp2Name(baseVO.getComp2Name());
                }else{//PA
                    reportVO.setCompCode(guestComp);
                    reportVO.setCompName(baseVO.getComp2Name());
                    reportVO.setComp2Code(hostComp);
                    reportVO.setComp2Name(baseVO.getCompName());
                    reportVO.setCompCurrCode(baseVO.getCurrCode());
                }
                //處理金額轉台幣 寫入指定會科欄位
//                this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(),
//                        baseVO.getCurrCode(), baseVO.getAmount(), bsisType);
                //改在ReportService 實作
                reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, rateList);
                reportVOList.add(reportVO);//寫入vo
            }
        }
        
        //重新排序
        if (CollectionUtils.isNotEmpty(reportVOList)) {
            this.sortReportTable(reportVOList);
        }
        
        //FOOTER TOTAL
//        this.footerTotal(reportVOList);
        //dataExport 不支援footer 寫入一筆
//        this.insertFooter(reportVOList);
        //改在ReportService 實作(initFooterMap==>footerTotal==>insertFooter)
        reportService.insertFooter(reportVOList, bsisType);
        
        logger.debug("export end!");
    }
    
    //customer sort method
    private void sortReportTable(List<IrsReportBVO> list){
        Collections.sort(list, new Comparator<IrsReportBVO>() {
            //先以compCode欄位排序, 再以comp2Code欄位排序
            @Override
            public int compare(IrsReportBVO c1, IrsReportBVO c2) {
//                    return c1.getCompCode().compareTo(c2.getCompCode());
                int c = c1.getCompCode().compareTo(c2.getCompCode());
                if(0 == c){
                        return c1.getComp2Code().compareTo(c2.getComp2Code());
                }else{
                    return c;
                }
            }
        });
    }
    
    private void queryCheckList(){
        logger.debug("queryCheckList");
        bsisType = null;
        reportVOList = new ArrayList<>();
        
        consolidationCheckList = ztfiAfrcTranFacade.findIrsReportCheckList(group, yearmonth, true);
//        nonConsolidationCheckList = ztfiAfrcTranFacade.findIrsReportCheckList(group, yearmonth, false);//20160815非合併聯署 停用
//        if (CollectionUtils.isEmpty(tranDetailList)) {
//            JsfUtil.addWarningMessage("查無資料!");
//            return;
//        }
    }
    
    //處理detail sheet
    private boolean postProcessXLSDetail(HSSFWorkbook wb, RptCompanyOrg org) {
        logger.debug("postProcessXLSDetail start:"+org.getCode());
        
        List<ReportBaseVO> resultList = ztfiAfrcTranFacade.findIrsReportBDetail(group, yearmonth, bsisType, false, org);
        if (CollectionUtils.isEmpty(resultList)) {
//            JsfUtil.addWarningMessage("查無資料!");
            return false;
        }
        
        List<IrsReportBVO> detailList = processDetailReportVO(resultList, CurrencyEnum.TWD.name(), rateList);
        HSSFSheet dSheet = wb.createSheet(org.getCode());//2B00XX...
        
        //headerRow
        Row headerRow = dSheet.createRow(0);
        String[] strTitle = dTitleLabel();//表頭
        int cols = 0;
        for (String title : strTitle) {
            headerRow.createCell(cols).setCellValue(title);
            cols++;
        }
        //contentRow
        int contentRowCount = 1;
        for(IrsReportBVO detail : detailList){
            Row detailTitleRow = dSheet.createRow(contentRowCount);
            
            if(SheetTypeEnum.BS.getValue().equals(bsisType)){
                //RE
                detailTitleRow.createCell(0).setCellValue(detail.getCompCode()+detail.getCompName());
                detailTitleRow.createCell(1).setCellValue(detail.getAmountReA()!=null?detail.getAmountReA().toString():"");
                detailTitleRow.createCell(2).setCellValue(detail.getAmountReB()!=null?detail.getAmountReB().toString():"");
                detailTitleRow.createCell(3).setCellValue(detail.getAmountReC()!=null?detail.getAmountReC().toString():"");
                detailTitleRow.createCell(4).setCellValue(detail.getAmountReD()!=null?detail.getAmountReD().toString():"");
                detailTitleRow.createCell(5).setCellValue(detail.getAmountReE()!=null?detail.getAmountReE().toString():"");
                detailTitleRow.createCell(6).setCellValue(detail.getAmountReF()!=null?detail.getAmountReF().toString():"");
                detailTitleRow.createCell(7).setCellValue(detail.getAmountReG()!=null?detail.getAmountReG().toString():"");
                detailTitleRow.createCell(8).setCellValue(detail.getAmountReH()!=null?detail.getAmountReH().toString():"");
                detailTitleRow.createCell(9).setCellValue(detail.getAmountReI()!=null?detail.getAmountReI().toString():"");
                detailTitleRow.createCell(10).setCellValue(detail.getAmountReJ()!=null?detail.getAmountReJ().toString():"");
                detailTitleRow.createCell(11).setCellValue(detail.getAmountReK()!=null?detail.getAmountReK().toString():"");
                detailTitleRow.createCell(12).setCellValue(detail.getAmountReL()!=null?detail.getAmountReL().toString():"");
                detailTitleRow.createCell(13).setCellValue(detail.getAmountReM()!=null?detail.getAmountReM().toString():"");
                detailTitleRow.createCell(14).setCellValue(detail.getAmountReN()!=null?detail.getAmountReN().toString():"");
                detailTitleRow.createCell(15).setCellValue(detail.getAmountReO()!=null?detail.getAmountReO().toString():"");
                detailTitleRow.createCell(16).setCellValue(detail.getTotalRe().toString());
                //PA
                detailTitleRow.createCell(17).setCellValue(detail.getComp2Code()+detail.getComp2Name());
                detailTitleRow.createCell(18).setCellValue(detail.getAmountPaA()!=null?detail.getAmountPaA().toString():"");
                detailTitleRow.createCell(19).setCellValue(detail.getAmountPaB()!=null?detail.getAmountPaB().toString():"");
                detailTitleRow.createCell(20).setCellValue(detail.getAmountPaC()!=null?detail.getAmountPaC().toString():"");
                detailTitleRow.createCell(21).setCellValue(detail.getAmountPaD()!=null?detail.getAmountPaD().toString():"");
                detailTitleRow.createCell(22).setCellValue(detail.getAmountPaE()!=null?detail.getAmountPaE().toString():"");
                detailTitleRow.createCell(23).setCellValue(detail.getAmountPaF()!=null?detail.getAmountPaF().toString():"");
                detailTitleRow.createCell(24).setCellValue(detail.getAmountPaG()!=null?detail.getAmountPaG().toString():"");
                detailTitleRow.createCell(25).setCellValue(detail.getAmountPaH()!=null?detail.getAmountPaH().toString():"");
                detailTitleRow.createCell(26).setCellValue(detail.getAmountPaI()!=null?detail.getAmountPaI().toString():"");
                detailTitleRow.createCell(27).setCellValue(detail.getAmountPaJ()!=null?detail.getAmountPaJ().toString():"");
                detailTitleRow.createCell(28).setCellValue(detail.getAmountPaK()!=null?detail.getAmountPaK().toString():"");
                detailTitleRow.createCell(29).setCellValue(detail.getTotalPa().toString());
                detailTitleRow.createCell(30).setCellValue(detail.getDiff().toString());
            }else if(SheetTypeEnum.IS.getValue().equals(bsisType)){
                //RE
                detailTitleRow.createCell(0).setCellValue(detail.getCompCode()+detail.getCompName());
                detailTitleRow.createCell(1).setCellValue(detail.getAmountReA()!=null?detail.getAmountReA().toString():"");
                detailTitleRow.createCell(2).setCellValue(detail.getAmountReB()!=null?detail.getAmountReB().toString():"");
                detailTitleRow.createCell(3).setCellValue(detail.getAmountReC()!=null?detail.getAmountReC().toString():"");
                detailTitleRow.createCell(4).setCellValue(detail.getAmountReD()!=null?detail.getAmountReD().toString():"");
                detailTitleRow.createCell(5).setCellValue(detail.getAmountReE()!=null?detail.getAmountReE().toString():"");
                detailTitleRow.createCell(6).setCellValue(detail.getAmountReF()!=null?detail.getAmountReF().toString():"");
                detailTitleRow.createCell(7).setCellValue(detail.getAmountReG()!=null?detail.getAmountReG().toString():"");
                detailTitleRow.createCell(8).setCellValue(detail.getAmountReH()!=null?detail.getAmountReH().toString():"");
                detailTitleRow.createCell(9).setCellValue(detail.getAmountReI()!=null?detail.getAmountReI().toString():"");
                detailTitleRow.createCell(10).setCellValue(detail.getAmountReJ()!=null?detail.getAmountReJ().toString():"");
                detailTitleRow.createCell(11).setCellValue(detail.getAmountReK()!=null?detail.getAmountReK().toString():"");
                detailTitleRow.createCell(12).setCellValue(detail.getAmountReL()!=null?detail.getAmountReL().toString():"");
                detailTitleRow.createCell(13).setCellValue(detail.getAmountReM()!=null?detail.getAmountReM().toString():"");
                detailTitleRow.createCell(14).setCellValue(detail.getAmountReN()!=null?detail.getAmountReN().toString():"");
                detailTitleRow.createCell(15).setCellValue(detail.getAmountReO()!=null?detail.getAmountReO().toString():"");
                detailTitleRow.createCell(16).setCellValue(detail.getAmountReP()!=null?detail.getAmountReP().toString():"");
                detailTitleRow.createCell(17).setCellValue(detail.getTotalRe().toString());
                //PA
                detailTitleRow.createCell(18).setCellValue(detail.getComp2Code()+detail.getComp2Name());
                detailTitleRow.createCell(19).setCellValue(detail.getAmountPaA()!=null?detail.getAmountPaA().toString():"");
                detailTitleRow.createCell(20).setCellValue(detail.getAmountPaB()!=null?detail.getAmountPaB().toString():"");
                detailTitleRow.createCell(21).setCellValue(detail.getAmountPaC()!=null?detail.getAmountPaC().toString():"");
                detailTitleRow.createCell(22).setCellValue(detail.getAmountPaD()!=null?detail.getAmountPaD().toString():"");
                detailTitleRow.createCell(23).setCellValue(detail.getAmountPaE()!=null?detail.getAmountPaE().toString():"");
                detailTitleRow.createCell(24).setCellValue(detail.getTotalPa().toString());
                detailTitleRow.createCell(25).setCellValue(detail.getDiff().toString());
            }
            contentRowCount++;
        }
        
//        logger.debug("postProcessXLSDetail after processHeader...");
        logger.debug("postProcessXLSDetail end:"+org.getCode());
        return true;
    }
    
    //20160818 增加指定幣別金額轉換
    private List<IrsReportBVO> processDetailReportVO(List<ReportBaseVO> reportBaseList, String toCurrencyCode, List<FcMonthlyExchangeRate> toCurrencyRateList){
        List<IrsReportBVO> detailReportVOList = new ArrayList<>();
        //process ReportBaseVO list to reportVOList
        for(ReportBaseVO baseVO:reportBaseList){
            String hostComp = baseVO.getCompCode();
            String guestComp = baseVO.getComp2Code();
            String repaType = baseVO.getSheetCode();
            boolean exist = false;
            
            for(IrsReportBVO reportVO:detailReportVOList){
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    if(hostComp.equals(reportVO.getCompCode()) && guestComp.equals(reportVO.getComp2Code())){
                        exist = true;
                        //處理金額轉台幣 寫入指定會科欄位
//                        this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(),
//                                baseVO.getAmount(), bsisType);
                        //改在ReportService 實作
                        reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, toCurrencyCode, toCurrencyRateList);
                        break;
                    }
                }else{
                    if(hostComp.equals(reportVO.getComp2Code()) && guestComp.equals(reportVO.getCompCode())){
                        exist = true;
                        //處理金額轉台幣 寫入指定會科欄位
//                        this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(),
//                                baseVO.getAmount(), bsisType);
                        //改在ReportService 實作
                        reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, toCurrencyCode, toCurrencyRateList);
                        break;
                    }
                }
            }
            
            if(!exist){
                IrsReportBVO reportVO = new IrsReportBVO();
                reportVO.setSheetType(bsisType);
                if(AccountTypeEnum.RE.getCode().equals(repaType)){
                    reportVO.setCompCode(hostComp);
                    reportVO.setCompName(baseVO.getCompName());
                    reportVO.setCompCurrCode(baseVO.getCurrCode());
                    reportVO.setComp2Code(guestComp);
                    reportVO.setComp2Name(baseVO.getComp2Name());
                }else{//PA
                    reportVO.setCompCode(guestComp);
                    reportVO.setCompName(baseVO.getComp2Name());
                    reportVO.setComp2Code(hostComp);
                    reportVO.setComp2Name(baseVO.getCompName());
                    reportVO.setCompCurrCode(baseVO.getCurrCode());
                }
                //處理金額轉台幣 寫入指定會科欄位
//                this.processAccTwdAmount(reportVO, repaType, baseVO.getAccCode(),
//                        baseVO.getCurrCode(), baseVO.getAmount(), bsisType);
                //改在ReportService 實作
                reportService.processReportBVO(reportVO, repaType, baseVO.getAccCode(), baseVO.getCurrCode(), baseVO.getAmount(), bsisType, yearmonth, toCurrencyCode, toCurrencyRateList);
                detailReportVOList.add(reportVO);//寫入vo
            }
        }
        
        //重新排序
        if (CollectionUtils.isNotEmpty(detailReportVOList)) {
            this.sortReportTable(detailReportVOList);
        }
        
        //FOOTER TOTAL
//        this.footerTotal(detailReportVOList);
        //dataExport 不支援footer 寫入一筆
//        this.insertFooter(detailReportVOList);
        //改在ReportService 實作(initFooterMap==>footerTotal==>insertFooter)
        reportService.insertFooter(detailReportVOList, bsisType);
        
        logger.debug("processDetailReportVO end!");
        return detailReportVOList;
    }
    
    private String[] dTitleLabel(){
        String[] cols = new String[]{};
        if(SheetTypeEnum.BS.getValue().equals(bsisType)){
            cols = ReportService.BS_REPA_COLS_HEADER;
        }else if(SheetTypeEnum.IS.getValue().equals(bsisType)){
            cols = ReportService.IS_REPA_COLS_HEADER;
        }
        return cols;
    }
    
    //找出第一層(25家)中虛擬的 合併公司 ==> 7家
    //20160817 只出2B00XX ?!
    private List<SelectItem> buildVirtualLevel1() {
        List<SelectItem> options = new ArrayList();
        List<RptCompanyOrg> result = rptCompanyOrgFacade.findVirtualCompany(group, 1);
        
        options.add(new SelectItem(null, "----"));//default
        if (CollectionUtils.isNotEmpty(result)) {
            for (RptCompanyOrg item : result) {
                logger.debug("buildVirtualLevel1 LoginAccount :"+userSession.getAdaccount());
                //powerUsers有所有報表公司權限
                if(this.isPowerUsers){
                    options.add(new SelectItem(item.getCode(), item.getCode()));
                }else{
                    List<FcUploaderR> uploaderList = item.getCompany().getUploaderList();
                    if (uploaderList != null && !uploaderList.isEmpty()) {
                        for(FcUploaderR uploader : uploaderList){
                            if(uploader.getTcUser().getLoginAccount().equals(userSession.getAdaccount())){
                                options.add(new SelectItem(item.getCode(), item.getCode()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return options;
    }
    //</editor-fold>
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

    public boolean isIsPowerUsers() {
        return isPowerUsers;
    }
    
    public List<SelectItem> getYmOptions() {
        return ymOptions;
    }

    public List<IrsReportBVO> getReportVOList() {
        return reportVOList;
    }

    public String getBsisType() {
        return bsisType;
    }

//    public boolean isShowMultiCurr() {
//        return showMultiCurr;
//    }

    public List<ZtfiAfrcTran> getTranDetailList() {
        return tranDetailList;
    }
    
    public int getCheckCount() {
        if (CollectionUtils.isEmpty(consolidationCheckList)) {
            return 0;
        }
        return consolidationCheckList.size();
    }
    public int getCheckCount2() {
        if (CollectionUtils.isEmpty(nonConsolidationCheckList)) {
            return 0;
        }
        return nonConsolidationCheckList.size();
    }
    public int getLevel1CheckCount() {
        if (CollectionUtils.isEmpty(level1CheckList)) {
            return 0;
        }
        return level1CheckList.size();
    }
    
    public List<SelectItem> getLevel1s() {
        return level1s;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
    //</editor-fold>
    
}