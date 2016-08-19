/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "EC_MEMBER")
@NamedQueries({
    @NamedQuery(name = "EcMember.findAll", query = "SELECT e FROM EcMember e"),
    @NamedQuery(name = "EcMember.findActiveByLoginAccount", query = "SELECT e FROM EcMember e WHERE e.active=TRUE AND e.loginAccount=:loginAccount")
})
public class EcMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "NAME")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "EMAIL")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "PHONE")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinTable(name = "EC_MEMBER_CUSTOMER", joinColumns = {
        @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EcCustomer> ecCustomerList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
//    private List<EcFormSatisfaction> ecFormSatisfactionList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
//    private List<EcPartnerComment> ecPartnerCommentList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
//    private List<EcFormBecustomer> ecFormBecustomerList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<EcPartner> ecPartnerList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
    private List<EcFormQa> ecFormQaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberId")
    private List<EcOrder> ecOrderList;

    public EcMember() {
    }

    public EcMember(Long id) {
        this.id = id;
    }

    public EcMember(Long id, String loginAccount, String name, String email, boolean active) {
        this.id = id;
        this.loginAccount = loginAccount;
        this.name = name;
        this.email = email;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EcCustomer> getEcCustomerList() {
        return ecCustomerList;
    }

    public void setEcCustomerList(List<EcCustomer> ecCustomerList) {
        this.ecCustomerList = ecCustomerList;
    }

//    public List<EcFormSatisfaction> getEcFormSatisfactionList() {
//        return ecFormSatisfactionList;
//    }
//
//    public void setEcFormSatisfactionList(List<EcFormSatisfaction> ecFormSatisfactionList) {
//        this.ecFormSatisfactionList = ecFormSatisfactionList;
//    }
//
//    public List<EcPartnerComment> getEcPartnerCommentList() {
//        return ecPartnerCommentList;
//    }
//
//    public void setEcPartnerCommentList(List<EcPartnerComment> ecPartnerCommentList) {
//        this.ecPartnerCommentList = ecPartnerCommentList;
//    }
//
//    public List<EcFormBecustomer> getEcFormBecustomerList() {
//        return ecFormBecustomerList;
//    }
//
//    public void setEcFormBecustomerList(List<EcFormBecustomer> ecFormBecustomerList) {
//        this.ecFormBecustomerList = ecFormBecustomerList;
//    }
//
    public List<EcPartner> getEcPartnerList() {
        return ecPartnerList;
    }

    public void setEcPartnerList(List<EcPartner> ecPartnerList) {
        this.ecPartnerList = ecPartnerList;
    }

    public List<EcFormQa> getEcFormQaList() {
        return ecFormQaList;
    }

    public void setEcFormQaList(List<EcFormQa> ecFormQaList) {
        this.ecFormQaList = ecFormQaList;
    }

    public List<EcOrder> getEcOrderList() {
        return ecOrderList;
    }

    public void setEcOrderList(List<EcOrder> ecOrderList) {
        this.ecOrderList = ecOrderList;
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
        return "com.tcci.ecdemo.entity.EcMember[ id=" + id + " ]";
    }
    
}
