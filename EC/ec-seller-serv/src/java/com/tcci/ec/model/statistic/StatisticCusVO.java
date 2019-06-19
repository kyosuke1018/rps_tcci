/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public class StatisticCusVO extends StatisticVO {
    private Long id;
    private String loginAccount;
    private String name;
    
    public void genLabel(Locale locale){
        this.label = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

