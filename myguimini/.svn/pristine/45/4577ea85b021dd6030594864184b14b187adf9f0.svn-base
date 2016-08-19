/* csrcprod: 中橡生產資訊 */

(function() {

    var WidgetCsrcprod = function() {
    };

    WidgetCsrcprod.prototype = {
        title: '中橡生產資訊',
        date: '', // yyyymmdd
        area: '', // RA(反應區), CA(收集區), WA(造粒區), NA(未定義)

        // route handler:
        route: function(params) {
            var _this = this;
            if (_this.date === '') {
                _this.date = date2YMD(new Date());
            }
            if (params.plant && params.area && params.line && params.reactor && params.tagCd) {
                var view = new WidgetView({pageId: '#pgCsrcprodL4'});
                app.changePage(view);
                view.$('#dt').html(formatYMD(_this.date, "-"));
                _this.loadPage4(view, params);
            } else if (params.plant && params.area && params.line && params.reactor) {
                var view = new WidgetView({pageId: '#pgCsrcprodL3'});
                app.changePage(view);
                view.$('#dt').html(formatYMD(_this.date, "-"));
                _this.loadPage3(view, params);
            } else if (params.plant && params.area) {
                var view = new WidgetView({pageId: '#pgCsrcprodL2'});
                app.changePage(view);
                if (_this.area === '') { // 進下一層又回來時，保留當時area
                    _this.area = params.area;
                }
                view.$('#area-' + _this.area).attr("checked", true).checkboxradio("refresh");
                view.$("input[name=area]").bind("change", function(event, ui) {
                    _this.area = view.$('input[name=area]:checked').val();
                    _this.tabChange(view);
                });
                view.$('#dt').html(formatYMD(_this.date, "-"));
                _this.loadPage2(view, params);
            } else {
                var view = new WidgetView({pageId: '#pgCsrcprod'});
                app.changePage(view);
                view.$('#dataDate').val(formatYMD(_this.date, '-'));
                view.$('#dataDate').change(function() {
                    if (view.$('#dataDate').val() !== '') {
                        _this.date = view.$('#dataDate').val().replace(/-/g, '');
                        _this.loadPage1(view);
                    }
                });
                _this.area = '';
                _this.loadPage1(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var serviceParams = {'date': _this.date};
            var service = buildLink('/csrcprod/service/csrcProdREST/sso/findL1', serviceParams);
            executeService(service, function(data) {
                var syncTime = date2YMDHMS(new Date(data.syncTime));
                view.$('#syncTime').html(syncTime);
                var datalist = data.dcsList || [];
                var content = '';
                $.each(datalist, function(index, obj) {
                    var linkParams = {plant: obj.plant, area: obj.area};
                    var link = buildLink('#csrcprod', linkParams);
                    content += '<li><a href="' + link + '">' + obj.plantNm + "-" + obj.areaNm + '</a></li>';
                    content += '<table width="100%"><tr>';
                    content += '<td align="center"><span class="' + _this.getLightCss(obj.light) + '"/><br/>即時燈號</td>';
                    content += '<td align="center"><span class="' + _this.getLightCss(obj.dayLight) + '"/><br/>今日燈號</td>';
                    content += '</tr>';
                    content += '</table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        loadPage2: function(view, params) {
            var _this = this;
            var serviceParams = {'date': _this.date, 'plant': params.plant};
            var service = buildLink('/csrcprod/service/csrcProdREST/sso/findL2', serviceParams);
            executeService(service, function(data) {
                var syncTime = date2YMDHMS(new Date(data.syncTime));
                view.$('#syncTime').html(syncTime);
                var datalist = data.dcsList || [];
                var plantList = _.groupBy(datalist, function(obj) {
                    return obj.area;
                });
                var path = '';
                $.each(plantList, function(key, datas) {
                    var content = '';
                    $.each(datas, function(index, obj) {
                        if (path === '') {
                            path = obj.plantNm;
                        }
                        var linkParams = {plant: obj.plant, area: obj.area, line: obj.line, reactor: obj.reactor};
                        var link = buildLink('#csrcprod', linkParams);
                        content += '<li name=ROW_' + key + '><a href="' + link + '">產線:' + obj.line + "-反應器:" + obj.reactor + '</a></li>';
                        content += '<table width="100%"><tr>';
                        content += '<td align="center"><span class="' + _this.getLightCss(obj.light) + '"/><br/>即時燈號</td>';
                        content += '<td align="center"><span class="' + _this.getLightCss(obj.dayLight) + '"/><br/>今日燈號</td>';
                        content += '</tr>';
                        content += '</table>';
                    });
                    view.$('#datalist-' + key).html(noContent(content));
                    view.$('#datalist-' + key).listview('refresh');
                });
                view.$('#path').html(path);
                _this.tabChange(view);
            });
        },
        loadPage3: function(view, params) {
            var _this = this;
            var serviceParams = {'date': _this.date, 'plant': params.plant, 'area': params.area, 'line': params.line, 'reactor': params.reactor};
            var service = buildLink('/csrcprod/service/csrcProdREST/sso/findL3', serviceParams);
            executeService(service, function(data) {
                view.$('#syncTime').html(date2YMDHMS(new Date(data.syncTime)));
                var datalist = data.dcsList || [];
                var plantList = _.groupBy(datalist, function(obj) {
                    return obj.plant + "_" + obj.area + "_" + obj.line + "_" + obj.reactor + "_" + obj.tagCd;
                });
                var path = '';
                var content = '';
                $.each(plantList, function(key, datas) {
                    $.each(datas, function(index, obj) {
                        path = path || (obj.plantNm + '-' + obj.areaNm + '/' + obj.line + '-' + obj.reactor);
                        if (path === '') {
                            path = obj.plantNm + '-' + obj.areaNm + '/' + obj.line + '-' + obj.reactor;
                        }
                        var linkParams = {plant: obj.plant, area: obj.area, line: obj.line, reactor: obj.reactor, tagCd: obj.tagCd};
                        var link = buildLink('#csrcprod', linkParams);
                        content += '<li name=ROW_' + key + '><a href="' + link + '">' + obj.tagNm + ' (' + obj.tagCd + ')</a></li>';
                        content += '<table><tr>';
                        content += '<td rowspan="3" align="center"><span class="' + _this.getLightCss(obj.light) + '"/></td>';
                        content += '<td class="label">實際值</td><td colspan="2" align="right">' + obj.value + " " + obj.unit + '</td></tr>';
                        content += '<tr><td class="label">範圍值</td><td class="value">' + obj.minValue + ' ~</td><td class="value">' + obj.maxValue + '</td></tr>';
                        content += '<tr><td class="label">上下限值</td><td class="value">' + _this.processValue(obj.lowerValue) + ' ~</td><td class="value">' + _this.processValue(obj.upperValue) + '</td></tr>';
                        content += '</table>';
                    });
                });
                view.$('#path').html(path);
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        loadPage4: function(view, params) {
            var _this = this;
            var serviceParams = {'date': _this.date, 'plant': params.plant, 'area': params.area, 'line': params.line, 'reactor': params.reactor, 'tagCd': params.tagCd};
            var service = buildLink('/csrcprod/service/csrcProdREST/sso/findL4', serviceParams);
            executeService(service, function(data) {
                var datalist = data.dcsList || [];
                var plantList = _.groupBy(datalist, function(obj) {
                    return obj.plant + "_" + obj.area + "_" + obj.time;
                });
                view.$('#syncTime').html(date2YMDHMS(new Date(data.syncTime)));
                var path;
                var content = '';
                $.each(plantList, function(key, datas) {
                    $.each(datas, function(index, obj) {
                        path = path || (obj.plantNm + '-' + obj.areaNm + '/' + obj.line + '-' + obj.reactor + '/' + obj.tagNm + '(' + obj.tagCd + ')');
                        content += '<li data-theme="a" name=ROW_' + key + '>' + obj.time + ':00</li>';
                        content += '<table><tr>';
                        content += '<td rowspan="3" align="center"><span class="' + _this.getLightCss(obj.light) + '" /></td>';
                        content += '<td class="label">實際值</td><td colspan="2" align="right">' + obj.value + " " + obj.unit + '</td></tr>';
                        content += '<tr><td class="label">範圍值</td><td class="value">' + obj.minValue + ' ~</td><td class="value">' + obj.maxValue + '</td></tr>';
                        content += '<tr><td class="label">上下限值</td><td class="value">' + _this.processValue(obj.lowerValue) + ' ~</td><td class="value">' + _this.processValue(obj.upperValue) + '</td></tr>';
                        content += '</table>';
                    });
                });
                view.$('#path').html(path);
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        tabChange: function(view) {
            var _this = this;
            view.$('ul').hide();
            view.$('#datalist-' + _this.area).show();
        },
        getLightCss: function(light) {
            var num = parseInt(light);
            return 0 === num ? 'greenlight48' :
                    1 === num ? 'yellowlight48' :
                    2 === num ? 'redlight48' : 'graylight48';
        },
        processValue: function(val) {
            return typeof val === 'undefined' ? '-' : val;
        }
    };

    window.WidgetCsrcprod = new WidgetCsrcprod();

})();
