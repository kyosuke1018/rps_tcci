/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * ET_RFQ_PM
 * @author Peter.pan
 */
public class RfqPmVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    //private Long rfqEbelp; // FK ET_RFQ_EKPO.EBELP
    private String mandt; // 用戶端
    private String ebeln; // 採購文件號碼
    private Long ebelp; // 採購文件的項目號碼
    private String banfn;// 請購單號碼
    private Long bnfpo;// 請購單的項目號碼
    private BigInteger extrow; // 服務行號
    private String ktext1; // 說明
    private BigDecimal menge; // 數量
    private String meins; // 基礎計量單位
    
    private BigDecimal tbtwr; // 單價
    private String waers; // 幣別碼
    private BigDecimal netwr; // 總價
    
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間

    public RfqPmVO() {
    }

    public RfqPmVO(Long id) {
        this.id = id;
    }

    public RfqPmVO(Long id, String mandt, String ebeln, Long ebelp, BigInteger extrow) {
        this.id = id;
        this.mandt = mandt;
        this.ebeln = ebeln;
        this.ebelp = ebelp;
        this.extrow = extrow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public BigInteger getExtrow() {
        return extrow;
    }

    public void setExtrow(BigInteger extrow) {
        this.extrow = extrow;
    }

    public String getKtext1() {
        return ktext1;
    }

    public void setKtext1(String ktext1) {
        this.ktext1 = ktext1;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public BigDecimal getTbtwr() {
        return tbtwr;
    }

    public void setTbtwr(BigDecimal tbtwr) {
        this.tbtwr = tbtwr;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RfqPmVO)) {
            return false;
        }
        RfqPmVO other = (RfqPmVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.RfqPmVO[ id=" + id + " ]";
    }
    
}