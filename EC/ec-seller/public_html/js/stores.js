/* global w3, PN_STORE, _asyncCompleteFlags, ST_PAY_URL, ST_PAY_SAVE_URL, ST_PAY_DEL_URL, ST_SHIP_URL, ST_SHIP_SAVE_URL, ST_SHIP_DEL_URL, OPS_URL, renderCusLevel, renderFeedback, OPS_SAVE_URL, OPS_DEL_URL, STORE_SAVE_URL, STORE_DEL_URL, STORE_FULL_URL, ST_LOGO_UPLOAD_URL, ST_BANNER_UPLOAD_URL, self, ST_LOGO_DEL_URL, ST_PIC_DEL_URL, STORE_LIST_URL, TEST_DATA, STORE_COUNT_URL, FIELD_UUID, OPTIONS_URL, renderSimpleListStr, ST_AREA_SAVE_URL, STORE_ADD_URL, STORE_DEF_URL, _myStores, STORE_OPEN_URL, _tccDealer, STORE_DEF_ATTR_URL, TCC_ENABLED, _adminUser, MEM_COUNT_URL, MEM_LIST_URL, genContentAdminCB, genContentFinCB, utils */
/**
 * dependencys : lib/*, consts.js, variables.js, tcc-utils.js, com/*.js
 */
var vo = {// variable objects the JS
    urlPrefix: null,// 前置網址 for 圖片顯示、檔案下載
    itemSelected: null,// 選取
    logo: null,
    banner: null,
    paymentList: null,
    shippingList: null,
    prdUnitList: null,
    areaList: null,
    shippingOps: [],// 商品運送選項
    paymentOps: [],// 商品付款選項
    areaOps: [],// 銷售區域選項
    // FormListM object
    storeBasicFL: null,
    storePaymentFL: null,
    storeShippingFL: null,
    storeStoreFL: null,
    storeBrandFL: null,
    storePrdUnitFL: null,
    storeCusLevelFL: null,
    storeFeedbackFL: null,
    // 銷售區域選項(for Search)
    areaSearchList: null
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
    initLayout(_adminUser, true, _adminUser);
    renderPanels();
    
    buildBasicForm();
    buildUploadForm();

    // Ajax 產出UI相關
    if( _adminUser ){
        $("#selectedItemContent").hide();
        $("#selectedItemHeader").hide();

        buildSearchForm();
        buildStoreListLazy({}, "stores");
    }
    
    fetchInitOptions("init");

    console.log(utils.formatDT(new Date()));
}

function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsStoreList').puifieldset({toggleable: true});
    $('#fsBasic').puifieldset({toggleable: true});
    $('#fsPicture').puifieldset({toggleable: true});
    $('#fsManager').puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plPayment', 'plShipping', 'plStoreArea', 'plCusLevel', 'plFeedback', 'plBrand', 'plPrdUnit', 'plLogo', 'plBanner']);
}

//<editor-fold defaultstate="collapsed" desc="for store common method">
function genCommonTableHead(){
    var html = '<thead><tr class="ui-widget-header ui-state-default">';
    if( !_adminUser ){
        html += '   <th width="260">'+utils.i18n("operation")+'</th><th>'+utils.i18n("cname")+'</th>';
    }else{
        html += '   <th>'+utils.i18n("cname")+'</th>';
    }
    html += '</tr></thead>';
    
    return html;
}

function genCommonTableRow(nameFL, i, value){
    var html = '<tr class="w3-padding-small w3-hover-pale-blue">';
    if( !_adminUser ){
        html += '    <td class="fit-content">'
                + '     <button type="button" onclick="'+nameFL+'.onClickEdit('+ i +')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal" ><i class="fa fa-pencil-square"></i></button>'
                + '     <button type="button" onclick="'+nameFL+'.onClickDelete('+ i +')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>'
                + '     <button type="button" onclick="'+nameFL+'.onClickUp('+ i +')" title="'+utils.i18n("move.up")+'" class="w3-btn w3-khaki" ><i class="fa fa-arrow-up"></i></button>'
                + '     <button type="button" onclick="'+nameFL+'.onClickDown('+ i +')" title="'+utils.i18n("move.down")+'" class="w3-btn w3-green" ><i class="fa fa-arrow-down"></i></button>'
                + '    </td>';
    }
    html += '    <td>' + utils.safePrint(value) + '</td>'
            + '</tr>';

    return html;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + (_adminUser?"?keys=salesArea":"?keys=shippingSys,paymentSys,salesArea");
    restUrl = restUrl + "&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

function buildInitObjects(response, ajaxKey) {
    if (isNA(response)) {
        console.assert("buildInitObjects error response isNA!");
        return;
    }
    
    vo.areaOps = isNA(response.salesArea)? [] : response.salesArea;// search & edit

    if( !_adminUser ){
        vo.shippingOps = isNA(response.shippingSys) ? [] : response.shippingSys;
        vo.paymentOps = isNA(response.paymentSys) ? [] : response.paymentSys;
        fetchStoreInfoFull(0, "storeFull");
    }else{
        buildSearchAreaPickList([]);
    }
    
    _asyncCompleteFlags[ajaxKey] = true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Search">
// 初始表單
function buildSearchForm(){
    // 查詢表單
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
}
// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["areaList"] = utils.toIdList(vo.areaSearchList, "value");
    
    buildStoreListLazy(formData, "storeListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
}
// 清除
function onClickClearSearch(event){
    $("#fmSearch")[0].reset();

    renderStoreListLazy({}, 0, false, null);// clear table
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildStoreListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = STORE_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchStoreListLazy, null, ajaxKey);
}

function fetchStoreListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderStoreListLazy(formData, totalRows, false, ajaxKey);
}

function renderStoreListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 800;
    var marginWidth = 80;
    var columnAll = [
            {field: "storeId", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "cname", headerText: utils.i18n("name.ch"), sortable: true},
            {field: "ename", headerText: utils.i18n("name.en"), sortable: true, content: genWideColEname},
            {field: "opened", headerText: utils.i18n("fd.store.opened"), sortable: true, bodyClass:"tac", content: genContentOpened},
            {field: "tccDealer", headerText: utils.i18n("fd.tccDealer"), sortable: true, 
                    bodyClass:"tac", content: genContentTccDealer, disabled:!TCC_ENABLED},
            {field: "managerCount", headerText: utils.i18n("fd.manager.count"), sortable: true, bodyClass:"tar"},
            {field: "loginAccount", headerText: utils.i18n("fd.seller.account"), sortable: true},
            {field: "name", headerText: utils.i18n("fd.seller.name"), sortable: true},
            {field: "idCode", headerText: utils.i18n("fd.idCode"), sortable: true},
            {field: "tel1", headerText: utils.i18n("fd.tel1"), sortable: true},
            {field: "email1", headerText: utils.i18n("fd.email1"), sortable: true},
            {field: "addr1", headerText: utils.i18n("fd.addr1"), sortable: true}
        ];

    // 處理自訂屬性 disabled、exported 
    var columns = [];
    for(var i=0; i<columnAll.length; i++){
        if( !utils.isTrue(columnAll[i].disabled) ){
            columns.push(columnAll[i]);
        }
    }
    
    renderSearchResultNum("#searchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbStoreLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbStoreLazy', pageSize, totalRows, columns, STORE_LIST_URL, 
            !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genWideColEname(data){
    return "<div class='wide-col' >"+utils.prettyPrint(data.ename)+"</div>";
}

function genContentOpened(data){
    return genContentBoolean(data, "opened");
}

function genContentTccDealer(data){
    return genContentBoolean(data, "tccDealer");
}

// DataTable 選取項目
function onSelectRow(item){
    if( isNA(item.storeId) ){
        console.log("onSelectRow item = ", item);
        return;
    }
    vo.itemSelected = item;
    resetSelectedItemNavBar();
    // 詳細資料
    //renderStoreFullInfo(itemSelected, "storeFull");
    fetchStoreInfoFull(item.storeId, "storeFull");
}

function fetchStoreInfoFull(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = STORE_FULL_URL.replace("{id}", storeId);
    utils.fetchData(restUrl, false, renderStoreFullInfo, null, ajaxKey);
}

// 產生供應商完整資訊畫面
function renderStoreFullInfo(response, ajaxKey){
    if( isNA(response) || isNA(response.storeId) ){
        console.error("renderStoreFullInfo response = ", response);
        return;
    }

    // 選取項目
    genSelectedItemTitle(response.cname);
    // 商店基本資料
    renderStoreBasic(response, ajaxKey);

    var shippings = utils.safeList(response.shippings);
    var payments = utils.safeList(response.payments);
    var areas = utils.safeList(response.areas);
    var prdBrands = utils.safeList(response.prdBrand);
    var prdUnits = utils.safeList(response.prdUnit);
    var cusLevels = utils.safeList(response.cusLevel);
    var cusFeedbacks = utils.safeList(response.cusFeedback);
    
    buildShippingPickList(shippings);
    buildPaymentPickList(payments);
    buildAreaPickList(areas);

    renderBrand(prdBrands, ajaxKey);
    renderPrdUnit(prdUnits, ajaxKey);
    renderCusLevel(cusLevels, ajaxKey);
    //renderFeedback(cusFeedbacks, ajaxKey);// 客戶意見類別 - for EC_CUS_FEEDBACK 目前無用
    
    //fetchStoreBasic(storeId, "basic");
    //fetchPayment(storeId, "payment");// 付款方式
    //fetchShipping(storeId, "shipping");// 運貨方式
    //fetchBrand(storeId, "prdBrand");// 商品品牌
    //fetchPrdUnit(storeId, "prdUnit");// 商品計量單位
    //fetchCusLevel(storeId, "cusLevel");// 客戶等級分類
    //fetchFeedback(storeId, "cusFeedback");// 客戶意見類別
    // 商店管理員
    buildManagerListLazy({"storeManager":true}, "memberListLazy");// Lazy方式抓取資料

    if( _adminUser ){
        // create goto button
        createGoToBtn("#btnGoToBasic", "#fsBasic");
        createGoToBtn("#btnGoToPicture", "#fsPicture");
        createGoToBtn("#btnGoToPayment", "#plPayment");
        createGoToBtn("#btnGoToShipping", "#plShipping");
        createGoToBtn("#btnGoToArea", "#plStoreArea");
        createGoToBtn("#btnGoToCusLevel", "#plCusLevel");
        //createGoToBtn("#btnGoToFeedback", "#plFeedback");
        createGoToBtn("#btnGoToBrand", "#plBrand");
        createGoToBtn("#btnGoToPrdUnit", "#plPrdUnit");

        $("#selectedItemHeader").show();
        $("#selectedItemContent").show();
        utils.scrollTo('#selectedItemHeader');
    }else{
        if( !isNA(response.defStore) && response.defStore ){
            if( $('#btnSetDef') ){
                disableBtn('#btnSetDef');
            }
        }
    }
    
    _asyncCompleteFlags[ajaxKey]=true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Fetch Store Info">
function fetchStoreBasic(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = STORE_FULL_URL.replace("{id}", storeId);
    utils.fetchData(restUrl, false, renderStoreBasic, null, ajaxKey);
}
function fetchPayment(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = ST_PAY_URL + (storeId>0?"?storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchStorePayment, null, ajaxKey);
}
function fetchShipping(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = ST_SHIP_URL + (storeId>0?"?storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchStoreShipping, null, ajaxKey);
}
function fetchBrand(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = OPS_URL + "?type=prdBrand" + (storeId>0?"&storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchBrand, null, ajaxKey);
}
function fetchPrdUnit(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = OPS_URL + "?type=prdUnit" + (storeId>0?"&storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchPrdUnit, null, ajaxKey);
}
function fetchCusLevel(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = OPS_URL + "?type=cusLevel" + (storeId>0?"&storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchCusLevel, null, ajaxKey);
}
function fetchFeedback(storeId, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var restUrl = OPS_URL + "?type=cusFeedback" + (storeId>0?"&storeId="+storeId:"");
    utils.fetchData(restUrl, false, afterFetchFeedback, null, ajaxKey);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Add Store">
function onClickAdd(){
    console.log("onClickAdd ...");
    buildAddStoreDlg();
}

function buildAddStoreDlg() {
    console.log("buildAddStoreDlg ...");
    $('#dlgAddStore').puidialog({
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
                    $('#dlgAddStore').puidialog('hide');
                }
                },{
                    text: utils.i18n("save"),
                    icon: 'fa-save',
                    click: onClickAddStore
                }],
                "beforeShow":function(event) {
                    utils.hideBodyScrollY();
                },
                "afterHide":function(event) {
                    utils.showBodyScrollY();
                }
    });

    $("#dlgAddStore").show();
}

function onClickAddStore(){
    var storeName = getTextValue("#cnameN");
    console.log("onClickSaveNewStore storeName = ", storeName);
    
    if( isEmpty(storeName) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("hit.msg"), "#msgAddStore");
        return;
    }
    
    var formData = {
            "cname": storeName
        };
    var restUrl = STORE_ADD_URL;
    utils.postData(restUrl, formData, false, afterAddStore);
}

function afterAddStore(response){
    console.log("afterAddStore ...\n", response);
    if( isNA(response.storeId) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("txt.MSG_NOT_DATA"));
        return;
    }

    $("#dlgAddStore").hide();
    utils.onSwitchStore({"value":response.storeId});
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Basic">
// 初始表單
function buildBasicForm(){
    if( _adminUser ){
        $("#save").hide();
        $("#cancel").hide();
        
        if( TCC_ENABLED ){
            createBtn('#btnDefStoreAttrs', "fa-th-list", onClickDefAttrs);
        }
    }else{
        createBtn('#btnOpenStore', 'fa-check-circle ', onClickOpen);
        createBtn('#btnCloseStore', 'fa-times-circle ', onClickClose);
        
        createBtn('#save', 'fa-save', onClickSave);// 儲存
        createBtn('#cancel', 'fa-ban', onClickReset);// 取消
        createBtn('#btnAddStore', 'fa-plus-square', onClickAdd);// 新增商店
        
        createBtn('#btnSetDef', 'fa-save', onClickToDef);// 儲存
    }

    $("#brief").puiinputtextarea({autoResize:true});
}

// 產生預設屬性
function onClickDefAttrs(){
    if( confirm(utils.i18n("confirm.def.attr")) ){
        var restUrl = STORE_DEF_ATTR_URL;
        var formData = {};
        if( !isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId) ){
            formData["storeId"] = vo.itemSelected.storeId;
        }
        utils.postData(restUrl, formData, false, afterSave, null, "defAttrs");
    }
}

// 開店
function onClickOpen(){
    if( !checkOpenStore() ){// 檢核不過恢復
        return;
    }
    if( confirm(utils.i18n("confirm.open.store")) ){
        var restUrl = STORE_OPEN_URL;
        var formData = {"opened":true};
        utils.postData(restUrl, formData, false, afterSave, null, "openStore");
    }
}

// 關店
function onClickClose(){
    if( confirm(utils.i18n("confirm.close.store")) ){
        var restUrl = STORE_OPEN_URL;
        var formData = {"opened":false};
        utils.postData(restUrl, formData, false, afterSave, null, "openStore");
    }
}

// 設為預設商店
function onClickToDef(){
    var restUrl = STORE_DEF_URL;
    var formData = {};
    utils.postData(restUrl, formData, false, afterSave, null, "toDefStore");
}

// 顯示基本資料
function renderStoreBasic(response, ajaxKey){
    vo.itemSelected = response;
    
    $('#editStoreForm')[0].reset();
    $("#btnLogo").html("");
    $("#btnBanner").html("");
    $("#selLogo").html("");
    $("#selBanner").html("");
    $("#logoImg").html("");
    $("#bannerImg").html("");
    
    utils.copyPropsToForm(response);
    var opened = isNA(response.opened)? false:response.opened;
    if( opened ){
        $("#btnOpenStore").hide();
        $("#btnCloseStore").show();
        $("#openStatusLabel").html(utils.i18n("store.opened"));
        $("#openStatusLabel").attr("class", "w3-blue w3-large");
    }else{
        $("#btnOpenStore").show();
        $("#btnCloseStore").hide();
        $("#openStatusLabel").html(utils.i18n("store.closed"));
        $("#openStatusLabel").attr("class", "w3-red w3-large");
    }
    
    $("#prate").html("<font color='green' >"+response.prate+"</font>");
    $("#nrate").html("<font color='red' >"+response.nrate+"</font>");
    $("#favCount").html("<font color='red' >"+response.favCount+"</font>");
    $("#favPrdCount").html("<font color='red' >"+response.favPrdCount+"</font>");
    
    vo.urlPrefix = response.res.urlPrefix;
    vo.logo = isNA(response.logo)?null:response.logo;
    vo.banner = isNA(response.banner)?null:response.banner;
    renderLogo();
    renderBanner();
    
    if( _adminUser ){
        $('#editStoreForm input').each(function() {
            if ($(this).attr('disabled')) {
                $(this).removeAttr('disabled');
            }else {
                $(this).attr({'disabled': 'disabled'});
            }
        });
        
        $("#editStoreForm .ui-state-disabled").css("opacity", ".9");
    }
    _asyncCompleteFlags[ajaxKey]=true;
}

// 儲存基本資料
function onClickSave(event){
    var $form = $("#editStoreForm");
    var formData = $form.serializeFormJSON();
    if( !isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId) ){
        formData["storeId"] = vo.itemSelected.storeId;
    }
    formData["opened"] = vo.itemSelected.opened;
    console.log("onClickSave formData =\n", formData);
    
    if( !valiateBasic(formData) ){
        return;
    }

    var restUrl = STORE_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave, null, "saveBasic");
}

function afterSave(response){
    vo.itemSelected = response;
    //renderStoreBasic(itemSelected);
    renderStoreFullInfo(response);

    if( _adminUser ){
        reloadDataTable('#tbStoreLazy', true);// keep status reload
    }
    
    utils.addSuccessMsg();
}

function valiateBasic(formData){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editStoreForm");
    if( !isEmpty(msg) ){
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    if( !isNA(formData.brief) && formData.brief.length>500 ){
        msg = utils.i18n("brief.max");
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }

    // 開店檢核
    if( formData['opened']===true ){
        if( !checkOpenStore() ){
            return false;
        }
    }

    return true;
}

function checkOpenStore(){
    if( utils.isEmptyAry(vo.paymentList) || utils.isEmptyAry(vo.shippingList) 
     || utils.isEmptyAry(vo.prdUnitList) 
     // || (_ utils.isEmptyAry(areaList) // 銷售區域
     ){
        var msg = utils.i18n("open.store.err");
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    return true;
}


// 重設基本資料 Form
function onClickReset(event){
    if( confirm(utils.i18n("cancel.edit")) ){
        if( !isNA(vo.itemSelected) ){
            renderStoreBasic(vo.itemSelected);
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Manager">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildManagerListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = MEM_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    formData["manageStoreId"] = storeId;
    utils.postData(restUrl, formData, false, fetchManagerListLazy, null, ajaxKey);
}

function fetchManagerListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderManagerListLazy(formData, totalRows, false, ajaxKey);
}

function renderManagerListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 600;
    var marginWidth = 80;
    var columns = [
            {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
            {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
            {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
            {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
            {field: "managerId", headerText: utils.i18n('super.user'), sortable: false, bodyClass:"tac", content: genContentAdminCB},
            {field: "managerId", headerText: utils.i18n('close.po.user'), sortable: false, bodyClass:"tac", content: genContentFinCB}
    ];
    
    renderSearchResultNum("#searchManagerResultMsg", totalRows);    
    if( totalRows===0 ){
        clearDataTable('#tbManagerLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbManagerLazy', pageSize, totalRows, columns, MEM_LIST_URL, 
            !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genContentAdminCB(data){
    return genContentBoolean(data, "storeOwner");
}

function genContentFinCB(data){
    return genContentBoolean(data, "fiUser");
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Pictures">
function buildUploadForm(){
    if( _adminUser ){
        $('#divLogoOption').hide();
        $('#divBannerOption').hide();
    }else{
        // upload
        //$('#btnUpload').hide();
        $('#fiLogo', '#fmUploadPic').change(onChangeLogo);
        createBtn('#btnUploadLogo', null, onClickUploadLogo);
        createBtn('#btnCancelLogo', null, onClickCancelLogo);

        $('#fiBanner', '#fmUploadPic').change(onChangeBanner);
        createBtn('#btnUploadBanner', null, onClickUploadBanner);
        createBtn('#btnCancelBanner', null, onClickCancelBanner);
    }
}

function onClickCancelLogo(){
    $("#btnLogo").html("");
    $("#selLogo").html("");
    $("#logoImg").html("");
    disableBtn('#btnUploadLogo');
    disableBtn('#btnCancelLogo');
    
    renderLogo();
}

function uploadPicture(restUrl, fileSelector, ori, callback){
    var formData = new FormData();
    formData.append("files", $(fileSelector)[0].files[0]);
    formData.append("filename", $(fileSelector)[0].files[0].name);
    formData.append("fileContentType", $(fileSelector)[0].files[0].type);
    if( !isNA(ori) && !isNA(ori.id) ){
        formData.append("id", ori.id);
    }
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    formData.append("storeId", storeId);

    utils.uploadFiles(restUrl, formData, callback, null);
}

function onChangeLogo(){
    $("#selLogo").html($("#fiLogo")[0].files[0].name);
    $("#logoImg").html("");
    $("#btnLogo").html("");
    enableBtn('#btnUploadLogo');
    enableBtn('#btnCancelLogo');
}

function onClickUploadLogo(){
    if( isNA($("#fiLogo")[0].files[0]) ){
        _alert(utils.i18n("nosel.upload"));
        return;
    }
    console.log("onClickUploadLogo files = ", $("#fiLogo")[0].files[0]);
    uploadPicture(ST_LOGO_UPLOAD_URL, "#fiLogo", vo.logo, afterUploadLogo);
}

function deleteLogo(){
    if( !isNA(vo.logo) && !isNA(vo.logo.id) ){
        if( confirm(utils.i18n("remove.confirm")) ){
            var formData = {"id": vo.logo.id};
            var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
            formData["storeId"] = storeId;
            utils.postData(ST_PIC_DEL_URL, formData, false, afterDeleteLogo, null, null);
        }
    }else{
        console.log("deleteLogo logo = ", vo.logo);
    }
}

function afterDeleteLogo(){
    vo.logo = null;
    $("#btnLogo").html("");
    $("#selLogo").html("");
    $("#logoImg").html("");
    disableBtn('#btnUploadLogo');
    disableBtn('#btnCancelLogo');
}

function afterUploadLogo(data, formData, optional){
    if( !isNA(data) && !isNA(data.filename) ){
        vo.logo = data;
        renderLogo();
    }else{
        vo.logo = null;
    }
}

function renderLogo(){
    if( !isNA(vo.logo) && !isNA(vo.logo.url) ){
        $("#selLogo").html(vo.logo.filename);
        var html = '&nbsp;<button type="button" onclick="deleteLogo()" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>';
        $("#btnLogo").html(html);
        
        var url = utils.safePrint(vo.urlPrefix + vo.logo.url);
        createImgLightBox('#logoImg', url, url, utils.safePrint(vo.logo.filename), null, utils.isSmallScreen()?'100':'130');
    }
    if( !_adminUser ){
        disableBtn('#btnUploadLogo');
        disableBtn('#btnCancelLogo');
    }
}

// Banner
function onClickCancelBanner(){
    $("#btnBanner").html("");
    $("#selBanner").html("");
    $("#bannerImg").html("");
    disableBtn('#btnUploadBanner');
    disableBtn('#btnCancelBanner');
    
    renderBanner();
}

function onChangeBanner(){
    $("#selBanner").html($("#fiBanner")[0].files[0].name);
    $("#bannerImg").html("");
    $("#btnBanner").html("");
    enableBtn('#btnUploadBanner');
    enableBtn('#btnCancelBanner');
}

function onClickUploadBanner(){
    if( isNA($("#fiBanner")[0].files[0]) ){
        _alert(utils.i18n("nosel.upload"));
        return;
    }
    console.log("onClickUploadBanner files = ", $("#fiBanner")[0].files[0]);
    uploadPicture(ST_BANNER_UPLOAD_URL, "#fiBanner", vo.banner, afterUploadBanner);
}

function afterUploadBanner(data, formData, optional){
    if( !isNA(data) && !isNA(data.filename) ){
        vo.banner = data;
        renderBanner();
    }else{
        vo.banner = null;
    }
}

function renderBanner(){
    if( !isNA(vo.banner) && !isNA(vo.banner.url) ){
        $("#selBanner").html(vo.banner.filename);
        var html = '&nbsp;<button type="button" onclick="deleteBanner()" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>';
        $("#btnBanner").html(html);
        
        var url = utils.safePrint(vo.urlPrefix + vo.banner.url);
        createImgLightBox('#bannerImg', url, url, utils.safePrint(vo.banner.filename), null, utils.isSmallScreen()?'100':'130');
    }
    if( !_adminUser ){
        disableBtn('#btnUploadBanner');
        disableBtn('#btnCancelBanner');
    }
}

function deleteBanner(){
    if( !isNA(vo.banner) && !isNA(vo.banner.id) ){
        if( confirm(utils.i18n("remove.confirm")) ){
            var formData = {"id": vo.banner.id};
            var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
            formData["storeId"] = storeId;
            console.log("deleteBanner formData = \n", formData);
            utils.postData(ST_PIC_DEL_URL, formData, false, afterDeleteBanner, null, null);
        }
    }else{
        console.log("deleteBanner banner = ", vo.banner);
    }
}

function afterDeleteBanner(){
    vo.banner = null;
    $("#btnBanner").html("");
    $("#selBanner").html("");
    $("#bannerImg").html("");
    disableBtn('#btnUploadBanner');
    disableBtn('#btnCancelBanner');
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Shipping">
function buildShippingPickList(shipMethods){
    vo.shippingList = utils.listToOptions(shipMethods, "code", "title");
    
    // 配送方式 shippings
    var shippingPickList = new SimplePickList('#shippings', '#editShippings', '#btEditShipping', '#saveShipping', '#closeShipping',
            '#pklShipping', vo.shippingOps, vo.shippingList,
            renderSimpleListStr, null, onClickSaveShipping, null);
    shippingPickList.inti();
}

// 儲存配送方式
function onClickSaveShipping(targetList) {
    var formData = {};
    formData['shipMethods'] = isNA(targetList) ? [] : targetList;

    var restUrl = ST_SHIP_SAVE_URL;
    formData["storeId"] = _adminUser ? vo.itemSelected.id : null;
    utils.postData(restUrl, formData, false, afterSaveShipping, null, null);
}

function afterSaveShipping(response) {
    buildShippingPickList(utils.getResponseList(response));
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Payment">
function buildPaymentPickList(payMethods){
    vo.paymentList = listToOptions(payMethods, "code", "title");
    
    // 付款方式
    var paymentPickList = new SimplePickList('#payments', '#editPayments', '#btEditPayment', '#savePayment', '#closePayment',
            '#pklPayment', vo.paymentOps, vo.paymentList,
            renderSimpleListStr, null, onClickSavePayment, null);
    paymentPickList.inti();
}
    
// 付款方式
function onClickSavePayment(targetList) {
    var formData = {};
    formData['payMethods'] = isNA(targetList) ? [] : targetList;

    var restUrl = ST_PAY_SAVE_URL;
    formData["storeId"] = _adminUser ? vo.itemSelected.id : null;
    utils.postData(restUrl, formData, false, afterSavePayment, null, null);
}

function afterSavePayment(response) {
    buildPaymentPickList(utils.getResponseList(response));
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Area">
// for search
function buildSearchAreaPickList(storeAreas){
    vo.areaSearchList = storeAreas;
    
    var areaPickList = new SimplePickList('#searchAreas', '#editSearchAreas', '#btEditSearchArea', '#setSearchArea', '#closeSearchArea',
            '#pklSearchArea', vo.areaOps, vo.areaSearchList,
            renderSimpleListStr, null, onClickSetSearchArea, null);
    areaPickList.inti();
}
    
function onClickSetSearchArea(targetList){
    buildSearchAreaPickList(targetList);
    vo.areaSearchList = targetList;
}

// for edit
function buildAreaPickList(storeAreas){
    vo.areaList = listToOptions(storeAreas, "areaId", "areaCname");
    
    var areaPickList = new SimplePickList('#storeAreas', '#editStoreAreas', '#btEditStoreArea', '#saveStoreArea', '#closeStoreArea',
            '#pklStoreArea', vo.areaOps, vo.areaList,
            renderSimpleListStr, null, onClickSaveArea, null);
    areaPickList.inti();
}
    
function onClickSaveArea(targetList) {
    var formData = {};
    formData['storeAreas'] = isNA(targetList) ? [] : targetList;

    var restUrl = ST_AREA_SAVE_URL;
    formData["storeId"] = _adminUser ? vo.itemSelected.id : null;
    utils.postData(restUrl, formData, false, afterSaveArea, null, null);
}

function afterSaveArea(response) {
    buildAreaPickList(utils.getResponseList(response));
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Shipping OLD">
function afterFetchStoreShipping(response, ajaxKey){
    vo.shippingList = utils.getResponseList(response);
    renderStoreShipping(vo.shippingList, ajaxKey);
}
function renderStoreShipping(shippingList, ajaxKey){
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;

    vo.storeShippingFL = new FormListM("storeShippingFL", // this object name
                        '#editShippingForm', '#existedShippings', '#confirmShipping', '#saveShipping', '#cancelShipping', // selector
                        shippingList, // data list
                        ST_SHIP_URL+"?storeId="+storeId,
                        ST_SHIP_SAVE_URL+"?storeId="+storeId,
                        ST_SHIP_DEL_URL+"?storeId="+storeId, // rest url
                        renderShippingList, // call back function
                        null, onEditShippingCB, afterSaveShippingCB, null, // call back function
                        true, true,
                        getEditShippingFormData, getSaveShippingFormData, // get form input data function
                        valiateEditShipping, valiateSaveShipping // validate form function
                    );
    vo.storeShippingFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderShippingList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].title);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditShippingCB(row){
    setTextValue("#titleShipping", row.title);
}

function getEditShippingFormData(formData){
    return formData;
}

// 驗證商品規格屬性
function valiateEditShipping(formData){
    if( isEmpty(formData.title) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storeShippingFL.list, formData, FIELD_UUID, "title", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSaveShippingFormData(list){
    var formData = {};
    //formData["typeId"] = (isNA(itemSelected) || isNA(itemSelected.id))?null:itemSelected.id;
    formData["shippingList"] = utils.safeList(list);
    return formData;
}

function valiateSaveShipping(formData){
    if( utils.isEmptyAry(formData["shippingList"]) ){
        // do nothiing
        return false;
    }
    return true;
}

function afterSaveShippingCB(){
    console.log("afterSaveShippingCB ....");
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Store Payment OLD">
function afterFetchStorePayment(response, ajaxKey){
    vo.paymentList = utils.getResponseList(response);
    renderStorePayment(vo.paymentList, ajaxKey);
}
function renderStorePayment(response, ajaxKey){
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    
    vo.storePaymentFL = new FormListM("storePaymentFL", // this object name
                        '#editPaymentForm', '#existedPayments', '#confirmPayment', '#savePayment', '#cancelPayment', // selector
                        vo.paymentList, // data list
                        ST_PAY_URL+"?storeId="+storeId,
                        ST_PAY_SAVE_URL+"?storeId="+storeId,
                        ST_PAY_DEL_URL+"?storeId="+storeId, // rest url
                        renderPaymentList, // call back function
                        null, onEditPaymentCB, afterSavePaymentCB, null, // call back function
                        true, true,
                        getEditPaymentFormData, getSavePaymentFormData, // get form input data function
                        valiateEditPayment, valiateSavePayment // validate form function
                    );
    vo.storePaymentFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderPaymentList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].title);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditPaymentCB(row){
    setTextValue("#titlePayment", row.title);
}

function getEditPaymentFormData(formData){
    return formData;
}

function valiateEditPayment(formData){
    if( isEmpty(formData.title) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storePaymentFL.list, formData, FIELD_UUID, "title", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSavePaymentFormData(list){
    var formData = {};
    //formData["typeId"] = (isNA(itemSelected) || isNA(itemSelected.id))?null:itemSelected.id;
    formData["paymentList"] = utils.safeList(list);
    return formData;
}

function valiateSavePayment(formData){
    if( utils.isEmptyAry(formData["paymentList"]) ){
        // do nothiing
        return false;
    }
    return true;
}

function afterSavePaymentCB(){
    console.log("afterSavePaymentCB ....");
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for brand">
function afterFetchBrand(response, ajaxKey){
    var list = utils.getResponseList(response);
    renderBrand(list, ajaxKey);
}
function renderBrand(list, ajaxKey){
    console.log("renderBrand list =\n", list);
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))?vo. itemSelected.storeId:0;
    
    vo.storeBrandFL = new FormListM("storeBrandFL", // this object name
                        '#editBrandForm', '#existedBrands', '#confirmBrand', '#saveBrand', '#cancelBrand', // selector
                        list, // data list
                        OPS_URL+"?type=prdBrand&storeId="+storeId,
                        OPS_SAVE_URL+"?type=prdBrand&storeId="+storeId,
                        OPS_DEL_URL+"?type=prdBrand&storeId="+storeId,  // rest url
                        renderBrandList, // call back function
                        null, onEditBrandCB, afterSaveBrandCB, null, // call back function
                        true, true,
                        getEditBrandFormData, getSaveBrandFormData, // get form input data function
                        valiateEditBrand, valiateSaveBrand // validate form function
                    );
    vo.storeBrandFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderBrandList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].cname);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditBrandCB(row){
    setTextValue("#titleBrand", row.cname);
}

function getEditBrandFormData(formData){
    return formData;
}

// 驗證
function valiateEditBrand(formData){
    console.log("valiateEditBrand formData =\n", formData);
    console.log("valiateEditBrand storeBrandFL.list =\n", vo.storeBrandFL.list);
    if( isEmpty(formData.cname) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storeBrandFL.list, formData, FIELD_UUID, "cname", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSaveBrandFormData(list){
    var formData = {};
    formData["optionList"] = utils.safeList(list);
    return formData;
}

function valiateSaveBrand(formData){
    if( utils.isEmptyAry(formData["optionList"]) ){
        return false;
    }
    return true;
}

function afterSaveBrandCB(){
    //console.log("afterSaveBrandCB response = \n", response);
    //renderBrand(response, "brandList");
    utils.addSuccessMsg();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Unit">
function afterFetchPrdUnit(response, ajaxKey){
    var list = utils.getResponseList(response);
    renderPrdUnit(list, ajaxKey);
}
function renderPrdUnit(list, ajaxKey){
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    vo.prdUnitList = list;
    
    vo.storePrdUnitFL = new FormListM("storePrdUnitFL", // this object name
                        '#editPrdUnitForm', '#existedPrdUnits', '#confirmPrdUnit', '#savePrdUnit', '#cancelPrdUnit', // selector
                        list, // data list
                        OPS_URL+"?type=prdUnit&storeId="+storeId,
                        OPS_SAVE_URL+"?type=prdUnit&storeId="+storeId,
                        OPS_DEL_URL+"?type=prdUnit&storeId="+storeId,  // rest url
                        renderPrdUnitList, // call back function
                        null, onEditPrdUnitCB, afterSavePrdUnitCB, null, // call back function
                        true, true,
                        getEditPrdUnitFormData, getSavePrdUnitFormData, // get form input data function
                        valiateEditPrdUnit, valiateSavePrdUnit // validate form function
                    );
    vo.storePrdUnitFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderPrdUnitList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].cname);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditPrdUnitCB(row){
    setTextValue("#titlePrdUnit", row.cname);
}

function getEditPrdUnitFormData(formData){
    return formData;
}

// 驗證
function valiateEditPrdUnit(formData){
    console.log("valiateEditPrdUnit formData =\n", formData);
    console.log("valiateEditPrdUnit storePrdUnitFL.list =\n", vo.storePrdUnitFL.list);
    if( isEmpty(formData.cname) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storePrdUnitFL.list, formData, FIELD_UUID, "cname", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSavePrdUnitFormData(list){
    var formData = {};
    formData["optionList"] = utils.safeList(list);
    return formData;
}

function valiateSavePrdUnit(formData){
    if( utils.isEmptyAry(formData["optionList"]) ){
        return false;
    }
    return true;
}

function afterSavePrdUnitCB(){
    utils.addSuccessMsg();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for customer level">
function afterFetchCusLevel(response, ajaxKey){
    var list = utils.getResponseList(response);
    renderCusLevel(list, ajaxKey);
}
function renderCusLevel(list, ajaxKey){
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    
    vo.storeCusLevelFL = new FormListM("storeCusLevelFL", // this object name
                        '#editCusLevelForm', '#existedCusLevels', '#confirmCusLevel', '#saveCusLevel', '#cancelCusLevel', // selector
                        list, // data list
                        OPS_URL+"?type=cusLevel", 
                        OPS_SAVE_URL+"?type=cusLevel", 
                        OPS_DEL_URL+"?type=cusLevel",  // rest url
                        renderCusLevelList, // call back function
                        null, onEditCusLevelCB, afterSaveCusLevelCB, null, // call back function
                        true, true,
                        getEditCusLevelFormData, getSaveCusLevelFormData, // get form input data function
                        valiateEditCusLevel, valiateSaveCusLevel // validate form function
                    );
    vo.storeCusLevelFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderCusLevelList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].cname);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditCusLevelCB(row){
    setTextValue("#titleCusLevel", row.cname);
}

function getEditCusLevelFormData(formData){
    return formData;
}

function valiateEditCusLevel(formData){
    if( isEmpty(formData.cname) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storeCusLevelFL.list, formData, FIELD_UUID, "cname", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSaveCusLevelFormData(list){
    var formData = {};
    formData["optionList"] = utils.safeList(list);
    return formData;
}

function valiateSaveCusLevel(formData){
    if( utils.isEmptyAry(formData["optionList"]) ){
        // do nothiing
        return false;
    }
    return true;
}

function afterSaveCusLevelCB(){
    console.log("afterSaveCusLevelCB ....");
    utils.addSuccessMsg();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for customer feedback">
function afterFetchFeedback(response, ajaxKey){
    var list = utils.getResponseList(response);
    renderFeedback(list, ajaxKey);
}
function renderFeedback(list, ajaxKey){
    var storeId = (!isNA(vo.itemSelected) && !isNA(vo.itemSelected.storeId))? vo.itemSelected.storeId:0;
    
    vo.storeFeedbackFL = new FormListM("storeFeedbackFL", // this object name
                        '#editFeedbackForm', '#existedFeedbacks', '#confirmFeedback', '#saveFeedback', '#cancelFeedback', // selector
                        list, // data list
                        OPS_URL+"?type=cusFeedback&storeId="+storeId,
                        OPS_SAVE_URL+"?type=cusFeedback&storeId="+storeId,
                        OPS_DEL_URL+"?type=cusFeedback&storeId="+storeId, // rest url
                        renderFeedbackList, // call back function
                        null, onEditFeedbackCB, afterSaveFeedbackCB, null, // call back function
                        true, true,
                        getEditFeedbackFormData, getSaveFeedbackFormData, // get form input data function
                        valiateEditFeedback, valiateSaveFeedback // validate form function
                    );
    vo.storeFeedbackFL.init();
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderFeedbackList(nameFL, selector, values){
    var html = '<table class="w3-table-all">'
             + genCommonTableHead();
             + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += genCommonTableRow(nameFL, i, values[i].cname);
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditFeedbackCB(row){
    setTextValue("#titleFeedback", row.cname);
}

function getEditFeedbackFormData(formData){
    return formData;
}

function valiateEditFeedback(formData){
    if( isEmpty(formData.cname) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.error"));
        return false;
    }
    if( !utils.checkUnique(vo.storeFeedbackFL.list, formData, FIELD_UUID, "cname", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSaveFeedbackFormData(list){
    var formData = {};
    formData["optionList"] = utils.safeList(list);
    return formData;
}

function valiateSaveFeedback(formData){
    if( utils.isEmptyAry(formData["optionList"]) ){
        // do nothiing
        return false;
    }
    return true;
}

function afterSaveFeedbackCB(){
    console.log("afterSaveFeedbackCB ....");
    utils.addSuccessMsg();
}
//</editor-fold>
