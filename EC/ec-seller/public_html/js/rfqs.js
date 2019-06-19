/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, com/menu.js, layout.js
 */
/* global ajaxFlags, _asyncCompleteFlags, MSG_NO_RECORD_FOUND, PO_COUNT_URL, PN_RFQ, PO_LIST_URL, DATATABLE_RWD_WIDTH, PO_PROCESS_URL, FIELD_UUID, OPTIONS_URL, w3, NO_SELECT_OP_STR, RFQ_LIST_URL, RFQ_COUNT_URL, utils */
var vo = {// variable objects the JS
    //itemlist: null,// 選取
    //records: null,
    cusLevels: null,
    rfqStatus: null
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
    buildRfqListLazy(utils.initUrlCriteria(), 'rfqList');// Lazy方式抓取資料

    $("#selectedItemHeader").hide();
    $("#fsRfqDetail").hide();// 訂單明細

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsRfq').puifieldset({toggleable: true});
    $('#fsRfqDetail').puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plRfqItems', 'plRfqShipping', 'plRfqPayment', 'plRfqMessage']);
}

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=rfqStatus,cusLevel";
    restUrl = restUrl + "&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

// 可預先建立的 UI 操作元件
function buildInitObjects(response, ajaxKey){
    vo.cusLevels = utils.getListProp(response, "cusLevel");
    vo.rfqStatus = utils.getListProp(response, "rfqStatus");
    
    buildSearchForm();
    
    _asyncCompleteFlags[ajaxKey]=true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Rfq Search">
// 查詢表單
function buildSearchForm(){
    createDropdown("#rfqStatus", vo.rfqStatus, {label:utils.i18n("rfq.status"), value:""}, null);// 詢價單狀態
    //$("#startAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //$("#endAt").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    createDatePicker("#startAt");
    createDatePicker("#endAt");
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
}
// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    if( !isEmpty(formData["orderNumber"]) && formData["orderNumber"].trim().length<4 ){
        return;
    }
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    
    buildRfqListLazy(formData, 'rfqList');// Lazy方式抓取資料

    $("#fsRfqDetail").hide();// 訂單明細
    $("#selectedItemHeader").hide();
}
// 清除
function onClickClearSearch(event){
    setDropdownValue('#rfqStatus', "");
    $("#fmSearch")[0].reset();
    
    renderRfqListLazy({}, 0, false, RFQ_LIST_URL);// clear table
    $("#fsRfqDetail").hide();// 訂單明細
    $("#selectedItemHeader").hide();
}
//</editor-fold>
