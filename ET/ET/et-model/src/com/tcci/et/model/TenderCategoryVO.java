/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model;

import java.io.Serializable;
import java.util.Date;

/**
 * ET_TENDER_CATEGORY
 * @author Peter.pan
 */
public class TenderCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tenderId;
    private Long categoryId;
    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;
    private Boolean disabled;
    
    private Long categoryName;

    public TenderCategoryVO() {
    }

    public TenderCategoryVO(Long id) {
        this.id = id;
    }

    public TenderCategoryVO(Long id, long tenderId, long categoryId) {
        this.id = id;
        this.tenderId = tenderId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Long categoryName) {
        this.categoryName = categoryName;
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
        if (!(object instanceof TenderCategoryVO)) {
            return false;
        }
        TenderCategoryVO other = (TenderCategoryVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.TenderCategoryVO[ id=" + id + " ]";
    }
    
}
