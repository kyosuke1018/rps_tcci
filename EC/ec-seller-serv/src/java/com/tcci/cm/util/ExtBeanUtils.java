package com.tcci.cm.util;

/**
 *
 * @author Peter.pan
 */
import com.tcci.cm.converter.BigDecimalConverter;
import com.tcci.cm.converter.BooleanConverter;
import com.tcci.cm.converter.DateConverter;
import com.tcci.cm.converter.DoubleConverter;
import com.tcci.cm.converter.LongConverter;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtBeanUtils extends BeanUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExtBeanUtils.class);

    public final static int DEF_MAX_DEPTH = 5;
    public final static String[] FILTER_EXCLUDE = {"serialVersionUID"}; // 整體性的排除欄位
    
    static {
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Timestamp.class);
        ConvertUtils.register(new BigDecimalConverter(), java.math.BigDecimal.class);
        ConvertUtils.register(new BooleanConverter(), java.lang.Boolean.class);// 不加 NULL 會變 false
        ConvertUtils.register(new LongConverter(), java.lang.Long.class);// 不加 NULL 會變 0
        ConvertUtils.register(new DoubleConverter(), java.lang.Double.class);// 不加 NULL 會變 0
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException ex) {
            logger.debug("copyProperties IllegalAccessException : \n", ex);
        } catch (InvocationTargetException ex) {
            logger.debug("copyProperties InvocationTargetException : \n", ex);
        }
    }
    
    public static String dump(Object o) {
        return dump(o, DEF_MAX_DEPTH, false, false);
    }

    /**
     *  顯示 Class Name
     * @param fullClassName
     * @param showFullClassName
     * @return 
     */
    public static String getShowClassName(String fullClassName, boolean showFullClassName){
        int i = fullClassName.lastIndexOf(".");
        if( !showFullClassName && i>0 ){
            return fullClassName.substring(i+1);
        }else{
            return fullClassName;
        }
    }
    
    /**
     * Dump Java Object Info
     * @param o
     * @param maxDepth
     * @param showall
     * @param showFullClassName
     * @return 
     */
    public static String dump(Object o, int maxDepth, boolean showall, boolean showFullClassName) {
        if( o==null ){
            logger.debug("dump... Object is NULL!");
            return "Object is NULL !";
        }
        maxDepth = maxDepth - 1;

        StringBuilder buffer = new StringBuilder();
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            if( showall || Array.getLength(o)>0 ){
                buffer.append("Array: ");
                buffer.append("[");
                for (int i = 0; i < Array.getLength(o); i++) {
                    Object value = Array.get(o, i);
                    if( value!=null ){
                        buffer.append(typeAwareDump(value, maxDepth, showall, showFullClassName));
                    }
                }
                buffer.append("]\n");
            }
        } else {
            StringBuilder subbuffer = new StringBuilder();
            String classname = oClass.getName();

            subbuffer.append("Class: ").append(getShowClassName(classname, showFullClassName));
            subbuffer.append("{\n");
            while (oClass != null) {
                Field[] fields = oClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);

                    if( !isIgnoreFieldName(fields[i].getName(), true, true) ){
                        try {
                            Object value = fields[i].get(o);
                            if( value!=null ){
                                String valueStr = typeAwareDump(value, maxDepth, showall, showFullClassName);

                                if( showall || !valueStr.isEmpty() ){
                                    subbuffer.append(fields[i].getName()).append("=").append(valueStr).append("\n");
                                }
                            }
                        } catch (IllegalAccessException e) {
                            subbuffer.append(fields[i].getName()).append("=").append(e.getMessage()).append("\n");
                        }
                    }
                }
                oClass = oClass.getSuperclass();
            }
            subbuffer.append("}\n");
            
            // ArrayList 為空時的處理
            if( showall || ( !classname.endsWith(".ArrayList") || subbuffer.toString().indexOf("size=0")<0 ) ){
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
     * @return 
     */
    public static String typeAwareDump(Object value, int maxDepth, boolean showall, boolean showFullClassName) {
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
                    SimpleDateFormat sdFormat = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
                    buffer.append(sdFormat.format((Date)value));
                }else if( value.getClass() == java.math.BigDecimal.class ){
                    buffer.append(((BigDecimal)value).doubleValue());
                }else if( value.getClass() == java.math.BigInteger.class ){
                    buffer.append(((BigInteger)value).longValue());
                }else{
                    if (maxDepth > 0) {
                        buffer.append(dump(value, maxDepth, showall, showFullClassName));
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
    
    /**
     * 是否為應忽略屬性 (如 serialVersionUID)
     * @param name
     * @param ignoreCase
     * @param ignoreUnderline
     * @return 
     */
    public static boolean isIgnoreFieldName(String name, boolean ignoreCase, boolean ignoreUnderline){
        String cname = (ignoreCase)? name.toLowerCase():name;
        cname = (ignoreUnderline)? cname.replaceAll("_", ""):cname;
        
        String[] filterExclude = FILTER_EXCLUDE;
        
        for(int i=0; filterExclude!=null && i<filterExclude.length; i++){
            String cfilter = (ignoreCase)? filterExclude[i].toLowerCase():filterExclude[i];
            cfilter = (ignoreUnderline)? cfilter.replaceAll("_", ""):cfilter;
            
            if( cfilter.equals(cname) ){
                return true;
            }
        }
        
        return false;
    }

    /**
     * 測試物件是否真的可序列化
     * @param original
     * @return 
     */
    public boolean checkSerializable(Serializable original){
        try{
            Serializable copy = SerializationUtils.clone(original);
            if( original.equals(copy) ){
                return true;
            }
        }catch(Exception e){
            logger.error("checkSerializable exception:\n", e);
        }
        return false;
    }
    
    /**
     * 印出特定 Class 的 getter、setter
     * @param aClass 
     */
    public static void printGettersSetters(Class aClass){
      Method[] methods = aClass.getMethods();

      for(Method method : methods){
        if(isGetter(method)) logger.debug("getter: " + method);
        if(isSetter(method)) logger.debug("setter: " + method);
      }
    }

    public static boolean isGetter(Method method){
      if(!method.getName().startsWith("get")){ return false; }
      if(method.getParameterTypes().length != 0){ return false; }  
      // if( void.class.equals(method.getReturnType() ) return false;
      return true;
    }

    public static boolean isSetter(Method method){
      if(!method.getName().startsWith("set")) return false;
      if(method.getParameterTypes().length != 1) return false;
      return true;
    }    
    
    /*public static void printClassAnnotations(Class aClass){
        Annotation[] annotations = aClass.getAnnotations();

        for(Annotation annotation : annotations){
            if(annotation instanceof MyAnnotation){
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                logger.debug("name: " + myAnnotation.name());
                logger.debug("value: " + myAnnotation.value());
            }
        }
    }

    public static void printMethodAnnotations(Method method){
        Annotation[] annotations = method.getDeclaredAnnotations();

        for(Annotation annotation : annotations){
            if(annotation instanceof MyAnnotation){
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                logger.debug("name: " + myAnnotation.name());
                logger.debug("value: " + myAnnotation.value());
            }
        }
    }*/

}

