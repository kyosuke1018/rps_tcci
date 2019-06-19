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
        else if(navigator.appVersion.indexOf('Trident/6.0') > 0) browser = "IE10";
        else if(navigator.appVersion.indexOf('Trident/7.0') > 0) browser = "IE11";
	}
	
	return browser;
}

function getDomWidth(){
	return document.documentElement.clientWidth;
}

function getDomHeight(){
	return document.documentElement.clientHeight;
}

function getHeader(){
	var temp = '';		
	temp += '<div class="headerLogo">';
	temp += '	<a href="index.html">';
	temp += '		<img class="logo" src="img/logo.png" alt="中國橡膠">';
//	temp += '		<img class="name" src="img/name.png" alt="中國橡膠">';
	temp += '	</a>';
	temp +=	'</div>';

	temp += '	<div class="headerFocus">';
	temp += '		<ul>';    
//	temp += '			<li><a href="map.html" title="網站地圖">網站地圖</a></li>';
//	temp += '			<li><i>|</i></li>';
//    temp += '			<li>';
//    temp += '			     <input id="headerSearchInput" type="text" placeholder="全文檢索" onKeyPress="enterPressSearch(event, this)"></span>';
//    temp += '			     <span  id="searchBtn" class="fa fa-search" style="font-size:14px; color:#a0a0a0" title="站內搜尋" onclick="headerSearchClick()"></span>';
//    temp += '			</li>';
//	temp += '			<li><i>|</i></li>';
	temp += '			<li>';
	temp += '               <a title="最新消息" href="news.html">最新消息</a>';
	temp += '           </li>';
//    temp += '			<li><i>|</i></li>';
//    temp += '			<li>';
//    temp += '               <a target="_blank" title="台灣水泥官方網站" href="http://www.taiwancement.com/"><div class="tccLogo"></div></a>';
//    temp += '           </li>';
	temp += '		</ul>';
	temp += '	</div>';
    
	temp += '	<div style="float:right">';
	temp += '		<div class="headerMenu">';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span               onmouseover="menuOver(1)" onmouseout="menuOut(1)">關於我們</span>';
	temp += '					<ul id="menuGroup1" onmouseover="menuOver(1)" onmouseout="menuOut(1)">';
	temp += '						<a href="about1.html"><li>品牌簡介</li></a>';
    temp += '						<a href="about2.html"><li>品牌歷史</li></a>';
    temp += '						<a href="about3.html"><li>服務據點</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';	
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span class="divLine">|</span>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp +=	'					<span               onmouseover="menuOver(2)" onmouseout="menuOut(2)">產品介紹</span>';
	temp += '					<ul id="menuGroup2" onmouseover="menuOver(2)" onmouseout="menuOut(2)">';
	temp += '						<a href="product1.html"><li>產品品項</li></a>';
	temp += '						<a href="product2.html"><li>產品應用</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span class="divLine">|</span>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp +=	'					<span               onmouseover="menuOver(2)" onmouseout="menuOut(2)">永續發展</span>';
	temp += '					<ul id="menuGroup2" onmouseover="menuOver(2)" onmouseout="menuOut(2)">';
	temp += '						<a href="sustainability1.html"><li>創新品質</li></a>';
	temp += '						<a href="sustainability2.html"><li>安全職場</li></a>';
	temp += '						<a href="sustainability3.html"><li>綠色營運</li></a>';
    temp += '						<a href="sustainability4.html"><li>社區關懷</li></a>';
    temp += '						<a href="sustainability5.html"><li>CSR 報告</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span class="divLine">|</span>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';	
	temp +=	'					<span               onmouseover="menuOver(3)" onmouseout="menuOut(3)">投資人服務</span>';
	temp += '					<ul id="menuGroup3"	onmouseover="menuOver(3)" onmouseout="menuOut(3)">';
	temp += '						<a href="Investors1.html"><li>公司簡介</li></a>';
    temp += '						<a href="Investors2.html"><li>經營團隊</li></a>';
    temp += '						<a href="Investors3.html"><li>公司治理</li></a>';
    temp += '						<a href="Investors4.html"><li>財務資訊</li></a>';
    temp += '						<a href="Investors5.html"><li>股東專欄</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span class="divLine">|</span>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp +=	'					<span               onmouseover="menuOver(4)" onmouseout="menuOut(4)">人力資源</span>';
	temp += '					<ul id="menuGroup3"	onmouseover="menuOver(4)" onmouseout="menuOut(4)">';
    temp += '                       <a href="#"><li>幸福共榮</li></a>';
    temp += '                       <a href="#"><li>成長平台</li></a>';
	temp += '						<a href="#"><li>大顯身手</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span class="divLine">|</span>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '			<ul>';
	temp += '				<li>';
	temp +=	'					<span               onmouseover="menuOver(5)" onmouseout="menuOut(5)">聯絡我們</span>';
	temp += '					<ul id="menuGroup4" onmouseover="menuOver(5)" onmouseout="menuOut(5)">';
	temp += '						<a href="#"><li>聯絡資訊</li></a>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '		</div>';	
	temp += '	</div>';
	
	temp += '	<div class="phoneMenuField">';
    temp += '       <div class="hamburger" onclick="togglePhoneMenu()">';
    temp += '           <div class="hamburLine1"></div>';
    temp += '           <div class="hamburLine2"></div>';
    temp += '           <div class="hamburLine3"></div>';
    temp += '       </div>';
	temp += '		<div id="phoneMenuBlock">';    
    temp += '           <h3>關於我們</h3>';
    temp += '           <div>';
    temp += '               <a href="#"><div>品牌簡介</div></a>';
    temp += '               <a href="#"><div>品牌歷史</div></a>';
    temp += '               <a href="#"><div>服務據點</div></a>';    
    temp += '           </div>'; 
    temp += '           <h3>產品介紹</h3>';
    temp += '           <div>';
    temp += '               <a href="#"><div>產品品項</div></a>';
    temp += '               <a href="#"><div>產品應用</div></a>';
    temp += '           </div>';
    temp += '           <h3>永續發展</h3>';
    temp += '           <div>';
    temp += '               <a href="#"><div>創新品質</div></a>';
    temp += '               <a href="#"><div>安全職場</div></a>';
    temp += '               <a href="#"><div>綠色營運</div></a>';
    temp += '               <a href="#"><div>社區關懷</div></a>';
    temp += '               <a href="#"><div>CSR報告</div></a>';
    temp += '           </div>';
    temp += '           <h3>投資人服務</h3>';
    temp += '           <div>';
    temp += '               <a href="#"><div>公司簡介</div></a>';
    temp += '               <a href="#"><div>經營團隊</div></a>';
    temp += '               <a href="#"><div>公司治理</div></a>';
    temp += '               <a href="#"><div>財務資訊</div></a>';
    temp += '               <a href="#"><div>股東專欄</div></a>';
    temp += '           </div>';
    temp += '           <h3>人力資源</h3>';
    temp += '           <div>';
    temp += '               <a href="#"><div>幸福共榮</div></a>';
    temp += '               <a href="#"><div>成長平台</div></a>';
    temp += '               <a href="#"><div>大顯身手</div></a>';
    temp += '           </div>';
    temp += '           <h3>聯絡我們</h3>';
    temp += '           <div>';
    temp += '		        <a target="_blank" href="#"><div>聯絡我們</div></a>';
    temp += '           </div>';
	temp += '           <span class="social">';
    temp += '               <a class="lang" href="#">English</a>';
    temp += '           </span>';
    temp += '           <span class="social">';
    temp += '               <a target="_blank" rel="noopener noreferrer" href="http://www.taiwancement.com/"><img src="img/logo_tcc.png"/></a>';
    temp += '           </span>';
	temp += '		</div>';
	temp +=	'	</div>';
	
	temp += '<div class="clear"></div>';
	
	return temp;
}

function getWebMap(){
	var temp = '';
	temp += '<div class="footerMenuField">';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">關於我們</div>';
	temp += '		<a class="footerSubMenu" href="about1.html">品牌簡介</a>';	
    temp += '		<a class="footerSubMenu" href="about2.html">品牌歷史</a>';
    temp += '		<a class="footerSubMenu" href="about3.html">服務據點</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">產品介紹</div>';
    temp += '		<a class="footerSubMenu" href="product1.html">產品品項</a>';
    temp += '		<a class="footerSubMenu" href="product2.html">產品應用</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">永續發展</div>';
    temp += '		<a class="footerSubMenu" href="sustainability1.html">創新品質</a>';    
    temp += '		<a class="footerSubMenu" href="sustainability2.html">安全職場</a>';
    temp += '		<a class="footerSubMenu" href="sustainability3.html">綠色營運</a>';
    temp += '		<a class="footerSubMenu" href="sustainability4.html">社區關懷</a>';
    temp += '		<a class="footerSubMenu" href="sustainability5.html">CSR 報告</a>';
	temp += '	</div>';
    temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">投資人服務</div>';
    temp += '		<a class="footerSubMenu" href="Investors1.html">公司簡介</a>';
    temp += '		<a class="footerSubMenu" href="Investors2.html">經營團隊</a>';
	temp += '		<a class="footerSubMenu" href="Investors3.html">公司治理</a>';
	temp += '		<a class="footerSubMenu" href="Investors4.html">財務資訊</a>';
	temp += '		<a class="footerSubMenu" href="Investors5.html">股東專欄</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">人力資源</div>';
    temp += '		<a class="footerSubMenu" target="_blank" href="#">幸福共榮</a>';
	temp += '		<a class="footerSubMenu" target="_blank" href="#">成長平台</a>';
    temp += '		<a class="footerSubMenu" target="_blank" href="#">大顯身手</a>';    
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">聯絡我們</div>';
	temp += '		<a class="footerSubMenu" href="#">聯絡資訊</a>';
	temp += '	</div>';
	temp += '</div>';	
	return temp;	
}

function getFooter(){
	var date = new Date();
	var temp = "";
	temp += '<div class="content">';
	temp += '	<div class="copyright">';
//	temp += '		<img src="img/logo.png">';
	temp += '		<span>Copyright © ' + date.getFullYear() + ' 中國合成橡膠股份有限公司</span>';
	temp += '	</div>';
	temp += '</div>';
	return temp;
}

function getWebMapOther(){
	var temp = "";
	temp += '<div class="footerMenuField">';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu"><a class="colorOther" href="index.html" title="CSR 網站首頁">首頁</a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu"><a class="colorOther" href="newslist.html" title="CSR 相關新聞">新聞訊息</a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu"><a class="colorOther" href="report.html" title="CSR 報告書 PDF 下載">報告書下載</a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';	
	temp += '		<div class="footerMainMenu"><a class="colorOther" href="quest.html" title="CSR 問卷調查">問卷調查</a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu">';
	temp += '			<a href="contact.html"><img src="img/menu_mail.png" title="聯絡我們"/></a>';
	temp += '			<a target="_blank" href="https://goo.gl/Wf5i5l"><img src="img/menu_fb.png" title="台泥 FB 電子報"/></a>';
	temp += '		</div>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu"><a class="colorOther" target="_blank" href="http://www.taiwancement.com" title="台灣水泥官方網站"><img src="img/logo_tcc.png"></a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';	
	temp += '		<div class="footerMainMenu"><a class="colorOther" target="_blank" href="#" title="保種中心臉書專頁"><img src="img/logo_fb.png"></a></div>';	
	temp += '	</div>';    
	temp += '	<div class="footerMenu">';	
	temp += '		<div class="footerMainMenu"><a class="colorOther" target="_blank" href="#" title="保種中心 youtube"><img src="img/logo_youtube.png"></a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '	</div>';
	temp += '</div>';
	return temp;
}

var MAIN_PLANT_LIST = ['', '蕨類植物', '裸子植物', '單子葉植物', '雙子葉植物', '苔蘚植物']; //for demo only
function breadcrumb(num){
    breadcrumb(num, []);
}

function breadcrumb(num, appendAry){
	var temp = '';
	var home = '<a href="index.html" title="返回首頁"><span class="home"></span></a>';
	var arrow = '&nbsp; > &nbsp;';

	switch(num){
		case "1-1": temp += home + arrow + '活動公告' + arrow + '<a class="last">最新消息</a>';
			break;
		case "1-2": temp += home + arrow + '活動公告' + arrow + '<a class="last">媒體報導</a>';
			break;
        case "1-3": temp += home + arrow + '活動公告' + arrow + '<a class="last">保種專案</a>';
			break;
        case "1-4": temp += home + arrow + '活動公告' + arrow + '<a class="last">百種興盛 允萌行動</a>';
			break;
        case "1-4-1": temp += home + arrow + '活動公告' + arrow + '<a href="announce_bloom.html">百種興盛 允萌行動</a>' + arrow + '<a class="last">植物生長狀況</a>';
			break;
        case "1-4-1-1": temp += home + arrow + '活動公告' + arrow + '<a href="announce_bloom.html">百種興盛 允萌行動</a>' + arrow + '植物生長狀況' + arrow + '<a class="last">植物生長照片</a>';
			break;            
		case "1-5": temp += home + arrow + '活動公告' + arrow + '<a class="last">影音</a>';
			break;
		case "1-6": temp += home + arrow + '活動公告' + arrow + '<a class="last">活動圖輯</a>';
			break;            
		case "2-1": temp += home + arrow + '保種中心簡介' + arrow + '<a class="last">宗旨</a>';
			break;
		case "2-2": temp += home + arrow + '保種中心簡介' + arrow + '<a class="last">歷史</a>';
			break;
		case "2-3": temp += home + arrow + '保種中心簡介' + arrow + '<a class="last">園區配置圖</a>';
			break;
		case "3-1":	temp += home + arrow + '植物檢索' + arrow + '<a class="last">植物名錄</a>';
			break;
        case "3-1-x":     temp += home + arrow + '植物檢索' + arrow + '植物名錄' + arrow + '<a class="last">' + appendAry[0] + '</a>';
            break;
        case "3-1-x-x":   temp += home + arrow + '植物檢索' + arrow + '植物名錄' + arrow + appendAry[0] + arrow + '<a class="last">' + appendAry[1] + '</a>';
            break;
        case "3-1-x-x-x": temp += home + arrow + '植物檢索' + arrow + '植物名錄' + arrow + appendAry[0] + arrow + appendAry[1] + arrow + '<a class="last">' + appendAry[2] + '</a>';
            break;
		case "3-2":	temp += home + arrow + '植物檢索' + arrow + '<a class="last">植物圖鑑</a>';
			break;
		case "3-3":	temp += home + arrow + '植物檢索' + arrow + '<a class="last">植物查詢</a>';
			break;
        case "4-1":	temp += home + arrow + '學術研究' + arrow + '<a class="last">出版品</a>';
			break;
        case "4-2":	temp += home + arrow + '學術研究' + arrow + '<a class="last">植物蒐藏</a>';
			break;
        case "4-2-x":	temp += home + arrow + '學術研究' + arrow + '<a href="research_collet.html">植物蒐藏</a>' + arrow + '<a class="last">' + appendAry[0] + '</a>';
			break;
        case "4-2-x-x":	temp += home + arrow + '學術研究' + arrow + '<a href="research_collet.html">植物蒐藏</a>' + arrow + appendAry[0] + arrow + '<a class="last">' + appendAry[1] + '</a>';
			break;
		case "4-3":	temp += home + arrow + '學術研究' + arrow + '<a class="last">研究論文</a>';
			break;
		case "4-4":	temp += home + arrow + '學術研究' + arrow + '<a class="last">樣本採集申請</a>';
			break;
		case "5-1":	temp += home + arrow + '聯絡資訊' + arrow + '<a class="last">中心聯絡資訊</a>';
			break;
		case "5-2":	temp += home + arrow + '聯絡資訊' + arrow + '<a class="last">中心工作成員</a>';
			break;
	}
	
	return temp;
}

function searchOpenClose(){
	var top = parseInt(document.getElementById("headerSearch").offsetTop);    
    var t;
    
	if(top < 0){
        document.getElementById("gsc-i-id1").select();
        t = -99;
		
		var open = setInterval(function(){
			t += 3;
			document.getElementById("headerSearch").style.top = t + "px";

			if(t >= 0)
				clearInterval(open);
		},1);
	}else{
		t = 0;
		var close = setInterval(function(){
			t -= 3;
			document.getElementById("headerSearch").style.top = t + "px";

			if(t <= -99)
				clearInterval(close);
		},1);
	}
}

function menuOver(id){
	if(checkBrowser() == "IE6"){
		id = "menuGroup"+id;	
		document.getElementById(id).style.visibility = "visible";
	}	
	return false;
}

function menuOut(id){
	if(checkBrowser() == "IE6"){
		id = "menuGroup"+id;	
		document.getElementById(id).style.visibility = "hidden";
	}
	return false;
}

function GetUrlString(str){
	var reg = new RegExp("(^|&)"+str+"=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);

	if(r!=null) 
		return(r[2]);
		
	return "";
}

function adjContainerHeight(){
    var bodyH = getDomHeight();
    var headerH = document.getElementById("headerField").offsetHeight;
    var footerH = document.getElementById("footerField").offsetHeight;
    var containerH = document.getElementById("container").offsetHeight;    
    
    if(headerH + footerH + containerH < bodyH)
        document.getElementById("container").style.minHeight = bodyH - headerH - footerH + "px";
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

var menuAni = false;
var phoneMenuExtend = false;
function togglePhoneMenu(){
    if(menuAni)
        return;
    
    menuAni = true;
    var headerFieldH = document.getElementById("headerField").offsetHeight;
    var bodyH = document.getElementsByTagName("body")[0].offsetHeight;
    var elem = document.getElementById("phoneMenuBlock");
    var elemHeight = elem.offsetHeight;
    
    if(elem.style.display == "none" || elem.style.display == ""){
        elem.style.display = "inline-block";        
        elem.style.overflow = "hidden";
        elem.style.height = 0 + "px";
        phoneMenuExtend = true;
        
        var height = 0;
        var h = setInterval(function(){
            height += 5;
            elem.style.height = height + "px";            
            
            if(elem.offsetHeight >= getDomHeight() - headerFieldH - 1){                
                document.getElementById("container").style.display = "none";
                clearInterval(h);                
                elem.style.overflow = "auto";
                elem.style.height = getDomHeight() - headerFieldH - 1 + "px";
                menuAni = false;
            }
        }, 0);

        if(isMobile.any()){
//            document.querySelector(".hamburLine1").style.transformOrigin = "18% 50%";
//            document.querySelector(".hamburLine3").style.transformOrigin = "18% 50%";            
            document.querySelector(".hamburLine1").style.transformOrigin = "12% 50%";
            document.querySelector(".hamburLine3").style.transformOrigin = "12% 50%";
        }else{
            document.querySelector(".hamburLine1").style.transformOrigin = "7% 50%";
            document.querySelector(".hamburLine3").style.transformOrigin = "7% 50%";
        }
        
        var obj1Deg = 0;
        var rotateSign1 = setInterval(function(){
            obj1Deg++;
            document.querySelector(".hamburLine1").style.transform = "rotate(" + obj1Deg + "deg)";
            if(obj1Deg == 45)
                clearInterval(rotateSign1);
        }, 0);

        var obj3Deg = 0;
        var rotateSign3 = setInterval(function(){
            obj3Deg--;                        
            document.querySelector(".hamburLine3").style.transform = "rotate(" + obj3Deg + "deg)";
            if(obj3Deg == -45)
                clearInterval(rotateSign3);
        }, 0);
        
        var obj2Width = 50;
        var shinkSign2 = setInterval(function(){
            obj2Width--;                        
            document.querySelector(".hamburLine2").style.width = obj2Width + "%";
            if(obj2Width == 0)
                clearInterval(shinkSign2);
        }, 0);
    }else{        
        document.getElementById("container").style.display = "block";
        elem.style.overflow = "hidden";

        phoneMenuExtend = false;
        
        var height = parseInt(elem.style.height);
        var x = setInterval(function(){
            height -= 5;            
            elem.style.height = height + "px";

            if(parseInt(elem.style.height) <= 10){
                clearInterval(x);
                elem.style.height = 0 + "px";
                elem.style.display = "none";
                menuAni = false;
            }
        }, 0);
        
        var obj1Deg = 45;
        var rotateSign1 = setInterval(function(){
            obj1Deg--;
            document.querySelector(".hamburLine1").style.transform = "rotate(" + obj1Deg + "deg)";
            if(obj1Deg == 0)
                clearInterval(rotateSign1);
        }, 0);

        var obj3Deg = -45;
        var rotateSign3 = setInterval(function(){
            obj3Deg++;                        
            document.querySelector(".hamburLine3").style.transform = "rotate(" + obj3Deg + "deg)";
            if(obj3Deg == 0)
                clearInterval(rotateSign3);
        }, 0);
        
        var obj2Width = 0;
        var growSign2 = setInterval(function(){
            obj2Width++;                        
            document.querySelector(".hamburLine2").style.width = obj2Width + "%";
            if(obj2Width == 50)
                clearInterval(growSign2);
        }, 0);
    }
}

$(".goTop").click(function(){
    $("html, body").animate({scrollTop:0}, 1000);
});
            
$(".goBottom").click(function(){
    $("html, body").animate({scrollTop: parseInt($("body").css("height")) - getDomHeight() }, 1000);			
});

function toogleContainer(){
    if(getDomWidth() < 980 && phoneMenuExtend == true)
        document.getElementById("container").style.display = "none";
    else
        document.getElementById("container").style.display = "block";
}

window.onresize = function(){
    if(getDomWidth() < 980 && phoneMenuExtend == true)
        document.getElementById("container").style.display = "none";
    else
        document.getElementById("container").style.display = "block";
}

window.onload = function(){
    if(checkBrowser() == "IE6" || checkBrowser() == "IE7" || checkBrowser() == "IE8"){
        alert("您使用的瀏覽器為 " + checkBrowser() + "，請使用 IE9(含) 以上版本的瀏覽器");
    }
}