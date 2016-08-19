/* pdapatrol: PDA巡檢資訊 */

(function() {

    var WidgetPdapatrol = function() {
    };

    WidgetPdapatrol.prototype = {
        title: 'PDA巡檢資訊',
        date: '',
        plant: '',
        parentDept: '',
        dept: '',
        areashift: '',
        comId: '',
        // route handler:
        route: function(params) {
            var _this = this;
            if (params.plant && params.parentDept && params.dept && params.shiftId && params.areaId) {
                var view = new WidgetView({pageId: '#pgPdapatrol4'});
                app.changePage(view);
                view.$('#date').html(formatYMD(_this.date, "-"));
                view.$("#plant").html(_this.plant);
                view.$("#parentDept").html(_this.parentDept);
                view.$("#dept").html(_this.dept);
                view.$("#areashift").html(_this.areashift);
                _this.loadPage4(view, params);
            } else if (params.plant && params.parentDept && params.dept) {
                var view = new WidgetView({pageId: '#pgPdapatrol3'});
                app.changePage(view);
                view.$('#date').html(formatYMD(_this.date, "-"));
                view.$("#plant").html(_this.plant);
                view.$("#parentDept").html(_this.parentDept);
                view.$("#dept").html(_this.dept);
                _this.loadPage3(view, params);
            } else if (params.plant && params.parentDept) {
                var view = new WidgetView({pageId: '#pgPdapatrol2'});
                app.changePage(view);
                view.$('#date').html(formatYMD(_this.date, "-"));
                view.$("#plant").html(_this.plant);
                view.$("#parentDept").html(_this.parentDept);
                _this.loadPage2(view, params);
            } else {
                if (_this.date === '') {
                    _this.date = yesterday();
                }
                var view = new WidgetView({pageId: '#pgPdapatrol'});
                app.changePage(view);
                view.$('#dataDate').val(formatYMD(_this.date, '-'));
                view.$('#dataDate').change(function() {
                    if (view.$('#dataDate').val() !== '') {
                        _this.date = view.$('#dataDate').val().replace(/-/g, '');
                        _this.loadPage1(view);
                    }
                });
                _this.loadPage1(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var service = '/dashboard/service/pdapatrolsum/v3/date/' + _this.date;
            executeService(service, function(datalist) {
                // groupBy公司
                var companys = _.groupBy(datalist, function(obj) {
                    return obj.comId + '_' + obj.comName;
                });
                var found = false; // 如果_this.comId不在資料裡, 改選第一家公司
                var firstId = '';
                var content = '';
                $.each(companys, function(key, plantList) {
                    var codeName = key.split('_');
                    if (_this.comId === codeName[0]) {
                        found = true;
                    } else if (_this.comId === '' && codeName[1] === app.tag) {
                        _this.comId = codeName[0];
                        found = true;
                    }
                    firstId = firstId === '' ? codeName[0] : firstId;
                    content += _this.page1_row(codeName[0], codeName[1], plantList);
                });
                if (!found) {
                    _this.comId = firstId;
                }
                // 建立公司tabs
                _this.buildCompanyTabs(view, companys);
                // 更新 datalist
                if ('' === content) {
                    view.$('#datalist').html('<h3 style="text-align:center;">目前無資料，系統將於每日上午9點及12點重新計算昨日資料。</h3>');
                } else {
                    view.$('#datalist').html(content);
                }
                view.$('#datalist').trigger('create');
                // 切換成選到的公司
                _this.companyChange(view);
                // 儲存點到的廠, 分廠名稱
                view.$('#datalist li a').click(function() {
                    _this.plant = $(this).attr('data-plant');
                    _this.parentDept = $(this).attr('data-parentDept');
                    return true;
                });
            });
        },
        loadPage2: function(view, params) {
            var _this = this;
            var service = '/dashboard/service/pdapatrolsum/v3/date/' + _this.date + '/' + params.plant + '/' + params.parentDept;
            executeService(service, function(datalist) {
                var content = '';
                $.each(datalist, function(index, obj) {
                    var linkParams = {plant: params.plant, parentDept: params.parentDept, dept: obj.deptId};
                    var link = buildLink('#pdapatrol', linkParams);
                    content += '<li><a href="' + link + '">' + obj.deptName + '</a></li>';
                    content += '<table><tr>';
                    content += '<td>' + _this.lightSpan(obj.flag, 64) + '</td>';
                    var areaAlert = obj.areaCntNordl > 0 ? 'alertRed' : '';
                    content += '<td>未完成路線(未巡): ' + (obj.areaCntNordl + obj.areaCntRdl) + ' (<span class="' + areaAlert + '">' + obj.areaCntNordl + '</span>)</br>';
                    content += '巡檢點數: ' + obj.patrolCnt + '<br/>';
                    content += '未完成巡檢點: ' + (obj.patrolCntNordl + obj.patrolCntRdl) + '</br>';
                    var objAlert = obj.notFeasibleCnt > 0 ? 'alertRed' : '';
                    content += '<span class="' + objAlert + '">巡檢時間異常路線: ' + obj.notFeasibleCnt + '</span></td>';
                    content += '</tr></table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#datalist li a').click(function() {
                    _this.dept = $(this).text();
                    return true;
                });
            });
        },
        loadPage3: function(view, params) {
            var _this = this;
            var service = '/dashboard/service/pdapatrolsum/v3/date/' + _this.date + '/' + params.plant + '/' + params.parentDept + '/' + params.dept;
            executeService(service, function(datalist) {
                var content = '';
                $.each(datalist, function(index, obj) {
                    var linkParams = {plant: params.plant, parentDept: params.parentDept, dept: params.dept, shiftId: obj.shiftId, areaId: obj.areaId};
                    var link = buildLink('#pdapatrol', linkParams);
                    content += '<li><a href="' + link + '">' + obj.areaName + ' (' + obj.shift + '班)</a></li>';
                    content += '<table><tr>';
                    content += '<td>' + _this.lightSpan(obj.flag, 64) + '</td>';
                    content += '<td>原因: ' + obj.reason + '<br/>';
                    content += '巡檢員: ' + obj.empName + '<br/>';
                    content += '實際巡檢時間: ' + _this.convertTime(obj.missionTotalTime);
                    if (obj.notFeasibleCnt > 0) {
                        content += '<br/><span class="alertRed">巡檢時間異常</span>';
                    }
                    content += '</td>';
                    content += '</tr></table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#datalist li a').click(function() {
                    _this.areashift = $(this).text();
                    return true;
                });
            });
        },
        loadPage4: function(view, params) {
            var _this = this;
            var service = '/dashboard/service/pdapatrolsum/v3/date/' + _this.date + '/' + params.plant + '/' + params.parentDept + '/' + params.dept + '/' + params.shiftId + '/' + params.areaId;
            executeService(service, function(datalist) {
                var content = '';
                $.each(datalist, function(index, obj) {
                    content += '<li>' + _this.lightSpan(obj.flag, 32) + obj.eqpName + '</li>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        page1_row: function(comId, comName, plantList) {
            var _this = this;
            // groupBy 廠
            var list = _.groupBy(plantList, function(obj) {
                return obj.plantId + '_' + obj.plantName;  // 2011_英德
            });
            var content = '';
            $.each(list, function(key, plant) {
                var codeName = key.split('_');
                var header = {flag: 1, areaCnt: 0, areaCntNordl: 0, areaCntRdl: 0, notFeasibleCnt: 0}; // 燈號, 班-路線數, 未巡, 有理由, 巡檢時間異常
                var listview = '<ul data-role="listview">';
                $.each(plant, function(index, row) {
                    if (header.flag < row.flag) {
                        header.flag = row.flag;
                    }
                    header.areaCnt += row.areaCnt;
                    header.areaCntNordl += row.areaCntNordl;
                    header.areaCntRdl += row.areaCntRdl;
                    header.notFeasibleCnt += row.notFeasibleCnt;
                    var linkParams = {plant: row.plantId, parentDept: row.parentDeptId};
                    var link = buildLink('#pdapatrol', linkParams);
                    listview += '<li data-theme="d"><a href="' + link + '" data-plant="' + codeName[1] + '" data-parentDept="' + row.parentDeptName + '">';
                    listview += _this.page1_row_body(row.parentDeptName, row, 48);
                    listview += '</a></li>';
                });
                listview += '</ul>';
                content += '<div data-role="collapsible" class="plantRow com_' + comId + '">';
                content += '<h3>' + _this.page1_row_body(codeName[1], header, 64) + '</h3>';
                content += listview;
                content += '</div>';
            });
            return content;
        },
        page1_row_body: function(name, row, lightSize) {
            var _this = this;
            var lightSpan = _this.lightSpan(row.flag, lightSize);
            var content = '<table><tr>';
            content += '<td>' + lightSpan + '</td>';
            content += '<td>' + name + ' (班-路線數: ' + row.areaCnt + ')<br/>';
            var areaAlert = row.areaCntNordl > 0 ? 'alertRed' : '';
            content += '未完成路線(未巡): ' + (row.areaCntNordl + row.areaCntRdl) + ' (<span class="' + areaAlert + '">' + row.areaCntNordl + '</span>)<br/>';
            var plantAlert = row.notFeasibleCnt > 0 ? 'alertRed' : '';
            content += '<span class="' + plantAlert + '">巡檢時間異常路線: ' + row.notFeasibleCnt + '</span>';
            content += '</td>';
            content += '</tr></table>';
            return content;
        },
        buildCompanyTabs: function(view, companys) {
            var _this = this;
            var content = '<fieldset data-role="controlgroup" data-type="horizontal">';
            $.each(companys, function(key, plantList) {
                var codeName = key.split('_');
                var id = 'comp_' + codeName[0];
                content += '<input type="radio" name="comp" id="' + id + '" class="comp" value="' + codeName[0] + '"';
                content += codeName[0] === _this.comId ? ' checked="checked"' : '';
                content += '/><label for="' + id + '">' + codeName[1] + '</label>';
            });
            content += '</fieldset>';
            view.$('#itemtabs').html(content);
            view.$('#itemtabs').trigger('create');
            view.$("input:radio.comp").bind("change", function(event, ui) {
                _this.comId = view.$('input[name=comp]:checked').val();
                _this.companyChange(view);
            });
            _this.companyChange(view);
        },
        companyChange: function(view) {
            var _this = this;
            view.$('div.plantRow').hide();
            view.$('div.com_' + _this.comId).show();
        },
        lightSpan: function(light, size) {
            var lightCss = 1 === light ? 'green' :
                    2 === light ? 'yellow' :
                    3 === light ? 'red' : 'gray';
            return '<span class="' + lightCss + 'light' + size + '"/>';
        },
        convertTime: function convertTime(totalSec) {
            if (isNaN(totalSec)) {
                return 'n/a';
            }
            var hours = parseInt(totalSec / 3600) % 24;
            var minutes = parseInt(totalSec / 60) % 60;
            var seconds = totalSec % 60;
            return (hours < 10 ? "0" + hours : hours) + ":"
                    + (minutes < 10 ? "0" + minutes : minutes) + ":"
                    + (seconds < 10 ? "0" + seconds : seconds);
        }
    };

    window.WidgetPdapatrol = new WidgetPdapatrol();

})();
