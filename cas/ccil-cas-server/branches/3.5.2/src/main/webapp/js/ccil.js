function lowCaseConverter() {
    var jid = document.getElementById("username");
    var data = jid.value;
    jid.value = data.toLowerCase().trim();
    jid = document.getElementById("password");
    data = jid.value;
    jid.value = data.trim();
    return true;
}