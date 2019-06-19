/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class CreditsLogVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long memberId;
    private Long storeId;
    private BigDecimal credits;
    private BigDecimal creditsOri;
    private BigDecimal creditsDiff;
    private Long creditsCur;
    private String reason;
    
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private String loginAccount;
    private String name;
    private String creatorAccount;
    private String creatorName;
    
    public CreditsLogVO() {
    }

    public CreditsLogVO(Long id) {
        this.id = id;
    }

    public CreditsLogVO(Long id, Long memberId, Long storeId, BigDecimal credits, BigDecimal creditsDiff, Long creditsCur) {
        this.id = id;
        this.memberId = memberId;
        this.storeId = storeId;
        this.credits = credits;
        this.creditsDiff = creditsDiff;
        this.creditsCur = creditsCur;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public BigDecimal getCreditsOri() {
        return creditsOri;
    }

    public void setCreditsOri(BigDecimal creditsOri) {
        this.creditsOri = creditsOri;
    }

    public BigDecimal getCreditsDiff() {
        return creditsDiff;
    }

    public void setCreditsDiff(BigDecimal creditsDiff) {
        this.creditsDiff = creditsDiff;
    }

    public Long getCreditsCur() {
        return creditsCur;
    }

    public void setCreditsCur(Long creditsCur) {
        this.creditsCur = creditsCur;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(object instanceof CreditsLogVO)) {
            return false;
        }
        CreditsLogVO other = (CreditsLogVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.CreditsLogVO[ id=" + id + " ]";
    }

    
}
