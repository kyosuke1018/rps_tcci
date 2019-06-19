/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_HOTPRD, w3, _asyncCompleteFlags, AD_COUNT_URL, TEST_DATA, AD_LIST_URL, OPTIONS_URL, utils.genNoSelectOpNum(), AD_SAVE_URL, AD_NOW_URL, AD_SAVE_SORTNUM_URL, onChangeCallBack, AD_SAVE_SORT_URL, utils.genNoSelectOpStr(), PN_CAROUSEL, AD_FULL_URL, AD_DEL_URL, genContentOnline, _adminUser, utils */
var vo = {// variable objects the JS
    itemSelected: null,// 選取
    stores: [],
    products: [],
    img: null,
    // FormListM object
    nowAdFL: null,
    urlPrefix: null,// 前置網址 for 圖片顯示、檔案下載
    // 廣告圖示 (C:首頁輪播展示圖、H:人氣商品、S:人氣商店)
    adType: null
};

vo.adType = (window.location.href.indexOf("/carousel.html")>0)? "C":
            (window.location.href.indexOf("/hotPrd.html")>0)? "H":"S";

$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    w3.includeHTML(start);

    if( isEmpty(vo.adType) ){
        // 未指定廣告圖示類別
        _alert(utils.i18n("ad.msg001"));
    }
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
    
    // Ajax 產出UI相關
    fetchInitOptions("init");
    buildAdListLazy({"type": vo.adType}, "ads");
    buildNowAdList();

    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#selectedItemContent").hide();

    if( vo.adType==='S' ){
        $(".prdOnly").each(function(){
            $(this).hide();
        });
    }

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsAdList').puifieldset({toggleable: true});
    $('#fsDetail').puifieldset({toggleable: true});
    $('#fsNowItems').puifieldset({toggleable: true});
}

//<editor-fold defaultstate="collapsed" desc="for init UI">
// 抓取初始選單選項
function fetchInitOptions(ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=stores&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}

function buildInitObjects(response, ajaxKey){
    if( utils.checkResponse(response) ){
        vo.urlPrefix = response.res.urlPrefix;
        vo.stores = utils.safeList(response.stores);

        buildSearchForm();
        buildBasicForm();
    }
    _asyncCompleteFlags[ajaxKey]=true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Ad Search">
// 初始表單
function buildSearchForm(){
    // 查詢表單
    createDropdown("#store", vo.stores, utils.genNoSelectOpNum(), null);
    setDropdownValue('#store', 0);
    
    var options = [
        {"value":"O", "label":utils.i18n("now.valid")},
        {"value":"A", "label":utils.i18n("valid.item")},
        {"value":"N", "label":utils.i18n("novalid.item")}
    ];
    createDropdown("#status", options, utils.genNoSelectOpStr(), null);
    
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
}

// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["type"] = vo.adType;
    buildAdListLazy(formData, "adListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#selectedItemContent').hide();
}
// 清除
function onClickClearSearch(event){
    setDropdownValue('#store', 0);
    setDropdownValue('#status', "");
    $("#fmSearch")[0].reset();
    
    renderAdListLazy({}, 0, false, null);// clear table
    $("#selectedItemHeader").hide();
    $('#selectedItemContent').hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Ad Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildAdListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = AD_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{"type": vo.adType}:formData;
    utils.postData(restUrl, formData, false, fetchAdListLazy, null, ajaxKey);
}

function fetchAdListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;
    vo.urlPrefix = response.res.urlPrefix;

    renderAdListLazy(formData, totalRows, false, ajaxKey);
}

function renderAdListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 800;
    var marginWidth = 80;
    var columnAll = [
            {field: "id", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "approveTime", headerText: utils.i18n("fd.approveTime"), content: genApproveInfoContent, sortable: true},
            {field: "starttime", headerText: utils.i18n("fd.starttime"), content: genStarttimeContent, sortable: true},
            {field: "endtime", headerText: utils.i18n("fd.endtime"), content: genEndtimeContent, sortable: true},
            {field: "onlined", headerText: utils.i18n("fd.online"), sortable: true, content: genContentOnline},
            {field: "storeName", headerText: utils.i18n("fd.storeName"), sortable: true},
            {field: "prdName", headerText: utils.i18n("fd.prdName"), sortable: true, disabled:(vo.adType==="S")},
            {field: "message", headerText: utils.i18n("market.msg"), sortable: true},
            {field: "filename", headerText: utils.i18n("fd.filename"), sortable: true}
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
        clearDataTable('#tbAdLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbAdLazy', pageSize, totalRows, columns, AD_LIST_URL, 
            !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genApproveInfoContent(data){
    return formatDateTimeStr(data.approveTime);
}
function genApproveTimeContent(data){
    return formatDateTimeStr(data.approveTime);
}
function genStarttimeContent(data){
    return utils.formatDateStr(data.starttime);
}
function genEndtimeContent(data){
    return utils.formatDateStr(data.endtime);
}

// DataTable 選取項目
function onSelectRow(item){
    if( isNA(item.id) ){
        console.log("onSelectRow item = ", item);
        return;
    }
    itemSelected = item;
    resetSelectedItemNavBar();
    // 詳細資料
    var restUrl = AD_FULL_URL.replace("{type}", vo.adType).replace("{id}", item.id);
    utils.fetchData(restUrl, false, renderAdFullInfo, null);
}

// 產生完整資訊畫面
function renderAdFullInfo(data){
    if( isNA(data) || isNA(data.id) ){
        console.error("renderAdFullInfo data = \n", data);
        return;
    }
    itemSelected = data;
    
    // 選取項目
    var title =(vo.adType==='C')?utils.i18n("carousel.item"):((vo.adType==='H')?utils.i18n("hot.prd"):utils.i18n("hot.store"));
    genSelectedItemTitle(title+"["+ data.id+"]");

    renderAdDetail(data);

    $("#selectedItemHeader").show();
    $("#selectedItemContent").show();
    utils.scrollTo('#selectedItemHeader');
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Ad Detail">
// 初始表單
function buildBasicForm(){
    createBtn('#add', 'fa-plus-square', onClickAdd);// 新增
    createBtn('#save', 'fa-save', onClickSave);// 儲存
    createBtn('#cancel', 'fa-ban', onClickReset);// 取消
    createBtn('#delete', 'fa-trash', onClickDelete);// 刪除

    createCheckbox("#approve");
    setCheckbox("#approve", false);
    
    $("#message").puiinputtextarea({autoResize:true});
    //$("#starttime").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //$("#endtime").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    createDatePicker("#starttime");
    createDatePicker("#endtime");
    
    createDropdown("#storeId", vo.stores, utils.genNoSelectOpNum(), onChangeStore);
    setDropdownValue("#storeId", 0);
    if( vo.adType!=='S' ){
        products = [];
        createDropdown("#prdId", products, utils.genNoSelectOpNum(), null);
        setDropdownValue('#prdId', 0);
        disableDropdown('#prdId');
    }

    $('#fiImg', '#editAdForm').change(onChangeImg);

    $('#approveInfo').html("");
    //$("#editAdForm")[0].reset();
}

// 新增
function onClickAdd(){
    $('#editAdForm')[0].reset();
    vo.itemSelected = {};
    var title = (vo.adType==='C')?utils.i18n("add.carousel"):((vo.adType==='H')?utils.i18n("add.hotstore"):utils.i18n("add.hotprd"));
    genSelectedItemTitle(title);
    
    resetSelectedItemNavBar();
    initAdDetail();
    disableBtn('#delete');
    
    $("#selectedItemHeader").show();
    $("#selectedItemContent").show();
    utils.scrollTo('#selectedItemHeader');
}

function initAdDetail(){
    $('#editAdForm')[0].reset();
    createCheckbox("#approve");
    setCheckbox("#approve", false);
    
    createDropdown("#storeId", vo.stores, utils.genNoSelectOpNum(), onChangeStore);
    setDropdownValue("#storeId", 0);
    if( vo.adType!=='S' ){
        vo.products = [];
        createDropdown("#prdId", vo.products, utils.genNoSelectOpNum(), null);
        setDropdownValue('#prdId', 0);
        disableDropdown('#prdId');
    }
    
    $('#approveInfo').html("");
    $('#selImg').html("");
}

// 顯示基本資料
function renderAdDetail(data){
    if( isNA(data) ){
        return;
    }
    enableBtn('#delete');
    
    $('#editAdForm')[0].reset();
    utils.copyPropsToForm(data);
    
    //$("#starttime").val(genStarttimeContent(data));
    //$("#endtime").val(genEndtimeContent(data));
    setDatePicker("#starttime", utils.toDate(data.starttime));
    setDatePicker("#endtime", utils.toDate(data.endtime));
    
    createDropdown("#storeId", vo.stores, utils.genNoSelectOpNum(), onChangeStore);
    setDropdownValue("#storeId", isNA(data.storeId)?0:data.storeId);
    if( vo.adType!=='S' ){
        vo.products = utils.safeList(data.prdList);
        createDropdown("#prdId", vo.products, utils.genNoSelectOpNum(), null);
        setDropdownValue('#prdId', isNA(data.prdId)?0:data.prdId);
        //disableDropdown('#prdId');
    }
    
    setCheckbox("#approve", !isNA(data.approveTime));
    $('#approveInfo').html(genApproveInfoContent(data));
    
    if( isNA(data.savename) ){
        $('#selImg').html("");
    }else{
        var url = utils.safePrint(vo.urlPrefix + data.url);
        createImgLightBox('#selImg', url, url, data.filename, null, utils.isSmallScreen()?'100':'130');
    }
}

// 儲存基本資料
function onClickSave(event){
    var $form = $("#editAdForm");
    var formData = $form.serializeFormJSON();
    formData["type"] = vo.adType;
    if( !isNA(itemSelected) && !isNA(itemSelected.id) ){
        formData["id"] = itemSelected.id;
    }
    formData["approve"] = !isNA(formData["approve"]);
    formData["starttime"] = toISODateTime(formData["starttime"]);
    formData["endtime"] = toISODateTime(formData["endtime"]);
    console.log("onClickSave ", formData);
    
    if( !valiateBasic(formData) ){
        return;
    }
    console.log("onClickSave formData =\n", formData);

    var formDataU = new FormData();
    for(var key in formData){
        formDataU.append(key, formData[key]);
    }
    
    if( !isNA($('#fiImg')[0].files[0]) ){
        formDataU.append("files", $('#fiImg')[0].files[0]);
        formDataU.append("filename", $('#fiImg')[0].files[0].name);
        formDataU.append("fileContentType", $('#fiImg')[0].files[0].type);
    }else if( isNA(itemSelected.id) || itemSelected.id===0 ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("pic.noselect"));
        return false;
    }
    console.log("onClickSave formDataU =\n", formDataU);

    var restUrl = AD_SAVE_URL;
    utils.uploadFiles(restUrl, formDataU, afterSave, null);
}

// 儲存後處理
function afterSave(response){
    itemSelected = response;
    
    reloadDataTable('#tbAdLazy', true);// keep status reload
    renderAdFullInfo(vo.itemSelected);
    buildNowAdList();
    
    utils.addSuccessMsg();
}

// 輸入驗證
function valiateBasic(formData){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editAdForm");
    if( !isEmpty(msg) ){
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    if( !isNA(formData.message) && formData.message.length>150 ){
        msg = utils.i18n("msg.len.over");
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }

    return true;
}

function onClickDelete(){
    if( confirm(utils.i18n("remove.confirm")) ){
        utils.postDelete(AD_DEL_URL, vo.itemSelected.id, vo.adType, afterDelete, null);
    }
}

function afterDelete(response, formData, optional){
    onClickSearch();
    buildNowAdList();
}

// 重設基本資料 Form 
function onClickReset(event){
    if( confirm(utils.i18n("cancel.edit")) ){
        //$("#editAdForm")[0].reset();
        renderAdDetail(vo.itemSelected);
    }
}
// 選取圖片
function onChangeImg(){
    console.log("onChangeImg ...");
    $("#selImg").html($("#fiImg")[0].files[0].name);
}

function changeApprove(event){
    console.log("changeApprove "+getCheckbox("#approve"));
}

function onChangeStore(item){
    console.log("onChangeStore item = ", item);
    if( vo.adType!=='S' ){
        if( item.value > 0 ) {
            var opLang = utils.getOptionLabelLang();
            var restUrl = OPTIONS_URL + "?keys=products&storeId=" + item.value+"&lang="+opLang;
            utils.fetchData(restUrl, false, buildProductOps, null, "products");
        }else{
            vo.products = [];
            createDropdown("#prdId", vo.products, utils.genNoSelectOpNum(), null);
            setDropdownValue('#prdId', 0);
            disableDropdown('#prdId');
        }
    }
}

function buildProductOps(response){
    if( utils.checkResponse(response) ){
        vo.products = utils.safeList(response.products);
        createDropdown("#prdId", vo.products, utils.genNoSelectOpNum(), null);
        setDropdownValue('#prdId', isNA(vo.itemSelected)?0:vo.itemSelected.prdId);
        enableDropdown('#prdId');
    }
}

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Now Ad List">
function buildNowAdList(){
    // AD_NOW_URL
    utils.fetchData(utils.addUrlQueryParam(AD_NOW_URL, "type", vo.adType), false, initNowAdList, null);
}

function initNowAdList(response){
    var listRestUrl = utils.addUrlQueryParam(AD_NOW_URL, "type", vo.adType);
    var saveRestUrl = utils.addUrlQueryParam(AD_SAVE_SORT_URL, "type", vo.adType);
    var list = utils.getResponseList(response);
    vo.urlPrefix = response.res.urlPrefix;
    
    vo.nowAdFL = new FormListM("nowAdFL", // this object name
            null, "#tbNowItems", null, "#saveSortnum", "#cancelSortnum", // selector
            list, // data list
            listRestUrl, saveRestUrl, null, // rest url
            renderNowAdList, // call back function
            null, null, null, null,// call back function
            true, true,
            null, getSaveFormData, // get form input data function
            null, valiateSave // validate form function
    );
    vo.nowAdFL.init();
}

function renderNowAdList(nameFL, selector, values){        
    var html = '<table class="w3-table-all">'
                + '<thead>'
                + '  <tr class="ui-widget-header ui-state-default">'
                + '    <th>'+utils.i18n("operation")+'</th><th>ID</th><th>'+utils.i18n("fd.mk.msg")+'</th><th>'+utils.i18n("fd.mk.pic")+'</th>'
                + '  </tr>'
                + '</thead>';
                + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            var url = utils.safePrint(vo.urlPrefix + values[i].url);
            //var imghtml = '<img src="'+url+'" height="100px;" width="100px;" />\n';

            html += '<tr class="w3-padding-small w3-hover-pale-blue">'
                + '    <td class="fit-content">'
                + '     <button type="button" onclick="'+nameFL+'.onClickUp('+ i +')" title="'+utils.i18n("move.up")+'" class="w3-btn w3-khaki" ><i class="fa fa-arrow-up"></i></button>';
            html += utils.isSmallScreen()?"<br/>":"";
            html += '     <button type="button" onclick="'+nameFL+'.onClickDown('+ i +')" title="'+utils.i18n("move.down")+'" class="w3-btn w3-green" ><i class="fa fa-arrow-down"></i></button>'
                + '    </td>'
                + '    <td>' + values[i].id + '</td>'
                + '    <td>' + utils.safePrint(values[i].message) + '</td>'
                + '    <td><div id="lbImg'+ values[i].id+'" ></div>' + utils.safePrint(values[i].filename)  + '</td>'
                //+ '    <td>' + imghtml + "<br/>" + utils.safePrint(values[i].filename) + '</td>'
                + '  </tr>';
        
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);

    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( !isNA(values[i].url) ){
                var url = utils.safePrint(vo.urlPrefix + values[i].url);
                createImgLightBox('#lbImg'+values[i].id, url, url, utils.safePrint(values[i].filename), null, utils.isSmallScreen()?'100':'130');
            }
        }
    }
}

function getSaveFormData(list){
    var formData = {};
    formData["type"] = vo.adType;
    formData["adList"] = utils.safeList(list);
    return formData;
}

function valiateSave(){
    return confirm(utils.i18n("change.order.confirm"));
}
//</editor-fold>

