/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ET_QUOTATION_ITEM
 * @author Peter.pan
 */
public class QuotationItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tenderId;
    private Long rfqId;
    private Long quoId;
    private Long ebelp;
    private String loekz;
    private String matnr;
    private String txz01;
    private BigDecimal menge;
    private String meins;
    private BigDecimal netpr;
    private BigDecimal peinh;
    private BigDecimal netwr;
    private BigDecimal brtwr;
    private String memo;
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    public QuotationItemVO() {
    }

    public QuotationItemVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getQuoId() {
        return quoId;
    }

    public void setQuoId(Long quoId) {
        this.quoId = quoId;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public String getLoekz() {
        return loekz;
    }

    public void setLoekz(String loekz) {
        this.loekz = loekz;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getTxz01() {
        return txz01;
    }

    public void setTxz01(String txz01) {
        this.txz01 = txz01;
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

    public BigDecimal getNetpr() {
        return netpr;
    }

    public void setNetpr(BigDecimal netpr) {
        this.netpr = netpr;
    }

    public BigDecimal getPeinh() {
        return peinh;
    }

    public void setPeinh(BigDecimal peinh) {
        this.peinh = peinh;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public BigDecimal getBrtwr() {
        return brtwr;
    }

    public void setBrtwr(BigDecimal brtwr) {
        this.brtwr = brtwr;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof QuotationItemVO)) {
            return false;
        }
        QuotationItemVO other = (QuotationItemVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.QuotationItemVO[ id=" + id + " ]";
    }
    
}
