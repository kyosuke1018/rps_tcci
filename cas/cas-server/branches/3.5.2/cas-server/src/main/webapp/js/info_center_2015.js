if (!String.prototype.trim) {
    String.prototype.trim = function() {
        return String(this).replace(/^\s+|\s+$/g, "");
    };
}
function lowCaseConverter() {
    var jid = document.getElementById("username");
    var data = jid.value;
    jid.value = data.toLowerCase().trim();
    jid = document.getElementById("password");
    data = jid.value;
    jid.value = data.trim();
    return true;
}
function resize() {
    var documentWidth = document.documentElement.clientWidth;
    var documentHeight = document.documentElement.clientHeight;
    var bg_width = parseInt($("#background").css("width"), 10);
    var bg_height = parseInt($("#background").css("height"), 10);
    var login_width = parseInt($("#login").css("width"), 10);
    var login_height = parseInt($("#login").css("height"), 10);
    var loginLeft = 0;
    var loginTop = 0;
    /*--	
     alert(documentWidth);
     alert(bg_width);
     alert(documentHeight);
     alert(bg_height);
     alert(login_width);
     alert(login_height);
     */
    if (documentWidth > bg_width) {
        loginLeft = documentWidth / 2 - login_width / 2;
    } else {
        loginLeft = bg_width / 2 - login_width / 2;
    }
    loginTop = bg_height / 2 - login_height / 2 - 118;

    $("#login").css("left", loginLeft + "px");
    $("#login").css("top", loginTop + "px");
    $("body").css("visibility", "visible");
}
$(window).on('resize', $.debounce(100, resize));
$(function()
{
    resize();
    //$.fn.snow({ minSize: 5, maxSize: 25, newOn: 1000, flakeColor: '#FFFFFF' }); //下雪
});



