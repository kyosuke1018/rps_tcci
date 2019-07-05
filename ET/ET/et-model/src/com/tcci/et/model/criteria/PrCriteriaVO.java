/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class PrCriteriaVO extends BaseCriteriaVO {
    private String mandt;
    private String banfn;
    private Long bnfpo;
    private String matnr;
    private String werks;
    private String bukrs;
    private String spras;

    private Date badat;
    private Date badatS;
    private Date badatE;

    private String land1;//國家代碼
    private String waers;//幣別碼
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

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getBanfn() {
        return banfn;
    }

    public void setBanfn(String banfn) {
        this.banfn = banfn;
    }

    public Long getBnfpo() {
        return bnfpo;
    }

    public void setBnfpo(Long bnfpo) {
        this.bnfpo = bnfpo;
    }

    public Date getBadat() {
        return badat;
    }

    public void setBadat(Date badat) {
        this.badat = badat;
    }

    public Date getBadatS() {
        return badatS;
    }

    public void setBadatS(Date badatS) {
        this.badatS = badatS;
    }

    public Date getBadatE() {
        return badatE;
    }

    public void setBadatE(Date badatE) {
        this.badatE = badatE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }


}
