/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;

/**
 *
 * @author Peter.pan
 */
public class StatisticOrderStatusVO extends StatisticVO {
    private String payStatus;
    private String shipStatus;
    
    public void genLabel(){
        PayStatusEnum payStatusEnum = PayStatusEnum.getFromCode(payStatus);
        ShipStatusEnum shipStatusEnum = ShipStatusEnum.getFromCode(shipStatus);
        
        StringBuilder sb = new StringBuilder();
        if( payStatusEnum!=null ){
            sb.append("【").append(payStatusEnum.getDisplayName()).append("】");
        }
        if( shipStatusEnum!=null ){
            sb.append("【").append(shipStatusEnum.getDisplayName()).append("】");
        }
        this.setLabel(sb.toString());
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }
    
}
