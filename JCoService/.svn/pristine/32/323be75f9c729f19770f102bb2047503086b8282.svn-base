var xhr, status, args;
var CALL_BACK_NULL_MSG = "網頁已過期，請按[F5]鍵重新整理後再試。";
var EXCEPTION_MSG = "系統發生錯誤，請 Email 此畫面給系統管理員，\n並告知執行動作，以利盡速為您排除問題。\n\n";
var ERROR_MSG = "\n\n(重新整理後仍無法執行，請 Email 此畫面給系統管理員，\n並告知執行動作，以利儘速為您排除問題，謝謝。)\n";

function alertErrorMsg(){
    alert(CALL_BACK_NULL_MSG + ERROR_MSG);
}

/* for primefaces component ajax oncomplete */
function onCompleteCheck(xhr, status, args) {
    if (status=='error' || status=='parsererror') {
        viewExpiredError();
        return false;
    } else if (status=='success') {
        return checkResponseXML(xhr.responseXML);
    }
    return true;
}

/* for f:ajax onevent="handleAjaxEvent" */
function handleAjaxEvent(e) {
    if (e.status=='complete') {
        if (e.responseCode != 200) {
            viewExpiredError();
            return false;
        } else {
            return checkResponseXML(e.responseXML);
        }
    }
    return true;
}

function viewExpiredError() {
    alert('網頁已過期，請按[F5]鍵重新整理後再試。');
}

function checkResponseXML(xml) {
    var error = jQuery(xml).find('error-name').eq(0).text();
    if (error.indexOf('ViewExpiredException') > -1) {
        viewExpiredError();
        return false;
    } else {
        return true;
    }
}
