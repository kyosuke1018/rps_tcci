function viewExpiredError() {
    PF('growlWV').renderMessage({"summary": "網頁已過期，請按[F5]鍵重新整理後再試。",
        "severity": "error"});    
}
function requestError() {
    PF('growlWV').renderMessage({"summary": "無法處理request，請稍後再試!",
        "severity": "warn"});    
}
function internalError() {
    PF('growlWV').renderMessage({"summary": "內部程式錯誤，請檢查輸入的資料是否正確!",
        "severity": "warn"});    
}
var originAjax = $.ajax;
$.ajax = function(options) {
    var complete = options.complete;
    var context = options.context ? options.context : $;
    options.complete = function(xhr, status) {
        if (complete) {
            complete.apply(context, arguments);
        }
        if ('success' === status) {
            //if (/^<\?xml version='1.0' encoding='UTF-8'\?>/.test(xhr.responseText)) {
                var error = '';
                try {
                    error = $($.parseXML(xhr.responseText)).find('error-name').eq(0).text();
                } catch (err) {
                }
                if (error.indexOf('ViewExpiredException') > -1) {
                    viewExpiredError();
                } else if ('' !== error) {
                    internalError();
                }
            //}
        } else if (/\/cas-server\/login/.test(xhr.responseText)) {
            viewExpiredError();
        } else {
            requestError();
        }
    };
    return originAjax.apply($, arguments);
};
