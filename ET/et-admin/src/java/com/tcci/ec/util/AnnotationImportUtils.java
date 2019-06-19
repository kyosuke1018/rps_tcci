/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.util;

import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import com.tcci.cm.annotation.model.ExcelFileFieldInfo;
import com.tcci.cm.annotation.util.ExcelFileFieldInfoUtil;
import com.tcci.cm.annotation.util.ExcelFileMetaUtil;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ImportUtils;
//import com.tcci.ec.model.ImportTcc;
//import com.tcci.ec.model.rs.ImportProductVO;
//import com.tcci.ec.model.ImportTccDealerVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class AnnotationImportUtils {
    private final static Logger logger = LoggerFactory.getLogger(AnnotationImportUtils.class);
    
    /**
     * 匯入商品 EXCEL
     * @param fileStream
     * @param contentType
     * @param sheetIndex
     * @param resList
     * @param impCodesList
     * @param impNamesList
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    /*
    public static int importProducts(InputStream fileStream, String contentType, int sheetIndex, Locale locale,
            List<ImportProductVO> resList)
            throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        ExcelFileMeta excelFileMeta = ExcelFileMetaUtil.getExcelFileInfo(ImportProductVO.class);
        Map<Integer, ExcelFileFieldInfo> fieldInfoMap = ExcelFileFieldInfoUtil.getImportExcelFileFieldMap(ImportProductVO.class);
        if (excelFileMeta==null || null == fieldInfoMap || fieldInfoMap.isEmpty()) {
            String msg = ImportProductVO.class.getName() + ": no @ExcelFileFieldInfo found.";
            logger.error(msg);
            return -1;
        }
        
        Workbook workbook = ImportUtils.getWorkBook(fileStream, contentType);
        if( workbook==null ){
            logger.error("importProducts not supported ContentType = "+contentType);
            return -1;
        }

        Sheet sheet = workbook.getSheetAt(sheetIndex);// 指定 Sheet
        Iterator<Row> rowIterator = sheet.iterator();
        
        boolean isHearder = true;
        int rowNum = 0;
        int noDataRow = 0;
        int MAX_COL = 80;// 過濾明顯多於空白行
        while (rowIterator.hasNext()) {// 2
            rowNum++;
            if( rowNum-excelFileMeta.headerRow()-noDataRow > GlobalConstant.MAX_IMPORT_NUM ){
                logger.error("importProducts rowNum = "+rowNum+" > "+GlobalConstant.MAX_IMPORT_NUM);
                return 0;
            }
            //StringBuilder sb = new StringBuilder();
            Row row = rowIterator.next();
            if( isHearder ){// 3
                logger.info("importProducts isHearder = "+isHearder+", rowNum="+rowNum+", headerRow="+excelFileMeta.headerRow());
                isHearder = (rowNum < excelFileMeta.headerRow());
            }else{
                logger.info("importProducts rowNum = "+rowNum);
                ImportProductVO obj = new ImportProductVO();
                obj.setErrList(new ArrayList<String>());// for 儲存錯誤
                int colIndex = 1;
                int col = 0;
                boolean hasData = false;
                //logger.info("importProducts getPhysicalNumberOfCells = "+row.getPhysicalNumberOfCells());
                //for(col=0; col<row.getPhysicalNumberOfCells(); col++){// not counting empty ones
                logger.info("importProducts getLastCellNum = "+row.getLastCellNum());
                for(col=0; col<row.getLastCellNum() && col<MAX_COL; col++){// 改用 row.getLastCellNum()
                    String errMsg = null;
                    ExcelFileFieldInfo colInfo = fieldInfoMap.get(colIndex);
                    if( colInfo==null ){
                        logger.error("importProducts colInfo is null, colIndex = "+colIndex);
                        continue;
                    }
                    Cell cell = row.getCell(col);

                    String cellValue = "";
                    if( cell!=null ){
                        String fieldName = colInfo.getFieldName();// 欄位名稱
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:// 3
                                cellValue = null;
                                break;
                            case Cell.CELL_TYPE_STRING:// 1
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:// 0
                                if (DateUtil.isCellDateFormatted(cell)) {// 判断是否是日期格式
                                    cellValue = DateUtils.formatDate(cell.getDateCellValue());
                                } else {// 數字
                                    try{
                                        cellValue = Double.toString(cell.getNumericCellValue());
                                        if( colInfo.getMetaData().dataType()==DataTypeEnum.STRING ){// 避免數字代碼變科學符號數字
                                            int e = cellValue.indexOf("E");
                                            if( cellValue.indexOf(".")>0 && e>0 ){
                                                cellValue = cellValue.substring(0, e);
                                                cellValue = StringUtils.replace(cellValue, ".", "");
                                            }
                                        }else{
                                            // 多餘小數點
                                            cellValue = cellValue.endsWith(".0")? cellValue.substring(0, cellValue.length()-2):cellValue;
                                        }
                                        logger.info("importProducts cell.getNumericCellValue()="+cell.getNumericCellValue()
                                            +", cellValue="+cellValue
                                            +", colInfo.getMetaData().dataType()="+colInfo.getMetaData().dataType()
                                            );
                                    }catch(Exception e){
                                        logger.error("importProducts exception　rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getNumericCellValue() = "+cell.getNumericCellValue(), e);
                                    }
                                }
                                break;
                            //case Cell.CELL_TYPE_BOOLEAN:// 4
                            //    cell.getBooleanCellValue();
                            //    break;
                            case Cell.CELL_TYPE_FORMULA:// 2
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0001");//不可輸入EXCEL公式或超連結!
                                cellValue = null;
                                logger.error("importTccMembers FORMULA rowNum="+rowNum+", col="+col+", fieldName="+fieldName);
                                break;
                            default:
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0002");//"資料格式有誤!";
                                logger.error("importTccMembers rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getCellType() ="+cell.getCellType());
                        }
                        logger.debug("importProducts col="+col+", colIndex="+colIndex+", colInfo.isOptional()="+colInfo.isOptional()+", "+fieldName+"="+cellValue);

                        // 設定至 Class Object
                        if( cellValue!=null && StringUtils.isNotEmpty(cellValue.trim()) ){
                            cellValue = cellValue.trim();
                            hasData = true;
                            try{
                                ExcelFileFieldInfoUtil.setObjProperty(obj, colInfo, fieldName, cellValue);
                            }catch(Exception e){
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0002");//"資料格式有誤!";
                                logger.error("importProducts rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getCellType() ="+cell.getCellType()+"\n", e);
                            }
                        }else if( !colInfo.isOptional() ){
                            logger.error("importProducts required col="+col+", colIndex="+colIndex+", colInfo.isOptional()="+colInfo.isOptional()+", "+fieldName+"="+cellValue);
                            errMsg = ResourceBundleUtils.getMessage(locale, "required.field");// 為必填欄位
                        }
                        // 錯誤資料格式有誤紀錄
                        if( errMsg!=null ){
                            String headerName = (colInfo.getMetaData()!=null && StringUtils.isNotBlank(colInfo.getMetaData().headerName()))? 
                                    colInfo.getMetaData().headerName():null;
                            String headerLabel = (headerName!=null)? ResourceBundleUtils.getMessage(locale, headerName):null;
                            // 第 {0} 欄
                            headerLabel = headerLabel==null?MessageFormat.format(ResourceBundleUtils.getMessage(locale, "msg.imp.0003"), colIndex):headerLabel;
                            errMsg = headerLabel + errMsg;
                            obj.getErrList().add(errMsg);
                        }
                    }

                    colIndex++;
                }// 1
                
                noDataRow = hasData? 0:noDataRow+1;
                obj.setHasData(hasData);
                obj.setRowNum(rowNum);// 列數
                resList.add(obj);
                
                if( hasData ){
                    //
                }else{
                    // 連續 10 列都無資料系統視為該 EXCEL已讀取完畢，不再往下讀取。
                    if( noDataRow>=10 ){
                        logger.info("importProducts noDataRow>=5, rowNum="+rowNum);
                        break;
                    }
                }
            }// end of else 3
        }// end of while 2
        
        return 1;
    }
    */
    
    /**
     * 匯入台泥經銷商下游客戶(攪拌站、檔口)
     * @param fileStream
     * @param contentType
     * @param sheetIndex
     * @param locale
     * @param resList
     * @param clazz
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException 
     */
    /*
    public static int importTccMembers(InputStream fileStream, String contentType, int sheetIndex, Locale locale, List resList, Class clazz)
            throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
        ExcelFileMeta excelFileMeta = ExcelFileMetaUtil.getExcelFileInfo(clazz);
        Map<Integer, ExcelFileFieldInfo> fieldInfoMap = ExcelFileFieldInfoUtil.getImportExcelFileFieldMap(clazz);
        
        if (excelFileMeta==null || null == fieldInfoMap || fieldInfoMap.isEmpty()) {
            String msg = ImportTcc.class.getName() + ": no @ExcelFileFieldInfo found.";
            logger.error(msg);
            return -1;
        }
        
        Workbook workbook = ImportUtils.getWorkBook(fileStream, contentType);
        if( workbook==null ){
            logger.error("importTccMembers not supported ContentType = "+contentType);
            return -2;
        }

        Sheet sheet = workbook.getSheetAt(sheetIndex);// 指定 Sheet
        Iterator<Row> rowIterator = sheet.iterator();
        
        boolean isHearder = true;
        int rowNum = 0;
        int noDataRow = 0;
        int MAX_COL = 80;// 過濾明顯多於空白行
        while (rowIterator.hasNext()) {// 2
            rowNum++;
            if( rowNum-excelFileMeta.headerRow()-noDataRow > GlobalConstant.MAX_IMPORT_NUM ){
                logger.error("importTccMembers rowNum = "+rowNum+" > "+GlobalConstant.MAX_IMPORT_NUM);
                return -3;
            }
            //StringBuilder sb = new StringBuilder();
            Row row = rowIterator.next();
            if( isHearder ){// 3
                logger.info("importTccMembers isHearder = "+isHearder+", rowNum="+rowNum+", headerRow="+excelFileMeta.headerRow());
                isHearder = (rowNum < excelFileMeta.headerRow());
            }else{
                logger.info("importTccMembers rowNum = "+rowNum);
                ImportTcc obj = (ImportTcc)clazz.newInstance();
                obj.setErrList(new ArrayList<String>());// for 儲存錯誤
                int colIndex = 1;
                boolean hasData = false;
                //logger.info("importTccMembers getPhysicalNumberOfCells = "+row.getPhysicalNumberOfCells());
                //for(col=0; col<row.getPhysicalNumberOfCells(); col++){// not counting empty ones
                logger.info("importTccMembers getLastCellNum = "+row.getLastCellNum());
                for(int col=0; col<row.getLastCellNum() && col<MAX_COL; col++){// 改用 row.getLastCellNum()
                    String errMsg = null;
                    ExcelFileFieldInfo colInfo = fieldInfoMap.get(colIndex);
                    if( colInfo==null ){
                        logger.error("importTccMembers colInfo is null, colIndex = "+colIndex);
                        continue;
                    }
                    Cell cell = row.getCell(col);

                    String cellValue = "";
                    if( cell!=null ){
                        String fieldName = colInfo.getFieldName();// 欄位名稱
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:// 3
                                cellValue = null;
                                break;
                            case Cell.CELL_TYPE_STRING:// 1
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:// 0
                                if (DateUtil.isCellDateFormatted(cell)) {// 判断是否是日期格式
                                    cellValue = DateUtils.formatDate(cell.getDateCellValue());
                                } else {// 數字
                                    try{
                                        cellValue = Double.toString(cell.getNumericCellValue());
                                        if( colInfo.getMetaData().dataType()==DataTypeEnum.STRING ){// 避免數字代碼變科學符號數字
                                            int e = cellValue.indexOf("E");
                                            if( cellValue.indexOf(".")>0 && e>0 ){
                                                cellValue = cellValue.substring(0, e);
                                                cellValue = StringUtils.replace(cellValue, ".", "");
                                            }
                                        }else{
                                            // 多餘小數點
                                            cellValue = cellValue.endsWith(".0")? cellValue.substring(0, cellValue.length()-2):cellValue;
                                        }
                                    }catch(Exception e){
                                        logger.error("importTccMembers rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getNumericCellValue() = "+cell.getNumericCellValue(), e);
                                    }
                                }
                                break;
                            //case Cell.CELL_TYPE_BOOLEAN:// 4
                            //    cell.getBooleanCellValue();
                            //    break;
                            case Cell.CELL_TYPE_FORMULA:// 2
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0001");//不可輸入EXCEL公式或超連結!
                                cellValue = null;
                                logger.error("importTccMembers FORMULA rowNum="+rowNum+", col="+col+", fieldName="+fieldName);
                                break;
                            default:
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0002");//"資料格式有誤!";
                                logger.error("importTccMembers rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getCellType() ="+cell.getCellType());
                        }
                        logger.debug("importTccMembers col="+col+", colIndex="+colIndex+", colInfo.isOptional()="+colInfo.isOptional()
                                    +", cell.getCellType() ="+cell.getCellType()+", "+fieldName+"="+cellValue);

                        // 設定至 Class Object
                        if( cellValue!=null && StringUtils.isNotEmpty(cellValue.trim()) ){
                            cellValue = cellValue.trim();
                            hasData = true;
                            try{
                                ExcelFileFieldInfoUtil.setObjProperty(obj, colInfo, fieldName, cellValue);
                            }catch(Exception e){
                                errMsg = ResourceBundleUtils.getMessage(locale, "msg.imp.0002");//"資料格式有誤!";
                                logger.error("importTccMembers rowNum="+rowNum+", col="+col+", fieldName="+fieldName+", cell.getCellType() ="+cell.getCellType()+"\n", e);
                            }
                        }else if( !colInfo.isOptional() ){
                            logger.error("importProducts required col="+col+", colIndex="+colIndex+", colInfo.isOptional()="+colInfo.isOptional()+", "+fieldName+"="+cellValue);
                            errMsg = ResourceBundleUtils.getMessage(locale, "required.field");// 為必填欄位
                        }
                    }else{
                        if( !colInfo.isOptional() ){
                            logger.error("importProducts required cell==null, col="+col+", colIndex="+colIndex+", colInfo.isOptional()="+colInfo.isOptional());
                            errMsg = ResourceBundleUtils.getMessage(locale, "required.field");// 為必填欄位
                        }
                    }
                    // 錯誤資料格式有誤紀錄
                    if( errMsg!=null ){
                        String headerName = (colInfo.getMetaData()!=null && StringUtils.isNotBlank(colInfo.getMetaData().headerName()))? 
                                colInfo.getMetaData().headerName():null;
                        String headerLabel = (headerName!=null)? ResourceBundleUtils.getMessage(locale, headerName):null;
                        // 第 {0} 欄
                        headerLabel = headerLabel==null?MessageFormat.format(ResourceBundleUtils.getMessage(locale, "msg.imp.0003"), colIndex):headerLabel;
                        errMsg = headerLabel + errMsg;
                        obj.getErrList().add(errMsg);
                        obj.setHasError(true);
                    }

                    colIndex++;
                }// 1
                
                noDataRow = hasData? 0:noDataRow+1;
                obj.setHasData(hasData);
                obj.setRowNum(rowNum);// 列數
                resList.add(obj);
                logger.debug("importTccMembers rowNum="+rowNum+", hasData="+obj.isHasData()+", hasError="+obj.isHasError());
                if( hasData ){
                    //
                }else{
                    // 連續 20 列都無資料系統視為該 EXCEL已讀取完畢，不再往下讀取。
                    if( noDataRow>=20 ){
                        logger.info("importTccMembers noDataRow>=5, rowNum="+rowNum);
                        break;
                    }
                }
            }// end of else 3
        }// end of while 2
        
        return 1;
    }   
    */
}
