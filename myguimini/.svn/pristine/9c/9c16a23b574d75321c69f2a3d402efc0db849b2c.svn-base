/* salesperformance: 台泥業務業績預警 */

(function() {

    var WidgetSalesperformance = function() {
    };

    WidgetSalesperformance.prototype = {
        title: '台泥業務業績預警',
        init: false,
        yearmon: '', // yyyymm
        category: 0, // 0:水泥熟料, 1:副商品
        authView: '', // O,C,M,A,F,I
        salesView: '', // F,I
        sales_office_list: '',
        sales_office: '',
        bukrsList: '',
        bzirkList: '',
        bukrs: '',
        transport: '',
        sales_area: '',
        bzirk: '',
        sales: '',
        werks: '',
        level: 0,
        tabs: [1, 1, 1, 1], // 0: 1:廠, 2:發貨工廠別, 3:銷售地區群組, 4:產品別, 5:客戶群組 (東莞辦)
        // 1: 1:區, 2:產品別, 3:客戶群組, 4:發貨工廠
        // 2: 1:業務員, 2:產品別, 3:客戶群組, 4:發貨工廠
        // 3: 1:物料, :2發貨工廠, 3:客戶群組

        // route handler:
        route: function(params) {
            var _this = this;
            var view = new WidgetView({pageId: '#pgSalesperformance'});
            app.changePage(view);
            if (_this.yearmon === '') {
                _this.yearmon = date2YM(new Date());
            }
            view.$('#yearmon').val(formatYMD(_this.yearmon, '-'));
            view.$('#yearmon').change(function() {
                _this.yearmon = view.$('#yearmon').val().replace(/-/g, '');
                _this.level = 0;
                _this.resetTabs();
                _this.loadPage(view);
            });
            if (params.dkey && params.level) {
                _this.level = parseInt(params.level) - 1;
                var key = _this.getKey();
                if (/O0..../.test(key)) {
                    _this.sales_office = params.dkey;
                    if ('0' !== _this.sales_office) {
                        _this.tabs[0] = 1;
                    }
                } else if (/O12.../.test(key)) {
                    _this.transport = params.dkey;
                } else if (/O13.../.test(key)) {
                    _this.sales_area = params.dkey;
                } else if (/O11...|C0..../.test(key)) {
                    _this.bukrs = params.dkey;
                } else if (/O211..|C1.1..|M0.1..|A0.1../.test(key)) {
                    _this.bzirk = params.dkey;
                } else if (/O14...|O212..|O3112.|C1.2..|C2.12.|M0.2..|M1.12.|A0.2../.test(key)) {
                    _this.material_group = params.dkey;
                } else if (/O15...|O213..|O3113.|C1.3..|C2.13.|M0.3..|M1.13.|A0.3../.test(key)) {
                    _this.cust_group = params.dkey;
                } else if (/O3111.|C2.11.|M1.11./.test(key)) {
                    _this.sales = params.dkey;
                    executeServiceText("/dashboard/sales/getView?sales=" + _this.sales, function(data) {
                        _this.salesView = data;
                        if ('I' === _this.salesView && 2 === _this.tabs[3]) {
                            _this.tabs[3] = 1;
                        }
                        _this.level++;
                        _this.loadPage(view);
                    });
                    return;
                } else if (/O214..|O3114.|O41112|C1.4..|C2.14.|C3.112|M0.4..|M1.14.|M2.112|A0.4..|A1.1.2|F0...2/.test(key)) {
                    _this.werks = params.dkey;
                } else {
                    // console.log('unknow key:' + key);
                    return;
                }
                _this.level++;
                _this.loadPage(view);
            } else if (!_this.init) {
                executeService('/dashboard/sales/auth', function(data) {
                    _this.init = true;
                    _this.authView = data.view;
                    if ('O' === _this.authView) {
                        var list = [];
                        for (var i = 0; i < data.authList.length; i++) {
                            list.push(data.authList[i].salesOffice);
                        }
                        _this.sales_office_list = JSON.stringify(list);
                    } else if ('C' === _this.authView) {
                        var list = [];
                        for (var i = 0; i < data.authList.length; i++) {
                            list.push(data.authList[i].bukrs);
                        }
                        _this.bukrsList = JSON.stringify(list);
                    } else if ('M' === _this.authView || 'A' === _this.authView) {
                        var list = [];
                        for (var i = 0; i < data.authList.length; i++) {
                            list.push(data.authList[i].bzirk);
                        }
                        _this.bzirkList = JSON.stringify(list);
                        if ('A' === _this.authView) {
                            _this.sales = data.sales;
                            executeServiceText("/dashboard/sales/getView?sales=" + _this.sales, function(data) {
                                _this.salesView = data;
                                _this.loadPage(view);
                            });
                            return;
                        }
                    } else if ('I' === _this.authView || 'F' === _this.authView) {
                        _this.salesView = _this.authView;
                        _this.sales = data.sales;
                        _this.bzirk = data.authList[0].bzirk;
                    }
                    _this.loadPage(view);
                });
            } else {
                _this.level = 0;
                _this.loadPage(view);
            }
        },
        loadPage: function(view) {
            var _this = this;
            _this.buildTabs(view);
            var api = '';
            var params = {};
            var action = false;
            var key = _this.getKey();
            if (/O0..../.test(key)) {
                api = 'findbysales_office';
                params['sales_office'] = _this.sales_office_list;
                action = true;
            } else if (/O1..../.test(key)) {
                api = (1 === _this.tabs[0]) ? 'findcompanyviewbysales_office' :
                        (2 === _this.tabs[0]) ? 'findcompanyview_transport' :
                        (3 === _this.tabs[0]) ? 'findcompanyview_sales_area' :
                        (4 === _this.tabs[0]) ? 'findcompanyview_material_group' :
                        (5 === _this.tabs[0]) ? 'findcompanyview_cust_group' :
                        null;
                params['sales_office'] = _this.sales_office;
                action = true;
            } else if (/O22.../.test(key)) {
                api = 'findbysales_office_werks';
                var list = [];
                list.push(_this.sales_office);
                params['sales_office'] = JSON.stringify(list);
                params['transport'] = _this.transport;
            } else if (/O23.../.test(key)) {
                api = 'findbysales_office_werks';
                var list = [];
                list.push(_this.sales_office);
                params['sales_office'] = JSON.stringify(list);
                params['sales_area'] = _this.sales_area;
            } else if (/O24.../.test(key)) {
                api = 'findbysales_office_material';
                var list = [];
                list.push(_this.sales_office);
                params['sales_office'] = JSON.stringify(list);
                params['material_group'] = _this.material_group;
            } else if (/O25.../.test(key)) {
                api = 'findcompanyview_material_group';
                params['sales_office'] = _this.sales_office;
                params['cust_group'] = _this.cust_group;
            } else if (/C0..../.test(key)) {
                api = 'findbybukrs';
                params['bukrs'] = _this.bukrsList;
                action = true;
            } else if (/O21...|C1..../.test(key)) {
                api = (1 === _this.tabs[1]) ? 'findareaviewbybukrs' :
                        (2 === _this.tabs[1]) ? 'findareaviewbybukrs_materialgroup' :
                        (3 === _this.tabs[1]) ? 'findareaviewbybukrs_custgroup' :
                        (4 === _this.tabs[1]) ? 'findareaviewbybukrs_ydgg' :
                        null;
                params['bukrs'] = _this.bukrs;
                action = true;
            } else if (/O311..|C2.1../.test(key)) {
                api = (1 === _this.tabs[2]) ? 'findpersonviewbybzirk' :
                        (2 === _this.tabs[2]) ? 'findpersonviewbybzirk_materialgroup' :
                        (3 === _this.tabs[2]) ? 'findpersonviewbybzirk_custgroup' :
                        (4 === _this.tabs[2]) ? 'findpersonviewbybzirk_ydgg' :
                        null;
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                action = true;
            } else if (/O312..|C2.2../.test(key)) {
                api = 'findareaviewbybukrs_material';
                params['bukrs'] = _this.bukrs;
                params['material_group'] = _this.material_group;
            } else if (/O313..|C2.3../.test(key)) {
                api = 'findareaviewbybukrs_materialgroup';
                params['bukrs'] = _this.bukrs;
                params['cust_group'] = _this.cust_group;
            } else if (/O314..|C2.4../.test(key)) {
                api = 'findbysaleswerks';
                params['bukrs'] = _this.bukrs;
                params['werks'] = _this.werks;
            } else if (/O4111.|C3.11./.test(key)) {
                api = (1 === _this.tabs[3]) ? 'findbysales' :
                        (2 === _this.tabs[3]) ? 'findbysales_ydgg' :
                        (3 === _this.tabs[3]) ? 'findbysales_custgroup' :
                        null;
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                action = (2 === _this.tabs[3]) ? true : false;
            } else if (/O4112.|C3.12./.test(key)) {
                api = 'findpersonviewbybzirk_material';
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                params['material_group'] = _this.material_group;
            } else if (/O4113.|C3.13./.test(key)) {
                api = 'findpersonviewbybzirk_materialgroup';
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                params['cust_group'] = _this.cust_group;
            } else if (/O4114.|C3.14./.test(key)) {
                api = 'findbysaleswerks';
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                params['werks'] = _this.werks;
            } else if (/O51112|C4.112/.test(key)) {
                api = 'findbysaleswerks';
                params['bukrs'] = _this.bukrs;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                params['werks'] = _this.werks;
            } else if (/M0..../.test(key)) {
                api = (1 === _this.tabs[1]) ? 'findbybzirk' :
                        (2 === _this.tabs[1]) ? 'findbybzirk_materialgroup' :
                        (3 === _this.tabs[1]) ? 'findbybzirk_custgroup' :
                        (4 === _this.tabs[1]) ? 'findbybzirk_ydgg' :
                        null;
                params['bzirk'] = _this.bzirkList;
                action = true;
            } else if (/M1.1../.test(key)) {
                api = (1 === _this.tabs[2]) ? 'findpersonviewbybzirk' :
                        (2 === _this.tabs[2]) ? 'findpersonviewbybzirk_materialgroup' :
                        (3 === _this.tabs[2]) ? 'findpersonviewbybzirk_custgroup' :
                        (4 === _this.tabs[2]) ? 'findpersonviewbybzirk_ydgg' :
                        null;
                params['bzirk'] = _this.bzirk;
                action = true;
            } else if (/M1.2../.test(key)) {
                api = 'findbybzirk_material';
                params['bzirk'] = _this.bzirkList;
                params['material_group'] = _this.material_group;
            } else if (/M1.3../.test(key)) {
                api = 'findbybzirk_materialgroup';
                params['bzirk'] = _this.bzirkList;
                params['cust_group'] = _this.cust_group;
            } else if (/M1.4../.test(key)) {
                api = 'findbybzirk_werks';
                params['bzirk'] = _this.bzirkList;
                params['werks'] = _this.werks;
            } else if (/M2.11./.test(key)) {
                api = (1 === _this.tabs[3]) ? 'findbysales' :
                        (2 === _this.tabs[3]) ? 'findbysales_ydgg' :
                        (3 === _this.tabs[3]) ? 'findbysales_custgroup' :
                        (4 === _this.tabs[3]) ? 'findbybzirk_ydgg' :
                        null;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                action = (2 === _this.tabs[3]) ? true : false;
            } else if (/M2.12./.test(key)) {
                api = 'findpersonviewbybzirk_material';
                params['bzirk'] = _this.bzirk;
                params['material_group'] = _this.material_group;
            } else if (/M2.13./.test(key)) {
                api = 'findpersonviewbybzirk_materialgroup';
                params['bzirk'] = _this.bzirk;
                params['cust_group'] = _this.cust_group;
            } else if (/M2.14./.test(key)) {
                api = 'findbysaleswerks';
                params['bzirk'] = _this.bzirk;
                params['werks'] = _this.werks;
            } else if (/M3.112/.test(key)) {
                api = 'findbysaleswerks';
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                params['werks'] = _this.werks;
            } else if (/A0..../.test(key)) {
                api = (1 === _this.tabs[1]) ? 'findbybzirk' :
                        (2 === _this.tabs[1]) ? 'findbybzirk_materialgroup' :
                        (3 === _this.tabs[1]) ? 'findbybzirk_custgroup' :
                        (4 === _this.tabs[1]) ? 'findbybzirk_ydgg' :
                        null;
                params['bzirk'] = _this.bzirkList;
                params['sales'] = _this.sales;
                action = true;
            } else if (/A1.1../.test(key)) {
                api = (1 === _this.tabs[3]) ? 'findbysales' :
                        (2 === _this.tabs[3]) ? 'findbysales_ydgg' :
                        (3 === _this.tabs[3]) ? 'findbysales_custgroup' :
                        null;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                action = (2 === _this.tabs[3]) ? true : false;
            } else if (/A1.2../.test(key)) {
                api = 'findbybzirk_material';
                params['bzirk'] = _this.bzirkList;
                params['sales'] = _this.sales;
                params['material_group'] = _this.material_group;
            } else if (/A1.3../.test(key)) {
                api = 'findbybzirk_materialgroup';
                params['bzirk'] = _this.bzirkList;
                params['sales'] = _this.sales;
                params['cust_group'] = _this.cust_group;
            } else if (/A1.4../.test(key)) {
                api = 'findbybzirk_werks';
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                params['werks'] = _this.werks;
            } else if (/A2.1.2/.test(key)) {
                api = 'findbysaleswerks';
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                params['werks'] = _this.werks;
            } else if (/"F0..../.test(key)) {
                api = (1 === _this.tabs[3]) ? 'findbysales' :
                        (2 === _this.tabs[3]) ? 'findbysales_ydgg' :
                        (3 === _this.tabs[3]) ? 'findbysales_custgroup' :
                        null;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                action = (2 === _this.tabs[3]) ? true : false;
            } else if (/F1...2/.test(key)) {
                api = 'findbysaleswerks';
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
                params['werks'] = _this.werks;
            } else if (/I0..../.test(key)) {
                api = (1 === _this.tabs[3]) ? 'findbysales' :
                        (3 === _this.tabs[3]) ? 'findbysales_custgroup' :
                        null;
                params['bzirk'] = _this.bzirk;
                params['sales'] = _this.sales;
            }
            if (api !== '') {
                _this.executeApi(view, api, params, action);
            } else {
                // console.log('unknow key:' + key);
            }
        },
        executeApi: function(view, api, params, action) {
            var _this = this;
            var service = '/dashboard/sales/' + api + '?year_month=' + _this.yearmon + '&category=' + _this.category;
            for (var key in params) {
                service += '&' + key + '=' + encodeURIComponent(params[key]);
            }
            var href = action ? '#salesperformance?level=' + (_this.level + 1) : '';
            executeService(service, function(data) {
                view.$('#updateTime').html(date2YMDHMS(new Date(data.syncTime)));
                var html = _this.salesSummary(data.summary, true, '');
                var detailList = data.detailList || [];
                for (var i = 0; i < detailList.length; i++) {
                    var obj = detailList[i];
                    html += _this.salesSummary(obj, false, href);
                }
                view.$('#datalist').html(noContent(html));
                view.$('#datalist').listview('refresh');
            });
        },
        salesSummary: function(obj, total, href) {
            var _this = this;
            obj = obj || {};
            var title;
            if (total) {
                title = '合計 (以下統計為' + (_this.category === 0 ? '水泥熟料' : '副商品') + ')';
            } else {
                title = obj.dimension;
            }
            var html;
            if (total || href === '') {
                html = '<li data-theme="a"><span class="plantName">' + title + '</span></li>';
            } else {
                html = '<li><a href="' + href + '&dkey=' + encodeURIComponent(obj.dimensionKey) + '">' + title + '</a></li>';
            }
            html += '<table><tr><td>';
            html += _this.lightSpan(obj.accumulateQuantityLight);
            html += '</td><td>';
            html += '噸數<br/>';
            html += '累計實際: ' + formatNumber(obj.realQuantity) + '</br>';
            html += '累計目標: ' + formatNumber(obj.accumulateQuantity) + '</br>';
            if (total) {
                html += '全月目標: ' + formatNumber(obj.targetQuantity) + '</br>';
            }
            html += '</td></tr>';
            html += '<tr><td>';
            html += _this.lightSpan(obj.accumulatePriceLight);
            html += '</td><td>';
            html += 'ASP(未稅)<br/>';
            html += '實際: ' + formatNumber(obj.realPrice) + '</br>';
            html += '目標: ' + formatNumber(total ? obj.targetPrice : obj.accumulatePrice);
            html += '</td></tr>';
            html += '</table>';
            return html;
        },
        resetTabs: function() {
            for (var i = 0; i < this.tabs.length; i++) {
                this.tabs[i] = 1;
            }
        },
        getKey: function() {
            var _this = this;
            var key = _this.authView + _this.level;
            for (var i = 0; i < _this.tabs.length; i++) {
                key += _this.tabs[i];
            }
            return key;
        },
        buildTabs: function(view) {
            var _this = this;
            var key = _this.getKey();
            var content = '';
            if (_this.level === 0) {
                content += '<fieldset data-role="controlgroup" data-type="horizontal">';
                content += _this.itemtab('category', 0, '水泥熟料', _this.category === 0);
                content += _this.itemtab('category', 1, '副商品', _this.category === 1);
                content += '</fieldset>';
            }
            content += '<fieldset data-role="controlgroup" data-type="horizontal">';
            var tabIdx = 0;
            if (/O1..../.test(key)) {
                tabIdx = 0;
                if ('0' === _this.sales_office) {
                    content += _this.itemtab('itemtab', 1, '廠', _this.tabs[0] === 1);
                    content += _this.itemtab('itemtab', 2, '發貨工廠別', _this.tabs[0] === 2);
                    content += _this.itemtab('itemtab', 3, '銷售地區群組', _this.tabs[0] === 3);
                    content += _this.itemtab('itemtab', 4, '產品別', _this.tabs[0] === 4);
                    content += _this.itemtab('itemtab', 5, '客戶群組', _this.tabs[0] === 5);
                }
            } else if (/O21...|C1....|M0....|A0..../.test(key)) {
                tabIdx = 1;
                content += _this.itemtab('itemtab', 1, '區', _this.tabs[1] === 1);
                content += _this.itemtab('itemtab', 2, '產品別', _this.tabs[1] === 2);
                content += _this.itemtab('itemtab', 3, '客戶群組', _this.tabs[1] === 3);
                content += _this.itemtab('itemtab', 4, '發貨工廠', _this.tabs[1] === 4);
            } else if (/O311..|C2.1..|M1.1../.test(key)) {
                tabIdx = 2;
                content += _this.itemtab('itemtab', 1, '業務員', _this.tabs[2] === 1);
                content += _this.itemtab('itemtab', 2, '產品別', _this.tabs[2] === 2);
                content += _this.itemtab('itemtab', 3, '客戶群組', _this.tabs[2] === 3);
                content += _this.itemtab('itemtab', 4, '發貨工廠', _this.tabs[2] === 4);
            } else if (/O4111.|C3.11.|M2.11.|A1.1..|F0....|I0..../.test(key)) {
                tabIdx = 3;
                content += _this.itemtab('itemtab', 1, '物料', _this.tabs[3] === 1);
                if ('F' === _this.salesView) {
                    content += _this.itemtab('itemtab', 2, '發貨工廠', _this.tabs[3] === 2);
                }
                content += _this.itemtab('itemtab', 3, '客戶群組', _this.tabs[3] === 3);
            }
            content += '</fieldset>';
            view.$('#itemtabs').html(content);
            view.$('#itemtabs').trigger('create');
            view.$("input:radio.itemtab").bind("change", function(event, ui) {
                var choice = parseInt(view.$('input[name=itemtab]:checked').val());
                if (0 === tabIdx) {
                    _this.tabs[0] = choice;
                } else if (1 === tabIdx) {
                    _this.tabs[1] = choice;
                } else if (2 === tabIdx) {
                    _this.tabs[2] = choice;
                } else if (3 === tabIdx) {
                    _this.tabs[3] = choice;
                }
                _this.loadPage(view);
            });
            view.$("input:radio.category").bind("change", function(event, ui) {
                var choice = parseInt(view.$('input[name=category]:checked').val());
                _this.category = choice;
                _this.level = 0;
                _this.resetTabs();
                _this.loadPage(view);
            });
        },
        itemtab: function(name, value, label, checked) {
            var id = name + value;
            var content = '<input type="radio" name="' + name + '" id="' + id + '" class="' + name + '" value="' + value + '"';
            content += checked ? ' checked="checked"' : '';
            content += '/><label for="' + id + '">' + label + '</label>';
            return content;
        },
        lightSpan: function(light) {
            var lightCss = light === 'R' ? 'redlight48' :
                    light === 'Y' ? 'yellowlight48' :
                    light === 'G' ? 'greenlight48' : 'graylight48';
            return '<span class="' + lightCss + '"/>';
        }
    };

    window.WidgetSalesperformance = new WidgetSalesperformance();

})();
