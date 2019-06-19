/*
 *  Simple PickList Object 
 */
function SimplePickList(showArea, editArea, 
    btEditSelector, btSaveSelector, btCloseSelector, 
    picklistSelector, 
    sourceList, targetList,
    renderListMethod,
    onClickEditCallBack,
    onClickSaveCallBack,
    onClickCloseCallBack
){
    var self = this;
    
    this.targetList =targetList;
    //this.events =events;
    
    this.inti = function(){
        createBtn(btEditSelector, 'fa-edit', self.onClickEditEvent);// 編輯商品活動
        createBtn(btSaveSelector, 'fa-save', self.onClickSaveEvent);// 儲存商品活動
        createBtn(btCloseSelector, 'fa-ban', self.onClickCloseEvent);// 關閉/取消商品活動編輯
        
        renderListMethod(showArea, targetList);
        $(editArea).hide();// 預設隱藏編輯區
    };
    
    // 商品活動 PickList
    this.renderEventPickList = function(){
        //console.log("renderEventPickList sourceList = ", sourceList);
        //console.log("renderEventPickList targetList = ", targetList);
        var srcAry = [];
        for(var i=0; i<sourceList.length; i++){
            var existed = false;
            for(var j=0; j<self.targetList.length; j++){
                if( self.targetList[j].value===sourceList[i].value ){
                    existed = true;
                    break;
                }
            }
            if( !existed ){
                srcAry.push(sourceList[i]);
            }
        }
    
        var desAry = self.targetList;

        $(picklistSelector).puipicklist({
            //showSourceControls: true,
            //showTargetControls: true,
            sourceCaption: '未選取',
            targetCaption: '已選取',
            //filter: true,
            sourceData: srcAry,
            targetData: desAry,
            content: function(op) {
                return '<span>● ' +op.label+ '</span>';
            },
            responsive: true,
            transfer: function(event, ui) {
                //ui.items: Transferred items. 
                //ui.from: Old list.
                //ui.to: New list.
                //ui.type: Type of transfer e.g. "dragdrop","button" and "command"
            }
        });
    };

    this.onClickEditEvent = function(event){
        self.renderEventPickList();
        $(showArea).hide();
        $(editArea).show();
        
        if( !isNA(onClickEditCallBack) ){
            onClickEditCallBack(self.targetList);
        }
    };
    
    // 儲存商品活動
    this.onClickSaveEvent = function(event){
        var targetData = $.map($(picklistSelector + ' select[name=target] option'), function (v) {
             return v.value; // maps the values and returns them in an array ["1", "2"]
        });
        
        targetData = isNA(targetData)? []:targetData;
        
        self.targetList = [];
        for(var i=0; i<sourceList.length; i++){
            var existed = false;
            for(var j=0; j<targetData.length; j++){
                //console.log(targetData[j], sourceList[i].value);
                if( targetData[j]===sourceList[i].value.toString() ){
                    existed = true;
                    break;
                }
            }
            if( existed ){
                self.targetList.push(sourceList[i]);
            }
        }
        
        console.log("onClickSaveEvent targetList = ", self.targetList);
        renderListMethod(showArea, self.targetList);
        $(showArea).show();
        $(editArea).hide();
        
        if( !isNA(onClickSaveCallBack) ){
            onClickSaveCallBack(self.targetList);
        }
    };
    
    // 關閉編輯商品活動
    this.onClickCloseEvent = function(event){
        $(showArea).show();
        $(editArea).hide();
        
        if( !isNA(onClickCloseCallBack) ){
            onClickCloseCallBack(self.targetList);
        }
    };
}

