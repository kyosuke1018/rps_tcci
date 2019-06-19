/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

//import com.sun.istack.NotNull;
import com.tcci.cm.annotation.InputCheckMeta;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_PRD_VARIANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcPrdVariant.findAll", query = "SELECT e FROM EcPrdVariant e")
    , @NamedQuery(name = "EcPrdVariant.findById", query = "SELECT e FROM EcPrdVariant e WHERE e.id = :id")
    , @NamedQuery(name = "EcPrdVariant.findByStoreId", query = "SELECT e FROM EcPrdVariant e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcPrdVariant.findByPrdId", query = "SELECT e FROM EcPrdVariant e WHERE e.prdId = :prdId")})
public class EcPrdVariant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PRD_VARIANT", sequenceName = "SEQ_PRD_VARIANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRD_VARIANT")        
    private Long id;
    @Basic(optional = false)
    @Column(name = "STORE_ID")
    private long storeId;
    @Basic(optional = false)
    @Column(name = "PRD_ID")
    private long prdId;
    
    @Basic(optional = false)
    @InputCheckMeta(key="EC_PRD_VARIANT.CNAME")
    @Column(name = "CNAME")
    private String cname;
    
    @InputCheckMeta(key="EC_PRD_VARIANT.ENAME")
    @Column(name = "ENAME")
    private String ename;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private BigDecimal price;
    
    @InputCheckMeta(key="EC_PRD_VARIANT.SKU")
    @Column(name = "SKU")
    private String sku;
    @InputCheckMeta(key="EC_PRD_VARIANT.BARCODE")
    @Column(name = "BARCODE")
    private String barcode;
    
    @Column(name = "SORTNUM")
    private Integer sortnum;
    @Column(name = "COMPARE_AT_PRICE")
    private BigDecimal compareAtPrice;
    @Column(name = "FULFILLMENT_SERVICE")
    private Long fulfillmentService;
    @Column(name = "INVENTORY_MGN")
    private Long inventoryMgn;
    @Column(name = "COLOR_ID")
    private Long colorId;
    @Column(name = "SIZE_ID")
    private Long sizeId;
    @Column(name = "TAXABLE")
    private Boolean taxable;
    @Column(name = "GRAMS")
    private BigDecimal grams;
    @Column(name = "IMAGE_ID")
    private Long imageId;
    @Column(name = "INVENTORY_QUANTITY")
    private Long inventoryQuantity;
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "WEIGHT_UNIT")
    private Long weightUnit;
    
    @InputCheckMeta(key="EC_PRD_VARIANT.VOLUME")
    @Column(name = "VOLUME")
    private String volume;

    @Column(name = "REQ_SHIPPING")
    private Boolean reqShipping;
    @Column(name = "DISABLED")
    private Boolean disabled;

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

    public EcPrdVariant() {
    }

    public EcPrdVariant(Long id) {
        this.id = id;
    }

    public EcPrdVariant(Long id, long storeId, long prdId, String cname) {
        this.id = id;
        this.storeId = storeId;
        this.prdId = prdId;
        this.cname = cname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getPrdId() {
        return prdId;
    }

    public void setPrdId(long prdId) {
        this.prdId = prdId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public Long getFulfillmentService() {
        return fulfillmentService;
    }

    public void setFulfillmentService(Long fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    public Long getInventoryMgn() {
        return inventoryMgn;
    }

    public void setInventoryMgn(Long inventoryMgn) {
        this.inventoryMgn = inventoryMgn;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getGrams() {
        return grams;
    }

    public void setGrams(BigDecimal grams) {
        this.grams = grams;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Long inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
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

    public Boolean getReqShipping() {
        return reqShipping;
    }

    public void setReqShipping(Boolean reqShipping) {
        this.reqShipping = reqShipping;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EcPrdVariant)) {
            return false;
        }
        EcPrdVariant other = (EcPrdVariant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcPrdVariant[ id=" + id + " ]";
    }
    
}
