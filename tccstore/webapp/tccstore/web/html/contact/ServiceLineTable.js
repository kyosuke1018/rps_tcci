function ServiceLineTable(fileName){
	var domWidth = document.documentElement.clientWidth;
	var domHeight = document.documentElement.clientHeight;
	var lines;

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

	function initial(){
		var elem, nodeTr, node, nodeImg;

		elem = document.getElementsByTagName("body")[0];

		node = document.createElement("table");
		node.id = "phoneNumberTable";
		node.style.width = "100%" ;
		node.style.maxWidth = "800px";
		node.style.margin = domWidth > 768 ? "30px auto 0" : "0 auto";
		node.style.border = domWidth > 768 ? "solid 1px #e0e0e0" : "none";
		node.style.borderRadius = isMobile.any() == null ? "5px" : "none";
		node.style.borderCollapse = "0";
		node.style.borderSpacing = "0";
		node.style.textAlign = "center";
		node.style.fontSize = "14px";
		node.style.boxShadow = domWidth > 768 ? "1px 1px 10px #e0e0e0" : "none";
		document.getElementsByTagName("body")[0].appendChild(node);

		nodeTr = document.createElement("tr");
		for(i in lines[0].split(/\s+/)){
			node = document.createElement("th");
			node.innerText = lines[0].split(/\s+/)[i];
			node.style.padding = domWidth > 768 ? "10px 10px" : "5px 10px";
			node.style.background = "#e0e0e0";                    
			nodeTr.appendChild(node);
		}
		if(isMobile.any() != null){
			node = document.createElement("th");
			node.style.width = "50px";
			node.style.background = "#e0e0e0";
			nodeTr.appendChild(node);
		}
		document.getElementById("phoneNumberTable").appendChild(nodeTr);				

		for(var i=1; i<lines.length; i++){
			var phoneNum;
			nodeTr = document.createElement("tr");

			for(j in lines[i].split(/\s+/)){
				var data = lines[i].split(/\s+/)[j];
				if(data != ""){
					node = document.createElement("td");
					node.alt = data;
					node.innerText = data;
					if(j == 1 && isMobile.any() != null){
						phoneNum = data;
						node.onclick = function(){ dial(this) };
					}							
					node.style.padding = "15px 10px";
					node.style.borderBottom = "solid 1px #f0f0f0";
					node.style.fontFamily = "Arial";
					node.style.color = "#333";
					nodeTr.appendChild(node);
				}
			}

			if(isMobile.any() != null){
				node = document.createElement("td");
				node.style.width = "50px";
				node.style.borderBottom = "solid 1px #f0f0f0";
				nodeImg = document.createElement("img");
				nodeImg.src = "img/icon_phone.png";
				nodeImg.alt = phoneNum;
				nodeImg.onclick = function(){dial(this)};
				nodeImg.style.width = "30px";
				node.appendChild(nodeImg);
				nodeTr.appendChild(node);
			}

			document.getElementById("phoneNumberTable").appendChild(nodeTr);
		}				
	}

	function slideNote(){
		var headerH = (document.getElementById("header").offsetHeight);
		var node = document.createElement("div");
		node.id = "slideNote";
		node.innerText = "点选 【电话号码】 或 【图示】 可进入拨号画面";
		node.style.width = "100%";
		node.style.padding = "5px 10px";
		node.style.position = "absolute";
		node.style.boxSizing = "border-box";
		node.style.top = headerH + "px";
		node.style.textAlign = "right";
		node.style.fontSize = "14px";
		node.style.color = "#fff";
		node.style.background = "#000";
		node.style.opacity = "0.75";
		document.getElementsByTagName("body")[0].appendChild(node);

		var closeNote;
		var pos = 0;
		var opacity = 0.8;
		setTimeout(function(){
			clearInterval(closeNote);
			closeNote = setInterval(function(){                        
				document.getElementById("slideNote").style.left = pos + "px";
				document.getElementById("slideNote").style.opacity = opacity;
				pos -= 1;
				opacity -= 0.003;
				if(pos == -domWidth) clearInterval(closeNote); 
			}, 0);
		}, 5000);
	}

	function getFile(){
		var txtFile;

		if (window.XMLHttpRequest)
			txtFile = new XMLHttpRequest(); // for IE7+, Firefox, Chrome, Opera, Safari
		else
			txtFile = new ActiveXObject("Microsoft.XMLHTTP"); // for IE6, IE5				

		txtFile.open("GET", fileName, false);
		txtFile.send(null);

		var txtDoc = txtFile.responseText;				
		lines = txtDoc.split("\r\n");				
	}

	function dial(obj){
		location.href = "tel:" + obj.alt.replace("-", "");
	}

	function rwdStyle(){
		if(document.documentElement.clientWidth > 768){
			document.getElementById("phoneNumberTable").style.margin = "30px auto 0";
			document.getElementById("phoneNumberTable").style.border = "solid 1px #e0e0e0";
			document.getElementById("phoneNumberTable").style.shadow = "1px 1px 10px #e0e0e0";
		}else{
			document.getElementById("phoneNumberTable").style.margin = "0 auto";
			document.getElementById("phoneNumberTable").style.border = "none";
			document.getElementById("phoneNumberTable").style.shadow = "none";
		}
	}

	window.addEventListener("resize", function(){
		rwdStyle();
	}, false);

	window.addEventListener("orientationchange", function(){
		rwdStyle();
	}, false);

	getFile();
	initial();	
	if(isMobile.any()){ slideNote(); }
}