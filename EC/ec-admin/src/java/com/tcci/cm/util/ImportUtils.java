/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class ImportUtils {
    private static Logger logger = LoggerFactory.getLogger(ImportUtils.class);
    
    public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    // IE上傳開啟中的EXCEL，CONTENT_TYPE會是application/octet-stream
    public static final String CONTENT_TYPE_XLSX_INEDIT = "application/octet-stream";
    
    /**
     * 取得正確版本 workbook
     * @param fileStream
     * @param contentType
     * @return
     * @throws IOException 
     */
    public static Workbook getWorkBook(InputStream fileStream, String contentType) throws IOException{
        Workbook wb = null;
        if( contentType.equalsIgnoreCase(CONTENT_TYPE_XLS) ){
            wb = new HSSFWorkbook(new POIFSFileSystem(fileStream));
        }else if( contentType.equalsIgnoreCase(CONTENT_TYPE_XLSX) ){
            wb = new XSSFWorkbook(fileStream);
        }else if( contentType.equalsIgnoreCase(CONTENT_TYPE_XLSX_INEDIT) ){
            // IE上傳開啟中的EXCEL，CONTENT_TYPE會是application/octet-stream
            try{
                wb = new HSSFWorkbook(new POIFSFileSystem(fileStream));
            }catch(Exception e){
                // ignore
            }
            if( wb == null ){
                wb = new XSSFWorkbook(fileStream);
            }
        }
        return wb;
    }
    
    /**
     * 
     * @param contentType 
     * @return  
     */
    public static boolean checkExcelContentType(String contentType){
        return CONTENT_TYPE_XLS.equals(contentType)
                || CONTENT_TYPE_XLSX.equals(contentType)
                || CONTENT_TYPE_XLSX_INEDIT.equals(contentType);
    }
}
