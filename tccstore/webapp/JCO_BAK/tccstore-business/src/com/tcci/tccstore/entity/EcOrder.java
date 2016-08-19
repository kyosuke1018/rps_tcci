/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.enums.OrderStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_ORDER")
@NamedQueries({
    @NamedQuery(name = "EcOrder.findAll", query = "SELECT e FROM EcOrder e"),
    @NamedQuery(name = "EcOrder.findByStatus", query = "SELECT e FROM EcOrder e WHERE e.status=:status ORDER BY e.id DESC")
})
public class EcOrder implements Serializable, Persistable{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_ORDER",sequenceName = "SEQ_ORDER", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_ORDER")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "PRODUCT_NAME")
    private String productName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "VEHICLE")
    private String vehicle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "DELIVERY_DATE")
    private String deliveryDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "METHOD")
    private String method;
    @Size(max = 50)
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Size(max = 50)
    @Column(name = "CONTRACT_NAME")
    private String contractName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PLANT_CODE")
    private String plantCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "PLANT_NAME")
    private String plantName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "SALESAREA_CODE")
    private String salesareaCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "SALESAREA_NAME")
    private String salesareaName;
    @Size(max = 20)
    @Column(name = "DELIVERY_CODE")
    private String deliveryCode;
    @Size(max = 140)
    @Column(name = "DELIVERY_NAME")
    private String deliveryName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "SALES_CODE")
    private String salesCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "SALES_NAME")
    private String salesName;
    @Size(max = 60)
    @Column(name = "SITE_LOC")
    private String siteLoc;
    @Column(name = "POSNR")
    private Integer posnr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BONUS")
    private int bonus;
    @Basic(optional = false)
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;
    @Size(max = 40)
    @Column(name = "SAP_ORDERNUM")
    private String sapOrdernum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "SALESAREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSalesarea salesareaId;
    @JoinColumn(name = "SALES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcSales salesId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcProduct productId;
    @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcPlant plantId;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember memberId;
    @JoinColumn(name = "DELIVERY_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcDeliveryPlace deliveryId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcCustomer customerId;
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcContract contractId;
    @JoinColumn(name = "APPROVER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser approver;
    @Column(name = "APPROVAL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;
    @Column (name="MESSAGE")
    private String message;

    public EcOrder() {
    }

    public EcOrder(Long id) {
        this.id = id;
    }

    public EcOrder(Long id, String productCode, String productName, BigDecimal unitPrice, BigDecimal quantity, BigDecimal amount, String vehicle, String deliveryDate, String method, String plantCode, String plantName, String salesareaCode, String salesareaName, String salesCode, String salesName, int bonus, OrderStatusEnum status, Date createtime) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.amount = amount;
        this.vehicle = vehicle;
        this.deliveryDate = deliveryDate;
        this.method = method;
        this.plantCode = plantCode;
        this.plantName = plantName;
        this.salesareaCode = salesareaCode;
        this.salesareaName = salesareaName;
        this.salesCode = salesCode;
        this.salesName = salesName;
        this.bonus = bonus;
        this.status = status;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

    public String getSalesareaName() {
        return salesareaName;
    }

    public void setSalesareaName(String salesareaName) {
        this.salesareaName = salesareaName;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSiteLoc() {
        return siteLoc;
    }

    public void setSiteLoc(String siteLoc) {
        this.siteLoc = siteLoc;
    }

    public Integer getPosnr() {
        return posnr;
    }

    public void setPosnr(Integer posnr) {
        this.posnr = posnr;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getSapOrdernum() {
        return sapOrdernum;
    }

    public void setSapOrdernum(String sapOrdernum) {
        this.sapOrdernum = sapOrdernum;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcSalesarea getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(EcSalesarea salesareaId) {
        this.salesareaId = salesareaId;
    }

    public EcSales getSalesId() {
        return salesId;
    }

    public void setSalesId(EcSales salesId) {
        this.salesId = salesId;
    }

    public EcProduct getProductId() {
        return productId;
    }

    public void setProductId(EcProduct productId) {
        this.productId = productId;
    }

    public EcPlant getPlantId() {
        return plantId;
    }

    public void setPlantId(EcPlant plantId) {
        this.plantId = plantId;
    }

    public EcMember getMemberId() {
        return memberId;
    }

    public void setMemberId(EcMember memberId) {
        this.memberId = memberId;
    }

    public EcDeliveryPlace getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(EcDeliveryPlace deliveryId) {
        this.deliveryId = deliveryId;
    }

    public EcCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(EcCustomer customerId) {
        this.customerId = customerId;
    }

    public EcContract getContractId() {
        return contractId;
    }

    public void setContractId(EcContract contractId) {
        this.contractId = contractId;
    }

    public TcUser getApprover() {
        return approver;
    }

    public void setApprover(TcUser approver) {
        this.approver = approver;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof EcOrder)) {
            return false;
        }
        EcOrder other = (EcOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcOrder[ id=" + id + " ]";
    }

}
