// 檢查IE版本
$(document).ready(function() {
    var miniIEVersion = 8;
    var maxIEVersion = 9;
    var msg;
    if ($.browser.msie && parseInt($.browser.version, 10) < miniIEVersion) {
        var agentStr = navigator.userAgent;
        if (agentStr.indexOf("Trident") > -1) {
            msg = "您的IE瀏覽器現在是相容性檢視狀態，請於主選單的 '工具'=>'相容性檢視設定' 中移除 'taiwancement.com'!";
        } else {
            msg = "本系統不支援舊版IE瀏覽器, 請更新IE版本至少 " 
            + miniIEVersion + " 以上,\n若有任何問題請洽台訊客服中心25366660 分機20990";
        }
        alert(msg);
    }else if ($.browser.msie && parseInt($.browser.version, 10) > maxIEVersion) {
        msg = "本系統不支援目前您使用的 IE 瀏覽器版本, \n若有任何問題請洽台訊客服中心25366660 分機20990";
        alert(msg);
    }
});
