/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import com.tcci.ec.enums.ProductStatusEnum;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public class StatisticPrdStatusVO extends StatisticVO {
    private String status;
    
    public void genLabel(Locale locale){
        ProductStatusEnum statusEnum = ProductStatusEnum.getFromCode(status);
        
        StringBuilder sb = new StringBuilder();
        if( statusEnum!=null ){
            sb.append("【").append(statusEnum.getDisplayName(locale)).append("】");
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