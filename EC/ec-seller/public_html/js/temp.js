/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global utils, w3 */

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
    //initLayout(false, true);
    //renderPanels();

    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#selectedItemContent").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    //$('#fsBasic').puifieldset({toggleable: true});
}

