/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global w3, PN_VENDOR, _asyncCompleteFlags, VENDOR_COUNT_URL, TEST_DATA, VENDOR_FULL_URL, VENDOR_LIST_URL, VENDOR_SAVE_URL, VENDOR_DEL_URL, utils */
var vo = {// variable objects the JS
    itemSelected: null,// 選取
    doAdd: false // 新增否(控制 datatable reflesh 方式)
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
    
    buildSearchForm();
    buildBasicForm();

    buildVendorListLazy({}, "vendors");
    
    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#fsBasic").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsSearchResult').puifieldset({toggleable: true});
    $('#fsBasic').puifieldset({toggleable: true});
    
    utils.renderMultiPanel(['plBasic']);
}

//<editor-fold defaultstate="collapsed" desc="for Vender Search">
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

    buildVendorListLazy(formData, "vendorListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
}
// 清除
function onClickClearSearch(event){
    $("#fmSearch")[0].reset();

    renderVendorListLazy({}, 0, false, null);// clear table
    $("#selectedItemHeader").hide();
    $('#fsBasic').hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Vendor Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildVendorListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = VENDOR_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchVendorListLazy, null, ajaxKey);
}

function fetchVendorListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderVendorListLazy(formData, totalRows, false, ajaxKey);
}

function renderVendorListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 800;
    var marginWidth = 80;
    var columns = [
            {field: "vendorId", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "code", headerText: utils.i18n("fd.vendor.code"), sortable: true},
            {field: "cname", headerText: utils.i18n("fd.ch.name"), sortable: true},
            {field: "ename", headerText: utils.i18n("fd.en.name"), sortable: true},
            {field: "nickname", headerText: utils.i18n("fd.nickname"), sortable: true},
            {field: "idCode", headerText: utils.i18n("fd.idCode"), sortable: true},
            {field: "tel1", headerText: utils.i18n("fd.tel1"), sortable: true},
            {field: "email1", headerText: utils.i18n("fd.email1"), sortable: true},
            {field: "addr1", headerText: utils.i18n("fd.addr1"), sortable: true}
        ];
        
    renderSearchResultNum("#searchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbVendorLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbVendorLazy', pageSize, totalRows, columns, VENDOR_LIST_URL, 
            !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth);
    }
}

// DataTable 選取項目
function onSelectRow(item){
    vo.doAdd = false;
    if( isNA(item.vendorId) ){
        console.log("onSelectRow item = ", item);
        return;
    }
    vo.itemSelected = item;
    resetSelectedItemNavBar();
    // 詳細資料
    var restUrl = VENDOR_FULL_URL.replace("{id}", item.vendorId);// 供應商ID
    utils.fetchData(restUrl, false, renderVendorFullInfo, null, "vendorFullInfo");
}

// 產生供應商完整資訊畫面
function renderVendorFullInfo(data, ajaxKey){
    if( isNA(data) ){
        console.error("renderVendorFullInfo data = ", data);
        return;
    }
    
    // 選取項目
    genSelectedItemTitle(data.cname);

    // 供應商資訊
    renderVendorBasic(data);

    $("#selectedItemHeader").show();
    $('#fsBasic').show();

    utils.scrollTo('#selectedItemHeader');
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function renderVendorBasic(data){
    $('#editVendorForm')[0].reset();
    utils.copyPropsToForm(data);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Vendor Basic">
// 初始表單
function buildBasicForm(){
    createBtn('#add', 'fa-plus-square', onClickAdd);// 新增
    createBtn('#save', 'fa-save', onClickSave);// 儲存
    createBtn('#cancel', 'fa-ban', onClickReset);// 取消
    createBtn('#delete', 'fa-trash', onClickDelete);// 刪除
}

// 顯示基本資料
function renderVendorBasic(data){
    $('#editVendorForm')[0].reset();
    utils.copyPropsToForm(data);
}

// 新增
function onClickAdd(){
    vo.doAdd = true;
    $('#editVendorForm')[0].reset();
    vo.itemSelected = {};
    genSelectedItemTitle(utils.i18n("add.vendor"));
    
    resetSelectedItemNavBar();
    
    $("#selectedItemHeader").show();
    $("#fsBasic").show();
    
    utils.scrollTo('#selectedItemHeader');
}

// 儲存基本資料
function onClickSave(event){
    var $form = $("#editVendorForm");
    var formData = $form.serializeFormJSON();
    formData["vendorId"] = vo.itemSelected.vendorId;
    console.log("onClickSave ", formData);
    
    if( !valiateBasic(formData) ){
        return;
    }

    var restUrl = VENDOR_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave, null, "saveBasic");
}

function afterSave(response){
    vo.itemSelected = response;
    renderVendorBasic(vo.itemSelected);

    reloadDataTable('#tbVendorLazy', !vo.doAdd);// keep status reload
    
    utils.addSuccessMsg();
}

function valiateBasic(formData){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editVendorForm");
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

    return true;
}

// 重設基本資料 Form
function onClickReset(event){
    if( confirm(utils.i18n("cancel.edit")) ){
        $("#editVendorForm")[0].reset();
    }
}

function onClickDelete(event){
    if( confirm(utils.i18n("remove.vendor.confirm")) ){
        var formData = {"vendorId": vo.itemSelected.vendorId};
        var restUrl = VENDOR_DEL_URL;
        utils.postData(restUrl, formData, false, afterDelete, null, "delVendor");
    }
}

function afterDelete(response){
    onClickSearch(null);
}
//</editor-fold>
