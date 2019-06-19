/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function ($) {
    // zh-TW
   $.DateTimePicker.i18n["zh-TW"] = $.extend($.DateTimePicker.i18n["zh-TW"], {

        language: "zh-TW",
        labels: {
            'year': '年',
            'month': '月',
            'day': '日',
            'hour': '時',
            'minutes': '分',
            'seconds': '秒',
            'meridiem': '午'
        },
        dateTimeFormat: "yyyy-MM-dd HH:mm",
        dateFormat: "yyyy-MM-dd",
        timeFormat: "HH:mm",

        shortDayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        fullDayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        shortMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
        fullMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],

        titleContentDate: "設置日期",
        titleContentTime: "設置時間",
        titleContentDateTime: "設置日期和時間",

        setButtonContent: "設置",
        clearButtonContent: "清除",
        formatHumanDate: function (oDate, sMode, sFormat) {
            if (sMode === "date")
                return  oDate.dayShort + ", " + oDate.yyyy + "年" +  oDate.month +"月" + oDate.dd + "日";
            else if (sMode === "time")
                return oDate.HH + "時" + oDate.mm + "分" + oDate.ss + "秒";
            else if (sMode === "datetime")
                return oDate.dayShort + ", " + oDate.yyyy + "年" +  oDate.month +"月" + oDate.dd + "日 " + oDate.HH + "時" + oDate.mm + "分";
        }
    });

    // zh-CN
    $.DateTimePicker.i18n["zh-CN"] = $.extend($.DateTimePicker.i18n["zh-CN"], {

        language: "zh-CN",
        labels: {
            'year': '年',
            'month': '月',
            'day': '日',
            'hour': '时',
            'minutes': '分',
            'seconds': '秒',
            'meridiem': '午'
        },
        dateTimeFormat: "yyyy-MM-dd HH:mm",
        dateFormat: "yyyy-MM-dd",
        timeFormat: "HH:mm",

        shortDayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        fullDayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        shortMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
        fullMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],

        titleContentDate: "设置日期",
        titleContentTime: "设置时间",
        titleContentDateTime: "设置日期和时间",

        setButtonContent: "设置",
        clearButtonContent: "清除",
        formatHumanDate: function (oDate, sMode, sFormat) {
            if (sMode === "date")
                return oDate.dayShort + ", " + oDate.yyyy + "年" +  oDate.month +"月" + oDate.dd + "日";
            else if (sMode === "time")
                return oDate.HH + "时" + oDate.mm + "分" + oDate.ss + "秒";
            else if (sMode === "datetime")
                return oDate.dayShort + ", " + oDate.yyyy + "年" +  oDate.month +"月" + oDate.dd + "日 " + oDate.HH + "时" + oDate.mm + "分";
        }
    });

    // en-US
    $.DateTimePicker.i18n["en-US"] = $.extend($.DateTimePicker.i18n["en-US"], {
        Language: "en-US",
        Labels: {
            'year': 'year',
            'month': 'month',
            'day': 'day',
            'hour': 'h',
            'minutes': 'minute',
            'seconds': 'seconds',
            'meridiem': 'noon'
        },
        dateTimeFormat: "yyyy-MM-dd HH:mm",
        dateFormat: "yyyy-MM-dd",
        timeFormat: "HH:mm",

        shortDayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        fullDayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        shortMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12' ],
        fullMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12' ],

        titleContentDate: "Set Date",
        titleContentTime: "Set time",
        titleContentDateTime: "Set date and time",

        setButtonContent: "Settings",
        clearButtonContent: "Clear",
        formatHumanDate: function (oDate, sMode, sFormat) {
            if (sMode === "date")
                return oDate.dayShort + ", " + oDate.yyyy + "year" + oDate.month + "month" + oDate.dd + "day";
            else if (sMode === "time")
                return oDate.HH + "hour" + oDate.mm + "minute" + oDate.ss + "seconds";
            else if (sMode === "datetime")
                return oDate.dayShort + ", " + oDate.yyyy + "year" + oDate.month + "month" + oDate.dd + "day" + oDate.HH + "hour" + oDate.mm + "minute";
        }
    });

    // tr-TR
   $.DateTimePicker.i18n["tr-TR"] = $.extend($.DateTimePicker.i18n["tr-TR"], {
        language: "tr-TR",
        labels: {
            'year': 'yıl',
            'month': 'ay',
            'day': 'gün',
            'hour': 'ne zaman',
            'minutes': 'bölmek',
            'seconds': 'ikinci',
            'meridiem': 'öğlen'
        },
        dateTimeFormat: "yyyy-MM-dd HH:mm",
        dateFormat: "yyyy-MM-dd",
        timeFormat: "HH:mm",

        shortDayNames: ['Pazar', 'Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma', 'Cumartesi'],
        fullDayNames: ['Pazar', 'Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma', 'Cumartesi'],
        shortMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
        fullMonthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],

        titleContentDate: "Tarihi ayarla",
        titleContentTime: "Zaman ayarla",
        titleContentDateTime: "Tarih ve saati ayarla",

        setButtonContent: "kurmak",
        clearButtonContent: "kaldırmak",
        formatHumanDate: function (oDate, sMode, sFormat) {
            if (sMode === "date")
                return  oDate.dayShort + ", " + oDate.yyyy + "yıl" +  oDate.month +"ay" + oDate.dd + "gün";
            else if (sMode === "time")
                return oDate.HH + "ne zaman" + oDate.mm + "bölmek" + oDate.ss + "ikinci";
            else if (sMode === "datetime")
                return oDate.dayShort + ", " + oDate.yyyy + "yıl" +  oDate.month +"ay" + oDate.dd + "gün " + oDate.HH + "ne zaman" + oDate.mm + "bölmek";
        }
    });

})(jQuery);
