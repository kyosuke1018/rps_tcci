
function showDlg(selector, width, btnOhters){
    var buttonAry = [
        {
            text: utils.i18n("close"), // 關閉
            icon: 'fa-close',
            click: function() {
                $(selector).puidialog('hide');
            }
        }
    ];
    
    if( !utils.isEmptyAry(btnOhters) ){
        buttonAry.concat(btnOhters);
    }
    
    $(selector).puidialog({
        "visible": true,
        "width": width,
        "height": "auto",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        "buttons": buttonAry,
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }
    });

    $(selector).puidialog('show');
    console.log("showDlg ", selector);
}

function showAlertDlg(selector, msg, width, btnOhters){
    $(selector).attr("title", utils.i18n("hit.msg"));// 提示訊息
    
    var html = '<i class="fa fa-exclamation-triangle" ></i>&nbsp;' + msg;
    $(selector+"Msg").html(html);
    
    showDlg(selector, width, btnOhters);
}

function _alert(msg){
    showAlertDlg('#_dlgAlert', msg, 300);
}