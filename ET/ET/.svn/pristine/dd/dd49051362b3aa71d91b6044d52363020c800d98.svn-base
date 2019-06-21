/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常用下拉選單。
 * @author Jackson.Lee
 */
public class SelectHelper {
    protected static Logger logger = LoggerFactory.getLogger(SelectHelper.class);    
    
    /**
     * 年度，顯示未來n年。
     */
    private static final int YEAR_RANGE = 5;
    
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
    
      /**
     * 取得時間下拉選單。
     * @return 
     */
    public static List<SelectItem> getTimeListItems() {
        // SelectOneMenu items for times
        // label: 00:00 ~ 23:30
        // value: 0 ~ 1410 (23*30+30)
        List<SelectItem> lstTime = new ArrayList<SelectItem>();
        int minutes = 0;
        for (int i = 0; i < 24; i++) {
            StringBuilder sb = new StringBuilder();
            if (i < 10) {
                sb.append("0");
            }
            sb.append(i).append(":");
            lstTime.add(new SelectItem(String.valueOf(minutes), sb.toString() + "00"));
            minutes += 30;
            lstTime.add(new SelectItem(String.valueOf(minutes), sb.toString() + "30"));
            minutes += 30;
        }
        return lstTime;
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

    /**
     * 取得年度的下拉選單。(顯示系統年+未來n年）
     * @return 
     */
    public static List<SelectItem> getYearListItems() {
        Calendar c = Calendar.getInstance();

        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH);

        //若系統月為12月，則顯示系統年+1年
        if (currMonth == Calendar.DECEMBER) {
            currYear++;
        }

        List<SelectItem> yearList = new ArrayList<SelectItem>();
        for (int i = currYear; i <= currYear + YEAR_RANGE; i++) {
            yearList.add(new SelectItem(i));
        }
        return yearList;
    }

    /**
     * 顯示月份下拉選單。
     * @return 
     */
    public static List<SelectItem> getMonthListItems() {
        Calendar c = Calendar.getInstance();

        List<SelectItem> monthList = new ArrayList<SelectItem>();
        for (int i = 1; i <= 12; i++) {
            monthList.add(new SelectItem(i));
        }
        return monthList;
    }    
    /**
     * 取得語系下拉選單
     * @return 
     */
    public static List<SelectItem> getLanguageSelectItems() {
        Object value;
        String label;
        List<SelectItem> lstTime = new ArrayList<SelectItem>();
        lstTime.add(new SelectItem(value="M", label="繁體中文(ZF)"));
        lstTime.add(new SelectItem(value="1", label="簡體中文(ZH)"));
        return lstTime;
    }    
}
