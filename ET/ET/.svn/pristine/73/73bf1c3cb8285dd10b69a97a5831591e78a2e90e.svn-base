package com.tcci.cm.util;

import com.tcci.cm.annotation.QueryCondFieldMeta;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class QueryCondUtils {
    private static final Logger logger = LoggerFactory.getLogger(QueryCondUtils.class);

    public final static int DEF_MAX_DEPTH = 5;
    
    /**
     * 取得 Annotation HeaderName
     * @param c
     * @param fieldName
     * @return 
     */
    public static String getFieldHeaderName(Class c, String fieldName) {
        QueryCondFieldMeta metaData = getQueryCondVOFieldMap(c, fieldName);
        
        if( metaData!=null ){
            return metaData.headerName();
        }
        
        return null;
    }

    /**
     * 取得 Annotation SubShowField
     * @param c
     * @param fieldName
     * @return 
     */
    public static String getSubShowField(Class c, String fieldName) {
        QueryCondFieldMeta metaData = getQueryCondVOFieldMap(c, fieldName);
        
        if( metaData!=null ){
            return metaData.subShowField();
        }
        
        return null;
    }
    
    /**
     * 取得 Annotation 內容
     * @param c
     * @param fieldName
     * @return 
     */
    public static QueryCondFieldMeta getQueryCondVOFieldMap(Class c, String fieldName) {
        Map<String, QueryCondFieldMeta> metaMap = new HashMap<String, QueryCondFieldMeta>();
        Field field = null;
        try{
            field = c.getDeclaredField(fieldName);
        }catch(Exception e){
            // ignore
        }
                
        if( field==null ){
            return null;
        }
        
        QueryCondFieldMeta metaData = field.getAnnotation(QueryCondFieldMeta.class);
        
        return metaData;
    }    

    /**
     * 預設呼叫 dumpCondVO
     * @param o
     * @return 
     */
    public static String dumpCondVO(Object o) {
        return dumpCondVO(o, DEF_MAX_DEPTH, false, false, true, false, false, null);
    }
    
    /**
     * Dump Java Object Info
     * @param o
     * @param maxDepth 最大掃描階層
     * @param showall // 是否全部顯示 (不管有沒有值)
     * @param showFullClassName // 是否顯示完整 ClassName
     * @param isFirstLevel // 是否為第一階層
     * @param isList // 是否為 List or Array
     * @param isListElement // 是否為 List 中的屬性
     * @param subShowField // 屬性類別物件中，要顯示的代表欄位
     * @return 
     */
    public static String dumpCondVO(Object o, int maxDepth, boolean showall, 
            boolean showFullClassName, boolean isFirstLevel, 
            boolean isList, boolean isListElement, String subShowField) {
        maxDepth = maxDepth - 1;

        StringBuilder buffer = new StringBuilder();
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            if( showall || Array.getLength(o)>0 ){
                if( !isList ){
                    buffer.append("Array: ");
                }
                //buffer.append("[");
                
                for (int i = 0; i < Array.getLength(o); i++) {
                    Object value = Array.get(o, i);
                    if( value!=null ){
                        buffer.append(typeAwareDump(value, maxDepth, showall, showFullClassName, true, subShowField)); // set isListElement = true
                    }
                }
                
                //buffer.append("]\n");
            }
        } else {
            StringBuilder subbuffer = new StringBuilder();
            String classname = oClass.getName();

            //subbuffer.append("Class: ").append(getShowClassName(classname, showFullClassName));
            //if( !isListElement ){ subbuffer.append("{\n"); }
            
            while (oClass != null) {
                Field[] fields = oClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    String fieldname = fields[i].getName();
                    
                    if( !ExtBeanUtils.isIgnoreFieldName(fieldname, true, true) ){
                        // 若為 List 只處理 elementData 即可
                        if( !isList || (isList && fieldname.equals("elementData")) ){// elementData 為 AarrayList 中實際存放資料的 field name
                            String headerName = null;
                            if( !isFirstLevel ){// 非第一層(一般只有第一層會有Annotation)
                                // headerName = fieldname;
                            }else{
                                headerName = getFieldHeaderName(oClass, fieldname);
                                subShowField = getSubShowField(oClass, fieldname); // 第一層取得，供後續 List Element 判斷顯示哪個欄位
                            }

                            if( headerName!=null // 第一層有Annotation
                                    || isList  // List or Array
                                    || (isListElement && fieldname.equals(subShowField) ) // element in array
                            ){
                                try {
                                    Object value = fields[i].get(o);
                                    if( value!=null ){
                                        String valueStr = typeAwareDump(value, maxDepth, showall, showFullClassName, isListElement, subShowField);
                                        
                                        if( showall || !valueStr.isEmpty() ){
                                            if( isList && fieldname.equals("elementData") ){
                                                if( valueStr.startsWith(",") ){ valueStr = valueStr.substring(1); }
                                                subbuffer.append(valueStr);
                                            }else{
                                                if( isListElement ){
                                                    subbuffer.append(",").append(valueStr);
                                                }else{
                                                    subbuffer.append(headerName).append("=").append(valueStr).append("\n");
                                                }
                                            }
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    subbuffer.append(headerName).append("=").append(e.getMessage()).append("\n");
                                }
                            }
                        }
                    }
                }
                oClass = oClass.getSuperclass();
            }
            //if( !isListElement ){ subbuffer.append("}\n"); }
            
            // ArrayList 為空時的處理
            if( showall || ( o.getClass()!=java.util.ArrayList.class || subbuffer.toString().indexOf("size=0")<0 ) ){
                buffer.append(subbuffer.toString());
            }
        }
        
        return buffer.toString();
    }

    /**
     * 非 Array 型態物件 Dump
     * @param value
     * @param maxDepth
     * @param showall
     * @param showFullClassName
     * @param isListElement
     * @param subShowField
     * @return 
     */
    public static String typeAwareDump(Object value, int maxDepth, boolean showall, boolean showFullClassName, 
            boolean isListElement, String subShowField) {
        StringBuilder buffer = new StringBuilder();
        
        if (value != null) {
            if (value.getClass().isPrimitive()
                    || value.getClass() == java.lang.Long.class
                    || value.getClass() == java.lang.String.class
                    || value.getClass() == java.lang.Integer.class
                    || value.getClass() == java.lang.Boolean.class
                    || value.getClass() == java.lang.Double.class
                    || value.getClass() == java.lang.Short.class
                    || value.getClass() == java.lang.Byte.class) {
                buffer.append(value);
            } else {
                if( value.getClass() == java.util.Date.class ){
                    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                    buffer.append(sdFormat.format((Date)value));
                }else if( value.getClass() == java.math.BigDecimal.class ){
                    buffer.append(((BigDecimal)value).doubleValue());
                }else if( value.getClass() == java.util.ArrayList.class ){
                    buffer.append(dumpCondVO(value, maxDepth, showall, showFullClassName, false, true, isListElement, subShowField));
                }else if( value.getClass().isArray() ){
                    buffer.append(dumpCondVO(value, maxDepth, showall, showFullClassName, false, true, isListElement, subShowField));
                }else{
                    if (maxDepth > 0) {
                        buffer.append(dumpCondVO(value, maxDepth, showall, showFullClassName, false, false, isListElement, subShowField));
                    } else {
                        try{
                            buffer.append(value.toString());
                        }catch(Exception e){
                            logger.debug("value.getClass() = " + value.getClass() + " Exception : "+e.getMessage());
                        }
                    }
                }
            }
        }
        
        return buffer.toString();
    }    
}
