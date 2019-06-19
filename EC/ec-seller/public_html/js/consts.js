var LANG = "C";
var TEST_DATA = false;
var LOG_LEVEL = "DEBUG";
var TCC_ENABLED = true;
var QUOTE_TOTAL = false;// 訂單報總價(GlobalConstant.QUOTE_TOTAL)
var QUANTITY_MODIFY = false;// 出貨訂單改量
var URL_NOW = window.location.href;
var URL_BASE = (URL_NOW.indexOf("://localhost")>=0)? "http://localhost:8080"
                :(URL_NOW.indexOf("://127.0.0.1")>=0)? "http://localhost:8080"
                :(URL_NOW.indexOf("file:///")>=0)? "http://localhost:8080"
                :"";// 同 IP or Domain Name
URL_BASE = URL_BASE + "/ec-seller-serv";

var KEEP_ALIVE_TIME = 300000; // 5min
var DATATABLE_RWD_WIDTH = 543;
var PAGE_SIZE_DEF = 10;
// File Upload
var UF_MAX_FILES = 5;
var UF_MAX_MB = 2;
// Filed
var FIELD_TO = "to";
var FIELD_FROM = "from";
var FIELD_ACCOUNT = "loginAccount";
var FIELD_TOKEN = "token";
var FIELD_ADMIN = "adminUser";
var FIELD_ST_OWNER = "storeOwner";
var FIELD_ST_FIUSER = "fiUser";
var FIELD_DEALER = "tccDealer";
var FIELD_STOREID = "storeId";

var FIELD_STATE = "state";
var FIELD_LANG = "language";
var FIELD_UUID = "clientUUID";
var FIELD_MODIFIED = "clientModified";

var FIELD_TABID = "tabId";

var DEF_LANG = "zh-CN";
var LANGS = [
    {"label":"繁", "code":"zh-TW", "value":0, "ec15":true},
    {"label":"简", "code":"zh-CN", "value":1, "ec15":true},
    {"label":"En", "code":"en-US", "value":2, "ec15":false},
    {"label":"Tr", "code":"tr-TR", "value":3, "ec15":false}
];

var FROM_WEBPAGE = "web";
var FROM_ANDROID = "android";
var FROM_INTERNAL = "internal";

// 代辦事項
var TODO_PRD_ONSALES = "1"; // 待上架商品
var TODO_PRD_CORRECT = "2"; // 待修正商品
var TODO_QUOTATION = "3"; // 待報價詢價單
var TODO_PO_CONFIRM = "4"; // 待確認訂單
var TODO_PO_PAY_RECEIVED = "5"; // 待確認收款訂單
var TODO_PO_SHIP = "6"; // 待出貨訂單
var TODO_CUS_CREDITS = "7"; // 信用額度申請
var TODO_PO_MSG_REPLY = "8"; // 待回覆訊息訂單
var TODO_MEM_MSG_REPLY = "9"; // 待回覆訪客留言
// 信用額度申請
var CREDIT_STATUS_NONE = "N";// 未申請
var CREDIT_STATUS_APPLY = "A";// 申請中
var CREDIT_STATUS_PASS = "P";// 已通過
var CREDIT_STATUS_ZERO = "Z";// 已無信用額度
// 商品狀態 (ProductStatusEnum)
var PRD_STATUS_PUB = "P";
var PRD_STATUS_REMOVE = "R";
var PRD_STATUS_RESERVED = "S";
var PRD_STATUS_APPLY = "A";
var PRD_STATUS_PASS = "AP";
var PRD_STATUS_REJECT = "AR";// 
// 詢價單狀態
var RFQ_STATUS_INQUIRY = "Inquiry";// 買方詢價
var RFQ_STATUS_QUOTATION = "Quotation";// 賣方報價
//var RFQ_STATUS_DECLINED = "Declined";
var RFQ_STATUS_REJECT = "Rejected";// 賣方拒絕-報價
var RFQ_STATUS_DELETE = "Deleted";// 刪除(買家)
// 訂單狀態
var PO_STATUS_PENDING = "Pending";// 待賣方確認
var PO_STATUS_DECLINED = "Declined";// 賣方回絕-訂單 訂單階段(未確認)
var PO_STATUS_RETURNED = "Returned";// 買方收回-訂單 訂單階段(未確認)
var PO_STATUS_APPROVE = "Approve";// 已核准
var PO_STATUS_WAITTING = "Waiting"; // 待買方確認訂單量、價
//var PO_STATUS_ACCEPTED = "Accepted";// 買方接受價格
var PO_STATUS_CANCEL = "Cancelled";// 取消 訂單階段(未付款 未出貨)  買買雙方都可執行
//var PO_STATUS_SELLERCONFIRM = "SellerConfirm";// 賣家最後確認
//var PO_STATUS_CUSCONFIRM = "CusConfirm";// 買家最後確認
var PO_STATUS_CLOSE = "Close";//結案

// 出貨狀態 
var SHIP_STATUS_NOT="A"; // 未出貨
var SHIP_STATUS_SHIPPED = "B";// 已出貨
var SHIP_STATUS_INSTALLMENT = "C";// 部分出貨
var SHIP_STATUS_ARRIVED = "D";// 已到貨

// 付款狀態
var PAY_STATUS_NOT_PAID="A"; // 未付款
var PAY_STATUS_PAID = "B";// 已收款
var PAY_STATUS_INSTALLMENT = "C";// 已收到部分款項
var PAY_STATUS_NOTIFY_PAID = "D";// 通知已付款

// SELLER PAGE URL
var HOME_PAGE = "index.html";
var LOGIN_PAGE = "login.html";
var SELLER_HOME_PAGE = "home.html";
var SELLER_PAGES = ["index.html","login.html","my.html","home.html","myProducts.html"
            ,"myPrdType.html","importPrd.html","importTccPrd.html"
            ,"orders.html","customers.html","importMyDownstream.html"
            ,"myStore.html","managers.html","vendors.html","noPaid.html"];

// ADMIN PAGE URL
var ADMIN_PAGE = "admin.html";
var ADMIN_LOGIN_PAGE = "loginAdmin.html";
var ADMIN_HOME_PAGE = "members.html";
var ADMIN_PAGES = ["admin.html","loginAdmin.html","members.html","importDeader.html","importDownstream.html"
            ,"stores.html","tccProds.html","prdType.html","products.html"
            ,"carousel.html","hotPrd.html","hotStore.html","bulletin.html"];

// DEALER PAGE URL
var DEALER_PAGE = "dealer.html";
var DEALER_LOGIN_PAGE = LOGIN_PAGE;// "loginDealer.html";// TCC經銷商不獨立登入頁，依DB設定決定

// DATA RESTful URL
var CHECK_LOGIN_URL = URL_BASE + '/services/auth/checkLogin';
var CHECK_IT_LOGIN_URL = URL_BASE + '/services/auth/internalLogin';
var WAITTING_URL = "json/waitting.json";
//var MENU_URL = "i18n/menus_LANG_CODE.json";
var MENU_URL = "i18n/menus.json";
//var ADMIN_MENU_URL = "i18n/menus-admin_LANG_CODE.json";
var ADMIN_MENU_URL = "i18n/menus-admin.json";
var I18N_PATH = "i18n/";
var I18N_PROPS_NAME = "strings";

// 首頁 
var LOGIN_URL = URL_BASE + '/services/auth/login';
var PWD_RESET_URL = URL_BASE + '/services/auth/reset';
var SWITCH_STORE_URL = URL_BASE + '/services/auth/switchStore';
// 共用
var GET_EXP_URL = URL_BASE + '/services/sys/exp/get';
// 一般選單選項
var OPTIONS_URL = URL_BASE + '/services/ops/get';
// 商品類別
var PRD_TYPE_URL = URL_BASE + '/services/products/type/tree';
var PRD_TYPE_SAVE_URL =  URL_BASE + '/services/products/type/save';
var PRD_TYPE_DEL_URL = URL_BASE + '/services/products/type/remove';
//var PRD_TYPE_LIST_URL = 'json/prdtypelist.json';
//var PRD_TYPE_LIST_URL2 = 'json/prdtypelist2.json';
//var PRD_TYPE_LIST_URL3 = 'json/prdtypelist3.json';
// 商品屬性 (依類別)
var PRD_TYPE_ATTR_URL = URL_BASE + '/services/products/type/{typeId}/attr/list';
var PRD_TYPE_ATTR_SAVE_URL  = URL_BASE + '/services/products/type/{typeId}/attr/save';
var PRD_TYPE_ATTR_DEL_URL  = URL_BASE + '/services/products/type/{typeId}/attr/remove';
// TCC 商品查詢
var PRD_TCC_COUNT_URL = URL_BASE + '/services/products/tcc/count';
var PRD_TCC_LIST_URL = URL_BASE + '/services/products/tcc/list';
var PRD_TCC_IMP_URL = URL_BASE + '/services/products/tcc/import';
// 商品查詢
var PRD_COUNT_URL = URL_BASE + '/services/products/count';
var PRD_LIST_URL = URL_BASE + '/services/products/list';
// 商品基本
var PRD_FULL_URL = URL_BASE + '/services/products/full/{id}';
var PRD_SAVE_URL = URL_BASE + '/services/products/save';
var PRD_CH_STATUS_URL = URL_BASE + '/services/products/status/change';
var PRD_APPROVE_URL = URL_BASE + '/services/products/approve';
var PRD_PUBLISH_URL = URL_BASE + '/services/products/publish';
var PRD_DEL_URL = URL_BASE + '/services/products/remove';
var PRD_EXP_URL = URL_BASE + '/services/products/exp';
// 商品預約上架
var PRD_RESERVE_URL = URL_BASE + '/services/products/reserve/save';
// 商品送審、下架
var PRD_MULTI_STATUS_URL = URL_BASE + '/services/products/status/save';
// 商品顏色
var PRD_COLOR_SAVE_URL = URL_BASE + '/services/products/{prdId}/color/save';
var PRD_COLOR_DEL_URL = URL_BASE + '/services/products/{prdId}/color/remove';
// 商品大小
var PRD_SIZE_SAVE_URL = URL_BASE + '/services/products/{prdId}/size/save';
var PRD_SIZE_DEL_URL = URL_BASE + '/services/products/{prdId}/size/remove';
// 商品型別設定
var PRD_VAR_URL = URL_BASE + '/services/products/{prdId}/variants';
var PRD_VAR_SAVE_URL = URL_BASE + '/services/products/{prdId}/variant/save';
var PRD_VAR_DEL_URL = URL_BASE + '/services/products/{prdId}/variant/remove';
// 商品簡介
var PRD_INTRO_URL = 'json/prdIntro.json';
var PRD_INTRO_SAVE_URL = URL_BASE + '/services/products/{prdId}/intro/save';
//var PRD_INTRO_DEL_URL = URL_BASE + '/services/products/{prdId}/intro/remove';
// 商品配送方式
var PRD_SHIP_SAVE_URL = URL_BASE + '/services/products/{prdId}/shipping/save';
// 商品付款方式
var PRD_PAY_SAVE_URL = URL_BASE + '/services/products/{prdId}/payment/save';
// 商品圖片
var CHECK_CORS_URL = URL_BASE + '/services/auth/checkCORS';
var PRD_PICS_URL = URL_BASE + '/services/products/{prdId}/picture/list';
var PRD_PIC_UPLOAD_URL = URL_BASE + '/services/products/{prdId}/picture/upload';
var PRD_PIC_SAVE_URL = URL_BASE + '/services/products/{prdId}/picture/save';
var PRD_PIC_DEL_URL = URL_BASE + '/services/products/{prdId}/picture/remove';
// 台泥商品
var TCC_PRD_PIC_UP_URL = URL_BASE + '/services/products/tcc/pic/upload';
var TCC_PRD_PIC_DEL_URL = URL_BASE + '/services/products/tcc/pic/delete';
var TCC_PRD_SAVE_URL = URL_BASE + '/services/products/tcc/save';
var TCC_PRD_ACT_URL = URL_BASE + '/services/products/tcc/active';

// 商品屬性 (依類別)
var PRD_ATTR_URL = URL_BASE + '/services/products/{prdId}/attrs';
var PRD_ATTR_SAVE_URL  = URL_BASE + '/services/products/{prdId}/attr/save';
var PRD_ATTR_DEL_URL  = URL_BASE + '/services/products/{prdId}/attr/remove';
// 商品明細
var PRD_DETAIL_SAVE_URL = URL_BASE + '/services/products/{prdId}/detail/save';
var PRD_DETAIL_UPLOAD_URL = URL_BASE + '/services/products/{prdId}/detail/upload';
var PRD_DETAIL_DEL_URL = URL_BASE + '/services/products/{prdId}/detail/remove';
// 商品庫存
var PRD_STOCK_COUNT_URL = URL_BASE + '/services/products/{prdId}/stock/count';
var PRD_STOCK_LOGS_URL = URL_BASE + '/services/products/{prdId}/stock/logs';
var PRD_STOCK_SAVE_URL  = URL_BASE + '/services/products/{prdId}/stock/save';
// 客戶
var CUS_COUNT_URL = URL_BASE + '/services/customers/count';
var CUS_LIST_URL = URL_BASE + '/services/customers/list';
var CUS_FULL_URL = URL_BASE + '/services/customers/full/{id}';
var CUS_SAVE_URL = URL_BASE + '/services/customers/save';
var CUS_SAVE_CREDITS_URL = URL_BASE + '/services/customers/credits/save';
var CUS_CREDITS_COUNT_URL = URL_BASE + '/services/customers/credits/count';
var CUS_CREDITS_LIST_URL = URL_BASE + '/services/customers/credits/list';

//var CUS_ADD_URL = URL_BASE + '/services/customers/add';
// 客戶意見反映
var CUS_FB_COUNT_URL = URL_BASE + '/services/customers/feedback/count';
var CUS_FB_LIST_URL = URL_BASE + '/services/customers/feedback/list';
var CUS_FB_SAVE_URL = URL_BASE + '/services/customers/feedback/save';

// 詢價單
var RFQ_COUNT_URL = URL_BASE + '/services/orders/rfq/count';
var RFQ_LIST_URL = URL_BASE + '/services/orders/rfq/list';
var RFQ_FULL_URL = URL_BASE + '/services/orders/rfq/full/{id}';
var RFQ_SAVE_URL = URL_BASE + '/services/orders/rfq/save';
var PO_QUOTE_URL = URL_BASE + '/services/orders/quote';

// 詢價單洽談記錄
var RFQ_MESSAGE_URL = URL_BASE + '/services/orders/{orderId}/message';
var RFQ_MESSAGE_SAVE_URL  = URL_BASE + '/services/orders/{orderId}/message/save';
var RFQ_MESSAGE_DEL_URL  = URL_BASE + '/services/orders/{orderId}/message/remove';

// 訂單
var PO_CHANGE_URL = URL_BASE + '/services/orders/change';
var PO_STATUS_SAVE_URL = URL_BASE + '/services/orders/status/save';// 確認訂單、回絕訂單
var PO_SUM_URL = URL_BASE + '/services/orders/sum';
var PO_COUNT_URL = URL_BASE + '/services/orders/count';
var PO_LIST_URL = URL_BASE + '/services/orders/list';
var PO_FULL_URL = URL_BASE + '/services/orders/full/{id}';
var PO_PAY_SAVE_URL = URL_BASE + '/services/orders/pay/save';// 付款確認
var PO_SHIP_SAVE_URL = URL_BASE + '/services/orders/ship/save';// 確認出貨
var PO_EXP_URL = URL_BASE + '/services/orders/exp';
var PO_MULTI_CLOSE_URL = URL_BASE + '/services/orders/close';// 訂單結案

// for 轉 EC10 訂單
var PO_TRAN_TO_EC10_OPS = URL_BASE + '/services/ops/ec10';
var PO_TRAN_TO_EC10_SEND = URL_BASE + '/services/orders/ec10/send';
// for 常用送達地點, 車號
var CUS_ADDR_ADD_URL = URL_BASE + '/services/customers/used/add';
var CUS_ADDR_SEL_URL = URL_BASE + '/services/customers/used/sel';
// for 車號
var PO_CAR_NO_SAVE = URL_BASE + '/services/orders/car/save';

// 訂單項目
var PO_ITEMS_LIST_URL = URL_BASE + '/services/orders/items/list';
// 訂單評價
var PO_RATE_URL = URL_BASE + '/services/orders/rate';
// 訂單處理記錄
var PO_PROCESS_URL = URL_BASE + '/services/orders/{orderId}/processs';
var PO_PROCESS_SAVE_URL  = URL_BASE + '/services/orders/{orderId}/process/save';
var PO_PROCESS_DEL_URL  = URL_BASE + '/services/orders/{orderId}/process/remove';

// 商店
var STORE_COUNT_URL = URL_BASE + '/services/stores/count';
var STORE_LIST_URL = URL_BASE + '/services/stores/list';
var STORE_FULL_URL = URL_BASE + '/services/stores/full/{id}';
var STORE_SAVE_URL = URL_BASE + '/services/stores/save';
var STORE_DEL_URL = URL_BASE + '/services/stores/remove';
var STORE_ADD_URL = URL_BASE + '/services/stores/add';
var STORE_DEF_URL = URL_BASE + '/services/stores/default';
var STORE_OPEN_URL = URL_BASE + '/services/stores/open';
var STORE_DEF_ATTR_URL = URL_BASE + '/services/stores/defAttrs';

var ST_LOGO_UPLOAD_URL = URL_BASE + '/services/stores/logo/upload';
var ST_BANNER_UPLOAD_URL = URL_BASE + '/services/stores/banner/upload';
var ST_PIC_DEL_URL = URL_BASE + '/services/stores/picture/remove';

// 商店管理員
var ST_ADD_USER_URL = URL_BASE + '/services/stores/manager/add';
var ST_DEL_USER_URL = URL_BASE + '/services/stores/manager/remove';
var ST_SET_ROLE_URL = URL_BASE + '/services/stores/manager/set';

// 付款方式
var ST_PAY_URL = URL_BASE + '/services/stores/payment/list';
var ST_PAY_SAVE_URL = URL_BASE + '/services/stores/payment/save';
//var ST_PAY_DEL_URL = URL_BASE + '/services/stores/payment/remove';
//  運送方式
var ST_SHIP_URL = URL_BASE + '/services/stores/shipping/list';
var ST_SHIP_SAVE_URL = URL_BASE + '/services/stores/shipping/save';
//var ST_SHIP_DEL_URL = URL_BASE + '/services/stores/shipping/remove';
// 銷售地區
var ST_AREA_SAVE_URL = URL_BASE + '/services/stores/salesArea/save';
// 供應商
var VENDOR_COUNT_URL = URL_BASE + '/services/vendors/count';
var VENDOR_LIST_URL = URL_BASE + '/services/vendors/list';
var VENDOR_FULL_URL = URL_BASE + '/services/vendors/full/{id}';
var VENDOR_SAVE_URL = URL_BASE + '/services/vendors/save';
var VENDOR_DEL_URL = URL_BASE + '/services/vendors/remove';

// 會員
var MEM_COUNT_URL = URL_BASE + '/services/members/count';
var MEM_LIST_URL = URL_BASE + '/services/members/list';
var MEM_FULL_URL = URL_BASE + '/services/members/full/{id}';
var MEM_SAVE_URL = URL_BASE + '/services/members/save';
var MEM_SELLER_URL = URL_BASE + '/services/members/seller';
var MEM_DEL_URL = URL_BASE + '/services/members/remove';
var MEM_CHG_PWD_URL = URL_BASE + '/services/members/changePwd';
var MEM_CHG_TYPE_URL = URL_BASE + '/services/members/changeType';
var MEM_APPLY_TYPE_URL = URL_BASE + '/services/members/applyType';
var MEM_PIC_UPLOAD_URL = URL_BASE + '/services/members/picture/upload';
var MEM_PIC_DEL_URL = URL_BASE + '/services/members/picture/remove';

// EC_OPTION
var OPS_URL = URL_BASE + '/services/ops/list';
var OPS_SAVE_URL = URL_BASE + '/services/ops/save';
var OPS_DEL_URL = URL_BASE + '/services/ops/remove';

// 統計
var STA_ST_TODO_URL = URL_BASE + '/services/statistic/store/todo';
var STA_ORDER_STATUS_URL = URL_BASE + '/services/statistic/store/orderStatus';
var STA_RFQ_STATUS_URL = URL_BASE + '/services/statistic/store/rfqStatus';
var STA_CUS_LEVEL_URL = URL_BASE + '/services/statistic/store/cusLevel';
var STA_PRD_STATUS_URL = URL_BASE + '/services/statistic/store/prdStatus';
var STA_PRD_TYPE_URL = URL_BASE + '/services/statistic/store/prdType';
var STA_ORDER_SUM_URL = URL_BASE + '/services/statistic/store';

var STA_ST_URL = URL_BASE + '/services/statistic/store';

// 留言
var ST_MSG_COUNT_URL = URL_BASE + '/services/members/message/count';
var ST_MSG_LIST_URL = URL_BASE + '/services/members/message/list';
var ST_MSG_REPLY_LIST_URL = URL_BASE + '/services/members/message/reply/list';
var ST_MSG_REPLY_URL = URL_BASE + '/services/members/message/reply/save';

// 廣告圖示 (首頁輪播展示圖、人氣商品)
var AD_COUNT_URL = URL_BASE + '/services/sys/ad/count';
var AD_LIST_URL = URL_BASE + '/services/sys/ad/list';
var AD_FULL_URL = URL_BASE + '/services/sys/ad/full/{type}/{id}';
var AD_NOW_URL = URL_BASE + '/services/sys/ad/now';
var AD_SAVE_URL = URL_BASE + '/services/sys/ad/save';
var AD_DEL_URL = URL_BASE + '/services/sys/ad/remove';
var AD_SAVE_SORT_URL = URL_BASE + '/services/sys/ad/sort/save';
// 批次匯入
var IMPORT_PRD_UPLOAD_URL = URL_BASE + '/services/products/import/upload';
var IMPORT_PRD_SAVE_URL = URL_BASE + '/services/products/import/save';
var IMPORT_DEALER_UPLOAD_URL = URL_BASE + '/services/members/import/dealer/upload';
var IMPORT_DEALER_SAVE_URL = URL_BASE + '/services/members/import/dealer/save';
var IMPORT_DS_UPLOAD_URL = URL_BASE + '/services/members/import/ds/upload';
var IMPORT_DS_SAVE_URL = URL_BASE + '/services/members/import/ds/save';
// 系統公告
var BULLETIN_COUNT_URL = URL_BASE + '/services/sys/bulletin/count';
var BULLETIN_LIST_URL = URL_BASE + '/services/sys/bulletin/list';
var BULLETIN_SAVE_URL = URL_BASE + '/services/sys/bulletin/save';
// 會員註冊
var MEM_REG_URL = URL_BASE + '/services/auth/register';
var MEM_VERIFY_URL = URL_BASE + '/services/auth/verify';
