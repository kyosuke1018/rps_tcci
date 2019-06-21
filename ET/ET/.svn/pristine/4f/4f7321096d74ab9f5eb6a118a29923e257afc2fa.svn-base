/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import java.util.Comparator;

/**
 *
 * @author Peter
 */
public class TcGroupComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        TcGroup tcGroup1 = ((TcGroup)o1);
        TcGroup tcGroup2 = ((TcGroup)o2);
        
        if( tcGroup1.getCode()!=null && tcGroup2.getCode()!=null ){
            return tcGroup1.getCode().compareTo(tcGroup2.getCode());
        }else{
            long res = (tcGroup1.getId()-tcGroup2.getId());
            if( res>0 ){ return 1; }
            else if( res<0 ){ return -1; }
            else { return 0; }
        }
    }
}
