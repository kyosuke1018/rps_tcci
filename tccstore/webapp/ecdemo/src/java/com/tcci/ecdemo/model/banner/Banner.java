/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.model.banner;

/**
 *
 * @author Jimmy.Lee
 */
public class Banner {
    public final static int CAT_HOME = 1; // 首頁
    public final static int CAT_ADVERTISING = 2; // 商品廣告證言
    public final static int CAT_PROMOTION = 3; // 促銷訊息
    public final static int CAT_BONUS = 4; // 紅利兌換活動
    public final static int CAT_REWARD = 5; // 金幣兌換活動
            
    private int category;
    private String contentUrl;
    private String link;

    public Banner() {
    }

    public Banner(int category, String contentUrl, String link) {
        this.category = category;
        this.contentUrl = contentUrl;
        this.link = link;
    }

    // getter, setter

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
