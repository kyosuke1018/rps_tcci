/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.attachment.AttachmentEventListener;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.entity.FcReportA0102;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.facade.FcReportA0102Facade;
import com.tcci.fcs.facade.FcReportUploadFacade;
import com.tcci.fcs.facade.service.ReportConfigService;
import com.tcci.fcs.model.global.GlobalConstant;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.fcs.util.ExcelAccountItem;
import com.tcci.fcs.util.ExcelValidator;
import com.tcci.fcs.util.ExcelValidatorItem;
import com.tcci.fcs.util.ExcelValueItem;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="reportUpload")
@ViewScoped
public class ReportUploadController implements AttachmentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ReportUploadController.class);
            
    private String yearmonth;
    private FcCompany company;
    private FcReportUpload reportUpload;
    private String currencyRate = "1.0";//20151002 之後是否改為直接依公司設定幣別及月份 抓取匯率設定檔報價匯率且不可修改
    private boolean allowSave = false;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    
    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private FcReportUploadFacade reportUplodaFacade;
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    @EJB
    private FcReportA0102Facade a0102Facade;
//    @EJB
//    private FcReportTemplateFacade reportTemplateFacade;
    @EJB
    private ReportConfigService reportConfigService;
    
    private Map<String, FcCompany> allCompanys = new HashMap<>();
    
    private Workbook workbook;
    private ExcelValidator validator;
    private BigDecimal rate; // 仟元台幣轉換率
    
    // key: sheetCode:compCode:acCode
    private Map<String, FcReportValue> excelValues = new HashMap<>();
    // sheetCode, compList(含SUM)
    private Map<String, List<FcCompany>> tabCompanys = new HashMap<>();
    private Map<String, BigDecimal> codeSums = new HashMap<>(); // 加總
    private Map<String, BigDecimal> codeXlsSums = new HashMap<>(); //原幣別 上傳金額 加總
    private static FcCompany SUM_COMPANY = new FcCompany("SUM", "總計");
    
    private List<ReportBaseVO> tabAccounts = new ArrayList<>();
    private List<FcReportA0102> excelReportA0102 = new ArrayList<>();
    private boolean showAmountXls = false;
    private FcCurrency currency;
    private CompanyGroupEnum group;
    private List<ExcelValueItem> excelValueList = new ArrayList<>();
    
    @PostConstruct
    private void init() {
        String comp = JsfUtil.getRequestParameter("comp");
        String code = JsfUtil.getRequestParameter("code");
        try {
            if (comp != null) {
                company = companyFacade.find(Long.valueOf(comp));
            } else if (code != null) {
                company = companyFacade.findByCode(code);
            }
//            yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, company.getGroup());
            yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, CompanyGroupEnum.getFromCode(company.getGroup().getCode()));
            //20151002 之後是否改為直接依公司設定幣別及月份 抓取匯率設定檔報價匯率且不可修改
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
                    if(transUSD){//特別兩段匯率處理 
                        FcMonthlyExchangeRate monthlyExchangeRate1  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, this.currency.getCode(), CurrencyEnum.USD.name());
                        FcMonthlyExchangeRate monthlyExchangeRate2  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, CurrencyEnum.USD.name(), company.getGroup().getCurrency().getCode());
                        if(null!=monthlyExchangeRate1 && null!=monthlyExchangeRate2){
                            currencyRate = monthlyExchangeRate1.getAvgRate().multiply(
                                    monthlyExchangeRate2.getAvgRate()
                            ).setScale(GlobalConstant.AMOUNT_SCALE_6, RoundingMode.HALF_UP).toString();//小數位數放大可某種程度減低匯差
                        }else{
                            JsfUtil.addErrorMessage("此公司幣別("+ this.currency.getCode() +") 未維護匯率!");
                            return;
                        }
                    }else{
                        FcMonthlyExchangeRate monthlyExchangeRate  = fcMonthlyExchangeRateFacade.findByYMAndCurrency(yearmonth, this.currency, company.getGroup().getCurrency());
                        if(null!=monthlyExchangeRate){
//                        currencyRate = monthlyExchangeRate.getRate().toString();
                            //20151209 改取平均匯率
                            currencyRate = monthlyExchangeRate.getAvgRate().toString();
                        }else{
                            JsfUtil.addErrorMessage("此公司幣別("+ this.currency.getCode() +") 未維護匯率!");
                            return;
                        }
                    }
                    showAmountXls = true;
                }
            }
        } catch (Exception ex) {
        }
        if (null==yearmonth || null==company) {
            JsfUtil.addErrorMessage("參數有誤!");
            return;
        }
        
//        group = company.getGroup();
        group = CompanyGroupEnum.getFromCode(company.getGroup().getCode());
        
        // 檢查權限
        boolean isAdmin = userSession.isUserInRole("ADMINISTRATORS,FINANCIAL_HQ");
//        boolean isCompanyUploader = userSession.getTcUser().equals(company.getUploader());
        boolean isCompanyUploader = companyFacade.isUploader(userSession.getTcUser(), company);
        if (!isAdmin && !isCompanyUploader) {
            JsfUtil.addErrorMessage("無此公司上傳權限!");
            return;
        }
//        List<FcCompany> companys = companyFacade.findAll();
        List<FcCompany> companys = companyFacade.findAllActiveByGroup(group, true, false);
        logger.debug("allCompanys:"+companys.size());
        for (FcCompany co : companys) {
            allCompanys.put(co.getCode(), co);
        }
        reportUpload = reportUplodaFacade.findByYearmonthCompany(yearmonth, company);
        if (null==reportUpload) {
            reportUpload = new FcReportUpload();
        } else {
            initReportTabs();
        }
        boolean stopUpload = configFacade.findValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, group);
        attachmentController.init(reportUpload, stopUpload);
        attachmentController.setEventListener(this);
        if (stopUpload) {
            JsfUtil.addErrorMessage("關帳中(停止報表上傳)");
        }
    }
    
    // interface implements
    @Override
    public boolean uploadVerify(UploadedFile uploadFile) {
        logger.debug("uploadVerify start");
        try {
            rate = new BigDecimal(currencyRate);
            if (BigDecimal.ZERO.equals(rate)) {
                JsfUtil.addErrorMessage("轉換率不正確!");
                return false;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("轉換率不正確!");
            return false;
        }
        InputStream in = null;
        excelValues.clear();
        tabCompanys.clear();
        codeSums.clear();
        codeXlsSums.clear();
        tabAccounts.clear();
        excelReportA0102.clear();
        try {
            in = uploadFile.getInputstream();
            workbook = WorkbookFactory.create(in);//Exception:Unable to construct record instance
            validator = new ExcelValidator(workbook);
            
            //依公司群組取得報表設定
            logger.debug("uploadVerify group:"+group.getCode());
            List<ExcelValidatorItem> excelValidList = new ArrayList<>();
            excelValueList = new ArrayList<>();
//            if(group.equals(CompanyGroupEnum.TCC)){
//                excelValidList = ReportConfig.VALIDATOR_ITEMS;
//                excelValueList = ReportConfig.VALUE_ITEMS;
//            }else if(group.equals(CompanyGroupEnum.CSRC)){
//                excelValidList = CsrcReportConfig.VALIDATOR_ITEMS;
//                excelValueList = CsrcReportConfig.VALUE_ITEMS;
//            }else if(group.equals(CompanyGroupEnum.CSRC_BVI)){
//                excelValidList = CsrcReportConfig.VALIDATOR_ITEMS_BVI;//驗證欄位不同
//                excelValueList = CsrcReportConfig.VALUE_ITEMS;
//            }
            excelValidList = reportConfigService.findValidatorItems(group);
//            excelValueList = reportConfigService.findValueItems(group);
            List<ExcelValueItem> allValueItem = reportConfigService.findValueItems(group);
            for(ExcelValueItem item : allValueItem){
                if(ReportSheetEnum.A0102.getCode().equals(item.getSheetCode())
                        || ReportSheetEnum.D0206.getCode().equals(item.getSheetCode())
                         || ReportSheetEnum.D0208.getCode().equals(item.getSheetCode())){
                    excelValueList.add(item);
                }
            }
            logger.debug("uploadVerify excelValidList:"+excelValidList.size());
            logger.debug("uploadVerify excelValueList:"+excelValueList.size());
            
            // 驗證特定欄位是否為空白或數值, 失敗將 throw exception
            for (ExcelValidatorItem vaItem : excelValidList) {
                //只檢查特定sheet
                if(ReportSheetEnum.A0102.getName().equals(vaItem.getSheetName())
                        || ReportSheetEnum.D0206.getName().equals(vaItem.getSheetName())
                         || ReportSheetEnum.D0208.getName().equals(vaItem.getSheetName())){
                    validator.isNumericOrBlank(vaItem);
                }
            }
            
            //檢查報表版本
            //20151014 部分上傳資料改到關係人對帳 不檢查
//            if(!this.validVersion()){
//                JsfUtil.addErrorMessage("報表版本不正確 請重新下載模板填寫後 進行上傳!");
//                return false;
//            }
            
            // 讀取需儲存之報表 (D0206, D0208)
            for (ExcelValueItem item : excelValueList) {
//                loadValues(item);
                if(ReportSheetEnum.A0102.getCode().equals(item.getSheetCode())){
                    loadA0102Values(item);
                }else if(ReportSheetEnum.D0206.getCode().equals(item.getSheetCode())
                         || ReportSheetEnum.D0208.getCode().equals(item.getSheetCode())){
                    loadValues(item);
                }
            }
            buildTabCompanys();
            JsfUtil.addSuccessMessage("excel驗證成功!");
            allowSave = true;
            return true;
        } catch (Exception ex) { // 會有 runtime exception (Could not resolve external workbook name)
            JsfUtil.addErrorMessage(ex.getMessage());
            logger.error("uploadVerify Exception:"+ex.getMessage());
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
        return true;
    }
    
    // action
    public void save() {
        reportUpload.setYearmonth(yearmonth);
        reportUpload.setCompany(company);
        reportUpload.setUploader(userSession.getTcUser());
        reportUpload.setModifytimestamp(new Date());
        try {
            //20151005 增加儲存A0102損益表
            reportUplodaFacade.save(reportUpload, attachmentController.getAttachmentVOList(), excelValues.values(), excelReportA0102);
            JsfUtil.addSuccessMessage("儲存成功!");
            // redirect to home 20151113修改 不回首頁
            FacesContext context = FacesContext.getCurrentInstance();
//            context.getExternalContext().getFlash().setKeepMessages(true);
//            try {
//                context.getExternalContext().redirect("home.xhtml");
//            } catch (IOException ex) {
//            }
            context.responseComplete();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    // help
    public String getPageTitle() {
        return "報表上傳";
    }
    
    public List<ExcelValueItem> getReportTabs() {
//        return ReportConfig.VALUE_ITEMS;
        return excelValueList;
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
            FcReportValue rv = excelValues.get(key);
            result = rv==null ? null : rv.getAmountOrig();
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
            FcReportValue rv = excelValues.get(key);
            result = rv==null ? null : rv.getAmountXls();
            return result;
        }
        if (null==result || result.compareTo(BigDecimal.ZERO)==0) {
            return null;
        } else {
            return result;
        }
    }
    
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
//        Map<FcCompany, Map<String, BigDecimal>> result = new HashMap<>(); // 公司, 會科代碼, 金額
        for (int row = startrow; ;row++) { // row is 1-based (excel row)
            String compCode = validator.getCompCode(poiSheet, row - 1);
            if (null == compCode) {
                break;
            }
//            logger.debug("loadValues error compCode:"+compCode);
            FcCompany comp = allCompanys.get(compCode);
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
//                logger.error("loadValues error col_row:"+acItem.getCol()+"_"+(row-1));
                BigDecimal amountXls = validator.getCellValue(poiSheet, row-1, CellReference.convertColStringToIndex(acItem.getCol()));
//                logger.error("loadValues error amountXls:"+acItem.getCode()+"_"+amountXls);
                if (null==amountXls || amountXls.compareTo(BigDecimal.ZERO)==0) {
                    continue;
                }
                BigDecimal amount = amountXls.multiply(rate);
//                if(group.equals(CompanyGroupEnum.CSRC_BVI)){//美金計價
//                    amount = amount.setScale(FcReportValue.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
//                }else{//台幣計價
                    amount = amount.setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
//                }
                amountXls = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
                FcReportValue rv = new FcReportValue(item.getSheetCode(), comp, acItem.getCode(), amount, amountXls);
                String key = item.getSheetCode() + ":" + compCode + ":" + acItem.getCode();
                excelValues.put(key, rv);
                // 計算加總
                addCodeSums(rv);
            }
        }
        logger.debug("loadValues end");
    }
    
    private void loadA0102Values(ExcelValueItem item) throws InvalidFormatException {
        String sheetName = item.getSheetName();
        int startrow = item.getStartRow();
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
        logger.debug("loadA0102Values start:"+sheetName);
        // 讀取會科金額 (空白及0會被排除)
        for (int row = startrow; ;row++) { // row is 1-based (excel row)
            ReportBaseVO reportA0102VO = new ReportBaseVO();
            String accountsCode = validator.getCellValueString(poiSheet, row - 1, 0);
            reportA0102VO.setAccCode(accountsCode);
            String accountsName = validator.getCellValueString(poiSheet, row - 1, 1);
            reportA0102VO.setAccName(accountsName);
//            logger.debug("loadIncomeValues accountsCode:"+accountsCode);
//            logger.debug("loadIncomeValues accountsName:"+accountsName);
            if (null == accountsCode) {
                break;
            }
            
            ExcelAccountItem acItem = item.getAcItems().get(0);
            BigDecimal amountXls = validator.getCellValue(poiSheet, row-1, CellReference.convertColStringToIndex(acItem.getCol()));
            if (null==amountXls || amountXls.compareTo(BigDecimal.ZERO)==0) {
                continue;
            }
            
            BigDecimal amount = amountXls.multiply(rate);
//            if(group.equals(CompanyGroupEnum.CSRC_BVI)){//美金計價
//                amount = amount.setScale(FcReportValue.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
//            }else{//台幣計價
                amount = amount.setScale(GlobalConstant.AMOUNT_SCALE, RoundingMode.HALF_UP);
//            }
            reportA0102VO.setAmount(amount);
            amountXls = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
            reportA0102VO.setAmountXls(amountXls);
            
            FcReportA0102 reportA0102 = new FcReportA0102(accountsCode, amount, amountXls);
            excelReportA0102.add(reportA0102);
            
            //20151013 增加當月差異金額對照
            boolean isJan = "01".equals(yearmonth.substring(4, 6));//報表年月是否為一月
            if(!isJan){
                FcReportA0102 lastMonthValse = a0102Facade.findByAccount(this.company, this.findLastYm(yearmonth), accountsCode);
                if(lastMonthValse != null){
                    reportA0102VO.setAmountMonthly(amount.subtract(lastMonthValse.getAmount()));
                    reportA0102VO.setAmountXlsMonthly(amountXls.subtract(lastMonthValse.getAmountXls()));
                }
            }
            tabAccounts.add(reportA0102VO);
        }
        logger.debug("loadA0102Values end");
    }
    
    private void buildTabCompanys() {
        for (ExcelValueItem item : excelValueList) {
            Set<FcCompany> compSet = new HashSet<>();
            for (FcReportValue rv : excelValues.values()) {
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
//        tabAccounts = a0102Facade.findReporVOByReport(reportUpload);//bulidTabAccounts
        //20151013 增加當月差異金額對照
        tabAccounts = a0102Facade.findMonthlyByYM(yearmonth, this.company);//bulidTabAccounts
    }
    
    private String findLastYm(String ym){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        String lastYm = null;
        try {
            Date date = sdf.parse(ym);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
            lastYm = sdf.format(calendar.getTime());
        } catch (ParseException ex) {
            logger.debug("findLastYm ParseException:"+ex);
        }
        
        return lastYm;
    }
    
    private void initReportTabs() {
        List<FcReportValue> list = reportUplodaFacade.findReportValue(reportUpload);
        for (FcReportValue rv : list) {
            String key = rv.getSheet() + ":" + rv.getCoid2().getCode() + ":" + rv.getCode();
            excelValues.put(key, rv);
            // 計算加總
            addCodeSums(rv);
        }
        //依公司群組取得報表設定
//        excelValueList = new ArrayList<>();
//        if(group.equals(CompanyGroupEnum.TCC)){
//            excelValueList = ReportConfig.VALUE_ITEMS;
//        }else if(group.equals(CompanyGroupEnum.CSRC) || group.equals(CompanyGroupEnum.CSRC_BVI)){
//            excelValueList = CsrcReportConfig.VALUE_ITEMS;
//        }
//        excelValueList = reportConfigService.findValueItems(group);
        List<ExcelValueItem> allValueItem = reportConfigService.findValueItems(group);
        for(ExcelValueItem item : allValueItem){
            if(ReportSheetEnum.A0102.getCode().equals(item.getSheetCode())
                    || ReportSheetEnum.D0206.getCode().equals(item.getSheetCode())
                    || ReportSheetEnum.D0208.getCode().equals(item.getSheetCode())){
                excelValueList.add(item);
            }
        }
        buildTabCompanys();
        this.bulidTabAccounts();
        logger.debug("codeSums:{}", codeSums);
    }
    
    private void addCodeSums(FcReportValue rv) {
        // 加總
        String sumKey = rv.getSheet() + ":" + rv.getCode();
        BigDecimal sum = codeSums.get(sumKey);
        if (null == sum) {
            codeSums.put(sumKey, rv.getAmountOrig());
            codeXlsSums.put(sumKey, rv.getAmountXls());
        } else {
            codeSums.put(sumKey, sum.add(rv.getAmountOrig()));
            if (null != rv.getAmountXls()) {
                codeXlsSums.put(sumKey, sum.add(rv.getAmountXls()));
            }
        }
    }
    
//    private boolean validVersion(){
//        boolean result = true;
//        FcReportTemplate template = reportTemplateFacade.findByYearmonth(yearmonth, group);
//        if(null != template.getVersion()){
//            String[] versionSetting = ReportConfig.VERSION_SETTING;
//            Sheet poiSheet = workbook.getSheet(versionSetting[0]);
//            String versionXls = validator.getCellValueString(poiSheet, Integer.parseInt(versionSetting[2]), CellReference.convertColStringToIndex(versionSetting[1]));
//            if(null == versionXls || !template.getVersion().equals(versionXls)){
//                return false;
//            }
//        }
//        return result;
//    }
    
    // getter, setter
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

    public FcReportUpload getReportUpload() {
        return reportUpload;
    }

    public void setReportUpload(FcReportUpload reportUpload) {
        this.reportUpload = reportUpload;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public AttachmentController getAttachmentController() {
        return attachmentController;
    }

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }

    public boolean isAllowSave() {
        return allowSave;
    }

    public void setAllowSave(boolean allowSave) {
        this.allowSave = allowSave;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public List<ReportBaseVO> getTabAccounts() {
        return tabAccounts;
    }

    public boolean isShowAmountXls() {
        return showAmountXls;
    }

    public void setShowAmountXls(boolean showAmountXls) {
        this.showAmountXls = showAmountXls;
    }

    public FcCurrency getCurrency() {
        return currency;
    }
}
