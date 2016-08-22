/***********************************************************************
 * Module:  ArrayUtils.java
 * Author:  jackson
 * Purpose: Defines the Class ArrayUtils
 ***********************************************************************/

package com.tcci.fc.util;

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
    public static <T> void remove(List<T> list, Integer index_remove_start, Integer index_remove_end){
        if(null == list){
            return;
        }
        int list_size = list.size();
        if(null == index_remove_start){
            index_remove_start = Integer.valueOf("0");
        }
        if(null == index_remove_end){
            index_remove_end = list_size-1;
        }
        List<T> removelist = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            T vo =list.get(i);
            if(i >= index_remove_start && i <= index_remove_end){
                removelist.add(vo);
            }
            
            
        }
        list.removeAll(removelist);
    }
}