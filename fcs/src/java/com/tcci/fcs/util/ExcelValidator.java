/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ExcelValidator {
    private final static Logger logger = LoggerFactory.getLogger(ExcelValidator.class);
    
    private Workbook workbook;
    
    public ExcelValidator(Workbook workbook) {
        this.workbook = workbook;
    }
    
    public boolean isNumericOrBlank(ExcelValidatorItem vaItem) throws InvalidFormatException {
        String sheetName = vaItem.getSheetName();
        String cellRange = vaItem.getCellRange();
        Sheet poiSheet = workbook.getSheet(vaItem.getSheetName());
        if (null == poiSheet && null != vaItem.getSheetEname()) {//中文sheetName查不到 改以英文sheetEname去取資料
            poiSheet = workbook.getSheet(vaItem.getSheetEname());
            if (null != poiSheet) {
                sheetName = vaItem.getSheetEname();
            }
        }
        if (null == poiSheet) {
            String error = MessageFormat.format("sheet:{0} not exist!", sheetName);
            throw new InvalidFormatException(error);
        }
        CellRangeAddress poiRange = CellRangeAddress.valueOf(cellRange);
        for (int row=poiRange.getFirstRow(); row<=poiRange.getLastRow(); row++) {
            Row poiRow = poiSheet.getRow(row);
            if (null == poiRow) {
                String error = MessageFormat.format("sheet:{0}, row {1} 不存在!", 
                        sheetName, row+1);
                logger.error(error);
                throw new InvalidFormatException(error);
            }
            for (int col=poiRange.getFirstColumn(); col<=poiRange.getLastColumn(); col++) {
                Cell poiCell = poiRow.getCell(col);
                CellReference cellRef = new CellReference(row, col);
                if (null == poiCell) {
                    String error = MessageFormat.format("sheet:{0}, cell {1} 不存在!", 
                            sheetName, cellRef.formatAsString());
                    logger.error(error);
                    throw new InvalidFormatException(error);
                }
                int cellType = poiCell.getCellType();
                if (cellType == Cell.CELL_TYPE_BLANK || cellType == Cell.CELL_TYPE_NUMERIC) {
                    return true;
                }
                else if (cellType == Cell.CELL_TYPE_FORMULA) {
                    switch (poiCell.getCachedFormulaResultType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            return true;
                        default:
                            String error = MessageFormat.format("sheet:{0}, cell {1} 必需是數字或空白", 
                                    sheetName, cellRef.formatAsString());
                            logger.error(error);
                            throw new InvalidFormatException(error);
                    }
                } else {
                    logger.error("cellType:{}", cellType);
                    String error = MessageFormat.format("sheet:{0}, cell {1} 必需是數字或空白", 
                            sheetName, cellRef.formatAsString());
                    logger.error(error);
                    throw new InvalidFormatException(error);
                }
            }
        }
        return true;
    }
    
    public boolean isNumericOrBlank(String sheetNmae, int startRow, int endRow, int col) throws InvalidFormatException {
        Sheet poiSheet = workbook.getSheet(sheetNmae);
        if (null == poiSheet) {
            String error = MessageFormat.format("sheet:{0} not exist!", sheetNmae);
            throw new InvalidFormatException(error);
        }
        for (int row=startRow; row<=endRow; row++) {
            Row poiRow = poiSheet.getRow(row);
            if (null == poiRow) {
                String error = MessageFormat.format("sheet:{0}, row {1} 不存在!", 
                        sheetNmae, row+1);
                logger.error(error);
                throw new InvalidFormatException(error);
            }
            Cell poiCell = poiRow.getCell(col);
            CellReference cellRef = new CellReference(row, col);
            if (null == poiCell) {
                String error = MessageFormat.format("sheet:{0}, cell {1} 不存在!",
                        sheetNmae, cellRef.formatAsString());
                logger.error(error);
                throw new InvalidFormatException(error);
            }
            int cellType = poiCell.getCellType();
            if (cellType == Cell.CELL_TYPE_BLANK || cellType == Cell.CELL_TYPE_NUMERIC) {
                return true;
            }
            else if (cellType == Cell.CELL_TYPE_FORMULA) {
                switch (poiCell.getCachedFormulaResultType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        return true;
                    default:
                        String error = MessageFormat.format("sheet:{0}, cell {1} 必需是數字或空白",
                                sheetNmae, cellRef.formatAsString());
                        logger.error(error);
                        throw new InvalidFormatException(error);
                }
            } else {
                logger.error("cellType:{}", cellType);
                String error = MessageFormat.format("sheet:{0}, cell {1} 必需是數字或空白",
                        sheetNmae, cellRef.formatAsString());
                logger.error(error);
                throw new InvalidFormatException(error);
            }
        }
        return true;
    }
    
    public String getCompCode(Sheet sheet, int row) {
        Row poiRow = sheet.getRow(row);
        if (null == poiRow) {
            return null;
        }
        Cell poiCell = poiRow.getCell(0);
        if (null == poiCell) {
            return null;
        }
        int cellType = poiCell.getCellType();
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf((int) poiCell.getNumericCellValue());
        } else {
            return StringUtils.trimToNull(poiCell.toString());
        }
    }
    
    public BigDecimal getCellValue(Sheet sheet, int row, int col) throws InvalidFormatException {
        Row poiRow = sheet.getRow(row);
        if (null == poiRow) {
            return null;
        }
        Cell poiCell = poiRow.getCell(col);
        int cellType = poiCell.getCellType();
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            return new BigDecimal(poiCell.getNumericCellValue());
        } else if (cellType == Cell.CELL_TYPE_FORMULA && 
                poiCell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
            return new BigDecimal(poiCell.getNumericCellValue());
        }else if(cellType == Cell.CELL_TYPE_BLANK){
            return null;
        }
        CellReference cellRef = new CellReference(poiCell);
        String error = MessageFormat.format("sheet:{0}, cell {1} 必需是數字或空白", 
                sheet.getSheetName(), cellRef.formatAsString());
        logger.error(error);
        throw new InvalidFormatException(error);
    }
    
    //取得會計科目
    public String getCellValueString(Sheet sheet, int row, int col) {
        Row poiRow = sheet.getRow(row);
        if (null == poiRow) {
            return null;
        }
        Cell poiCell = poiRow.getCell(col);
        if (null == poiCell) {
            return null;
        }
        int cellType = poiCell.getCellType();
        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf((int) poiCell.getNumericCellValue());
        } else {
            return StringUtils.trimToNull(poiCell.toString());
        }
    }
    
    public void setReportVersion(Sheet sheet, int row, int col, String str) {
        Row poiRow = sheet.getRow(row);
        if (null == poiRow) {
            return;
        }
        Cell poiCell = poiRow.getCell(col);
        if (null == poiCell) {
            poiCell = poiRow.createCell(col);
        }
        
        poiCell.setCellValue(str);
        CellStyle style = poiCell.getCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        poiCell.setCellStyle(style);
    }
    
}
