/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_SEQUENCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtSequence.findAll", query = "SELECT e FROM EtSequence e")
    , @NamedQuery(name = "EtSequence.findById", query = "SELECT e FROM EtSequence e WHERE e.id = :id")
    , @NamedQuery(name = "EtSequence.findByType", query = "SELECT e FROM EtSequence e WHERE e.type = :type")
    , @NamedQuery(name = "EtSequence.findByClientType", query = "SELECT e FROM EtSequence e WHERE e.sapClientCode = :sapClientCode and e.type = :type")
    , @NamedQuery(name = "EtSequence.findBySeqNum", query = "SELECT e FROM EtSequence e WHERE e.seqNum = :seqNum")})
public class EtSequence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_SEQUENCE", sequenceName = "SEQ_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SEQUENCE")        
    private Long id;
    @Size(max = 10)
    @Column(name = "SAP_CLIENT_CODE")
    private String sapClientCode;
    @Size(max = 10)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "SEQ_NUM")
    private Long seqNum;
    @Column(name = "MODIFIER")
    private Long modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtSequence() {
    }

    public EtSequence(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EtSequence)) {
            return false;
        }
        EtSequence other = (EtSequence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtSequence[ id=" + id + " ]";
    }
    
}
