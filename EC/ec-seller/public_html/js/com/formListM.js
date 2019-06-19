/* global FIELD_UUID, FIELD_MODIFIED, utils */
// Form & List for all rows save together
// 單筆刪除、整批儲存(新增、修改)，以 UUID 做 KEY
function FormListM(nameFL, // this object name
        formSelector, listSelector, btnConfirm, btnSave, btnCancel, // selector
        list, // data list
        listRestUrl, saveRestUrl, delRestUrl, // rest url
        renderList, // call back function
        initFormCB, onEditRowCB, afterSaveCB, afterDeleteCB, // call back function
        useIndexParam, hasSortnum,
        getEditFormData, getSaveFormData, // get form input data function
        valiateEdit, valiateSave // validate form function
){
    var self = this;
    this.list = list;
    this.editRowKey = null;// 整批儲存須以 UUID 做 KEY // 編輯中
    
    this.init = function(){
        utils.initListUUID(self.list);
        renderList(nameFL, listSelector, self.list);//, nameFL+".onEditRow", nameFL+".onDeleteRow");
        
        if( !isNA(formSelector) ){
            createBtn(btnConfirm, 'fa-check-circle-o', self.onClickConfirm);// 確認
            self.prepareAdd();// 新增模式
        }
        createBtn(btnSave, 'fa-save', self.onClickSave);// 儲存
        createBtn(btnCancel, 'fa-ban', self.onClickCancel);// 取消
    };

    // 回新增模式
    this.prepareAdd = function(){
        if( !isNA(formSelector) ){
            self.editRowKey = null;
            $(formSelector)[0].reset();

            if( !isNA(initFormCB) ){
                initFormCB();// for Form 的特殊預設
            }
        }
    };

    // 編輯
    this.onClickEdit = function(param){
        var row = useIndexParam?self.list[param]:param;
        console.log("onEditRow row =", row);
        self.editRowKey = row[FIELD_UUID];

        if( !isNA(onEditRowCB) ){
            onEditRowCB(row);// for Form 的特殊預設
        }
    };

    // 確認新增/編輯
    this.onClickConfirm = function(){
        var $form = $(formSelector);
        var formData = $form.serializeFormJSON();
        formData = getEditFormData(formData);

        if( !valiateEdit(formData) ){
            console.log("valiateEdit error ", nameFL, formData);
            return;
        }
        self.list = utils.safeList(self.list);
        
        if( self.editRowKey===null ){// 新增
            formData[FIELD_UUID] = utils.genUUID();
            formData[FIELD_MODIFIED] = true;
            self.list.push(formData);
        }else{
            var idx = utils.findIndexByUUID(self.list, self.editRowKey);
            if( idx>=0 ){
                utils.copyProps(self.list[idx], formData);
                self.list[idx][FIELD_MODIFIED] = true;
            }
        }
        
        if( hasSortnum ){// 有排序需求
            utils.resetSortnum(self.list);
        }
        renderList(nameFL, listSelector, self.list);
        self.prepareAdd();// 回到新增模式
        //$("#attrScopeT").click();// 預設
    };

    // 刪除
    this.onClickDelete = function(param){
        var row = useIndexParam?self.list[param]:param;
        var idx = useIndexParam?param:utils.findIndexByUUID(self.list, param[FIELD_UUID]);
        console.log("onDeleteRow ... ", idx, row);
        
        if( confirm(utils.i18n("remove.item.confirm")) ){
            if( self.editRowKey===row[FIELD_UUID] ){// 編輯中項目
                self.prepareAdd();// 回到新增模式
            }

            var id = row["id"];
            if( !isNA(id) && id>0 ){// 已進DB
                var formData = {"id": id};
                utils.postData(delRestUrl, formData, false, self.afterDelete, null, idx);
            }else{
                self.list.splice(idx, 1);
                renderList(nameFL, listSelector, self.list);
                utils.addSuccessMsg();
            }
        }
    };
    
    // 刪除後處理
    this.afterDelete = function(response, formData, idx){
        if( utils.checkResponse(response) ){
            self.list.splice(idx, 1);
            renderList(nameFL, listSelector, self.list);
            utils.addSuccessMsg();
            
            if( !isNA(afterDeleteCB) ){
                afterDeleteCB();
            }
        }
    };

    // 上移
    this.onClickUp = function(param){
        var idx = useIndexParam?param:utils.findIndexByUUID(self.list, param[FIELD_UUID]);
        if( idx<=0 ){
            return;
        }
        var item = self.list[idx];// 預存
        self.list.splice(idx, 1);
        self.list.splice(idx-1, 0, item);
        utils.resetSortnum(self.list);
        renderList(nameFL, listSelector, self.list);
    };

    // 下移
    this.onClickDown = function(param){
        var idx = useIndexParam?param:utils.findIndexByUUID(self.list, param[FIELD_UUID]);
        if( idx >= self.list.length-1 ){
            return;
        }
        var item = self.list[idx];// 預存
        self.list.splice(idx, 1);
        self.list.splice(idx+1, 0, item);
        utils.resetSortnum(self.list);
        renderList(nameFL, listSelector, self.list);
    };

    // 儲存
    this.onClickSave = function(){
        if( !self.checkChange() ){
            utils.addMessage("info", utils.i18n("hit.msg"), utils.i18n("no.input"));
            return;
        }
        var formData = getSaveFormData(self.list);
        if( !valiateSave(formData) ){
            return;
        }
        utils.postData(saveRestUrl, formData, false, self.afterSave, null, null);
    };
    
    this.checkChange = function(){
        if( !isNA(self.list) && !isNA(self.list.length) ){
            for(var i=0; i<self.list.length; i++){
                if( self.list[i][FIELD_MODIFIED] ){
                    return true;
                }
            }
        }
        return false;// 無異動
    };
    
    this.afterSave = function(response, formData){
        if( utils.checkResponse(response) ){
            self.list = utils.getResponseList(response);
            utils.initListUUID(self.list);
            renderList(nameFL, listSelector, self.list);
            self.prepareAdd();// 回到新增模式
            
            if( !isNA(afterSaveCB) ){
                afterSaveCB(response);
            }
        }
    };
    
    // 取消
    this.onClickCancel = function(){
        if( confirm("確定取消未儲存的新增/編輯嗎?") ){
            utils.fetchData(listRestUrl, false, self.afterCancel, null, null);
        }
    };
    
    this.afterCancel = function(response, formData){
        if( utils.checkResponse(response) ){
            self.list = utils.getResponseList(response);
            utils.initListUUID(self.list);
            renderList(nameFL, listSelector, self.list);
            self.prepareAdd();// 回到新增模式
        }
    };
}
