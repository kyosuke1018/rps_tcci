/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.fileio.annotation.util;

import com.tcci.fc.util.time.DateUtils;
import com.tcci.fc.fileio.ExcelFileFieldInfo;
import com.tcci.fc.fileio.annotation.ExcelFileFieldMeta;
import com.tcci.fc.fileio.annotation.enums.DataTypeEnum;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson.Lee
 */
public class ExcelFileFieldInfoUtil {

    protected final static Logger logger = LoggerFactory.getLogger(ExcelFileFieldInfoUtil.class);
    

    /**
     * 取得匯入檔Index對應欄位說明Map: 依傳入的類別，取得meta data定義之 importIndex/ExcelFileFieldInfo Map。
     * <ol>
     * <li>importIndex不可重覆定義。</li>
     * <li>若未定義欄位 importIndex(小於0)，則跳過不匯入</li>
     * </ol>
     */
    public static Map<Integer, ExcelFileFieldInfo> getImportExcelFileFieldMap(Class c) {

        Map<Integer, ExcelFileFieldInfo> metaMap = new HashMap<Integer, ExcelFileFieldInfo>();
        Field[] fs = c.getDeclaredFields();
        int idx = 0;
        for (Field f : fs) {
            String fieldName = f.getName();
            ExcelFileFieldMeta metaData = f.getAnnotation(ExcelFileFieldMeta.class);
            //若該欄位沒有設Annotation，則跳過不檢查
            if (null == metaData) {
                String msg = c.getName() + ": @ExcelFileFieldInfo not found.";
                logger.debug(msg);
                continue;
            }
            

            //取得該欄位在Excel表中的欄位順序
            Integer key = metaData.importIndex();
            //若未定義欄位 importIndex(小於0)，則跳過不匯入
            if (key < 0) {
                continue;
            }
            
            //是否為NotNull
            boolean isNotNull = false;
            NotNull notNull = f.getAnnotation(NotNull.class);
            if (null!=notNull){
                isNotNull = true;
            }            
            ExcelFileFieldInfo fieldInfo = new ExcelFileFieldInfo(fieldName, metaData, isNotNull);
            
            //importIndex，檢查該值是否重覆定義
            if (key >= 0 && metaMap.keySet().contains(key)) {
                String msg = f.getName() + ": duplicate importIndex.";
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            
            metaMap.put(key, fieldInfo);
            idx++;
        }
        return metaMap;
    }

    /**
     * 取得匯出檔Index對應欄位說明Map: 依傳入的類別，取得meta data定義之 exportIndex/ExcelFileFieldInfo Map。
     * <ol>
     * <li>importIndex 不可重覆定義。</li>
     * <li>若未定義欄位 exportIndex(小於0)，則跳過不匯入</li>
     * </ol>
     */
    public static Map<Integer, ExcelFileFieldInfo> getExportExcelFileFieldMap(Class c) {

        Map<Integer, ExcelFileFieldInfo> metaMap = new HashMap<Integer, ExcelFileFieldInfo>();
        Field[] fs = c.getDeclaredFields();
        int idx = 0;
        for (Field f : fs) {
            String fieldName = f.getName();
            ExcelFileFieldMeta metaData = f.getAnnotation(ExcelFileFieldMeta.class);
            //若該欄位沒有設Annotation，則跳過不檢查
            if (null == metaData) {
                String msg = c.getName() + ": @ExcelFileFieldInfo not found.";
                logger.debug(msg);
                continue;
            }

            //取得該欄位在Excel表中的欄位順序
            Integer key = metaData.exportIndex();
            //若未定義欄位 exportIndex(小於0)，則跳過不匯出
            if (key < 0) {
                continue;
            }
            //exportIndex，檢查該值是否重覆定義
            if (key >= 0 && metaMap.keySet().contains(key)) {
                String msg = f.getName() + ": duplicate exportIndex.";
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            
            //是否為NotNull
            boolean isNotNull = false;
            NotNull notNull = f.getAnnotation(NotNull.class);
            if (null!=notNull){
                isNotNull = true;
            }
            ExcelFileFieldInfo fieldInfo = new ExcelFileFieldInfo(fieldName, metaData, isNotNull);
            metaMap.put(key, fieldInfo);
            idx++;
        }
        return metaMap;
    }

    /**
     * 依照欄位格式定義，將值設定於VO
     *
     * @param detail
     * @param item
     * @param key
     * @param value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void setObjProperty(Object detail, ExcelFileFieldInfo fieldInfo, String key, String value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        DataTypeEnum dataType = fieldInfo.getMetaData().dataType();
        if (DataTypeEnum.INT.equals(dataType)) {
            if (StringUtils.isBlank(value)) {
                value = "0";
            }
            PropertyUtils.setProperty(detail, key, new Integer(value));
        } else if (DataTypeEnum.LONG.equals(dataType)) {
            if (StringUtils.isBlank(value)) {
                value = "0";
            }
            PropertyUtils.setProperty(detail, key, new Long(value));
        } else if (DataTypeEnum.BIG_DECIMAL.equals(dataType)) {
            // 轉為BigDecimal
            if (StringUtils.isBlank(value)) {
                PropertyUtils.setProperty(detail, key, BigDecimal.ZERO);
            } else {
                PropertyUtils.setProperty(detail, key, new BigDecimal(value));
            }
        } else if (DataTypeEnum.BOOLEAN.equals(dataType)) {
            if (StringUtils.isBlank(value)) {
                value = "0";
            }
            PropertyUtils.setProperty(detail, key, Boolean.valueOf(value));
        } else if (DataTypeEnum.DATE.equals(dataType)) {
            // YYYYMMDD
            // or
            // YYYY/MM/DD，轉為Calendar
            Date dt = null;
            if (StringUtils.indexOf(value, "/") != -1) {
                dt = DateUtils.getISODate(value, "/");
            }else if (StringUtils.indexOf(value, "-") != -1) {
                dt = DateUtils.getISODate(value);
            } else {
                dt = DateUtils.getISODate(value, "");
            }

            if (null != dt) {
                //2012/8/28 新增年月傳入民國時錯誤檢核，年度必須大於1911才合理
                Date year1911 = DateUtils.getDate(1911, 1, 1);
                if (dt.before(year1911)){
                    String msg = "Date format error, must be YYYY/MM/DD";
                    logger.error(msg);
                    throw new RuntimeException(msg);
                }
                PropertyUtils.setProperty(detail, key, dt);
            } else {
                // DataTypeEnum.STRING
                PropertyUtils.setProperty(detail, key, value);
            }
        } else {
            PropertyUtils.setProperty(detail, key, value);
        }
    }
    
    /**
     * 依Class及欄位名稱，取得ExcelFileFieldInfo
     * @param c
     * @param fieldName
     * @return 
     */
    public static ExcelFileFieldInfo getExcelFileFieldInfoByFieldName(Class c, String fieldName) {
        ExcelFileFieldInfo fieldInfo = null;
        try {
            Field f = c.getDeclaredField(fieldName);
            ExcelFileFieldMeta metaData = f.getAnnotation(ExcelFileFieldMeta.class);
            //若該欄位沒有設Annotation，則回傳null
            if (null == metaData) {
                String msg = c.getName() + ": @ExcelFileFieldInfo not found.";
                logger.debug(msg);
                return null;
            }

            //是否為NotNull
            boolean isNotNull = false;
            NotNull notNull = f.getAnnotation(NotNull.class);
            if (null != notNull) {
                isNotNull = true;
            }
            fieldInfo = new ExcelFileFieldInfo(fieldName, metaData, isNotNull);

        } catch (Exception e) {
            logger.error("getExcelFileFieldInfoByFieldName failed!", e);
            return null;
        }
        return fieldInfo;
    }
    
                
}
