/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter.pan
 */
public class RandomSelectVO {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private long id;
    private int num;
    private List<Long> containsList;
    private Long selectedId;

    /**
     * 自 containsList 隨機取出一筆資料
     * @return 
     */
    public Long findSelectedId(){
        if( containsList!=null && !containsList.isEmpty() ){
            if( containsList.size()==1 ){
                logger.debug("RandomSelectVO id="+id+", num="+num+", selectedId="+0);
                return containsList.get(0);
            }else{
                int i = randomInteger(0, containsList.size()-1);
                logger.debug("RandomSelectVO id="+id+", num="+num+", selectedId="+i);
                return containsList.get(i);
            }
        }
        return null;
    }

    public int randomInteger(int min, int max) {
        int randomNum = min + (int)(Math.random() * ((max - min) + 1));
        return randomNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getContainsList() {
        return containsList;
    }

    public void setContainsList(List<Long> containsList) {
        this.containsList = containsList;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
