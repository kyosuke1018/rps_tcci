package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkBudget;
import com.tcci.sksp.entity.ar.SkSalesDayAchievement;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesDayAchievementFacade;
import com.tcci.sksp.vo.SalesAchievementVO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
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
public class SalesAchievementDetailReportController {
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    
    private List<SkSalesDayAchievement> salesDayAchievementList;  
    private SalesAchievementVO totalVO;
           
    @EJB SkSalesDayAchievementFacade salesDayAchievementFacade;    
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public List<SkSalesDayAchievement> getSalesDayAchievementList() {
        return salesDayAchievementList;
    }

    public void setSalesDayAchievementList(List<SkSalesDayAchievement> salesDayAchievementList) {
        this.salesDayAchievementList = salesDayAchievementList;
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
    /*
    private String getQueryCommand() throws IOException{
        StringBuffer buffer = new StringBuffer();
        String fullPathFileName = "/report/sql/sales_achievement_detail.txt";
        java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(fullPathFileName);
        if( is != null ){
            InputStreamReader isr =
                new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                    buffer.append((char)ch);
            }
            in.close();
            System.out.println(buffer.toString());
        }
        return buffer.toString();        
    }
    */
    
    public String doSearchAction(){       
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        String year = queryCriteriaController.getFilter().getYear();        
        String month = queryCriteriaController.getFilter().getMonth();  
        BigDecimal totalGrossProfitRate = null;
        try{
            //System.out.println("salescode="+ sales + ",year=" + year +",month=" + month);
            salesDayAchievementList = salesDayAchievementFacade.findByCriteria(sales, year + month);
        }catch(Exception e){
            FacesUtil.addFacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage());
            e.printStackTrace();
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
            bundle.getString("invoiceTimestamp"),
            bundle.getString("amount"),
            bundle.getString("premiumDiscount"),
            bundle.getString("salesReturn"),
            bundle.getString("salesDiscount"),
            bundle.getString("netProfit"),
            bundle.getString("grossProfitRate"),
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
                queryCriteriaController.getFilter().getYear()+bundle.getString("yy")+queryCriteriaController.getFilter().getMonth()+bundle.getString("mm")+"   "+
                bundle.getString("menu.sales.report.sales.achievement.detail"));
        //span title column
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));     
        
        //add footer            
        HSSFRow footRow = sheet.createRow(sheet.getLastRowNum()+1);       
        footRow.createCell(0).setCellValue("合計");                
        footRow.createCell(1).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getAmount())); 
        footRow.createCell(2).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getPremiumDiscount())); 
        footRow.createCell(3).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getSalesReturn())); 
        footRow.createCell(4).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getSalesDiscount())); 
        footRow.createCell(5).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getNetProfit())); 
        footRow.createCell(6).setCellValue(MessageFormat.format("{0,number,#.##%}",totalVO.getGrossProfitRate())); 
        footRow.createCell(7).setCellValue(MessageFormat.format("{0,number,#.##%}",totalVO.getAchievementRate())); 
        footRow.createCell(8).setCellValue(NumberFormat.getNumberInstance().format(totalVO.getBudget())); 
        for (int i = 0; i < footRow.getPhysicalNumberOfCells(); i++) {
            footRow.getCell(i).setCellStyle(cellStyle);                    
        }  

    }

    public String translateDateToString(Date d, String formatString){
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        return sdf.format(d);
    }
     
    public String formatNumber(Number n){
        return NumberFormat.getNumberInstance().format(n);
    }

    public String formatPercent(Number n){
        return MessageFormat.format("{0,number,#.##%}",n); 
    }
    
}
