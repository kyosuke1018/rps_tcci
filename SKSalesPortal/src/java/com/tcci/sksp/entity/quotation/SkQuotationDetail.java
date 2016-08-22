/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.quotation;

import com.google.gson.annotations.Expose;
import com.tcci.sksp.vo.SkQuotationDetailVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author neo
 */
@Entity
@Table(name = "SK_QUOTATION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkQuotationDetail.findAll", query = "SELECT s FROM SkQuotationDetail s"),
    @NamedQuery(name = "SkQuotationDetail.findById", query = "SELECT s FROM SkQuotationDetail s WHERE s.id = :id"),
    @NamedQuery(name = "SkQuotationDetail.findByProductNumber", query = "SELECT s FROM SkQuotationDetail s WHERE s.productNumber = :productNumber"),
    @NamedQuery(name = "SkQuotationDetail.findByPremiumDiscount", query = "SELECT s FROM SkQuotationDetail s WHERE s.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "SkQuotationDetail.findByQuantity", query = "SELECT s FROM SkQuotationDetail s WHERE s.quantity = :quantity"),
    @NamedQuery(name = "SkQuotationDetail.findByPrice", query = "SELECT s FROM SkQuotationDetail s WHERE s.price = :price")})
public class SkQuotationDetail implements Serializable {

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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    @Expose
    private BigDecimal price;
    @Column(name = "QUANTITY")
    @Expose
    private BigDecimal quantity;
    @Column(name = "PREMIUM_DISCOUNT")
    @Expose
    private BigDecimal premiumDiscount;
    @JoinColumn(name = "QUOTATION_MASTER", referencedColumnName = "ID")
    @ManyToOne
    @Expose(serialize = false)
    private SkQuotationMaster quotationMaster;
    @OneToMany(mappedBy = "quotationDetail", cascade = CascadeType.ALL)
    @Expose
    private List<SkQuotationGift> giftList;

    public SkQuotationDetail() {
        this.price = BigDecimal.ZERO;
        this.quantity = BigDecimal.ZERO;
        this.premiumDiscount = BigDecimal.ZERO;
    }

    public SkQuotationDetail(SkQuotationDetailVO vo) {
        this.productNumber = vo.getProductNumber();
        this.price = vo.getPrice();
        this.quantity = vo.getQuantity();
        this.premiumDiscount = vo.getPremiumDiscount();
        this.giftList = new ArrayList();
    }

    public SkQuotationDetail(SkQuotationDetail another) {
        this.productNumber = another.getProductNumber();
        this.price = another.getPrice();
        this.quantity = another.getQuantity();
        this.premiumDiscount = another.getPremiumDiscount();
        this.quotationMaster = another.getQuotationMaster();
        this.giftList = another.getGiftList();
    }

    public SkQuotationDetail(Long id) {
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

    public SkQuotationMaster getQuotationMaster() {
        return quotationMaster;
    }

    public void setQuotationMaster(SkQuotationMaster quotationMaster) {
        this.quotationMaster = quotationMaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<SkQuotationGift> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<SkQuotationGift> giftList) {
        this.giftList = giftList;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkQuotationDetail)) {
            return false;
        }
        SkQuotationDetail other = (SkQuotationDetail) object;
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

    public static void copyProperty(SkQuotationDetailVO vo, SkQuotationDetail target) {
        target.setProductNumber(vo.getProductNumber());
        target.setPrice(vo.getPrice());
        target.setQuantity(vo.getQuantity());
        target.setPremiumDiscount(vo.getPremiumDiscount());
    }
}
