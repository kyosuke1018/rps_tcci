/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.math.BigDecimal;

/**
 *
 * @author Kyle.Cheng
 */
public class Store {
    private Long id;
    private String cname;
    private String ename;
    private String brief;//簡介
    private Seller seller;
    private boolean disabled;
//    private boolean favorite;
    private BigDecimal prate;//正評
    private BigDecimal nrate;//負評
    private Boolean opened;//開店
    
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public BigDecimal getPrate() {
        return prate;
    }

    public void setPrate(BigDecimal prate) {
        this.prate = prate;
    }

    public BigDecimal getNrate() {
        return nrate;
    }

    public void setNrate(BigDecimal nrate) {
        this.nrate = nrate;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

}
