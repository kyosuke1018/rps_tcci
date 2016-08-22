/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery("input[type=text]").focus(function(){    
    this.select(); 
});

function remitCalc(rownumber) {
    var arAmount = jQuery('#paymentDetail\\:' + rownumber + '\\:arAmount').val();    
    var premiumDiscount = jQuery('#paymentDetail\\:' + rownumber + '\\:premiumDiscount').val();   
    var salesDiscount = jQuery('#paymentDetail\\:' + rownumber + '\\:salesDiscount').val();
    var salesReturn = jQuery('#paymentDetail\\:' + rownumber + '\\:salesReturn').val();
    var negativeAr = jQuery('#paymentDetail\\:' + rownumber + '\\:negativeAr').val();
    var differenceCharge = jQuery('#paymentDetail\\:' + rownumber + '\\:differenceCharge').val();
    var itemAdvanceReceiptA = jQuery('#paymentDetail\\:' + rownumber + '\\:itemAdvanceReceiptA').val();    
    var itemAdvanceReceiptJ = jQuery('#paymentDetail\\:' + rownumber + '\\:itemAdvanceReceiptJ').val();   
    var amount2 = jQuery('#paymentDetail\\:' + rownumber + '\\:amount2').val();   
    var amount = (parseInt(arAmount) || 0) + (parseInt(premiumDiscount) || 0) - (parseInt(salesDiscount) || 0) - (parseInt(salesReturn) || 0) - (parseInt(negativeAr) || 0) - (parseInt(differenceCharge) || 0) - (parseInt(itemAdvanceReceiptA) || 0) - (parseInt(itemAdvanceReceiptJ) || 0) - (parseInt(amount2) || 0);
    jQuery('#paymentDetail\\:' + rownumber + '\\:amount').text(commafy(amount));
    jQuery('#paymentDetail\\:' + rownumber + '\\:amountHidden').val(amount);  
    calcTotal();
}

function calcTotal() {
    var rowCount = $('#paymentDetail table tbody tr').length; 
    var cashTotal = 0, checkTotal = 0;  
    for (i = 0; i < rowCount; i++) {
        var paymentType = jQuery('#paymentDetail\\:' + i + '\\:paymentType option:selected').val();
        var iamount = jQuery('#paymentDetail\\:' + i + '\\:amountHidden').val();     
        if (paymentType == 'CASH') {
            cashTotal += (parseInt(iamount) || 0);          
        } else if (paymentType == 'CHECK') {
            checkTotal += (parseInt(iamount) || 0);
        }
        var paymentType2 = jQuery('#paymentDetail\\:' + i + '\\:paymentType2 option:selected').val();
        var iamount2 = jQuery('#paymentDetail\\:' + i + '\\:amount2').val();   
        if (paymentType2 == 'CASH') {
            cashTotal += (parseInt(iamount2) || 0);
        } else if (paymentType2 == 'CHECK') {
            checkTotal += (parseInt(iamount2) || 0);
        }        
    }
    jQuery('#cashRemitTotal').text(commafy(cashTotal));
    jQuery('#cashRemitTotalHidden').val(cashTotal);
    jQuery('#checkRemitTotal').text(commafy(checkTotal));
    jQuery('#checkRemitTotalHidden').val(checkTotal);     
}

function calcColumnTotal(name) {
    var rowCount = $('#paymentDetail table tbody tr').length; 
    var total = 0;  
    for (i = 0; i < rowCount; i++) {
        var iamount = jQuery('#paymentDetail\\:' + i + '\\:'+name).val();
        total += (parseInt(iamount) || 0);
    }
    jQuery('#'+name+'Total').text(commafy(total));
}

function commafy(num)
{
num = num+"";
var re=/(-?\d+)(\d{3})/
while(re.test(num))
{
num=num.replace(re,"$1,$2")
}
return num;
}