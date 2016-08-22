/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.enums.BankEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_AR_REMIT_MASTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkArRemitMaster.findAll", query = "SELECT s FROM SkArRemitMaster s"),
    @NamedQuery(name = "SkArRemitMaster.findById", query = "SELECT s FROM SkArRemitMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SkArRemitMaster.findByCustomer", query = "SELECT s FROM SkArRemitMaster s WHERE s.customer = :customer"),
    @NamedQuery(name = "SkArRemitMaster.findBySapid", query = "SELECT s FROM SkArRemitMaster s WHERE s.sapid = :sapid"),
    @NamedQuery(name = "SkArRemitMaster.findByBank", query = "SELECT s FROM SkArRemitMaster s WHERE s.bank = :bank"),
    @NamedQuery(name = "SkArRemitMaster.findByRemittanceAmount", query = "SELECT s FROM SkArRemitMaster s WHERE s.remittanceAmount = :remittanceAmount"),
    @NamedQuery(name = "SkArRemitMaster.findByCheckAmount", query = "SELECT s FROM SkArRemitMaster s WHERE s.checkAmount = :checkAmount"),
    @NamedQuery(name = "SkArRemitMaster.findByFinanceReviewer", query = "SELECT s FROM SkArRemitMaster s WHERE s.financeReviewer = :financeReviewer"),
    @NamedQuery(name = "SkArRemitMaster.findByStatus", query = "SELECT s FROM SkArRemitMaster s WHERE s.status = :status"),
    @NamedQuery(name = "SkArRemitMaster.findByReviewTimestamp", query = "SELECT s FROM SkArRemitMaster s WHERE s.reviewTimestamp = :reviewTimestamp"),
    @NamedQuery(name = "SkArRemitMaster.findByCreatetimestamp", query = "SELECT s FROM SkArRemitMaster s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkArRemitMaster.findByModifytimestamp", query = "SELECT s FROM SkArRemitMaster s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkArRemitMaster implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Column(name = "SAPID")
    private String sapid;
    @Column(name = "BANK")
    @Enumerated(EnumType.STRING)
    private BankEnum bank;
    @Column(name = "REMITTANCE_AMOUNT")
    private BigDecimal remittanceAmount;
    @Column(name = "CHECK_AMOUNT")
    private BigDecimal checkAmount;
    @JoinColumn(name = "FINANCE_REVIEWER", referencedColumnName="ID")
    @ManyToOne
    private TcUser financeReviewer;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RemitMasterStatusEnum status;    
    @Column(name = "REVIEW_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewTimestamp;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "INTERFACE_ID", referencedColumnName="ID")
    @ManyToOne
    private SkFiMasterInterface fiInterface;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;    
    @OneToMany(mappedBy = "arRemitMaster")
    @OrderBy("invoiceTimestamp")
    private Collection<SkArRemitItem> skArRemitItemCollection;

    public SkArRemitMaster() {
    }

    public SkArRemitMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public BankEnum getBank() {
        return bank;
    }

    public void setBank(BankEnum bank) {
        this.bank = bank;
    }

    public BigDecimal getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(BigDecimal remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public BigDecimal getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(BigDecimal checkAmount) {
        this.checkAmount = checkAmount;
    }

    public TcUser getFinanceReviewer() {
        return financeReviewer;
    }

    public void setFinanceReviewer(TcUser financeReviewer) {
        this.financeReviewer = financeReviewer;
    }

    public RemitMasterStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RemitMasterStatusEnum status) {
        this.status = status;
    }

    public Date getReviewTimestamp() {
        return reviewTimestamp;
    }

    public void setReviewTimestamp(Date reviewTimestamp) {
        this.reviewTimestamp = reviewTimestamp;
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

    public SkFiMasterInterface getFiInterface() {
        return fiInterface;
    }

    public void setFiInterface(SkFiMasterInterface fiInterface) {
        this.fiInterface = fiInterface;
    }

    public boolean isFinanceCancelable() {
        return (getStatus() != RemitMasterStatusEnum.CANCELED &&
                getStatus() != RemitMasterStatusEnum.TRANSFER_ADVANCE &&
                getStatus() != RemitMasterStatusEnum.TRANSFER_SAP &&
                getStatus() != RemitMasterStatusEnum.TRANSFER_OK);
    }
    
    public boolean isSalesCancelable() {
        return (getStatus() == RemitMasterStatusEnum.NOT_YET);
    }        

    @XmlTransient
    public Collection<SkArRemitItem> getSkArRemitItemCollection() {
        return skArRemitItemCollection;
    }

    public void setSkArRemitItemCollection(Collection<SkArRemitItem> skArRemitItemCollection) {
        this.skArRemitItemCollection = skArRemitItemCollection;
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
        if (!(object instanceof SkArRemitMaster)) {
            return false;
        }
        SkArRemitMaster other = (SkArRemitMaster) object;
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
