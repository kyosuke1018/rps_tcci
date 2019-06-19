/* 
 * dependencys : consts.js
 */
/* global variables, LOG_LEVEL */
var _renderThisPage = null;
var _menus = {};
var _func = {};// 目前功能
var _asyncCompleteFlags = {};// 非同步工作控制
var _maxWaittingTimes = 20;// 非同步工作等待最大次數
var _waittingTimer = 0;// for waitting async jobs completed
var _timerMap = {};// 特殊需要，控制延後執行指定工作
var _language = null;// 語系
var _needCheckLogin = true;// 是否需檢查登入
var _doMobileLogin = false;// 手機轉址
var _doInternalLogin = false;// 內部轉址
var _notLoginAlert = false;// 控制401錯誤警示一次即可
var _myStores = [];
var _tabId = null;// for open new tab 
var _menuWidth = 180;// 子選單寬度
var _tccDealer = false;// 台泥供應商 - 使用EC1.5
var _storeOwner = false;
var _fiUser = false;
var _adminUser = false;

var _isMobile = {
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (this.Android() || this.BlackBerry() || this.iOS() || this.Opera() || this.Windows());
    }
};
