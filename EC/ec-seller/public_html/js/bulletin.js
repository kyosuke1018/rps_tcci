/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_BULLETIN, w3, genContentDisabled, genCreateTimeContent, BULLETIN_LIST_URL, TEST_DATA, BULLETIN_SAVE_URL, BULLETIN_COUNT_URL, genContentOnline, _adminUser, utils */
var vo = {// variable objects the JS
    itemSelected: null// 選取
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
    initLayout(_adminUser, true);
    renderPanels();

    buildSearchForm();
    buildBasicForm();
    // Ajax 產出UI相關
    buildBulletinListLazy({"active":true}, "bulletin");
    
    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#selectedItemContent").hide();

    console.log(utils.formatDT(new Date()));
}

function renderPanels(){
    $('#fsSearch').puifieldset({toggleable: true});
    $('#fsBulletinList').puifieldset({toggleable: true});
    $('#fsDetail').puifieldset({toggleable: true});
}

//<editor-fold defaultstate="collapsed" desc="for Bulletin Search">
// 初始表單
function buildSearchForm(){
    // 查詢表單
    createCheckbox("#cbActive" , onClickSearch);
}

// 查詢
function onClickSearch(event){
    //var $form = $("#fmSearch");
    var formData = { "active":getCheckbox("#cbActive") }; //$form.serializeFormJSON();
    buildBulletinListLazy(formData, "bulletinListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#selectedItemContent').hide();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Bulletin Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildBulletinListLazy(formData, ajaxKey){
    // 先取得總筆數 for 分頁
    var restUrl = BULLETIN_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchBulletinListLazy, null, ajaxKey);
}

function fetchBulletinListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;
    renderBulletinListLazy(formData, totalRows, false, ajaxKey);
}

function renderBulletinListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 800;
    var marginWidth = 80;
    var columns = [
            {field: "id", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "disabled", headerText: utils.i18n("fd.disabled"), content: genContentDisabled, sortable: true},
            {field: "starttime", headerText: utils.i18n("fd.starttime"), content: genStarttimeContent, sortable: true},
            {field: "endtime", headerText: utils.i18n("fd.endtime"), content: genEndtimeContent, sortable: true},
            {field: "onlined", headerText: utils.i18n("fd.online"), sortable: true, content: genContentOnline},
            {field: "content", headerText: utils.i18n("fd.content"), sortable: false, content: genMsgContent},
            {field: "creatorLabel", headerText: utils.i18n("fd.creatorLabel"), sortable: false},
            {field: "createtime", headerText: utils.i18n("fd.createtime"), content: genCreateTimeContent}
        ];
        
    renderSearchResultNum("#searchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbBulletinLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbBulletinLazy', pageSize, totalRows, columns, BULLETIN_LIST_URL, 
            !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genStarttimeContent(data){
    return utils.formatDateStr(data.starttime);
}
function genEndtimeContent(data){
    return utils.formatDateStr(data.endtime);
}
function genMsgContent(data){
    return "<div class='longtxt' >"+data.content+"</div>";
}

// DataTable 選取項目
function onSelectRow(item){
    if( isNA(item.id) ){
        console.log("onSelectRow item = ", item);
        return;
    }
    vo.itemSelected = item;
    resetSelectedItemNavBar();
    // 詳細資料
    renderBulletinFullInfo(vo.itemSelected);
}

// 產生供應商完整資訊畫面
function renderBulletinFullInfo(data){
    if( isNA(data) || isNA(data.id) ){
        console.error("renderBulletinFullInfo data = ", data);
        return;
    }
    
    // 選取項目
    genSelectedItemTitle(utils.i18n("fd.bulletin")+"[" + data.id + "]");

    renderBulletinDetail(data);

    $("#selectedItemHeader").show();
    $("#selectedItemContent").show();
    utils.scrollTo('#selectedItemHeader');
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Bulletin Detail">
// 初始表單
function buildBasicForm(){
    createBtn('#add', 'fa-plus-square', onClickAdd);// 新增
    createBtn('#save', 'fa-save', onClickSave);// 儲存
    createBtn('#cancel', 'fa-ban', onClickReset);// 取消

    createCheckbox("#active");
    $("#content").puiinputtextarea({autoResize:true});
    //$("#starttime").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    //$("#endtime").datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    createDatePicker("#starttime");
    createDatePicker("#endtime");

    $("#editBulletinForm")[0].reset();
}

// 新增
function onClickAdd(){
    $('#editBulletinForm')[0].reset();
    vo.itemSelected = {};
    genSelectedItemTitle(utils.i18n("add.bulletin"));
    
    resetSelectedItemNavBar();
    
    $("#selectedItemHeader").show();
    $("#selectedItemContent").show();
    
    setCheckbox("#active", true);
    
    utils.scrollTo('#selectedItemHeader');
}

// 顯示基本資料
function renderBulletinDetail(data){
    //console.log(data);
    if( isNA(data) ){
        return;
    }
    $('#editBulletinForm')[0].reset();
    utils.copyPropsToForm(data);
    
    //$("#starttime").val(genStarttimeContent(data));
    //$("#endtime").val(genEndtimeContent(data));
    setDatePicker("#starttime", utils.toDate(data.starttime));
    setDatePicker("#endtime", utils.toDate(data.endtime));
    
    var active = !isNA(data.disabled) && !data.disabled;
    setCheckbox("#active", active);
}

// 儲存基本資料
function onClickSave(event){
    var $form = $("#editBulletinForm");
    var formData = $form.serializeFormJSON();
    if( !isNA(vo.itemSelected) && !isNA(vo.itemSelected.id) ){
        formData["id"] = vo.itemSelected.id;
    }
    formData["active"] = getCheckbox("#active");
    formData["starttime"] = toISODateTime(formData["starttime"]);
    formData["endtime"] = toISODateTime(formData["endtime"]);
    console.log("onClickSave ", formData);
    
    if( !valiateBasic(formData) ){
        return;
    }
    console.log("onClickSave formData =\n", formData);

    var restUrl = BULLETIN_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave, null, null);
}

// 儲存後處理
function afterSave(response){
    vo.itemSelected = response;
    
    reloadDataTable('#tbBulletinLazy', true);// keep status reload
    renderBulletinFullInfo(vo.itemSelected);
    
    utils.addSuccessMsg();
}

// 輸入驗證
function valiateBasic(formData){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editBulletinForm");
    if( !isEmpty(msg) ){
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    if( !isNA(formData.message) && formData.message.length>500 ){
        msg = utils.i18n("bulletin.content.max");
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }

    return true;
}

// 重設基本資料 Form
function onClickReset(event){
    if( confirm(utils.i18n("cancel.edit")) ){
        $("#editBulletinForm")[0].reset();
        vo.itemSelected = null;
        // 預設顯示、隱藏
        $("#selectedItemHeader").hide();
        $("#selectedItemContent").hide();
    }
}
//</editor-fold>
