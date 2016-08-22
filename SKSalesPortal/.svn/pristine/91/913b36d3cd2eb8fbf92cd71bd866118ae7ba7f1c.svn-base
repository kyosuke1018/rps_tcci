package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.enums.OrderTypeEnum;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.vo.SalesReturnDiscountVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class SalesReturnDiscountReportController {
    @ManagedProperty(value = "#{queryCriteriaController}")
    private QueryCriteriaController queryCriteriaController;   
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }    
    
    @EJB
    private SkSalesDetailsFacade salesDetailsFacade;
    
    private List<SalesReturnDiscountVO> salesReturnDiscountList;

    public List<SalesReturnDiscountVO> getSalesReturnDiscountList() {
        return salesReturnDiscountList;
    }

    public void setSalesReturnDiscountList(List<SalesReturnDiscountVO> salesReturnDiscountList) {
        this.salesReturnDiscountList = salesReturnDiscountList;
    }
   
    public void doSearch() {
        salesReturnDiscountList = salesDetailsFacade.findByCriteria(queryCriteriaController.getFilter());
        if (salesReturnDiscountList == null) {
            salesReturnDiscountList = new ArrayList<SalesReturnDiscountVO>();
        }
    }
    
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        String[] title = {"買方","認列日期","請款文件","訂單號碼","發票號碼","物料","單價","數量","未稅金額","稅金"};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue(title[i]);
        }
                 
        //shift one row for title
        sheet.shiftRows(0, sheet.getLastRowNum() , 1);
        //add title        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
        Date startDate = queryCriteriaController.getFilter().getOrderDateStart();
        Date endDate = queryCriteriaController.getFilter().getOrderDateEnd();
        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell   = row.createCell(0);        
        String reportTitle = "銷售群組[" + queryCriteriaController.getFilter().getSales().getCode() + "] ";
        reportTitle += (startDate != null ? formatter.format(startDate) : "") + " ~ " + (endDate != null ? formatter.format(endDate) : "");
        reportTitle += (queryCriteriaController.getFilter().getOrderType().equals(OrderTypeEnum.SALES_RETURN) ? " 退貨明細表" : " 事後折讓明細表");
        cell.setCellValue(reportTitle);
        //span title column
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));      
    }
  
}
