/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, layout.js
 */
/* global PN_HOME, optional, STA_ORDER_STATUS_URL, w3, STA_RFQ_STATUS_URL, STA_CUS_LEVEL_URL, STA_PRD_STATUS_URL, STA_PRD_TYPE_URL, ST_MSG_COUNT_URL, genCreateTimeContent, TEST_DATA, ST_MSG_LIST_URL, ST_MSG_REPLY_LIST_URL, ST_MSG_REPLY_URL, STA_ORDER_SUM_URL, c3, _language, DEF_LANG, STA_ST_TODO_URL, TODO_PRD_ONSALES, TODO_PRD_CORRECT, TODO_QUOTATION, TODO_PO_CONFIRM, TODO_PO_PAY_RECEIVED, TODO_PO_SHIP, TODO_PO_MSG_REPLY, TODO_MEM_MSG_REPLY, PRD_STATUS_PASS, PRD_STATUS_REJECT, RFQ_STATUS_INQUIRY, PO_STATUS_PENDING, PAY_STATUS_NOTIFY_PAID, PAY_STATUS_PAID, SHIP_STATUS_NOT, CREDIT_STATUS_APPLY, TODO_CUS_CREDITS, _COLORS, STA_ST_URL, _tccDealer, utils */
// columns for datagrid
var USE_DEMO_DATA = false;
var GRID_COLS = [
    {"cols":1, "min":0, "max":600},
    {"cols":2, "min":601, "max":1024},
    {"cols":3, "min":1025, "max":9999}
];
var TODO_ROWS = 2;// 有幾個代辦事項

var vo = {// variable objects the JS
    //itemSelected: null,// 選取
    htmlNoData: "",
    todoList: [],
    gridCols: 0
};

// 營運管理： C11 商品銷售量, C12 商品庫存量, C21 商品未出貨量, C31 銷售金額, C32 未付款金额-逾期金額
// 銷售分析： C41 依客戶, C42 依市場 - 依销售趋势, C51 依商品, C52 依區域
var chartMap = {};

$(document).ready(function(){
    if( !utils.checkBrowserSupport() ){ return; }
    w3.includeHTML(start);
});

function start(){
    console.log(utils.formatDT(new Date()));
    // 登入檢查
    utils.checkLogin();// trigger _renderThisPage
}

function _renderThisPage(){
    // init layout
    initLayout(false, true);
    renderPanels();
    vo.htmlNoData = genNoDataImg();
    // Dashboard
    $("#divMsgReply").hide();

    // Ajax 產出UI相關
    fetchStatisticData("statistic");
    initChartInput();// 會觸發一次 fetchChartDataXXX();
    //buildMemberMsg({}, "memberMsg");

    console.log(utils.formatDT(new Date()));
}

function renderPanels(){
    // puipanel
    $('#plTodo').puipanel({"title":utils.i18n("store.todo.count")});
    $('#plOrderStatus').puipanel({"title":utils.i18n("order.status")});
    $('#plRfqStatus').puipanel({"title":utils.i18n("rfq.status")});
    $('#plCusLevel').puipanel({"title":utils.i18n("cus.level")});
    $('#plPrdStatus').puipanel({"title":utils.i18n("prd.status")});
    $('#plPrdType').puipanel({"title":utils.i18n("sale.prd.type")});
    $('#plOrderCumulative').puipanel({"title":utils.i18n("order.cumulative.status")});
    $('#plMemberMsg').puipanel({"title":utils.i18n("visitor.msg")});
    
    createDatePicker("#endAt", new Date());
    createBtn('#btnConfirm', 'fa-search', onClickOrderCumulative);

    $('.card').css("min-height", "380px");
    //$('.card').css("max-height", "400px");
}

function onClickOrderCumulative(){
    fetchOrderCumulative();
}

function autoResizeGrid(){
    $(window).resize(function(){
        var cols = getColNumByWidth(TODO_ROWS);
        console.log("cols = ", cols);
        if( vo.gridCols!==cols ){
            vo.gridCols = cols;
            renderTodoGrid(vo.todoList, vo.gridCols);
        }
    });
    $(window).resize();
}

//<editor-fold defaultstate="collapsed" desc="for Statistic Chart Operation">
function initChartInput(){
    // ChartJS Plugin register
    registerPluginsForChartJS();
    // chart panels
    $('#plC11').puipanel({"title":utils.i18n('title.product.sales')});// 商品銷售量
    $('#plC12').puipanel({"title":utils.i18n('title.product.inventory')});// 商品庫存量
    $('#plC21').puipanel({"title":utils.i18n('title.undelivered.orders')});// 商品未出貨量
    //$('#plC31').puipanel({"title":utils.i18n('title.order.amt')});// 銷售金額 // X預計收款金額 title.cash.flow
    $('#plC32').puipanel({"title":utils.i18n('title.order.due')});// 未付款金额-逾期金額

    $('#plC41').puipanel({"title":utils.i18n('title.by.customer')});// 依客戶
    $('#plC42').puipanel({"title":utils.i18n('title.by.market')});// 依市場 - 依销售趋势
    $('#plC51').puipanel({"title":utils.i18n('title.by.product')});// 依商品
    $('#plC52').puipanel({"title":utils.i18n('title.by.area')});// 依區域

    var months = utils.genYMOpions(12, "/");
    // 營運管理：
    createDropdown("#slC11", months, null, onChangeDateC11, "ui-widget");
    setDropdownValue("#slC11", months[1].value);// 預設上個月
    createDropdown("#slC12", months, null, onChangeDateC12, "ui-widget");
    setDropdownValue("#slC12", months[1].value);// 預設上個月
    
    createDropdown("#slC21", months, null, onChangeDateC21, "ui-widget");
    setDropdownValue("#slC21", months[1].value);// 預設上個月
    
    //createDropdown("#slC31", months, null, onChangeDateC31, "ui-widget");
    //setDropdownValue("#slC31", months[1].value);// 預設上個月
    createDropdown("#slC32", months, null, onChangeDateC32, "ui-widget");
    setDropdownValue("#slC32", months[1].value);// 預設上個月
    // 銷售分析：
    createDropdown("#slC41", months, null, onChangeDateC41, "ui-widget");
    setDropdownValue("#slC41", months[1].value);// 預設上個月
    createDropdown("#slC42", months, null, onChangeDateC42, "ui-widget");
    setDropdownValue("#slC42", months[1].value);// 預設上個月
    
    createDropdown("#slC51", months, null, onChangeDateC51, "ui-widget");
    setDropdownValue("#slC51", months[1].value);// 預設上個月
    createDropdown("#slC52", months, null, onChangeDateC52, "ui-widget");
    setDropdownValue("#slC52", months[1].value);// 預設上個月
}
// for 營運管理
// 1-1 Product Sales - Pie 商品銷售量
function onChangeDateC11(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C11");
        return;
    }
    fetchChartData("prdSales", "C11", renderChartForSingleDataset);
}
// 1-2 Product Inventory - Bar 商品庫存量
function onChangeDateC12(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C12");
        return;
    }
    fetchChartData("prdInventory", "C12", renderChartForSingleDataset);
}
// 2-1 Undelivered Orders - Bar 商品未出貨量
function onChangeDateC21(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C21");
        return;
    }
    fetchChartData("undelivered", "C21", renderChartForSingleDataset);
}
// 3-1 Sales Amount - Bar 銷售金額
function onChangeDateC31(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C31");
        return;
    }
    fetchChartData("salesAmt", "C31", renderChartForSingleDataset);
}

// 3-2 Order Due - Bar 未付款金额-逾期金額
function onChangeDateC32(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C32");
        return;
    }
    fetchChartData("orderDue", "C32", renderChartForSingleDataset);
}
// for 銷售分析
// 4-1 By Customer - Bar 依客戶
function onChangeDateC41(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C41");
        return;
    }
    fetchChartData("anaByCustomer", "C41", renderChartForPairDataset);
}
// 4-2 By Market - Line 依市場 - 依销售趋势
function onChangeDateC42(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C42");
        return;
    }
    fetchChartData("anaByMarket", "C42", renderChartForMultiDataset);
}
// 5-1 By Product - Bar 依商品
function onChangeDateC51(){
    if( USE_DEMO_DATA ){
        renderDemoChart("C51");
        return;
    }
    fetchChartData("anaByProduct", "C51", renderChartForPairDataset);
}
// 5-2 By Area - Bar 依區域 // TODO
function onChangeDateC52(){
    //if( USE_DEMO_DATA ){
        renderDemoChart("C52");
    //    return;
    //}
    //fetchChartData("anaByArea", "C52", renderChartForPairDataset);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Statistic Chart Common Function">
// fetch chart data
function fetchChartData(type, selectorKey, callback){
    var formData = {};
    formData['type'] = type;//"prdSales";
    formData['endAt'] = getDropdownValue("#sl"+selectorKey);
    var restUrl = STA_ST_URL;
    utils.postData(restUrl, formData, false, callback, callback, selectorKey);
}

// for 單組資料 (Pie、Bar)
function renderChartForSingleDataset(response, formData, selectorKey){
    console.log("renderChartForSingleDataset selectorKey = ", selectorKey);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#div"+selectorKey).html(vo.htmlNoData);
        //chartMap[selectorKey] = null;
    }else{
        var datas = [];
        var labels = [];
        var colors = [];
        for(var i=0; i<list.length; i++){
            labels.push(list[i].label);
            datas.push(isNA(list[i]["value"])?0:list[i]["value"]);
            colors.push(_COLORS[i%_COLORS.length]);
        }
        
        var PIES = ["C11"];// 使用 Pie Chart 的代號
        var config = PIES.indexOf(selectorKey)>=0
                        ?genPieConfig(datas, labels, colors)
                        :genBarConfig([datas], labels, colors);// 注意: BarChart 的 datas 要再包一層[] 
        
        var canvasId = "cv"+selectorKey;
        $("#div"+selectorKey).html("<canvas id=\""+canvasId+"\" width=\"400\" height=\"250\"></canvas>");
        chartMap[selectorKey] = renderChartJS("#"+canvasId, config);
    }
}

// for 雙組資料 (Bar)
function renderChartForPairDataset(response, formData, selectorKey){
    console.log("renderChartForPairDataset selectorKey = ", selectorKey);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#div"+selectorKey).html(vo.htmlNoData);
        //chartMap[selectorKey] = null;
    }else{
        var datas = [[], []];
        var labels = [];
        var colors = [];
        for(var i=0; i<list.length; i++){
            labels.push(list[i].label);
            datas[0].push(isNA(list[i]["value"])?0:list[i]["value"]);
            datas[1].push(isNA(list[i]["value2"])?0:list[i]["value2"]);
        }
        colors.push(_COLORS[0]);
        colors.push(_COLORS[1]);

        var config = genBarConfig(datas, labels, colors);
        console.log("renderChartForPairDataset config=\n", config);
        var canvasId = "cv"+selectorKey;
        $("#div"+selectorKey).html("<canvas id=\""+canvasId+"\" width=\"400\" height=\"250\"></canvas>");
        chartMap[selectorKey] = renderChartJS("#"+canvasId, config);
    }
}

// for 多組資料 (Line)
function renderChartForMultiDataset(response, formData, selectorKey){
    console.log("renderChartForMultiDataset selectorKey = ", selectorKey);
    var nodata = (isNA(response) || isNA(response.nodata))?true:response.nodata;
    if( nodata ){
        $("#div"+selectorKey).html(vo.htmlNoData);
        return;
    }
    var datasets = isNA(response)?null:response.datasets;
    var timeSeriesLabels = isNA(response)?null:response.timeSeriesLabels;
    var datasetLabels = isNA(response)?null:response.datasetLabels;   
    if( utils.isEmptyAry(datasets) || utils.isEmptyAry(timeSeriesLabels) || utils.isEmptyAry(datasetLabels) ){
        $("#div"+selectorKey).html(vo.htmlNoData);
        //chartMap[selectorKey] = null;
    }else{
        // for 多組資料 (Line)
        var colors = [];
        for(var i=0; i<datasets.length; i++){
            colors.push(_COLORS[i%_COLORS.length]);
        }
        var specDataIdxs = [0];
        var config = genLineConfig(datasets, datasetLabels, timeSeriesLabels, colors, specDataIdxs);
        
        var canvasId = "cv"+selectorKey;
        $("#div"+selectorKey).html("<canvas id=\""+canvasId+"\" width=\"400\" height=\"250\"></canvas>");
        chartMap[selectorKey] = renderChartJS("#"+canvasId, config);
    }
}
//</editor-fold>

function fetchStatisticData(ajaxKey){
    // GET
    utils.fetchData(STA_ST_TODO_URL, false, renderTodo, null, "storeTodo");
    /*
    utils.fetchData(STA_ORDER_STATUS_URL, false, renderOrderStatus, null, "orderStatus");
    utils.fetchData(STA_RFQ_STATUS_URL, false, renderRfqStatus, null, "rfqStatus");
    utils.fetchData(STA_CUS_LEVEL_URL, false, renderCusLevel, null, "cusLevel");
    utils.fetchData(STA_PRD_STATUS_URL, false, renderPrdStatus, null, "prdStatus");
    utils.fetchData(STA_PRD_TYPE_URL, false, renderPrdType, null, "prdType");
    */
    // POST
    //fetchOrderCumulative();
}

// To Do List
function renderTodo(response){
    vo.todoList = utils.safeList(response.list);
    autoResizeGrid();
}

function renderTodoGrid(list, columns){
    $('#gridTodo').html("<div id='dgTodo'></div>");// for puidatagrid redraw 
    
    // for DEMO
    var demoList = [];
    for(var i=0; i<list.length; i++){
        //  待報價詢價單、待確認訂、待出貨訂單
        if( (!_tccDealer && list[i].code===TODO_QUOTATION) || list[i].code===TODO_PO_CONFIRM || list[i].code===TODO_PO_SHIP ){
            demoList.push(list[i]);
        }
    }
    
    $('#dgTodo').puidatagrid({
        columns: columns,
        datasource: demoList,// for DEMO list,
        content: function(data) {
            if( isNA(data) || isNA(data.label) ){
                return "";
            }
            var html = "<div class='divTodo' >";
            html += "<i class='fa fa-info-circle' ></i>&nbsp;<span class='fieldname' >" + data.label + "：</span>";
            html += "<span style='color:red;' >" + data.value + "</span>";
            if( !isNA(data) && data.value>0 ){
                var url = "#";
                if( data.code===TODO_PRD_ONSALES ){// 待上架商品
                    url = "myProducts.html?status="+PRD_STATUS_PASS;
                }else if( data.code===TODO_PRD_CORRECT ){// 待修正商品
                    url = "myProducts.html?status="+PRD_STATUS_REJECT;
                }else if( data.code===TODO_QUOTATION ){// 待報價詢價單
                    url = "rfqs.html?rfqStatus="+RFQ_STATUS_INQUIRY;
                }else if( data.code===TODO_PO_CONFIRM ){// 待確認訂單
                    url = "orders.html?status="+PO_STATUS_PENDING;
                }else if( data.code===TODO_PO_PAY_RECEIVED ){// 待確認收款訂單
                    url = "orders.html?status="+PO_STATUS_PENDING+"&payStatus="+PAY_STATUS_NOTIFY_PAID;
                }else if( data.code===TODO_PO_SHIP ){// 待出貨訂單
                    url = "orders.html?status="+PO_STATUS_PENDING+"&payStatus="+PAY_STATUS_PAID+"&shipStatus="+SHIP_STATUS_NOT;
                }else if( data.code===TODO_CUS_CREDITS ){// 信用額度申請
                    url = "customers.html?creditsStatus="+CREDIT_STATUS_APPLY;
                }else if( data.code===TODO_PO_MSG_REPLY ){// 待回覆訊息訂單
                    url = "orders.html?replyMsg=true";
                }else if( data.code===TODO_MEM_MSG_REPLY ){// 待回覆訪客留言
                    url = "javascript:showMsgForReply();";
                }
                html += "&nbsp;<a class='w3-btn linkTodo' href='"+ url+"' ><i class='fa fa-external-link' ></i>"+ utils.i18n("goto.process") + "</a>";
            }
            html += "</div>";
            return html;
        }
    });
}

// 待回覆訪客留言
function showMsgForReply(){
    setCheckbox("#replyMsg", true);
    utils.scrollTo("#plMemberMsg");
}

function fetchOrderCumulative(){
    var formData = {};
    formData["type"] = "orderCumulative";
    //var endAt = $("#endAt").datepicker('getDate');
    //formData["endAt"] = utils.formatDT(endAt);
    formData["endAt"] = getDatePicker("#endAt");
    console.log("fetchOrderCumulative = {}", formData["endAt"]);
    utils.postData(STA_ORDER_SUM_URL, formData, false, renderOrderCumulative, null, "orderCumulative");
}

//<editor-fold defaultstate="collapsed" desc="for Statistic">
// 訂單狀態
function renderOrderStatus(response, ajaxKey){
    console.log("renderOrderStatus response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divOrderStatus").html(vo.htmlNoData);
    }else{
        renderStatisticPie("#divOrderStatus", list);
    }
}
// 詢價單狀態
function renderRfqStatus(response, ajaxKey){
    console.log("renderRfqStatus response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divRfqStatus").html(vo.htmlNoData);
    }else{
        renderStatisticPie("#divRfqStatus", list);
    }
}
// 訂單累計
function renderOrderCumulative(response, formData, ajaxKey){
    console.log("renderOrderCumulative response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divOrderCumulative").html(vo.htmlNoData);
    }else{
        renderStatisticList("#divOrderCumulative", list, utils.i18n("money.unit"));
    }
}
// 客戶等級
function renderCusLevel(response, ajaxKey){
    console.log("renderCusLevel response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divCusLevel").html(vo.htmlNoData);
    }else{
        renderStatisticPie("#divCusLevel", list);
    }
}
// 商品狀態
function renderPrdStatus(response, ajaxKey){
    console.log("renderPrdStatus response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divPrdStatus").html(vo.htmlNoData);
    }else{
        renderStatisticPie("#divPrdStatus", list);
    }
}
// 銷售商品類別
function renderPrdType(response, ajaxKey){
    console.log("renderPrdType response = \n", response);
    var list = utils.getResponseList(response);
    if( utils.isEmptyAry(list) ){
        $("#divPrdType").html(vo.htmlNoData);
    }else{
        renderStatisticPie("#divPrdType", list);
    }
}

// 顯示統計列表
function renderStatisticList(selector, list, unit){
    var html = "<div class='statlist' ><ul>";
    if( !isNA(list) ){
        for(var i=0; i<list.length; i++){
            var label = !isEmpty(list[i].label)?(utils.i18n("label.now")+list[i].label):utils.i18n("none.cat");
            var value = !isNA(list[i].value)?list[i].value:0;
            unit = isNA(unit)? utils.i18n("rows"):unit;
            var txt = label + "：" + utils.printNumber(value) + " " + unit;
            html += "<li>" + txt + "</li>";
        }
    }
    html += "</ul></div>";
    
    $(selector).html(html);
}

// 顯示統計 Pie Chart
function renderStatisticPie(selector, list, unit){
    var columns = [];
    if( !isNA(list) ){
        for(var i=0; i<list.length; i++){
            var label = !isEmpty(list[i].label)?list[i].label:utils.i18n("none.cat");
            var value = !isNA(list[i].value)?list[i].value:0;
            unit = isNA(unit)? utils.i18n("rows"):unit;
            var txt = label + "：" + value  + " " + unit;
            columns.push([txt , value]);
        }
    }

    console.log("renderStatisticPie columns = \n", columns);
    var sort = true;
    var generate = function () { return c3.generate({
            bindto:  selector,
            data: {
                columns: columns,
                type : 'pie'
            },
            pie: {
                sort: sort,
                onmouseover: function (d, i) { console.log(d, i); },
                onmouseout: function (d, i) { console.log(d, i); },
                onclick: function (d, i) { console.log(d, i); }
            }
        });
    };
  
    var chart = generate();
}

function sort(value1, value2){
    return value1 > value2;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Demo Chart">
function renderDemoChart(key){
    if( key==="C11" ){// 1-1 Product Sales - Pie 商品銷售量
        var datasC11 = [22, 28, 20, 30];
        var labelsC11 = [utils.i18n("demo.prd1"), utils.i18n("demo.prd2"), utils.i18n("demo.prd3"), utils.i18n("demo.prd4")];
        var colorC11 = ["rgb(208,146,167)","rgb(128,158,194)","rgb(231,188,41)","rgb(243,164,71)"];
        var configC11 = genPieConfig(datasC11, labelsC11, colorC11);
        if( isNA(chartMap["C11"]) ){
            $("#divC11").html("<canvas id=\"cvC11\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C11"] = renderChartJS("#cvC11", configC11);
        }else{
            chartMap["C11"].config = configC11;
            chartMap["C11"].update();
        }
    }else if( key==="C12" ){// 1-2 Product Inventory - Bar 商品庫存量
        var datasetLine = {
                //label: '安全庫存',
                // Changes this dataset to become a line
                type: 'line',
                borderColor: "rgb(156,133,192)",
                borderWidth: 2,
                data: [200, 200, 200, 200],
                fill: false
            };    
        var datasC12 = [[200, 250, 300, 250]];
        var labelsC12 = [utils.i18n("demo.prd1"), utils.i18n("demo.prd2"), utils.i18n("demo.prd3"), utils.i18n("demo.prd4")];
        var colorC12 = ["rgb(208,146,167)","rgb(128,158,194)","rgb(231,188,41)","rgb(243,164,71)"];
        var configC12 = genBarConfig(datasC12, labelsC12, colorC12);       
        if( isNA(chartMap["C12"]) ){
            $("#divC12").html("<canvas id=\"cvC12\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C12"] = renderChartJS("#cvC12", configC12);
        }else{
            chartMap["C12"].config = configC12;
            chartMap["C12"].update();
        }
    }else if( key==="C21" ){// 2-1 Undelivered Orders - Bar 商品未出貨量
        var datasC21 = [[80, 100, 200, 150]];
        var labelsC21 = [utils.i18n("demo.prd1"), utils.i18n("demo.prd2"), utils.i18n("demo.prd3"), utils.i18n("demo.prd4")];
        var colorC21 = ["rgb(165,181,146)","rgb(243,164,71)","rgb(231,188,41)","rgb(208,146,167)"];
        var configC21 = genBarConfig(datasC21, labelsC21, colorC21);
        if( isNA(chartMap["C21"]) ){
            $("#divC21").html("<canvas id=\"cvC21\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C21"] = renderChartJS("#cvC21", configC21);
        }else{
            chartMap["C21"].config = configC21;
            chartMap["C21"].update();
        }
    }else if( key==="C31" ){// 3-2 Cash Flow
        var datasC31 = [[120000, 53000, 75000, 52000, 61000]];
        var labelsC31 = [utils.i18n("demo.cus1"), utils.i18n("demo.cus2"), utils.i18n("demo.cus3"), utils.i18n("demo.cus4"), utils.i18n("demo.cus5")];
        var colorC31 = ["rgb(165,181,146)","rgb(243,164,71)","rgb(231,188,41)","rgb(208,146,167)","rgb(156,133,192)"];
        var configC31 = genBarConfig(datasC31, labelsC31, colorC31);
        if( isNA(chartMap["C31"]) ){
            $("#divC31").html("<canvas id=\"cvC31\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C31"] = renderChartJS("#cvC31", configC31);
        }else{
            chartMap["C31"].config = configC31;
            chartMap["C31"].update();
        }
    }else if( key==="C32" ){// 3-2 Order Due - Bar 未付款金额-逾期金額
        var datasC32 = [[20000, 13000, 25000, 12000, 11000]];
        var labelsC32 = [utils.i18n("demo.cus1"), utils.i18n("demo.cus2"), utils.i18n("demo.cus3"), utils.i18n("demo.cus4"), utils.i18n("demo.cus5")];
        var colorC32 = ["rgb(165,181,146)","rgb(243,164,71)","rgb(231,188,41)","rgb(208,146,167)","rgb(156,133,192)"];
        var configC32 = genBarConfig(datasC32, labelsC32, colorC32);
        if( isNA(chartMap["C32"]) ){
            $("#divC32").html("<canvas id=\"cvC32\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C32"] = renderChartJS("#cvC32", configC32);
        }else{
            chartMap["C32"].config = configC32;
            chartMap["C32"].update();
        }
    }else if( key==="C41" ){// 4-1 By Customer - Bar 依客戶
        var datasC41 = [
            [20000, 13000, 25000, 12000, 11000],
            [24000, 10000, 23000, 15000, 14000]
        ];
        var labelsC41 = [utils.i18n("demo.cus1"), utils.i18n("demo.cus2"), utils.i18n("demo.cus3"), utils.i18n("demo.cus4"), utils.i18n("demo.cus5")];
        var colorC41 = ["rgb(165,181,146)","rgb(243,164,71)"];
        var configC41 = genBarConfig(datasC41, labelsC41, colorC41);
        if( isNA(chartMap["C41"]) ){
            $("#divC41").html("<canvas id=\"cvC41\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C41"] = renderChartJS("#cvC41", configC41);
        }else{
            chartMap["C41"].config = configC41;
            chartMap["C41"].update();
        }        
    }else if( key==="C42" ){// 4-2 By Market - Line 依市場 - 依销售趋势
        var datasC42 = [
            [20000+13000, 13000+25000, 25000+12000, 12000+11000, 11000+20000, 25000+12000, 14000+24000],
            [20000, 18000, 20000, 12000, 11000, 12000, 14000],
            [13000, 20000, 17000, 11000, 20000, 25000, 24000]
        ];
        var dataLabelsC42 = [utils.i18n("demo.total"), utils.i18n("demo.cus2"), utils.i18n("demo.cus3")];
        var xLabelsC42 = ['2018/09', '2018/10', '2018/11', '2018/12', '2019/01', '2019/02', '2019/03'];
        var colorC42 = ["rgb(165,181,146)","rgb(243,164,71)","rgb(208,146,167)"];
        var specDataIdxs = [0];
        var configC42 = genLineConfig(datasC42, dataLabelsC42, xLabelsC42, colorC42, specDataIdxs);
        if( isNA(chartMap["C42"]) ){
            $("#divC42").html("<canvas id=\"cvC42\" width=\"400\" height=\"250\" ></canvas>");
            chartMap["C42"] = renderChartJS("#cvC42", configC42);
        }else{
            chartMap["C42"].config = configC42;
            chartMap["C42"].update();
        }
    }else if( key==="C51" ){// 5-1 By Product - Bar 依商品
        var datasC51 = [
            [20000, 13000, 25000, 12000],
            [25000, 18000, 22000, 16000]
        ];
        var labelsC51 = [utils.i18n("demo.prd1"), utils.i18n("demo.prd2"), utils.i18n("demo.prd3"), utils.i18n("demo.prd4")];
        var colorC51 = ["rgb(243,164,71)","rgb(208,146,167)"];
        var configC51 = genBarConfig(datasC51, labelsC51, colorC51);
        if( isNA(chartMap["C51"]) ){
            $("#divC51").html("<canvas id=\"cvC51\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C51"] = renderChartJS("#cvC51", configC51);
        }else{
            chartMap["C51"].config = configC51;
            chartMap["C51"].update();
        }
    }else if( key==="C52" ){// 5-2 By Area - Bar 依區域
        var datasC52 = [
            [20000, 25000, 15000],
            [25000, 28000, 20000]
        ];
        var labelsC52 = ["Taiwan", "Turkey", "Nederland"];
        var colorC52 = ["rgb(165,181,146)","rgb(243,164,71)"];
        var configC52 = genBarConfig(datasC52, labelsC52, colorC52);
        if( isNA(chartMap["C52"]) ){
            $("#divC52").html("<canvas id=\"cvC52\" width=\"400\" height=\"250\"></canvas>");
            chartMap["C52"] = renderChartJS("#cvC52", configC52);
        }else{
            chartMap["C52"].config = configC52;
            chartMap["C52"].update();
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for datagrid">
function getColNumByWidth(totalRows){
    var w = utils.getDomWidth();
    for(var i=0; i<GRID_COLS.length; i++){
        if( GRID_COLS[i].max>=w && GRID_COLS[i].min<=w ){
            if( GRID_COLS[i].cols>totalRows ){
                return totalRows;
            }
            return GRID_COLS[i].cols;
        }
    }
}
//</editor-fold>
