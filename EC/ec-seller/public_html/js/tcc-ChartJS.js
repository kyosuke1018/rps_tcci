/*var _COLORS = [
            '#85144b','#00A5FF','#3D9970','#0074D9','#FF851B',
            '#F012BE','#FFDC00','#AFEEEE','#2ECC40','#7FDBFF',
            '#FF4136','#B10DC9','#CD853F','#01FF70','#39CCCC'
        ];
*/
/* global utils */

var _COLORS = ["rgb(165,181,146)","rgb(243,164,71)","rgb(156,133,192)","rgb(208,146,167)","rgb(231,188,41)",
                "rgb(165,161,146)","rgb(223,164,71)","rgb(201,188,41)","rgb(188,146,167)","rgb(156,133,172)",
                "rgb(185,161,146)","rgb(223,164,91)","rgb(201,168,41)","rgb(188,166,167)","rgb(176,133,172)"
              ];
function getRndColors(len){
    var colors = [];
    var s = utils.getRandom(0, _COLORS.length-1);
    while(len>0){
        //console.log("getRndColors s = "+s);
        colors.push(_COLORS[s]);
        s = (s<_COLORS.length-1)?s+1:0;
        len--;
    }
    //console.log("getRndColors colors = \n");
    return colors;
}

// Utils for Chart JS 
function registerPluginsForChartJS(){
    // Register plugin to always show tooltip
    // ref: https://github.com/chartjs/Chart.js/issues/4045
    Chart.plugins.register({
	beforeRender: function(chart) {
            if (chart.config.options.showAllTooltips) {
                // create an array of tooltips
                // we can't use the chart tooltip because there is only one tooltip per chart
                chart.pluginTooltips = [];
                chart.config.data.datasets.forEach(function(dataset, i) {
                    chart.getDatasetMeta(i).data.forEach(function(sector, j) {
                        chart.pluginTooltips.push(
                            new Chart.Tooltip(
                                {
                                    _chart: chart.chart,
                                    _chartInstance: chart,
                                    _data: chart.data,
                                    _options: chart.options.tooltips,
                                    _active: [sector]
                                },
                                chart
                            )
                        );
                    });
                });

                // turn off normal tooltips
                chart.options.tooltips.enabled = false;
            }
	},
	afterDraw: function(chart, easing) {
            if (chart.config.options.showAllTooltips) {
                // we don't want the permanent tooltips to animate, so don't do anything till the animation runs atleast once
                if (!chart.allTooltipsOnce) {
                    if (easing !== 1) return;
                    chart.allTooltipsOnce = true;
                }

                // turn on tooltips
                chart.options.tooltips.enabled = true;
                Chart.helpers.each(chart.pluginTooltips, function(tooltip) {
                    tooltip.initialize();
                    tooltip._options.bodyFontFamily = "'Lato', sans-serif";
                    tooltip._options.backgroundColor = "rgba(0,0,0,0.3)";
                    tooltip._options.displayColors = false;// 顯示對應顏色方塊
                    tooltip._options.bodyFontSize = 12;//tooltip._chart.height * 0.06;
                    /*tooltip._options.custom = function(elements, eventPosition) {
                        var tooltip = this;
                        console.log("positioners.custom tooltip = \n", tooltip);
                        console.log("positioners.custom elements = \n", elements);
                        console.log("positioners.custom eventPosition = \n", eventPosition);
                        return {
                            x: 0,
                            y: 0
                        };
                    };*/
                    tooltip._options.yPadding = 3;
                    tooltip._options.xPadding = 6;
                    tooltip._options.titleSpacing = 10;
                    tooltip._options.position = 'average';
                    tooltip._options.caretSize = tooltip._options.bodyFontSize;// * 0.5;
                    tooltip._options.cornerRadius = tooltip._options.bodyFontSize * 0.2;
                    //console.log("tooltip._options = \n", tooltip._options);
                    tooltip.update();
                    // we don't actually need this since we are not animating tooltips
                    tooltip.pivot();
                    tooltip.transition(easing).draw();
                });
                chart.options.tooltips.enabled = false;
            }
	}
    });
}

function renderChartJS(selector, config){
    var ctx = $(selector);
    return new Chart(ctx, config);
}

// Pie Chart
function genPieConfig(datas, labels, colors){
    colors = utils.isEmptyAry(colors)?getRndColors(labels.length):colors;
       
    var config = {
        type: 'pie',
        data: {
            datasets: [{
                    data: datas,//[80,100,200,150],
                    backgroundColor: colors//['#f67019','#f53794','#537bc4','#acc236']//,
                    //label: 'Dataset 1'
                }],
            labels: labels//['散裝42.5：80TO', '散裝52.5：100TO', '袋裝32.5R：200TO', '袋裝42.5：150TO']
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            showAllTooltips: true,
            legend: {
                position: 'left',
                display: true
            },
            tooltips: {
                enabled: false,
                callbacks: {
                    label: function(tooltipItem, data) {
                        //console.log("tooltipItem = \n", tooltipItem);
                        //console.log("data = \n", data);
                        var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                        var label = data.labels[tooltipItem.index];
                        //return label + "：" +value;
                        return value;
                    }
                }
            }
        }
    };
    
    return config;
}

// Bar Chart
function genBarConfig(datasets, labels, colors, datasetLine){
    var singleSet = (datasets.length===1);
    if( singleSet ){
        // 單組資料: 每個 BAR 不同顏色
        colors = utils.isEmptyAry(colors)?getRndColors(labels.length):colors;
    }else{
        // 多組資料: 同組資料的 BAR 一樣顏色
        colors = utils.isEmptyAry(colors)?getRndColors(datasets.length):colors;
    }
    
    var list = [];
    if( !isNA(datasetLine) ){// line for combi chart
        list.push(datasetLine);
    }
    for(var i=0; i<datasets.length; i++){
        var dataset = {
                backgroundColor: (singleSet)?colors:colors[i],//"rgba(255, 99, 132, 0.5)",
                //borderColor: "rgb(99, 132, 255)",
                borderWidth: 1,
                data: datasets[i]//,
                //label: "訂單未出貨"
            };
            
        list.push(dataset);
    }
        
    var data = {
        datasets: list,
        labels: labels//["42.5 Bulk", "52.5 Bulk", "32.5R Bag", "42.5 Bag"]
    };

    var config = {
        type: 'bar',
        data: data,
        options: {
            //showAllTooltips: true,
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                display: false //true,
                //position: 'top'
            },
            title: {
                display: false //true,
                //text: 'Chart.js Bar Chart'
            },
            scales: {
                xAxes: [{
                        type: 'category',
                        // Specific to Bar Controller
                        categoryPercentage: 0.8,
                        barPercentage: 0.9,

                        // offset settings
                        offset: true,
                        
                        display: true,
                        scaleLabel: {
                            display: false//,
                            //labelString: 'Month'
                        },
                        ticks: {
                            autoSkip: false,
                            callback: function(label, index, labels) {
                                label = isNA(label)?"":label.length>20?label.substring(0,20)+"...":label;
                                return label;
                            }
                        }
                    }],
                yAxes: [{
                        display: true,
                        scaleLabel: {
                            beginAtZero: true,
                            //stepSize: 1000,
                            display: false//,
                            //labelString: 'm.t.'
                        },
                        ticks: {
                            min: 0,
                            callback: function(label, index, labels) {
                                return utils.printNumber(label); //label/1000+'k';
                            }
                        }
                    }]
            },
            tooltips: {
                enabled: true,
                mode: 'index',
                intersect: true
                /*backgroundColor:"rgba(0,0,0,1)",
                callbacks: {
                    title: function(tooltipItems, data) {
                        return "";
                    },
                    label: function(tooltipItem, data) {
                        var datasetLabel = "";
                        var label = data.labels[tooltipItem.index];
                        return data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index] + '%';
                    }
                    // labelColor: function(tooltipItem, chart) {
                    // 	return {
                    // 			borderColor: 'rgb(255, 0, 0)',
                    // 			backgroundColor: 'rgba(255, 0, 0, 0)'
                    // 	}
                    // }
                }*/
            }
        }
    };
    return config;
}

// Line Chart
function genLineConfig(datasets, dataLabels, xlabels, colors, specDataIdxs){
    // 多組資料: 同組資料的 BAR 一樣顏色
    colors = utils.isEmptyAry(colors)?getRndColors(datasets.length):colors;
    
    var list = [];
    for(var i=0; i<datasets.length; i++){
        var data = {
                backgroundColor: colors[i],//"rgba(255, 99, 132, 0.5)",
                borderColor: colors[i],//"rgb(99, 132, 255)",
                borderWidth: 1,
                fill: false,
                data: datasets[i],
                label: dataLabels[i]
            };
        if( !isNA(specDataIdxs) && specDataIdxs.indexOf(i)>=0 ){// 特殊樣式
            // triangle, rect, rectRounded, rectRot, cross, crossRot, star, line, dash
            data['pointStyle'] = 'triangle';
            data['borderWidth'] = 2;
        }
        list.push(data);
    }

    var config = {
        type: 'line',
        data: {
            labels: xlabels,
            datasets: list
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            //spanGaps: false,
            elements: {
                line: {
                    tension: 0.000001
                }
            },
            legend: {
                labels: {
                    usePointStyle: true
                }
            },
            title: {
                display: false//,
                //text: 'Chart.js Line Chart'
            },
            tooltips: {
                mode: 'index',
                intersect: false
            },
            hover: {
                mode: 'nearest',
                intersect: true
            },
            scales: {
                xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: false//,
                            //labelString: 'Month'
                        }
                    }],
                yAxes: [{
                        display: true,
                        scaleLabel: {
                            //beginAtZero: true,
                            //stepSize: 1000,
                            display: true//,
                            //labelString: 'm.t.'
                        },
                        ticks: {
                            min: 0,
                            callback: function(label, index, labels) {
                                return utils.printNumber(label); //label/1000+'k';
                            }
                        }
                    }]
            }
        }
    };
    
    return config;
}
