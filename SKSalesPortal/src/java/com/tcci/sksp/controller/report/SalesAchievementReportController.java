package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkSalesMonthAchievement;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesMonthAchievementFacade;
import com.tcci.sksp.vo.SalesAchievementVO;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

@ManagedBean
@ViewScoped
public class SalesAchievementReportController {
    
    private List<SkSalesMonthAchievement> salesMonthAchievementList;  
    private SalesAchievementVO totalVO;
           
    @EJB SkSalesMonthAchievementFacade salesMonthAchievementFacade;    

    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    @ManagedProperty(value = "#{salesAchievementDetailReportController}")
    SalesAchievementDetailReportController salesAchievementDetailReportController;
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public void setSalesAchievementDetailReportController(SalesAchievementDetailReportController salesAchievementDetailReportController) {
        this.salesAchievementDetailReportController = salesAchievementDetailReportController;
    }

    
    public List<SkSalesMonthAchievement> getSalesMonthAchievementList() {
        return salesMonthAchievementList;
    }

    public void setSalesMonthAchievementList(List<SkSalesMonthAchievement> salesMonthAchievementList) {
        this.salesMonthAchievementList = salesMonthAchievementList;
    }
  
    public SalesAchievementVO getTotalVO() {
        return totalVO;
    }

    public void setTotalVO(SalesAchievementVO totalVO) {
        this.totalVO = totalVO;
    }
      
    @PostConstruct
    public void init() {
        //setQueryCriteriaController(queryCriteriaController);
    }
    public String doSearchAction(){ 
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        String year = queryCriteriaController.getFilter().getYear();        
        try{
            //System.out.println("sales=" + sales.getCode()  +",year=" + year );
            salesMonthAchievementList = salesMonthAchievementFacade.findByCriteria(sales, year);
            //System.out.println("salesMonthArchievementList size=" + salesMonthArchievementList.size() );
        }catch(Exception e){
            FacesUtil.addFacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    } 
   
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        ResourceBundle bundle = com.tcci.sksp.controller.util.FacesUtil.getResourceBundle();
        String[] title ={
            bundle.getString("ym"),
            bundle.getString("amount"),
            bundle.getString("premiumDiscount"),
            bundle.getString("salesReturn"),
            bundle.getString("salesDiscount"),
            bundle.getString("netProfit"),
            bundle.getString("grossProfitRate"),
            bundle.getString("overdueAmount"),
            bundle.getString("achievementRate"),
            bundle.getString("budget")};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue( title[i] );                       
        }  
        
        //shift one row for title
        sheet.shiftRows(0, sheet.getLastRowNum() , 1);
        //add title        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell   = row.createCell(0);        
        cell.setCellValue(bundle.getString("sales.group")+"："+queryCriteriaController.getFilter().getSales().getDisplayIdentifier()+"   "+
                queryCriteriaController.getFilter().getYear()+bundle.getString("yy")+"   "+
                bundle.getString("menu.sales.report.sales.achievement"));
        //span title column
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));        
        
        
        //add footer            
        HSSFRow footRow = sheet.createRow(sheet.getLastRowNum()+1);       
        footRow.createCell(0).setCellValue("合計");                
        footRow.createCell(1).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getAmount())); 
        footRow.createCell(2).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getPremiumDiscount())); 
        footRow.createCell(3).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getSalesReturn())); 
        footRow.createCell(4).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getSalesDiscount())); 
        footRow.createCell(5).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getNetProfit())); 
        footRow.createCell(6).setCellValue(MessageFormat.format("{0,number,#.##%}",totalVO.getGrossProfitRate())); 
        footRow.createCell(7).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getOverdueAmount())); 
        footRow.createCell(8).setCellValue(MessageFormat.format("{0,number,#.##%}",totalVO.getAchievementRate())); 
        footRow.createCell(9).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getBudget())); 
        for (int i = 0; i < footRow.getPhysicalNumberOfCells(); i++) {
            footRow.getCell(i).setCellStyle(cellStyle);                    
        }  

    }

    public String translateDateToString(Date d, String formatString){
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        return sdf.format(d);
    }
    
    public String formatNumber(Number n){
        return n!=null? NumberFormat.getNumberInstance().format(n): null;
    }

    public String formatPercent(Number n){
        return n!=null? MessageFormat.format("{0,number,#.##%}",n): null; 
    }
    
    public String callSalesDailyAchievementReportAction(SkSalesMonthAchievement monthArchievement){
        salesAchievementDetailReportController.queryCriteriaController.getFilter().setSales( queryCriteriaController.getFilter().getSales());
        salesAchievementDetailReportController.queryCriteriaController.getFilter().setYear( DateUtil.getDateFormat( monthArchievement.getBaselineTimestamp(),"yyyy") );
        salesAchievementDetailReportController.queryCriteriaController.getFilter().setMonth( DateUtil.getDateFormat( monthArchievement.getBaselineTimestamp(),"MM") );
        salesAchievementDetailReportController.doSearchAction();
        return null;
    }
    
}
