/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.fileio.annotation.util;

import com.tcci.fc.fileio.annotation.ExcelFileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel Meta Info Util
 * @author Jackson.Lee
 */
public class ExcelFileMetaUtil {
    protected final static Logger logger = LoggerFactory.getLogger(ExcelFileMetaUtil.class);
 
    /**
     * 從傳入的類別取得Excel檔案定義資訊(header)。
     */
    public static ExcelFileMeta getExcelFileInfo(Class clazz) {
        if (!clazz.isAnnotationPresent(ExcelFileMeta.class)) {
            String msg = clazz.getName() + ": @ExcelFileInfo is not found.";
            throw new RuntimeException(msg);
        }
        logger.debug(clazz.getName() + ": @ExcelFileInfo is found.");

        ExcelFileMeta info = (ExcelFileMeta) clazz.getAnnotation(ExcelFileMeta.class);

        if (info.headerRow() < 0 || info.headerRow() > info.maxRecord()) {
            String msg = clazz.getName() + ": @ExcelFileInfo headerRow defined error.";
            throw new RuntimeException(msg);
        }

        if (info.maxRecord() < 0) {
            String msg = clazz.getName() + ": @ExcelFileInfo maxRecord<0 defined error.";
            throw new RuntimeException(msg);
        }
        return info;
    }
    
    
}
