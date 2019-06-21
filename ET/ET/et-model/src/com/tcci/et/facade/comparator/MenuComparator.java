/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.comparator;

import com.tcci.et.model.admin.MenuFunctionVO;
import java.util.Comparator;

/**
 *
 * @author Peter.pan
 */
public class MenuComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        if( o1==null ){
            return 1;
        }
        if( o2==null ){
            return -1;
        }

        MenuFunctionVO vo1 = ((MenuFunctionVO)o1);
        MenuFunctionVO vo2 = ((MenuFunctionVO)o2);
        
        // 小到大排序
        if( vo1.getMsortnum()!=vo2.getMsortnum() ){
            return vo1.getMsortnum()-vo2.getMsortnum();
        }else if( vo1.getSsortnum()!=vo2.getSsortnum() ){
            return vo1.getSsortnum()-vo2.getSsortnum();
        }else{
            return vo1.getSortnum()-vo2.getSortnum();
        }
    }
}
