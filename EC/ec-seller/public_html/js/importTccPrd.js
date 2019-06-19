/* global w3, _asyncCompleteFlags, TEST_DATA, PRD_TCC_LIST_URL, PRD_TCC_COUNT_URL, PRD_TCC_IMP_URL, _adminUser, utils */
var vo = {// variable objects the JS
    itemSelected: null,// 選取商品
    checkedItems: [],
    pageItems: [],
    stores: null,// 商店
    storeId: 0,
    urlPrefix: null// 前置網址 for 圖片顯示、檔案下載
};

$(document).ready(function () {
    if (!utils.checkBrowserSupport()) {
        return;
    }
    w3.includeHTML(start);
});

function start(){
    console.log(utils.formatDT(new Date()));
    // 登入檢查
    utils.checkLogin();// trigger _renderThisPage
}

function _renderThisPage(){
    // init layout
    initLayout(_adminUser, true);
    renderPanels();
    buildSearchFrom();
    buildProductListLazy(utils.initUrlCriteria(), "prdListLazy");// Lazy方式抓取資料

    console.log(utils.formatDT(new Date()));
}

function renderPanels() {
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsSearchResult').puifieldset({toggleable: true});
}

//<editor-fold defaultstate="collapsed" desc="for Init UI">
// 查詢表單
function buildSearchFrom() {
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    
    createBtn('#btnSave', 'fa-save', onClickSave);
}

// 查詢
function onClickSearch() {
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();   
    buildProductListLazy(formData, "prdListLazy");// Lazy方式抓取資料
}

// 清除
function onClickClearSearch() {
    renderProductListLazy({}, 0, false, null);// clear table

    $("#fmSearch")[0].reset();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildProductListLazy(formData, ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;
    $("#divMultiOperation").hide();

    // 先取得總筆數 for 分頁
    var restUrl = PRD_TCC_COUNT_URL;// 先查總筆數
    if( !_adminUser ){
        formData["storeId"] = null;
    }
    utils.postData(restUrl, formData, false, fetchProductListLazy, null, ajaxKey);
}

function fetchProductListLazy(response, formData, ajaxKey) {
    var totalRows = response.res.totalRows;
    vo.urlPrefix = response.res.urlPrefix;

    if (!isNA(totalRows) && totalRows > 0) {
        $("#divMultiOperation").show();
    }

    renderProductListLazy(formData, totalRows, false, ajaxKey);
}

function renderProductListLazy(formData, totalRows, retry, ajaxKey) {
    var pageSize = 100;// 不會太多，暫不分頁
    var minWidth = 700;
    var marginWidth = 80;
    vo.pageItems = [];
    // ID
    var columns = [
        // 勾選欄
        {field: "id", headerText: utils.i18n("fd.check"), sortable: false, bodyClass: "tac", bodyStyle:"width:80px;", content: genCheckBoxContent},
        // 不需維護牌價
        //{field: "price", headerText: utils.i18n("fd.compareAtPrice"), sortable: false, bodyClass: "tar", bodyStyle:"width:60px;", content: genPriceContent},
        {field: "id", headerText: "ID", sortable: true, bodyClass: "tar", bodyStyle:"width:80px;padding-right:10px;"},
        {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: true, bodyStyle:"width:140px;"},
        {field: "name", headerText: utils.i18n("fd.prd.name"), sortable: true, bodyStyle:"width:280px;"},
        {field: "url", headerText: utils.i18n("prd.picture"), sortable: false, content:genPicContent}
    ];

    renderSearchResultNum("#searchResultMsg", totalRows, true);
    if (totalRows === 0) {
        clearDataTable('#tbProductLazy', columns, minWidth, marginWidth, ajaxKey);
    } else {
        renderDataTableLazy('#tbProductLazy', pageSize, totalRows, columns, PRD_TCC_LIST_URL,
                !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth, afterRenderTable);
    }
}

function genPicContent(data){
    var id = data.id;
    var html = '<span id="selImg'+id+'" ></span>&nbsp;';
    return html;
}
function afterRenderTable(formData, resList){
    if( utils.isEmptyAry(resList) ){
        console.log("afterRenderTable resList is Empty!");
        return;
    }
    for(var i=0; i<resList.length; i++){
        var id = resList[i].id;
        if( isNA(resList[i].url) ){
            $('#selImg'+id).html("");
        }else{
            var url = utils.safePrint(vo.urlPrefix + resList[i].url);
            createImgLightBox('#selImg'+id, url, url, resList[i].filename, null, utils.isSmallScreen()?'100':'60');
        }
    }
}

// for renderSearchResultNum checkbox event
function selectAll(){
    console.log(getCheckbox("#_selectAll"));
    var checked = getCheckbox("#_selectAll");
    if( checked && utils.isEmptyAry(vo.pageItems) ){
        utils.addMessage("info", utils.i18n("prompts"), utils.i18n("noitem.can.sel"));
        return;
    }
    for(var i=0; i<vo.pageItems.length; i++){
        var prdId = vo.pageItems[i].id;
        $('#cbSel' + prdId).prop("checked", checked);
        checkRow(prdId);
    }
}

function genCheckBoxContent(data) {
    if( isNA(data.productId) ){
        vo.pageItems.push(data);
        return '<input type="checkbox" id="cbSel' + data.id + '" onchange="checkRow(' + data.id + ')" />';
    }
    return "<span class='hit' >"+utils.i18n("msg.imported")+"</span>";
}

function genPriceContent(data) {
    if( isNA(data.productId) ){
        return '<input type="text" size="10" id="tbPrice' + data.id 
                + '" onblur="checkPrice(' + data.id + ')" value="'+utils.printNumber(data.price)+'" />';
    }
    return "<span class='hit' >"+utils.printNumber(data.price)+"</span>";
}

function checkPrice(prdId){
    var price = $('#tbPrice' + prdId).val();
    if( isNaN(price) ){
        _alert("["+utils.i18n("fd.compareAtPrice")+"]"+utils.i18n("in.err"));
        $('#tbPrice' + prdId).focus();
        return false;
    }
    return true;
}

function checkRow(prdId) {
    var idx = vo.checkedItems.indexOf(prdId);
    if( $('#cbSel' + prdId).prop("checked") ){
        if (idx < 0) {
            vo.checkedItems.push(prdId);
            vo.checkedItems.sort();
        }
    } else {
        if (idx >= 0) {
            vo.checkedItems.splice(idx, 1);
        }
    }
    //console.log("checkRow checkedItems = ", checkedItems);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Save Import">
// 儲存匯入
function onClickSave(){
    if (!confirm(utils.i18n("confirm.imp.sel"))) {
        return false;
    }

    var tccPrdList = [];
    if( !isNA(vo.pageItems) ){
        for(var i=0; i<vo.pageItems.length; i++){
            var prdId = vo.pageItems[i].id;
            //var price = $('#tbPrice' + prdId).val(); // 不需維護牌價
            
            if( vo.checkedItems.indexOf(prdId)>=0 ){
                 // 不需維護牌價
                /*if( isNaN(price) || isEmpty(price) ){
                    _alert("["+utils.i18n("fd.compareAtPrice")+"]"+utils.i18n("in.err"));
                    $('#tbPrice' + prdId).focus();
                    return false;
                }
                pageItems[i]["price"] = price;*/
                tccPrdList.push(vo.pageItems[i]);
            }
        }
    }
    
    var formData = {};
    formData['tccPrdList'] = tccPrdList;
    console.log("saveStockLog formData = \n", formData);

    var restUrl = PRD_TCC_IMP_URL;
    utils.postData(restUrl, formData, false, afterSave);
    return true;
}

function afterSave(response, formData, ajaxKey) {
    utils.addSuccessMsg();
    onClickSearch();// 重查即可
}
//</editor-fold>
