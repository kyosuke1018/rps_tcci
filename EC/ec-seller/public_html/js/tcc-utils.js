// dependencys : jquery, primeUI, underscore
/* global _asyncCompleteFlags, LOG_LEVEL, _maxWaittingTimes, WAITTING_URL, MSG_NOT_DATA, MSG_INPUT_ERR, MSG_IN_KEYWORD, CHECK_LOGIN_URL, HOME_PAGE, SELLER_HOME_PAGE, LOGIN_PAGE, NO_SELECT_OP, _timerMap, _, FIELD_UUID, FIELD_MODIFIED, TEST_DATA, FIELD_TOKEN, validator, MSG_EXEC_ERR, MSG_SYS_ERR, FIELD_ADMIN, ADMIN_HOME_PAGE, ADMIN_LOGIN_PAGE, FIELD_ACCOUNT, _needCheckLogin, FIELD_LANG, ADMIN_PAGE, ADMIN_PAGES, URL_NOW, I18N_PROPS_NAME, I18N_PATH, LANGS, FIELD_FROM, SELLER_PAGES, SWITCH_STORE_URL, FIELD_STOREID, FIELD_TABID, DEF_LANG, FIELD_STATE, FIELD_DEALER, DEALER_LOGIN_PAGE, FIELD_ST_OWNER, FIELD_ST_FIUSER, CHECK_IT_LOGIN_URL, _renderThisPage */
//<editor-fold defaultstate="collapsed" desc="for global init">
$(function(){
    initApp();
});

function initApp(){
    console.log("initApp ...");
    _tabId = getTabId();// for open new tab
    console.log("initApp _tabId = "+_tabId);
    getLanguage(getSession(FIELD_LANG));// get default language
    console.log("initApp _language = ", _language);
    
    // ajax loading screen
    $body = $("body");
    $(document).on({
        ajaxStart: function() { $body.addClass("loading"); },
        ajaxStop: function() {
            $body.removeClass("loading"); 
            if( checkAsyncComplete(_asyncCompleteFlags) ){
                hideOverlay("startOverlay", "H");
            } 
        }
    });
    
    // ajax add JWT header
    $.ajaxPrefilter(function(options) {
        var token = getSession(FIELD_TOKEN);
        var language = getSession(FIELD_LANG);
        options.beforeSend = function (xhr) {
            //xhr.setRequestHeader("Content-Type", "application/json"); // 勿在此設定，會造成上傳檔案錯誤
            if( !isNA(token) ){
                xhr.setRequestHeader('Authorization', 'Bearer '+token);
            }
            if( !isNA(language) ){
                xhr.setRequestHeader('Custom-Language', language);
            }
        };
    });
    
    // serialize form data
    $.fn.serializeFormJSON = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if( !isEmpty(this.value) ){
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            }
        });
        return o;
    };
}

function getSysName(forAdmin, forDealer){
    return (forAdmin?i18n("sys.name"):(forDealer?i18n("dealer.sys.name"):i18n("seller.sys.name")));
}

function getLoginPage(forAdmin, forDealer){
    return (forAdmin?ADMIN_LOGIN_PAGE:(forDealer?DEALER_LOGIN_PAGE:LOGIN_PAGE));
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Ajax">
// common ajax process 
function fetchData(restUrl, retry, successCallback, failCallback, optional){
    if( _notLoginAlert ){// 未登入警示中，不再執行任何 AJAX
        return;
    }
    $.ajax({
        type: "GET",
        url: restUrl,
        dataType: "json",
        context: this,
        success: function(response) {
            console.log("fetchData success ", restUrl, response);
            getRestCallBack(response, successCallback, failCallback, optional);
        },
        error: function (response) {
            console.log("fetchData error ", restUrl, response);
            if( _doMobileLogin ){// 手機登入驗證中忽略錯誤訊息
                return;
            }
            if( !retry && response!==undefined && response.status===0 ){
                fetchData(restUrl, !retry, successCallback, failCallback, optional);// status=0時，重試一次
            }else{
                restErrorHandler(response, failCallback, optional);
            }
        }
    });
}

function getRestCallBack(response, successCallback, failCallback, optional){
    if( checkResponse(response) ){
        if( !isNA(successCallback) ){
            successCallback(response, optional);
        }
    }else{
        if( !isNA(failCallback) ){
            failCallback(response, optional);
        }
    }
}

function postRestCallBack(response, successCallback, failCallback, formData, optional){
    if( checkResponse(response) ){
        if( !isNA(successCallback) ){
            successCallback(response, formData, optional);
        }
    }else{
        if( !isNA(failCallback) ){
            failCallback(response, formData, optional);
        }
    }
}

function restErrorHandler(response, failCallback, optional){
    if( !isNA(failCallback) ){
        failCallback(response, optional);
    }else{
        if( !isNA(response) && response.status!==401 ){
            var msg = i18n("txt.MSG_SYS_ERR") + ((response!==undefined)?" (" 
                    + i18n("status.code") + "="+response.status+")":"");
            //hideLoading();// 隱藏 loading image
            addMessage("error", i18n("txt.MSG_EXEC_ERR"), msg);
        }else{
            var loginPage = getLoginPage(inPages(URL_NOW, ADMIN_PAGES), isDealer());
            afterLoginFail(response, loginPage);// 未授權
        }
    }
}

function postDelete(restUrl, id, type, callback, optional){
    if( _notLoginAlert ){// 未登入警示中，不再執行任何 AJAX
        return;
    }
    var formData = {};
    formData['id'] = id;
    if( !isNA(type) ){
        formData['type'] = type;
    }
    postData(restUrl, formData, false, callback, null, optional);
}

function postData(restUrl, formData, retry, successCallback, failCallback, optional){
    if( _notLoginAlert ){// 未登入警示中，不再執行任何 AJAX
        return;
    }
    formData = isNA(formData)?{}:formData;
    var submitData = JSON.stringify(formData);
    if( TEST_DATA ){
        return fetchData(restUrl, retry, successCallback, failCallback, optional);
    }
    // 先查查詢結果總筆數
    $.ajax({
        type: "POST",
        url: restUrl,
        data: submitData,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        //xhrFields: {
        //    withCredentials: true
        //},
        success: function (response, status, jqXHR) {
            console.log("postData success "+restUrl+"\n", formData, response);
            postRestCallBack(response, successCallback, failCallback, formData, optional);
        },
        error: function (response) {
            console.log("postData error "+restUrl+"\n", formData, response);
            if( _doMobileLogin ){// 手機登入驗證中忽略錯誤訊息
                return;
            }
            if( !retry && response!==undefined ){
                postData(restUrl, formData, true, successCallback, failCallback, optional);// status=0時，重試一次
            }else{
                restErrorHandler(response, failCallback, optional);
            }
        },
        complete: function(jqXHR, textStatus){
            //console.log("postData complete restUrl = "+restUrl);
        }
    });
}

function uploadFiles(restUrl, formData, callback, optional){
    if( _notLoginAlert ){// 未登入警示中，不再執行任何 AJAX
        return;
    }
    if( !window.FormData ){
        alert("Your browser not support ajax file upload!");
        return;
    }
    $.ajax({
        url :  restUrl,
        data : formData, // ? formdata : form.serialize(),
        cache : false,
        contentType : false,
        processData : false,
        type : 'POST',
        success : function(response, textStatus, jqXHR){
            console.log("uploadFiles success ...\n", response);
            callback(response, formData, optional);
        },
        error: function (response) {
            console.log("uploadFiles fail ...\n", response);
            var statusCode = isNA(response.status)? -1:response.status;
            addMessage("error", i18n("errors"), i18nWP("upload.err.status", statusCode));
        }
    });    
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for restful result">
function isSuccess(response){
    if( !isNA(response) && !isNA(response.res) 
            && !isNA(response.res.status) && response.res.status===1 ){
        return true;
    }
    return false;
}

// check ajax response
function checkResponse(response){
    if( _needCheckLogin ){
        if( isNA(getSession(FIELD_TOKEN)) ){
            addMessage("warn", i18n("prompts"), i18n("nologin"));
            return false;
        }
    }
    if( isSuccess(response) ){
        return true;
    }else{
        console.log("checkResponse fail response = \n", response);
        if( !isNA(response) && !isNA(response.res) ){ 
            if( !isEmptyAry(response.res.errors) ){// 詳細錯誤訊息
                var msg = "";
                for(var i=0; i<response.res.errors.length; i++){
                    msg = msg + (msg==="")?response.res.errors[i]:"<br/>" + response.res.errors[i];
                }
                addMessage("error", i18n("errors"), msg);
            }else if( !isNA(response.res.msg) ){// 簡易錯誤訊息
                addMessage("error", i18n("errors"), response.res.msg);
            }
        }else{
            alert(i18n("txt.MSG_NOT_DATA"));
        }
    }
    return false;
}

function getResponseList(response){
    var list = (isNA(response) || isNA(response.list))? []:response.list;
    return list;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="depend primeUI">
// severity : info、warn、error
function addMessage(severityLevel, summaryIn, detailIn, selector) {
    selector = isNA(selector)?'#_message':selector;// default

    var msg = {"summary": summaryIn, "detail": detailIn};
    $(selector).puimessages('show', severityLevel, msg);

    var growl = [{severity: severityLevel, summary: summaryIn, detail: detailIn}];
    $('#_growl').puigrowl('show', growl);
    
    delayRun(closeMessage, selector, 6000);
}

function closeMessage(selector){
    selector = isNA(selector)?'#message':selector;// default
    $(selector).puimessages('clear');
}

function addSuccessMsg(){
    addMessage("info", i18n("exec.res"), i18n("exec.finish"));
}

function renderMultiPanel(ids){
    if( isEmptyAry(ids) ){
        return;
    }
    for(var i=0; i<ids.length; i++){
        $('#'+ids[i]).puipanel();
    }
}

function autoResizeDataTable(selector, minWidth, marginWidth, delimitWidth){
    $(window).resize(function(){
        //console.log("autoResizeDataTable", $(window).innerWidth(), delimitWidth, marginWidth);
        if( $(window).innerWidth()>delimitWidth ){
            var w = $(window).innerWidth() - (($(window).innerWidth()>900)?marginWidth:marginWidth*0.6);
            $(selector+" > .ui-datatable-tablewrapper table").css("min-width", minWidth+"px");
            $(selector+" > .ui-datatable-tablewrapper").css("max-width", w);
            $(selector+" > .ui-datatable-tablewrapper").css("overflow-x", "auto");
        }else{
            //$(selector+" > .ui-datatable-tablewrapper table").css("width", "100%");
            $(selector+" > .ui-datatable-tablewrapper table").css("min-width", "100%");
            $(selector+" > .ui-datatable-tablewrapper").css("max-width", "100%");
            $(selector+" > .ui-datatable-tablewrapper").css("overflow-x", '');
        }
    });
    $(window).resize();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for i18N">
function loadI18nProperties(i18nPath) {
    var langCode = getSession(FIELD_LANG);
    langCode = isNA(langCode)?DEF_LANG:langCode;
    var propsPath = isNA(i18nPath)? I18N_PATH:i18nPath;
    
    $.i18n.properties({
        name : I18N_PROPS_NAME,
        path : propsPath,
        mode : 'map',
        language : langCode,
        async: false,
        callback : function() {
            // tag text
            $('legend, label, button, span, a, h2').each(function() {
                //console.log("legend ...", $(this).text());
                if( $(this).text() ){
                    var key = $(this).text().trim().replace("：", "").replace(":", "");
                    var html = $(this).html().trim().replace(key, $.i18n.prop(key));
                    $(this).html(html);
                }
            });
            // title
            $('a, input, textarea, div, .il8n-title').each(function() {
                //console.log("title ...", $(this).attr("title"));
                if( $(this).attr("title") ){
                    var key = $(this).attr("title").trim().replace("：", "").replace(":", "");
                    var value = $(this).attr("title").trim().replace(key, $.i18n.prop(key));
                    $(this).attr("title", value);
                }
            });
            // placeholder
            $('input').each(function() {
                if( $(this).attr("placeholder") ){
                    var key = $(this).attr("placeholder").trim().replace("：", "").replace(":", "");
                    var value = $(this).attr("placeholder").trim().replace(key, $.i18n.prop(key));
                    $(this).attr("placeholder", value);
                }
            });
        }
    });
}

function i18n(key){
    return $.i18n.prop(key);
}
function i18nWP(key){
    if( isNA(arguments[1]) ){
        return i18n(key);
    }else if( arguments.length<2 ){
        return $.i18n.prop(key, arguments[1]);
    }else if( arguments.length<3 ){
        return $.i18n.prop(key, arguments[1], arguments[2]);
    }else{
        return $.i18n.prop(key, arguments[1], arguments[2], arguments[3]);
    }
}

function getLanguage(langCode){
    console.log("getLanguage ...\n", navigator);
    langCode = isNA(langCode)?navigator.language:langCode;
    langCode = isNA(langCode)?DEF_LANG:langCode;
    
    for(var i=0; i<LANGS.length; i++){
        if( LANGS[i].code === langCode ){
            _language = LANGS[i];
            setSession(FIELD_LANG, _language.code);
            //console.log("_language = ", _language);
            return _language.value;
        }
    }
    console.assert("getLanguage error!");
    return 0;
}

function getLangCode(){
    var langCode = getSession(FIELD_LANG);
    langCode = isNA(langCode)?DEF_LANG:langCode;
    return langCode;
}

function getOptionLabelLang(){
    var langCode = getLangCode();
    if( langCode==="en-US" || langCode==="tr-TR" ){
        return "E";
    }
    return "C";
}

function getLanguageOps(forDealer){
    if( !forDealer ){
        return LANGS;
    }
    var ops = [];
    for(var i=0; i<LANGS.length; i++){
        if( LANGS[i].ec15 ){
            ops.push(LANGS[i]);
        }
    }
    return ops;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Login & Switch Store">
function onSwitchStore(item){
    console.log("onSwitchStore ...", item, getSession(FIELD_STOREID));
    if( item.value.toString()===getSession(FIELD_STOREID) ){
        return;
    }
    var storeId = item.value;
    if( isNA(storeId) || storeId===0 ){
        alert(i18n("txt.NO_STORE"));
        return;
    }
    var formData = {"storeId": storeId};
    var restUrl = SWITCH_STORE_URL;
    postData(restUrl, formData, false, afterSwitchStore);
}
function afterSwitchStore(response){
    if( !isNA(response) && !isNA(response[FIELD_TOKEN]) && !isNA(response.login) && response.login ){
        console.log("afterSwitchStore response = \n"+response);
        console.log("afterSwitchStore storeId = \n"+response[FIELD_STOREID]);
        keepAuthResultToSession(response);// 儲存授權結果

        var url = window.location.href;// 需惕除 tabId 等參數
        var i = url.indexOf("?");
        
        if( i>0 ){
            url = url.substring(0, i);
            window.location.href = url;
        }else{
            window.location.reload();
        }
    }
}

// ex. http://localhost:8383/ec-seller/login.html?from=android&token=thisisatesttokenfrommobileapp&loginAccount=test01&state
function checkLoginFromApp(){
    var loginAccount = getUrlParameter(FIELD_ACCOUNT);
    var storeId = getUrlParameter(FIELD_STOREID);
    var from = getUrlParameter(FIELD_FROM);
    var token = getUrlParameter(FIELD_TOKEN);
    var language = getUrlParameter(FIELD_LANG);
    var tccDealer = getUrlParameter(FIELD_DEALER);
    var storeOwner = getUrlParameter(FIELD_ST_OWNER);
    var fiUser = getUrlParameter(FIELD_ST_FIUSER);
    var state = getUrlParameter(FIELD_STATE);

    if( isNA(from) || isNA(loginAccount) || isNA(token) ){
        _doMobileLogin = false;
        return;
    }
    _doMobileLogin = true;
    console.log("checkLoginFromApp _doMobileLogin = "+_doMobileLogin);
    console.log("checkLoginFromApp token = "+token);
    var authRes = {};
    authRes[FIELD_ACCOUNT] = loginAccount;
    authRes[FIELD_TOKEN] = token;
    authRes[FIELD_ADMIN] = false;
    authRes[FIELD_DEALER] = (tccDealer==="true");
    authRes[FIELD_ST_OWNER] = (storeOwner==="true");
    authRes[FIELD_ST_FIUSER] = (fiUser==="true");
    authRes[FIELD_STOREID] = storeId;
    
    keepAuthResultToSession(authRes);// 儲存授權結果
    setSession(FIELD_FROM, from);
    setSession(FIELD_LANG, language);
    keepState(state);

    console.log("checkLoginFromApp Session FIELD_ACCOUNT = "+getSession(FIELD_ACCOUNT));
    var restUrl = CHECK_LOGIN_URL+"?code="+window.location.pathname;
    fetchData(restUrl, false, afterCheckLogin, afterLoginFail, LOGIN_PAGE);
}

// open new tab in browser
function checkIsNewTab(){
    console.log("checkIsNewTab ...");
    var tabId = getUrlParameter(FIELD_TABID);
    var account = getUrlParameter(FIELD_ACCOUNT);
    var storeId = getUrlParameter(FIELD_STOREID);
    var from = getUrlParameter(FIELD_FROM);
    var langCode = getUrlParameter(FIELD_LANG);
    var storeOwner = getUrlParameter(FIELD_ST_OWNER);
    var fiUser = getUrlParameter(FIELD_ST_FIUSER);
    
    if( !isEmpty(tabId) && !isEmpty(account) ){
        var token = getCookie(tabId);
        if( !isEmpty(token) ){
            console.log("open new tab ...");
            var authRes = {};
            authRes[FIELD_ACCOUNT] = account;// for display
            authRes[FIELD_TOKEN] = token;// for check login
            authRes[FIELD_ADMIN] = tabId.startWith("admin.");
            authRes[FIELD_DEALER] = tabId.startWith("dealer.");
            authRes[FIELD_ST_OWNER] = (storeOwner==="true");;
            authRes[FIELD_ST_FIUSER] = (fiUser==="true");;
            authRes[FIELD_STOREID] = isNA(storeId)?null:storeId;
            
            keepAuthResultToSession(authRes);// 儲存授權結果
            setSession(FIELD_FROM, from);
            setSession(FIELD_LANG, langCode);
            return token;
        }
    }
    return null;
}

function checkLogin(){
    checkLoginFromApp();// 手機登入
    if( _doMobileLogin ){
        return;
    }
    
    var token = getSession(FIELD_TOKEN);
    if( isEmpty(token) ){
        token = checkIsNewTab();// for 右鍵新開TAB
    }
    
    var nowUrl = window.location.href;
    var adminPage = inPages(nowUrl, ADMIN_PAGES);
    _adminUser = isAdminUser();
    _tccDealer = isDealer();
    _storeOwner = isStoreOwner();
    _fiUser = isFiUser();
    console.log("checkLogin _adminUser="+_adminUser+", _tccDealer="+_tccDealer+", _storeOwner="+_storeOwner+", _fiUser="+_fiUser);
    
    var loginPage = getLoginPage(adminPage, _tccDealer);
    if( isEmpty(token) || (adminPage && !_adminUser) ){// 未登入
        window.location.href = loginPage;
        return;
    }
    
    if( !isNA(nowUrl) && (nowUrl.endWith(HOME_PAGE) || nowUrl.indexOf(".html")===-1) ){// index.html
        window.location.href = SELLER_HOME_PAGE+"?"+FIELD_TOKEN+"="+token;
    }else if( !isNA(nowUrl) && nowUrl.endWith(ADMIN_PAGE) ){// admin.html
        if( !isEmpty(token) && _adminUser ){
            window.location.href = ADMIN_HOME_PAGE+"?"+FIELD_TOKEN+"="+token;
        }else{
            window.location.href = loginPage;
        }
    }else{// others
        var restUrl = CHECK_LOGIN_URL+"?code="+window.location.pathname;
        fetchData(restUrl, false, afterCheckLogin, afterLoginFail, loginPage);
    }
}

function afterCheckLogin(response, redirectUrlOnFail){
    var loginPage = getLoginPage(inPages(URL_NOW, ADMIN_PAGES), isDealer());
    
    redirectUrlOnFail = isNA(redirectUrlOnFail)?loginPage:redirectUrlOnFail;

    if( !isNA(response) && !isNA(response[FIELD_TOKEN]) && !isNA(response.login) && response.login ){
        keepAuthResultToSession(response);// 儲存授權結果
        _adminUser = isAdminUser();
        _tccDealer = isDealer();
        _storeOwner = isStoreOwner();
        _fiUser = isFiUser();
        console.log("checkLogin _adminUser="+_adminUser+", _tccDealer="+_tccDealer+", _storeOwner="+_storeOwner+", _fiUser="+_fiUser);

        // 手機登入
        if( _doMobileLogin ){// clear params ....
            var pureUrl = getPagePureUrl(URL_NOW, _doMobileLogin?SELLER_PAGES:ADMIN_PAGES);
            if( !isNA(pureUrl) ){
                //console.log("pureUrl = "+pureUrl);
                window.location.href = pureUrl;
                return;
            }
        }
        
        // for 多商家選單
        $("#_switchStore").hide();
        if( $("#btnSetDef") ){
            $("#btnSetDef").hide();
        }
        _myStores = safeList(response["stores"]);
        createDropdown("#_myStores", _myStores, null, onSwitchStore);
        if( !isEmptyAry(_myStores) && _myStores.length>1 ){
            var storeId = isNA(response[FIELD_STOREID])? _myStores[0].value:response[FIELD_STOREID];
            console.log("afterCheckLogin storeId =", storeId);
            setDropdownValue("#_myStores", storeId);
            $("#_switchStore").show();
            if( $("#btnSetDef") ){
                $("#btnSetDef").show();
            }
        }
        
        if( !isNA(_renderThisPage) ){
            _renderThisPage();
        }
    }else{
        console.log("afterCheckLogin fail !");
        window.location.href = redirectUrlOnFail;
    }
}

function afterLoginFail(response, redirectUrl){
    if( _notLoginAlert || _doMobileLogin ){
        return;
    }
    _notLoginAlert = true;// 未登入警示中，不再執行任何 AJAX
    console.log("afterLoginFail ... ", response);
    // i18n 可能未載入須寫死
    var langCode = getLangCode();
    var msg = (!isNA(langCode) && langCode==='zh-TW')
        ? "您尚未登入，或畫面停滯過久，請重新登入系統。"
        : (!isNA(langCode) && langCode==='tr-TR')
        ? "Giriş yapmadınız veya ekran çok uzun süre durdu. Lütfen tekrar giriş yapın."
        : (!isNA(langCode) && langCode==='en-US')
        ? "You have not logged in, or the screen has been stagnant for too long. Please log in again."
        : "您尚未登入，或画面停滞过久，请重新登入系统。";
    alert(msg);
    gotoLogin(redirectUrl);
}

function gotoLogin(redirectUrl){
    redirectUrl = isNA(redirectUrl)?LOGIN_PAGE:redirectUrl;
    window.location.href = redirectUrl;
}

function isAdminUser(){
    var adminPage = inPages(URL_NOW, ADMIN_PAGES);
    var admin = getSession(FIELD_ADMIN);
    //console.log("isAdminUser admin = ", adminPage, admin);
    return !isNA(admin) && admin==="true" && adminPage;
}

function isDealer(){
    var dealerPage = inPages(URL_NOW, SELLER_PAGES);
    var dealer = getSession(FIELD_DEALER);
    return !isNA(dealer) && dealer==="true" && dealerPage;
}

function isStoreOwner(){
    var sellerPage = inPages(URL_NOW, SELLER_PAGES);
    var storeOwner = getSession(FIELD_ST_OWNER);
    return !isNA(storeOwner) && storeOwner==="true" && sellerPage;
}

function isFiUser(){
    var sellerPage = inPages(URL_NOW, SELLER_PAGES);
    var fiUser = getSession(FIELD_ST_FIUSER);
    return !isNA(fiUser) && fiUser==="true" && sellerPage;
}

function inPages(url, pages){
    if( !isEmptyAry(pages) ){
        for(var i=0; i<pages.length; i++){
            if( url.indexOf("/"+pages[i])>0 ){
                return true;
            } 
        }
    }
    return false;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Storage">
function isSupportStorage(){
    return (typeof(sessionStorage) !== "undefined");
}
function setSession(name, value){
    removeSession(name);
    if( !isNA(value) ){
        sessionStorage.setItem(name, value);
    }
}
function getSession(name){
    return sessionStorage.getItem(name);
}
function removeSession(name){
    sessionStorage.removeItem(name);
}
function keepAuthResultToSession(response){// 儲存授權結果
    setSession(FIELD_ACCOUNT, response[FIELD_ACCOUNT]);
    setSession(FIELD_TOKEN, response[FIELD_TOKEN]);
    setSession(FIELD_ADMIN, response[FIELD_ADMIN]);
    setSession(FIELD_DEALER, response[FIELD_DEALER]);
    setSession(FIELD_ST_OWNER, response[FIELD_ST_OWNER]);
    setSession(FIELD_ST_FIUSER, response[FIELD_ST_FIUSER]);
    setSession(FIELD_STOREID, response[FIELD_STOREID]);
}
function clearSession(){
    removeSession(FIELD_ACCOUNT);
    removeSession(FIELD_TOKEN);
    removeSession(FIELD_ADMIN);
    removeSession(FIELD_DEALER);
    removeSession(FIELD_ST_OWNER);
    removeSession(FIELD_ST_FIUSER);
    removeSession(FIELD_STOREID);
}
function getAuthResult(){
    if( isNA(getSession(FIELD_TOKEN)) ){
       return null; 
    }
    var authRes = {};
    authRes[FIELD_ACCOUNT] = getSession(FIELD_ACCOUNT);
    authRes[FIELD_TOKEN] = getSession(FIELD_TOKEN);
    authRes[FIELD_ADMIN] = getSession(FIELD_ADMIN);
    authRes[FIELD_DEALER] = getSession(FIELD_DEALER);
    authRes[FIELD_ST_OWNER] = getSession(FIELD_ST_OWNER);
    authRes[FIELD_ST_FIUSER] = getSession(FIELD_ST_FIUSER);
    authRes[FIELD_STOREID] = getSession(FIELD_STOREID);
    return authRes;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for URL">
// URL 有初始查詢條件
function initUrlCriteria(){
    var orderStatus = getUrlParameter("status");
    var rfqStatus = getUrlParameter("rfqStatus");
    var payStatus = getUrlParameter("payStatus");
    var shipStatus = getUrlParameter("shipStatus");
    var replyMsg = getUrlParameter("replyMsg");
    
    var formData = {};
    if( !isEmpty(orderStatus) ){
        formData["orderStatus"] = orderStatus;
    }
    if( !isEmpty(rfqStatus) ){
        formData["rfqStatus"] = rfqStatus;
    }
    if( !isEmpty(payStatus) ){
        formData["payStatus"] = payStatus;
    }
    if( !isEmpty(shipStatus) ){
        formData["shipStatus"] = shipStatus;
    }
    if( !isEmpty(replyMsg) ){
        formData["replyMsg"] = replyMsg;
    }
    console.log("initUrlCriteria ...\n", formData);
    return formData;
}

function getPagePureUrl(url, pages){
    if( !isEmptyAry(pages) ){
        for(var i=0; i<pages.length; i++){
            if( url.indexOf("/"+pages[i])>0 ){
                return pages[i];
            } 
        }
    }
    return null;
}

function addUrlQueryParam(url, pname, pvalue){
    if( isNA(pname) || isNA(pvalue) ){
        return url;
    }
    var ch = checkUrlByKeyword("?", url)?"&":"?";
    url = url + ch + pname + "=" + encodeURI(pvalue);
    return url;
}

function checkUrlByKeyword(key, url){
    url = isNA(url)?window.location.href:url;
    return (url.indexOf(key)>=0);
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Cookie">
function setCookie(cname,cvalue,exdays){
    var d = new Date();
    d.setTime(d.getTime()+(exdays*24*60*60*1000));// ? days
    var expires = "expires="+d.toGMTString();
    document.cookie = cname+"="+cvalue+"; "+expires;
}

function getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name)===0) { return c.substring(name.length, c.length); }
    }
    return "";
}

function removeCookie(cname){
    setCookie(cname, null, -1);
}

function clearCookies(){
    console.log("clearCookies ...");
    document.cookie.split(";").forEach(function(c){
        document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); 
    });
}

// for open new tab
function getTabId(){
    var tabId = getUrlParameter(FIELD_TABID);
    if( !isNA(tabId) ){
        return tabId;
    }
    
    var token = getSession(FIELD_TOKEN);
    var loginAccount = getSession(FIELD_ACCOUNT);
    var storeId = getSession(FIELD_STOREID);
    var adminUser = getSession(FIELD_ADMIN);
    var tccDealer = getSession(FIELD_DEALER);
    var storeOwner = getSession(FIELD_ST_OWNER);
    var fiUser = getSession(FIELD_ST_FIUSER);
    if( !isNA(token) && !isNA(loginAccount) ){
        tabId = genTabId(loginAccount, storeId, adminUser, tccDealer, storeOwner, fiUser);
    }
    return tabId;
}

function genTabId(loginAccount, storeId, adminUser, tccDealer, storeOwner, fiUser){
    console.log("genTabId tccDealer = "+tccDealer);
    var tabId = (!isNA(adminUser) && adminUser==='true')?"admin."+loginAccount:
                (!isNA(tccDealer) && tccDealer==='true')?"dealer."+loginAccount:
                "seller."+loginAccount+".store"+storeId;
    return tabId;
}

function keepState(state){
    setCookie(FIELD_STATE, state, 1);
}

function removeState(){
    removeCookie(FIELD_STATE);
}

function keepCookieForNewTab(){
    //var tabId = getUrlParameter(FIELD_TABID);
    var token = getSession(FIELD_TOKEN);
    var loginAccount = getSession(FIELD_ACCOUNT);
    var storeId = getSession(FIELD_STOREID);
    var adminUser = getSession(FIELD_ADMIN);
    var storeOwner = getSession(FIELD_ST_OWNER);
    var fiUser = getSession(FIELD_ST_FIUSER);
    var tccDealer = getSession(FIELD_DEALER);
    
    if( !isNA(token) && !isNA(loginAccount) ){
        _tabId = genTabId(loginAccount, storeId, adminUser, tccDealer, storeOwner, fiUser);
        setCookie(_tabId, token, 1);
        console.log("keepCookieForNewTab document.cookie = \n", document.cookie);
    }
}

function removeCookieForNewTab(){
    var loginAccount = getSession(FIELD_ACCOUNT);
    
    if( !isNA(loginAccount) ){
        console.log("removeCookieForNewTab _tabId = ", _tabId);
        removeCookie(_tabId);
        console.log("removeCookieForNewTab document.cookie = \n", document.cookie);
    }
}

function getCookieForNewTab(tabId){
    if( !isNA(tabId) ){
        _tabId = tabId;
        return getCookie(_tabId);
    }
    
    var token = getSession(FIELD_TOKEN);
    var loginAccount = getSession(FIELD_ACCOUNT);
    var storeId = getSession(FIELD_STOREID);
    var adminUser = getSession(FIELD_ADMIN);
    var storeOwner = getSession(FIELD_ST_OWNER);
    var fiUser = getSession(FIELD_ST_FIUSER);
    var tccDealer = getSession(FIELD_DEALER);
    
    if( !isNA(token) && !isNA(loginAccount) ){
        _tabId = genTabId(loginAccount, storeId, adminUser, tccDealer, storeOwner, fiUser);
        return getCookie(_tabId);
    }
    
    return null;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for form handle">
function detectCapsLock(inputId, msgId){
    var input = document.getElementById(inputId);
    var text = document.getElementById(msgId);
    text.style.display = "none";

    input.addEventListener("keyup", function(event){
        if( !isNA(event) && !isNA(event.getModifierState) ){
            if (event.getModifierState("CapsLock")) {
                text.style.display = "block";
            } else {
                text.style.display = "none";
            }
        }
    });
}

function disableFormInput(selector){
    $(selector+' input').each(function () {
        //if( $(this).attr('disabled') ){
        //    $(this).removeAttr('disabled');
        //}else{
            $(this).attr({'disabled': 'disabled'});
        //}
    });
}

// copy json object values to form input values
function copyPropsToForm(obj, idPostfix){
    idPostfix = isNA(idPostfix)?"":idPostfix;
    for(var name in obj){
        if( $("#"+name+idPostfix) ){
            if( $("#"+name+idPostfix).prop("tagName")!=='SELECT' ){
                $("#"+name+idPostfix).val(isNA(obj[name])?"":obj[name]);
            }
        }
    }
}

function copyPropsToView(obj, idPostfix){
    idPostfix = isNA(idPostfix)?"":idPostfix;
    for(var name in obj){
        if( $("#"+name+idPostfix) ){
            $("#"+name+idPostfix).html(isNA(obj[name])?"":obj[name]);
        }
    }
}

// input text 輸入驗證 (搭配 title & required 屬性)
function validateInputRequired(formSelector){
    var $inputs = $(formSelector+' :input');
    var errMsg = "";
    $inputs.each(function(){
        //console.log($(this).attr("name"), $(this).attr("type"));
        if( $(this).attr("type")==="file" 
         || $(this).attr("type")==="button" 
         || isNA($(this).attr("title")) 
         || isNA($(this).val()) ){
            return;
        }
        var title = $(this).attr("title");
        //title = isNA($.i18n) || isNA($.i18n.prop(title))? title:$.i18n.prop(title);// i18n
        var value = $(this).val().trim();
        $(this).val(value);
        // 必填檢查 required
        if( $(this).attr("required")==="required" ){
            if( isEmpty(value) ){
                if( isEmpty(errMsg) ){
                    errMsg = i18nWP("noinput.fd", title); //"["+title+"]未輸入!";
                    return errMsg;
                }
            }
        }
        // 最大長度會直接限制
        // 最小長度檢查 minlength
        if( !isNA($(this).attr("minlength")) ){
            var len = parseInt($(this).attr("minlength"));
            if( value.length<len ){
                if( isEmpty(errMsg) ){
                    errMsg = i18nWP("input.len.over", title, len); //"["+title+"]長度不可小於 "+len+" !";
                    return errMsg;
                }
            }
        }
        // 整數 min max
        if( !isEmpty(value) && (!isNA($(this).attr("min")) || !isNA($(this).attr("max"))) ){
            if( !isEmpty(value) && !validator.isInt(value) ){
                if( isEmpty(errMsg) ){
                    errMsg = i18nWP("input.integer", title);//"["+title+"]須為整數格式!";
                    return errMsg;
                }
            }
            if( !isNA($(this).attr("min")) ){
                var min = parseInt($(this).attr("min"));
                if( parseInt(value)<min ){
                    if( isEmpty(errMsg) ){
                        errMsg = i18nWP("input.len.than", title, min);//"["+title+"]須大於 "+ min +" !";
                        return errMsg;
                    }
                }
            }
            if( !isNA($(this).attr("max")) ){
                var max = parseInt($(this).attr("max"));
                if( parseInt(value)>max ){
                    if( isEmpty(errMsg) ){
                        errMsg = i18nWP("input.less.than", title, max);//"["+title+"]須小於 "+ max +" !";
                        return errMsg;
                    }
                }
            }
        }

        // email 格式 name
        var name = $(this).attr("name");
        if( !isNA(name) && name.indexOf("email")===0 ){
            if( !isEmpty(value) && !validator.isEmail(value) ){
                errMsg = i18nWP("input.format.err", title);//"["+title+"]格式不正確!";
                return errMsg;
            }
        }
    });
    
    return errMsg;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for object & collection">
function toIdList(ary, idField){
    var ids = [];
    if( isNA(ary) ){
        return ids;
    }
    for(var i=0; i<ary.length; i++){
        var value = ary[i][idField];
        ids.push(value);
    }

    return ids;
}

function listToOptions(ary, valueField, labelField){
    var ops = [];
    if( isNA(ary) ){
        return ops;
    }
    for(var i=0; i<ary.length; i++){
        var label = ary[i][labelField];
        var value = ary[i][valueField];
        ops.push({"label":label, "value":value});
    }

    return ops;
}

function genNoSelectOpNum(){
    return {label:i18n("txt.noselct"), value:0};
}
function genNoSelectOpStr(){
    return {label:i18n("txt.noselct"), value:""};
}

function cloneObj(o) {
    return (JSON.parse(JSON.stringify(o)));
}

function copyProps(desObj, srcObj){
    for(var k in srcObj){
        desObj[k]=srcObj[k];
    }
}

function mergeObj(main, other) {
    for(var key in other){
        if( isNA(main[key]) ){
            main[key] = other[key];
        }
    }

    return main;
}

function contains(ary, item, fieldname){
    if( !isNA(ary) && ary.length && ary.length>0 ){
        if( isNA(fieldname) ){
            for(var i=0; i<ary.length; i++){
                if( ary[i]===item ){
                    return true;
                }
            }
        }else{
            for(var i=0; i<ary.length; i++){
                if( ary[i][fieldname]===item[fieldname] ){
                    return true;
                }
            }
        }
    }
    return false;
}

function safeList(ary){
    if( !isNA(ary) && ary.length ){
        return ary;
    }
    return [];
};

// get json array property
function getListProp(item, key){
    if( isNA(item) ){
        return [];
    }
    var values = isEmptyAry(item[key])? []:item[key];
    return values;
}

// map
function getRowsByName(datas, keyName) {
    for (var name in datas) {
        if (name.toLowerCase() === keyName.toLowerCase()) {
            return datas[name];
        }
    }
}

// 重設 sortnum 值
function resetSortnum(list){
    if( isNA(list) || isNA(list.length) || list.length===0 ){
        return list;
    }else{
        for(var i=0; i<list.length; i++){
            if( isNA(list[i]["sortnum"]) || list[i]["sortnum"]!==i+1 ){
                list[i]["sortnum"] = i+1;
                list[i][FIELD_MODIFIED] = true;// 順序變更也要儲存
            }
        }
    }
}

// fund object index in list by key field value 
function fundIndexByKey(list, field, value){
    if( !isNA(list) && !isNA(list.length) ){
        for(var i=0; i<list.length; i++){
            if( list[i][field]===value ){
                return i;
            }
        }
    }
    return -1;
}

// 提供 lsit 每個 item 唯一的 UUID
function initListUUID(list){
    if( !isNA(list) ){
        for(var i=0; i<list.length; i++){
            list[i][FIELD_UUID] = genUUID();
            list[i][FIELD_MODIFIED] = false;
        }
    }
}

// 依 UUID 取得在 list 所處 index
function findIndexByUUID(list, uuid){
    return fundIndexByKey(list, FIELD_UUID, uuid);
}

// Check Unique
function checkUnique(list, obj, keyField, valueField, isString){
    if( isEmptyAry(list) ){
        return true;
    }
    for(var i=0; i<list.length; i++){
        if( obj[keyField] !== list[i][keyField] ){
            if( (isString && obj[valueField].trim() === list[i][valueField].trim()) 
             || (!isString && obj[valueField] === list[i][valueField]) ){
                return false;
            }
        }
    }
    return true;
}

function isSingleItem(list){
    return !isNA(list) && !isNA(list.length) && list.length===1;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for tree data">
function convertTreeNodesToOps(ary){
    var ops = [];
    if( isNA(ary) ){
        return ops;
    }

    for(var i=0; i<ary.length; i++){
        var label = ary[i].data.cname;
        var value = ary[i].data.id;
        ops.push({"label":label, "value":value});
    }

    return ops;
}

function findChildrenFromTree(treeNodes, parent){
    if( isNA(treeNodes) ){
        return null;
    }
    for(var i=0; i<treeNodes.length; i++){
        if( parent===null ){
            return treeNodes;
        }else if( parent === treeNodes[i].data.id ){
            return treeNodes[i].children;
        }else{
            var children = findChildrenFromTree(treeNodes[i].children, parent);
            if( children!==null ){
                return children;
            }
        }
    }
    return null;
}

function findSameParenItemsFromTree(treeNodes, id){
    if( isNA(treeNodes) || isNA(id) ){
        return null;
    }
    for(var i=0; i<treeNodes.length; i++){
        if( id === treeNodes[i].data.id ){
            return treeNodes;
        }else{
            var items = findSameParenItemsFromTree(treeNodes[i].children, id);
            if( items!==null ){
                return items;
            }
        }
    }
    
    return null;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for variable check">
/**
  ^\\d+$                //非負整數（正整數+0）     
  ^[0-9]*[1-9][0-9]*$　　//正整數     
  ^((-\\d+)|(0+))$      //非正整數（負整數+0）     
  ^-[0-9]*[1-9][0-9]*$  //負整數     
  ^-?\\d+$              //整數     
  ^\\d+(\\.\\d+)?$      //非負浮點數（正浮點數+0）     
  ^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$　　//正浮點數     
  ^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$　　//非正浮點數（負浮點數+0）     
  ^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$　　//負浮點數     
  ^(-?\\d+)(\\.\\d+)?$　　//浮點數
 * @param {type} re
 * @param {type} s
 * @returns {unresolved}
 */
function TestRegex(re, s){  // 参數說明 re 为正則表達式   s 为要判斷的字符 
    return re.test(s);
}

function isPositiveInt(str){
    var re = /^[0-9]*[1-9][0-9]*$/;
    return TestRegex(re, str);
}

function isNA(o){
    return (o === null || o === undefined);
}

function isEmpty(str){
    return isNA(str) || str==="" || (isNaN(str) && str.trim().length===0);
}
function isEmptyAry(ary){
    return isNA(ary) || ary.length===0;
}

function isId(o){
    return !isNA(o) && isNumeric(o) && parseInt(o)>0;
}

function isNumeric(num){
    return !isNaN(num) && !isEmpty(num);
}

function nvl(ori, def){
    if( isNA(ori) ){
        return def;
    }
    return ori;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Overlay">
function showOverlay(overlayId, dir, msg) {
    if (dir==='V') {
        document.getElementById(overlayId).style.height = "100vh";
    } else if (dir==='H') {
        document.getElementById(overlayId).style.width = "100%";
    } else {
        document.getElementById(overlayId).style.height = "100vh";
        document.getElementById(overlayId).style.width = "100%";
    }

    if( overlayId==="errorOverlay" ){
        document.getElementById("errorMsg").innerHTML = (!isNA(msg))?"<br/>"+msg+"<br/>":"&nbsp;";
    }
}

function hideOverlay(overlayId, dir) {
    if( isNA(document.getElementById(overlayId)) || isNA(document.getElementById(overlayId).style) ){
        return;
    }
    if(dir==='V') {
        document.getElementById(overlayId).style.height = "0";
    }else if(dir==='H') {
        document.getElementById(overlayId).style.width = "0";
    }else{
        document.getElementById(overlayId).style.height = "0";
        document.getElementById(overlayId).style.width = "0";
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Timer">
// 特殊需要，控制延後執行指定工作
function delayRun(func, params, ms) {
    var timerKey = genUUID();
    _timerMap[timerKey] = setTimeout(function(){
        func(params);
        if( _timerMap[timerKey] ){
            clearTimeout(_timerMap[timerKey]);
            console.log("clearTimeout ... ", timerKey);
        }
    }, ms);
}

// for waitting async jobs completed
function waitting(period){
    _maxWaittingTimes--;
    if( _waittingTimer ){ clearTimeout(_waittingTimer); }
    if( !checkAsyncComplete(_asyncCompleteFlags) && _maxWaittingTimes>0 ){
        waitDummy(period);
        //_waittingTimer = setTimeout('waitDummy('+period+'); clearTimeout(_waittingTimer);', 500);
    }else{
        _waittingTimer = setTimeout('hideOverlay("startOverlay", "H"); clearTimeout(_waittingTimer);', 500);
        if( _maxWaittingTimes<=0 ){
            showOverlay("errorOverlay", "V", i18n("exceptions"));
        }
        _maxWaittingTimes = 20;// 復原
    }
}
function waitDummy(period){
    $.getJSON(WAITTING_URL, function(data){
        console.log("waitDummy ...", _waittingTimer, data);
        _waittingTimer = setTimeout("waitting("+period+");", period);// 間隔檢查時間
    });
}
function checkAsyncComplete(completeFlags){
    var completed = true;
    if( isNA(completeFlags) ){
        console.warn("checkAsyncComplete no completeFlags ...");
        return completed;
    }
    for(var k in completeFlags){
        if( !completeFlags[k] ){
            console.log("checkAsyncComplete no completed "+k);
            completed = false;
            break;
        }
    }
    return completed;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Number">
function getNumber(numStr, def){
    if( isNA(numStr) ){
        //console.error("getNumber isNA numStr = "+numStr);
        return isNA(def)?"":def;
    }
    numStr = numStr.replace(/,/g, "");
    return Number(numStr);
}

function printNumber(num, def){
    if( isNA(num) || isNaN(num) ){
        //console.warn("printNumber isNaN num = "+num);
        return isNA(def)?"&nbsp;":def;// 放全型空白 for RWD DIV 畫面美觀
    }
    return toThousands(num);
}

function toThousands(numIn){
    var numStr = (numIn || 0).toString();
    var sign, dg, num, i;
    // 檢查小數
    i = numStr.indexOf(".");
    if( i>0 ){
        dg = numStr.substring(i);
        num = numStr.substring(0, i);
    }else if( i===0 ){
        return numStr;
    }else {
        dg = "";
        num = numStr;
    }
    // 檢查負數
    if( num.indexOf("-")>=0 ){
        sign = "-";
        num = num.substring(num.indexOf("-")+1);
    }else{
        sign = "";
    }
            
    result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) { result = num + result; }
    result = sign + result + dg;
    return result;
}

//產生min到max之間的亂數
function getRandom(min,max){
    return Math.floor(Math.random()*(max-min+1))+min;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for String">
function genUUID() {
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c==='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function prettyPrintMsgs(msgAry){
    var messages = [];
    for(var idx in msgAry){
        messages.push(breakLine(escapeHTML(msgAry[idx])));
    }
    return messages;
}

function escapeHTML(html) {
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}

function asHTML(text){
    if( (typeof text) === 'string' ){
        return text.replace(/\n\r/g, "<br/>")
                .replace(/\n/g, "<br/>");
    }else{
        return text;
    }
}

function safePrint(data, def){
    if( isNA(data) || data==='' ){
        return isNA(def)?'':def;
    }else{
        if( (typeof data) === 'string' ){
            data = data.replace(/<script/i, "&lt;script");
        }
        return data;
    }
}

function prettyPrint(data){
    return safePrint(data, "　");// 放全型空白 for RWD DIV 畫面美觀
}

function leftPad(ori, pad){
    ori = ""+ori;
    return (ori.length<=pad.length)? (pad + ori).slice(-pad.length):ori;
}

function indents(n){
    var indentStr = '';
    while(n>0){
        indentStr += '  ';
        n--;
    }
    return indentStr;
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Date & Time">
function toDate(str){
    return isNA(str)?null:new Date(formatDateStr(str));
}
function toDateTime(str){
    return isNA(str)?null:new Date(toISODateTime(str));
}

function nowYM(){
    var date = new Date();
    return date.getFullYear()+''+leftPad(date.getMonth()+1, '00');
} 

/** 
 * ex. '2011-01-01' to '2011-01-01T00:00:00' for restful
 * ex. '2011-01-01 12:21:11' to '2011-01-01T12:21:11' for restful
 * @param {type} dateTimeStr
 * @returns {unresolved}
 */
function toISODateTime(dateTimeStr){
    if( isNA(dateTimeStr) || isNA(dateTimeStr.length) || dateTimeStr.length<10 ){
        return null;
    }
    if( dateTimeStr.length===10 ){
        return dateTimeStr + ' 00:00:00';
    }else{
        //dateTimeStr = dateTimeStr.replace(" ", "T");
        if( dateTimeStr.length===13 ){
            dateTimeStr = dateTimeStr + ":00:00";
        }else if( dateTimeStr.length===16 ){
            dateTimeStr = dateTimeStr + ":00";
        }
        return dateTimeStr;
    }
}

function formatDate(date) {
    if( isNA(date) ){
        return "";
    }
    return date.getFullYear() + "-" + leftPad(date.getMonth()+1, '00') + "-" + leftPad(date.getDate(), '00');
}

function formatDateTime(date, showSecond, showAmPm) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    if( showAmPm ){
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
    }
    //minutes = (minutes < 10) ? '0'+minutes:minutes;
    hours = leftPad(hours, '00');
    minutes = leftPad(minutes, '00');
    seconds = leftPad(seconds, '00');
    var strTime = hours + ':' + minutes + ((showSecond)?':'+seconds:'') + ((showAmPm)?' '+ampm:'');
    return date.getFullYear() + "-" 
            + leftPad(date.getMonth()+1, '00') + "-" 
            + leftPad(date.getDate(), '00') 
            + " " + strTime;
}

function formatDT(date) {
    return formatDateTime(date, true, false);
}

function formatDateHM(date) {
    return formatDateTime(date, false, false);
}

function formatYM(date) {
    return date.getFullYear() + "/" + leftPad(date.getMonth()+1, '00');
}

function genYMList(num, delimiter) {
    var list = [];
    var date = new Date();
    var yy = date.getFullYear();
    var mm = (date.getMonth() + 1);

    for(var i=0; i<num; i++){
        var item = yy + delimiter + ((mm<10)?'0'+mm:mm);
        list.push(item);

        mm = mm - 1;
        yy = (mm>0)? yy:yy-1;
        mm = (mm>0)? mm:12;
    }

    return list;
}

function genYMOpions(num, delimiter) {
    var list = [];
    var date = new Date();
    var yy = date.getFullYear();
    var mm = (date.getMonth() + 1);

    for(var i=0; i<num; i++){
        var mstr = ((mm<10)?'0'+mm:mm);
        var item = yy + delimiter + mstr;
        var dt = yy + "-" + mstr + "-01";
        var op = {
            "label":item,
            "value":toISODateTime(dt)
        };
        list.push(op);

        mm = mm - 1;
        yy = (mm>0)? yy:yy-1;
        mm = (mm>0)? mm:12;
    }

    return list;
}

function formatDateHMStr(timeStr) {
    var dt = formatDateTimeStr(timeStr);
    if( !isNA(dt) && dt.length>16){
        dt = dt.substring(0, 16);
    }
    return dt;
}

function formatDateStr(timeStr){
    return isNA(timeStr)?"":(timeStr.length>=10? timeStr.substring(0, 10):timeStr);
}

function formatDateTimeStr(timeStr){
    return timeStr; // server 端已處理
    /*
    if( isNA(timeStr) ){
        return "";
    }
    if( timeStr.indexOf("T")>0 ){// ex. 2008-05-10T11:00:00-05:00
        timeStr = timeStr.substring(0, timeStr.length-6);
        return timeStr.replace("T", " ");
    }
    return timeStr;
    */
}

// yyyyMMdd to yyyy-MM-dd
function formatFromSapDate(str){
    if( !isEmpty(str) && str.length===8 ){
        return str.substring(0, 4)+"-"+str.substring(4, 6)+"-"+str.substring(6);
    }else{
        return str;
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Boolean">
function isTrue(bool){
    return !isNA(bool) && bool;
}

function isFalse(bool){
    return !isNA(bool) && !bool;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Image">
function resizeFullImg(selector) {
    var viewportWidth = $(window).innerWidth();
    var viewportHeight = $(window).innerHeight();

    var width = $(selector).width();
    var height = $(selector).height();

    if ((viewportWidth / viewportHeight) > (width / height)) {
        $(selector).css({
            'width': '100%',
            'height': 'auto',
            'margin-left': 0 - width / 2,
            'margin-top': 0 - height / 2
        });
    } else {
        $(selector).css({
            'width': 'auto',
            'height': '100%',
            'margin-left': 0 - width / 2,
            'margin-top': 0 - height / 2
        });
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Browser & Client Info">   
function getClientInfo(){
    $.ajax({ 
        url: 'http://ipinfo.io',
        dataType: 'json', 
        //data: data, 
        success: function( data ) {
            console.log(data);
            console.log(JSON.stringify(data));
        }, 
        error: function (response) {
            console.log("error ["+response.status+"]"+response.statusText);
        },
        timeout: 3000 //3 second timeout 
    });
}

function getBrowserInfo(){
    var nVer = navigator.appVersion;
    var nAgt = navigator.userAgent;
    var browserName  = navigator.appName;
    var fullVersion  = ''+parseFloat(navigator.appVersion); 
    var majorVersion = parseInt(navigator.appVersion,10);
    var nameOffset,verOffset,ix;

    // In Opera 15+, the true version is after "OPR/" 
    if ((verOffset=nAgt.indexOf("OPR/"))!==-1) {
        browserName = "Opera";
        fullVersion = nAgt.substring(verOffset+4);
    }
    // In older Opera, the true version is after "Opera" or after "Version"
    else if ((verOffset=nAgt.indexOf("Opera"))!==-1) {
        browserName = "Opera";
        fullVersion = nAgt.substring(verOffset+6);
        if ((verOffset=nAgt.indexOf("Version"))!==-1) 
            fullVersion = nAgt.substring(verOffset+8);
    }
    // In MSIE, the true version is after "MSIE" in userAgent
    else if ((verOffset=nAgt.indexOf("MSIE"))!==-1) {
        browserName = "Microsoft Internet Explorer";
        fullVersion = nAgt.substring(verOffset+5);
    }
    // In Chrome, the true version is after "Chrome" 
    else if ((verOffset=nAgt.indexOf("Chrome"))!==-1) {
        browserName = "Chrome";
        fullVersion = nAgt.substring(verOffset+7);
    }
    // In Safari, the true version is after "Safari" or after "Version" 
    else if ((verOffset=nAgt.indexOf("Safari"))!==-1) {
        browserName = "Safari";
        fullVersion = nAgt.substring(verOffset+7);
        if ((verOffset=nAgt.indexOf("Version"))!==-1) 
            fullVersion = nAgt.substring(verOffset+8);
    }
    // In Firefox, the true version is after "Firefox" 
    else if ((verOffset=nAgt.indexOf("Firefox"))!==-1) {
        browserName = "Firefox";
        fullVersion = nAgt.substring(verOffset+8);
    }
    // In most other browsers, "name/version" is at the end of userAgent 
    else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < 
            (verOffset=nAgt.lastIndexOf('/')) ) 
    {
        browserName = nAgt.substring(nameOffset,verOffset);
        fullVersion = nAgt.substring(verOffset+1);
        if (browserName.toLowerCase()===browserName.toUpperCase()) {
            browserName = navigator.appName;
        }
    }
    // trim the fullVersion string at semicolon/space if present
    if ((ix=fullVersion.indexOf(";"))!==-1)
        fullVersion=fullVersion.substring(0,ix);
    if ((ix=fullVersion.indexOf(" "))!==-1)
        fullVersion=fullVersion.substring(0,ix);

    majorVersion = parseInt(''+fullVersion,10);
    if (isNaN(majorVersion)) {
        fullVersion  = ''+parseFloat(navigator.appVersion); 
        majorVersion = parseInt(navigator.appVersion,10);
    }

    var info = {
        "browserName":browserName,
        "fullVersion":fullVersion,
        "majorVersion":majorVersion,
        "appName":navigator.appName,
        "userAgent":navigator.userAgent
    };
    
    return info;
}

function checkBrowserSupport(){
    var client = getBrowserInfo();
    if( client.browserName === "Microsoft Internet Explorer" ){
        if( client.majorVersion < 10 ){
            console.warn(client);
            return false;
        }
    }
    return true;
}

function getBrowserType(){
    var browser = "";	
    
    if(/Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor))
        browser = "Chrome";
    
    if(/Safari/.test(navigator.userAgent) && /Apple Computer/.test(navigator.vendor))
        browser = "Safari";
    
    if (navigator.userAgent.indexOf('Firefox') !== -1 && parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox') + 8)) >= 3.6)
        browser = "Firefox";
    
    if(navigator.userAgent.toLowerCase().indexOf('msie') !== -1 || navigator.appVersion.indexOf('Trident/') > 0){
        if(navigator.appVersion.indexOf("MSIE 6.") !== -1) browser = "IE6";
        else if(navigator.appVersion.indexOf("MSIE 7.") !== -1) browser = "IE7";
        else if(navigator.appVersion.indexOf("MSIE 8.") !== -1) browser = "IE8";
        else if(navigator.appVersion.indexOf("MSIE 9.") !== -1) browser = "IE9";
        else if(navigator.appVersion.indexOf('Trident/6.0') > 0) browser = "IE10";
        else if(navigator.appVersion.indexOf('Trident/7.0') > 0) browser = "IE11";
    }
    
    return browser;
}

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Window & Screen & Document & Position">
// scroll y to selector object
function scrollTo(selector, offset){
    var y = $(selector).offset().top;
    //y = y<60? y:y-60;
    y = isNA(offset)? y:y-offset;
    $('html,body').animate({scrollTop: y}, 'slow');
}

function scrollToTop(){
    $('html,body').animate({ scrollTop: 0 }, 'fast');/* 返回到最頂上 */
}

function hideBodyScrollY(){
    $('html,body').css('overflow-y', 'hidden');
}
function showBodyScrollY(){
    $('html,body').css('overflow-y', 'auto');
}

function getDomWidth(){
    return document.documentElement.clientWidth;
}

function getDomHeight(){
    return document.documentElement.clientHeight;
}

function isSmallScreen(){
    return getDomWidth()<560;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Prototype, Polyfill method">
String.prototype.hashCode = function() {
  var hash = 0, i, chr;
  if (this.length === 0) return hash;
  for (i = 0; i < this.length; i++) {
    chr   = this.charCodeAt(i);
    hash  = ((hash << 5) - hash) + chr;
    hash |= 0; // Convert to 32bit integer
  }
  return hash;
};

if ( typeof String.prototype.startWith !== 'function' ) {
    String.prototype.startWith = function( str ) {
        return str.length > 0 && this.substring( 0, str.length ) === str;
    };
};

if ( typeof String.prototype.endWith !== 'function' ) {
    String.prototype.endWith = function( str ) {
        return str.length > 0 && this.substring( this.length - str.length, this.length ) === str;
    };
};

Number.isInteger = Number.isInteger || function(value) {
    return typeof value === 'number' && 
            isFinite(value) && 
            Math.floor(value) === value;
};
//</editor-fold>

var utils = {
    // for global init
    getSysName: getSysName,
    getLoginPage: getLoginPage,
    // for Ajax
    fetchData: fetchData,
    getRestCallBack: getRestCallBack,
    postRestCallBack: postRestCallBack,
    restErrorHandler: restErrorHandler,
    postDelete: postDelete,
    postData: postData,
    uploadFiles: uploadFiles,
    // for restful result
    isSuccess: isSuccess,
    checkResponse: checkResponse,
    getResponseList: getResponseList,
    // depend primeUI
    addMessage: addMessage,
    closeMessage: closeMessage,
    addSuccessMsg: addSuccessMsg,
    renderMultiPanel: renderMultiPanel,
    autoResizeDataTable: autoResizeDataTable,
    // for i18N
    loadI18nProperties: loadI18nProperties,
    i18n: i18n,
    i18nWP: i18nWP,
    getLanguage: getLanguage,
    getLangCode: getLangCode,
    getOptionLabelLang: getOptionLabelLang,
    getLanguageOps: getLanguageOps,
    // for Login & Switch Store
    onSwitchStore: onSwitchStore,
    afterSwitchStore: afterSwitchStore,
    checkLoginFromApp: checkLoginFromApp,
    checkIsNewTab: checkIsNewTab,
    checkLogin: checkLogin,
    afterCheckLogin: afterCheckLogin,
    afterLoginFail: afterLoginFail,
    gotoLogin: gotoLogin,
    isAdminUser: isAdminUser,
    isDealer: isDealer,
    isStoreOwner: isStoreOwner,
    isFiUser: isFiUser,
    inPages: inPages,
    // for Storage
    isSupportStorage: isSupportStorage,
    setSession: setSession,
    getSession: getSession,
    removeSession: removeSession,
    keepAuthResultToSession: keepAuthResultToSession,
    clearSession: clearSession,
    getAuthResult: getAuthResult,
    // for URL
    initUrlCriteria: initUrlCriteria,
    getPagePureUrl: getPagePureUrl,
    addUrlQueryParam: addUrlQueryParam,
    checkUrlByKeyword: checkUrlByKeyword,
    getUrlParameter: getUrlParameter,
    // for Cookie
    setCookie: setCookie,
    getCookie: getCookie,
    removeCookie: removeCookie,
    clearCookies: clearCookies,
    getTabId: getTabId,
    genTabId: genTabId,
    keepState: keepState,
    removeState: removeState,
    keepCookieForNewTab: keepCookieForNewTab,
    removeCookieForNewTab: removeCookieForNewTab,
    getCookieForNewTab: getCookieForNewTab,
    // for form handle
    detectCapsLock: detectCapsLock,
    disableFormInput: disableFormInput,
    copyPropsToForm: copyPropsToForm,
    copyPropsToView: copyPropsToView,
    validateInputRequired: validateInputRequired,
    // for object & collection
    toIdList: toIdList,
    listToOptions: listToOptions,
    genNoSelectOpNum: genNoSelectOpNum,
    genNoSelectOpStr: genNoSelectOpStr,
    cloneObj: cloneObj,
    copyProps: copyProps,
    mergeObj: mergeObj,
    contains: contains,
    safeList: safeList,
    getListProp: getListProp,
    getRowsByName: getRowsByName,
    resetSortnum: resetSortnum,
    fundIndexByKey: fundIndexByKey,
    initListUUID: initListUUID,
    findIndexByUUID: findIndexByUUID,
    checkUnique: checkUnique,
    isSingleItem: isSingleItem,
    // for tree data
    convertTreeNodesToOps: convertTreeNodesToOps,
    findChildrenFromTree: findChildrenFromTree,
    findSameParenItemsFromTree: findSameParenItemsFromTree,
    // for variable check
    TestRegex: TestRegex,
    isPositiveInt: isPositiveInt,
    //isNA: isNA,  
    //isEmpty: isEmpty,  
    isEmptyAry: isEmptyAry,  
    isId: isId,  
    isNumeric: isNumeric,
    nvl: nvl,//

    // for Overlay
    showOverlay: showOverlay,
    hideOverlay: hideOverlay,
    // for Timer
    delayRun: delayRun,
    waitting: waitting,//
    waitDummy: waitDummy,
    checkAsyncComplete: checkAsyncComplete,
    // for Number
    getNumber: getNumber,
    printNumber: printNumber,
    toThousands: toThousands,
    getRandom: getRandom,
    // for String
    genUUID: genUUID,
    prettyPrintMsgs: prettyPrintMsgs,//
    escapeHTML: escapeHTML,//
    asHTML: asHTML,
    safePrint: safePrint,
    prettyPrint: prettyPrint,
    leftPad: leftPad,
    indents: indents,
    // for Date & Time
    toDate: toDate,
    toDateTime: toDateTime,
    nowYM: nowYM,
    toISODateTime: toISODateTime,
    formatDate: formatDate,
    formatDateTime: formatDateTime,//
    formatDT: formatDT,
    formatDateHM: formatDateHM,//
    formatYM: formatYM,//
    genYMList: genYMList,//
    genYMOpions: genYMOpions,
    formatDateHMStr: formatDateHMStr,
    formatDateStr: formatDateStr,
    formatFromSapDate: formatFromSapDate,
    // for Boolean
    isTrue: isTrue,
    isFalse: isFalse,
    // for Image
    resizeFullImg: resizeFullImg,//
    // for Browser & Client Info
    getClientInfo: getClientInfo,//
    getBrowserInfo: getBrowserInfo,
    checkBrowserSupport: checkBrowserSupport,
    getBrowserType: getBrowserType,//
    // for Window & Screen & Document & Position
    scrollTo: scrollTo,
    scrollToTop: scrollToTop,//
    hideBodyScrollY: hideBodyScrollY,
    showBodyScrollY: showBodyScrollY,
    getDomWidth: getDomWidth,
    getDomHeight: getDomHeight,//
    isSmallScreen: isSmallScreen
};
