/**
 * 控制是否使用中
 * @type Number
 */
var CHECK_ACTION_INTERVAL = 10 * 60 * 1000;// 10 min.
var MAX_ACTION_INTERVAL = 3 * 60 * 60 * 1000;// 3 hr.
//var CHECK_ACTION_INTERVAL = 10 * 1000;// 10 sec. // for test
//var MAX_ACTION_INTERVAL = 3 * 10 * 1000;// 30 sec. // for test
var keepAliveURL = "/faces/javax.faces.resource/check.png?ln=images";// add context path in generalPageTemplate.xhtml
var lastActionTime = 0;
var checkActionTimer = 0;

function startCheckAction(){
    //alert('startCheckAction');
    if( !checkActionTimer ){
        checkActionTimer = setInterval('checkAction();', CHECK_ACTION_INTERVAL);
    }
}
function stopCheckAction(){
    //alert('stopCheckAction');
    if( checkActionTimer ){
        clearInterval(checkActionTimer);
    }
}
function onAction(){
    // reset init
    lastActionTime = new Date().getTime();
}
function checkAction(){
    var now = new Date().getTime();
    var period = now - lastActionTime;
    if( period >= CHECK_ACTION_INTERVAL ){
        if( period < MAX_ACTION_INTERVAL ){
            //alert("sendKeepAliveRequest period = "+period);
            sendKeepAliveRequest();
        }else{
            //alert("stopCheckAction period = "+period);
            stopCheckAction();
        }
    }
}
function sendKeepAliveRequest(){
    var imgObj = new Image();
    imgObj.src = keepAliveURL;
}

/**
 * check IE Version
 * @param {type} baseline
 * @returns {undefined}
 */
function checkIEVersionAbove(baseline) {
    if ($.browser.msie && parseInt($.browser.version, 10) < baseline) {
        //var agentStr = navigator.userAgent;
        var msg;
        //if (agentStr.indexOf("Trident") > -1) {
        //    msg = "您的IE瀏覽器現在是相容性檢視狀態，請於主選單的 '工具'=>'相容性檢視設定' 中移除 'taiwancement.com'!";
        //} else {
        msg = "本系統不支援舊版IE瀏覽器，請更新IE版本至少 " + baseline + " 以上!";
        //}
        msg += "\n若有任何問題請聯絡台訊客服: service@tcci.com.tw";
        alert(msg);
    }
}

/**
 * 對話框對齊頂端
 * @param {type} dialogId
 */
function dialogToTop(dialogId) {
    var dialog = $('#'+dialogId);
    var topY = $(document).scrollTop();
    dialog.offset({ top: topY });
}

/**
 * 回到頁面頂端
 */
function gotoTop() {
    window.scrollTo(0, 0);
}

/**
 * key code 
 */
var KEY_UP = 38;
var KEY_DOWN = 40;

/**
 * 依上下鍵在移動各 TextBox 間移動
 * @param {type} tbId : TextBox ID
 */
function moveCursorInTextbox(tbId){
    var tbSelector = "input[id$=':"+tbId+"']:enabled";

    $( document ).on("keydown", tbSelector, function(e) { // jQuery 1.7+
    //$(tbSelector).keydown(function(e){
        if( e.target.type !== "text" ){// 只針對 TextBox
            return;
        }
        
        var len = $(tbSelector).length;// 個數
        if( len <= 1 ){
            return;
        }        

        var id = $(tbSelector).index($(this));// 目前序號
        
        var nextId = 0; // 目的序號
        var ignore = true; // 是否忽略
        if( e.which === KEY_UP ){// up
            nextId = (id<=0)? 0:id-1;
            ignore = false;
        }else if( e.which === KEY_DOWN ){// down
            nextId = (id>=len-1)? len-1:id+1;
            ignore = false;
        }

        if( !ignore ){// 移動焦點至 nextId
            $(tbSelector+":eq("+nextId+")").focus();
        }
    });
    
    // 取得焦點同時選取文字
    $( document ).on("focus", tbSelector, function(e) { // jQuery 1.7+
    //$(tbSelector).focus(function(){
        $(this).select().mouseup(function (e) { e.preventDefault(); });
    });
}

function textDelayChange(filterId, filterLastId, callback, delay) {
    $(filterId).bind('input keyup', function() {
        var $this = $(this);
        clearTimeout($this.data('timer'));
        $this.data('timer', setTimeout(function() {
            $this.removeData('timer');
            var userFilterLast = $(filterLastId).val();
            var userFilter = $.trim($(filterId).val());
            if (userFilterLast !== userFilter) {
                $(filterLastId).val(userFilter);
                callback();
            }
        }, delay));
    });
}

/**
 * 檢查 Primefaces Calendar 有無異動
 * 利用隱藏span包含原值
 * @param {type} id
 * <h:outputText id="calPhysicalFinishDate_ORI" value="#{projectController.projectVO.physicalFinishDate}" style="display:none" >
 *   <f:convertDateTime pattern="#{app.dateFormat}" />
 * </h:outputText>
 */
function checkCalendarModify(id){
    var tbSelector = "input[id$='"+id+"_input']";
    var tbSelectorORI = "span[id$='"+id+"_ORI']"; // 隱藏span包含原值
    
    if( $(tbSelector).length>0 && $(tbSelectorORI).length>0 ){
        if( $(tbSelector).length > 1 ){// 多筆
            for(i=0; i<$(tbSelector).length; i++){
                if( $(tbSelector+":eq("+i+")").val() !== $(tbSelectorORI+":eq("+i+")").text() ){
                    return false;
                }
            }
        }else{// 單筆
            return $(tbSelector).val() === $(tbSelectorORI).text();
        }
    }
    return true;
}

/**
 * 左右移動查詢結果 (寬度超過螢幕)
 */
var activeDataTableID = "dtResult";
function moveDatatable(toRight){
    var selectorStr = "div.ui-datatable-tablewrapper";// 所有 dataTable
    // var selectorStr = "div#"+activeDataTableID+" div.ui-datatable-tablewrapper";
    moveOneDatatable(selectorStr, toRight);
}
function moveOneDatatable(selectorStr, toRight){
    jQuery.each($(selectorStr), function(index, obj){
        var oriPos = $(obj).scrollLeft();
        var w = $(window).width() - 100;
        if( toRight ){
            $(obj).scrollLeft(oriPos + w);
        }else{
            $(obj).scrollLeft(oriPos - w);
        }
    });
}

/**
 * Resize datatable in chrome
 * @type Number
 */
var resizeDataTableOnstart = true;
var dataTableResizeWidthDiff = 100;
function doResize(){
    $.browser.chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());
    $.browser.firefox = /firefox/.test(navigator.userAgent.toLowerCase());
    if($.browser.chrome || $.browser.firefox){
        //var w = $("#dtResult_paginator_top").width();
        var w = $(window).width() - dataTableResizeWidthDiff;
        //alert(activeDataTableID);
        if( activeDataTableID==="" ){// 同時多個要 Resize 的 dataTable
            $(".ui-datatable-tablewrapper").css("width", w);// 所有 dataTable
        }else{
            $("div#"+activeDataTableID+" div.ui-datatable-tablewrapper").css("width", w);
        }
        $('body').css('overflow-x', 'hidden');
    }
}
  
/**
 * Delay to Resize
 */
var resize_timer = 0;
function doResizeLazy(delay){
    resize_timer = setTimeout("alert(resize_timer); doResize(); clearTimeout(resize_timer);", delay);
}

/**
 * 隨機字串
 * @param {type} len
 * @param {type} charSet
 * @returns {String.randomString}
 */
function randomString(len, charSet) {
    charSet = charSet || 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var randomString = '';
    for (var i = 0; i < len; i++) {
    	var randomPoz = Math.floor(Math.random() * charSet.length);
    	randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}

// for PF 5.X
function resizeFilterSelet(){
    $("select[id$=':filter']").css("min-width", "40px");
}

/* common script */
function trimInput(input) {
    input.value = $.trim(input.value);
}

function trimInputToLower(input) {
    input.value = $.trim(input.value).toLowerCase();
}

/**
 * Get url parameter jquery Or How to Get Query String Values In js
 * @param {type} sParam
 * @returns {getUrlParameter.sParameterName|Boolean}
 * ex. 
 *      var id = getUrlParameter('id');
 */
var getUrlParameter = function getUrlParameter(sParam) {
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

//<editor-fold defaultstate="collapsed" desc="for Window & Screen & Document & Position">
// scroll y to selector object
function scrollTo(selector, offset){
    console.log(selector);
    console.log($(selector));
    console.log($(selector).offset());
    var y = $(selector).offset().top;
    //y = y<60? y:y-60;
    y = isNA(offset)? y:y-offset;
    $('html,body').animate({scrollTop: y}, 'slow');
}

function scrollToTop(){
    $('html,body').animate({ scrollTop: 0 }, 'slow');/* 返回到最頂上 */
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
