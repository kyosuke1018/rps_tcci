/* skspmonitor: 景德業績預警 */

(function() {

    var WidgetSkspmonitor = function() {
    };

    WidgetSkspmonitor.prototype = {
        title: '景德業績預警',
        yearmon: '', // yyyymm
        access1: '', // 選到的 channle
        domain: '',
        salesman: '',
        // route handler:
        route: function(params) {
            var _this = this;
            if (_this.yearmon === '') {
                _this.yearmon = date2YM(new Date());
            }
            _this.access1 = params.access1 ? params.access1 : '';
            _this.accessid = params.accessid ? params.accessid : '';
            _this.domain = params.domain ? params.domain : '';
            _this.domainid = params.domainid ? params.domainid : '';
            _this.salesman = params.salesman ? params.salesman : '';
            _this.sapid = params.sapid ? params.sapid : '';

            var view = new WidgetView({pageId: '#pgSkspmonitor'});
            app.changePage(view);
            view.$('#yearmon').val(formatYMD(_this.yearmon, '-'));
            view.$('#yearmon').change(function() {
                _this.yearmon = view.$('#yearmon').val().replace(/-/g, '');
                _this.loadPage(view);
            });
            _this.loadPage(view);
        },
        loadPage: function(view) {
            var _this = this;
            var service = '';
            if (_this.access1 === '') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/Allareas?';
            }
            // level 1's total
            if (_this.access1 === 'TOTAL') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/AllareasTotal?';
            }
            // level 2's total
            if (_this.domainid === '01_TO') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/AllareasTotalT?';
            }
            // level 3's total
            if (_this.sapid === '01_TO') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/AllareasTotalS?';
            }

            if (_this.access1 !== '' &&
                    _this.domain === '' &&
                    _this.salesman === '') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/Area?code=' + _this.access1 + '&';
            }
            if (_this.domain !== '' &&
                    _this.salesman === '') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/Sales?code=' + _this.domain + '&';
            }
            if (_this.salesman !== '') {
                service = '/SKSalesPortal/service/achieve2/GetAchieve/Products?code=' + _this.salesman + '&';
            }

            service += 'yyyymm=' + _this.yearmon;
            executeService(service, function(list) {
                var content = '';
                $.each(list, function(index, obj) {
                    if ('TOTAL' === obj.ACCESS_1) {
                        obj.saleName = '合計';
                        content = _this.page_row(obj) + content;
                        ;
                    } else {
                        content += _this.page_row(obj);
                    }
                });
                view.$('#datalist').html(noContent(content));
                view.$('#datalist').listview('refresh');
                view.$('#datalist tr.detail').hide();
                view.$('#datalist a.toggleDetail').click(function() {
                    $(this).parents('table').find('tr.detail').toggle();
                    return false;
                });
            });
        },
        page_row: function(obj) {
            var content = '';
            var link = '';

            if ('TOTAL' === obj.ACCESS_1 &&
                    typeof obj.DOMAINID === "undefined") {
                link = buildLink('#skspmonitor', {access1: obj.ACCESS_1});
                content = '<li><a href="' + link + '">' + obj.ACCESS_1 + '</a></li>';
            }

            if ('TOTAL' === obj.ACCESS_1 &&
                    typeof obj.DOMAINID !== "undefined" &&
                    typeof obj.SAPID === "undefined") {
                content = '<li data-theme="a"><span class="plantName">' + obj.DOMAIN + '</a></li>';
            }

            if ('TOTAL' === obj.ACCESS_1 &&
                    'TOTAL' === obj.DOMAIN &&
                    typeof obj.SAPID !== "undefined") {
                content = '<li data-theme="a"><span class="plantName">' + obj.SALESMAN + '</a></li>';
            }

            if ('TOTAL' !== obj.ACCESS_1 && typeof obj.DOMAIN === "undefined") {
                link = buildLink('#skspmonitor', {access1: obj.ACCESSID});
                content = '<li><a href="' + link + '">' + obj.ACCESS_1 + '</a></li>';
            }

            if ('TOTAL' !== obj.ACCESS_1 &&
                    typeof obj.DOMAIN !== "undefined" &&
                    typeof obj.SAPID === "undefined") {
                link = buildLink('#skspmonitor', {domain: obj.DOMAINID});
                content = '<li><a href="' + link + '">' + obj.DOMAIN + '</a></li>';
            }
            if ('TOTAL' !== obj.ACCESS_1 &&
                    'TOTAL' !== obj.DOMAIN &&
                    typeof  obj.SAPID !== "undefined") {
                link = buildLink('#skspmonitor', {salesman: obj.SAPID});
                content = '<li><a href="' + link + '">' + obj.SALESMAN + '</a></li>';
            }
            if (typeof  obj.PDTYPE !== "undefined") {
                content = '<li data-theme="a"><span class="plantName">' + obj.PDTYPE + '</span></a></li>';
            }

            var lightCss = '0' === obj.LIGHT.toString() ? 'green' :
                    '1' === obj.LIGHT.toString() ? 'yellow' :
                    '2' === obj.LIGHT.toString() ? 'red' : 'gray';
            lightCss += 'light64';
            content += '<table>';
            content += '<tr><td rowspan="16" align="center"><span class="' + lightCss + '"></td>';
            content += '<td>銷貨淨額</td><td align="right">' + formatNumber(obj.SALES_AMOUNT) + '</td></tr>';
            content += '<tr><td>毛利率</td><td align="right">' + formatNumber(obj.GROSS_PROFIT_RATE) + '%</td></tr>';
            content += '<tr><td>累計達成率</td><td align="right">' + formatNumber(obj.CUMULATIVE_ACHIVEMENT_RATE) + '%</td></tr>';
            content += '<tr><td>全月達成率</td><td align="right">' + formatNumber(obj.MONTH_ACHIVEMENT_RATE) + '%</td></tr>';
            content += '<tr class="detail"><td>發票金額</td><td align="right">' + formatNumber(obj.INVOICE_AMOUNT) + '</td></tr>';
            content += '<tr class="detail"><td>事前折溢</td><td align="right">' + formatNumber(obj.PREMIUM_DISCOUNT) + '</td></tr>';
            content += '<tr class="detail"><td>銷貨退回</td><td align="right">' + formatNumber(obj.SALES_RETURN) + '</td></tr>';
            content += '<tr class="detail"><td>事後折讓</td><td align="right">' + formatNumber(obj.SALES_DISCOUNT) + '</td></tr>';
            content += '<tr class="detail"><td>累計預算</td><td align="right">' + formatNumber(obj.BUDGET_TARGET) + '</td></tr>';
            content += '<tr class="detail"><td>全月預算</td><td align="right">' + formatNumber(obj.BUDGET) + '</td></tr>';
            content += '<tr class="detail"><td>應收帳款</td><td align="right">' + formatNumber(obj.SHOULD_PAY_AMOUNT) + '</td></tr>';
            content += '<tr class="detail"><td>收款金額</td><td align="right">' + formatNumber(obj.PAYMENT_AMOUNT) + '</td></tr>';
            content += '<tr class="detail"><td>繳款率</td><td align="right">' + formatNumber(obj.PAYMENT_RATE) + '</td></tr>';
            content += '<tr class="detail"><td>負責人姓名</td><td align="right">' + obj.RESPONSE_NAME + '</td></tr>';
            content += '<tr class="detail"><td>負責人電話</td><td align="right">' + obj.RESPONSE_PHONE + '</td></tr>';
            content += '<tr><td colspan="2"><a href="#" class="toggleDetail">點此顯示/隱藏明細</a></td></tr>';
            content += '</table>';
            return content;
        }
    };

    window.WidgetSkspmonitor = new WidgetSkspmonitor();

})();
