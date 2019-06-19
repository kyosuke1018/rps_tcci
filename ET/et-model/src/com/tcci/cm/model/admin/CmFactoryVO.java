/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import java.io.Serializable;

/**
 *
 * @author gilbert
 */
public class CmFactoryVO implements Serializable, Comparable<CmFactoryVO> {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String sapClientCode;
    private String currency;
    private Long categoryId;
    private Long companyId;

    private boolean selected;

    public CmFactoryVO() {
    }

    public CmFactoryVO(Long id) {
        this.id = id;
    }

    public CmFactoryVO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
        if (!(object instanceof CmFactoryVO)) {
            return false;
        }
        
        CmFactoryVO other = (CmFactoryVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        
        if( sapClientCode!=null && code!=null && other.sapClientCode!=null && other.code!=null ){
            if( sapClientCode.equals(other.sapClientCode) && code.equals(other.code) ){
                return true;
            }else{
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+"[ id=" + id + " ]";
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    // 顯式代表字串
    public String getDisplayLabel(){
        return this.getCode() + "-" + this.getName();
    }    

    @Override
    public int compareTo(CmFactoryVO o) {
        if( o==null || this.getCode()==null ){
            return 0;
        }
        return this.getCode().compareTo(o.getCode());// QAS & PRD 環境不正常排序????
    }
}
