/* user management */
$(function() {
    textDelayChange('#nameFilter', '#nameFilterLast', remoteNameFilterChange);
});
function RolesSelectAll(all) {
    $('#roleList input:checkbox').attr('checked', all);
}
function editUserDlg_OK() {
    var editAccount = $('#editAccount').val().toLowerCase();
    $('#editAccount').val(editAccount);
    if ('' === editAccount) {
        alert('登入帳號必填!');
        $('#editAccount').focus();
        return false;
    }
    if (!/^[a-z0-9_\.\-]*$/.test(editAccount)) {
        alert('登入帳號僅能輸入 a-z0-9_.- 等字元!');
        $('#editAccount').focus();
        return false;
    }
    return true;
}
function editUserDlg_complete(xhr, status, args) {
    if(args.validationFailed) {
        alert("Validation Failed");
    } else {
        if ('' !== args.error) {
            alert(args.error);
        } else {
            PF('editUserDlg').hide();
        }
    }
}
