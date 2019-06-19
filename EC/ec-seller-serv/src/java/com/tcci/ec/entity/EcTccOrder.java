/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_TCC_ORDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcTccOrder.findAll", query = "SELECT e FROM EcTccOrder e")
    , @NamedQuery(name = "EcTccOrder.findById", query = "SELECT e FROM EcTccOrder e WHERE e.id = :id")})
public class EcTccOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TCC_ORDER", sequenceName = "SEQ_TCC_ORDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TCC_ORDER")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_STORE_ID")
    private long srcStoreId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_MEMBER_ID")
    private long srcMemberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_ORDER_ID")
    private long srcOrderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_PRODUCT_ID")
    private long srcProductId;

    @Column(name = "TCC_ORDER_ID")
    private Long tccOrderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private long customerId;
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CUSTOMER_CODE")
    private String customerCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private long productId;
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
    @Column(name = "CONTRACT_ID")
    private Long contractId;
    @Size(max = 50)
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLANT_ID")
    private long plantId;
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
    @Column(name = "SALESAREA_ID")
    private long salesareaId;
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
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;
    @Size(max = 20)
    @Column(name = "DELIVERY_CODE")
    private String deliveryCode;
    @Size(max = 140)
    @Column(name = "DELIVERY_NAME")
    private String deliveryName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALES_ID")
    private long salesId;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "BONUS")
    private int bonus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 40)
    @Column(name = "SAP_ORDERNUM")
    private String sapOrdernum;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Size(max = 255)
    @Column(name = "MESSAGE")
    private String message;
    @Size(max = 50)
    @Column(name = "CONTRACT_NAME")
    private String contractName;
    @Size(max = 60)
    @Column(name = "SITE_LOC")
    private String siteLoc;
    @Column(name = "APPROVER")
    private Long approver;
    @Column(name = "APPROVAL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;
    @Column(name = "POSNR")
    private Integer posnr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_BUYER_ID")
    private long srcBuyerId;
    @Size(max = 3)
    @Column(name = "TRAN_MODE")
    private String tranMode;
    @Size(max = 3)
    @Column(name = "TRAN_TYPE")
    private String tranType;

    public EcTccOrder() {
    }

    public EcTccOrder(Long id) {
        this.id = id;
    }

    public String getTranMode() {
        return tranMode;
    }

    public void setTranMode(String tranMode) {
        this.tranMode = tranMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public Long getTccOrderId() {
        return tccOrderId;
    }

    public void setTccOrderId(Long tccOrderId) {
        this.tccOrderId = tccOrderId;
    }

    public long getSrcStoreId() {
        return srcStoreId;
    }

    public void setSrcStoreId(long srcStoreId) {
        this.srcStoreId = srcStoreId;
    }

    public long getSrcMemberId() {
        return srcMemberId;
    }

    public void setSrcMemberId(long srcMemberId) {
        this.srcMemberId = srcMemberId;
    }

    public long getSrcOrderId() {
        return srcOrderId;
    }

    public void setSrcOrderId(long srcOrderId) {
        this.srcOrderId = srcOrderId;
    }

    public long getSrcProductId() {
        return srcProductId;
    }

    public void setSrcProductId(long srcProductId) {
        this.srcProductId = srcProductId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(long salesareaId) {
        this.salesareaId = salesareaId;
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

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public long getSalesId() {
        return salesId;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSapOrdernum() {
        return sapOrdernum;
    }

    public void setSapOrdernum(String sapOrdernum) {
        this.sapOrdernum = sapOrdernum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSiteLoc() {
        return siteLoc;
    }

    public void setSiteLoc(String siteLoc) {
        this.siteLoc = siteLoc;
    }

    public Long getApprover() {
        return approver;
    }

    public void setApprover(Long approver) {
        this.approver = approver;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Integer getPosnr() {
        return posnr;
    }

    public void setPosnr(Integer posnr) {
        this.posnr = posnr;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getSrcBuyerId() {
        return srcBuyerId;
    }

    public void setSrcBuyerId(long srcBuyerId) {
        this.srcBuyerId = srcBuyerId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
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
        if (!(object instanceof EcTccOrder)) {
            return false;
        }
        EcTccOrder other = (EcTccOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcTccOrder[ id=" + id + " ]";
    }
    
}
