/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author gilbert
 */
public class ListUtils {
    public static <T extends Object> T getFirstElement(List<T> list) {
        if(null != list && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
//    public static List<SelectItem> getOptions(List<Object> list) {
//        List<SelectItem> factoryOptions = new ArrayList<>();
//        for (Object entity : list) {
//            Object value = entity;
//            String label = entity.toString();
//            factoryOptions.add(new SelectItem(value, label));
//        }
//        return factoryOptions;
//    }
    public static List<SelectItem>  getOptions(Collection<? extends Object> shapes) {
        List<SelectItem> options = new ArrayList<>();
        for (Object entity : shapes) {
            Object value = entity;
            String label = entity.toString();
            options.add(new SelectItem(value, label));
        }
        
        return options;
    }
}
