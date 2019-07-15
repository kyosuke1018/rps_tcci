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
        loginLeft = documentWidth-(documentWidth / 4 - 255);
    } else {
        loginLeft = bg_width-(bg_width / 2 - 255);
    }
    loginTop = bg_height / 3 - login_height / 2 - 95;

    $("#login").css("left", loginLeft + "px");
    $("#login").css("top", loginTop + "px");
    $("body").css("visibility", "visible");
}
function resize2() {
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
        loginLeft = documentWidth-(documentWidth / 4 - 255);
    } else {
        loginLeft = bg_width-(bg_width / 2 - 255);
    }
    loginTop = bg_height / 3 - login_height / 2 - 85;

    $("#login").css("left", loginLeft + "px");
    $("#login").css("top", loginTop + "px");
    $("body").css("visibility", "visible");
}



