/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.model.interfaces.IOperator;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_MEMBER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcMember.findAll", query = "SELECT e FROM EcMember e")
    , @NamedQuery(name = "EcMember.findById", query = "SELECT e FROM EcMember e WHERE e.id = :id")
    , @NamedQuery(name = "EcMember.findByLoginAccount", query = "SELECT e FROM EcMember e WHERE e.loginAccount = :loginAccount"),
    @NamedQuery(name = "EcMember.findByDisabled", query = "SELECT e FROM EcMember e WHERE e.disabled=:disabled")
})
public class EcMember implements IOperator, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_MEMBER", sequenceName = "SEQ_MEMBER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBER")        
    private Long id;
    @Basic(optional = false)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "ACTIVE")
    private Boolean active;
    @Basic(optional = false)
    @Column(name = "DISABLED")
    private Boolean disabled;
    
    @Column(name = "APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvetime;
    @Column(name = "APPLYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applytime;
    @Column(name = "APPLY_VENDER_CODE")
    private String applyVenderCode;
    @Column(name = "APPLY_VENDER_NAME")
    private String applyVenderName;

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

    @Override
    public String getLabel(){
        return this.name + "(" + this.loginAccount + ")";
    }
    
    public EcMember() {
    }

    public EcMember(Long id) {
        this.id = id;
    }

    public EcMember(Long id, String loginAccount, String name, Boolean active) {
        this.id = id;
        this.loginAccount = loginAccount;
        this.name = name;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getApplyVenderCode() {
        return applyVenderCode;
    }

    public void setApplyVenderCode(String applyVenderCode) {
        this.applyVenderCode = applyVenderCode;
    }

    public String getApplyVenderName() {
        return applyVenderName;
    }

    public void setApplyVenderName(String applyVenderName) {
        this.applyVenderName = applyVenderName;
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
        if (!(object instanceof EcMember)) {
            return false;
        }
        EcMember other = (EcMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcMember[ id=" + id + " ]";
    }
    
}
