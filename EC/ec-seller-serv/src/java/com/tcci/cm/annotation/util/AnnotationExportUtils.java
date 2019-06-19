/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.annotation.util;

import com.tcci.cm.enums.BooleanTypeEnum;
import com.tcci.cm.util.ExportUtils;
import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.model.ExcelFileFieldInfo;
import com.tcci.cm.annotation.model.ExcelMultiSheetVO;
import com.tcci.fc.util.DateUtils;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class AnnotationExportUtils {
    protected final static Logger logger = LoggerFactory.getLogger(AnnotationExportUtils.class);
    
    /**
     * Gereate Multi Sheets Excel Report 
     * @param srcList
     * @return 
     * @throws java.lang.Exception
     */
    public static Workbook gereateMultiSheetExcelReport(List<ExcelMultiSheetVO> srcList) throws Exception{
        if( srcList==null || srcList.isEmpty() ){
            return null;
        }

        Workbook workbook = new HSSFWorkbook(); //Excel97
        
        for(ExcelMultiSheetVO vo : srcList){
            logger.debug("sheet name = "+vo.getName());
            /*if( workbook.getSheet(vo.getName())!=null ){
                workbook.removeName(vo.getName());
            }*/
            Sheet sheet = workbook.createSheet(vo.getName());
            
            generateReportExportFromPOIWithAnnotation(
                vo.getClazz(),
                vo.getDataList(),
                null, // String[] ignoreProperties,
                null, // List<String> headers,
                null, // String templateFileName,
                false, // Boolean isReplaceNull,
                null, // String nullStr,
                false, // boolean isAppendCheckMsg,
                true, // boolean isDynamicGenTitle,
                0, // int dynamicGenTitleIndex,
                workbook,
                sheet);
        }// end of for
        
        return workbook;
    }
    
    /**
     * 產生暫存Excel檔
     * @param clazz
     * @param dataList
     * @param templateFileName
     * @param headers Excel檔的Title，若為null則不寫入(以行為單位)
     * @return 
     * @throws Exception 
     */
    public static Workbook gereateExcelReport(Class clazz
            , List dataList
            , String templateFileName
            , List<String> headers
    ) throws Exception {
        try {
            Workbook wb = generateReportExportFromPOIWithAnnotation(
                            clazz,
                            dataList,
                            headers,    
                            templateFileName);
            logger.info("gereateExcelReport ... ReportEngine: Finish Generate XLS Popup Report Function!");
            return wb;
        } catch (Exception ex) {
            logger.error("gereateReport Exception:\n", ex);
            throw ex;
        }
    }

    /**
     * 依Annotation產生Excel Workbook。不產生檢核訊息。
     *
     * @param clazz
     * @param dataList
     * @param headers Excel檔的Title，若為null則不寫入(以行為單位)
     * @param templateFileName 範例檔路徑
     * @return
     * @throws Exception
     */
    public static Workbook generateReportExportFromPOIWithAnnotation(
            Class clazz,
            List dataList,
            List<String> headers,
            String templateFileName) throws Exception {
        InputStream is;
        Workbook workbook;
        Sheet sheet;
        
        if( templateFileName!=null && !templateFileName.isEmpty() ){
            is = ExportUtils.class.getClassLoader().getResourceAsStream(templateFileName);
            if (null == is) {
                String msg = "InputStream is not found!templateFileName=" + templateFileName;
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            workbook = new HSSFWorkbook(is); //Excel97
            sheet = workbook.getSheetAt(0);
        }else{
            workbook = new HSSFWorkbook(); //Excel97
            sheet = workbook.createSheet("Sheet 1");
        }
        
        return generateReportExportFromPOIWithAnnotation(
                clazz,
                dataList,
                null, // ignoreProperties
                headers,
                templateFileName,
                false,// isReplaceNull
                null, // nullStr
                false,// isAppendCheckMsg
                (templateFileName==null || templateFileName.isEmpty()), // isDynamicGenTitle
                0,
                workbook,
                sheet);
    }
    
    /**
     * 依Annotation產生Excel Workbook，可依參數決定是否產生檢核訊息。
     *
     * @param clazz 資料所屬Class
     * @param dataList 資料內容
     * @param ignoreProperties 不匯出的欄位，傳入VO的property name(將寬度設為0，值改*)
     * @param headers Excel檔的Title，若為null則不寫入(以行為單位)
     * @param templateFileName 範例檔路徑
     * @param isReplaceNull 是否用nullStr參數取代空白字串
     * @param nullStr 空白取代字串(當isReplaceNull=true時，如NA)
     * @param isAppendCheckMsg 是否產生檢核訊息
     * @param isDynamicGenTitle 是否依定義動態匯出抬頭欄位(由Annotation定義的TitleName)
     * @param dynamicGenTitleIndex 動態匯出抬頭時，指定抬頭所在的列數
     * @param workbook
     * @param sheet
     * 
     * @return
     * @throws Exception
     */
    public static Workbook generateReportExportFromPOIWithAnnotation(
            Class clazz,
            List dataList,
            String[] ignoreProperties,
            List<String> headers,
            String templateFileName,
            Boolean isReplaceNull,
            String nullStr,
            boolean isAppendCheckMsg,
            boolean isDynamicGenTitle,
            int dynamicGenTitleIndex,
            Workbook workbook,
            Sheet sheet) throws Exception {

        //取得定義Annotation之exportIndex/ExcelFileFieldInfo Map
        Map<Integer, ExcelFileFieldInfo> fieldInfoMap = ExcelFileFieldInfoUtil.getExportExcelFileFieldMap(clazz);
        if (null == fieldInfoMap || fieldInfoMap.isEmpty()) {
            String msg = clazz.getName() + ": no @ExcelFileFieldInfo found.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        
        //取得檔案資訊
        ExcelFileMeta fileMeta = ExcelFileMetaUtil.getExcelFileInfo(clazz);

        //跳過Header 
        int index = fileMeta.headerRow();
        if (index == 0) {
            index = sheet.getLastRowNum() - 1;
            if( index<0 ){
                index = 0;
            }
        }

        //計算行數
        int cellNum = fieldInfoMap.size();//扣掉跳過不匯出的欄位數
        if (cellNum == 0) {
            cellNum = sheet.getRow(index).getLastCellNum();
        }

        //取得不匯出欄位的Index
        List<Integer> ingnoreIndexes = getIgnoreFieldIndex(cellNum, fieldInfoMap, ignoreProperties);

        //匯出抬頭欄位(由Annotation定義的TitleName 或 另外輸入 headers)
        if( isDynamicGenTitle ){
            // generateDynamicTitle(sheet, dynamicGenTitleIndex, workbook, cellNum, fieldInfoMap, ingnoreIndexes);
            generateDynamicTitle(sheet, dynamicGenTitleIndex, cellNum, fieldInfoMap, ingnoreIndexes, headers);
            index++;
        }

        //產生Excel Workbook
        if (CollectionUtils.isNotEmpty(dataList)) {// 0
            //取得每一筆資料
            for (Object data : dataList) {
                //產生Row
                Row row = sheet.createRow(index);

                for (int i = 0; i < cellNum; i++) { //第j欄
                    //建立Cell                                       
                    Cell cell = row.createCell(i);

                    //不匯出的欄位隱藏，其值以*代替
                    if (ingnoreIndexes.contains(i)) {
                        sheet.setColumnWidth(i, 0);
                        sheet.setColumnHidden(i, true);
                        cell.setCellValue("*");
                        continue;
                    }

                    //取得該欄的Meta/Value
                    ExcelFileFieldInfo fieldInfo = fieldInfoMap.get(i);

                    Object value = PropertyUtils.getProperty(data, fieldInfo.getFieldName());
                    if (null == value) {
                        //若資料內容為空值，依傳入參數決定是否用特殊字串取代
                        if (isReplaceNull) {
                            cell.setCellValue(nullStr);
                        }
                    } else {
                        // cell = 
                        generateCellValue(fieldInfo, value, cell);
                    }
                }
                index++;
            }
            
            /*for(int col=0; col<cellNum; col++){
                sheet.autoSizeColumn(col);//自動調整欄寬
            }*/
        }// 0
        
        return workbook;
    }

    /**
     * 取得不匯出欄位的Index
     *
     * @param cellNum
     * @param fieldInfoMap
     * @param ignoreProperties
     * @return
     */
    private static List<Integer> getIgnoreFieldIndex(int cellNum, Map<Integer, ExcelFileFieldInfo> fieldInfoMap, String[] ignoreProperties) {
        //取得要跳過不匯出欄位有哪些ColumnIndex
        List<Integer> ingnoreIndexes = new ArrayList<Integer>();
        if( ignoreProperties==null ){
            return ingnoreIndexes;
        }
        for (int i = 0; i < cellNum; i++) { //第j欄
            //取得該欄的Meta/Value
            ExcelFileFieldInfo fieldInfo = fieldInfoMap.get(i);
            //if (ArrayUtils.contains(ignoreProperties, fieldInfo.getFieldName())) {
            if( Arrays.asList(ignoreProperties).contains(fieldInfo.getFieldName()) ){
                ingnoreIndexes.add(i);
            }
        }
        return ingnoreIndexes;
    }

    /**
     * 動態產生Title
     *
     * @param sheet
     * @param dynamicGenTitleIndex
     * @param workbook
     * @param cellNum
     * @param fieldInfoMap
     * @param ingnoreIndexes
     */
    private static void generateDynamicTitle(Sheet sheet, int dynamicGenTitleIndex
            , int cellNum
            , Map<Integer, ExcelFileFieldInfo> fieldInfoMap
            , List<Integer> ingnoreIndexes
            , List<String> headers
    ){

        //List<CellStyle> tableHeaderStyles = getTableHeaderStyles(workbook); //JIRAPROCUREMENT-91

        //產生Row
        Row row = sheet.getRow(dynamicGenTitleIndex);
        if (null == row) {
            row = sheet.createRow(dynamicGenTitleIndex);
        }

        for (int i = 0; i < cellNum; i++) { //第j欄
            //取得該欄的Meta/Value
            ExcelFileFieldInfo fieldInfo = fieldInfoMap.get(i);

            //建立Cell                                       
            Cell cell = row.getCell(i);
            if (null == cell) {
                cell = row.createCell(i);
            }

            String sV = fieldInfo.getMetaData().headerName();
            cell.setCellType(Cell.CELL_TYPE_STRING);

            // 另外設定 Header
            if( headers!=null && i<headers.size() && headers.get(i)!=null ){
                sV = headers.get(i);
            }
            
            //若為NotNull欄位，標題要加上*
            if (fieldInfo.isNotNull()) {
                sV = "*" + sV;
            }
            cell.setCellValue(sV);

            //Style
//            int index = fieldInfo.getMetaData().tableHeaderStyleIndex();
//            cell.setCellStyle(tableHeaderStyles.get(index));
//            cell.setCellComment(null);

            //將不匯出的欄位隱藏
            if (ingnoreIndexes.contains(i)) {
                sheet.setColumnWidth(i, 0);
                sheet.setColumnHidden(i, true);
            }

            // sheet.autoSizeColumn(i);//自動調整欄寬
        }
    }

    /**
     * 設定一般欄位的值
     *
     * @param fieldInfo
     * @param value
     * @param oldCell
     * @return
     */
    private static Cell generateCellValue(ExcelFileFieldInfo fieldInfo, Object value, Cell cell) {
        String format;
        //依資料型態產生cell內容
        switch (fieldInfo.getMetaData().dataType()) {
            case INT:
                Integer iv = (Integer) value;
                format = "#,##0";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(iv);
                break;
            case LONG:
                Long lv = (Long) value;
                format = "#,##0";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(lv);
                break;
            case BIG_DECIMAL:
                BigDecimal bv = (BigDecimal) value;
                format = "#,##0.000";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(bv.doubleValue());
                break;
            case DATE:
                Date dv = (Date) value;
                format = "yyyy/mm/dd";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(DateUtils.getISODateStr(dv, "/"));
                break;
            case DATETIME:
                Date dt = (Date) value;
                format = "yyyy/MM/dd HH:mm:ss";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(DateUtils.formatDateString(dt, format));
                break;
            case BOOLEAN:
                Boolean boolV = (Boolean) value;
                cell.setCellType(Cell.CELL_TYPE_STRING);
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                if (boolV) {
                    cell.setCellValue(BooleanTypeEnum.YES.getDisplayName());
                } else {
                    cell.setCellValue(BooleanTypeEnum.NO.getDisplayName());
                }
                break;
            default://STRING or BOOLEAN
                String sV = (String) value;
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.getCellStyle().setWrapText(true);// 自動換行 & Enable換行符號
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(sV);
                break;
        }
        return cell;
    }
}
