/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class BaseCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    protected Long id;
    protected Long memberId;
    protected String loginAccount;
    protected Long sellerId;
    protected Long storeId;
    protected Long prdId;
    protected Long orderId;
    protected Long cartId;
    protected Long typeId;
    protected String type;
    protected String status;
    protected String keyword;
    protected String orderNumber;
    protected Boolean replyMsg;

    protected String orderStatus;
    protected String rfqStatus;
    protected String payStatus;
    protected String shipStatus;  
    protected String transactionType;// PAYMENT_TYPE

    protected String primaryType;
    protected Long primaryId;
    protected Long parent;

    protected Date startAt;
    protected Date endAt;

    protected List<Long> idList;
    protected List<Long> storeList;
    protected List<Long> prdList;
    protected List<Long> orderList;
    
    protected List<String> statusList;
    protected List<String> ignoreStatusList;
    
    protected boolean fullData;
    
    protected boolean groupByOnly;
    protected boolean forExport;
    
    protected String code;
    
    protected Boolean active;
    protected Boolean disabled;
    
    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = (typeId==null||typeId==0)?null:typeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getIgnoreStatusList() {
        return ignoreStatusList;
    }

    public void setIgnoreStatusList(List<String> ignoreStatusList) {
        this.ignoreStatusList = ignoreStatusList;
    }

    public String getRfqStatus() {
        return rfqStatus;
    }

    public void setRfqStatus(String rfqStatus) {
        this.rfqStatus = rfqStatus;
    }

    public boolean isGroupByOnly() {
        return groupByOnly;
    }

    public void setGroupByOnly(boolean groupByOnly) {
        this.groupByOnly = groupByOnly;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public boolean isForExport() {
        return forExport;
    }

    public void setForExport(boolean forExport) {
        this.forExport = forExport;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public boolean isFullData() {
        return fullData;
    }

    public void setFullData(boolean fullData) {
        this.fullData = fullData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public List<Long> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Long> storeList) {
        this.storeList = storeList;
    }

    public List<Long> getPrdList() {
        return prdList;
    }

    public void setPrdList(List<Long> prdList) {
        this.prdList = prdList;
    }

    public List<Long> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Long> orderList) {
        this.orderList = orderList;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Boolean getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(Boolean replyMsg) {
        this.replyMsg = replyMsg;
    }
    
}
