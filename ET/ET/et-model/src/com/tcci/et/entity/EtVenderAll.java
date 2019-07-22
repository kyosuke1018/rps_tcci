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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter.pan
 */
@Entity
@Table(name = "ET_VENDER_ALL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtVenderAll.findAll", query = "SELECT e FROM EtVenderAll e")
    , @NamedQuery(name = "EtVenderAll.findById", query = "SELECT e FROM EtVenderAll e WHERE e.id = :id")
    , @NamedQuery(name = "EtVenderAll.findByName", query = "SELECT e FROM EtVenderAll e WHERE e.name = :name")
    , @NamedQuery(name = "EtVenderAll.findByApplyId", query = "SELECT e FROM EtVenderAll e WHERE e.applyId = :applyId")
    , @NamedQuery(name = "EtVenderAll.findByMandtLifnr", query = "SELECT e FROM EtVenderAll e WHERE e.mandt = :mandt and e.lifnr = :lifnr")
    , @NamedQuery(name = "EtVenderAll.findByMandt", query = "SELECT e FROM EtVenderAll e WHERE e.mandt = :mandt")
    , @NamedQuery(name = "EtVenderAll.findByLifnr", query = "SELECT e FROM EtVenderAll e WHERE e.lifnr = :lifnr")
    , @NamedQuery(name = "EtVenderAll.findByLifnrUi", query = "SELECT e FROM EtVenderAll e WHERE e.lifnrUi = :lifnrUi")})
public class EtVenderAll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_VENDER_ALL", sequenceName = "SEQ_VENDER_ALL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VENDER_ALL")        
    private Long id;
    @Size(max = 256)
    @Column(name = "NAME")
    private String name;
    @Column(name = "APPLY_ID")
    private Long applyId;
    @Size(max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Size(max = 20)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 20)
    @Column(name = "LIFNR_UI")
    private String lifnrUi;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "SYNCTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date synctimestamp;
    @Size(max = 3)
    @Column(name = "STATUS")
    private String status;
    
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

    public EtVenderAll() {
    }

    public EtVenderAll(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getLifnrUi() {
        return lifnrUi;
    }

    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getSynctimestamp() {
        return synctimestamp;
    }

    public void setSynctimestamp(Date synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof EtVenderAll)) {
            return false;
        }
        EtVenderAll other = (EtVenderAll) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtVenderAll[ id=" + id + " ]";
    }
    
}
