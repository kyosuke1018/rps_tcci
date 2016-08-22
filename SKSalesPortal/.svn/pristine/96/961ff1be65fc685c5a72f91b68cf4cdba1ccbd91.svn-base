/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Table(name = "SK_PRODUCT_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkProductCategory.findAll", query = "SELECT s FROM SkProductCategory s"),
    @NamedQuery(name = "SkProductCategory.findById", query = "SELECT s FROM SkProductCategory s WHERE s.id = :id"),
    @NamedQuery(name = "SkProductCategory.findByCategory", query = "SELECT s FROM SkProductCategory s WHERE s.category = :category"),
    @NamedQuery(name = "SkProductCategory.findByDescription", query = "SELECT s FROM SkProductCategory s WHERE s.description = :description"),
    @NamedQuery(name = "SkProductCategory.findByRemark", query = "SELECT s FROM SkProductCategory s WHERE s.remark = :remark")})
public class SkProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 10)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 30)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "REMARK")
    private String remark;

    public SkProductCategory() {
    }

    public SkProductCategory(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof SkProductCategory)) {
            return false;
        }
        SkProductCategory other = (SkProductCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkProductCategory[ id=" + id + " ]";
    }
    
}
