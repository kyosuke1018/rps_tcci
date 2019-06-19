/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author jason.yu
 */
public class ExcelParserIntegerAccount implements Parser {

    private String fileName;
    private InputStream inputStream;

    @Override
    public Vector parse()throws IOException {
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        wb = new HSSFWorkbook(fs);


        // set the values 
        Vector vector = new Vector();

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
                Vector rowVector = new Vector();
                // loop for every cell in each row 
                for (int c = c1; c < c2; c++) {
                    HSSFCell cell = row.getCell(c);
                    if (cell != null) {
                        String cellValue = getCellValue(cell);
                        if (cellValue != null
                                && cellValue.trim().length() > 0) {
                            // add the value
                            rowVector.add(cellValue);
                        }
                    }
                }
                vector.add(rowVector);
            }
        }
        return vector;
    }  

    private String getCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }

        String result = null;

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
                HSSFCellStyle cellStyle = cell.getCellStyle();
                short dataFormat = cellStyle.getDataFormat();

                // assumption is made that dataFormat = 15, 
                // when cellType is HSSFCell.CELL_TYPE_NUMERIC 
                // is equal to a DATE format. 
                if (dataFormat == 15) {
                    result = cell.getDateCellValue().toString();
                } else {
                    //cell.setCellType(Cell.CELL_TYPE_STRING);
                    //result = cell.getStringCellValue();
                    //result = String.valueOf(cell.getNumericCellValue());
                    double d = cell.getNumericCellValue();
                    result = String.valueOf( Double.valueOf(d).intValue() );
                    
                }

                break;
            case HSSFCell.CELL_TYPE_STRING:
                result = cell.getStringCellValue();
                break;
            default:
                break;
        }
        if( result != null)
            result = result.trim();
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
        inputStream = this.getClass().getResourceAsStream(fileName);
    }
    
    @Override
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    //</editor-fold>
}
