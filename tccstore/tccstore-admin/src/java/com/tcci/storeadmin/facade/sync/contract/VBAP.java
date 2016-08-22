/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.contract;

/**
 *
 * @author Jimmy.Lee
 */
public class VBAP {

    private String vbeln; // 合約號
    private int posnr;    // 項次
    private String matnr; // 料號
    private String waerk; // 僅保留 'RMB'
    private String werks; // 廠代碼

    // getter, setter
    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public int getPosnr() {
        return posnr;
    }

    public void setPosnr(int posnr) {
        this.posnr = posnr;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getWaerk() {
        return waerk;
    }

    public void setWaerk(String waerk) {
        this.waerk = waerk;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

}
