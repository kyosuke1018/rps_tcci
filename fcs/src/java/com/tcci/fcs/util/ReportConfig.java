/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.util;

import com.tcci.fcs.enums.CompanyGroupEnum;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class ReportConfig {
    // 上傳後需驗證(空白或數字)之儲存格
//    public final static List<ExcelValidatorItem> VALIDATOR_ITEMS = new ArrayList<>();
    
    // 上傳後需儲存之報表資料
//    public final static List<ExcelValueItem> VALUE_ITEMS = new ArrayList<>();
//    public final static List<ExcelAccountItem> D0208_ACITEMS = new ArrayList<>();
//    public final static List<ExcelAccountItem> D0206_ACITEMS = new ArrayList<>();
//    public final static List<ExcelAccountItem> A0102_ACITEMS = new ArrayList<>();
    
    // 匯出設定值
    public final static int EXP_STARTROW = 9; // 0 是第一列 目前各公司群組共同使用
    //D0206 D0208 收入支出income and expenses
    public final static String[] EXP_CODES_IE_TCC = { 
        "COID1", "COID1_NAME", "4111", "4114", "4115", "4116", "4117", "4119", "4300", "4500", "4600", "4800", "7110", "7210", "7480", "SUM1",
        "COID2", "COID2_NAME", "0001", "0002", "0003", "0004", "7510", "7880", "SUM2", "DIFF", "NOTE"
    };
    public final static String[] EXP_CODES_IE_CSRC = { 
        "COID1", "COID1_NAME", "4111", "4112", "4113", "4114", "4800", "7110", "7210", "7480", "SUM1",
        "COID2", "COID2_NAME", "0001", "0002", "0003", "0004", "7510", "7880", "3220", "SUM2", "DIFF", "NOTE"
    };
    //D0202 D0204 資產負債Assets and liabilities
    public final static String[] EXP_CODES_AL_TCC = { 
        "COID1", "COID1_NAME", "1161", "1181", "1212", "1421", "1429", "1479", "1920", "1419", "1940", "SUM1",
        "COID2", "COID2_NAME", "2100", "2161", "2181", "2220", "2310", "2645", "2399", "SUM2", "DIFF", "NOTE"
    };
    
    // 公司位置<公司代碼, 順序>
//    public final static Map<String, FcCompany> TCC_REPORT_COMPANY = new HashMap<>();
    // 報表匯出樣版
    public final static String REVENUE_REPORT_PATH = "/report/revenue/";
    public final static Map<CompanyGroupEnum, String> REPORT_TEMPLATE = new HashMap<>();
    public final static Map<CompanyGroupEnum, String> REPORT_ORI_TEMPLATE = new HashMap<>();
    public final static String IRS_REPORT_PATH = "/report/irs/";
    
    public final static String REPORT_COMPARE_PATH = "/report/compare/";
    public final static Map<CompanyGroupEnum, String> COMPARE_TEMPLATE = new HashMap<>();//收入支出D0206 D0208
    public final static Map<CompanyGroupEnum, String> COMPARE_TEMPLATE_2 = new HashMap<>();//資產負債D0202 D0204
    
    public final static String[] VERSION_SETTING = {"控制總表", "G", "1"};//版本號碼設定座標

    // 設定值
    static {
        // 上傳後需驗證(空白或數字)之儲存格
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("損益表", "D9:D10"));    // 損益表(A0102)
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("損益表", "D12:D16"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D10:D18")); // 營業收入(A0214)
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D20:D21"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D26:D27"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D30:D31"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D33:D36"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D40:D41"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D46:D47"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D50:D51"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D57:D65"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D68:D71"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D74:D75"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D78:D79"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D82:D83"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D86:D87"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業收入", "D92:D93"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業及營業外收入_合併聯屬公司間", "C39:V155")); // 營業及營業外收入_合併聯屬公司間(D0206)
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業及營業外支出_合併聯屬公司間", "C41:H157")); // 營業及營業外支出_合併聯屬公司間(D0208)
        // 實際匯整資料
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業及營業外收入_合併聯屬公司間", "C11:V37"));
//        VALIDATOR_ITEMS.add(new ExcelValidatorItem("營業及營業外支出_合併聯屬公司間", "C13:H39"));
        
        // 上傳後需儲存之報表資料
        // D0206(營業及營業外收入_合併聯屬公司間)
//        D0206_ACITEMS.add(new ExcelAccountItem("C", "4111", "銷貨(水泥熟料)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("E", "4114", "銷貨(電力)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("G", "4115", "銷貨(化工)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("I", "4116", "銷貨(礦源)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("K", "4117", "銷貨(電子陶瓷)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("M", "4119", "銷貨(其他)"));
//        D0206_ACITEMS.add(new ExcelAccountItem("O", "4300", "租賃建設業"));
//        D0206_ACITEMS.add(new ExcelAccountItem("P", "4500", "營建業"));
//        D0206_ACITEMS.add(new ExcelAccountItem("Q", "4600", "勞務收入"));
//        D0206_ACITEMS.add(new ExcelAccountItem("R", "4800", "其他營業"));
//        D0206_ACITEMS.add(new ExcelAccountItem("T", "7110", "利息收入"));
//        D0206_ACITEMS.add(new ExcelAccountItem("U", "7210", "租金收入"));
//        D0206_ACITEMS.add(new ExcelAccountItem("V", "7480", "什項收入"));
//        VALUE_ITEMS.add(new ExcelValueItem(ReportSheetEnum.D0206.getCode(), ReportSheetEnum.D0206.getName(), 11, D0206_ACITEMS));
        // D0208(營業及營業外支出_合併聯屬公司間)
//        D0208_ACITEMS.add(new ExcelAccountItem("C", "0001", "進貨運輸成本運什費"));
//        D0208_ACITEMS.add(new ExcelAccountItem("D", "0002", "其他營業成本"));
//        D0208_ACITEMS.add(new ExcelAccountItem("E", "0003", "營業費用_租金支出"));
//        D0208_ACITEMS.add(new ExcelAccountItem("F", "0004", "營業費用_其他"));
//        D0208_ACITEMS.add(new ExcelAccountItem("G", "7510", "利息費用"));
//        D0208_ACITEMS.add(new ExcelAccountItem("H", "7880", "什項支出"));
//        VALUE_ITEMS.add(new ExcelValueItem(ReportSheetEnum.D0208.getCode(), ReportSheetEnum.D0208.getName(), 13, D0208_ACITEMS));
        //損益表(A0102)
//        A0102_ACITEMS.add(new ExcelAccountItem("D", "", "金額"));
//        VALUE_ITEMS.add(new ExcelValueItem(ReportSheetEnum.A0102.getCode(), ReportSheetEnum.A0102.getName(), 8, A0102_ACITEMS));
	
	
	// 報表匯出樣版
	REPORT_TEMPLATE.put(CompanyGroupEnum.TCC, "Report_TCC_Revenue_Template.xls");
	REPORT_TEMPLATE.put(CompanyGroupEnum.CSRC, "Report_CSRC_Revenue_Template.xls");
//	REPORT_TEMPLATE.put(CompanyGroupEnum.CSRC_BVI, "Report_BVI_Revenue_Template.xls");
        REPORT_ORI_TEMPLATE.put(CompanyGroupEnum.TCC, "Report_TCC_Revenue_Ori_Template.xls");
	REPORT_ORI_TEMPLATE.put(CompanyGroupEnum.CSRC, "Report_CSRC_Revenue_Ori_Template.xls");
        // 對帳表匯出樣版
	COMPARE_TEMPLATE.put(CompanyGroupEnum.TCC, "compare_template_tcc.xls");
	COMPARE_TEMPLATE.put(CompanyGroupEnum.CSRC, "compare_template_csrc.xls");
//	COMPARE_TEMPLATE.put(CompanyGroupEnum.CSRC_BVI, "compare_template_csrc.xls");//CSRC_BVI excel value 使用CSRC設定
        COMPARE_TEMPLATE_2.put(CompanyGroupEnum.TCC, "compare_template_AL_tcc.xlsx");
    }
    
}
