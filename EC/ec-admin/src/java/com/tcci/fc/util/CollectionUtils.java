/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.tcci.fc.util;

import java.util.ArrayList;
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
    public static List<String> intersect(List<String> ls, List<String> ls2) {
        if( ls==null || ls2==null){
            return null;
        }
        
        List<String> list = new ArrayList<String>();
        list.addAll(ls);
        list.retainAll(ls2);
        
        return list;
    }
    
    public static List<String> union(List<String> ls, List<String> ls2) {
        if( ls==null ){
            return ls2;
        }
        if( ls2==null ){
            return ls;
        }
        
        List<String> list = new ArrayList<String>();
        list.addAll(ls);
        
        for(String item : ls2){
            if( !ls.contains(item) ){
                list.add(item);
            }
        }

        return list;
    }
    
    public static List diff(List<String> ls, List<String> ls2) {
        if( ls==null ){
            return null;
        }
        if( ls2==null ){
            return ls;
        }
        
        List<String> list = new ArrayList<String>();
        List<String> rmlist = new ArrayList<String>();
        
        for(String item : ls){
            if( ls2.contains(item) ){
                rmlist.add(item);
            }
        }
        
        list.addAll(ls);
        list.removeAll(rmlist);
        
        return list;
    }
}
