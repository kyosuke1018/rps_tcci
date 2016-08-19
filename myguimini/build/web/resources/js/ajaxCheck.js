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
