/* 
 * dependencys : jQuery, consts.js, tcc-utils.js, com/menu.js, layout.js
 */
/* global ajaxFlags, _asyncCompleteFlags, PRD_TYPE_URL, PN_PRD_TYPE, PRD_TYPE_ATTR_URL, validator, PRD_TYPE_SAVE_URL, PRD_TYPE_DEL_URL, PRD_TYPE_ATTR_DEL_URL, PRD_TYPE_ATTR_SAVE_URL, FIELD_UUID, FIELD_MODIFIED, w3, utils.genNoSelectOpNum(), FIELD_ADMIN, utils, _adminUser */
var vo = {// variable objects the JS
    typeTreeNodes: null,
    itemSelected: null,
    attrs: null,// 商品規格屬性
    prdAttrFL: null // FormListM object for product type attributes
};

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
    initLayout(_adminUser, true);
    renderPanels();
    
    // Ajax 產出UI相關
    buildPrdTypeTree("tree");

    // 其他UI操作元件 (Buttons, ...)
    buildOperationComs(_adminUser);
    
    // 預設顯示、隱藏
    $("#selectedItemHeader").hide();
    $("#selectedItemContent").hide();

    console.log(utils.formatDT(new Date()));
}
function renderPanels(){
    $('#fsTree').puifieldset({toggleable: true});
    $('#fsEdit').puifieldset({toggleable: true});
    $('#fsAttr').puifieldset({toggleable: true});
}

// 可是先建立的操作元件
function buildOperationComs(adminUser){
    console.log("buildOperationComs adminUser = ", adminUser);
    if( adminUser ){
        createBtn('#add', 'fa-plus-square', onClickAdd);// 新增
        createBtn('#addSub', 'fa-plus-circle', onClickAddSub);// 新增子項目
        createBtn('#delete', 'fa-trash', onClickDelete);// 刪除
        createBtn('#save', 'fa-save', onClickSave);// 儲存
        createBtn('#cancel', 'fa-ban', onClickCencalEdit);// 取消
        // 依分類設定產品規格屬性
        createBtn('#attr', 'fa-list', onClickAttr);// 依分類設定產品規格屬性
        
        $("#noteArea").hide();// 賣家後台註解
    }else{
        $("#addArea").hide();// for 新增
    }
}

//<editor-fold defaultstate="collapsed" desc="for Product Type tree">
// 建立商品分類樹狀列表
function buildPrdTypeTree(ajaxKey){
    _asyncCompleteFlags[ajaxKey]=false;
    var opLang = utils.getOptionLabelLang();
    var restUrl = PRD_TYPE_URL + "?lang="+opLang;
    utils.fetchData(restUrl, false, renderPrdTypeTree, null, ajaxKey);
}

function rerenderTree(){
    // rerender treetable
    $('#treeContainer').html("<div id='treePrdType' ></div>");
    buildPrdTypeTree("tree");
}
    
/**
 * 產生商品分類樹狀列表
 * @param {type} data
 * @param {type} ajaxKey
 * @returns {undefined}
 */
function renderPrdTypeTree(data, ajaxKey){
    vo.typeTreeNodes = (!isNA(data) && !isNA(data.nodes))? utils.cloneObj(data.nodes):[];   
    //expandAllNode();// for 全部展開

    var cols = utils.isSmallScreen()?[
            {field:'cname', headerText:utils.i18n("fd.type.cname")},
            {field:'prdNum', headerText:utils.i18n("fd.prdNum"), bodyClass:'tar maxw80', content:genPrdNumber, bodyStyle:'max-width:80px;'}
        ]:[
            {field:'cname', headerText:utils.i18n("fd.type.cname")},
            {field:'levelnum', headerText:utils.i18n("fd.levelnum"), bodyClass:'tac'},
            {field:'parentName', headerText:utils.i18n("fd.parentName")},
            {field:'prdNum', headerText:utils.i18n("fd.prdNum"), bodyClass:'tar', content:genPrdNumber}//,
            //{field:'attrNum', headerText:utils.i18n("fd.attrNum"), bodyClass:'tar', content:genAttrNumber}
        ];

    $('#treePrdType').puitreetable({
        selectionMode: 'single',
        nodes: vo.typeTreeNodes,
        columns: cols,
        // events (nodeSelect、nodeUnselect、afterExpand、afterCollapse)
        afterExpand: function(event, ui){
            console.log("afterExpand ...", event, ui);
        },
        afterCollapse: function(event, ui){
        },
        nodeSelect: function(event, ui) {
            if( _adminUser ){
                onSelectNode(event, ui);
            }
        }
    });
    
    //$('#treePrdType').puitreetable('expandNode', );
    _asyncCompleteFlags[ajaxKey]=true;
}

function genPrdNumber(data){
    return  data.prdNum; //
}
function genAttrNumber(data){
    return  data.levelnum<3? "":data.attrNum;
}

function onSelectNode(event, ui){
    //console.log("onSelectNode ui = ", ui);
    if( isNA(ui.data) || isNA(ui.data.id) ){
        return;
    }
    console.log("onSelectNode ui = ", ui);
    $('#treePrdType').puitreetable('expandNode', ui.node);// 展開

    var item = ui.data;
    vo.itemSelected = utils.cloneObj(item);
    
    if( _adminUser ){
        $("#selectedItemHeader").show();
        // 選取項目
        genSelectedItemTitle(item.cname);

        prepareEdit(item);
        utils.scrollTo('#selectedItemHeader');
    }else{
        $("#fsEdit").hide();
        if( vo.itemSelected.levelnum>=3 ){
            if( vo.itemSelected.attrNum>0 ){
                $("#selectedItemContent").show();
                onClickAttr(null);
            }
        }
    }
}

// for 展開選取項目
function expandSelectedNode(){
    // 第1層固定展開
    if( !utils.isEmptyAry(vo.typeTreeNodes) ){
        for(var i=0; i<vo.typeTreeNodes.length; i++){
            console.log("expanded ... ", vo.typeTreeNodes[i]);
            vo.typeTreeNodes[i]['expanded'] = true;
        }
    }else{
        return;
    }
    
    if( vo.itemSelected!==null ){
        console.log("expandSelectedNode itemSelected = ", vo.itemSelected);
        for(var i=0; i<vo.typeTreeNodes.length; i++){
            if( vo.typeTreeNodes[i].children ){
                for(var j=0; j<vo.typeTreeNodes[i].children.length; j++){
                    if( vo.typeTreeNodes[i].children[j].data.id===vo.itemSelected.id ){
                        // extend parent node
                        vo.typeTreeNodes[i]['expanded'] = true;
                        console.log("itemSelected found 2 ...", vo.typeTreeNodes[i].children[j]);
                        return;
                    }
                    if( vo.typeTreeNodes[i].children[j].children ){
                        for(var k=0; k<vo.typeTreeNodes[i].children[j].children.length; k++){
                            if( vo.typeTreeNodes[i].children[j].children[k].data.id===vo.itemSelected.id ){
                                // extend parent node
                                vo.typeTreeNodes[i]['expanded'] = true;
                                vo.typeTreeNodes[i].children[j]['expanded'] = true;
                                console.log("itemSelected found 3 ...", vo.typeTreeNodes[i].children[j].children[k]);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}

// 全部展開
function expandAllNode(){
    if( !utils.isEmptyAry(vo.typeTreeNodes) ){
        for(var i=0; i<vo.typeTreeNodes.length; i++){
            console.log("expanded ... ", vo.typeTreeNodes[i]);
            vo.typeTreeNodes[i]['expanded'] = true;// level 1
            if( vo.typeTreeNodes[i].children ){
                for(var j=0; j<vo.typeTreeNodes[i].children.length; j++){
                    vo.typeTreeNodes[i].children[j]['expanded'] = true;// level 2
                }
            }
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Type Add, Edit, Delete">
// 儲存分類
function onClickSave(event){
    var formSelector = "#fmEdit";
    var $form = $(formSelector);
    var formData = $form.serializeFormJSON();
    console.log("onClickSave formData = ", formData);
    if( !valiateInput(formData) ){
        return;
    }
    utils.postData(PRD_TYPE_SAVE_URL, formData, false, afterSavePrdType, null, null);
}

function afterSavePrdType(response, formData, optional){
    console.log("afterSavePrdType ...", response);
    if( !utils.checkResponse(response) ){
        return;
    }
    // return ID
    setTextValue("#id", response.res.id);
    vo.itemSelected["id"] = response.res.id;
    // rerender treetable
    rerenderTree();
    // show msg
    utils.addSuccessMsg();
}

// 刪除分類
function onClickDelete(event){
    if( confirm(utils.i18n("remove.tree.confirm")) ){
        var formData = {};
        formData["id"] = $("#id").val();
        
        utils.postData(PRD_TYPE_DEL_URL, formData, false, afterRemovePrdType, null, null);
    }
}

function afterRemovePrdType(response, formData, optional){
    if( utils.isSuccess(response) ){
        // rerender treetable
        rerenderTree();
        // init status
        vo.itemSelected = null;
        $("#id").val("");
        $("#selectedItemHeader").hide();
        $("#selectedItemContent").hide();
        // show msg
        utils.addSuccessMsg();
    }
}

// 新增子分類
function onClickAddSub(event){
    prepareAddSub();
}
function prepareAddSub(){
    var item = {
        "id": null, 
        "cname": "", 
        "levelnum": vo.itemSelected.levelnum+1,
        "parent": vo.itemSelected.id,
        "parentName": null, 
        "sortnum": 1, 
        "parentL1": vo.itemSelected.parent
    };
    
    prepareEdit(item);
}

// 新增分類
function onClickAdd(event){
    prepareAdd();
    $("#selectedItemHeader").hide();
}
function prepareAdd(){
    vo.itemSelected = {
        "id": null, 
        "cname": "", 
        "levelnum": 1,
        "parent": null,
        "parentName": null, 
        "sortnum": 1, 
        "parentL1": null
    };
    // 編輯區 上層類別選單
    buildTypeSelectForEdit();

    // 初始值
    setDropdownValue('#typeL1', 0);
    setDropdownValue('#typeL2', 0);
    setTextValue("#id", 0);
    setTextValue("#levelnum", vo.itemSelected.levelnum);
    setTextValue("#cname", vo.itemSelected.cname);
    setTextValue("#sortnum", vo.itemSelected.sortnum);

    disableBtn("#addSub");
    disableBtn("#attr");
    $("#delete").hide();

    $("#selectedItemContent").show();
    $("#fsAttr").hide();
}

// 新增類別時，建立編輯區 上層類別選單
function buildTypeSelectForEdit(){
    var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
    var ops = null;
    if( !isNA(parentTypesL1) ){
        ops = utils.convertTreeNodesToOps(parentTypesL1);
    }
    createDropdown("#typeL1", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1);// 第一層
    enableDropdown('#typeL1');
    createDropdown("#typeL2", null, utils.genNoSelectOpNum(), onChangePrdTypeL2);// 第二層
    disableDropdown('#typeL2');
}

// 變更第一層選單
function onChangePrdTypeL1(item){
    //console.log("onChangePrdTypeL1 item = " ,item);
    if( item.value===0 ){
        vo.itemSelected.parent = null;
        vo.itemSelected.levelnum = 1;
    }else{
        vo.itemSelected.parent = item.value;
        vo.itemSelected.levelnum = 2;
    }
    
    // 第二層選單
    var parentTypesL2 = utils.findChildrenFromTree(vo.typeTreeNodes, item.value);
    var ops = null;
    if( !isNA(parentTypesL2) ){
        ops = utils.convertTreeNodesToOps(parentTypesL2);
    }
    createDropdown("#typeL2", ops, utils.genNoSelectOpNum(), onChangePrdTypeL2);
    setDropdownValue('#typeL2', 0);
    enableDropdown('#typeL2');
    setTextValue("#levelnum", vo.itemSelected.levelnum);
}

// 變更第二層選單
function onChangePrdTypeL2(item){
    //console.log("onChangePrdTypeL2 item = " ,item);
    if( item.value===0 ){
        vo.itemSelected.parent = getDropdownValue('#typeL1');
        vo.itemSelected.parentL1 = null;
        vo.itemSelected.levelnum = 2;
    }else{
        vo.itemSelected.parent = item.value;
        vo.itemSelected.parentL1 = Number(getDropdownValue('#typeL1'));
        vo.itemSelected.levelnum = 3;
    }
    
    setTextValue("#levelnum", vo.itemSelected.levelnum);
}

// 編輯
function prepareEdit(item){
    if( isNA(item) ){
        console.assert("prepareEdit error item = ", item);
        return;
    }

    $('#fmEdit')[0].reset();
    
    // 編輯區 
    if( item.levelnum===1 ){
        createDropdown("#typeL1", null, utils.genNoSelectOpNum(), onChangePrdTypeL1);// 第一層
        disableDropdown('#typeL1');
        createDropdown("#typeL2", null, utils.genNoSelectOpNum(), onChangePrdTypeL2);// 第二層
        disableDropdown('#typeL2');
    }else if( item.levelnum===2 ){
        var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
        var ops = null;
        if( !isNA(parentTypesL1) ){
            ops = utils.convertTreeNodesToOps(parentTypesL1);
        }
        createDropdown("#typeL1", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1);
        $('#typeL1').puidropdown('selectValue', item.parent);// 第一層
        setDropdownValue('#typeL1', item.parent);
        enableDropdown('#typeL1');

        createDropdown("#typeL2", null, utils.genNoSelectOpNum(), onChangePrdTypeL2);// 第二層
        disableDropdown('#typeL2');
    }else if( item.levelnum===3 ){
        var parentTypesL1 = utils.findChildrenFromTree(vo.typeTreeNodes, null);
        var ops = null;
        if( !isNA(parentTypesL1) ){
            ops = utils.convertTreeNodesToOps(parentTypesL1);
        }
        createDropdown("#typeL1", ops, utils.genNoSelectOpNum(), onChangePrdTypeL1);
        setDropdownValue('#typeL1', item.parentL1);// 第一層
        enableDropdown('#typeL1');

        var parentTypesL2 = utils.findChildrenFromTree(vo.typeTreeNodes, item.parentL1);
        var opsL2 = null;
        if( !isNA(parentTypesL2) ){
            opsL2 = utils.convertTreeNodesToOps(parentTypesL2);
        }
        createDropdown("#typeL2", opsL2, utils.genNoSelectOpNum(), onChangePrdTypeL2);
        setDropdownValue('#typeL2', item.parent);// 第二層
        enableDropdown('#typeL2');
    }

    // 初始值
    setTextValue("#id", item.id);
    setTextValue("#levelnum", item.levelnum);
    setTextValue("#cname", item.cname);
    setTextValue("#sortnum", item.sortnum);

    // 新增子分類
    $('#addSub').puibutton(item.levelnum<3? "enable":"disable");
    // 分類規格屬性
    if( isNA(item.id) || item.levelnum<3 ){
        disableBtn("#attr");
    }else{
        enableBtn("#attr");
    }
    $("#delete").show();
    
    $("#selectedItemContent").show();
    $("#fsAttr").hide();

    utils.scrollTo('#fsEdit');
}

// 取消編輯
function onClickCencalEdit(){
    $("#selectedItemHeader").hide();
    $("#selectedItemContent").hide();
    $('#fmEdit')[0].reset();

    vo.itemSelected = {};
}

// 輸入檢查
function valiateInput(item){
    // input text 輸入驗證 (搭配 title & required 屬性)
    var msg = utils.validateInputRequired("#fmEdit");
    if( !isEmpty(msg) ){
        utils.addMessage("error", utils.i18n("in.err"), msg);
        return false;
    }
    // 其他輸入驗證
    if( (item.levelnum==="3" && (isNA(item.typeL1) || item.typeL1==="0" || isNA(item.typeL2) || item.typeL2==="0"))
     || (item.levelnum==="2" && (isNA(item.typeL1) || item.typeL1==="0"))
    ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("input.err.parent"));
        return false;
    }

    return true;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="for Product Type Attributes">
// 依分類設定產品規格屬性
function onClickAttr(event){
    var restUrl = PRD_TYPE_ATTR_URL.replace("{typeId}", vo.itemSelected.id);
    utils.fetchData(restUrl, false, initPrdAttrs, null, null);
}

// 初始設定產品規格屬性
function initPrdAttrs(response){
    vo.attrs = utils.getResponseList(response);

    if( !_adminUser ){
        $("#editArea").hide();
        $("#saveArea").hide();
        renderAttrListView("#existedAttrs", vo.attrs);
    }else{
        vo.prdAttrFL = new FormListM("prdAttrFL", // this object name
                        '#editAttrForm', '#existedAttrs', '#confirmAttr', '#saveAttr', '#cancelAttr', // selector
                        vo.attrs, // data list
                        PRD_TYPE_ATTR_URL.replace("{typeId}", vo.itemSelected.id), 
                        PRD_TYPE_ATTR_SAVE_URL.replace("{typeId}", vo.itemSelected.id), 
                        PRD_TYPE_ATTR_DEL_URL.replace("{typeId}", vo.itemSelected.id), // rest url
                        renderAttrList, // call back function
                        null, onEditAttrCB, afterSaveAttrCB, afterrDelAttrCB, // call back function
                        true, true,
                        getEditAttrFormData, getSaveAttrFormData, // get form input data function
                        valiateEditAttr, valiateSaveAttr // validate form function
                    );
        vo.prdAttrFL.init();
    }
    $("#fsAttr").show();
    utils.scrollTo("#fsAttr");
}

// 產生商品規格屬性維護列表
function renderAttrList(nameFL, selector, values){                      
    var html = '<table class="w3-table-all">'
                + '<thead>'
                + '  <tr class="ui-widget-header ui-state-default">'
                + '    <th width="260">'+utils.i18n("operation")+'</th><th>'+utils.i18n("cname")+'</th>'
                + '  </tr>'
                + '</thead>';
                + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html += '<tr class="w3-padding-small w3-hover-pale-blue">'
                    + '    <td class="fit-content">'
                    + '     <button type="button" onclick="'+nameFL+'.onClickEdit('+ i +')" title="'+utils.i18n("edit")+'" class="w3-btn w3-teal" ><i class="fa fa-pencil-square"></i></button>'
                    + '     <button type="button" onclick="'+nameFL+'.onClickDelete('+ i +')" title="'+utils.i18n("remove")+'" class="w3-btn w3-deep-orange" ><i class="fa fa-trash"></i></button>'
                    + '     <button type="button" onclick="'+nameFL+'.onClickUp('+ i +')" title="'+utils.i18n("move.up")+'" class="w3-btn w3-khaki" ><i class="fa fa-arrow-up"></i></button>'
                    + '     <button type="button" onclick="'+nameFL+'.onClickDown('+ i +')" title="'+utils.i18n("move.down")+'" class="w3-btn w3-green" ><i class="fa fa-arrow-down"></i></button>'
                    + '    </td>'
                    + '    <td>' + utils.safePrint(values[i].cname) + '</td>'
                    + '  </tr>';
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function renderAttrListView(selector, values){
    var html = '<table class="w3-table-all">'
                + '<thead>'
                + '  <tr class="ui-widget-header ui-state-default">'
                + '    <th>'+utils.i18n("cname")+'</th>'
                + '  </tr>'
                + '</thead>';
                + '<tbody>';
    if( !isNA(values) ){
        for(var i=0; i<values.length; i++){
            if( isNA(values[i].disabled) || !values[i].disabled ){// filter disabled
                html +=  '<tr class="w3-padding-small w3-hover-pale-blue">'
                        + '    <td>' + utils.safePrint(values[i].cname) + '</td>'
                        + '  </tr>';
            }
        }
    }
    html += '</tbody>';
    html += "</table>";
    $(selector).html(html);
}

function onEditAttrCB(row){
    setTextValue("#attrName", row.cname);
}

function getEditAttrFormData(formData){
    return formData;
}

// 驗證商品規格屬性
function valiateEditAttr(formData){
    if( isEmpty(formData.cname) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("input.err.prdattr"));
        return false;
    }
    if( !utils.checkUnique(vo.prdAttrFL.list, formData, FIELD_UUID, "cname", true) ){
        utils.addMessage("error", utils.i18n("in.err"), utils.i18n("name.norepeat"));
        return false;
    }
    
    return true;
}

function getSaveAttrFormData(list){
    var formData = {};
    formData["typeId"] = (isNA(vo.itemSelected) || isNA(vo.itemSelected.id))?null:vo.itemSelected.id;
    formData["attrs"] = utils.safeList(list);
    return formData;
}

function valiateSaveAttr(formData){
    if( isNA(formData["typeId"]) ){
        _alert(utils.i18n("nosel.prdtype"));
        return false;
    }
    if( utils.isEmptyAry(formData["attrs"]) ){
        // do nothiing
        return false;
    }
    return true;
}

function afterSaveAttrCB(){
    console.log("afterSaveAttrCB ....");
    // rerender treetable
    rerenderTree();
    utils.addSuccessMsg();
}

function afterrDelAttrCB(){
    // rerender treetable
    rerenderTree();
}
//</editor-fold>
