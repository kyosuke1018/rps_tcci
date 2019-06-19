/* 
 * dependencys : jQuery, primeUI
 */
/* global NO_SELECT_OP, MSG_IN_KEYWORD, NO_SELECT_OP_NUM, NO_SELECT_OP_STR */
// create select
function createDropdown(selector, options, noSelectionOption, onChangeCallBack, styleClass){
    var ops = isNA(options)? []:options;
    if( !isNA(noSelectionOption) ){
        ops = [noSelectionOption].concat(ops);
    }
    styleClass = isNA(styleClass)?"selectInput":styleClass;
    
    var attrs = {
        data: ops,
        effectSpeed: "fast",
        styleClass: styleClass,
        change: function(event, item){
            if( !isNA(onChangeCallBack) ){
                onChangeCallBack(ops[item.index]);
            }
        }
    };
    
    $(selector).puidropdown(attrs);
}

function hideDropdown(selector){
    $(selector).closest( ".ui-dropdown" ).hide();
}

function showDropdown(selector){
    $(selector).closest( ".ui-dropdown" ).hide();
}

function enableDropdown(selector){
    $(selector).puidropdown('enable');
}

function disableDropdown(selector){
    $(selector).puidropdown('disable');
}

function setDropdownValue(selector, value){
    $(selector).puidropdown('selectValue', value);
}

function getDropdownValue(selector){
    return $(selector).puidropdown('getSelectedValue');
}

function getDropdownLabel(selector){
    return $(selector).puidropdown('getSelectedLabel');
}

function getSelectedOption(selector){
    var op = {};
    op['value'] = getDropdownValue(selector);
    op['label'] = getDropdownLabel(selector);
    return op;
}
function getSelectedOptionOnChange(event){
    return getSelectedOption(event.target);
}

// build select
/*function buildSelectByList(selector, ary, selectCallBack){
    var ops = isNA(ary)? []:ary;
    $(selector).puidropdown({
        data: ops,
        effectSpeed: "fast",
        //style: "min-width:150px;",
        styleClass: "selectInput",
        change: function(event, item){
            if( !isNA(selectCallBack) ){
                console.log(event);
                console.log(item);
                selectCallBack(ops[item.index]);
            }
        }
    });
}*/

// build AutoComplete
function buildAutoCompleteByUrl(selector, url, needKeyword, selectCallBack, retry){
    var filtered = [];
    $(selector).puiautocomplete({
        effect: 'fade',
        effectSpeed: 'fast',
        styleClass: "selectInput",
        forceselection: true, 
        dropdown: !needKeyword,
        select: function(event, item) {
            if( !isNA(selectCallBack) ){
                selectCallBack(filtered[item.index()]);
            }
        },
        completeSource: function(request, response) {
            var keywork = request.query;
            if( needKeyword && keywork.trim() === '' ){
                alert("Not input keyword!");
                return;
            }
            // 最多顯示 100 筆 rows=100
            //var dataurl = URL_PLANT_AUTOCOMPLETE+'?plantClassLevel='+plantClassLevel+'&keywork='+keywork+'&offset=0&rows=100';
            var restUrl = url;
            
            $.ajax({
                type: "GET",
                url: restUrl,
                dataType: "json",
                context: this,
                success: function(data) {
                    console.log("buildAutoCompleteByUrl success ...", restUrl, data);
                    var list = data.list;
                    for(var i = 0; i < list.length; i++) {
                        //var txt = (list[i].ename!==undefined)? list[i].ename:"";
                        //txt = (list[i].cname!==undefined)? txt + " " + list[i].cname:txt;
                        var txt = (!isNA(list[i].label))?  list[i].label:"";
                        filtered.push({'value':list[i].id, 'label':txt.trim()});
                    }
                    response.call(this, filtered);
                },
                error: function (response) {
                    console.log("buildAutoCompleteByUrl success ...", restUrl, response);
                    if( !retry && response!==undefined && response.status===0 ){
                        buildAutoCompleteByUrl(selector, restUrl, needKeyword, !retry);// status=0時，重試一次
                    }else{
                        utils.restErrorHandler(response);
                    }
                }
            });
        }
    });
}
