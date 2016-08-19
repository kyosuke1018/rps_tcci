package com.tcci.fc.fileio.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匯入檔案格式Meta Data
 * @author Jackson.Lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExcelFileMeta {
    /**Header總
     * @return 數*/
    int headerRow();    
    /**最大列
     * @return 數*/
    int maxRecord() default -1;
    /**用來指定列號欄位名
     * @return 稱*/
    String rowNumColumnName() default "";
    /**決定該筆資料是否Highlight的欄位名
     * @return 稱*/
    String highlightColumnDef() default "";
}
