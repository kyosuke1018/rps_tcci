/* global MSG_INPUT_ERR, MSG_NOT_SUPPORT, MSG_NOT_DATA */
var LANG = "C";
var MSG_INPUT_ERR = "輸入錯誤!";
var MSG_NOT_SUPPORT = "不支援此資料類型";
var MSG_NOT_DATA = "查無資料!";
var MSG_NOT_PLANTNUM = "查無蒐藏植物總數!";
var MSG_NO_RECORD_FOUND = "查無資料!";
var LABEL_FILE = "檔案";
var LABEL_DATE = "日期";
var LABEL_DOC = "文章";
var LABEL_COUNT_S = "共有 ";
var LABEL_COUNT_E = " 項結果";

/**
 * Global Constant
 * depend lang.js
 */
// [DEV] http://localhost:8080 ; [QAS] http://192.168.203.152:8080 ; 
// [PRD] http://www.csrcgroup.com, http://192.168.18.31
var NOW_URL = window.location.href;
var HOST_NAME = (NOW_URL.indexOf("://localhost")>=0)? "http://localhost:8080":"";
var REST_ROOT = "/csrcweb/resources";

// 首頁最新消息
var URL_HOME_NEWS = HOST_NAME+REST_ROOT+"/pubs/news?offset=0&rows=3&lang=";
// 網站文章
var URL_DOC_COUNT = HOST_NAME+REST_ROOT+"/pubs/countInType/";
var URL_DOC_LIST = HOST_NAME+REST_ROOT+"/pubs/pubsInType/";
var URL_DOC_VIEW = HOST_NAME+REST_ROOT+"/pubs/";
// 固定網頁
var URL_FIX_PAGE = HOST_NAME+REST_ROOT+"/pubs/F/";
//var DOC_VISIT_CODE = "F00001";// 參訪記錄

/**
 * Get url parameter, var id = getUrlParameter('id');
 * @param {type} sParam
 * @returns {getUrlParameter.sParameterName|Boolean}
 */
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
};

function escapeHtml(unsafe) {
    var safeStr = safePrint(unsafe);
    
    if( (typeof safeStr) === 'string' ){
        return safeStr.replace(/&/g, "&amp;")
                    .replace(/</g, "&lt;")
                    .replace(/>/g, "&gt;")
                    .replace(/"/g, "&quot;")
                    .replace(/'/g, "&#039;");
    }else{
        return safeStr;
    }
}

function safePrint(data){
    if( data===undefined || data === 'null' ){
        return '';
    }else{
        if( (typeof data) === 'string' ){
            data = data.replace(/<script/i, "&lt;script");
        }
        return data;
    }
}

function restErrorHandler(response){
    var msg = MSG_INPUT_ERR + ((response!==undefined)?" (status ="+response.status+")":"");
    hideLoading();// 隱藏 loading image
    alert(msg);
}

// 選取文章 (lang for html page)
function selectDoc(data){
    switch(data.dataType){
        case "H":// 自製網頁
            var url = "newsArticle.html?id=" + data.id + "&lang=" + getUrlParameter("lang");
            openDoc(url, data.openMethod);
            break;
        case "U":// 上傳檔案
            if( data.urls === undefined ){// for 單一檔案
                openDoc(data.url, data.openMethod);
            }
            break;
        case "L":// 連結
            openDoc(data.url, data.openMethod);
            break;
        default:
            alert(MSG_NOT_SUPPORT+"["+data.dataType+"]!");
    }
}

// 開啟文章 (一律開新視窗)
function openDoc(url, openMethod){
    if( openMethod==='N'){
        window.open(url, "_blank");
    }else{
        location.href = url;
    }
}

// ======== 以下 jquery dependency ===============
$(function(){
    // serialize form data
    $.fn.serializeFormJSON = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
});
    
/**
 * Simaple RESTful Get Data to Render Page
 * @param {type} restUrl
 * @param {type} retry
 * @returns {undefined}
 */
function fetchData(restUrl, retry){
    $.ajax({
        type: "GET",
        url: restUrl,
        dataType: "json",
        context: this,
        success: function(response) {
            if( response!==undefined ){
                renderPage(response);
            }else{
                alert(MSG_NOT_DATA);
            }
        },
        error: function (response) {
            if( !retry && response!==undefined && response.status===0 ){
                fetchData(restUrl, !retry);// status=0時，重試一次
            }else{
                restErrorHandler(response);
            }
        }
    });
}
     
// *** BEGIN: for 全文檢索 ****
// 全文檢索查詢按鈕 (override in fullindex_search.html)
var headerSearchClick = function(){
    var keyword = $('#headerSearchInput').val();
    if( keyword!=='undefined' && keyword!=='' ){
        keyword = encodeURI(keyword);
        window.location.href = "fullindex_search.html?keyword="+keyword;
    }else{
        alert(MSG_INPUT_ERR);
    }
};

// 全文檢索 enter keypress
var enterPressSearch = function(e, obj){
    var code = (e.keyCode ? e.keyCode : e.which);
    if(code === 13) { //Enter keycode
        event.preventDefault();
        headerSearchClick();
    }
};
// *** END: for 全文檢索 ****

// 列印區塊內容
function printBlock(divId){
    //var value = printlist.innerHTML;
    var value = $('#'+divId).html();
    var printPage = window.open("","printPage","");
    printPage.document.open();
    printPage.document.write("<HTML><head></head><BODY onload='window.print();window.close()'>");
    printPage.document.write("<PRE>");
    printPage.document.write(value);
    printPage.document.write("<P style='page-break-after:always'></P>");// 自動列印與分頁列印
    printPage.document.write("</PRE>");
    printPage.document.close("</BODY></HTML>");
}

// loading
function showLoadingBefore(selector){
    $(selector).before('<div style="width:100%; margin:50px 0 150px; text-align:center"><img class="loading" src="img/loading.svg"></div>');
}
function hideLoading(){
    $(".loading").css("display", "none").parent().css("margin", "0");
}
