/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_IMPORT_PRD, IMPORT_PRD_UPLOAD_URL, IMPORT_PRD_SAVE_URL, w3, _language, utils */
var vo = {// variable objects the JS
    impFile: null,
    products: null,
    errList: null,
    resultMsg: null,
    canImport: null,
    errorCount: null
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
    
    buildImportForm();
    // 預設顯示、隱藏
    //$("#selectedItemHeader").hide();
    //$("#selectedItemContent").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsImport').puifieldset({toggleable: true});
    $('#fsPreview').puifieldset({toggleable: true});
}

function buildImportForm(){
    $('#fiImport', '#fmImport').change(onChangeImport);
    $('#btnUploadImport').click(onClickUploadImport);
    $('#btnUploadImport').prop('disabled', true);

    $('#msgSaveResut').puimessages();
    
    var href = (utils.getLangCode() === "zh-TW")?"file/import_prds.xlsx":"file/import_prds_cn.xlsx";
    $('a#linkTemplate').attr("href", href);
}

function uploadSingleFile(restUrl, fileSelector, ori, callback){
    var formData = new FormData();
    formData.append("files", $(fileSelector)[0].files[0]);
    formData.append("filename", $(fileSelector)[0].files[0].name);
    formData.append("fileContentType", $(fileSelector)[0].files[0].type);
    if( !isNA(ori) && !isNA(ori.id) ){
        formData.append("id", ori.id);
    }
    //var storeId = (!isNA(itemSelected) && !isNA(itemSelected.storeId))? itemSelected.storeId:0;
    //formData.append("storeId", storeId);

    utils.uploadFiles(restUrl, formData, afterUploadImport, null);
}

function onChangeImport(){
    console.log("onChangeImport ...");
    $('#btnUploadImport').prop('disabled', false);
    renderUploadFileName("#selImport", "#fiImport");
}

function onClickUploadImport(){
    if( isNA($("#fiImport")[0].files[0]) ){
        _alert(utils.i18n("no.sel.upload"));
        return;
    }
    console.log("onClickUploadImport files = ", $("#fiImport")[0].files[0]);
    uploadSingleFile(IMPORT_PRD_UPLOAD_URL, "#fiImport", vo.impFile, afterUploadImport);
}

function afterUploadImport(response, formData, optional){
    vo.products = (isNA(response)||isNA(response.resList))?[]:response.resList;
    vo.errList = (isNA(response)||isNA(response.errList))?[]:response.errList;
    vo.resultMsg = isNA(response)?"":response.resultMsg;
    vo.errorCount = isNA(response)?null:response.errorCount;
    vo.canImport = isNA(response)?false:response.canImport;
    
    $("#msgImportResut").html(vo.resultMsg);
    $("#msgImportResut").css("color", "red");
    $("#msgImportResut").css("font-size", "16px");
    
    if( vo.canImport && utils.isEmptyAry(vo.errList) ){
        var btnSave = "&nbsp;&nbsp;<button id='btnSave' onclick='onClickSave()' class='w3-btn w3-deep-orange' ><i class='fa fa-repeat'></i> "+utils.i18n("import.confirm")+"</button>";
        $("#msgImportResut").append(btnSave);
        $("#msgImportResut").css("color", "blue");
        
        var minWidth = 1000;
        var marginWidth = 80;
        var columns = [
            {field: "cname", headerText: utils.i18n("fd.prd.name"), sortable: false},       
            {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: false},       
            {field: "typeL1Name", headerText: utils.i18n("fd.typeL1Name"), sortable: false},       
            {field: "typeL2Name", headerText: utils.i18n("fd.typeL2Name"), sortable: false},       
            {field: "typeName", headerText: utils.i18n("fd.typeName"), sortable: false},
            {field: "vendorName", headerText: utils.i18n("fd.vendorName"), sortable: false},       
            {field: "brandName", headerText: utils.i18n("fd.brandName"), sortable: false},       
            {field: "price", headerText: utils.i18n("fd.price"), sortable: false, bodyClass:"tar", content:genPriceContent},       
            //{field: "compareAtPrice", headerText: utils.i18n("fd.compareAtPrice"), sortable: false, bodyClass:"tar", content:genCompareAtPriceContent},       
            {field: "priceUnitName", headerText: utils.i18n("fd.priceUnit"), sortable: false},       
            //{field: "priceAmt", headerText: utils.i18n("fd.priceAmt"), sortable: false, bodyClass:"tar", content:genPriceAmtContent},       
            {field: "stock", headerText: utils.i18n("fd.stock"), sortable: false, bodyClass:"tar", content:genStockContent},       
            //{field: "sku", headerText: utils.i18n("fd.sku"), sortable: false},       
            //{field: "barcode", headerText: utils.i18n("fd.barcode"), sortable: false},       
            {field: "minAmt", headerText: utils.i18n("fd.minAmt"), sortable: false, bodyClass:"tar", content:genMinAmtContent},       
            {field: "maxAmt", headerText: utils.i18n("fd.maxAmt"), sortable: false, bodyClass:"tar", content:genMaxAmtContent},       
            {field: "intAmtYN", headerText: utils.i18n("fd.intAmtYN"), sortable: false, bodyClass:"tac"}
        ];

        renderDataTable("#tbProduct", 1000, columns, vo.products, null, minWidth, marginWidth);
        $("#tbProduct").show();
        $("#tbErrorMsg").hide();
    }else{// 請修正錯誤再重新上傳!
        $("#msgImportResut").append('&nbsp;&nbsp;<i class="fa fa-exclamation-triangle"></i>'+utils.i18n("fix.upload"));
        var columns = [
            {field: "cname", headerText: utils.i18n("fd.prd.name"), sortable: false, bodyStyle:"min-width:80px;"},    
            {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: false, bodyStyle:"min-width:80px;"}, 
            {field: "errMsgs", headerText: utils.i18n("fd.errMsgs"), sortable: false}
        ];
        renderDataTable("#tbErrorMsg", 1000, columns, vo.errList, null, null, null);
        $("#tbProduct").hide();
        $("#tbErrorMsg").show();
    }

    utils.scrollTo("#fsPreview");
}

function onClickSave(){
    var formData = {"importPrdList": vo.products};
    var restUrl = IMPORT_PRD_SAVE_URL;
    utils.postData(restUrl, formData, false, successCallback, null, null);
}

function successCallback(response){
    $("#msgImportResut").html("");
    utils.addMessage("info", utils.i18n("process.res"), utils.i18n("import.prd.success"), "#msgSaveResut");
}

function genPriceContent(row){
    return utils.printNumber(row.price);
}
function genCompareAtPriceContent(row){
    return utils.printNumber(row.compareAtPrice);
}
function genStockContent(row){
    return utils.printNumber(row.stock);
}
function genPriceAmtContent(row){
    return utils.printNumber(row.priceAmt);
}
function genMinAmtContent(row){
    return utils.printNumber(row.minAmt);
}
function genMaxAmtContent(row){
    return utils.printNumber(row.maxAmt);
}
