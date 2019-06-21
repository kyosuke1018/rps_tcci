/***********************************************************************
 * Module:  ArrayUtils.java
 * Author:  jackson
 * Purpose: Defines the Class ArrayUtils
 ***********************************************************************/

package com.tcci.fc.util;

import com.tcci.fc.entity.essential.Persistable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/** Array相關工具
 * 
 * @pdOid 1721056a-8bfe-4138-a3c7-1859b1b597cf */
public class ArrayUtils extends org.apache.commons.lang.ArrayUtils {
    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }
    
    /**
     * 合併 List, 但不重複
     */
    public static <T> List<T> addAllDistinct(List<T> srcList, List<T> addList){
        List<T> resList = new ArrayList<T>();
        if( srcList==null || srcList.isEmpty() ){
            resList.addAll(addList);
        }else{
            resList.addAll(srcList);
            if( addList!=null ){
                for(T obj : addList){
                    if( !resList.contains(obj) ){
                        resList.add(obj);
                    }
                }
            }
        }
        
        return resList;
    }
    
    /**
     * 轉換為 id list (for SQL)
     * @param objList
     * @return 
     */
    public static List<Long> convertToIdList(List<Persistable> objList){
        if( objList==null ){
            return null;
        }
        
        List<Long> ids = new ArrayList<Long>();
        for(Persistable item : objList){
            ids.add(item.getId());
        }
        return ids;
    }
}