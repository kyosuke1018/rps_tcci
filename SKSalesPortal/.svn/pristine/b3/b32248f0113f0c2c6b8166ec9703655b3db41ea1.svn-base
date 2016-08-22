package com.tcci.sapproxy.jco;

import com.tcci.worklist.entity.datawarehouse.TcSapclient;
import com.tcci.sapproxy.PpProxy;
import com.tcci.sapproxy.exception.SapProxyRuntimeException;
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
     * 依Sapclient定義的欄位，取得JCo連結參數。
     * @param sapclient
     * @return 
     */
    public static Properties getJCoProp(Properties jndiConfig, TcSapclient sapclient) {
        Properties jcoProp = new Properties();
        for (int j = 0; j < JCO_PROP_POSTFIX.length; j++) {
            String key = StringUtils.trim(sapclient.getCode()) + "." + JCO_PROP_POSTFIX[j];
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
    
    /**
     * get PpProxy
     *
     * @param sapClientCode
     * @param sapModuleEnum
     * @param operator
     * @return
     */
    public static PpProxy getSapProxy(String sapClientCode, SapModuleEnum sapModuleEnum, String operator) {
        PpProxy ppProxy;
        ppProxy = PpSapProxyFactory.createProxy(sapClientCode, operator);
        if (null == ppProxy) {
            throw new SapProxyRuntimeException("can not get ppProxy");
        }
        return ppProxy;
    }

    public static PpProxy getSapProxy(String sapClientCode, String operator) {
        return getSapProxy(sapClientCode, null, operator);
    }    
}
