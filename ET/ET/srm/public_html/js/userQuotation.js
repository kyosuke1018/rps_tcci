// input : tenderId, memberId

var venderList = [
    {value: 1, label:'南宁市佳信安防工程有限公司'},
    {value: 2, label:'梧州市闽桓建筑工程有限公司'}
];

var tender = {
    id:1,
    name:"台泥廣東區域 2019 年 5 月擴廠工程與設備招標",
    quoteEndDate: "2019-04-40",
    cur:"RMB"
};

var rfqList = [
    {id: 1, ebelp:1, txz01:"乙炔减压器 YQE-213", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:120, netwr:120, brtwr:1200, waers: 'RMB'},
    {id: 2, ebelp:2, txz01:"氧气减压器 YQY-12", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:115, netwr:115, brtwr:1150, waers: 'RMB'},
    {id: 3, ebelp:3, txz01:"乙炔管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
    {id: 4, ebelp:4, txz01:"氧气管 φ8mm 30m/RL", menge:120, meins:"M", lfdat:"2018-02-28", peinh:1000, netpr:4700, netwr:4700, brtwr:564, waers: 'RMB'},
    {id: 5, ebelp:5, txz01:"油石 200X50X25 420#", menge:2, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:25640, netwr:25640, brtwr:51.28, waers: 'RMB'},
    {id: 6, ebelp:6, txz01:"聚西先胺气管 FESTO:PAN 12x1.75  152702", menge:20, meins:"M", lfdat:"2018-02-28", peinh:1, netpr:45, netwr:45, brtwr:900, waers: 'RMB'},
    {id: 7, ebelp:7, txz01:"不锈钢奶头油嘴 M8*1", menge:20, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:1800, netwr:1800, brtwr:36, waers: 'RMB'},
    {id: 8, ebelp:8, txz01:"细白密实羊毛毡 1M*1M*10MM", menge:3, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:80, netwr:80, brtwr:240, waers: 'RMB'},
    {id: 9, ebelp:9, txz01:"射吸式割炬 G01-100 (捷锐牌310C)", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:205, netwr:205, brtwr:2050, waers: 'RMB'},
    {id: 10, ebelp:10, txz01:"气管接头 FESTO:QS-1/2-12 153010", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:21, netwr:21, brtwr:210, waers: 'RMB'},
    {id: 11, ebelp:11, txz01:"滤袋布 2000mm", menge:40, meins:"M2", lfdat:"2018-02-28", peinh:1, netpr:10, netwr:10, brtwr:400, waers: 'RMB'},
    {id: 12, ebelp:12, txz01:"压力表 Y60 0-10MPa", menge:4, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:30, netwr:30, brtwr:120, waers: 'RMB'},
    {id: 13, ebelp:13, txz01:"焊机电缆快速接头 EKJ50-2", menge:10, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:18, netwr:18, brtwr:180, waers: 'RMB'},
    {id: 14, ebelp:14, txz01:"高细白密实羊毛毡 1000X1000X8MM", menge:3, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:80, netwr:80, brtwr:240, waers: 'RMB'},
    {id: 15, ebelp:15, txz01:"预沉室入口膨胀节整修,具体详见附图 图号BT-C19-D110-504标识", menge:1, meins:"LE", lfdat:"2018-03-10", peinh:1, netpr:10160, netwr:10160, brtwr:10160, pstyp:9, waers: 'RMB'},
    {id: 16, ebelp:16, txz01:"割嘴 G01-100 1#(捷锐PG01-100-1)", menge:50, meins:"PC", lfdat:"2018-02-28", peinh:1000, netpr:21500, netwr:21500, brtwr:1075, waers: 'RMB'},
    {id: 17, ebelp:17, txz01:"移动式电缆盘 YDX10A/220V 40m", menge:4, meins:"PC", lfdat:"2018-02-28", peinh:1, netpr:65, netwr:65, brtwr:260, waers: 'RMB'}
];

var srvItemsMap = {
    ebelp15: [
        {extrow:10, ktext1:"预沉室入口膨胀节蒙皮拆除（D=4.3，高0.4）", menge:1, meins:"SET", tbtwr:3000, netwr:3000},
        {extrow:20, ktext1:"预沉室入口膨胀节导流板整修（约5.4平方）", menge:5.4, meins:"M2", tbtwr:400, netwr:2160},
        {extrow:30, ktext1:"预沉室入口膨胀节蒙皮更换修复（D=4.3，高0.4）", menge:1, meins:"SET", tbtwr:5000, netwr:5000}
    ]
};

var quoInfo = {
    statusLabel: '暫存',
    saveDate: '2019-04-40 13:40:33'
};

var curOptions = [
    {label:"RMB", value:"RMB"}
];

var fileInputNum = 3;
var fileList = [
    {id: 1, name:"DOC001.pdf", desc:"規格書1"},
    {id: 2, name:"DOC002.pdf", desc:"規格書2"},
    {id: 3, name:"REF001.pdf", desc:"報價參考資訊"}
];

var taxTypes = [
    {label:"零稅率", value:"Z"},
    {label:"增值稅", value:"A"},
    {label:"普通發票", value:"I"}
];

var taxRates = [
    {label:"3%", value:"3"},
    {label:"5%", value:"5"},
    {label:"6%", value:"6"},
    {label:"10%", value:"10"},
    {label:"11%", value:"11"},
    {label:"13%", value:"13"},
    {label:"16", value:"16"},
    {label:"17%", value:"17"}
];

function initLayout(){
    createDatePicker("#eindt");
}

function fetchData(formData){
    // TODO AJAX
    afterFetchData({}, formData);
}

function afterFetchData(response, formData){
    if( getVenders(response) ){
        if( getRfqItems(response) ){
            if( getSrvItems(response) ){
                renderVenderList();
                renderRfqList();
                renderPayFee(1, 0);
                renderExistedFiles();
                renderFileUpload();

                $('#divStep2').hide();
            }
        }
    }
}

function getVenders(response){
    return !isEmptyAry(venderList);
}

function getRfqItems(response){
    return !isEmptyAry(rfqList);
}

function getSrvItems(response){
    return true;
}

function renderVenderList(){
    var html = genSelectInput("vender", venderList, 1);
    $("#spVenders").html(html);
}

function renderRfqList(){
    var rfqItemHeads = "<tr><th>項次</th><th>品項名稱</th><th>數量</th><th>單位</th><th>計價數量</th><th>單價</th><th>總價</th><th>交貨日</th><th>可提供數量</th></tr>\n";

    var html = "<table class='tbRfqItems'>\n";
    html += rfqItemHeads;
    for(var i=0; i<rfqList.length; i++){
        console.log(rfqList[i]);
        var row = genRfqItemRow(rfqList[i], i);
        html = html + row;
    }
    html += "</table>\n";

    $("#divRfqList").html(html);
}

function renderPayFee(taxType, taxRate){
    var html = "";
    for(var i=0; i<taxTypes.length; i++){
        //html += "<input type='radio' id='rd"+i+"' name='taxType' value='"+taxTypes[i].value+"' /><label for='rd"+i+"' >"+taxTypes[i].label+"</label>";
        html += genRadioInput("taxType", i, taxTypes[i].value, taxTypes[i].label, taxType===taxTypes[i].value);
    }
    $("#spTaxType").html(html);

    html = "";
    for(var i=0; i<taxRates.length; i++){
        //html += "<input type='radio' id='rd"+i+"' name='taxType' value='"+taxTypes[i].value+"' /><label for='rd"+i+"' >"+taxTypes[i].label+"</label>";
        html += genRadioInput("taxRate", i, taxRates[i].value, taxRates[i].label, taxRate===taxRates[i].value);
    }
    $("#spTaxRate").html(html);
}

function renderExistedFiles(){
    var html = "";
    
    for(var i=0; i<fileList.length; i++){
        html += genFileItem(fileList[i], i);
    }
    
    $("#divExistedFiles").html(html);
}

function renderFileUpload(){
    var html = "";
    
    for(var i=0; i<fileInputNum; i++){
        html += genFileInput(i);
    }
    
    $("#divUploadFiles").html(html);
}

function genRfqItemRow(ekpo, index){
    var isSrv = (!isNA(ekpo.pstyp) &&  ekpo.pstyp===9);// 服務類
    
    var html = (index%2===0)?"<tr>":"<tr class='trOdd' >";
    html += "<td class='tar'>"+ ekpo.id +"</td>\n";
    html += "<td>"+ ekpo.txz01 +"</td>\n";
    html += "<td class='tar'>"+ ekpo.menge +"</td>\n";
    html += "<td class='tac'>"+ ekpo.meins +"</td>\n";
    html += "<td class='tar'>"+ ekpo.peinh +"</td>\n";
    html += "<td class='tac'>"+ genTextInput("Netwr"+ekpo.id, 8, ekpo.netwr, isSrv) +"</td>\n";// netpr
    html += "<td class='tac'>"+ genTextInput("Brtwr"+ekpo.id, 10, ekpo.brtwr, true) +"</td>\n";// brtwr
    //html += "<td class='tac'>"+ genSelectInput("Waers"+ekpo.id, curOptions, ekpo.waers) +"</td>\n";
    html += "<td class='tac'>"+ genCalendarInput("Eindt"+ekpo.id, 10, ekpo.eindt, false) +"</td>\n"; 
    html += "<td class='tac'>"+ genTextInput("Menge"+ekpo.id, 5, ekpo.menge) +"</td>\n"; 
    html += "</tr>\n";

    if( isSrv ){
        var srv = genSrvItems(ekpo.ebelp);
        console.log("srv = \n");

        html += (index%2===0)?"<tr>":"<tr class='trOdd' >";
        html += "<td class='tar'></td>\n";
        html += "<td colspan='8' >\n";
        html += srv;
        html += "</td>\n";
        html += "</tr>\n";
    }
    return html;
}

function genSrvItems(ebelp){
    if( isNA(srvItemsMap['ebelp'+ebelp]) ){
        console.error("renderSrvItems empty ebelp"+ebelp);
        return;
    }
    var list = srvItemsMap['ebelp'+ebelp];
    
    // 服務號碼 名稱 數量 單位 單價 總價
    var srvItemHeads = "<tr><th>服務號碼</th><th>名稱</th><th>數量</th><th>單位</th><th>單價</th><th>總價</th></tr>\n";
    
    var html = "<table class='tbSrvItems pt100'>\n";
    html += srvItemHeads;
    for(var i=0; i<list.length; i++){
        console.log(list[i]);
        var row = genSrvItemRow(list[i], i);
        html = html + row;
    }
    html += "</table>\n";

    return html;
}

function genSrvItemRow(pm, index){
    // 服務號碼 名稱 數量 單位 單價 總價
    var html = "<tr>\n";
    html += "<td class='tar'>"+ pm.extrow +"</td>\n";
    html += "<td>"+ pm.ktext1 +"</td>\n";
    html += "<td class='tar'>"+ pm.menge +"</td>\n";
    html += "<td class='tac'>"+ pm.meins +"</td>\n";
    html += "<td class='tac'>"+ genTextInput("Tbtwr"+pm.extrow, 8, pm.tbtwr, false) +"</td>\n";// tbtwr
    html += "<td class='tac'>"+ genTextInput("Netwr"+pm.extrow, 8, pm.netwr, true) +"</td>\n";// netwr
    html += "</tr>\n";
    return html;
}

function genTextInput(key, size, def, isReadOnly){
    var ro = isReadOnly?"readonly":"";
    var val = isNA(def)?"":def;
    var html = "<input type='text' id='txt'"+key+"' size='"+size+"' value='"+val+"' "+ro+" />";
    return html;
}

function genCalendarInput(key, size, def, isReadOnly){
    var ro = isReadOnly?"readonly":"";
    var val = isNA(def)?"":def;
    var html = "<input type='text' data-field='date' id='txt'"+key+"' size='"+size+"' value='"+val+"' "+ro+" />";
    return html;
}

function genSelectInput(key, opList, def){
    var html = "<select id='sel'"+key+"' >";
    for(var i=0; i<opList.length; i++){
        var sel = (def===opList[i].value)? " selected ":"";
        html += "<option value='"+opList[i].value+"' "+sel+">"+opList[i].label+"</option>\n";
    }
    html += "</select>\n";
    return html;
}

function genRadioInput(mame, index, value, label, isChecked){
    var checked = isChecked?" checked ":"";
    var id = mame+index;
    var html = "<input type='radio' id='"+id+"' name='"+mame+"' value='"+value+"' "+checked+" /><label for='"+id+"' >"+label+"</label>&nbsp;";
    return html;
}

function genFileItem(fs, index){
    var html = "<span class='spFile' >";
    html += fs.name;
    html += "(" + fs.desc + ")";
    html += "<a href='#' id='doDelFile("+index+")' ><img class='imgDel' src='img/delete.png' /></a>";
    html += "</span>&nbsp;";
    
    return html;
}

function genFileInput(key){
    var html = "<div id='fs'"+key+"' >\n";
    html += "<span><input id='itFileDesc'"+key+" type='text' size='20' placeholder='輸入上傳檔案說明' /></span>\n";
    html += "<span class='fieldname' ><input type='file' id='files' name='files' /></span>\n";
    html += "</div>\n";
    
    return html;
}

// 棄標
function doDiscard(){
}

function doNext(){
    calQuoteTotal();
    doSaveQuotation();
    
    $('#divStep2').show();
    scrollTo("#divStep2", -10);
}

function calQuoteTotal(){
    // TODO
    var total = "134,535.28";
    $("#spTotal").html(total);
    
    var netTotal = "134,535.28";
    $("#spNetTotal").html(netTotal);
}

function doDelFile(id){
    if( confirm("確定要刪除此檔案嗎?") ){
        // TODO
    }
}

function addFile(){
    fileInputNum++;
    $("#divUploadFiles").append(genFileInput(fileInputNum));
}

function doUpload(){
    
}

function doSaveQuotation(){
    // TODO 
}

function doSendQuotation(){
    if( confirm("您確定要送出投標資訊嗎?") ){
        // TODO
    }
}
