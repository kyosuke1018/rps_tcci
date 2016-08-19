/* icsmonitor: 內控預警 */

(function() {

    var WidgetIcsmonitor = function() {
    };

    WidgetIcsmonitor.prototype = {
        title: '內控預警',
        date: '', // yyyymmdd
        plant: '',
        rulename: '',
        type: 'Rule',
        module: 'FI',
        compSel: '',
        // route handler:
        route: function(params) {
            var _this = this;
            if (_this.date === '') {
                _this.date = date2YMD(new Date());
            }
            if (params.plant && params.rulecode) {
                var view = new WidgetView({pageId: '#pgIcsmonitor3'});
                app.changePage(view);
                view.$('#date').html(formatYMD(_this.date, '/'));
                view.$('#factory').html(_this.plant + ', ' + _this.rulename);
                if (_this.type === 'Rule') {
                    view.$('#type').html('異常預警');
                } else if (_this.type === 'Feedback') {
                    view.$('#type').html('回饋率');
                } else if (_this.type === 'Close') {
                    view.$('#type').html('結案率');
                }
                _this.loadPage3(view, params.plant, params.rulecode, params.lightId);
            } else if (params.plant) {
                var view = new WidgetView({pageId: '#pgIcsmonitor2'});
                app.changePage(view);
                view.$('#dataDate').val(formatYMD(_this.date, '-'));
                view.$('#type').val(_this.type);
                view.$('#type').selectmenu('refresh');
                view.$('#plant').html(_this.plant);
                $('#module_' + _this.module).attr("checked", true).checkboxradio("refresh");
                _this.page2_moduleChange(view);
                view.$('#dataDate').change(function() {
                    if (view.$('#dataDate').val() !== '') {
                        _this.date = view.$('#dataDate').val().replace(/-/g, '');
                        _this.loadPage2(view, params.plant);
                    }
                });
                view.$('#type').change(function() {
                    _this.type = view.$('#type').val();
                    _this.loadPage2(view, params.plant);
                });
                view.$("input[name=module]").bind("change", function(event, ui) {
                    _this.module = view.$('input[name=module]:checked').val();
                    _this.page2_moduleChange(view);
                });
                _this.loadPage2(view, params.plant);
            } else {
                var view = new WidgetView({pageId: '#pgIcsmonitor'});
                app.changePage(view);
                view.$('#dataDate').val(formatYMD(_this.date, '-'));
                $('#type_' + _this.type).attr("checked", true).checkboxradio("refresh");
                view.$('#dataDate').change(function() {
                    if (view.$('#dataDate').val() !== '') {
                        _this.date = view.$('#dataDate').val().replace(/-/g, '');
                        _this.loadPage1(view);
                    }
                });
                view.$("input[name=type]").bind("change", function(event, ui) {
                    _this.type = view.$('input[name=type]:checked').val();
                    _this.page1_typeChange(view);
                });
                _this.loadPage1(view);
                _this.page1_typeChange(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var service = '/ics/service/icsAlertREST/sso/findalllights?date=' + _this.date;
            executeService(service, function(list) {
                var companys = {};
                var compList = [];
                var content = '';
                var codeFirst = '';
                var compSelExist = false; 
                var codeTag = '';
                $.each(list, function(index, obj) {
                    $.each(obj.factoryGroupLightList, function(index2, factory) {
                        var clazz = obj.company.code + '_' + obj.type;
                        content += '<li data-theme="a" class="' + clazz + '"><span class="plantName">' + factory.groupName + '</span></li>';
                        content += _this.page1_factory(factory, clazz);
                    });
                    var compCode = obj.company.code;
                    var compName = obj.company.name;
                    if (codeFirst === '') {
                        codeFirst = compCode;
                    }
                    if (_this.compSel === compCode) {
                        compSelExist = true;
                    }
                    if (app.tag === compName) {
                        codeTag = compCode;
                    }
                    if (!(compCode in companys)) {
                        companys[compCode] = compName;
                        compList.push(obj.company);
                    }
                });
                if (!compSelExist) {
                    _this.compSel = (codeTag === '') ? codeFirst : codeTag;
                }
                _this.buildCompanyTabs(view, compList);
                view.$('#datalist').html(content);
                view.$('#datalist').listview('refresh');
                view.$('ul a.light').click(function() {
                    _this.plant = $(this).attr('data-plant');
                    _this.module = $(this).attr('data-module');
                    return true;
                });
                view.$('#datalist > *').hide();
                view.$('#datalist .' + _this.compSel + '_' + _this.type).show();
            });
        },
        loadPage2: function(view, factory) {
            var _this = this;
            var linkParams = {date: _this.date, plant: factory, type: _this.type, company: _this.compSel};
            var service = buildLink('/ics/service/icsAlertREST/sso/findplantview', linkParams);
            executeService(service, function(data) {
                var list = data.ruleLightVOList || [];
                var map = {};
                $.each(list, function(index, obj) {
                    var content = '';
                    if (obj.moduleCode in map) {
                        content = map[obj.moduleCode];
                    }
                    if (obj.hasDetail) {
                        var linkParams = {plant: factory, rulecode: obj.ruleCode, lightId: obj.id};
                        var link = buildLink('#icsmonitor', linkParams);
                        content += '<li><a href="' + link + '" data-rulename="' + obj.titleName + '">' + obj.ruleCode + '</a></li>';
                    } else {
                        content += '<li data-theme="a"><span class="plantName">' + obj.ruleCode + '</span></li>';
                    }
                    content += _this.page2_module(obj);
                    map[obj.moduleCode] = content;
                });
                view.$('#datalist_FI').html(noContent(map['FI']));
                view.$('#datalist_SD').html(noContent(map['SD']));
                view.$('#datalist_MM').html(noContent(map['MM']));
                view.$('#datalist_PM').html(noContent(map['PM']));
                view.$('ul').listview('refresh');
                view.$('ul li a').click(function() {
                    _this.rulename = $(this).attr('data-rulename');
                });
                view.$('span.toggle').click(function() {
                    $(this).parents('table').find('tr:last span').toggle();
                });
            });
        },
        loadPage3: function(view, plant, rulecode, lightId) {
            var _this = this;
            var linkParams = {date: _this.date, plant: plant, rule: rulecode, light_id: lightId, type: _this.type, company: _this.compSel};
            var service = buildLink('/ics/service/icsAlertREST/sso/finddetail', linkParams);
            executeService(service, function(data) {
                if (data.hasLight) {
                    view.$('#lightDesc').html(data.lightDesc);
                }
                var list = data.dataList || [];
                var content = '';
                $.each(list, function(index, obj) {
                    var lightSpan = '';
                    if (data.lightDesc) {
                        lightSpan = _this.lightSpan(obj.colorCode, '');
                        if (lightSpan.indexOf('gray') > -1) {
                            lightSpan = '';
                        }
                    }
                    content += '<li><a href="#"><table><td>' + lightSpan + '</td><td>' + obj.subject + '</td></tr></table></a></li>';
                    content += '<div class="ui-body ui-body-e" style="display:none; white-space: pre-wrap; cursor:pointer;">';
                    content += obj.description + '\n(再按一次返回)';
                    content += '</div>';
                });
                if (content !== '') {
                    var typeDesc = '';
                    if (_this.type === 'Rule') {
                        typeDesc = '異常事件';
                    } else if (_this.type === 'Feedback') {
                        typeDesc = '未回饋';
                    } else if (_this.type === 'Close') {
                        typeDesc = '未結案';
                    }
                    content = '<li data-theme="a"><span class="plantName">' + typeDesc + '</span></li>' + content;
                }
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#datalist li a').click(function() {
                    var li = $(this).parent().parent().parent();
                    view.$('#datalist > *').hide();
                    li.next().show();
                    return false;
                });
                view.$('#datalist div.ui-body').click(function() {
                    $(this).hide();
                    view.$('#datalist > li').show();
                    return false;
                });
            });
        },
        page1_typeChange: function(view) {
            var _this = this;
            view.$('#datalist > *').hide();
            view.$('#datalist .' + _this.compSel + '_' + _this.type).show();
        },
        page1_compChange: function(view) {
            var _this = this;
            view.$('#datalist > *').hide();
            view.$('#datalist .' + _this.compSel + '_' + _this.type).show();
        },
        page1_factory: function(factory, clazz) {
            var _this = this;
            var html = '<table width="100%" class="' + clazz + '"><tr>';
            $.each(factory.moduleLights, function(index, obj) {
                var link = buildLink('#icsmonitor', {plant: factory.groupCode});
                html += _this.page1_cell(obj, link, factory.groupName);
            });
            html += '</tr></table>';
            return html;
        },
        page1_cell: function(obj, link, plant) {
            var _this = this;
            var html = '<td align="center" valign="top">';
            html += obj.moduleName + '<br/>';
            var lightSpan = _this.lightSpan(obj.colorCode, '');
            if (lightSpan.indexOf("gray") > -1) {
                html += lightSpan;
            } else {
                html += '<a href="' + link + '" data-plant="' + plant + '" data-module="' + obj.moduleCode + '" class="light">' + lightSpan + '</a></td>';
            }
            return html;
        },
        page2_module: function(obj) {
            var _this = this;
            var html = '<table width="100%" class="module_' + obj.moduleCode + '">';
            var lightSpan = _this.lightSpan(obj.colorCode, 'toggle');
            html += '<tr><td rowspan="3" align="center" width="1%">';
            html += lightSpan + '</td>';
            html += '<td colspan="2"><span class="plantName">' + obj.titleName + '</span></td></tr>';
            html += '<tr><td colspan="2"><span style="white-space: pre-wrap;">' + obj.countDesc + '<span></td></tr>';
            html += '<tr><td colspan="2">更新時間: ' + date2YMDHMS(new Date(obj.createTimeStamp)) + '</td></tr>';
            html += '<tr><td colspan="3"><span class="ui-body ui-body-e" style="display:none; white-space: pre-wrap;">' + obj.lightDesc + '</span></td></tr>';
            html += '</table>';
            return html;
        },
        page2_moduleChange: function(view) {
            var _this = this;
            view.$('ul').hide();
            view.$('#datalist_' + _this.module).show();
        },
        buildCompanyTabs: function(view, compList) {
            var _this = this;
            var content = '';
            $.each(compList, function(index, obj) {
                var code = obj.code;
                var name = obj.name;
                var selected = (code === _this.compSel) ? 'selected' : '';
                content += '<option value="' + code + '" ' + selected + '>' + name + '</option>';
            });
            view.$('#companys').html(content);
            view.$('#companys').selectmenu('refresh');
            view.$("#companys").bind("change", function(event, ui) {
                _this.compSel = view.$('#companys').val();
                _this.companyChange(view);
            });
        },
        companyChange: function(view) {
            var _this = this;
            view.$('#datalist > *').hide();
            var show = '#datalist .' + _this.compSel + '_' + _this.type;
            view.$(show).show();
        },
        lightSpan: function(colorCode, extraClass) {
            var lightCss = /0|G/.test(colorCode) ? 'greenlight48 ' :
                    /1|Y/.test(colorCode) ? 'yellowlight48 ' :
                    /2|R/.test(colorCode) ? 'redlight48 ' : 'graylight48 ';
            return '<span class="' + lightCss + extraClass + '"/>';
        }
    };

    window.WidgetIcsmonitor = new WidgetIcsmonitor();

})();
