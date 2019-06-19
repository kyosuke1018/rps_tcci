/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.StrOptionVO;
import java.util.List;

/**
 *
 * @author Peter.pan
 * 
 */
public class OptionsMapVO extends BaseResponseVO {
    // Enum
    private List<StrOptionVO> prdStatus;
    private List<StrOptionVO> orderStatus;
    private List<StrOptionVO> rfqStatus;
    private List<StrOptionVO> payStatus;
    private List<StrOptionVO> shipStatus;
    private List<StrOptionVO> memberTypeStatus;
    private List<StrOptionVO> storeType;
    private List<StrOptionVO> orderSrc;
    // 獨立 Table
    private PrdTypeTreeVO prdTypeTree;
    private List<LongOptionVO> prdColor;
    private List<LongOptionVO> prdSize;
    private List<LongOptionVO> shipping;
    private List<LongOptionVO> payment;
    private List<LongOptionVO> taxType;
    private List<LongOptionVO> sellerVendor;
    // EC_OPTION
    private List<LongOptionVO> prdBrand;
    private List<LongOptionVO> paymentGateway;
    private List<LongOptionVO> cardType;
    private List<LongOptionVO> payThirdParty;
    private List<LongOptionVO> weightUnit;
    private List<LongOptionVO> cusLevel;
    
    public PrdTypeTreeVO getPrdTypeTree() {
        return prdTypeTree;
    }

    public void setPrdTypeTree(PrdTypeTreeVO prdTypeTree) {
        this.prdTypeTree = prdTypeTree;
    }

    public List<StrOptionVO> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<StrOptionVO> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<StrOptionVO> getRfqStatus() {
        return rfqStatus;
    }

    public void setRfqStatus(List<StrOptionVO> rfqStatus) {
        this.rfqStatus = rfqStatus;
    }

    public List<StrOptionVO> getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(List<StrOptionVO> payStatus) {
        this.payStatus = payStatus;
    }

    public List<StrOptionVO> getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(List<StrOptionVO> shipStatus) {
        this.shipStatus = shipStatus;
    }

    public List<StrOptionVO> getPrdStatus() {
        return prdStatus;
    }

    public void setPrdStatus(List<StrOptionVO> prdStatus) {
        this.prdStatus = prdStatus;
    }

    public List<StrOptionVO> getMemberTypeStatus() {
        return memberTypeStatus;
    }

    public void setMemberTypeStatus(List<StrOptionVO> memberTypeStatus) {
        this.memberTypeStatus = memberTypeStatus;
    }

    public List<StrOptionVO> getStoreType() {
        return storeType;
    }

    public void setStoreType(List<StrOptionVO> storeType) {
        this.storeType = storeType;
    }

    public List<StrOptionVO> getOrderSrc() {
        return orderSrc;
    }

    public void setOrderSrc(List<StrOptionVO> orderSrc) {
        this.orderSrc = orderSrc;
    }

    public List<LongOptionVO> getPrdColor() {
        return prdColor;
    }

    public void setPrdColor(List<LongOptionVO> prdColor) {
        this.prdColor = prdColor;
    }

    public List<LongOptionVO> getPrdSize() {
        return prdSize;
    }

    public void setPrdSize(List<LongOptionVO> prdSize) {
        this.prdSize = prdSize;
    }

    public List<LongOptionVO> getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(List<LongOptionVO> cusLevel) {
        this.cusLevel = cusLevel;
    }

    public List<LongOptionVO> getShipping() {
        return shipping;
    }

    public void setShipping(List<LongOptionVO> shipping) {
        this.shipping = shipping;
    }

    public List<LongOptionVO> getPayment() {
        return payment;
    }

    public void setPayment(List<LongOptionVO> payment) {
        this.payment = payment;
    }

    public List<LongOptionVO> getTaxType() {
        return taxType;
    }

    public void setTaxType(List<LongOptionVO> taxType) {
        this.taxType = taxType;
    }

    public List<LongOptionVO> getSellerVendor() {
        return sellerVendor;
    }

    public void setSellerVendor(List<LongOptionVO> sellerVendor) {
        this.sellerVendor = sellerVendor;
    }

    public List<LongOptionVO> getPrdBrand() {
        return prdBrand;
    }

    public void setPrdBrand(List<LongOptionVO> prdBrand) {
        this.prdBrand = prdBrand;
    }

    public List<LongOptionVO> getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(List<LongOptionVO> paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public List<LongOptionVO> getCardType() {
        return cardType;
    }

    public void setCardType(List<LongOptionVO> cardType) {
        this.cardType = cardType;
    }

    public List<LongOptionVO> getPayThirdParty() {
        return payThirdParty;
    }

    public void setPayThirdParty(List<LongOptionVO> payThirdParty) {
        this.payThirdParty = payThirdParty;
    }

    public List<LongOptionVO> getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(List<LongOptionVO> weightUnit) {
        this.weightUnit = weightUnit;
    }
    
}
