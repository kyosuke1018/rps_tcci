package com.tcci.sksp.controller.report;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.vo.YearlySalesDetailsVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class SalesDetailHistoryDialogController {

    Logger logger = LoggerFactory.getLogger(SalesDetailHistoryDialogController.class);
    private static final ResourceBundle rb = ResourceBundle.getBundle("/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    @ManagedProperty(value = "#{yearlySalesDetailController}")
    private YearlySalesDetailController yearlySalesDetailController;
    @EJB
    private SkCustomerFacade customerFacade;
    @EJB
    private SkSalesDetailsFacade skSalesDetailsFacade;

    public void setYearlySalesDetailController(YearlySalesDetailController yearlySalesDetailController) {
        this.yearlySalesDetailController = yearlySalesDetailController;
    }
    private static int THREE_YEARS = 3;
    private SkCustomer customer;

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public void initDialogByCustomer(String customerCode) {
        initDialogByCustomerProduct(customerCode, null);
    }

    public void initDialogByCustomerProduct(String customerCode, String matnr) {

        List<YearlySalesDetailsVO> yearlySalesDetailsVOList = new ArrayList<YearlySalesDetailsVO>();
        this.customer = customerFacade.findByCode(customerCode);
        if (this.customer != null) {
            for (int i = 0; i < THREE_YEARS; i++) {
                Date now = new Date();
                String year = String.valueOf(now.getYear() - i + 1900);
                logger.debug("year={}", year);
                yearlySalesDetailsVOList.addAll(skSalesDetailsFacade.findYearlySalesDetailsByCriteria(null, customer, year, matnr, true));
            }
        }
        yearlySalesDetailController.setYearRendered(true);
        yearlySalesDetailController.setYearlySalesDetailsVOList(yearlySalesDetailsVOList);
    }

    public void postProcessorXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        //add header (sales & customer).
        sheet.shiftRows(0, sheet.getLastRowNum(), 1);
        HSSFRow header = sheet.getRow(0);
        if (this.customer != null) {
            HSSFCell headerSecondColumn = header.createCell(0);
            if (StringUtils.isNotEmpty(this.customer.getSimpleCode())) {
                headerSecondColumn.setCellValue(rb.getString("customer.code") + ": " + this.customer.getSimpleCode());
            }
            if (StringUtils.isNotEmpty(this.customer.getName())) {
                headerSecondColumn.setCellValue(headerSecondColumn.getStringCellValue() + " - " + this.customer.getName());
            }
        }
        HSSFCellStyle alignRight = wb.createCellStyle();
        alignRight.setAlignment(CellStyle.ALIGN_RIGHT);
        alignRight.setWrapText(true);
        HSSFCellStyle alignLeft = wb.createCellStyle();
        alignLeft.setWrapText(true);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            //1-3欄靠左.
            for (int j = 0; j < 3; j++) {
                HSSFCell cell = row.getCell(j);
                cell.setCellStyle(alignLeft);
                cell.setCellValue(cell.getStringCellValue().replace("<br/>", "\n\r"));
            }
            for (int j = 3; j < row.getLastCellNum(); j++) {
                HSSFCell cell = row.getCell(j);
                cell.setCellStyle(alignRight);
                cell.setCellValue(cell.getStringCellValue().replace("<br/>", "\n\r"));
            }
        }
    }
}
