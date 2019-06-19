/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, com/menu.js, layout.js
 */
/* global ajaxFlags, _asyncCompleteFlags, PRD_LIST_URL, MSG_NO_RECORD_FOUND, PRD_COUNT_URL, PRD_PICS_URL, PN_PRODUCT, NO_SELECT_OP, renderSimpleTextList, renderSimpleListStr, OPTIONS_URL, PRD_INTRO_SAVE_URL, TEST_DATA, PRD_FULL_URL, PRD_TYPE_ATTR_URL, postData, PRD_SAVE_URL, PRD_COLOR_SAVE_URL, PRD_COLOR_DEL_URL, PRD_SIZE_SAVE_URL, PRD_SIZE_DEL_URL, PRD_VAR_SAVE_URL, PRD_SHIP_SAVE_URL, FIELD_UUID, FIELD_MODIFIED, PRD_ATTR_SAVE_URL, PRD_ATTR_DEL_URL, PRD_PAY_SAVE_URL, FILE_UPLOAD_URL, PRD_DETAIL_SAVE_URL, PRD_DETAIL_UPLOAD_URL, PRD_DETAIL_DEL_URL, PRD_PIC_SAVE_URL, PRD_PIC_DEL_URL, PRD_ATTR_URL, PRD_VAR_DEL_URL, w3, utils.genNoSelectOpNum(), utils.genNoSelectOpStr(), genCreateTimeContent, genModifyTimeContent, PRD_DEL_URL, PRD_RESERVE_URL, PRD_EXP_URL, PRD_EXP_SAVE_URL, PRD_EXP_GET_URL, PRD_APPROVE_URL, PRD_PUBLISH_URL, PRD_MULTI_STATUS_URL, PRD_STATUS_PASS, PRD_STATUS_RESERVED, PRD_STATUS_PUB, PRD_STOCK_SAVE_URL, PRD_STOCK_URL, PRD_STOCK_COUNT_URL, PRD_STOCK_LOGS_URL, PRD_STATUS_APPLY, PRD_CH_STATUS_URL, PRD_STATUS_REJECT, TODO_PRD_ONSALES, TODO_PRD_CORRECT, _tccDealer, PRD_STATUS_REMOVE, GET_EXP_URL, _adminUser, utils */
var vo = {// variable objects the JS
    itemSelected: null,// 選取商品
    colors: null,// 顏色
    sizes: null,// 大小
    variants: null,// 多型別
    noVariant: false,// 有無多型別
    pictures: null,// 圖片
    gallery: null,// 圖片展示元件
    introductions: null,// 商品簡介
    events: null,// 商品活動
    shippings: null,// 商品運送方式
    payments: null,// 商品付款方式
    details: null,// 商品詳細說明
    attrs: null,// 商品規格屬性

    editVariantKey: null,// 編輯中多型別屬性設定 // 無排序或暫存，可直接使用index做key

    prdTypeTree: {},
    prdStatus: [],// 商品狀態
    prdStatusSeller: [],// 商品狀態
    prdStatusAdmin: [],// 商品狀態
    sellerVendor: [],// 供應商
    prdBrand: [],// 品牌
    prdUnit: [],// 計量單位
    weightUnit: [],// 重量單位
    prdTypeAttrs: [],// 產品類別屬性
    shippingOps: [],// 商品運送選項
    paymentOps: [],// 商品付款選項
    stockLogOps: [],// 庫存異動類型
    picList: [],

    variantFL: null, // FormListS object for variant
    prdAttrFL: null, // FormListM object for product attributes

    checkedItems: [],
    pageItems: [],
    stores: null,// 商店
    storeId: 0,
    expHeaders: [],
    urlPrefix: null,// 前置網址 for 圖片顯示、檔案下載

    // 銷售區域選項(for Search)
    areaOps: [],// 銷售區域選項
    areaSearchList: null
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
    if( !_adminUser ){
        $("#storeDIV").hide();
        $("#brandBSTxt").hide();
        $("#vendorBSTxt").hide();
        $("#priceUnitBSTxt").hide();
        $("#weightUnitBSTxt").hide();
    }
    // Ajax 產出UI相關
    fetchInitOptions("init");

    buildOperationComs();

    buildProductListLazy(utils.initUrlCriteria(), "prdListLazy");// Lazy方式抓取資料

    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#fsBasic").hide();
    $('#fsVariantOptions').hide();
    $("#fsVariant").hide();
    $("#fsFullInfo").hide();

    console.log(utils.formatDT(new Date()));
}

function renderPanels() {
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsSearchResult').puifieldset({toggleable: true});
    $('#fsBasic').puifieldset({toggleable: true});
    $('#fsVariantOptions').puifieldset({toggleable: true});
    $('#fsColor').puifieldset({toggleable: false});
    $('#fsSize').puifieldset({toggleable: false});
    $('#fsVariant').puifieldset({toggleable: true});
    $('#fsFullInfo').puifieldset({toggleable: true});
    $('#fsProdPics').puifieldset({toggleable: true});
    $('#fsProdIntro').puifieldset({toggleable: true});
    $('#fsDetail').puifieldset({toggleable: true});
    $('#fsPrdAttrs').puifieldset({toggleable: true});

    utils.renderMultiPanel(['color', 'size', 'plVariants', 'plIntros']);
}

function showMultiUploadDiv(show) {
    console.log("hideMultiUploadDiv divMultiUpload show = " + show);
    if (show) {
        $("#divMultiUpload").show();
    } else {
        $("#divMultiUpload").hide();
    }
}

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=prdTypeTree,prdStatus,stockLogType,salesArea";
    restUrl = _adminUser ? utils.addUrlQueryParam(restUrl + ",stores,prdStatusAdmin", "storeId", vo.storeId) : 
            restUrl + ",prdBrand,prdStatusSeller,sellerVendor,prdUnit,weightUnit,shipping,payment";
    restUrl = restUrl + "&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

function buildInitObjects(response, ajaxKey) {
    if (isNA(response)) {
        console.assert("buildInitObjects error response isNA!");
        return;
    }

    vo.urlPrefix = response.res.urlPrefix;
    vo.prdStatus = isNA(response.prdStatus) ? [] : response.prdStatus;
    vo.prdStatusSeller = isNA(response.prdStatusSeller) ? [] : response.prdStatusSeller;
    vo.prdStatusAdmin = isNA(response.prdStatusAdmin) ? [] : response.prdStatusAdmin;
    vo.sellerVendor = isNA(response.sellerVendor) ? [] : response.sellerVendor;
    vo.prdBrand = isNA(response.prdBrand) ? [] : response.prdBrand;
    vo.weightUnit = isNA(response.weightUnit) ? [] : response.weightUnit;
    vo.prdUnit = isNA(response.prdUnit) ? [] : response.prdUnit;
    vo.shippingOps = isNA(response.shipping) ? [] : response.shipping;
    vo.paymentOps = isNA(response.payment) ? [] : response.payment;
    vo.stores = isNA(response.stores) ? [] : response.stores;
    vo.stockLogOps = isNA(response.stockLogType) ? [] : response.stockLogType;
    vo.areaOps = isNA(response.salesArea)? [] : response.salesArea;

    vo.prdTypeTree = response.prdTypeTree;
    
    if( ajaxKey==="init" ){
        // 商品分類
        vo.typeTreeNodes = (!isNA(vo.prdTypeTree) && !isNA(vo.prdTypeTree.nodes)) ? vo.prdTypeTree.nodes : [];
        renderPrdTypeSelect();// for search form
        // 查詢表單
        buildSearchFrom();
    }
    
    // 商品基本資料表單
    renderPrdTypeSelectBS();// for product basic form
    buildBasicFrom();

    _asyncCompleteFlags[ajaxKey] = true;
}

// 可預先建立的 UI 操作元件
function buildOperationComs() {
    // 基本資料編輯區
    buildOperationBasic();

    // 型別維護
    createBtn('#variantOps', 'fa-vimeo', onClickVariant);// 維護多型別資訊
    createBtn('#addColor', 'fa-plus-square', onClickAddColor);// 新增顏色
    createBtn('#addSize', 'fa-plus-square', onClickAddSize);// 新增大小

    // 商品簡介
    buildOperationIntro();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Search Form">
// 產生初始商品類別選單
function renderPrdTypeSelect() {
    var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
    var ops = null;
    if (!isNA(parentTypesL1)) {
        ops = utils.convertTreeNodesToOps(parentTypesL1);
    }
    createDropdown("#typeL1", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1);// 第一層
    enableDropdown('#typeL1');
    createDropdown("#typeL2", null, utils.genNoSelectOpNum(), onChangePrdTypeL2);// 第二層
    disableDropdown('#typeL2');
    createDropdown("#typeL3", null, utils.genNoSelectOpNum(), onChangePrdTypeL3);// 第三層
    disableDropdown('#typeL3');
}

function onChangePrdTypeL1(item) {
    // 第二層選單
    var parentTypesL2 = utils.findChildrenFromTree(vo.typeTreeNodes, item.value);
    var ops = null;
    if (!isNA(parentTypesL2)) {
        ops = utils.convertTreeNodesToOps(parentTypesL2);
    }
    createDropdown("#typeL2", ops, utils.genNoSelectOpNum(), onChangePrdTypeL2);
    setDropdownValue('#typeL2', 0);
    enableDropdown('#typeL2');

    createDropdown("#typeL3", null, utils.genNoSelectOpNum(), onChangePrdTypeL2);// 第三層
    disableDropdown('#typeL3');
}

function onChangePrdTypeL2(item) {
    // 第3層選單
    var parentTypesL3 = utils.findChildrenFromTree(vo.typeTreeNodes, item.value);
    var ops = null;
    if (!isNA(parentTypesL3)) {
        ops = utils.convertTreeNodesToOps(parentTypesL3);
    }
    createDropdown("#typeL3", ops, utils.genNoSelectOpNum(), onChangePrdTypeL3);
    setDropdownValue('#typeL3', 0);
    enableDropdown('#typeL3');
}

function onChangePrdTypeL3(item) {
    console.log("onChangePrdTypeL3 item = ", item);
}

// 查詢表單
function buildSearchFrom() {
    if( _adminUser ){
        createDropdown("#storeId", vo.stores, utils.genNoSelectOpNum(), onChangeStore);// store
        buildSearchAreaPickList([]);// 銷售區域
    }
    createDropdown("#status", vo.prdStatus, utils.genNoSelectOpStr(), null);// 狀態
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    
}

function onChangeStore(event) {
    //storeId = getDropdownValue("#storeId");
    console.log("storeId = ", getDropdownValue("#storeId"));
}
// 查詢
function onClickSearch(event) {
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    if( _adminUser ){
        formData["areaList"] = utils.toIdList(vo.areaSearchList, "value");
    }
    
    buildProductListLazy(formData, "prdListLazy");// Lazy方式抓取資料
    
    if( _adminUser ){
        storeId = formData["storeId"];
        fetchInitOptions("changeStore");
    }
}
// 清除
function onClickClearSearch(event) {
    renderProductListLazy({}, 0, false, null);// clear table
    if (_adminUser) {
        setDropdownValue("#storeId", 0);
    }
    setDropdownValue("#status", "");

    $("#fmSearch")[0].reset();
    // 商品分類
    //renderPrdTypeSelectAll(prdTypeTree);
    vo.typeTreeNodes = (!isNA(vo.prdTypeTree) && !isNA(vo.prdTypeTree.nodes)) ? vo.prdTypeTree.nodes : [];
    renderPrdTypeSelect();// for search form
    renderPrdTypeSelectBS();// for product basic form
    // hide detail area
    $("#selectedItemHeader").hide();
    $("#fsBasic").hide();
    $("#fsFullInfo").hide();
}

// for Store Area
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
    //console.log("areaSearchList = ", areaSearchList);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildProductListLazy(formData, ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;

    // 先取得總筆數 for 分頁
    var restUrl = PRD_COUNT_URL;// 先查總筆數
    if( !_adminUser ){
        formData["storeId"] = null;
    }
    utils.postData(restUrl, formData, false, fetchProductListLazy, null, ajaxKey);
}

function fetchProductListLazy(response, formData, ajaxKey) {
    var totalRows = response.res.totalRows;
    vo.urlPrefix = response.res.urlPrefix;

    renderProductListLazy(formData, totalRows, false, ajaxKey);
}

function renderProductListLazy(formData, totalRows, retry, ajaxKey) {
    var pageSize = 10;
    var minWidth = 700;
    var marginWidth = 80;
    vo.expHeaders = [];
    vo.pageItems = [];
    
    var columnAll = [
        // 勾選欄
        {field: "id", headerText: utils.i18n("fd.check"), sortable: false, bodyClass: "tac", content: genCheckBoxContent, disabled:_tccDealer, exported:false},
        // 封面圖
        {field: "url", headerText: utils.i18n("prd.pic.icon"), sortable: false, bodyClass: "tac", content: genPictureContent, exported:false},
        // ID
        {field: "id", headerText: "ID", sortable: true, bodyClass: "tar"},
        {field: "storeName", headerText: utils.i18n("fd.storeName"), sortable: true, disabled:!_adminUser},
        {field: "cname", headerText: utils.i18n("fd.prd.name"), sortable: true},
        {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: true},
        {field: "statusName", headerText: utils.i18n("fd.statusName"), sortable: true},
        {field: "vendorName", headerText: utils.i18n("fd.vendorName"), sortable: true},
        {field: "typeName", headerText: utils.i18n("fd.typeName"), sortable: true},
        //{field: "compareAtPrice", headerText: utils.i18n("fd.compareAtPrice"), sortable: true, bodyClass: "tar", content: genCompareAtPriceContent},
        {field: "price", headerText: utils.i18n("fd.price"), sortable: true, bodyClass: "tar", content: genPriceContent},
        {field: "curName", headerText: utils.i18n("fd.curName"), sortable: true},
        //{field: "stock", headerText: utils.i18n("fd.stock"), sortable: true, bodyClass: "tar", content: genStockContent},
        //{field: "favCount", headerText: utils.i18n("fd.favprd.count"), sortable: true, bodyClass: "tar"},
        {field: "publishTime", headerText: utils.i18n("fd.publishTime"), sortable: true, content: genPubTimeContent},
        {field: "createtime", headerText: utils.i18n("fd.createtime"), sortable: true, content: genCreateTimeContent},
        {field: "modifytime", headerText: utils.i18n("fd.modifytime"), sortable: true, content: genModifyTimeContent}
    ];

    // 處理自訂屬性 disabled、exported 
    var columns = [];
    for(var i=0; i<columnAll.length; i++){
        if( !utils.isTrue(columnAll[i].disabled) ){
            columns.push(columnAll[i]);
        }
        if( !isFalse(columnAll[i].exported) ){
            vo.expHeaders.push(columnAll[i].headerText);
        }
    }

    renderSearchResultNum("#searchResultMsg", totalRows, !_tccDealer, true);
    $("#divMultiOperation").hide();
    $("#divMultiOperationAdmin").hide();
    
    if (totalRows === 0) {
        clearDataTable('#tbProductLazy', columns, minWidth, marginWidth, ajaxKey);
    } else {
        renderDataTableLazy('#tbProductLazy', pageSize, totalRows, columns, PRD_LIST_URL,
                !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth, 
                afterRenderTable, beforeRenderTable);
        createBtn('#btnExpResult', 'fa-file-excel-o', onClickExpPrd);// 匯出
    }
}

function beforeRenderTable(formData, list){
    vo.picList = [];
    vo.pageItems = [];
    vo.checkedItems = [];
}

function afterRenderTable(formData, list){
    // 圖示LightBox
    for(var i=0; i<vo.picList.length; i++){
        var id = vo.picList[i].id;
        var url = vo.picList[i].url;
        var name = vo.picList[i].name;
        var size = vo.picList[i].size;
        console.log("url = ", url);
        createImgLightBox('#prdPic'+id, url, url, name, size, size);
    }
    
    //console.log("afterRenderTable pageItems = \n", pageItems);
    // 多筆操作按鈕
    if( !utils.isEmptyAry(vo.pageItems) ){
        if( _adminUser ){
            $("#divMultiOperationAdmin").show();
        }else{
            $("#divMultiOperation").show();
        }
    }
    // 全選/全不選
    displaySelectAll(!utils.isEmptyAry(vo.pageItems));
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
        var prdId = vo.pageItems[i];
        $('#cbSel' + prdId).prop("checked", checked);
        checkRow(vo.pageItems[i]);
    }
}
// 勾選欄
function genCheckBoxContent(data) {
    if( !_adminUser || (_adminUser && data.status===PRD_STATUS_APPLY) ){
        vo.pageItems.push(data.id);
        return '<input type="checkbox" id="cbSel' + data.id + '" onchange="checkRow(' + data.id + ')" />';
    }
    return "";
}
// 圖示欄
function genPictureContent(data) {
    if( isEmpty(data.url) ){
        return "<i class='fa fa-question-circle-o fa-2x' title='"+utils.i18n("no.upload.pic")+"' ></i>";
    }
    var url = isEmpty(vo.urlPrefix) ? data.url : vo.urlPrefix + data.url;
    var size = utils.isSmallScreen()?100:50;    
    var img = {
        "id":data.id,
        "url":url,
        "size":size,
        "name":data.cname
    };
    vo.picList.push(img);
    return "<span id='prdPic"+data.id+"' ></span>";//"<img src='"+url+"' style='max-width:"+size+"px;max-height:"+size+"px;' />";
}
function genPriceContent(row) {
    return utils.printNumber(row.price);
}
function genStockContent(row) {
    return utils.printNumber(row.stock);
}
function genCompareAtPriceContent(row) {
    return utils.printNumber(row.compareAtPrice);
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
    console.log("checkRow checkedItems = \n", vo.checkedItems);
}

// DataTable選取項目
function onSelectRow(item) {
    if (isNA(item.id) || isNA(item.storeId)) {
        _alert(utils.i18n("data.err"));
        return;
    }
    $(".forOld").show();// 舊商品才顯示區域
    
    vo.storeId = item.storeId;
    resetSelectedItemNavBar();
    createGoToBtn("#btnGoToBasic", "#fsBasic");
    createGoToBtn("#btnGoToVariantOptions", "#fsVariantOptions");
    createGoToBtn("#btnGoToVariant", "#fsVariant");
    createGoToBtn("#btnGoToProdPics", "#divProdPics");
    createGoToBtn("#btnGoToProdIntro", "#divProdIntro");
    createGoToBtn("#btnGoToDetail", "#fsDetail");
    createGoToBtn("#btnGoToPrdAttrs", "#fsPrdAttrs");
    // fetch full product info 
    var restUrl = PRD_FULL_URL.replace("{id}", item.id);
    restUrl = _adminUser ? utils.addUrlQueryParam(restUrl, "storeId", vo.storeId) : restUrl;
    utils.fetchData(restUrl, false, renderPrdFullInfo, null, "prdFullInfo");
}

// 顯示商品完整資訊
function renderPrdFullInfo(item, ajaxKey) {
    console.log("renderPrdFullInfo item = ", item);
    vo.itemSelected = item;
    // 選取項目
    genSelectedItemTitle(item.cname);
    // 基本資料
    // 會觸發 onchange 所以一定要照順序
    //$("#editPrdBasicForm")[0].reset();
    onClickResetBasic(null);
    utils.copyPropsToForm(item, "BS");

    //buildBasicFrom();
    var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
    var ops = null;
    if (!isNA(parentTypesL1)) {
        ops = utils.convertTreeNodesToOps(parentTypesL1);
    }
    createDropdown("#typeL1BS", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1BS);
    setDropdownValue("#typeL1BS", isNA(item.typeL1) ? 0 : item.typeL1);

    setDropdownValue("#brandBS", isNA(item.brandId) ? 0 : item.brandId);
    setDropdownValue("#vendorBS", isNA(item.vendorId) ? 0 : item.vendorId);
    setDropdownValue("#priceUnitBS", isNA(item.priceUnit) ? 0 : item.priceUnit);
    setDropdownValue("#weightUnitBS", isNA(item.weightUnit) ? 0 : item.weightUnit);

    $('#spanCurName').html(item.curName);
    $('#statusName').html(item.statusName);
    var label = (item.status === PRD_STATUS_RESERVED) ? utils.i18n("time.pub.reserve") : utils.i18n("pub.time");
    var pubTimeLabel = (!isNA(item.publishTime) 
            && (item.status===PRD_STATUS_PUB
                ||item.status===PRD_STATUS_RESERVED))
            ? '<span title="' + utils.i18n("pub.time") + '" >(' + label + '：' + utils.formatDateStr(item.publishTime) + ")</span>":"";
    
    $('#showPublishTime').html(pubTimeLabel);// 最近上架時間
    $('#publishTime').hide();// 預約上架時間
    $("#approveDIV").hide();// 審核
    $("#publishDIV").hide();// 上架

    // 下單數量須為正整數
    var intAmt = isNA(item.intAmt) ? false : item.intAmt;
    setCheckbox('#intAmtBS', intAmt);

    createBtn('#btnChangeStatus', "fa fa-refresh", onClickChangeStatus);// 變更狀態
    if (item.status === PRD_STATUS_PASS) {
        createBtn('#btnPublish', "fa fa-arrow-up", onClickPublish);// 上架
        $("#publishDIV").show();
    }

    // 庫存
    $('#stockQuantity').html(isNA(item.stock) ? 0 : item.stock);

    // 型別選項/設定
    vo.noVariant = isNA(vo.itemSelected.hasVariations) || !vo.itemSelected.hasVariations;
    // 暫不支援
    /*
    if (vo.noVariant) {
        checkCheckbox('#cbNoVariant');// 預設單一型別
    } else {
        uncheckCheckbox('#cbNoVariant');
    }
    */
    vo.colors = utils.getListProp(vo.itemSelected, 'colors');
    vo.sizes = utils.getListProp(vo.itemSelected, 'sizes');
    checkNoVariant();
    renderVariantOptions(vo.colors, vo.sizes);

    vo.variants = utils.getListProp(vo.itemSelected, 'variants');
    // renderVariantList('#existedVariants', variants, 'onEditVariant', 'onDeleteVariant');
    vo.variantFL = new FormListS("variantFL",
            "#editVariantForm", "#existedVariants", '#saveVariant', '#clearVariant', // selector
            vo.variants, // data list
            PRD_VAR_SAVE_URL.replace("{prdId}", vo.itemSelected.id),
            PRD_VAR_DEL_URL.replace("{prdId}", vo.itemSelected.id), // rest url
            renderVariantList,
            onEditVariant, afterSaveVariant, true, // call back 
            getFormDataVariant, // get form input data
            valiateVariant, null, null);
    vo.variantFL.init();

    // 產生商品明細資訊
    renderDetail(vo.itemSelected, false);

    // iframe upload page 
    document.getElementById("ifUpload").src = "lib/fileupload/fileUpload.html?prdId="
            + vo.itemSelected.id + "&storeId=" + vo.storeId + "&adminUser=" + _adminUser;

    $("#selectedItemHeader").show();
    $("#fsBasic").show();
    $("#fsFullInfo").show();

    if (_adminUser) {
        $('#editPrdBasicForm > .ui-inputtext').each(function () {
            $(this).removeAttr('border');
        });
        utils.disableFormInput("#editPrdBasicForm");

        disableDropdown("#typeL1BS");
        disableDropdown("#typeL2BS");
        disableDropdown("#typeL3BS");
        hideDropdown("#brandBS");
        $("#brandBSTxt").html(utils.prettyPrint(vo.itemSelected.brandName));
        hideDropdown("#vendorBS");
        $("#vendorBSTxt").html(utils.prettyPrint(vo.itemSelected.vendorName));
        hideDropdown("#priceUnitBS");
        $("#priceUnitBSTxt").html(utils.prettyPrint(vo.itemSelected.priceUnitName));
        hideDropdown("#weightUnitBS");
        $("#weightUnitBSTxt").html(utils.prettyPrint(vo.itemSelected.weightUnitName));

        disableCheckbox('#intAmtBS');

        $("#statusDIV").hide();
        if (vo.itemSelected.status === PRD_STATUS_APPLY) {// 申請上架審核
            $("#approveDIV").show();
        }
        
        if( utils.isEmptyAry(vo.colors) && utils.isEmptyAry(vo.sizes) ){
            $("#fsVariantOptions").hide();
            $("#fsVariant").hide();
        }

        $("#editPrdBasicForm .ui-dropdown .ui-dropdown-label").css("font-weight", "bold");
        $("#editPrdBasicForm .ui-state-disabled").css("opacity", ".9");
        $("#editPrdBasicForm :disabled").css("opacity", ".9");
    }else{
        if( _tccDealer ){
            utils.disableFormInput("#editPrdBasicForm");
            $("#priceBS").removeAttr('disabled');
            
            disableDropdown("#typeL1BS");
            disableDropdown("#typeL2BS");
            disableDropdown("#typeL3BS");
            disableDropdown("#brandBS");
            disableDropdown("#vendorBS");
            disableDropdown("#priceUnitBS");
            disableDropdown("#weightUnitBS");
            disableCheckbox('#intAmtBS');
            
            $("#editPrdBasicForm .ui-dropdown .ui-dropdown-label").css("font-weight", "bold");
            $("#editPrdBasicForm .ui-state-disabled").css("opacity", ".6");
            $("#editPrdBasicForm :disabled").css("opacity", ".6");
            
            createBtn('#btnOnSale', "fa fa-arrow-up", onClickOnSale);// TCC PRD 上架
            createBtn('#btnNotOnSale', "fa fa-arrow-down", onClickNotOnSale);// TCC PRD 下架
            if( vo.itemSelected.status===PRD_STATUS_PUB ){
                $("#btnNotOnSale").show();
                $("#btnOnSale").hide();
            }
            if( vo.itemSelected.status===PRD_STATUS_REMOVE ){
                $("#btnOnSale").show();
                $("#btnNotOnSale").hide();
            }
            
            $("#fsFullInfo").hide();// 無商品明细
            $("#resetBasic").hide();
            $("#btnChangeStatus").hide();
            $("#statusDIV").hide();
            $("#approveDIV").hide();
        }else{
            setDropdownValue("#statusBS", ''); // isNA(item.status)?"":item.status
            $("#statusDIV").show();
        }

        // 新增時會隱藏
        $('#delete').show();// 刪除
        $('#btnAddStockLog').show();// 新增庫存
        $('#btnViewStockLog').show();// 檢視庫存
    }
    
    // 台泥匯入商品不需維護明細資料
    if( vo.itemSelected.tccPrd ){
        hideNotTccPrd();
    }else{
        showNotTccPrd();
    }

    utils.scrollTo('#selectedItemHeader');
    _asyncCompleteFlags[ajaxKey] = true;
}

function onClickOnSale(){
    changeTccPrdStatus(PRD_STATUS_PUB);
}
function onClickNotOnSale(){
    changeTccPrdStatus(PRD_STATUS_REMOVE);
}
function changeTccPrdStatus(status){
    if (!confirm(utils.i18n("confirm.change.status"))) {
        return;
    }
    var formData = {
        "storeId": vo.storeId,
        "status": status,
        "id": vo.itemSelected.id,
        "forDealer":true
    };
    var restUrl = PRD_CH_STATUS_URL;
    utils.postData(restUrl, formData, false, afterSaveBasic);
}

function onClickChangeStatus(){
    if (!confirm(utils.i18n("confirm.change.status"))) {
        return;
    }
    //var $form = $("#editPrdBasicForm");
    //var formData = $form.serializeFormJSON();
    var formData = {};
    formData["id"] = vo.itemSelected.id;
    formData["publishTime"] = getTextValue("#publishTime");// getDateTimePicker("#publishTime");
    formData["status"] = getDropdownValue("#statusBS");
    if (formData["status"] === PRD_STATUS_RESERVED) {// 預約上架
        formData["publishTime"] = toISODateTime(formData["publishTime"]);
    } else {
        formData["publishTime"] = vo.itemSelected.publishTime;
    }
    console.log("onClickChangeStatus =\n", formData);
    var restUrl = PRD_CH_STATUS_URL;
    utils.postData(restUrl, formData, false, afterSaveBasic);
}
function onClickPublish() {
    if (!confirm(utils.i18n("confirm.publish"))) {
        return;
    }
    var formData = {
        "storeId": vo.storeId,
        "id": vo.itemSelected.id
    };
    var restUrl = PRD_PUBLISH_URL;
    utils.postData(restUrl, formData, false, afterSaveBasic);
}

function checkNoVariant() {
    vo.noVariant = true; //getCheckbox("#cbNoVariant");// 暫不支援
    if (!vo.noVariant) {
        $('#fsVariantOptions').show();
        $("#fsVariant").show();
        enableBtn('#variantOps');
    } else {
        $('#fsVariantOptions').hide();
        $("#fsVariant").hide();
        disableBtn('#variantOps');
    }
}

function genPubTimeContent(row) {
    return formatDateTimeStr(row.publishTime);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Operation">
// 新增商品
function onClickAddPrd(event) {
    resetSelectedItemNavBar();
    createGoToBtn("#btnGoToBasic", "#fsBasic");
    createGoToBtn("#btnGoToVariantOptions", "#fsVariantOptions");
    createGoToBtn("#btnGoToVariant", "#fsVariant");
    createGoToBtn("#btnGoToProdPics", "#divProdPics");
    createGoToBtn("#btnGoToProdIntro", "#divProdIntro");
    createGoToBtn("#btnGoToDetail", "#fsDetail");
    createGoToBtn("#btnGoToPrdAttrs", "#fsPrdAttrs");

    vo.itemSelected = {};

    $('#editPrdBasicForm')[0].reset();
    renderPrdTypeSelectBS();// for product basic form

    $(".forOld").hide();// 舊商品才顯示區域

    // 狀態
    setDropdownValue("#statusBS", '');
    $('#statusName').html(utils.i18n("draf.data"));// 草稿
    $('#spanCurName').html("人民幣");
    $('#showPublishTime').html("");// 最近上架時間
    $('#publishTime').hide();// 預約上架時間
    $("#approveDIV").hide();// 審核
    $("#publishDIV").hide();// 上架
    $("#statusDIV").hide();
    $('#stockQuantity').html(0);// 庫存

    $('#delete').hide();// 刪除
    $('#btnAddStockLog').hide();// 新增庫存
    $('#btnViewStockLog').hide();// 檢視庫存

    $("#selectedItemHeader").show();
    $("#fsBasic").show();
    disableBtn('#variantOps');
    //$('#delete').puibutton('enable');
    $('#fsVariantOptions').hide();
    $("#fsVariant").hide();

    genSelectedItemTitle(utils.i18n("add.prd"));
    // 型別設定
    vo.noVariant = true;
    //checkCheckbox("#cbNoVariant");// 暫不支援
    // 商品明細
    $("#fsFullInfo").hide();

    utils.scrollTo('#selectedItemHeader');
}

// 刪除商品
function onClickDelete(event) {
    if (confirm(utils.i18n("remove.prd.confirm"))) {
        console.log(vo.itemSelected);
        utils.postDelete(PRD_DEL_URL, vo.itemSelected.id, null, afterDelete, null);
    }
}

function afterDelete(response, formData, optional) {
    // hide detail area
    $("#selectedItemHeader").hide();
    $("#fsBasic").hide();
    $("#fsFullInfo").hide();
    
    onClickSearch();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Reapprove">
// 重新送審確認
function confirmReapprove() {
    if ( !isNA(vo.itemSelected) 
      && (vo.itemSelected.status === PRD_STATUS_PUB 
      || vo.itemSelected.status === PRD_STATUS_RESERVED 
      || vo.itemSelected.status === PRD_STATUS_PASS)) {
        if( utils.isTrue(vo.itemSelected.tccPrd) ){// 台泥匯入商品不需重審
            return confirm(utils.i18n("confirm.reApprove3"));
        }else{
            return confirm(utils.i18n("confirm.reApprove1") + "\n" + utils.i18n("confirm.reApprove2") + "\n" + utils.i18n("confirm.reApprove3"));
        }
    }
    
    return true;
}
// 重新送審後刷新頁面(狀態)
function afterReapprove() {
    if ( !isNA(vo.itemSelected) 
      && !utils.isTrue(vo.itemSelected.tccPrd) // 台泥匯入商品不需重審
      && (vo.itemSelected.status === PRD_STATUS_PUB 
      || vo.itemSelected.status === PRD_STATUS_RESERVED 
      || vo.itemSelected.status === PRD_STATUS_PASS)) {
        vo.itemSelected["status"] = PRD_STATUS_APPLY;// 重審
        vo.itemSelected["statusOri"] = PRD_STATUS_APPLY;
        renderPrdFullInfo(vo.itemSelected);
        reloadDataTable('#tbProductLazy', true);// keep status reload
        return true;// 重新送審
    }
    return false;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Stock">
function onClickAddStockLog() {
    buildAddStockLogDlg();
}
function onClickViewStockLog() {
    fetchStockLog();
}
function buildAddStockLogDlg() {
    console.log("buildAddStockLogDlg ...");

    createDropdown("#logType", vo.stockLogOps, utils.genNoSelectOpStr(), null);
    setDropdownValue("#logType", "");

    $('#dlgAddStockLog').puidialog({
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
                    $('#dlgAddStockLog').puidialog('hide');
                }
            }, {
                text: utils.i18n("save.and.next"),
                icon: 'fa-save',
                click: onClickSaveStockLogNext
            }, {
                text: utils.i18n("save.and.close"),
                icon: 'fa-save',
                click: onClickSaveStockLogClose
            }],
            "beforeShow":function(event) {
                utils.hideBodyScrollY();
            },
            "afterHide":function(event) {
                utils.showBodyScrollY();
            }
    });

    $("#dlgAddStockLog").show();
}
function buildViewStockLogDlg() {
    console.log("buildViewStockLogDlg ...");

    $('#dlgViewStockLog').puidialog({
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
                    $('#dlgViewStockLog').puidialog('hide');
                }
            }],
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }

    });

    $("#dlgViewStockLog").show();
}
function fetchStockLog() {
    // 先取得總筆數 for 分頁
    var formData = {};
    formData["storeId"] = _adminUser ? vo.storeId : null;
    formData["prdId"] = vo.itemSelected.id;

    var restUrl = PRD_STOCK_COUNT_URL.replace("{prdId}", vo.itemSelected.id);
    // 先查總筆數
    utils.postData(restUrl, formData, false, fetchStockLogLazy, null, "stockLogs");
}
function fetchStockLogLazy(response, formData, ajaxKey) {
    var totalRows = response.res.totalRows;
    renderStockLogLazy(formData, totalRows, false, ajaxKey);

    buildViewStockLogDlg();
}
function renderStockLogLazy(formData, totalRows, retry, ajaxKey) {
    var pageSize = 10;
    var minWidth = 380;
    var marginWidth = 0;
    var columns = [
        {field: "dataTime", headerText: utils.i18n("fd.stock.dataTime"), sortable: false},
        {field: "typeName", headerText: utils.i18n("fd.stock.type"), sortable: false},
        {field: "quantity", headerText: utils.i18n("fd.stock.quantity"), sortable: false},
        {field: "memo", headerText: utils.i18n("fd.stock.memo"), sortable: false}
    ];

    renderSearchResultNum("#slSearchResultMsg", totalRows);
    if (totalRows === 0) {
        clearDataTable('#tbStockLog', columns, minWidth, marginWidth, ajaxKey);
    } else {
        renderDataTableLazy('#tbStockLog', pageSize, totalRows, columns, PRD_STOCK_LOGS_URL.replace("{prdId}", vo.itemSelected.id),
                !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}

function onClickSaveStockLogNext() {
    if (saveStockLog()) {
        setDropdownValue("#logType", "");
        setTextValue("#logQuantity", "");
        setTextValue("#logMemo", "");
    }
}
function onClickSaveStockLogClose() {
    if (saveStockLog()) {
        setDropdownValue("#logType", "");
        setTextValue("#logQuantity", "");
        setTextValue("#logMemo", "");

        $('#dlgAddStockLog').puidialog('hide');
    }
}
function saveStockLog() {
    if (!confirm(utils.i18n("confirm.change.stock"))) {
        return false;
    }
    var $form = $("#fmAddStockLog");
    var formData = $form.serializeFormJSON();
    console.log("saveStockLog formData = \n", formData);

    var restUrl = PRD_STOCK_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
    utils.postData(restUrl, formData, false, afterSaveStockLog);
    return true;
}
function afterSaveStockLog(response, formData, ajaxKey) {
    afterSaveBasic(response, ajaxKey);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Basic">
function buildOperationBasic() { 
    // 基本資料編輯區
    //$("#startAt").datetimepicker({"showTimepicker": false, "showButtonPanel": false});
    //$("#publishTime").datetimepicker({"showTimepicker": false, "showButtonPanel": false});
    createDatePicker("#startAt");
    //createDatePicker("#publishTime");

    if( _adminUser ){
        createBtn('#btnPassM', 'fa-check-square-o', onClickApproveMulti);
        createBtn('#btnRejectM', 'fa-ban', onClickRejectMulti);

        createBtn('#btnApprove', 'fa-check-square-o', onClickApprove);// 審核通過
        createBtn('#btnReject', 'fa-ban', onClickReject);// 審核不通過

        $("#btnAddStockLog").hide();
    } else {
        createBtn('#add', 'fa-plus-square', onClickAddPrd);// 新增
        createBtn('#delete', 'fa-trash', onClickDelete);// 刪除
        createBtn('#saveBasic', 'fa-save', onClickSaveBasic);// 儲存
        createBtn('#resetBasic', 'fa-ban', onClickResetBasic);// 重設

        createBtn('#btnReserve', 'fa-clock-o', onClickReserve);// 預約上架
        createBtn('#btnUnreserve', 'fa-ban', onClickUnreserve);// 取消預約
        createBtn('#btnPublishNowM', 'fa-arrow-up', onClickPublishNow);
        createBtn('#btnSendApproveM', 'fa-share-square-o ', onClickSendApprove);
        createBtn('#btnNoSaleM', 'fa-arrow-down', onClickNoSale);
        createBtn('#btnAddStockLog', 'fa-plus-square', onClickAddStockLog); // 新增庫存
    }

    createBtn('#btnViewStockLog', 'fa-eye', onClickViewStockLog);// 檢視庫存
}

// 勾選檢查
function checkEmptySelect() {
    if (utils.isEmptyAry(vo.checkedItems)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("nosel.item"));
        return false;
    }
    return true;
}

// 多筆狀態變更
function changeMultiPrdStatus(status) {
    if( !checkEmptySelect() ){
        return;
    }
    if( !confirm(utils.i18n("confirm.multi.status")) ){
        return;
    }
    var formData = {
        "prdList": vo.checkedItems,
        "status": status
    };
    utils.postData(PRD_MULTI_STATUS_URL, formData, false, afterChangeMultiPrdStatus);
}
function afterChangeMultiPrdStatus(response) {
    var totalRows = response.res.totalRows;
    reloadDataTable('#tbProductLazy', true);// keep status reload
    vo.checkedItems = [];
    clearSelectAll();
    utils.addMessage("info", utils.i18n("prompts"), utils.i18nWP("multi.status.counts", totalRows));
}

function onClickPublishNow() {
    changeMultiPrdStatus(PRD_STATUS_PUB);
}
function onClickSendApprove() {
    changeMultiPrdStatus(PRD_STATUS_APPLY);
}
function onClickNoSale() {
    changeMultiPrdStatus("R");
}

function onClickApproveMulti(){
    changeMultiPrdStatus(PRD_STATUS_PASS);
}

function onClickRejectMulti(){
    changeMultiPrdStatus(PRD_STATUS_REJECT);
}

function onClickApprove() {
    if (!confirm(utils.i18n("confirm.approve"))) {
        return;
    }
    var formData = {
        "storeId": vo.storeId,
        "id": vo.itemSelected.id,
        "pass": true
    };
    var restUrl = PRD_APPROVE_URL;
    utils.postData(restUrl, formData, false, afterSaveBasic);
}
function onClickReject() {
    if (!confirm(utils.i18n("confirm.reject"))) {
        return;
    }
    var formData = {
        "storeId": vo.storeId,
        "id": vo.itemSelected.id,
        "pass": false
    };
    var restUrl = PRD_APPROVE_URL;
    utils.postData(restUrl, formData, false, afterSaveBasic);
}

function onClickExpPrd() {
    console.log("onClickExpPrd ...");
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["headers"] = vo.expHeaders;

    var restUrl = PRD_EXP_URL;
    utils.postData(restUrl, formData, false, afterExpPrd);
}

function afterExpPrd(response) {
    console.log("afterExpPrd ...");
    if (!isNA(response.filename)) {
        window.location.href = GET_EXP_URL + "?filename=" + response.filename;
    } else {
        utils.addMessage("error", utils.i18n("errors"), utils.i18n("exp.fail"));
    }
}

function changeIntAmt() {
    console.log("changeIntAmt ..." + getCheckbox("#intAmtBS"));
}

// 產生初始商品類別選單
function renderPrdTypeSelectBS() {
    var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
    var ops = null;
    if (!isNA(parentTypesL1)) {
        ops = utils.convertTreeNodesToOps(parentTypesL1);
    }
    createDropdown("#typeL1BS", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1BS);// 第一層
    enableDropdown('#typeL1BS');
    createDropdown("#typeL2BS", null, utils.genNoSelectOpNum(), onChangePrdTypeL2BS);// 第二層
    disableDropdown('#typeL2BS');
    createDropdown("#typeL3BS", null, utils.genNoSelectOpNum(), onChangePrdTypeL3BS);// 第三層
    disableDropdown('#typeL3BS');
}

function onChangePrdTypeL1BS(item) {
    console.log("onChangePrdTypeL1BS item = ", item);
    // 第二層選單
    var parentTypesL2 = utils.findChildrenFromTree(vo.typeTreeNodes, item.value);
    var ops = null;
    if (!isNA(parentTypesL2)) {
        ops = utils.convertTreeNodesToOps(parentTypesL2);
    }
    createDropdown("#typeL2BS", ops, utils.genNoSelectOpNum(), onChangePrdTypeL2BS);
    setDropdownValue('#typeL2BS', (isNA(vo.itemSelected) || isNA(vo.itemSelected.typeL2)) ? 0 : vo.itemSelected.typeL2);
    enableDropdown('#typeL2BS');

    //createDropdown("#typeL3BS", null, utils.genNoSelectOpNum(), onChangePrdTypeL3BS);// 第三層
    //disableDropdown('#typeL3BS');
}

function onChangePrdTypeL2BS(item) {
    // 第3層選單
    var parentTypesL3 = utils.findChildrenFromTree(vo.typeTreeNodes, item.value);
    var ops = [];
    if (!isNA(parentTypesL3)) {
        ops = utils.convertTreeNodesToOps(parentTypesL3);
    }
    createDropdown("#typeL3BS", ops, utils.genNoSelectOpNum(), onChangePrdTypeL3BS);
    setDropdownValue('#typeL3BS', (isNA(vo.itemSelected) || isNA(vo.itemSelected.typeId)) ? 0 : vo.itemSelected.typeId);
    enableDropdown('#typeL3BS');
}

function onChangePrdTypeL3BS(item) {
    if (item.value === 0) {
        vo.prdTypeAttrs = [];
        createDropdown("#attrId", vo.prdTypeAttrs, utils.genNoSelectOpNum(), null);// 規格名稱
    } else {
        var restUrl = PRD_TYPE_ATTR_URL.replace("{typeId}", item.value);
        restUrl = _adminUser ? utils.addUrlQueryParam(restUrl, "storeId", vo.storeId) : restUrl;
        utils.fetchData(restUrl, false, renderByPrdType, null, "prdTypeAttr");
    }
}

// 產品分類關聯資料
function renderByPrdType(response) {
    vo.prdTypeAttrs = utils.listToOptions(utils.getResponseList(response), "id", "cname");
    createDropdown("#attrId", vo.prdTypeAttrs, utils.genNoSelectOpNum(), null);// 規格名稱
}

// 商品基本資料表單
function buildBasicFrom() {
    if( !_adminUser ){
        createDropdown("#statusBS", vo.prdStatusSeller, {label: utils.i18n("sel.new.status"), value: ""}, onChangeStatusBasic);
    }
    createDropdown("#vendorBS", vo.sellerVendor, utils.genNoSelectOpNum(), null);
    createDropdown("#brandBS", vo.prdBrand, utils.genNoSelectOpNum(), null);
    createDropdown("#priceUnitBS", vo.prdUnit, utils.genNoSelectOpNum(), null);
    createDropdown("#weightUnitBS", vo.weightUnit, utils.genNoSelectOpNum(), null);
    createCheckbox("#intAmtBS");
}

// 變更狀態
function onChangeStatusBasic(item) {
    console.log(vo.prdStatusSeller);
    console.log(item);
    console.log(vo.itemSelected.statusOri);

    var statusOri = ""; //vo.itemSelected.statusOri

    $('#publishTime').hide();

    if (!isEmpty(item.value) && !utils.contains(vo.prdStatusSeller, item, "value")) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("cannot.change.status"));
        setDropdownValue("#statusBS", statusOri);
        return;
    }
    if (item.value === PRD_STATUS_RESERVED) {// 預約上架
        if (!(vo.itemSelected.statusOri === PRD_STATUS_RESERVED || vo.itemSelected.statusOri === PRD_STATUS_PASS)) {// 預約上架、審核通過
            utils.addMessage("error", utils.i18n("in.err"), utils.i18n("cannot.reserve"));
            setDropdownValue("#statusBS", statusOri);
            return;
        }
        $('#publishTime').show();
    }
}

// 儲存商品基本資料
function onClickSaveBasic(event) {
    var $form = $("#editPrdBasicForm");
    var formData = $form.serializeFormJSON();
    formData["id"] = vo.itemSelected.id;
    if (formData["status"] === PRD_STATUS_RESERVED) {// 預約上架
        formData["publishTime"] = toISODateTime(formData["publishTime"]);
    } else {
        formData["publishTime"] = vo.itemSelected.publishTime;
    }
    formData["intAmt"] = getCheckbox('#intAmtBS');

    //vo.noVariant = getCheckbox("#cbNoVariant");// 暫不支援
    formData["hasVariations"] = !vo.noVariant;

    console.log("onClickSaveBasic ", formData);

    if (!valiateBasic(formData)) {
        return;
    }

    if( confirmReapprove() ){// 重審確認
        var restUrl = PRD_SAVE_URL;
        formData["storeId"] = _adminUser ? vo.storeId : null;
        formData["status"] = isEmpty(formData["status"]) ? vo.itemSelected.statusOri : formData["status"];
        utils.postData(restUrl, formData, false, afterSaveBasic, null, "saveBasic");
    }
}

function afterSaveBasic(response, ajaxkey) {
    vo.itemSelected = response;
    renderPrdFullInfo(response);

    //reloadDataTable('#tbProductLazy', true);// keep status reload    
    onClickSearch(null);
    utils.addSuccessMsg();
}

// 重設商品基本資料 Form
function onClickResetBasic(event) {
    setDropdownValue('#typeL3BS', 0);
    setDropdownValue('#typeL2BS', 0);
    setDropdownValue('#typeL1BS', 0);
    setDropdownValue('#priceUnitBS', 0);
    setDropdownValue('#brandBS', 0);
    setDropdownValue('#vendorBS', 0);
    setDropdownValue('#weightUnitBS', 0);
    if( !_adminUser ){
        //setDropdownValue('#statusBS', 'D');
        setDropdownValue('#statusBS', '');
    }

    $("#editPrdBasicForm")[0].reset();
}

function valiateBasic(formData) {
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editPrdBasicForm");
    if (!isEmpty(msg)) {
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    if (formData["status"] === PRD_STATUS_RESERVED && isEmpty(formData["publishTime"])) {// 預約上架
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("noinput.reserve"));
        return false;
    }

    return true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Reserve">
function onClickReserve() {
    if (!checkEmptySelect()) {
        return;
    }
    var startAt = toISODateTime($("#startAt").val());
    //var startAt = getDateTimePicker("#startAt");
    if (isEmpty(startAt)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("oninput.reserve.date"));
        return;
    }

    var formData = {
        "prdList": vo.checkedItems,
        "active": true,
        "startAt": startAt
    };
    formData["storeId"] = _adminUser ? vo.storeId : null;
    utils.postData(PRD_RESERVE_URL, formData, false, afterReserve, null, null);
}

function afterReserve(response) {
    var totalRows = response.res.totalRows;
    reloadDataTable('#tbProductLazy', true);// keep status reload 
    vo.checkedItems = [];
    utils.addMessage("info", utils.i18n("prompts"), utils.i18nWP("finish.reserve.counts", totalRows));
}

function onClickUnreserve() {
    if (!checkEmptySelect()) {
        return;
    }
    var formData = {
        "prdList": vo.checkedItems,
        "active": false
    };
    formData["storeId"] = _adminUser ? vo.storeId : null;
    utils.postData(PRD_RESERVE_URL, formData, false, afterReserve, null, null);
}

function afterUnreserve(response) {
    var totalRows = response.res.totalRows;
    reloadDataTable('#tbProductLazy', true);// keep status reload 
    vo.checkedItems = [];
    utils.addMessage("info", utils.i18n("prompts"), utils.i18nWP("cancel.reserve.counts", totalRows));
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Color">
// 顯示顏色
function renderColors(colors) {
    renderSimpleTextEditList('#existedColors', colors, _adminUser ? null : 'onDeleteColor', 'label');
    createDropdown('#colorIn', colors, utils.genNoSelectOpNum(), onSelectColor);
    if (isNA(colors) || colors.length === 0) {
        disableDropdown('#colorIn');
    } else {
        enableDropdown('#colorIn');
    }
}
// 新增顏色
function onClickAddColor(event) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    var val = $('#colorName').val();
    var formData = {};
    formData["prdId"] = vo.itemSelected.id;
    formData["cname"] = val;

    if (confirmReapprove()) {// 重審確認
        var restUrl = PRD_COLOR_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveColors, null, null);
    }
}
// 刪除顏色
function onDeleteColor(idx) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    if (confirm(utils.i18n("remove.item.confirm"))) {
        var formData = {};
        formData["prdId"] = vo.itemSelected.id;
        formData["id"] = vo.colors[idx].value;

        var restUrl = PRD_COLOR_DEL_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveColors, null, null);
    }
}
// 刷新顏色選項
function afterSaveColors(response) {
    if (!isNA(response) && !isNA(response.list)) {
        vo.colors = response.list;
        renderColors(vo.colors);
        setTextValue("#colorName", "");
        checkNoVariant();

        afterReapprove();// 重新送審後刷新頁面(狀態)
        utils.addSuccessMsg();
    }
}

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Size">
// 顯示大小
function renderSizes(sizes) {
    renderSimpleTextEditList('#existedSizes', sizes, _adminUser ? null : 'onDeleteSize', 'label');
    createDropdown('#sizeIn', sizes, utils.genNoSelectOpNum(), onSelectSize);
    if (isNA(sizes) || sizes.length === 0) {
        disableDropdown('#sizeIn');
    } else {
        enableDropdown('#sizeIn');
    }
}
// 新增大小
function onClickAddSize(event) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    var val = $('#sizeName').val();
    //sizes.push(val);
    var formData = {};
    formData["prdId"] = vo.itemSelected.id;
    formData["cname"] = val;

    if (confirmReapprove()) {// 重審確認
        var restUrl = PRD_SIZE_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveSizes, null, null);
    }
}
// 刪除大小
function onDeleteSize(idx) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    if (confirm(utils.i18n("remove.item.confirm"))) {
        var formData = {};
        formData["prdId"] = vo.itemSelected.id;
        formData["id"] = vo.sizes[idx].value;
        var restUrl = PRD_SIZE_DEL_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveSizes, null, null);
    }
}
// 刷新大小選項
function afterSaveSizes(response) {
    if (!isNA(response) && !isNA(response.list)) {
        vo.sizes = response.list;
        renderSizes(vo.sizes);
        setTextValue("#sizeName", "");
        checkNoVariant();
        afterReapprove();// 重新送審後刷新頁面(狀態)
        utils.addSuccessMsg();
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Variant">
// 維護商品型別
function onClickVariant(event) {
    editVariantKey = null;
    // 型別設定
    renderVariantOptions(vo.colors, vo.sizes);

    $('#fsVariantOptions').show();
    $('#fsVariant').show();

    $('#variantOps').puibutton(vo.noVariant ? 'disable' : 'enable');
    //$('#delete').puibutton('disable');

    utils.scrollTo("#fsVariantOptions");
}

function renderVariantOptions(colors, sizes) {
    // 型別設定
    renderColors(colors);
    renderSizes(sizes);
}

// 選取大小
function onSelectSize(data) {
    console.log(data);
}
// 選取顏色
function onSelectColor(data) {
    console.log(data);
}

function changeNoVariant() {
    //vo.noVariant = getCheckbox("#cbNoVariant");// 暫不支援
    console.log("changeNoVariant ...", vo.noVariant);
    $('#variantOps').puibutton(vo.noVariant ? 'disable' : 'enable');
    $('#fsVariantOptions').hide();
    $('#fsVariant').hide();
}

function onEditVariant(row) {
    setDropdownValue('#colorIn', isNA(row.colorId) ? 0 : row.colorId);
    setDropdownValue('#sizeIn', isNA(row.sizeId) ? 0 : row.sizeId);

    //setTextValue("#compareAtPriceIn", row.compareAtPrice);
    setTextValue("#priceIn", row.price);
    //setTextValue("#barcodeIn", row.barcode);
    //setTextValue("#skuIn", row.sku);
}

function getFormDataVariant(formData) {
    formData['colorName'] = getDropdownLabel('#colorIn');
    formData['sizeName'] = getDropdownLabel('#sizeIn');
    console.log("getFormDataVariant formData = ", formData);
    return formData;
}

function valiateVariant(row) {
    console.log("valiateVariant ... ", row);
    if (!utils.isId(row.colorId) && !utils.isId(row.sizeId)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("noinput.color.size"));
        return false;
    }
    /*if (!isEmpty(row.compareAtPrice) && isNaN(row.compareAtPrice)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("oninput.cap"));
        return false;
    }*/
    if (!isEmpty(row.price) && isNaN(row.price)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("inprice.err"));
        return false;
    }
    //if (isEmpty(row.compareAtPrice) && isEmpty(row.price) && isEmpty(row.barcode) && isEmpty(row.sku)) {
    if ( isEmpty(row.price) ) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("oninput.for.variant"));
        return false;
    }
    if (!confirmReapprove()) {// 重審確認
        return false;
    }
    return true;
}

function afterSaveVariant(list) {
    vo.variants = list;
    afterReapprove();// 重新送審後刷新頁面(狀態)
}

// 產生型別維護列表
function renderVariantList(nameFL, selector, values) {
    var html = '<table class="w3-table-all">'
            + '<thead>'
            + '  <tr class="ui-widget-header ui-state-default">'
            + '    <th>' + utils.i18n("operation") + '</th><th>'
            + utils.i18n("fd.color") + '</th><th>'
            + utils.i18n("fd.size") + '</th><th>'
            //+ utils.i18n("fd.compareAtPrice") + '</th><th>'
            + utils.i18n("fd.price") + '</th><th>'
            //+ utils.i18n("fd.barcode") + '</th><th>'
            //+ utils.i18n("fd.sku") + '</th>'
            + '  </tr>'
            + '</thead>';
    if (!isNA(values)) {
        values.sort();
        for (var i = 0; i < values.length; i++) {
            html += '<tr class="w3-padding-small w3-hover-pale-blue">';
            if (!_adminUser) {
                html += '  <td class="fit-content">'
                        + '    <button onclick="' + nameFL + '.onEditRow(' + i + ')" title="' + utils.i18n("edit") + '" class="w3-btn w3-teal" ><i class="fa fa-pencil-square"></i></button>'
                        + '    <button onclick="' + nameFL + '.onDeleteRow(' + i + ')" title="' + utils.i18n("remove") + '" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>'
                        + '  </td>';
            }
            html += '  <td>' + utils.safePrint(values[i].colorName) + '</td>'
                    + '  <td>' + utils.safePrint(values[i].sizeName) + '</td>'
                    //+ '  <td>' + utils.safePrint(values[i].compareAtPrice) + '</td>'
                    + '  <td>' + utils.safePrint(values[i].price) + '</td>'
                    //+ '  <td>' + utils.safePrint(values[i].barcode) + '</td>'
                    //+ '  <td>' + utils.safePrint(values[i].sku) + '</td>'
                    + '</tr>';
        }
    }
    html += "</table>";
    $(selector).html(html);
}

// get variant attributes
function renderVariantAttrList(prefix, item, key, opPrefix, opPostfix) {
    var html = prefix;
    var def = !isNA(item[key]) ? item[key] : utils.i18n("nothing");// 預設取商品主檔設定

    if (!utils.isEmptyAry(item.variants)) {// 有多型別設定
        var varhtml = "";
        for (var i = 0; i < item.variants.length; i++) {
            if (!isNA(item.variants[i][key])) {
                varhtml += item.variants[i].cname + " " + opPrefix + item.variants[i][key] + opPostfix + "<br/>";
            }
        }
        html = html + (varhtml === "" ? def : "<br/>" + varhtml);
    } else {
        html = html + def;
    }

    return html;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Brief">
// 產生商品明細資訊
function renderDetail(item, isNew) {
    // 產品標題
    $("#prdtitle").html(item.cname);
    // 商品訂價 $xxx
    //var compareAtPrice = renderVariantAttrList(utils.i18n("fd.compareAtPrice") + "：", item, "compareAtPrice", "$", "");
    //$("#compareAtPrice").html(compareAtPrice);
    // 商品優惠價 $xxx
    var price = renderVariantAttrList(utils.i18n("fd.price") + "：", item, "price", "$", "");
    $("#price").html(price);
    // 剩餘數量 xx組 inventory_quantity
    //var inventoryQuantity = renderVariantAttrList("剩餘數量：", item, "inventory_quantity", "", " 組");
    //$("#inventorys").html(inventoryQuantity);
    // 商品活動 events
    //var srcEvents = [
    //    {"value":1, "label":"現折活動5/20前衛浴設備結帳85折up"},
    //    {"value":2, "label":"期間限定 !7周年同慶★新客領咖啡▼首購送100+3%購物金"}
    //];

    // 商品簡介
    vo.introductions = (isNew || isNA(item.introductions)) ? [] : item.introductions;
    // 商品活動 events
    vo.events = (isNew || isNA(item.events)) ? [] : item.events;
    // 商品編號 U010143376
    // 配送方式 shippings
    vo.shippings = (isNew || isNA(item.shippings)) ? [] : item.shippings;
    // 付款方式：Web-ATM | ATM | 信用卡
    vo.payments = (isNew || isNA(item.payments)) ? [] : item.payments;
    // 商品詳細說明
    vo.details = (isNew || isNA(item.details)) ? [] : item.details;
    // 商品規格屬性
    vo.attrs = (isNew || isNA(item.attrs)) ? [] : item.attrs;

    renderByPrdType(item);// 商品類別預設規格屬性

    if (isNew) {
        if (!isNA(vo.gallery)) {
            //vo.gallery.stop();
            //vo.gallery.destroy();
            $("#prdPics").hide();
        }
    } else {
        // 商品圖片
        buildPrdPicGalleria("prdPics");
    }

    // 商品簡介
    renderIntroductions(vo.introductions);

    // 商品活動 events
    /*var desEvents = events;
     var eventPickList = new SimplePickList('#events', '#editEvents', '#btEditEvent', '#saveEvent', '#closeEvent', 
     '#pklEvent', srcEvents, desEvents, 
     renderSimpleTextList);
     eventPickList.inti();*/

    // 配送方式 shippings
    var shippingPickList = new SimplePickList('#shippings', '#editShippings', '#btEditShipping', '#saveShipping', '#closeShipping',
            '#pklShipping', vo.shippingOps, vo.shippings,
            renderSimpleListStr, null, onClickSaveShipping, null);
    shippingPickList.inti();

    // 付款方式
    var paymentPickList = new SimplePickList('#payments', '#editPayments', '#btEditPayment', '#savePayment', '#closePayment',
            '#pklPayment', vo.paymentOps, vo.payments,
            renderSimpleListStr, null, onClickSavePayment, null);
    paymentPickList.inti();

    // 商品詳細說明
    renderRichTextList('#details', vo.details, true);
    // 商品規格屬性
    initPrdAttrs(vo.attrs);
}

// 儲存配送方式
function onClickSaveShipping(targetList) {
    var formData = {};
    formData['shippings'] = isNA(targetList) ? [] : targetList;

    if (confirmReapprove()) {// 重審確認
        var restUrl = PRD_SHIP_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveShipping, null, null);
    }
}

function afterSaveShipping(response) {
    vo.shippings = utils.getResponseList(response);
    var shippingPickList = new SimplePickList('#shippings', '#editShippings', '#btEditShipping', '#saveShipping', '#closeShipping',
            '#pklShipping', vo.shippingOps, vo.shippings,
            renderSimpleListStr, null, onClickSaveShipping, null);
    shippingPickList.inti();
    afterReapprove();// 重新送審後刷新頁面(狀態)
}

// 付款方式
function onClickSavePayment(targetList) {
    var formData = {};
    formData['payments'] = isNA(targetList) ? [] : targetList;

    if (confirmReapprove()) {// 重審確認
        var restUrl = PRD_PAY_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSavePayment, null, null);
    }
}

function afterSavePayment(response) {
    vo.payments = utils.getResponseList(response);
    var paymentPickList = new SimplePickList('#payments', '#editPayments', '#btEditPayment', '#savePayment', '#closePayment',
            '#pklPayment', vo.paymentOps, vo.payments,
            renderSimpleListStr, null, onClickSavePayment, null);
    paymentPickList.inti();
    afterReapprove();// 重新送審後刷新頁面(狀態)
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Introduction">
function buildOperationIntro() {
    createBtn('#btEditIntrodution', 'fa-edit', onClickEditIntrodution);// 編輯商品簡介
    createBtn('#addIntro', 'fa-plus-square', onClickAddIntro);// 新增商品簡介
    createBtn('#saveIntro', 'fa-save', onClickSaveIntro);// 儲存商品簡介
    createBtn('#closeIntro', 'fa-ban', onClickCloseIntro);// 關閉/取消商品簡介編輯
}

function renderIntroductions(list) {
    vo.introductions = isNA(list) ? [] : list;
    console.log("renderIntroductions introductions = ", vo.introductions);
    // 商品簡介 顯示區
    renderSimpleTextList('#introductions', vo.introductions);
    $("#introductions").show();// 預設隱藏編輯區
    // 商品簡介 編輯區
    $("#editIntroductions").hide();// 預設隱藏編輯區
}

// 編輯商品簡介
function onClickEditIntrodution(event) {
    renderSimpleTextEditList('#existedIntros', vo.introductions, 'onDeleteIntro', null);
    $("#introductions").hide();
    $("#editIntroductions").show();

    utils.scrollTo("#divProdIntro");
}
// 新增商品簡介
function onClickAddIntro(event) {
    //disableBtn("#addIntro");
    var val = $('#introdution').val();

    vo.introductions.push(val);
    renderSimpleTextEditList('#existedIntros', vo.introductions, 'onDeleteIntro', null);
    setTextValue("#introdution", "");
}
// 儲存商品簡介
function onClickSaveIntro(event) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    var formData = {};
    formData["prdId"] = vo.itemSelected.id;
    formData["introductions"] = vo.introductions;

    if (confirmReapprove()) {// 重審確認
        var restUrl = PRD_INTRO_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(restUrl, formData, false, afterSaveAttrs, null, null);
    }
}
function afterSaveAttrs(response, formData) {
    if (utils.checkResponse(response)) {
        renderIntroductions(utils.getResponseList(response));
        afterReapprove();// 重新送審後刷新頁面(狀態)
        utils.addSuccessMsg();
    }
}

// 取消編輯商品簡介
function onClickCloseIntro(event) {
    renderSimpleTextList('#introductions', vo.introductions, null);
    $('#introductions').show();
    $('#editIntroductions').hide();
}
// 刪除商品簡介
function onDeleteIntro(idx) {
    if (confirm(utils.i18n("remove.item.confirm"))) {
        vo.introductions.splice(idx, 1);
        renderSimpleTextEditList('#existedIntros', vo.introductions, 'onDeleteIntro', null);
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Detail">
// 產生圖文列表
var nowIdx = 0;
function renderRichTextList(selector, values, canEdit) {
    var btnClass = "w3-btn w3-teal w3-padding-small w3-round-large";

    if (utils.isEmptyAry(values)) {
        if( !_adminUser && !_tccDealer ){
            var html = '<button id="btAddPreDetail" type="button" onclick="onClickFirstDetail()" class="' + btnClass + '">新增段落</button>';
            $(selector).html(html);
        }
        return;
    }

    var html = '<table style="width:100%" >';

    for (var i = 0; i < values.length; i++) {
        var type = values[i].contentType;
        var content = utils.safePrint(values[i].contentTxt);
        var filename = utils.safePrint(values[i].filename);
        var url = isEmpty(vo.urlPrefix) ? values[i].url : vo.urlPrefix + values[i].url;
        //var title = utils.safePrint(values[i].title);
        var editMode = isNA(values[i].editMode) ? false : values[i].editMode;

        html += '<tr>';
        if (canEdit && !_adminUser) {
            html += '<td style="width:110px;max-width:140px;" class="w3-border-bottom w3-border-gray">';
            html += '    <button id="btAddPreDetail" type="button" onclick="onClickAddPreDetail(' + i + ')" class="' + btnClass + '">' + utils.i18n("add.up.seg") + '</button><br/>';
            html += '    <button id="btAddPostDetail" type="button" onclick="onClickAddNextDetail(' + i + ')" class="' + btnClass + '" >' + utils.i18n("add.down.seg") + '</button><br/>';
            html += '    <button id="btEditDetail" type="button" onclick="onClickEditDetail(' + i + ')" class="' + btnClass + '" >' + utils.i18n("edit.seg") + '</button><br/>';
            html += '    <button id="btDeleteDetail" type="button" onclick="onClickDeleteDetail(' + i + ')" class="' + btnClass + '" >' + utils.i18n("remove.seg") + '</button><br/>';
            html += '</td>';

            if (editMode) {// 編輯
                html += '<td style="width:64px;" class="w3-border-bottom w3-border-gray">';
                html += '    <label class="ui-widget">' + utils.i18n("seg.content1") + '<br/>' + utils.i18n("seg.content2") + '：</label><br/>';
                html += '    <input type="radio" class="w3-radio" name="detailType' + i + '" id="detailType' + i + '" value="T" ' + (type === "T" ? "checked" : "") + ' onchange="onChangeDetailType(' + i + ')" />';
                html += '    <label class="ui-widget">' + utils.i18n("texts") + '</label><br/>';
                html += '    <input type="radio" class="w3-radio" name="detailType' + i + '" id="detailType' + i + '" value="I" ' + (type === "I" ? "checked" : "") + ' onchange="onChangeDetailType(' + i + ')" />';
                html += '    <label class="ui-widget">' + utils.i18n("pictures") + '</label><br/>';
                html += '</td>';
            } else {
                html += '<td></td>';
            }
        }

        if ("I" === type) {// 圖片
            if (editMode && !_adminUser) {// 編輯
                html += '<td class="w3-border-bottom w3-padding-small w3-border-gray">';
                // <input name="fiLogo" id="fiLogo" style="display:none;" type="file">
                html += '   <div>';
                html += '       <label class="w3-btn w3-teal">';
                html += '           <input name="fiContent' + i + '" id="fiContent' + i + '" style="display:none;" type="file">';
                html += '           <i class="fa fa-photo"></i> ' + utils.i18n("pictures");
                html += '       </label>&nbsp;';
                html += '       <span id="selDetailImg' + i + '" ></span>';
                html += '   </div>';
                //html += '   <input name="fiContent'+i+'" id="fiContent'+i+'" type="file" size="30" class="w3-input w3-border" />';
                html += '   ' + utils.i18n("pic.desc") + '：' + (isEmpty(filename) ? '' : '( ' + filename + ' )');
                html += '   <input name="fiContentTitle' + i + '" id="fiContentTitle' + i + '" value="' + content + '" placeholder="' + utils.i18n("input.pic.desc") + '" type="text" size="30" class="w3-input w3-border" style="margin-bottom:2px;" />';
                html += '   <input type="button" value="' + utils.i18n("save") + '" onclick="onClickSaveDetail(' + i + ')" class="' + btnClass + '" style="margin-bottom:2px;" />';
                html += '   &nbsp;';
                html += '   <input type="button" value="' + utils.i18n("cancel") + '" onclick="onClickCancelDetail(' + i + ')" class="' + btnClass + '" />';
                html += '</td>';
            } else {// 顯示
                html += '<td>';
                html += '    <p style="text-align: center;">&nbsp;<img src="' + url + '" title="' + content + '" /></p>';
                html += '</td>';
            }
        } else {// 文字
            if (editMode && !_adminUser) {// 編輯
                html += '<td class="w3-border-bottom w3-border-gray">';
                html += '   <textarea name="taContent' + i + '" id="taContent' + i + '" rows="5" style="width:98%;margin-bottom:2px;" >' + content + '</textarea>';
                html += '   <input type="button" value="' + utils.i18n("save") + '" onclick="onClickSaveDetail(' + i + ')" class="' + btnClass + '" />';
                html += '   &nbsp;';
                html += '   <input type="button" value="' + utils.i18n("cancel") + '" onclick="onClickCancelDetail(' + i + ')" class="' + btnClass + '" />';
                html += '</td>';
                $("#taContent" + i).puiinputtextarea({autoResize: true});
            } else {// 顯示
                html += '<td style="text-align:center;" >';
                html += '    <div style="text-align:left;padding:5px;" >';
                html += '        <div style="color:black;font-size:medium;">';
                html += utils.asHTML(content);
                html += '        </div>';
                html += '    </div>';
                html += '</td>';
            }
        }
        html += '</tr>';
    }

    html += '</table>';
    $(selector).html(html);
    // Add events
    $("input[type=file]", "#fmDetail").change(prepareUpload);
    //$('#fiContent'+i, '#fmDetail').change(onChangeLogo);
}

function prepareUpload(event) {
    console.log("prepareUpload", event.target.id, idx);
    var idx = event.target.id.substring(event.target.id.length - 1);
    $("#selDetailImg" + idx).html($(event.target)[0].files[0].name);
}

// 新增第一筆段落
function onClickFirstDetail() {
    vo.details = [];
    onClickAddNextDetail(0);
}
// 新增上一段落
function onClickAddPreDetail(idx) {
    var item = {
        "sortnum": idx - 1,
        "contentType": "T",
        "contentTxt": "",
        "editMode": true
    };
    vo.details.splice(idx, 0, item);
    renderRichTextList('#details', vo.details, true);
}
// 新增下一段落
function onClickAddNextDetail(idx) {
    var item = {
        "sortnum": idx + 1,
        "contentType": "T",
        "editMode": true
    };
    vo.details.splice(idx + 1, 0, item);
    renderRichTextList('#details', vo.details, true);
}
// 編輯此段落
function onClickEditDetail(idx) {
    vo.details[idx]["editMode"] = true;
    renderRichTextList('#details', vo.details, true);
}

// 變更段落內容類型
function onChangeDetailType(idx) {
    var contentType = getRadioValue("detailType" + idx, "#fmDetail");//$("input[name=detailType"+idx+"]:checked", "#fmDetail").val();

    vo.details[idx]['contentTypeOri'] = vo.details[idx].contentType;
    vo.details[idx].contentType = contentType;
    renderRichTextList('#details', vo.details, true);
}
// 刪除此段落
function onClickDeleteDetail(idx) {
    if (confirm(utils.i18n("remove.item.confirm"))) {
        if (!isNA(vo.details[idx].id)) {// 已存DB
            var restUrl = PRD_DETAIL_DEL_URL.replace("{prdId}", vo.itemSelected.id);
            utils.postDelete(restUrl, vo.details[idx].id, null, afterDeleteDetail, null);
        }
        vo.details.splice(idx, 1);
    }
}
function afterDeleteDetail(response) {
    if (isNA(response)) {
        console.log("afterDeleteDetail error response = ", response);
        return;
    }
    vo.details = utils.getResponseList(response);
    renderRichTextList('#details', vo.details, true);
}

// 取消編輯此段落
function onClickCancelDetail(idx) {
    vo.details[idx]["editMode"] = false;
    if (!isNA(vo.details[idx]['contentTypeOri'])) {
        vo.details[idx].contentType = vo.details[idx]['contentTypeOri'];
    }
    renderRichTextList('#details', vo.details, true);
}

// 儲存此段落
function onClickSaveDetail(idx) {
    var type = getRadioValue("detailType" + idx, "#fmDetail");// $("input[name=detailType"+idx+"]:checked", "#fmDetail").val();
    console.log("onClickSaveDetail... ", idx, type);

    if (confirmReapprove()) {// 重審確認
        if (type === "T") {
            savePrdDetail(idx);
        } else {
            uploadPrdDetail(idx);
        }
    }
}

// 儲存文字段落
function savePrdDetail(idx) {
    var formData = utils.cloneObj(vo.details[idx]);
    formData['contentType'] = "T";
    formData['sortnum'] = idx + 1;
    formData['contentTxt'] = isNA($("#taContent" + idx).val()) ? "" : $("#taContent" + idx).val();

    if (!validateDetailInput(formData['contentType'], formData['contentTxt'], null)) {
        return;
    }

    var restUrl = PRD_DETAIL_SAVE_URL.replace("{prdId}", vo.itemSelected.id);
    formData["storeId"] = _adminUser ? vo.storeId : null;
    utils.postData(restUrl, formData, false, afterSaveDetail, null, null);
}
// 儲存圖片段落
function uploadPrdDetail(idx) {
    var type = "I";
    var content = isNA($("#fiContentTitle" + idx).val()) ? "" : $("#fiContentTitle" + idx).val();

    var formData = new FormData();
    var files = $("#fiContent" + idx)[0].files[0];
    formData.append("files", $("#fiContent" + idx)[0].files[0]);
    formData.append("filename", $("#fiContent" + idx)[0].files[0].name);
    formData.append("fileContentType", $("#fiContent" + idx)[0].files[0].type);
    if (!isNA(vo.details[idx].id)) {
        formData.append("id", vo.details[idx].id);
    } else {
        formData.append("id", 0);
    }
    if (_adminUser) {
        formData.append("storeId", vo.storeId);
    }
    formData.append("prdId", vo.itemSelected.id);
    formData.append("contentType", "I");
    formData.append("contentTxt", content);
    var sortnum = idx + 1;
    formData.append("sortnum", sortnum);

    if (!validateDetailInput(type, content, files)) {
        return;
    }

    var restUrl = PRD_DETAIL_UPLOAD_URL.replace("{prdId}", vo.itemSelected.id);
    utils.uploadFiles(restUrl, formData, afterSaveDetail, null);
}

function afterSaveDetail(response, formData, optional) {
    if (utils.checkResponse(response)) {
        vo.details = utils.getResponseList(response);
        renderRichTextList('#details', vo.details, true);
        afterReapprove();// 重新送審後刷新頁面(狀態)
        utils.addSuccessMsg();
    }
}

function validateDetailInput(type, content, files) {
    if (type === "T") {
        if (isEmpty(content)) {
            utils.addMessage("error", utils.i18n("in.err"), utils.i18n("seg.content.required"));
            return false;
        } else if (content.length > 600) {
            utils.addMessage("error", utils.i18n("in.err"), utils.i18n("seg.txt.max"));
            return false;
        }
    } else if (type === "I") {
        if (isNA(files)) {
            utils.addMessage("error", utils.i18n("in.err"), utils.i18n("seg.no.pic"));
            return false;
        }
    }
    return true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Picture">
// 建立產品照片區
function buildPrdPicGalleria(ajaxKey) {
    _asyncCompleteFlags[ajaxKey] = false;
    console.log("buildPrdPicGalleria vo.itemSelected = ", vo.itemSelected);
    var restUrl = PRD_PICS_URL.replace("{prdId}", vo.itemSelected.id);
    restUrl = _adminUser ? utils.addUrlQueryParam(restUrl, "storeId", vo.storeId) : restUrl;
    utils.fetchData(restUrl, false, renderPrdPicGalleria, null, ajaxKey);
}
// 產生產品照片區
function renderPrdPicGalleria(response, ajaxKey) {
    vo.pictures = utils.getResponseList(response);

    if (utils.isEmptyAry(vo.pictures)) {// 無圖片
        vo.gallery = null;
        $("#prdPics").hide();
        $("#selectedPic").html(utils.i18n("no.prd.pic"));
        $("#selectedPic").show();
        return;
    }

    var html = "";
    var first = -1;
    for (var i in vo.pictures) {
        if( !isNA(vo.pictures[i].url) ){
            first = first<0?i:first;
            var id = utils.safePrint(vo.pictures[i].id);
            var url = utils.safePrint(vo.urlPrefix + vo.pictures[i].url);
            var desc = utils.safePrint(vo.pictures[i].description);
            html += '<img alt="' + desc + '" src="' + url + '" data-id="' + id + '" data-image="' + url + '" data-description="' + desc + '"/>\n';
        }
    }
    
    if( first<0 ){// 無圖片
        vo.gallery = null;
        $("#prdPics").hide();
        $("#selectedPic").html(utils.i18n("no.prd.pic"));
        $("#selectedPic").show();
        return;
    }
    
    $("#prdPics").html(html);
    $("#prdPics").show();

    vo.gallery = $("#prdPics").unitegallery({
        theme_enable_text_panel: true,
        gallery_height: 400,
        gallery_width: 400,
        gallery_mousewheel_role: "none",
        gallery_play_interval: 60000,
        gallery_autoplay: false
    });
    //vo.gallery.play();

    vo.gallery.on("item_change", onSelectPic);
    onSelectPic(first, {"index": first, "title": utils.safePrint(vo.pictures[first].description)});// default

    _asyncCompleteFlags[ajaxKey] = true;
}
// 選取圖片
function onSelectPic(idx, data) {//on item change, get item number and item data
    var html = "";
    html += "<span class='fieldname'>" + utils.i18n("sel.pic") + "：</span>";

    if( _adminUser || _tccDealer ){
        html += "<span class='fieldvalue'>" + utils.safePrint(data.title) + "</span>&nbsp;";
    } else {
        html += "<input id='picTitle" + idx + "' type='text' value='" + utils.safePrint(data.title) + "' size='30' placeholder='" + utils.i18n("input.pic.desc") + "' />&nbsp;";
        if( utils.isSmallScreen() ){
            html += "<br/>";
        }
        html += "<input id='coverPicId" + idx + "' type='checkbox' /><label>" + utils.i18n("cover.pic.setting") + "</label>&nbsp;";
        html += "<button onclick='onClickSavePic(" + idx + ")' title='" + utils.i18n("save") + "' class='w3-btn w3-circle w3-teal' ><i class='fa fa-save'></i></button>";
        html += "&nbsp;";
        html += "<button onclick='onClickDeletePic(" + idx + ")' title='" + utils.i18n("remove") + "' class='w3-btn w3-circle w3-deep-orange' ><i class='fa fa-trash'></i></button>";
    }

    $("#selectedPic").html(html);
    $("#selectedPic").show();
    $("#picTitle" + idx).puiinputtext();
    createCheckbox("#coverPicId" + idx);
    console.log("coverPicId ... ", data);
    console.log("coverPicId ... ", vo.pictures[idx].id);
    console.log("coverPicId ... ", vo.itemSelected.coverPicId);
    console.log("coverPicId ... ", vo.pictures[idx].id === vo.itemSelected.coverPicId);
    setCheckbox("#coverPicId" + idx, vo.pictures[idx].id === vo.itemSelected.coverPicId);
}
// 儲存商品圖片說明
function onClickSavePic(idx) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }

    if (confirmReapprove()) {// 重審確認
        var formData = {};
        formData["prdId"] = vo.itemSelected.id;
        formData["id"] = vo.pictures[idx].id;
        formData["description"] = $("#picTitle" + idx).val();
        formData["storeId"] = _adminUser ? vo.storeId : null;
        if( getCheckbox("#coverPicId" + idx) ){
            formData["coverPicId"] = vo.pictures[idx].id;
            vo.itemSelected["coverPicId"] = vo.pictures[idx].id;
        }

        utils.postData(PRD_PIC_SAVE_URL.replace("{prdId}", vo.itemSelected.id), formData, false, afterSavePics, null, null);
    }
}
// 刪除商品圖片
function onClickDeletePic(idx) {
    if (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) {
        _alert(utils.i18n("onsel.prd"));
        return;
    }
    if (confirm(utils.i18n("remove.pic.confirm"))) {
        var formData = {};
        formData["prdId"] = vo.itemSelected.id;
        formData["id"] = vo.pictures[idx].id;
        formData["storeId"] = _adminUser ? vo.storeId : null;
        utils.postData(PRD_PIC_DEL_URL.replace("{prdId}", vo.itemSelected.id), formData, false, afterSavePics, null, null);
    }
}

function afterSavePics(response) {
    reloadDataTable('#tbProductLazy', true);// keep status reload 
    renderPrdPicGalleria(response);
    afterReapprove();// 重新送審後刷新頁面(狀態)
    utils.addSuccessMsg();
}
function afterUploadPics(data) {
    console.log("afterSavePics ...", data);
    buildPrdPicGalleria("prdPics");
    utils.addSuccessMsg();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Attributes">
// 初始設定產品規格屬性
function initPrdAttrs(attrs) {
    $("#attrScopeT").click(function () {
        onChangeAttrScopeT();
    });
    $("#attrScopeP").click(function () {
        onChangeAttrScopeP();
    });

    //prepareEditAttr();

    vo.prdAttrFL = new FormListM("prdAttrFL", // this object name
            '#editAttrForm', '#existedAttrs', '#confirmAttr', '#saveAttr', '#cancelAttr', // selector
            attrs, // data list
            PRD_ATTR_URL.replace("{prdId}", vo.itemSelected.id),
            PRD_ATTR_SAVE_URL.replace("{prdId}", vo.itemSelected.id),
            PRD_ATTR_DEL_URL.replace("{prdId}", vo.itemSelected.id), // rest url
            renderAttrList, // call back function
            initAttrFormCB, onEditAttrCB, afterSaveAttrCB, null, // call back function
            true, true,
            getEditAttrFormData, getSaveAttrFormData, // get form input data function
            valiateEditAttr, valiateSaveAttr // validate form function
            );
    vo.prdAttrFL.init();
}

function onChangeAttrScopeT() {
    //enableDropdown("#attrId");
    disableInput("#attrName");
    $("#divAttrId").show();
    $("#attrName").hide();
}

function onChangeAttrScopeP() {
    //disableDropdown("#attrId");// 移除，避免選單未自動隱藏問題
    enableInput("#attrName");
    $("#divAttrId").hide();
    $("#attrName").show();
}

// 產生商品規格屬性維護列表
function renderAttrList(nameFL, selector, values) {
    var html = '<table class="w3-table-all">'
            + '<thead>'
            + '  <tr class="ui-widget-header ui-state-default">';
    if( !_adminUser && !_tccDealer ){
        html += '    <th>' + utils.i18n("operation") + '</th>';
    }
    html += '<th>' + utils.i18n("has.saved") + '</th><th>' + utils.i18n("cname") + '</th><th>' + utils.i18n("content.desc") + '</th>'
         + '  </tr>'
         + '</thead>';
    +'<tbody>';
    if (!isNA(values)) {
        for (var i = 0; i < values.length; i++) {
            html += '<tr class="w3-padding-small w3-hover-pale-blue">';
            if( !_adminUser && !_tccDealer ){
                html += '    <td class="fit-content">'
                        + '     <button type="button" onclick="' + nameFL + '.onClickEdit(' + i + ')" title="' + utils.i18n("edit") + '" class="w3-btn w3-teal" ><i class="fa fa-pencil-square"></i></button>'
                        + '     <button type="button" onclick="' + nameFL + '.onClickDelete(' + i + ')" title="' + utils.i18n("remove") + '" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>'
                        + '     <button type="button" onclick="' + nameFL + '.onClickUp(' + i + ')" title="' + utils.i18n("move.up") + '" class="w3-btn w3-khaki" ><i class="fa fa-arrow-up"></i></button>'
                        + '     <button type="button" onclick="' + nameFL + '.onClickDown(' + i + ')" title="' + utils.i18n("move.down") + '" class="w3-btn w3-green" ><i class="fa fa-arrow-down"></i></button>'
                        + '    </td>';
            }
            html += '    <td class="tac" >' + ((isNA(values[i].id) || values[i].id === 0) ? '<i class="fa fa-times notsaved"></i>' : '<i class="fa fa-check saved" ></i>') + '</td>'
                    + '    <td>' + utils.safePrint(values[i].attrName) + '</td>'
                    + '    <td>' + utils.safePrint(values[i].attrValue) + '</td>'
                    + '  </tr>';
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function initAttrFormCB() {
    $("#attrScopeT").click();// 預設
}

function onEditAttrCB(row) {
    var attrScope = isNA(row.attrScope) ? "T" : row.attrScope;
    setRadioValue("attrScope", "#editAttrForm", attrScope);
    if (attrScope === "T") {// 來自商品選項
        setDropdownValue('#attrId', row.attrId);
        setTextValue("#attrName", "");
        onChangeAttrScopeT();
    } else {
        setDropdownValue('#attrId', 0);
        setTextValue("#attrName", row.attrName);
        onChangeAttrScopeP();
    }

    setTextValue("#attrValue", row.attrValue);
    setTextValue("#sortNum", row.sortNum);
}

function getEditAttrFormData(formData) {
    if (formData.attrScope === "T") {// 來自商品選項
        formData["attrName"] = getDropdownLabel("#attrId");// for list display
    }
    return formData;
}

// 驗證商品規格屬性
function valiateEditAttr(formData) {
    if (isEmpty(formData.attrValue)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("input.err.spec"));
        return false;
    }
    if (!utils.checkUnique(vo.prdAttrFL.list, formData, FIELD_UUID, "attrName", true)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    return true;
}

function getSaveAttrFormData(list) {
    var formData = {};
    formData["prdId"] = (isNA(vo.itemSelected) || isNA(vo.itemSelected.id)) ? null : vo.itemSelected.id;
    formData["attrVals"] = isNA(list) ? [] : list;
    return formData;
}

function valiateSaveAttr(formData) {
    if (isNA(formData["prdId"])) {
        _alert(utils.i18n("onsel.prd"));
        return false;
    }
    if (utils.isEmptyAry(formData["attrVals"])) {
        // do nothiing
        return false;
    }
    if (!confirmReapprove()) {// 重審確認
        return false;
    }
    return true;
}

function afterSaveAttrCB() {
    console.log("afterSaveAttrCB ....");
    afterReapprove();// 重新送審後刷新頁面(狀態)
}
//</editor-fold>
