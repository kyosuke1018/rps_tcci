// primeUI (使用 PrimeElements 時，不需要) // jquery use .prop
// change: function(event, checked)
function createCheckbox(selector, callback){
    $(selector).change(callback);
    /*
    if( isNA(callback) ){
        $(selector).puicheckbox();
    }else{
        $(selector).puicheckbox({
            change: callback
        });
    }
    */
}

function setCheckbox(selector, check){
    if( check ){
        checkCheckbox(selector);
    }else{
        uncheckCheckbox(selector);
    }
}

function getCheckbox(selector){
    //console.log($(selector));
    //console.log($(selector).prop('checked'));
    return $(selector).prop('checked');
    //return $(selector).puicheckbox('isChecked');
}
function getCheckboxByName(name){
    return $("input[name='"+name+"']:checked").val() === "on";
}

function checkCheckbox(selector){
    //$(selector).puicheckbox('check');
    $(selector).attr('checked', 'checked');// for puicheckbox bug
}

function uncheckCheckbox(selector){
    //$(selector).puicheckbox('uncheck');
    $(selector).removeAttr('checked');// for puicheckbox bug
}

function enableCheckbox(selector){
    //$(selector).puicheckbox('enable');
    $(selector).attr('disabled', 'disabled');// for puicheckbox bug
}

function disableCheckbox(selector){
    //$(selector).puicheckbox('disable');
    $(selector).removeAttr('disabled');// for puicheckbox bug
}
