/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EC_COMPANY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCompany.findAll", query = "SELECT e FROM EcCompany e")
    , @NamedQuery(name = "EcCompany.findById", query = "SELECT e FROM EcCompany e WHERE e.id = :id")
    , @NamedQuery(name = "EcCompany.findByType", query = "SELECT e FROM EcCompany e WHERE e.type = :type")})
public class EcCompany implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_COMPANY", sequenceName = "SEQ_COMPANY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMPANY")        
    private Long id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "MAIN_ID")
    private Long mainId;//EC_MEMBER.ID
    @Column(name = "CODE")
    private String code;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "ID_TYPE")
    private String idType;
    @Column(name = "ID_CODE")
    private String idCode;
    @Column(name = "NICKNAME")
    private String nickname;
    @Column(name = "BRIEF")
    private String brief;
    @Column(name = "BRIEF2")
    private String brief2;
    @Column(name = "BRIEF3")
    private String brief3;
//    @Column(name = "EMAIL1")
//    private String email1;
//    @Column(name = "EMAIL2")
//    private String email2;
//    @Column(name = "EMAIL3")
//    private String email3;
    @Column(name = "TEL1")
    private String tel1;
    @Column(name = "TEL2")
    private String tel2;
    @Column(name = "TEL3")
    private String tel3;
    @Column(name = "TEL4")
    private String tel4;
    @Column(name = "OWNER1")
    private String owner1;
    @Column(name = "OWNER2")
    private String owner2;
    @Column(name = "OWNER1_TITLE")
    private String owner1Tile;
    @Column(name = "OWNER2_TITLE")
    private String owner2Tile;
//    @Column(name = "FAX1")
//    private String fax1;
//    @Column(name = "FAX2")
//    private String fax2;
    @Column(name = "COUNTRY")
    private Long country;
    @Column(name = "COUNTRY_CODE")
    private String countryCode;
    @Column(name = "STATE")
    private Long state;
    @Column(name = "ADDR1")
    private String addr1;
    @Column(name = "ADDR2")
    private String addr2;
//    @Column(name = "CONTACT1")
//    private String contact1;
//    @Column(name = "CONTACT2")
//    private String contact2;
//    @Column(name = "CONTACT3")
//    private String contact3;
//    @Column(name = "WEB_ID1")
//    private String webId1;
//    @Column(name = "WEB_ID2")
//    private String webId2;
//    @Column(name = "LONGITUDE")
//    private String longitude;
//    @Column(name = "LATITUDE")
//    private String latitude;
    @Column(name = "URL1")
    private String url1;
    @Column(name = "URL2")
    private String url2;
    @Column(name = "START_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;//創立時間
//    @Column(name = "CATEGORY")
//    private Long category;
    @Column(name = "INDUSTRY")
    private String industry;//行業別
    @Column(name = "CAPITAL")
    private BigDecimal capital;//資本額
    @Column(name = "YEAR_REVENUE")
    private BigDecimal yearRevenue;//年營業額
    @Column(name = "EMPLOYEE")
    private BigDecimal employee;//員工人數
    @Column(name = "CURRENCY")
    private Long currency;//訂單幣別
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;//訂單幣別
    
    @Column(name = "BANK1")
    private String bank1;
    @Column(name = "BANK2")
    private String bank2;
    @Column(name = "BANK1_BENEFICIARY")
    private String bank1Beneficiary;
    @Column(name = "BANK2_BENEFICIARY")
    private String bank2Beneficiary;
    @Column(name = "BANK1_CODE")
    private String bank1Code;
    @Column(name = "BANK2_CODE")
    private String bank2Code;
    @Column(name = "BANK1_ACCOUNT")
    private String bank1Account;
    @Column(name = "BANK2_ACCOUNT")
    private String bank2Account;
    @Column(name = "BANK1_ADDR")
    private String bank1Addr;
    @Column(name = "BANK2_ADDR")
    private String bank2Addr;
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

    public EcCompany() {
    }

    public EcCompany(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

//    public BigDecimal getYearIncome() {
//        return yearIncome;
//    }
//
//    public void setYearIncome(BigDecimal yearIncome) {
//        this.yearIncome = yearIncome;
//    }

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

//    public String getWebId1() {
//        return webId1;
//    }
//
//    public void setWebId1(String webId1) {
//        this.webId1 = webId1;
//    }
//
//    public String getWebId2() {
//        return webId2;
//    }
//
//    public void setWebId2(String webId2) {
//        this.webId2 = webId2;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

//    public String getEmail1() {
//        return email1;
//    }
//
//    public void setEmail1(String email1) {
//        this.email1 = email1;
//    }
//
//    public String getEmail2() {
//        return email2;
//    }
//
//    public void setEmail2(String email2) {
//        this.email2 = email2;
//    }
//
//    public String getEmail3() {
//        return email3;
//    }
//
//    public void setEmail3(String email3) {
//        this.email3 = email3;
//    }

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

    public String getTel4() {
        return tel4;
    }

    public void setTel4(String tel4) {
        this.tel4 = tel4;
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

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public String getBank1() {
        return bank1;
    }

    public void setBank1(String bank1) {
        this.bank1 = bank1;
    }

    public String getBank2() {
        return bank2;
    }

    public void setBank2(String bank2) {
        this.bank2 = bank2;
    }

    public String getBank1Beneficiary() {
        return bank1Beneficiary;
    }

    public void setBank1Beneficiary(String bank1Beneficiary) {
        this.bank1Beneficiary = bank1Beneficiary;
    }

    public String getBank2Beneficiary() {
        return bank2Beneficiary;
    }

    public void setBank2Beneficiary(String bank2Beneficiary) {
        this.bank2Beneficiary = bank2Beneficiary;
    }

    public String getBank1Code() {
        return bank1Code;
    }

    public void setBank1Code(String bank1Code) {
        this.bank1Code = bank1Code;
    }

    public String getBank2Code() {
        return bank2Code;
    }

    public void setBank2Code(String bank2Code) {
        this.bank2Code = bank2Code;
    }

    public String getBank1Account() {
        return bank1Account;
    }

    public void setBank1Account(String bank1Account) {
        this.bank1Account = bank1Account;
    }

    public String getBank2Account() {
        return bank2Account;
    }

    public void setBank2Account(String bank2Account) {
        this.bank2Account = bank2Account;
    }

    public String getBank1Addr() {
        return bank1Addr;
    }

    public void setBank1Addr(String bank1Addr) {
        this.bank1Addr = bank1Addr;
    }

    public String getBank2Addr() {
        return bank2Addr;
    }

    public void setBank2Addr(String bank2Addr) {
        this.bank2Addr = bank2Addr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCompany)) {
            return false;
        }
        EcCompany other = (EcCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCompany[ id=" + id + " ]";
    }
    
}
