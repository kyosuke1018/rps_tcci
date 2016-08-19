/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.util;

import com.tcci.tccstore.enums.PartnerStatusEnum;

/**
 *
 * @author Neo.Fu
 */
public class PartnerFilter {
    private Long id;
    private String name;
    private Boolean active;
    private PartnerStatusEnum status;
    private String loginAccount;
    private String province;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PartnerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PartnerStatusEnum status) {
        this.status = status;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    
}
