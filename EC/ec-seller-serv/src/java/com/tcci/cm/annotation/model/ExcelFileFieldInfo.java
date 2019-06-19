/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.annotation.model;

import com.tcci.cm.annotation.ExcelFileFieldMeta;

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
    
    //此欄位是否 Optional
    private boolean optional;

    public ExcelFileFieldInfo(String fieldName, ExcelFileFieldMeta metaData, boolean notNull) {
        this.fieldName = fieldName;
        this.metaData = metaData;
        this.notNull = notNull;
        this.optional = (metaData!=null && metaData.isOptional());
    }

    public ExcelFileFieldInfo(String fieldName, ExcelFileFieldMeta metaData, boolean notNull, boolean optional) {
        this.fieldName = fieldName;
        this.metaData = metaData;
        this.notNull = notNull;
        this.optional = optional;
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

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }    
}
