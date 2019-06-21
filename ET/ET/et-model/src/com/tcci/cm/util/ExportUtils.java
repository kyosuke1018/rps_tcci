/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import com.tcci.cm.enums.NumericPatternEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel匯出工具
 *
 * @author Peter Pan
 */
public class ExportUtils {
    protected final static Logger logger = LoggerFactory.getLogger(ExportUtils.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATE_STR);
    private final static String FONT_EXCEL_DEF = "Serif"; // 確保 autoSizeColumn 有效 (Serif、Sans-serif、Monospaced、Dialog、DialogInput)
    private final static int BASE_WIDTH = 512;
    private final static int MIN_COL_WIDTH = 512 * 3;
    private final static int MAX_COL_WIDTH = 512 * 16;

    /**
     * 共用 Fromat Excel method
     * @param document
     * @param sheetIndex
     * @param colsWidth
     * @param highLightColumns
     * @param bigAmountColumns
     * @param contentColumns
     * @param numericColumns
     * @param numericPatterns 
     * @param removeLastRow 
     */
    public static void formatExcel(Workbook document, 
            int sheetIndex,
            Map<Integer, Integer> colsWidth,
            int[] highLightColumns, 
            int[] bigAmountColumns,
            int[] contentColumns,
            int[] numericColumns,
            Map<String, String> numericPatterns,
            boolean removeLastRow
    ){
        // 刪除 footer row
        if( removeLastRow ){
            removeLastRow((HSSFWorkbook) document, sheetIndex);
        }
        
        // Header 文字過濾
        processHeader((HSSFWorkbook) document, sheetIndex, highLightColumns, 0, 1);
        logger.debug("formatExcel after processHeader...");

        // 大額數值欄位處理
        if (null!=bigAmountColumns && bigAmountColumns.length>0){
            translateXlsBigAmountNumericFields((HSSFWorkbook) document, sheetIndex, bigAmountColumns);
        }
        logger.debug("formatExcel after translateXlsBigAmountNumericFields...");

        //內文 換行文字過濾
        if (null!=contentColumns && contentColumns.length>0){
            processContentFields((HSSFWorkbook) document, sheetIndex, contentColumns);
        }
        logger.debug("formatExcel after processContentFields...");

        // 樣式變更 (目前用於 Format 數字, HighLight)
        if( (null!=numericColumns && numericColumns.length>0) 
                || (null!=highLightColumns && highLightColumns.length>0) ){
            formatXlsStyle((HSSFWorkbook) document
                        , sheetIndex
                        , 1
                        , highLightColumns
                        , numericColumns
                        , numericPatterns);
        }
        logger.debug("formatExcel after highLightFields...");
        
        // 設定預設樣板
        setDefStyle((HSSFWorkbook) document, sheetIndex, colsWidth);
        logger.debug("formatExcel after setDefStyle...");
        
        //autoAdjColWidth((HSSFWorkbook) document);
        //logger.debug("formatExcel after autoAdjColWidth...");
    }
    
    /**
     * Remove a row by its index
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(HSSFSheet sheet, int rowIndex) {
        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum){
            sheet.shiftRows(rowIndex+1,lastRowNum, -1);
        }
        if(rowIndex==lastRowNum){
            HSSFRow removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null){
                sheet.removeRow(removingRow);
            }
        }
    }
    
    /**
     * remove last row
     * @param wb
     * @param sheetIndex 
     */
    public static void removeLastRow(Workbook wb, int sheetIndex){
        HSSFSheet sheet = (HSSFSheet)(wb.getSheetAt(sheetIndex));
        int lastRowNum=sheet.getLastRowNum();
        logger.debug("removeLastRow lastRowNum = "+lastRowNum);
        HSSFRow removingRow=sheet.getRow(lastRowNum);
        if(removingRow!=null){
            sheet.removeRow(removingRow);
        }
    }
    
    /**
     * 都是字串簡易EXCEL處理
     * @param document
     * @param sheetIndex
     * @param colsWidth 
     * @param removeLastRow 
     */
    public static void formatExcelOnlyString(Workbook document, int sheetIndex, Map<Integer, Integer> colsWidth, boolean removeLastRow){
        formatExcel(document, sheetIndex, colsWidth, null, null, null, null, null, removeLastRow);
    }
 
    /**
     * 設定預設樣板
     * 
     * @param wb
     * @param sheetIndex 
     * @param colsWidth 
     */
    public static void setDefStyle(HSSFWorkbook wb, int sheetIndex, Map<Integer, Integer> colsWidth){
        // 預設 style
        HSSFSheet sheet = (HSSFSheet)(wb.getSheetAt(sheetIndex));
        HSSFRow row = sheet.getRow(0);
        int cols = row.getPhysicalNumberOfCells();// 行數
        
        HSSFCellStyle defStyle = wb.createCellStyle();
        defStyle.setWrapText(true);//自動換行
        defStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直置中
        // defStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定 Font 確保可自動欄寬
        HSSFFont font = wb.createFont();
        font.setFontName(FONT_EXCEL_DEF);// 字型 => 確保 autoSizeColumn 有效
        defStyle.setFont(font);
        
        for(int col=0; col<cols; col++){
            sheet.setDefaultColumnStyle(col, defStyle);
        }
        
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)
            row = sheet.getRow(r);
            row.setRowStyle(defStyle);
            for(int col=0; col<cols; col++){
                HSSFCell cell = row.getCell(col);
                cell.setCellStyle(defStyle);
            }
        }       
        
        // 寬度設定
        for(int col=0; col<cols; col++){
            if( colsWidth!=null && colsWidth.get(col)!=null ){
                sheet.setColumnWidth(col, BASE_WIDTH * colsWidth.get(col));// 最小寬度
            }else{
                sheet.autoSizeColumn(col);//自動調整欄寬
                if( sheet.getColumnWidth(col)>MAX_COL_WIDTH ){
                    sheet.setColumnWidth(col, MAX_COL_WIDTH);
                }
                // sheet.setColumnWidth(col, MIN_COL_WIDTH);// 最小寬度
            }
        }
    }
    
    /**
     * 自動調整欄寬
     * @param wb 
     * @param sheetIndex 
     */
    public static void autoAdjColWidth(HSSFWorkbook wb, int sheetIndex){
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        HSSFRow row = sheet.getRow(0);
        int cols = row.getPhysicalNumberOfCells();

        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontName(FONT_EXCEL_DEF);// 字型 => 確保 autoSizeColumn 有效
        // 指定 Font 確保可自動欄寬
        cs.setFont(font);
        
        for(int col=0; col<cols; col++){
            sheet.setDefaultColumnStyle(col, cs);
            sheet.autoSizeColumn(col);//自動調整欄寬
        }
    }
    
    /**
     * 產生HttpResponse Stream，以UTF-8為匯出檔名的編碼。
     *
     * @param workbook
     * @param fileName
     */
    public static void outputReport(Workbook workbook, String fileName) {
        outputReport(workbook, fileName, "UTF-8");
    }

    /**
     * 轉換 workbook to InputStream for primeface download
     * @param workbook
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static InputStream saveWorkbookToInputStream(Workbook workbook, String fileName) throws IOException {
        String savepath = FileUtils.getTempPath();
        String fullname =  savepath + File.separator + "TMP_" + UUID.randomUUID().toString() + "_" + fileName;
        saveWorkbook(workbook, fullname);
        
        return new FileInputStream(fullname);
    }

    /**
     * 儲存 workbook
     * @param workbook
     * @param fullname
     * @throws IOException 
     */
    public static void saveWorkbook(Workbook workbook, String fullname) throws IOException {
        OutputStream outputStream = new FileOutputStream(fullname);
        workbook.write(outputStream);
        outputStream.close();
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
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.debug("ReportEngine: Finish Generate XLS Popup Report Function!");
    }

    /**
     * 產生檔名
     * @param templateName
     * @param extensionName
     * @return 
     * @throws java.io.UnsupportedEncodingException 
     */
    public static String genFileName(String templateName, String extensionName) throws UnsupportedEncodingException {
        final String targetDateStr = sdf.format(new Date());
        templateName = URLEncoder.encode(templateName, "UTF-8"); //匯出中文檔名編碼處理
        String fileName = templateName+"-" + targetDateStr + extensionName;
        
        return fileName;
    }
    
    /**
     * EXCEL 傳為 StreamedContent
     * @param wb
     * @param outputName
     * @param extensionName
     * @return 
     */
    public static StreamedContent genFileStream(Workbook wb, String outputName, String extensionName) {
        FileOutputStream fileOuts;
        StreamedContent sc = null;
        try {
            File xlsFile = File.createTempFile("test", extensionName);

            fileOuts = new FileOutputStream(xlsFile);
            wb.write(fileOuts);
            fileOuts.close();

            return genFileStream(new FileInputStream(xlsFile), outputName, extensionName);
            
            //String fileName = genFileName(templateName, extensionName);
            //sc = new DefaultStreamedContent(new FileInputStream(xlsFile), "application/excel", fileName);
        } catch (IOException e) {
            String msg = "genFileStream exception :\n";
            logger.error(msg, e);
        }     
        return sc;
    }
    
    /**
     * 取得 EXCEL StreamedContent
     * @param stream
     * @param outputName
     * @param extensionName
     * @return 
     */
    public static StreamedContent genFileStream(InputStream stream, String outputName, String extensionName) {
        StreamedContent sc = null;
        try {
            //String fileName = getFileNmae();
            String fileName = genFileName(outputName, extensionName);

            sc = new DefaultStreamedContent(stream, "application/excel", fileName);
        } catch (Exception e) {
            String msg = "genFileStream exception :\n";
            logger.error(msg, e);
        }     
        return sc;
    }    

    /**
     * 過濾標頭 (變更標頭顏色), Freeze
     * @param wb
     * @param sheetIndex
     * @param highLightColumns
     * @param FreezeCol
     * @param FreezeRow 
     */
    public static void processHeader(HSSFWorkbook wb, int sheetIndex, int[] highLightColumns, int FreezeCol, int FreezeRow){
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        sheet.createFreezePane(FreezeCol, FreezeRow); // 凍結窗格

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
        // 標題文字
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗體
        font.setFontName(FONT_EXCEL_DEF);// 字型 => 確保 autoSizeColumn 有效
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);// 置中
        // 標題背景
        // 0B64A0
        //HSSFColor headerColor = setColor(wb, (byte) 0xDD, (byte) 0xDD, (byte) 0xFF);
        HSSFColor headerColor = setColor(wb, (byte) 0x0B, (byte) 0x64, (byte) 0xA0);
        style.setFillForegroundColor(headerColor.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // high light column color
        HSSFColor lightGray = setColor(wb, (byte) 0xFF, (byte)0xFF, (byte) 0x99);
        HSSFCellStyle lightStyle = wb.createCellStyle();
        lightStyle.cloneStyleFrom(style);
        lightStyle.setFillForegroundColor(lightGray.getIndex());

        //header 加入filter掉<br/>功能
        HSSFRow row = sheet.getRow(0);
        if( row==null ){
            logger.error("processHeader row = null !");
            return;
        }
        for(int c=0; c < row.getPhysicalNumberOfCells();c++) {            
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
            // high light column
            for (int i = 0; highLightColumns!=null && i < highLightColumns.length; i++) {
                if( c == highLightColumns[i] ){
                    cell_new.setCellStyle(lightStyle);
                }
            }
            // sheet.autoSizeColumn(c);//自動調整欄寬
        }     
    }

    /**
     * 針對指定欄位轉換為數值欄位
     * @param wb
     * @param sheetIndex
     * @param numericColumns 指定欄位
     */
    public static void translateXlsNumericFields(HSSFWorkbook wb, int sheetIndex, int[] numericColumns){
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)            
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < numericColumns.length; i++) {
                int columnIdx = numericColumns[i];

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
        }    
    }
    
    /**
     * 針對指定欄位轉換為大額數值欄位
     * @param wb
     * @param sheetIndex
     * @param columns 指定欄位
     */
    public static void translateXlsBigAmountNumericFields(HSSFWorkbook wb, int sheetIndex, int[] columns){
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        for (int r = 1; r < rows; r++) { //掃描所有的Row取得資料 (header除外)            
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < columns.length; i++) {
                int columnIdx = columns[i];
                sheet.setColumnWidth(columnIdx, 400 * 10);
            }
        } 
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
     * 針對指定欄位處理換行文字、HTML TAG
     * 
     * @param wb
     * @param sheetIndex
     * @param columns 指定欄位
     */
    public static void processContentFields(HSSFWorkbook wb, int sheetIndex, int[] columns){
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        CellStyle style1 = wb.createCellStyle();
        style1.setWrapText(true);//自動換行
        
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row

        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)            
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; i < columns.length; i++) {
                int columnIdx = columns[i];
                sheet.setColumnWidth(columnIdx, 256 * 40);// 加大寬度

                HSSFCell cell = row.getCell(columnIdx);
                if( cell!=null ){
                    String sV = (String) cell.getStringCellValue();
                    sV = (sV!=null)? sV.replaceAll("<[^>]*>", ""):"";// 清除所有 TAG

                    Cell cell_new = row.createCell(columnIdx);
                    cell_new.setCellValue(sV);

                    cell_new.setCellStyle(style1);
                }
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
    
    /**
     * Format Excel Style
     * @param wb
     * @param sheetIndex
     * @param startRow
     * @param highLightColumns
     * @param numericColumns
     * @param numericPatterns 
     */
    public static void formatXlsStyle(HSSFWorkbook wb
            , int sheetIndex
            , int startRow
            , int[] highLightColumns
            , int[] numericColumns
            , Map<String, String> numericPatterns){
        
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        int rows = sheet.getPhysicalNumberOfRows(); //取得所有的Row
        
        Map<Integer, HSSFCellStyle> colStyles = new HashMap<Integer, HSSFCellStyle>();

        // STEP1 : Style 變更 (只針對準備 CellStyle)
        HSSFColor lightGray = setColor(wb, (byte) 0xFF, (byte)0xFF, (byte) 0x99);
        HSSFRow row0 = sheet.getRow(0);
        if( row0==null ){ return; }
        int cols = row0.getPhysicalNumberOfCells();
        for(int c=0; c<cols; c++){
            boolean needSetStyle = false;
            HSSFCellStyle cs = null;
            
            // STEP1.1 : Numeric Format 欄位
            for (int i = 0; numericColumns!=null && i < numericColumns.length; i++) {
                if( c == numericColumns[i] ){// 數字欄位
                    String patternCode = numericPatterns.get(String.valueOf(c));
                    NumericPatternEnum numericPatternEnum;
                    if( patternCode==null ){// // 無設定 pattern，用預設
                        numericPatternEnum = NumericPatternEnum.Digits3;
                    }else{// 有設定 pattern
                        numericPatternEnum = NumericPatternEnum.fromCode(patternCode);
                    }
                    
                    if (cs == null) {
                        cs = wb.createCellStyle();
                    }
                    if (StringUtils.isNotBlank(numericPatternEnum.getXlsFormat())) {
                        HSSFDataFormat format = wb.createDataFormat();
                        try {
                            //String formatDigits3 = "#,##0.000_);(#,##0.000)";
                            //cs.setDataFormat(format.getFormat(formatDigits3));
                            cs.setDataFormat(format.getFormat(numericPatternEnum.getXlsFormat()));
                        } catch (Exception e) {
                            logger.error("formatXlsStyle Exception : numericPatternEnum.getXlsFormat()) = " + numericPatternEnum.getXlsFormat(), e);
                        }
                    }
                    cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    needSetStyle = true;
                }
            }

            // STEP1.2 : High Light 欄位
            for (int i = 0; highLightColumns!=null && i < highLightColumns.length; i++) {
                if( c == highLightColumns[i] ){
                    if( cs==null ){ cs = wb.createCellStyle(); }
                    cs.setFillForegroundColor(lightGray.getIndex());
                    cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    i = highLightColumns.length;
                    needSetStyle = true;
                }
            }
            
            if( needSetStyle ){
                logger.debug(" setDefaultColumnStyle column = "+c);
                // sheet.setDefaultColumnStyle(c, cs);
                colStyles.put(c, cs);
            } 
        }        
        
        // STEP2 : 內容值變更 (只針對 CellValue, CellType)
        for(int r=1; r<rows; r++){ //掃描所有的Row取得資料 (header除外)
            HSSFRow row = sheet.getRow(r);
            for (int i = 0; numericColumns!=null && i < numericColumns.length; i++) {// 2 數字欄位
                int columnIdx = numericColumns[i];
                //logger.debug("columnIdx = "+columnIdx);
                HSSFCell cell = row.getCell(columnIdx);
                
                // 處理應為數值欄位，但目前是字串格式 (例如有千分位的數字字串)
                String sValue;
                if( cell.getCellType()==Cell.CELL_TYPE_STRING ){
                    sValue = (String) cell.getStringCellValue();
                }else{
                    continue; // 非字串格式不需處裡
                }
                
                sValue = StringUtils.replace(sValue, ",", ""); // 剔除 "," 號
                if (StringUtils.isBlank(sValue)) {
                    sValue = "0";
                }
                Cell cell_new = row.createCell(columnIdx);
                cell_new.setCellType(Cell.CELL_TYPE_NUMERIC);
                
                String pattern = numericPatterns.get(String.valueOf(columnIdx));
                if( pattern!=null ){// 1 有指定 Format
                    NumericPatternEnum numericPatternEnum = NumericPatternEnum.fromCode(pattern);
                    if (numericPatternEnum==null || NumericPatternEnum.DigitsCode == numericPatternEnum) {
                        cell_new.setCellValue(sValue);
                    } else {
                        cell_new.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell_new.setCellValue(Double.valueOf(sValue));
                    }
                }// 1
            }// 2
            
            // 套用 CellStyle
            for(int c=0; c<cols; c++){
                if( colStyles.get(c)!=null ){// 有樣式設定
                    HSSFCell cell = row.getCell(c);
                    cell.setCellStyle(colStyles.get(c));
                }
            }
        }// end of for
        
        sheet.setPrintGridlines(true);
        /*for(int col=0; col<cols; col++){
            sheet.autoSizeColumn(col);//自動調整欄寬
        }*/
    }
    
}
