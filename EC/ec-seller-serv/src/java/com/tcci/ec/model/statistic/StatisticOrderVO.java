/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import com.tcci.ec.enums.PeriodEnum;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public class StatisticOrderVO extends StatisticVO {
    private Integer sortnum;
    private String period;

    public void genLabel(Locale locale){
        PeriodEnum enum1 = null;
        if( period!=null ){
            enum1 = PeriodEnum.getFromCode(period);
            
        }
        this.label = enum1!=null?enum1.getDisplayName(locale):"";
    }
    
    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    
    
}
