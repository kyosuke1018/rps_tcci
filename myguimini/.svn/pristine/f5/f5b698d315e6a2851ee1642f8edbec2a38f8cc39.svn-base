/*
 * 日期相關function
 */
function date2YMD(date) {
    var y = date.getFullYear();
    if (isNaN(y)) {
        return '';
    }
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return '' + y + (m <= 9 ? '0' + m : m) + (d <= 9 ? '0' + d : d);
}

function date2YM(date) {
    var y = date.getFullYear();
    if (isNaN(y)) {
        return '';
    }
    var m = date.getMonth() + 1;
    return '' + y + (m <= 9 ? '0' + m : m);
}

function date2YMDHMS(date) {
    var y = date.getFullYear();
    if (isNaN(y)) {
        return '';
    }
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var mi = date.getMinutes();
    var s = date.getSeconds();
    return '' + y + '/' + (m <= 9 ? '0' + m : m) + '/' + (d <= 9 ? '0' + d : d) + ' '
            + (h <= 9 ? '0' + h : h) + ':' + (mi <= 9 ? '0' + mi : mi) + ':' + (s <= 9 ? '0' + s : s);
}

function date2MD(date) {
    var m = date.getMonth() + 1;
    if (isNaN(m)) {
        return '';
    }
    var d = date.getDate();
    return '' + (m <= 9 ? '0' + m : m) + '/' + (d <= 9 ? '0' + d : d);
}

function yesterday() {
    var date = new Date();
    date.setDate(date.getDate() - 1);
    return date2YMD(date);
}

function formatYMD(str, separator) {
    if (str.length === 8) {
        return str.substring(0, 4) + separator + str.substring(4, 6) + separator + str.substring(6);
    } else if (str.length === 6) {
        return str.substring(0, 4) + separator + str.substring(4, 6);
    } else {
        return str;
    }
}

function formatHHMMSS(str) {
    if (str.length === 6) {
        return str.substring(0, 2) + ':' + str.substring(2, 4) + ':' + str.substring(4, 6);
    } else {
        return str;
    }
}

/*
 * 數值相關function
 */
function formatNumber(number) {
    number = number || 0;
    number = number.toFixed(0) + '';
    var x = number.split('.');
    var x1 = x[0];
    var x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}

/*
 * helper function
 */
function noContent(content) {
    if (typeof(content) === "undefined" || content === '') {
        return '<h3 style="text-align:center;">無資料</h3>';
    } else {
        return content;
    }
}

function parseQueryString(queryString) {
    var params = {};
    if (queryString) {
        _.each(
                _.map(decodeURI(queryString).split(/&/g), function(el, i) {
            var aux = el.split('=');
            var o = {};
            if (aux.length >= 1) {
                var val = undefined;
                if (aux.length === 2)
                    val = aux[1];
                o[aux[0]] = val;
            }
            return o;
        }),
                function(o) {
                    _.extend(params, o);
                }
        );
    }
    return params;
}

function buildLink(route, params) {
    var queryString = '';
    for (var key in params) {
        if (queryString !== '') {
            queryString += '&';
        }
        queryString += key + '=' + encodeURIComponent(params[key]);
    }
    return queryString === '' ? route : route + '?' + queryString;
}
