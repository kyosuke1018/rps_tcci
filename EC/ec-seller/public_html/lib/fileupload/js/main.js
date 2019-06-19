/*
 * jQuery File Upload Plugin JS Example
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * https://opensource.org/licenses/MIT
 */
/* global $, window, PRD_PIC_UPLOAD_URL, CHECK_CORS_URL, FIELD_LANG, UF_MAX_FILES, UF_MAX_MB, utils */
var prdId = utils.getUrlParameter("prdId");
var storeId = utils.getUrlParameter("storeId");
var uploadRestUrl = PRD_PIC_UPLOAD_URL.replace("{prdId}", prdId);
console.log("main.js storeId = "+storeId+", prdId = "+prdId);

function resize() {
    parent.document.getElementById("ifUpload").height = document.body.scrollHeight; //將子頁面高度傳到父頁面框架
    console.log(parent.document.getElementById("ifUpload").height);
}

$(function () {
    'use strict';
    parent.showMultiUploadDiv(false);
    
    // i18n language
    var langValue = utils.getLanguage(utils.getSession(FIELD_LANG));
    console.log("main.js langValue = "+langValue);
    loadI18nProperties("../../i18n/");

    _adminUser = utils.isAdminUser();
    uploadRestUrl = _adminUser?utils.addUrlQueryParam(uploadRestUrl, "storeId", storeId):uploadRestUrl;
    console.log("uploadRestUrl = ", uploadRestUrl);
    
    $('#btnSelFile').click(function(event){
        // 重新送審確認
        if( !parent.confirmReapprove() ){
            event.preventDefault();
            return;
        }
    });
    
    $('#btnSelFile').change(function(event){
        console.log("fileupload btnSelFile change ...");
        //resize();
    });

    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: uploadRestUrl
    });

    // Enable iframe cross-domain access via redirect option:
    $('#fileupload').fileupload(
        'option',
        'redirect',
        window.location.href.replace(
            /\/[^\/]*$/,
            'cors/result.html?%s'
        )
    );
   
    $('#fileupload').fileupload('option', {
        url: uploadRestUrl,
        paramName: 'files',
        maxNumberOfFiles: UF_MAX_FILES,
        // The maximum allowed file size in bytes:
        maxFileSize: UF_MAX_MB*1024*1024, // 2 MB
        acceptFileTypes: /(\.|\/)(gif|jp?eg|png)$/i,
        messages: {
            maxNumberOfFiles: utils.i18nWP("fu.maxNumberOfFiles", UF_MAX_FILES),
            acceptFileTypes: utils.i18n("fu.acceptFileTypes"),
            maxFileSize: utils.i18n("fu.maxFileSize", UF_MAX_MB),
            minFileSize: utils.i18n("fu.minFileSize")
        },
        start: function(event, data){
            console.log("fileupload start data = ", data);
            parent.showMultiUploadDiv(false);
        },
        completed: function(event, data){
            console.log("fileupload completed data = ", data);
            if( !parent.afterReapprove() ){// 重送審也會重載
                // 重載現有商品圖片
                parent.buildPrdPicGalleria("prdPics");
                parent.reloadDataTable('#tbProductLazy', true);// keep status reload
                parent.showMultiUploadDiv(true);
            }
        },
        failed: function(event, data){
            console.log("fileupload failed data = ", data);
            if( isNA(data) || isNA(data.errorThrown) 
                    || data.errorThrown!=='abort' ){// 非取消
                alert(utils.i18n("fs.upload.fail"));
                parent.showMultiUploadDiv(true);
            }
        },
        finish: function(event, data){
            console.log("fileupload finish data = ", data);
            parent.showMultiUploadDiv(true);
        },
        destroyed: function (e, data){
            console.log("fileupload destroyed = ", data);
            parent.buildPrdPicGalleria("prdPics");
            parent.reloadDataTable('#tbProductLazy', true);// keep status reload
        },
        destroyfailed: function(event, data){
            alert(utils.i18n("fs.remove.fail"));
        }
    });
    
    parent.showMultiUploadDiv(true);
});
