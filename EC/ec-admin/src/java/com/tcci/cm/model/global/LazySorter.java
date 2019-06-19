/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

import java.util.Comparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class LazySorter<T> implements Comparator<T> {
    protected static final Logger logger = LoggerFactory.getLogger(LazySorter.class);
    private String sortField;
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    /**
     * 
     * @param obj1
     * @param obj2
     * @return 
     */
    @Override
    public int compare(T obj1, T obj2) {
        try {
            Object value1 = PropertyUtils.getProperty(obj1, this.sortField);            
            //logger.debug("value1=" + value1);
            Object value2 = PropertyUtils.getProperty(obj2, this.sortField);
            //logger.debug("value2=" + value2);
            // logger.debug("PropertyUtils.getPropertyType(obj1, this.sortField)=" + PropertyUtils.getPropertyType(obj1, this.sortField));
             
            if( PropertyUtils.getPropertyType(obj1, this.sortField) == Boolean.class 
                || PropertyUtils.getPropertyType(obj2, this.sortField) == Boolean.class ){
                return compareBoolean(value1, value2);
            }
            
            if( value1==null && PropertyUtils.getPropertyType(obj1, this.sortField) == String.class ){
                value1 = "";
            }
            if( value2==null && PropertyUtils.getPropertyType(obj2, this.sortField) == String.class ){
                value2 = "";
            }
            
            if( value1==null || value2==null ){
                return SortOrder.ASCENDING.equals(sortOrder) ? 1 : -1;
            }else{                    
                int value = ((Comparable)value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            }
        } catch(Exception e) {
            logger.error("compare exception:\n", e);
            return -1;
        }
    }
    
    /**
     * Boolean compare
     * @param value1
     * @param value2
     * @return 
     */
    public int compareBoolean(Object value1, Object value2){
        Integer cmpValue1;
        Integer cmpValue2;

        if( value1==null ){
            cmpValue1 = 0;
        }else{
            if( Boolean.FALSE.equals(value1) ){
                cmpValue1 = 1;
            }else{
                cmpValue1 = 2;
            }
        }
        if( value2==null ){
            cmpValue2 = 0;
        }else{
            if( Boolean.FALSE.equals(value2) ){
                cmpValue2 = 1;
            }else{
                cmpValue2 = 2;
            }
        }
        int value = ((Comparable)cmpValue1).compareTo(cmpValue2);
        return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;        
    }
}
