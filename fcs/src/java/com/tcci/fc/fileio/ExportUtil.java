/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.fileio;

import com.tcci.fc.util.ExcelUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fc.fileio.annotation.ExcelFileMeta;
import com.tcci.fc.fileio.annotation.util.ExcelFileFieldInfoUtil;
import com.tcci.fc.fileio.annotation.util.ExcelFileMetaUtil;
import com.tcci.fc.enums.NumericPatternEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel匯出工具-匯出格式為Excel97-2003
 *
 * @author Jackson.Lee
 */
public class ExportUtil {

    protected final static Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 產生HttpResponse Stream，以BIG5為匯出檔名的編碼。
     *
     * @param workbook
     * @param fileName
     */
    public static void outputReport(Workbook workbook, String fileName) {
        outputReport(workbook, fileName, "BIG5");
    }

    /**
     * 產生HttpResponse Stream，以傳入參數fileNameEncoding為匯出檔名的編碼。
     *
     * @param workbook
     * @param fileName
     * @param fileNameEncoding
     */
    public static void outputReport(Workbook workbook, String fileName, String fileNameEncoding) {
        try {
            String fName = new String(fileName.getBytes(fileNameEncoding), "ISO_8859_1");
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setHeader("Cache-Control", "max-age=0");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=" + fName);
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            context.responseComplete();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.debug("ReportEngine: Finish Generate XLS Popup Report Function!");
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
    public static Workbook generateReportExportFromPOIWithAnnotation(
            Class clazz,
            List dataList,
            String templateFileName) throws Exception {
        return generateReportExportFromPOIWithAnnotation(
                clazz,
                dataList,
                null,
                null,
                templateFileName,
                false,
                null,
                false,
                false,
                0);
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
        return generateReportExportFromPOIWithAnnotation(
                clazz,
                dataList,
                null,
                headers,
                templateFileName,
                false,
                null,
                false,
                false,
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

        java.io.InputStream is = ExportUtil.class.getClassLoader().getResourceAsStream(templateFileName);
        if (null == is) {
            String msg = "InputStream is not found!templateFileName=" + templateFileName;
            logger.error(msg);
            throw new RuntimeException(msg);
        }

        Workbook workbook = new HSSFWorkbook(is); //Excel97
        Sheet sheet = workbook.getSheetAt(0);

//        //第一行Header的字型
//        CellStyle headerStyle1 = workbook.createCellStyle();
//        headerStyle1.setAlignment(CellStyle.ALIGN_CENTER);
//        headerStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        Font headerFont1 = workbook.createFont();
//        headerFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        headerFont1.setFontHeightInPoints((short) 24); //第一行header字型設為24
//        headerFont1.setUnderline((byte) 1);//有底線
//        headerStyle1.setFont(headerFont1);
//        
//        CellStyle headerStyle2 = workbook.createCellStyle();
//        headerStyle2.cloneStyleFrom(headerStyle1);
//        headerStyle2.setAlignment(CellStyle.ALIGN_LEFT);
//        Font headerFont2 = workbook.createFont();
//        headerFont2.setFontHeightInPoints((short)16);
//        headerStyle2.setFont(headerFont2);

        List<CellStyle> headerStyles = getHeaderStyles(workbook);

        //依傳入參數產生headers
        if (CollectionUtils.isNotEmpty(headers)) {
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
        }
        
        //取得檔案資訊
        ExcelFileMeta fileMeta = ExcelFileMetaUtil.getExcelFileInfo(clazz);

        //跳過Header 
        int index = fileMeta.headerRow();
        if (index == 0) {
            index = sheet.getLastRowNum() - 1;
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
            generateDynamicTitle(sheet, dynamicGenTitleIndex, workbook, cellNum, fieldInfoMap, ingnoreIndexes);
        }

        //產生Excel Workbook
        if (CollectionUtils.isNotEmpty(dataList)) {
            
            CellStyle chkStyle = getCheckMessageStyle(workbook);
            CellStyle sysGenStyle = getSysGenStyle(workbook);
            CellStyle generalStyle = getGeneralDataStyle(workbook);

            //增加Highlight功能 2012/7/31
            String highlightColumn = fileMeta.highlightColumnDef();
            CellStyle highlightStyle = getHighlightStyle(workbook);
            

            //取得每一筆資料
            for (Object data : dataList) {
                //System.out.println("generateFileFromTemplate i=" + i);

                //產生Row
                Row row = sheet.createRow(index);

                //依highlightColumn決定是否要更換為highlightStyle 2012/7/31
                boolean isHighlight = false;
                if (StringUtils.isNotBlank(highlightColumn)) {
                    isHighlight = (Boolean) PropertyUtils.getProperty(data, highlightColumn);
                }

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
                    if (fieldInfo.getMetaData().isSystemGenerate()) {
                        cell.setCellStyle(sysGenStyle);
                    } //檢核欄位
                    else if (fieldInfo.getMetaData().isCheckMessage()) {
                        cell.setCellStyle(chkStyle);
                    } else {
                        if (isHighlight){
                            cell.setCellStyle(highlightStyle);
                        }else{
                            cell.setCellStyle(generalStyle);
                        }
                    }


                    Object value = PropertyUtils.getProperty(data, fieldInfo.getFieldName());
                    if (null == value) {
                        //若資料內容為空值，依傳入參數決定是否用特殊字串取代
                        if (isReplaceNull) {
                            cell.setCellValue(nullStr);
                        }
                    } else if (fieldInfo.getMetaData().isCheckMessage()) {
                        //產生檢核訊息.....
                        if (isAppendCheckMsg) {
                            String sV = (String) value;
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(sV);
                        }
                    } else {
                        cell = generateCellValue(fieldInfo, value, cell);
                    }

                }
                index++;
            }
        }
        return workbook;
    }

    /**
     * 一般字型
     *
     * @param workbook
     * @return
     */
    private static CellStyle getGeneralDataStyle(Workbook workbook) {
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 14); //字型設為14
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setFont(dataFont);
        return generalStyle;
    }

    /**
     * 系統產生欄位Style: 用黃色
     *
     * @param workbook
     * @return
     */
    private static CellStyle getSysGenStyle(Workbook workbook) {
        Font sysGenFont = workbook.createFont();
        sysGenFont.setFontHeightInPoints((short) 14); //字型設為14
        CellStyle sysGenStyle = workbook.createCellStyle();
        sysGenStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        sysGenStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        sysGenStyle.setFont(sysGenFont);
        return sysGenStyle;
    }

    /**
     * 設定檢核訊息的字型及Style
     *
     * @param workbook
     * @return
     */
    private static CellStyle getCheckMessageStyle(Workbook workbook) {
        CellStyle chkStyle = workbook.createCellStyle();
        chkStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        chkStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());//系統產生欄位用黃色
        chkStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font chkFont = workbook.createFont();
        chkFont.setColor(IndexedColors.RED.getIndex());
        chkFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        chkFont.setFontHeightInPoints((short) 14); //其它字型設為14
        chkStyle.setFont(chkFont);
        return chkStyle;
    }

    /**
     * 設定Highlight的字型及Style
     *
     * @param workbook
     * @return
     */
    private static CellStyle getHighlightStyle(Workbook workbook) {
        Font styleFont = workbook.createFont();
        styleFont.setFontHeightInPoints((short) 14); //字型設為14
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(styleFont);
        return style;
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
        String format = "";
        //依資料型態產生cell內容
        switch (fieldInfo.getMetaData().dataType()) {
            case INT:
            case LONG:
                Long lv = (Long) value;
                format = "#,##0";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(lv.longValue());
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
            case BOOLEAN:
                Boolean boolV = (Boolean) value;
                cell.setCellType(Cell.CELL_TYPE_STRING);
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                if (boolV) {
                    cell.setCellValue("TRUE");
                } else {
                    cell.setCellValue("FALSE");
                }
                break;
            default://STRING or BOOLEAN
                String sV = (String) value;
                format = "General";
                cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(sV);
                break;
        }
        return cell;
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
    private static void generateDynamicTitle(Sheet sheet, int dynamicGenTitleIndex, Workbook workbook, int cellNum,
            Map<Integer, ExcelFileFieldInfo> fieldInfoMap, List<Integer> ingnoreIndexes) {

        List<CellStyle> tableHeaderStyles = getTableHeaderStyles(workbook); //JIRAPROCUREMENT-91

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

            //若為NotNull欄位，標題要加上*
            if (fieldInfo.isNotNull()) {
                sV = "*" + sV;
            }
            cell.setCellValue(sV);

            //Style
            int index = fieldInfo.getMetaData().tableHeaderStyleIndex();
            cell.setCellStyle(tableHeaderStyles.get(index));

            //Comment 註解
//            String commentStr = fieldInfo.getMetaData().tableHeaderComment();
//            if (StringUtils.isNotBlank(commentStr)){
//                HSSFPatriarch patr = ((HSSFSheet)sheet).createDrawingPatriarch();
//                HSSFComment  comment = patr.createCellComment(new HSSFClientAnchor());
//                CreationHelper createHelper = oldCell.getSheet().getWorkbook().getCreationHelper();
//                comment.setString(createHelper.createRichTextString(commentStr));
//                oldCell.setCellComment(comment);
//            }else{
            cell.setCellComment(null);
//            }

            //將不匯出的欄位隱藏
            if (ingnoreIndexes.contains(i)) {
                sheet.setColumnWidth(i, 0);
                sheet.setColumnHidden(i, true);
            }

            sheet.autoSizeColumn(i);//自動調整欄寬
        }
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
            if (ArrayUtils.contains(ignoreProperties, fieldInfo.getFieldName())) {
                ingnoreIndexes.add(i);
            }
        }
        return ingnoreIndexes;
    }

    /**
     * 定義Header Styles。
     *
     * @return
     */
    public static List<CellStyle> getHeaderStyles(Workbook workbook) {
        List<CellStyle> headerStyles = new ArrayList<CellStyle>();

        //第一行Header的字型
        CellStyle headerStyle1 = workbook.createCellStyle();
        headerStyle1.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font headerFont1 = workbook.createFont();
        headerFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont1.setFontHeightInPoints((short) 24); //第一行header字型設為24
        headerFont1.setUnderline((byte) 1);//有底線
        headerStyle1.setFont(headerFont1);
        headerStyles.add(headerStyle1);

        //預設Header的字型
        CellStyle headerStyle2 = workbook.createCellStyle();
        headerStyle2.cloneStyleFrom(headerStyle1);
        headerStyle2.setAlignment(CellStyle.ALIGN_LEFT);
        Font headerFont2 = workbook.createFont();
        headerFont2.setFontHeightInPoints((short) 16);
        headerStyle2.setFont(headerFont2);
        headerStyles.add(headerStyle2);

        return headerStyles;
    }

    /**
     * 定義Header Styles，供表格動態產生標題使用。
     *
     * @return
     */
    public static List<CellStyle> getTableHeaderStyles(Workbook workbook) {
        List<CellStyle> headerStyles = new ArrayList<CellStyle>();
        
        //預設字型
        CellStyle titleStyle1 = workbook.createCellStyle(); //default
        titleStyle1.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle1.setBorderTop((short) 1); // single line border
        titleStyle1.setBorderBottom((short) 1); // single line border
        titleStyle1.setBorderLeft((short) 1);
        titleStyle1.setBorderRight((short) 1);
        
        HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
        palette.setColorAtIndex(new Byte((byte) 41), new Byte((byte) 183), new Byte((byte) 222), new Byte((byte) 232));
        titleStyle1.setFillForegroundColor(palette.getColor(41).getIndex()); //填藍色
        titleStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        Font titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) 14); //其它字型設為14
        titleStyle1.setFont(titleFont);
        headerStyles.add(titleStyle1);
        
        CellStyle titleStyle2 = workbook.createCellStyle();
        titleStyle2.cloneStyleFrom(titleStyle1);
        palette.setColorAtIndex(new Byte((byte) 42), new Byte((byte) 252), new Byte((byte) 213), new Byte((byte) 180));
        titleStyle2.setFillForegroundColor(palette.getColor(42).getIndex()); //填橘色
        headerStyles.add(titleStyle2);
        
        return headerStyles;
    }
    public static StreamedContent genFile(Workbook wb, String templateName, String extensionName) {
        FileOutputStream fileOuts = null;
        StreamedContent sc = null;
        try {
            
            File xlsFile = File.createTempFile("test", extensionName);
            
            fileOuts = new FileOutputStream(xlsFile);
            wb.write(fileOuts);
            fileOuts.close();
            
            //String fileName = getFileNmae();
            String fileName = ExcelUtils.genFileName(templateName, extensionName);
            
            sc = new DefaultStreamedContent(new FileInputStream(xlsFile), "application/excel", fileName);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            String msg = "xlsFile not found!";
            logger.error(msg, e);
        }
        return sc;
    }
    
    /**
     * 取得 EXCEL StreamedContent
     * @param stream
     * @param templateName
     * @param extensionName
     * @return
     */
    public static StreamedContent genFile(InputStream stream, String templateName, String extensionName) {
        StreamedContent sc = null;
        try {
            //String fileName = getFileNmae();
            String fileName = ExcelUtils.genFileName(templateName, extensionName);
            
            sc = new DefaultStreamedContent(stream, "application/excel", fileName);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            String msg = "xlsFile not found!";
            logger.error(msg, e);
        }
        return sc;
    }
    
    /**
     * 過濾標頭 (變更標頭顏色)
     * @param wb
     */
    public static void processHeader(HSSFWorkbook wb){
        HSSFSheet sheet = wb.getSheetAt(0);
        
        // header cellStyle
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        
        HSSFFont font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        HSSFColor headerColor = setColor(wb, (byte) 0xDD, (byte) 0xDD, (byte) 0xFF);
        style.setFillForegroundColor(headerColor.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        //header 加入filter掉<br/>功能
        HSSFRow row = sheet.getRow(4);
        for(int c=0; c < row.getLastCellNum();c++) {
            HSSFCell cell = row.getCell(c);
            String cellValue = (String) cell.getStringCellValue();
            
            cellValue = StringUtils.replace(cellValue, "<br/>", "");
            cellValue = StringUtils.replace(cellValue, "<br />", "");
            cellValue = StringUtils.replace(cellValue, "<BR/>", "");
            cellValue = StringUtils.replace(cellValue, "<BR />", "");
            
            Cell cell_new = row.createCell(c);
            cell_new.setCellValue(cellValue);
            
            // set header cellStyle
            cell_new.setCellStyle(style);
        }
    }
    public static void processHeader(HSSFWorkbook wb, HSSFSheet sheet){
        // header cellStyle
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        
        HSSFFont font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        HSSFColor headerColor = setColor(wb, (byte) 0xDD, (byte) 0xDD, (byte) 0xFF);
        style.setFillForegroundColor(headerColor.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        //header 加入filter掉<br/>功能
        HSSFRow row = sheet.getRow(0);
        if(row != null){
            for(int c=0; c < row.getPhysicalNumberOfCells();c++) {
                HSSFCell cell = row.getCell(c);
                String cellValue = (String) cell.getStringCellValue();
                
//            cellValue = StringUtils.replace(cellValue, "<br/>", "");
//            cellValue = StringUtils.replace(cellValue, "<br />", "");
//            cellValue = StringUtils.replace(cellValue, "<BR/>", "");
//            cellValue = StringUtils.replace(cellValue, "<BR />", "");
                
                Cell cell_new = row.createCell(c);
                cell_new.setCellValue(cellValue);
                
                // set header cellStyle
                cell_new.setCellStyle(style);
            }
        }
    }
    
    public static CellStyle borderStyle(CellStyle style, Short border) {
	// header cellStyle
        style.setBorderRight(border);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(border);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(border);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(border);
        style.setTopBorderColor(HSSFColor.BLACK.index);
	
	return style;
    }
    
    
    /**
     * 針對指定欄位轉換為數值欄位
     * @param wb
     * @param numericColumns 指定欄位
     */
    public static void translateXlsNumericFields(HSSFWorkbook wb, int[] numericColumns){
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            //for(int c=0; c < row.getPhysicalNumberOfCells();c++) {
            for (int i = 0; i < numericColumns.length; i++) {
                int columnIdx = numericColumns[i];
                
                //if(c != columnIdx){//非數字不處理
                //    continue;
                //}
                HSSFCell cell = row.getCell(columnIdx);
                String sV = (String) cell.getStringCellValue();
                sV = StringUtils.replace(sV, ",", "");
                if (StringUtils.isBlank(sV)){
                    sV = "0";
                }
                Cell cell_new = row.createCell(columnIdx);
                cell_new.setCellValue(Double.valueOf(sV));
                String format = "#,##0.000";
                cell_new.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                cell_new.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            //}
        }
    }
    
    /**
     * 針對指定欄位轉換為數值欄位
     * @param wb
     * @param numericColumns 指定欄位
     */
    public static void translateXlsNumericFields(HSSFWorkbook wb, int[] numericColumns,String[] numericPatterns){
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFCellStyle  cellStyle = wb.createCellStyle();
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            //for(int c=0; c < row.getPhysicalNumberOfCells();c++) {
            for (int i = 0; i < numericColumns.length; i++) {
                int columnIdx = numericColumns[i];
                
                //if(c != columnIdx){//非數字不處理
                //    continue;
                //}
                HSSFCell cell = row.getCell(columnIdx);
                String sValue = (String) cell.getStringCellValue();
                sValue = StringUtils.replace(sValue, ",", "");
                if (StringUtils.isBlank(sValue)){
                    sValue = "0";
                }
                Cell cell_new = row.createCell(columnIdx);
                
                
                String pattern = null;
                try {
                    pattern = numericPatterns[i];
                } catch (Exception e) {
                }
                NumericPatternEnum numericPatternEnum = NumericPatternEnum.fromCode(pattern);
                if(NumericPatternEnum.Integer == numericPatternEnum){
                    cell_new.setCellValue(sValue);
                }else{
                    cell_new.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell_new.setCellValue(Double.valueOf(sValue));
                    logger.debug("columnIdx="+columnIdx+"|pattern="+pattern+"|value="+cell_new.getNumericCellValue());
                }
                if (StringUtils.isEmpty(pattern)){
                    pattern = "#,##0.000";
                }
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(pattern));
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                
                cell_new.setCellStyle(cellStyle);
                
            }
            //}
        }
    }
    /**
     * 針對指定欄位轉換為大額數值欄位
     * @param wb
     * @param columns 指定欄位
     */
    public static void translateXlsBigAmountNumericFields(HSSFWorkbook wb, int[] columns){
        HSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for (int r = 1; r < rows; r++) { //掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < columns.length; i++) {
                int columnIdx = columns[i];
                
//                HSSFCell oldCell = row.getCell(columnIdx);
//                String sV = (String) oldCell.getStringCellValue();
//                sV = StringUtils.replace(sV, ",", "");
//                if (StringUtils.isBlank(sV)) {
//                    sV = "0";
//                }
//                Cell cell_new = row.createCell(columnIdx);
//                cell_new.setCellValue(Double.valueOf(sV));
//                String format = "#,##0.00";//大額數字情況 不四捨五入到小數第一位
//                newStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
//                cell_new.setCellStyle(newStyle);
                sheet.setColumnWidth(columnIdx, 400 * 10);
//                cell_new.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
        }
    }
    
    /**
     *  High Light 指定欄位
     * @param wb
     * @param highLightColumns
    public static void highLightFields(HSSFWorkbook wb, int[] highLightColumns, int startRow) {
        // High Light Style
        CellStyle newStyle = wb.createCellStyle();
        HSSFColor lightGray = setColor(wb, (byte) 0xFF, (byte)0xFF, (byte) 0x99);
        // cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        
        
        // 逐筆處理
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=startRow; r<rows; r++){ //1 掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < highLightColumns.length; i++) { // High Light 欄位
                HSSFCell oldCell = row.getCell(highLightColumns[i]);
                newStyle.cloneStyleFrom(oldCell.getCellStyle());
                newStyle.setFillForegroundColor(lightGray.getIndex());
                newStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                oldCell.setCellStyle(newStyle);
            }
        }//1
    }*/
    public static void highLightFields(HSSFWorkbook wb, int[] highLightColumns) {
        highLightFields(wb, highLightColumns, 0);
    }
    public static void highLightFields(HSSFWorkbook wb, int[] highLightColumns, int startRow) {
        highLightFields(wb, highLightColumns, startRow, 0);
    }
    /**
     *  High Light 指定欄位
     * @param wb
     * @param highLightColumns
     * @param startRow 指定起始列
     * @param sheetIndex 指定sheet
     */
    public static void highLightFields(HSSFWorkbook wb, int[] highLightColumns, int startRow, int sheetIndex) {
        // High Light Style
        CellStyle newStyle = wb.createCellStyle();
        HSSFColor lightGray = setColor(wb, (byte) 0xFF, (byte)0xFF, (byte) 0x99);
        // cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        
        // 逐筆處理
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=startRow; r<rows; r++){ //1 掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < highLightColumns.length; i++) { // High Light 欄位
                HSSFCell oldCell = row.getCell(highLightColumns[i]);
                newStyle.cloneStyleFrom(oldCell.getCellStyle());
                newStyle.setFillForegroundColor(lightGray.getIndex());
                newStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                oldCell.setCellStyle(newStyle);
            }
        }//1
    }
    
    /**
     * 自訂顏色
     * @param workbook
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static HSSFColor setColor(HSSFWorkbook workbook, byte r,byte g, byte b){
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor= palette.findColor(r, g, b);
            if (hssfColor == null ){
                palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g,b);
                hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
            }
        } catch (Exception e) {
            logger.error("=== setColor Exception ! ===", e);
        }
        
        return hssfColor;
    }
    
    /**
     * 針對指定欄位處理換行文字
     * @param wb
     * @param columns 指定欄位
     */
    public static void processContentFields(HSSFWorkbook wb, int[] columns){
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < columns.length; i++) {
                int columnIdx = columns[i];
                
                HSSFCell cell = row.getCell(columnIdx);
                String sV = (String) cell.getStringCellValue();
                sV = StringUtils.replace(sV, "<p class=\"wordBreak\">", "");
                sV = StringUtils.replace(sV, "</p>", "");
                sV = StringUtils.replace(sV, "<br/>", "");
                
                Cell cell_new = row.createCell(columnIdx);
                cell_new.setCellValue(sV);
            }
        }
    }
    
    /**
     * 針對指定Sheet 指定名稱
     * @param wb
     * @param sheetNum 指定Sheet
     * @param SheetName 名稱
     */
    public static void setSheetName(HSSFWorkbook wb, int sheetNum, String SheetName){
        wb.setSheetName(sheetNum, SheetName);
    }
    
    public static void writeCell(Row row, int col, String str) {
	Cell cell = row.getCell(col);
	if (null == cell) {
	    cell = row.createCell(col);
	}
	cell.setCellType(Cell.CELL_TYPE_STRING);
	cell.setCellValue(str);
    }
    
    //AutoSize
    public static void processAutoSize(HSSFWorkbook wb, int length){
        processAutoSize(wb, length, 0);
    }
    public static void processAutoSize(HSSFWorkbook wb, int length, int sheetIndex){
        HSSFSheet sheet = wb.getSheetAt(0);
        for(int i=0;i<length;i++){
            sheet.autoSizeColumn(i);
        }
    }
    
}
