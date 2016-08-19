/* ppmonitor: 台泥採購預警 */

(function() {

    var WidgetPpmonitor = function() {
    };

    WidgetPpmonitor.prototype = {
        title: '台泥採購預警',
        // route handler:
        route: function(params) {
            var _this = this;
            var view = new WidgetView({pageId: '#pgPpmonitor'});
            app.changePage(view);
            var date = yesterday();
            view.$('#dataDate').val(formatYMD(date, '-'));
            _this.buildFactory(view);
            view.$("input[name=choice]").bind("change", function(event, ui) {
                _this.choiceChange(view);
            });
            view.$('#factory').change(function() {
                _this.loadPage_Matl(view);
            });
            view.$('#dataDate').change(function() {
                if (view.$('#dataDate').val() !== '') {
                    _this.choiceChange(view);
                }
            });
            _this.choiceChange(view);
        },
        buildFactory: function(view) {
            view.$('#factory').parent().hide();
            var service = '/dashboard/service/swlsService/listDashboardFactory/tcc';
            executeService(service, function(data) {
                var list = data.swlsDashboardFactoryDtoList || [];
                var content = '';
                for (var i = 0; i < list.length; i++) {
                    var obj = list[i];
                    if (obj.categoryCode === '1' || obj.categoryCode === '2') {
                        var selected = (i === 0) ? 'selected' : '';
                        content += '<option value="' + obj.factoryCode + '" ' + selected + '>' + obj.factoryName + '</option>';
                    }
                }
                view.$('#factory').html(content);
                view.$('#factory').selectmenu('refresh');
            });
        },
        choiceChange: function(view) {
            var _this = this;
            var choice = view.$('input[name=choice]:checked').val();
            if (choice === 'coal') {
                view.$('#factory').parent().hide();
                _this.loadPage_Coal(view);
            } else {
                view.$('#factory').parent().show();
                _this.loadPage_Matl(view);
            }
        },
        loadPage_Coal: function(view) {
            var _this = this;
            var date = view.$('#dataDate').val().replace(/-/g, '');
            var service = '/dashboard/service/swlsService/listCoalMineInventory/tcc/' + date;
            executeService(service, function(data) {
                var datalist = data.swlsInventoryDtoList || [];
                var content = '';
                $.each(datalist, function(index, obj) {
                    var lightSpan = _this.lightSpan(obj.lightType);
                    content += '<li data-theme="a"><span class="plantName">' + obj.plantName + '</span></li>';
                    content += '<table><tr><td rowspan="2" align="center">' + lightSpan + '</td>';
                    content += '<td>存量: </td><td class="col3">' + obj.stock + '</td><td>噸</td></tr>';
                    content += '<tr><td>安全量: </td><td class="col3">' + obj.stockKpi + '</td><td>噸</td></tr>';
                    content += '</table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#updateTime').html(data.lastSyncTime || 'n/a');
            });
        },
        loadPage_Matl: function(view) {
            var _this = this;
            var date = view.$('#dataDate').val().replace(/-/g, '');
            var factoryCode = view.$('#factory').val();
            var service = '/dashboard/service/swlsService/listAccessoryInventory_v1/tcc/' + date + '/' + factoryCode;
            executeService(service, function(data) {
                var datalist = data.swlsAccessoryInventoryDtoList || [];
                var content = '';
                $.each(datalist, function(index, obj) {
                    if (obj.alertType === 'A') {
                        return; // skip by廠燈號
                    }
                    var lightSpan = _this.lightSpan(obj.lightType);
                    content += '<li data-theme="a"><span class="plantName">' + obj.zviewpTxtsh + '</span></li>';
                    content += '<table><tr><td rowspan="2" align="center">' + lightSpan + '</td>';
                    content += '<td>存量: </td><td class="col3">' + obj.stock + '</td><td>噸</td></tr>';
                    content += '<tr><td>安全量: </td><td class="col3">' + obj.stockKpi + '</td><td>噸</td></tr>';
                    content += '</table>';
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#updateTime').html(data.lastSyncTime || 'n/a');
            });
        },
        lightSpan: function(lightType) {
            var lightCss = lightType === '0' ? 'greenlight48' :
                    lightType === '1' ? 'yellowlight48' : 'redlight48';
            return '<span class="' + lightCss + '"/>';
        }
    };

    window.WidgetPpmonitor = new WidgetPpmonitor();

})();
