/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
public class CustomerReportController {

    protected final static Logger logger = LoggerFactory.getLogger(CustomerReportController.class);
    private String code;
    private String name;
    private String zipCode;
    private String city;
    private String street;
    private SkSalesMember selectedSales;
    private String selectedPaymentTerm;
    private List<SkCustomer> customerList;
    //private List<SelectItem> paymentTermList;
    private List<String> paymentTermList;
    
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkSalesMemberFacade salesMemberFacade;

    @ManagedProperty(value = "#{customerSalesOrderReportController}")
    CustomerSalesOrderReportController customerSalesOrderReportController;

    @ManagedProperty(value = "#{customerSalesOrderYearlyReportController}")
    CustomerSalesOrderYearlyReportController customerSalesOrderYearlyReportController;

    public void setCustomerSalesOrderReportController(CustomerSalesOrderReportController customerSalesOrderReportController) {
        this.customerSalesOrderReportController = customerSalesOrderReportController;
    }

    public void setCustomerSalesOrderYearlyReportController(CustomerSalesOrderYearlyReportController customerSalesOrderYearlyReportController) {
        this.customerSalesOrderYearlyReportController = customerSalesOrderYearlyReportController;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<SkCustomer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<SkCustomer> customerList) {
        this.customerList = customerList;
    }

    public String getSelectedPaymentTerm() {
        return selectedPaymentTerm;
    }

    public void setSelectedPaymentTerm(String selectedPaymentTerm) {
        this.selectedPaymentTerm = selectedPaymentTerm;
    }

    public SkSalesMember getSelectedSales() {
        return selectedSales;
    }

    public void setSelectedSales(SkSalesMember selectedSales) {
        this.selectedSales = selectedSales;
    }

    public List<String> getPaymentTermList() {
        if (paymentTermList == null || paymentTermList.isEmpty()) {
            paymentTermList = customerFacade.getAllOfPaymentTerm();
        }
        return paymentTermList;
    }

    public void setPaymentTermList(List<String> paymentTermList) {
        this.paymentTermList = paymentTermList;
    }

    public String doSearchAction() {
        try {
            //logger.debug("selectedPaymentTerm=" + selectedPaymentTerm +",type=" + selectedPaymentTerm.getClass().getCanonicalName() );
            logger.debug("selectedPaymentTerm=" + selectedPaymentTerm);
            customerList = customerFacade.findByCriteria(selectedSales, code, name, zipCode, city, street, selectedPaymentTerm);
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public String doExportAction() {
        return null;
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        String[] title ={"客戶編號","客戶名稱","郵遞區號","縣市","地址","付款方式","銷售群組","NA","NA"};
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue( title[i] );
        }
    }
    
    public String callCustomerSalesOrderAction(SkCustomer customer){
        SkSalesMember sales = salesMemberFacade.findByCode(customer.getSapid());
        customerSalesOrderReportController.queryCriteriaController.getFilter().setSales(sales);
        customerSalesOrderReportController.queryCriteriaController.getFilter().setSkCustomer(customer);
        customerSalesOrderReportController.selectCustomerController.setSelectedCustomer(customer.getSimpleCode());
        customerSalesOrderReportController.selectCustomerController.setName(customer.getName());
        customerSalesOrderReportController.doSearchAction();
        return null;
    }
    
    public String callCustomerSalesOrderYearlyReportAction(SkCustomer customer){
        SkSalesMember sales = salesMemberFacade.findByCode(customer.getSapid());
        customerSalesOrderReportController.queryCriteriaController.getFilter().setSales(sales);
        customerSalesOrderReportController.queryCriteriaController.getFilter().setSkCustomer(customer);
        customerSalesOrderReportController.selectCustomerController.setSelectedCustomer(customer.getSimpleCode());
        customerSalesOrderReportController.selectCustomerController.setName(customer.getName());
        customerSalesOrderReportController.doSearchAction();
        return null;
    }
}
