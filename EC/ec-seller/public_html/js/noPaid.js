/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, com/menu.js, layout.js
 */
/* global ajaxFlags, _asyncCompleteFlags, MSG_NO_RECORD_FOUND, PO_COUNT_URL, PN_ORDER, PO_LIST_URL, DATATABLE_RWD_WIDTH, PO_PROCESS_URL, FIELD_UUID, OPTIONS_URL, w3, NO_SELECT_OP_STR, NOPAID_SUM_URL, PO_SUM_URL, expHeaders, PO_EXP_URL, afterExpOrder, voOC, utils */
var vo = {// variable objects in the JS
    cusLevels:[],
    rfqStatus:[],
    orderStatus:[],
    payStatus:[],
    shipStatus:[],
    supportTranEC10:false// 本頁是否提供轉單EC1.0功能
};

$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    w3.includeHTML(start);
});

function start(){
    console.log(utils.formatDT(new Date()));
    // 登入檢查
    utils.checkLogin();// trigger _renderThisPage
}

function _renderThisPage(){
    // init layout
    initLayout(false, true);
    renderPanels();
    // Ajax 產出UI相關
    fetchInitOptions("init");
    
    // 客戶訂單
    var formData = {};
    formData["rfq"] = false;
    formData["orderStatus"] = "Approve"; // 已審核
    formData["payStatus"] = "A";         // 未付款
    
    getNoPaidTotal(formData, 'noPaid');
    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料
    // 處理記錄表單
    buildRecordProcessForm();

    $("#selectedItemHeader").hide();
    $("#fsOrderDetail").hide();// 訂單明細
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsList').puifieldset({toggleable: true});
    $('#fsOrderDetail').puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plOrderItems', 'plOrderShipping', 'plOrderPayment', 'plRfqMessage', 'plOrderProcess']);
}

function getNoPaidTotal(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    
    var restUrl = PO_SUM_URL;
    utils.postData(restUrl, formData, false, showNoPaidTotal, null, ajaxKey);
}

function showNoPaidTotal(response, formData, ajaxKey){
    if( !isNA(response) && !isNA(response.res) &&  !isNA(response.res.total) ){
        var html = "<i class='fa fa-star'></i>" + utils.i18n("nopaid.po.total") 
                 + "：<font color='red' >" + printNumber(response.res.total) + "</font> " 
                 + utils.i18n("nopaid.po.memo");
        $("#totalMsg").html(html);
    }else{
        $("#totalMsg").html("");
    }
    
    _asyncCompleteFlags[ajaxKey]=true;
}

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=rfqStatus,orderStatus,payStatus,shipStatus,cusLevel,favCars&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

// 可預先建立的 UI 操作元件
function buildInitObjects(response, ajaxKey){
    vo.cusLevels = utils.getListProp(response, "cusLevel");
    vo.rfqStatus = utils.getListProp(response, "rfqStatus");
    vo.orderStatus = utils.getListProp(response, "orderStatus");
    vo.payStatus = utils.getListProp(response, "payStatus");
    vo.shipStatus = utils.getListProp(response, "shipStatus");
    // use in order-common.js
    voOC.favCarNoList = utils.getListProp(response, "favCars");
    
    buildSearchForm();
    
    _asyncCompleteFlags[ajaxKey]=true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Order Search">
// 查詢表單
function buildSearchForm(){
    //createDropdown("#rfqStatus", rfqStatus, {label:'=詢價單狀態=', value:""}, null);// 詢價單狀態
    //createDropdown("#orderStatus", orderStatus, {label:utils.i18n("po.status"), value:""}, null);// 訂單狀態
    //createDropdown("#payStatus", payStatus, {label:utils.i18n("pay.status"), value:""}, null);// 付款狀態
    //createDropdown("#shipStatus", shipStatus, {label:utils.i18n("shipping.status"), value:""}, null);// 出貨狀態
    //$("#startAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //$("#endAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    createDatePicker("#startAt");
    createDatePicker("#endAt");
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
}
// common render after search
function commonRenderAfterOrderSearch(){
    $("#selectedItemHeader").hide();
    $("#fsOrderDetail").hide();// 訂單明細
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();
}

// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    
    formData["rfq"] = false;
    formData["orderStatus"] = "Approve"; // 已審核
    formData["payStatus"] = "A";         // 未付款
    
    getNoPaidTotal(formData, 'noPaid');
    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料

    $("#fsOrderDetail").hide();// 訂單明細
    $("#selectedItemHeader").hide();
}
// 清除
function onClickClearSearch(event){
    $("#fmSearch")[0].reset();
    $("#totalMsg").html("");
    
    renderOrderListLazy({}, 0, false, PO_LIST_URL);// clear table
    $("#fsOrderDetail").hide();// 訂單明細
    $("#selectedItemHeader").hide();
}

// 匯出查詢結果 // expHeaders, afterExpOrder define in order-common.js
function onClickExpOrder() {
    console.log("onClickExpOrder ...");
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    
    formData["rfq"] = false;
    formData["orderStatus"] = "Approve"; // 已審核
    formData["payStatus"] = "A";         // 未付款
    
    formData["headers"] = voOC.expHeaders;;

    var restUrl = PO_EXP_URL;
    utils.postData(restUrl, formData, false, afterExpOrder);
}
//</editor-fold>
