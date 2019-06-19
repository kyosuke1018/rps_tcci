/* global URL_DOC_VIEW */
var LANG = "C";
var realType = ""; // for preview

$(function(){        
    $("#header").html(getHeader());
    $("#footer").html(getFooter());
    $("#webMap").html(getWebMap());
                
    showLoadingBefore("#footerField");// loading
                
    var id = GetUrlString("id");//1;
    var pubType = GetUrlString("pubType");
    var code = GetUrlString("code");
    var resturl;
    if( pubType==='NA' ){// for preview
        resturl = URL_DOC_VIEW+pubType+"/"+code;
        realType = GetUrlString("realType");
    }else{
        resturl = URL_DOC_VIEW+id;
    }
    console.log(resturl);
    fetchData(resturl, false);
});
            
function renderPage(response){
	$("#caption").html(escapeHtml(response.title));
    $(".mediaArticle").append('<div style="font-size:20px; font-weight:bold; color:#008766">' + escapeHtml(response.summary) + '</div>');
    $(".mediaArticle").append(safePrint(response.contents));// 此處不可用escapeHtml
    //$(".mediaArticle img").css("width", getDomWidth()<980 ? "100%":"60%");
    //$(".mediaArticle img").css("margin", "0 20%");
              
    var pubType = (realType!==undefined && realType!=='')? realType:response.type;
                
    setTimeout(function(){
        adjArticelImg();
                
        $(".mediaArticle table").before($("<div/>").attr("class","tableWrap").attr("width","100%").css("overflow-x","auto"));
        $(".mediaArticle table").each(function(index){
            $(".tableWrap:eq(" + index + ")").prepend($(this));
        });
        
        $(".mediaArticle img").each(function(){
            var url = $(this).attr("src");
            $(this).before($("<a/>").attr("class", "openImg").attr("href", url).attr("target", "_blend"));
        });
        $(".mediaArticle img").each(function(index){
            $(".openImg:eq(" + index + ")").prepend(this);
        });
    }, 500);

    window.onresize = function(){
        adjArticelImg();                
    };
            
    setTimeout(function(){
        adjContainerHeight();
    }, 100);
                
    hideLoading();// 隱藏 loading image
}
            
function adjArticelImg(){
    var w = getDomWidth();
    if( w<980 ){
        $(".mediaArticle img").each(function(){
            // console.log("attr width = "+$(this).attr("width"));
            if( w > $(this).attr("width") ){
                $(this).css("width",  $(this).attr("width") + "px");
                $(this).css("height",  $(this).attr("height") + "px");
            }else{
                $(this).css("width",  "100%");
                $(this).css("height", "auto");
                //$(this).css("margin", "0");
            }
        });
    }else{
        $(".mediaArticle img").each(function(){
            $(this).css("width",  $(this).attr("width") + "px");
            $(this).css("height",  $(this).attr("height") + "px");
        });
    }
}