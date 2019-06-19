/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.util;

import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class StoreFilter {
    private Long id;
    private String keyword;//關鍵字
    private String order;//new:最近更新;
    //限制資料筆數
    //ex:前20筆:startResult=0,maxResult=20
    //第51~100筆:startResult=50,maxResult=50
    private int startResult;//起始筆數
    private int maxResult;//最大筆數
    private List<Long> areaList;//銷售地區 AREA_ID list
    private List<Long> storeIdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getStartResult() {
        return startResult;
    }

    public void setStartResult(int startResult) {
        this.startResult = startResult;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public List<Long> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Long> areaList) {
        this.areaList = areaList;
    }

    public List<Long> getStoreIdList() {
        return storeIdList;
    }

    public void setStoreIdList(List<Long> storeIdList) {
        this.storeIdList = storeIdList;
    }

}
