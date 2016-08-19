/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ExportUtil {
    protected final static Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    
    public static <T> void exportToExcel(InputStream in, List<T> list, int headerRow, OutputStream out) 
            throws IOException, InvalidFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(headerRow);
        ArrayList<String> fieldNames = getFieldNames(header);
        int nCols = fieldNames.size();
        int idxRow = headerRow + 1;
        for (T t : list) {            
            Row row = sheet.getRow(idxRow);
            if (row == null) {
                row = sheet.createRow(idxRow);
            }
            for (int idxCol = 0; idxCol < nCols; idxCol++) {
                String field = fieldNames.get(idxCol);
                if (field == null) {
                    continue;
                }
                Cell cell = row.getCell(idxCol);
                if (cell==null) {
                    cell = row.createCell(idxCol);
                }
                CellStyle cs = workbook.createCellStyle();
                cs.setWrapText(true);
                cell.setCellStyle(cs);
                cell.setCellValue(BeanUtils.getProperty(t, field));
            }
            idxRow++;
        }
        workbook.write(out);
    }
    
    private static ArrayList<String> getFieldNames(Row row) {
        ArrayList<String> fieldNames = new ArrayList<String>();
        int maxColIx = row.getLastCellNum();
        for (int colIx = 0; colIx < maxColIx; colIx++) {
            Cell cell = row.getCell(colIx);
            String field = getCellString(cell);
            if (isField(field)) {
                fieldNames.add(field);
            } else {
                fieldNames.add(null);
            }
        }
        fieldNames.trimToSize();
        return fieldNames;
    }

    private static boolean isField(String field) {
        if (field == null) 
            return false;
        return field.matches("[a-z][a-zA-Z_]*");
    }
    
    private static String getCellString(Cell cell) {
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if(DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        return sdf.format(date);
                    } else {
                        int d = (int) cell.getNumericCellValue();
                        return String.valueOf(d);
                    }
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue().trim();
                default:
                    return cell.toString();
            }            
        } catch (Exception ex) {
            return "";
        }
    }
    
}
