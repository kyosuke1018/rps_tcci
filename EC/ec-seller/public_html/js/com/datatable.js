/* global _asyncCompleteFlags, DATATABLE_RWD_WIDTH, PAGE_SIZE_DEF, utils */
function clearDataTable(selector, colSettings, minWidth, marginWidth, ajaxKey){
    renderDataTable(selector, null, colSettings, [], null, minWidth, marginWidth);
    if( !isNA(ajaxKey) ){
        _asyncCompleteFlags[ajaxKey]=true;
    }
}

function reloadDataTable(selector, keepStatus){
    if( keepStatus ){
        $(selector).puidatatable('sort');// keep state reload
    }else{
        $(selector).puidatatable('reload');
    }
}

function renderDataTable(selector, pageSize, colSettings, data, onSelectRow, minWidth, marginWidth){
    var options = {
        responsive: true,
        selectionMode: "single",
        emptyMessage: utils.i18n("txt.MSG_NO_RECORD_FOUND"),
        columns: colSettings,
        datasource: data
    };

    pageSize = isNA(pageSize)?PAGE_SIZE_DEF:pageSize;
    var totalRecords = utils.isEmptyAry(data)?0:data.length;
    if( totalRecords > pageSize ){
        options['paginator'] = {
            rows: pageSize,
            totalRecords: totalRecords
        };
    }
    
    if( !isNA(onSelectRow) ){
        options['rowSelect'] = function(event, data){ onSelectRow(data); };
    }

    $(selector).puidatatable(options);
    if( isNA(pageSize) ){// clear table
        $(selector).puidatatable('setTotalRecords', 0);
    }
    
    // for 資料欄位太多時，使用橫向捲軸
    if( !isNA(minWidth) ){
        utils.autoResizeDataTable(selector, minWidth, marginWidth, DATATABLE_RWD_WIDTH);
    }
}

function renderDataTableLazy(selector, pageSize, totalRows, colSettings, url, 
        isPost, formData, onSelectRow, retry, ajaxKey, minWidth, marginWidth, 
        afterRenderTable, beforeRenderTable){
    formData = isNA(formData)?{}:formData;
    var submitData = JSON.stringify(formData);
    var options = {
        responsive: true,
        emptyMessage: utils.i18n("txt.MSG_NO_RECORD_FOUND"),
        lazy: true,
        paginator: {
            rows: pageSize,
            totalRecords: totalRows
        },
        resizableColumns: true,
        columnResizeMode: 'expand',
        fixColumnWidths: false,
        columns: colSettings,
        datasource: function(callback, ui) {
            var self = this;
            var sortField = ui.sortField;
            var sortOrder = ui.sortOrder;
            var offset = ui.first;
            offset = isNA(offset)?0:offset;

            var dataurl = url + "?offset=" + offset + "&rows=" + pageSize;
            if( !isNA(sortField) ){
                dataurl += "&sortField=" + sortField;
            }
            if( !isNA(sortOrder) ){
                dataurl += "&sortOrder=" + sortOrder;
            }

            $.ajax({
                type: isPost?"POST":"GET",
                url: dataurl,
                data: submitData,
                processData: isPost,
                crossDomain : true,
                contentType: "application/json; charset=utf-8",
                context: this,
                success: function(response){
                    console.log("renderDataTableLazy success ...", dataurl, response);
                    if( !isNA(beforeRenderTable) ){
                        beforeRenderTable(formData, localData);
                    }
                    var localData = utils.getResponseList(response);
                    if( isPost ){
                        callback.call(self, localData);
                    }else{
                        callback.call(self, localData.slice(offset, offset + pageSize));// for TEST JSON
                    }
                    // for 資料欄位太多時，使用橫向捲軸
                    if( !isNA(minWidth) ){
                        utils.autoResizeDataTable("#"+self.id, minWidth, marginWidth, DATATABLE_RWD_WIDTH);
                    }
                    if( !isNA(afterRenderTable) ){
                        afterRenderTable(formData, localData);
                    }
                },
                error: function (response) {
                    console.log("renderDataTableLazy error ...", dataurl, response);
                    if( !retry && response!==undefined && response.status===0 ){
                        renderDataTableLazy(selector, pageSize, totalRows, colSettings, url, 
                                            isPost, formData, onSelectRow, !retry, ajaxKey, minWidth, marginWidth);// status=0時，重試一次
                    }else{
                        utils.restErrorHandler(response);
                    }
                },
                complete: function(jqXHR, status){
                    if( !isNA(ajaxKey) ){
                        _asyncCompleteFlags[ajaxKey]=true;
                    }
                }
            });
        }
    };
    
    if( !isNA(onSelectRow) ){
        options["selectionMode"] = "single",
        options["rowSelect"] = function(event, data){
            onSelectRow(data);
        };
    }
    
    $(selector).puidatatable(options);
}