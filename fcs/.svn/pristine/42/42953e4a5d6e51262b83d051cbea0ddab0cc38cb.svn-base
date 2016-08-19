package com.tcci.fc.fileio;

import com.tcci.fc.fileio.annotation.util.ExcelFileFieldInfoUtil;


/**
 * 記錄檔案匯入時的錯誤資訊。依列數排序。
 * @author Jackson.Lee
 */
public class ExcelConstraintViolation<T> implements Comparable{
    
    /**
     * 列數(第幾列)或序號
     */
    private Integer rowNum = 0;
    
    /**
     * 發生錯誤的欄位名稱(在rootCauseBean中)
     */
    private String fieldName;
    
    /**
     * 原始錯誤資料Bean
     */
    private T rootCauseBean;
    
    /**
     * 錯誤訊息
     */
    private String message;
    
    /**
     * 此錯誤所屬VO的欄位Metadata
     */
    private ExcelFileFieldInfo excelFileFieldInfo;

    public ExcelConstraintViolation(int rowNum, String fieldName, T rootCauseBean, String message) {
        this.rowNum = rowNum;
        this.rootCauseBean = rootCauseBean;
        this.message = message;
        excelFileFieldInfo = ExcelFileFieldInfoUtil.getExcelFileFieldInfoByFieldName(rootCauseBean.getClass(), fieldName);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getRootCauseBean() {
        return rootCauseBean;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public String getFieldName() {
        return fieldName;
    }

    /**
     * 取得此錯誤所屬VO的欄位Metadata
     * @return 
     */
    public ExcelFileFieldInfo getExcelFileFieldInfo() {
        return excelFileFieldInfo;
    }

    /**
     * 取得欄位在匯入檔中的欄位順序(importIndex)
     * @return 
     */
    public Integer getImportIndex(){
        if (null != excelFileFieldInfo) {
            return excelFileFieldInfo.getMetaData().importIndex();
        }
        return 0;
    }
    
    @Override
    public int compareTo(Object o) {
        ExcelConstraintViolation target = (ExcelConstraintViolation)o;
        //先比列數
        if (this.getRowNum().compareTo(target.getRowNum())!=0){
             return this.getRowNum().compareTo(target.getRowNum());
        }
        
        //再比欄位順序
        if (this.getImportIndex().compareTo(target.getImportIndex())!=0){
             return this.getImportIndex().compareTo(target.getImportIndex());
        }        
        
        //再比訊息        
        return this.getMessage().compareTo(target.getMessage());
    }
}
