/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.fc.entity.org.TcUser;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_FI_DETAIL_INTERFACE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkFiDetailInterface.findAll", query = "SELECT s FROM SkFiDetailInterface s"),
    @NamedQuery(name = "SkFiDetailInterface.findById", query = "SELECT s FROM SkFiDetailInterface s WHERE s.id = :id"),
    @NamedQuery(name = "SkFiDetailInterface.findByTransactionItem", query = "SELECT s FROM SkFiDetailInterface s WHERE s.transactionItem = :transactionItem"),
    @NamedQuery(name = "SkFiDetailInterface.findByTransactionDate", query = "SELECT s FROM SkFiDetailInterface s WHERE s.transactionDate = :transactionDate"),
    @NamedQuery(name = "SkFiDetailInterface.findByTransactionType", query = "SELECT s FROM SkFiDetailInterface s WHERE s.transactionType = :transactionType"),
    @NamedQuery(name = "SkFiDetailInterface.findByGeneralLedgerCode", query = "SELECT s FROM SkFiDetailInterface s WHERE s.generalLedgerCode = :generalLedgerCode"),
    @NamedQuery(name = "SkFiDetailInterface.findBySummonsCode", query = "SELECT s FROM SkFiDetailInterface s WHERE s.summonsCode = :summonsCode"),
    @NamedQuery(name = "SkFiDetailInterface.findByCustomerNumber", query = "SELECT s FROM SkFiDetailInterface s WHERE s.customerNumber = :customerNumber"),
    @NamedQuery(name = "SkFiDetailInterface.findByInvoiceNumber", query = "SELECT s FROM SkFiDetailInterface s WHERE s.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "SkFiDetailInterface.findByOrderNumber", query = "SELECT s FROM SkFiDetailInterface s WHERE s.orderNumber = :orderNumber"),
    @NamedQuery(name = "SkFiDetailInterface.findBySalesGroup", query = "SELECT s FROM SkFiDetailInterface s WHERE s.salesGroup = :salesGroup"),
    @NamedQuery(name = "SkFiDetailInterface.findByTransactionAmount", query = "SELECT s FROM SkFiDetailInterface s WHERE s.transactionAmount = :transactionAmount"),
    @NamedQuery(name = "SkFiDetailInterface.findByQuantity", query = "SELECT s FROM SkFiDetailInterface s WHERE s.quantity = :quantity"),
    @NamedQuery(name = "SkFiDetailInterface.findByCheckNumber", query = "SELECT s FROM SkFiDetailInterface s WHERE s.checkNumber = :checkNumber"),
    @NamedQuery(name = "SkFiDetailInterface.findByCheckDueDate", query = "SELECT s FROM SkFiDetailInterface s WHERE s.checkDueDate = :checkDueDate"),
    @NamedQuery(name = "SkFiDetailInterface.findByCheckBank", query = "SELECT s FROM SkFiDetailInterface s WHERE s.checkBank = :checkBank"),
    @NamedQuery(name = "SkFiDetailInterface.findByCheckAccount", query = "SELECT s FROM SkFiDetailInterface s WHERE s.checkAccount = :checkAccount"),
    @NamedQuery(name = "SkFiDetailInterface.findByOwner", query = "SELECT s FROM SkFiDetailInterface s WHERE s.owner = :owner"),
    @NamedQuery(name = "SkFiDetailInterface.findByStatus", query = "SELECT s FROM SkFiDetailInterface s WHERE s.status = :status"),
    @NamedQuery(name = "SkFiDetailInterface.findByUploadTimestamp", query = "SELECT s FROM SkFiDetailInterface s WHERE s.uploadTimestamp = :uploadTimestamp"),
    @NamedQuery(name = "SkFiDetailInterface.findByUploader", query = "SELECT s FROM SkFiDetailInterface s WHERE s.uploader = :uploader"),
    @NamedQuery(name = "SkFiDetailInterface.findByCreatetimestamp", query = "SELECT s FROM SkFiDetailInterface s WHERE s.createtimestamp = :createtimestamp"),
    @NamedQuery(name = "SkFiDetailInterface.findByModifytimestamp", query = "SELECT s FROM SkFiDetailInterface s WHERE s.modifytimestamp = :modifytimestamp"),
    @NamedQuery(name = "SkFiDetailInterface.findByReferenceclassname", query = "SELECT s FROM SkFiDetailInterface s WHERE s.referenceclassname = :referenceclassname"),
    @NamedQuery(name = "SkFiDetailInterface.findByReferenceid", query = "SELECT s FROM SkFiDetailInterface s WHERE s.referenceid = :referenceid")})
public class SkFiDetailInterface implements Serializable {

//    private static final Logger logger = LoggerFactory.getLogger(SkFiDetailInterface.class);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TRANSACTION_ITEM")
    private String transactionItem;
    @Column(name = "TRANSACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;
    @Column(name = "GENERAL_LEDGER_CODE")
    private String generalLedgerCode;
    @Column(name = "SUMMONS_CODE")
    private String summonsCode;
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "SALES_GROUP")
    private String salesGroup;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Size(max = 10)
    @Column(name = "CHECK_NUMBER")
    private String checkNumber;
    @Column(name = "CHECK_DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDueDate;
    @Size(max = 10)
    @Column(name = "CHECK_BANK")
    private String checkBank;
    @Size(max = 16)
    @Column(name = "CHECK_ACCOUNT")
    private String checkAccount;
    @Column(name = "OWNER")
    private String owner;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "UPLOAD_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTimestamp;
    @Column(name = "UPLOADER")
    private String uploader;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @Column(name = "REFERENCECLASSNAME")
    private String referenceclassname;
    @Column(name = "REFERENCEID")
    private Long referenceid;
    @Column(name = "RETURN_CODE")
    private String returnCode;
    @Column(name = "RETURN_MESSAGE")
    private String returnMessage;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "MASTER", referencedColumnName = "ID")
    @ManyToOne
    private SkFiMasterInterface master;

    public SkFiDetailInterface() {
        this.transactionType = "CLAR";
    }

    public SkFiDetailInterface(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionItem() {
        return transactionItem;
    }

    public void setTransactionItem(String transactionItem) {
        this.transactionItem = transactionItem;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getGeneralLedgerCode() {
        return generalLedgerCode;
    }

    public void setGeneralLedgerCode(String generalLedgerCode) {
        this.generalLedgerCode = generalLedgerCode;
    }

    public String getSummonsCode() {
        return summonsCode;
    }

    public void setSummonsCode(String summonsCode) {
        this.summonsCode = summonsCode;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSalesGroup() {
        return salesGroup;
    }

    public void setSalesGroup(String salesGroup) {
        this.salesGroup = salesGroup;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Date getCheckDueDate() {
        return checkDueDate;
    }

    public void setCheckDueDate(Date checkDueDate) {
        this.checkDueDate = checkDueDate;
    }

    public String getCheckBank() {
        return checkBank;
    }

    public void setCheckBank(String checkBank) {
        this.checkBank = checkBank;
    }

    public String getCheckAccount() {
        return checkAccount;
    }

    public void setCheckAccount(String checkAccount) {
        this.checkAccount = checkAccount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Date uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
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

    public String getReferenceclassname() {
        return referenceclassname;
    }

    public void setReferenceclassname(String referenceclassname) {
        this.referenceclassname = referenceclassname;
    }

    public Long getReferenceid() {
        return referenceid;
    }

    public void setReferenceid(Long referenceid) {
        this.referenceid = referenceid;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public SkFiMasterInterface getMaster() {
        return master;
    }

    public void setMaster(SkFiMasterInterface master) {
        this.master = master;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SkFiDetailInterface)) {
            return false;
        }
        SkFiDetailInterface other = (SkFiDetailInterface) object;
        if ((this.id == null && other.id == null)) {
            if ((this.master != null && other.master == null) || (this.master == null || other.master != null)) {
                return false;
            } else {
                if (this.master.equals(other.master)) {
                    if ((this.transactionItem == null && other.transactionItem != null) || (this.transactionItem != null && other.transactionItem == null)) {
                        return false;
                    } else {
                        if (this.transactionItem.equals(other.transactionItem)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.ar.SkFiDetailInterface[ id=" + id + " ]";
    }
}
