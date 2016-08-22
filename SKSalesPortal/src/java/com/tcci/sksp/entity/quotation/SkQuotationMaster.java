/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.vo.SkQuotationMasterVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "SK_QUOTATION_MASTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkQuotationMaster.findAll", query = "SELECT s FROM SkQuotationMaster s"),
    @NamedQuery(name = "SkQuotationMaster.findById", query = "SELECT s FROM SkQuotationMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SkQuotationMaster.findByAmount", query = "SELECT s FROM SkQuotationMaster s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkQuotationMaster.findByTotalAmount", query = "SELECT s FROM SkQuotationMaster s WHERE s.totalAmount = :totalAmount"),
    @NamedQuery(name = "SkQuotationMaster.findByPremiumDiscount", query = "SELECT s FROM SkQuotationMaster s WHERE s.premiumDiscount = :premiumDiscount")})
public class SkQuotationMaster implements Serializable, TcObject {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    @Expose
    private Long id;
    @Column(name = "QUOTATION_DATE")
    @Temporal(TemporalType.DATE)
    @Expose
    private Date quotationDate;
    @Column(name = "PO_NO")
    @Expose
    private String poNo;
    @Column(name = "AMOUNT")
    @Expose
    private BigDecimal amount;
    @Column(name = "PREMIUM_DISCOUNT")
    @Expose
    private BigDecimal premiumDiscount;
    @Column(name = "TOTAL_AMOUNT")
    @Expose
    private BigDecimal totalAmount;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @Expose
    private QuotationStatusEnum status;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date modifytimestamp;
    @Column(name = "REMARK")
    @Expose
    private String remark;
    @Column(name = "REMARK1")
    @Expose
    private String remark1;
    @Column(name = "REMARK2")
    @Expose
    private String remark2;
    @Column(name = "REMARK3")
    @Expose
    private String remark3;
    @Column(name = "NOTE")
    @Expose
    private String note;
    @OneToMany(mappedBy = "quotationMaster", cascade = CascadeType.ALL)
    @Expose
    private Collection<SkQuotationDetail> detailCollection;
    @OneToMany(mappedBy = "quotationMaster", cascade = CascadeType.ALL)
    @Expose
    private Collection<SkQuotationReviewHistory> reviewHistoryCollection;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private SkCustomer customer;
    @JoinColumn(name = "CONSIGNEE", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private SkCustomer consignee;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser modifier;
    @Expose
    @Column(name = "QUOTATION_NO")
    private String quotationNo;
    @Expose
    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    public SkQuotationMaster() {
    }

    public SkQuotationMaster(SkQuotationMasterVO vo) {
        this.id = vo.getId();
        this.quotationDate = vo.getQuotationDate();
        this.poNo = vo.getPoNo();
        this.amount = vo.getAmount();
        this.premiumDiscount = vo.getPremiumDiscount();
        this.totalAmount = vo.getTotalAmount();
        this.status = vo.getStatus();
        this.remark = vo.getRemark();
        this.remark1 = vo.getRemark1();
        this.remark2 = vo.getRemark2();
        this.remark3 = vo.getRemark3();
        this.note = vo.getNote();
        this.customer = vo.getCustomer();
        this.consignee = vo.getConsignee();
        this.detailCollection = new ArrayList();
        this.reviewHistoryCollection = new ArrayList();
        this.creator = vo.getCreator();
        this.createtimestamp = vo.getCreatetimestamp();
        this.modifier = vo.getModifier();
        this.modifytimestamp = vo.getModifytimestamp();
        this.quotationNo = vo.getQuotationNo();
        this.errorMessage = vo.getErrorMessage();
    }

    public SkQuotationMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public QuotationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(QuotationStatusEnum status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public SkCustomer getConsignee() {
        return consignee;
    }

    public void setConsignee(SkCustomer consignee) {
        this.consignee = consignee;
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

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<SkQuotationDetail> getDetailCollection() {
        return detailCollection;
    }

    public void setDetailCollection(Collection<SkQuotationDetail> detailCollection) {
        this.detailCollection = detailCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<SkQuotationReviewHistory> getReviewHistoryCollection() {
        return reviewHistoryCollection;
    }

    public void setReviewHistoryCollection(Collection<SkQuotationReviewHistory> reviewHistoryCollection) {
        this.reviewHistoryCollection = reviewHistoryCollection;
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
        if (!(object instanceof SkQuotationMaster)) {
            return false;
        }
        SkQuotationMaster other = (SkQuotationMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.quotation.SkQuotationMaster:" + id;
    }

    public String toStringVerbose() {
        StringBuilder sb = new StringBuilder();
        return sb.append("id=").append(id == null ? "" : id).append("\n")
                .append("customer=").append(customer == null ? "null" : customer.getDisplayIdentifier()).append("\n")
                .append("consignee=").append(consignee == null ? "null" : consignee.getDisplayIdentifier()).append("\n")
                .append("quotationDate=").append(quotationDate == null ? "null" : quotationDate).append("\n")
                .append("poNo=").append(poNo == null ? "null" : poNo).append("\n")
                .append("amount=").append(amount == null ? "null" : amount).append("\n")
                .append("premiumDiscount=").append(premiumDiscount == null ? "null" : premiumDiscount).append("\n")
                .append("totalAmount=").append(totalAmount == null ? "null" : totalAmount).append("\n")
                .append("quotationNo=").append(quotationNo == null ? "null" : quotationNo).append("\n")
                .append("errorMessage=").append(errorMessage == null ? "null" : errorMessage).append("\n")
                .append("status=").append(status == null ? "null" : status.getDisplayName()).append("\n")
                .append("remark=").append(remark == null ? "null" : remark).append("\n")
                .append("remark1=").append(remark1 == null ? "null" : remark1).append("\n")
                .append("remark2=").append(remark2 == null ? "null" : remark2).append("\n")
                .append("remark3=").append(remark3 == null ? "null" : remark3).append("\n")
                .append("detailCollection=").append(detailCollection == null ? "[]" : detailCollection.toString()).append("\n")
                .append("reviewHistoryCollection=").append(reviewHistoryCollection == null ? "[]" : reviewHistoryCollection.toString()).append("\n")
                .append("creator=").append(creator == null ? "null" : creator).append("\n")
                .append("createtimestamp=").append(createtimestamp == null ? "null" : createtimestamp).append("\n")
                .append("modifier=").append(modifier == null ? "null" : modifier).append("\n")
                .append("modifytimestamp=").append(modifytimestamp == null ? "null" : modifytimestamp).append("\n").toString();
    }

    @Override
    public String getDisplayIdentifier() {
        return this.getClass().getCanonicalName() + ":" + this.id;
    }

    public static void copyProperty(SkQuotationMasterVO vo, SkQuotationMaster target) {
        target.setQuotationDate(vo.getQuotationDate());
        target.setPoNo(vo.getPoNo());
        target.setAmount(vo.getAmount());
        target.setPremiumDiscount(vo.getPremiumDiscount());
        target.setTotalAmount(vo.getTotalAmount());
        target.setStatus(vo.getStatus());
        target.setRemark(vo.getRemark());
        target.setRemark1(vo.getRemark1());
        target.setRemark2(vo.getRemark2());
        target.setRemark3(vo.getRemark3());
        target.setNote(vo.getNote());
        target.setCustomer(vo.getCustomer());
        target.setConsignee(vo.getConsignee());
        target.setCreator(vo.getCreator());
        target.setCreatetimestamp(vo.getCreatetimestamp());
        target.setModifier(vo.getModifier());
        target.setModifytimestamp(vo.getModifytimestamp());
    }
}
