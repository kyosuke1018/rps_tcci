/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.util;

/**
 *
 * @author Jimmy.Lee
 */
public class ExcelValidatorItem {
    private String sheetName;
    private String sheetEname;
    private String cellRange;

    public ExcelValidatorItem(String sheetName, String cellRange) {
        this.sheetName = sheetName;
        this.cellRange = cellRange;
    }
    
    public ExcelValidatorItem(String sheetName, String sheetEname, String cellRange) {
        this.sheetName = sheetName;
        this.sheetEname = sheetEname;
        this.cellRange = cellRange;
    }
    
    // getter, setter
    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetEname() {
        return sheetEname;
    }

    public void setSheetEname(String sheetEname) {
        this.sheetEname = sheetEname;
    }

    public String getCellRange() {
        return cellRange;
    }

    public void setCellRange(String cellRange) {
        this.cellRange = cellRange;
    }

}
