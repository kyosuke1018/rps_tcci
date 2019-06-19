/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class PriceRuleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String valueType;
    private Long value;
    private Date starttime;
    private Date endtime;
    private String targetSelect;
    private String targetType;
    private String allocationMethod;
    private String cusSel;
    private String prdSel;
    private Boolean oncePerCus;
    private Long usageLimit;
    private Long quantityRange;
    private Long priceRange;
    private Long shippingRange;
    private String prdSql;
    private String cusSql;
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    public PriceRuleVO() {
    }

    public PriceRuleVO(Long id) {
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof PriceRuleVO)) {
            return false;
        }
        PriceRuleVO other = (PriceRuleVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.PriceRuleVO[ id=" + id + " ]";
    }
    
}
