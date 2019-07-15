/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gilbert
 */
@Entity
@Table(name = "CM_FACTORY")
@NamedQueries({
    @NamedQuery(name = "CmFactory.findAll", query = "SELECT p FROM CmFactory p"),
    @NamedQuery(name = "CmFactory.findByCategory", query = "SELECT p FROM CmFactory p where p.categoryId=:categoryId order by p.sapClientCode, p.code"),
    @NamedQuery(name = "CmFactory.findByCompany", query = "SELECT p FROM CmFactory p where p.companyId=:companyId order by p.sapClientCode, p.code")
})
public class CmFactory implements Serializable, Comparable<CmFactory> {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 10)
    @Column(name = "SAP_CLIENT_CODE")
    private String sapClientCode;
    @Size(max = 3)
    @Column(name = "CURRENCY")
    private String currency;
    
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    @ManyToOne
    private CmFactoryCategory categoryId;
    
    @Column(name = "COMPANY_ID")
    private Long companyId;
        
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factoryId")
    private List<CmUserfactory> userfactoryList;
    
    @Transient
    private boolean selected;

    public CmFactory() {
    }

    public CmFactory(Long id) {
        this.id = id;
    }

    public CmFactory(Long id, String code, String name) {
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

    public CmFactoryCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CmFactoryCategory categoryId) {
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
        if (!(object instanceof CmFactory)) {
            return false;
        }
        
        CmFactory other = (CmFactory) object;
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

    public List<CmUserfactory> getUserfactoryList() {
        return userfactoryList;
    }

    public void setUserfactoryList(List<CmUserfactory> userfactoryList) {
        this.userfactoryList = userfactoryList;
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
    public int compareTo(CmFactory o) {
        if( o==null || this.getCode()==null ){
            return 0;
        }
        return this.getCode().compareTo(o.getCode());// QAS & PRD 環境不正常排序????
    }
}