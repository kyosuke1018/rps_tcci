var xhr, status, args;

/* for primefaces component ajax oncomplete */
function onCompleteCheck(xhr, status, args) {
    if (status==='error' || status==='parsererror') {
        viewExpiredError();
        return false;
    } else if (status==='success') {
        return checkResponseXML(xhr.responseXML);
    }
    return true;
}

function onErrorCheck(xml) {
    var error = jQuery(xml).find('error-name').eq(0).text();
    var errorMsg = jQuery(xml).find('error-message').eq(0).text();
    var msg = EXCEPTION_MSG;
    msg = msg + "錯誤內容:"+error+"\n"+errorMsg+"\n";
    msg = msg + "Server IP:"+jQuery("#_serverIP_").text()+"\n";
    msg = msg + "進入功能時間:"+jQuery("#_systime_").text()+"\n";
    
    alert(msg);
}

/* for f:ajax onevent="handleAjaxEvent" */
function handleAjaxEvent(e) {
    if (e.status==='complete') {
        if (e.responseCode !== 200) {
            viewExpiredError();
            return false;
        } else {
            return checkResponseXML(e.responseXML);
        }
    }
    return true;
}

function checkResponseXML(xml) {
    var error = jQuery(xml).find('error-name').eq(0).text();
    if (error.indexOf('ViewExpiredException') > -1) {
        viewExpiredError();
        return false;
    } else if( error!=="undefined" && error!=="" ){
        onErrorCheck(xml);
        return false;
    } else {
        return true;
    }
}

function handleCallBack(xhr, status, args) {
    handleCallBack(xhr, status, args, true);
}

function handleCallBack(xhr, status, args, showDefMsg) {
    try{
        if( args.success !== undefined ){
            if( !args.success ){
                if( args.msg !== undefined && args.msg !== "" ){
                    alert(args.msg);
                }else{
                    if( showDefMsg ){
                        alert(EXCEPTION_MSG);
                    }
                }
            }else{
                return true;
            }
        }
    }catch(err){
        alert(CALL_BACK_NULL_MSG);
    }
    
    return false;
}