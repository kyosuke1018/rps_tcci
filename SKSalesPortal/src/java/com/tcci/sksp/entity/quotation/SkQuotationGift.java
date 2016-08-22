package com.tcci.sksp.entity.quotation;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.vo.SkQuotationGiftVO;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "SK_QUOTATION_GIFT")
@XmlRootElement
public class SkQuotationGift implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    @Expose
    private Long id;
    @Size(max = 18)
    @Column(name = "PRODUCT_NUMBER")
    @Expose
    private String productNumber;
    @Column(name = "QUANTITY")
    @Expose
    private BigDecimal quantity;
    @Column(name = "PRICE")
    @Expose
    private BigDecimal price;
    @JoinColumn(name = "QUOTATION_DETAIL", referencedColumnName = "ID")
    @ManyToOne
    @Expose(serialize = false)
    private SkQuotationDetail quotationDetail;

    public SkQuotationGift() {
        this.quantity = BigDecimal.ZERO;
        this.price = BigDecimal.ZERO;
    }

    public SkQuotationGift(SkQuotationGiftVO vo) {
        this.productNumber = vo.getProductNumber();
        this.quantity = vo.getQuantity();
        this.price = vo.getPrice();
    }

    public SkQuotationGift(SkQuotationGift another) {
        this.productNumber = another.getProductNumber();
        this.quantity = another.getQuantity();
        this.price = another.getPrice();
        this.quotationDetail = another.getQuotationDetail();
    }

    public SkQuotationGift(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SkQuotationDetail getQuotationDetail() {
        return quotationDetail;
    }

    public void setQuotationDetail(SkQuotationDetail quotationDetail) {
        this.quotationDetail = quotationDetail;
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
        if (!(object instanceof SkQuotationGift)) {
            return false;
        }
        SkQuotationGift other = (SkQuotationGift) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.quotation.SkQuotationGift:" + id;
    }
}
