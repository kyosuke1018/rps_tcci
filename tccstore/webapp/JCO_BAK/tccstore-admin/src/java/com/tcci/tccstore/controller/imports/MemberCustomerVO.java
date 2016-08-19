/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcMemberCustomer;
import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public class MemberCustomerVO extends ExcelVOBase {

    private String loginAccount;
    private String customer;
    private String customers;

    private EcMemberCustomer entity;
    private EcMember ecMember;
    private EcCustomer ecCustomer;
    private List<EcCustomer> ecCustomers;

    // getter, setter
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public EcMemberCustomer getEntity() {
        return entity;
    }

    public void setEntity(EcMemberCustomer entity) {
        this.entity = entity;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public EcCustomer getEcCustomer() {
        return ecCustomer;
    }

    public void setEcCustomer(EcCustomer ecCustomer) {
        this.ecCustomer = ecCustomer;
    }

    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    public List<EcCustomer> getEcCustomers() {
        return ecCustomers;
    }

    public void setEcCustomers(List<EcCustomer> ecCustomers) {
        this.ecCustomers = ecCustomers;
    }

}
