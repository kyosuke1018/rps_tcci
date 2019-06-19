/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.et.enums.FormStatusEnum;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "ET_VC_FORM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtVcForm.findAll", query = "SELECT e FROM EtVcForm e")
    , @NamedQuery(name = "EtVcForm.findById", query = "SELECT e FROM EtVcForm e WHERE e.id = :id")
    , @NamedQuery(name = "EtVcForm.findByVender", query = "SELECT e FROM EtVcForm e WHERE e.mandt = :mandt AND e.venderCode = :venderCode")})
public class EtVcForm implements Serializable, Persistable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_VC_FORM", sequenceName = "SEQ_VC_FORM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VC_FORM")        
    private Long id;
    @Column(name = "FACTORY_ID")
    private Long factoryId;
    @Column(name = "MANDT")
    private String mandt;
    @Column(name = "LIFNR_UI")
    private String venderCode;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "CIDS")
    private String cids;
    @Column(name = "CNAMES")
    private String cnames;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private FormStatusEnum status;
    @JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne
    private TcProcess process;//流程
    
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

    public EtVcForm() {
    }

    public EtVcForm(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getCids() {
        return cids;
    }

    public void setCids(String cids) {
        this.cids = cids;
    }

    public String getCnames() {
        return cnames;
    }

    public void setCnames(String cnames) {
        this.cnames = cnames;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public FormStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FormStatusEnum status) {
        this.status = status;
    }

    public TcProcess getProcess() {
        return process;
    }

    public void setProcess(TcProcess process) {
        this.process = process;
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
        if (!(object instanceof EtVcForm)) {
            return false;
        }
        EtVcForm other = (EtVcForm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EtVcForm[ id=" + id + " ]";
    }
    
    public String mailSubject() {
        return "(單號:" + id + ")";
    }
    
}
