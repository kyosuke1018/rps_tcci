/* lms: 證照預警 */

(function() {

    var WidgetLms = function() {
    };

    WidgetLms.prototype = {
        title: '證照預警',
        choiceSelected: '', // 選到的事業別

        // route handler:
        route: function(params) {
            var _this = this;
            if (params.factory && params.department) {
                _this.loadPage2(params);
            } else {
                _this.loadPage1();
            }
        },
        // 顯示第一層
        loadPage1: function() {
            var _this = this;
            var view = new WidgetView({pageId: '#pgLms'});
            app.changePage(view);
            view.$("#choice").bind("change", function(event, ui) {
                _this.choiceSelected = view.$('#choice').val();
                _this.choiceChange(view);
            });
            var service = '/lms/service/lmsREST/sso/findLevel1';
            executeService(service, function(data) {
                view.$('#syncTime').html(date2YMDHMS(new Date(data.syncTime)));
                var list = data.licenseList || [];
                var groupByComp = _.groupBy(list, function(obj) {
                    return obj.companyCode + "_" + obj.companyName;
                });
                _this.buildChoice(view, groupByComp);
            });
        },
        // 顯示第二層
        loadPage2: function(params) {
            var _this = this;
            var view = new WidgetView({pageId: '#pgLms2'});
            app.changePage(view);
            var service = '/lms/service/lmsREST/sso/findLevel2?factory=' + params.factory + '&department=' + params.department;
            executeService(service, function(data) {
                view.$('#syncTime').html(date2YMDHMS(new Date(data.syncTime)));
                var list = data.licenseList || [];
                var content = '';
                var path = '';
                $.each(list, function(index, obj) {
                    if (path === '') {
                        path = obj.factoryName + ' ' + obj.licenseRespDepName;
                    }
                    content += '<li data-theme="a"><span class="plantName">' + obj.licenseName + '</span></li>';
                    content += '<table>';
                    content += '<tr><td>證照號碼:</td><td>' + obj.licenseCode + '</td></tr>';
                    content += '<tr><td>發照單位:</td><td>' + obj.licenseUnit + '</td></tr>';
                    content += '<tr><td>到期日:</td><td>' + obj.licenseEndDate + '</td></tr>';
                    content += '<tr><td>展期作業天數:</td><td>' + obj.licensePrepareDays + '天</td></tr>';
                    content += '<tr><td>狀態:</td><td>' + obj.licenseStatus + '</td></tr>';
                    var light1 = _this.getLightCss(obj.licenseLight);
                    var light2 = _this.getLightCss(obj.licenseChargePersonLight);
                    content += '<tr><td colspan="2">到期預警:<span class="' + light1 + '"/>，負責人:<span class="' + light2 + '"/>';
                    content += '</td></tr>';
                    content += '</table>';
                });
                view.$('#path').html(path);
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        buildChoice: function(view, groupByComp) {
            var _this = this;
            var datalist = '';
            var choiceHtml = '';
            if (_this.choiceSelected === '') {
                $.each(groupByComp, function(key, vo) {
                    var codeName = key.split('_');
                    if (codeName[1] === app.tag) {
                        _this.choiceSelected = codeName[0];
                        return false; // break each
                    }
                });
            }
            $.each(groupByComp, function(key, vo) {
                var codeName = key.split('_');
                if (_this.choiceSelected === '') {
                    _this.choiceSelected = codeName[0];
                }
                var selected = _this.choiceSelected === codeName[0] ? ' selected ' : '';
                choiceHtml += '<option value="' + codeName[0] + '"' + selected + '>' + codeName[1] + '</option>';
                $.each(vo, function(index, obj) {
                    datalist += _this.page1_row(obj);
                });
            });
            view.$('#choice').html(choiceHtml);
            view.$('#choice').selectmenu('refresh');
            view.$('#datalist').html(noContent(datalist));
            view.$('#datalist').listview('refresh');
            _this.choiceChange(view);
        },
        choiceChange: function(view) {
            var _this = this;
            view.$('#datalist li').hide();
            view.$('#datalist table').hide();
            view.$('#datalist li.comp' + _this.choiceSelected).show();
            view.$('#datalist table.comp' + _this.choiceSelected).show();
        },
        getLightCss: function(light) {
            return  0 === light ? "greenlight32" :
                    1 === light ? "yellowlight32" :
                    2 === light ? "yellowworklight32" :
                    3 === light ? "yellowalertlight32" :
                    4 === light ? "redlight32" :
                    5 === light ? "redalertlight32" : "graylight32";
        },
        page1_row: function(obj) {
            var _this = this;
            var deptLight = new Array();
            $.each(obj.licenseDepLight, function(index, obj) {
                deptLight[obj.depCode] = obj.licenseLight;
            });
            var content = '';
            var compClass = 'comp' + obj.companyCode;
            content += '<li data-theme="a" class="' + compClass + '"><span class="plantName">' + obj.factoryName + '</span></li>';
            content += '<table class="' + compClass + '" width="100%"><tr>';
            content += '<td align="center">' + _this.page1_row_light(obj.factoryCode, 'PM', deptLight['PM']) + '<br/>工務</td>';
            content += '<td align="center">' + _this.page1_row_light(obj.factoryCode, 'FI', deptLight['FI']) + '<br/>財務</td>';
            content += '<td align="center">' + _this.page1_row_light(obj.factoryCode, 'GA', deptLight['GA']) + '<br/>總務</td>';
            content += '<td align="center">' + _this.page1_row_light(obj.factoryCode, 'SD', deptLight['SD']) + '<br/>業務</td>';
            content += '<td align="center">' + _this.page1_row_light(obj.factoryCode, 'HR', deptLight['HR']) + '<br/>人資</td>';
            content += '</tr></table>';
            return content;
        },
        page1_row_light: function(factory, department, light) {
            var _this = this;
            var lightCss = _this.getLightCss(light);
            var lightSpan = '<span class="' + lightCss + '"/>';
            if (0 !== light && -1 !== light) {
                var link = buildLink('#lms', {factory: factory, department: department});
                lightSpan = '<a href="' + link + '">' + lightSpan + '</a>';
            }
            return lightSpan;
        }
    };

    window.WidgetLms = new WidgetLms();

})();
