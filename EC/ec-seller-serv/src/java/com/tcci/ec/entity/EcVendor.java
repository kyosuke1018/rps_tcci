/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_VENDOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcVendor.findAll", query = "SELECT e FROM EcVendor e")
    , @NamedQuery(name = "EcVendor.findById", query = "SELECT e FROM EcVendor e WHERE e.id = :id")
    , @NamedQuery(name = "EcVendor.findByType", query = "SELECT e FROM EcVendor e WHERE e.type = :type")
    , @NamedQuery(name = "EcVendor.findByStoreId", query = "SELECT e FROM EcVendor e WHERE e.storeId = :storeId")})
public class EcVendor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_VENDOR", sequenceName = "SEQ_VENDOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VENDOR")
    private Long id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "CODE")
    private String code;
    @Column(name = "DISABLED")
    private Boolean disabled = false;
    /*
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "ID_TYPE")
    private String idType;
    @Column(name = "ID_CODE")
    private String idCode;
    @Column(name = "NICKNAME")
    private String nickname;
    @Column(name = "BRIEF")
    private String brief;
    @Column(name = "EMAIL1")
    private String email1;
    @Column(name = "EMAIL2")
    private String email2;
    @Column(name = "EMAIL3")
    private String email3;
    @Column(name = "TEL1")
    private String tel1;
    @Column(name = "TEL2")
    private String tel2;
    @Column(name = "TEL3")
    private String tel3;
    @Column(name = "FAX1")
    private String fax1;
    @Column(name = "FAX2")
    private String fax2;
    @Column(name = "COUNTRY")
    private Long country;
    @Column(name = "STATE")
    private Long state;
    @Column(name = "ADDR1")
    private String addr1;
    @Column(name = "ADDR2")
    private String addr2;
    
    @Column(name = "OWNER1")
    private Long owner1;
    @Column(name = "OWNER2")
    private Long owner2;
    @Column(name = "CONTACT1")
    private Long contact1;
    @Column(name = "CONTACT2")
    private Long contact2;
    @Column(name = "CONTACT3")
    private Long contact3;
    */
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcVendor() {
    }

    public EcVendor(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
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
        if (!(object instanceof EcVendor)) {
            return false;
        }
        EcVendor other = (EcVendor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcVendor[ id=" + id + " ]";
    }
    
}
