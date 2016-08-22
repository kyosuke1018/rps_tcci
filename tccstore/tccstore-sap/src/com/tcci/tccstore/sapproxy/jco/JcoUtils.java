package com.tcci.tccstore.sapproxy.jco;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoTable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.Column;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson.Lee
 */
public class JcoUtils {

    //Cache: Key 為 entity的Class，Value為 Table欄位和Entity欄位Mappings: 
    private static Map<Class, Map<String, String>> fieldPropMappingInfosCache = new HashMap<Class, Map<String, String>>();
    /**
     * JCO連線參數結尾
     */
    static String[] JCO_PROP_POSTFIX = {"jco.client.lang",
        "jco.client.passwd", "jco.client.user", "jco.client.client", "jco.client.sysnr", "jco.client.ashost", "jco.destinationName",
        "jco.destination.pool_capacity", "jco.destination.peak_limit"};
    protected static final Logger logger = LoggerFactory.getLogger(JcoUtils.class);

    /**
     * 顯示JCoTable的各欄位名稱及說明。
     * @param tb 
     */
    public static void showTableStructure(JCoTable tb) {

        int numRows = tb.getNumRows();
        System.out.println("Table rows = " + numRows);
        tb.firstRow();

        JCoMetaData tbMeta = tb.getMetaData();
        System.out.println("============================================================");
        System.out.println("Table name = " + tbMeta.getName());
        System.out.println("Field count = " + tbMeta.getFieldCount());
        System.out.println("FIELD NAME  : TYPE : DESC : VALUE");
        System.out.println("--------------------------------------");
        for (int i = 0; i < tbMeta.getFieldCount(); i++) {
            System.out.println(tbMeta.getName(i) + " : " + tbMeta.getDescription(i) + " : " + tbMeta.getTypeAsString(i) + "/" + tbMeta.getLength(i) + " : [" + tb.getString(i) + "]");

        }
    }

    /**
     * 依Sapclient定義的欄位，取得JCo連結參數。
     * @param sapclient
     * @return 
     */
    public static Properties getJCoProp(Properties jndiConfig, String sapclient) {
        Properties jcoProp = new Properties();
        for (int j = 0; j < JCO_PROP_POSTFIX.length; j++) {
            String key = StringUtils.trim(sapclient) + "." + JCO_PROP_POSTFIX[j];
            String value = jndiConfig.getProperty(key);
            try {
                jcoProp.setProperty(JCO_PROP_POSTFIX[j], value);
            } catch (Exception e) {
                logger.error("JCOSyncSap key=" + key + ",value=" + value);
            }
        }

        //sap.route
        String value = jndiConfig.getProperty("sap.route");
        jcoProp.setProperty("sap.route", value);

        return jcoProp;
    }

    /**
     * 取指定JCoTable中，指定欄位的資料; 若無指定欄位則回傳所有欄位資料。
     * @param outputTable
     * @return 多筆欄位名稱/欄位值Map的List。
     */
    public static List<Map<String, Object>> getJCoTableData(JCoTable outputTable, List<String> fieldNames) {
        List<Map<String, Object>> results = null;
        if (null != outputTable) {
            results = new ArrayList<Map<String, Object>>();
            //取得表格內所有資料
            outputTable.firstRow();
            for (int i = 0; i < outputTable.getNumRows(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                if (CollectionUtils.isNotEmpty(fieldNames)) {//若有指定要回傳的欄位名稱
                    for (String fieldName : fieldNames) {
                        Object value = outputTable.getValue(fieldName);
                        if (value instanceof java.lang.String) {
                            value = StringUtils.trim((String) value);
                        }
                        item.put(fieldName, value);
                    }
                } //若沒有指定要回傳的欄位名稱，則回傳全部欄位
                else {
                    for (int j = 0; j < outputTable.getFieldCount(); j++) {
                        String fieldName = outputTable.getMetaData().getName(j);
                        Object value = outputTable.getValue(j);
                        if (value instanceof java.lang.String) {
                            value = StringUtils.trim((String) value);
                        }
                        item.put(fieldName, value);
                    }
                }
                results.add(item);
                //下一行
                outputTable.nextRow();
            }
        }

        return results;
    }

    /**
     * 取指定JCoTable中，所有欄位資料，JCoTable只包含一個Structure時使用。
     * @param outputTable
     * @return 單筆欄位名稱/欄位值Map。
     */
    public static Map<String, Object> getJCoTableDataAsMap(JCoTable outputTable) {

        Map<String, Object> results = null;
        if (null != outputTable) {
            int numRows = outputTable.getNumRows();
            logger.debug("output rows = " + numRows);
            outputTable.setRow(0);

            results = new HashMap<String, Object>();
            //取得表格內所有資料
            outputTable.firstRow();
            for (int j = 0; j < outputTable.getFieldCount(); j++) {
                String fieldName = outputTable.getMetaData().getName(j);
                Object value = outputTable.getValue(j);
                if (value instanceof java.lang.String) {
                    value = StringUtils.trim((String) value);
                }
                results.put(fieldName, value);
            }
        }
        return results;
    }
    
    /**
     * 取指定 JCoTable 中，所有資料的各欄位資料.
     * @param outputTable
     * @return List包含每筆中各欄位名稱/欄位值Map。
     */
    public static List<Map<String, Object>> getJCoTableDataAsMapList(JCoTable outputTable) {

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        if (null != outputTable) {
            int numRows = outputTable.getNumRows();
            logger.debug("output rows = " + numRows);
            //outputTable.setRow(0);


            //取得表格內所有資料
            outputTable.firstRow();
            //先讀一行!?
            Map<String, Object> result = new HashMap<String, Object>();
            for (int j = 0; j < outputTable.getFieldCount(); j++) {
                String fieldName = outputTable.getMetaData().getName(j);
                Object value = outputTable.getValue(j);
                if (value instanceof java.lang.String) {
                    value = StringUtils.trim((String) value);
                }
                result.put(fieldName, value);
            }
            results.add(result);
            while (outputTable.nextRow()) {
                result = new HashMap<String, Object>();
                for (int j = 0; j < outputTable.getFieldCount(); j++) {
                    String fieldName = outputTable.getMetaData().getName(j);
                    Object value = outputTable.getValue(j);
                    if (value instanceof java.lang.String) {
                        value = StringUtils.trim((String) value);
                    }
                    result.put(fieldName, value);
                }
                results.add(result);
            }
        }
        return results;
    }

    /**
     * 取得Table欄位和Entity欄位Mapping資訊。
     * Key: Table Column Name.
     * Value: Entity Field Property Name.
     * @param clazz
     * @return 
     */
    public static Map<String, String> getFieldsPropsMappingInfo(Class clazz) {
        Map<String, String> fieldPropMapping = new HashMap<String, String>();
//        logger.debug("class name: " + clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            Annotation[] fieldAnnotations = field.getDeclaredAnnotations(); //do something to these                
            for (Annotation annotation : fieldAnnotations) {
                if (annotation instanceof Column) {
                    Column tbColumn = (Column) annotation;
                    fieldPropMapping.put(tbColumn.name(), camelString(tbColumn.name()));
//                    logger.debug("columnName/fieldName: " + tbColumn.name() + "/" + StringUtils.camelString(tbColumn.name()));
                }
            }
        }
        return fieldPropMapping;
    }

    /**
     * 依傳入的SAP資料(dataMap)，及欄位對應Map，產生Entity物件。
     * @param fieldPropMapping
     * @param dataMap
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static Object generateEntity(
            Map<String, String> fieldPropMapping,
            Map<String, Object> dataMap,
            Class clazz) throws Exception {
        return generateEntity(fieldPropMapping,
                dataMap,
                clazz,
                null,
                null);
    }

    /**
     * 依傳入的SAP資料(dataMap)，及欄位對應Map，產生Entity物件。
     * @param fieldPropMapping
     * @param dataMap
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static Object generateEntity(
            Map<String, String> fieldPropMapping,
            Map<String, Object> dataMap,
            Class clazz,
            Class pkClazz,
            Object pkObject) throws Exception {
        if (null == fieldPropMapping || fieldPropMapping.isEmpty()) {
            return null;
        }

        Object entity = null;

        if (null == pkClazz) {
            entity = clazz.newInstance();
        } else {
            Constructor constructor = clazz.getConstructor(pkClazz);
            entity = constructor.newInstance(pkObject);
        }

        for (String columnName : fieldPropMapping.keySet()) {
            Object value = dataMap.get(columnName);
            String fieldName = fieldPropMapping.get(columnName);

            if (null != value) {
                BeanUtils.setProperty(entity, fieldName, value);
            }
        }
        return entity;
    }

    /**
     * 將傳入以_分隔字串，轉為駝峰文字。
     * @param str 以_分隔字串，如: DOC_NUMBER, ALT_TAX_CL
     * @return 駝峰文字，如: docNumber, altTaxCl
     */
    public static String camelString(String str) {
        String returnStr = "";//要返回的值
        str = str.toLowerCase();
        String[] arr = str.split("_");
        returnStr += arr[0];//第一字肯定是小寫
        for (int i = 1; i < arr.length; i++) {//?第二字開始
            StringBuilder strbu = new StringBuilder(arr[i]);
            strbu.setCharAt(0, Character.toTitleCase(arr[i].charAt(0)));
            returnStr += strbu.toString();
        }
        return returnStr;//返回
    }

    /**
     * 取得JCoFunction傳出的結構名稱.
     * @param function
     * @return 
     */
    public static List<String> getOutputJCoTableNames(String inputParamPrfix, JCoFunction function) {
        List<String> outputTableNames = new ArrayList<String>();
        JCoFieldIterator it = function.getTableParameterList().getFieldIterator();
        while (it.hasNextField()) {
            JCoField f = it.nextField();
            logger.debug("f.getName() = " + f.getName());
            if (!StringUtils.startsWith(f.getName(), inputParamPrfix)) {
                outputTableNames.add(f.getName());
            }
        }
        return outputTableNames;
    }

    /**
     * 取得JcoFunction回傳結果，回傳String與JCoTable構成的Map。
     * Key: JCoTable Name
     * Value: JCoTable
     * @param function
     * @return 
     */
    public static Map<String, JCoTable> getResultsMap(String inputParamPrfix, JCoFunction function) {
        Map<String, JCoTable> results = new HashMap<String, JCoTable>();
        List<String> outputTableNames = getOutputJCoTableNames(inputParamPrfix, function);
        //取得outputTables
        for (String outputTableName : outputTableNames) {
            JCoTable table = function.getTableParameterList().getTable(outputTableName);
            results.put(outputTableName, table);
        }
        return results;
    }

    /**
     *  取得Table欄位和Entity欄位Mapping
     * @param clazz
     * @return 
     */
    public static Map<String, String> getFieldsPropsMappingInfoWithCache(Class clazz) {
        Map<String, String> fieldPropMappingInfo = fieldPropMappingInfosCache.get(clazz);
        if (null == fieldPropMappingInfo) {
            fieldPropMappingInfo = JcoUtils.getFieldsPropsMappingInfo(clazz);
            fieldPropMappingInfosCache.put(clazz, fieldPropMappingInfo);
        }
        return fieldPropMappingInfo;
    }
}
