package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "SK_ADVANCE_PAYMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkAdvancePayment.findAll", query = "SELECT s FROM SkAdvancePayment s"),
    @NamedQuery(name = "SkAdvancePayment.findById", query = "SELECT s FROM SkAdvancePayment s WHERE s.id = :id"),
    @NamedQuery(name = "SkAdvancePayment.findByAmount", query = "SELECT s FROM SkAdvancePayment s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkAdvancePayment.findByCreatetimestamp", query = "SELECT s FROM SkAdvancePayment s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkAdvancePayment.findByModifytimestamp", query = "SELECT s FROM SkAdvancePayment s WHERE s.modifytimestamp = :modifytimestamp")})
public class SkAdvancePayment implements Serializable, Persistable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @NotNull
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "AR_REMIT_MASTER", referencedColumnName = "ID")
    @OneToOne
    private SkArRemitMaster arRemitMaster;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "INTERFACE_ID", referencedColumnName="ID")
    @ManyToOne
    private SkFiMasterInterface fiInterface;
    @OneToMany(mappedBy = "advancePayment")
    private Collection<SkCheckMaster> skCheckMasterCollection;

    public SkAdvancePayment() {
    }

    public SkAdvancePayment(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkArRemitMaster getArRemitMaster() {
        return arRemitMaster;
    }

    public void setArRemitMaster(SkArRemitMaster arRemitMaster) {
        this.arRemitMaster = arRemitMaster;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public SkFiMasterInterface getFiInterface() {
        return fiInterface;
    }

    public void setFiInterface(SkFiMasterInterface fiInterface) {
        this.fiInterface = fiInterface;
    }

    public Collection<SkCheckMaster> getSkCheckMasterCollection() {
        return skCheckMasterCollection;
    }

    public void setSkCheckMasterCollection(Collection<SkCheckMaster> skCheckMasterCollection) {
        this.skCheckMasterCollection = skCheckMasterCollection;
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
        if (!(object instanceof SkAdvancePayment)) {
            return false;
        }
        SkAdvancePayment other = (SkAdvancePayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkAdvancePayment:" + id;
    }
}
