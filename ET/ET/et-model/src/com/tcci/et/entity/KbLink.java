/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.org.TcUser;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "KB_LINK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KbLink.findAll", query = "SELECT k FROM KbLink k"),
    @NamedQuery(name = "KbLink.findById", query = "SELECT k FROM KbLink k WHERE k.id = :id"),
    @NamedQuery(name = "KbLink.findByPrimaryid", query = "SELECT k FROM KbLink k WHERE k.primaryid = :primaryid")})
public class KbLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="KB_LINK_ID_SEQ",sequenceName = "KB_LINK_ID_SEQ", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="KB_LINK_ID_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PRIMARYTYPE")
    private String primaryType;
    @Column(name = "PRIMARYID")
    private Long primaryid;
    @Size(max = 1024)
    @Column(name = "URL")
    private String url;
    @Column(name = "OPENMETHOD")
    private String openMethod;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    public KbLink() {
    }

    public KbLink(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Long getPrimaryid() {
        return primaryid;
    }

    public void setPrimaryid(Long primaryid) {
        this.primaryid = primaryid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOpenMethod() {
        return openMethod;
    }

    public void setOpenMethod(String openMethod) {
        this.openMethod = openMethod;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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
        if (!(object instanceof KbLink)) {
            return false;
        }
        KbLink other = (KbLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.KbLink[ id=" + id + " ]";
    }
    
}
