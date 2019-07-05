//------------------------------ UI Ctrl (start) ------------------------------//
function getHeader(){
    var browser = checkBrowser();
	var temp = '';
    
    temp += '<div class="headerLogo">';
	temp += '	<a href="index.html">';
    temp += '       <span class="logoBorder">';
	temp += '		    <img class="logo" src="img/logo1.png" alt="台泥水泥">';
    temp +=	'       </span>';
    temp += '       <span class="logoMobi">';
    temp += '		    <img class="logo" src="img/logo.png" alt="台泥水泥">';
    temp +=	'       </span>';
    temp += '       <h1 class="name">台泥陽光採購平台</h1>';
	temp += '	</a>';
	temp +=	'</div>';
    
	//Desktop Menu
	temp += '<div class="deskMenuField">';
    temp += '   <div class="user">';
    if(!SESSION){
        temp += '   <a class="menu" href="userLogin.html">登 入</a>';        
        temp += '   <span class="divide">|</span>';
        temp += '   <a class="menu" href="userRegister.html">申 請</a>';
    }else{
        temp += '   <span class="dropMenu">';
        temp += '       <div class="acct">' + getShortSession(SESSION) + ' <i class="fas fa-caret-down"></i></div>';
        temp += '       <ul>';
        temp += '           <a href="userBindVender.html"><li>綁定供應商</li></a>';
        temp += '           <a href="userPswdChange.html"><li>更改密碼</li></a>';
        temp += '           <a href="#" onclick=logout()><li>登 出</li></a>';
        temp += '       </ul>';
        temp += '   </span>';
    }
    temp += '   </div>';    
	temp += '</div>';
	
    //Phone Menu
	temp += '<div class="phoneMenuField">';
    temp += '   <div class="hamburger" onclick="togglePhoneMenu()">';
    temp += '       <i class="hamburOpen  fas fa-bars"></i>';
    temp += '       <i class="hamburClose fas fa-times"></i>';
    temp += '   </div>';
    
    temp += '   <div id="phoneMenu">';
    if(!SESSION){
        temp += '      <a href="userLogin.html"><div class="acct">登 入</div></a>';        
    }else{
        temp += '      <div class="acct"><i class="fas fa-user-circle"></i>' + getShortSession(SESSION) + '</div>';
        temp += '      <div id="phoneAccordionMenu">';    
        temp += '           <h3 onclick="closeAccdordionExtend(\'userBindVender.html\')">綁定供應商</h3>';
        temp += '           <div class="empty">';
        temp += '           </div>';
        temp += '           <h3 onclick="closeAccdordionExtend(\'userPswdChange.html\')">更改密碼</h3>';
        temp += '           <div class="empty">';
        temp += '           </div>';
        temp += '           <h3 onclick="logout()">登 出</h3>';
        temp += '           <div class="empty">';
        temp += '           </div>';
        //temp += '           <h3>關於台泥</h3>';
        //temp += '           <div>';
        //temp += '               <a href="aboutHistory.html"><div>台泥歷史</div></a>';
        //temp += '               <a href="aboutWordFromCompany.html"><div>經營者的話</div></a>';
        //temp += '               <a href="aboutManagementTeam.html"><div>經營團隊</div></a>';
        //temp += '           </div>';     
        temp += '	   </div>';
    }
    temp +=	'   </div>';    
	temp +=	'</div>';
	
	temp += '<div class="clear"></div>';	
	return temp;
}

function getFooter(){
	var date = new Date();
	var temp = "";
	temp += '<div class="content">';
	temp += '	<div class="copyright">';
	temp += '		© ' + date.getFullYear() + ' <span> ALL RIGHTS RESERVED, TCC GROUP</span>';
	temp += '	</div>';
	temp += '</div>';
	return temp;
}

function getDomWidth(){
	return document.documentElement.clientWidth;
}

function getDomHeight(){
	return document.documentElement.clientHeight;
}

function initHeadFoot(){
    $("#header").html(getHeader());
    $("#footer").html(getFooter());
//    $("#phoneAccordionMenu").accordion({
//        collapsible: true,
//        heightStyle: "content"
//    });
    //$("#phoneAccordionMenu h3:eq(0)").trigger("click");
    adjPhoneMenuStyle();
}

function createAccordionUI(id){
    $("#" + id).accordion({
        collapsible: true,
        heightStyle: "content"
    });
//    $("#" + id + " h3:eq(0)").trigger("click");
}

function adjPhoneMenuStyle(){
    $("#phoneAccordionMenu").css("text-align", "center").css("font-size", "20px");
    $("#phoneAccordionMenu h3").css("padding", "12px 0").css("outline", "none").css("color", "#505050");
    $("#phoneAccordionMenu h3:last").css("padding", "12px 0 25px");
}

function getShortSession(session){
    idx = session.indexOf("@");
    
    if(idx != -1)
        session = session.substr(0, idx);
    
    return session;
}

var menuAni = false;
var phoneMenuExtend = false;
function togglePhoneMenu(){
    if(menuAni)
        return;
    
    menuAni = true;
    var headerH = $("#headerField").outerHeight(true);
    var phoneMenuH = parseInt($("#phoneMenu").css("height"));
    var adjPhoneMenuH = (phoneMenuH > getDomHeight()-headerH) ? phoneMenuH : getDomHeight()-headerH;    
    
    if(isMobile.iOS())
       $("#phoneMenu").css("margin", "-6px 0 0");
    
    if("none" == $("#phoneMenu").css("display") || "" == $("#phoneMenu").css("display")){
        phoneMenuExtend = true;        
        $("#phoneMenu").css("height", "0").css("display", "inline-block");
        $("#phoneMenu").animate({height : (adjPhoneMenuH) + "px"}, 500, function(){
            $("body").css("background", "#f0f0f0");
            $("#container").css("display", "none");
            menuAni = false;            
        });
        $(".hamburOpen").addClass("hamburOpenAni").animate({opacity:0}, 300);
        $(".hamburClose").addClass("hamburOpenAni").animate({opacity:1}, 300);
    }else{        
        phoneMenuExtend = false;
        $("body").css("background", "#fff");
        $("#container").css("display", "block");
        $("#phoneMenu").animate({height : "0"}, 500, function(){
            $("#phoneMenu").css("display", "none").css("height", "auto");
            menuAni = false;
        });
        $(".hamburOpen").addClass("hamburCloseAni").animate({opacity:1}, 300);
        $(".hamburClose").addClass("hamburCloseAni").animate({opacity:0}, 300);
        
        setTimeout(function(){
            $(".hamburOpen").removeClass("hamburOpenAni").removeClass("hamburCloseAni");
            $(".hamburClose").removeClass("hamburOpenAni").removeClass("hamburCloseAni");
        }, 300);
    }
}

function toogleContainer(){
    if(getDomWidth() <= 1200 && phoneMenuExtend == true)
        document.getElementById("container").style.display = "none";
    else
        document.getElementById("container").style.display = "block";
}

function closeAccdordionExtend(url){
    setTimeout(function(){
        $("#phoneAccordionMenu .empty").css("display", "none");
        if(url.indexOf("http") > -1)
            window.open(url);            
        else
            location.href = url;
    }, 5);
}

$(".goTop").click(function(){
    $("html, body").animate({scrollTop:0}, 1000);
});
            
$(".goBottom").click(function(){
    $("html, body").animate({scrollTop: parseInt($("body").css("height")) - getDomHeight()}, 1000);			
});

$(window).on("resize", function(){
    if(getDomWidth() <= 1200 && phoneMenuExtend == true)
        document.getElementById("container").style.display = "none";
    else
        document.getElementById("container").style.display = "block";    
});
//------------------------------ UI Ctrl (end) ------------------------------//

//------------------------------ Tools (start) ------------------------------//
function GetUrlString(str){
	var reg = new RegExp("(^|&)"+str+"=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);

	if(r!=null) 
		return(r[2]);
		
	return "";
}

function toThousands(num){
    var num = (num || 0).toString(), result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) { result = num + result; }
    return result;
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
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
//------------------------------ Tools (end) ------------------------------//

//------------------------------ Ani Obj (start) ------------------------------//
function runNumber(obj, endNum, runTime, addType) {
    var re = /,/g;
    
    this.obj = obj;
    this.startNum = parseFloat(this.obj.text().replace(re, ""));
    this.endNum = parseFloat(endNum.replace(re, ""));
    this.objOffsetTop = this.obj.offset().top;
    this.animated = false;
    this.add = (this.endNum - this.startNum) / (runTime*10);

    this.toThousands = function(num){
        var num = (num || 0).toString(), result = '';
        while (num.length > 3) {
            result = ',' + num.slice(-3) + result;
            num = num.slice(0, num.length - 3);
        }
        if (num) { result = num + result; }
        return result;
    }

    var run, _this = this, output = 0;
    clearInterval(run);

//    $(window).scroll(function(){
        if(!_this.animated) {

//            if($(window).scrollTop() > _this.objOffsetTop - getDomHeight() + 100) {
                _this.animated = true;

                run = setInterval(function(){
                    _this.obj.addClass("runNumColor");
                    _this.startNum = _this.startNum + _this.add;
                    output = _this.startNum;
                    output = (addType == "int") ? this.toThousands(Math.round(output)) : (Math.ceil(output*100)/100).toFixed(2);

                    if(_this.add > 0){
                        if(_this.startNum > _this.endNum){
                            clearInterval(run);
                            _this.obj.text(endNum);
                            //_this.obj.addClass("runNumZoomIn");
                        }else{
                            _this.obj.text(output);
                        }
                    }else{
                        if(_this.startNum < _this.endNum){
                            clearInterval(run);
                            _this.obj.text(endNum);
                            //_this.obj.addClass("runNumZoomIn");
                        }else{
                            _this.obj.text(output);
                        }
                    }
                }, 100);
//            }
        }
//    });
}


function customInput(id, type, title){
    this.obj = $("#" + id);    
    this.obj.append("<div class=\"title\" style=\'top:18px; left:10px\'>" + title + "</div>");
    this.obj.append("<input type=" + type + " />");
    this.input = this.obj.find("input").attr("name", id);

    var _this = this;

    this.input.focusin(function(){
        _this.obj.find(".title").animate({top:0, left:0, fontSize:"12px", opacity:0.6}, 300);        
        _this.input.css("border-bottom", "solid 1px #b00000");
    });

    this.input.blur(function(){
        if($.trim(_this.input.val()) == ""){
            if(getDomWidth() <= 1200)
                _this.obj.find(".title").animate({top:"18px", left:"10px", fontSize:"14px", opacity:1}, 300);
            else
                _this.obj.find(".title").animate({top:"18px", left:"10px", fontSize:"16px", opacity:1}, 300);
        }
        
        _this.input.css("border-bottom", "dotted 1px #808080");
    });
}

customInput.prototype.getVal = function(){
    return $.trim(this.input.val());
}

customInput.prototype.setVal = function(){
    return this.input.val("");
}

function validInputEmpty(obj){
    var validation = true;
    obj.each(function(idx){
        if( $(this).val() == "" ){                        
            alert( "[" + $(this).prev().text() + "] 不可為空白");
            validation = false;
            return false;
        }
    });

    return validation;
}
//------------------------------ Ani Obj (end) ------------------------------//

//------------------------------ Check Browser (start) ------------------------------//
window.onload = function(){
    if(checkBrowser() == "IE6" || checkBrowser() == "IE7" || checkBrowser() == "IE8" || checkBrowser() == "IE9"){
        alert("您使用的瀏覽器為 " + checkBrowser() + "，請使用 IE10(含) 以上版本的瀏覽器。\n\n或使用 Chrome 瀏覽器");
    }
}

var isMobile = {
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
};

function checkBrowser(){
	var browser = "";	
	
	if(/Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor))
		browser = "Chrome";
	
	if(/Safari/.test(navigator.userAgent) && /Apple Computer/.test(navigator.vendor))
		browser = "Safari";
    
    if (navigator.userAgent.indexOf('Firefox') != -1 && parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox') + 8)) >= 3.6)
        browser = "Firefox";

	if(navigator.userAgent.toLowerCase().indexOf('msie') != -1 || navigator.appVersion.indexOf('Trident/') > 0){
		if(navigator.appVersion.indexOf("MSIE 6.") != -1) browser = "IE6";
		else if(navigator.appVersion.indexOf("MSIE 7.") != -1) browser = "IE7";
		else if(navigator.appVersion.indexOf("MSIE 8.") != -1) browser = "IE8";
        else if(navigator.appVersion.indexOf("MSIE 9.") != -1) browser = "IE9";
        else if(navigator.appVersion.indexOf("MSIE 10.") != -1) browser = "IE10";
        else if(navigator.appVersion.indexOf('Trident/6.0') > 0) browser = "IE10";
        else if(navigator.appVersion.indexOf('Trident/7.0') > 0) browser = "IE11";
	}
	
	return browser;
}
//------------------------------ Chk Browser (end) ------------------------------//