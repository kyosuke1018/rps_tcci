//<editor-fold defaultstate="collapsed" desc="for variable check">
/**
  ^\\d+$                //非負整數（正整數+0）     
  ^[0-9]*[1-9][0-9]*$　　//正整數     
  ^((-\\d+)|(0+))$      //非正整數（負整數+0）     
  ^-[0-9]*[1-9][0-9]*$  //負整數     
  ^-?\\d+$              //整數     
  ^\\d+(\\.\\d+)?$      //非負浮點數（正浮點數+0）     
  ^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$　　//正浮點數     
  ^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$　　//非正浮點數（負浮點數+0）     
  ^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$　　//負浮點數     
  ^(-?\\d+)(\\.\\d+)?$　　//浮點數
 * @param {type} re
 * @param {type} s
 * @returns {unresolved}
 */
function TestRegex(re, s){  // 参數說明 re 为正則表達式   s 为要判斷的字符 
    return re.test(s);
}

function isPositiveInt(str){
    var re = /^[0-9]*[1-9][0-9]*$/;
    return TestRegex(re, str);
}

function isNA(o){
    return (o === null || o === undefined);
}

function isEmpty(str){
    return isNA(str) || str==="" || (isNaN(str) && str.trim().length===0);
}
function isEmptyAry(ary){
    return isNA(ary) || ary.length===0;
}

function isId(o){
    return !isNA(o) && isNumeric(o) && parseInt(o)>0;
}

function isNumeric(num){
    return !isNaN(num) && !isEmpty(num);
}

function nvl(ori, def){
    if( isNA(ori) ){
        return def;
    }
    return ori;
}
//</editor-fold>

function scrollTo(selector, offset){
    var y = $(selector).offset().top;
    //y = y<60? y:y-60;
    y = isNA(offset)? y:y-offset;
    $('html,body').animate({scrollTop: y}, 'slow');
}

var utils = {
    TestRegex: TestRegex,
    isPositiveInt: isPositiveInt,
    isNA: isNA,
    isEmpty: isEmpty,
    isEmptyAry: isEmptyAry,
    isId: isId,
    isNumeric: isNumeric,
    nvl: nvl,
    
    scrollTo:scrollTo
};