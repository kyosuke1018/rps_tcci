/* global DEF_LANG, utils */
var _useForMobile = true; // true: DateTimePicker.js; false: jquery-ui.js
var _datepickerI18n = null;

// yyyy-MM-dd
function createDatePicker(selector, def){
    var langCode = utils.getLangCode();
    if( _useForMobile ){
        $(selector+"DIV").DateTimePicker({
                language: langCode,
                defaultDate: new Date(),
                /*titleContentDate: "設置月份",
                dateFormat: 'MM yyyy',
                formatHumanDate: function(sDate){
                  return sDate.month + " / " + sDate.yyyy;
                },*/
                //beforeShow: function(element){ utils.hideBodyScrollY(); },
                //afterHide: function(element){ utils.showBodyScrollY(); },
                //buttonsToDisplay: [ "SetButton","HeaderCloseButton"],
                //clearButtonContent: "取消",
                animationDuration:200
        });
    }else{
        $(selector).datetimepicker({"showTimepicker":false, "showButtonPanel":false});
    }
    
    if( !isNA(def) ){
        setDatePicker(selector, def);
    }
}

function setDatePicker(selector, date){
    if( _useForMobile ){
        $(selector).val(isNA(date)?"":formatDate(date));
    }else{
        $(selector).datepicker('setDate', isNA(date)?"":date);
    }
}

function getDatePicker(selector){// yyyy-MM-dd 00:00:00
    if( _useForMobile ){
        var endAt = $(selector).val();
        return toISODateTime(endAt);
    }else{
        var endAt = $(selector).datepicker('getDate');
        return utils.formatDT(endAt);
    }
}

// yyyy-MM-dd HH:mm
function createDateTimePicker(selector, def){
    createDatePicker(selector, def);
}

function setDateTimePicker(selector, date){
    if( _useForMobile ){
        $(selector).val(isNA(date)?"":utils.formatDT(date));
    }else{
        $(selector).datepicker('setDate', isNA(date)?"":date);
    }
}

function getDateTimePicker(selector){// yyyy-MM-dd HH:mm:00
    return getDatePicker(selector);
}

function loadDatepickerI18n(langCode) {
    if( _useForMobile ){
        // 建立時設定
        return;
    }
    langCode = isNA(langCode)? DEF_LANG:langCode;
    console.log("loadDatepickerI18n ", langCode);
    if (_datepickerI18n !== null){ document.getElementById('datepickerI18n').removeElement(); }; 
    _datepickerI18n = document.createElement("script"); 
    if( _useForMobile ){
        _datepickerI18n.src = 'lib/jquery/i18n/DateTimePicker-i18n-'+langCode+'.js?v=0.0.9'; 
    }else{
        _datepickerI18n.src = 'lib/jquery/i18n/jquery.ui.datepicker-'+langCode+'.js?v=0.0.9'; 
    }
    _datepickerI18n.type = "text/javascript"; 
    _datepickerI18n.id = "datepickerI18n";
    document.getElementsByTagName("head")[0].appendChild(_datepickerI18n);
    console.log("loadDatepickerI18n ", _datepickerI18n.src);
} 

