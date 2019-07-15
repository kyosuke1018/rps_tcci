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
$(window).on('resize', $.debounce(100, resize ));
$(function()
{
    resize();
    //$.fn.snow({ minSize: 5, maxSize: 25, newOn: 1000, flakeColor: '#FFFFFF' }); //下雪
});	
function resize() {
    var documentWidth = document.documentElement.clientWidth;
    var bg_width = parseInt($("#background").css("width"), 10);
    var loginLeft=60;
    
    if(documentWidth > bg_width) {
        loginLeft=documentWidth/2 - bg_width/2 + loginLeft;
    }
    $("#login").css("left", loginLeft + "px");
    $("body").css("visibility", "visible");
}


