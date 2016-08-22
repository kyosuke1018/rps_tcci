/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.ar.SkSalesAllowances;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesAllowancesFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class QuerySalesAllowancesController {
    //<editor-fold defaultstate="collapsed" desc="variables">

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SkSalesMember salesMember;
    private String orderNumber;
    private String invoiceNumber;
    private String returnNumber;
    private Date exportDate;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private SkSalesAllowancesFacade salesAllowancesFacade;
    @EJB
    private SkSalesMemberFacade salesMemberFacade;
    @ManagedProperty(value = "#{salesAllowancesListController}")
    private SalesAllowancesListController salesAllowancesListController;

    public void setSalesAllowancesListController(SalesAllowancesListController salesAllowancesListController) {
        this.salesAllowancesListController = salesAllowancesListController;
    }
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter,setter">
    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public SkSalesMember getSalesMember() {
        return salesMember;
    }

    public void setSalesMember(SkSalesMember salesMember) {
        this.salesMember = salesMember;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        salesMember = salesMemberFacade.findByMember(sessionController.getUser());
        orderNumber = "";
        invoiceNumber = "";
        returnNumber = "";
        exportDate = new Date();
    }

    public void query() {
        logger.debug("query(), code={},orderNumber={},invoiceNumber={},returnNumber={}", new Object[]{salesMember.getCode(), orderNumber, invoiceNumber, returnNumber});
        List<SkSalesAllowances> salesAllowances = salesAllowancesFacade.findByCriteria(salesMember.getCode(), orderNumber, invoiceNumber, returnNumber);
        logger.debug("salesAllowances.size()={}", salesAllowances.size());
        salesAllowancesListController.setSalesAllowancesList(salesAllowances);
    }

    public void queryForExport() {
        List<SkSalesAllowances> salesAllowancesList = salesAllowancesFacade.findForExport();
        logger.debug("salesAllowances.size()={}", salesAllowancesList.size());
        String beginEnd = "S";
        SkSalesAllowances lastSalesAllowances = null;
        for (SkSalesAllowances salesAllowances : salesAllowancesList) {
            if (lastSalesAllowances == null || 
                    (lastSalesAllowances.getOrderNumber().equals(salesAllowances.getOrderNumber()) 
                    && lastSalesAllowances.getReason().equals(salesAllowances.getReason()))) {
                salesAllowances.setBeginEnd(beginEnd);
                if (beginEnd.equals("S")) {
                    beginEnd = "";
                }
            } else {
                lastSalesAllowances.setBeginEnd((lastSalesAllowances.getBeginEnd() == null ? "" : lastSalesAllowances.getBeginEnd()) + "E");
                salesAllowances.setBeginEnd("S");
            }
            lastSalesAllowances = salesAllowances;
        }
        lastSalesAllowances.setBeginEnd((lastSalesAllowances.getBeginEnd() == null ? "" : lastSalesAllowances.getBeginEnd()) + "E");
        salesAllowancesListController.setSalesAllowancesList(salesAllowancesList);
    }

    public String getBuyer(SkSalesAllowances salesAllowances) {
        if ("N".equals(salesAllowances.getRefOrder())) {
            return salesAllowances.getBuyer();
        } else {
            return "";
        }
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        sheet.removeRow(header);
        sheet.shiftRows(1, sheet.getLastRowNum(), -1);
    }

    public String getUploadItem(String item) {
        String returnItem = "";
        final int LENGTH = 6;
        for (int i = 1; i <= LENGTH - item.length(); i++) {
            returnItem += "0";
        }
        returnItem += item;
        return returnItem;

    }
}
