/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.ec.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 *
 * @author leno
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    public static boolean isWrapperType(Class<?> clazz){
        return WRAPPER_TYPES.contains(clazz);
    }
    
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(Date.class);
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    public void exportExcel(String title, Map<String,String> fields,
            Collection<T> dataset, OutputStream out, String pattern) throws Exception {
        List<String> fieldArray = new ArrayList<String>();
        List<String> xlsHeaderArray = new ArrayList<String>();
        //TODO fields是否为空
        if( fields==null || fields.isEmpty() ){
            fields = new HashMap<String,String>();
            if(dataset.iterator().hasNext()){
                T instance = dataset.iterator().next();
                Field[] ff= instance.getClass().getDeclaredFields();
                //TODO 是否是简单类型
                for(Field f: ff){
                    if(f.getType().isPrimitive() || isWrapperType(f.getType())){
                        System.out.println(f.getName());
                        fieldArray.add(f.getName());
                        xlsHeaderArray.add(f.getName());
                        fields.put(f.getName(), f.getName());
                    }
                }
            }
        }else{
            for(String key:fields.keySet()){
                fieldArray.add(key);
                xlsHeaderArray.add(fields.get(key));
            }
        }
        
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < xlsHeaderArray.size(); i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(fields.get(fieldArray.get(i)));
            System.out.println("--"+text);
            cell.setCellValue(text);
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while( it.hasNext() ){
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            //TODO 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            for (short i = 0; i < fieldArray.size(); i++) {
                HSSFCell cell = row.createCell(i);
                try {
                    Object value = BeanUtils.getProperty(t, fieldArray.get(i));
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    }else if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        String str = "是";
                        if (!bValue) {
                            str = "否";
                        }
                        cell.setCellValue(str);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        String str = sdf.format(date);
                        cell.setCellValue(str);
                    } else if (value instanceof byte[]) {
                        //TODO nothing
                        cell.setCellValue("");
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if(value!=null){
                            textValue = value.toString();
                            cell.setCellValue(textValue);
                        }
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }
        }
        
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}