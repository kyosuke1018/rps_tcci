/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_HOME, PN_SELLER_LOGIN, CryptoJS, TEST_DATA, LOGIN_URL, AesUtil, MSG_INPUT_ERR, SELLER_HOME_PAGE, FIELD_TOKEN, FIELD_ADMIN, ADMIN_HOME_PAGE, FIELD_ACCOUNT, LOGIN_PAGE, FIELD_LANG, w3, PWD_RESET_URL, ADMIN_PAGES, URL_NOW, FIELD_FROM, FROM_WEBPAGE, _doMobileLogin, FIELD_STOREID, FIELD_STATE, genUUID, ADMIN_LOGIN_PAGE, DEALER_LOGIN_PAGE, FIELD_DEALER, FIELD_ST_OWNER, FIELD_ST_FIUSER, FROM_INTERNAL, CHECK_IT_LOGIN_URL, utils */
//var iv = "F27D5C9927726BCEFE7510B1BDD3D137";
//var salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
var iv = utils.genUUID().replace(/-/g, "");
var salt = utils.genUUID().replace(/-/g, "")+utils.genUUID().replace(/-/g, "");
var keySize = 128;
var iterationCount = 100;//
var passPhrase = "This is EC backend system.";//
var forAdmin = window.location.href.indexOf(ADMIN_LOGIN_PAGE)>0? true:false;
var forDealer = false;// TCC經銷商不獨立登入頁，依DB設定決定
//var forDealer = window.location.href.indexOf(DEALER_LOGIN_PAGE)>0? true:false;
var state = utils.genUUID();

$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    w3.includeHTML(start);
    utils.detectCapsLock("psw", "msgCapsLock");
});

function start(){
    console.log(utils.formatDT(new Date()));
    
    _needCheckLogin = false;
    // 訊息顯示區
    $('#_message').puimessages();
    $('#_growl').puigrowl({life:5000});
    
    //utils.setSession(FIELD_LANG, "zh-CN");// EC1.5 強制簡體
    //utils.setSession(FIELD_LANG, "en-US");// 英文版
    utils.getLanguage(utils.getSession(FIELD_LANG));
    loadI18nProperties();
    // Title
    var sysName = utils.getSysName(forAdmin, forDealer);
    document.title = sysName;
    var remeberUser = utils.getCookie(getRememberMeKey());
    $("#account").val(isEmpty(remeberUser)?"test01":remeberUser);// for test
    //$("#psw").val("abcd1234");// for test
    
    // 登入
    $("#send").click(function(e) {
        onClickLogin(e);
    });   
    $("#psw").keydown(function (e) {
        if (e.keyCode === 13 /*Enter*/) {
            onClickLogin(e);
            e.preventDefault();
        }
    });
    // 忘記密碼
    $("#askPsw").click(function(e) {
        resetPassword();
    });
    
    hideLoginResultMsg();
    utils.hideOverlay("startOverlay", "H"); 
    
    checkInternalLogin();
    
    console.log(utils.formatDT(new Date()));
}
// 忘記密碼
function resetPassword(){
    var account = $("#account").val();
    if( isEmpty(account) ){
        showLoginResultMsg(utils.i18n("in.noaccount.err"));
        return;
    }
    if( confirm(utils.i18n("pwd.reset.msg")) ){
        var formData = {"loginAccount": account.trim()};
        
        utils.postData(PWD_RESET_URL, formData, false, afterResetPassword);
    }
}

function afterResetPassword(response, formData){
    showLoginResultMsg(utils.i18n("reset.pwd.send"));
}
// 登入
function onClickLogin(e){
    $("#send").attr("disabled", true);
    e.preventDefault();
    utils.clearSession();
    hideLoginResultMsg();
    
    if( TEST_DATA ){
        window.location.href=SELLER_HOME_PAGE;
        return;
    }
    
    var account = $("#account").val();
    var psw = $("#psw").val();
    if( isEmpty(account) || isEmpty(psw) ){
        showLoginResultMsg(utils.i18n("in.err")+"!");
        return;
    }
    // encrypt password
    var info = utils.getBrowserInfo();
    passPhrase = info.userAgent;
    iterationCount = (Math.floor(Math.random() * 10)+1)*50;
    var ciphertext = encryptPsw(psw);
    
    utils.showOverlay("startOverlay", "H", utils.i18n("logining")); 
    var formData = {
        "loginAccount":account, 
        "pwd":ciphertext, 
        "salt":salt, 
        "iv":iv, 
        "passPhrase":passPhrase, 
        "iterationCount":iterationCount, 
        "forAdmin":forAdmin
        //,"forDealer":forDealer
    };
    doLogin(LOGIN_URL, formData);
}
// 加密
function encryptPsw(plainText){
    var aes = new AesUtil(keySize, iterationCount);
    //var key = aes.generateKey(salt, passPhrase);
    var encrypted = aes.encrypt(salt, iv, passPhrase, plainText);
    
    return encrypted;
}

function doLogin(restUrl, formData){
    utils.clearSession();
    utils.removeCookieForNewTab();
    
    formData = isNA(formData)?{}:formData;
    if( TEST_DATA ){
        return utils.fetchData(restUrl, false, afterLogin, null);
    }
    utils.postData(restUrl, formData, false, afterLogin, loginFail);
}

// for ec-amin link to ec-seller
function checkInternalLogin(){
    var loginAccount = utils.getUrlParameter(FIELD_ACCOUNT);
    var from = utils.getUrlParameter(FIELD_FROM);
    var token = utils.getUrlParameter(FIELD_TOKEN);
    // var language = utils.getUrlParameter(FIELD_LANG);
    
    if( from===FROM_INTERNAL && !isNA(loginAccount) && !isNA(token) ){
        var restUrl = CHECK_IT_LOGIN_URL;
        var formData = {"token":token, "loginAccount":loginAccount};
        doLogin(restUrl, formData);
    }
}

function afterLogin(response){
    console.log("afterLogin response = \n", response);
    utils.hideOverlay("startOverlay", "H"); 
    if( isNA(response) || isNA(response[FIELD_TOKEN]) || isEmpty(response[FIELD_TOKEN]) ){
        $("#send").attr("disabled", false);
        if( forAdmin ){
            showLoginResultMsg(utils.i18n("login.fail.admin"));
        }else if( forDealer ){
            showLoginResultMsg(utils.i18n("login.fail.dealer"));
        }else{
            showLoginResultMsg(utils.i18n("login.fail.seller"));
        }
    }else{
        rememberMe(response[FIELD_ACCOUNT]);
        
        var authRes = {};
        authRes[FIELD_ACCOUNT] = response[FIELD_ACCOUNT];
        authRes[FIELD_TOKEN] = response[FIELD_TOKEN];
        authRes[FIELD_ADMIN] = forAdmin?response[FIELD_ADMIN]:false;
        //authRes[FIELD_DEALER] = forDealer?response[FIELD_DEALER]:false;
        authRes[FIELD_DEALER] = response[FIELD_DEALER];
        authRes[FIELD_ST_OWNER] = response[FIELD_ST_OWNER];
        authRes[FIELD_ST_FIUSER] = response[FIELD_ST_FIUSER];
        authRes[FIELD_STOREID] = response[FIELD_STOREID];
        keepAuthResultToSession(authRes);// 儲存授權結果
        utils.setSession(FIELD_FROM, FROM_WEBPAGE);
        utils.setSession(FIELD_LANG, utils.getLangCode());
        
        utils.keepCookieForNewTab();// for open new tab
        utils.keepState(state);
        
        window.location.href = forAdmin?ADMIN_HOME_PAGE:SELLER_HOME_PAGE;
    }
}

function loginFail(response){
    $("#send").attr("disabled", false);
    utils.hideOverlay("startOverlay", "H"); 
    showLoginResultMsg(forAdmin?utils.i18n("login.fail.admin"):utils.i18n("login.fail.seller"));
}

function getRememberMeKey(){
    return FIELD_ACCOUNT+(forAdmin?".admin":forDealer?".dealer":".seller");
}
function rememberMe(loginAccount){ 
    if( getCheckboxByName("remember") ){
        utils.setCookie(getRememberMeKey(), loginAccount, 7);// 7 days
    }else{
        utils.removeCookie(getRememberMeKey());
    }
}

function hideLoginResultMsg(){
    $("#loginResult").hide();
}
function showLoginResultMsg(msg, info){
    if( !isNA(info) && info ){
        html = '<i class="fa fa-info-circle" ></i>&nbsp;' + msg;
    }else{
        html = '<i class="fa fa-exclamation-triangle" ></i>&nbsp;' + msg;
    }
    $("#loginResult").html(html);
    $("#loginResult").show();
}