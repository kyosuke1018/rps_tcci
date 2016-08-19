/* envmonitor: 環保資料預警 */

(function() {
    var WidgetWrmonitor = function() {
    };
    
    WidgetWrmonitor.prototype = {
        title: '軸瓦預警',
        page: 1,
        plant: '',
        line: '',
        plantNm: '',
        // route handler:
        route: function(params) {
            var _this = this;
            if (params.plant && params.line) {
                _this.plant = params.plant;
                _this.line = params.line;
                _this.page = 2;
            } else {
                _this.page = 1;
            }
            var view = new WidgetView({pageId: '#pgWrmonitor'});
            app.changePage(view);
            _this.loadPage(view);
        },
        loadPage: function(view) {
            var _this = this;
            var service = '/tccprod/service/tccProdREST/sso/wr_dcs/findWrL' + _this.page;
            if (_this.page === 2) {
                service += '?plant=' + _this.plant + '&line=' + _this.line;
            }
            executeService(service, function(data) {
                var syncTime = date2YMDHMS(new Date(data.syncTime));
                view.$('#syncTime').html(syncTime);
                var list = data.wrDcsList || [];
                var content = '';
                if (_this.page === 1) {
                    var plants = {};
                    var plantList = [];
                    $.each(list, function(index, obj) {
                        var plantCode = obj.plant;
                        var plantName = obj.plantNm;
                        var line = obj.line;
                        var light = obj.light;
                        if (!(plantCode in plants)) {
                            var wrmonitorVO = {plant: plantCode, plantNm: plantName, 
                            light1: line === 'K01' ? light : -1, 
                            light2: line === 'K02' ? light : -1,
                            light3: line === 'K03' ? light : -1, 
                            light4: line === 'K04' ? light : -1};//預設燈號-1 不顯示
                            
                            plants[plantCode] = plantName;
                            plantList.push(wrmonitorVO);
                        }else{
                            $.each(plantList, function(index2, obj2) {
                                if (obj2.plant === plantCode) {
                                    var wrmonitorVO = obj2;
                                    if (line === 'K01') {
                                        wrmonitorVO.light1 = light;
                                    }else if (line === 'K02') {
                                        wrmonitorVO.light2 = light;
                                    }else if (line === 'K03') {
                                        wrmonitorVO.light3 = light;
                                    }else if (line === 'K04') {
                                        wrmonitorVO.light4 = light;
                                    }
                                    plantList[index2] = wrmonitorVO;
                                }
                            });
                        }
                    });
                    $.each(plantList, function(index, obj) {
                        content += _this.page_content1(obj);
                    });
                }else if (_this.page === 2) {
                    $.each(list, function(index, obj) {
                        content += _this.page_content2(obj);
                    });
                    
                    if(list.length > 0){
                        _this.plantNm = list[0].plantNm;
                    }
                    view.$('#path').html(_this.plantNm+' '+_this.line);
                }
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
            });
        },
        page_content1: function(obj) {
            var _this = this;
            var title = obj.plant + obj.plantNm;
            var link1 = '#wrmonitor?plant=' + obj.plant + '&line=K01';
            var link2 = '#wrmonitor?plant=' + obj.plant + '&line=K02';
            var link3 = '#wrmonitor?plant=' + obj.plant + '&line=K03';
            var link4 = '#wrmonitor?plant=' + obj.plant + '&line=K04';
            var content = '<li data-theme="a">' + title + '</li>';
            content += '<table width="100%"><tr>';
            //灰燈要整個拿掉還是不給連結就好?! <span class="graylight48"/>
            //20160729 99顯示灰燈無資料, 預設值-1不顯示
            content += obj.light1 === -1?'<td width="25%" align="center"></td>'
                :obj.light1 === 99?'<td width="25%" align="center"><span class="graylight48"/><br/>K01</td>'
                :'<td width="25%" align="center"><a href="' + link1 + '">' + _this.lightSpan(obj.light1, 48) + '<br/>K01</a></td>';
            content += obj.light2 === -1?'<td width="25%" align="center"></td>'
                :obj.light2 === 99?'<td width="25%" align="center"><span class="graylight48"/><br/>K02</td>'
                :'<td width="25%" align="center"><a href="' + link2 + '">' + _this.lightSpan(obj.light2, 48) + '<br/>K02</a></td>';
            content += obj.light3 === -1?'<td width="25%" align="center"></td>'
                :obj.light3 === 99?'<td width="25%" align="center"><span class="graylight48"/><br/>K03</td>'
                :'<td width="25%" align="center"><a href="' + link3 + '">' + _this.lightSpan(obj.light3, 48) + '<br/>K03</a></td>';
            content += obj.light4 === -1?'<td width="25%" align="center"></td>'
                :obj.light4 === 99?'<td width="25%" align="center"><span class="graylight48"/><br/>K04</td>'
                :'<td width="25%" align="center"><a href="' + link4 + '">' + _this.lightSpan(obj.light4, 48) + '<br/>K04</a></td>';
            content += '</tr></table>';
            return content;
        },
        page_content2: function(obj) {
            var _this = this;
            var title = obj.tagCd + '-' + obj.tagNm;
            var content = '';
            content += '<li data-theme="a"><span class="plantName">' + title + '</span></li>';
            content += '<table width="100%"><tr>';
            var lightSpan = _this.lightSpan(obj.light, 48);
            content += '<td width="33%" align="center">' + lightSpan + '</td>';
            content += '<td width="66%" align="left">' + obj.comment + '</td>';
            content += '</tr></table>';
            return content;
        },
        lightSpan: function(light, size) {
            var lightCss = 0 === light ? 'green' :
                    1 === light ? 'red' :
                    99 === light ? 'gray' : 'gray';
            return '<span class="' + lightCss + 'light' + size + '"/>';
        }
    };
    
    window.WidgetWrmonitor = new WidgetWrmonitor();
    
})();