/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import java.io.Serializable;

/**
 *
 * @author Peter.pan
 */
public class CmCompanyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String companyName;
    private String sapClient;
    private String sapClientCode;
    private String language;
    private Integer sortNum;

    private boolean selected;
    
    public CmCompanyVO() {
    }

    public CmCompanyVO(Long id) {
        this.id = id;
    }

    //<editor-fold defaultstate="collapsed" desc="for getter & setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getSapClient() {
        return sapClient;
    }

    public void setSapClient(String sapClient) {
        this.sapClient = sapClient;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmCompanyVO)) {
            return false;
        }
        CmCompanyVO other = (CmCompanyVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.cm.model.admin.CmCompanyVO[ id=" + id + " ]";
    }
    
}
