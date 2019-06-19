/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.annotation.model;

import java.util.List;

/**
 * for 多 Sheets EXCEL
 * 
 * @author Peter
 */
public class ExcelMultiSheetVO {
    private String name; // sheet name
    private Class clazz; // 對應 VO class
    private List dataList; // List<clazz>
    private List<String> headers; // excel header
    private String templateFileName; // template file name

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }
    //</editor-fold>
    
}
