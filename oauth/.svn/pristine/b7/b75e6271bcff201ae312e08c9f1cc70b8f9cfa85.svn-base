/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.enums.PartnerStatusEnum;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_PARTNER")
@NamedQueries({
    @NamedQuery(name = "EcPartner.findAll", query = "SELECT e FROM EcPartner e"),
    @NamedQuery(name = "EcPartner.findByName", query = "SELECT e FROM EcPartner e WHERE e.name = :name")})
public class EcPartner implements Serializable, Persistable, ContentHolder {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_EC", sequenceName = "SEQ_EC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 150)
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "CITY")
    private String city;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "TOWN")
    private String town;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "CONTACT")
    private String contact;
    @Size(max = 50)
    @Column(name = "PHONE")
    private String phone;
    @Size(max = 64)
    @Column(name = "SOCIAL")
    private String social;
    @Size(max = 20)
    @Column(name = "THEME")
    private String theme;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private PartnerStatusEnum status;
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "ACTIVE")
    private boolean active;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecPartner")
//    private List<EcPartnerComment> ecAllPartnerCommentList;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember owner;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecPartner")
//    private List<EcMemberPartner> memberPartnerList;
    @Column(name = "MEMBER")
    private String member;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "RATE")
    private Double averageRate;
    @JoinColumn(name = "APPROVER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser approver;
    @Column(name = "APPROVAL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;

    public EcPartner() {
    }

    public EcPartner(Long id) {
        this.id = id;
    }

    public EcPartner(Long id, String name, PartnerStatusEnum status, Date createtime, boolean active) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createtime = createtime;
        this.active = active;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public PartnerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PartnerStatusEnum status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public List<EcPartnerComment> getEcAllPartnerCommentList() {
//        return ecAllPartnerCommentList;
//    }
//
//    public void setEcAllPartnerCommentList(List<EcPartnerComment> ecAllPartnerCommentList) {
//        this.ecAllPartnerCommentList = ecAllPartnerCommentList;
//    }
//
//    public List<EcPartnerComment> getEcPartnerCommentList() {
//        List<EcPartnerComment> activeCommentList = new ArrayList();
//        for (EcPartnerComment partnerComment : this.ecAllPartnerCommentList) {
//            if (partnerComment.isActive()) {
//                activeCommentList.add(partnerComment);
//            }
//        }
//        return activeCommentList;
//    }
//
//    public List<EcMemberPartner> getMemberPartnerList() {
//        return memberPartnerList;
//    }
//
//    public void setMemberPartnerList(List<EcMemberPartner> memberPartnerList) {
//        this.memberPartnerList = memberPartnerList;
//    }

    public EcMember getOwner() {
        return owner;
    }

    public void setOwner(EcMember owner) {
        this.owner = owner;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public TcUser getApprover() {
        return approver;
    }

    public void setApprover(TcUser approver) {
        this.approver = approver;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
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
        if (!(object instanceof EcPartner)) {
            return false;
        }
        EcPartner other = (EcPartner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public String getDisplayIdentifier() {
        return this.name.concat("(").concat(String.valueOf(this.id)).concat(")");
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcPartner[ id=" + id + "\n"
                + "name=" + name + "\n"
                + "description=" + description + "\n"
                + "address=" + address + "\n"
                + "phone=" + phone + "\n"
                + "social=" + social + "\n"
                + "address=" + address + "\n"
                + "theme=" + theme + "\n"
                + "status=" + status + "\n"
                + "member=" + member + "\n"
                + "active=" + active + "\n"
                + "owner=" + (owner != null ? "member[loginAccount=" + owner.getLoginAccount() + "]" : "null") + "\n"
                //+ "ecAllPartnerCommentList=" + (ecAllPartnerCommentList != null ? "List(" + ecAllPartnerCommentList.size() + " EcPartnerComment(s))" : "null") + "\n"
                //+ "ecPartnerCommentList=" + (getEcPartnerCommentList() != null ? "List(" + getEcPartnerCommentList().size() + " EcPartnerComment(s))" : "null") + "\n"
                + "]";
    }

}
