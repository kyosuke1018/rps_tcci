/* global _asyncCompleteFlags, PO_COUNT_URL, MSG_NO_RECORD_FOUND, PO_LIST_URL, DATATABLE_RWD_WIDTH, PO_PROCESS_URL, PO_FULL_URL, PO_ITEMS_LIST_URL, PO_PROCESS_SAVE_URL, PO_PROCESS_DEL_URL, genCreateTimeContent, selectedOrder, RFQ_SAVE_URL, RFQ_COUNT_URL, RFQ_LIST_URL, RFQ_MESSAGE_SAVE_URL, RFQ_MESSAGE_DEL_URL, RFQ_STATUS_QUOTATION, RFQ_STATUS_INQUIRY, RFQ_STATUS_REJECT, QUOTE_TOTAL, PO_QUOTE_URL, _tccDealer, utils */
var voRC = {// variable objects in rfq-common.js
    rfqMessageFL:null, // FormListS object for variant
    selectedRfq:null
};

//<editor-fold defaultstate="collapsed" desc="for Rfq Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildRfqListLazy(formData, ajaxKey){
    formData = isNA(formData)?{}:formData;
    console.log("buildRfqListLazy formData = ", formData);
    
    // 先取得總筆數 for 分頁
    var restUrl = RFQ_COUNT_URL;// 先查總筆數
    utils.postData(restUrl, formData, false, fetchRfqListLazy, null, ajaxKey);
}

function fetchRfqListLazy(response, formData, ajaxKey){
    console.log("fetchRfqListLazy response = ", response);
    var totalRows = response.res.totalRows;
    
    renderRfqListLazy(formData, totalRows, false, ajaxKey);
}

function renderRfqListLazy(formData, totalRows, retry, ajaxKey){
    var pageSize = 10;
    var minWidth = 1200;
    var marginWidth = 80;
    var columns = [
            {field: "id", headerText: "ID", sortable: true, bodyClass:"tar"},
            {field: "statusLabel", headerText: utils.i18n("fd.statusLabel"), sortable: true, content: genStatusContent},
            {field: "orderNumber", headerText: utils.i18n("fd.orderNumber"), sortable: true},
            {field: "createtime", headerText: utils.i18n("fd.rfq.createtime"), sortable: true, content: genCreateTimeContent, scrollable:true},
            {field: "itemCount", headerText: utils.i18n("fd.itemCount"), sortable: true, bodyClass:"tar"},
            {field: "total", headerText: utils.i18n("fd.total"), sortable: true, bodyClass:"tar required", content: genTotalContent},
            {field: "subTotal", headerText: utils.i18n("fd.subTotal"), sortable: true, bodyClass:"tar", content: genSubTotalContent},
            {field: "shippingTotal", headerText: utils.i18n("shipping.price"), sortable: true, bodyClass:"tar", content: genShippingTotalContent},
            {field: "curName", headerText: utils.i18n("fd.curName"), sortable: true},
            {field: "cname", headerText: utils.i18n("fd.cus.cname"), sortable: true},
            {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
            {field: "email1", headerText: utils.i18n("fd.contact.email1"), sortable: true},
            {field: "message", headerText: utils.i18n("fd.order.memo"), sortable: true, content: genContentNote}
        ];

    renderSearchResultNum("#rfqSearchResultMsg", totalRows);
    if( totalRows===0 ){
        clearDataTable('#tbRfqLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbRfqLazy', pageSize, totalRows, columns, RFQ_LIST_URL, 
            true, formData, onSelectRfq, retry, "rfqList", minWidth, marginWidth);
    }
}

function genStatusContent(row){
    console.log("genStatusContent row.status = "+row.status);
    return (row.status===RFQ_STATUS_INQUIRY)?
            "<span style='color:red'>"+row.statusLabel+"<span>":(
                (row.status===RFQ_STATUS_QUOTATION)?
                "<span style='color:green'>"+row.statusLabel+"<span>":
                "<span style='color:gray'>"+row.statusLabel+"<span>"
            );
}
function genTotalContent(row){
    return utils.printNumber(row.total);
}
function genSubTotalContent(row){
    return utils.printNumber(row.subTotal);
}
function genShippingTotalContent(row){
    return utils.printNumber(row.shippingTotal);
}
function genContentNote(data){
    return genContentHtml(data, "note");
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Rfq Select">
// 選取訂單
function onSelectRfq(row){
    if( isNA(row.id) ){
        console.log("onSelectRfq row = ", row);
        return;
    }
    var restUrl = PO_FULL_URL.replace("{id}", row.id);
    utils.fetchData(restUrl, false, buildRfqDetail, null, null);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Rfq Detail">
// 訂單詳細資訊
function buildRfqDetail(data){
    //console.log("buildRfqDetail data = ", data);
    voRC.selectedRfq = data;
    var pageSize =10;
    
    // 訂單詳細資料
    $("#rfq_number").html(utils.prettyPrint(data.orderNumber));// 訂單編號
    $("#rfq_status").html(utils.prettyPrint(data.statusLabel));// 訂單狀態
    $("#rfq_time").html(genCreateTimeContent(data));// 訂單時間
    $("#rfq_account").html(utils.prettyPrint(data.loginAccount));// 客戶loginAccount
    $("#rfq_cus").html(utils.prettyPrint(data.cname));// 客戶名稱
    $("#item_num").html(utils.printNumber(data.itemCount));// 商品筆數
    $("#total_price").html(utils.printNumber(data.subTotal));// 貨款金額
    $("#total_discount").html(utils.printNumber(data.discountTotal));// 折扣金額
    $("#rfq_message").html(utils.prettyPrint(data.message));// 特殊需求
    $("#rfq_delivery_date").html(utils.prettyPrint(utils.formatDateStr(data.deliveryDate)));// 期望交貨日期

    // 報價
    $("#shippingTotal").val(utils.printNumber(isNA(data.shippingTotal)?0:data.shippingTotal));// 運費
    $("#subtotal_pay").val(utils.printNumber(data.subTotal));// 報價總金額
    if( !QUOTE_TOTAL ){
        disableInput("#subtotal_pay");
    }
    $("#spanTotalPrice").html(utils.printNumber(data.total));// 商品總金額
    
    createBtn("#sendTotalPrice", "fa fa-envelope-o", onClickSendPrice);// 確定提供報價
    createBtn("#rejectSendPrice", "fa fa-ban", onClickRejectSendPrice);// 拒絕提供報價
    createBtn("#viewPriceHis", "fa fa-history", onClickViewPriceHis);// 歷史報價記錄
    
    if( data.status!==RFQ_STATUS_INQUIRY ){// [買方詢價狀態]才顯示
        disableBtn("#sendTotalPrice");
        disableBtn("#rejectSendPrice");
    }else{
        enableBtn("#sendTotalPrice");
        enableBtn("#rejectSendPrice");
    }

    // 購買商品清單
    itemlist = utils.safeList(data.items);
    renderRfqItems(pageSize, itemlist);

    // 運送資訊
    $("#shipping_method").html(utils.prettyPrint(data.shipping));
    $("#shipping_contact1").html(utils.prettyPrint(data.recipient));
    $("#shipping_phone1").html(utils.prettyPrint(data.phone));
    $("#carNo").html(utils.prettyPrint(data.carNo));
    $("#driver").html(utils.prettyPrint(data.driver));
    $("#shipping_addr").html(utils.prettyPrint(data.address));
    $("#shipping_note").html(utils.prettyPrint(data.shippingNote));

    // 付款資訊
    $("#payType").html(utils.prettyPrint(data.payment));
    $("#gateway").html(utils.prettyPrint(data.gateway));
    $("#invoice").html(utils.prettyPrint(data.invoice));
    $("#invoice_no").html(utils.prettyPrint(data.invoiceNo));
    $("#invoice_name").html(utils.prettyPrint(data.invoiceNo));
    $("#invoice_addr").html(utils.prettyPrint(data.invoiceAddress));
    $("#pay_note").html(utils.prettyPrint(data.payNote));

    // 客戶資訊

    // 洽談記錄
    buildRecordMessageForm();
    var messages = utils.safeList(data.messages);
    voRC.rfqMessageFL = new FormListS("voRC.rfqMessageFL", 
                    "#editMessageForm", "#existedMessages", '#saveMessage', '#cancelMessage',// selector
                    messages, // data list
                    RFQ_MESSAGE_SAVE_URL.replace("{orderId}", data.id), 
                    RFQ_MESSAGE_DEL_URL.replace("{orderId}", data.id), // rest url
                    renderMessageList,
                    onEditMessage, afterSaveMessage, true, // call back 
                    getFormDataMessage, // get form input data
                    valiateMessage,
                    utils.i18n("sendmsg.confirm"),
                    null);
    voRC.rfqMessageFL.init();

    createBtn("#closeRfq", "fa fa-ban", onClickCloseRfq);// 關閉明細
    $("#fsRfqDetail").show();// 訂單明細
    utils.scrollTo("#fsRfqDetail");

    if( utils.checkUrlByKeyword("/rfqs.html") ){// 訂單維護功能
        // 選取項目
        genSelectedItemTitle(data.orderNumber);
        $("#selectedItemHeader").show();
        utils.scrollTo('#selectedItemHeader');
        
        createGoToBtn("#btnGoToRfqDetail", "#fsRfqDetail");
        createGoToBtn("#btnGoToRfqItems", "#plRfqItems");
        createGoToBtn("#btnGoToRfqShipping", "#plRfqShipping");
        createGoToBtn("#btnGoToRfqPayment", "#plRfqPayment");
        createGoToBtn("#btnGoToRfqMessage", "#plRfqMessage");
    }
}
// 確定提供報價
function onClickSendPrice(){
    if( !confirm(utils.i18n("sendprice.confirm")) ){
        return;
    }
    if( utils.isEmptyAry(itemlist) ){
        console.log("itemlist = "+itemlist);
        return;
    }
    
    if( !QUOTE_TOTAL ){// 報單價
        quoteUnitPrice();
    }else{
        quoteTotalPrice();
    }
}

function getShippingTotal(){
    // 運費
    var shippingTotal = $("#shippingTotal").val();
    if( isEmpty(shippingTotal) || !(parseInt(shippingTotal)===0 || utils.isPositiveInt(shippingTotal)) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("input.shipping.err"));
        return 0.0;
    }
    return utils.getNumber(shippingTotal);
}

// 計算顯示報價金額相關資訊
function calTotalInfo(event){
    var target = isNA(event)?null:(event.target||document.activeElement);
    var targetId = isNA(target)?null:target.id;
    console.log("event = ", event);
    console.log("target = ", target);
    console.log("targetId = ", targetId);
    var quoteList = [];
    var subtotal = 0.0;
    var total = 0.0;
    var shippingTotal = getShippingTotal();
    total = total + Number(shippingTotal);

    for(var i=0; i<itemlist.length; i++){
        var id = itemlist[i].id;
        var productId = itemlist[i].productId;
        var price = utils.getNumber($("#item_price"+id).val());
        var quantity = utils.getNumber($("#item_quantity"+id).text());
        var itemTotal = 0;
        if( !isNaN(price) && !isNaN(quantity) ){
            itemTotal = price * quantity;
            $("#item_total"+id).html(utils.printNumber(itemTotal));

            subtotal = subtotal + itemTotal;

            quoteList.push({"id":id, "prdId":productId, "price":price, "quantity":quantity});
        }else{
            if( isNA(targetId) || targetId==="item_price"+id || targetId==="item_quantity"+id ){
                _alert(utils.i18n("in.err"));
            }
            return null;
        }
    }

    total = total + subtotal;
    $("#subtotal_pay").val(utils.printNumber(subtotal));
    $("#spanTotalPrice").html(utils.printNumber(total));

    var formData = {
        "orderId": voRC.selectedRfq.id,
        "shippingTotal": shippingTotal,
        "subTotal": subtotal,
        "total": total,
        "statusType":"O",// 訂單/詢價單狀態異動
        "status":RFQ_STATUS_QUOTATION,
        "quoteList":quoteList
    };
    console.log(formData);

    return formData;
}

function quoteUnitPrice(){// 報單價
    var formData = calTotalInfo();
    if( !isNA(formData) ){
        utils.postData(PO_QUOTE_URL, formData, false, afterSendPrice);
    }
}

function quoteTotalPrice(){// 報總價
    var subtotal = $("#subtotal_pay").val();
    var shippingTotal = getShippingTotal();
    if( isEmpty(subtotal) || !utils.isPositiveInt(subtotal) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("input.price.err"));
        return;
    }
    if( subtotal/voRC.selectedRfq.subTotal < 0.8 ){
        if( !confirm(utils.i18n("input.total.confirm")) ){
            return;
        }
    }
    var formData = {
        "id": voRC.selectedRfq.id,
        "shippingTotal": shippingTotal,
        "subTotal": subtotal,
        "status":RFQ_STATUS_QUOTATION
    };
    utils.postData(RFQ_SAVE_URL, formData, false, afterSendPrice, null, null);
}

function afterSendPrice(response, formData, optional){
    buildRfqDetail(response);
    
    if( utils.checkUrlByKeyword("/rfqs.html") ){// 訂單維護功能
        reloadDataTable("#tbRfqLazy", true);
    }
    utils.addSuccessMsg();
}

// 拒絕提供報價
function onClickRejectSendPrice(){
    if( !confirm(utils.i18n("reject.sendprice.confirm")) ){
        return;
    }
    var formData = {
        "id": voRC.selectedRfq.id,
        "status":RFQ_STATUS_REJECT
    };
    utils.postData(RFQ_SAVE_URL, formData, false, afterRejectSendPrice, null, null);
}

function afterRejectSendPrice(response, formData, optional){
    buildRfqDetail(response);
}

// 歷史報價記錄
function onClickViewPriceHis(){
    renderLogs(voRC.selectedRfq.logs);
    
    $('#dlgLogs').puidialog({
        "visible": true,
        "width": "auto",
        "height": "auto",
        "modal": true,
        "effectSpeed": "slow",
        "responsive": true,
        "buttons": [{
                    text: utils.i18n("close"),
                    icon: 'fa-close',
                    click: function() {
                        $('#dlgLogs').puidialog('hide');
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
function renderLogs(data){
    data = utils.safeList(data);
    
    var colSettings = [
            {field: "createtime", headerText: utils.i18n("pricing.time.user"), content: genCreateInfoContent},
            {field: "shippingTotal", headerText: utils.i18n("fd.shipping.price"), bodyClass:"tar"},
            {field: "total", headerText: utils.i18n("fd.total.price"), bodyClass:"tar"}
        ];
    
    renderDataTable('#tbLogs', null, colSettings, data, null, null, null);
}

function genCreateInfoContent(row){
    return formatDateTimeStr(row.createtime) + "<br/>" + row.creator;
}

// 關閉訂單明細
function onClickCloseRfq(){
    $("#fsRfqDetail").hide();// 訂單明細
    if( utils.checkUrlByKeyword("/rfqs.html") ){// 訂單維護功能
        $("#selectedItemHeader").hide();
    }
    utils.scrollTo("#fmSearch");
}

// 購買商品清單
function renderRfqItems(pageSize, data){
    data = utils.safeList(data);
    
    var colSettings = [
            {field: "sno", headerText: utils.i18n("fd.sno"), sortable: true},
            {field: "cname", headerText: utils.i18n("cname"), sortable: true},
            {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: true},
            //{field: "variant", headerText: utils.i18n("fd.variant"), sortable: false},
            {field: "price", headerText: utils.i18n("fd.unitprice"), sortable: false, bodyClass:'tar', content: genPriceContent},
            {field: "quantity", headerText: utils.i18n("fd.quantity"), sortable: false, bodyClass:'tar', content: genQuantityContent},
            {field: "total", headerText: utils.i18n("fd.amt"), sortable: true, bodyClass:'tar', content: genTotalPriceContent}
        ];
    
    renderDataTable('#tbRfqItems', pageSize, colSettings, data, null, 800, 100);
}

// 單價
function genPriceContent(data){
    if( QUOTE_TOTAL ){
        return data.price;
    }else{
        // 可改單價
        //    訂單報價 for EC2.0
        //    訂單確認 for EC1.5
        var id = data.id;
        var readonly = (voRC.selectedRfq.status===RFQ_STATUS_INQUIRY)
                    ?"class='ui-inputtext ui-widget ui-state-default ui-corner-all' "
                    :"readonly class='ui-widget ui-corner-all readonly' ";
        return "<input "+readonly+" name='item_price"+id+"' id='item_price"+id
                +"' value='"+data.price+"' type='text' size='6' onblur='calTotalInfo(event);' />";
    }
}
// 數量
function genQuantityContent(data){
    var id = data.id;
    return "<span id='item_quantity"+id+"' >"+data.quantity+"</span>";
}
// 金額
function genTotalPriceContent(data){
    if( QUOTE_TOTAL ){
        return data.total;
    }else{
        var id = data.id;
        return "<span id='item_total"+id+"' >"+data.total+"</span>";
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Rfq Message Records">
// 處理記錄表單
function buildRecordMessageForm(){
    //$("#messageTime").datetimepicker();
    $("#message").puiinputtextarea({autoResize:true});
}

function onEditMessage(row){
    setTextValue("#message", row.message);
}

function getFormDataMessage(formData){
    return formData;
}

function valiateMessage(row){
    if( isEmpty($("#message").val()) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("msg.content.required"));
        return false;
    }
    return true;
}

function afterSaveMessage(response){
    utils.addSuccessMsg();
}

// 產生訂單處理紀錄列表
function renderMessageList(nameFL, selector, data){
    data = utils.safeList(data);
    var pageSize = 10;
    var colSettings = [
            //{field: "id", headerText: "操作", sortable: false, bodyClass: "tac", content: genOperationContent},
            {field: "createtime", headerText: utils.i18n("fd.msg.time"), sortable: false, content: genMessageTimeContent, bodyStyle:"width:160px;"},
            {field: "buyer", headerText: utils.i18n("fd.buyer.msg"), sortable: false, content: genBuyerContent, bodyStyle:"width:80px;text-align:center;"},
            {field: "message", headerText: utils.i18n("fd.msg.content"), sortable: false}
        ];
    renderDataTable(selector, pageSize, colSettings, data, null, 400, 70);
}

// 操作
function genOperationContent(row){
    var idx = utils.fundIndexByKey(voRC.rfqMessageFL.list, "id", row.id);
    var html = '<button onclick="voRC.rfqMessageFL.onEditRow('+ idx + ')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal w3-center" ><i class="fa fa-pencil-square"></i></button>'
             + '&nbsp;'
             + '<button onclick="voRC.rfqMessageFL.onDeleteRow(' + idx + ')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange w3-center" ><i class="fa fa-trash"></i></button>'
             ;
     return html;
}
// 記錄時間
function genMessageTimeContent(row){
    return formatDateTimeStr(row.createtime);
}
function genBuyerContent(row){
    return row.buyer===true?"<i class='fa fa-check' ></i>":"";
}
//</editor-fold>
