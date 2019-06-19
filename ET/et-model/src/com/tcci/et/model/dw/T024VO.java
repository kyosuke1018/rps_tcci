/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.dw;

import java.util.Date;

/**
 * 採購群組
 * @author Peter.pan
 */
public class T024VO {
    private String mandt;
    private String ekgrp;
    private String eknam;
    private String ektel;
    private String ldest;
    private String telfx;
    private String telNumber;
    private String telExtens;
    private String smtpAddr;
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    public String getEknam() {
        return eknam;
    }

    public void setEknam(String eknam) {
        this.eknam = eknam;
    }

    public String getEktel() {
        return ektel;
    }

    public void setEktel(String ektel) {
        this.ektel = ektel;
    }

    public String getLdest() {
        return ldest;
    }

    public void setLdest(String ldest) {
        this.ldest = ldest;
    }

    public String getTelfx() {
        return telfx;
    }

    public void setTelfx(String telfx) {
        this.telfx = telfx;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getTelExtens() {
        return telExtens;
    }

    public void setTelExtens(String telExtens) {
        this.telExtens = telExtens;
    }

    public String getSmtpAddr() {
        return smtpAddr;
    }

    public void setSmtpAddr(String smtpAddr) {
        this.smtpAddr = smtpAddr;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    
}
