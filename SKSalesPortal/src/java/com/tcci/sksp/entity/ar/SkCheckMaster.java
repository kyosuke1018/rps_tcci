/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.entity.SkCustomer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_CHECK_MASTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkCheckMaster.findAll", query = "SELECT s FROM SkCheckMaster s"),
    @NamedQuery(name = "SkCheckMaster.findById", query = "SELECT s FROM SkCheckMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SkCheckMaster.findByCheckNumber", query = "SELECT s FROM SkCheckMaster s WHERE s.checkNumber = :checkNumber"),
    @NamedQuery(name = "SkCheckMaster.findByDueDate", query = "SELECT s FROM SkCheckMaster s WHERE s.dueDate = :dueDate"),
    @NamedQuery(name = "SkCheckMaster.findByBillingBank", query = "SELECT s FROM SkCheckMaster s WHERE s.billingBank = :billingBank"),
    @NamedQuery(name = "SkCheckMaster.findByBillingAccount", query = "SELECT s FROM SkCheckMaster s WHERE s.billingAccount = :billingAccount"),
    @NamedQuery(name = "SkCheckMaster.findByAmount", query = "SELECT s FROM SkCheckMaster s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkCheckMaster.findByCreatetimestamp", query = "SELECT s FROM SkCheckMaster s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkCheckMaster.findByModifytimestamp", query = "SELECT s FROM SkCheckMaster s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkCheckMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Size(max = 10)
    @Column(name = "CHECK_NUMBER")
    private String checkNumber;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Size(max = 16)
    @Column(name = "BILLING_ACCOUNT")
    private String billingAccount;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;
    @JoinColumn(name = "BILLING_BANK", referencedColumnName = "ID")
    @ManyToOne
    private SkBank billingBank;
    @JoinColumn(name = "ADVANCE_PAYMENT", referencedColumnName="ID")
    @ManyToOne
    private SkAdvancePayment advancePayment;
        
    public SkCheckMaster() {
    }

    public SkCheckMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public SkBank getBillingBank() {
        return billingBank;
    }

    public void setBillingBank(SkBank billingBank) {
        this.billingBank = billingBank;
    }

    public String getBillingAccount() {
        return billingAccount;
    }

    public void setBillingAccount(String billingAccount) {
        this.billingAccount = billingAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public SkAdvancePayment getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(SkAdvancePayment advancePayment) {
        this.advancePayment = advancePayment;
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
        if (!(object instanceof SkCheckMaster)) {
            return false;
        }
        SkCheckMaster other = (SkCheckMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
}
