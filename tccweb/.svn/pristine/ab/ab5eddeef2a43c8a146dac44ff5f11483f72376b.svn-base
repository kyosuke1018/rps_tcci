package com.tcci.fc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class ResultSetHelper<T> {
    protected static final Logger logger = LoggerFactory.getLogger(ResultSetHelper.class);        

    private Class<T> rowClass;
    
    private int cols;

    public ResultSetHelper(Class<T> rowClass) {
        this.rowClass = rowClass;
    }

    /**
     * 依 pojo field name 找出在 ResultSet 的第幾欄
     * @param meta
     * @param fieldname
     * @return 
     */
    public int findColIndex(ResultSetMetaData meta, String pojofieldname, boolean ignoreUnderline){
        try{
            for (int col = 0; col < meta.getColumnCount(); col++) {
                String colname = meta.getColumnName(col + 1).toUpperCase();
                String fieldname = pojofieldname.toUpperCase();
                
                if( ignoreUnderline ){
                    {
                        colname = colname.replaceAll("_", "");
                        fieldname = fieldname.replaceAll("_", "");
                    }
                }
                
                if( colname.equals(fieldname) ){
                    return col;
                }
            }
        }catch(Exception e){
            logger.error("findColIndex => findColIndex Exception", e);
        }
        return -1;
    }
    
    /**
     * queryToPOJOList : 將 Native Sql 查詢得到的 ResultList 轉為 List<T>，T為一普通 POJO (不需為 Entity)
     * @param entityManager
     * @param sql
     * @param params
     * @return 
     */
    public List<T> queryToPOJOList(EntityManager entityManager, String sql, Map<String, Object> params){       
        return queryToPOJOList(entityManager, sql, params, 0, 0);
    }
    
    /**
     * 有限制筆數
     * queryToPOJOList : 將 Native Sql 查詢得到的 ResultList 轉為 List<T>，T為一普通 POJO (不需為 Entity)
     * @param entityManager
     * @param sql
     * @param params
     * @param firstResult - position of the first result, numbered from 0
     * @param maxResults - maximum number of results to retrieve
     * @return 
     */
    public List<T> queryToPOJOList(EntityManager entityManager, String sql, Map<String, Object> params, int firstResult, int maxResults){       
        List<T> resList = null;
        logger.debug("sql = \n" + sql);
        logPararms(params);
        
        try{
            // 找出 物件屬性 對應 查詢結果欄位順序，存於 fieldSequence
            String[] fieldSequence = getColumnFieldSequence(entityManager, sql);
            
            // 取出執行查詢動作
            Query query = entityManager.createNativeQuery(sql);
            
            if( maxResults>0 && maxResults>firstResult ){ // 部分查詢
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResults);
            }
            
            for (String key : params.keySet()) { // 條件參數
                query.setParameter(key, params.get(key));
            }

            List list = query.getResultList();

            // 將 ResultSet 轉換成 POJO List
            resList = resultSetToPOJOList(list, fieldSequence, cols);
            logger.debug("queryToPOJOList resList = "+(resList!=null?resList.size():0));
        }catch(Exception e){
            logger.error("queryToPOJOList => findColIndex Exception", e);
        }
        
        return resList;
    }
    
    /**
     * 找出 物件屬性 對應 查詢結果欄位順序，存於 fieldSequence
     * @param entityManager
     * @param sql
     * @return 
     */
    public String[] getColumnFieldSequence(EntityManager entityManager, String sql){
        String[] fieldSequence = null;
        try{
            // 取出 ResultSetMetaData
            Connection connection = entityManager.unwrap(java.sql.Connection.class);  
            PreparedStatement stmt = connection.prepareStatement(sql.replaceAll("#", ":"));
//            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSetMetaData meta = stmt.getMetaData();

            // 找出 物件屬性 對應 查詢結果欄位順序，存於 fieldSequence
            cols = meta.getColumnCount();
            fieldSequence = new String[cols];
            //getFields返回的是某个类里的所有public类型的变量，包括继承父类的
            //getDeclaredFields返回的是某个类里的所有类型的变量，不包括继承父类的
            Field[] fields = rowClass.getDeclaredFields();
            Class thisClass = rowClass;
            Class superClass = rowClass;
            while(true){
                superClass = getSuperClass(thisClass);
                if(superClass.equals(thisClass)){
                    break;
                }
                thisClass = superClass;
                Field[] fields2 = superClass.getDeclaredFields();
                fields = (Field[])ArrayUtils.addAll(fields, fields2);            
            }

            if( fields!=null ){
                for(Field field :fields){
                    String fieldname = field.getName();
                    int colid = findColIndex(meta, fieldname, true);
                    if( colid>=0 ){// 有對應的欄位
                        fieldSequence[colid] = fieldname;
                        // for DEBUG
                        //logger.debug("getColumnFieldSequence => map field : colid = "+colid+" : "+fieldname);
                    }else{
                        logger.debug("getColumnFieldSequence => no map field : fieldname = "+fieldname);
                    }
                }
            }else{
                logger.debug("getColumnFieldSequence => no field defined in "+rowClass);
            }
        }catch(Exception e){
            logger.error("getColumnFieldSequence => findColIndex Exception", e);
        }
        
        return fieldSequence;
    }
    private Class getSuperClass(Class subClass){
         Class superClass = rowClass.getSuperclass();
         return superClass;
    }
    
    /**
     * 將 ResultSet 轉換成 POJO List
     * @param list
     * @param fieldSequence
     * @param cols
     * @return 
     */
    public List<T> resultSetToPOJOList(List list, String[] fieldSequence, int cols){
        List<T> resList = new ArrayList<T>();
        
        try{
            for (Object row : list) {
                T bean = rowClass.newInstance();
                
                Object[] columns;
                if( cols>1 ){
                    columns = (Object[]) row;
                }else{ // 結果為單一欄位時
                    columns = new Object[1];
                    columns[0] = row;
                }
                
                for(int i=0; i<columns.length; i++){
                    try{
                        if( fieldSequence[i]!=null ){ // 有對應的欄位
                            Object value = typeConvertor(columns[i], bean, fieldSequence[i]);
                            if( value!=null ){
                                //PropertyUtils.setProperty(bean, fieldSequence[i], value);
                                BeanUtils.setProperty(bean, fieldSequence[i], value);
                                // for DEBUG
                                //ogger.debug("fieldSequence[i] = "+fieldSequence[i]+" : columns[i] = "+ columns[i] + " : value = "+ value);
                            }
                        }
                    }catch(Exception ex){
                        logger.debug("columns[i].getClass().getName() = " + columns[i].getClass().getName());
                        logger.debug("resultSetToPOJOList => fieldSequence[i] = " + fieldSequence[i] + "; columns[i] = " + columns[i]);
                        logger.debug("resultSetToPOJOList => findColIndex IllegalArgumentException", ex);
                    }
                }

                resList.add((T)bean);
            }
        }catch(Exception e){
            logger.error("resultSetToPOJOList => findColIndex Exception", e);
        }
        
        return resList;
    }
    
    /**
     *  特殊型態轉換
     * @param inObj
     * @param bean
     * @param name
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    public Object typeConvertor(Object inObj, T bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        Object outObject = inObj;
        
        if( inObj != null ){
            if( inObj.getClass() == BigDecimal.class ){
                Class type = PropertyUtils.getPropertyType(bean, name);

                if( type == Long.class ){
                    BigDecimal bd = (BigDecimal)inObj;
                    outObject = bd.longValue();
                }else if( type == Boolean.class ){
                    BigDecimal bd = (BigDecimal)inObj;
                    outObject = (bd.intValue()==0)? Boolean.FALSE:Boolean.TRUE;
                }
            }
        }
        
        return outObject;
    }
    
    public void logPararms(Map<String, Object> params){
        if( params!=null ){
            for(String key : params.keySet()){
                logger.debug(key + " = " + params.get(key));
            }
        }
    }
}
