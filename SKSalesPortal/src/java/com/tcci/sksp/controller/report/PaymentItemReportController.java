package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.ArDataModel;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkPaymentItemFacade;
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
public class PaymentItemReportController {
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    
    private List<SkPaymentItem> paymentItemList;
    private SkPaymentRate[] selectedPaymentRateList;
    private ArDataModel arDataModel;
    
       
    @EJB SkPaymentItemFacade skPaymentItemFacade;    
    
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

    public List<SkPaymentItem> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<SkPaymentItem> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }
      
    @PostConstruct
    public void init() {
        setQueryCriteriaController(queryCriteriaController);
        if(paymentItemList==null && FacesUtil.getRequestParameter("sapid")!=null && 
                FacesUtil.getRequestParameter("year")!=null && FacesUtil.getRequestParameter("month")!=null){
            doSearchAction();
        }
    }
    public String doSearchAction(){       
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        String date = queryCriteriaController.getFilter().getYear() +"/"+ queryCriteriaController.getFilter().getMonth();        
        try{
            paymentItemList = skPaymentItemFacade.findByCriteria(sales, date);
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
            bundle.getString("paymentItem.baselineTimestamp"),
            bundle.getString("paymentItem.arAmount"),
            bundle.getString("paymentItem.premiumDiscount"),
            bundle.getString("paymentItem.salesReturn"),
            bundle.getString("paymentItem.salesDiscount"),
            bundle.getString("paymentItem.paymentAmount"),
            bundle.getString("paymentItem.orderNumber"),
            bundle.getString("paymentItem.invoiceNumber"),
            bundle.getString("paymentItem.customer.code"),
            bundle.getString("paymentItem.customer.name")};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue( title[i] );
        }
        
        //shift one row for title
        sheet.shiftRows(0, sheet.getLastRowNum() , 1);
        //add title        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell   = row.createCell(0);        
        cell.setCellValue(bundle.getString("sales.group")+"："+queryCriteriaController.getFilter().getSales().getDisplayIdentifier()+"   "+bundle.getString("menu.sales.report.payment.item"));
        //span title column
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));        
    }
}
