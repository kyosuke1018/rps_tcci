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

	if(navigator.userAgent.toLowerCase().indexOf('msie') != -1){
		
		if(navigator.appVersion.indexOf("MSIE 7.") != -1){		
			browser = "IE7";
		}
		else if(navigator.appVersion.indexOf("MSIE 6.") != -1){
			browser = "IE6";
		}
		else if(navigator.appVersion.indexOf("MSIE 8.") != -1){
			browser = "IE8";
		}	
	}
	
	return browser;
}

function getDomWideh(){
	return document.documentElement.clientWidth;
}

function getDomHeight(){
	return document.documentElement.clientHeight;
}

var antiAD;
function getSearchAPI(){
    if(checkBrowser() == "IE8" || checkBrowser() == "IE7" || checkBrowser() == "IE6"){        
	   return false;
	}
    
	(function(){
        var cx = '015426534168755917368:wkcflf9qv7c';
		var gcse = document.createElement('script');
		gcse.type = 'text/javascript';
		gcse.async = true;
		gcse.src = 'https://cse.google.com/cse.js?cx=' + cx;
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(gcse, s);
	})();

//    var gscWidth = document.getElementById("headerSearch").offsetWidth;
//    var headerWidth = document.getElementById("header").offsetWidth;
//    document.getElementById("headerSearch").style.left = getDomWideh()/2 + headerWidth/2 - gscWidth + "px";        
	    
	setTimeout(function(){
        var elems, gscSearchBtn, gscResultCloseBtn;
                
        if(window.attachEvent){
            document.getElementById("gsc-i-id1").attachEvent("keyup", function(e){
                if(e.keyCode == 13){
                    searchOpenClose();
                    antiGSCad();
                }
            });
            
            document.querySelector(".gsc-search-button-v2").attachEvent("click", function(){
                searchOpenClose();
                antiGSCad();
            });
            
            document.querySelector(".gsc-results-close-btn").attachEvent("click", function(){
                clearInterval(antiAD);
            });
        }else{
            document.getElementById("gsc-i-id1").addEventListener("keyup", function(e){
                if(e.keyCode == 13){
                    searchOpenClose();
                    antiGSCad();
                }
            }, false);
            
            document.querySelector(".gsc-search-button-v2").addEventListener("click", function(){
                searchOpenClose();
                antiGSCad();
            }, false);
            
            document.querySelector(".gsc-results-close-btn").addEventListener("click", function(){
                clearInterval(antiAD);
            }, false);
        }
        
//        elems = document.getElementsByTagName("input");
//        for(var i=0; i<elems.length; i++){
//            if(elems[i].getAttribute("class") == "gsc-search-button gsc-search-button-v2");
//               gscSearchBtn = elems[i];
//        }
//        
//        gscSearchBtn.addEventListener("click", function(){
//			searchOpenClose();
//			antiGSCad();
//		});      
        
//        elems = document.getElementsByTagName("div");
//        for(var i=0; i<elems.length; i++){
//            if(elems[i].getAttribute("class") == "gsc-results-close-btn gsc-results-close-btn-visible")
//                gscResultCloseBtn = elems[i];
//        }
//        
//        gscResultCloseBtn.addEventListener("click", function(){
//			clearInterval(antiAD);
//		});
	}, 2000);
}

function antiGSCad(){
	antiAD = setInterval(function(){        
//		document.querySelector(".gsc-adBlock").style.display = "none";
//		document.querySelector(".gsc-adBlockVertical").style.display = "none";
        
        var elems = document.getElementsByTagName("div");
        for(var i=0; i<elems.length; i++){
            if(elems[i].getAttribute("class") == "gsc-adBlock")
                elems[i].style.display = "none";
            
            if(elems[i].getAttribute("class") == "gsc-adBlockVertical")
                elems[i].style.display = "none";
        }
	}, 500);
}

function getHeader(){
	var temp = '';		
	temp += '<div class="headerLogo">';
	temp += '	<a href="index.html">';
	temp += '		<img class="logo" src="img/logo.png" alt="台泥、台灣水泥">';
	temp += '		<img class="name" src="img/name.png" alt="台泥企業永續發展">';
//	temp += '		<span class="caption">台泥企業永續發展</br>';
//	temp += '			<span class="subCaption">Corporate Sustainability Report</span>';
//	temp += '		</span>';
	temp += '	</a>';
	temp +=	'</div>';

    if(checkBrowser() != "IE8" && checkBrowser() != "IE7" && checkBrowser() != "IE6"){
        temp += '	<div id="headerSearch">';    
        temp += '		<gcse:search></gcse:search>';
        temp += '		<div id="closeBtn" onclick="searchOpenClose()">▲ 收合</div>';
        temp += '	</div>';
    }

	temp += '	<div class="headerFocus">';
	temp += '		<ul>';    
	temp += '			<li><a href="map.html" title="CSR 網站地圖">網站地圖</a></li>';
	temp += '			<li><i>|</i></li>';
	temp += '			<li><a href="newslist.html" title="CSR 相關新聞">新聞訊息</a></li>';
	temp += '			<li><i>|</i></li>';
	temp += '			<li><a href="report.html" title="CSR 報告書 PDF 下載">報告書下載</a></li>';
	temp += '			<li><i>|</i></li>';
	temp += '			<li><a href="quest.html"  title="CSR 問卷調查">問卷調查</a></li>';
	temp += '			<li><i>|</i></li>';
    
    if(checkBrowser() != "IE8" && checkBrowser() != "IE7" && checkBrowser() != "IE6"){
	   temp += '			<li><span id="openBtn" title="站內搜尋" onclick="searchOpenClose()">搜尋</span></li>';
	   temp += '			<li><i>|</i></li>';
    }
    
	temp += '			<li><a href="contact.html"><img src="img/menu_mail.png" title="聯絡我們"/></a></li>';
	temp += '			<li><a href="https://goo.gl/Wf5i5l" target="_blank"><img src="img/menu_fb.png" title="台泥 FB 電子報"/></a></li>';
	temp += '			<li><img src="img/menu_weixin.png" title="台泥微信 QR code" onclick="weixinOpenClose()"/><img id="weixinQR" src="img/menu_weixinQR.jpg"/></li>';
	temp += '		</ul>';
	temp += '	</div>';
    
	temp += '	<div style="float:right">';
	temp += '		<div class="headerMenu">';
	temp += '			<ul>';
	temp += '				<li>';
	temp += '					<span               onmouseover="menuOver(1)" onmouseout="menuOut(1)" class="color3">公司簡介</span>';
	temp += '					<ul id="menuGroup1" onmouseover="menuOver(1)" onmouseout="menuOut(1)">';
	temp += '						<li><a href="csr3-1.html">公司概況</a></li>';
	temp += '						<li><a href="csr3-2.html">關聯企業</a></li>';
	temp += '						<li><a href="csr3-3.html">影音</a></li>';
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
	temp +=	'					<span               onmouseover="menuOver(2)" onmouseout="menuOut(2)" class="color5">低碳產品</span>';
	temp += '					<ul id="menuGroup2" onmouseover="menuOver(2)" onmouseout="menuOut(2)">';
	temp += '						<li><a href="csr5-1.html">產品介紹</a></li>';
	temp += '						<li><a href="csr5-2.html">研發創新</a></li>';
	temp += '						<li><a href="csr5-3.html">港廠電合一</a></li>';
	temp += '						<li><a href="csr5-4.html">餘熱發電</a></li>';
	temp += '						<li><a href="csr5-5.html">二氧化碳捕獲</a></li>';
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
	temp +=	'					<span               onmouseover="menuOver(3)" onmouseout="menuOut(3)" class="color7">環境管理</span>';
	temp += '					<ul id="menuGroup3"	onmouseover="menuOver(3)" onmouseout="menuOut(3)">';
	temp += '						<li><a href="csr7-1.html">環境管理政策及投入</a></li>';
	temp += '						<li><a href="csr7-2.html">溫室氣體管理</a></li>';
	temp += '						<li><a href="csr7-3.html">原料使用</a></li>';
	temp += '						<li><a href="csr7-4.html">能源管理</a></li>';
	temp += '						<li><a href="csr7-5.html">水資源管理</a></li>';
	temp += '						<li><a href="csr7-6.html">空氣汙染防制</a></li>';
	temp += '						<li><a href="csr7-7.html">廢棄資源管理</a></li>';
	temp += '						<li><a href="csr7-8.html">環境影響監測</a></li>';
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
	temp +=	'					<span               onmouseover="menuOver(4)" onmouseout="menuOut(4)" class="color9">社會參與</span>';
	temp += '					<ul id="menuGroup4" onmouseover="menuOver(4)" onmouseout="menuOut(4)">';
	temp += '						<li><a href="csr9-1.html">社會關懷</a></li>';
	temp += '						<li><a href="csr9-2.html">永續生態</a></li>';
	temp += '						<li><a href="csr9-3.html">藝文推廣</a></li>';
	temp += '					</ul>';
	temp += '				</li>';
	temp += '			</ul>';
	temp += '		</div>';	
	temp += '	</div>';
	
	temp += '	<div class="phoneMenuField">';
	temp += '		<img id="phoneMenuIcon" src="img/phone_menu.png" onclick="showHidePhoneMenu()">';
	temp += '		<div id="phoneMenuBlock">';
    temp += '           </br>';
    temp += '			<div class="phoneMenu">';
	temp += '			    <div class="phoneMainMenu"><a class="colorOther" href="newslist.html">新聞訊息</a></div>';
	temp += '			</div>';
    temp += '			<div class="phoneMenu">';
	temp += '			    <div class="phoneMainMenu"><a class="colorOther" href="report.html">報告書下載</a></div>';
	temp += '			</div>';
    temp += '			<div class="phoneMenu">';
	temp += '			    <div class="phoneMainMenu"><a class="colorOther" href="quest.html">問卷調查</a></div>';
	temp += '			</div>';
    temp += '			<div class="phoneMenu">';
	temp += '			    <div class="phoneMainMenu"><a class="colorOther" href="authenticate/SGS.pdf" target="_blank">第三方認證</a></div>';
	temp += '			</div>';
    temp += '			<div class="phoneMenu">';
	temp += '			    <div class="phoneMainMenu"><a class="colorOther" href="http://www.taiwancement.com/" target="_blank">台灣水泥</a></div>';
	temp += '			</div>';
    temp += '			<div class="phoneMenu">';
    temp += '		        <a href="contact.html"><img src="img/menu_mail.png" title="聯絡我們"/></a>';
    temp += '		        <a target="_blank" href="https://goo.gl/Wf5i5l"><img src="img/menu_fb.png" title="台泥 FB 電子報"/></a>';
    temp += '		        <a target="_blank" href="http://weixin.qq.com/r/wDvz6_DEQr09raSI927S"><img src="img/menu_weixin.png" title="台泥微信"/></a>';
	temp += '			</div>';    
    temp += '           </br></br>';
    temp += '           <div class="containerDivideDash"></div>';
    temp += '           </br>';
    temp += '   		<div class="phoneMenu">';
    temp += '		        <div class="phoneMainMenu color3">目錄</div>';
	temp += '				<a class="phoneSubMenu" href="csr0-1.html">關於報告書</a>';
	temp += '				<a class="phoneSubMenu" href="csr0-2.html">董事長的話</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
    temp += '               <div class="phoneMainMenu color1"><a href="csr1-1.html" style="color:#e6240f">2015永續績效</a></div>';
//	temp += '				<a class="phoneSubMenu" href=""></a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color2">永續議題管理</div>';
	temp += '				<a class="phoneSubMenu" href="csr2-1.html">利害關係人溝通</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color3">公司簡介</div>';
	temp += '				<a class="phoneSubMenu" href="csr3-1.html">公司概況</a>';
	temp += '				<a class="phoneSubMenu" href="csr3-2.html">關聯企業</a>';
	temp += '				<a class="phoneSubMenu" href="csr3-3.html">影音</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color4">公司治理</div>';
	temp += '				<a class="phoneSubMenu" href="csr4-1.html">董事會運作</a>';
	temp += '				<a class="phoneSubMenu" href="csr4-2.html">審計委員會及薪酬委員會</a>';
	temp += '				<a class="phoneSubMenu" href="csr4-3.html">誠信經營</a>';
	temp += '				<a class="phoneSubMenu" href="csr4-4.html">風險類型</a>';
	temp += '				<a class="phoneSubMenu" href="csr4-5.html">財務績效</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color5">低碳產品</div>';
	temp += '				<a class="phoneSubMenu" href="csr5-1.html">產品介紹</a>';
	temp += '				<a class="phoneSubMenu" href="csr5-2.html">研發創新</a>';
	temp += '				<a class="phoneSubMenu" href="csr5-3.html">港廠電合一</a>';
	temp += '				<a class="phoneSubMenu" href="csr5-4.html">餘熱發電</a>';
	temp += '				<a class="phoneSubMenu" href="csr5-5.html">二氧化碳捕獲</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color6">最佳職場</div>';
	temp += '				<a class="phoneSubMenu" href="csr6-1.html">人力概況</a>';
	temp += '				<a class="phoneSubMenu" href="csr6-2.html">儲備幹部訓練</a>';
	temp += '				<a class="phoneSubMenu" href="csr6-3.html">安全照顧</a>';
	temp += '				<a class="phoneSubMenu" href="csr6-4.html">福利制度</a>';
	temp += '				<a class="phoneSubMenu" href="csr6-5.html">人才培育</a>';
	temp += '				<a class="phoneSubMenu" href="csr6-6.html">工作生活平衡</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color7">環境管理</div>';
	temp += '				<a class="phoneSubMenu" href="csr7-1.html">環境管理政策及投入</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-2.html">溫室氣體管理</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-3.html">原料使用</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-4.html">能源管理</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-5.html">水資源管理</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-6.html">空氣汙染防制</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-7.html">廢棄資源管理</a>';
	temp += '				<a class="phoneSubMenu" href="csr7-8.html">環境影響監測</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color8">夥伴關係</div>';
	temp += '				<a class="phoneSubMenu" href="csr8-1.html">客戶服務</a>';
	temp += '				<a class="phoneSubMenu" href="csr8-2.html">供應商管理</a>';
	temp += '			</div>';
	temp += '			<div class="phoneMenu">';
	temp += '				<div class="phoneMainMenu color9">社會參與</div>';
	temp += '				<a class="phoneSubMenu" href="csr9-1.html">社會關懷</a>';
	temp += '				<a class="phoneSubMenu" href="csr9-2.html">永續生態</a>';
	temp += '				<a class="phoneSubMenu" href="csr9-3.html">藝文推廣</a>';
	temp += '			</div>';
	temp += '		</div>';
	temp +=	'	</div>';
	
	temp += '<div class="clear"></div>';
	
	return temp;
}

function getWebMap(){
	var temp = '';
	temp += '<div class="footerMenuField">';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color3">目錄</div>';
	temp += '		<a class="footerSubMenu" href="csr0-1.html">關於報告書</a>';
	temp += '		<a class="footerSubMenu" href="csr0-2.html">董事長的話</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color1"><a href="csr1-1.html" style="color:#e6240f">2015永續績效</a></div>';
//	temp += '		<a class="footerSubMenu" href="">2015年</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color2">永續議題管理</div>';
	temp += '		<a class="footerSubMenu" href="csr2-1.html">利害關係人溝通</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color3">公司簡介</div>';
	temp += '		<a class="footerSubMenu" href="csr3-1.html">公司概況</a>';
	temp += '		<a class="footerSubMenu" href="csr3-2.html">關聯企業</a>';
	temp += '		<a class="footerSubMenu" href="csr3-3.html">影音</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color4">公司治理</div>';
	temp += '		<a class="footerSubMenu" href="csr4-1.html">董事會運作</a>';
	temp += '		<a class="footerSubMenu" href="csr4-2.html">審計委員會及薪酬委員會</a>';
	temp += '		<a class="footerSubMenu" href="csr4-3.html">誠信經營</a>';
	temp += '		<a class="footerSubMenu" href="csr4-4.html">風險類型</a>';
	temp += '		<a class="footerSubMenu" href="csr4-5.html">財務績效</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color5">低碳產品</div>';
	temp += '		<a class="footerSubMenu" href="csr5-1.html">產品介紹</a>';
	temp += '		<a class="footerSubMenu" href="csr5-2.html">研發創新</a>';
	temp += '		<a class="footerSubMenu" href="csr5-3.html">港廠電合一</a>';
	temp += '		<a class="footerSubMenu" href="csr5-4.html">餘熱發電</a>';
	temp += '		<a class="footerSubMenu" href="csr5-5.html">二氧化碳捕獲</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color6">最佳職場</div>';
	temp += '		<a class="footerSubMenu" href="csr6-1.html">人力概況</a>';
	temp += '		<a class="footerSubMenu" href="csr6-2.html">儲備幹部訓練</a>';
	temp += '		<a class="footerSubMenu" href="csr6-3.html">安全照顧</a>';
	temp += '		<a class="footerSubMenu" href="csr6-4.html">福利制度</a>';
	temp += '		<a class="footerSubMenu" href="csr6-5.html">人才培育</a>';
	temp += '		<a class="footerSubMenu" href="csr6-6.html">工作生活平衡</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color7">環境管理</div>';
	temp += '		<a class="footerSubMenu" href="csr7-1.html">環境管理政策及投入</a>';
	temp += '		<a class="footerSubMenu" href="csr7-2.html">溫室氣體管理</a>';
	temp += '		<a class="footerSubMenu" href="csr7-3.html">原料使用</a>';
	temp += '		<a class="footerSubMenu" href="csr7-4.html">能源管理</a>';
	temp += '		<a class="footerSubMenu" href="csr7-5.html">水資源管理</a>';
	temp += '		<a class="footerSubMenu" href="csr7-6.html">空氣汙染防制</a>';
	temp += '		<a class="footerSubMenu" href="csr7-7.html">廢棄資源管理</a>';
	temp += '		<a class="footerSubMenu" href="csr7-8.html">環境影響監測</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color8">夥伴關係</div>';
	temp += '		<a class="footerSubMenu" href="csr8-1.html">客戶服務</a>';
	temp += '		<a class="footerSubMenu" href="csr8-2.html">供應商管理</a>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu color9">社會參與</div>';
	temp += '		<a class="footerSubMenu" href="csr9-1.html">社會關懷</a>';
	temp += '		<a class="footerSubMenu" href="csr9-2.html">永續生態</a>';
	temp += '		<a class="footerSubMenu" href="csr9-3.html">藝文推廣</a>';
	temp += '	</div>';
	temp += '</div>';	
	return temp;	
}

function getFooter(){
	var date = new Date();
	var temp = "";
	temp += '<div class="footerEnd">';
	temp += '	<div class="copyright">';
	temp += '		<div class="authenticate">';
	temp += '			<a target="_blank" href="authenticate/SGS.pdf" title="SGS 認證">第三方認證</a>';
	temp += '			&nbsp;&nbsp;|&nbsp;&nbsp;';
	temp += '			<a target="_blank" href="http://www.taiwancement.com/" title="台灣水泥官方網站">台灣水泥</a>';
    temp += '			&nbsp;&nbsp;|&nbsp;&nbsp;';
	temp += '			<a target="_blank" href="mailto:tcc_csr@taiwancement.com" title="聯絡窗口">聯絡我們</a>';
	temp += '		</div>';
	temp += '		<div class="note">';
	temp += '			<img src="img/logo2.png">';
	temp += '			Copyright © ' + date.getFullYear() + ' 台灣水泥股份有限公司。';
	temp += '			</br>';
	temp += '			著作權所有，並保留一切權利。';
	temp += '		</div>';
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
	temp += '			<a onclick="weixinOpenClose()"><img src="img/menu_weixin.png" title="台泥微信 QR code"/></a>';
	temp += '		</div>';
	temp += '	</div>';
	temp += '	<div class="footerMenu">';
	temp += '		<div class="footerMainMenu"><a class="colorOther" target="_blank" href="authenticate/SGS.pdf" title="SGS 認證">第三方認證</a></div>';	
	temp += '	</div>';
	temp += '	<div class="footerMenu">';	
	temp += '		<div class="footerMainMenu"><a class="colorOther" target="_blank" href="http://www.taiwancement.com" title="台灣水泥官方網站">台灣水泥</a></div>';	
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

function getReport(){
	var temp = '';
	temp += '<div class="reportList">';
	temp += '	<div class="report reportCurrent">';
	temp += '		<a href="report/report_2015.pdf" target="_blank">';
	temp += '			<img src="report/cover_2015.jpg"/>';
	temp += '			<span>2015 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';
	temp += '	</br></br>';
	temp += '	<div class="title" align="left">- 歷年報告書</div>';
	temp += '	<div class="containerDivideDash"></div>';
	temp += '	</br>';
	temp += '	<div class="report">';
	temp += '		<a href="report/report_2014.pdf" target="_blank">';
	temp += '			<img src="report/cover_2014.jpg"/>';
	temp += '			<span>2014 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';	
	temp += '	<div class="report">';
	temp += '		<a href="report/report_2013.pdf" target="_blank">';
	temp += '			<img src="report/cover_2013.jpg"/>';
	temp += '			<span>2013 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';
	temp += '	<div class="report">';
	temp += '		<a href="report/report_2012.pdf" target="_blank">';
	temp += '			<img src="report/cover_2012.jpg"/>';
	temp += '			<span>2012 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';	
	temp += '	<div class="report">';	
	temp += '		<a href="report/report_2011.pdf" target="_blank">';
	temp += '			<img src="report/cover_2011.jpg"/>';
	temp += '			<span>2011 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';	
	temp += '	<div class="report">';
	temp += '		<a href="report/report_2010.pdf" target="_blank">';
	temp += '			<img src="report/cover_2010.jpg"/>';
	temp += '			<span>2010 報告書</span>';
	temp += '		</a>';
	temp += '	</div>';
	temp += "</div>";
	return temp;
}

function breadcrumb(num){
	var temp = '';
	var home = '<a href="index.html" title="返回首頁"><span class="home"></span></a>';
	//var home = "";
	var arrow = '&nbsp; > &nbsp;';

	switch(num){
		case "0-1": temp += home + arrow + '目錄' + arrow + '<a class="last">關於報告書</a>';
			break;
		case "0-2": temp += home + arrow + '目錄' + arrow + '<a class="last">董事長的話</a>';
			break;
		case "1-1": temp += home + arrow + '2015永續績效';
			break;			
		case "2-1":	temp += home + arrow + '永續議題管理' + arrow + '<a class="last">利害關係人溝通</a>';
			break;			
		case "3-1":	temp += home + arrow + '公司簡介' + arrow + '<a class="last">公司概況</a>';
			break;			
		case "3-2":	temp += home + arrow + '公司簡介' + arrow + '<a class="last">關聯企業</a>';
			break;			
		case "4-1":	temp += home + arrow + '公司治理' + arrow + '<a class="last">董事會運作</a>';
			break;
		case "4-2":	temp += home + arrow + '公司治理' + arrow + '<a class="last">審計委員會及薪酬委員會</a>';
			break;			
		case "4-3":	temp += home + arrow + '公司治理' + arrow + '<a class="last">誠信經營</a>';
			break;
		case "4-4":	temp += home + arrow + '公司治理' + arrow + '<a class="last">風險類型</a>';
			break;
		case "4-5":	temp += home + arrow + '公司治理' + arrow + '<a class="last">財務績效</a>';
			break;
		case "5-1":	temp += home + arrow + '低碳產品' + arrow + '<a class="last">產品介紹</a>';
			break;
		case "5-2":	temp += home + arrow + '低碳產品' + arrow + '<a class="last">研發創新</a>';
			break;
		case "5-3":	temp += home + arrow + '低碳產品' + arrow + '<a class="last">港廠電合一</a>';
			break;
		case "5-4":	temp += home + arrow + '低碳產品' + arrow + '<a class="last">餘熱發電</a>';
			break;
		case "5-5":	temp += home + arrow + '低碳產品' + arrow + '<a class="last">二氧化碳捕獲</a>';
			break;
		case "6-1":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">人力概況</a>';
			break;
		case "6-2":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">儲備幹部訓練</a>';
			break;
		case "6-3":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">安全照顧</a>';
			break;
		case "6-4":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">福利制度</a>';
			break;
		case "6-5":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">人才培育</a>';
			break;
		case "6-6":	temp += home + arrow + '最佳職場' + arrow + '<a class="last">工作生活平衡</a>';
			break;
		case "7-1":	temp += home + arrow + '環境管理' + arrow + '<a class="last">環境管理政策及投入</a>';
			break;
		case "7-2":	temp += home + arrow + '環境管理' + arrow + '<a class="last">溫室氣體管理</a>';
			break;
		case "7-3":	temp += home + arrow + '環境管理' + arrow + '<a class="last">原料使用</a>';
			break;
		case "7-4":	temp += home + arrow + '環境管理' + arrow + '<a class="last">能源管理</a>';
			break;
		case "7-5":	temp += home + arrow + '環境管理' + arrow + '<a class="last">水資源管理</a>';
			break;
		case "7-6":	temp += home + arrow + '環境管理' + arrow + '<a class="last">空氣汙染防制</a>';
			break;
		case "7-7":	temp += home + arrow + '環境管理' + arrow + '<a class="last">廢棄資源管理</a>';
			break;
		case "7-8":	temp += home + arrow + '環境管理' + arrow + '<a class="last">環境影響監測</a>';
			break;
		case "8-1":	temp += home + arrow + '夥伴關係' + arrow + '<a class="last">客戶服務</a>';
			break;
		case "8-2":	temp += home + arrow + '夥伴關係' + arrow + '<a class="last">供應商管理</a>';
			break;
		case "9-1":	temp += home + arrow + '社會參與' + arrow + '<a class="last">社會關懷</a>';
			break;			
		case "9-2":	temp += home + arrow + '社會參與' + arrow + '<a class="last">永續生態</a>';
			break;			
		case "9-3":	temp += home + arrow + '社會參與' + arrow + '<a class="last">藝文推廣</a>';
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

function weixinOpenClose(){
	var element = document.getElementById("weixinQR");
	var display = element.currentStyle ? element.currentStyle.display : getComputedStyle(element, null).display;

	if(display == "none")
		element.style.display = "block";
	else
		element.style.display = "none";
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

function showHidePhoneMenu(){
    var bodyH = document.getElementsByTagName("body")[0].offsetHeight;
    var elem = document.getElementById("phoneMenuBlock");
    var elemHeight = elem.offsetHeight;
    
    if(bodyH > elemHeight)
        elem.style.height = bodyH + "px";

    if(elem.style.display == "none" || elem.style.display == ""){
        document.getElementById("phoneMenuIcon").src = "img/phone_Menu_press.png";
        elem.style.display = "block";
    }else{
        document.getElementById("phoneMenuIcon").src = "img/phone_Menu.png";
        elem.style.display = "none";
    }
    
//	var domH = document.documentElement.clientHeight;
//	var bodyH = document.getElementsByTagName("body")[0].offsetHeight;
//	var headerH = document.getElementById("header").offsetHeight;
//	var blockH = document.getElementById("phoneMenuBlock").offsetHeight;
//    
//	if(blockH <= 0){
//		document.getElementById("phoneMenuIcon").src = "img/phone_Menu_press.png";
//		var height = 0;
//		var h = setInterval(function(){
//				document.getElementById("phoneMenuBlock").style.height = height + "px";
//				height += 10;
//
//				if(height >= domH){					
//					document.getElementById("phoneMenuBlock").style.height = "auto";
//					setTimeout(function(){
//						var temp = document.getElementById("phoneMenuBlock").offsetHeight;
//
//						if(temp < bodyH)
//							document.getElementById("phoneMenuBlock").style.height = bodyH - headerH + "px";					
//					}, 100);
//					
//					clearInterval(h);
//				}
//		}, 0);
//	}else{		
//		var height = domH - headerH;
//		document.getElementById("phoneMenuBlock").style.height = height + "px";
//		document.getElementById("phoneMenuIcon").src = "img/phone_Menu.png";
//		
//		var h = setInterval(function(){
//			if(height <= 0)
//				clearInterval(h);
//			
//			document.getElementById("phoneMenuBlock").style.height = height + "px";
//			height -= 10;			
//		}, 0);
//	}
}