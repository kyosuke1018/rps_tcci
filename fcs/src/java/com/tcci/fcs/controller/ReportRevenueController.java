/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fcs.entity.FcCompGroup;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcConfigFacade;
import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.facade.FcCompGroupFacade;
import com.tcci.fcs.facade.FcReportA0102Facade;
import com.tcci.fcs.facade.FcReportValueFacade;
import com.tcci.fcs.facade.service.ReportService;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.fcs.util.ReportConfig;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
 * @author Mendel.Lee
 */
@ManagedBean(name = "reportRevenue")
@ViewScoped
public class ReportRevenueController {

    private static final Logger logger = LoggerFactory.getLogger(ReportRevenueController.class);

    private String yearmonth;
    private StreamedContent exportFile; // 匯出檔案

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    @Inject
    private FcCompGroupFacade compGroupFacade;

    @Inject
    private FcConfigFacade configFacade;

    @Inject
    private FcCompanyFacade companyFacade;

    @Inject
    private FcReportA0102Facade reportA0102Facade;

    @Inject
    private FcReportValueFacade reportValueFacade;
    
    // 集團選項
    private CompanyGroupEnum groupCondition;

    // 損益表資料
    private List<ReportBaseVO> incomeStatementList;

    // 沖銷表資料
    private List<ReportBaseVO> writeOffList;

    // 去年損益表資料
    private List<ReportBaseVO> incomeStatementLastYearList;

    // 去年沖銷表資料
    private List<ReportBaseVO> writeOffLastYearList;

    // 公司位置<公司代碼, 順序>
    private Map<String, FcCompany> companyLocationMap = new HashMap<String, FcCompany>();

    // 損益表會科位置<會科代碼, 列數>
    private Map<String, Integer> incomeAccountMap = new HashMap<String, Integer>();

    // 沖銷表會科位置<會科代碼, 行數>
    private Map<String, Integer> writeOffAccountMap = new HashMap<String, Integer>();

    // 今年營收表會科位置<會科代碼, 列數>
    private Map<String, Integer> revenueAccountThisYearMap = new HashMap<String, Integer>();

    // 去年營收表會科位置<會科代碼, 列數>
    private Map<String, Integer> revenueAccountLastYearMap = new HashMap<String, Integer>();
    
    // 企業團選項
    private List<SelectItem> groupsOptionList;
    private List<FcUserCompGroupR> cgList;
    private boolean isAdmin;
    private boolean noPermission = false;
    
    @PostConstruct
    private void init() {
        //20151119 增加多選公司群組
        cgList =  userSession.getTcUser().getCompGroupList();
        isAdmin = userSession.isUserInRole("ADMINISTRATORS");
        //公司群組權限
	groupsOptionList = this.buildGroupOptions();
        if (groupsOptionList == null || groupsOptionList.isEmpty()) {
            noPermission = true;
            return;
        }
        if(this.isAdmin){
            groupCondition = CompanyGroupEnum.TCC;//預設台泥
        }else{
            if (this.cgList != null && !this.cgList.isEmpty()) {
                groupCondition = this.cgList.get(0).getGroup();
            }
        }
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, groupCondition);
    }
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.groupCondition);
        yearmonth = configFacade.findValue(FcConfigKeyEnum.YEARMONTH, groupCondition);
    }
    
    /**
     * 產生合併報表
     */
    public void exportRevenue() {

	logger.debug("exportRevenue:" + this.groupCondition);
	incomeStatementList = reportA0102Facade.findByYM(yearmonth, groupCondition);
	writeOffList = reportValueFacade.findByYM(yearmonth, groupCondition);
	
//	if (CollectionUtils.isEmpty(incomeStatementList) && CollectionUtils.isEmpty(writeOffList)) {
//	    JsfUtil.addWarningMessage("該月份無資料！");
//	    return ;
//	}

	String path = ReportConfig.REVENUE_REPORT_PATH + ReportConfig.REPORT_TEMPLATE.get(groupCondition);
	InputStream in = this.getClass().getResourceAsStream(path);
	try {
	    Workbook workbook = WorkbookFactory.create(in);
	    if (!preProcessReport(workbook)) {
		JsfUtil.addWarningMessage("公司排序未設定！");
		return;
	    }
	    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    // 損益表
	    genIncomeStatement(workbook.getSheetAt(0), evaluator, false);
	    // 沖銷彙總表
	    genWriteOff(workbook.getSheetAt(1), evaluator, false);
            // 營收彙總表
            genRevenue(workbook.getSheetAt(2), evaluator, false);//20160307 原幣別金額報表需求
	    if (groupCondition.equals(CompanyGroupEnum.TCC)) {
		// 營收彙總表 (仟元)
		genRevenueThousand(workbook.getSheetAt(3), evaluator);
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    workbook.write(out);
	    String filename = "Report_Revenue_" + yearmonth + ".xls";
	    exportFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()),
		    "application/octet-stream", filename);
	} catch (Exception ex) {
	    logger.error("export exception!", ex);
	    JsfUtil.addErrorMessage(ex.getMessage());
	}
    }
    /**
     * 產生合併報表 oriCurrency
     * 20160307 原幣別金額報表需求
     */
    public void exportRevenueOri() {
        logger.debug("exportRevenue:" + this.groupCondition);
	incomeStatementList = reportA0102Facade.findByYM(yearmonth, groupCondition);
	writeOffList = reportValueFacade.findByYM(yearmonth, groupCondition);
	
	String path = ReportConfig.REVENUE_REPORT_PATH + ReportConfig.REPORT_ORI_TEMPLATE.get(groupCondition);
	InputStream in = this.getClass().getResourceAsStream(path);
	try {
	    Workbook workbook = WorkbookFactory.create(in);
	    if (!preProcessReport(workbook)) {
		JsfUtil.addWarningMessage("公司排序未設定！");
		return;
	    }
	    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    // 損益表
	    genIncomeStatement(workbook.getSheetAt(0), evaluator, true);//20160307 原幣別金額報表需求
	    // 沖銷彙總表
	    genWriteOff(workbook.getSheetAt(1), evaluator, true);//20160307 原幣別金額報表需求
            //20160307 原幣別金額報表需求
            // 營收彙總表
            genRevenue(workbook.getSheetAt(2), evaluator, true);//20160307 原幣別金額報表需求
	    if (groupCondition.equals(CompanyGroupEnum.TCC)) {
		 //營收彙總表 (仟元)
		genRevenueThousand(workbook.getSheetAt(3), evaluator);
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    workbook.write(out);
	    String filename = "Report_Revenue_Ori_" + yearmonth + ".xls";
	    exportFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()),
		    "application/octet-stream", filename);
	} catch (Exception ex) {
	    logger.error("export exception!", ex);
	    JsfUtil.addErrorMessage(ex.getMessage());
	}
    }

    /**
     * 預先取得位置資訊
     *
     * @param workbook
     */
    private boolean preProcessReport(Workbook workbook) {

	// 位移量
	int INCOME_START_ROW = 2;
	int WRITE_OFF_START_COL = 2;
	int REVENUE_START_ROW = 2;

	Pattern p = Pattern.compile("\\d{4,6}");

	/*
	 * 設定公司位置
	 */
	companyLocationMap = new HashMap<String, FcCompany>();
	Set<Integer> checkSortSet = new HashSet<Integer>();
	List<FcCompany> companyList = companyFacade.findAllActiveByGroup(groupCondition, true, false);
	// 判斷是否設定排序－檢查是否有重覆的排序值
	for (FcCompany company : companyList) {
	    if (checkSortSet.contains(company.getSort())) {
		return false;
	    }
	    checkSortSet.add(company.getSort());
	}
	companyLocationMap = adjustSorting(companyList);
	// 若無重覆排序值，則用設定值
//	for (FcCompany company : companyList) {
//	    companyLocationMap.put(company.getCode(), company);
//	}

	/*
	 * 設定損益表科目
	 */
	incomeAccountMap = new HashMap<String, Integer>();
	int incomeAccountSort = 1;
	Sheet incomeSheet = workbook.getSheetAt(0);
	for (int i = INCOME_START_ROW; i <= incomeSheet.getLastRowNum(); i++) {
	    Row row = incomeSheet.getRow(i);
	    Cell cell = row.getCell(0);
	    String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
	    if (p.matcher(cellValue).matches()) {
		incomeAccountMap.put(cellValue, incomeAccountSort++);
	    }
	}

	/*
	 * 設定沖銷彙總表科目
	 */
	writeOffAccountMap = new HashMap<String, Integer>();
	int WriteOffAccountSort = 1;
	Sheet writeOffSheet = workbook.getSheetAt(1);
	Row rowTemp = writeOffSheet.getRow(0);
	for (int i = WRITE_OFF_START_COL; i < rowTemp.getLastCellNum(); i++) {

	    Cell cell = rowTemp.getCell(i);
	    String cellValue = cell.getStringCellValue();
	    if (p.matcher(cellValue).matches()) {
		writeOffAccountMap.put(cellValue, WriteOffAccountSort++);
	    }
	}

	/*
	 * 設定營收彙總表科目
	 */
	revenueAccountThisYearMap = new HashMap<String, Integer>();
	revenueAccountLastYearMap = new HashMap<String, Integer>();
        int revenueAccountThisYearSort = 1;
        Sheet revenueSheet = workbook.getSheetAt(2);
        for (int i = REVENUE_START_ROW; i <= revenueSheet.getLastRowNum(); i++) {
            Row row = revenueSheet.getRow(i);
            Cell cell = row.getCell(0);
            String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
            if (p.matcher(cellValue).matches()) {
                revenueAccountThisYearMap.put(cellValue, revenueAccountThisYearSort++);
            } else {
                revenueAccountThisYearSort++;
                break;
            }
        }
        
        int revenueAccountLastYearSort = 1;
        for (int i = revenueAccountThisYearSort; i <= revenueSheet.getLastRowNum(); i++) {
            Row row = revenueSheet.getRow(i);
            Cell cell = row.getCell(0);
            String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
            if (p.matcher(cellValue).matches()) {
                revenueAccountLastYearMap.put(cellValue, revenueAccountLastYearSort++);
            }
        }
	
	return true;
    }

    /**
     * 產出損益表
     *
     * @param sheet
     * @param evaluator
     */
    private void genIncomeStatement(Sheet sheet, FormulaEvaluator evaluator, boolean oriCurrency) {

	// 位移量
	int SHIFT_COL = 1;
	int SHIFT_ROW = 1;

	// 將公司寫入
	for (Map.Entry<String, FcCompany> entrySet : companyLocationMap.entrySet()) {

	    int pos = entrySet.getValue().getSort() + SHIFT_COL;

	    Row row0 = sheet.getRow(0);
	    if (null == row0) {
		row0 = sheet.createRow(0);
	    }
	    Row row1 = sheet.getRow(1);
	    if (null == row1) {
		row1 = sheet.createRow(1);
	    }

	    writeCell(row0, pos, entrySet.getValue().getCode());
            if(!oriCurrency){
                writeCell(row1, pos, entrySet.getValue().getAbbreviation());
            }else{
                String str = entrySet.getValue().getAbbreviation()+"("+entrySet.getValue().getCurrency().getCode()+")";
                writeCell(row1, pos, str);
            }
	    
	}
	// 將數字填入
	for (ReportBaseVO incomeStatement : incomeStatementList) {
	    Integer rowNo = incomeAccountMap.get(incomeStatement.getAccCode());
	    if (rowNo == null) {
		continue;
	    }
	    rowNo = rowNo + SHIFT_ROW;
	    Row row = sheet.getRow(rowNo);
	    if (null == row) {
		row = sheet.createRow(rowNo);
	    }

	    FcCompany company = companyLocationMap.get(incomeStatement.getCompCode());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
            if(!oriCurrency){
                writeCell(row, colNo, incomeStatement.getAmount());
            }else{
                writeCell(row, colNo, incomeStatement.getAmountXls());
            }
	}
	// 重新計算加總
	Row formulaRow = sheet.getRow(2);
	this.evaluateFormula(evaluator, sheet, 2, sheet.getLastRowNum(), 2, formulaRow.getLastCellNum());

	// 自動調整大小
	Row row = sheet.getRow(0);
	for (int i = 2; i <= row.getLastCellNum(); i++) {
	    sheet.autoSizeColumn(i);
	}
    }

    /**
     * 產出沖銷彙總表
     *
     * @param sheet
     * @param evaluator
     */
    private void genWriteOff(Sheet sheet, FormulaEvaluator evaluator, boolean oriCurrency) {

	// 位移量
	int SHIFT_COL = 1;
	int SHIFT_ROW = 0;

	// 將公司寫入
	for (Map.Entry<String, FcCompany> entrySet : companyLocationMap.entrySet()) {

	    int pos = entrySet.getValue().getSort() + SHIFT_ROW;

	    Row row = sheet.getRow(pos);
	    if (null == row) {
		row = sheet.createRow(pos);
	    }

	    writeCell(row, 0, entrySet.getValue().getCode());
            if(!oriCurrency){
                writeCell(row, 1, entrySet.getValue().getName());
            }else{
                String str = entrySet.getValue().getName() + "("+entrySet.getValue().getCurrency().getCode() + ")";
                writeCell(row, 1, str);
            }
	}
	// 將數字填入
	for (ReportBaseVO writeOff : writeOffList) {
	    FcCompany company = companyLocationMap.get(writeOff.getCompCode());
	    if (company == null) {
		continue;
	    }
	    Integer rowNo = company.getSort();
	    if (rowNo == null) {
		continue;
	    }
	    rowNo = rowNo + SHIFT_ROW;
	    Row row = sheet.getRow(rowNo);
	    if (null == row) {
		row = sheet.createRow(rowNo);
	    }

	    Integer colNo = writeOffAccountMap.get(writeOff.getAccCode());
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
            if(!oriCurrency){
                writeCell(row, colNo, writeOff.getAmount());
            }else{
                writeCell(row, colNo, writeOff.getAmountXls());
            }
	}
	// 重新計算加總
	Row formulaRow = sheet.getRow(1);
	this.evaluateFormula(evaluator, sheet, 1, sheet.getLastRowNum(), 2, formulaRow.getLastCellNum());

	// 自動調整大小
	Row row = sheet.getRow(0);
	for (int i = 2; i <= row.getLastCellNum(); i++) {
	    sheet.autoSizeColumn(i);
	}
    }

    /**
     * 產生營收彙總表
     * @param sheet
     * @param evaluator 
     */
    private void genRevenue(Sheet sheet, FormulaEvaluator evaluator, boolean oriCurrency) {

	DecimalFormat df = new DecimalFormat("00");
	/*
	 * 營收表標題列數
	 */
	// 公司列數
	int TITLE_THIS_Y_ROW = 1;
	// 會科
	int ACCOUNT_THIS_Y_ROW = TITLE_THIS_Y_ROW + 1;
	// 104(1-6) 累計營收小計(A)
	int REVENUE_THIS_Y_ROW = ACCOUNT_THIS_Y_ROW + revenueAccountThisYearMap.keySet().size();
	// 沖銷
	int WRITE_OFF_THIS_Y_ROW = REVENUE_THIS_Y_ROW + 1;
	// 104(1-6) 累計合併營收(B)
	int CR_THIS_Y_RANGE_THIS_M = WRITE_OFF_THIS_Y_ROW + 1;
	// (B / A)
	int CR_RATIO_THIS_Y = CR_THIS_Y_RANGE_THIS_M + 1;
	// 104(1-5) 累計合併營收(B)
	int CR_THIS_Y_RANGE_LAST_M = CR_RATIO_THIS_Y + 2;
	// 104(6-6) 單月合併營收
	int CR_THIS_Y_SINGLE_THIS_M = CR_THIS_Y_RANGE_LAST_M + 1;
	
	// 公司列數
	int TITLE_LAST_Y_ROW = CR_THIS_Y_SINGLE_THIS_M + 4;
	// 會科
	int ACCOUNT_LAST_Y_ROW = TITLE_LAST_Y_ROW + 1;
	// 累計營收小計(A)
	int REVENUE_LAST_Y_ROW = ACCOUNT_LAST_Y_ROW + revenueAccountLastYearMap.keySet().size();
	// 沖銷
	int WRITE_OFF_LAST_Y_ROW = REVENUE_LAST_Y_ROW + 1;
	// 累計合併營收(B)
	int CR_LAST_Y_RANGE_THIS_M = WRITE_OFF_LAST_Y_ROW + 1;
	// (B / A)
	int CR_RATIO_LAST_Y = CR_LAST_Y_RANGE_THIS_M + 1;
	// 累計合併營收(B)
	int CR_LAST_Y_RANGE_LAST_M = CR_RATIO_LAST_Y + 2;
	// 單月合併營收
	int CR_LAST_Y_SINGLE_THIS_M = CR_LAST_Y_RANGE_LAST_M + 1;
	
	String yearStr = this.yearmonth.substring(0, 4);
	String monthStr = this.yearmonth.substring(4);
	int yearInt = Integer.parseInt(yearStr);
	int monthInt = Integer.parseInt(monthStr);

	String rocThisYear = String.valueOf(yearInt - 1911);
	String rocLastYear = String.valueOf(yearInt - 1911 - 1);

	// 104(1-5) 累計合併營收(B)
	List<ReportBaseVO> incomeStatementThisYLastMList = reportA0102Facade.findByYM(yearStr + df.format(monthInt-1), groupCondition);
	List<ReportBaseVO> incomeStatementLastYLastMList = reportA0102Facade.findByYM((yearInt - 1) + df.format(monthInt-1), groupCondition);
	// 104(1-5) 累計合併營收(B)
	List<ReportBaseVO> writeOffThisYLastMList = reportValueFacade.findByYM(yearStr + df.format(monthInt-1), groupCondition);
	List<ReportBaseVO> writeOffLastYLastMList = reportValueFacade.findByYM((yearInt - 1) + df.format(monthInt-1), groupCondition);
	incomeStatementLastYearList = reportA0102Facade.findByYM((yearInt - 1) + monthStr, groupCondition);
	writeOffLastYearList = reportValueFacade.findByYM((yearInt - 1) + monthStr, groupCondition);
	

	// 201406→104(1-6)
	String rocThisYearAndRangeThisMonth = rocThisYear + "(1-" + monthInt + ")";
	// 201406→104(1-5)
	String rocThisYearAndRangeLastMonth = rocThisYear + "(1-" + (monthInt - 1) + ")";
	// 201406→104(6-6)
	String rocThisYearAndSingleThisMonth = rocThisYear + "(" + (monthInt) + "-" + (monthInt) + ")";

	// 201406→103(1-6)
	String rocLastYearAndRangeThisMonth = rocLastYear + "(1-" + monthInt + ")";
	// 201406→103(1-5)
	String rocLastYearAndRangeLastMonth = rocLastYear + "(1-" + (monthInt - 1) + ")";
	// 201406→103(6-6)
	String rocLastYearAndSingleThisMonth = rocLastYear + "(" + (monthInt) + "-" + (monthInt) + ")";
	
	// 填寫幣別
	FcCompGroup compGroup = compGroupFacade.findByCode(groupCondition.getCode());
	String unit = "單位：" + compGroup.getCurrency().getName() + "/元";
	Row unitRow = sheet.getRow(0);
	writeCell(unitRow, 0, unit);
	// 填寫年月
	// 公司列數
	int yearMonthTitlePos = 0;
	Row titleThisYearRow = sheet.getRow(TITLE_THIS_Y_ROW);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocThisYearAndRangeThisMonth);
	// 累計營收小計(A)
	titleThisYearRow = sheet.getRow(REVENUE_THIS_Y_ROW);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocThisYearAndRangeThisMonth);
	// 累計合併營收(B)
	titleThisYearRow = sheet.getRow(CR_THIS_Y_RANGE_THIS_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocThisYearAndRangeThisMonth);
	// 累計合併營收(B)
	titleThisYearRow = sheet.getRow(CR_THIS_Y_RANGE_LAST_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocThisYearAndRangeLastMonth);
	// 單月合併營收
	titleThisYearRow = sheet.getRow(CR_THIS_Y_SINGLE_THIS_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocThisYearAndSingleThisMonth);
	
	// 公司列數
	titleThisYearRow = sheet.getRow(TITLE_LAST_Y_ROW);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocLastYearAndRangeThisMonth);
	// 累計營收小計(A)
	titleThisYearRow = sheet.getRow(REVENUE_LAST_Y_ROW);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocLastYearAndRangeThisMonth);
	// 累計合併營收(B)
	titleThisYearRow = sheet.getRow(CR_LAST_Y_RANGE_THIS_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocLastYearAndRangeThisMonth);
	// 累計合併營收(B)
	titleThisYearRow = sheet.getRow(CR_LAST_Y_RANGE_LAST_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocLastYearAndRangeLastMonth);
	// 單月合併營收
	titleThisYearRow = sheet.getRow(CR_LAST_Y_SINGLE_THIS_M);
	writeCell(titleThisYearRow, yearMonthTitlePos, rocLastYearAndSingleThisMonth);
	
	// 位移量
	int SHIFT_COL = 2;
	int SHIFT_ROW = 1;

	// 將公司寫入
	for (Map.Entry<String, FcCompany> entrySet : companyLocationMap.entrySet()) {

	    int pos = entrySet.getValue().getSort() + SHIFT_COL;

	    Row row0 = sheet.getRow(TITLE_THIS_Y_ROW);
	    if (null == row0) {
		row0 = sheet.createRow(TITLE_THIS_Y_ROW);
	    }
	    Row row1 = sheet.getRow(TITLE_LAST_Y_ROW);
	    if (null == row1) {
		row1 = sheet.createRow(TITLE_LAST_Y_ROW);
	    }

	    writeCell(row0, pos, entrySet.getValue().getAbbreviation());
	    writeCell(row1, pos, entrySet.getValue().getAbbreviation());
	}
	// 將數字填入
	// 損益-填入今年
	for (ReportBaseVO incomeStatement : incomeStatementList) {
	    Integer rowNo = revenueAccountThisYearMap.get(incomeStatement.getAccCode());
	    if (rowNo == null) {
		continue;
	    }
	    rowNo = rowNo + TITLE_THIS_Y_ROW;
	    Row row = sheet.getRow(rowNo);
	    if (null == row) {
		row = sheet.createRow(rowNo);
	    }

	    FcCompany company = companyLocationMap.get(incomeStatement.getCompCode());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
            if(!oriCurrency){
                writeCell(row, colNo, incomeStatement.getAmount());
            }else{
                writeCell(row, colNo, incomeStatement.getAmountXls());
            }
	}
	// 損益-填入去年
	for (ReportBaseVO incomeStatement : incomeStatementLastYearList) {
	    Integer rowNo = revenueAccountLastYearMap.get(incomeStatement.getAccCode());
	    if (rowNo == null) {
		continue;
	    }
	    rowNo = rowNo + TITLE_LAST_Y_ROW;
	    Row row = sheet.getRow(rowNo);
	    if (null == row) {
		row = sheet.createRow(rowNo);
	    }

	    FcCompany company = companyLocationMap.get(incomeStatement.getCompCode());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
            if(!oriCurrency){
                writeCell(row, colNo, incomeStatement.getAmount());
            }else{
                writeCell(row, colNo, incomeStatement.getAmountXls());
            }
	}
	
	// 沖銷-填入去年
	List<ReportBaseVO> removeWriteOffList = new ArrayList<ReportBaseVO>();
	String[] writeOffAccount = {"4111","4114","4115","4116","4117","4119"//
		,"4300","4500","4600","4800"};
	Set<String> writeOffSet = new HashSet(Arrays.asList(writeOffAccount));
	for (ReportBaseVO writeOffLastYear : writeOffLastYearList) {
	    if (!writeOffSet.contains(writeOffLastYear.getAccCode())) {
		removeWriteOffList.add(writeOffLastYear);
	    }
	}
	for (ReportBaseVO removeWriteOff : removeWriteOffList) {
	    writeOffLastYearList.remove(removeWriteOff);
	}
	Row writeOffRow = sheet.getRow(WRITE_OFF_LAST_Y_ROW);
	Map<String,BigDecimal> writeOffLastYearMap = //
		ReportService.sumByCompCode(writeOffLastYearList);
	for (Map.Entry<String, BigDecimal> entrySet : writeOffLastYearMap.entrySet()) {
	    
	    FcCompany company = companyLocationMap.get(entrySet.getKey());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
	    writeCell(writeOffRow, colNo, entrySet.getValue().negate());
	}
	
	// 損益-沖銷：填入今年上個月-104(1-5) 累計合併營收(B)
	// 沖銷
	removeWriteOffList = new ArrayList<ReportBaseVO>();
	for (ReportBaseVO writeOffLastYear : writeOffThisYLastMList) {
	    if (!writeOffSet.contains(writeOffLastYear.getAccCode())) {
		removeWriteOffList.add(writeOffLastYear);
	    }
	}
	for (ReportBaseVO removeWriteOff : removeWriteOffList) {
	    writeOffThisYLastMList.remove(removeWriteOff);
	}
	Map<String,BigDecimal> writeOffThisYLastMMap = //
		ReportService.sumByCompCode(writeOffThisYLastMList);
	// 損益
	List<ReportBaseVO> removeIncomeStatementList = new ArrayList<ReportBaseVO>();
	// 考量台泥舊資料與中橡舊資料的異同，若資料不變的情況下，需到2016年11月才能使用下面這段
//	String[] incomeStatementAccount = {"4000","4100"};
	String[] incomeStatementAccount;
	if (groupCondition.equals(CompanyGroupEnum.TCC)) {
	    incomeStatementAccount = new String[]{"4000","4100"};
	} else {
	    incomeStatementAccount = new String[]{"4000","4110","4112","4113","4114","4190"};
	}
	Set<String> incomeStatementOffSet = new HashSet(Arrays.asList(incomeStatementAccount));
	for (ReportBaseVO incomeStatementThisYear : incomeStatementThisYLastMList) {
	    if (incomeStatementOffSet.contains(incomeStatementThisYear.getAccCode())) {
		removeIncomeStatementList.add(incomeStatementThisYear);
	    }
	}
	for (ReportBaseVO incomeStatementOff : removeIncomeStatementList) {
	    incomeStatementThisYLastMList.remove(incomeStatementOff);
	}
	Row revenueLastMonthRow = sheet.getRow(CR_THIS_Y_RANGE_LAST_M);
	Map<String,BigDecimal> incomeStatementThisYLastMMap = //
		ReportService.sumByCompCode(incomeStatementThisYLastMList);
	for (Map.Entry<String, BigDecimal> entrySet : incomeStatementThisYLastMMap.entrySet()) {
	    
	    FcCompany company = companyLocationMap.get(entrySet.getKey());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
	    
	    BigDecimal writeOff = writeOffThisYLastMMap.get(entrySet.getKey());
	    if (writeOff == null) {
		writeOff = new BigDecimal("0");
	    }
	    writeOff = writeOff;
	    writeCell(revenueLastMonthRow, colNo, entrySet.getValue().subtract(writeOff));
	}
	
	// 損益-沖銷：填入去年上個月-103(1-5) 累計合併營收(B)
	// 沖銷
	removeWriteOffList = new ArrayList<ReportBaseVO>();
	for (ReportBaseVO writeOffLastYear : writeOffLastYLastMList) {
	    if (!writeOffSet.contains(writeOffLastYear.getAccCode())) {
		removeWriteOffList.add(writeOffLastYear);
	    }
	}
	for (ReportBaseVO removeWriteOff : removeWriteOffList) {
	    writeOffLastYLastMList.remove(removeWriteOff);
	}
	Map<String,BigDecimal> writeOffLastYLastMMap = //
		ReportService.sumByCompCode(writeOffLastYLastMList);
	// 損益
	removeIncomeStatementList = new ArrayList<ReportBaseVO>();
	for (ReportBaseVO incomeStatementThisYear : incomeStatementLastYLastMList) {
	    if (incomeStatementOffSet.contains(incomeStatementThisYear.getAccCode())) {
		removeIncomeStatementList.add(incomeStatementThisYear);
	    }
	}
	for (ReportBaseVO incomeStatementOff : removeIncomeStatementList) {
	    incomeStatementLastYLastMList.remove(incomeStatementOff);
	}
	revenueLastMonthRow = sheet.getRow(CR_LAST_Y_RANGE_LAST_M);
	Map<String,BigDecimal> incomeStatementLastYLastMMap = //
		ReportService.sumByCompCode(incomeStatementLastYLastMList);
	for (Map.Entry<String, BigDecimal> entrySet : incomeStatementLastYLastMMap.entrySet()) {
	    
	    FcCompany company = companyLocationMap.get(entrySet.getKey());
	    if (company == null) {
		continue;
	    }
	    Integer colNo = company.getSort();
	    if (colNo == null) {
		continue;
	    }
	    colNo = colNo + SHIFT_COL;
	    
	    BigDecimal writeOff = writeOffLastYLastMMap.get(entrySet.getKey());
	    if (writeOff == null) {
		writeOff = new BigDecimal("0");
	    }
	    writeOff = writeOff;
	    writeCell(revenueLastMonthRow, colNo, entrySet.getValue().subtract(writeOff));
	}
	
	/**
	 * 重新計算加總
	 */
	Row formulaRow = sheet.getRow(2);
	this.evaluateFormula(evaluator, sheet, 2, sheet.getLastRowNum(), 2, formulaRow.getLastCellNum());
	
	// 自動調整大小
	Row row = sheet.getRow(0);
	for (int i = 2; i < row.getLastCellNum(); i++) {
	    sheet.autoSizeColumn(i);
	}
    }

    /**
     * 產生營收彙總表 (仟元)
     * @param sheet
     * @param evaluator 
     */
    private void genRevenueThousand(Sheet sheet, FormulaEvaluator evaluator) {
	
	// 填寫幣別
	FcCompGroup compGroup = compGroupFacade.findByCode(groupCondition.getCode());
	String unit = "單位：" + compGroup.getCurrency().getName() + "/仟元";
	Row unitRow = sheet.getRow(0);
	writeCell(unitRow, 0, unit);
	
	// 重新計算加總
	Row formulaRow = sheet.getRow(1);
	this.evaluateFormula(evaluator, sheet, 1, sheet.getLastRowNum(), 0, formulaRow.getLastCellNum());
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
    
    private void evaluateFormulaSingleRow(FormulaEvaluator evaluator, Sheet sheet//
	    , int rowNo, int colNo1, int colNo2) {
	
	this.evaluateFormula(evaluator, sheet, rowNo, rowNo, colNo1, colNo2);
    }
    
    private void evaluateFormulaSingleCell(FormulaEvaluator evaluator, Sheet sheet//
	    , int rowNo1, int rowNo2, int colNo) {
	
	this.evaluateFormula(evaluator, sheet, rowNo1, rowNo2, colNo, colNo);
    }

    /**
     * 
     * @param evaluator
     * @param sheet
     * @param rowNo1
     * @param rowNo2
     * @param colNo1
     * @param colNo2 
     */
    private void evaluateFormula(FormulaEvaluator evaluator, Sheet sheet//
	    , int rowNo1, int rowNo2, int colNo1, int colNo2) {
	
	for (int i = rowNo1; i <= rowNo2; i++) {
	    
	    Row row = sheet.getRow(i);
	    
	    if (null == row) {
		continue;
	    }
	    
	    for (int j = colNo1; j <= colNo2; j++) {
		
		Cell cell = row.getCell(j);
		
		if (null == cell) {
		    continue;
		}
		
		if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
		    evaluator.evaluateFormulaCell(cell);
		}
	    }
	}
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
    
    private HashMap<String, FcCompany> adjustSorting(List<FcCompany> companyList) {
	
	HashMap<String, FcCompany> result = new HashMap<String, FcCompany>();
	Map<Integer, FcCompany> sortMap = new TreeMap<Integer, FcCompany>();
	for (FcCompany company : companyList) {
	    sortMap.put(company.getSort(), company);
	}
	int i = 1;
	for (Map.Entry<Integer, FcCompany> sortComp : sortMap.entrySet()) {
	    FcCompany company = sortComp.getValue();
	    company.setSort(i);
	    result.put(company.getCode(), company);
	    i++;
	}
	return result;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter & setter">
    public List<SelectItem> getGroupsOptionList() {
	return groupsOptionList;
    }

    public void setGroupsOptionList(List<SelectItem> groupsOptionList) {
	this.groupsOptionList = groupsOptionList;
    }
    
    public String getYearmonth() {
	return yearmonth;
    }
    
    public void setYearmonth(String yearmonth) {
	this.yearmonth = yearmonth;
    }

    public CompanyGroupEnum getGroupCondition() {
	return groupCondition;
    }

    public void setGroupCondition(CompanyGroupEnum groupCondition) {
	this.groupCondition = groupCondition;
    }
    
    public UserSession getUserSession() {
	return userSession;
    }
    
    public void setUserSession(UserSession userSession) {
	this.userSession = userSession;
    }
    
    public StreamedContent getExportFile() {
	return exportFile;
    }
    
    public void setExportFile(StreamedContent exportFile) {
	this.exportFile = exportFile;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

//</editor-fold>
    
}
