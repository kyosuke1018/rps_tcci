/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author neo
 */
public class SkQuotationDetailVO implements Serializable {

    @Expose
    private Long id;
    @Expose
    private String productNumber;
    @Expose
    private String productName;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal quantity;
    @Expose
    private BigDecimal premiumDiscount;
    @Expose
    private SkQuotationMasterVO quotationMaster;
    @Expose
    private List<SkQuotationGiftVO> giftList;

    public SkQuotationDetailVO(SkQuotationDetail detail) {
        this.id = detail.getId();
        this.productNumber = detail.getProductNumber();
        this.price = detail.getPrice();
        this.quantity = detail.getQuantity();
        this.premiumDiscount = detail.getPremiumDiscount();
        this.giftList = new ArrayList();
    }

    public SkQuotationDetailVO(Long id) {
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

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
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

    public SkQuotationMasterVO getQuotationMaster() {
        return quotationMaster;
    }

    public void setQuotationMaster(SkQuotationMasterVO quotationMaster) {
        this.quotationMaster = quotationMaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<SkQuotationGiftVO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<SkQuotationGiftVO> giftList) {
        this.giftList = giftList;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkQuotationDetailVO)) {
            return false;
        }
        SkQuotationDetailVO other = (SkQuotationDetailVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("id=").append(id == null ? "null" : id).append("\n")
                .append("productNumber=").append(productNumber == null ? "null" : productNumber).append("\n")
                .append("price=").append(price == null ? "null" : price).append("\n")
                .append("quantity=").append(quantity == null ? "null" : quantity).append("\n")
                .append("premiumDiscount=").append(premiumDiscount == null ? "null" : premiumDiscount).append("\n")
                .append("quotationMaster=").append(quotationMaster == null ? "null" : quotationMaster).append("\n")
                .append("giftList=").append(giftList == null ? "null" : giftList.toString()).append("\n")
                .toString();
    }
}
