/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

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

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_MEMBER")
@NamedQueries({
    @NamedQuery(name = "EcMember.findAll", query = "SELECT e FROM EcMember e"),
    @NamedQuery(name = "EcMember.findActiveByLoginAccount", query = "SELECT e FROM EcMember e WHERE e.active=TRUE AND e.loginAccount=:loginAccount"),
    @NamedQuery(name = "EcMember.findByLoginAccount", query = "SELECT e FROM EcMember e WHERE e.loginAccount=:loginAccount"),
    @NamedQuery(name = "EcMember.findAllActive", query = "SELECT e FROM EcMember e WHERE e.active=TRUE order by e.loginAccount ASC")
})
public class EcMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_MEMBER",sequenceName = "SEQ_MEMBER", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_MEMBER")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @Size(min = 1, max = 64)
    @Column(name = "NAME")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecMember")
//    private List<EcMemberCustomer> ecMemberCustomerList;
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
    
    @Column(name = "DISABLED")
    private boolean disabled;
    @Column(name = "SELLER_APPLY")
    private boolean sellerApply;//已申請賣家身份
    @Column(name = "SELLER_APPROVE")
    private boolean sellerApprove;//已核准賣家身份
    @Column(name = "APPLYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applytime;//申請時間
    @Column(name = "APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvetime;//核准時間
    @Column(name = "TYPE")
    private String type;//帳號類型(P:個人;C:公司)MemberTypeEnum
    @Column(name = "COM_APPLY")
    private Boolean comApply;//已申請公司戶
    @Column(name = "COM_APPROVE")
    private Boolean comApprove;//已核准公司戶
    @Column(name = "COM_APPROVETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comApprovetime;//申請公司戶時間
    @Column(name = "COM_APPLYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comApplytime;//核准公司戶時間
    @Column(name = "COM_APPROVER")
    private Long comApprover;//核准人
    //v1.5
    @Column(name = "TCC_DEALER")
    private Boolean tccDealer;//台泥經銷商
    @Column(name = "TCC_DS")
    private Boolean tccDs;//台泥經銷商下游廠商(檔口、攪拌站)
    
    @Column(name = "RESET_PWD")
    private String resetPwd;
    @Column(name = "RESET_PWD_EXPIRED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPwdExpired;

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

    public EcMember(String email, String name, String phone) {
        this.loginAccount = email;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.modifytime = new Date();
        this.active = true;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSellerApply() {
        return sellerApply;
    }

    public void setSellerApply(boolean sellerApply) {
        this.sellerApply = sellerApply;
    }

    public boolean isSellerApprove() {
        return sellerApprove;
    }

    public void setSellerApprove(boolean sellerApprove) {
        this.sellerApprove = sellerApprove;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getComApprover() {
        return comApprover;
    }

    public void setComApprover(Long comApprover) {
        this.comApprover = comApprover;
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
