/* cementsales:台泥業務銷售,cementprod:台泥生產資訊,vpphour:窯磨運轉  */

(function() {

    var WidgetCementsales = function() {
    };

    WidgetCementsales.prototype = {
        title: '台泥業務銷售',
        path: '',
        // route handler:
        route: function(params) {
            var _this = this;
            if (params.choice && params.compCode) {
                var view = new WidgetView({pageId: '#pgCementsales2'});
                app.changePage(view);
                view.$('#path').html(_this.path);
                _this.loadPage2(view, params.choice, params.compCode);
            } else {
                var view = new WidgetView({pageId: '#pgCementsales'});
                app.changePage(view);
                view.$('#choice').change(function() {
                    _this.loadPage1(view);
                });
                _this.loadPage1(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var choice = view.$('#choice').val();
            var service = '/dashboard/service/cementsales/v3/' + choice;
            executeService(service, function(data) {
                var updateTime = formatYMD(data.date, '/') + ' ' + formatHHMMSS(data.time);
                view.$('#updateTime').html(updateTime);
                var datalist = data.data;
                var content = '';
                $.each(datalist, function(index, obj) {
                    if (/today|yesterday/.test(choice)) {
                        var linkParams = {choice: choice, compCode: obj.compCode};
                        var link = buildLink('#cementsales', linkParams);
                        content += '<li><a href="' + link + '">' + obj.compName + '</a></li>';
                    } else {
                        content += '<li data-theme="a"><span class="plantName">' + obj.compName + '</span></li>';
                    }
                    content += '<table><tr><td rowspan="3" align="center"><img src="img/coins.png"/></td>';
                    content += '<td>總銷量:</td><td class="col3">' + formatNumber(obj.amountAll) + '</td></tr>';
                    content += '<tr><td>水泥銷量:</td><td class="col3">' + formatNumber(obj.amount1) + '</td></tr>';
                    content += '<tr><td>熟料銷量:</td><td class="col3"">' + formatNumber(obj.amount2) + '</td></tr>';
                    content += '</table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#datalist li a').click(function() {
                    _this.path = view.$(this).text();
                });
            });
        },
        loadPage2: function(view, choice, compCode) {
            var _this = this;
            var service = '/dashboard/service/cementsales/v3/' + choice + '/' + compCode;
            executeService(service, function(list) {
                var content = '';
                $.each(list, function(index, obj) {
                    content += '<li><table>';
                    content += '<tr><td>項目: </td><td class="col2">' + obj.prodName + '</td></tr>';
                    content += '<tr><td>銷量: </td><td class="col2">' + formatNumber(obj.prodAmount) + '</td></tr>';
                    content += '</table></li>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        }
    };

    window.WidgetCementsales = new WidgetCementsales();

})();

// cementprod:台泥生產資訊 ---------------------------------
(function() {

    var WidgetCementprod = function() {
    };

    WidgetCementprod.prototype = {
        title: '台泥生產資訊',
        // route handler:
        route: function(params) {
            var _this = this;
            var view = new WidgetView({pageId: '#pgCementprod'});
            app.changePage(view);
            view.$('#choice').change(function() {
                _this.loadPage1(view);
            });
            view.$("input[name=category]").bind("change", function(event, ui) {
                _this.loadPage1(view);
            });
            _this.loadPage1(view);
        },
        loadPage1: function(view) {
            var _this = this;
            var choice = view.$('#choice').val();
            var category = view.$('input[name=category]:checked').val();
            var service = '/dashboard/service/cementprod/date/' + choice;
            if ('1' === category) {
                service += 'P';
            }
            executeService(service, function(data) {
                var updateTime = formatYMD(data.date, '/') + ' ' + formatHHMMSS(data.time);
                view.$('#updateTime').html(updateTime);
                var list = data.data || [];
                var content = '';
                $.each(list, function(index, obj) {
                    content += '<li data-theme="a"><span class="plantName">' + obj.plantName + '</span></li>';
                    if ('0' === category) {
                        content += _this.page1_prod(obj);
                    } else {
                        content += _this.page1_power(obj);
                    }
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        page1_prod: function(obj) {
            var _this = this;
            var content = '<table><tr><td rowspan="4" align="center"><img src="img/information.png"/></td>';
            content += '<td>石灰石:</td><td class="col3">' + formatNumber(_this.processValue(obj.pp01)) + '</td></tr>';
            content += '<tr><td>生料:</td><td class="col3">' + formatNumber(_this.processValue(obj.pp02)) + '</td></tr>';
            content += '<tr><td>熟料:</td><td class="col3">' + formatNumber(_this.processValue(obj.pp03)) + '</td></tr>';
            content += '<tr><td>水泥:</td><td class="col3">' + formatNumber(_this.processValue(obj.pp04)) + '</td></tr>';
            content += '</table>';
            return content;
        },
        page1_power: function(obj) {
            var _this = this;
            var content = '<table><tr><td rowspan="4" align="center"><img src="img/battery.png"/></td>';
            content += '<td>生料工序:</td><td class="col3">' + _this.processValue(obj.pp19) + '</td></tr>';
            content += '<tr><td>熟料工序:</td><td class="col3">' + _this.processValue(obj.pp20) + '</td></tr>';
            content += '<tr><td>水泥工序:</td><td class="col3">' + _this.processValue(obj.pp21) + '</td></tr>';
            content += '<tr><td>熟料綜合:</td><td class="col3">' + _this.processValue(obj.pp22) + '</td></tr>';
            content += '</table>';
            return content;
        },
        processValue: function(val) {
            return typeof val === 'undefined' ? 0 : val;
        }
    };

    window.WidgetCementprod = new WidgetCementprod();


})();

// vpphour:窯磨運轉 ----------------------------------------
(function() {

    var WidgetVpphour = function() {
    };

    WidgetVpphour.prototype = {
        title: '窯磨運轉',
        // route handler:
        route: function(params) {
            var _this = this;
            var view = new WidgetView({pageId: '#pgVpphour'});
            app.changePage(view);
            _this.loadPage1(view);
        },
        loadPage1: function(view) {
            var _this = this;
            var service = '/dashboard/service/vpphour/getdata';
            executeService(service, function(data) {
                var updateTime = formatYMD(data.date, '/') + ' ' + formatHHMMSS(data.time);
                view.$('#updateTime').html(updateTime);
                var datalist = data.plants;
                var array = [];
                var lastPlant = {plant: '', yauimg: 0, moimg: 0, data: []}; // 0:gray, 1:green, 2:yellow, 3:red
                for (var i = 0; i < datalist.length; i++) {
                    var obj = datalist[i];
                    var plant = obj.plant;
                    if (plant !== lastPlant.plant) {
                        if (lastPlant.plant !== '') {
                            array.push(lastPlant);
                        }
                        lastPlant = {plant: plant, yauimg: 0, moimg: 0, data: []};
                    }
                    var mtype = obj.mtype; // 1:窯, 0:磨
                    var img = (obj.mins === 60) ? 1 : (obj.mins >= 54) ? 2 : 3;
                    if (mtype === 1) {
                        if (lastPlant.yauimg < img) {
                            lastPlant.yauimg = img;
                        }
                    } else {
                        if (lastPlant.moimg < img) {
                            lastPlant.moimg = img;
                        }
                    }
                    lastPlant.data.push(obj);
                }
                if (lastPlant.plant !== '') {
                    array.push(lastPlant);
                }
                var lights = ["graylight48", "greenlight48", "yellowlight48", "redlight48"];
                var content = '';
                for (var i = 0; i < array.length; i++) {
                    var obj = array[i];
                    var plant = '<span class="vpplant">' + obj.plant + '</span>';
                    var yauimg = lights[obj.yauimg];
                    var moimg = lights[obj.moimg];
                    var yau = '<span class="vpyaumo">窯: </span><span class="' + yauimg + '"/>';
                    var mo = '<span class="vpyaumo">磨: </span><span class="' + moimg + '"/>';
                    content += '<div data-role="collapsible"><h3>' + plant + yau + mo + '</h3>';
                    var listview = '<ul data-role="listview" data-divider-theme="e">';
                    listview += _this.yaumo(obj.data, 1, '窯');
                    listview += _this.yaumo(obj.data, 0, '磨');
                    listview += '</ul>';
                    content += listview + '</div>';
                }
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').trigger('create');
            });
        },
        yaumo: function(list, mtype, divider) {
            var listview = '<li data-role="list-divider">' + divider + '</li>';
            for (var j = 0; j < list.length; j++) {
                var p = list[j];
                if (p.mtype !== mtype) {
                    continue;
                }
                var lightCss = 'greenlight32';
                if (p.mins >= 60) {
                } else if (p.mins >= 54) {
                    lightCss = 'yellowlight32';
                } else {
                    lightCss = 'redlight32';
                }
                var machine = '<span class="vpmachine">' + p.machine + ':</span>';
                var mins = '<span class="vpmins">' + p.mins + '</span> 分鐘';
                listview += '<li>';
                listview += '<span class="' + lightCss + '"/>';
                listview += machine + mins;
                listview += '</li>';
            }
            return listview;
        }
    };

    window.WidgetVpphour = new WidgetVpphour();

})();
