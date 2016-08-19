/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.util;

import java.util.Comparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Peter
 */
public class LazySorter<T> implements Comparator<T> {
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
            //System.out.println("value1=" + value1);
            Object value2 = PropertyUtils.getProperty(obj2, this.sortField);
            //System.out.println("value2=" + value2);
          
            if( value1==null && PropertyUtils.getPropertyType(obj1, this.sortField) == String.class ){
                value1 = "";
            }
            if( value2==null && PropertyUtils.getPropertyType(obj2, this.sortField) == String.class ){
                value2 = "";
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
            e.getStackTrace();
            return -1;
        }
    }
}
