/* global ST_MSG_COUNT_URL, TEST_DATA, genCreateTimeContent, ST_MSG_LIST_URL, ST_MSG_REPLY_URL, ST_MSG_REPLY_LIST_URL, utils */
var voMM = {// variable objects in member-msg.js
    replyMsgId:null
};

//<editor-fold defaultstate="collapsed" desc="for Member Message">
function changeReplyMsg(){
    var formData = {};
    if( getCheckbox("#replyMsg") ){
        formData['replyMsg'] = getCheckbox("#replyMsg");
    }
    console.log("changeReplyMsg ...");
    buildMemberMsg(formData, "memberMsg");
}

// 訪客留言
function buildMemberMsg(formData, ajaxKey){
    utils.postData(ST_MSG_COUNT_URL, formData, false, fetchMemberMsgLazy, null, ajaxKey);
}

function fetchMemberMsgLazy(response, formData, ajaxKey){
    var totalRows = response.res.totalRows;

    renderMemberMsgsLazy(formData, totalRows, false, ajaxKey);
}

function renderMemberMsgsLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 600;
    var marginWidth = 80;
    var columns = [
            {field: "replyCount", headerText: utils.i18n("fd.replyCount"), sortable: false, content: genReplyContent, bodyClass:"tac"},
            {field: "createtime", headerText: utils.i18n("fd.msg.createtime"), sortable: false, content: genCreateTimeContent},
            {field: "type", headerText: utils.i18n("fd.obj.type"), sortable: false, content: genMsgTypeContent},
            {field: "prdName", headerText: utils.i18n("fd.prdName"), sortable: false, content: genPrdContent},
            {field: "memberName", headerText: utils.i18n("fd.memberName"), sortable: false, content: genMemberContent},
            {field: "message", headerText: utils.i18n("fd.message"), sortable: false, bodyStyle:"min-width:200px"}
        ];
        
    renderSearchResultNum("#mmSearchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbMemberMsg', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbMemberMsg', pageSize, totalRows, columns, ST_MSG_LIST_URL, 
            !TEST_DATA, formData, null, retry, ajaxKey, minWidth, marginWidth);
    }
}

// 新增回覆
function onClickAddReply(msgId){
    console.log("addReply ...", msgId);
    voMM.replyMsgId = msgId;
    $('#dlgMsgReply').puidialog({
        "visible": true,
        "width": "300",
        "height": "200",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        buttons: [{
                    text: utils.i18n("close"),
                    icon: 'fa-close',
                    click: function() {
                        $('#dlgMsgReply').puidialog('hide');
                    }
                },{
                    text: utils.i18n("send"),
                    icon: 'fa-envelope-o',
                    click: function() {
                        onClickReplyMessage();
                    }
                }],
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }
    });

    $("#divMsgReply").show();
}

function onClickReplyMessage(){
    console.log("replyMessage ...", getTextValue("#replyMessage"));
    if( isNA(voMM.replyMsgId) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("in.err"));
        return;
    }

    var formData = {
        "parent": voMM.replyMsgId,
        "message": getTextValue("#replyMessage")
    };
    var restUrl = ST_MSG_REPLY_URL;
    utils.postData(restUrl, formData, false, afterReplyMsgs, null);
}

function afterReplyMsgs(response, formData, optional){
    console.log("afterReplyMsgs response = \n", response);
    $('#dlgMsgReply').puidialog('hide');

    reloadDataTable('#tbMemberMsg', true);// keep status reload    
    utils.addSuccessMsg();
    voMM.replyMsgId = null;
}

// 查看回覆
function onClickViewReply(parent){
    console.log("onClickViewReply ...", parent);
    var restUrl = ST_MSG_REPLY_LIST_URL+"?parent="+parent;
    utils.fetchData(restUrl, false, renderReplyMsgs, null);
}

function renderReplyMsgs(response){
    if( !utils.checkResponse(response) ){
        return;
    }
    var columns = [
            {field: "createtime", headerText: utils.i18n("fd.createtime"), sortable: false, content: genCreateTimeContent},
            {field: "memberName", headerText: utils.i18n("fd.memberName"), sortable: false, content: genMemberContent},
            {field: "message", headerText: utils.i18n("fd.message"), sortable: false, bodyStyle:"min-width:200px"}
        ];
    renderSearchResultNum("#mrSearchResultMsg", response.rowNum);
    renderDataTable("#tbMsgReplys", null, columns, utils.getResponseList(response), null, null, null);
    
    $('#dlgMsgReplyList').puidialog({
        "visible": true,
        "width": "auto",
        "height": "auto",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        buttons: [{
                    text: utils.i18n("close"),
                    icon: 'fa-close',
                    click: function() {
                        $('#dlgMsgReplyList').puidialog('hide');
                    }
                }],
        "beforeShow":function(event) {
            utils.hideBodyScrollY();
        },
        "afterHide":function(event) {
            utils.showBodyScrollY();
        }
    });
}

function genReplyContent(data){
    var html = '<button onclick="onClickAddReply('+data.id+')" title="'+utils.i18n("fd.new.reply")+'" class="w3-btn w3-teal" ><i class="fa fa-plus-square"></i></button>';
    
    if( !isNA(data.replyCount) && data.replyCount>0 ){
        html += '<button onclick="onClickViewReply('+data.id+')" title="'+utils.i18n("fd.view.reply")+'" class="w3-btn w3-deep-orange" style="margin-left:2px;" >'
             + '<i class="fa fa-eye"></i>&nbsp;('+data.replyCount+')'
             + '</button>';
    }
    return html;
}
function genMsgTypeContent(data){
    return data.type==='P'?utils.i18n("fd.forprd"):utils.i18n("fd.forstore");
}
function genMemberContent(data){
    return data.memberName+"("+data.loginAccount+")";
}
function genPrdContent(data){
    return data.prdName;
}
//</editor-fold>
