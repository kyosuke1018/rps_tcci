/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author jason.yu
 */
public class ExcelParser implements Parser {

    private InputStream inputStream;

    @Override
    public void setFileName(String fileName) {
        inputStream = this.getClass().getResourceAsStream(fileName);
    }

    @Override
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public List<List<Object>> parse()throws IOException {
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        wb = new HSSFWorkbook(fs);

        // set the values 
        List<List<Object>> values = new ArrayList<List<Object>>();

        // loop for every worksheet in the workbook 
        int numOfSheets = wb.getNumberOfSheets();
        for (int i = 0; i < numOfSheets; i++) {
            HSSFSheet sheet = wb.getSheetAt(i);

            // loop for every row in each worksheet 
            for (Iterator rows = sheet.rowIterator();
                    rows.hasNext();) {
                HSSFRow row = (HSSFRow) rows.next();
                short c1 = row.getFirstCellNum();
                short c2 = row.getLastCellNum();
                List<Object> rowList = new ArrayList<Object>();                
                // loop for every cell in each row 
                for (int c = c1; c < c2; c++) {
                    HSSFCell cell = row.getCell(c);
                    Object cellValue = getCellValue(cell);
                    // add the value
                    rowList.add(cellValue);
                }
                values.add(rowList);
            }
        }
        return values;
    }

    private Object getCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }

        Object result = null;

        int cellType = cell.getCellType();
        switch (cellType) {
            case HSSFCell.CELL_TYPE_BLANK:
                result = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                result = cell.getBooleanCellValue()
                        ? "true" : "false";
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                result = "ERROR: " + cell.getErrorCellValue();
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                result = cell.getCellFormula();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                boolean isDateFormat = HSSFDateUtil.isCellDateFormatted(cell);
                if (isDateFormat) {
                    result = cell.getDateCellValue();
                } else {
                    //bug: 5960310439 -> 5.96030541E9(double) -> 2147483647 (int)
                    //double d = cell.getNumericCellValue();
                    //result = String.valueOf( Double.valueOf(d).intValue() );
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    result = cell.getStringCellValue().trim();
                }

                break;
            case HSSFCell.CELL_TYPE_STRING:
                result = cell.getStringCellValue().trim();
                break;
            default:
                break;
        }
        return result;
    }
}
