/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model;

import java.util.Date;

/**
 *
 * @author Jimmy.Lee
 */
public class TestModel {

    private String account;
    private String name;
    private String email;
    private Date createtime;

    public TestModel() {
    }
    
    public TestModel(String account, String name, String email) {
        this.account = account;
        this.name = name;
        this.email = email;
    }
    
    // getter, setter
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}