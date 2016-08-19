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
public class VBAK {

    private String vbeln; // 合約號
    private String vkorg; // 銷售組織
    private String kunnr; // 客戶代碼
    private String zzadd00; // 合約名稱(僅留非空白)

    // getter, setter
    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getZzadd00() {
        return zzadd00;
    }

    public void setZzadd00(String zzadd00) {
        this.zzadd00 = zzadd00;
    }

}
