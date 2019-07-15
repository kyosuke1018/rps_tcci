/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_RFQ_VENDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtRfqVender.findAll", query = "SELECT e FROM EtRfqVender e")
    , @NamedQuery(name = "EtRfqVender.findById", query = "SELECT e FROM EtRfqVender e WHERE e.id = :id")
    , @NamedQuery(name = "EtRfqVender.findByTenderId", query = "SELECT e FROM EtRfqVender e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtRfqVender.findByRfqId", query = "SELECT e FROM EtRfqVender e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtRfqVender.findByVenderId", query = "SELECT e FROM EtRfqVender e WHERE e.venderId = :venderId")
    , @NamedQuery(name = "EtRfqVender.findByMemberId", query = "SELECT e FROM EtRfqVender e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EtRfqVender.findByMandt", query = "SELECT e FROM EtRfqVender e WHERE e.mandt = :mandt")
    , @NamedQuery(name = "EtRfqVender.findByEbeln", query = "SELECT e FROM EtRfqVender e WHERE e.ebeln = :ebeln")
    , @NamedQuery(name = "EtRfqVender.findByLifnr", query = "SELECT e FROM EtRfqVender e WHERE e.lifnr = :lifnr")})
public class EtRfqVender implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_RFQ_VENDER", sequenceName = "SEQ_RFQ_VENDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RFQ_VENDER")        
    private Long id;
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Column(name = "RFQ_ID")
    private Long rfqId;
    @Column(name = "VENDER_ID")
    private Long venderId;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Size(max = 20)
    @Column(name = "LIFNR")
    private String lifnr;
    @Size(max = 8)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 80)
    @Column(name = "NAME1")
    private String name1;
    @Size(max = 80)
    @Column(name = "NAME2")
    private String name2;
    @Size(max = 40)
    @Column(name = "SORT1")
    private String sort1;
    @Size(max = 80)
    @Column(name = "STR_SUPPL1")
    private String strSuppl1;
    @Size(max = 80)
    @Column(name = "STR_SUPPL2")
    private String strSuppl2;
    @Size(max = 20)
    @Column(name = "POST_CODE1")
    private String postCode1;
    @Size(max = 80)
    @Column(name = "CITY1")
    private String city1;
    @Size(max = 6)
    @Column(name = "COUNTRY")
    private String country;
    @Size(max = 4)
    @Column(name = "LANGU")
    private String langu;
    @Size(max = 60)
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Size(max = 60)
    @Column(name = "CELL_NUMBER")
    private String cellNumber;
    @Size(max = 60)
    @Column(name = "FAX_NUMBER")
    private String faxNumber;
    @Column(name = "DISABLED")
    private Boolean disabled;
    
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

    public EtRfqVender() {
    }

    public EtRfqVender(Long id) {
        this.id = id;
    }

    public EtRfqVender(Long id, String mandt, String ebeln) {
        this.id = id;
        this.mandt = mandt;
        this.ebeln = ebeln;
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

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getSort1() {
        return sort1;
    }

    public void setSort1(String sort1) {
        this.sort1 = sort1;
    }

    public String getStrSuppl1() {
        return strSuppl1;
    }

    public void setStrSuppl1(String strSuppl1) {
        this.strSuppl1 = strSuppl1;
    }

    public String getStrSuppl2() {
        return strSuppl2;
    }

    public void setStrSuppl2(String strSuppl2) {
        this.strSuppl2 = strSuppl2;
    }

    public String getPostCode1() {
        return postCode1;
    }

    public void setPostCode1(String postCode1) {
        this.postCode1 = postCode1;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLangu() {
        return langu;
    }

    public void setLangu(String langu) {
        this.langu = langu;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtRfqVender)) {
            return false;
        }
        EtRfqVender other = (EtRfqVender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtRfqVender[ id=" + id + " ]";
    }
    
}