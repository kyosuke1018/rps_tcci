/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilbert
 */
public class NativeSQLUtils {
    private static Logger logger = LoggerFactory.getLogger(NativeSQLUtils.class);

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
        return likeFilter(value, true);
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
     * @param columnName
     * @param value
     * @return
     */
    public static String getLikeTranslateSQL(String pColumnName, String pValue, Map<String, Object> params, String andOr) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(pValue)) {
            return sql.toString();
        }

        sql.append(" ").append(andOr).append(" ( ");
        String[] values = StringUtils.converterZhCode(pValue);
        int i = 1;
        for (String value : values) {
            String key = pColumnName.replaceAll("\\.", "") + i;
            if (i != 1) {
                sql.append("or ");
            }
            sql.append(wrapUPPER(pColumnName).toString()).append(" LIKE UPPER(#").append(key).append(") ");
            String map_value = StringUtils.trim(likeFilter(value, false).toString());
            logger.debug(key + "=" + map_value);
            params.put(key, map_value);

            i++;
        }
        sql.append(" ) ");
        return sql.toString();
    }
    public static String getLikeTranslateSQL(String pColumnName, String pValue, Map<String, Object> params) {
        return getLikeTranslateSQL(pColumnName, pValue, params, "AND");
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
        String prefixChar = "#";
        boolean doAnd = true;
        boolean isIn = true;
        return SQLUtils.getInSQL(pColumnName, pValueList, params, startIdx, prefixChar, doAnd, isIn);
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
        int startIdx = 1;
        String prefixChar = "#";
        boolean doAnd = true;
        boolean isIn = false;
        return SQLUtils.getInSQL(pColumnName, pValueList, params, startIdx, prefixChar, doAnd, isIn);
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

    public static String genEqualSQL(String pColumnName, Object pValue, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        //if (StringUtils.isBlank(pValue)) {
        //    return sql.toString();
        //}
        if( pValue == null ){
            return sql.toString();
        } 

        sql.append(" AND (");
        String map_key = pColumnName.replaceAll("\\.", "\\_");
        //String map_value = pValue;

        sql.append(pColumnName).append(" = #").append(map_key).append(" ");
        params.put(map_key, pValue);
//        logger.debug("map_value=" + map_value);
        sql.append(") ");
        return sql.toString();
    }
    
    /**
     * 依傳入的SQL及參數，計算總筆數。
     * @param EntityManager
     * @param sql
     * @param params
     * @return 總筆數
     */
    public static BigDecimal countBySQL(EntityManager em, String sql, Map<String, Object> params) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT COUNT(*) FROM ( ");
        queryString.append(sql);
        queryString.append(" ) TOTAL");

        Query query = em.createNativeQuery(queryString.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }

        List results = query.getResultList();
        if (CollectionUtils.isEmpty(results)) {
            return BigDecimal.ZERO;
        } else {
            return (BigDecimal) results.get(0);
        }
    }    
    
}
