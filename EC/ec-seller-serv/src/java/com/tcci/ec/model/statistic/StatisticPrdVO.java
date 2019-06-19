/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter.pan
 */
public class StatisticPrdVO extends StatisticVO {
    private Long id;
    private String cname;
    private String ename;
    
    public void genLabel(Locale locale){
        if( Locale.TRADITIONAL_CHINESE==locale || Locale.SIMPLIFIED_CHINESE== locale ){
            this.label = cname;
        }else{
            this.label = StringUtils.isNotBlank(ename)?ename:cname;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
