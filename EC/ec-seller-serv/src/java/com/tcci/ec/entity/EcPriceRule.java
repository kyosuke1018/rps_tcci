/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@Table(name = "EC_PRICE_RULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcPriceRule.findAll", query = "SELECT e FROM EcPriceRule e")
    , @NamedQuery(name = "EcPriceRule.findById", query = "SELECT e FROM EcPriceRule e WHERE e.id = :id")
    , @NamedQuery(name = "EcPriceRule.findByTitle", query = "SELECT e FROM EcPriceRule e WHERE e.title = :title")
    , @NamedQuery(name = "EcPriceRule.findByValueType", query = "SELECT e FROM EcPriceRule e WHERE e.valueType = :valueType")})
public class EcPriceRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PRICE_RULE", sequenceName = "SEQ_PRICE_RULE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRICE_RULE")        
    private Long id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "VALUE_TYPE")
    private String valueType;
    @Column(name = "VALUE")
    private Long value;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Column(name = "TARGET_SELECT")
    private String targetSelect;
    @Column(name = "TARGET_TYPE")
    private String targetType;
    @Column(name = "ALLOCATION_METHOD")
    private String allocationMethod;
    @Column(name = "CUS_SEL")
    private String cusSel;
    @Column(name = "PRD_SEL")
    private String prdSel;
    @Column(name = "ONCE_PER_CUS")
    private Boolean oncePerCus;
    @Column(name = "USAGE_LIMIT")
    private Long usageLimit;
    @Column(name = "QUANTITY_RANGE")
    private Long quantityRange;
    @Column(name = "PRICE_RANGE")
    private Long priceRange;
    @Column(name = "SHIPPING_RANGE")
    private Long shippingRange;
    @Column(name = "PRD_SQL")
    private String prdSql;
    @Column(name = "CUS_SQL")
    private String cusSql;

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

    public EcPriceRule() {
    }

    public EcPriceRule(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getTargetSelect() {
        return targetSelect;
    }

    public void setTargetSelect(String targetSelect) {
        this.targetSelect = targetSelect;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getAllocationMethod() {
        return allocationMethod;
    }

    public void setAllocationMethod(String allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public String getCusSel() {
        return cusSel;
    }

    public void setCusSel(String cusSel) {
        this.cusSel = cusSel;
    }

    public String getPrdSel() {
        return prdSel;
    }

    public void setPrdSel(String prdSel) {
        this.prdSel = prdSel;
    }

    public Boolean getOncePerCus() {
        return oncePerCus;
    }

    public void setOncePerCus(Boolean oncePerCus) {
        this.oncePerCus = oncePerCus;
    }

    public Long getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Long usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Long getQuantityRange() {
        return quantityRange;
    }

    public void setQuantityRange(Long quantityRange) {
        this.quantityRange = quantityRange;
    }

    public Long getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(Long priceRange) {
        this.priceRange = priceRange;
    }

    public Long getShippingRange() {
        return shippingRange;
    }

    public void setShippingRange(Long shippingRange) {
        this.shippingRange = shippingRange;
    }

    public String getPrdSql() {
        return prdSql;
    }

    public void setPrdSql(String prdSql) {
        this.prdSql = prdSql;
    }

    public String getCusSql() {
        return cusSql;
    }

    public void setCusSql(String cusSql) {
        this.cusSql = cusSql;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EcPriceRule)) {
            return false;
        }
        EcPriceRule other = (EcPriceRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcPriceRule[ id=" + id + " ]";
    }
    
}
