/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.enums.ec10.ShipMethodEC10Enum;
import com.tcci.ec.enums.ec10.TranModeEC10Enum;
import com.tcci.ec.enums.ec10.TranTypeEC10Enum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public class TccOrderVO implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Long id;
    
    private Long srcStoreId;
    private Long srcMemberId;
    private Long srcOrderId;
    private Long srcProductId;
    
    private Long tccOrderId;
    private Long memberId;
    private Long customerId;
    private String customerCode;
    private String customerName;
    
    private Long productId; // FK:EC_PRODUCT.ID
    private String productCode; // 料號
    private String productName; // 品名
    private BigDecimal unitPrice; // 單價(RMB)
    private BigDecimal quantity; // 數量
    private BigDecimal amount; // 總額
    private String vehicle; // 車號
    private String deliveryDate; // 提貨日
    private String method; // 廠交自提/工地交自提
    private Long contractId; // FK:EC_CONTRACT.ID
    private String contractCode; // 合約號碼
    private Long plantId; // FK:EC_PLANT.ID
    private String plantCode; // 廠代碼
    private String plantName; // 廠名稱
    private Long salesareaId; // FK:EC_SALESAREA.ID
    private String salesareaCode; // 銷售區代碼
    private String salesareaName; // 銷售區名稱
    private Long deliveryId; // FK:EC_DELIVERY_PLACE.ID
    private String deliveryCode; // 送達地點代碼
    private String deliveryName; // 送達地點名稱
    private Long salesId; // FK:EC_SALES.ID
    private String salesCode; // 業務員工號
    private String salesName; // 業務員名稱
    private Integer bonus; // 紅利
    private String status; // 狀態 (OPEN:未出貨, CANCEL: 取消, CLOSE:已出貨)
    private String sapOrdernum; // SAP訂單號碼
    private Date createtime; // 建立時間
    private String message; // 訊息
    private String contractName; // 合約名稱
    private String siteLoc; // 袋裝噴碼
    private Long approver; // 審核員(FK:TC_USER.ID)
    private Date approvalTime; // 審核時間
    private Integer posnr; // 合約項次
    
    private Long srcBuyerId;
    private String tranMode;
    private String tranType;
    
    private String tranModeName;
    private String tranTypeName;
    private String methodName;
    
    private String deliveryDateLabel;
    
    public void genDeliveryDateLabel(){
        if( deliveryDate!=null && deliveryDate.length()==8 ){
            deliveryDateLabel = deliveryDate.substring(0, 4)+"-"+deliveryDate.substring(4, 6)+"-"+deliveryDate.substring(6);
        }else{
            deliveryDateLabel = deliveryDate;
        }
    }
    
    public void genTranModeName(Locale locale){
        TranModeEC10Enum enum1 = TranModeEC10Enum.getFromCode(this.tranMode);
        tranModeName = enum1!=null?enum1.getDisplayName(locale):"";
    }

    public void genTranTypeName(Locale locale){
        TranTypeEC10Enum enum1 = TranTypeEC10Enum.getFromCode(this.tranType);
        tranTypeName = enum1!=null?enum1.getDisplayName(locale):"";
    }

    public void genMethodName(Locale locale){
        ShipMethodEC10Enum enum1 = ShipMethodEC10Enum.getFromCode(this.method);
        methodName = enum1!=null?enum1.getDisplayName(locale):"";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryDateLabel() {
        return deliveryDateLabel;
    }

    public void setDeliveryDateLabel(String deliveryDateLabel) {
        this.deliveryDateLabel = deliveryDateLabel;
    }

    public String getTranMode() {
        return tranMode;
    }

    public void setTranMode(String tranMode) {
        this.tranMode = tranMode;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranTypeName() {
        return tranTypeName;
    }

    public void setTranTypeName(String tranTypeName) {
        this.tranTypeName = tranTypeName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getTranModeName() {
        return tranModeName;
    }

    public void setTranModeName(String tranModeName) {
        this.tranModeName = tranModeName;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getSrcStoreId() {
        return srcStoreId;
    }

    public void setSrcStoreId(Long srcStoreId) {
        this.srcStoreId = srcStoreId;
    }

    public Long getSrcMemberId() {
        return srcMemberId;
    }

    public void setSrcMemberId(Long srcMemberId) {
        this.srcMemberId = srcMemberId;
    }

    public Long getSrcOrderId() {
        return srcOrderId;
    }

    public void setSrcOrderId(Long srcOrderId) {
        this.srcOrderId = srcOrderId;
    }

    public Long getSrcProductId() {
        return srcProductId;
    }

    public void setSrcProductId(Long srcProductId) {
        this.srcProductId = srcProductId;
    }

    public Long getTccOrderId() {
        return tccOrderId;
    }

    public void setTccOrderId(Long tccOrderId) {
        this.tccOrderId = tccOrderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
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

    public Long getSalesareaId() {
        return salesareaId;
    }

    public void setSalesareaId(Long salesareaId) {
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

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
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

    public Long getSrcBuyerId() {
        return srcBuyerId;
    }

    public void setSrcBuyerId(Long srcBuyerId) {
        this.srcBuyerId = srcBuyerId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TccOrderVO)) {
            return false;
        }
        TccOrderVO other = (TccOrderVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.TccOrderVO[ id=" + id + ", srcOrderId=" + srcOrderId +" ]";
    }
    
}
