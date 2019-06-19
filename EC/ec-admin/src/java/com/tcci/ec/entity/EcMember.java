/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.model.interfaces.IOperator;
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
    , @NamedQuery(name = "EcMember.findByLoginAccount", query = "SELECT e FROM EcMember e WHERE e.loginAccount = :loginAccount")
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
    
    @Column(name = "SELLER_APPLY")
    private Boolean sellerApply;
    @Column(name = "SELLER_APPROVE")
    private Boolean sellerApprove;
    @Column(name = "APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvetime;
    @Column(name = "APPLYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applytime;

    @Column(name = "COM_APPLY")
    private Boolean comApply;
    @Column(name = "COM_APPROVE")
    private Boolean comApprove;
    @Column(name = "COM_APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comApprovetime;
    @Column(name = "COM_APPLYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comApplytime;
    @Column(name = "COM_APPROVER")
    private Long comApprover;
    
    @Column(name = "ADMIN_USER")
    private Boolean adminUser;
    
    @Column(name = "TCC_DEALER")
    private Boolean tccDealer;
    @Column(name = "TCC_DS")
    private Boolean tccDs;
    
    @Column(name = "RESET_PWD")
    private String resetPwd;
    @Column(name = "RESET_PWD_EXPIRED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPwdExpired;
    
    @Column(name = "VERIFY_CODE")
    private String verifyCode;
    @Column(name = "VERIFY_CODE_EXPIRED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifyCodeExpired;

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

    @Override
    public String getLabel(){
        return this.name + "(" + this.loginAccount + ")";
    }
    
    public EcMember() {
    }

    public EcMember(Long id) {
        this.id = id;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getVerifyCodeExpired() {
        return verifyCodeExpired;
    }

    public void setVerifyCodeExpired(Date verifyCodeExpired) {
        this.verifyCodeExpired = verifyCodeExpired;
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

    public Long getComApprover() {
        return comApprover;
    }

    public void setComApprover(Long comApprover) {
        this.comApprover = comApprover;
    }

    public Boolean getComApply() {
        return comApply;
    }

    public void setComApply(Boolean comApply) {
        this.comApply = comApply;
    }

    public Boolean getComApprove() {
        return comApprove;
    }

    public void setComApprove(Boolean comApprove) {
        this.comApprove = comApprove;
    }

    public Date getComApprovetime() {
        return comApprovetime;
    }

    public void setComApprovetime(Date comApprovetime) {
        this.comApprovetime = comApprovetime;
    }

    public Date getComApplytime() {
        return comApplytime;
    }

    public void setComApplytime(Date comApplytime) {
        this.comApplytime = comApplytime;
    }

    public Boolean getTccDealer() {
        return tccDealer;
    }

    public void setTccDealer(Boolean tccDealer) {
        this.tccDealer = tccDealer;
    }

    public Boolean getTccDs() {
        return tccDs;
    }

    public void setTccDs(Boolean tccDs) {
        this.tccDs = tccDs;
    }

    public String getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(String resetPwd) {
        this.resetPwd = resetPwd;
    }

    public Date getResetPwdExpired() {
        return resetPwdExpired;
    }

    public void setResetPwdExpired(Date resetPwdExpired) {
        this.resetPwdExpired = resetPwdExpired;
    }

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public Boolean getSellerApply() {
        return sellerApply;
    }

    public void setSellerApply(Boolean sellerApply) {
        this.sellerApply = sellerApply;
    }

    public Boolean getSellerApprove() {
        return sellerApprove;
    }

    public void setSellerApprove(Boolean sellerApprove) {
        this.sellerApprove = sellerApprove;
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

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
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
