package com.tcci.cm.annotation;

import com.tcci.cm.annotation.enums.DataTypeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 檔案匯入欄位格式Meta Data
 * @author Jackson.Lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelFileFieldMeta {
    /**
     * 對應Excel匯入檔的Index，從0開始，小於0則不匯入。
     * @return 
     */
    int importIndex() default -1;
    /**
     * 對應Excel匯出檔的Index，從0開始，小於0則不匯出。
     * @return 
     */
    int exportIndex() default -1;    
    
    /**
     * 非必要
     * @return 
     */
    boolean isOptional() default false;
    
    /**
     * 欄位名稱。用在匯入時產生錯誤訊息及匯出時產生抬頭使用。
     * @return 
     */
    String headerName() default "";
    /**
     * 欄位型態，預設為字串。
     * @return 
     */
    DataTypeEnum dataType() default DataTypeEnum.STRING;
    /**
     * 說明
     * @return 
     */
    String description() default "";
    /**
     * 此欄位是否為檢核訊息要寫入的欄位，預設為false。
     * @return 
     */
    boolean isCheckMessage() default false;
    /**
     * 此欄位是否為列號的欄位，預設為false。
     * @return 
     */
    boolean isRowNum() default false;    
    /**
     * 此欄位是否為系統產生欄位，預設為false。
     * @return 
     */
    boolean isSystemGenerate() default false;
    /**
     * 表格標題的註解(comment)，在動態產生標題時使用。
     * @return 
     */
    String tableHeaderComment()  default "";
    /**
     * 表格標題的Style，在動態產生標題時使用，預設為Index=0。
     * 在ExportUtil中，可指定多組Style。
     * @return 
     */
    int tableHeaderStyleIndex() default 0;
}
