/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

/**
 *
 * @author Peter.pan
 */
public class StatisticCusLevelVO extends StatisticVO {
    private Long levelId;
    
    public void genLabel(){
        StringBuilder sb = new StringBuilder();
        if( this.label!=null ){
            sb.append("【").append(this.label).append("】");
        }
        this.setLabel(sb.toString());
    }
    
    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
}
