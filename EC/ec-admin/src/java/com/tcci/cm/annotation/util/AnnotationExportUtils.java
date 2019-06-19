/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.annotation.util;


import com.tcci.cm.annotation.ExcelFileMeta;
import com.tcci.cm.annotation.model.ExcelFileFieldInfo;
import com.tcci.cm.util.DateUtil;
import com.tcci.cm.util.ExportUtils;
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
import org.apache.poi.ss.usermodel.CellType;
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
     * 產生暫存Excel檔
     * @param clazz
     * @param dataList
     * @param outputFileFullName
     * @param templateFileName
     * @param headers Excel檔的Title，若為null則不寫入(以行為單位)
     * @throws Exception 
     */
    public static Workbook gereateExcelReport(Class clazz
            , List dataList, String outputFileFullName
            , String templateFileName
            , List<String> headers
    ) throws Exception {
        try {
            Workbook wb = generateReportExportFromPOIWithAnnotation(
                            clazz,
                            dataList,
                            headers,    
                            templateFileName);
            logger.info("ReportEngine: Finish Generate XLS Popup Report Function!");
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
     * @param templateFileName 範例檔路徑
     * @return
     * @throws Exception
     */
//    public static Workbook generateReportExportFromPOIWithAnnotation(
//            Class clazz,
//            List dataList,
//            String templateFileName) throws Exception {
//        return generateReportExportFromPOIWithAnnotation(
//                clazz,
//                dataList,
//                null,
//                null,
//                templateFileName,
//                false,
//                null,
//                false,
//                true, // isDynamicGenTitle
//                0);
//    }

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
        return generateReportExportFromPOIWithAnnotation(
                clazz,
                dataList,
                null, // ignoreProperties
                headers,
                templateFileName,
                false,// isReplaceNull
                null, // nullStr
                false,// isAppendCheckMsg
                true, // isDynamicGenTitle
                0);
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
            int dynamicGenTitleIndex) throws Exception {

        //取得定義Annotation之exportIndex/ExcelFileFieldInfo Map
        Map<Integer, ExcelFileFieldInfo> fieldInfoMap = ExcelFileFieldInfoUtil.getExportExcelFileFieldMap(clazz);
        if (null == fieldInfoMap || fieldInfoMap.isEmpty()) {
            String msg = clazz.getName() + ": no @ExcelFileFieldInfo found.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }

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
            //File xlsFile = File.createTempFile("test", "xls");
            //is = new FileInputStream(xlsFile);
            
            workbook = new HSSFWorkbook(); //Excel97
            sheet = workbook.createSheet("Sheet 1");
        }

        // Workbook workbook = new HSSFWorkbook(is); //Excel97
        // Sheet sheet = workbook.getSheetAt(0);

        //List<CellStyle> headerStyles = getHeaderStyles(workbook);

        //依傳入參數產生headers
        /*if (CollectionUtils.isNotEmpty(headers)) {
            for (int i = 0; i < headers.size(); i++) {
                Row headerRow = sheet.createRow(i);
                Cell cell = headerRow.createCell(0);
                cell.setCellValue(headers.get(i));
                if (i == 0) {//第一行設定為最大字體
                    cell.setCellStyle(headerStyles.get(0));
                } else {
                    cell.setCellStyle(headerStyles.get(1));
                }
            }
        }*/
        
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

        //匯出抬頭欄位(由Annotation定義的TitleName)
        if (isDynamicGenTitle) {
            // generateDynamicTitle(sheet, dynamicGenTitleIndex, workbook, cellNum, fieldInfoMap, ingnoreIndexes);
            generateDynamicTitle(sheet, dynamicGenTitleIndex, cellNum, fieldInfoMap, ingnoreIndexes);
            index++;
        }

        //產生Excel Workbook
        if (CollectionUtils.isNotEmpty(dataList)) {// 0
//            CellStyle chkStyle = ExportUtils.getCheckMessageStyle(workbook);
//            CellStyle sysGenStyle = ExportUtils.getSysGenStyle(workbook);
//            CellStyle generalStyle = ExportUtils.getGeneralDataStyle(workbook);
//
//            //增加Highlight功能 2012/7/31
//            String highlightColumn = fileMeta.highlightColumnDef();
//            CellStyle highlightStyle = ExportUtils.getHighlightStyle(workbook);    

            //取得每一筆資料
            for (Object data : dataList) {
                //System.out.println("generateFileFromTemplate i=" + i);

                //產生Row
                Row row = sheet.createRow(index);

                //依highlightColumn決定是否要更換為highlightStyle 2012/7/31
//                boolean isHighlight = false;
//                if (StringUtils.isNotBlank(highlightColumn)) {
//                    isHighlight = (Boolean) PropertyUtils.getProperty(data, highlightColumn);
//                }

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

                    //設定系統產生欄位Style
//                    if (fieldInfo.getMetaData().isSystemGenerate()) {
//                        cell.setCellStyle(sysGenStyle);
//                    } //檢核欄位
//                    else if (fieldInfo.getMetaData().isCheckMessage()) {
//                        cell.setCellStyle(chkStyle);
//                    } else {
//                        if (isHighlight){
//                            cell.setCellStyle(highlightStyle);
//                        }else{
//                            cell.setCellStyle(generalStyle);
//                        }
//                    }

                    Object value = PropertyUtils.getProperty(data, fieldInfo.getFieldName());
                    if (null == value) {
                        //若資料內容為空值，依傳入參數決定是否用特殊字串取代
                        if (isReplaceNull) {
                            cell.setCellValue(nullStr);
                        }
//                    } else if (fieldInfo.getMetaData().isCheckMessage()) {
//                        //產生檢核訊息.....
//                        if (isAppendCheckMsg) {
//                            String sV = (String) value;
//                            cell.setCellType(Cell.CELL_TYPE_STRING);
//                            cell.setCellValue(sV);
//                        }
                    } else {
                        cell = generateCellValue(fieldInfo, value, cell);
                    }
                }
                index++;
            }
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
        for (int i = 0; i < cellNum; i++) { //第j欄
            //取得該欄的Meta/Value
            ExcelFileFieldInfo fieldInfo = fieldInfoMap.get(i);
            
            if( ignoreProperties!=null && Arrays.asList(ignoreProperties).contains(fieldInfo.getFieldName()) ){
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
            // , Workbook workbook
            , int cellNum,
            Map<Integer, ExcelFileFieldInfo> fieldInfoMap, List<Integer> ingnoreIndexes) {

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
            cell.setCellType(CellType.STRING); //Cell.CELL_TYPE_STRING);

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
                cell.setCellType(CellType.NUMERIC); //Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(iv.doubleValue());
                break;
            case LONG:
                Long lv = (Long) value;
                format = "#,##0";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.NUMERIC); //Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(lv.longValue());
                break;
            case BIG_DECIMAL:
                BigDecimal bv = (BigDecimal) value;
                format = "#,##0.000";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.NUMERIC); //Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(bv.doubleValue());
                break;
            case DATE:
                Date dv = (Date) value;
                format = "yyyy/MM/dd";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.STRING);// Cell.CELL_TYPE_STRING);
                cell.setCellValue(DateUtil.getISODateStr(dv, "/"));
                break;
            case DATEHHMM:
                Date dv1 = (Date) value;
                format = "yyyy/MM/dd HH:mm";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.STRING);// Cell.CELL_TYPE_STRING);
                cell.setCellValue(DateUtil.formatDateString(dv1, format));
                break;
            case DATETIME:
                Date dv2 = (Date) value;
                format = "yyyy/MM/dd HH:mm:ss";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.STRING);// Cell.CELL_TYPE_STRING);
                cell.setCellValue(DateUtil.formatDateString(dv2, format));
                break;
            case BOOLEAN:
                Boolean boolV = (Boolean) value;
                cell.setCellType(CellType.STRING);// Cell.CELL_TYPE_STRING);
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                if (boolV) {
                    cell.setCellValue("是");
                } else {
                    cell.setCellValue("否");
                }
                break;
            default://STRING or BOOLEAN
                String sV = (String) value;
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(CellType.STRING);// Cell.CELL_TYPE_STRING);
                cell.setCellValue(sV);
                break;
        }
        return cell;
    }
}
