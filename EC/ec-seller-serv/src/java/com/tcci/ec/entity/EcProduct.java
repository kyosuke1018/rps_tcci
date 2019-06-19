/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcProduct.findAll", query = "SELECT e FROM EcProduct e")
    , @NamedQuery(name = "EcProduct.findById", query = "SELECT e FROM EcProduct e WHERE e.id = :id")
    , @NamedQuery(name = "EcProduct.findByStoreId", query = "SELECT e FROM EcProduct e WHERE e.storeId = :storeId")})
public class EcProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PRODUCT", sequenceName = "SEQ_PRODUCT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "TYPE_ID")
    private Long typeId;
    @Column(name = "VENDOR_ID")
    private Long vendorId;
    @Column(name = "BRAND_ID")
    private Long brandId;
    @Column(name = "SERIAL_ID")
    private Long serialId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "COMPARE_AT_PRICE")
    private BigDecimal compareAtPrice;
    @Column(name = "PRICE_UNIT")
    private Long priceUnit;
    @Column(name = "PRICE_AMT")
    private BigDecimal priceAmt;
    @Column(name = "ITEM_UNIT")
    private Long itemUnit;
    @Column(name = "ITEM_AMT")
    private BigDecimal itemAmt;

    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "WEIGHT_UNIT")
    private Long weightUnit;
    
    @Column(name = "VOLUME")
    private String volume;
    
    @Column(name = "HAS_VARIATIONS")
    private Boolean hasVariations;
    @Column(name = "HAS_IMAGES")
    private Boolean hasImages;
    @Column(name = "CODE")
    private String code;
    @Column(name = "SKU")
    private String sku;
    @Column(name = "BARCODE")
    private String barcode;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "PUBLISH_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishTime;
    @Column(name = "PUBLISH_SCOPE")
    private String publishScope;

    @Column(name = "MIN_AMT")
    private BigDecimal minAmt;
    @Column(name = "MAX_AMT")
    private BigDecimal maxAmt;
    @Column(name = "INT_AMT")
    private Boolean intAmt;
    @Column(name = "COVER_PIC_ID")
    private Long coverPicId;
    @Column(name = "CURRENCY_ID")
    private Long currencyId;

    @Column(name = "STOCK_SETTLE")
    private BigDecimal stockSettle;
    @Column(name = "STOCK_SETTLE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stockSettleDate;
    
    @Column(name = "APPLICANT")
    private Long applicant;
    @Column(name = "APPLY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;
    @Column(name = "TCC_PRD")
    private Boolean tccPrd;// 台泥轉入商品
            
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcProduct() {
    }

    public EcProduct(Long id) {
        this.id = id;
    }

    public Boolean getTccPrd() {
        return tccPrd;
    }

    public void setTccPrd(Boolean tccPrd) {
        this.tccPrd = tccPrd;
    }

    public Long getApplicant() {
        return applicant;
    }

    public void setApplicant(Long applicant) {
        this.applicant = applicant;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getStockSettle() {
        return stockSettle;
    }

    public void setStockSettle(BigDecimal stockSettle) {
        this.stockSettle = stockSettle;
    }

    public Date getStockSettleDate() {
        return stockSettleDate;
    }

    public void setStockSettleDate(Date stockSettleDate) {
        this.stockSettleDate = stockSettleDate;
    }

    public Long getCoverPicId() {
        return coverPicId;
    }

    public void setCoverPicId(Long coverPicId) {
        this.coverPicId = coverPicId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(BigDecimal maxAmt) {
        this.maxAmt = maxAmt;
    }

    public Boolean getIntAmt() {
        return intAmt;
    }

    public void setIntAmt(Boolean intAmt) {
        this.intAmt = intAmt;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Long weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerial(Long serialId) {
        this.serialId = serialId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Long priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getPriceAmt() {
        return priceAmt;
    }

    public void setPriceAmt(BigDecimal priceAmt) {
        this.priceAmt = priceAmt;
    }

    public Long getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Long itemUnit) {
        this.itemUnit = itemUnit;
    }

    public BigDecimal getItemAmt() {
        return itemAmt;
    }

    public void setItemAmt(BigDecimal itemAmt) {
        this.itemAmt = itemAmt;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public Boolean getHasImages() {
        return hasImages;
    }

    public void setHasImages(Boolean hasImages) {
        this.hasImages = hasImages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishScope() {
        return publishScope;
    }

    public void setPublishScope(String publishScope) {
        this.publishScope = publishScope;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
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
        if (!(object instanceof EcProduct)) {
            return false;
        }
        EcProduct other = (EcProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcProduct[ id=" + id + " ]";
    }
    
}
