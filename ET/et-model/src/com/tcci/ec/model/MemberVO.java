/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.model.interfaces.IOperator;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public class MemberVO extends BaseResponseVO implements IOperator, Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long memberId;
    private String memberType;
    private String memberTypeOri;
    private String memberTypeName;
    // for EC_MEMBER
    @InputCheckMeta(key="EC_MEMBER.LOGIN_ACCOUNT")
    private String loginAccount;
//    private String password;
    private String pwd;//for new 匯入會員 不能用password當變數
    @InputCheckMeta(key="EC_MEMBER.NAME")
    private String name;
    @InputCheckMeta(key="EC_MEMBER.EMAIL")
    private String email;
//    @InputCheckMeta(key="EC_MEMBER.PHONE")
    private String phone;
    private Boolean active;
    private Boolean disabled;
    private Boolean sellerApply;
    private Boolean sellerApprove;
    private Date approvetime;
    private Date applytime;
    private Boolean adminUser;
    private Boolean tccDealer;
    private Boolean tccDs;

    private String verifyCode;
    private Date verifyCodeExpired;
    private String status;

    private Boolean comApply;
    private Boolean comApprove;
    private Date comApprovetime;
    private Date comApplytime;
    private Long comApprover;
    //註冊時填寫 供後台審核參考
    private String mandt;
    private String applyVenderCode;
    private String applyVenderName;
    
    //for ET_VENDER
    private List<VenderVO> venderList;
    private List<LongOptionVO> venders;
    private List<VenderVO> addVenderList;
    private String venderCodes;
    
    
    //for 待核 申請
    private Long factoryId;
    private String factoryName;
    private String venderCodesApply;
    private Long processId;
    private String executionstate;
    private Date starttime;
    private Date endtime;
    private List<ApplyFormVO> applyFormList;

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
    private String tel4;
    private String fax1;
    private Long country;
    private Long state;//所在區域
    private String addr1;
    private String addr2;
    private String idType;
    private String brief;
    private String brief2;
    private String brief3;
    private String url1;
    private String url2;

    // EC_PERSON
    private String gender;
    private Date birthday;
    private Integer age;

    // EC_COMPANY
    private String venderType;
    private String countryCode;
    private String countryName;
    private Long currency;
    private String currencyCode;
    private String currencyName;
    
    private String email3;
    private String fax2;
    private String owner1;// 負責人
    private String owner2;
    private String owner1Tile;
    private String owner2Tile;
    private String contact1;// 聯絡電話
    private String contact2;
    private String contact3;
    private String webId1;
    private String webId2;
    private String longitude;
    private String latitude;

    private Date startAt;//創立時間
    private Long category;
    private String industry;//行業別
    private BigDecimal capital;//資本額
    private BigDecimal yearIncome;//年收入
    private BigDecimal yearRevenue;//年營業額
    private BigDecimal employee;//員工人數
    private String bank1;
    private String bank1Beneficiary;
    private String bank1Code;
    private String bank1Account;
    private String bank1Addr;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    private FileVO picture;
    
    private Long sellerId;
    private Long storeId;
    private String storeCname;
    private String storeEname;
    
    private BigDecimal totalAmt;
    private Date lastBuyDate;
    
    private List<LongOptionVO> stores;
    
    private FileVO pic;
    private Long picId;

    private Long managerId;// for EC_STORE_USER.ID
    private boolean manager;// 目前是否為商店管理員身分(對應 storeId)
    private Boolean storeOwner;
    private Boolean fiUser;
    
    @Override
    public String getLabel(){
        return this.name + "(" + this.loginAccount + ")";
    }
    
    public void genTypeName(Locale locale){
        MemberTypeEnum enum1 = MemberTypeEnum.getFromCode(this.memberType);
        memberTypeName = enum1!=null?enum1.getDisplayName(locale):"";
    }
    
    public MemberVO() {
    }

    public MemberVO(Long id) {
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

    public String getStoreCname() {
        return storeCname;
    }

    public void setStoreCname(String storeCname) {
        this.storeCname = storeCname;
    }

    public String getStoreEname() {
        return storeEname;
    }

    public void setStoreEname(String storeEname) {
        this.storeEname = storeEname;
    }

    public Boolean getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(Boolean storeOwner) {
        this.storeOwner = storeOwner;
    }

    public Boolean getFiUser() {
        return fiUser;
    }

    public void setFiUser(Boolean fiUser) {
        this.fiUser = fiUser;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public MemberVO(Long id, String loginAccount, String name, Boolean active) {
        this.id = id;
        this.loginAccount = loginAccount;
        this.name = name;
        this.active = active;
    }

    public FileVO getPic() {
        return pic;
    }

    public void setPic(FileVO pic) {
        this.pic = pic;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
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

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
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

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(BigDecimal yearIncome) {
        this.yearIncome = yearIncome;
    }

    public List<LongOptionVO> getStores() {
        return stores;
    }

    public void setStores(List<LongOptionVO> stores) {
        this.stores = stores;
    }

    public String getMemberTypeOri() {
        return memberTypeOri;
    }

    public void setMemberTypeOri(String memberTypeOri) {
        this.memberTypeOri = memberTypeOri;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getMemberTypeName() {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

    public Date getLastBuyDate() {
        return lastBuyDate;
    }

    public void setLastBuyDate(Date lastBuyDate) {
        this.lastBuyDate = lastBuyDate;
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

    public List<LongOptionVO> getVenders() {
        return venders;
    }

    public void setVenders(List<LongOptionVO> venders) {
        this.venders = venders;
    }

    public List<VenderVO> getAddVenderList() {
        return addVenderList;
    }

    public void setAddVenderList(List<VenderVO> addVenderList) {
        this.addVenderList = addVenderList;
    }

    public String getVenderCodes() {
        return venderCodes;
    }

    public void setVenderCodes(String venderCodes) {
        this.venderCodes = venderCodes;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(String executionstate) {
        this.executionstate = executionstate;
    }

    public List<ApplyFormVO> getApplyFormList() {
        return applyFormList;
    }

    public void setApplyFormList(List<ApplyFormVO> applyFormList) {
        this.applyFormList = applyFormList;
    }

    public List<VenderVO> getVenderList() {
        return venderList;
    }

    public void setVenderList(List<VenderVO> venderList) {
        this.venderList = venderList;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getTel4() {
        return tel4;
    }

    public void setTel4(String tel4) {
        this.tel4 = tel4;
    }

    public String getBrief2() {
        return brief2;
    }

    public void setBrief2(String brief2) {
        this.brief2 = brief2;
    }

    public String getBrief3() {
        return brief3;
    }

    public void setBrief3(String brief3) {
        this.brief3 = brief3;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getOwner1Tile() {
        return owner1Tile;
    }

    public void setOwner1Tile(String owner1Tile) {
        this.owner1Tile = owner1Tile;
    }

    public String getOwner2Tile() {
        return owner2Tile;
    }

    public void setOwner2Tile(String owner2Tile) {
        this.owner2Tile = owner2Tile;
    }

    public BigDecimal getYearRevenue() {
        return yearRevenue;
    }

    public void setYearRevenue(BigDecimal yearRevenue) {
        this.yearRevenue = yearRevenue;
    }

    public BigDecimal getEmployee() {
        return employee;
    }

    public void setEmployee(BigDecimal employee) {
        this.employee = employee;
    }

    public String getBank1() {
        return bank1;
    }

    public void setBank1(String bank1) {
        this.bank1 = bank1;
    }

    public String getBank1Beneficiary() {
        return bank1Beneficiary;
    }

    public void setBank1Beneficiary(String bank1Beneficiary) {
        this.bank1Beneficiary = bank1Beneficiary;
    }

    public String getBank1Code() {
        return bank1Code;
    }

    public void setBank1Code(String bank1Code) {
        this.bank1Code = bank1Code;
    }

    public String getBank1Account() {
        return bank1Account;
    }

    public void setBank1Account(String bank1Account) {
        this.bank1Account = bank1Account;
    }

    public String getBank1Addr() {
        return bank1Addr;
    }

    public void setBank1Addr(String bank1Addr) {
        this.bank1Addr = bank1Addr;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getVenderType() {
        return venderType;
    }

    public void setVenderType(String venderType) {
        this.venderType = venderType;
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
        return "com.tcci.ec.model.MemberVO[ id=" + memberId + " ]";
    }
    
}
