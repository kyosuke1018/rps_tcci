/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.util;

import com.tcci.fc.util.time.DateUtils;
import com.tcci.fc.util.zhcoder.Zhcoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilbert
 */
public class NativeSQLUtils {

    private static Logger logger = LoggerFactory.getLogger(NativeSQLUtils.class);
    private static boolean isWorkWithZhCoder = true;
    private static Zhcoder zhcoder = new Zhcoder();
    /**
     * Between's SQL
     *
     * @param columnName
     * @param valueStart
     * @param valueEnd
     * @return
     */
    public static StringBuilder getBetweenSQL(String columnName, String valueStart, String valueEnd) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(columnName)) {
            return sql;
        }
        if (StringUtils.isBlank(valueStart) && StringUtils.isBlank(valueEnd)) {
            return sql;
        }
        sql.append("and (");
        if (StringUtils.isNotBlank(valueStart)) {
            sql.append(columnName).append(" ");
            sql.append(">= ");

            sql.append(wrapQuotes(valueStart));

        }

        if (StringUtils.isNotBlank(valueEnd)) {
            if (StringUtils.isNotBlank(valueStart)) {
                sql.append("and ");
            }
            sql.append(columnName).append(" ");
            sql.append("<= ");
            sql.append(wrapQuotes(valueEnd));
        }

        sql.append(")").append(" \n");
        return sql;
    }

    /**
     * 無條件前後加%及單引號
     *
     * @param field
     * @return
     */
    private static StringBuilder likeFilter(String value) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(value)) {
            return sql;
        }
        String result = StringUtils.trimToNull(value);
        result = result.replaceAll("\\*", "%");
        result = wrapPercent(result).toString();
        result = wrapQuotes(result).toString();
        sql.append(result).append(" ");
        return sql;
    }

    /**
     * 無條件前後加%或單引號
     *
     * @param value
     * @param isQuotes
     * @return
     */
    private static StringBuilder likeFilter(String value, boolean isQuotes) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(value)) {
            return sql;
        }
        String result = StringUtils.trimToNull(value);
        result = result.replaceAll("\\*", "%");
        result = wrapPercent(result).toString();
        if (isQuotes) {
            result = wrapQuotes(result).toString();
        }
        sql.append(result).append(" ");
        return sql;
    }

    /**
     * 無條件前後加單引號
     *
     * @param value
     * @return
     */
    private static StringBuilder wrapQuotes(String value) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(value)) {
            return sql;
        }
        String result = StringUtils.trimToNull(value);
        result = "'" + result + "'";
        sql.append(result).append(" ");
        return sql;
    }

    /**
     * 無條件前後加%
     *
     * @param value
     * @return
     */
    private static StringBuilder wrapPercent(String value) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(value)) {
            return sql;
        }
        String result = StringUtils.trimToEmpty(value);
        result = "%" + result + "%";
        sql.append(result);
        return sql;
    }

    /**
     * 欄位加上UPPER function
     *
     * @param value
     * @return
     */
    private static StringBuilder wrapUPPER(String value) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(value)) {
            return sql;
        }
        String result = StringUtils.trimToEmpty(value);
        result = "UPPER( " + result + " )";
        sql.append(result);
        return sql;
    }

    /**
     * like SQL+繁簡查詢 and (f2.description like #materialName1 or f2.description
     * like #materialName2)
     *
     * @param pColumnName
     * @param pValue
     * @param columnName
     * @param params
     * @param value
     * @return
     */
    public static String getLikeTranslateSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String[] values = converterZhCode(pValue);
        int i = 1;
        for (String value : values) {
            String key = pColumnName.replaceAll("\\.", "") + i;
            if (i != 1) {
                sql.append("or ");
            }
            sql.append(wrapUPPER(pColumnName).toString()).append(" LIKE UPPER(#").append(key).append(") ");
//            String map_value = wrapPercent(value).toString();
            String map_value = StringUtils.trim(likeFilter(value, false).toString());
            logger.debug(key + "=" + map_value);
            params.put(key, map_value);

            i++;
        }
        sql.append(" ) ");
        return sql.toString();
    }
    
    /**
     * 支援 "*" 號查詢
     * @param pColumnName
     * @param pValue
     * @param params
     * @return 
     */
    public static String getLikeStarSQL(String pColumnName, String pValue, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        if(StringUtils.isBlank(pValue)){
            return sql.toString();
        }
        
        sql.append(" AND ");
        String key = pColumnName.replaceAll("\\.", "");
        sql.append(wrapUPPER(pColumnName).toString()).append(" LIKE UPPER(#").append(key).append(") ");
        String map_value = StringUtils.trim(likeFilter(pValue, false).toString());
        
        params.put(key, map_value);
        logger.info("sql.toString() ="+sql.toString());
        return sql.toString();
    }
    
    /**
     * SQL "IN" command
     * @param pColumnName
     * @param pValueList
     * @param params
     * @return 
     */
    public static String getInSQL(String pColumnName, List<?> pValueList, Map<String, Object> params){
        return getInSQL(pColumnName, pValueList, params, 1);
    }

    public static String getInSQL(String pColumnName, List<?> pValueList, Map<String, Object> params, int startIdx){
        StringBuilder sql  = new StringBuilder();
        if (CollectionUtils.isNotEmpty(pValueList)) {
            sql.append(" AND ").append(pColumnName).append(" IN ( ");
            int idx = startIdx;
            for (Object item : pValueList) {
                String param_key = "#" + pColumnName + idx;
                param_key = param_key.replaceAll("\\(", "");
                param_key = param_key.replaceAll("\\)", "");
                param_key = param_key.replaceAll(",", "_");
                param_key = param_key.replaceAll("\\.", "_");
                sql.append(param_key).append(",");
                
                params.put(param_key.replaceAll("#", ""), item);
                idx++;
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(" ) ");
        }
        return sql.toString();
    }
    
    /**
     * gen NotIn SQL
     *
     * @param pColumnName
     * @param pValueList
     * @param params
     * @return
     */
    public static String getNotInSQL(String pColumnName, List<String> pValueList, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(pValueList)) {
            sql.append(" AND ").append(pColumnName).append(" NOT IN ( ");
            int idx = 1;
            for (String ekorg : pValueList) {
                String param_key = "#" + pColumnName + idx;
                sql.append(param_key).append(",");
                params.put(param_key.replaceAll("#", ""), ekorg);
                idx++;
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(" ) ");
        }
        return sql.toString();
    }

    /**
     * gen like SQL and (f2.description like #materialName1 or f2.description
     * like #materialName2)
     *
     * @param columnName
     * @param value
     * @return
     */
    public static String getLikeSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append("AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        String map_value = wrapPercent(pValue).toString();

        sql.append(pColumnName).append(" LIKE #").append(map_key).append(" ");
        params.put(map_key, map_value);
        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }

    /**
     * gen equal SQL and (f2.description like #materialName1 or f2.description
     * like #materialName2)
     *
     * @param columnName
     * @param value
     * @return
     */
    /*public static String genEqulSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if(StringUtils.isEmpty(pValue)){
            return sql.toString();
        }

        sql.append(" ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        String map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, map_value);
        logger.debug("map_value=" + map_value);
        sql.append(" ");
        return sql.toString();
    }
    
    public static String genEqulSQL(String pColumnName, Long pValue, Map<String, Object> params){
        StringBuilder sql  = new StringBuilder();
        if(null == pValue){
            return sql.toString();
        }

        sql.append(" ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        Long map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put( map_key, map_value );          
        logger.debug("map_value="+map_value);
        sql.append(" ");
        return sql.toString();
    }*/     
    /**
     * gen equal SQL
     *
     * @param pColumnName
     * @param pValue
     * @param params
     * @return
     */
    public static String genEqulSQL(String pColumnName, Object pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (pValue == null) {
            sql.append("AND ").append(pColumnName).append(" IS NULL ");
        } else {
            sql.append("AND ( ");
            String map_key = pColumnName.replaceAll("\\.", "\\_");

            sql.append(pColumnName).append(" = #").append(map_key).append(" ");
            params.put(map_key, pValue);

            sql.append(" ) ");
        }
        return sql.toString();
    }

    /**
     * gen like SQL and (f2.description like #materialName1 or f2.description
     * like #materialName2)
     *
     * @param columnName
     * @param value
     * @return
     */
    public static String genLikeSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        String map_value = wrapPercent(pValue).toString();

        sql.append(pColumnName).append(" LIKE #").append(map_key).append(" ");
        params.put(map_key, map_value);
        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }

    /**
     *
     * @param pColumnName
     * @param pValue
     * @param operator >=, <=
     * @param params
     * @return
     */ 
    public static String genRangeSQL(String pColumnName, Date pValueStart, Date pValueEnd, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        logger.debug("pValueStart=" + DateUtils.getSimpleISODateTimeStr(pValueStart));

        int idx = 1;
        if (pValueStart != null) {
            String param_key = pColumnName.replaceAll("\\.", "\\_") + idx;
            idx++;
            String operator = ">=";
            sql.append(" AND  ").append(pColumnName).append(" ").append(operator).append(" ").append("#").append(param_key);
            params.put(param_key, pValueStart);
        }
        if (pValueEnd != null) {
            pValueEnd = DateUtils.getFirstMinuteOfDay(pValueEnd);//加一天
            pValueEnd = DateUtils.addDays(pValueEnd, 1);
            logger.debug("pValueEnd=" + DateUtils.getSimpleISODateTimeStr(pValueEnd));
            String param_key = pColumnName.replaceAll("\\.", "\\_") + idx;
            String operator = "<";
            sql.append(" AND  ").append(pColumnName).append(" ").append(operator).append(" ").append("#").append(param_key);
            params.put(param_key, pValueEnd);
        }
        return sql.toString();
    }

    public static String genRangeSQL(String pColumnName, String pValueStart, String pValueEnd, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        int idx = 1;
        if (StringUtils.isNotBlank(pValueStart)) {
            logger.debug("pValueStart=" + pValueStart);
            String param_key = pColumnName.replaceAll("\\.", "\\_") + idx;
            idx++;
            String operator = ">=";
            sql.append(" AND  ").append(pColumnName).append(" ").append(operator).append(" ").append("#").append(param_key);
            params.put(param_key, pValueStart);
        }
        if (StringUtils.isNotBlank(pValueEnd)) {
            logger.debug("pValueEnd=" + pValueEnd);
            String param_key = pColumnName.replaceAll("\\.", "\\_") + idx;
            String operator = "<=";
            sql.append(" AND  ").append(pColumnName).append(" ").append(operator).append(" ").append("#").append(param_key);
            params.put(param_key, pValueEnd);
        }
        return sql.toString();
    }

    public static String genEqualSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        String map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, map_value);
//        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }
    
    public static String genValueSQL(String pColumnName, String pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append(" ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        String map_value = pValue;

        sql.append("#").append(map_key);
        params.put(map_key, map_value);
//        logger.debug("map_value=" + map_value);
        sql.append(" ");
        return sql.toString();
    }    
    
    public static String genEqualSQL(String pColumnName, Long pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (null == pValue) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        Long map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, map_value);
//        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }    
    
    public static String genEqualSQL(String pColumnName, Short pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (null == pValue) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        Short map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, map_value);
//        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }        
    
    public static String genEqualSQL(String pColumnName, Integer pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if (null == pValue) {
            return sql.toString();
        }

        sql.append(" AND ( ");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        Integer map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, map_value);
//        logger.debug("map_value=" + map_value);
        sql.append(" ) ");
        return sql.toString();
    }   
    
    /**
     * 取得簡繁體陣列
     * @param value 簡體或繁體
     * @return 簡繁體陣列 or null
     */
    public static String[] converterZhCode(String value){
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String[] materialNames = null;
        if (isWorkWithZhCoder) {
            materialNames = zhcoder.converterResult(value);
        } else {
            materialNames = new String[1];
            materialNames[0] = value;
        }
        return materialNames;
    } 
}
