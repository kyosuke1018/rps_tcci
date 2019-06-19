/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public enum ValidateStrEnum {
    // String code, int cLen, int eLen, String label, boolean canEmpty
    EC_BULLETIN_CONTENT("EC_BULLETIN.CONTENT", 500, 500, "公告內容", false), //
    
    EC_COMPANY_TYPE("EC_COMPANY.TYPE", 6, 20, "類型", true),
    EC_COMPANY_CNAME("EC_COMPANY.CNAME", 300, 300, "中文名稱", false), //
    EC_COMPANY_ENAME("EC_COMPANY.ENAME", 100, 300, "英文名稱", true),
    EC_COMPANY_ID_TYPE("EC_COMPANY.ID_TYPE", 0, 2, "ID_CODE類別", true),
    EC_COMPANY_ID_CODE("EC_COMPANY.ID_CODE", 16, 50, "統編、營業登記號", false), //
    EC_COMPANY_NICKNAME("EC_COMPANY.NICKNAME", 100, 100, "簡稱", false), //
    EC_COMPANY_BRIEF("EC_COMPANY.BRIEF", 500, 500, "簡介", true),
    EC_COMPANY_EMAIL1("EC_COMPANY.EMAIL1", 33, 100, "電郵1", true),
    EC_COMPANY_EMAIL2("EC_COMPANY.EMAIL2", 33, 100, "電郵2", true),
    EC_COMPANY_EMAIL3("EC_COMPANY.EMAIL3", 33, 100, "電郵3", true),
    EC_COMPANY_TEL1("EC_COMPANY.TEL1", 10, 30, "電話1", true),
    EC_COMPANY_TEL2("EC_COMPANY.TEL2", 10, 30, "電話2", true),
    EC_COMPANY_TEL3("EC_COMPANY.TEL3", 10, 30, "電話3", true),
    EC_COMPANY_FAX1("EC_COMPANY.FAX1", 10, 30, "傳真1", true),
    EC_COMPANY_FAX2("EC_COMPANY.FAX2", 10, 30, "傳真2", true),
    EC_COMPANY_ADDR1("EC_COMPANY.ADDR1", 500, 500, "地址1", true),
    EC_COMPANY_ADDR2("EC_COMPANY.ADDR2", 500, 500, "地址2", true),
    EC_COMPANY_OWNER1("EC_COMPANY.OWNER1", 33, 100, "負責人1", true),
    EC_COMPANY_OWNER2("EC_COMPANY.OWNER2", 33, 100, "負責人1", true),
    EC_COMPANY_CONTACT1("EC_COMPANY.CONTACT1", 33, 100, "聯絡人1", true),
    EC_COMPANY_CONTACT2("EC_COMPANY.CONTACT2", 33, 100, "聯絡人2", true),
    EC_COMPANY_CONTACT3("EC_COMPANY.CONTACT3", 33, 100, "聯絡人3", true),
    EC_COMPANY_WEB_ID1("EC_COMPANY.WEB_ID1", 10, 30, "微信帳號", true),
    EC_COMPANY_WEB_ID2("EC_COMPANY.WEB_ID2", 10, 30, "社群帳號", true),
    EC_COMPANY_LONGITUDE("EC_COMPANY.LONGITUDE", 6, 20, "經度", true),
    EC_COMPANY_LATITUDE("EC_COMPANY.LATITUDE", 6, 20, "緯度", true),
    EC_COMPANY_URL1("EC_COMPANY.URL1", 340, 1024, "網址1", true),
    EC_COMPANY_URL2("EC_COMPANY.URL2", 340, 1024, "網址2", true),
    
    EC_COUNTRY_CNAME("EC_COUNTRY.CNAME", 33, 100, "中文名稱", false), //
    EC_COUNTRY_ENAME("EC_COUNTRY.ENAME", 33, 100, "英文名稱", true),
    EC_COUNTRY_SHOTNAME("EC_COUNTRY.SHOTNAME", 16, 50, "簡寫", true),
    
    EC_CREDITS_LOG_REASON("EC_CREDITS_LOG.REASON", 300, 300, "異動原因", true),
    
    EC_CURRENCY_CODE("EC_CURRENCY.CODE", 3, 10, "代碼", false), //
    EC_CURRENCY_NAME("EC_CURRENCY.NAME", 20, 60, "名稱", false), //
    
    EC_CUSTOMER_MEMO("EC_CUSTOMER.MEMO", 300, 300, "備註", true),
    
    EC_CUS_ADDR_ADDRESS("EC_CUS_ADDR.ADDRESS", 80, 80, "地址", false), //
    EC_CUS_ADDR_ADDRESS_2("EC_CUS_ADDR.ADDRESS_2", 80, 80, "地址2", true),
    EC_CUS_ADDR_TYPE("EC_CUS_ADDR.TYPE", 3, 10, "地址類型(Bill or Ship)", true),
    EC_CUS_ADDR_ALIAS("EC_CUS_ADDR.ALIAS", 40, 40, "別名", true),
    EC_CUS_ADDR_COMPANY("EC_CUS_ADDR.COMPANY", 40, 40, "公司", true),
    EC_CUS_ADDR_CITY("EC_CUS_ADDR.CITY", 40, 40, "城市", true),
    EC_CUS_ADDR_STATE_REGION("EC_CUS_ADDR.STATE_REGION", 40, 40, "州or區", true),
    EC_CUS_ADDR_COUNTRY("EC_CUS_ADDR.COUNTRY", 40, 40, "國家", true),
    EC_CUS_ADDR_POSTAL_CODE("EC_CUS_ADDR.POSTAL_CODE", 6, 20, "郵遞區號", true),
    EC_CUS_ADDR_FAX("EC_CUS_ADDR.FAX", 6, 20, "傳真", true),
    EC_CUS_ADDR_PHONE("EC_CUS_ADDR.PHONE", 6, 20, "電話", true),
    EC_CUS_ADDR_PHONE_EXTENSION("EC_CUS_ADDR.PHONE_EXTENSION", 6, 20, "分機", true),
    EC_CUS_ADDR_RECIPIENT("EC_CUS_ADDR.RECIPIENT", 40, 40, "收件人姓名", true),
    
    EC_CUS_FEEDBACK_CONTENT("EC_CUS_FEEDBACK.CONTENT", 1500, 1500, "反映內容", false), //
    EC_CUS_FEEDBACK_PROCESS("EC_CUS_FEEDBACK.PROCESS", 1500, 1500, "處理內容", true),
    
    EC_DEPARTMENT_CNAME("EC_DEPARTMENT.CNAME", 200, 200, "中文名稱", false), //
    EC_DEPARTMENT_ENAME("EC_DEPARTMENT.ENAME", 200, 200, "英文名稱", true),
    EC_DEPARTMENT_CODE("EC_DEPARTMENT.CODE", 33, 100, "代碼", true),
    EC_DEPARTMENT_MEMO("EC_DEPARTMENT.MEMO", 600, 600, "備註", true),
    
    EC_DISCOUNT_CNAME("EC_DISCOUNT.CNAME", 100, 100, "中文名稱", false), //
    EC_DISCOUNT_ENAME("EC_DISCOUNT.ENAME", 100, 100, "英文名稱", true),
    EC_DISCOUNT_CODE("EC_DISCOUNT.CODE", 100, 100, "代碼", true),
    EC_DISCOUNT_MEMO("EC_DISCOUNT.MEMO", 300, 300, "備註", true),
    
    EC_FILE_PRIMARY_TYPE("EC_FILE.PRIMARY_TYPE", 16, 50, "對應主關聯類別", false), //
    EC_FILE_NAME("EC_FILE.NAME", 600, 600, "顯示名稱", true),
    EC_FILE_DESCRIPTION("EC_FILE.DESCRIPTION", 600, 600, "說明", true),
    EC_FILE_FILENAME("EC_FILE.FILENAME", 600, 600, "原檔名", true),
    EC_FILE_SAVENAME("EC_FILE.SAVENAME", 16, 50, "儲存檔名", false), //
    EC_FILE_SAVEDIR("EC_FILE.SAVEDIR", 33, 100, "儲存路徑", true),
    EC_FILE_CONTENT_TYPE("EC_FILE.CONTENT_TYPE", 33, 100, "檔案類型", true),
    
    EC_FLOW_TEMPLATE_CNAME("EC_FLOW_TEMPLATE.CNAME", 300, 300, "中文名稱", false), //
    EC_FLOW_TEMPLATE_ENAME("EC_FLOW_TEMPLATE.ENAME", 100, 300, "英文名稱", true),

    EC_AD_MESSAGE("EC_AD.MESSAGE", 500, 500, "宣傳文字", true),
    
    EC_MEMBER_LOGIN_ACCOUNT("EC_MEMBER.LOGIN_ACCOUNT", 16, 50, "會員帳號", false), //
    EC_MEMBER_NAME("EC_MEMBER.NAME", 64, 64, "姓名", false), //
    EC_MEMBER_EMAIL("EC_MEMBER.EMAIL", 21, 64, "電子信箱", false),
    EC_MEMBER_PHONE("EC_MEMBER.PHONE", 16, 50, "電話", false), //
    EC_MEMBER_PASSWORD("EC_MEMBER.PASSWORD", 21, 64, "密碼(SHA256)", false), //
    EC_MEMBER_TYPE("EC_MEMBER.TYPE", 10, 30, "帳號類型", true), //(P:個人;C:公司)MemberTypeEnum
    
    EC_MEMBER_MSG_TYPE("EC_MEMBER_MSG.TYPE", 1, 3, "類別", true),
    EC_MEMBER_MSG_MESSAGE("EC_MEMBER_MSG.MESSAGE", 500, 500, "留言內容", false), //
    
    EC_OPTION_TYPE("EC_OPTION.TYPE", 10, 30, "類別", false), //
    EC_OPTION_CNAME("EC_OPTION.CNAME", 200, 200, "中文名稱", false), //
    EC_OPTION_ENAME("EC_OPTION.ENAME", 66, 200, "英文名稱", true),
    EC_OPTION_CODE("EC_OPTION.CODE", 33, 100, "代碼", true),
    EC_OPTION_MEMO("EC_OPTION.MEMO", 100, 300, "備註", true),
    
    EC_ORDER_ORDER_NUMBER("EC_ORDER.ORDER_NUMBER", 40, 40, "訂單編號", false), //
    //EC_ORDER_SHIPPING_DATE("EC_ORDER.SHIPPING_DATE", 2, 8, "出貨日", true),
    EC_ORDER_STATUS("EC_ORDER.STATUS", 6, 20, "狀態", false), //
    EC_ORDER_MESSAGE("EC_ORDER.MESSAGE", 255, 255, "訊息", true),
    //EC_ORDER_RECIPIENT("EC_ORDER.RECIPIENT", 40, 40, "收件人姓名", true),
    //EC_ORDER_ADDRESS("EC_ORDER.ADDRESS", 160, 160, "收件地址", true),
    //EC_ORDER_PHONE("EC_ORDER.PHONE", 6, 20, "收件人連絡電話", true),
    EC_ORDER_PAY_STATUS("EC_ORDER.PAY_STATUS", 6, 20, "付款狀態", true),
    EC_ORDER_SHIP_STATUS("EC_ORDER.SHIP_STATUS", 6, 20, "出貨狀態", true),
    EC_ORDER_PAYMENT_TYPE("EC_ORDER.PAYMENT_TYPE", 6, 20, "付款方式", true),
    
    EC_ORDER_LOG_EVENT_TYPE("EC_ORDER_LOG.EVENT_TYPE", 3, 10, "", true),
    EC_ORDER_LOG_MESSAGE("EC_ORDER_LOG.MESSAGE", 40, 120, "訊息", true),
    
    EC_ORDER_MESSAGE_MESSAGE("EC_ORDER_MESSAGE.MESSAGE", 120, 120, "訊息", false), //
    
    EC_ORDER_PROCESS_PROCESS("EC_ORDER_PROCESS.PROCESS", 1024, 1024, "處理內容", false), //
    
    EC_ORDER_RATE_CUSTOMER_RATE("EC_ORDER_RATE.CUSTOMER_RATE", 3, 10, "買方給評", true),
    EC_ORDER_RATE_CUSTOMER_MESSAGE("EC_ORDER_RATE.CUSTOMER_MESSAGE", 255, 255, "買方意見", true),
    EC_ORDER_RATE_SELLER_RATE("EC_ORDER_RATE.SELLER_RATE", 3, 10, "賣方給評", true),
    EC_ORDER_RATE_SELLER_MESSAGE("EC_ORDER_RATE.SELLER_MESSAGE", 255, 255, "賣方意見", true),
    
    EC_ORDER_SHIP_INFO_RECIPIENT("EC_ORDER_SHIP_INFO.RECIPIENT", 40, 40, "收件人姓名", false), //
    EC_ORDER_SHIP_INFO_ADDRESS("EC_ORDER_SHIP_INFO.ADDRESS", 160, 160, "收件地址", false), //
    EC_ORDER_SHIP_INFO_PHONE("EC_ORDER_SHIP_INFO.PHONE", 20, 20, "收件人連絡電話", false), //
    EC_ORDER_SHIP_INFO_CAR_NO("EC_ORDER_SHIP_INFO.CAR_NO", 20, 20, "車號", true),
    EC_ORDER_SHIP_INFO_DRIVER("EC_ORDER_SHIP_INFO.DRIVER", 40, 40, "司機", true),
    EC_ORDER_SHIP_INFO_SHIPPING_CODE("EC_ORDER_SHIP_INFO.SHIPPING_CODE", 16, 50, "物流編號", true),
    
    EC_PAGE_TEMPLATE_CNAME("EC_PAGE_TEMPLATE.CNAME", 300, 300, "中文名稱", false), //
    EC_PAGE_TEMPLATE_ENAME("EC_PAGE_TEMPLATE.ENAME", 100, 300, "英文名稱", true),
    
    EC_PAYMENT_TITLE("EC_PAYMENT.TITLE", 80, 80, "名稱", false), //
    EC_PAYMENT_MEMO("EC_PAYMENT.MEMO", 80, 80, "敘述", true),
    EC_PAYMENT_TYPE("EC_PAYMENT.TYPE", 3, 10, "類別", true), // (Credit Card or On Delivery)
    
    EC_PERSON_TYPE("EC_PERSON.TYPE", 6, 20, "類型", true),
    EC_PERSON_CNAME("EC_PERSON.CNAME", 200, 200, "中文名稱", false), //
    EC_PERSON_ENAME("EC_PERSON.ENAME", 66, 200, "英文名稱", true),
    EC_PERSON_ID_CODE("EC_PERSON.ID_CODE", 16, 50, "身分證ID或護照ID", true),
    EC_PERSON_NICKNAME("EC_PERSON.NICKNAME", 100, 100, "簡稱", false), //
    EC_PERSON_EMAIL1("EC_PERSON.EMAIL1", 33, 100, "電郵1", true),
    EC_PERSON_EMAIL2("EC_PERSON.EMAIL2", 33, 100, "電郵2", true),
    EC_PERSON_TEL1("EC_PERSON.TEL1", 10, 30, "電話1", true),
    EC_PERSON_TEL2("EC_PERSON.TEL2", 10, 30, "電話2", true),
    EC_PERSON_TEL3("EC_PERSON.TEL3", 10, 30, "電話3", true),
    EC_PERSON_FAX1("EC_PERSON.FAX1", 10, 30, "傳真", true),
    EC_PERSON_ADDR1("EC_PERSON.ADDR1", 500, 500, "地址1", true),
    EC_PERSON_ADDR2("EC_PERSON.ADDR2", 500, 500, "地址2", true),
    EC_PERSON_ID_TYPE("EC_PERSON.ID_TYPE", 0, 2, "ID_CODE類別", true),
    EC_PERSON_BRIEF("EC_PERSON.BRIEF", 500, 500, "簡介", true),
    EC_PERSON_GENDER("EC_PERSON.GENDER", 0, 2, "性別", true),
    EC_PERSON_URL1("EC_PERSON.URL1", 340, 1024, "網址1", true),
    EC_PERSON_URL2("EC_PERSON.URL2", 340, 1024, "網址2", true),
    
    EC_PRD_ATTR_CNAME("EC_PRD_ATTR.CNAME", 200, 200, "屬性名稱", false), //
    EC_PRD_ATTR_ENAME("EC_PRD_ATTR.ENAME", 200, 200, "英文名稱", true),
    EC_PRD_ATTR_MEMO("EC_PRD_ATTR.MEMO", 600, 600, "備註", true),
    EC_PRD_ATTR_DATA_TYPE("EC_PRD_ATTR.DATA_TYPE", 6, 20, "資料類型", true),
    EC_PRD_ATTR_VAL_ATTR_VALUE("EC_PRD_ATTR_VAL.ATTR_VALUE", 1500, 1500, "設定值", true),
    
    EC_PRD_DETAIL_CONTENT_TYPE("EC_PRD_DETAIL.CONTENT_TYPE", 1, 3, "內容樣式", false), //
    EC_PRD_DETAIL_CONTENT_TXT("EC_PRD_DETAIL.CONTENT_TXT", 2000, 2000, "文字內容", true),
    
    EC_PRD_INTRO_TEXT("EC_PRD_INTRO.TEXT", 600, 600, "商品簡述", false), //
    
    EC_PRD_PAYMENT_MEMO("EC_PRD_PAYMENT.MEMO", 300, 300, "備註", true),
    
    EC_PRD_SHIPPING_MEMO("EC_PRD_SHIPPING.MEMO", 300, 300, "備註", true),
    
    EC_PRD_TYPE_CNAME("EC_PRD_TYPE.CNAME", 200, 200, "分類名稱", false), //
    EC_PRD_TYPE_ENAME("EC_PRD_TYPE.ENAME", 200, 200, "英文名稱", true),
    EC_PRD_TYPE_CODE("EC_PRD_TYPE.CODE", 33, 100, "代碼", true),
    EC_PRD_TYPE_MEMO("EC_PRD_TYPE.MEMO", 600, 600, "備註", true),
    
    EC_PRD_VARIANT_CNAME("EC_PRD_VARIANT.CNAME", 100, 300, "商品型別名稱", false), //
    EC_PRD_VARIANT_ENAME("EC_PRD_VARIANT.ENAME", 100, 300, "英文名稱", true),
    EC_PRD_VARIANT_BARCODE("EC_PRD_VARIANT.BARCODE", 16, 50, "BAR CODE", true),
    EC_PRD_VARIANT_SKU("EC_PRD_VARIANT.SKU", 16, 50, "庫存單位(Stock Keeping Unit)", true),
    EC_PRD_VARIANT_VOLUME("EC_PRD_VARIANT.VOLUME", 200, 200, "體積", true),
    
    EC_PRD_VAR_OPTION_TYPE("EC_PRD_VAR_OPTION.TYPE", 1, 3, "型別類型", false), //
    EC_PRD_VAR_OPTION_CNAME("EC_PRD_VAR_OPTION.CNAME", 200, 200, "中文名稱", false), //
    EC_PRD_VAR_OPTION_ENAME("EC_PRD_VAR_OPTION.ENAME", 200, 200, "英文名稱", true),
    EC_PRD_VAR_OPTION_MEMO("EC_PRD_VAR_OPTION.MEMO", 600, 600, "備註", true),
    
    EC_PREREQUISTITE_IDS_TYPE("EC_PREREQUISTITE_IDS.TYPE", 3, 10, "類別", false), //
    
    EC_PRICE_RULE_TITLE("EC_PRICE_RULE.TITLE", 300, 300, "標題", false), //
    EC_PRICE_RULE_VALUE_TYPE("EC_PRICE_RULE.VALUE_TYPE", 1, 3, "值類別(A:amount, P:percentage)", true),
    EC_PRICE_RULE_TARGET_SELECT("EC_PRICE_RULE.TARGET_SELECT", 1, 3, "target selection (A: all, E :entitled)", true),
    EC_PRICE_RULE_TARGET_TYPE("EC_PRICE_RULE.TARGET_TYPE", 1, 3, "折扣金額對象 (I:訂購商品; S:運費)", true),
    EC_PRICE_RULE_ALLOCATION_METHOD("EC_PRICE_RULE.ALLOCATION_METHOD", 1, 3, "設定範圍方式 (E:each、A:across)", true),
    EC_PRICE_RULE_CUS_SEL("EC_PRICE_RULE.CUS_SEL", 1, 3, "customer selection  (A: all, E :prerequisite)", true),
    EC_PRICE_RULE_PRD_SEL("EC_PRICE_RULE.PRD_SEL", 1, 3, "product selection  (A: all, E :prerequisite)", true),
    EC_PRICE_RULE_PRD_SQL("EC_PRICE_RULE.PRD_SQL", 333, 1000, "商品複雜條件SQL", true),
    EC_PRICE_RULE_CUS_SQL("EC_PRICE_RULE.CUS_SQL", 333, 1000, "客戶複雜條件SQL", true),
    
    EC_PRODUCT_CNAME("EC_PRODUCT.CNAME", 200, 200, "中文名稱", false), //
    EC_PRODUCT_ENAME("EC_PRODUCT.ENAME", 200, 200, "英文名稱", true),
    EC_PRODUCT_CODE("EC_PRODUCT.CODE", 33, 100, "代碼", false), //
    EC_PRODUCT_STATUS("EC_PRODUCT.STATUS", 1, 3, "狀態", false), //
    EC_PRODUCT_PUBLISH_SCOPE("EC_PRODUCT.PUBLISH_SCOPE", 1, 3, "發佈範圍", true),
    EC_PRODUCT_VOLUME("EC_PRODUCT.VOLUME", 200, 200, "體積", true),
    EC_PRODUCT_BARCODE("EC_PRODUCT.BARCODE", 16, 50, "BAR CODE", true),
    EC_PRODUCT_SKU("EC_PRODUCT.SKU", 16, 50, "庫存單位(Stock Keeping Unit)", true),
    
    EC_PUSH_LOG_CATEGORY("EC_PUSH_LOG.CATEGORY", 6, 20, "", true),
    EC_PUSH_LOG_PUSH_TYPE("EC_PUSH_LOG.PUSH_TYPE", 6, 20, "TAG,ALIAS", true),
    EC_PUSH_LOG_TITLE("EC_PUSH_LOG.TITLE", 200, 200, "", true),
    EC_PUSH_LOG_ALERT("EC_PUSH_LOG.ALERT", 1000, 1000, "", true),
    EC_PUSH_LOG_AUDIENCE("EC_PUSH_LOG.AUDIENCE", 333, 1000, "", true),
    EC_PUSH_LOG_PUSH_RESULT("EC_PUSH_LOG.PUSH_RESULT", 333, 1000, "", true),
    
    EC_SELLER_TYPE("EC_SELLER.TYPE", 6, 20, "類型", true),
    
    EC_STORE_USER_MEMO("EC_STORE_USER.MEMO", 600, 600, "備註", true),
    
    EC_SERVICE_SERVICE("EC_SERVICE.SERVICE", 85, 255, "服務", true),
    EC_SERVICE_SERVICE_URL("EC_SERVICE.SERVICE_URL", 85, 255, "服務網址", true),
    EC_SERVICE_DESCRIPTION("EC_SERVICE.DESCRIPTION", 500, 500, "服務說明", true),
    
    EC_SHIPPING_TITLE("EC_SHIPPING.TITLE", 80, 80, "名稱", false), //
    EC_SHIPPING_MEMO("EC_SHIPPING.MEMO", 80, 80, "敘述", true),
    EC_SHIPPING_TYPE("EC_SHIPPING.TYPE", 3, 10, "類別", true),//(A:自行取貨;B:宅配)
    
    EC_STOCK_LOG("EC_STOCK_LOG.MEMO", 600, 600, "備註說明", true),
    
    EC_STORE_TYPE("EC_STORE.TYPE", 6, 20, "類型", true),
    EC_STORE_CNAME("EC_STORE.CNAME", 300, 300, "中文名稱", true),
    EC_STORE_ENAME("EC_STORE.ENAME", 100, 300, "英文名稱", true),
    EC_STORE_BRIEF("EC_STORE.BRIEF", 1500, 1500, "簡介", true),
    EC_STORE_REMIT_ACCOUNT("EC_STORE.REMIT_ACCOUNT", 33, 100, "匯款帳戶", true),
    
    EC_VENDOR_TYPE("EC_VENDOR.TYPE", 6, 20, "類型", true),
    EC_VENDOR_CNAME("EC_VENDOR.CNAME", 300, 300, "中文名稱", false), //
    EC_VENDOR_ENAME("EC_VENDOR.ENAME", 100, 300, "英文名稱", true),
    EC_VENDOR_ID_TYPE("EC_VENDOR.ID_TYPE", 0, 2, "ID_CODE類別", true),
    EC_VENDOR_ID_CODE("EC_VENDOR.ID_CODE", 16, 50, "統編、營業登記號", true),
    EC_VENDOR_NICKNAME("EC_VENDOR.NICKNAME", 100, 100, "簡稱", true),
    EC_VENDOR_BRIEF("EC_VENDOR.BRIEF", 500, 500, "簡介", true),
    EC_VENDOR_EMAIL1("EC_VENDOR.EMAIL1", 33, 100, "電郵1", true),
    EC_VENDOR_EMAIL2("EC_VENDOR.EMAIL2", 33, 100, "電郵2", true),
    EC_VENDOR_EMAIL3("EC_VENDOR.EMAIL3", 33, 100, "電郵3", true),
    EC_VENDOR_TEL1("EC_VENDOR.TEL1", 10, 30, "電話1", true),
    EC_VENDOR_TEL2("EC_VENDOR.TEL2", 10, 30, "電話2", true),
    EC_VENDOR_TEL3("EC_VENDOR.TEL3", 10, 30, "電話3", true),
    EC_VENDOR_FAX1("EC_VENDOR.FAX1", 10, 30, "傳真1", true),
    EC_VENDOR_FAX2("EC_VENDOR.FAX2", 10, 30, "傳真2", true),
    EC_VENDOR_ADDR1("EC_VENDOR.ADDR1", 500, 500, "地址1", true),
    EC_VENDOR_ADDR2("EC_VENDOR.ADDR2", 500, 500, "地址2", true),
    EC_VENDOR_CODE("EC_VENDOR.CODE", 16, 50, "供應商編碼", true);

    private String code;
    private int cLen;// 中文最大長度
    private int eLen;// 英文最大長度
    private String label;
    private boolean canEmpty;// 不可空白
    
    ValidateStrEnum(String code, int cLen, int eLen, String label, boolean canEmpty){
        this.code = code;
        this.cLen = cLen;
        this.eLen = eLen;
        this.label = label;
        this.canEmpty = canEmpty;
    }
    public static ValidateStrEnum getFromCode(String code){
        for (ValidateStrEnum enum1 : ValidateStrEnum.values()) {
            if( code!=null && enum1.getCode().equals(code) ) {
                return enum1;
            }
        }
        return null; // default
    }
    
    /**
     * 顯示名稱 (取自enum.properties => [class name].[enum name])
     * @return 
     */
    public String getDisplayName(){
        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = label;
        }
        return res;
    }
    public String getDisplayName(Locale locale){
        String res = ResourceBundleUtils.getDisplayName(locale, GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
        if( res==null ){
            res = label;
        }
        return res;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int geteLen() {
        return eLen;
    }

    public void seteLen(int eLen) {
        this.eLen = eLen;
    }

    public int getcLen() {
        return cLen;
    }

    public void setcLen(int cLen) {
        this.cLen = cLen;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCanEmpty() {
        return canEmpty;
    }

    public void setCanEmpty(boolean canEmpty) {
        this.canEmpty = canEmpty;
    }

}
