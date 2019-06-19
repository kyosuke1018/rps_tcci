function nativeJsCheck(){
    var lang = navigator.userLanguage;
    if( lang!==undefined && lang.toLowerCase().indexOf("tw")>=0 ){
        document.getElementById("ieOnly").innerText = "本系統不支援您使用的瀏覽器。請使用 IE10 之後的版本，或使用 Chrome、Firefox 等其他瀏覽器。";
    }else{
        document.getElementById("ieOnly").innerText = "本系统不支援您使用的浏览器。请使用 IE10 之后的版本，或使用 Chrome、Firefox 等其他浏览器。";
    }
}

window.load=nativeJsCheck();

