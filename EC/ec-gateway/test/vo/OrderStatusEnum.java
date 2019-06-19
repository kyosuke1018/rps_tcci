/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vo;

/**
 *
 * @author Neo.Fu
 */
public enum OrderStatusEnum {
    
    Unknown("Unknown", "購物車"),      // 未成立(購物車)
    Inquiry("Inquiry", "買方詢價"),   //買方詢價
    quotation("quotation", "賣方報價"), //賣方報價
    Pending("Pending", "待賣方確認"),  // 待確認 買方送出 待賣方確認
    Approve("Approve", "已核准"), // 已核准
    Declined("Declined", "賣方拒絕"), // 賣方拒絕
//    Paid("Paid", "已付款"),    // 已付款  賣方確認
    Authorized("Authorized", "已授權"),//已授權
//    Shipped("Shipped", "已出貨"),      // 已出貨  賣方確認
    Cancelled("Cancelled", "取消"), // 取消
    Deleted("Deleted", "刪除"),  //刪除
    Returned("Returned", "退回"),     //退回
    Close("Close", "結案");     //結案  到貨
    
    private String code;
    private String name;
    
    OrderStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
    
    public static OrderStatusEnum fromCode(String code) {
        if (code != null) {
            for (OrderStatusEnum thisEnum : OrderStatusEnum.values()) {
                if (code.equals(thisEnum.getCode())) {
                    return thisEnum;
                }
            }
        }
        return null;
    }   

    public static OrderStatusEnum fromString(String key) {
        OrderStatusEnum result = null;
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.toString().equals(key)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
