
function disableInput(selector){
    $(selector).css("background-color", "#EEEEEE");
    $(selector).attr("disabled", true);
}

function enableInput(selector){
    $(selector).css("background-color", "#FFFFFF");
    $(selector).removeAttr("disabled");
}

function setTextValue(selector, value){
    $(selector).val(value);
}

function getTextValue(selector){
    return $(selector).val();
}

function copyToNotEmpty(selector){
    if( getTextValue(selector)==="" ){
        setTextValue(selector, $(event.target).val());
    }
}
