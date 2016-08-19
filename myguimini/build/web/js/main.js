// generic widget view
window.WidgetView = Backbone.View.extend({
    pageId: '',
    render: function(eventName) {
        $(this.el).html(_.template($(this.options.pageId).html()));
        return this;
    }
});

var AppRouter = Backbone.Router.extend({
    init: false,
    tag: '',
    preferences: {},
    routes: {
        "": "pgMain",
        "pgLogin": "pgLogin",
        "pgMain": "pgMain",
        "*actions": "defaultRoute" // Backbone will try to match the route above first
    },
    initialize: function() {
        // Handle back button throughout the application
        $('.back').live('click', function(event) {
            window.history.back();
            return false;
        });
        $('.logout').live('click', function(event) {
            localStorage.removeItem('tgt');
            localStorage.removeItem('loginDate');
            localStorage.removeItem('preferences');
            app.navigate('pgLogin', {trigger: true});
            return false;
        });
        $('.home').live('click', function(event) {
            app.navigate('', {trigger: true});
            return false;
        });
        this.firstPage = true;
    },
    pgLogin: function() {
        var _this = this;
        var view = new WidgetView({pageId: '#pgLogin'});
        this.changePage(view);
        var accunt = localStorage.getItem('account');
        var password = localStorage.getItem('password');
        var remember = localStorage.getItem('remember');
        if (accunt !== null) {
            view.$('#account').val(accunt);
        }
        if (password !== null) {
            view.$('#password').val(Base64.decode(password));
        }
        if (remember === '0') {
            view.$('#remember').attr("checked", false).checkboxradio("refresh");
        }
        view.$('#remember').click(function() {
            if (!view.$(this).is(':checked')) {
                localStorage.clear();
                localStorage.setItem('remember', '0');
            }
        });
        view.$('#logingroup :input').keyup(function(e) {
            if (e.keyCode === 13) {
                _this.login(view);
            }
        });
        view.$('#btnLogin').click(function(e) {
            _this.login(view);
            return false;
        });
        view.$('#app_version').html(app_version);
    },
    pgMain: function() {
        var _this = this;
        var view = new WidgetView({pageId: '#pgMain'});
        _this.changePage(view);
        view.$('#app_version').html(app_version);
        view.$("#tag").bind("change", function(event, ui) {
            _this.tag = view.$('#tag').val();
            _this.updatePreferences(view);
        });
        if (_this.loadPreferences(view)) {
            _this.buildTags(view);
            _this.updatePreferences(view);
        }
        // 重設之前選到的項目,以便預設與app.tag相同
        window.WidgetPdapatrol.comId = '';
        window.WidgetLms.choiceSelected = '';
    },
    defaultRoute: function(action) {
        var widgetId = '';
        var params = {};
        var ary = action.split('?');
        if (ary.length > 0) {
            widgetId = ary[0];
        }
        if (ary.length > 1) {
            params = parseQueryString(ary[1]);
        }
        var widget = this.findWidget(widgetId);
        if (widget) {
            if (!this.init) { // 重新整理時回到第一層
                if (Object.keys(params).length > 0) {
                    app.navigate(ary[0], {trigger: true});
                    return;
                } else {
                    this.init = true;
                }
            }
            widget.route(params);
        } else {
            alert('widget not found!');
            app.navigate('', {trigger: true});
        }
    },
    changePage: function(page) {
        $(page.el).attr('data-role', 'page');
        page.render();
        $('body').append($(page.el));
        var transition = $.mobile.defaultPageTransition;
        // We don't want to slide the first page
        if (this.firstPage) {
            transition = 'none';
            this.firstPage = false;
        }
        $.mobile.changePage($(page.el), {changeHash: false, transition: transition});
    },
    login: function(view) {
        localStorage.removeItem('preferences');
        var account = $.trim(view.$('#account').val()).toLowerCase();
        view.$('#account').val(account);
        if (account === '') {
            alert('帳號不能為空白!');
            view.$('#account').focus();
            return;
        }
        var password = view.$('#password').val();
        if (password === '') {
            alert('密碼不能為空白!');
            view.$('#password').focus();
            return;
        }
        var params = {username: account, password: password};
        var url = "/myguimini/servlet/login";
        var remember = view.$('#remember').is(':checked');
        if (remember) {
            localStorage.setItem('account', account);
            localStorage.setItem('password', Base64.encode(password));
            localStorage.setItem('remember', '1');
        }
        $.mobile.showPageLoadingMsg();
        $.post(url, params, function(data) {
            data = $.trim(data);
            localStorage.setItem('tgt', data);
            localStorage.setItem('loginDate', date2YMD(new Date()));
            localStorage.removeItem('preferences');
            app.tag = '';
            app.navigate('', {trigger: true});
        }, "text").fail(function(jqxhr, textStatus, error) {
            $.mobile.hidePageLoadingMsg();
            var err = textStatus + ", " + error + "," + jqxhr.status;
            // console.log("Request Failed: " + err);
            if (jqxhr.status === 401) {
                alert("登入失敗");
            } else if (jqxhr.status === 403) {
                alert("您目前無權限使用此服務!");
            } else if (jqxhr.status === 500) {
                alert("服務執行錯誤!");
            } else {
                alert(err);
            }
        }).always(function() {
            $.mobile.hidePageLoadingMsg();
        });
    },
    findWidget: function(widgetId) {
        widgetId = widgetId.toLowerCase();
        var widget = 'Widget' + widgetId.charAt(0).toUpperCase() + widgetId.slice(1); // ppmonitor -> WidgetPpmonitor
        if (window[widget] && typeof window[widget].route !== 'undefined') {
            return window[widget];
        } else {
            return false;
        }
    },
    loadPreferences: function(view) {
        var _this = this;
        var today = date2YMD(new Date());
        var loginDate = localStorage.getItem('loginDate');
        _this.preferences = (today === loginDate) ? JSON.parse(localStorage.getItem('preferences')) : null;
        if (_this.preferences !== null) {
            return true;
        } else {
            var device = encodeURI('myguimini:' + navigator.userAgent);
            executeService('/mygui/service/preference/mydashboard?device=' + device, function(data) {
                _this.preferences = data;
                localStorage.setItem('preferences', JSON.stringify(_this.preferences));
                _this.buildTags(view);
                _this.updatePreferences(view);
            });
            return false;
        }
    },
    buildTags: function(view) {
        var _this = this;
        var tags = {};
        tags['全部'] = true;
        var found = false;
        $.each(_this.preferences, function(index, obj) {
            var widget = _this.findWidget(obj.name);
            if (widget) {
                (obj.tags || '').toLowerCase().split(' ').forEach(function(tag) {
                    if (tag !== '' && tag !== 'full' && !tags[tag]) {
                        tags[tag] = true;
                        if (_this.tag === tag) {
                            found = true;
                        }
                    }
                });
            }
        });
        if (!found) {
            _this.tag = '全部';
        }
        var content = '';
        for (var tag in tags) {
            var selected = tag === _this.tag ? 'selected' : '';
            content += '<option value="' + tag + '" ' + selected + '>' + tag + '</option>';
        }
        view.$('#tag').html(content);
        view.$('#tag').selectmenu('refresh');
    },
    updatePreferences: function(view) {
        var _this = this;
        var content = '';
        $.each(_this.preferences, function(index, obj) {
            var widgetId = obj.name.toLowerCase();
            var widget = _this.findWidget(widgetId);
            if (widget) {
                if (_this.tag === '全部' || (obj.tags || '').indexOf(_this.tag) > -1) {
                    content += '<li><a href="#' + widgetId + '"><img src="img/icon_' + widgetId + '.png" width="422" height="422"/><h2>' + widget.title + '</h2></a></li>';
                }
            }
        });
        if (content === '') {
            content = '<h3 style="text-align:center;">請調整MyGUI個人化設定後按重新整理</h3>';
        }
        view.$('#preferences').html(content);
        view.$('#preferences').listview('refresh');
    }
});

var app;
$(document).ready(function() {
    app = new AppRouter();
    Backbone.history.start();
});

function executeService(service, onsuccess) {
    var tgt = localStorage.getItem('tgt');
    if (null === tgt) {
        app.navigate("pgLogin", {trigger: true});
        return;
    }
    var params = {service: service, tgt: tgt};
    var url = '/myguimini/servlet/executeService';
    $.mobile.showPageLoadingMsg();
    $.getJSON(url, params, function(data) {
        onsuccess(data);
    }).fail(function(jqxhr, textStatus, error) {
        $.mobile.hidePageLoadingMsg();
        var err = textStatus + ", " + error + "," + jqxhr.status;
        // console.log("Request Service: " + service);
        // console.log("Request Failed: " + err);
        if (jqxhr.status === 401) {
            // alert('Session已過期!');
            app.navigate("pgLogin", {trigger: true});
            return;
        } else if (jqxhr.status === 403) {
            alert("您目前無權限使用此服務!");
            return;
        } else if (jqxhr.status === 500) {
            alert("服務執行錯誤!");
            return;
        }
        alert("很抱歉，目前無回應!");
    }).always(function() {
        $.mobile.hidePageLoadingMsg();
    });
}

function executeServiceText(service, onsuccess) {
    var tgt = localStorage.getItem('tgt');
    if (null === tgt) {
        app.navigate("pgLogin", {trigger: true});
        return;
    }
    var params = {service: service, tgt: tgt};
    var url = '/myguimini/servlet/executeService';
    $.mobile.showPageLoadingMsg();
    $.get(url, params, function(data) {
        onsuccess(data);
    }).fail(function(jqxhr, textStatus, error) {
        var err = textStatus + ", " + error + "," + jqxhr.status;
        // console.log("Request Service: " + service);
        // console.log("Request Failed: " + err);
        if (jqxhr.status === 401) {
            // alert('Session已過期!');
            app.navigate("pgLogin", {trigger: true});
            return;
        } else if (jqxhr.status === 403) {
            alert("您目前無權限使用此服務!");
            return;
        } else if (jqxhr.status === 500) {
            alert("服務執行錯誤!");
            return;
        }
        alert("很抱歉，目前無回應!");
    }).always(function() {
        $.mobile.hidePageLoadingMsg();
    });
}
