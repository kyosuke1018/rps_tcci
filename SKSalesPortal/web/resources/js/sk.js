/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function editPremiumDiscountDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('editPremiumDiscountDialog').hide();
}

function fiInterfaceDialog_showComplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        return;
    }
    PF('fiInterfaceDialog').show();
}

function fiInterfaceDialog_uploadComplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('fiInterfaceDialog').hide();
}

function editCheckDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('editCheckDialog').hide();
}

function editSalesMemberDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('editMemberDlg').hide();
}

function editCrudChannelDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('crudChannelDlg').hide();
}

function editSalesAllowancesDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('editSalesAllowancesDialog').hide();
}

function reviewOrderDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg != 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('reviewOrderDialogVar').hide();
}
function editQuotaionMailGroupDialog_okcomplete(xhr, status, args) {
    if (typeof args.errormsg !== 'undefined') {
        alert(args.errormsg);
        return;
    }
    PF('editQuotationMailGroupDialogVar').hide();
}