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
@Table(name = "EC_CUS_ADDR")
@NamedQueries({
    @NamedQuery(name = "EcCusAddr.findAll", query = "SELECT e FROM EcCusAddr e"),
    @NamedQuery(name = "EcCusAddr.findByMember", query = "SELECT e FROM EcCusAddr e WHERE e.member=:member")
})
public class EcCusAddr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CUS_ADDR", sequenceName = "SEQ_CUS_ADDR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUS_ADDR")
    private Long id;
//    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCustomer customer;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcMember member;
    @Size(max = 40)
    @Column(name = "RECIPIENT")
    private String recipient;
    @Size(max = 80)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 80)
    @Column(name = "ADDRESS_2")
    private String address2;
    @Column(name = "TYPE")
    private String type;
    @Size(max = 10)
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE_REGION")
    private String stateRegion;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "PHONE_EXTENSION")
    private String phoneExtension;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRIMARY")
    private boolean primary;
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
    
    @Column(name = "CAR_NO")
    private String carNo;
    @Column(name = "PATROL_LATITUDE")
    private float patrolLatitude;//緯度
    @Column(name = "PATROL_LONGITUDE")
    private float patrolLongitude;//經度
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;//送達地點 EC_DELIVERY_PLACE.ID

    public EcCusAddr() {
    }

    public EcCusAddr(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcMember getMember() {
        return member;
    }

    public void setMember(EcMember member) {
        this.member = member;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateRegion() {
        return stateRegion;
    }

    public void setStateRegion(String stateRegion) {
        this.stateRegion = stateRegion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public float getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(float patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public float getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(float patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
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
        if (!(object instanceof EcCusAddr)) {
            return false;
        }
        EcCusAddr other = (EcCusAddr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCusAddr[ id=" + id + " ]";
    }

}
