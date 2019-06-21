var LANG = "C";
var pubType = "N";

$(function(){
    $("#header").html(getHeader());
    $("#footer").html(getFooter());
    $("#webMap").html(getWebMap());
    
    showLoadingBefore("#footerField");// loading

    window.onresize = function(){
        if(getDomWidth() < 980)
            $(".ui-datatable table").css("width", "980px");
    };

    // 先查總筆數，再顯示DataTable
    // var pubType = GetUrlString("type");//1;
    var resturl = URL_DOC_COUNT + pubType + "?lang=" + LANG;// 先查總筆數
    console.log(resturl);
    fetchData(resturl, false);
});

// 顯示查詢結果
function renderPage(response){
    // var pubType = GetUrlString("type");//1;
    var totalRows = response.totalRows;
    console.log("totalRows="+totalRows);
    
    renderRecordTable(totalRows, pubType, false);
}

// render data table
function renderRecordTable(totalRows, pubType, retry) {
    var pageSize = 5;
    $('.mediaList').puidatatable({
        lazy: true,
        // responsive: true,
        selectionMode: "single",
        emptyMessage: "查無資料!",
        paginator: {
            rows: pageSize,
            totalRecords: totalRows
        },
        columns: [
            {field: "dataDateStr", headerText: "日期", sortable: true},
            {field: "title", headerText: "文章",
                content: genContent, // 產生文章欄位值
                sortable: true
            }
        ],
        datasource: function(callback, ui) {
            var self = this;
            var sortField = ui.sortField;
            var sortOrder = ui.sortOrder;
            var offset = ui.first;
            var dataurl = URL_DOC_LIST + pubType + "?lang=" + LANG
                        + "&offset=" + offset + "&rows=" + pageSize 
                        + "&sortField=" + sortField + "&sortOrder=" + sortOrder;
            console.log(dataurl);
            $.ajax({
                type: "GET",
                url: dataurl,
                contentType: "application/json; charset=utf-8",
                context: this,
                success: function (response) {
                    var localData = response.list;
                    // alert(localData);
                    callback.call(self, localData);
                    afterSearch(pubType, totalRows);
                },
                error: function (response) {
                    if( !retry && response!==undefined && response.status===0 ){
                        renderRecordTable(totalRows, pageSize, pubType, !retry);// status=0時，重試一次
                    }else{
                        restErrorHandler(response);
                    }
                }
            });
        },
        rowSelect: function(event, data){
            selectDoc(data);
        }
    });
}

// 產生文章欄位值
function genContent(data){
    var html = "";
    html += '<span class="title">' + escapeHtml(data.title) + '</span>';// 標題

    if( data.urls !== undefined ){// for 多檔案
        html += '&nbsp;';
        $.each(data.urls, function(i, obj){
            var target = (data==='N')?"_blank":"_self";
            html += ' <a target="'+target+'" href="' + obj + '"><u>[檔案' + (i+1) + ']</u></a>';
        });
    }

    if( data.summary !== undefined ){// 大綱
        html += '</br>';
        html += '<div class="outline" >'+escapeHtml(data.summary)+'</div>';                        
    }

    return html;
}

function afterSearch(pubType, totalRows){
    $(".ui-datatable thead th:eq(1)").css("text-align", "left");
    $(".ui-datatable tbody tr").find("td:eq(0)").css("font-size", "13px");
    $(".ui-datatable tbody tr").find("td:eq(0)").css("color", "#808080");
    
    $('#caption').html("共 " + totalRows + " 筆");

    if(getDomWidth() < 980)
        $(".ui-datatable table").css("width", "980px");        

    setTimeout(function(){
        adjContainerHeight();
    }, 100);

    hideLoading();// 隱藏 loading image
}