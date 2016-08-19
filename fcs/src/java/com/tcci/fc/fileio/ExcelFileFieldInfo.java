/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.fileio;

import com.tcci.fc.fileio.annotation.ExcelFileFieldMeta;

/**
 * Keep field Name and Field MetaData;
 * @author Jackson.Lee
 */
public class ExcelFileFieldInfo {
    //欄位名稱
    private String fieldName;
    
    //欄位meta data
    private ExcelFileFieldMeta metaData;
    
    //此欄位是否NotNull(@NotNull)
    private boolean notNull;
    

    public ExcelFileFieldInfo(String fieldName, ExcelFileFieldMeta metaData, boolean notNull) {
        this.fieldName = fieldName;
        this.metaData = metaData;
        this.notNull = notNull;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ExcelFileFieldMeta getMetaData() {
        return metaData;
    }

    public void setMetaData(ExcelFileFieldMeta metaData) {
        this.metaData = metaData;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    
    
}
