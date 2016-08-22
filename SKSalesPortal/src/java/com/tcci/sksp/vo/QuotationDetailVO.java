/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class QuotationDetailVO {
    
    private int index;
    private SkQuotationDetail detail;
    private String category;
    private boolean gift;
    private String orderNumber;
    private List<SkProductMaster> productList;
    private List<QuotationGiftVO> giftVOList;
    
    public QuotationDetailVO(SkQuotationDetail detail) {
        this.gift = false;
        this.detail = detail;
        this.giftVOList = new ArrayList();
    }
    
    public QuotationDetailVO(SkQuotationGift gift) {
        this.gift = true;
        this.detail = new SkQuotationDetail();
        this.detail.setProductNumber(gift.getProductNumber());
        this.detail.setQuantity(gift.getQuantity());
        this.detail.setPrice(gift.getPrice());
        this.detail.setPremiumDiscount(gift.getQuantity().multiply(gift.getPrice()));
    }
    
    public SkQuotationDetail getDetail() {
        return detail;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public List<SkProductMaster> getProductList() {
        return productList;
    }
    
    public void setProductList(List<SkProductMaster> productList) {
        this.productList = productList;
    }
    
    public List<QuotationGiftVO> getGiftVOList() {
        return giftVOList;
    }
    
    public void setGiftVOList(List<QuotationGiftVO> giftVOList) {
        this.giftVOList = giftVOList;
    }
    
    public boolean isGift() {
        return gift;
    }
    
    public void setGift(boolean gift) {
        this.gift = gift;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
}
