package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.ArDataModel;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkPaymentRateFacade;
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
public class PaymentRateReportController {
    
    
    private List<SkPaymentRate> paymentRateList;
    private SkPaymentRate[] selectedPaymentRateList;
    private ArDataModel arDataModel;
    
       
    @EJB SkPaymentRateFacade skPaymentRateFacade;    
    
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    
    @ManagedProperty(value = "#{paymentItemReportController}")
    PaymentItemReportController paymentItemReportController;

    public void setPaymentItemReportController(PaymentItemReportController paymentItemReportController) {
        this.paymentItemReportController = paymentItemReportController;
    }
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public ArDataModel getArDataModel() {
        return arDataModel;
    }

    public void setArDataModel(ArDataModel arDataModel) {
        this.arDataModel = arDataModel;
    }

    public SkPaymentRate[] getSelectedPaymentRateList() {
        return selectedPaymentRateList;
    }

    public void setSelectedOverdueArList(SkPaymentRate[] selectedPaymentRateList) {
        this.selectedPaymentRateList = selectedPaymentRateList;
    }

    public List<SkPaymentRate> getPaymentRateList() {
        return paymentRateList;
    }

    public void setPaymentRateList(List<SkPaymentRate> paymentRateList) {
        this.paymentRateList = paymentRateList;
    }
      
    @PostConstruct
    public void init() {
        setQueryCriteriaController(queryCriteriaController);
    }
    public String doSearchAction(){       
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        String date = queryCriteriaController.getFilter().getYear();        
        try{
            paymentRateList = skPaymentRateFacade.findByCriteria(sales, date);           
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
            bundle.getString("paymentRate.baselineTimestamp"),
            bundle.getString("paymentRate.arAmount"),
            bundle.getString("paymentRate.premiumDiscount"),
            bundle.getString("paymentRate.salesReturn"),
            bundle.getString("paymentRate.salesDiscount"),
            bundle.getString("paymentRate.paymentAmount"),
            bundle.getString("paymentRate.paymentRate"),
            bundle.getString("paymentRate.weight")};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue( title[i] );
        }
        
        //shift one row for title
        sheet.shiftRows(0, sheet.getLastRowNum() , 1);
        //add title        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell   = row.createCell(0);        
        cell.setCellValue(bundle.getString("sales.group")+"："+queryCriteriaController.getFilter().getSales().getDisplayIdentifier()+"   "+bundle.getString("menu.sales.report.payment.rate"));
        //span title column
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));        

    }

    public String translateDateToString(Date d, String formatString){
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        return sdf.format(d);
    }
    
    public String callPaymentItemAction(SkPaymentRate rate){
        queryCriteriaController.getFilter().setYear(translateDateToString(rate.getBaselineTimestamp(),"yyyy") );
        queryCriteriaController.getFilter().setMonth(translateDateToString(rate.getBaselineTimestamp(),"MM") );
        paymentItemReportController.doSearchAction();
        return null;
    }
}
