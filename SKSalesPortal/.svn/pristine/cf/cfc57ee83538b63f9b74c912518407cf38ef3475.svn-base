package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class CustomerSalesOrderYearlyReportController {

    protected final static Logger logger = LoggerFactory.getLogger(CustomerSalesOrderReportController.class);
    ResourceBundle rb = ResourceBundle.getBundle("/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    @EJB
    SkSalesDetailsFacade skSalesDetailFacade;
    @EJB
    SkCustomerFacade skCustomerFacade;
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    @ManagedProperty(value = "#{selectCustomerController}")
    SelectCustomerController selectCustomerController;
    @ManagedProperty(value = "#{yearlySalesDetailController}")
    YearlySalesDetailController yearlySalesDetailController;

    public void setYearlySalesDetailController(YearlySalesDetailController yearlySalesDetailController) {
        this.yearlySalesDetailController = yearlySalesDetailController;
    }

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }

    @PostConstruct
    private void init() {
        logger.debug("init start!");
        //String customerSimpleCode = FacesUtil.getRequestParameter(ConstantsUtil.CUSTOMER_SIMPLE_CODE);
        if (!StringUtils.isEmpty(selectCustomerController.getSelectedCustomer())) {
            doSearchAction();
        }
    }

    public String doSearchAction() {
        try {
            String year = queryCriteriaController.getFilter().getYear();
            if (year == null) {
                Date now = new Date();
                year = String.valueOf(now.getYear() + 1900);
                queryCriteriaController.getFilter().setYear(year);
            }
            SkSalesMember sales = queryCriteriaController.getFilter().getSales();
            String customerSimpleCode = selectCustomerController.getSelectedCustomer();
            SkCustomer skCustomer = skCustomerFacade.findBySimpleCode(customerSimpleCode);
            if (skCustomer == null) {
                selectCustomerController.setName(rb.getString("customer.msg.notexists"));
                yearlySalesDetailController.setYearlySalesDetailsVOList(new ArrayList());
                return null;
            }
            yearlySalesDetailController.setYearlySalesDetailsVOList(skSalesDetailFacade.findYearlySalesDetailsByCriteria(sales, skCustomer, year));
            yearlySalesDetailController.setYearRendered(false);
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
        String[] title = null;
        if (yearlySalesDetailController.isYearRendered()) {
            title = new String[]{"產品", "", "年", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "總計"};
        } else {
            title = new String[]{"產品", "", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "總計"};
        }
        logger.debug("header.getPhysicalNumberOfCells()={}", header.getPhysicalNumberOfCells());
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue(title[i]);
        }
        //shift one row for title
        sheet.shiftRows(0, sheet.getLastRowNum(), 1);
        //add title        
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        String customerSimpleCode = selectCustomerController.getSelectedCustomer();
        SkCustomer skCustomer = skCustomerFacade.findBySimpleCode(customerSimpleCode);
        logger.debug("customerSimpleCode={}", customerSimpleCode);
        logger.debug("skCustoer={}", skCustomer);
        logger.debug("year={}", queryCriteriaController.getFilter().getYear());
        cell.setCellValue(customerSimpleCode + " " + skCustomer.getName() + "   " + queryCriteriaController.getFilter().getYear() + "年");
        //span title column
        if (yearlySalesDetailController.isYearRendered()) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
        } else {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
        }
        //sheet.getLastRowNum();
        // replace all <br/> with \n
        sheet.autoSizeColumn(0);
        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setWrapText(true);
        int index = 0;
        for (Iterator rows = sheet.rowIterator(); rows.hasNext();) {
            HSSFRow arow = (HSSFRow) rows.next();
            if (index++ < 2) {
                continue;
            }
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
//        Integer[] skipColumns = {2};
//        removeUnnecessaryColumns(sheet, skipColumns);
    }

    public void removeUnnecessaryColumns(HSSFSheet sheet, Integer[] removeColumns) {
        for (Iterator it = sheet.iterator(); it.hasNext();) {
            HSSFRow row = (HSSFRow) it.next();
            for (int i = 0; i < removeColumns.length; i++) {
                HSSFCell removeCell = row.getCell(removeColumns[i]);
                int lastCellNum = row.getLastCellNum();
                HSSFCell replaceCell = removeCell;
                HSSFCell shiftCell = null;
                //TODO: 移除最後一個欄位會有問題.
                for (int j = removeColumns[i] + 1; j < lastCellNum; j++) {
                    shiftCell = row.getCell(j);
                    replaceCell.setCellValue(shiftCell.getStringCellValue());
                    replaceCell.setCellStyle(shiftCell.getCellStyle());
                    replaceCell = shiftCell;
                }
                //remove last cell.
                if (shiftCell != null) {
                    row.removeCell(shiftCell);
                }
            }
        }
    }
}
