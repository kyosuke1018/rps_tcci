/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import com.google.gson.annotations.Expose;
import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.enums.SalesAllowancesPageEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_CUSTOMER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkCustomer.findAll", query = "SELECT s FROM SkCustomer s"),
    @NamedQuery(name = "SkCustomer.findById", query = "SELECT s FROM SkCustomer s WHERE s.id = :id"),
    @NamedQuery(name = "SkCustomer.findByCode", query = "SELECT s FROM SkCustomer s WHERE s.code = :code"),
    @NamedQuery(name = "SkCustomer.findByName", query = "SELECT s FROM SkCustomer s WHERE s.name = :name"),
    @NamedQuery(name = "SkCustomer.findBySapid", query = "SELECT s FROM SkCustomer s WHERE s.sapid = :sapid"),
    @NamedQuery(name = "SkCustomer.findBySimpleCode", query = "SELECT s FROM SkCustomer s WHERE s.simpleCode = :simpleCode"),
    @NamedQuery(name = "SkCustomer.findByCreatetimestamp", query = "SELECT s FROM SkCustomer s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkCustomer.findByModifytimestamp", query = "SELECT s FROM SkCustomer s WHERE s.modifytimestamp = :modifytimestamp"),
    @NamedQuery(name = "SkCustomer.findByPaymentTerm", query = "SELECT s FROM SkCustomer s WHERE s.paymentTerm = :paymentTerm")})
public class SkCustomer implements Serializable,DisplayIdentity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @Expose
    private Long id;
    @Size(max = 20)
    @Column(name = "CODE")
    @Expose
    private String code;
    @Column(name = "SIMPLE_CODE")
    @Expose
    private String simpleCode;
    @Column(name = "VAT")
    @Expose
    private String vat;
    @Size(max = 70)
    @Column(name = "NAME")
    @Expose
    private String name;
    @Size(max = 8)
    @Column(name = "SAPID")
    @Expose
    private String sapid;
    @Size(max = 4)
    @Column(name = "PAYMENT_TERM")
    @Expose
    private String paymentTerm;
    
    @Column(name = "ZIP_CODE")
    @Expose
    private String zipCode;
    @Column(name = "CITY")
    @Expose
    private String city;
    @Column(name = "STREET")
    @Expose
    private String street;
    @Column(name = "SHIPPING_CONDITION")
    @Expose
    private String shippingCondition;
    @Column(name = "TELEPHONE")
    @Expose
    private String telephone;
    @Column(name = "DOS_CODE")
    @Expose
    private String dosCode;
    @Column(name = "HEALTH_INSURANCE_CODE")
    @Expose
    private String healthInsuranceCode;
    @Column(name = "CONTROL_CODE")
    @Expose
    private String controlCode;
    @Column(name="KUKLA")
    private String kukla;
    /*
    @OneToMany(mappedBy = "customer")
    private Collection<SkAccountsReceivable> skAccountsReceivableCollection;
    @OneToMany(mappedBy = "customer")
    private Collection<SkCheckMaster> skCheckMasterCollection;
     */
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser modifier;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date modifytimestamp;
    @JoinColumn(name = "PARENT_GROUP", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private SkCustomer parentCustomer;
    @Column(name = "SALES_ALLOWANCES_PAGE")
    @Enumerated(EnumType.STRING)
    @Expose
    private SalesAllowancesPageEnum salesAllowancesPage;
    @Column(name = "DISCOUNT_RATE")
    private BigDecimal discountRate;
    
    public SkCustomer() {
    }

    public SkCustomer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSimpleCode() {
        return simpleCode;
    }

    public void setSimpleCode(String simpleCode) {
        this.simpleCode = simpleCode;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShippingCondition() {
        return shippingCondition;
    }

    public void setShippingCondition(String shippingCondition) {
        this.shippingCondition = shippingCondition;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDosCode() {
        return dosCode;
    }

    public void setDosCode(String dosCode) {
        this.dosCode = dosCode;
    }

    public String getHealthInsuranceCode() {
        return healthInsuranceCode;
    }

    public void setHealthInsuranceCode(String healthInsuranceCode) {
        this.healthInsuranceCode = healthInsuranceCode;
    }

    public String getControlCode() {
        return controlCode;
    }

    public void setControlCode(String controlCode) {
        this.controlCode = controlCode;
    }

    public String getKukla() {
        return kukla;
    }

    public void setKukla(String kukla) {
        this.kukla = kukla;
    }
    
    /*
    @XmlTransient
    public Collection<SkAccountsReceivable> getSkAccountsReceivableCollection() {
        return skAccountsReceivableCollection;
    }

    public void setSkAccountsReceivableCollection(Collection<SkAccountsReceivable> skAccountsReceivableCollection) {
        this.skAccountsReceivableCollection = skAccountsReceivableCollection;
    }

    @XmlTransient
    public Collection<SkCheckMaster> getSkCheckMasterCollection() {
        return skCheckMasterCollection;
    }

    public void setSkCheckMasterCollection(Collection<SkCheckMaster> skCheckMasterCollection) {
        this.skCheckMasterCollection = skCheckMasterCollection;
    }
    */
    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }
    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public SkCustomer getParentCustomer() {
        return parentCustomer;
    }

    public void setParentCustomer(SkCustomer parentCustomer) {
        this.parentCustomer = parentCustomer;
    }

    public SalesAllowancesPageEnum getSalesAllowancesPage() {
        return salesAllowancesPage;
    }

    public void setSalesAllowancesPage(SalesAllowancesPageEnum salesAllowancesPage) {
        this.salesAllowancesPage = salesAllowancesPage;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
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
        if (!(object instanceof SkCustomer)) {
            return false;
        }
        SkCustomer other = (SkCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
    @Override
    public String getDisplayIdentifier(){
        String display = this.code;
        if( !StringUtils.isEmpty(this.simpleCode)){
            display = this.simpleCode;
        }
        if( !StringUtils.isEmpty(this.name)){
            display = display + " - " + this.name;
        }
        return display;
    }

}
