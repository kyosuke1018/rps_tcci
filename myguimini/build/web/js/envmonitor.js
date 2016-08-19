/* envmonitor: 環保資料預警 */

(function() {

    var WidgetEnvmonitor = function() {
    };

    WidgetEnvmonitor.prototype = {
        title: '環保資料預警',
        page: 1,
        date: '', // yyyymmdd
        plant: '',
        line: '',
        tag: '',
        time: '',
        plantNm: '',
        tagNm: '',
        hideGreen: false,
        // route handler:
        route: function(params) {
            var _this = this;
            if (_this.date === '') {
                _this.date = date2YMD(new Date());
            }
            if (params.plant && params.line && params.tag && params.time) {
                _this.plant = params.plant;
                _this.line = params.line;
                _this.tag = params.tag;
                _this.time = params.time;
                _this.page = 4;
            } else if (params.plant && params.line && params.tag) {
                _this.plant = params.plant;
                _this.line = params.line;
                _this.tag = params.tag;
                _this.page = 3;
            } else if (params.plant && params.line) {
                _this.plant = params.plant;
                _this.line = params.line;
                _this.page = 2;
            } else if (params.plant) {
                _this.plant = params.plant;
                _this.page = 1;
            } else {
                _this.page = 0;
            }
            var view = new WidgetView({pageId: '#pgEnvmonitor'});
            app.changePage(view);
            view.$('#dataDate').val(formatYMD(_this.date, '-'));
            view.$('#dataDate').change(function() {
                if (view.$('#dataDate').val() !== '') {
                    _this.date = view.$('#dataDate').val().replace(/-/g, '');
                    if (_this.page !== 0) {
                        _this.page = 0;
                        app.navigate('pgEnvmonitor', {trigger: true});
                    } else {
                        _this.loadPage(view);
                    }
                }
            });
            view.$('#chkHideGreen').click(function() {
                _this.hideGreen = view.$('#chkHideGreen').is(':checked');
                _this.toggleLight(view);
            });
            _this.loadPage(view);
        },
        loadPage: function(view) {
            var _this = this;
            var path = _this.page === 0 ? '' : _this.plantNm;
            if (_this.page > 1) path += ' ' + _this.line;
            if (_this.page > 2) path += ' ' + _this.tagNm;
            view.$('#path').html(path);
            if (_this.page > 2) {
                view.$('#divHideGreen').show();
                if (_this.hideGreen) {
                    view.$('#chkHideGreen').attr("checked", true).checkboxradio("refresh");
                }
            }
            var service = '/tccprod/service/tccProdREST/sso/findL' + _this.page;
            service += '?date=' + _this.date;
            if (_this.page > 0) {
                service += '&plant=' + _this.plant;
            }
            if (_this.page > 1) {
                service += '&line=' + _this.line;
            }
            if (_this.page > 2) {
                service += '&tagCd=' + _this.tag;
            }
            if (_this.page > 3) {
                service += '&time=' + _this.time;
            }
            executeService(service, function(data) {
                var list = data.epsList || [];
                var content = '';
                $.each(list, function(index, obj) {
                    if (_this.page < 3) {
                        content += _this.page_content1(obj);
                    } else {
                        content += _this.page_content2(obj);
                    }
                });
                view.$('#datalist').html(noContent(content));
                _this.toggleLight(view);
                view.$('#datalist').listview('refresh');
                view.$('#datalist li a').click(function() {
                    var item = view.$(this).text();
                    switch (_this.page) {
                        case 0:
                            _this.plantNm = item;
                            break;
                        case 2:
                            _this.tagNm = item;
                            break;
                    }
                });
            });
        },
        page_content1: function(obj) {
            var _this = this;
            var title = _this.page === 0 ? obj.plantNm :
                    _this.page === 1 ? obj.line :
                    _this.page === 2 ? obj.tagNm : '';
            var link = '#envmonitor?plant=';
            link += _this.page === 0 ? obj.plant :
                    _this.page === 1 ? _this.plant + '&line=' + obj.line :
                    _this.page === 2 ? _this.plant + '&line=' + _this.line + '&tag=' + obj.tagCd : '';
            var content = '<li><a href="' + link + '">' + title + '</a></li>';
            content += '<table width="100%"><tr>';
            var lightSpan1 = _this.lightSpan(obj.dayLight, 48);
            var lightSpan2 = _this.lightSpan(obj.light, 48);
            content += '<td width="33%" align="center">' + lightSpan1 + '<br/>當日</td>';
            content += '<td width="33%" align="center">' + lightSpan2 + '<br/>即時</td>';
            content += '<td>紅燈數: ' + obj.redCount + '<br/>黃燈數: ' + obj.yellowCount + '</td>';
            content += '</tr></table>';
            return content;
        },
        page_content2: function(obj) {
            var _this = this;
            var content = '';
            var title = _this.lightSpan(obj.light, 32);
            title += _this.page === 3 ? obj.minTime + ' ~ ' + obj.maxTime : obj.maxTime;
            var light = 0 === obj.light ? 'green' :
                    1 === obj.light ? 'yellow' :
                    2 === obj.light ? 'red' : 'gray';
            if (_this.page === 3) {
                var linkParams = {plant: _this.plant, line: _this.line, tag: _this.tag, time: obj.maxTime};
                var link = buildLink('#envmonitor', linkParams);
                content += '<li class="' + light + '"><a href="' + link + '">' + title + '</a></li>';
            } else {
                content += '<li class="' + light + '">' + title + '</li>';
            }
            return content;
        },
        toggleLight: function(view) {
            var _this = this;
            if (_this.hideGreen) {
                view.$('li.green').hide();
            } else {
                view.$('li.green').show();
            }
        },
        lightSpan: function(light, size) {
            var lightCss = 0 === light ? 'green' :
                    1 === light ? 'yellow' :
                    2 === light ? 'red' : 'gray';
            return '<span class="' + lightCss + 'light' + size + '"/>';
        }
    };

    window.WidgetEnvmonitor = new WidgetEnvmonitor();

})();
