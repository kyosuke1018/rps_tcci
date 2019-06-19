/* 
 * dependencys : primeui.js, com/menu.js
 */
/* global SYS_NAME, SELLER_HOME_PAGE, ADMIN_HOME_PAGE, LOGIN_PAGE, ADMIN_LOGIN_PAGE, FIELD_ACCOUNT, _language, LANGS, FIELD_LANG, URL_NOW, _menus, I18N_PROPS_NAME, I18N_PATH, FIELD_FROM, FROM_ANDROID, selectAll, DEF_LANG, GRID_COLS, FIELD_DEALER, DEALER_LOGIN_PAGE, TCC_ENABLED, FIELD_ST_FIUSER, FIELD_ST_OWNER, utils */
function initLayout(){
    var admin = arguments[0];// for 系統管理
    var showHeaderMenu = arguments[1];// 顯示功能選單
    var showSticky = arguments[2];// 顯示選取項目標頭
    // 特殊身分
    _tccDealer = ("true"===utils.getSession(FIELD_DEALER));
    _storeOwner = ("true"===utils.getSession(FIELD_ST_OWNER));
    _fiUser = ("true"===utils.getSession(FIELD_ST_FIUSER));
    // 語系
    var langCode = isNA(utils.getSession(FIELD_LANG))?DEF_LANG:utils.getSession(FIELD_LANG);
    changeStyleByLang(langCode);// 依語系變更 css
    // i18n 
    loadI18nProperties();
    loadDatepickerI18n(langCode);
    // Overlay
    genOverlayContent();
    
    if( !TCC_ENABLED ){// EC2.0 only
        hideTccOnly();
    }
    if( admin ){
        hideSellerOnly();
        hideDealerOnly();
    } else {
        hideAdminOnly();
        if( _tccDealer ){
            hideNotDealer();
        }else{
            hideDealerOnly();
        }
    }
    
    // Title
    var sysName = utils.getSysName(admin, _tccDealer);
    var loginPage = utils.getLoginPage(admin, _tccDealer);
    document.title = sysName;
    $('.sysName').html(sysName);
    $('a#home').attr("href", admin?ADMIN_HOME_PAGE:SELLER_HOME_PAGE);
    // logout
    $('a#logoutLink1').attr("href", loginPage);
    $('a#logoutLink2').attr("href", loginPage);
    $('a#logoutLink1').click(function(){
        utils.clearSession();
    });
    $('a#logoutLink2').click(function(){
        utils.clearSession();
    });
   
    // login user
    var loginAccount = utils.getSession(FIELD_ACCOUNT);
    var userLabel = admin?'<i class="fa fa-user-circle-o" ></i>&nbsp;'+loginAccount+'&nbsp;&nbsp;'
                    :'<a href="my.html" title="'+utils.i18n("mt.my.profile")+'" ><i class="fa fa-user-circle-o" ></i>&nbsp;'+loginAccount+'</a>&nbsp;&nbsp;';
    $('#_loginUser').html(userLabel);
    if( utils.getSession(FIELD_FROM)===FROM_ANDROID ){
        $('.logoutLink').hide();// APP & WEB 避免都出現"登出"
        $('._switchLang').hide();// APP 傳入語言別
    }
    
    // switch language
    $('#_switchLang').puiselectbutton({
        choices: utils.getLanguageOps(_tccDealer),
        change: function(event, data) {
            console.log(data.index, LANGS[data.index].code);
            _language = LANGS[data.index];
            utils.setSession(FIELD_LANG, _language.code);
            console.log("change to ", _language.code);
            window.location.reload();
        }
    });
    $('#_switchLang').puiselectbutton('selectOption', isNA(_language)?0:_language.value);

    // 訊息顯示區
    $('#_message').puimessages();
    $('#_growl').puigrowl({life:5000});

    // Header & Menu & Function Name
    if( showHeaderMenu ){
        buildHeaderMenu(admin, langCode, "_menu");
        // sticky selected item nav bar 
        if( isNA(showSticky) || showSticky ){// 預設使用
            console.log("showSticky = "+showSticky);
            window.onscroll = function(){ stickySelectedItemNavBar(); };
        }
    }

    /* 按下GoTop按鈕時的事件 */
    $('#_divGoToTop').click(function(){
        utils.scrollToTop();/* 返回到最頂上 */
        return false;
    });
    /* 偵測卷軸滑動時，往下滑超過80px就讓GoTop按鈕出現 */
    $(window).scroll(function() {
        showGoToTop();
    });
    showGoToTop();
}

function showGoToTop(){
    if ( $(this).scrollTop() > 80){
        $('#_divGoToTop').fadeIn();
    } else {
        $('#_divGoToTop').fadeOut();
    }
}

function changeStyleByLang(langCode){
    _menuWidth = (langCode==="zh-TW" || langCode==="zh-CN")? 180:300;// 子選單寬度
}

//<editor-fold defaultstate="collapsed" desc="for Operation Function Name">
function afterLoadMenu(){
    _func = findMenuItemByUrl(_menus, URL_NOW);
    //console.log("func = \n", _func);
    var label = utils.i18n(_func.code);
    label = isEmpty(label)?_func.name:label;
    showFunctionName(label);
}

function showFunctionName(funcName){
    var pagename = isNA(funcName)?"":funcName;
    document.title = document.title + (isEmpty(pagename)?"":" - " + pagename);
    // 主 Penal (功能名稱)
    $('#plMain').attr("title", pagename).attr("style", "width:100%;margin-bottom:20px");
    $('#plMain').puipanel();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for sticky selected item nav bar ">
// sticky selected item nav bar 
function stickySelectedItemNavBar() {
    var navbar = document.getElementById("selectedItemHeader");
    var stickyFlag = document.getElementById("stickyFlag");
    if( isNA(navbar) ){
        console.assert("no selectedItemHeader");
        return;
    }

    var sticky = isNA(stickyFlag)||isNA(stickyFlag.offsetTop)?300:stickyFlag.offsetTop;

    if( window.pageYOffset >= sticky ) {
        navbar.classList.add("sticky");
    } else {
        navbar.classList.remove("sticky");
    }
}

function resetSelectedItemNavBar() {
    var navbar = document.getElementById("selectedItemHeader");
    navbar.classList.remove("sticky");
}

function genSelectedItemTitle(title){
    // 選取項目
    var html = "<i class='fa fa-edit' ></i>&nbsp;" + utils.i18n("sel.item")
             + "：<span class='selectedItemName' >"+ utils.safePrint(title) +"</span>&nbsp;";
    $('#selectItem').html(html);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Overlay">
function genOverlayContent(){
    var overlayHtml = "";
    //overlayHtml +=  '<div id="startOverlay" class="startOverlay" >'
    //            +  '    <div class="overlay-text"><i class="fa fa-spinner fa-pulse fa-fw fa-3x"></i></div>'
    //            +  '</div>'
    overlayHtml +=  '<div id="errorOverlay" class="errorOverlay" >'
                +  '    <div class="overlay-text">'
                +  '        <i class="fa fa-exclamation-triangle" ></i>' + utils.i18n("load.fail")
                +  '        <span id="errorMsg" >&nbsp;</span>'
                +  '        <a class="reload" href="javascript:location.reload();" >'
                +  '            <i class="fa fa-refresh" ></i>&nbsp;重試&nbsp;<i class="fa fa-refresh" ></i>'
                +  '        </a>'
                +  '    </div>'
                +  '</div>';
    $('#_overlay').html(overlayHtml);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for filed content">
/**
 * Boolean 值顯示
 * @param {type} data
 * @param {type} key
 * @returns {String}
 */
function genContentBoolean(data, key){
    if( isNA(data) ){
        return "NA";
    }
    var html = (!isNA(data[key]) && data[key])?"<i class='fa fa-check' ></i>":"<i class='fa fa-times' ></i>";
    return '<div style="width:100%;text-align:center;' + (data[key]?'color:green;':'color:red;') + '" >' + html + '</div>';
}

function genContentDisabled(data){// 因標題顯示有效
    if( isNA(data["disabled"]) || isNA(data["disabled"]) ){
        return "NA";
    }
    var html = (!data["disabled"])?"<i class='fa fa-check' ></i>":"<i class='fa fa-times' ></i>";
    return '<div style="width:100%;text-align:center;' + (!data["disabled"]?'color:green;':'color:red;') + '" >' + html + '</div>';
}

function genContentOnline(data){
    if( isNA(data["onlined"]) || isNA(data["onlined"]) ){
        return "NA";
    }
    var html = (data["onlined"])?"<i class='fa fa-check' ></i>":"<i class='fa fa-times' ></i>";
    return '<div style="width:100%;text-align:center;' + (data["onlined"]?'color:green;':'color:red;') + '" >' + html + '</div>';
}

function genContentHtml(data, key){
    if( isNA(data) || isNA(data[key]) ){
        return "NA";
    }
    return (data[key])?'<div class="htmlContent" >'+data[key]+'</div>':'';
}

// get time content in datatable
function genCreateTimeContent(data){
    return formatDateTimeStr(data.createtime);
}
function genModifyTimeContent(data){
    return formatDateTimeStr(data.modifytime);
}

function genCurNameContent(currency){
    if( isNA(currency) ){
        return "";
    }
    if( currency.toUpperCase()==="RMB"  ){
        return utils.i18n("cur.cn");
    }else if( currency.toUpperCase()==="TWD"  ){
        return utils.i18n("cur.tw");
    }else if( currency.toUpperCase()==="USD"  ){
        return utils.i18n("cur.us");
    }
    
    return "";
}


function renderOrderRate(rate){
    return isNA(rate)?utils.i18n("no.rate"):(rate>0?utils.i18n("pos.rate"):utils.i18n("nag.rate"));
}

function renderUploadFileName(viewSelector, fileSelector){
    if( !isNA($(fileSelector)) && !isNA($(fileSelector)[0]) && !isNA($(fileSelector)[0].files[0]) ){
        $(viewSelector).html($(fileSelector)[0].files[0].name);
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for list content">
// 產生簡易文字列表
function renderSimpleTextList(selector, values, labelField){
    var html = "";
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            var label = isNA(labelField)?values[i]:values[i][labelField];
            html += "● "+ label + "<br/>";
        }
    }
    $(selector).html(html);
}

// 產生簡易文字串列
function renderSimpleListStr(selector, values, delimiter, lineNum){
    var introHtml = "";
    delimiter = isNA(delimiter)? ", ":delimiter;
    if( !utils.isEmptyAry(values) ){
        for(var i=0; i<values.length; i++){
            introHtml += (isNA(values[i].label)? values[i] : values[i].label) + (i<values.length-1?delimiter:"");
            if( !isNA(lineNum) && (i+1)%lineNum===0 && i!==values.length ){
                introHtml += "<br/>";
            }
        }
    }else{
        introHtml = utils.i18n("msg.noset");
    }
    $(selector).html(introHtml);
}

// 產生簡易文字維護列表
function renderSimpleTextEditList(selector, values, deleteFuncName, labelField){
    var html = '<ul class="w3-ul">';
    if( !isNA(values) ){
        //values.sort();
        for(var i=0; i<values.length; i++){
            var label = isNA(labelField)?values[i]:values[i][labelField];
            html += '<li class="w3-padding-small w3-hover-pale-blue">';
            if( !isNA(deleteFuncName) ){
                html += '<button onclick="' + deleteFuncName + '('+ i +')" title="'+utils.i18n("remove")+'" class="w3-btn w3-teal" ><i class="fa fa-trash"></i></button>';
            }
            html += '&nbsp;&nbsp;' + label + '</li>';
        }
    }
    html += "</ul>";
    $(selector).html(html);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for search result">
// 查詢結果筆數
function renderSearchResultNum(selector, totalRows, showSelectCheckbox, showExportBtn, expBtnId){
    var html = "<span class='spanSearchResult' ><i class='fa fa-search' ></i>&nbsp;"+utils.i18nWP("total.rows", totalRows);
    if( utils.isTrue(showSelectCheckbox) && totalRows>0 ){// 全選/全不選
        html += "<span id='_spanSelectAll' style='margin-left:20px;' ><input id='_selectAll' name='_selectAll' type='checkbox' />"+utils.i18n("fs.sel.all")+"</span>";
    }
    if( utils.isTrue(showExportBtn) && totalRows>0 ){
        expBtnId = isNA(expBtnId)?"btnExpResult":expBtnId;
        html += "<button id='"+expBtnId+"' type='button' style='margin-left:20px;' >"+utils.i18n("export.search.result")+"</button>";
    }
    
    html += "</span>";
    
    if( utils.isSmallScreen() ){
        html += "<hr/>";
    }
    $(selector).html(html);
    
    if( utils.isTrue(showSelectCheckbox) && totalRows>0 ){// 全選/全不選
        createCheckbox("#_selectAll", selectAll);
    }
}
function hideSearchResult(selector){
    $(selector).hide();
}
// for 需 by 頁，動態決定是否顯示
function displaySelectAll(show){
    if( show ){
        $("#_spanSelectAll").show();
    }else{
        $("#_spanSelectAll").hide();
    }
}
function clearSelectAll(){
    setCheckbox("#_selectAll", false);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for images display">
function genNoDataImg(){
    return "<div class='full tac vam' ><img src='img/NoData.png' width='55%' /><br/>" 
            + utils.i18n("txt.MSG_NOT_DATA_NOW") 
            + "</div>";
}

function createImgLightBox(selector, urlO, urlS, title, width, height){
    //idth = isNA(width)?"100px":width;
    //height = isNA(height)?"100px":height;
    var widthHtml = isNA(width)? "":" width='"+width+"' ";
    var heightHtml = isNA(height)? "":" height='"+height+"' ";
    
    var id = utils.genUUID();
    var imghtml = '<span id="'+id+'">'
                    +'<a href="'+utils.safePrint(urlO)+'" title="'+utils.safePrint(title)+'" >'
                    +'<img src="'+utils.safePrint(urlS)+'" ';
    imghtml = imghtml + widthHtml;
    imghtml = imghtml + heightHtml;
    imghtml = imghtml + '/>'
            +'</a>'
            +'</span>';
    $(selector).html(imghtml);
    $('#'+id).puilightbox();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for multi objects show or hide">
// seller, admin, tcc, dealer, notDealer, storeOwner, fiUser, notTccPrd
function hideSellerOnly(){
    $(".seller").hide();
    /*$(".seller").each(function(){
        if( !isNA($(this).hide) ){
            $(this).hide();
        }
    });*/
}

function hideAdminOnly(){
    $(".admin").hide();
}

function hideTccOnly(){
    $(".tcc").hide();
}

function hideDealerOnly(){
    $(".dealer").hide();
}

function hideNotDealer(){
    $(".notDealer").hide();
}

function hideStoreOwnerOnly(){
    $(".storeOwner").hide();
}

function hideFiUserOnly(){
    $(".fiUser").hide();
}

function showNotTccPrd(){
    $(".notTccPrd").show();
}

function hideNotTccPrd(){
    $(".notTccPrd").hide();
}
//</editor-fold>
