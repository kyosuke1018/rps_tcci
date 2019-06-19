/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import java.util.List;

/**
 *
 * @author Peter.pan
 * 
 */
public class OptionsMapVO extends BaseResponseVO {
    // Enum
    private List<StrOptionVO> prdStatus;
    private List<StrOptionVO> prdStatusSeller;
    private List<StrOptionVO> prdStatusAdmin;
    private List<StrOptionVO> orderStatus;
    private List<StrOptionVO> rfqStatus;
    private List<StrOptionVO> payStatus;
    private List<StrOptionVO> shipStatus;
    private List<StrOptionVO> memberTypeStatus;
    private List<StrOptionVO> storeType;
    private List<StrOptionVO> orderSrc;
    private List<StrOptionVO> stockLogType;
    private List<StrOptionVO> shippingSys;
    private List<StrOptionVO> paymentSys;
    private List<StrOptionVO> creditStatus;
    // 獨立 Table
    private PrdTypeTreeVO prdTypeTree;
    private List<LongOptionVO> stores;
    private List<LongOptionVO> products;
    private List<LongOptionVO> prdColor;
    private List<LongOptionVO> prdSize;
    private List<LongOptionVO> shipping;
    private List<LongOptionVO> payment;
    private List<LongOptionVO> currency;
    private List<LongOptionVO> taxType;
    private List<LongOptionVO> sellerVendor;
    private List<LongOptionVO> dealers;
    // EC_OPTION
    private List<LongOptionVO> prdBrand;
    private List<LongOptionVO> paymentGateway;
    private List<LongOptionVO> cardType;
    private List<LongOptionVO> payThirdParty;
    private List<LongOptionVO> prdUnit;
    private List<LongOptionVO> weightUnit;
    private List<LongOptionVO> cusLevel;
    private List<LongOptionVO> cusFeedback;
    private List<LongOptionVO> salesArea;
    private List<LongOptionVO> industry;
    
    // for EC1.0
    private List<StrOptionVO> deliveryDateEC10;// 提貨日期
    // enums
    private List<StrOptionVO> tranTypeEC10;// 交易類型
    private List<StrOptionVO> shipMethodEC10;// 提貨方式    
    // 獨立 Table
    private List<LongOptionVO> customerEC10;// 下單客戶
    private List<LongOptionVO> contractEC10;// 合約
    private List<LongOptionVO> salesAreaEC10;// 銷售地區
    private List<LongOptionVO> plantEC10;// 出貨廠
    private List<LongOptionVO> salesEC10;// 業務
    
     
    private List<StrOptionVO> provinceEC10;// 省    
    private List<StrOptionVO> cityEC10;// 市    
    private List<StrOptionVO> districtEC10;// 區   
    private List<StrOptionVO> townEC10;// 鎮      
    
    private List<StrOptionVO> combineOptionsEC10;// for 併單
    private OrderVO selectedOrder;// for 併單

    // 常用設定
    private List<LongOptionVO> cusAddrEC10;// 送達地點
    private List<StrOptionVO> favCars;// 車號

    public List<StrOptionVO> getFavCars() {
        return favCars;
    }

    public void setFavCars(List<StrOptionVO> favCars) {
        this.favCars = favCars;
    }
    
    public OrderVO getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(OrderVO selectedOrder) {
        this.selectedOrder = selectedOrder;
    }
    
    public PrdTypeTreeVO getPrdTypeTree() {
        return prdTypeTree;
    }

    public List<LongOptionVO> getCusAddrEC10() {
        return cusAddrEC10;
    }

    public void setCusAddrEC10(List<LongOptionVO> cusAddrEC10) {
        this.cusAddrEC10 = cusAddrEC10;
    }

    public List<StrOptionVO> getDeliveryDateEC10() {
        return deliveryDateEC10;
    }

    public List<StrOptionVO> getCombineOptionsEC10() {
        return combineOptionsEC10;
    }

    public void setCombineOptionsEC10(List<StrOptionVO> combineOptionsEC10) {
        this.combineOptionsEC10 = combineOptionsEC10;
    }

    public void setDeliveryDateEC10(List<StrOptionVO> deliveryDateEC10) {
        this.deliveryDateEC10 = deliveryDateEC10;
    }

    public void setPrdTypeTree(PrdTypeTreeVO prdTypeTree) {
        this.prdTypeTree = prdTypeTree;
    }

    public List<StrOptionVO> getTranTypeEC10() {
        return tranTypeEC10;
    }

    public void setTranTypeEC10(List<StrOptionVO> tranTypeEC10) {
        this.tranTypeEC10 = tranTypeEC10;
    }

    public List<StrOptionVO> getShipMethodEC10() {
        return shipMethodEC10;
    }

    public void setShipMethodEC10(List<StrOptionVO> shipMethodEC10) {
        this.shipMethodEC10 = shipMethodEC10;
    }

    public List<LongOptionVO> getCustomerEC10() {
        return customerEC10;
    }

    public void setCustomerEC10(List<LongOptionVO> customerEC10) {
        this.customerEC10 = customerEC10;
    }

    public List<LongOptionVO> getContractEC10() {
        return contractEC10;
    }

    public void setContractEC10(List<LongOptionVO> contractEC10) {
        this.contractEC10 = contractEC10;
    }

    public List<LongOptionVO> getSalesAreaEC10() {
        return salesAreaEC10;
    }

    public void setSalesAreaEC10(List<LongOptionVO> salesAreaEC10) {
        this.salesAreaEC10 = salesAreaEC10;
    }

    public List<LongOptionVO> getPlantEC10() {
        return plantEC10;
    }

    public void setPlantEC10(List<LongOptionVO> plantEC10) {
        this.plantEC10 = plantEC10;
    }

    public List<LongOptionVO> getSalesEC10() {
        return salesEC10;
    }

    public void setSalesEC10(List<LongOptionVO> salesEC10) {
        this.salesEC10 = salesEC10;
    }

    public List<StrOptionVO> getProvinceEC10() {
        return provinceEC10;
    }

    public void setProvinceEC10(List<StrOptionVO> provinceEC10) {
        this.provinceEC10 = provinceEC10;
    }

    public List<StrOptionVO> getCityEC10() {
        return cityEC10;
    }

    public void setCityEC10(List<StrOptionVO> cityEC10) {
        this.cityEC10 = cityEC10;
    }

    public List<StrOptionVO> getDistrictEC10() {
        return districtEC10;
    }

    public void setDistrictEC10(List<StrOptionVO> districtEC10) {
        this.districtEC10 = districtEC10;
    }

    public List<StrOptionVO> getTownEC10() {
        return townEC10;
    }

    public void setTownEC10(List<StrOptionVO> townEC10) {
        this.townEC10 = townEC10;
    }

    public List<LongOptionVO> getDealers() {
        return dealers;
    }

    public void setDealers(List<LongOptionVO> dealers) {
        this.dealers = dealers;
    }

    public List<StrOptionVO> getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(List<StrOptionVO> creditStatus) {
        this.creditStatus = creditStatus;
    }

    public List<LongOptionVO> getIndustry() {
        return industry;
    }

    public void setIndustry(List<LongOptionVO> industry) {
        this.industry = industry;
    }

    public List<LongOptionVO> getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(List<LongOptionVO> salesArea) {
        this.salesArea = salesArea;
    }

    public List<LongOptionVO> getCusFeedback() {
        return cusFeedback;
    }

    public void setCusFeedback(List<LongOptionVO> cusFeedback) {
        this.cusFeedback = cusFeedback;
    }

    public List<StrOptionVO> getShippingSys() {
        return shippingSys;
    }

    public void setShippingSys(List<StrOptionVO> shippingSys) {
        this.shippingSys = shippingSys;
    }

    public List<StrOptionVO> getPaymentSys() {
        return paymentSys;
    }

    public void setPaymentSys(List<StrOptionVO> paymentSys) {
        this.paymentSys = paymentSys;
    }

    public List<StrOptionVO> getStockLogType() {
        return stockLogType;
    }

    public void setStockLogType(List<StrOptionVO> stockLogType) {
        this.stockLogType = stockLogType;
    }

    public List<LongOptionVO> getPrdUnit() {
        return prdUnit;
    }

    public void setPrdUnit(List<LongOptionVO> prdUnit) {
        this.prdUnit = prdUnit;
    }

    public List<LongOptionVO> getStores() {
        return stores;
    }

    public void setStores(List<LongOptionVO> stores) {
        this.stores = stores;
    }

    public List<LongOptionVO> getCurrency() {
        return currency;
    }

    public void setCurrency(List<LongOptionVO> currency) {
        this.currency = currency;
    }

    public List<StrOptionVO> getPrdStatusSeller() {
        return prdStatusSeller;
    }

    public void setPrdStatusSeller(List<StrOptionVO> prdStatusSeller) {
        this.prdStatusSeller = prdStatusSeller;
    }

    public List<StrOptionVO> getPrdStatusAdmin() {
        return prdStatusAdmin;
    }

    public void setPrdStatusAdmin(List<StrOptionVO> prdStatusAdmin) {
        this.prdStatusAdmin = prdStatusAdmin;
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

    public List<LongOptionVO> getProducts() {
        return products;
    }

    public void setProducts(List<LongOptionVO> products) {
        this.products = products;
    }
    
}
