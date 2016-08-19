function checkIE7Plus() {
   
    browsername=navigator.appName;
    if (browsername.indexOf("Microsoft")!=-1) {
        browsername="MSIE"
       // alert('browsername'+browsername);
    } else {
        return;
    }
    //detect the browserversion
    browserversion = 7;
    if (navigator.appVersion.indexOf("MSIE 2.")!=-1) {browserversion = 2;}
    if (navigator.appVersion.indexOf("MSIE 3.")!=-1) {browserversion = 3;}
    if (navigator.appVersion.indexOf("MSIE 4.")!=-1) {browserversion = 4;}
    if (navigator.appVersion.indexOf("MSIE 5.")!=-1) {browserversion = 5;}
    if (navigator.appVersion.indexOf("MSIE 6.")!=-1) {browserversion = 6;}
    if (navigator.appVersion.indexOf("MSIE 7.")!=-1) {browserversion = 7;}
    if (browserversion < 7){
       // document.getElementById("loginButton").disabled = "true";
        message = "本系統不支援舊版IE瀏覽器, 請更新IE版本至少7.0以上, 若有任何問題請洽台訊客服中心25366660 分機20990";
        alert(message);
    }
}
function lowCaseConverter(){
    var jid = document.getElementById("j_username");
    var data = jid.value;
    jid.value =  jQuery.trim(data.toLowerCase() );
    //alert("'" + jid.value +"'");
    jid = document.getElementById("j_password");
    data = jid.value;
    jid.value =  jQuery.trim(data);
    //alert("'" + jid.value +"'");
    return true;
}


