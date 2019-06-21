/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.dw;

import java.util.Date;

/**
 * 採購組織
 * @author Peter.pan
 */
public class T024eVO {
    private String mandt;
    private String ekorg;
    private String bukrs;// = "NA";// 中橡資料有NULL狀況
    private String ekotx;
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getEkotx() {
        return ekotx;
    }

    public void setEkotx(String ekotx) {
        this.ekotx = ekotx;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }

    
}
