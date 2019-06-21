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

    private Long id;
    private Long tenderId;
    private Long rfqId;
    private String mandt;
    private String ebeln;
    private long ebelp;
    private BigInteger extrow;
    private String ktext1;
    private BigDecimal menge;
    private String meins;
    private BigDecimal tbtwr;
    private String waers;
    private BigDecimal netwr;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    public RfqPmVO() {
    }

    public RfqPmVO(Long id) {
        this.id = id;
    }

    public RfqPmVO(Long id, String mandt, String ebeln, long ebelp, BigInteger extrow) {
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

    public long getEbelp() {
        return ebelp;
    }

    public void setEbelp(long ebelp) {
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
