/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_BANNER")
@NamedQueries({
    @NamedQuery(name = "EcBanner.findByCategory", query = "SELECT e FROM EcBanner e WHERE e.active=TRUE AND e.category=:category")
})
public class EcBanner implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATEGORY")
    private int category;
    @Size(max = 255)
    @Column(name = "SUBJECT")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "CONTENT_URL")
    private String contentUrl;
    @Size(max = 255)
    @Column(name = "LINK")
    private String link;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecBanner")
    private List<EcBannerProduct> ecBannerProductList;

    public EcBanner() {
    }

    public EcBanner(Long id) {
        this.id = id;
    }

    public EcBanner(Long id, int category, String contentUrl, boolean active) {
        this.id = id;
        this.category = category;
        this.contentUrl = contentUrl;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EcBannerProduct> getEcBannerProductList() {
        return ecBannerProductList;
    }

    public void setEcBannerProductList(List<EcBannerProduct> ecBannerProductList) {
        this.ecBannerProductList = ecBannerProductList;
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
        if (!(object instanceof EcBanner)) {
            return false;
        }
        EcBanner other = (EcBanner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcBanner[ id=" + id + " ]";
    }

}
