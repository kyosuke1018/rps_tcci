/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Excel import/export
 * @author Jimmy.Lee
 */
public class ExcelUtil {
    /*
     * 將 List<T> 轉成 excel output stream
     * @param in: excel template
     * @param list: List<T>
     * @param headerRow: excel template row number(0-based), 該列將對映 T 變數名稱
     * @param out: 輸出資料
     * 輸出資料將從 headerRow 下一列開始
     */
    public static <T> void exportList(InputStream in, List<T> list, int headerRow, OutputStream out)
            throws InvalidFormatException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Workbook workbook = WorkbookFactory.create(in);
        exportList(workbook, list, headerRow, out);
    }

    /*
     * 將 List<T> 轉成 excel output stream
     * @param workbook: excel workbook
     * @param list: List<T>
     * @param headerRow: excel template row number(0-based), 該列將對映 T 變數名稱
     * @param out: 輸出資料
     * 輸出資料將從 headerRow 下一列開始
     */
    public static <T> void exportList(Workbook workbook, List<T> list, int headerRow, OutputStream out)
            throws IOException, InvalidFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
                try {
                    Class type = PropertyUtils.getPropertyType(t, field);
                    if (type.getCanonicalName().equals("int") 
                            || type.getCanonicalName().equals("java.lang.Integer")) {
                        Integer value = (Integer) PropertyUtils.getProperty(t, field);
                        cell.setCellValue(value.doubleValue());
                    } else {
                        cell.setCellValue(BeanUtils.getProperty(t, field));
                    }
                } catch (Exception ex) {
                }
            }
            idxRow++;
        }
        workbook.write(out);
    }

    /*
     * 將 excel 轉成 List<T>
     * @param in: excel file
     * @param clazz: T.class
     * @param headerRow: excel file row number(0-based), 該列將對映 T 變數名稱
     * @param maxRecord: 最多筆數 (0 則不限制)
     */
    public static <T> List<T> importList(InputStream in, Class<T> clazz, int headerRow, int maxRecord)
            throws IOException,
                   InvalidFormatException,
                   InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        List<T> result = new ArrayList<T>();
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(0);
        int sheetRows = sheet.getPhysicalNumberOfRows();
        Row header = sheet.getRow(headerRow);
        List<String> fieldNames = getFieldNames(header);
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
            BeanUtils.setProperty(obj, "rowIndex", idxRow + 1); // 1 based.
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
            if (!constraintViolations.isEmpty()) {
                BeanUtils.setProperty(obj, "valid", false);
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                    sb.append(constraintViolation.getMessage()).append('\n');
                }
                BeanUtils.setProperty(obj, "message", sb.toString());
            }
            result.add(obj);
            if (maxRecord > 0 && result.size()>=maxRecord) {
                break;
            }
        }
        return result;
    }
    
    /**
     * 
     * @param <T>
     * @param in
     * @param clazz
     * @param headerRow
     * @param maxRecord
     * @param sheetName
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static <T> List<T> importList(InputStream in, Class<T> clazz, int headerRow, int maxRecord, String sheetName)
            throws IOException,
                   InvalidFormatException,
                   InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException {
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheet(sheetName);
        if(null == sheet){
            return null;
        }
        return importList(sheet, clazz, headerRow, maxRecord);
    } 
    
    public static <T> List<T> importList(Sheet sheet, Class<T> clazz, int headerRow, int maxRecord)
            throws IOException,
                   InvalidFormatException,
                   InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        List<T> result = new ArrayList<T>();
        int sheetRows = sheet.getPhysicalNumberOfRows();
        Row header = sheet.getRow(headerRow);
        List<String> fieldNames = getFieldNames(header);
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
                        try {
                            BeanUtils.setProperty(obj, field, value);
                        } catch (Exception ex) {
                            BeanUtils.setProperty(obj, "valid", false);
                            BeanUtils.setProperty(obj, "message", value + "資料不正確");
                            break;
                        }
                    }
                }
            }
            BeanUtils.setProperty(obj, "rowIndex", idxRow + 1); // 1 based.
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
            if (!constraintViolations.isEmpty()) {
                BeanUtils.setProperty(obj, "valid", false);
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                    sb.append(constraintViolation.getMessage()).append('\n');
                }
                BeanUtils.setProperty(obj, "message", sb.toString());
            }
            result.add(obj);
            if (maxRecord > 0 && result.size()>=maxRecord) {
                break;
            }
        }
        return result;
    }

    // helper
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
        return field.matches("[a-z][a-zA-Z0-9_]*");
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
//                        int d = (int) cell.getNumericCellValue();
//                        return String.valueOf(d);
                        //20160525 資料上傳 數值被轉成整數 bug fixed
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String value_string = cell.getStringCellValue().trim();
                        return value_string;
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
