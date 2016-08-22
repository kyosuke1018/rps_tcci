/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.controller.util.SelectProductsController;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.vo.SalesDetailsVO;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class CustomerSalesOrderReportController {

    protected final static Logger logger = LoggerFactory.getLogger(CustomerSalesOrderReportController.class);
    private List<SalesDetailsVO> orderDetailList;
    @EJB
    SkSalesDetailsFacade salesDetailsFacade;
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    @ManagedProperty(value = "#{selectCustomerController}")
    SelectCustomerController selectCustomerController;
    @ManagedProperty(value = "#{selectProductsController}")
    private SelectProductsController selectProductsController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }

    public void setSelectProductsController(SelectProductsController selectProductsController) {
        this.selectProductsController = selectProductsController;
    }

    public List<SalesDetailsVO> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<SalesDetailsVO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @PostConstruct
    private void init() {
        logger.debug("init start!");
        //String customerSimpleCode = FacesUtil.getRequestParameter(ConstantsUtil.CUSTOMER_SIMPLE_CODE);
        if( !StringUtils.isEmpty( selectCustomerController.getSelectedCustomer() ) ){
            doSearchAction();
        }
    }

    public String doSearchAction() {
        try {
            Date startDate = DateUtil.convertDateToStartTime(queryCriteriaController.getFilter().getInvoiceStart());
            Date endDate = DateUtil.convertDateToEndTime(queryCriteriaController.getFilter().getInvoiceEnd());
            //orderDetailList = salesDetailsFacade.findByCriteria(queryCriteriaController.getFilter().getSales(), selectCustomerController.getSelectedCustomer() , startDate, endDate, queryCriteriaController.getProductNumber());
            orderDetailList = salesDetailsFacade.findByCriteria(
                    queryCriteriaController.getFilter().getSales(),
                    selectCustomerController.getSelectedCustomer() ,
                    startDate,
                    endDate,
                    selectProductsController.getProductFilter());
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
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
        String[] title = {"客戶","發票號碼/發票日期", "銷貨單號","產品","數量/單位","單價/(贈品)","發票金額","溢價折讓"};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue(title[i]);
            sheet.autoSizeColumn(i); 
        }
        // replace all <br/> with \n       
        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setWrapText(true);   
        int index = 0;
        for (Iterator rows = sheet.rowIterator();rows.hasNext();) {               
                HSSFRow arow = (HSSFRow) rows.next();
                if (index++ < 1) continue;
                short c1 = arow.getFirstCellNum();
                short c2 = arow.getLastCellNum();                         
                // loop for every cell in each row 
                for (int c = c1; c < c2; c++) {
                    HSSFCell acell = arow.getCell(c);                  
                    if (acell != null) {
                        String cellValue = acell.getStringCellValue();                 
                        if (!cellValue.isEmpty()) {                   
                            String cellValue2 = cellValue.replace("<br/>", "\n");           
                            acell.setCellStyle(cellStyle2);                                     
                            acell.setCellValue(new HSSFRichTextString(cellValue2));
                        }
                    }
                }           
         }        
    }
}
