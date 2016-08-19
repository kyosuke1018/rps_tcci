/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.model.member;

/**
 *
 * @author Jimmy.Lee
 */
public class Member {

    private String account;
    private String name;
    private String email;

    public Member() {
    }

    public Member(String account, String name, String email) {
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
}
