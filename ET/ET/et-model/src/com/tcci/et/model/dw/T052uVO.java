/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.dw;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class T052uVO {
    private String mandt;
    private String spras;
    private String zterm;
    private short ztagg;
    
    private String text1;
    private Date syncTimeStamp;

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getZterm() {
        return zterm;
    }

    public void setZterm(String zterm) {
        this.zterm = zterm;
    }

    public short getZtagg() {
        return ztagg;
    }

    public void setZtagg(short ztagg) {
        this.ztagg = ztagg;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public Date getSyncTimeStamp() {
        return syncTimeStamp;
    }

    public void setSyncTimeStamp(Date syncTimeStamp) {
        this.syncTimeStamp = syncTimeStamp;
    }


}
