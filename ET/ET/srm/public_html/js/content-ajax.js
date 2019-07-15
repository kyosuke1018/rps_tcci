//------------------------------ URL (start) ------------------------------//
var REST_URL = "http://192.168.203.51/et-web/";
//------------------------------ URL (end) ------------------------------//

//------------------------------ Cookie (start) ------------------------------//
//HTML5 - sessionStorage
//var session = sessionStorage.getItem("tccEbiddingLoginAcct");
//function logout(){
//    sessionStorage.removeItem("tccEbiddingLoginAcct");
//    sessionStorage.removeItem("tccEbiddingMemberId");
//    location.href = "index.html";
//}

//cookie
var SESSION = readCookie("tccEbiddingLoginAcct");
var MEMBER_ID = readCookie("tccEbiddingMemberId");

//建立
function createCookie(name, value, days, path) {
    if(days){
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    }
    else var expires = ";";
    
    document.cookie = name + "=" + value + expires;
}

//讀取
function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');

    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

//刪除
function eraseCookie(name) {
    document.cookie = name + "=; expires=-1";
}

function logout(){
    eraseCookie("tccEbiddingLoginAcct");
    eraseCookie("tccEbiddingMemberId");
    location.href = "index.html";
}
//------------------------------ Cookie (end) ------------------------------//

//------------------------------ Ajax (start) ------------------------------//
//Common Get
function ajaxGetData(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);
    
    $.ajax({
        type: "GET",
        url: restUrl,
        data: submitData,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            console.log("postData success "+restUrl+"\n", formData, response);
            fetchData(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}

//Common Post
function ajaxPostData(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);

    $.ajax({
        type: "POST",
        url: restUrl,
        data: submitData,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            console.log("postData success "+restUrl+"\n", formData, response);
            fetchData(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}



//Vender List
function ajaxGetVender(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);
    
    $.ajax({
        type: "GET",
        url: restUrl,
        data: submitData,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            console.log("postData success "+restUrl+"\n", formData, response);
            fetchVenderData(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}

//Vender Count
function ajaxPostVenderCount(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);

    $.ajax({
        type: "POST",
        url: restUrl,
        data: null,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            //console.log("postData success "+restUrl+"\n", formData, response);
            fetchVenderCount(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}

//Tender Count
function ajaxPostTenderCount(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);

    $.ajax({
        type: "POST",
        url: restUrl,
        data: null,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            //console.log("postData success "+restUrl+"\n", formData, response);
            fetchTenderCount(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}

//Bind New Vender Option
function ajaxGetBindVenderOptionData(restUrl, formData){
    //formData = formData?{}:formData;
    var submitData = JSON.stringify(formData);

    $.ajax({
        type: "GET",
        url: restUrl,
        data: null,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        crossDomain : true,
        success: function (response, status, jqXHR) {
            //console.log("postData success "+restUrl+"\n", formData, response);
            fetchBindVenderOption(response);
        },
        error: function (response) {
        },
        complete: function(jqXHR, textStatus){
        }
    });
}
//------------------------------ Ajax (end) ------------------------------//