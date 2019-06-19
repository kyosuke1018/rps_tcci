/* global FIELD_LANG, w3, MEM_REG_URL, MEM_VERIFY_URL, utils */
var loginAccount = "";

$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    w3.includeHTML(start);
});

function start(){
    console.log(utils.formatDT(new Date()));
    _needCheckLogin = false;
    // 訊息顯示區
    $('#_message').puimessages();
    $('#_growl').puigrowl({life:5000});
    
    utils.getLanguage(utils.getSession(FIELD_LANG));

    loadI18nProperties();
    
    // 登入
    $("#register").click(function(e) {
        onClickRegister(e);
    });   
    // 驗證
    $("#verify").click(function(e) {
        onClickVerify(e);
    });

    hideRegisterResultMsg();
    hideVerifyResultMsg();
    utils.hideOverlay("startOverlay", "H"); 
    
    console.log(utils.formatDT(new Date()));
}

// 登入
function onClickRegister(e){
    console.log("onClickRegister ...");
    $("#register").attr("disabled", true);
    e.preventDefault();

    loginAccount = $('#loginAccount').val().trim();
    var name = $('#name').val().trim();
    var email = $('#email').val().trim();
    
    if( loginAccount==="" || name==="" || email==="" ){
        $("#register").attr("disabled", false);
        alert("不可輸入空白!");
        return;
    }

    var restUrl = MEM_REG_URL;
    var formData = {
        "loginAccount":loginAccount,
        "name":name,
        "email":email
    };
    utils.postData(restUrl, formData, false, afterRegisterSuccess, afterRegisterFail, "verify");
}

function afterRegisterSuccess(response){
    utils.addMessage("info", utils.i18n("hit.msg"), utils.i18n("after.register.success"));
    $("#register").attr("disabled", false);
}
function afterRegisterFail(response){
    showRegisterResultMsg(utils.i18n("after.register.fail"));
    $("#register").attr("disabled", false);
}

function hideRegisterResultMsg(){
    $("#registerResult").hide();
}
function showRegisterResultMsg(msg, info){
    if( !isNA(info) && info ){
        html = '<i class="fa fa-info-circle" ></i>&nbsp;' + msg;
    }else{
        html = '<i class="fa fa-exclamation-triangle" ></i>&nbsp;' + msg;
    }
    $("#registerResult").html(html);
    $("#registerResult").show();
}


// 驗證
function onClickVerify(e){
    console.log("onClickVerify ...");
    $("#verify").attr("disabled", true);
    e.preventDefault();

    var loginAccount = $('#loginAccount').val().trim();
    var verifyCode = $('#verifyCode').val().trim();

    var restUrl = MEM_VERIFY_URL;
    var formData = {
        "loginAccount":loginAccount,
        "verifyCode":verifyCode
    };
    utils.postData(restUrl, formData, false, afterVerifySuccess, afterVerifyFail, "verify");
}
function afterVerifySuccess(response){
    utils.addMessage("info", utils.i18n("hit.msg"), utils.i18n("after.verify.success"));
    $("#verify").attr("disabled", false);
}
function afterVerifyFail(response){
    showVerifyResultMsg(utils.i18n("after.verify.fail"));
    $("#verify").attr("disabled", false);
}

function hideVerifyResultMsg(){
    $("#verifyResult").hide();
}
function showVerifyResultMsg(msg, info){
    var html = "";
    if( !isNA(info) && info ){
        html = '<i class="fa fa-info-circle" ></i>&nbsp;' + msg;
    }else{
        html = '<i class="fa fa-exclamation-triangle" ></i>&nbsp;' + msg;
    }
    $("#verifyResult").html(html);
    $("#verifyResult").show();
}
