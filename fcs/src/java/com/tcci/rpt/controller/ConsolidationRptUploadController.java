/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.attachment.AttachmentEventListener;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.facade.service.ReportConfigService;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.fcs.util.ExcelAccountItem;
import com.tcci.fcs.util.ExcelValidator;
import com.tcci.fcs.util.ExcelValueItem;
import com.tcci.rpt.entity.RptDValue;
import com.tcci.rpt.entity.RptSheetUpload;
import com.tcci.rpt.entity.ZtfiAfcsCsbu;
import com.tcci.rpt.facade.RptDValueFacade;
import com.tcci.rpt.facade.RptSheetUploadFacade;
import com.tcci.rpt.facade.RptTbValueFacade;
import com.tcci.rpt.facade.ZtfiAfcsCsbuFacade;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name="consolidationRptUpload")
@ViewScoped
public class ConsolidationRptUploadController implements AttachmentEventListener {
    private final static Logger logger = LoggerFactory.getLogger(ConsolidationRptUploadController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    public UserSession getUserSession() {
        return userSession;
    }
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    public AttachmentController getAttachmentController() {
        return attachmentController;
    }

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }
    
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private RptSheetUploadFacade rptSheetUploadFacade;
    @EJB
    private ReportConfigService reportConfigService;
    @EJB
    private RptDValueFacade valueFacade;
    @EJB
    private RptTbValueFacade tbValueFacade;
    @EJB
    private ZtfiAfcsCsbuFacade ztfiAfcsCsbuFacade;
    //</editor-fold>
    
    private String yearmonth;
    private FcCompany company;
    private CompanyGroupEnum group;
    private RptSheetUpload rptSheetUpload;
    private boolean allowSave = false;
    private Workbook workbook;
    private ExcelValidator validator;
    private List<ExcelValueItem> excelValueList = new ArrayList<>();
    private Map<String, FcCompany> dValidCompanys = new HashMap<>();//D大類報表 驗證公司清單
    private BigDecimal rate;
    private String currencyRate = "1.0";
    private FcCurrency currency;
    private boolean showAmountXls = false;
    // key: sheetCode:compCode:acCode
    private Map<String, RptDValue> excelValues = new HashMap<>();
    // sheetCode, compList(含SUM)
    private Map<String, List<FcCompany>> tabCompanys = new HashMap<>();
    private Map<String, BigDecimal> codeSums = new HashMap<>(); // 加總
    private Map<String, BigDecimal> codeXlsSums = new HashMap<>(); //原幣別 上傳金額 加總
    private static FcCompany SUM_COMPANY = new FcCompany("SUM", "總計");
    private List<ReportBaseVO> tabAccounts = new ArrayList<>();
    private String zbukto;
    
    @PostConstruct
    private void init() {
        String code = JsfUtil.getRequestParameter("code");
        try {
            if (code != null) {
                company = companyFacade.findByCode(code);
            }
//            if(null==company || null==company.getGroup()){
//                JsfUtil.addErrorMessage("無此公司上傳權限!");
//                return;
//            }
            yearmonth = configFacade.findValue(FcConfigKeyEnum.RPT_OPEN_YM, CompanyGroupEnum.getFromCode(company.getGroup().getCode()));
            if (null==yearmonth || null==company) {
                JsfUtil.addErrorMessage("參數有誤!");
                return;
            }
            if (!company.isConsolidationRptUpload()) {
                JsfUtil.addErrorMessage("非合併報表上傳公司 不得進行操作!");
                return;
            }
            //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
            /**
            if(null!=company && null!=company.getCurrency()){
                this.currency = company.getCurrency();
                //來源幣別不等於目標幣別
                if(!company.getGroup().getCurrency().getCode().equals(this.currency.getCode())){
                    boolean transUSD = false;
                    for (String compCode : GlobalConstant.TRANS_USD_COMP_CODE) {
                        if(company.getCode().equals(compCode)){//上傳公司為原中橡BVI底下四家之一
                            transUSD = true;
                            break;
                        }
                    }
                    //20160203 資產負債 使用期末匯率
                    if(transUSD){//特別兩段匯率處理 
                        FcMonthlyExchangeRate monthlyExchangeRate1  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, this.currency.getCode(), GlobalConstant.CURR_USD);
                        FcMonthlyExchangeRate monthlyExchangeRate2  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, GlobalConstant.CURR_USD, company.getGroup().getCurrency().getCode());
                        if(null!=monthlyExchangeRate1 && null!=monthlyExchangeRate2){
                            currencyRate = monthlyExchangeRate1.getRate().multiply(
                                    monthlyExchangeRate2.getRate()
                            ).setScale(GlobalConstant.AMOUNT_SCALE_6, RoundingMode.HALF_UP).toString();//小數位數放大可某種程度減低匯差
                        }else{
                            JsfUtil.addErrorMessage("此公司幣別("+ this.currency.getCode() +") 未維護匯率!");
                            return;
                        }
                    }else{
                        FcMonthlyExchangeRate monthlyExchangeRate  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, this.currency, company.getGroup().getCurrency());
                        if(null!=monthlyExchangeRate){
                            currencyRate = monthlyExchangeRate.getRate().toString();
                        }else{
                            JsfUtil.addErrorMessage("此公司幣別("+ this.currency.getCode() +") 未維護匯率!");
                            return;
                        }
                    }
                    showAmountXls = true;
                }
            }
            */
            if(null!=company && null!=company.getCurrency()){
                this.currency = company.getCurrency();
            }
            
            //20160317 isConsolidationRptUpload ==> 要上傳TB
            if(this.company.isConsolidationRptUpload()){ //有要上傳TB才檢查
                //find zbukto by ZTFI_AFCS_CSBU
                ZtfiAfcsCsbu ztfiAfcsCsbu = ztfiAfcsCsbuFacade.findByCompany(company);
                if(ztfiAfcsCsbu != null){
                    zbukto = ztfiAfcsCsbu.getZbukto();
                    logger.debug("zbukto:{}", zbukto);
                }else{
                    JsfUtil.addErrorMessage("(ZTFI_AFCS_CSBU)查無目標合併公司代碼!");
                    return;
                }
            }
        } catch (Exception ex) {
        }
        group = CompanyGroupEnum.getFromCode(company.getGroup().getCode());
        
        // 檢查權限
        boolean isAdmin = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
        boolean isCompanyUploader = companyFacade.isUploader(userSession.getTcUser(), company);
	if (!isAdmin && !isCompanyUploader) {
	    JsfUtil.addErrorMessage("無此公司上傳權限!");
	    return;
	}
        
        //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
        /**
        List<FcCompany> companys = companyFacade.findAllActiveByGroup(group, true, false);//此處報表檢核對象為 合併營收定義26家
        for (FcCompany co : companys) {
            dValidCompanys.put(co.getCode(), co);
        }
        */
        rptSheetUpload = rptSheetUploadFacade.findByYearmonthCompany(yearmonth, company);
        if (null==rptSheetUpload) {
            rptSheetUpload = new RptSheetUpload();
        } else {
            this.initRptUploadData();
        }
        
        boolean stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.RPT_STOPUPLOAD, false, group);
        attachmentController.init(rptSheetUpload, stopUpload);
        attachmentController.setEventListener(this);
        if (stopUpload) {
            JsfUtil.addErrorMessage("關帳中(停止報表上傳)");
        }
    }
    
    private void initRptUploadData() {
        //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
        /**
        List<RptDValue> list = valueFacade.findReportValue(rptSheetUpload);
        for (RptDValue rv : list) {
            String key = rv.getSheet() + ":" + rv.getCoid2().getCode() + ":" + rv.getCode();
            excelValues.put(key, rv);
            // 計算加總
            addCodeSums(rv);
        }
        */
        List<ExcelValueItem> allValueItem = reportConfigService.findValueItems(group);
        for(ExcelValueItem item : allValueItem){
            //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
//            if((ReportSheetEnum.TB.getCode().equals(item.getSheetCode()) && this.company.isNonSap())
//                    || ReportSheetEnum.D0202.getCode().equals(item.getSheetCode())
//                    || ReportSheetEnum.D0204.getCode().equals(item.getSheetCode())){
//                excelValueList.add(item);
//            }
            if(ReportSheetEnum.TB.getCode().equals(item.getSheetCode()) && this.company.isConsolidationRptUpload()){
                excelValueList.add(item);
            }
        }
        
//        this.buildTabCompanys();//D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
        this.bulidTabAccounts();
//        logger.debug("codeSums:{}", codeSums);
    }
    
    private void buildTabCompanys() {
       for (ExcelValueItem item : excelValueList) {
           Set<FcCompany> compSet = new HashSet<>();
            for (RptDValue rv : excelValues.values()) {
                if (!rv.getSheet().equals(item.getSheetCode())) {
                    continue;
                }
                compSet.add(rv.getCoid2());
            }
            List<FcCompany> compList = new ArrayList<>(compSet);
            Collections.sort(compList, new Comparator<FcCompany>() {
                @Override
                public int compare(FcCompany c1, FcCompany c2) {
                    return c1.getCode().compareTo(c2.getCode());
                }
            });
            compList.add(SUM_COMPANY);
            tabCompanys.put(item.getSheetCode(), compList);
       } 
    }
    
    private void bulidTabAccounts(){
        if(this.company.isConsolidationRptUpload()){
            this.tabAccounts = tbValueFacade.findMonthly(yearmonth, company);
        }else{
            this.tabAccounts = new ArrayList<>();
        }
    }
    
    // interface implements
    @Override
    public boolean uploadVerify(UploadedFile uploadFile) {
        //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
        /**
        try {
            this.rate = new BigDecimal(currencyRate);
            if (BigDecimal.ZERO.equals(rate)) {
                JsfUtil.addErrorMessage("轉換率不正確!");
                return false;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("轉換率不正確!");
            return false;
        }
        */
        logger.debug("uploadVerify yearmonth:"+yearmonth);
        InputStream in = null;
        excelValues.clear();
        tabCompanys.clear();
        codeSums.clear();
        codeXlsSums.clear();
        tabAccounts.clear();
        try {
            in = uploadFile.getInputstream();
            workbook = WorkbookFactory.create(in);
            validator = new ExcelValidator(workbook);
            
            //依公司群組取得報表設定
            logger.debug("uploadVerify group:"+group.getCode());
//            List<ExcelValidatorItem> excelValidList = new ArrayList<>();
            excelValueList = new ArrayList<>();
//            excelValidList = reportConfigService.findValidatorItems(group);//D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
            List<ExcelValueItem> allValueItem = reportConfigService.findValueItems(group);
            for(ExcelValueItem item : allValueItem){
                //非SAP公司上傳檔案需含trail balance
                if(ReportSheetEnum.TB.getCode().equals(item.getSheetCode())){
                    if (this.company.isConsolidationRptUpload()) {
                        excelValueList.add(item);
                    }
                //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
//                }else if(ReportSheetEnum.D0202.getCode().equals(item.getSheetCode())
//                        || ReportSheetEnum.D0204.getCode().equals(item.getSheetCode())){
//                    if (company.isConsolidationRevenue()) {//合併營收26家才接收D大類
//                        excelValueList.add(item);
//                    }
                }
            }
//            logger.debug("uploadVerify excelValidList:"+excelValidList.size());
            logger.debug("uploadVerify excelValueList:"+excelValueList.size());
            // 驗證特定欄位是否為空白或數值, 失敗將 throw exception
            //自訂檢查 TB;row:2~319 col:C;
//            validator.isNumericOrBlank(ReportSheetEnum.TB.getCode(),1,318,2);
            
            //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
            /**
            for (ExcelValidatorItem vaItem : excelValidList) {
                //只檢查特定sheet
                if(ReportSheetEnum.D0202.getName().equals(vaItem.getSheetName())
                         || ReportSheetEnum.D0204.getName().equals(vaItem.getSheetName())){
                    if (company.isConsolidationRevenue()) {//合併營收26家才接收D大類
                        validator.isNumericOrBlank(vaItem);
                    }
                }
            }
            */
            
            // 讀取需儲存之報表 
            //TB(detail) D0202 D0204
            for (ExcelValueItem item : excelValueList) {
                //非SAP公司上傳檔案需含trail balance
                if((ReportSheetEnum.TB.getCode().equals(item.getSheetCode()))){
                    if (this.company.isConsolidationRptUpload()) {
                        this.loadTrailBlanceValues(ReportSheetEnum.TB.getCode(), 2);
                    }
                }
                //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
                /**
                if(ReportSheetEnum.D0202.getCode().equals(item.getSheetCode())
                         || ReportSheetEnum.D0204.getCode().equals(item.getSheetCode())){
                    if (this.company.isConsolidationRevenue()) {//合併營收26家才接收D大類
                        this.loadValues(item);
                    }
                }
                */
            }
            
            if (CollectionUtils.isNotEmpty(tabAccounts)) {
                logger.debug("uploadVerify tabAccounts:"+tabAccounts.size());
                if(!this.validTB()){
                    JsfUtil.addErrorMessage("sheet:{TB} 金額合計應為零!");
                    allowSave = false;
                    return false;
                }
            }
            
//            this.buildTabCompanys();//D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
//            JsfUtil.addSuccessMessage("excel驗證成功!");
            JsfUtil.addSuccessMessage("excel已讀取, 如資料確認無誤請儲存!");
            allowSave = true;
            return true;
        } catch (Exception ex) { // 會有 runtime exception (Could not resolve external workbook name)
            JsfUtil.addErrorMessage("uploadVerify "+ex.getMessage());
            logger.error("uploadVerify Exception:"+ex);
//            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        allowSave = false;
        return false;
    }
    
    private void loadTrailBlanceValues(String sheetName, int startrow) throws InvalidFormatException {
        logger.debug("loadTrailBlanceValues start");
//        list.clear();
        Sheet poiSheet = workbook.getSheet(sheetName);
        if (null == poiSheet) {
            return;
        }
        logger.debug("loadTrailBlanceValues poiSheet:"+sheetName);
        // 讀取會科金額 (空白及0會被排除)
        for (int row = startrow; ;row++) { 
            if(row>350){
                break;
            }
            String accountsCode = validator.getCellValueString(poiSheet, row - 1, 0);
            String accountsName = validator.getCellValueString(poiSheet, row - 1, 1);
            if (null == accountsCode 
                    || null == accountsName ){
                continue;
            }
//            logger.debug("loadTrailBlanceValues accountsCode:"+accountsCode);
//            logger.debug("loadTrailBlanceValues accountsName:"+accountsName);
            
            try{
                BigDecimal amountXls = validator.getCellValue(poiSheet, row-1, 2);
//                logger.debug("loadTrailBlanceValues amountXls:"+amountXls);
                if (null==amountXls || amountXls.compareTo(BigDecimal.ZERO)==0) {
                    continue;
                }
                BigDecimal amount;
                if(CurrencyEnum.TWD.name().equals(this.currency.getCode())){
                    amount = amountXls.setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
                }else{
                    amount = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_2, RoundingMode.HALF_UP);
                }
//                logger.debug("loadTrailBlanceValues accountsCode:"+accountsCode);
//                logger.debug("loadTrailBlanceValues accountsName:"+accountsName);
//                logger.debug("loadTrailBlanceValues amount:"+amount);
                ReportBaseVO vo = new ReportBaseVO();
                vo.setAccCode(accountsCode);
                vo.setAccName(accountsName);
                vo.setAmount(amount);
                tabAccounts.add(vo);
            } catch (Exception ex) {
                String error = MessageFormat.format("sheet:{0}, row:(1), code:{2} 無法取得數值",
                        sheetName, row, accountsCode);
                logger.error(error);
                throw new InvalidFormatException(error);
            }
        }
        logger.debug("loadTrailBlanceValues end");
    }
    
    //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
    /**
    private void loadValues(ExcelValueItem item) throws InvalidFormatException {
        String sheetName = item.getSheetName();
        logger.debug("loadValues start:"+sheetName);
        int startrow = item.getStartRow();
        List<ExcelAccountItem> acItems = item.getAcItems();
        Sheet poiSheet = workbook.getSheet(sheetName);
        if (null == poiSheet && null != item.getSheetCode()) {//中文sheetName查不到 改以英文sheetEname去取資料
            poiSheet = workbook.getSheet(item.getSheetCode());
            if (null != poiSheet) {
                sheetName = item.getSheetCode();
            }
        }
        if (null == poiSheet) {
            String error = MessageFormat.format("sheet:{0} not exist!", sheetName);
            throw new InvalidFormatException(error);
        }
        Set<String> companys = new HashSet<>();
        Map<FcCompany, Map<String, BigDecimal>> result = new HashMap<>(); // 公司, 會科代碼, 金額
        for (int row = startrow; ;row++) { // row is 1-based (excel row)
            String compCode = validator.getCompCode(poiSheet, row - 1);
            if (null == compCode) {
                break;
            }
//            logger.debug("loadValues error compCode:"+compCode);
            FcCompany comp = dValidCompanys.get(compCode);
            if (null == comp) {
                logger.error("loadValues error compCode:"+compCode);
//                String error = "公司代碼不正確:" + sheetName + ", 列" + (row+1);
                String error = "公司代碼("+ compCode +")不正確:" + sheetName + ", 列" + (row);
                throw new InvalidFormatException(error);
            }
            if (!companys.add(compCode)) {
                logger.error("loadValues error duplicate compCode:"+compCode);
                String error = "公司資料重複:" + sheetName + ", 列" + (row+1);
                throw new InvalidFormatException(error);
            }
            // 讀取會科金額 (空白及0會被排除)
            for (ExcelAccountItem acItem : acItems) {
                BigDecimal amountXls = validator.getCellValue(poiSheet, row-1, CellReference.convertColStringToIndex(acItem.getCol()));
//                logger.error("loadValues error amountXls:"+acItem.getCode()+"_"+amountXls);
                if (null==amountXls || amountXls.compareTo(BigDecimal.ZERO)==0) {
                    continue;
                }
                BigDecimal amount = amountXls.multiply(rate);
                amount = amount.setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
                amountXls = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
                RptDValue rv = new RptDValue(item.getSheetCode(), comp, acItem.getCode(), amount, amountXls);
                String key = item.getSheetCode() + ":" + compCode + ":" + acItem.getCode();
//                logger.debug("loadValues key:"+key);
                excelValues.put(key, rv);
                // 計算加總
                this.addCodeSums(rv);
            }
        }
        logger.debug("loadValues end");
    }
    */
    
    private void addCodeSums(RptDValue rv) {
        // 加總
        String sumKey = rv.getSheet() + ":" + rv.getCode();
        BigDecimal sum = codeSums.get(sumKey);
        if (null == sum) {
            codeSums.put(sumKey, rv.getAmount());
            codeXlsSums.put(sumKey, rv.getAmountXls());
        } else {
            codeSums.put(sumKey, sum.add(rv.getAmount()));
            if (null != rv.getAmountXls()) {
                codeXlsSums.put(sumKey, sum.add(rv.getAmountXls()));
            }
        }
    }
    
    public List<FcCompany> getCompList(ExcelValueItem item) {
        return (null==item || null==tabCompanys) ? Collections.EMPTY_LIST : tabCompanys.get(item.getSheetCode());
    }
    
    public BigDecimal getValue(ExcelValueItem item, FcCompany comp, ExcelAccountItem acitem) {
        if (null==item || null==comp || null==acitem) {
            return null;
        }
        BigDecimal result;
        if (comp==SUM_COMPANY) {
            String key = item.getSheetCode() + ":" + acitem.getCode();
            result = codeSums.get(key);
        } else {
            String key = item.getSheetCode() + ":" + comp.getCode() + ":" + acitem.getCode();
            RptDValue rv = excelValues.get(key);
            result = rv==null ? null : rv.getAmount();
            return result;
        }
        if (null==result || result.compareTo(BigDecimal.ZERO)==0) {
            return null;
        } else {
            return result;
        }
    }
    
    public BigDecimal getXlsValue(ExcelValueItem item, FcCompany comp, ExcelAccountItem acitem) {
        if (null==item || null==comp || null==acitem) {
            return null;
        }
        BigDecimal result;
        if (comp==SUM_COMPANY) {
            String key = item.getSheetCode() + ":" + acitem.getCode();
            result = codeXlsSums.get(key);
        } else {
            String key = item.getSheetCode() + ":" + comp.getCode() + ":" + acitem.getCode();
            RptDValue rv = excelValues.get(key);
            result = rv==null ? null : rv.getAmountXls();
            return result;
        }
        if (null==result || result.compareTo(BigDecimal.ZERO)==0) {
            return null;
        } else {
            return result;
        }
    }
    //1.合計為零
    private boolean validTB(){
        boolean result = false;
        BigDecimal sum = BigDecimal.ZERO;
        for(ReportBaseVO tbVO : tabAccounts){
            sum = sum.add(tbVO.getAmount());
        }
        if(sum.compareTo(BigDecimal.ZERO) == 0){
            result = true;
        }
        return result;
    }
    
    // action
    public void save() {
        rptSheetUpload.setYearmonth(yearmonth);
        rptSheetUpload.setCompany(company);
        rptSheetUpload.setModifier(userSession.getTcUser());
        rptSheetUpload.setModifytimestamp(new Date());
        try {
            rptSheetUploadFacade.save(rptSheetUpload, attachmentController.getAttachmentVOList()
                    , excelValues.values(), tabAccounts, zbukto);
            
            JsfUtil.addSuccessMessage("儲存成功!");
            FacesContext context = FacesContext.getCurrentInstance();
            context.responseComplete();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public RptSheetUpload getRptSheetUpload() {
        return rptSheetUpload;
    }

    public boolean isAllowSave() {
        return allowSave;
    }
    
    public String getCurrencyRate() {
        return currencyRate;
    }

    public List<ReportBaseVO> getTabAccounts() {
        return tabAccounts;
    }

    public List<ExcelValueItem> getReportTabs() {
        return excelValueList;
    }

    public boolean isShowAmountXls() {
        return showAmountXls;
    }
    //</editor-fold>
    
    
}
