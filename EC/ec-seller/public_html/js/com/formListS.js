// Form & List for single row save
// 單筆儲存、刪除，直接以 ID 做 KEY
function FormListS(nameFL, // this object name
        formSelector, listSelector, btnSave, btnClear, // selector
        list, // data list
        saveRestUrl, delRestUrl, // rest url
        renderList, // call back function
        onEditRowCB, afterSaveCB, useIndexParam, // call back function
        getFormData, // get form input data function
        valiateSave, // validate form function
        confirmSaveMsg, // confirm message before save
        confirmClearMsg // confirm message before clear
){
    var self = this;
    
    this.list = list;
    this.editRowKey = null;// 單筆儲存可直接以 ID 做 KEY
    
    this.init = function(){
        createBtn(btnSave, 'fa-save', self.onClickSave);// 儲存型別設定
        createBtn(btnClear, 'fa-ban', self.onClickClear);// 清除型別維護

        renderList(nameFL, listSelector, self.list);//, nameFL+".onEditRow", nameFL+".onDeleteRow");
    };
    
    this.onEditRow = function(param){
        var row = useIndexParam?self.list[param]:param;
        self.editRowKey = row.id;
        onEditRowCB(row);
    };

    this.onDeleteRow = function(param){
        if( confirm(utils.i18n("remove.item.confirm")) ){
            var formData = {}; //{"id":self.list[idx].id};
            formData["id"] = (useIndexParam)?self.list[param].id:param.id;
            utils.postData(delRestUrl, formData, false, self.afterSave, null, null);
        }
    };

    this.onClickClear = function(event){
        if( !isNA(formSelector) ){
            if( !isNA(confirmClearMsg) ){
                if( !confirm(confirmClearMsg) ){
                    return;
                }
            }
            $(formSelector)[0].reset();
            self.editRowKey = null;
        }
    };

    // 儲存型別修改
    this.onClickSave = function(event){
        if( !isNA(confirmSaveMsg) ){
            if( !confirm(confirmSaveMsg) ){
                return;
            }
        }
        var $form = $(formSelector);
        var formData = $form.serializeFormJSON();
        if( self.editRowKey!==null ){// 修改
            formData['id'] = self.editRowKey;
        }
        formData = getFormData(formData);//
        console.log("onClickSave formData = ", formData);
        if( !valiateSave(formData) ){
            console.log("valiateSave error ", nameFL, formData);
            return;
        }

        var restUrl = saveRestUrl;
        utils.postData(restUrl, formData, false, self.afterSave, null, null);
    };

    this.afterSave = function(response){
        if( !utils.checkResponse(response) ){
            return;
        }
        self.list = utils.getResponseList(response);
        if( !isNA(formSelector) ){
            $(formSelector)[0].reset();
        }
        self.editRowKey = null;
        
        //renderList(nameFL, listSelector, self.list);//, nameFL+".onEditRow", nameFL+".onDeleteRow");
        self.reload();
        
        afterSaveCB(response);
    };
    
    this.reload = function(){
        renderList(nameFL, listSelector, self.list);
    };
}