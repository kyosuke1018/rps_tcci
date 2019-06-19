/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global w3, PN_MEMBER, _asyncCompleteFlags, MEM_COUNT_URL, TEST_DATA, MEM_LIST_URL, MEM_FULL_URL, renderMemberFullInfo, MEM_SAVE_URL, MEM_DEL_URL, validator, MEM_CHG_PWD_URL, MEM_CHG_TYPE_URL, OPTIONS_URL, MEM_SELLER_URL, onClickSellerApprove, TCC_ENABLED, MEM_APPLY_TYPE_URL, MEM_PIC_UPLOAD_URL, MEM_PIC_DEL_URL, ST_ADD_USER_URL, ST_DEL_USER_URL, ST_SET_ROLE_URL, FIELD_ACCOUNT, _storeOwner, utils */
var vo = {// variable objects the JS
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
    initLayout(false, true, false);
    renderPanels();
    
    buildBasicForm();
    buildMemberListLazy({"storeManager":true}, "memberListLazy");// Lazy方式抓取資料
    
    if( !_storeOwner ){
        hideStoreOwnerOnly();
    }
    
    // 等待非同步工作完成
    console.log(utils.formatDT(new Date()));
}

function renderPanels(){
    $('#fsSearchResult').puifieldset({toggleable: true});
}

function buildBasicForm(){
    createBtn('#add', 'fa-plus-square', onClickAdd);
}

function onClickAdd(){
    var restUrl = ST_ADD_USER_URL;
    var formData = {};
    formData["loginAccount"] = getTextValue("#loginAccount");
    formData["name"] = getTextValue("#name");
    
    if( formData["loginAccount"]==="" ){
        _alert(utils.i18nWP("noinput.fd", utils.i18n("fd.loginAccount")));
        return;
    }
    if( formData["name"]===""){
        _alert(utils.i18nWP("noinput.fd", utils.i18n("fd.mem.name")));
        return;
    }

    utils.postData(restUrl, formData, false, afterAddManager);
}

function afterAddManager(response, formData, ajaxKey){
    setTextValue("#loginAccount", "");
    setTextValue("#name", "");
    buildMemberListLazy({"storeManager":true}, "memberListLazy");// Lazy方式抓取資料
    utils.addSuccessMsg();
}

//<editor-fold defaultstate="collapsed" desc="for Member Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildMemberListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;

    // 先取得總筆數 for 分頁
    var restUrl = MEM_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchMemberListLazy, null, ajaxKey);
}

function fetchMemberListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderMemberListLazy(formData, totalRows, false, ajaxKey);
}

function renderMemberListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 600;
    var marginWidth = 80;
    var columnAll = [
        {field: "managerId", headerText: utils.i18n("operation"), sortable: false, bodyClass:"tac", content: genContentBtnDel, disabled:!_storeOwner},
        {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
        {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
        {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
        {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
        {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
        {field: "managerId", headerText: utils.i18n('super.user'), sortable: false, bodyClass:"tac", content: genContentAdminCB},
        {field: "managerId", headerText: utils.i18n('close.po.user'), sortable: false, bodyClass:"tac", content: genContentFinCB}
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
        clearDataTable('#tbMemberLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbMemberLazy', pageSize, totalRows, columns, MEM_LIST_URL, 
            !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genContentAdminCB(data){
    if( !_storeOwner || data.loginAccount===utils.getSession(FIELD_ACCOUNT) ){
        return genContentBoolean(data, "storeOwner");
    }else if( utils.isTrue(data.storeOwner) ){
        return '<input type="checkbox" checked onclick="setRole(1, '+data.managerId+', '+data.memberId+', false)"  />';
    }else{
        return '<input type="checkbox" onclick="setRole(1, '+data.managerId+', '+data.memberId+', true)"  />';
    }
}

function genContentFinCB(data){
    if( !_storeOwner || data.loginAccount===utils.getSession(FIELD_ACCOUNT) ){
        return genContentBoolean(data, "fiUser");
    }else if( utils.isTrue(data.fiUser) ){
        return '<input type="checkbox" checked onclick="setRole(2, '+data.managerId+', '+data.memberId+', false)"  />';
    }else{
        return '<input type="checkbox" onclick="setRole(2, '+data.managerId+', '+data.memberId+', true)"  />';
    }
}

function genContentBtnDel(data){
    if( true===data.storeOwner ){
        return '';
    }
    var id = data.managerId;
    return '<button id="del'+id+'" type="button" onclick="onClickDelete('+id+')" class="w3-btn w3-brown" >'+ utils.i18n("remove")+'</button>';
}

function setRole(role, managerId, memberId, enable){
    console.log("setRole role="+role+", managerId = "+managerId);
    var roleCode = (role===1)?'O':(role===2)?'F':null;
    if( isNA(roleCode) ){
        // 無設定
        _alert(utils.i18n("msg.noset"));
        return;
    }
    var restUrl = ST_SET_ROLE_URL;
    var formData = {"id":managerId, "memberId":memberId, "roleCode":roleCode, "enable":enable};

    utils.postData(restUrl, formData, false, afterSave);
}

function onClickDelete(managerId){
    var restUrl = ST_DEL_USER_URL;
    var formData = {"id":managerId};

    utils.postData(restUrl, formData, false, afterSave);
}

function afterSave(response, formData, ajaxKey){
    buildMemberListLazy({"storeManager":true}, "memberListLazy");// Lazy方式抓取資料
    utils.addSuccessMsg();
}

// 客戶 DataTable 選取項目
//function onSelectRow(item){
//    console.log("onSelectRow item = ", item);
//}
//</editor-fold>
