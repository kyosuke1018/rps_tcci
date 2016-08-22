/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.google.gson.annotations.Expose;
import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.entity.quotation.SkQuotationReviewHistory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author nEO.Fu
 */
public class SkQuotationMasterVO implements Serializable, TcObject {

    @Expose
    private Long id;
    @Expose
    private Date quotationDate;
    @Expose
    private String poNo;
    @Expose
    private BigDecimal amount;
    @Expose
    private BigDecimal premiumDiscount;
    @Expose
    private BigDecimal totalAmount;
    @Expose
    private QuotationStatusEnum status;
    @Expose
    private Date createtimestamp;
    @Expose
    private Date modifytimestamp;
    @Expose
    private String remark;
    @Expose
    private String remark1;
    @Expose
    private String remark2;
    @Expose
    private String remark3;
    @Expose
    private String note;
    @Expose
    private Collection<SkQuotationDetailVO> detailCollection;
    @Expose
    private Collection<SkQuotationReviewHistory> reviewHistoryCollection;
    @Expose
    private SkCustomer customer;
    @Expose
    private SkCustomer consignee;
    @Expose
    private TcUser creator;
    @Expose
    private TcUser modifier;
    @Expose
    private String quotationNo;
    @Expose
    private String errorMessage;

    public SkQuotationMasterVO(SkQuotationMaster master) {
        this.id = master.getId();
        this.quotationDate = master.getQuotationDate();
        this.poNo = master.getPoNo();
        this.amount = master.getAmount();
        this.premiumDiscount = master.getPremiumDiscount();
        this.totalAmount = master.getTotalAmount();
        this.status = master.getStatus();
        this.remark = master.getRemark();
        this.remark1 = master.getRemark1();
        this.remark2 = master.getRemark2();
        this.remark3 = master.getRemark3();
        this.note = master.getNote();
        this.customer = master.getCustomer();
        this.consignee = master.getConsignee();
        this.detailCollection = new ArrayList();
        this.reviewHistoryCollection = new ArrayList();
        this.creator = master.getCreator();
        this.createtimestamp = master.getCreatetimestamp();
        this.modifier = master.getModifier();
        this.modifytimestamp = master.getModifytimestamp();
        this.quotationNo = master.getQuotationNo();
        this.errorMessage = master.getErrorMessage();
    }

    private void init() {
        this.status = QuotationStatusEnum.OPEN;
    }

    public SkQuotationMasterVO() {
        init();
    }

    public SkQuotationMasterVO(Long id) {
        init();
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

    public Collection<SkQuotationDetailVO> getDetailCollection() {
        return detailCollection;
    }

    public void setDetailCollection(Collection<SkQuotationDetailVO> detailCollection) {
        this.detailCollection = detailCollection;
    }

    public Collection<SkQuotationReviewHistory> getReviewHistoryCollection() {
        return reviewHistoryCollection;
    }

    public void setReviewHistoryCollection(Collection<SkQuotationReviewHistory> reviewHistoryCollection) {
        this.reviewHistoryCollection = reviewHistoryCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkQuotationMasterVO)) {
            return false;
        }
        SkQuotationMasterVO other = (SkQuotationMasterVO) object;
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
                .append("modifytimestamp=").append(modifytimestamp == null ? "null" : modifytimestamp).append("\n")
                .append("quotationNo=").append(quotationNo == null ? "null" : quotationNo).append("\n")
                .append("errorMessage=").append(errorMessage == null ? "null" : errorMessage).append("\n").toString();
    }

    @Override
    public String getDisplayIdentifier() {
        return toString();
    }
}
