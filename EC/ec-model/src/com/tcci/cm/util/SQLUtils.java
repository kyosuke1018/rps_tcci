/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import com.tcci.fc.util.zhcoder.Zhcoder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilbert
 */
public class SQLUtils {
    private static final Logger logger = LoggerFactory.getLogger(SQLUtils.class);
    
    public static final int DEF_MAX_RESULT_SIZE = 10000; // 預設最大查詢筆數

    private static boolean isWorkWithZhCoder = true;
//    private static Zhcoder zhcoder = new Zhcoder();
    private static Zhcoder zhcoder;
    
    public static String safeParamName(String pColumnName){
        if( pColumnName==null ){
            return "";
        }
        pColumnName = pColumnName.replaceAll("\\(", "");
        pColumnName = pColumnName.replaceAll("\\)", "");
        pColumnName = pColumnName.replaceAll(",", "_");
        pColumnName = pColumnName.replaceAll("\\.", "_");
        pColumnName = pColumnName.replaceAll(" ", "");

        return pColumnName;
    }
    
    /**
     * IN or Not IN SQL
     * @param pColumnName
     * @param pValueList
     * @param params
     * @param startIdx
     * @param prefixChar
     * @param isIn
     * @return 
     */
    public static String getInSQL(String pColumnName, List<?> pValueList, 
            Map<String, Object> params, int startIdx, String prefixChar, boolean doAnd, boolean isIn) {
        StringBuilder sql  = new StringBuilder();
        if (CollectionUtils.isNotEmpty(pValueList)) {
            sql.append(doAnd?" AND ":" OR ");
            sql.append(pColumnName);
            sql.append(isIn?" IN ( ":" NOT IN ( ");

            int idx = startIdx;
            pColumnName = safeParamName(pColumnName);
            for (Object item : pValueList) {
                String param_key = pColumnName + idx;               
                params.put(param_key, item);
                
                sql.append(prefixChar).append(param_key).append(",");

                idx++;
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(" ) ");
        }
        return sql.toString();
    }
    
    /**
     * 繁簡轉換SQL
     * and (f2.description like #materialName1 or f2.description like #materialName2)
     * @param columnName
     * @param value
     * @return 
     */
    public static String getLikeTranslateSQL(String columnName, String value){
        StringBuilder sb  = new StringBuilder();
        if(StringUtils.isEmpty(value)){
            return sb.toString();
        }
        
        
        sb.append(" AND ( ");
        String[] values =converterZhCode(value);
        int i = 0;
        for (String value_l : values) {
            if(i!=0){
                sb.append(" OR ");
            }
            sb.append(columnName);
            sb.append(" LIKE ");
            sb.append("'");        
            sb.append(likeFilter(value_l));        
            sb.append("'");  
            i++;
        }
        sb.append(" ) ");
        return sb.toString();
    } 
    
    
    /**
     * 取得NOT Like查詢結果，加入繁簡轉換功能
     * and (f2.description NOT like #materialName1 and f2.description NOT like #materialName2)
     * @param columnName
     * @param value
     * @return 
     */
    public static String getNotlikeTranslateSQL(String columnName, String value){
        StringBuilder sb  = new StringBuilder();
        if(StringUtils.isEmpty(value)){
            return sb.toString();
        }
        
        
        sb.append(" AND ( ");
        String[] values =converterZhCode(value);
        int i = 0;
        for (String value_l : values) {
            if(i!=0){
                sb.append(" AND ");
            }
            sb.append(columnName);
            sb.append(" NOT LIKE ");
            sb.append("'");        
            sb.append(likeFilter(value_l));        
            sb.append("'");  
            i++;
        }
        sb.append(" ) ");
        return sb.toString();
    }        
    
    /**
     * 無條件前後加%
     * @param field
     * @return 
     */
    public static String likeFilter(String field) {
        String result = StringUtils.trimToNull(field);
        if(StringUtils.isNotBlank(result)){
            result = result.replaceAll("\\*", "%");
        }
        return (StringUtils.isEmpty(field)) ? "%" : "%" + result + "%";
    }   
    
    /**
     * 取得簡繁體陣列
     * @param value 簡體或繁體
     * @return 簡繁體陣列 or null
     */
    public static String[] converterZhCode(String value){
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String[] materialNames = null;
        if (isWorkWithZhCoder) {
            zhcoder = new Zhcoder();
            materialNames = zhcoder.converterResult(value);
        } else {
            materialNames = new String[1];
            materialNames[0] = value;
        }
        return materialNames;
    }   

    /**
     * 依傳入的SQL及參數，計算總筆數。
     * @param em
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
