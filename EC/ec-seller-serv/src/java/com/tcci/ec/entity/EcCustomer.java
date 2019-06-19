/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_CUSTOMER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCustomer.findAll", query = "SELECT e FROM EcCustomer e")
    , @NamedQuery(name = "EcCustomer.findByKey", query = "SELECT e FROM EcCustomer e WHERE e.storeId = :storeId AND e.memberId = :memberId")
    , @NamedQuery(name = "EcCustomer.findById", query = "SELECT e FROM EcCustomer e WHERE e.id = :id")})
public class EcCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CUSTOMER", sequenceName = "SEQ_CUSTOMER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMER")        
    private Long id;

    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "LEVEL_ID")
    private Long levelId;
    @InputCheckMeta(key="EC_CUSTOMER.CREDITS", type=DataTypeEnum.BIG_DECIMAL)
    @Column(name = "CREDITS")
    private BigDecimal credits;
    @Column(name = "CUS_TYPE")
    private String cusType;

    @InputCheckMeta(key="EC_CUSTOMER.EXPECTED_CREDITS", type=DataTypeEnum.BIG_DECIMAL)
    @Column(name = "EXPECTED_CREDITS")
    private BigDecimal expectedCredits;// 期望信用額度
    @Column(name = "APPLY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime; // 申請信用額度時間
    @Column(name = "CREDITS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creditsTime; // 最近設定信用額度時間
    @Column(name = "CREDITS_USER")
    private Long creditsUser; // 最近設定信用人員ID
    @InputCheckMeta(key="EC_CUSTOMER.MEMO", type=DataTypeEnum.STRING)
    @Column(name = "MEMO")
    private String memo; // 備註
    @Column(name = "CREDITS_CUR")
    private Long creditsCur; // 信用額度幣別 EC_CURRENCY.ID
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcCustomer() {
    }

    public EcCustomer(Long id) {
        this.id = id;
    }
    
    public EcCustomer(Long memberId, Long storeId) {
        this.memberId = memberId;
        this.storeId = storeId;
    }

    public BigDecimal getExpectedCredits() {
        return expectedCredits;
    }

    public void setExpectedCredits(BigDecimal expectedCredits) {
        this.expectedCredits = expectedCredits;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getCreditsTime() {
        return creditsTime;
    }

    public void setCreditsTime(Date creditsTime) {
        this.creditsTime = creditsTime;
    }

    public Long getCreditsUser() {
        return creditsUser;
    }

    public void setCreditsUser(Long creditsUser) {
        this.creditsUser = creditsUser;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreditsCur() {
        return creditsCur;
    }

    public void setCreditsCur(Long creditsCur) {
        this.creditsCur = creditsCur;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
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
        if (!(object instanceof EcCustomer)) {
            return false;
        }
        EcCustomer other = (EcCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCustomer[ id=" + id + " ]";
    }
    
}
