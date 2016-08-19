/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
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
public class ImportUtil {
    protected final static Logger logger = LoggerFactory.getLogger(ImportUtil.class);

    public static <T> List<T> importFromExcle(InputStream in, Class<T> clazz, int headerRow, int maxRecord)
            throws IOException, 
                   InvalidFormatException, 
                   InstantiationException, 
                   IllegalAccessException, 
                   InvocationTargetException {
        List<T> result = new ArrayList<T>();
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(0);
        int sheetRows = sheet.getPhysicalNumberOfRows();
        Row header = sheet.getRow(headerRow);
        ArrayList<String> fieldNames = getFieldNames(header);
        int nCols = fieldNames.size();
        for (int idxRow = headerRow + 1; idxRow < sheetRows; idxRow++) {
            Row row = sheet.getRow(idxRow);
            T obj = clazz.newInstance();
            if (row != null) {
                for (int idxCol = 0; idxCol < nCols; idxCol++) {
                    Cell cell = row.getCell(idxCol);
                    String field = fieldNames.get(idxCol);
                    String value = getCellString(cell);
                    if (field != null && value != null
                            && !field.isEmpty() && !value.isEmpty()) {
                        BeanUtils.setProperty(obj, field, value);
                    }
                }
            }
            result.add(obj);
            if (maxRecord > 0 && result.size()>=maxRecord) {
                break;
            }
        }
        return result;
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
