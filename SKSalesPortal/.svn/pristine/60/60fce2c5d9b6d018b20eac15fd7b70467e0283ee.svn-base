package com.tcci.sksp.vo;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author nEO.Fu
 */
public class SkQuotationGiftVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Expose
    private Long id;
    @Expose
    private String productNumber;
    @Expose
    private String productName;
    @Expose
    private BigDecimal quantity;
    @Expose
    private BigDecimal price;
    @Expose(serialize = false, deserialize = false)
    private SkQuotationDetailVO quotationDetail;

    public SkQuotationGiftVO(SkQuotationGift gift) {
        this.id = gift.getId();
        this.productNumber = gift.getProductNumber();
        this.quantity = gift.getQuantity();
        this.price = gift.getPrice();
    }

    public SkQuotationGiftVO(Long id) {
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public SkQuotationDetailVO getQuotationDetail() {
        return quotationDetail;
    }

    public void setQuotationDetail(SkQuotationDetailVO quotationDetail) {
        this.quotationDetail = quotationDetail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (!(object instanceof SkQuotationGiftVO)) {
            return false;
        }
        SkQuotationGiftVO other = (SkQuotationGiftVO) object;
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
