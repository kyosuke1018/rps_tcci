/* global ajaxFlags, _asyncCompleteFlags, MSG_NO_RECORD_FOUND, PO_COUNT_URL, PN_ORDER, PO_LIST_URL, DATATABLE_RWD_WIDTH, PO_PROCESS_URL, FIELD_UUID, OPTIONS_URL, w3, NO_SELECT_OP_STR, PO_EXP_URL, afterExpOrder, expHeaders, PO_STATUS_APPROVE, SHIP_STATUS_NOT, onClickForEC10, onClickForEC10M, _tccDealer, voOC, utils */
var vo = {// variable objects in the JS
    itemlist:[],
    records:[],
    cusLevels:[],
    rfqStatus:[],
    orderStatus:[],
    payStatus:[],
    shipStatus:[],
    supportTranEC10:true,// 本頁是否提供轉單EC1.0功能
    // 隱藏條件 (for export)
    hc:{
        closed:null,
        tranToEC10:null,
        forCombine:null
    }
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
    buildOrderListLazy(utils.initUrlCriteria(), 'orderList');// Lazy方式抓取資料
    // 處理記錄表單
    //buildRecordProcessForm();

    $("#selectedItemHeader").hide();
    $("#fsOrderDetail").hide();// 訂單明細
    // for 轉單 EC1.0
    $("#combineKeys").hide();
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsSearchResult').puifieldset({toggleable: true});
    $('#fsOrderDetail').puifieldset({toggleable: true});
    
    $('#fsOrderInfoEC10').puifieldset({toggleable: true});
    $("#fsOrderInfoEC10View").puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plOrderItems', 'plOrderShipping', 'plOrderPayment', 'plRfqMessage', 'plOrderProcess']);
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
    createDropdown("#orderStatus", vo.orderStatus, {label:utils.i18n("po.status"), value:""}, null);// 訂單狀態
    createDropdown("#payStatus", vo.payStatus, {label:utils.i18n("pay.status"), value:""}, null);// 付款狀態
    createDropdown("#shipStatus", vo.shipStatus, {label:utils.i18n("shipping.status"), value:""}, null);// 出貨狀態
    //$("#startAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //$("#endAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //createDatePicker("#startAt");
    var langCode = utils.getLangCode();
    $("#startAtDIV").DateTimePicker({
            language: langCode,
            defaultDate: new Date(),
            beforeShow: function(element){ utils.hideBodyScrollY(); },
            afterHide: function(element){ utils.showBodyScrollY(); },
            //buttonsToDisplay: [ "SetButton","HeaderCloseButton"],
            //clearButtonContent: "取消",
            animationDuration:200
    });

    //createDatePicker("#endAt");
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    createBtn('#noClosed', 'fa-search-plus', onClickNoClosed);// 未結案訂單
    
    // 待轉台泥訂單查詢按紐
    if( _tccDealer ){
        renderSearchBtnForEC10();
    }
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
function onClickSearch(){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    formData["shipStartAt"] = toISODateTime(formData["shipStartAt"]);
    formData["shipEndAt"] = toISODateTime(formData["shipEndAt"]);
    vo.hc.closed = null;
    vo.hc.tranToEC10 = null;
    vo.hc.forCombine = null;
    
    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料
    commonRenderAfterOrderSearch();
    utils.scrollTo("#topOrderSearchResult");
}
// 清除
function onClickClearSearch(){
    setDropdownValue('#orderStatus', "");
    setDropdownValue('#payStatus', "");
    setDropdownValue('#shipStatus', "");
    $("#fmSearch")[0].reset();
    
    renderOrderListLazy({}, 0, false, PO_LIST_URL);// clear table
    commonRenderAfterOrderSearch();
}
// 未結案訂單
function onClickNoClosed(){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    formData["shipStartAt"] = toISODateTime(formData["shipStartAt"]);
    formData["shipEndAt"] = toISODateTime(formData["shipEndAt"]);
    
    formData["closed"] = false;
    vo.hc.closed = false;
    vo.hc.tranToEC10 = null;
    vo.hc.forCombine = null;
    
    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料
    commonRenderAfterOrderSearch();
    
    utils.scrollTo("#topOrderSearchResult");
}
//</editor-fold>
