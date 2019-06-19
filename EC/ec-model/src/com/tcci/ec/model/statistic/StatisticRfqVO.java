/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import com.tcci.ec.enums.RfqStatusEnum;

/**
 *
 * @author Peter.pan
 */
public class StatisticRfqVO extends StatisticVO {
    private String status;
    
    public void genLabel(){
        RfqStatusEnum rfqStatusEnum = RfqStatusEnum.getFromCode(status);
        
        StringBuilder sb = new StringBuilder();
        if( rfqStatusEnum!=null ){
            sb.append("【").append(rfqStatusEnum.getDisplayName()).append("】");
        }
        this.setLabel(sb.toString());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
