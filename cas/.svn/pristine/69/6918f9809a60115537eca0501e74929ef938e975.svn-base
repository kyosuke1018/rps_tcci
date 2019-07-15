if(!String.prototype.trim) {
    String.prototype.trim = function() {
        return String(this).replace(/^\s+|\s+$/g, "");
    };
} 
function lowCaseConverter(){
    var jid = document.getElementById("username");
    var data = jid.value;
    jid.value = data.toLowerCase().trim();
    jid = document.getElementById("password");
    data = jid.value;
    jid.value = data.trim();
    return true;
}

$(function()
{
    $(window).on('resize', $.debounce(100, resize ));
    if(window.screen.width <= 1024)
    {
        $("#login .line").width(window.screen.width*0.38);
        if(window.screen.width <= 800)
        {
            $("#login .slogan1").css("font-size", 18 + "px");
            $("#login .slogan2").css("font-size", 28 + "px");
            $("#login .slogan1").css("margin-top", 8 + "px");
        }
    }
    document.getElementById("step").src = "images/eagle/step.png"; //解決IE因緩衝區而無法顯示圖片正確位置問題
    setTimeout(function()
    {
        resize();
    }, 100);
});

function resize() {
    //$("body").css("overflow", "hidden");
    var documentWidth = document.documentElement.clientWidth;
    var documentHeight = document.documentElement.clientHeight;
    scale = parseInt($("#step").css("height"), 10)/(273+20);
    $("#eagle").css("height", 170 * scale + "px");
    $("#ground").css("height", 136 * scale + "px");
    var bottom = parseInt($("#eagle").css("top"),10) + $("#eagle").height();
    var tops = documentHeight - parseInt($("#step").css("height"), 10);
    if (tops < bottom)
        tops = bottom;
    $("#step").css("top", tops + "px");
    if (documentWidth < parseInt($("#login").css("left"), 10) + $("#login").width())
        $("#ground").css("width", parseInt($("#login").css("left"), 10) + $("#login").width() + 50 + "px");
    else
        $("#ground").css("width","100%");
    var gtop = parseInt($("#step").css("top"),10) + $("#step").height()/2;
    $("#ground").css("top", gtop + "px");
    $("body").css("visibility", "visible");
//$("body").css("overflow", "auto");
}
