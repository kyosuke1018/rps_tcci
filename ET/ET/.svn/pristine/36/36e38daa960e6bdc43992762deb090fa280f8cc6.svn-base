/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常用下拉選單
 * @author Kyle.Cheng
 */
public class SelectItemUtils {
    protected static Logger logger = LoggerFactory.getLogger(SelectItemUtils.class); 
    /**
     * Map 轉換成 SelectItem[]
     *
     * @param map
     * @return SelectItem[]
     */
    public static SelectItem[] MapToFilterSelectOption(Map map, String def_option_text) {
        if (map == null || map.isEmpty()) {
            return (new SelectItem[]{new SelectItem("", def_option_text)});
        }

        SelectItem[] options = new SelectItem[map.size() + 1];
        options[0] = new SelectItem("", def_option_text);

        Iterator iter = map.keySet().iterator();
        int i = 1;
        while (iter.hasNext()) {
            Object key;
            if ((key = iter.next()) != null) {
                options[i++] = new SelectItem((String) key, (String) map.get(key));
            }
        }

        return options;
    }
    
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
}
