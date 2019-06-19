/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_HOME, ADMIN_LOGIN_PAGE, LOGIN_PAGE, DEALER_LOGIN_PAGE, utils */
$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    console.log(utils.formatDT(new Date()));
    
    // 簡易網址導頁
    var loginPage = null;
    if( window.location.href.indexOf("/seller.html")>0 || window.location.href.indexOf("/index20.html")>0 ){
        loginPage=LOGIN_PAGE;
    }else if( window.location.href.indexOf("/dealer.html")>0 ){
        loginPage=DEALER_LOGIN_PAGE;
    }else if( window.location.href.indexOf("/admin.html")>0 ){
        loginPage=ADMIN_LOGIN_PAGE;
    }
    if( !isNA(loginPage) ){
        window.location.href=loginPage;
    }

    // 導頁按鈕
    $("#btnEC20").click(function(){
        window.location.href=LOGIN_PAGE;
    });
    //$("#btnEC15").click(function(){
    //    window.location.href=DEALER_LOGIN_PAGE;
    //});
    $("#btnAdmin").click(function(){
        window.location.href=ADMIN_LOGIN_PAGE;
    });
    
    utils.hideOverlay("startOverlay", "H"); 

    console.log(utils.formatDT(new Date()));
});
