package com.tcci.fc.util;

import com.tcci.fc.util.time.DateUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilbert
 */
public class ExcelUtils {
    private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static final int MAX_EXCEL_EXPORT_SIZE = 65535;//Excel最大匯出筆數
    public static final String EXCEL_FILE_EXT = ".xls";
    public static final String EXCEL_APP_TYPE = "application/vnd.ms-excel";
    
    //TODO:產生檔名用改時分秒
    private static SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
    
    /**
     * 拷贝行并填充数据
     *
     * @param pSourceSheetIdx 源表单名称  start form 0 
     * @param pTargetSheetIdx 目标表单名称 start form 0 
     * @param pStartRow 起始读取行 start form 0 
     * @param pEndRow 结束读取行 start form 0 
     * @param pPosition 目标保存 start form 0 
     */
    public static void copyRows(HSSFSheet fromsheet, HSSFSheet newsheet, int pStartRow,
            int pEndRow, int pPosition) {
        HSSFRow sourceRow;
        HSSFRow targetRow;
        HSSFCell sourceCell;
        HSSFCell targetCell;
        HSSFSheet sourceSheet;
        HSSFSheet targetSheet;
        int cType;
        int i;
        int j;
        int targetRowFrom;
        int targetRowTo;

        if ((pStartRow == -1) || (pEndRow == -1)) {
            return;
        }
        sourceSheet = fromsheet;
        targetSheet = newsheet;

        List<CellRangeAddress> oldRanges = new ArrayList<CellRangeAddress>();
        for (i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            oldRanges.add(sourceSheet.getMergedRegion(i));
        }

        // 拷贝合并的单元格。原理：复制当前合并单元格后，原位置的格式会移动到新位置，需在原位置生成旧格式
        for (int k = 0; k < oldRanges.size(); k++) {
            CellRangeAddress oldRange = oldRanges.get(k);
            CellRangeAddress newRange = new CellRangeAddress(oldRange.getFirstRow(), oldRange.getLastRow(),
                    oldRange.getFirstColumn(), oldRange.getLastColumn());

            if (oldRange.getFirstRow() >= pStartRow && oldRange.getLastRow() <= pEndRow) {
                targetRowFrom = oldRange.getFirstRow() - pStartRow + pPosition;
                targetRowTo = oldRange.getLastRow() - pStartRow + pPosition;
                oldRange.setFirstRow(targetRowFrom);
                oldRange.setLastRow(targetRowTo);
                targetSheet.addMergedRegion(oldRange);
                sourceSheet.addMergedRegion(newRange);
            }
        }
        // 设置列宽
        for (i = pStartRow; i <= pEndRow; i++) {
            sourceRow = sourceSheet.getRow(i);
            if (sourceRow != null) {
                for (j = sourceRow.getLastCellNum(); j > sourceRow.getFirstCellNum(); j--) {
                    targetSheet.setColumnWidth(j, sourceSheet.getColumnWidth(j));
                    targetSheet.setColumnHidden(j, false);
                }
                break;
            }
        }
        // 拷贝行并填充数据
        for (; i <= pEndRow; i++) {
            sourceRow = sourceSheet.getRow(i);
            if (sourceRow == null) {
                continue;
            }
            targetRow = targetSheet.createRow(i - pStartRow + pPosition);
            targetRow.setHeight(sourceRow.getHeight());
            for (j = sourceRow.getFirstCellNum(); j < sourceRow.getPhysicalNumberOfCells(); j++) {
                sourceCell = sourceRow.getCell(j);
                if (sourceCell == null) {
                    continue;
                }
                targetCell = targetRow.createCell(j);
                // targetCell.setEncoding(((Object) sourceCell).getEncoding());
                targetCell.setCellStyle(sourceCell.getCellStyle());
                cType = sourceCell.getCellType();
                targetCell.setCellType(cType);
                switch (cType) {
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        // System.out.println("--------TYPE_BOOLEAN:" +
                        // targetCell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                        // System.out.println("--------TYPE_ERROR:" +
                        // targetCell.getErrorCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        // parseFormula这个函数的用途在后面说明
                        targetCell.setCellFormula(parseFormula(sourceCell.getCellFormula()));
                        // System.out.println("--------TYPE_FORMULA:" +
                        // targetCell.getCellFormula());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        // System.out.println("--------TYPE_NUMERIC:" +
                        // targetCell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        targetCell.setCellValue(sourceCell.getRichStringCellValue());
                        // System.out.println("--------TYPE_STRING:" + i +
                        // targetCell.getRichStringCellValue());
                        break;
                }
            }
        }
    }

    private static String parseFormula(String pPOIFormula) {
        final String cstReplaceString = "ATTR(semiVolatile)"; //$NON-NLS-1$
        StringBuilder result = new StringBuilder();
        int index;

        index = pPOIFormula.indexOf(cstReplaceString);
        if (index >= 0) {
            result.append(pPOIFormula.substring(0, index));
            result.append(pPOIFormula.substring(index + cstReplaceString.length()));
        } else {
            result.append(pPOIFormula);
        }

        return result.toString();
    }
    /**
     * getCoIIdx
     * @param address 'O,4'
     * @return 
     */
    public static int getCoIIdx(String address) {
        int pointer_col = 0;
        String[] list = address.split(",");
        String col = list[0];
        int loop = 0;
        for (int i = (col.toCharArray().length-1); i >= 0; i--) {
            char  ch = col.toCharArray()[i];
            int A = (int)'A';
            pointer_col += ((int)ch - ((int)'A'-1))+(25*loop);
//            System.out.println("pointer_col="+pointer_col);
            loop++;
        }
        pointer_col--;
        return pointer_col;
    }
    /**
     * 
     * @param address 'O,4'
     * @return 
     */
    public static int getRowIdx(String address) {
        String[] list = address.split(",");
        String col = list[1];
        int intValue = Integer.valueOf(col);
        int pointer_row = intValue -1;
        return pointer_row;
    }    
    public static void main(String args[]) {
        String para = "AA,4";
        System.out.println(ExcelUtils.getCoIIdx(para));
        System.out.println(ExcelUtils.getRowIdx(para));
    }
    public static int getIndex(String address,IndexEnum indexEnum) {
        if(indexEnum == IndexEnum.Column){
            return ExcelUtils.getCoIIdx(address);
        }else if(indexEnum == IndexEnum.Row){
            return ExcelUtils.getRowIdx(address);
        }
        return 0;
    } 
    public static String genFileName(String templateName, String extensionName) {
        assert StringUtils.isNotEmpty(templateName);
        assert StringUtils.isNotEmpty(extensionName);
        Date targetDate = new Date();
        final String targetDateStr = sdf.format(targetDate);
        String fileName = templateName+"-" + targetDateStr + extensionName;
        return fileName;
    }     
    public enum IndexEnum {
        Column("Column", "Column")
        ,Row("Row", "Row")
        ;

        private String code;
        private String name;    
        IndexEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }    

        public String getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static IndexEnum fromCode(String code) {
            if (code != null) {
                for (IndexEnum achievementColEnum : IndexEnum.values()) {
                    if (code.equals(achievementColEnum.getCode())) {
                        return achievementColEnum;
                    }
                }
            }
            return null;
        }       
    }   
    
    /**
     * 取得 EXCEL StreamedContent
     * @param wb
     * @param templateName
     * @param extensionName
     * @return 
     */
    public static StreamedContent genFile(Workbook wb, String templateName, String extensionName) {
        StreamedContent sc = null;    
        FileOutputStream fileOuts;
        try {

            File xlsFile = File.createTempFile("test", extensionName);

            fileOuts = new FileOutputStream(xlsFile);
            wb.write(fileOuts);
            fileOuts.close();

            //String fileName = getFileNmae();
            String fileName = ExcelUtils.genFileName(templateName, extensionName);

            sc = new DefaultStreamedContent(new FileInputStream(xlsFile), "application/excel", fileName);
        } catch (FileNotFoundException e) {
            logger.error("genFile FileNotFoundException:\n", e);
        } catch (IOException e) {
            logger.error("genFile IOException:\n", e);
        }
        return sc;
    }
    
    /**
     * 合併 Excel 檔
     * @param inFileNames
     * @param outFileName
     * @param onlyKeepFirstHearder 只保留第一個 Header
     * @return 
     */
    public static int mergeExcel(List<String> inFileNames, String outFileName, boolean onlyKeepFirstHearder){
        try {
            int row_counts = 0;
            
            for(int i=0; i<inFileNames.size(); i++){
                logger.debug("workbook.write "+i+"... START");
                
                FileInputStream file = new FileInputStream(new File(inFileNames.get(i)));

                //Get the workbook instance for XLS file 
                HSSFWorkbook workbook = new HSSFWorkbook(file);
                
                //Get first sheet from the workbook
                HSSFSheet sheet = workbook.getSheetAt(0);
 
                if( i==0 ){
                    // keep column style
                    Iterator<Row> rowIterator = sheet.iterator();
                    while(rowIterator.hasNext()) {// 1
                        Row row = rowIterator.next();
                        row_counts++;
                        if( row_counts == 2 ){// 2 跳過 Header                          
                            Iterator<Cell> cellIterator = row.cellIterator();
                            int cellnum = 0;
                            while(cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                if( cell.getCellStyle()!=null ){
                                    HSSFCellStyle cs = workbook.createCellStyle();
                                    cs.cloneStyleFrom(cell.getCellStyle());
                                    sheet.setDefaultColumnStyle(cellnum, cs);
                                    logger.debug(cellnum+" cell.getCellStyle()!=null => "+cs.getDataFormatString());
                                }else{
                                    logger.debug(cellnum+" cell.getCellStyle()==null");
                                }
                                cellnum++;
                            }
                        }// 2
                    }// 1
                    
                    file.close();
                    FileOutputStream outfile = new FileOutputStream(new File(outFileName));
                    workbook.write(outfile);
                    outfile.close();
                }else{// 1              
                    FileInputStream outfile = new FileInputStream(new File(outFileName));

                    //Get the workbook instance for XLS file 
                    HSSFWorkbook outWorkBook = new HSSFWorkbook(outfile);
                    HSSFSheet outSheet = outWorkBook.getSheetAt(0);
                    
                    //Iterate through each rows from first sheet
                    Iterator<Row> rowIterator = sheet.iterator();
                    boolean firstRow = onlyKeepFirstHearder;
                    while(rowIterator.hasNext()) {// 2
                        Row row = rowIterator.next();
                        Row outRow = outSheet.createRow(row_counts);

                        // set outRow = row; 
                        if( !firstRow ){
                            row_counts++;
                            
                            //For each row, iterate through each columns
                            Iterator<Cell> cellIterator = row.cellIterator();
                            int cellnum = 0;
                            while(cellIterator.hasNext()) {// 1
                                Cell cell = cellIterator.next();
                                Cell outCell = outRow.createCell(cellnum);

                                switch(cell.getCellType()) {
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        outCell.setCellValue(cell.getBooleanCellValue());
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        outCell.setCellValue(cell.getNumericCellValue());
                                        break;
                                    case Cell.CELL_TYPE_STRING:
                                        outCell.setCellValue(cell.getStringCellValue());
                                        break;
                                }

                                cellnum++;
                            }// end of while 1
                        }// end of if
                        
                        firstRow = false; // 非第一筆
                    }// end of while 2
                    
                    outfile.close();
                    file.close();
                    
                    FileOutputStream outFile2 =new FileOutputStream(new File(outFileName));
                    outWorkBook.write(outFile2);
                    outFile2.close();
                }// 1
                
                logger.debug("workbook.write "+i+"... END"); 
            }

            /*logger.debug("workbook set style ... START"); 
            FileInputStream outfile = new FileInputStream(new File(outFileName));
            HSSFWorkbook outWorkBook = new HSSFWorkbook(outfile);
            HSSFSheet outSheet = outWorkBook.getSheetAt(0);

            // set style
            if( csAry != null ){
                for(int col=0; col<DEF_COL_NUMBER; col++){
                    if( csAry[col]!=null ){
                        outSheet.setDefaultColumnStyle(col, csAry[col]);
                        logger.debug("set style col = "+col+"; csAry[col] = "+csAry[col].getDataFormat());
                    }
                }
            }
            
            outfile.close();
            FileOutputStream outFile2 =new FileOutputStream(new File(outFileName));
            outWorkBook.write(outFile2);
            outFile2.close();
            logger.debug("workbook set style ... END");*/
            
            return row_counts;
        } catch (Exception e) {
            logger.error("mergeExcel Exception:\n", e);
        }
        
        return 0;
    }    
    
    /**
     * 把傳入的cell欄位值抓出來
     * @param cell
     * @return
     */
    public static String getCellValue(HSSFRow row ,int cell_index){
        HSSFCell cell = row.getCell(cell_index);
        String result = "";
        if (cell == null)
            return result;

        int ctype = cell.getCellType();
        if (ctype == HSSFCell.CELL_TYPE_STRING)
            result = cell.getRichStringCellValue().getString();
        else if (ctype == HSSFCell.CELL_TYPE_NUMERIC){
            if (HSSFDateUtil.isCellDateFormatted(cell)) { // 是否為日期型
                result = DateUtils.getISODateStr(cell.getDateCellValue());
            }else { // 是否為數值型
              double d = cell.getNumericCellValue();
              if (d-(int)d < Double.MIN_VALUE) { // 是否為int型
                result = Integer.toString((int)d);
              } else { // 是否為double型
                result = Double.toString(cell.getNumericCellValue());
              }
            }
        }else if (ctype == HSSFCell.CELL_TYPE_BOOLEAN)
            result = String.valueOf(cell.getBooleanCellValue());
        else
            result = cell.toString();

        return result;
    }    
}
