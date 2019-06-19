/**
 * dependencys : lib/*, consts.js, variables.js, tcc-utils.js, com/*.js
 */
/* global ajaxFlags, _asyncCompleteFlags, PN_CUSTOMER, CUS_COUNT_URL, MSG_NO_RECORD_FOUND, CUS_LIST_URL, PO_COUNT_URL, PO_LIST_URL, CUS_FEEDBACK_URL, OPTIONS_URL, buildInitObjects, TEST_DATA, item, CUS_FULL_URL, CUS_FB_COUNT_URL, CUS_FB_LIST_URL, CUS_FB_SAVE_URL, CUS_SAVE_URL, w3, utils.genNoSelectOpNum(), NO_SELECT_OP_STR, genCreateTimeContent, onClickAddCustomer, CUS_ADD_URL, CUS_CREDITS_LIST_URL, CUS_CREDITS_COUNT_URL, storeId, adminUser, CUS_SAVE_CREDITS_URL, expHeaders, PO_EXP_URL, afterExpOrder, _tccDealer, voOC, utils */
var vo = {// variable objects the JS
    itemSelected:null,// 選取
    cusLevels:[],
    rfqStatus:[],
    orderStatus:[],
    payStatus:[],
    shipStatus:[],
    creditStatus:[],
    supportTranEC10:false// 本頁是否提供轉單EC1.0功能
};
//var vo.itemSelected = null;// 選取
//var supportTranEC10 = false;// 本頁是否提供轉單EC1.0功能

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

    buildCusListLazy(null, "cusListLazy");// Lazy方式抓取資料
    
    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
    $('#fsOrder').hide();
    $("#fsOrderDetail").hide();// 訂單明細    
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();
    
    $('#fsFeedback').hide();
    $('#plMemberMsg').hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsCusList').puifieldset({toggleable: true});
    $('#fsBasic').puifieldset({toggleable: true});
    $('#fsOrder').puifieldset({toggleable: true});
    $('#fsFeedback').puifieldset({toggleable: true});
    $('#fsOrderDetail').puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plBasic', 'plContact']);
    utils.renderMultiPanel(['plOrderItems', 'plOrderShipping', 'plOrderPayment', 'plRfqMessage', 'plOrderProcess']);
    // 客戶留言 - 即首頁[訪客留言]中，此客戶的留言
    var msgTitle = utils.i18n("customer.msg") + " - " + utils.i18n("customer.msg.memo");
    $('#plMemberMsg').puipanel({"title":msgTitle});
}

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=rfqStatus,orderStatus,payStatus,shipStatus,cusLevel,creditStatus,cusFeedback,favCars&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

// 可預先建立的 UI 操作元件
function buildInitObjects(response, ajaxKey){
    vo.cusLevels = utils.getListProp(response, "cusLevel");
    vo.orderStatus = utils.getListProp(response, "orderStatus");
    vo.rfqStatus = utils.getListProp(response, "rfqStatus");
    vo.payStatus = utils.getListProp(response, "payStatus");
    vo.shipStatus = utils.getListProp(response, "shipStatus");
    vo.creditStatus = utils.getListProp(response, "creditStatus");
    // use in order-common.js
    voOC.favCarNoList = utils.getListProp(response, "favCars");
    
    buildSearchForm();
    
    buildCusBasicForm();
    
    _asyncCompleteFlags[ajaxKey]=true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Customer Search">
// 初始表單
function buildSearchForm(){
    // 查詢表單
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    //createBtn('#add', 'fa-plus-square', onClickAddCustomer);// 新增

    createDropdown("#cusLevel", vo.cusLevels, utils.genNoSelectOpNum(), null);// 等級
    createDropdown("#rfqStatus", vo.rfqStatus, {label:utils.i18n("rfq.status"), value:""}, null);// 詢價單狀態
    createDropdown("#orderStatus", vo.orderStatus, {label:utils.i18n("po.status"), value:""}, null);// 訂單狀態
    createDropdown("#payStatus", vo.payStatus, {label:utils.i18n("pay.status"), value:""}, null);// 付款狀態
    createDropdown("#shipStatus", vo.shipStatus, {label:utils.i18n("shipping.status"), value:""}, null);// 出貨狀態
    createDropdown("#creditStatus", vo.creditStatus, utils.genNoSelectOpStr(), null);
}
// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    console.log("onClickSearch formData = ", formData);
    
    buildCusListLazy(formData, "cusListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
    $('#fsOrder').hide();
    $('#fsFeedback').hide();
}
// 清除
function onClickClearSearch(event){
    setDropdownValue('#cusLevel', 0);
    setDropdownValue('#rfqStatus', "");
    setDropdownValue('#orderStatus', "");
    setDropdownValue('#payStatus', "");
    setDropdownValue('#shipStatus', "");
    setDropdownValue('#creditStatus', "");
    
    $("#fmSearch")[0].reset();
    
    renderCusListLazy({}, 0, false, null);// clear table
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
    $('#fsOrder').hide();
    $('#fsFeedback').hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Customer Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildCusListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = CUS_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchCusListLazy, null, ajaxKey);
}

function fetchCusListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;   
    renderCusListLazy(formData, totalRows, false, ajaxKey);
}

function renderCusListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 800;
    var marginWidth = 80;
    var columnAll = [
        {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
        {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
        {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
        {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
        {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
        {field: "active", headerText: utils.i18n("fd.active"), sortable: true, content: genContentActive},
        {field: "levelName", headerText: utils.i18n("fd.levelName"), sortable: true, disabled:_tccDealer},
        //{field: "tel1", headerText: utils.i18n("fd.tel1"), sortable: true},
        //{field: "email1", headerText: utils.i18n("fd.email1"), sortable: true},
        //{field: "addr1", headerText: utils.i18n("fd.addr1"), sortable: true},
        {field: "noPayAmt", headerText: utils.i18n("fd.noPayAmt"), sortable: true, bodyClass:'tar', content: genNoPayAmtContent},
        {field: "totalAmt", headerText: utils.i18n("fd.totalAmt"), sortable: true, bodyClass:'tar', content: genTotalAmtContent},
        {field: "lastBuyDate", headerText: utils.i18n("fd.lastBuyDate"), sortable: true, content: genLastBuyDateContent},
        {field: "firstBuyDate", headerText: utils.i18n("fd.firstBuyDate"), sortable: true, content: genFirstBuyDateContent}
        //{field: "notShipped", headerText: "未出貨", sortable: true, content: genContentNotShipped},
        //{field: "notPay", headerText: "未付款", sortable: true, content: genContentNotPay},
        //{field: "needInvoice", headerText: "待發票", sortable: true, content: genContentNeedInvoice}
    ];

    // 處理自訂屬性 disabled、exported 
    var columns = [];
    for(var i=0; i<columnAll.length; i++){
        if( !utils.isTrue(columnAll[i].disabled) ){
            columns.push(columnAll[i]);
        }
        //if( !isFalse(columnAll[i].exported) ){
        //    voOC.expHeaders.push(columnAll[i].headerText);
        //}
    }
    
    renderSearchResultNum("#searchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbCustomLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbCustomLazy', pageSize, totalRows, columns, CUS_LIST_URL, 
            !TEST_DATA, formData, onSelectCus, retry, ajaxKey, minWidth, marginWidth);
    }
}

// 有效
function genContentActive(data){
    return genContentBoolean(data, "active");
}
function genNoPayAmtContent(row){
    if( !isNA(row.noPayAmt) && row.noPayAmt>0 ){
        return "<font color='red' >"+utils.printNumber(row.noPayAmt)+"</font>";
    }else{
        return 0;
    }
}
function genTotalAmtContent(row){
    return utils.printNumber(row.totalAmt);
}
function genLastBuyDateContent(row){
    return formatDateTimeStr(row.lastBuyDate);
}
function genFirstBuyDateContent(row){
    return formatDateTimeStr(row.firstBuyDate);
}

// 客戶 DataTable 選取項目
function onSelectCus(item){
    if( isNA(item.id) ){
        console.log("onSelectCus item = ", item);
        return;
    }
    vo.itemSelected = item;
    resetSelectedItemNavBar();
    createGoToBtn("#btnGoToBasic", "#fsBasic");
    createGoToBtn("#btnGoToOrder", "#fsOrder");
    //createGoToBtn("#btnGoToFeedback", "#fsFeedback");
    createGoToBtn("#btnCusMessage", "#plMemberMsg");

    // 客戶詳細資料
    var restUrl = CUS_FULL_URL.replace("{id}", item.id);// 客戶ID
    utils.fetchData(restUrl, false, renderCusFullInfo, null, "cusFullInfo");
    
    var formData = {
        "memberId": vo.itemSelected.memberId // 會員ID
    };
    // 客戶訂單 Lazy loading
    buildOrderListLazy(formData, 'orderList');
    $("#fsOrderDetail").hide();// 訂單明細

    // 客戶反映 Lazy loading - for EC_CUS_FEEDBACK 目前無用
    //buildFeedbackList(formData, "cusFeedback");
    // 客戶留言 EC_MEMBER_MSG
    var formData = {};
    if( !isNA(vo.itemSelected.memberId) ){
        formData['memberId'] = vo.itemSelected.memberId;
    }
    buildMemberMsg(formData, "memberMsg");
}

// 產生客戶完整資訊畫面
function renderCusFullInfo(data, ajaxKey){
    if( isNA(data) ){
        console.error("renderCusFullInfo data = ", data);
        return;
    }
    
    // 選取項目
    genSelectedItemTitle(data.cname);
    // 客戶資訊
    renderCusBasic(data);
    // 客戶訂單
    // 客戶反映處理

    $("#selectedItemHeader").show();
    $('#fsBasic').show();
    $('#fsOrder').show();
    $('#fsFeedback').show();
    utils.scrollTo('#selectedItemHeader');
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function genContentNotShipped(data){
    return genContentBoolean(data, "notShipped");
}

function genContentNotPay(data){
    return genContentBoolean(data, "notPay");
}

function genContentNeedInvoice(data){
    return genContentBoolean(data, "needInvoice");
}

// 匯出查詢結果 // expHeaders, afterExpOrder define in order-common.js
function onClickExpOrder() {
    console.log("onClickExpOrder ...");
    var formData = {
        "memberId": vo.itemSelected.memberId // 會員ID
    };
    
    formData["headers"] = voOC.expHeaders;

    var restUrl = PO_EXP_URL;
    utils.postData(restUrl, formData, false, afterExpOrder);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Add Customer">
/*function onClickAddCustomer(){
    var loginAccount = $("#itLoginAccount").val();
    if( isEmpty(loginAccount) ){
        alert(utils.i18n("account.required"));
        return;
    }
    if( !confirm(utils.i18n("confirm.add.cus")) ){
        return;
    }
    var formData = {};
    formData["loginAccount"] = loginAccount;
    console.log("onClickAddCustomer formData = ", formData);
    var restUrl = CUS_ADD_URL;
    utils.postData(restUrl, formData, false, afterAddCus, null, "addCus");
}

function afterAddCus(response, formData, ajaxKey){
    vo.itemSelected = response;
    renderCusBasic(vo.itemSelected);
    reloadDataTable('#tbCustomLazy', true);// keep status reload
    utils.addSuccessMsg();
}*/
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Customer Detail Info">
function buildCusBasicForm(){
    // 客戶基本資訊
    createDropdown("#level", vo.cusLevels, utils.genNoSelectOpNum(), onChangeCusLevel);// 等級
    createBtn('#saveCus', 'fa-save', onClickSaveCus);//
    createBtn('#addCredits', 'fa fa-plus-circle', onClickAddCreditsLog);//
}

function onChangeCusLevel(data){
    console.log("onChangeCusLevel data =", data);
}

function onClickSaveCus(event){
    if( confirm(utils.i18n("change.cus.confirm")) ){
        var formData = {};
        formData["id"] = vo.itemSelected.id;
        formData["levelId"] = getDropdownValue("#level");
        formData["credits"] = getTextValue("#credits");
        if( !isNA(formData["credits"]) ){
            formData["credits"] = utils.getNumber(formData["credits"]);
        }
        console.log("onChangeCusLevel formData = ", formData);
        var restUrl = CUS_SAVE_URL;
        utils.postData(restUrl, formData, false, afterSaveCus, null, "saveCus");
    }
}
function afterSaveCus(response, formData, ajaxKey){
    //if( !utils.checkResponse(response) ){
    //    return;
    //}
    vo.itemSelected = response;
    reloadDataTable('#tbCustomLazy', true);// keep status reload
    renderCusBasic(vo.itemSelected);
    utils.addSuccessMsg();
}

function renderCusBasic(data){
    utils.copyPropsToView(data, null);
    console.log("renderCusBasic loginAccount = " + data.loginAccount);
    console.log("renderCusBasic loginAccount = " + $("#"));

    $("#receiveAd").html(genContentBoolean(data, "receiveAd"));
    setDropdownValue("#level", isNA(data.levelId)?0:data.levelId);
    
    // credits
    $("#credits").val("");
    if( !isNA(data.credits) ){
        $("#credits").val(utils.printNumber(data.credits));
        //$("#addCredits").show();
    }else{
        //$("#addCredits").hide();
    }
    $("#creditsApplyStatus").html("");
    if( !isNA(data.creditsApplyTime) && isNA(data.credits) ){
        $("#creditsApplyStatus").html(utils.i18n("apply.now"));
    }
    
    //$('#btnViewCreditsLog').show();
    createBtn('#btnViewCreditsLog', 'fa-eye', onClickViewCreditsLog);// 檢視庫存
    $('#plMemberMsg').show();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Customer Feedback">
// 直接抓取所有資料，顯示於 datatable，適用於資料量不多
function buildFeedbackList(formData, ajaxKey){
    formData = isNA(formData)?{}:formData;
    _asyncCompleteFlags[ajaxKey]=false;
    
    // 不分頁直接呼叫
    var restUrl = CUS_FB_COUNT_URL;
    utils.postData(restUrl, formData, false, renderFeedbackList, null, ajaxKey);
}

function renderFeedbackList(response, formData, ajaxKey){
    var pageSize = 5;
    var minWidth = 640;
    var marginWidth = 80;
    var totalRows = isNA(response.res.totalRows)? 0:response.res.totalRows;

    var columns = [
            {field: "createtime", headerText: utils.i18n("fd.createtime"), sortable: true, content: genCreateTimeContent},
            {field: "typeName", headerText: utils.i18n("fd.typeName"), sortable: true},
            {field: "content", headerText: utils.i18n("fd.content"), sortable: true, 
                bodyClass:"longTxt", content: genFeedbackContent},
            {field: "processTime", headerText: utils.i18n("fd.processTime"), sortable: true, content: genHandleContent},
            {field: "process", headerText: utils.i18n("fd.process"), sortable: true, content: genProcessContent},
            {field: "closeTime", headerText: utils.i18n("fd.closeTime"), sortable: true, content: genCloseContent}
        ];
    
    var restUrl = CUS_FB_LIST_URL;
    renderSearchResultNum("#fbSearchResultMsg", totalRows);
    renderDataTableLazy('#tbFeedback', pageSize, totalRows, columns, restUrl, 
            true, formData, null, false, ajaxKey, minWidth, marginWidth);
}

// 反映內容
function genFeedbackContent(row){
    return  '   <textarea class="readonly" readonly cols="25" rows="4" placeholder="'+utils.i18n("fd.content")+'" style="vertical-align: middle;" >'+row.content+'</textarea>';
}
// 處理
function genHandleContent(row){
    var html = "";
    if( !isNA(row.processTime) ){
        html += utils.i18n("fd.processor") + "：<br/>" + utils.safePrint(row.processUserName) + "<br/>";
        html += utils.i18n("fd.processTime") + "：<br/>" + formatDateTimeStr(row.processTime);
    }else{
        html += '<p style="width:100%;text-align:center;" ><span class="w3-text-red" ><b>'+utils.i18n("no.process")+'</b></span></p>';
    }
    return html;
}
// 處理內容
function genProcessContent(row){
    var html = "";
    var process = isNA(row.process)?"":row.process;
    if( row.closed ){
        //html = '<p>'+utils.safePrint(process)+'</p>';
        html += '   <textarea class="readonly" readonly cols="27" rows="4" placeholder="'+utils.i18n("in.processtime")+'" style="vertical-align: middle;" >'+process+'</textarea>';
    }else{
        html += '<div>';
        html += '   <textarea id="process'+row.id+'" cols="25" rows="4" placeholder="'+utils.i18n("in.processtime")+'" style="vertical-align: middle;" >'+process+'</textarea>';
        html += '   <button onclick="processFeedback('+row.id+')" class="w3-btn w3-teal w3-round-large w3-padding-small" >'+utils.i18n("save")+'</button>';
        html += '</div>';
        
        $("#process"+row.id).puiinputtextarea({autoResize:true});
    }
    return html;
}
// 結案
function genCloseContent(row){
    var html = "";
    if( row.closed ){
        html += utils.i18n("fd.close.user") + "：<br/>" + utils.safePrint(row.closeUserName) + "<br/>";
        html += utils.i18n("fd.closetime") + "：<br/>" + formatDateTimeStr(row.closeTime) + "<br/>";
        html += '<div style="width:100%;">';
        html += '   <button onclick="cancelCloseFeedback('+row.id+')" class="w3-btn w3-orange w3-round-large w3-padding-small" >' +utils.i18n("cancel.finish")+ '</button>';
        html += '</div>';
    }else{
        html += '<div style="width:100%;text-align:center;">';
        html += '   <span class="w3-text-orange" ><b>' +utils.i18n("no.close")+ '</b></span><br/>';
        html += '   <button onclick="closeFeedback('+row.id+')" class="w3-btn w3-teal w3-round-large w3-padding-small" >' +utils.i18n("setting.close")+ '</button>';
        html += '</div>';
    }
    return html;
}
// 儲存
function saveFeedback(formData){
    formData["memberId"] = vo.itemSelected.memberId;
    var restUrl = CUS_FB_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveFeedback, null, "cusFeedback");
}
function afterSaveFeedback(response, formData, ajaxKey){
    reloadDataTable('#tbFeedback', true);
    
    utils.addSuccessMsg();
}
// 處理
function processFeedback(id){
    var formData = {
        "action": "PROCESS",
        "id": id,
        "process": $('#process'+id).val()
    };
    saveFeedback(formData);
}
// 設為結案
function closeFeedback(id){
    var formData = {
        "action": "CLOSE",
        "id": id
    };
    saveFeedback(formData);
}
// 取消結案
function cancelCloseFeedback(id){
    var formData = {
        "action": "CANCEL",
        "id": id
    };
    saveFeedback(formData);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Customer Credits Log">
// add credits log
function onClickAddCreditsLog() {
    buildAddCreditsLogDlg();
}
function buildAddCreditsLogDlg() {
    console.log("buildAddCreditsLogDlg ...");

    var creditsLogOps = [
        {"label":utils.i18n("credits.type.plus"), "value":1},
        {"label":utils.i18n("credits.type.minus"), "value":-1}
    ];
    createDropdown("#logType", creditsLogOps, null, null);
    setDropdownValue("#logType", 1);

    $('#dlgAddCreditsLog').puidialog({
        "visible": true,
        "width": "auto",
        "height": "auto",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        "buttons": [{
                text: utils.i18n("close"),
                icon: 'fa-close',
                click: function () {
                    $('#dlgAddCreditsLog').puidialog('hide');
                }
            }, {
                text: utils.i18n("save"),
                icon: 'fa-save',
                click: onClickSaveCreditsLogClose
            }],
            "beforeShow":function(event) {
                utils.hideBodyScrollY();
            },
            "afterHide":function(event) {
                utils.showBodyScrollY();
            }
    });

    $("#dlgAddCreditsLog").show();
}


function onClickSaveCreditsLogClose(){
    if (saveCreditsLog()) {
        setDropdownValue("#logType", "");
        setTextValue("#logQuantity", "");
        setTextValue("#logMemo", "");

        $('#dlgAddCreditsLog').puidialog('hide');
    }
}

function saveCreditsLog() {
    var $form = $("#fmAddCreditsLog");
    var formData = $form.serializeFormJSON();
    formData["id"] = vo.itemSelected.id;
    formData["memberId"] = vo.itemSelected.memberId;
    console.log("saveCreditsLog formData = \n", formData);
    
    if( !valiateCreditsLog(formData) ){
        return false;
    }
    if (!confirm(utils.i18n("confirm.change.credits"))) {
        return false;
    }

    var restUrl = CUS_SAVE_CREDITS_URL;
    utils.postData(restUrl, formData, false, afterSaveCreditsLog);
    return true;
}

function valiateCreditsLog(formData) {
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#fmAddCreditsLog");
    if (!isEmpty(msg)) {
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    return true;
}

function afterSaveCreditsLog(response, formData, ajaxKey) {
    afterSaveCus(response, formData, ajaxKey);
}

// view credits log
function onClickViewCreditsLog() {
    fetchCreditsLog();
}
function buildViewCreditsLogDlg() {
    console.log("buildViewCreditsLogDlg ...");

    $('#dlgViewCreditsLog').puidialog({
        "visible": true,
        "width": "auto",
        "height": "auto",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        "buttons": [{
                text: utils.i18n("close"),
                icon: 'fa-close',
                click: function () {
                    $('#dlgViewCreditsLog').puidialog('hide');
                }
            }],
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }

    });

    $("#dlgViewCreditsLog").show();
}
function fetchCreditsLog() {
    // 先取得總筆數 for 分頁
    var formData = {};
    formData["memberId"] = vo.itemSelected.memberId;

    var restUrl = CUS_CREDITS_COUNT_URL;
    // 先查總筆數
    utils.postData(restUrl, formData, false, fetchCreditsLogLazy, null, "creditsLogs");
}
function fetchCreditsLogLazy(response, formData, ajaxKey) {
    var totalRows = response.res.totalRows;
    renderCreditsLogLazy(formData, totalRows, false, ajaxKey);

    buildViewCreditsLogDlg();
}
function renderCreditsLogLazy(formData, totalRows, retry, ajaxKey) {
    var pageSize = 10;
    var minWidth = 380;
    var marginWidth = 0;
    var columns = [
        {field: "createtime", headerText: utils.i18n("fd.credits.time"), sortable: false},
        {field: "creditsOri", headerText: utils.i18n("fd.credits.ori"), sortable: false, content: genCreditsOri},
        {field: "credits", headerText: utils.i18n("fd.credits.after"), sortable: false, content: genCredits},
        {field: "creditsDiff", headerText: utils.i18n("fd.credits.diff"), sortable: false, content: genCreditsDiff},
        {field: "creatorName", headerText: utils.i18n("fd.credits.loguser"), sortable: false}
    ];

    renderSearchResultNum("#clSearchResultMsg", totalRows);
    if (totalRows === 0) {
        clearDataTable('#tbCreditsLog', columns, minWidth, marginWidth, ajaxKey);
    } else {
        renderDataTableLazy('#tbCreditsLog', pageSize, totalRows, columns, CUS_CREDITS_LIST_URL,
                !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genCreditsOri(row){
    return utils.printNumber(row.creditsOri);
}
function genCredits(row){
    return utils.printNumber(row.credits);
}
function genCreditsDiff(row){
    return utils.printNumber(row.creditsDiff);
}
//</editor-fold>

// common render after search
function commonRenderAfterOrderSearch(){
    //$("#selectedItemHeader").hide();
    $("#fsOrderDetail").hide();// 訂單明細
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();
}
