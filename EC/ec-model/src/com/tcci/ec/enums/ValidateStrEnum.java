/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.enums;

//import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.fc.util.ResourceBundleUtils;

/**
 *
 * @author Peter.pan
 */
public enum ValidateStrEnum {
    EC_BULLETIN_CONTENT("EC_BULLETIN.CONTENT", 500, 500, "公告內容"),
    
    EC_COMPANY_TYPE("EC_COMPANY.TYPE", 6, 20, "類型"),
    EC_COMPANY_CNAME("EC_COMPANY.CNAME", 300, 300, "中文名稱"),
    EC_COMPANY_ENAME("EC_COMPANY.ENAME", 100, 300, "英文名稱"),
    EC_COMPANY_ID_TYPE("EC_COMPANY.ID_TYPE", 0, 2, "ID_CODE類別"),
    EC_COMPANY_ID_CODE("EC_COMPANY.ID_CODE", 16, 50, "統編、營業登記號"),
    EC_COMPANY_NICKNAME("EC_COMPANY.NICKNAME", 100, 100, "簡稱"),
    EC_COMPANY_BRIEF("EC_COMPANY.BRIEF", 500, 500, "簡介"),
    EC_COMPANY_EMAIL1("EC_COMPANY.EMAIL1", 33, 100, "電郵1"),
    EC_COMPANY_EMAIL2("EC_COMPANY.EMAIL2", 33, 100, "電郵2"),
    EC_COMPANY_EMAIL3("EC_COMPANY.EMAIL3", 33, 100, "電郵3"),
    EC_COMPANY_TEL1("EC_COMPANY.TEL1", 10, 30, "電話1"),
    EC_COMPANY_TEL2("EC_COMPANY.TEL2", 10, 30, "電話2"),
    EC_COMPANY_TEL3("EC_COMPANY.TEL3", 10, 30, "電話3"),
    EC_COMPANY_FAX1("EC_COMPANY.FAX1", 10, 30, "傳真1"),
    EC_COMPANY_FAX2("EC_COMPANY.FAX2", 10, 30, "傳真2"),
    EC_COMPANY_ADDR1("EC_COMPANY.ADDR1", 500, 500, "地址1"),
    EC_COMPANY_ADDR2("EC_COMPANY.ADDR2", 500, 500, "地址2"),
    EC_COMPANY_OWNER1("EC_COMPANY.OWNER1", 33, 100, "負責人1"),
    EC_COMPANY_OWNER2("EC_COMPANY.OWNER2", 33, 100, "負責人1"),
    EC_COMPANY_CONTACT1("EC_COMPANY.CONTACT1", 33, 100, "聯絡人1"),
    EC_COMPANY_CONTACT2("EC_COMPANY.CONTACT2", 33, 100, "聯絡人2"),
    EC_COMPANY_CONTACT3("EC_COMPANY.CONTACT3", 33, 100, "聯絡人3"),
    EC_COMPANY_WEB_ID1("EC_COMPANY.WEB_ID1", 10, 30, "微信帳號"),
    EC_COMPANY_WEB_ID2("EC_COMPANY.WEB_ID2", 10, 30, "社群帳號"),
    EC_COMPANY_LONGITUDE("EC_COMPANY.LONGITUDE", 6, 20, "經度"),
    EC_COMPANY_LATITUDE("EC_COMPANY.LATITUDE", 6, 20, "緯度"),
    EC_COMPANY_URL1("EC_COMPANY.URL1", 340, 1024, "網址1"),
    EC_COMPANY_URL2("EC_COMPANY.URL2", 340, 1024, "網址2"),
    
    EC_COUNTRY_CNAME("EC_COUNTRY.CNAME", 33, 100, "主語系名稱"),
    EC_COUNTRY_ENAME("EC_COUNTRY.ENAME", 33, 100, "英文名稱"),
    EC_COUNTRY_SHOTNAME("EC_COUNTRY.SHOTNAME", 16, 50, "簡寫"),
    
    EC_CURRENCY_CODE("EC_CURRENCY.CODE", 3, 10, "代碼"),
    EC_CURRENCY_NAME("EC_CURRENCY.NAME", 20, 60, "名稱"),
    
    EC_CUSTOMER_SHORTNAME("EC_CUSTOMER.SHORTNAME", 64, 64, "簡稱"),
    
    EC_CUS_ADDR_ADDRESS("EC_CUS_ADDR.ADDRESS", 80, 80, "地址"),
    EC_CUS_ADDR_ADDRESS_2("EC_CUS_ADDR.ADDRESS_2", 80, 80, "地址2"),
    EC_CUS_ADDR_TYPE("EC_CUS_ADDR.TYPE", 3, 10, "地址類型(Bill or Ship)"),
    EC_CUS_ADDR_ALIAS("EC_CUS_ADDR.ALIAS", 40, 40, "別名"),
    EC_CUS_ADDR_COMPANY("EC_CUS_ADDR.COMPANY", 40, 40, "公司"),
    EC_CUS_ADDR_CITY("EC_CUS_ADDR.CITY", 40, 40, "城市"),
    EC_CUS_ADDR_STATE_REGION("EC_CUS_ADDR.STATE_REGION", 40, 40, "州or區"),
    EC_CUS_ADDR_COUNTRY("EC_CUS_ADDR.COUNTRY", 40, 40, "國家"),
    EC_CUS_ADDR_POSTAL_CODE("EC_CUS_ADDR.POSTAL_CODE", 6, 20, "郵遞區號"),
    EC_CUS_ADDR_FAX("EC_CUS_ADDR.FAX", 6, 20, "傳真"),
    EC_CUS_ADDR_PHONE("EC_CUS_ADDR.PHONE", 6, 20, "電話"),
    EC_CUS_ADDR_PHONE_EXTENSION("EC_CUS_ADDR.PHONE_EXTENSION", 6, 20, "分機"),
    EC_CUS_ADDR_RECIPIENT("EC_CUS_ADDR.RECIPIENT", 40, 40, "收件人姓名"),
    
    EC_CUS_FEEDBACK_CONTENT("EC_CUS_FEEDBACK.CONTENT", 1500, 1500, "反映內容"),
    EC_CUS_FEEDBACK_PROCESS("EC_CUS_FEEDBACK.PROCESS", 1500, 1500, "處理內容"),
    
    EC_DEPARTMENT_CNAME("EC_DEPARTMENT.CNAME", 200, 200, "主語系名稱"),
    EC_DEPARTMENT_ENAME("EC_DEPARTMENT.ENAME", 200, 200, "英文名稱"),
    EC_DEPARTMENT_CODE("EC_DEPARTMENT.CODE", 33, 100, "代碼"),
    EC_DEPARTMENT_MEMO("EC_DEPARTMENT.MEMO", 600, 600, "備註"),
    
    EC_DISCOUNT_CNAME("EC_DISCOUNT.CNAME", 100, 100, "主語系名稱"),
    EC_DISCOUNT_ENAME("EC_DISCOUNT.ENAME", 100, 100, "英文名稱"),
    EC_DISCOUNT_CODE("EC_DISCOUNT.CODE", 100, 100, "代碼"),
    EC_DISCOUNT_MEMO("EC_DISCOUNT.MEMO", 300, 300, "備註"),
    
    EC_ENCRYPT_SALT("EC_ENCRYPT.SALT", 85, 256, "salt"),
    EC_ENCRYPT_IV("EC_ENCRYPT.IV", 85, 256, "iv"),
    EC_ENCRYPT_PASS_PHRASE("EC_ENCRYPT.PASS_PHRASE", 170, 512, "passphrase"),
    
    EC_FILE_PRIMARY_TYPE("EC_FILE.PRIMARY_TYPE", 16, 50, "對應主關聯類別(Class Name or FileEnum)"),
    EC_FILE_NAME("EC_FILE.NAME", 600, 600, "顯示名稱"),
    EC_FILE_DESCRIPTION("EC_FILE.DESCRIPTION", 600, 600, "說明"),
    EC_FILE_FILENAME("EC_FILE.FILENAME", 600, 600, "原檔名"),
    EC_FILE_SAVENAME("EC_FILE.SAVENAME", 16, 50, "儲存檔名"),
    EC_FILE_SAVEDIR("EC_FILE.SAVEDIR", 33, 100, "儲存路徑"),
    EC_FILE_CONTENT_TYPE("EC_FILE.CONTENT_TYPE", 33, 100, "檔案類型"),
    
    EC_FLOW_TEMPLATE_CNAME("EC_FLOW_TEMPLATE.CNAME", 300, 300, "中文名稱"),
    EC_FLOW_TEMPLATE_ENAME("EC_FLOW_TEMPLATE.ENAME", 100, 300, "英文名稱"),

    EC_AD_MESSAGE("EC_AD.MESSAGE", 500, 500, "宣傳文字"),
    
    EC_MEMBER_LOGIN_ACCOUNT("EC_MEMBER.LOGIN_ACCOUNT", 16, 50, "登入帳號"),
    EC_MEMBER_NAME("EC_MEMBER.NAME", 64, 64, "姓名"),
    EC_MEMBER_EMAIL("EC_MEMBER.EMAIL", 21, 64, "電子信箱"),
    EC_MEMBER_PHONE("EC_MEMBER.PHONE", 16, 50, "電話"),
    EC_MEMBER_PASSWORD("EC_MEMBER.PASSWORD", 21, 64, "密碼(SHA256)"),
    EC_MEMBER_TYPE("EC_MEMBER.TYPE", 10, 30, "帳號類型(P:個人;C:公司)MemberTypeEnum"),
    
    EC_MEMBER_MSG_TYPE("EC_MEMBER_MSG.TYPE", 1, 3, "類別"),
    EC_MEMBER_MSG_MESSAGE("EC_MEMBER_MSG.MESSAGE", 500, 500, "留言內容"),
    
    EC_OPTION_TYPE("EC_OPTION.TYPE", 10, 30, "類別"),
    EC_OPTION_CNAME("EC_OPTION.CNAME", 200, 200, "中文名稱"),
    EC_OPTION_ENAME("EC_OPTION.ENAME", 66, 200, "英文名稱"),
    EC_OPTION_CODE("EC_OPTION.CODE", 33, 100, "代碼"),
    EC_OPTION_MEMO("EC_OPTION.MEMO", 100, 300, "備註"),
    
    EC_ORDER_ORDER_NUMBER("EC_ORDER.ORDER_NUMBER", 40, 40, "訂單編號"),
    EC_ORDER_SHIPPING_DATE("EC_ORDER.SHIPPING_DATE", 2, 8, "出貨日"),
    EC_ORDER_STATUS("EC_ORDER.STATUS", 6, 20, "狀態 (Unknown:未成立(購物車), Pending:待確認, Inquiry:買方詢價, quotation:賣方報價, Approve:已核准, Declined:賣方拒絕, Authorized:已授權, Cancelled: 取消, Deleted:刪除, Returned:退回)"),
    EC_ORDER_MESSAGE("EC_ORDER.MESSAGE", 255, 255, "訊息"),
    //EC_ORDER_RECIPIENT("EC_ORDER.RECIPIENT", 40, 40, "收件人姓名"),
    //EC_ORDER_ADDRESS("EC_ORDER.ADDRESS", 160, 160, "收件地址"),
    //EC_ORDER_PHONE("EC_ORDER.PHONE", 6, 20, "收件人連絡電話"),
    EC_ORDER_PAY_STATUS("EC_ORDER.PAY_STATUS", 6, 20, "付款狀態 (A:未付款, B:已收款, C:已收到部分款項)"),
    EC_ORDER_SHIP_STATUS("EC_ORDER.SHIP_STATUS", 6, 20, "出貨狀態 (A:未出貨, B:已出貨, C:部分出貨, D:已到貨)"),
    EC_ORDER_PAYMENT_TYPE("EC_ORDER.PAYMENT_TYPE", 6, 20, "付款方式 (A:付款後出貨, B:貨到付款, C:分期付款)"),
    
    EC_ORDER_LOG_EVENT_TYPE("EC_ORDER_LOG.EVENT_TYPE", 3, 10, ""),
    EC_ORDER_LOG_MESSAGE("EC_ORDER_LOG.MESSAGE", 40, 120, "訊息"),
    
    EC_ORDER_MESSAGE_MESSAGE("EC_ORDER_MESSAGE.MESSAGE", 120, 120, "訊息"),
    
    EC_ORDER_PROCESS_PROCESS("EC_ORDER_PROCESS.PROCESS", 1024, 1024, "處理內容"),
    
    EC_ORDER_RATE_CUSTOMER_RATE("EC_ORDER_RATE.CUSTOMER_RATE", 3, 10, "買方給評"),
    EC_ORDER_RATE_CUSTOMER_MESSAGE("EC_ORDER_RATE.CUSTOMER_MESSAGE", 255, 255, "買方意見"),
    EC_ORDER_RATE_SELLER_RATE("EC_ORDER_RATE.SELLER_RATE", 3, 10, "賣方給評"),
    EC_ORDER_RATE_SELLER_MESSAGE("EC_ORDER_RATE.SELLER_MESSAGE", 255, 255, "賣方意見"),
    
    EC_ORDER_SHIP_INFO_RECIPIENT("EC_ORDER_SHIP_INFO.RECIPIENT", 40, 40, "收件人姓名"),
    EC_ORDER_SHIP_INFO_ADDRESS("EC_ORDER_SHIP_INFO.ADDRESS", 160, 160, "收件地址"),
    EC_ORDER_SHIP_INFO_PHONE("EC_ORDER_SHIP_INFO.PHONE", 20, 20, "收件人連絡電話"),
    EC_ORDER_SHIP_INFO_CAR_NO("EC_ORDER_SHIP_INFO.CAR_NO", 20, 20, "車號"),
    EC_ORDER_SHIP_INFO_DRIVER("EC_ORDER_SHIP_INFO.DRIVER", 40, 40, "司機"),
    
    EC_PAGE_TEMPLATE_CNAME("EC_PAGE_TEMPLATE.CNAME", 300, 300, "中文名稱"),
    EC_PAGE_TEMPLATE_ENAME("EC_PAGE_TEMPLATE.ENAME", 100, 300, "英文名稱"),
    
    EC_PAYMENT_TITLE("EC_PAYMENT.TITLE", 80, 80, "名稱"),
    EC_PAYMENT_MEMO("EC_PAYMENT.MEMO", 80, 80, "敘述"),
    EC_PAYMENT_TYPE("EC_PAYMENT.TYPE", 3, 10, "類別(Credit Card or On Delivery)"),
    
    EC_PERSON_TYPE("EC_PERSON.TYPE", 6, 20, "類型"),
    EC_PERSON_CNAME("EC_PERSON.CNAME", 200, 200, "中文名稱"),
    EC_PERSON_ENAME("EC_PERSON.ENAME", 66, 200, "英文名稱"),
    EC_PERSON_ID_CODE("EC_PERSON.ID_CODE", 16, 50, "身分證ID或護照ID"),
    EC_PERSON_NICKNAME("EC_PERSON.NICKNAME", 100, 100, "簡稱"),
    EC_PERSON_EMAIL1("EC_PERSON.EMAIL1", 33, 100, "電郵1"),
    EC_PERSON_EMAIL2("EC_PERSON.EMAIL2", 33, 100, "電郵2"),
    EC_PERSON_TEL1("EC_PERSON.TEL1", 10, 30, "電話1"),
    EC_PERSON_TEL2("EC_PERSON.TEL2", 10, 30, "電話2"),
    EC_PERSON_TEL3("EC_PERSON.TEL3", 10, 30, "電話3"),
    EC_PERSON_FAX1("EC_PERSON.FAX1", 10, 30, "傳真"),
    EC_PERSON_ADDR1("EC_PERSON.ADDR1", 500, 500, "地址1"),
    EC_PERSON_ADDR2("EC_PERSON.ADDR2", 500, 500, "地址2"),
    EC_PERSON_ID_TYPE("EC_PERSON.ID_TYPE", 0, 2, "ID_CODE類別"),
    EC_PERSON_BRIEF("EC_PERSON.BRIEF", 500, 500, "簡介"),
    EC_PERSON_GENDER("EC_PERSON.GENDER", 0, 2, "性別"),
    EC_PERSON_URL1("EC_PERSON.URL1", 340, 1024, "網址1"),
    EC_PERSON_URL2("EC_PERSON.URL2", 340, 1024, "網址2"),
    
    EC_PRD_ATTR_CNAME("EC_PRD_ATTR.CNAME", 200, 200, "屬性名稱"),
    EC_PRD_ATTR_ENAME("EC_PRD_ATTR.ENAME", 200, 200, "英文名稱"),
    EC_PRD_ATTR_MEMO("EC_PRD_ATTR.MEMO", 600, 600, "備註"),
    EC_PRD_ATTR_DATA_TYPE("EC_PRD_ATTR.DATA_TYPE", 6, 20, "資料類型 DataTypeEnum (文字)"),
    EC_PRD_ATTR_VAL_ATTR_VALUE("EC_PRD_ATTR_VAL.ATTR_VALUE", 1500, 1500, "設定值"),
    
    EC_PRD_DETAIL_CONTENT_TYPE("EC_PRD_DETAIL.CONTENT_TYPE", 1, 3, "內容內行 (T: text; I: image)"),
    EC_PRD_DETAIL_CONTENT_TXT("EC_PRD_DETAIL.CONTENT_TXT", 2000, 2000, "文字內容"),
    
    EC_PRD_INTRO_TEXT("EC_PRD_INTRO.TEXT", 600, 600, "商品簡述"),
    
    EC_PRD_PAYMENT_MEMO("EC_PRD_PAYMENT.MEMO", 300, 300, "備註"),
    
    EC_PRD_SHIPPING_MEMO("EC_PRD_SHIPPING.MEMO", 300, 300, "備註"),
    
    EC_PRD_TYPE_CNAME("EC_PRD_TYPE.CNAME", 200, 200, "分類名稱"),
    EC_PRD_TYPE_ENAME("EC_PRD_TYPE.ENAME", 200, 200, "英文名稱"),
    EC_PRD_TYPE_CODE("EC_PRD_TYPE.CODE", 33, 100, "代碼"),
    EC_PRD_TYPE_MEMO("EC_PRD_TYPE.MEMO", 600, 600, "備註"),
    
    EC_PRD_VARIANT_CNAME("EC_PRD_VARIANT.CNAME", 100, 300, "商品型別名稱"),
    EC_PRD_VARIANT_ENAME("EC_PRD_VARIANT.ENAME", 100, 300, "英文名稱"),
    EC_PRD_VARIANT_BARCODE("EC_PRD_VARIANT.BARCODE", 16, 50, "BAR CODE"),
    EC_PRD_VARIANT_SKU("EC_PRD_VARIANT.SKU", 16, 50, "庫存單位(Stock Keeping Unit)"),
    EC_PRD_VARIANT_VOLUME("EC_PRD_VARIANT.VOLUME", 200, 200, "體積"),
    
    EC_PRD_VAR_OPTION_TYPE("EC_PRD_VAR_OPTION.TYPE", 1, 3, "型別類型"),
    EC_PRD_VAR_OPTION_CNAME("EC_PRD_VAR_OPTION.CNAME", 200, 200, "主語系名稱"),
    EC_PRD_VAR_OPTION_ENAME("EC_PRD_VAR_OPTION.ENAME", 200, 200, "英文名稱"),
    EC_PRD_VAR_OPTION_MEMO("EC_PRD_VAR_OPTION.MEMO", 600, 600, "備註"),
    
    EC_PREREQUISTITE_IDS_TYPE("EC_PREREQUISTITE_IDS.TYPE", 3, 10, "類別"),
    
    EC_PRICE_RULE_TITLE("EC_PRICE_RULE.TITLE", 300, 300, "標題"),
    EC_PRICE_RULE_VALUE_TYPE("EC_PRICE_RULE.VALUE_TYPE", 1, 3, "值類別(A:amount, P:percentage)"),
    EC_PRICE_RULE_TARGET_SELECT("EC_PRICE_RULE.TARGET_SELECT", 1, 3, "target selection (A: all, E :entitled)"),
    EC_PRICE_RULE_TARGET_TYPE("EC_PRICE_RULE.TARGET_TYPE", 1, 3, "折扣金額對象 (I:訂購商品; S:運費)"),
    EC_PRICE_RULE_ALLOCATION_METHOD("EC_PRICE_RULE.ALLOCATION_METHOD", 1, 3, "設定範圍方式 (E:each、A:across)"),
    EC_PRICE_RULE_CUS_SEL("EC_PRICE_RULE.CUS_SEL", 1, 3, "customer selection  (A: all, E :prerequisite)"),
    EC_PRICE_RULE_PRD_SEL("EC_PRICE_RULE.PRD_SEL", 1, 3, "product selection  (A: all, E :prerequisite)"),
    EC_PRICE_RULE_PRD_SQL("EC_PRICE_RULE.PRD_SQL", 333, 1000, "商品複雜條件SQL"),
    EC_PRICE_RULE_CUS_SQL("EC_PRICE_RULE.CUS_SQL", 333, 1000, "客戶複雜條件SQL"),
    
    EC_PRODUCT_CNAME("EC_PRODUCT.CNAME", 200, 200, "主語系名稱"),
    EC_PRODUCT_ENAME("EC_PRODUCT.ENAME", 200, 200, "英文名稱"),
    EC_PRODUCT_CODE("EC_PRODUCT.CODE", 33, 100, "代碼"),
    EC_PRODUCT_STATUS("EC_PRODUCT.STATUS", 1, 3, "狀態"),
    EC_PRODUCT_PUBLISH_SCOPE("EC_PRODUCT.PUBLISH_SCOPE", 1, 3, "發佈範圍"),
    EC_PRODUCT_VOLUME("EC_PRODUCT.VOLUME", 200, 200, "體積"),
    EC_PRODUCT_BARCODE("EC_PRODUCT.BARCODE", 16, 50, "BAR CODE"),
    EC_PRODUCT_SKU("EC_PRODUCT.SKU", 16, 50, "庫存單位(Stock Keeping Unit)"),
    
    EC_PUSH_LOG_CATEGORY("EC_PUSH_LOG.CATEGORY", 6, 20, ""),
    EC_PUSH_LOG_PUSH_TYPE("EC_PUSH_LOG.PUSH_TYPE", 6, 20, "TAG,ALIAS"),
    EC_PUSH_LOG_TITLE("EC_PUSH_LOG.TITLE", 200, 200, ""),
    EC_PUSH_LOG_ALERT("EC_PUSH_LOG.ALERT", 1000, 1000, ""),
    EC_PUSH_LOG_AUDIENCE("EC_PUSH_LOG.AUDIENCE", 333, 1000, ""),
    EC_PUSH_LOG_PUSH_RESULT("EC_PUSH_LOG.PUSH_RESULT", 333, 1000, ""),
    
    EC_SELLER_TYPE("EC_SELLER.TYPE", 6, 20, "類型"),
    
    EC_SELLER_USER_MEMO("EC_SELLER_USER.MEMO", 600, 600, "備註"),
    
    EC_SERVICE_SERVICE("EC_SERVICE.SERVICE", 85, 255, "服務"),
    EC_SERVICE_SERVICE_URL("EC_SERVICE.SERVICE_URL", 85, 255, "服務網址"),
    EC_SERVICE_DESCRIPTION("EC_SERVICE.DESCRIPTION", 500, 500, "服務說明"),
    
    EC_SHIPPING_TITLE("EC_SHIPPING.TITLE", 80, 80, "名稱"),
    EC_SHIPPING_MEMO("EC_SHIPPING.MEMO", 80, 80, "敘述"),
    EC_SHIPPING_TYPE("EC_SHIPPING.TYPE", 3, 10, "類別(A:自行取貨;B:宅配)"),
    
    EC_STORE_TYPE("EC_STORE.TYPE", 6, 20, "類型ID"),
    EC_STORE_CNAME("EC_STORE.CNAME", 300, 300, "中文名稱"),
    EC_STORE_ENAME("EC_STORE.ENAME", 100, 300, "英文名稱"),
    EC_STORE_BRIEF("EC_STORE.BRIEF", 1500, 1500, "簡介"),
    
    EC_VENDOR_TYPE("EC_VENDOR.TYPE", 6, 20, "類型"),
    EC_VENDOR_CNAME("EC_VENDOR.CNAME", 300, 300, "中文名稱"),
    EC_VENDOR_ENAME("EC_VENDOR.ENAME", 100, 300, "英文名稱"),
    EC_VENDOR_ID_TYPE("EC_VENDOR.ID_TYPE", 0, 2, "ID_CODE類別"),
    EC_VENDOR_ID_CODE("EC_VENDOR.ID_CODE", 16, 50, "統編、營業登記號"),
    EC_VENDOR_NICKNAME("EC_VENDOR.NICKNAME", 100, 100, "簡稱"),
    EC_VENDOR_BRIEF("EC_VENDOR.BRIEF", 500, 500, "簡介"),
    EC_VENDOR_EMAIL1("EC_VENDOR.EMAIL1", 33, 100, "電郵1"),
    EC_VENDOR_EMAIL2("EC_VENDOR.EMAIL2", 33, 100, "電郵2"),
    EC_VENDOR_EMAIL3("EC_VENDOR.EMAIL3", 33, 100, "電郵3"),
    EC_VENDOR_TEL1("EC_VENDOR.TEL1", 10, 30, "電話1"),
    EC_VENDOR_TEL2("EC_VENDOR.TEL2", 10, 30, "電話2"),
    EC_VENDOR_TEL3("EC_VENDOR.TEL3", 10, 30, "電話3"),
    EC_VENDOR_FAX1("EC_VENDOR.FAX1", 10, 30, "傳真1"),
    EC_VENDOR_FAX2("EC_VENDOR.FAX2", 10, 30, "傳真2"),
    EC_VENDOR_ADDR1("EC_VENDOR.ADDR1", 500, 500, "地址1"),
    EC_VENDOR_ADDR2("EC_VENDOR.ADDR2", 500, 500, "地址2"),
    EC_VENDOR_CODE("EC_VENDOR.CODE", 16, 50, "供應商編碼"),
    
    TC_GROUP_CODE("TC_GROUP.CODE", 10, 30, "群組代號"),
    TC_GROUP_NAME("TC_GROUP.NAME", 90, 90, "群組名稱"),
    
    TC_USER_LOGIN_ACCOUNT("TC_USER.LOGIN_ACCOUNT", 20, 60, "登入帳號"),
    TC_USER_EMAIL("TC_USER.EMAIL", 20, 60, ""),
    TC_USER_EMP_ID("TC_USER.EMP_ID", 6, 20, ""),
    TC_USER_CNAME("TC_USER.CNAME", 20, 20, "");

    private String code;
    private int eLen;
    private int cLen;
    private String label;
    
    ValidateStrEnum(String code, int cLen, int eLen, String label){
        this.code = code;
        this.cLen = cLen;
        this.eLen = eLen;
        this.label = label;
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
//        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE , this.getClass().getSimpleName()+"."+this.toString() );
//        if( res==null ){
//            res = label;
//        }
//        return res;
        return label;
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

}
