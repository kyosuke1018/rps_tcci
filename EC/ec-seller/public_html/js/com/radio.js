

function setRadioValue(name, formSelector, value){
    $("input[name='"+name+"'][value='"+value+"']", formSelector).prop("checked", true);
}

function getRadioValue(name, formSelector){
    return $("input[name='"+name+"']:checked", formSelector).val();
}
