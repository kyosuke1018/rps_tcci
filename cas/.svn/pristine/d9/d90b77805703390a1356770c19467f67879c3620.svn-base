function getViewportHeight() 
{
    if (document.documentElement.clientHeight)
    {
        return document.documentElement.clientHeight;
    }
    else if (document.body && document.body.offsetHeight)
    {
        return document.body.offsetHeight;
    }
    else
    {
        return 0;
    }
}
                
function getViewportWidth() 
{
    if (document.documentElement.clientWidth)
    {
        return document.documentElement.clientWidth;
    }
    else if (document.body && document.body.offsetWidth)
    {
        return document.body.offsetWidth;
    }
    else
    {
        return 0;
    }
}

function resize() {
    setTimeout(function(){
        var documentWidth = getViewportWidth();
        var documentHeight = getViewportHeight();
        //window.screen.width  螢幕寬度
        //window.screen.height 螢幕高度
        //window.screen.availWidth  扣掉工作列可用寬度
        //window.screen.availHeight 扣掉工作列可用高度
        if(documentWidth<1024) {
            documentWidth=1024;
            $("#building table").css("width","1024px");
            $("#ground").css("width", "1024px");
        }else {
            $("#building table").css("width","100%");
            $("#ground").css("width", "100%");
        }
        if(documentHeight<600) {
            documentHeight=600;
        }
            
        $("#fu").css("width", "100%");
        $("#fu").css("height", "100%");

        $("#building table .logo").css("width", documentWidth*0.42 + "px");
        $("#building table .snake").css("width", documentWidth*0.23 + "px");
		
        //若圖片過大超過螢幕顯示範圍作修正, 讓logo圖大小為螢幕寬度的一半
//        logoWidth = parseInt($("#building table .logo").css("width"), 10);
        if(logoWidth > (documentWidth/2))
        {			
            $("#building table .logo").css("width", documentWidth/2 + "px");			
            scale = documentWidth/2/503;								 //503為logo圖原始寬度
            $("#building table .snake").css("width", 271*scale + "px");	 //271為snake圖原始寬度
        }
        $("#building table .logo").css("height", documentHeight*0.78 + "px");
        $("#building table .snake").css("height", documentHeight*0.75 + "px");
        
        	
        scale = parseInt($("#building table .logo").css("height"), 10) / 434; //434為logo圖原始高度		
	    $("#building table .logo").css("height", 434*scale + "px");  //434為logo圖原始高度
            $("#building table .snake").css("height", 434*scale + "px"); //434為snake圖原始高度
		
        
        $("#ground").css("height", 81*scale + "px");  //81為ground原始高度
        $("#jewelry").css("height", 80*scale + "px"); //80為ground原始高度		
        $("#ouplets").css("height", 83*scale + "px");  //83為ouplets原始高度
        $("#firecracker1").css("height", 338*(scale+0.2) + "px"); //338為firecracker1原始高度
        $("#firecracker2").css("height", 338*(scale+0.2) + "px"); //338為firecracker1原始高度


        $("#building table").css("margin-top", ( documentHeight - parseInt($("#building table").css("height"), 10) ) + "px" );
        $("#ground").css("margin-top", ( documentHeight - parseInt($("#ground").css("height"), 10) ) + "px" );
        $("#jewelry").css("margin-top", ( documentHeight - parseInt($("#jewelry").css("height"), 10) - 5 ) + "px" );		
        $("#ouplets").css("left", (documentWidth/2 - parseInt($("#ouplets").css("width"), 10)/2) + "px");		
        if(parseInt($("#ouplets").css("left"))<350) {
            $("#ouplets").css("left","350px");
        }
        $("#ouplets").css("top",documentHeight*0.07+"px");
        if(parseInt($("#ouplets").css("top"))<50) {
            $("#ouplets").css("top","50px");
        }
        $("#firecracker1").css("left", 25 + "px");
        $("#firecracker2").css("left", (documentWidth - parseInt($("#firecracker2").css("width"), 10) - 25) + "px");
        if(parseInt($("#firecracker2").css("left"))<900) {
            $("#firecracker2").css("left","900px");
        }
        $("#login").css("left",documentWidth*0.58+"px");
        if(parseInt($("#login").css("left"))<580) {
            $("#login").css("left","580px !important");
        }
        $("#login").css("top",documentHeight*0.3+"px");
        if(parseInt($("#login").css("top"))<180) {
            $("#login").css("top","180px");
        }
        //
        $("#jewelry").css("left",documentWidth*0.57+"px");
        if(parseInt($("#jewelry").css("left"))<580) {
            $("#jewelry").css("left","580px");
        }
        
        
        if($.browser.chrome)
        {
            $("#login .left .space").css("height", "0px");
        }
        $("#building").css("visibility","visible");
    },500);
}