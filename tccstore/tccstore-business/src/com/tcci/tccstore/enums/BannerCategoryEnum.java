/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.enums;

/**
 *
 * @author Jimmy.Lee
 */
public enum BannerCategoryEnum {

    HOME(1), // 首頁
    ADVERTISING(2), // 商品廣告證言
    PROMOTION(3), // 紅利促銷
    BONUS(4), // 紅利兌換活動
    GOLD(5), // 金幣兌換活動
    PRODUCT_USAGE(6), // 產品使用
    CAMPAIGN_TCC(7), // 台泥專屬APP活動
    CAMPAIGN_PARTNER(8),// 台泥伙伴活動
    ;

    private final int value;

    private BannerCategoryEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
