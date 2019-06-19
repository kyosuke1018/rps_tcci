package com.tcci.cm.annotation;

import com.tcci.cm.model.global.GlobalConstant;
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
    /**Header總數(含Title
     * @return )*/
    int headerRow();    
    /**最大列
     * @return 數*/
    int maxRecord() default GlobalConstant.MAX_EXCEL_EXPORT_SIZE;
    /**用來指定列號欄位名
     * @return 稱*/
    String rowNumColumnName() default "";
    /**決定該筆資料是否Highlight的欄位名
     * @return 稱*/
    String highlightColumnDef() default "";
}
