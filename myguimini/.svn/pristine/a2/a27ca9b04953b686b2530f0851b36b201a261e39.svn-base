/* onsitemonitor:出貨車船監控, distributionportal:物流運力資訊 */

(function() {

    var WidgetOnsitemonitor = function() {
    };

    WidgetOnsitemonitor.prototype = {
        title: '出貨車船監控',
        // route handler:
        route: function(params) {
            var _this = this;
            if (params.plant) {
                var view = new WidgetView({pageId: '#pgOnsitemonitor2'});
                app.changePage(view);
                view.$("input[type='radio']").bind("change", function(event, ui) {
                    _this.tabChange(view);
                });
                _this.loadPage2(view, params.plant);
            } else {
                var view = new WidgetView({pageId: '#pgOnsitemonitor'});
                app.changePage(view);
                _this.loadPage1(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var service = '/shipmentMonitor/resources/deliveryMonitor/sso/getAllBriefs';
            executeService(service, function(datalist) {
                var content = '';
                var updateTime = '';
                $.each(datalist, function(index, obj) {
                    content += _this.page1_row(obj);
                    if (updateTime === '') {
                        updateTime = obj.updateTime;
                    }
                });
                view.$('#updateTime').html(date2YMDHMS(new Date(updateTime)));
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        loadPage2: function(view, plant) {
            var _this = this;
            var service = '/shipmentMonitor/resources/factory/sso/detail?plant=' + plant;
            executeService(service, function(data) {
                view.$('#updateTime').html(data.refreshDate);
                _this.page2_count(view, data);
                _this.page2_summary(view, data.mgFactoryInfoList);
                _this.tabChange(view);
            });
        },
        tabChange: function(view) {
            var choice = view.$('input[name=choice]:checked').val();
            if ('1' === choice) {
                view.$('#datalist1').show();
                view.$('#datalist2').hide();
            } else {
                view.$('#datalist1').hide();
                view.$('#datalist2').show();
            }
        },
        page1_row: function(obj) {
            var content = '';
            var linkParams = {plant: obj.plantCode};
            var link = buildLink('#onsitemonitor', linkParams);
            content += '<li><a href="' + link + '">' + obj.plantName + '</a></li>';
            content += '<table>';
            var lightCss = obj.anyLineBusy ? 'redlight48' :
                    obj.timeOut ? 'yellowlight48' : 'greenlight48';
            var sum = obj.waitingVehicleCount + obj.loadingVehicleCount + obj.leavingVehicleCount;
            content += '<tr><td rowspan="2" align="center"><span class="' + lightCss + '"></td>';
            content += '<td>出貨量:</td><td class="col3">' + formatNumber(obj.deliveryQuantity) + '</td><td>噸</td></tr>';
            content += '<tr><td>在廠車船數:</td><td class="col3">' + sum + '</td><td>輛(艘)</td></tr>';
            content += '</table>';
            return content;
        },
        page2_count: function(view, data) {
            var _this = this;
            var content = '';
            content += _this.page2_count_list('等待區', data.waittingAreaList);
            content += _this.page2_count_list('裝貨區', data.loadingAreaList);
            content += _this.page2_count_list('待出廠', data.outputingAreaList);
            view.$('#datalist1').html(noContent(content));
            view.$('#datalist1').trigger('create');
        },
        page2_count_list: function(title, list) {
            var html = '<ul data-role="listview">';
            var count = 0;
            var light = 0;
            $.each(list, function(index, obj) {
                var lane = obj.lane || '';
                count += parseInt(obj.count);
                var img = 'green';
                if ('1' === obj.light) {
                    img = 'red';
                    light = 1;
                } else if ('2' === obj.light) {
                    img = 'yellow';
                    if (light !== 1) {
                        light = 2;
                    }
                }
                html += '<li><table>';
                html += '<tr><td rowspan="3" align="center"><span class="' + img + 'light48"/></td>';
                html += '<td>品名: </td><td class="col3">' + obj.typeName + '</td>';
                html += '<tr><td>車道編號: </td><td class="col3">' + lane + '</td></tr>';
                html += '<tr><td>在廠車(船)數: </td><td class="col3">' + obj.count + '</td></tr>';
                html += '</table></li>';
            });
            var lightCss = 'gray';
            if (list.length === 0) {
                html += '<li>無</li>';
            } else {
                lightCss = (0 === light) ? 'green' : (1 === light) ? 'red' : 'yellow';
                if ('待出廠' === title) {
                    count = parseInt(list[0].count);
                }
            }
            var header = '<div data-role="collapsible"><h3><span class="' + lightCss + 'light48"/>'
                    + title + ', 在廠車(船)數 ' + count + ' 輛(艘)</h3>';
            html += '</ul>';
            html += '</div>';
            return header + html;
        },
        page2_summary: function(view, list) {
            var _this = this;
            var content = '';
            var total = 0;
            var car = 0;
            var ship = 0;
            $.each(list, function(index, obj) {
                content += _this.page2_summary_line(obj);
                total += obj.total;
                car += (obj.carNum || 0);
                ship += (obj.shipmentNum || 0);
            });
            var plant = list.length > 0 ? list[0].factory : '';
            view.$('#plant').html(plant);
            view.$('#total').html(formatNumber(total) + '噸');
            view.$('#carShip').html(car + '輛/' + ship + '艘');
            view.$('#datalist2').html(content);
            view.$('#datalist2').listview('refresh');
        },
        page2_summary_line: function(obj) {
            var title = obj.pack + ', ' + obj.prod;
            var car = obj.carNum || 0;
            var ship = obj.shipmentNum || 0;
            var total = formatNumber(obj.total);
            var html = '<li data-theme="a"><span class="plantName">' + title + '</span></li>';
            html += '<table><tr><td>出貨累計: </td><td>' + total + ' 噸</td></tr>';
            html += '<tr><td>在廠車/船數: </td><td>' + car + ' 輛/' + ship + ' 艘</td></tr>';
            html += '</table>';
            return html;
        }
    };

    window.WidgetOnsitemonitor = new WidgetOnsitemonitor();

// 物流運力資訊 ---------------------------------------------------
    var WidgetDistributionportal = function() {
    };

    WidgetDistributionportal.prototype = {
        title: '物流運力資訊',
        init: false,
        sales: '',
        name: '',
        view: '', // O,C(page1,2,3), M(page2,3), A,F(page3)
        path: '',
        bukrs: {}, // 廠代碼(view是C時要檢查)

        // route handler:
        route: function(params) {
            var _this = this;
            if (!_this.init) {
                executeService('/dashboard/service/swlsService/shm/findSalesPermission', function(data) {
                    _this.sales = data.sales;
                    _this.name = data.name;
                    _this.view = data.view;
                    var authList = data.authList || [];
                    $.each(authList, function(index, obj) {
                        _this.bukrs[obj.bukrs] = true;
                    });
                    if ('O' === _this.view || 'C' === _this.view) {
                        var view = new WidgetView({pageId: '#pgDistributionportal'});
                        app.changePage(view);
                        view.$("input[type='radio']").bind("change", function(event, ui) {
                            _this.tabChange(view);
                        });
                        _this.loadPage1(view);
                    } else if ('M' === _this.view) {
                        var view = new WidgetView({pageId: '#pgDistributionportal2'});
                        app.changePage(view);
                        _this.loadPage2(view, '');
                    } else if ('A' === _this.view || 'F' === _this.view) {
                        var view = new WidgetView({pageId: '#pgDistributionportal2'});
                        app.changePage(view);
                        _this.loadPage3(view, '');
                    } else {
                        var view = new WidgetView({pageId: '#pgDistributionportal2'});
                        app.changePage(view);
                        _this.loadPage3(view, '');
                    }
                    _this.init = 1;
                });
            } else if (params.pf) {
                var view = new WidgetView({pageId: '#pgDistributionportal2'});
                app.changePage(view);
                _this.loadPage2(view, params.pf);
            } else if (params.rg && params.sales) {
                var view = new WidgetView({pageId: '#pgDistributionportal4'});
                app.changePage(view);
                view.$("input[type='radio']").bind("change", function(event, ui) {
                    var choice = view.$('input[name=choice]:checked').val();
                    view.$('ul').hide();
                    view.$('#datalist' + choice).show();
                });
                view.$('#path').html(_this.path);
                _this.loadPage4(view, params.rg, params.sales);
            } else if (params.rg) {
                var view = new WidgetView({pageId: '#pgDistributionportal2'});
                app.changePage(view);
                _this.loadPage3(view, params.rg);
            } else {
                var view = new WidgetView({pageId: '#pgDistributionportal'});
                app.changePage(view);
                view.$("input[type='radio']").bind("change", function(event, ui) {
                    dp_ChoiceChange(view);
                });
                _this.loadPage1(view);
            }
        },
        loadPage1: function(view) {
            var _this = this;
            var service = '/dashboard/service/swlsService/shm/listShmTransport/tcc/';
            executeService(service, function(data) {
                var datalist = data.shmSwlsTransportDtoList || [];
                var content1 = '';
                var content2 = '';
                var syncTime = '';
                $.each(datalist, function(index, obj) {
                    if (syncTime === '') {
                        syncTime = obj.syncTime;
                    }
                    if ('C' === _this.view && !_this.bukrs[obj.plantCode]) {
                        return; //無權限, 下一筆
                    }
                    var title = obj.plantName + '-' + obj.freighttypeName;
                    var linkParams = {pf: obj.plantCode + obj.freighttypeCode};
                    var link = buildLink('#distributionportal', linkParams);
                    content1 += '<li><a href="' + link + '">' + title + '</a></li>';
                    content2 += '<li data-theme="a"><span class="plantName">' + title + '</span></li>';
                    content1 += _this.page1_day(obj);
                    content2 += _this.page1_month(obj);
                });
                view.$('#datalist1').html(content1);
                view.$('#datalist2').html(content2);
                view.$('#datalist1').listview('refresh');
                view.$('#datalist2').listview('refresh');
                view.$('#updateTime').html(syncTime || 'n/a');
            });
        },
        loadPage2: function(view, pf) {
            var _this = this;
            executeService('/dashboard/service/swlsService/shm/listShmRegionLight', function(data) {
                var datalist = data.shmSwlsTransportRegionDtoList || [];
                var content1 = '';
                var syncTime = '';
                var path = '';
                $.each(datalist, function(index, obj) {
                    if (syncTime === '') {
                        syncTime = obj.syncTime;
                    }
                    if (pf !== '' && pf !== (obj.plantCode + obj.freighttypeCode)) {
                        return;
                    }
                    if (path === '') {
                        path = obj.plantName + '-' + obj.freighttypeName;
                    }
                    var title = obj.regionName + '-' + obj.freighttypeName;
                    var linkParams = {rg: obj.regionCode};
                    var link = buildLink('#distributionportal', linkParams);
                    content1 += '<li data-role="list-divider"><a href="' + link + '">' + title + '</a></li>';
                    content1 += _this.page2_row(obj);
                });
                view.$('#datalist1').html(noContent(content1));
                view.$('#datalist1').listview('refresh');
                view.$('#updateTime').html(syncTime || 'n/a');
                view.$('#path').html(path);
            });
        },
        loadPage3: function(view, rg) {
            var _this = this;
            executeService('/dashboard/service/swlsService/shm/listShmSalesLight', function(data) {
                var datalist = data.shmSwlsTransportRegionDtoList || [];
                var content1 = '';
                var syncTime = '';
                var path = '';
                $.each(datalist, function(index, obj) {
                    if (syncTime === '') {
                        syncTime = obj.syncTime;
                    }
                    if (rg !== '' && rg !== obj.regionCode) {
                        return;
                    }
                    if (path === '') {
                        path = obj.regionName + '-' + obj.freighttypeName;
                    }
                    var title = obj.salesName + '-' + obj.freighttypeName;
                    var linkParams = {rg: obj.regionCode, sales: obj.salesId};
                    var link = buildLink('#distributionportal', linkParams);
                    content1 += '<li data-role="list-divider"><a href="' + link + '">' + title + '</a></li>';
                    content1 += _this.page2_row(obj);
                });
                view.$('#datalist1').html(noContent(content1));
                view.$('#datalist1').listview('refresh');
                view.$('#updateTime').html(syncTime || 'n/a');
                view.$('#path').html(path);
                view.$('#datalist1 li a').click(function() {
                    _this.path = view.$(this).text();
                });
            });
        },
        loadPage4: function(view, rg, sales) {
            var _this = this;
            var service = '/dashboard/service/swlsService/shm/listShmCustLight/' + rg;
            executeService(service, function(data) {
                var datalist = data.recoreds || [];
                var content0 = _this.page4_content(datalist, '0', sales); // 在途
                var content1 = _this.page4_content(datalist, '1', sales); // 待卸
                var content2 = _this.page4_content(datalist, '2', sales); // 在卸
                view.$('#datalist0').html(noContent(content0));
                view.$('#datalist0').listview('refresh');
                view.$('#datalist1').html(noContent(content1));
                view.$('#datalist1').listview('refresh');
                view.$('#datalist2').html(noContent(content2));
                view.$('#datalist2').listview('refresh');
            });
        },
        tabChange: function(view) {
            var choice = view.$('input[name=choice]:checked').val();
            if ('1' === choice) {
                view.$('#datalist1').show();
                view.$('#datalist2').hide();
            } else {
                view.$('#datalist1').hide();
                view.$('#datalist2').show();
            }
        },
        page1_day: function(obj) {
            var _this = this;
            var html = '<table width="100%"><tr>';
            html += _this.page1_cell('計劃量', obj.statusPlanned, obj.plannedQuantity, '');
            html += _this.page1_cell('運力滿足', obj.statusShipping, obj.capability, obj.vehicleCount);
            html += _this.page1_cell('待卸', obj.statusUnloadwaiting, obj.unloadQuantity, obj.unloadCount);
            html += '</tr></table>';
            return html;
        },
        page1_month: function(obj) {
            var _this = this;
            var html = '<table width="100%"><tr>';
            html += _this.page1_cell('累計計劃量', obj.statusAccumPlanned, obj.accumEffectiveQuantity, '');
            html += _this.page1_cell('累計完成', '', obj.accumLoadedQuantity, '');
            html += _this.page1_cell('累計未完成', obj.statusAccumUndone, obj.accumUndoneQuantity, '');
            html += '</tr></table>';
            return html;
        },
        page1_cell: function(title, light, ton, count) {
            var _this = this;
            var html = '<td width="33%" align="center" valign="top">';
            html += title + '<br/>' + _this.lightSpan(light);
            if ('3' !== light || '運力滿足' === title) {
                if ('0' !== ton) {
                    html += '<br/>' + ton + ' 噸';
                }
                if ('' !== count) {
                    html += '<br/>' + count + ' 輛/艘';
                }
            }
            html += '</td>';
            return html;
        },
        page2_row: function(obj) {
            var _this = this;
            var html = '<table width="100%"><tr>';
            var desc = '實際:' + obj.plannedQuantity + ' 噸<br/>目標:' + obj.targetQuantity + ' 噸';
            html += _this.page2_row_cell('計劃量', obj.statusPlanned, desc);
            desc = obj.loadCount + ' 輛/艘<br/>' + obj.loadQuantity + ' 噸';
            html += _this.page2_row_cell('當日訂單', obj.statusShipping, desc);
            desc = obj.unloadCount + ' 輛/艘<br/>' + obj.unloadQuantity + ' 噸';
            html += _this.page2_row_cell('待卸', obj.statusPier, desc);
            html += '</tr></table>';
            return html;
        },
        page2_row_cell: function(title, light, desc) {
            var _this = this;
            var html = '<td width="33%" align="center" valign="top">';
            html += title + '<br/>' + _this.lightSpan(light) + '<br/>' + desc;
            html += '</td>';
            return html;
        },
        page4_content: function(datalist, step, sales) {
            var html = '';
            $.each(datalist, function(index, obj) {
                if (obj.step !== step || obj.ownerEmpId !== sales) {
                    return;
                }
                // 在卸一律灰燈
                var lightCss = '2' === step ? 'gray' :
                        '0' === obj.statusLoaded ? 'red' :
                        '1' === obj.statusLoaded ? 'yellow' :
                        '2' === obj.statusLoaded ? 'green' : 'gray';
                html += '<li data-theme="a"><span class="plantName">' + obj.customerName + '</span></li>';
                html += '<table><tr><td rowspan="3" align="center"><span class="' + lightCss + 'light64"/></td>';
                html += '<td>品項:</td><td>' + obj.materialName + '</td></tr>';
                html += '<tr><td>車船數: </td><td>' + obj.loadedCount + '</td></tr>';
                html += '<tr><td>噸數:</td><td>' + obj.loadedSum + '</td></tr>';
                html += '</table>';
            });
            return html;
        },
        lightSpan: function(light) {
            var html = '<span class="';
            if ('0' === light) {
                html += 'redlight48';
            } else if ('1' === light) {
                html += 'yellowlight48';
            } else if ('2' === light) {
                html += 'greenlight48';
            } else if ('3' === light) {
                html += 'graylight48';
            }
            html += '"/>';
            return html;
        }
    };

    window.WidgetDistributionportal = new WidgetDistributionportal();

})();
