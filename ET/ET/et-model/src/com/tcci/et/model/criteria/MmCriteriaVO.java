/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

/**
 *
 * @author Peter.pan
 */
public class MmCriteriaVO extends BaseCriteriaVO {
    private String mandt;
    private String matnr;
    private String werks;
    private String bukrs;
    private String spras;
    
    private String dimid;
    
    private String bstyp;
    private String matnrUI;

    public void setMatnr(String matnr) {
        this.matnr = matnr;
        if( matnr!=null ){
            matnrUI = matnr.startsWith("000000")?matnr.substring(6):matnr;
        }else{
            matnrUI = null;
        }
    }

    public String getMatnrUI() {
        return matnrUI;
    }

    public void setMatnrUI(String matnrUI) {
        this.matnrUI = matnrUI;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBstyp() {
        return bstyp;
    }

    public void setBstyp(String bstyp) {
        this.bstyp = bstyp;
    }

    public String getMatnr() {
        return matnr;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getDimid() {
        return dimid;
    }

    public void setDimid(String dimid) {
        this.dimid = dimid;
    }

    
}
