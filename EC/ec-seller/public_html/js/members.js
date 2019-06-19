/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global w3, PN_MEMBER, _asyncCompleteFlags, MEM_COUNT_URL, TEST_DATA, MEM_LIST_URL, MEM_FULL_URL, renderMemberFullInfo, MEM_SAVE_URL, MEM_DEL_URL, validator, MEM_CHG_PWD_URL, MEM_CHG_TYPE_URL, OPTIONS_URL, MEM_SELLER_URL, onClickSellerApprove, TCC_ENABLED, MEM_APPLY_TYPE_URL, MEM_PIC_UPLOAD_URL, MEM_PIC_DEL_URL, _adminUser, utils, _tccDealer */
var vo = {// variable objects the JS
    itemSelected: null,// 選取
    urlPrefix: null,// 前置網址 for 圖片顯示、檔案下載
    pic: null
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
    initLayout(_adminUser, true, _adminUser);
    renderPanels();
    fetchInitOptions("init");
    
    buildBasicForm();
    buildUploadForm();
    
    if( _adminUser ){
        buildSearchForm();
        buildMemberListLazy(null, "memberListLazy");// Lazy方式抓取資料
        // 預設顯示、隱藏
        $("#selectedItemHeader").hide();
        $("#selectedItemContent").hide();
    }else{
        fetchMemberFullInfo(0);
    }

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    if( _adminUser ){
        $('#fsSearch').puifieldset({toggleable: true});
        $('#fsSearchResult').puifieldset({toggleable: true});
    }
    
    $('#fsBasic').puifieldset({toggleable: true});
    utils.renderMultiPanel(['plBasic', 'plDetail']);
    
    // 關聯經銷商
    $('#fsTccDealer').puifieldset({toggleable: true});
    $('#fsTccDealer').hide();
    // 關聯下游客戶
    $('#fsTccDs').puifieldset({toggleable: true});
    $('#fsTccDs').hide();
}
// 抓取初始選單選項
function fetchInitOptions(ajaxKey){
    _asyncCompleteFlags[ajaxKey] = false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = OPTIONS_URL + "?keys=industry,salesArea&lang="+opLang;
    utils.fetchData(restUrl, false, buildInitObjects, null, ajaxKey);
}
function buildInitObjects(response, ajaxKey){
    var industryOps = isNA(response.industry)? [] : response.industry; response.industry;
    var areaOps = isNA(response.salesArea)? [] : response.salesArea;
    //createDropdown("#weightUnitBS", weightUnit, utils.genNoSelectOpNum(), null);
    //setDropdownValue('#weightUnitBS', 0);
    //hideDropdown("#weightUnitBS");
    createDropdown("#category", industryOps, utils.genNoSelectOpNum(), null);// 產業別
    createDropdown("#state", areaOps, utils.genNoSelectOpNum(), null);// 所在區域
    
    _asyncCompleteFlags[ajaxKey] = true;
}

//<editor-fold defaultstate="collapsed" desc="for Member Search">
// 初始表單
function buildSearchForm(){
    // 查詢表單
    createBtn('#search', 'fa-search', onClickSearch);// 查詢
    createBtn('#clear', 'fa-ban', onClickClearSearch);// 清除
    createBtn('#delete', 'fa-trash', onClickClearSearch);// 刪除

    createBtn('#add', 'fa-plus-square', onClickAddMember);// 新增
    
    createCheckbox("#identityUnion", null);
    
    createCheckbox("#adminUserS", null);
    createCheckbox("#sellerApplyS", null);
    createCheckbox("#sellerApproveS", null);
    createCheckbox("#tccDealerS", null);
    createCheckbox("#tccDsS", null);
}
// 查詢
function onClickSearch(event){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    
    formData["identityUnion"] = getCheckbox("#identityUnion");
    if( getCheckbox("#adminUserS") ){ formData["adminUser"]=true; }
    if( getCheckbox("#sellerApplyS") ){ formData["sellerApply"]=true; }
    if( getCheckbox("#sellerApproveS") ){ formData["sellerApprove"]=true; }
    if( getCheckbox("#tccDealerS") ){ formData["tccDealer"]=true; }
    if( getCheckbox("#tccDsS") ){ formData["tccDs"]=true; }    
    console.log("onClickSearch formData = ", formData);

    buildMemberListLazy(formData, "memberListLazy");// Lazy方式抓取資料
    
    $("#selectedItemHeader").hide();
    $('#selectedItemContent').hide();
}
// 清除
function onClickClearSearch(event){
    setCheckbox("#adminUserS", false);
    setCheckbox("#sellerApplyS", false);
    setCheckbox("#sellerApproveS", false);
    setCheckbox("#tccDealerS", false);
    setCheckbox("#tccDsS", false);

    $("#fmSearch")[0].reset();

    renderMemberListLazy({}, 0, false, null);// clear table
    $("#selectedItemHeader").hide();
    $('#selectedItemContent').hide();
}
// 新增
function onClickAddMember(){
    $('#editMemberBasicForm')[0].reset();
    vo.itemSelected = {};
    genSelectedItemTitle(utils.i18n("add.member"));
    
    resetSelectedItemNavBar();
    createGoToBtn("#btnGoToBasic", "#fsBasic");
    createGoToBtn("#btnGoToDetail", "#plDetail");
    
    $('#selectedItemHeader').show();
    $('#selectedItemContent').show();
    utils.scrollTo('#selectedItemHeader');
    
    initBasicForm();
    createDatePicker("#birthday");// 預設個人
    
    $("#divMemberTypeSel").show();
    $("#divMemberType").hide();
    $("#spanMemberType").html("");
    $('#delete').hide();
    // 預設個人帳號
    $(".companyOnly").hide();
    $(".personOnly").show();
    // for EC1.5
    // 關聯經銷商
    $('#btnGoToTccDealer').hide();
    $('#fsTccDealer').hide();
    // 關聯下游客戶
    $('#btnGoToTccDs').hide();
    $('#fsTccDs').hide();
}
//</editor-fold>

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
        {field: "memberId", headerText: "ID", sortable: true, bodyClass:"tar"},
        {field: "loginAccount", headerText: utils.i18n("fd.loginAccount"), sortable: true},
        {field: "name", headerText: utils.i18n("fd.mem.name"), sortable: true},
        {field: "email", headerText: utils.i18n("fd.email"), sortable: true},
        {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
        {field: "tccDealer", headerText: utils.i18n("fd.tccDealer"), sortable: true, content: genContentTccDealer, disabled:!TCC_ENABLED},
        {field: "tccDs", headerText: utils.i18n("fd.tccDs"), sortable: true, content: genContentTccDs, disabled:!TCC_ENABLED},
        {field: "sellerApprove", headerText: utils.i18n("fd.sellerApprove"), sortable: true, content: genContentSeller},
        {field: "adminUser", headerText: utils.i18n("fd.adminUser"), sortable: true, content: genContentAdmin},
        {field: "active", headerText: utils.i18n("fd.active"), sortable: true, content: genContentActive},
        {field: "storeCname", headerText: utils.i18n("fd.sefStroe"), sortable: true},
        {field: "totalAmt", headerText: utils.i18n("fd.totalAmt"), sortable: true, bodyClass:"tar", content: genTotalAmtContent},
        {field: "lastBuyDate", headerText: utils.i18n("fd.lastBuyDate"), sortable: true, content: genOrderTimeContent}
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
            !TEST_DATA, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth);
    }
}

function genContentTccDealer(data){
    return genContentBoolean(data, "tccDealer");
}
function genContentTccDs(data){
    return genContentBoolean(data, "tccDs");
}
function genContentSeller(data){
    return genContentBoolean(data, "sellerApprove");
}
function genContentAdmin(data){
    return genContentBoolean(data, "adminUser");
}
// 有效
function genContentActive(data){
    return genContentBoolean(data, "active");
}
// 最近消費日
function genOrderTimeContent(row){
    return formatDateTimeStr(row.lastBuyDate);
}

// 客戶 DataTable 選取項目
function onSelectRow(item){
    if( isNA(item.memberId) ){
        console.log("onSelectRow item = ", item);
        return;
    }
    vo.itemSelected = item;
    resetSelectedItemNavBar();
    createGoToBtn("#btnGoToBasic", "#fsBasic");
    createGoToBtn("#btnGoToDetail", "#plDetail");
    createGoToBtn("#btnGoToTccDealer", "#fsTccDealer");
    createGoToBtn("#btnGoToTccDs", "#fsTccDs");
    $('#delete').show();
    // 詳細資料
    fetchMemberFullInfo(item.memberId);
}

function fetchMemberFullInfo(memberId){
    var restUrl = MEM_FULL_URL.replace("{id}", memberId);
    utils.fetchData(restUrl, false, renderMemberFullInfo, null, "memberFullInfo");
}

// 產生完整資訊畫面
function renderMemberFullInfo(data, ajaxKey){
    if( isNA(data) ){
        console.error("renderMemberFullInfo data = ", data);
        return;
    }
    vo.itemSelected = data;
    
    if( _adminUser ){
        // 選取項目
        genSelectedItemTitle(data.name + " (" + data.loginAccount + ")");
        $('#selectedItemHeader').show();
        $('#selectedItemContent').show();
        utils.scrollTo('#selectedItemHeader');
    }
    
    initBasicForm();
    renderMemberBasic(data);
    
    // for EC1.5
    // 關聯經銷商
    $('#btnGoToTccDealer').hide();
    $('#fsTccDealer').hide();
    if( TCC_ENABLED && !isNA(vo.itemSelected.tccDs) && vo.itemSelected.tccDs ){
        renderTccDealerList(data.memberId);
        $('#btnGoToTccDealer').show();
        $('#fsTccDealer').show();
    }
    // 關聯下游客戶
    $('#btnGoToTccDs').hide();
    $('#fsTccDs').hide();
    if( TCC_ENABLED && !isNA(vo.itemSelected.tccDealer) && vo.itemSelected.tccDealer ){
        renderTccDsList(data.memberId);
        $('#btnGoToTccDs').show();
        $('#fsTccDs').show();
    }
    
    _asyncCompleteFlags[ajaxKey]=true;
}

function genTotalAmtContent(row){
    return utils.printNumber(row.totalAmt);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Member Basic">
// 初始表單
function buildBasicForm(){
    $("#brief").puiinputtextarea({autoResize:true});
    //$("#birthday").datetimepicker({"showTimepicker":false, "showButtonPanel":false});

    createBtn('#btnApplyForCom', null, onClickApplyForCom);// 申請轉為公司戶
    createBtn('#btnToPerson', null, onClickToPerson);// 變更為[個人帳號]
    createBtn('#btnToCompany', null, onClickToCompany);// 變更為[公司/團體帳號]
    
    createBtn('#save', 'fa-save', onClickSave);// 儲存
    createBtn('#cancel', 'fa-ban', onClickReset);// 取消
    if( _adminUser ){
        createBtn('#delete', 'fa-trash', onClickDelete);// 刪除
    }else{
        createBtn('#btnChangePwd', 'fa-key', onClickChangePwd);// 變更密碼
        $('#delete').hide();
        $('#loginAccount').puiinputtext('disable');
    }
}

function onClickApplyForCom(){
    console.log("onClickApplyForCom ...");
    if( confirm(utils.i18n("confirm.apply.for.company")) ){
        var formData = {
            "memberType":"C"
        };
        var restUrl = MEM_APPLY_TYPE_URL;
        utils.postData(restUrl, formData, false, afterChangeMemberType, null);
    }
}
function onClickToPerson(){
    console.log("onClickToPerson ...");
    if( confirm(utils.i18n("confirm.ch.ToPerson")) ){
        var formData = {
            "memberId":vo.itemSelected.memberId,
            "memberType":"P"
        };
        var restUrl = MEM_CHG_TYPE_URL;
        utils.postData(restUrl, formData, false, afterChangeMemberType, null);
    }
}
function onClickToCompany(){
    console.log("onClickToCompany ...");
    if( confirm(utils.i18n("confirm.ch.ToCompany")) ){
        var formData = {
            "memberId":vo.itemSelected.memberId,
            "memberType":"C"
        };
        var restUrl = MEM_CHG_TYPE_URL;
        utils.postData(restUrl, formData, false, afterChangeMemberType, null);
    }
}

function afterChangeMemberType(response){
    afterSave(response);
}

function onClickChangePwd(){
    console.log("onClickChangePwd ...");
    $('#dlgChangePwd').puidialog({
        "visible": true,
        "width": "300",
        //"height": "200",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        buttons: [{
                    text: utils.i18n("close"),
                    icon: 'fa-close',
                    click: function() {
                        $('#dlgChangePwd').puidialog('hide');
                    }
                },{
                    text: utils.i18n("action.confirm"),
                    icon: 'fa-check',
                    click: function() {
                        onClickConfirmChangePwd();
                    }
                }],
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }
    });

    $("#divChangePwd").show();
}

function onClickConfirmChangePwd(){
    console.log("onClickConfirmChangePwd ...");
    var formData = {
        "pwdOri": getTextValue("#pwdOri"),
        "pwdNew": getTextValue("#pwdNew"),
        "pwdNew2": getTextValue("#pwdNew2")
    };
    var restUrl = MEM_CHG_PWD_URL;
    utils.postData(restUrl, formData, false, afterChangePwd, null);
}

function afterChangePwd(response){
    console.log("afterChangePwd response = \n", response);
    $('#dlgChangePwd').puidialog('hide');
    utils.addSuccessMsg();
}

function initBasicForm(){
    $("#editMemberBasicForm")[0].reset();
    setRadioValue("type", "#editMemberBasicForm", "P");
    setRadioValue("gender", "#editMemberBasicForm", "M");
    $('#active').puicheckbox('check');
    $('#sellerApply').puicheckbox('uncheck');
    $('#sellerApprove').puicheckbox('uncheck');
    $('#tccDealer').puicheckbox('uncheck');
    //$('#tccDs').puicheckbox('uncheck');
    $('#approveResult').html("");
    
    if( !_adminUser ){
        disableCheckbox('#active');
        disableCheckbox('#sellerApply');
        disableCheckbox('#sellerApprove');
        disableCheckbox('#tccDealer');
        //disableCheckbox('#tccDs');
        $('#btnSellerApprove').hide();
    }else{
        createBtn('#btnSellerApprove', null, onClickSellerApprove);
    }
}

function renderMemberBasic(data){
    utils.copyPropsToForm(data);
    // 千分位處理
    if( !isNA(data.capital) ){
        $("#capital").val(utils.printNumber(data.capital));
    }
    if( !isNA(data.yearIncome) ){
        $("#yearIncome").val(utils.printNumber(data.yearIncome));
    }
    
    $("#divMemberTypeSel").hide();
    $("#divMemberType").show();
    $("#spanMemberType").html(data.memberTypeName);
    // 申請轉公司戶中
    if( (!isNA(data.comApply) && data.comApply)  && (isNA(data.comApprove) || !data.comApprove) ){
        $("#spanComApplication").show();
    }else{
        $("#spanComApplication").hide();
    }
    var type = isNA(data.memberType)?"P":data.memberType;
    var gender = isNA(data.gender)?"M":data.gender;
    setRadioValue("type", "#editMemberBasicForm", type);
    setRadioValue("gender", "#editMemberBasicForm", gender);
    
    setCheckbox('#active', (!isNA(data.active) && data.active));
    // 賣家資訊
    setCheckbox('#sellerApply', (!isNA(data.sellerApply) && data.sellerApply));
    setCheckbox('#sellerApprove', (!isNA(data.sellerApprove) && data.sellerApprove));
    if( !isNA(data.sellerApprove) && data.sellerApprove ){
        var html = "<br/>("+utils.i18n("fd.seller.no")+"："+data.sellerId+"，"+utils.i18n("fd.st.no")+"："+data.storeId+")";
        $('#approveResult').html(html);
        $('#btnSellerApprove').hide();
    }else{
        $('#btnSellerApprove').show();
    }
    setCheckbox('#tccDealer', (!isNA(data.tccDealer) && data.tccDealer));
    //setCheckbox('#tccDs', (!isNA(data.tccDs) && data.tccDs));
    
    $("#btnPic").html("");
    $("#selPic").html("");
    $("#picImg").html("");
    vo.urlPrefix = data.res.urlPrefix;
    vo.pic = isNA(data.pic)?null:data.pic;
    renderPic();
    
    if( !_tccDealer ){
        if( type==="P" ){// 個人
            createDatePicker("#birthday");
            setDatePicker("#birthday", utils.toDate(data.birthday));
            $("#btnToPerson").hide();
            $("#btnToCompany").show();
            $(".companyOnly").hide();
            $(".personOnly").show();
        }else{// 公司
            createDatePicker("#startAt");
            setDatePicker("#startAt", utils.toDate(data.startAt));
            $("#btnToPerson").show();
            $("#btnToCompany").hide();
            $(".companyOnly").show();
            $(".personOnly").hide();

            $("#btnApplyForCom").hide();
        }
    }
}

function onChangeType(){
    var type = getRadioValue("type", "#editMemberBasicForm");
    console.log("onChangeType type =", type);
    if( type==="P" ){// 個人
        createDatePicker("#birthday");
        $(".companyOnly").hide();
        $(".personOnly").show();
    }else{// 公司
        createDatePicker("#startAt");
        $(".companyOnly").show();
        $(".personOnly").hide();
    }
}

function changeActive(){
    console.log("changeActive ", $('#active').puicheckbox('isChecked'));
}
function changeSellerApply(){
    var apply = getCheckbox('#sellerApply');
    console.log("changeSellerApply ", apply);
}
function changeSellerApprove(){
    var approve = getCheckbox('#sellerApprove');
    console.log("changeSellerApply ", approve);
}
function changeTccDealer(){
    var flag = getCheckbox('#tccDealer');
    console.log("changeTccDealer ", flag);
}
function changeTccDs(){
    var flag = getCheckbox('#tccDc');
    console.log("changeTccDs ", flag);
}

function onClickSellerApprove(){
    //var msg = approve?"確定要核准此會員成為本系統的賣家嗎?"+"\n"+"(經核准後，此會員即可使用賣家商店管理等相關功能。)"
    //            :"確定關閉此會員賣家相關功能的使用權限嗎?";
    var msg = "確定要核准此會員成為本系統的賣家嗎?"+"\n"+"(經核准後，此會員即可使用賣家商店管理等相關功能。)";
    if( confirm(msg) ){
        var formData = {
            "memberId":vo.itemSelected.memberId,
            "sellerApprove":true
        };
        var restUrl = MEM_SELLER_URL;
        utils.postData(restUrl, formData, false, afterChangeSeller, null, "changeSellerApply");
    }
}
function afterChangeSeller(response){
    var sellerApprove = isNA(response.sellerApprove)?false:response.sellerApprove;
    setCheckbox('#sellerApprove', sellerApprove);
    $('#btnSellerApprove').hide();
    
    reloadDataTable('#tbMemberLazy', true);// keep status reload
}

// 儲存基本資料
function onClickSave(event){
    var isNew = isNA(vo.itemSelected.memberId);
    var $form = $("#editMemberBasicForm");
    var formData = $form.serializeFormJSON();
    var memberType = getRadioValue("type", "#editMemberBasicForm");
    var active = getCheckbox('#active');
    var sellerApply = getCheckbox('#sellerApply');
    var sellerApprove = getCheckbox('#sellerApprove');
    var tccDealer = getCheckbox('#tccDealer');
    
    // 千分位處理
    if( !isNA(formData["capital"]) ){
        formData["capital"] = utils.getNumber(formData["capital"]);//.replace(/,/ig, "");
        $("#capital").val(formData["capital"]);
    }
    if( !isNA(formData["yearIncome"]) ){
        formData["yearIncome"] = utils.getNumber(formData["yearIncome"]);//.replace(/,/ig, "");
        $("#yearIncome").val(formData["yearIncome"]);
    }
    
    if( !isNew ){
        formData["memberId"] = vo.itemSelected.memberId;
    }
    formData["memberType"] = memberType;
    formData["sellerApply"] = sellerApply;
    if( _adminUser ){
        formData["active"] = active;
        formData["sellerApply"] = sellerApply;
        formData["sellerApprove"] = sellerApprove;
        formData["tccDealer"] = tccDealer;
    }
    if( memberType==="P" ){
        var gender = getRadioValue("gender", "#editMemberBasicForm");
        formData["gender"] = gender;
        formData["birthday"] = toISODateTime(formData["birthday"]);
    }else{
        formData["startAt"] = toISODateTime(formData["startAt"]);
    }
    console.log("onClickSave ", formData);
    
    if( !valiateBasic(formData) ){
        return;
    }

    var restUrl = MEM_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSave, null, "saveBasic");
}

function afterSave(response){
    itemSelected = response;
    renderMemberBasic(vo.itemSelected);
    if( _adminUser ){
        reloadDataTable('#tbMemberLazy', true);// keep status reload
        utils.scrollTo('#selectedItemHeader');
    }
    utils.addSuccessMsg();
}

function valiateBasic(formData){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#editMemberBasicForm");
    if( !isEmpty(msg) ){
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證

    return true;
}

// 重設基本資料 Form
function onClickReset(event){
    if( confirm(utils.i18n("cancel.edit")) ){
        if( isNA(vo.itemSelected) || isNA(vo.itemSelected.memberId) ){
            onClickAddMember();
        }else{
            onSelectRow(vo.itemSelected);
        }
    }
}

function onClickDelete(){
    if( confirm(utils.i18n("remove.confirm")) ){
        utils.postDelete(MEM_DEL_URL, vo.itemSelected.memberId, null, afterDelete, null);
    }
}

function afterDelete(response, formData, optional){
    onClickSearch();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Member Picture">
function buildUploadForm(){
    $('#fiPic', '#editMemberBasicForm').change(onChangePic);
    createBtn('#btnUploadPic', null, onClickUploadPic);
    createBtn('#btnCancelPic', null, onClickCancelPic);
}

function onClickCancelPic(){
    $("#btnPic").html("");
    $("#selPic").html("");
    $("#picImg").html("");
    disableBtn('#btnUploadPic');
    disableBtn('#btnCancelPic');
    
    renderPic();
}

function uploadPicture(restUrl, fileSelector, ori, callback){
    var formData = new FormData();
    formData.append("files", $(fileSelector)[0].files[0]);
    formData.append("filename", $(fileSelector)[0].files[0].name);
    formData.append("fileContentType", $(fileSelector)[0].files[0].type);
    if( !isNA(ori) && !isNA(ori.id) ){
        formData.append("id", ori.id);
    }
    if( !isNA(vo.itemSelected) && !isNA(vo.itemSelected.memberId) ){
        formData.append("memberId", vo.itemSelected.memberId);
    }

    utils.uploadFiles(restUrl, formData, callback, null);
}

function onChangePic(){
    console.log("onChangePic ...");
    $("#selPic").html($("#fiPic")[0].files[0].name);
    $("#picImg").html("");
    $("#btnPic").html("");
    enableBtn('#btnUploadPic');
    enableBtn('#btnCancelPic');
}

function onClickUploadPic(){
    if( isNA($("#fiPic")[0].files[0]) ){
        _alert(utils.i18n("nosel.upload"));
        return;
    }
    console.log("onClickUploadPic files = ", $("#fiPic")[0].files[0]);
    uploadPicture(MEM_PIC_UPLOAD_URL, "#fiPic", vo.pic, afterUploadPic);
}

function deletePic(){
    if( !isNA(vo.pic) && !isNA(vo.pic.id) ){
        if( confirm(utils.i18n("remove.confirm")) ){
            var formData = {"id": vo.pic.id};
            if( !isNA(vo.itemSelected) && !isNA(vo.itemSelected.memberId) ){
                formData["memberId"] = vo.itemSelected.memberId;
            }
            utils.postData(MEM_PIC_DEL_URL, formData, false, afterDeletePic, null, null);
        }
    }else{
        console.log("deletePic pic = ", vo.pic);
    }
}

function afterDeletePic(){
    vo.pic = null;
    $("#btnPic").html("");
    $("#selPic").html("");
    $("#picImg").html("");
    disableBtn('#btnUploadPic');
    disableBtn('#btnCancelPic');
}

function afterUploadPic(data, formData, optional){
    if( !isNA(data) && !isNA(data.filename) ){
        vo.pic = data;
        renderPic();
    }else{
        vo.pic = null;
    }
}

function renderPic(){
    if( !isNA(vo.pic) && !isNA(vo.pic.url) ){
        $("#selPic").html(vo.pic.filename);
        var html = '&nbsp;<button type="button" onclick="deletePic()" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>';
        $("#btnPic").html(html);
        
        var url = utils.safePrint(vo.urlPrefix + vo.pic.url);
        createImgLightBox('#picImg', url, url, utils.safePrint(vo.pic.filename), null, utils.isSmallScreen()?'100':'130');
    }
    disableBtn('#btnUploadPic');
    disableBtn('#btnCancelPic');
}
//</editor-fold>
