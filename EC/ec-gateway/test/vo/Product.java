/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class Product {
    private Long id;
    private Store store;
    private String cname;
    private BigDecimal price;
    private String code;
    private List<String> imageList;
    private PrdType type;
    private boolean favorite;
    private String prdUnit;//PRICE_UNIT 計價單位

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public PrdType getType() {
        return type;
    }

    public void setType(PrdType type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getPrdUnit() {
        return prdUnit;
    }

    public void setPrdUnit(String prdUnit) {
        this.prdUnit = prdUnit;
    }
    
    
}
