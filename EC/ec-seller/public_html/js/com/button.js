/* 
 * dependencys : jQuery, primeUI
 */
// create primeUI button
function createBtn(selector, iconClass, onClickCallBack){
    var options = {
        click: function(event) {
            onClickCallBack(event);
        }
    };
    if( !isNA(iconClass) ){
        options["icon"] = iconClass;
    }    
    $(selector).puibutton(options);
}

function disableBtn(selector){
    $(selector).puibutton('disable');
}

function enableBtn(selector){
    $(selector).puibutton('enable');
}

function createGoToBtn(btnSelector, targetSelector, offset){
    if( isNA(offset) ){
        offset = 60;// sticky header height
    }
    createBtn(btnSelector, null, function(){ utils.scrollTo(targetSelector, offset); });
}
