/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.quotation;

import com.google.gson.annotations.Expose;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.enums.QuotationReviewOptionEnum;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author neo
 */
@Entity
@Table(name = "SK_QUOTATION_REVIEW_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkQuotationReviewHistory.findAll", query = "SELECT s FROM SkQuotationReviewHistory s"),
    @NamedQuery(name = "SkQuotationReviewHistory.findById", query = "SELECT s FROM SkQuotationReviewHistory s WHERE s.id = :id"),
    @NamedQuery(name = "SkQuotationReviewHistory.findByRemark", query = "SELECT s FROM SkQuotationReviewHistory s WHERE s.remark = :remark")})
public class SkQuotationReviewHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    @Expose
    private Long id;
    @Column(name = "REVIEW_OPTION")
    @Enumerated(EnumType.STRING)
    @Expose
    private QuotationReviewOptionEnum reviewOption;
    @Size(max = 4000)
    @Column(name = "REMARK")
    @Expose
    private String remark;
    @JoinColumn(name = "QUOTATION_MASTER", referencedColumnName = "ID")
    @ManyToOne
    @Expose(serialize = false)
    private SkQuotationMaster quotationMaster;
    @JoinColumn(name = "REVIEWER", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser reviewer;
    @Column(name = "REVIEW_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date reviewDate;

    public SkQuotationReviewHistory() {
    }

    public SkQuotationReviewHistory(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SkQuotationMaster getQuotationMaster() {
        return quotationMaster;
    }

    public void setQuotationMaster(SkQuotationMaster quotationMaster) {
        this.quotationMaster = quotationMaster;
    }

    public TcUser getReviewer() {
        return reviewer;
    }

    public void setReviewer(TcUser reviewer) {
        this.reviewer = reviewer;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public QuotationReviewOptionEnum getReviewOption() {
        return reviewOption;
    }

    public void setReviewOption(QuotationReviewOptionEnum reviewOption) {
        this.reviewOption = reviewOption;
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
        if (!(object instanceof SkQuotationReviewHistory)) {
            return false;
        }
        SkQuotationReviewHistory other = (SkQuotationReviewHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public static void copyProperty(SkQuotationReviewHistory source, SkQuotationReviewHistory target) {
        target.setReviewOption(source.getReviewOption());
        target.setRemark(source.getRemark());
        target.setQuotationMaster(source.getQuotationMaster());
        target.setReviewer(source.getReviewer());
        target.setReviewDate(source.getReviewDate());
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.quotation.SkQuotationReviewHistory:" + id;
    }
}
