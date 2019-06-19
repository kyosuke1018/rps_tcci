/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.InputCheckMeta;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class StoreVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long storeId;
    private String storeType;
    private Long id; // EC_COMPANY.ID
    private Long styleId;
    private Long flowId;
    private Long prdTypeLevel;
    private Long sellerId;
    private Boolean opened;

    @InputCheckMeta(key="EC_STORE.REMIT_ACCOUNT")
    private String remitAccount;
    // for EC_MEMBER
    private Long memberId;
    private String loginAccount;
    private String name;
    private String email;
    private String phone;
    private Boolean active;
    private Boolean tccDealer;
    private Boolean tccDs;
    
    // for EC_PERSON、EC_COMPANY
    @InputCheckMeta(key="EC_COMPANY.TYPE")
    private String type;
    private Long mainId;
    @InputCheckMeta(key="EC_COMPANY.CNAME")
    private String cname;
    @InputCheckMeta(key="EC_COMPANY.ENAME")
    private String ename;
    @InputCheckMeta(key="EC_COMPANY.ID_CODE")
    private String idCode;
    @InputCheckMeta(key="EC_COMPANY.NICKNAME")
    private String nickname;
    @InputCheckMeta(key="EC_COMPANY.EMAIL1")
    private String email1;
    @InputCheckMeta(key="EC_COMPANY.EMAIL2")
    private String email2;
    @InputCheckMeta(key="EC_COMPANY.TEL1")
    private String tel1;
    @InputCheckMeta(key="EC_COMPANY.TEL2")
    private String tel2;
    @InputCheckMeta(key="EC_COMPANY.TEL3")
    private String tel3;
    @InputCheckMeta(key="EC_COMPANY.FAX1")
    private String fax1;
    
    private Long country;
    private Long state;
    @InputCheckMeta(key="EC_COMPANY.ADDR1")
    private String addr1;
    @InputCheckMeta(key="EC_COMPANY.ADDR2")
    private String addr2;
    @InputCheckMeta(key="EC_COMPANY.ID_TYPE")
    private String idType;
    @InputCheckMeta(key="EC_COMPANY.BRIEF")
    private String brief;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;
    
    @InputCheckMeta(key="EC_COMPANY.EMAIL3")
    private String email3;
    @InputCheckMeta(key="EC_COMPANY.FAX2")
    private String fax2;
    @InputCheckMeta(key="EC_COMPANY.OWNER1")
    private String owner1;
    @InputCheckMeta(key="EC_COMPANY.OWNER2")
    private String owner2;
    @InputCheckMeta(key="EC_COMPANY.CONTACT1")
    private String contact1;
    @InputCheckMeta(key="EC_COMPANY.CONTACT2")
    private String contact2;
    @InputCheckMeta(key="EC_COMPANY.CONTACT3")
    private String contact3;
    @InputCheckMeta(key="EC_COMPANY.WEB_ID1")
    private String webId1;
    @InputCheckMeta(key="EC_COMPANY.WEB_ID2")
    private String webId2;
    @InputCheckMeta(key="EC_COMPANY.LONGITUDE")
    private String longitude;
    @InputCheckMeta(key="EC_COMPANY.LATITUDE")
    private String latitude;
    @InputCheckMeta(key="EC_COMPANY.URL1")
    private String url1;
    @InputCheckMeta(key="EC_COMPANY.URL2")
    private String url2;
    
    private Boolean disabled;
    private Boolean defStore;
    
    private List<ShippingVO> shippings;
    private List<PaymentVO> payments;
    private List<StoreAreaVO> areas;
    
    private List<OptionVO> prdBrand;
    private List<OptionVO> prdUnit;
    private List<OptionVO> weightUnit;
    private List<OptionVO> cusLevel;
    private List<OptionVO> cusFeedback;

    private FileVO logo;
    private FileVO banner;
    
    private int prate;// 正評
    private int nrate;// 負評
    private int favCount;// 追蹤商店
    private int favPrdCount;// 追蹤商品
    private int managerCount;// 管理員人數
    
    public StoreVO() {
    }

    public StoreVO(Long id) {
        this.id = id;
    }

    public int getManagerCount() {
        return managerCount;
    }

    public void setManagerCount(int managerCount) {
        this.managerCount = managerCount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getDefStore() {
        return defStore;
    }

    public void setDefStore(Boolean defStore) {
        this.defStore = defStore;
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

    public List<StoreAreaVO> getAreas() {
        return areas;
    }

    public void setAreas(List<StoreAreaVO> areas) {
        this.areas = areas;
    }

    public List<OptionVO> getPrdBrand() {
        return prdBrand;
    }

    public void setPrdBrand(List<OptionVO> prdBrand) {
        this.prdBrand = prdBrand;
    }

    public List<OptionVO> getPrdUnit() {
        return prdUnit;
    }

    public void setPrdUnit(List<OptionVO> prdUnit) {
        this.prdUnit = prdUnit;
    }

    public List<OptionVO> getCusFeedback() {
        return cusFeedback;
    }

    public void setCusFeedback(List<OptionVO> cusFeedback) {
        this.cusFeedback = cusFeedback;
    }

    public List<OptionVO> getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(List<OptionVO> weightUnit) {
        this.weightUnit = weightUnit;
    }

    public List<OptionVO> getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(List<OptionVO> cusLevel) {
        this.cusLevel = cusLevel;
    }

    public List<ShippingVO> getShippings() {
        return shippings;
    }

    public void setShippings(List<ShippingVO> shippings) {
        this.shippings = shippings;
    }

    public List<PaymentVO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentVO> payments) {
        this.payments = payments;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getFavPrdCount() {
        return favPrdCount;
    }

    public void setFavPrdCount(int favPrdCount) {
        this.favPrdCount = favPrdCount;
    }

    public int getPrate() {
        return prate;
    }

    public void setPrate(int prate) {
        this.prate = prate;
    }

    public int getNrate() {
        return nrate;
    }

    public void setNrate(int nrate) {
        this.nrate = nrate;
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

    public String getRemitAccount() {
        return remitAccount;
    }

    public void setRemitAccount(String remitAccount) {
        this.remitAccount = remitAccount;
    }

    public FileVO getLogo() {
        return logo;
    }

    public void setLogo(FileVO logo) {
        this.logo = logo;
    }

    public FileVO getBanner() {
        return banner;
    }

    public void setBanner(FileVO banner) {
        this.banner = banner;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
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

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
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

    public Long getPrdTypeLevel() {
        return prdTypeLevel;
    }

    public void setPrdTypeLevel(Long prdTypeLevel) {
        this.prdTypeLevel = prdTypeLevel;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
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
        if (!(object instanceof StoreVO)) {
            return false;
        }
        StoreVO other = (StoreVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.StoreVO[ id=" + id + " ]";
    }
    
}
