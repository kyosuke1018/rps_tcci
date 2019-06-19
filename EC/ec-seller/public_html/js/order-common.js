/* global _asyncCompleteFlags, PO_COUNT_URL, MSG_NO_RECORD_FOUND, PO_LIST_URL, DATATABLE_RWD_WIDTH, PO_PROCESS_URL, PO_FULL_URL, PO_ITEMS_LIST_URL, PO_PROCESS_SAVE_URL, PO_PROCESS_DEL_URL, RFQ_LIST_URL, PO_RATE_URL, PO_STATUS_PENDING, onClickConfirmOrder, onClickRejectOrder, PO_STATUS_APPROVE, PO_STATUS_SAVE_URL, onClickSearch, SHIP_STATUS_SHIPPED, PO_SHIP_SAVE_URL, SHIP_STATUS_NOT, PO_STATUS_CANCEL, PAY_STATUS_NOT_PAID, SHIP_STATUS_INSTALLMENT, PAY_STATUS_INSTALLMENT, PAY_STATUS_PAID, PO_PAY_SAVE_URL, PAY_STATUS_NOTIFY_PAID, RFQ_MESSAGE_SAVE_URL, RFQ_MESSAGE_DEL_URL, PO_STATUS_CLOSE, SHIP_STATUS_ARRIVED, PO_STATUS_RETURNED, PO_STATUS_DECLINED, QUOTE_TOTAL, QUANTITY_MODIFY, itemlist, _tccDealer, PO_QUOTE_URL, PRD_EXP_URL, PO_EXP_URL, GET_EXP_URL, onClickExpOrder, _adminUser, TEST_DATA, PO_MULTI_CLOSE_URL, _fiUser, PO_STATUS_ACCEPTED, PO_STATUS_SELLERCONFIRM, PO_CHANGE_URL, PO_TRAN_TO_EC10_OPS, PO_TRAN_TO_EC10_SEND, PO_CAR_NO_SAVE, supportTranEC10, vo, CUS_ADDR_ADD_URL, CUS_ADDR_SEL_URL, utils */
var voOC = {// variable objects in order-common.js
    orderProcessFL:null,// FormListS object for variant
    rfqMessageFL:null,  // FormListS object for variant
    selectedOrder:null,
    expHeaders:[],
    pageItems:[],
    checkedItems:[],
    showCheckbox:true,
    // for EC1.0
    toggleCombineSearch:false,
    toggleCombineDivEC10:false,// C:併單
    toggleDivEC10:false,// O:一對一; S:拆單
    deliveryDateEC10:[],
    tranTypeEC10:[],
    shipMethodEC10:[],
    customerEC10:[],
    contractEC10:[],
    provinceEC10:[],
    cityEC10:[],
    districtEC10:[],
    townEC10:[],
    salesAreaEC10:[],
    plantEC10:[],
    salesEC10:[],
    cusAddrEC10:[],
    carListEdit:[],// 編輯 車號(載運數量) EC1.0
    tranMode:"O", // O:一對一; S:拆單; C:併單
    // 車號編輯 EC1.5
    editCarNo:false,
    carNoList:[],
    favCarNoList:[], // 常用車號
    triggerDropdown:true
};

//<editor-fold defaultstate="collapsed" desc="for Order Search Result">
// Lazy方式抓取資料，顯示於 datatable，適用於資料量較大時
function buildOrderListLazy(formData, ajaxKey){
    formData = isNA(formData)?{}:formData;
    console.log("buildOrderListLazy formData = ", formData);
    
    // 先取得總筆數 for 分頁
    var restUrl = PO_COUNT_URL;// 先查總筆數
    utils.postData(restUrl, formData, false, fetchOrderListLazy, null, ajaxKey);
}

function fetchOrderListLazy(response, formData, ajaxKey){
    console.log("fetchOrderListLazy response = ", response);
    var totalRows = response.res.totalRows;
    
    renderOrderListLazy(formData, totalRows, false, ajaxKey);
}

function renderOrderListLazy(formData, totalRows, retry, ajaxKey){
    voOC.showCheckbox = voOC.showCheckbox && !_adminUser && _fiUser;// showCheckbox define in container file
    voOC.expHeaders = [];
    voOC.pageItems = [];

    var pageSize = 10;
    var minWidth = 1300;
    var marginWidth = 80;
    var columnAll = [
        // 勾選欄
        {field: "id", headerText: utils.i18n("fd.check"), sortable: false, bodyClass: "tac", content: genCheckBoxContent, disabled:!voOC.showCheckbox, exported:false},
        // ID, 狀態
        {field: "id", headerText: "ID", sortable: true, bodyClass:'tar'},
        {field: "statusLabel", headerText: utils.i18n("fd.statusLabel"), sortable: true},
        // 付款狀態
        {field: "payStatusLabel", headerText: utils.i18n("fd.payment.status"), sortable: true, content: genPayContent, disabled:_tccDealer, exported:!_tccDealer},
        // 出貨狀態, 編號, 訂單時間, 商品筆數, 報價總金額,
        // 原商品總價, 運費, 幣別, 客戶姓名, 聯絡電話, 
        // 聯絡E-Mail, 出貨方式, 備註事項
        {field: "shipStatusLabel", headerText: utils.i18n("fd.shipping.status"), sortable: true, content: genShipContent},
        {field: "orderNumber", headerText: utils.i18n("fd.orderNumber"), sortable: true},
        {field: "approvetime", headerText: utils.i18n("fd.approvetime"), sortable: true, content: genOrderTimeContent},
        {field: "itemCount", headerText: utils.i18n("fd.itemCount"), sortable: true, bodyClass:'tar', disabled:_tccDealer},
        // 產品名稱 // EC1.0 只有單筆 DETAIL
        {field: "productName", headerText: utils.i18n("prd.cname"), sortable: true, disabled:!_tccDealer},
        {field: "total", headerText: utils.i18n("fd.total"), sortable: true, bodyClass:'tar required', content: genTotalContent},
        {field: "subTotal", headerText: utils.i18n("fd.subTotal"), sortable: true, bodyClass:'tar', content: genSubTotalContent},
        {field: "shippingTotal", headerText: utils.i18n("fd.shippingTotal"), sortable: true, bodyClass:'tar', content: genShippingTotalContent},
        //{field: "discountTotal", headerText: utils.i18n("fd.discountTotal"), sortable: true, bodyClass:'tar', content: genDiscountTotalContent},
        {field: "curName", headerText: utils.i18n("fd.curName"), sortable: true, content: genCurContent},
        {field: "cname", headerText: utils.i18n("fd.cus.cname"), sortable: true},
        {field: "phone", headerText: utils.i18n("fd.phone"), sortable: true},
        {field: "email1", headerText: utils.i18n("fd.contact.email"), sortable: true},
        {field: "payment", headerText: utils.i18n("fd.payment"), sortable: true, disabled:_tccDealer},
        {field: "shipping", headerText: utils.i18n("fd.shipping"), sortable: true}
    ];

    // 處理自訂屬性 disabled、exported 
    var columns = [];
    for(var i=0; i<columnAll.length; i++){
        if( !utils.isTrue(columnAll[i].disabled) ){
            columns.push(columnAll[i]);
        }
        if( !isFalse(columnAll[i].exported) ){
            voOC.expHeaders.push(columnAll[i].headerText);
        }
    }
    
    renderSearchResultNum("#orderSearchResultMsg", totalRows, voOC.showCheckbox, true);
    $("#divMultiOperation").hide();// 多筆操作按鈕先隱藏
    if( totalRows===0 ){
        clearDataTable('#tbOrderLazy', columns, minWidth, marginWidth, ajaxKey);
    }else{
        renderDataTableLazy('#tbOrderLazy', pageSize, totalRows, columns, PO_LIST_URL, 
        !TEST_DATA, formData, onSelectOrder, retry, "orderList", minWidth, marginWidth, 
        afterRenderTable, beforeRenderTable);

        createBtn('#btnExpResult', 'fa-file-excel-o', onClickExpOrder);// 匯出
    }
}

// 匯出查詢結果 // expHeaders, afterExpOrder define in order-common.js
function onClickExpOrder() {
    console.log("onClickExpOrder ...");
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    formData["shipStartAt"] = toISODateTime(formData["shipStartAt"]);
    formData["shipEndAt"] = toISODateTime(formData["shipEndAt"]);

    if( !isNA(vo) && !isNA(vo.hc) ){// 隱藏條件 (for export)
        if( isFalse(vo.hc.closed) ){
            formData["closed"]=false;// 未結案
        }
        if( isFalse(vo.hc.closed) ){
            formData["tranToEC10"]=false;// 未轉單
        }
        if( utils.isTrue(vo.hc.forCombine) ){
            formData["forCombine"]=true;// 可併單
        }
    }
    
    formData["headers"] = voOC.expHeaders;

    console.log("onClickExpOrder formData = \n", formData);
    var restUrl = PO_EXP_URL;
    utils.postData(restUrl, formData, false, afterExpOrder);
}

// 選取訂單
function onSelectOrder(row){
    if( isNA(row.id) ){
        console.log("onSelectOrder row = ", row);
        return;
    }
    var restUrl = PO_FULL_URL.replace("{id}", row.id);
    utils.fetchData(restUrl, false, renderOrderDetail, null, null);
}
// 勾選欄
function genCheckBoxContent(row) {
    if( voOC.toggleCombineSearch ){
        // for 併單
        voOC.pageItems.push(row.id);
        return '<input type="checkbox" id="cbSel' + row.id + '" onchange="checkRow(' + row.id + ')" />';
    }else{
        // for 結案
        // 已核准 & 已到貨
        if( row.status===PO_STATUS_APPROVE && row.shipStatus===SHIP_STATUS_ARRIVED ){
            voOC.pageItems.push(row.id);
            return '<input type="checkbox" id="cbSel' + row.id + '" onchange="checkRow(' + row.id + ')" />';
        }
    }
    return "";
}

function genPayContent(row){
    var color = (row.payStatus===PAY_STATUS_NOT_PAID || row.payStatus===PAY_STATUS_INSTALLMENT)?"red":"green";
    return "<font color='"+color+"' >"+row.payStatusLabel+"</font>";
}
function genShipContent(row){
    var color = (row.shipStatus===SHIP_STATUS_NOT || row.shipStatus===SHIP_STATUS_INSTALLMENT)?"red":"green";
    return "<font color='"+color+"' >"+row.shipStatusLabel+"</font>";
}
function genCurContent(row){
    return genCurNameContent(row.currency);
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
function genDiscountTotalContent(row){
    return utils.printNumber(row.discountTotal);
}

// 訂單時間
function genOrderTimeContent(row){
    return formatDateTimeStr(row.approvetime);
}

// 處理時間
function genProcessTimeContent(row){
    return formatDateTimeStr(row.processed_at);
}

function genContentNote(data){
    return genContentHtml(data, "note");
}

function afterExpOrder(response) {
    console.log("afterExpPrd ...");
    if (!isNA(response.filename)) {
        window.location.href = GET_EXP_URL + "?filename=" + response.filename;
    } else {
        utils.addMessage("error", utils.i18n("errors"), utils.i18n("exp.fail"));
    }
}

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Order Multi Select Operation">
// for renderSearchResultNum checkbox event
function selectAll(){
    console.log(getCheckbox("#_selectAll"));
    var checked = getCheckbox("#_selectAll");
    if( checked && utils.isEmptyAry(voOC.pageItems) ){
        utils.addMessage("info", utils.i18n("prompts"), utils.i18n("noitem.can.sel"));
        return;
    }
    for(var i=0; i<voOC.pageItems.length; i++){
        var id = voOC.pageItems[i];
        $('#cbSel' + id).prop("checked", checked);
        checkRow(voOC.pageItems[i]);
    }
}

function checkRow(id) {
    var idx = voOC.checkedItems.indexOf(id);
    if( $('#cbSel' + id).prop("checked") ){
        if (idx < 0) {
            voOC.checkedItems.push(id);
            voOC.checkedItems.sort();
        }
    } else {
        if (idx >= 0) {
            voOC.checkedItems.splice(idx, 1);
        }
    }
    console.log("checkRow checkedItems = ", voOC.checkedItems);
}

function beforeRenderTable(formData, list){
    voOC.pageItems = [];
    voOC.checkedItems = [];
}

function afterRenderTable(formData, list){
    //console.log("afterRenderTable pageItems = \n", voOC.pageItems);
    // 多筆操作按鈕
    if( voOC.showCheckbox && !utils.isEmptyAry(voOC.pageItems) ){
        $("#divMultiOperation").show();
        $("#btnMultiPoClose").hide();
        $("#btnEditForEC10M").hide();
        if( voOC.toggleCombineSearch ){// for 併單
            createBtn("#btnEditForEC10M", "fa fa-check-square-o", onClickTranToEC10M);
            $("#btnEditForEC10M").show();
        }else{// for 結案
            createBtn("#btnMultiPoClose", "fa fa-check-square-o", onClickCloseMultiPo);
            $("#btnMultiPoClose").show();
        }
    }
    // 全選/全不選
    displaySelectAll(!utils.isEmptyAry(voOC.pageItems));
    clearSelectAll();
}

// 多筆結案
function onClickCloseMultiPo(){
    if( !checkEmptySelect() ){
        return;
    }
    if( !confirm(utils.i18n("confirm.multi.status")) ){
        return;
    }
    var formData = {
        "orderList": voOC.checkedItems,
        "status": PO_STATUS_CLOSE
    };
    utils.postData(PO_MULTI_CLOSE_URL, formData, false, afterChangeMultiStatus);
}
function afterChangeMultiStatus(response) {
    var totalRows = response.res.totalRows;
    reloadDataTable('#tbOrderLazy', true);// keep status reload
    voOC.checkedItems = [];
    utils.addMessage("info", utils.i18n("prompts"), utils.i18nWP("multi.status.counts", totalRows));
}

// 勾選檢查
function checkEmptySelect() {
    if (utils.isEmptyAry(voOC.checkedItems)) {
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("nosel.item"));
        return false;
    }
    return true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Order Detail">
function buildOrderDetailForm(){
    // 賣家評價
    createDropdown('#sellerRate', [
        {label:utils.i18n("pos.rate"), value:1},
        {label:utils.i18n("nag.rate"), value:-1}
    ], {label:utils.i18n("no.rate"), value:0}, null);
    $("#sellerMessage").puiinputtextarea({autoResize:true});
    createBtn("#sendSellerRate", "fa fa-envelope-o", onClickSendRate);

    createBtn("#closeOrder", "fa fa-ban", onClickCloseOrder);// 關閉明細
    
    createBtn("#btnPoConfirm", "fa fa-check", onClickConfirmOrder);
    createBtn("#btnPoReject", "fa fa-times", onClickRejectOrder);
    createBtn("#btnPoCancel", "fa fa-ban", onClickConfirmCancel);
    createBtn("#btnPoClose", "fa fa-check-square-o", onClickClosePo);
    
    createBtn("#btnPayConfirm", "fa fa-money", onClickConfirmPay);
    createBtn("#btnShipConfirm", "fa fa-truck", onClickConfirmShip);

    createBtn("#btnConfirmPoChange", "fa fa-pencil-square", onClickConfirmPoChange);
    
    if( _tccDealer ){
        //待轉台泥訂單明細按紐
        renderSwitchBtnForEC10();
    }
}

// 確認付款
function onClickConfirmPay(){
    if( !confirm(utils.i18n("confirm.pay.status")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }
    console.log("onClickConfirmPay ...");
    var formData = {
        "id": voOC.selectedOrder.id,
        "status": PAY_STATUS_PAID
    };
    var restUrl = PO_PAY_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveOrder, null, 'confirmShip');
}

function getShippingTotal(){
    // 運費
    var shippingTotal = voOC.selectedOrder.shippingTotal;
    shippingTotal = isNA(shippingTotal)?0.0:shippingTotal;
    return shippingTotal;
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
    var shippingTotal = 0.0;
    //var shippingTotal = getShippingTotal();
    //total = total + Number(shippingTotal);

    for(var i=0; i<itemlist.length; i++){
        var id = itemlist[i].id;
        var productId = itemlist[i].productId;
        var price = utils.getNumber($("#item_price"+id).val());
        var shipping = utils.getNumber($("#item_shipping"+id).val());
        var quantity = utils.getNumber($("#item_quantity"+id).val());
        var itemTotal = 0;
        var itemShipTotal = 0;
        if( !isNaN(price) && !isNaN(shipping) && !isNaN(quantity) 
                && price>0 && quantity>0){
            itemTotal = price * quantity;
            itemShipTotal = shipping * quantity;
            $("#item_total"+id).html(utils.printNumber(itemTotal+itemShipTotal));
            subtotal = subtotal + itemTotal;
            shippingTotal = shippingTotal + itemShipTotal;

            quoteList.push({"id":id, "prdId":productId, "price":price, "shipping":shipping, "quantity":quantity});
        }else{
            if( isNA(targetId) || targetId==="item_price"+id || targetId==="item_shipping"+id || targetId==="item_quantity"+id ){
                _alert(utils.i18n("po.in.err"));// 價、量輸入錯誤!
            }
            return null;
        }
    }

    total = total + subtotal + shippingTotal;
    $("#total_price").html(utils.printNumber(subtotal));
    $("#total_shipping").html(utils.printNumber(shippingTotal));
    $("#total_pay").html(utils.printNumber(total));

    var formData = {
        "orderId": voOC.selectedOrder.id,
        "shippingTotal": shippingTotal,
        "subTotal": subtotal,
        "total": total,
        "quoteList":quoteList
    };
    console.log(formData);
    return formData;
}

// 確認PO量、價修改
function onClickConfirmPoChange(){
    console.log("onClickConfirmPoChange ...");
    if( !confirm(utils.i18n("confirm.po.change")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }
    var formData = calTotalInfo();
    if( !isNA(formData) ){
        utils.postData(PO_CHANGE_URL, formData, false, afterSaveOrder, null, 'confirmChange');
    }
}

// 確認出貨 (可改量 for EC2.0 及 EC1.5)
function onClickConfirmShip(){
    console.log("onClickConfirmShip ...");
    if( !confirm(utils.i18n("confirm.ship.status")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }

    if( QUANTITY_MODIFY ){// 改量
        var formData = calTotalInfo();
        if( !isNA(formData) ){
            formData["statusType"] = "S"; // 出貨狀態異動
            formData["status"] = PO_STATUS_APPROVE;
            formData["shipStatus"] = SHIP_STATUS_SHIPPED;
            utils.postData(PO_QUOTE_URL, formData, false, afterSaveOrder, null, 'confirmShip');
        }
    }else{
        // 只改狀態
        var formData = {
            "id": voOC.selectedOrder.id,
            "status": SHIP_STATUS_SHIPPED
        };
        var restUrl = PO_SHIP_SAVE_URL;
        utils.postData(restUrl, formData, false, afterSaveOrder, null, 'confirmShip');
    }
}

// PO結案
function onClickClosePo(){
    if( !confirm(utils.i18n("po.close.msg")) ){
        return;
    }
    console.log("onClickClosePo ...");
    var formData = {
        "id": voOC.selectedOrder.id,
        "status": PO_STATUS_CLOSE
    };
    var restUrl = PO_STATUS_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveOrder, null, 'closePo');
}

// 取消PO
function onClickConfirmCancel(){
    if( !confirm(utils.i18n("po.cancel.msg")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }
    console.log("onClickConfirmCancel ...");
    var formData = {
        "id": voOC.selectedOrder.id,
        "status": PO_STATUS_CANCEL
    };
    var restUrl = PO_STATUS_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveOrder, null, 'confirmCancel');

}

// 確認PO
function onClickConfirmOrder(){
    console.log("onClickConfirmOrder ...");
    if( !confirm(utils.i18n("confirm.po.status")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }
    
    if( _tccDealer && !QUOTE_TOTAL ){
        var formData = calTotalInfo();
        if( !isNA(formData) ){
            formData["status"] = PO_STATUS_APPROVE;
            utils.postData(PO_CHANGE_URL, formData, false, afterSaveOrder, null, 'confirmOrder');
        }
        // 可改單價  訂單確認 for EC1.5
        /*var formData = calTotalInfo();
        if( !isNA(formData) ){
            formData["statusType"] = "O";// 訂單/詢價單狀態異動
            formData["status"] = PO_STATUS_APPROVE;
            utils.postData(PO_QUOTE_URL, formData, false, afterSaveOrder, null, 'confirmShip');
        }*/
    }else{
        var formData = {
            "id": voOC.selectedOrder.id,
            "status": PO_STATUS_APPROVE
        };
        var restUrl = PO_STATUS_SAVE_URL;
        utils.postData(restUrl, formData, false, afterSaveOrder, null, 'confirmOrder');
    }
}

// 回絕PO
function onClickRejectOrder(){
    if( !confirm(utils.i18n("confirm.po.status")+"\n"+utils.i18n("msg.sendto.buyer")) ){
        return;
    }
    console.log("onClickRejectOrder ...");
    var formData = {
        "id": voOC.selectedOrder.id,
        "status": PO_STATUS_DECLINED
    };
    var restUrl = PO_STATUS_SAVE_URL;
    utils.postData(restUrl, formData, false, afterSaveOrder, null, 'rejectOrder');
}

function afterSaveOrder(response, formData, ajaxKey){
    renderOrderDetail(response);    
    if( !isNA(onClickSearch) ){
        reloadDataTable('#tbOrderLazy', true);// keep status reload
    }
    utils.addSuccessMsg();
}

// 訂單詳細資訊
function renderOrderDetail(data){
    console.log("renderOrderDetail data = ", data);
    voOC.selectedOrder = data;
    var pageSize =10;
    
    // 建立訂單表單元件
    buildOrderDetailForm();
    // 訂單詳細資料
    $("#order_number").html(utils.prettyPrint(data.orderNumber, "　"));// 訂單編號
    $("#order_account").html(utils.prettyPrint(data.loginAccount));// 客戶loginAccount
    $("#order_cus").html(utils.prettyPrint(data.cname));// 客戶名稱
    $("#item_num").html(utils.prettyPrint(data.itemCount));// 商品筆數
    $("#total_price").html(utils.printNumber(data.subTotal));// 貨款總金額
    $("#total_shipping").html(utils.printNumber(data.shippingTotal));// 運費金額
    $("#total_discount").html(utils.prettyPrint(data.discountTotal, "NA"));// 折扣金額
    $("#total_pay").html(utils.printNumber(data.total));// 應付總金額
    $("#order_message").html(utils.prettyPrint(data.message));// 特殊需求
    $("#order_delivery_date").html(utils.prettyPrint(utils.formatDateStr(data.deliveryDate), "　"));// 期望交貨日期
    
    var statusLabel = utils.prettyPrint(data.statusLabel);
    if( !_tccDealer ){
        if( !isEmpty(data.payStatusLabel) ){
            statusLabel += "【"+utils.prettyPrint(data.payStatusLabel)+"】";
        } 
    }
    if( !isEmpty(data.shipStatusLabel) ){
        statusLabel += "【"+utils.prettyPrint(data.shipStatusLabel)+"】";
    }
    $("#order_status").html(utils.prettyPrint(statusLabel));// 訂單狀態

    // for EC1.0
    initDivForEC10();
    voOC.toggleDivEC10 = false;
    voOC.toggleCombineDivEC10 = false;
    $("#btnEditForEC10").hide();
    $("#btnViewForEC10").hide();

    if( _tccDealer && utils.isTrue(vo.supportTranEC10) ){// 只有 orders.html 支援
        if( voOC.selectedOrder.tranToEC10 ){// 已轉單
            $("#btnViewForEC10").show();
        }else if( data.status===PO_STATUS_APPROVE // 賣家已確認
            && data.shipStatus===SHIP_STATUS_NOT // 未出貨
            && utils.isTrue(data.buyerCheck)// 買家已確認
        ){
            $("#btnEditForEC10").show();
        }
    }

    // 確定量、價修改
    $("#btnConfirmPoChange").hide();
    if( data.status===PO_STATUS_APPROVE ){// 已確認
        $("#btnConfirmPoChange").show();
    }
    // 訂單確認
    $("#btnPoConfirm").hide();
    if( data.status===PO_STATUS_PENDING ){
        $("#btnPoConfirm").show();
    }
    // 訂單回絕
    $("#btnPoReject").hide();
    if( data.status===PO_STATUS_PENDING ){
        $("#btnPoReject").show();
    }
    // 取消訂單
    $("#btnPoCancel").hide();
    if( data.status===PO_STATUS_APPROVE 
            && data.shipStatus===SHIP_STATUS_NOT // 未出貨
            && (!_tccDealer || data.payStatus===PAY_STATUS_NOT_PAID) // 未付款
            ){
        $("#btnPoCancel").show();
    }
    // 結案
    $("#btnPoClose").hide();
    if( data.status===PO_STATUS_APPROVE 
    // data.status===PO_STATUS_CUSCONFIRM // 改為[買家最後確認]後 // 20190321
            && data.shipStatus===SHIP_STATUS_ARRIVED // 已到貨
            && (!_tccDealer || data.payStatus===PAY_STATUS_PAID) // 已付款
            ){
        $("#btnPoClose").show();
    }
    // 付款確認
    $("#btnPayConfirm").hide();
    if( data.status===PO_STATUS_APPROVE 
            && (!_tccDealer || data.payStatus===PAY_STATUS_NOTIFY_PAID) ){// 通知已付款
        $("#btnPayConfirm").show();
    }
    // 確認出貨
    $("#btnShipConfirm").hide();
    if( data.status===PO_STATUS_APPROVE // [賣加已確認]
            && (!_tccDealer || data.payStatus===PAY_STATUS_PAID) // 已付款
            && data.shipStatus===SHIP_STATUS_NOT ){// 未出貨
        $("#btnShipConfirm").show();
    }

    // 訂單時間
    if( isEmpty(data.approvetime) ){
        //$("#orderActionBtn").show();
        $(".approvetime").hide();
    }else{
        //$("#orderActionBtn").hide();
        $(".approvetime").show();
        $("#order_time").html(genOrderTimeContent(data));
    }

    // 評價
    $("#customerRate").html(renderOrderRate(data.customerRate));//
    $("#customerMessage").html(utils.prettyPrint(data.customerMessage));// 
    setDropdownValue('#sellerRate', isNA(data.sellerRate)?0:data.sellerRate);
    $("#sellerMessage").val(utils.prettyPrint(data.sellerMessage));//

    // 購買商品清單
    itemlist = utils.safeList(data.items);
    renderOrderItems(pageSize, itemlist);

    // 運送資訊
    $("#shipping_method").html(utils.prettyPrint(data.shipping));
    $("#shipping_code").html(utils.prettyPrint(data.shippingCode));
    $("#shipping_contact1").html(utils.prettyPrint(data.recipient));
    $("#shipping_phone1").html(utils.prettyPrint(data.phone));
    // 初始車號顯示區域
    initCarNoArea();
    buildCarNoBtns();
    
    $("#driver").html(utils.prettyPrint(data.driver));
    if( _tccDealer ){
        $("#deliveryPlace").html(utils.prettyPrint(data.deliveryPlaceName));
    }
    $("#shipping_addr").html(utils.prettyPrint(data.address));
    $("#shipping_note").html(utils.prettyPrint(data.shippingNote));

    // 付款資訊
    if( !_tccDealer ){
        $("#payType").html(utils.prettyPrint(data.payment));
        $("#gateway").html(utils.prettyPrint(data.gateway));
        $("#invoice").html(utils.prettyPrint(data.invoice));
        $("#invoice_no").html(utils.prettyPrint(data.invoiceNo));
        $("#invoice_name").html(utils.prettyPrint(data.invoiceNo));
        $("#invoice_addr").html(utils.prettyPrint(data.invoiceAddress));
        $("#pay_note").html(utils.prettyPrint(data.payNote));
    }

    // 洽談記錄
    buildRecordMessageForm();
    var messages = utils.safeList(data.messages);
    voOC.rfqMessageFL = new FormListS("voOC.rfqMessageFL", 
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
    voOC.rfqMessageFL.init();
    
    // 處理記錄
    buildRecordProcessForm();
    var records = utils.safeList(data.records);
    voOC.orderProcessFL = new FormListS("voOC.orderProcessFL", 
        "#editRecordForm", "#existedRecords", '#saveRecord', '#cancelRecord',// selector
        records, // data list
        PO_PROCESS_SAVE_URL.replace("{orderId}", data.id), 
        PO_PROCESS_DEL_URL.replace("{orderId}", data.id), // rest url
        renderProcessList,
        onEditProcess, afterSaveProcess, true, // call back 
        getFormDataProcess, // get form input data
        valiateProcess, null, null);
    voOC.orderProcessFL.init();

    $("#fsOrderDetail").show();// 訂單明細

    if( !utils.checkUrlByKeyword("/customers.html") ){// 客戶維護功能不顯示
        // 選取項目
        genSelectedItemTitle(data.orderNumber);
        $("#selectedItemHeader").show();
        utils.scrollTo('#selectedItemHeader');
        
        createGoToBtn("#btnGoToOrderDetail", "#fsOrderDetail");
        createGoToBtn("#btnGoToOrderItems", "#plOrderItems");
        createGoToBtn("#btnGoToOrderShipping", "#plOrderShipping");
        if( !_tccDealer ){
            createGoToBtn("#btnGoToOrderPayment", "#plOrderPayment");
        }
        createGoToBtn("#btnGoToRfqMessage", "#plRfqMessage");
        createGoToBtn("#btnGoToOrderProcess", "#plOrderProcess");
    }else{
        utils.scrollTo('#fsOrderDetail', 40);
    }
}

// 顯示洽談訊息
function renderOrderMsgList(selector, data){
    data = utils.safeList(data);

    var pageSize = 10;
    var colSettings = [
        //{field: "id", headerText: "操作", sortable: false, bodyClass: "tac", content: genOperationContent},
        {field: "createtime", headerText: utils.i18n("fd.msg.time"), sortable: false, content: genMessageTimeContent, bodyStyle:"width:160px;"},
        {field: "message", headerText: utils.i18n("fd.msg.content"), sortable: false}
    ];
    renderDataTable(selector, pageSize, colSettings, data, null, 400, 70);
}
// 記錄時間
function genMessageTimeContent(row){
    return formatDateTimeStr(row.createtime);
}
// 送出評價
function onClickSendRate(){
    if( !confirm(utils.i18n("confirm.send.rate")) ){
        return;
    }
    console.log("onClickSendRate ...");
    var formData = {
        "orderId":voOC.selectedOrder.id,
        "sellerRate": getDropdownValue('#sellerRate'),
        "sellerMessage": $('#sellerMessage').val()
    };
    var restUrl = PO_RATE_URL;
    utils.postData(restUrl, formData, false, afterSendRate, null, 'sendRate');
}
// Callback for 送出評價
function afterSendRate(response){
    console.log("afterSendRate ...");
    utils.addSuccessMsg();
}

// 關閉訂單明細
function onClickCloseOrder(){
    commonRenderAfterOrderSearch();// 不同頁面應各自實作
    voOC.toggleDivEC10 = false;
    voOC.toggleCombineDivEC10 = false;

    utils.scrollTo("#fmSearch");
}

// 顯示購買商品清單
function renderOrderItems(pageSize, data){
    data = utils.safeList(data);
    console.log("renderOrderItems data = \n", data);
    
    var colSettings = [
        {field: "sno", headerText: utils.i18n("fd.sno"), sortable: true},
        {field: "cname", headerText: utils.i18n("cname"), sortable: true},
        {field: "code", headerText: utils.i18n("fd.prd.code"), sortable: true},
        //{field: "typeName", headerText: "類別", sortable: false},
        {field: "price", headerText: utils.i18n("fd.unit.price"), sortable: false, bodyClass:"tar", content: genPriceContent},
        {field: "shipping", headerText: utils.i18n("fd.unit.ship"), sortable: false, bodyClass:"tar", content: genShippingContent},
        {field: "quantity", headerText: utils.i18n("fd.quantity"), sortable: false, bodyClass:"tar", content: genQuantityContent},
        {field: "total", headerText: utils.i18n("fd.total"), sortable: true, bodyClass:"tar", content: genTotalPriceContent}
    ];
    
    renderDataTable('#tbOrderItems', pageSize, colSettings, data, null, 800, 100);
}

// 單價
// 可改單價
//    訂單報價 for EC2.0 (in rfq-common.js)
//    訂單確認 for EC1.5
function genPriceContent(data){
    if( QUOTE_TOTAL ){
        return utils.printNumber(data.price);
    }else{
        var id = data.id;
        //var readonly = (voOC.selectedOrder.status===PO_STATUS_PENDING && _tccDealer)
        var readonly = ((voOC.selectedOrder.status===PO_STATUS_PENDING || voOC.selectedOrder.status===PO_STATUS_APPROVE) && _tccDealer)
        ?"class='ui-inputtext ui-widget ui-state-default ui-corner-all' "
        :"readonly class='ui-widget ui-corner-all readonly' ";
        return "<input onblur='calTotalInfo(event)' "+readonly
                +" name='item_price"+id+"' id='item_price"+id
                +"' value='"+utils.printNumber(data.price)+"' type='text' size='6' />";
    }
}
function genShippingContent(data){
    if( QUOTE_TOTAL ){
        return utils.printNumber(data.shipping);
    }else{
        var id = data.id;
        var readonly = ((voOC.selectedOrder.status===PO_STATUS_PENDING || voOC.selectedOrder.status===PO_STATUS_APPROVE) && _tccDealer)
        ?"class='ui-inputtext ui-widget ui-state-default ui-corner-all' "
        :"readonly class='ui-widget ui-corner-all readonly' ";
        return "<input onblur='calTotalInfo(event)' "+readonly
                +" name='item_shipping"+id+"' id='item_shipping"+id
                +"' value='"+utils.printNumber(data.shipping)+"' type='text' size='6' />";
    }
}
// 數量
// 可改量
//   出貨 for EC2.0 及 EC1.5
function genQuantityContent(data){
    if( QUOTE_TOTAL ){
        return utils.printNumber(data.quantity);
    }else{
        var id = data.id;
        //var readonly = (voOC.selectedOrder.status===PO_STATUS_APPROVE 
        //                && (!_tccDealer || voOC.selectedOrder.payStatus===PAY_STATUS_PAID) // 已付款
        //                && voOC.selectedOrder.shipStatus===SHIP_STATUS_NOT)// 未出貨
        var readonly = ((voOC.selectedOrder.status===PO_STATUS_PENDING || voOC.selectedOrder.status===PO_STATUS_APPROVE) && _tccDealer)
        ?"class='ui-inputtext ui-widget ui-state-default ui-corner-all' "
        :"readonly class='ui-widget ui-corner-all readonly' ";
        return "<input onblur='calTotalInfo(event)' "+readonly
                +" name='item_quantity"+id+"' id='item_quantity"+id
                +"' value='"+utils.printNumber(data.quantity)+"' type='text' size='6' />";
    }
}
// 金額
function genTotalPriceContent(data){
    if( QUOTE_TOTAL ){
        return utils.printNumber(data.total);
    }else{
        var id = data.id;
        return "<span id='item_total"+id+"' >"+utils.printNumber(data.total)+"</span>";
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Transfer to EC1.0">
// 初始 for Transfer to EC1.0 區塊
function initDivForEC10(){
    $("#fsOrderDetail").hide();// 訂單明細
    if( !utils.checkUrlByKeyword("/customers.html") ){// 客戶維護功能不顯示
        $("#selectedItemHeader").hide();
    }
    $("#fsOrderInfoEC10").hide();
    $("#fsOrderInfoEC10View").hide();
    $("#divResultEC10").hide();       
}

// 轉台泥訂單 查詢按紐
function renderSearchBtnForEC10(){
    createBtn('#btnForEC10', 'fa-search-plus', onClickSearchForEC10);// 待轉台泥訂單(單筆、拆單)
    createBtn('#btnForEC10M', 'fa-compress', onClickForEC10M);// 待轉台泥訂單(併單) 
    
    createBtn('#btnIsEC10', 'fa-search-plus', onClickSearchIsEC10);// 已轉台泥訂單
}

// 待轉台泥訂單(併單) 
function onClickForEC10M(){
    console.log("onClickForEC10M toggleCombineDivEC10 = "+voOC.toggleCombineDivEC10);
    if( voOC.toggleCombineDivEC10 ){
        $("#combineKeys").hide();
    }else{
        buildCombineForm();
    }
    voOC.toggleCombineDivEC10 = !voOC.toggleCombineDivEC10;
}

// 產生待轉台泥訂單(併單) 選單
function buildCombineForm(){
    console.log("onClickConfirmPay ...");   
    var formData = {
        "keys": "combineOptions"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, renderCombineForm, null, 'renderCombineForm');
}
// 顯示待轉台泥訂單(併單) 選單
function renderCombineForm(response, formData){
    var combineOptionsEC10 = isNA(response.combineOptionsEC10)? []:response.combineOptionsEC10;
    if( utils.isEmptyAry(combineOptionsEC10) ){
        // 無符合併單條件的訂單!
        _alert(utils.i18n("no.po.found"));
        $("#combineKeys").hide();
        voOC.toggleCombineSearch = false;
        return;
    }
    // 選取併單條件
    createDropdown("#combineKeys", combineOptionsEC10, {label:utils.i18n("ec10.combineOp"), value:""}, onChangeCombineOpEC10);
    $("#combineKeys").show();
    voOC.toggleCombineSearch = true;
}

// 變更 併單 條件選單
function onChangeCombineOpEC10(op){
    if( isEmpty(op.value) ){
        renderOrderListLazy({}, 0, false, PO_LIST_URL);// clear table
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var formData = {};
    formData["forCombine"] = true;
    formData["combineKeys"] = op.value;
    if( !isNA(vo) && !isNA(vo.hc) ){
        vo.hc.closed = null;
        vo.hc.tranToEC10 = null;
        vo.hc.forCombine = true;
    }

    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料
    
    initDivForEC10();
    voOC.toggleDivEC10 = false;
    voOC.toggleCombineDivEC10 = false;
    
    utils.scrollTo("#topOrderSearchResult");
}

// 待轉台泥訂單　查詢(for 單筆、拆單)
function onClickSearchForEC10(){
    searchEC10(false); // 未轉單成功
}
// 已轉台泥訂單　查詢
function onClickSearchIsEC10(){
    searchEC10(true); // 已轉單成功
}
function searchEC10(tranToEC10){
    var $form = $("#fmSearch");
    var formData = $form.serializeFormJSON();
    formData["startAt"] = toISODateTime(formData["startAt"]);
    formData["endAt"] = toISODateTime(formData["endAt"]);
    
    formData["orderStatus"] = PO_STATUS_APPROVE;// 賣家已核准
    formData["shipStatus"] = SHIP_STATUS_NOT;// 未出貨
    formData["buyerCheck"]=true;// 買方已確認訂單量、價

    formData["tranToEC10"]=tranToEC10;
    if( !isNA(vo) && !isNA(vo.hc) ){
        vo.hc.closed = null;
        vo.hc.tranToEC10 = false;
        vo.hc.forCombine = null;
    }
    
    buildOrderListLazy(formData, 'orderList');// Lazy方式抓取資料
    initDivForEC10();
    voOC.toggleDivEC10 = false;
    voOC.toggleCombineSearch = false;    
    voOC.toggleCombineDivEC10 = false;
    
    utils.scrollTo("#topOrderSearchResult");
}

// 編輯轉 EC1.0 補充資訊(for 併單)
function onClickTranToEC10M(){
    console.log("onClickTranToEC10M ...");
    initDivForEC10();
    
    $("#btnAddCarEC10").hide();// 併單不可加車號
    $("#divBtnEC10").show();
    voOC.toggleCombineDivEC10 = true;

    initEC10Options();
}

// 待轉台泥訂單明細按紐
function renderSwitchBtnForEC10(){
    createBtn("#btnEditForEC10", "fa fa-shopping-cart", onClickTranToEC10);
    createBtn("#btnViewForEC10", "fa fa-eye", onClickViewToEC10);
}

// 編輯轉 EC1.0 補充資訊
function onClickTranToEC10(){
    console.log("onClickTranToEC10 selectedOrder = \n", voOC.selectedOrder);
    if( voOC.toggleCombineDivEC10 ){
        // 確定要離開目前併單編輯畫面?
        if( !confirm(utils.i18n("ec10.msg001")) ){
            return;
        }
        voOC.toggleCombineDivEC10 = false;
    }
    
    // 單筆、拆單 // 未轉單
    if( !voOC.toggleDivEC10 ){
        $("#btnAddCarEC10").show();
        $("#divBtnEC10").show();
        $("#divResultEC10").hide();
        initEC10Options();
    }else{
        $("#fsOrderInfoEC10").hide();
        voOC.toggleDivEC10 = false;
    }
    voOC.toggleDivEC10 = !voOC.toggleDivEC10;
}
// 檢視轉 EC1.0 補充資訊 (單筆、拆單)
function onClickViewToEC10(){
    console.log("onClickViewToEC10 selectedOrder = \n", voOC.selectedOrder);
    if( !voOC.toggleDivEC10 ){
        $("#divResultEC10").hide();
        renderOrderViewEC10(voOC.selectedOrder.tccOrder, voOC.selectedOrder.carListEC10);
    }else{
        $("#fsOrderInfoEC10View").hide();
    }
    voOC.toggleDivEC10 = !voOC.toggleDivEC10;
}

// 建立轉 EC1.0 補充資訊相關選單
function initEC10Options(){
    if( voOC.toggleCombineDivEC10 && (utils.isEmptyAry(voOC.checkedItems) || utils.isSingleItem(voOC.checkedItems)) ){
        // 至少須選取兩筆訂單!
        _alert(utils.i18n("less.two.po"));
        return;
    }

    var orderId = voOC.toggleCombineDivEC10?voOC.checkedItems[0]:voOC.selectedOrder.id;
    console.log("initEC10Options orderId = "+orderId+", checkedItems = "+voOC.checkedItems);
    var formData = {
        "tranMode": voOC.toggleCombineDivEC10?"C":"O", // 此處只需知道是否是併單
        "orderList": voOC.checkedItems, // for 併單總數量
        "orderId": orderId,
        //"keys": "deliveryDate,tranType,shipMethod,customer,contract,province,city,district,town,salesArea,plant,sales,cusAddr"
        "keys": "deliveryDate,tranType,shipMethod,customer,contract,province,city,district,town,plant,sales,cusAddr"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'initEC10Options');
}
// 建立轉 EC1.0 補充資訊輸入表單
function buildEC10Form(response, formData){
    console.log("buildEC10Form response = \n", response);
    voOC.triggerDropdown = false;
    
    var init = !isNA(formData.tranMode);
    var keys = formData.keys;
    if( voOC.toggleCombineDivEC10 // 併單
        && init // 只有 initEC10Options 要變更總量 
        && !isNA(response.selectedOrder) ){
        voOC.selectedOrder = response.selectedOrder;
    }
    $("#spanTotalQuantity").html(utils.i18n("total.quantity")+"："+voOC.selectedOrder.quantity);

    // 省
    if( keys.indexOf("province")>=0 ){
        voOC.provinceEC10 = utils.getListProp(response, "provinceEC10");
        createDropdown("#provinceEC10", voOC.provinceEC10, {label:utils.i18n("ec10.province"), value:""}, onChangeProvinceEC10);
        if( init && !isNA(voOC.selectedOrder) && !isNA(voOC.selectedOrder.province) ){
            setDropdownValue("#provinceEC10", voOC.selectedOrder.province);
        }else{
            if( utils.isSingleItem(voOC.provinceEC10) ){
                setDropdownValue("#provinceEC10", voOC.provinceEC10[0].value);
            }
        }
    }
    // 市
    if( keys.indexOf("city")>=0 ){
        voOC.cityEC10 = utils.getListProp(response, "cityEC10");
        createDropdown("#cityEC10", voOC.cityEC10, {label:utils.i18n("ec10.city"), value:""}, onChangeCityEC10);
        if( init && !isNA(voOC.selectedOrder) && !isNA(voOC.selectedOrder.city) ){
            setDropdownValue("#cityEC10", voOC.selectedOrder.city);
        }else{
            if( utils.isSingleItem(voOC.cityEC10) ){
                setDropdownValue("#cityEC10", voOC.cityEC10[0].value);
            }
        }
    }
    // 區
    if( keys.indexOf("district")>=0 ){
        voOC.districtEC10 = utils.getListProp(response, "districtEC10");
        createDropdown("#districtEC10", voOC.districtEC10, {label:utils.i18n("ec10.district"), value:""}, onChangeDistrictEC10);
        if( init && !isNA(voOC.selectedOrder) && !isNA(voOC.selectedOrder.district) ){
            setDropdownValue("#districtEC10", voOC.selectedOrder.district);
        }else{
            if( utils.isSingleItem(voOC.districtEC10) ){
                setDropdownValue("#districtEC10", voOC.districtEC10[0].value);
            }
        }
    }
    // 鎮
    if( keys.indexOf("town")>=0 ){
        voOC.townEC10 = utils.getListProp(response, "townEC10");
        createDropdown("#townEC10", voOC.townEC10, {label:utils.i18n("ec10.town"), value:""}, onChangeTownEC10);
        if( init && !isNA(voOC.selectedOrder) && !isNA(voOC.selectedOrder.town) ){
            setDropdownValue("#townEC10", voOC.selectedOrder.town);
        }else{
            if( utils.isSingleItem(voOC.townEC10) ){
                setDropdownValue("#townEC10", voOC.townEC10[0].value);
            }
        }
    }    
    // 提單日期
    if( keys.indexOf("deliveryDate")>=0 ){
        voOC.deliveryDateEC10 = utils.getListProp(response, "deliveryDateEC10");
        createDropdown("#deliveryDateEC10", voOC.deliveryDateEC10, {label:"===", value:""}, null);
        setDropdownValue("#deliveryDateEC10", voOC.deliveryDateEC10[0].value);
    }
    // 交易類型
    if( keys.indexOf("tranType")>=0 ){
        voOC.tranTypeEC10 = utils.getListProp(response, "tranTypeEC10");
        createDropdown("#tranTypeEC10", voOC.tranTypeEC10, {label:"===", value:""}, onChangeTranTypeEC10);
        if( utils.isSingleItem(voOC.tranTypeEC10) ){
            setDropdownValue("#tranTypeEC10", voOC.tranTypeEC10[0].value);
        }
    }
    // 提貨方式
    if( keys.indexOf("shipMethod")>=0 ){
        voOC.shipMethodEC10 = utils.getListProp(response, "shipMethodEC10");
        createDropdown("#shipMethodEC10", voOC.shipMethodEC10, {label:"===", value:""}, null);
        if( utils.isSingleItem(voOC.shipMethodEC10) ){
            setDropdownValue("#shipMethodEC10", voOC.shipMethodEC10[0].value);
        }
    }
    // 客戶
    if( keys.indexOf("customer")>=0 ){
        voOC.customerEC10 = utils.getListProp(response, "customerEC10");
        createDropdown("#customerEC10", voOC.customerEC10, {label:"===", value:0}, onChangeCustomerEC10);
        if( utils.isSingleItem(voOC.customerEC10) ){
            setDropdownValue("#customerEC10", voOC.customerEC10[0].value);
        }
    }
    // 合約
    if( keys.indexOf("contract")>=0 ){
        voOC.contractEC10 = utils.getListProp(response, "contractEC10");
        createDropdown("#contractEC10", voOC.contractEC10, {label:"===", value:0}, onChangeContractEC10);
        if( utils.isSingleItem(voOC.contractEC10) ){
            setDropdownValue("#contractEC10", voOC.contractEC10[0].value);
        }
    }
    // 銷售區域
    //salesAreaEC10 = utils.getListProp(response, "salesAreaEC10");
    //createDropdown("#salesAreaEC10", salesAreaEC10, {label:utils.i18n("ec10.salesArea"), value:0}, null);
    // 廠別
    if( keys.indexOf("plant")>=0 ){
        voOC.plantEC10 = utils.getListProp(response, "plantEC10");
        createDropdown("#plantEC10", voOC.plantEC10, {label:"===", value:0}, null);
        if( utils.isSingleItem(voOC.plantEC10) ){
            setDropdownValue("#plantEC10", voOC.plantEC10[0].value);
        }
    }
    // 業務
    if( keys.indexOf("sales")>=0 ){
        voOC.salesEC10 = utils.getListProp(response, "salesEC10");
        createDropdown("#salesEC10", voOC.salesEC10, {label:"===", value:0}, null);
        if( utils.isSingleItem(voOC.salesEC10) ){
            setDropdownValue("#salesEC10", voOC.salesEC10[0].value);
        }
    }
    // 常用
    if( keys.indexOf("cusAddr")>=0 ){
        voOC.cusAddrEC10 = utils.getListProp(response, "cusAddrEC10");
        // 自常用選取
        createDropdown("#cusAddrEC10", voOC.cusAddrEC10, {label:"=== "+utils.i18n("ec10.msg005")+" ===", value:0}, onChangeCusAddr);
    }
    
    if( init ){
        // 車號(載運數量)
        voOC.carListEdit = utils.safeList(voOC.selectedOrder.carList);
        if( voOC.toggleCombineDivEC10 && isEmptyAry(voOC.carListEdit) ){// 併單
            voOC.carListEdit = [{"carNo":"", "quantity":0}];
        }

        buildCarInfo("#tbCarQuantity", voOC.carListEdit);
        if( utils.isSingleItem(voOC.carListEdit) ){
            $("#quantity0").val(voOC.selectedOrder.quantity);// 單一車號預設數量同總數量
        }

        createBtn("#btnAddCusAddr", "fa fa-star", onClickAddCusAddr);// 加入常用(送達地點)
        createBtn("#btnAddCarEC10", "fa fa-plus-square-o", onClickAddCar);// 新增車號(載運數量)
        createBtn("#btnSendEC10", "fa fa-check-square-o", onClickSendEC10);// 確定送出
        createBtn("#btnCancelEC10", "fa fa-ban", onClickCancelEC10);// 取消
    }
    
    // 交易類型 - 控制合約顯示
    var tranType = getDropdownValue("#tranTypeEC10");
    if( tranType!=="C" ){
        $(".contractOnly").hide();
    }
    
    $("#fsOrderInfoEC10").show();
    voOC.triggerDropdown = true;
    utils.scrollTo("#topOfOrderInfoEC10", voOC.toggleCombineDivEC10?0:40);
}

// 加入常用
function onClickAddCusAddr(){
    console.log("onClickAddCusAddr ...");
    if( !voOC.triggerDropdown ){
        return;
    }
    var province = getDropdownValue("#provinceEC10");
    var city = getDropdownValue("#cityEC10");
    var district = getDropdownValue("#districtEC10");
    var town = getDropdownValue("#townEC10");
    
    if( isEmpty(province) || isEmpty(city) || isEmpty(district) || isEmpty(town) ){
        // 未選取送達地點(省、市、區、鎮)!
        _alert(utils.i18n("ec10.msg004"));
        return;
    }

    var formData = {
        "type": "deliveryId",
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "city": city,
        "district": district,
        "town": town
    };
    var restUrl = CUS_ADDR_ADD_URL;
    utils.postData(restUrl, formData, false, afterAddCusAddr, null, 'onClickAddCusAddr');
}
function afterAddCusAddr(response, formData){
    voOC.cusAddrEC10 = utils.getListProp(response, "list");
    // 自常用選取
    createDropdown("#cusAddrEC10", voOC.cusAddrEC10, {label:"=== "+utils.i18n("ec10.msg005")+" ===", value:0}, onChangeCusAddr);
}

// 變更常用送達地點
function onChangeCusAddr(item){
    console.log("onChangeCusAddr item ...\n", item);
    var deliveryId = item.value;
    if( !utils.isId(deliveryId) ){
        return;
    }
    var formData = {
        "type": "deliveryId",
        "deliveryId": deliveryId
    };
    var restUrl = CUS_ADDR_SEL_URL;
    utils.postData(restUrl, formData, false, afterChangeCusAddr, null, 'onChangeCusAddr');
}
function afterChangeCusAddr(response, formData){
    provinceEC10 = utils.getListProp(response, "provinceList");
    createDropdown("#provinceEC10", provinceEC10, {label:utils.i18n("ec10.province"), value:""}, onChangeProvinceEC10);
    cityEC10 = utils.getListProp(response, "cityList");
    createDropdown("#cityEC10", cityEC10, {label:utils.i18n("ec10.city"), value:""}, onChangeCityEC10);
    districtEC10 = utils.getListProp(response, "districtList");
    createDropdown("#districtEC10", districtEC10, {label:utils.i18n("ec10.district"), value:""}, onChangeDistrictEC10);
    townEC10 = utils.getListProp(response, "townList");
    createDropdown("#townEC10", townEC10, {label:utils.i18n("ec10.town"), value:""}, onChangeTownEC10);

    voOC.triggerDropdown = false;
    setDropdownValue("#provinceEC10", response.province);
    setDropdownValue("#cityEC10", response.city);
    setDropdownValue("#districtEC10", response.district);
    voOC.triggerDropdown = true;
    setDropdownValue("#townEC10", response.town);
}

// 車號(載運數量)
function buildCarInfo(selector, carList){
    var html = '<table class="w3-table-all" style="max-width:300px;" >'
            + '<thead>'
            + '  <tr class="ui-widget-header ui-state-default">';
    if( !voOC.toggleCombineDivEC10 ){// 併單固定一車
        html += '    <th>' + utils.i18n("operation") + '</th>';
    }
    html += '    <th>' + utils.i18n("car.no") + '</th>';
    html += '    <th>' + utils.i18n("fd.quantity") + '(' + utils.i18n("wt.unit.mt") + ')</th>';
    html += '  </tr>';
    html += '</thead>';
    html += '<tbody>';
    
    if( !isNA(carList) ){
        for (var i = 0; i < carList.length; i++) {
            var carNo = carList[i].carNo;
            var quantity = carList[i].quantity;
            quantity = isNA(quantity)?"":quantity;
            html += '  <tr class="w3-padding-small w3-hover-pale-blue tac">';
            if( !voOC.toggleCombineDivEC10 ){// 併單固定一車
                html += '    <td><button type="button" onclick="onClickDeleteCar(' + i + ')" title="' + utils.i18n("remove") + '" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button></td>';
            }
            html += '    <td><input id="carNo'+i+'" value="'+carNo+'" type="text" size="12" class="ui-inputtext ui-widget ui-state-default ui-corner-all" /></td>';
            html += '    <td><input id="quantity'+i+'" value="'+quantity+'" type="text" size="8" class="ui-inputtext ui-widget ui-state-default ui-corner-all" /></td>';
            html += '  </tr>';
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}
// 刪除車號
function onClickDeleteCar(idx){
    if( !utils.isEmptyAry(voOC.carListEdit) ){
        if( utils.isSingleItem(voOC.carListEdit) ){
            // 車號必填，不可全部刪除!
            _alert(utils.i18n("carNo.required"));
        }else{
            voOC.carListEdit.splice(idx, 1);
            buildCarInfo("#tbCarQuantity", voOC.carListEdit);
        }
    }
}
// 新增車號
function onClickAddCar(){
    voOC.carListEdit = utils.safeList(voOC.carListEdit);
    voOC.carListEdit.push({"carNo":""});
    buildCarInfo("#tbCarQuantity", voOC.carListEdit);
}
// 取得輸入 車號(載運數量) 資訊 (含檢查)
function getInputCarInfo(carList){
    var carAmountList = [];
    var carTotal = 0;
    var carNoStrList = "";// 檢查重複
    if( !isNA(carList) ){
        for (var i = 0; i < carList.length; i++) {
            var carNo = $("#carNo"+i).val().trim();
            var quantity = $("#quantity"+i).val().trim();
            if( isEmpty(carNo) || !utils.isNumeric(quantity) || Number(quantity)<=0 
                    || carNoStrList.toUpperCase().indexOf("["+carNo.toUpperCase()+"]")>-1 ){
                console.error("getInputCarInfo carNo = "+carNo+", quantity = ", quantity);
                // 車號(載運數量)輸入錯誤
                _alert(utils.i18n("ec10.carNo")+utils.i18n("in.err"));
                return null;
            }
            carNoStrList = carNoStrList + "[" + carNo + "]";
            quantity = utils.getNumber(quantity, "");
            carTotal = carTotal + quantity;
            carAmountList.push({"carNo":carNo, "quantity":quantity});
        }
    }
    
    var total = voOC.selectedOrder.quantity;// 併單在 OptionsREST 中已有加總處理
    if( carTotal !== total ){
        console.error("getInputCarInfo carTotal = "+carTotal+", total = ", total);
        // 與交易數量不符!
        _alert(utils.i18n("error.quantity"));
        return null;
    }

    if( utils.isEmptyAry(carAmountList) ){
        // 車號(載運數量)必填
        _alert(utils.i18n("car.quantity.required"));
        return null;
    }
    return carAmountList;
}

function onChangeTranTypeEC10(item){
    console.log("onChangeTranTypeEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeTranTypeEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var tranType = item.value;
    if( tranType==="C" ){
        $(".contractOnly").show();
    }else{
        $(".contractOnly").hide();
    }
}
// 變更下單客戶
function onChangeCustomerEC10(item){
    console.log("onChangeCustomerEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeContractEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var customerId = item.value;
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "customerId": customerId,
        "keys": "contract,sales"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeContractEC10');
}
// 變更合約
function onChangeContractEC10(item){
    console.log("onChangeContractEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeContractEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var contractId = item.value;
    var customerId = getDropdownValue("#customerEC10");
    var province = getDropdownValue("#provinceEC10");
    var city = getDropdownValue("#cityEC10");
    var district = getDropdownValue("#districtEC10");
    var town = getDropdownValue("#townEC10");
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "city": city,
        "district": district,
        "town": town,
        "customerId": customerId,
        "contractId": contractId,
        "keys": "shipMethod,plant"
    };

    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeContractEC10');
}
// 變更 省
function onChangeProvinceEC10(item){
    console.log("onChangeProvinceEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeProvinceEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var province = item.value;
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "keys": "city"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeProvinceEC10');
}
// 變更 市
function onChangeCityEC10(item){
    console.log("onChangeCityEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeCityEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var province = getDropdownValue("#provinceEC10");
    var city = item.value;
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "city": city,
        "keys": "district"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeCityEC10');
}
// 變更 區
function onChangeDistrictEC10(item){
    console.log("onChangeDistrictEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeDistrictEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var province = getDropdownValue("#provinceEC10");
    var city = getDropdownValue("#cityEC10");
    var district = item.value;
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "city": city,
        "district": district,
        "keys": "town"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeDistrictEC10');
}
// 變更 鎮
function onChangeTownEC10(item){
    console.log("onChangeTownEC10 item = ", item);
    if( isNA(item) || isNA(item.value) ){
        console.error("onChangeTownEC10 item = ", item);
        return;
    }
    if( !voOC.triggerDropdown ){
        return;
    }
    var province = getDropdownValue("#provinceEC10");
    var city = getDropdownValue("#cityEC10");
    var district = getDropdownValue("#districtEC10");
    var town = item.value;
    var formData = {
        "orderId": voOC.selectedOrder.id,
        "province": province,
        "city": city,
        "district": district,
        "town": town,
        "keys": "plant"
    };
    var restUrl = PO_TRAN_TO_EC10_OPS;
    utils.postData(restUrl, formData, false, buildEC10Form, null, 'onChangeTownEC10');
}
// 取消
function onClickCancelEC10(){
    voOC.toggleDivEC10 = false;
    voOC.toggleCombineDivEC10 = false;
    
    $("#fsOrderInfoEC10").hide();
}

// 確定送出 EC1.0 訂單
function onClickSendEC10(){
    console.log("onClickSendEC10 ...");
    // deliveryDate,tranType,shipMethod,customer,contract,province,city,district,town,salesArea,plant,sales
    var orderId = voOC.selectedOrder.id;
    var productCode = voOC.selectedOrder.productCode;
    
    var province = getDropdownValue("#provinceEC10");
    var city = getDropdownValue("#cityEC10");
    var district = getDropdownValue("#districtEC10");
    var town = getDropdownValue("#townEC10");
    
    var deliveryDate = getDropdownValue("#deliveryDateEC10");
    var tranType = getDropdownValue("#tranTypeEC10");
    var shipMethod = getDropdownValue("#shipMethodEC10");
    var customer = getDropdownValue("#customerEC10");
    var contract = getDropdownValue("#contractEC10");
    var plant = getDropdownValue("#plantEC10");
    var sales = getDropdownValue("#salesEC10");

    // 取得輸入 車號(載運數量) 資訊 (含檢查)
    var carAmountList = getInputCarInfo(voOC.carListEdit);

    var formData = {
        "productCode": productCode,
        "province":province,
        "city":city,
        "district":district,
        "town":town,
        "deliveryDate":deliveryDate,
        "tranType":tranType,
        "shipMethod":shipMethod,
        "customerId":Number(customer),
        "contractId":Number(contract),
        "plantId":Number(plant),
        "salesId":Number(sales),
        "carList":carAmountList
    };
    
    if( voOC.toggleCombineDivEC10 ){// 併單
        voOC.tranMode = "C";
        formData["orderList"] = voOC.checkedItems;
    }else{
        formData["orderId"] = orderId;
        if( carAmountList.length>1 ){
            voOC.tranMode = "S";// 拆單
        }
    }
    
    formData["tranMode"] = voOC.tranMode;
    
    //console.log("onClickSendEC10 formData : \n", formData);
    // 輸入檢查
    if( checkBeforeSendEC10(formData) ){
        // 確定要送出嗎? 確定後，將直接轉拋[台泥行動商城]訂單，每個車號一筆訂單。
        if( confirm(utils.i18n("confirm.send") + "\n( " + utils.i18n("confirm.ec10") + ")") ){
            var restUrl = PO_TRAN_TO_EC10_SEND;
            utils.postData(restUrl, formData, false, afterSendEC10, null, 'onClickSendEC10');
        }
    }
}
// 送出 EC1.0 訂單後結果
function afterSendEC10(response, formData){
    // renderOrderDetail(response, formData);
    if( utils.isEmptyAry(response.list) ){
        // 系統無回應結果!
        _alert(utils.i18n("no.result"));
        return;
    }
    
    var resList = response.list;
    var carsSuccess = "";
    var carsFail = "";
    var countSuccess = 0;
    var countFail = 0;
    var html = "<pre>";
    for(var i=0; i<resList.length; i++){
        if( resList[i].status==='OK' || resList[i].status==='SUCCESS' ){// 成功
            carsSuccess = (carsSuccess==="")? carsSuccess+resList[i].carNo:carsSuccess+","+resList[i].carNo;
            countSuccess++;
        }else{
            var msg = resList[i].carNo + " - ";
            if( utils.isEmptyAry(resList[i].errors) ){
                msg += resList[i].msg;
            }else{
                for(var e=0; e<resList[i].errors.length; e++){
                    msg += "[" + resList[i].errors[e] + "]";
                }
            }
            
            carsFail += (carsFail==="")?msg:"\n"+msg;
            countFail++;
        }
    }
    // 轉單成功"+countSuccess+"筆，對應車號
    html += (countSuccess>0)?utils.i18nWP("ec10.msg002", countSuccess, carsSuccess):"";
    if( countFail>0 ){
        // 轉單失敗"+countSuccess+"筆，對應車號及失敗原因如下
        html += (countSuccess>0)?"\n":"";
        html += utils.i18nWP("ec10.msg003", countFail)+"：\n";
        html += carsFail;
    }
    html += "</pre>";
    
    $("#divResultEC10").html(html);
    $("#divResultEC10").show();
    
    if( countSuccess>0 ){
        if( formData.tranMode==="C" ){
            $("#divBtnEC10").hide();
        }else {
            if( !isNA(voOC.selectedOrder) && utils.isId(voOC.selectedOrder.id) ){
                var restUrl = PO_FULL_URL.replace("{id}", voOC.selectedOrder.id);
                utils.fetchData(restUrl, false, afterReloadOrder);
            }
        }
    }
}

function afterReloadOrder(response){
    renderOrderDetail(response);
    $("#divResultEC10").show();
}

// 送出 EC1.0 訂單檢查
function checkBeforeSendEC10(formData){
    console.log("checkBeforeSendEC10 ...");

    if( !utils.isId(formData.orderId) && utils.isEmptyAry(formData.orderList) ){
        _alert(utils.i18n("edit.ec10.po")+utils.i18n("in.err"));
        return false;
    }
    if( utils.isEmptyAry(formData.carList) ){
        return false;
    }
    if( isEmpty(formData.province) ){
        _alert(utils.i18n("ec10.province")+utils.i18n("in.err"));
        return false;
    }
    if( isEmpty(formData.city) ){
        _alert(utils.i18n("ec10.city")+utils.i18n("in.err"));
        return false;
    }
    if( isEmpty(formData.district) ){
        _alert(utils.i18n("ec10.district")+utils.i18n("in.err"));
        return false;
    }
    if( isEmpty(formData.town) ){
        _alert(utils.i18n("ec10.town")+utils.i18n("in.err"));
        return false;
    }
    
    if( isEmpty(formData.deliveryDate) ){
        _alert(utils.i18n("ec10.trandate")+utils.i18n("in.err"));
        return false;
    }
    if( isEmpty(formData.tranType) ){
        _alert(utils.i18n("ec10.trantype")+utils.i18n("in.err"));
        return false;
    }
    if( isEmpty(formData.shipMethod) ){
        _alert(utils.i18n("ec10.shipMethod")+utils.i18n("in.err"));
        return false;
    }
    
    if( !utils.isId(formData.customerId) ){
        _alert(utils.i18n("ec10.customer")+utils.i18n("in.err"));
        return false;
    }
    if( formData.tranType==="C" ){// 合約
        if( !utils.isId(formData.contractId) ){
            _alert(utils.i18n("ec10.contractNo")+utils.i18n("in.err"));
            return false;
        }
    }
    if( !utils.isId(formData.plantId) ){
        _alert(utils.i18n("ec10.plant")+utils.i18n("in.err"));
        return false;
    }
    if( !utils.isId(formData.salesId) ){
        _alert(utils.i18n("ec10.sales")+utils.i18n("in.err"));
        return false;
    }

    return true;
}
// 顯示轉 EC1.0 補充資訊
function renderOrderViewEC10(tccOrder, carList){
    if( isNA(tccOrder) ){
        console.log("renderOrderViewEC10 ...");
        return;
    }

    $("#deliveryEC10_Lable").html(utils.prettyPrint(tccOrder.deliveryName));
    
    $("#deliveryDateEC10_Lable").html(utils.prettyPrint(utils.formatFromSapDate(tccOrder.deliveryDate)));
    $("#customerEC10_Lable").html(utils.prettyPrint(tccOrder.customerName));
    
    $("#tranTypeEC10_Lable").html(utils.prettyPrint(tccOrder.tranTypeName));
    $("#contractEC10_Lable").html(utils.prettyPrint(tccOrder.contractName));
    
    $("#plantEC10_Lable").html(utils.prettyPrint(tccOrder.plantName));
    $("#shipMethodEC10_Lable").html(utils.prettyPrint(tccOrder.methodName));
    $("#salesEC10_Lable").html(utils.prettyPrint(tccOrder.salesName));
    
    if( !utils.isEmptyAry(carList) ){
        buildCarInfoView("#tbCarQuantityView", carList);
    }
    
    if( tccOrder.tranType!=="C" ){// 合約
        $(".contractOnly").hide();
    }

    $("#fsOrderInfoEC10View").show();
}
// 顯示轉 EC1.0 補充資訊 - 車號、數量部分
function buildCarInfoView(selector, carList){
    var html = '<table class="w3-table-all" style="max-width:300px;" >'
            + '<thead>'
            + '  <tr class="ui-widget-header ui-state-default">'
            + '    <th>' + utils.i18n("car.no") + '</th>'
            + '    <th>' + utils.i18n("fd.quantity") + '(' + utils.i18n("wt.unit.mt") + ')</th>'
            + '  </tr>'
            + '</thead>'
            + '<tbody>';
    if( !isNA(carList) ){
        for (var i = 0; i < carList.length; i++) {
            var carNo = carList[i].carNo;
            var quantity = carList[i].quantity;
            quantity = isNA(quantity)?"":quantity;
            html += '  <tr class="w3-padding-small w3-hover-pale-blue">'
                 + '    <td><span>' + carNo + '</span></td>'
                 + '    <td class="tar"><span>' + quantity + '</span></td>'
                 + '  </tr>';
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for CarNo EC1.5">
// 初始車號顯示區域
function initCarNoArea(){
    var carNoListOri = voOC.selectedOrder.carList;
    voOC.editCarNo = false;
    voOC.carNoList = [];
    if( !utils.isEmptyAry(carNoListOri) ){
        for(var i=0; i<carNoListOri.length; i++){
            if( !isEmpty(carNoListOri[i].carNo) ){
                voOC.carNoList.push(carNoListOri[i].carNo.trim());
            }
        }
    }
    renderCarNoList(voOC.carNoList);
    
    $("#carNoView").show();
    $(".carNoEdit").hide();
    if( voOC.selectedOrder.shipStatus===SHIP_STATUS_ARRIVED ){// 未到貨前，才可修改車號
        $("#btnCarNoEdit").hide();
    }else{
        $("#btnCarNoEdit").show();
    }
}
// 建立車號編輯按鈕
function buildCarNoBtns(){
    createBtn("#btnCarNoEdit", "fa fa-pencil", onClickCarNoEdit);
    createBtn("#btnCarNoAdd", "fa fa-plus-circle", onClickCarNoAdd);
    createDropdown('#selFavCarNoAdd', voOC.favCarNoList, {label:utils.i18n("ec10.msg005"), value:""}, onChangeFavCarNo);// 自常用選取
    createBtn("#btnCarNoSave", "fa fa-save", onClickCarNoSave);
    createBtn("#btnCarNoCancel", "fa fa-ban", onClickCarNoCancel);
}
// 顯示車號
function renderCarNoList(carList){
    var html = "";
    if( !isNA(carList) ){
        for (var i = 0; i < carList.length; i++) {
            var carNo = carList[i].toUpperCase().trim();
            if( html==="" ){
                html += carNo;
            }else{
                html += "、" + carNo;
            }
        }
    }
    $("#carNo").html(html);
}
// 編輯車號
function onClickCarNoEdit(){
    renderCarNoForm(voOC.carNoList);
    
    voOC.editCarNo = true;
    $("#carNoView").hide();
    $(".carNoEdit").show();
}
// 新增車號
function onClickCarNoAdd(){
    voOC.carNoList = utils.safeList(voOC.carNoList);
    voOC.carNoList.push("");
    renderCarNoForm(voOC.carNoList);
}
// 自常用車號選取加入
function onChangeFavCarNo(item){
    var carNo = item.value;
    if( isEmpty(carNo) ){
        return;
    }
    voOC.carNoList = utils.safeList(voOC.carNoList);
    for(var i=0; i<voOC.carNoList.length; i++){
        if( voOC.carNoList[i].trim()===carNo ){
            // 車號已存在!
            _alert(utils.i18n("car.existed"));
            return;
        }
    }
    
    voOC.carNoList.push(carNo);
    renderCarNoForm(voOC.carNoList);
}
// 儲存車號
function onClickCarNoSave(){
    console.log("onClickCarNoSave ...");
    var len = voOC.carNoList.length;
    voOC.carNoList = [];
    for(var i=0; i<len; i++){
        var carNo = $("#carNo_"+i).val();
        carNo = carNo.toUpperCase().trim();
        voOC.carNoList.push(carNo);
    }
    
    var orderId = voOC.selectedOrder.id;
    var productId = voOC.selectedOrder.productId;
    var formData = {
        "orderId":orderId,
        "prdId":productId,
        "carNoList":voOC.carNoList
    };
    console.log("onClickCarNoSave formData =\n", formData);
    // 輸入檢查
    if( checkBeforeSaveCarNo(formData) ){
        // 確定要修改車號?
        if( confirm(utils.i18n("confirm.carNo")) ){
            var restUrl = PO_CAR_NO_SAVE;
            utils.postData(restUrl, formData, false, afterSaveCarNo, null, 'onClickCarNoSave');
        }
    }
}
// 儲存車號檢查
function checkBeforeSaveCarNo(formData){
    var carList = formData.carNoList;
    var carNoStrList = "";// 檢查重複
    if( !isNA(carList) ){
        for (var i=0; i < carList.length; i++) {
            var carNo = carList[i].toUpperCase().trim();
            if( isEmpty(carNo) || carNo.length>20 
             || carNoStrList.toUpperCase().indexOf("["+carNo.toUpperCase()+"]")>-1 ){
                console.error("getInputCarInfo carNo = "+carNo);
                // 輸入錯誤
                _alert(utils.i18n("in.err"));
                return false;
            }
            carNoStrList = carNoStrList + "[" + carNo + "]";
        }
    }
    return true;
}
// after 儲存車號
function afterSaveCarNo(response, formData){
    voOC.selectedOrder.carList = utils.safeList(response.list);
    initCarNoArea();
}
// 取消編輯車號
function onClickCarNoCancel(){
    initCarNoArea();
}
// 顯示車號編輯表單
function renderCarNoForm(list){
    list = utils.safeList(list);
    var html = "";
    for(var i=0; i<list.length; i++){
        var carNo = list[i];
        html += '<input id="carNo_'+i+'" value="'+carNo+'" onblur="keepCarNo('+i+')" type="text" size="10" class="ui-inputtext ui-widget ui-state-default ui-corner-all" />&nbsp;';
        html += '<button type="button" onclick="deleteCarNo('+i+')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>';
        html += '<button type="button" onclick="addFavoriteCar('+i+')" title="'+utils.i18n("add.favorites")+'" class="w3-btn w3-green" style="margin-left:1px;" ><i class="fa fa-star"></i></button>&nbsp;&nbsp;';
    }
    $("#carNoForm").html(html);
}
// 暫存
function keepCarNo(idx){
    var carNo = $("#carNo_"+idx).val();
    carNo = carNo.toUpperCase().trim();
    voOC.carNoList[idx] = carNo;
}
// 刪除
function deleteCarNo(idx){
    voOC.carNoList.splice(idx, 1);
    renderCarNoForm(voOC.carNoList);
}
// 加入常用車號
function addFavoriteCar(idx){
    var carNo = voOC.carNoList[idx];
    if( isEmpty(carNo) ){
        _alert(utils.i18n("can.not.empty"));
        return;
    }
    var formData = {
        "type": "carNo",
        "carNo": carNo
    };
    var restUrl = CUS_ADDR_ADD_URL;
    utils.postData(restUrl, formData, false, afterAddFavoriteCar, null, 'addFavoriteCar');
}
function afterAddFavoriteCar(response, formData){
    // 加入選單
    voOC.favCarNoList.push({value:formData.carNo, label:formData.carNo});
    createDropdown('#selFavCarNoAdd', voOC.favCarNoList, {label:utils.i18n("ec10.msg005"), value:""}, onChangeFavCarNo);// 自常用選取

    _alert(utils.i18n("add.max.favorites"));
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
    var idx = utils.fundIndexByKey(voOC.rfqMessageFL.list, "id", row.id);
    var html = '<button onclick="voOC.rfqMessageFL.onEditRow('+ idx + ')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal w3-center" ><i class="fa fa-pencil-square"></i></button>'
            + '&nbsp;'
            + '<button onclick="voOC.rfqMessageFL.onDeleteRow(' + idx + ')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange w3-center" ><i class="fa fa-trash"></i></button>'
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

//<editor-fold defaultstate="collapsed" desc="for Order Process Records">
// 處理記錄表單
function buildRecordProcessForm(){
    //$("#processTime").datetimepicker({"showTimepicker":true, "showButtonPanel":true});
    createDatePicker("#processTime");
    $("#process").puiinputtextarea({autoResize:true});
    $("#editRecordForm")[0].reset();
}

function onEditProcess(row){
    setTextValue("#processTime", utils.formatDateHMStr(row.processTime));
    setTextValue("#process", row.process);
}

function getFormDataProcess(formData){
    //console.log("getFormDataProcess ", formData['processTime']);
    //console.log("getFormDataProcess ", toISODateTime(formData['processTime']));
    formData['processTime'] = toISODateTime(formData['processTime']);
    return formData;
}

function valiateProcess(row){
    if( isEmpty($("#process").val()) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("process.noempty"));
        return false;
    }
    return true;
}

function afterSaveProcess(response){
    console.log("afterSaveProcess ...");
    reloadDataTable('#existedRecords', true);// keep status reload    
    utils.addSuccessMsg();
}

// 產生訂單處理紀錄列表
function renderProcessList(nameFL, selector, data){
    data = utils.safeList(data);
    var pageSize = 10;
    var colSettings = [
        {field: "id", headerText: utils.i18n("operation"), sortable: false, bodyClass: "tac", content: genOperationContent},
        {field: "processTime", headerText: utils.i18n("fd.processTime"), sortable: false, content: genProcessTimeContent},
        {field: "process", headerText: utils.i18n("fd.process"), sortable: false},
        {field: "recordTime", headerText: utils.i18n("fd.recordTime"), sortable: false, content: genRecordTimeContent}
    ];
    renderDataTable(selector, pageSize, colSettings, data, null, 400, 70);
}

// 操作
function genOperationContent(row){
    var idx = utils.fundIndexByKey(voOC.orderProcessFL.list, "id", row.id);
    var html = '<button onclick="voOC.orderProcessFL.onEditRow('+ idx + ')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal w3-center" ><i class="fa fa-pencil-square"></i></button>'
            + '&nbsp;'
            + '<button onclick="voOC.orderProcessFL.onDeleteRow(' + idx + ')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange w3-center" ><i class="fa fa-trash"></i></button>'
    ;
    return html;
}
// 處理時間
function genProcessTimeContent(row){
    var dt = formatDateTimeStr(row.processTime);
    console.log("genProcessTimeContent ", dt);
    if( !isNA(dt) && dt.length>16 ){
        return dt.substring(0, 16);
    }
    return dt;
}
// 記錄時間
function genRecordTimeContent(row){
    return formatDateTimeStr(row.recordTime);
}
//</editor-fold>
