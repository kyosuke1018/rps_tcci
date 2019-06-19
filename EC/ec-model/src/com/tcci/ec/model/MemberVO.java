/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class MemberVO extends BaseResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long memberId;
    private String memberType;
    // for EC_MEMBER
    @InputCheckMeta(key="EC_MEMBER.LOGIN_ACCOUNT")
    private String loginAccount;
    @InputCheckMeta(key="EC_MEMBER.NAME")
    private String name;
    @InputCheckMeta(key="EC_MEMBER.LOGIN_ACCOUNT")
    private String email;
    @InputCheckMeta(key="EC_MEMBER.PHONE")
    private String phone;
    private Boolean active;
    private Boolean disabled;
    private Boolean sellerApply;
    private Boolean sellerApprove;
    private Date approvetime;
    private Date applytime;
    private Boolean adminUser;

    // for EC_PERSON、EC_COMPANY
    private String type;
    private Long mainId;
    private String cname;
    private String ename;
    private String idCode;
    private String nickname;
    private String email1;
    private String email2;
    private String tel1;
    private String tel2;
    private String tel3;
    private String fax1;
    private Long country;
    private Long state;
    private String addr1;
    private String addr2;
    private String idType;
    private String brief;

    // EC_PERSON
    private String gender;
    private Date birthday;
    private Integer age;

    // EC_COMPANY
    private String email3;
    private String fax2;
    private String owner1;
    private String owner2;
    private String contact1;
    private String contact2;
    private String contact3;
    private String webId1;
    private String webId2;
    private String longitude;
    private String latitude;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private FileVO picture;
    
    private Long sellerId;
    private Long storeId;
    
    public MemberVO() {
    }

    public MemberVO(Long id) {
        this.id = id;
    }

    public MemberVO(Long id, String loginAccount, String name, Boolean active) {
        this.id = id;
        this.loginAccount = loginAccount;
        this.name = name;
        this.active = active;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileVO getPicture() {
        return picture;
    }

    public void setPicture(FileVO picture) {
        this.picture = picture;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getFax2() {
        return fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    public String getOwner1() {
        return owner1;
    }

    public void setOwner1(String owner1) {
        this.owner1 = owner1;
    }

    public String getOwner2() {
        return owner2;
    }

    public void setOwner2(String owner2) {
        this.owner2 = owner2;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    public String getWebId1() {
        return webId1;
    }

    public void setWebId1(String webId1) {
        this.webId1 = webId1;
    }

    public String getWebId2() {
        return webId2;
    }

    public void setWebId2(String webId2) {
        this.webId2 = webId2;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
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
        if (!(object instanceof MemberVO)) {
            return false;
        }
        MemberVO other = (MemberVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.MemberVO[ id=" + id + " ]";
    }
    
}
