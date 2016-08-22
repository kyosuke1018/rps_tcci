package com.tcci.sksp.vo;

import com.tcci.sksp.entity.ar.SkProductCategory;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import java.util.List;

/**
 *
 * @author Neo.Fu
 */
public class QuotationGiftVO {

    private QuotationDetailVO detailVO;
    private SkQuotationGift gift;
    private String category;
    private List<SkProductMaster> productList;

    public QuotationDetailVO getDetailVO() {
        return detailVO;
    }

    public void setDetailVO(QuotationDetailVO detailVO) {
        this.detailVO = detailVO;
    }

    public SkQuotationGift getGift() {
        return gift;
    }

    public void setGift(SkQuotationGift gift) {
        this.gift = gift;
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
}
