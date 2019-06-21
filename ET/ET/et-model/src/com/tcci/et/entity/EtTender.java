/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_TENDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtTender.findAll", query = "SELECT k FROM EtTender k"),
    @NamedQuery(name = "EtTender.findById", query = "SELECT k FROM EtTender k WHERE k.id = :id"),
    @NamedQuery(name = "EtTender.findByCode", query = "SELECT k FROM EtTender k WHERE k.code = :code")})
public class EtTender implements ContentHolder, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="SEQ_TENDER",sequenceName = "SEQ_TENDER", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_TENDER")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CODE")
    private String code;
    @Size(max = 600)
    @Column(name = "TITLE")
    private String title;
    @Column(name = "SUMMARY")
    private String summary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datadate;
    @Column(name = "S_SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ssaleDate;
    @Column(name = "E_SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date esaleDate;
    @Column(name = "S_TENDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stenderDate;
    @Column(name = "E_TENDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date etenderDate;
    @Column(name = "VERIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifyDate;
    @Column(name = "CLOSE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;
    @Column(name = "FACTORY_ID")
    private Long factoryId;
    @Column(name = "AREA_ID")
    private Long areaId;
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "INSURANCE_AMOUNT")
    private BigDecimal insuranceAmount;
    @Size(max = 3)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 3)
    @Column(name = "LANG")
    private String lang;
//    @Lob
//    @Column(name = "CONTENT")
//    private String content;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtTender() {
    }

    public EtTender(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSsaleDate() {
        return ssaleDate;
    }

    public void setSsaleDate(Date ssaleDate) {
        this.ssaleDate = ssaleDate;
    }

    public Date getEsaleDate() {
        return esaleDate;
    }

    public void setEsaleDate(Date esaleDate) {
        this.esaleDate = esaleDate;
    }

    public Date getStenderDate() {
        return stenderDate;
    }

    public void setStenderDate(Date stenderDate) {
        this.stenderDate = stenderDate;
    }

    public Date getEtenderDate() {
        return etenderDate;
    }

    public void setEtenderDate(Date etenderDate) {
        this.etenderDate = etenderDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
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

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getDatadate() {
        return datadate;
    }

    public void setDatadate(Date datadate) {
        this.datadate = datadate;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
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
        if (!(object instanceof EtTender)) {
            return false;
        }
        EtTender other = (EtTender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.KbPublication[ id=" + id + " ]";
    }
    
}
