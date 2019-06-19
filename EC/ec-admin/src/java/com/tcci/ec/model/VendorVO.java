/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.InputCheckMeta;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class VendorVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long vendorId;
    private Long storeId;
    private String code;
    private Boolean disabled = false;
    
    protected Long id;

    // for EC_PERSON、EC_COMPANY
    @InputCheckMeta(key="EC_COMPANY.TYPE")
    protected String type;
    protected Long mainId;
    @InputCheckMeta(key="EC_COMPANY.CNAME")
    protected String cname;
    @InputCheckMeta(key="EC_COMPANY.ENAME")
    protected String ename;
    @InputCheckMeta(key="EC_COMPANY.ID_CODE")
    protected String idCode;
    @InputCheckMeta(key="EC_COMPANY.NICKNAME")
    protected String nickname;
    @InputCheckMeta(key="EC_COMPANY.EMAIL1")
    protected String email1;
    @InputCheckMeta(key="EC_COMPANY.EMAIL2")
    protected String email2;
    @InputCheckMeta(key="EC_COMPANY.TEL1")
    protected String tel1;
    @InputCheckMeta(key="EC_COMPANY.TEL2")
    protected String tel2;
    @InputCheckMeta(key="EC_COMPANY.TEL3")
    protected String tel3;
    @InputCheckMeta(key="EC_COMPANY.FAX1")
    protected String fax1;
    
    protected Long country;
    protected Long state;
    @InputCheckMeta(key="EC_COMPANY.ADDR1")
    protected String addr1;
    @InputCheckMeta(key="EC_COMPANY.ADDR2")
    protected String addr2;
    @InputCheckMeta(key="EC_COMPANY.ID_TYPE")
    protected String idType;
    @InputCheckMeta(key="EC_COMPANY.BRIEF")
    protected String brief;

    protected Long creatorId;
    protected Date createtime;
    protected Long modifierId;
    protected Date modifytime;
    
    @InputCheckMeta(key="EC_COMPANY.EMAIL3")
    protected String email3;
    @InputCheckMeta(key="EC_COMPANY.FAX2")
    protected String fax2;
    @InputCheckMeta(key="EC_COMPANY.OWNER1")
    protected String owner1;
    @InputCheckMeta(key="EC_COMPANY.OWNER2")
    protected String owner2;
    @InputCheckMeta(key="EC_COMPANY.CONTACT1")
    protected String contact1;
    @InputCheckMeta(key="EC_COMPANY.CONTACT2")
    protected String contact2;
    @InputCheckMeta(key="EC_COMPANY.CONTACT3")
    protected String contact3;
    @InputCheckMeta(key="EC_COMPANY.WEB_ID1")
    protected String webId1;
    @InputCheckMeta(key="EC_COMPANY.WEB_ID2")
    protected String webId2;
    @InputCheckMeta(key="EC_COMPANY.LONGITUDE")
    protected String longitude;
    @InputCheckMeta(key="EC_COMPANY.LATITUDE")
    protected String latitude;
    @InputCheckMeta(key="EC_COMPANY.URL1")
    protected String url1;
    @InputCheckMeta(key="EC_COMPANY.URL2")
    protected String url2;

    public VendorVO() {
    }

    public VendorVO(Long id) {
        this.id = id;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
        if (!(object instanceof VendorVO)) {
            return false;
        }
        VendorVO other = (VendorVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.VendorVO[ id=" + id + " ]";
    }
    
}
