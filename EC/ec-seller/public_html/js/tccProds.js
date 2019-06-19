/* global w3, _asyncCompleteFlags, TEST_DATA, PRD_TCC_LIST_URL, PRD_TCC_COUNT_URL, PRD_TCC_IMP_URL, TCC_PRD_PIC_URL, TCC_PRD_PIC_UP_URL, TCC_PRD_PIC_DEL_URL, _adminUser, TCC_PRD_ADD_URL, TCC_PRD_DEL_URL, TCC_PRD_SAVE_URL, TCC_PRD_ACT_URL, utils */
var vo = {// variable objects the JS
    itemSelected: null,// 選取
    //stores: null,// 商店
    //storeId: 0,
    urlPrefix: null,// 前置網址 for 圖片顯示、檔案下載
    prdList: [],
    hasPicList: [],
    noPicList: []
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
    createCheckbox("#noPicOnly", null);
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    
    createBtn('#btnAdd', 'fa-plus-square', onClickAdd);// 新增商品
    createBtn('#btnUpdate', 'fa-save', onClickUpdate);// 修改商品
    createBtn('#btnCancel', 'fa-ban', onClickCancel);// 取消編輯
    
    $("#btnUpdate").hide();
    $("#btnCancel").hide();
}

// 查詢
function onClickSearch() {
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    if( getCheckbox("#noPicOnly") ){ formData["noPicOnly"]=true; }    
    buildProductListLazy(formData, "prdListLazy");// Lazy方式抓取資料
}

// 清除
function onClickClearSearch() {
    vo.prdList = [];
    renderProductListLazy({}, 0, false, null);// clear table

    setCheckbox("#noPicOnly", false);
    $("#fmSearch")[0].reset();
}

function cehckInput(){
    if( isEmpty($("#code").val()) ){
        _alert(utils.i18nWP("noinput.fd", utils.i18n("fd.prd.code")));
        return false;
    }
    if( isEmpty($("#name").val()) ){
        _alert(utils.i18nWP("noinput.fd", utils.i18n("fd.prd.name")));
        return false;
    }
    return true;
}

function onClickAdd(){
    if( !cehckInput() ){
        return;
    }
    var formData = {};
    formData["code"] = $("#code").val();
    formData["name"] = $("#name").val();
    console.log("onClickAdd formData = \n", formData);
    var restUrl = TCC_PRD_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave);
}

function onClickUpdate(){
    if( !cehckInput() || isNA(vo.itemSelected) || isNA(vo.itemSelected.id) ){
        return;
    }
    var formData = {};
    formData["id"] = vo.itemSelected.id;
    formData["code"] = $("#code").val();
    formData["name"] = $("#name").val();
    console.log("onClickUpdate formData = \n", formData);
    var restUrl = TCC_PRD_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave);
}

function onClickCancel(){
    vo.itemSelected = null;
    
    $("#code").val("");
    $("#name").val("");
    
    $("#btnAdd").show();
    $("#btnUpdate").hide();
    $("#btnCancel").hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildProductListLazy(formData, ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;
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
    vo.prdList=[];
    vo.hasPicList=[];
    vo.noPicList=[];
    renderProductListLazy(formData, totalRows, false, ajaxKey);
}

function renderProductListLazy(formData, totalRows, retry, ajaxKey) {
    var pageSize = 100;// 不會太多，暫不分頁
    var minWidth = 700;
    var marginWidth = 80;
    var columns = [
        {field: "id", headerText: utils.i18n("operation"), sortable: false, bodyStyle:"width:60px;", content:genOperationContent},
        {field: "id", headerText: "ID", sortable: true, bodyClass: "tar", bodyStyle:"width:70px;padding-right:10px;"},
        {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: true, bodyStyle:"width:120px;", content:genCodeContent},
        {field: "name", headerText: utils.i18n("fd.prd.name"), sortable: true, bodyStyle:"width:180px;"},
        {field: "url", headerText: utils.i18n("prd.picture"), sortable: false, content:genPicContent}
    ];

    renderSearchResultNum("#searchResultMsg", totalRows, false);

    if (totalRows === 0) {
        clearDataTable('#tbProductLazy', columns, minWidth, marginWidth, ajaxKey);
    } else {
        renderDataTableLazy('#tbProductLazy', pageSize, totalRows, columns, PRD_TCC_LIST_URL,
                !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth, afterRenderTable);
    }
}

function genOperationContent(data){
    vo.prdList.push(data);
    var id = data.id;
    var html = '<button type="button" onclick="onClickEdit('+ id +')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal" style="margin-right:2px;" ><i class="fa fa-pencil-square"></i></button>';
    if( utils.isTrue(data.active) ){
        html+= '<button type="button" onclick="onClickRemove('+ id +')" title="'+utils.i18n("stop.sales")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-arrow-down"></i></button>';
    }else{
        html+= '<button type="button" onclick="onClickActive('+ id +')" title="'+utils.i18n("start.sales")+'" class="w3-btn w3-blue" ><i class="fa fa-arrow-up"></i></button>';
    }
    return html;
}

function genCodeContent(data){
    if( utils.isTrue(data.active) ){
        return data.code;
    }else{
        return "<span style='text-decoration:line-through' >"+data.code+"</span>";
    }
}

function genPicContent(data){
    var id = data.id;
    if( isNA(data.url) ){
        vo.noPicList.push(data);
    }else{
        vo.hasPicList.push(data);
    }
    var html = '<span id="selImg'+id+'" ></span>&nbsp;'
            + '<label class="w3-btn w3-teal">'
            + '    <input name="fiImg'+id+'" id="fiImg'+id+'" style="display:none;" '
            + '     onchange="onChangeImg('+id+')" type="file" title="prd.picture" required="true" />'
            + '    <i class="fa fa-photo"></i>' + utils.i18n("select.file")
            + '</label>&nbsp;'
            + '<button type="button" onclick="onClickUpload('+id+')" class="w3-btn w3-deep-orange" >'+ utils.i18n("fs.upload")+'</button>&nbsp;'
            + '<button type="button" onclick="onClickDelete('+id+')" class="w3-btn w3-brown" >'+ utils.i18n("remove")+'</button>'
            ;
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

// 編輯商品
function onClickEdit(id){
    console.log("onClickEdit id = "+id);
    vo.itemSelected = null;
    for(var i=0; i<vo.prdList.length; i++){
        if( id===vo.prdList[i].id ){
            vo.itemSelected = utils.cloneObj(vo.prdList[i]);
        }
    }
    if( !isNA(vo.itemSelected) ){
        $("#code").val(vo.itemSelected.code);
        $("#name").val(vo.itemSelected.name);
        
        $("#btnAdd").hide();
        $("#btnUpdate").show();
        $("#btnCancel").show();
        
        utils.scrollTo("#editForm", 100);
    }
}

function onClickRemove(id){
    if( !confirm(utils.i18n("stop.sales.confirm")) ){
        return;
    }
    console.log("onClickRemove id = "+id);
    var formData = {"id":id, active:false};
    var restUrl = TCC_PRD_ACT_URL;
    utils.postData(restUrl, formData, false, afterSave);
}

function onClickActive(id){
    if( !confirm(utils.i18n("start.sales.confirm")) ){
        return;
    }
    console.log("onClickActive id = "+id);
    var formData = {"id":id, active:true};
    var restUrl = TCC_PRD_ACT_URL;
    utils.postData(restUrl, formData, false, afterSave);
}

function afterSave(response, formData, ajaxKey) {
    utils.addSuccessMsg();
    onClickSearch();// 重查即可
}

// 選取圖片
function onChangeImg(id){
    console.log("onChangeImg id = "+id);
    $("#selImg"+id).html($("#fiImg"+id)[0].files[0].name);
}

function onClickUpload(id){
    var formDataU = new FormData();
    var fileSelector = "#fiImg"+id;
    if( !isNA(id) && !isNA($(fileSelector)[0].files[0]) ){
        formDataU.append("id", id);
        formDataU.append("files", $(fileSelector)[0].files[0]);
        formDataU.append("filename", $(fileSelector)[0].files[0].name);
        formDataU.append("fileContentType", $(fileSelector)[0].files[0].type);
    }else{
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("pic.noselect"));
        return false;
    }
    console.log("onClickUpload formDataU =\n", formDataU);

    var restUrl = TCC_PRD_PIC_UP_URL;
    utils.uploadFiles(restUrl, formDataU, afterSave, null);
}

function onClickDelete(id){
    if( !confirm(utils.i18n("remove.confirm")) ){
        return;
    }
    console.log("onClickDelete id = "+id);
    //$("#selImg"+id).html($("#fiImg"+id)[0].files[0].name);
    var formData = {"id":id};
    var restUrl = TCC_PRD_PIC_DEL_URL;
    utils.postData(restUrl, formData, false, afterSave);
}

function afterSave(response, formData, ajaxKey) {
    utils.addSuccessMsg();
    onClickSearch();// 重查即可
    
    $("#code").val("");
    $("#name").val("");
    
    $("#btnAdd").show();
    $("#btnUpdate").hide();
    $("#btnCancel").hide();
}
//</editor-fold>
