/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.tcci.fc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Peter
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils{
    /**
     * 
     * @param ls
     * @param ls2
     * @return 
     */
    public static List intersect(List ls, List ls2) {
        if( ls==null || ls2==null){
            return null;
        }
        
        List list = new ArrayList();
        list.addAll(ls);
        list.retainAll(ls2);
        
        return list;
    }
    
    public static List union(List ls, List ls2) {
        if( ls==null ){
            return ls2;
        }
        if( ls2==null ){
            return ls;
        }
        
        List list = new ArrayList();
        list.addAll(ls);
        
        for(Object item : ls2){
            if( !ls.contains(item) ){
                list.add(item);
            }
        }

        return list;
    }
    
    public static List diff(List ls, List ls2) {
        if( ls==null ){
            return null;
        }
        if( ls2==null ){
            return ls;
        }
        
        List list = new ArrayList();
        List rmlist = new ArrayList<String>();
        
        for(Object item : ls){
            if( ls2.contains(item) ){
                rmlist.add(item);
            }
        }
        
        list.addAll(ls);
        list.removeAll(rmlist);
        
        return list;
    }
    
    public static List reverse(List ls) {
        Object[] ary = ls.toArray();
        CollectionUtils.reverseArray(ary);
        return Arrays.asList(ary);
    }
}
