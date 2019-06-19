/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global w3, IMPORT_DEALER_UPLOAD_URL, IMPORT_DEALER_UPLOAD_URL, IMPORT_DS_UPLOAD_URL, IMPORT_DS_SAVE_URL, IMPORT_DEALER_SAVE_URL, OPTIONS_URL, _adminUser, utils */
var vo = {// variable objects the JS
    impFile: null,
    members: null,
    errList: null,
    resultMsg: null,
    canImport: null,
    errorCount: null,
    impType: null,
    impDsByAdmin: false
};

vo.impType = (window.location.href.indexOf("/importDeader.html")>0)? "DR":
            (window.location.href.indexOf("/importDownstream.html")>0)? "DS":
            (window.location.href.indexOf("/importMyDownstream.html")>0)? "DS":null;

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
    vo.impDsByAdmin = _adminUser && (window.location.href.indexOf("/importDownstream.html")>0);
    if( vo.impDsByAdmin ){
        fetchInitOptions();
    }else{
        renderTccDsList(0);
    }
    
    renderPanels();
    buildImportForm();
    buildOptionList();// 產業別、區域別

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsImport').puifieldset({toggleable: true});
    $('#fsPreview').puifieldset({toggleable: true});
    $('#fsIndustry').puifieldset({toggleable: true});
    $('#fsArea').puifieldset({toggleable: true});

    // 關聯下游客戶
    console.log("renderPanels ...", $('#fsTccDs'));
    if( $('#fsTccDs') ){
        $('#fsTccDs').puifieldset({toggleable: true});
        $('#fsTccDs').hide();
    }
}

function fetchInitOptions(){
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=dealers,stores&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects);
}

function buildInitObjects(response){
    var dealers = utils.safeList(response.dealers);
    createDropdown("#dealerId", dealers, utils.genNoSelectOpNum(), onChangeDealer);
    setDropdownValue("#dealerId", 0);
}

function onChangeDealer(){
    vo.dealerId = getDropdownValue('#dealerId');
    console.log("dealerId = " , vo.dealerId);

    renderTccDsList(vo.dealerId);
}

function buildImportForm(){
    $('#fiImport', '#fmImport').change(onChangeImport);
    $('#btnUploadImport').click(onClickUploadImport);
    $('#btnUploadImport').prop('disabled', true);

    $('#msgSaveResut').puimessages();
    
    var langPostfix = (utils.getLangCode() === "zh-TW")?"":"_cn";
    //var href = (impType==="DR")?"file/import_tcc_mems"+langPostfix+".xlsx":"file/import_tcc_ds"+langPostfix+".xlsx";
    var href = (vo.impType==="DR")?"file/import_tcc_mems"+langPostfix+".xlsx":"file/import_tcc_ds_M"+langPostfix+".xlsx";
    $('a#linkTemplate').attr("href", href);
}

function buildOptionList(){
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=industry,salesArea&lang="+opLang;
    utils.fetchData(restUrl, false, renderOptionList);
}

function renderOptionList(response){
    var industryList = response.industry;
    var salesAreaList = response.salesArea;
    
    renderSimpleListStr("#divIndustryList", industryList, ",&nbsp;&nbsp;");
    renderSimpleListStr("#divAreaList", salesAreaList, ",&nbsp;&nbsp;");
}

function uploadSingleFile(restUrl, fileSelector, ori, callback){
    var formData = new FormData();
    formData.append("files", $(fileSelector)[0].files[0]);
    formData.append("filename", $(fileSelector)[0].files[0].name);
    formData.append("fileContentType", $(fileSelector)[0].files[0].type);
    if( !isNA(ori) && !isNA(ori.id) ){
        formData.append("id", ori.id);
    }
    if( vo.impDsByAdmin ){
        var dealerId = getDropdownValue('#dealerId');
        console.log("dealerId = ", dealerId);
        //var storeId = getDropdownValue('#storeId');
        if( !isNA(dealerId) && dealerId>0 ){
            formData.append("dealerId", dealerId);
        }
    }    
    console.log("uploadSingleFile formData = \n", formData);
    utils.uploadFiles(restUrl, formData, afterUploadImport, null);
}

function onChangeImport(){
    console.log("onChangeImport ...");
    $('#btnUploadImport').prop('disabled', false);
    renderUploadFileName("#selImport", "#fiImport");
    $("#msgImportResut").html("");
}

function onClickUploadImport(){
    if( isNA($("#fiImport")[0].files[0]) ){
        _alert(utils.i18n("no.sel.upload"));
        return;
    }
    if( vo.impDsByAdmin ){
        var dealerId = getDropdownValue('#dealerId');
        //var storeId = getDropdownValue('#storeId');
        //if( (isNA(dealerId)||dealerId===0) && (isNA(storeId)||storeId===0) ){
        /*if( isNA(dealerId) || dealerId===0 ){
            _alert(utils.i18n("no.sel.dealer"));
            return;
        }*/
    }
    console.log("onClickUploadImport files = ", $("#fiImport")[0].files[0]);
    var restUrl = vo.impType==="DR"?IMPORT_DEALER_UPLOAD_URL:IMPORT_DS_UPLOAD_URL;
    uploadSingleFile(restUrl, "#fiImport", vo.impFile, afterUploadImport);
}

function afterUploadImport(response, formData, optional){
    vo.members = (isNA(response)||isNA(response.resList))?[]:response.resList;
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
        
        var minWidth = 1200;
        var marginWidth = 80;

        // 統編	公司名稱	"*會員帳號(手機)"	
        // *會員名稱 是否為最高權限管理員(是/否) 是否為訂單結案人(是/否)
        // E-mail 電話 公司負責人 資本額(元) 年收入(元) 
        // 產業別	 所在區域 創立時間
        //var columns = [{field: "add", headerText: utils.i18n("operation"), sortable: true, bodyClass: "tac", content: genOpContent}];
        var columns = [];
        if( vo.impType==="DS" ){
            columns.push({field: "dealerCode", headerText: utils.i18n("belong.dealer"), sortable: false});
        }
        
        var columnsM = [
            {field: "idCode", headerText: utils.i18n("fd.idCode"), sortable: false},
            {field: "cname", headerText: utils.i18n("company.name"), sortable: false},
            {field: "loginAccount", headerText: utils.i18n("member.account"), sortable: false},
            {field: "name", headerText: utils.i18n("member.name"), sortable: false},
            {field: "phone", headerText: utils.i18n("tel"), sortable: false},
            {field: "email", headerText: utils.i18n("fd.email"), sortable: false},       
            {field: "owner1", headerText: utils.i18n("incharge"), sortable: false}   
        ];
        columns = columns.concat(columnsM);
        
        if( vo.impType==="DR" ){// 經銷商
            columns.push({field: "storeOwnerYN", headerText: utils.i18n("is.super.user"), sortable: false, bodyClass: "tac"});
            columns.push({field: "finUserYN", headerText: utils.i18n("is.fi.user"), sortable: false, bodyClass: "tac"});
        }
        
        var columnsL = [
            {field: "capital", headerText: utils.i18n("fd.capital"), sortable: false, bodyClass: "tar", content: genCapitalContent},
            {field: "yearIncome", headerText: utils.i18n("fd.year.income"), sortable: false, bodyClass: "tar", content: genYearIncomeContent},
            {field: "categoryName", headerText: utils.i18n("fd.industry"), sortable: false},
            {field: "stateName", headerText: utils.i18n("fd.com.area"), sortable: false},
            {field: "startAt", headerText: utils.i18n("fd.com.startAt"), sortable: false, content: genStartAtContent}
        ];
        
        columns = columns.concat(columnsL);

        renderDataTable("#tbMember", 1200, columns, vo.members, null, minWidth, marginWidth);
        $("#tbMember").show();
        $("#tbErrorMsg").hide();
    }else{// 請修正錯誤再重新上傳!
        $("#msgImportResut").append('&nbsp;&nbsp;<i class="fa fa-exclamation-triangle"></i>'+utils.i18n("fix.upload"));
        var columns = [
            {field: "loginAccount", headerText: utils.i18n("member.account"), sortable: false, bodyStyle:"min-width:80px;"},    
            {field: "name", headerText: utils.i18n("member.name"), sortable: false, bodyStyle:"min-width:80px;"}, 
            {field: "errMsgs", headerText: utils.i18n("fd.errMsgs"), sortable: false}
        ];
        renderDataTable("#tbErrorMsg", 1000, columns, vo.errList, null, null, null);
        $("#tbMember").hide();
        $("#tbErrorMsg").show();
    }
    
    utils.scrollTo("#fsPreview");
}

function onClickSave(){
    var formData = vo.impType==="DR"?{"importTccDealerList": vo.members}:{"importTccDsList": vo.members};
    if( vo.impDsByAdmin ){
        var dealerId = getDropdownValue('#dealerId');
        //var storeId = getDropdownValue('#storeId');
        //if( (isNA(dealerId)||dealerId===0) && (isNA(storeId)||storeId===0) ){
        if( isNA(dealerId) || dealerId===0 ){
            _alert(utils.i18n("no.sel.dealer"));
            return;
        }
        formData["dealerId"] = dealerId;
    }
    
    var restUrl = vo.impType==="DR"?IMPORT_DEALER_SAVE_URL:IMPORT_DS_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveImp, null, null);
}

function afterSaveImp(response){
    // 清除匯入結果
    $("#msgImportResut").html("");
    // 顯示儲存結果
    var msg = utils.i18n("import.success");
    if( !isNA(response) && !isNA(response.res) && !isNA(response.res.msg) ){
        msg = msg + " ("+response.res.msg+")";
    }
    utils.addMessage("info", utils.i18n("process.res"), msg, "#msgSaveResut");

    if( vo.impType==="DS" ){
        // 查出目前綁定下游客戶
        var dealerId = vo.impDsByAdmin?getDropdownValue('#dealerId'):0;
        if( dealerId>0 ){
            renderTccDsList(dealerId);
        }
    }
}

function genOpContent(row){
    return (row.add)?utils.i18n("add"):utils.i18n("modify");
}
function genStartAtContent(row){
    return utils.formatDateStr(row.startAt);
}
function genYearIncomeContent(row){
    return utils.printNumber(row.yearIncome);
}
function genCapitalContent(row){
    return utils.printNumber(row.capital);
}

