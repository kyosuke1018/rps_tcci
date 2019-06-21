/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查詢條件有 LOG 需求時，搭配 QueryCondUtils 使用
 * 
 * @author Peter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryCondFieldMeta {
    /**
     * 顯示標題
     * @return 
     */
    String headerName() default "";

    /**
     * 若為自訂 Object, 指定記錄 子欄位名稱
     * @return 
     */
    String subShowField() default "";
    
    /**
     * 說明
     * @return 
     */
    String description() default "";

    /**
     * 可否多選
     */
    boolean isMultiSelect() default false;

    /**
     * 順序
     */
    int index() default 0;
}
