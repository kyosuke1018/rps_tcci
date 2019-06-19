/*
 * dependencys : jQuery, tcc-utils.js
 */
/* global MENU_URL, MSG_NOT_DATA, _asyncCompleteFlags, ADMIN_MENU_URL, FIELD_TOKEN, FIELD_ACCOUNT, FIELD_TABID, FIELD_STOREID, FIELD_ADMIN, _tabId, FIELD_LANG, FIELD_FROM, TCC_ENABLED, _menuWidth, _tccDealer, FIELD_ST_OWNER, FIELD_ST_FIUSER, utils */
//var _menuWidth = 160;// 子選單寬度

function findMenuItemByUrl(menus, url){
    for(var i=0; i<menus.length; i++){
        var f = menus[i];
        if( f.leaf && f.url!=='#' ){
            if( url.indexOf("/"+f.url)>0 ){
                return f;
            }
        }else{
            var submenus = utils.safeList(f.menus);
            var node = findMenuItemByUrl(submenus, url);
            if( !isNA(node) ){
                return node;
            }
        }
    }
    return null;
}

function buildHeaderMenu(admin, langCode, ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    // 主選單開啟按鈕
    var collapsed = true;
    $(".nav-bar > .menuLink").click(function(){// collapse & extend
        $("header nav").animate(collapsed? {'left' : '0'}:{'left' : '-100%'});
        collapsed = !collapsed;
        return collapsed;
    });
    $(".nav-bar").click(function(){// collapse only
        if( !collapsed ){
            $("header nav").animate({'left' : '-100%'});
            collapsed = !collapsed;
        }
        return collapsed;
    });

    // 選單建置 : JSON to Menu
    var retry = false;
    var restUrl = admin?ADMIN_MENU_URL.replace("LANG_CODE", langCode):MENU_URL.replace("LANG_CODE", langCode);
    utils.fetchData(restUrl, retry, renderMenu, null, ajaxKey);
}

function renderMenu(response, ajaxKey){
    if( isNA(response) || isNA(response.menus) ){
        logI("renderMenu response is NA ...", response);
        return;
    }

    // 選單建置 : JSON to List
    var ul = $('ul.navigation');
    var menuHtml = "";
    _menus = utils.safeList(response.menus);
    // console.log("menus = \n", _menus);
    for(var i=0; i<_menus.length; i++){
        var f = _menus[i];
        if( isNA(f.show) || f.show ){
            if( !TCC_ENABLED 
            || isNA(f.flag)
            || (_tccDealer && f.flag==="ec1.5")
            || (!_tccDealer && f.flag==="ec2.0")
            ){
                if( f.leaf ){
                    menuHtml += menuItem(f);
                }else{
                    menuHtml += subMenu(f, _menuWidth);
                }
            }
        }
    }

    ul.append(menuHtml);
    
    // 子選單開闔 (sub-menu 產生後宣告)
    $('.sub-menu').click(function(){
        $(this).children('.children').slideToggle();
    });
    
    afterLoadMenu();
    _asyncCompleteFlags[ajaxKey]=true;
}

// UI Components
var subMenu = function(item, width){
    var html = "";
    html += '<li class="sub-menu">\n';
    var iconClass = isNA(item.iconClass)?"fa fa-cog":item.iconClass;
    var url = genMenuUrl(item.url);
    var label = utils.i18n(item.code);
    label = isEmpty(label)?item.name:label;
    html += utils.indents(1) + '<a href="' + url + '"><i class="'+ iconClass +'" aria-hidden="true"></i>' + label + '&nbsp;<i class="fa fa-angle-down"></i></a>\n';
    // set width by lang
    //if( utils.isSmallScreen() ){
    //if( utils.getDomWidth()<900 ){    
        html += utils.indents(1) + '<ul class="children" >\n';
    //}else{
    //    html += utils.indents(1) + '<ul class="children" style="width:'+width+'px;" >\n';
    //}
    
    var submenus = utils.safeList(item.menus);
    for(var j=0; j<submenus.length; j++){
        var g = submenus[j];
        if( isNA(g.show) || g.show ){
            var display = false;
            if( isNA(g.show) || g.show ){
                if( !TCC_ENABLED 
                || isNA(g.flag)
                || (_tccDealer && g.flag==="ec1.5")
                || (!_tccDealer && g.flag==="ec2.0")
                ){
                    html += utils.indents(2) + menuItem(g);
                }
            }
        }
    }
    html += utils.indents(1) + '</ul>\n';
    html += '</li>\n';
    
    return html;
};

var menuItem = function(item){
    var iconClass = isNA(item.iconClass)?"fa fa-cog":item.iconClass;
    var url = genMenuUrl(item.url);
    var label = utils.i18n(item.code);
    label = isEmpty(label)?item.name:label;
    return '<li><a href="' + url + '"><i class="'+ iconClass +'" aria-hidden="true"></i> ' + label + '</a></li>\n';
};

// 考慮新開 TAB 問題
var genMenuUrl = function(url){
    var tabId = _tabId;
    var loginAccount = getSession(FIELD_ACCOUNT);
    var storeId = getSession(FIELD_STOREID);
    var langCode = getSession(FIELD_LANG);   
    var from = getSession(FIELD_FROM);
    var storeOwner = getSession(FIELD_ST_OWNER);
    var fiUser = getSession(FIELD_ST_FIUSER);
    
    if( !isEmpty(tabId) && !isEmpty(loginAccount) ){// for open new tab
        url = utils.addUrlQueryParam(url, FIELD_TABID, encodeURI(tabId));
        url = utils.addUrlQueryParam(url, FIELD_ACCOUNT, encodeURI(loginAccount));
        url = utils.addUrlQueryParam(url, FIELD_STOREID, encodeURI(storeId));
        url = utils.addUrlQueryParam(url, FIELD_LANG, encodeURI(langCode));
        url = utils.addUrlQueryParam(url, FIELD_FROM, encodeURI(from));
        url = utils.addUrlQueryParam(url, FIELD_ST_OWNER, encodeURI(storeOwner));
        url = utils.addUrlQueryParam(url, FIELD_ST_FIUSER, encodeURI(fiUser));
    }
    url = utils.addUrlQueryParam(url, "t", encodeURI((new Date()).getTime()));
    
    return url;
};