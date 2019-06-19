/* 
 * dependencys : tcc-utils.js
 */
/* global utils */

function prepareScatterData(restData, uiKey, dataKey, widget) {
    var datas = restData.datas;
    for(var i=0; i<datas.length; i++){
        if( datas[i].key === uiKey ){
            widget.dataYM = datas[i].ym;
            widget.title = datas[i].title;
            var dataIn = toScatterData(datas[i], dataKey);
            widget.data = dataIn;
                    
        }
    }
}

function prepareCombiData(restData, uiKey, dataKey, widget){
    var datas = restData.datas;
    for(var i=0; i<datas.length; i++){
        if( datas[i].key === uiKey ){        
            var dataIn = toBarDataSingle(datas[i], dataKey);
            widget.title = datas[i].title;
            widget.data = dataIn;
        }
    }
}

function prepareTrendChartData(restData, uiKey, dataKey, widget){
    var datas = restData.datas;
    for(var i=0; i<datas.length; i++){
        if( datas[i].key === uiKey ){
            var dataIn = toBarDataSingle(datas[i], dataKey);
            widget.title = datas[i].title;
            widget.data = dataIn;
        }
    }
}

function toScatterData(restData, root) {
    var chartData = {};
    var columns = [];
    var xs = {};
    var names = {};

    var rows = utils.getRowsByName(restData, root);
    if (rows !== null) {
        rows.forEach(function(item, index) {
            var xId = item.id + "_x";
            names[item.id] = item.name;
            xs[item.id] = xId;
            columns.push([xId, item.x]);
            columns.push([item.id, item.y]);
        });
    }

    chartData["columns"] = columns;
    chartData["xs"] = xs;
    chartData["names"] = names;

    return chartData;
}

function toBarDataSingle(restData, root) {
    var chartData = {};
    var columns = [];
    var names = {};
    var types = {};
    var axes = {};

    var rows = utils.getRowsByName(restData, root);
    if (rows !== null) {
        //console.log(rows.length);
        rows.forEach(function(item, index) {
            //console.log(item.name);
            names[item.id] = item.name;

            var column = [];
            column.push(item.id);
            var values = item.values;
            values.forEach(function(itemV, indexV) {
                column.push(itemV);
            });

            columns.push(column);

            // 部分線圖
            if (item.id.indexOf("P") === 0) { // 線圖資料 id 一律以 P 開頭識別 
                types[item.id] = "line";
                axes[item.id] = "y2";
            }
        });

        // X 軸為時間序列
        var x = [];
        var timeseries = utils.getRowsByName(restData, "timeseries");
        if( !isNA(timeseries) ){
            x = x.concat(["timeseries"], timeseries);
            columns.push(x);
        }
    }

    chartData["columns"] = columns;
    chartData["names"] = names;
    chartData["types"] = types;
    chartData["axes"] = axes;

    // X 軸為文字標記
    var categories = utils.getRowsByName(restData, "categories");
    var ids = utils.getRowsByName(restData, "ids");
    if (categories !== null) {
        chartData["categories"] = categories;
        chartData["ids"] = ids;
    }

    return chartData;
}

function toLineData(restData, root) {
    var chartData = {};
    var columns = [];
    var names = {};
    //var types = {};
    //var axes = {};

    var rows = utils.getRowsByName(restData, root);
    if (rows !== null) {
        //console.log(rows.length);
        rows.forEach(function(item, index) {
            //console.log(item.name);
            names[item.id] = item.name;

            var column = [];
            column.push(item.id);
            var values = item.values;
            values.forEach(function(itemV, indexV) {
                column.push(itemV);
            });

            columns.push(column);

            // 部分線圖
            //if( item.id.indexOf("P")==0 ){// 線圖資料 id 一律以 P 開頭識別 
            //    types[item.id] = "line";
            //    axes[item.id] = "y2";
            //}
        });

        // X 軸為時間序列
        var x = [];
        var timeseries = utils.getRowsByName(restData, "timeseries");
        if( !isNA(timeseries) ){
            x = x.concat(["timeseries"], timeseries);
            columns.push(x);
        }
    }

    chartData["columns"] = columns;
    chartData["names"] = names;
    //chartData["types"] = types;
    //chartData["axes"] = axes;

    // X 軸為文字標記
    var categories = utils.getRowsByName(restData, "categories");
    if (categories !== null) {
        chartData["categories"] = categories;
    }

    return chartData;
}

function toLightList(restData, root) {
    var list = [];
    var ids = utils.getRowsByName(restData, "ids");
    var names = utils.getRowsByName(restData, "names");
    var groups = utils.getRowsByName(restData, "groups");
    var rows = utils.getRowsByName(restData, root);

    if (rows !== null && ids !== null && names !== null) {
        for (var i = 0; i < ids.length; i++) {
            var id = ids[i];
            var name = names[i];
            var group = groups[i];

            var item = {}; // = utils.cloneObj(rows[0]); // 資料太多, 共用測試資料
            for (var k = 0; k < rows.length; k++) {
                if (rows[k].id === id || rows[k].id === "") {
                    item = utils.cloneObj(rows[k]);
                }
            }

            item.id = id;
            item.name = name;
            item.group = group;

            list.push(item);
        }
    }

    return list;
}
