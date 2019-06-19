/* global TEST_DATA, MEM_LIST_URL, MEM_COUNT_URL, _asyncCompleteFlags, utils */

//<editor-fold defaultstate="collapsed" desc="for Tcc Downstream">
function renderTccDsList(dealerId){
    var formData = {};
    formData["dealerId"] = dealerId;
    formData["tccDs"] = true;
    buildTccDsListLazy(formData, "tccDs");
}

function buildTccDsListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    
    // 先取得總筆數 for 分頁
    var restUrl = MEM_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchTccDsListLazy, null, ajaxKey);
}

function fetchTccDsListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderTccDsListLazy(formData, totalRows, false, ajaxKey);
}

function renderTccDsListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 600;
    var marginWidth = 80;
    var columns = [
            {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
            {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
            {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
            {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true}
    ];
        
    renderSearchResultNum("#searchTccDsResultMsg", totalRows);    
    if( totalRows===0 ){
        clearDataTable('#tbTccDsLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbTccDsLazy', pageSize, totalRows, columns, MEM_LIST_URL, 
            !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Tcc Dealer">
function renderTccDealerList(dsId){
    var formData = {};
    formData["dsId"] = dsId;
    formData["tccDealer"] = true;
    buildTccDealerListLazy(formData, "tccDealer");
}

function buildTccDealerListLazy(formData, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    
    // 先取得總筆數 for 分頁
    var restUrl = MEM_COUNT_URL;// 先查總筆數
    formData = isNA(formData)?{}:formData;
    utils.postData(restUrl, formData, false, fetchTccDealerListLazy, null, ajaxKey);
}

function fetchTccDealerListLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderTccDealerListLazy(formData, totalRows, false, ajaxKey);
}

function renderTccDealerListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 600;
    var marginWidth = 80;
    var columns = [
            {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
            {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
            {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
            {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true}
    ];
        
    renderSearchResultNum("#searchTccDealerResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbTccDealerLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbTccDealerLazy', pageSize, totalRows, columns, MEM_LIST_URL, 
            !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}
//</editor-fold>
