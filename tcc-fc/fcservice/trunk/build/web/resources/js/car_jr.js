jQuery(function() {
    updateAllData();
});

function updateAllData(){
       
    //更新等待區車次資料，包含熟料車、散泥車、袋泥車       
    updateSingleQueueCar('car1CarsBlock', 'jsonCar1Infos', 'car1_', 'car1Total', 21, '車');
    updateSingleQueueCar('car2CarsBlock', 'jsonCar2Infos', 'car2_', 'car2Total', 21, '車');
    updateSingleQueueCar('car3CarsBlock', 'jsonCar3Infos', 'car3_', 'car3Total', 21, '車');
    
    //裝船等待區區塊
    updateSingleQueueCar('cementWaitingShipsBlock', 'jsonShip1Infos', 'cementShip1_', 'cementWaitingTotal', 21, '車');
    updateSingleQueueCar('scatterWaitingShipsBlock', 'jsonShip2Infos', 'scatterShip1_', 'scatterWaitingTotal', 21, '車');
    
    //處理中區塊
    updateSD('jsonSDLoadingInfos');
    updatePK('jsonPKLoadingInfos');//袋裝
    updateTK('jsonTKLoadingInfos');//太空包
    updateSL('jsonSLLoadingInfos');//散泥
    updateSingleQueueCar('AShipDiv_box', 'jsonALoadingInfos', 'A_', 'AShipTotal', 7, '船'); //1#大裝船機
    updateSingleQueueCar('MShipDiv_box', 'jsonMLoadingInfos', 'M_', 'MShipTotal', 7, '船'); //2#大裝船機
    updateSingleQueueCar('CShipDiv_box', 'jsonCLoadingInfos', 'C_', 'CShipTotal', 7, '船'); //1#小裝船機
    updateSingleQueueCar('XShipDiv_box', 'jsonBZLoadingInfos', 'BZ_', 'XShipTotal', 7, '船'); //2#小裝船機
    
    updateSingleQueueCar('AABlock', 'jsonTKWSLoadingInfos', 'AA_', 'AATotal', 8, '車'); //太空包倉庫(TK)
    updateSingleQueueCar('BBBlock', 'jsonSLWSLoadingInfos', 'BB_', 'BBTotal', 10, '車'); //熟料倉庫(SL)
    updateSingleQueueCar('CCBlock', 'jsonZKWSLoadingInfos', 'CC_', 'CCTotal', 7, ''); //袋裝倉庫(ZK, ZK1)
    
    //待出廠區塊
    updateOutputingArea();
}
            


/*
  *  更新單一Queue圖示
  *  carsBlockId: 區塊Id
  *  jsonValId: json字串Id
  *  carSpanIdHeader: 要更新的車次ID Prefix
  *  totalId: 總數Id
  *  blockMaxCar: 區塊最大車次
  *  unitName: 單位名稱(車、船)
  *  例: car3CarsBlock, jsonCar3Infos, car3_, car3Total
 */
function updateSingleQueueCar(carsBlockId, jsonValId, carSpanIdHeader, totalId, blockMaxCar, unitName){
    //      alert( jQuery("#"+jsonValId).val());
    var result = jQuery("#"+jsonValId).val();

    var lastCarToolTip = "<br/><br/>";//記錄最後一台車的ToolTip(超出最大車次時要累積車號)
    
    //Reset
    for (var i=0; i<blockMaxCar; i++){
        var index = i+1;
        jQuery("#"+carSpanIdHeader+index.toString()).hide();
    }                                

    if (result.length==0){        
        return;
    }

    //總數
    var total = 0;
    jQuery.each(eval(result), function (key, val) {
        //          alert('key='+key+';val='+val);
        total++;
        var index = key+1;
        if (index>blockMaxCar){
            lastCarToolTip = lastCarToolTip+'&nbsp;'+val.truckno;
            return true;//continue
        }
        
        var objId = "#"+carSpanIdHeader+index.toString();    

        //依carType決定圖示
        var className = '';
        
        if (val.carType==1){
            className = "carType1";
        }else if (val.carType==2){
            className = "carType2";
        }else{
            className = "carType3"
        }    

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);          
    });	             
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    if (total>blockMaxCar){
        var lastBlockId = carSpanIdHeader+blockMaxCar.toString();
        handleLastRedToolTip(lastBlockId, lastCarToolTip);
    }
                
    jQuery('#'+totalId).text(total.toString()+' '+unitName);                
    
    //init toolTip
    initToolTip(carsBlockId, total);

}	
            

//SD: 散泥1-2期:            
function updateSD(jsonStringId){
    var carSpanIdHeader = 'SD';
    var result = jQuery("#"+jsonStringId).val();
    //Queue總筆數
    var queueSize = 11;
    //記錄SN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);      
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#SD1_1CarTotal').text('0車');
    jQuery('#SD1_2CarTotal').text('0車');
    jQuery('#SD1_3CarTotal').text('0車');
    jQuery('#SD2_1CarTotal').text('0車');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Q1-Q2最大值為7筆
        if (i<2){
            snQueueMaxCar[i]=7;
        }
        //Q3最大值為5筆
        else if (i==2){
            snQueueMaxCar[i]=5;
        }
        //Q4-Q7不存在
        else  if (i>=3 && i<=6){
            snQueueMaxCar[i]=0;
        }
        else{
            //其它Queue最大值為6筆
            snQueueMaxCar[i]=6;
        }
    }
    
    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);
    
    //處理每一個item
    jQuery.each(eval(result), function (key, val) {
        //取得在哪一個Queue
        var queueNo = getQueueNo(val.portId, carSpanIdHeader);
        //將該Queue總數+1
        var total = snQueue[queueNo-1]+1;
        //記錄該Queu筆數(+1)
        snQueue[queueNo-1] =total;        
        //處理最後一筆資料的toolTip
        if (total>snQueueMaxCar[queueNo-1]){
            lastCarToolTips[queueNo-1]  = lastCarToolTips[queueNo-1]+'&nbsp;'+val.truckno;
            //            alert('queueNo='+queueNo+';total='+total+';lastCarToolTips='+lastCarToolTips[queueNo-1]);
            return true;//continue
        }               

        //取得item id
        var objId = "#"+carSpanIdHeader+queueNo+"_"+total.toString();    
        var tt = carSpanIdHeader+queueNo;

        //SD1-11用直的圖
        var className = '';
            className = "carType1_1";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);          
    });	      
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //設定各區塊車次總數
    //散泥1期1、2
    var SD1CarTotal = calculateCarNum(snQueue, 1, 2);
    jQuery('#SD1_1CarTotal').text(SD1CarTotal);
    
    //散泥1期2(3)
    var SD2CarTotal = snQueue[2];
    jQuery('#SD1_2CarTotal').text(SD2CarTotal);
    
    //散泥1期3(8、9)
    var SD3CarTotal = calculateCarNum(snQueue, 8, 9);
    jQuery('#SD1_3CarTotal').text(SD3CarTotal);    
    
    //散泥2期3(10、11)
    var SD4CarTotal = calculateCarNum(snQueue, 10, 11);
    jQuery('#SD2_1CarTotal').text(SD4CarTotal);    
    
    //init toolTip
    initToolTip('SD1Block', SD1CarTotal);
    initToolTip('SD2Block', SD2CarTotal);     
    initToolTip('SD3Block', SD3CarTotal);     
    initToolTip('SD4Block', SD4CarTotal);     
}


//PK: 袋裝:
function updatePK(jsonStringId){
    var carSpanIdHeader = 'PK';
    var result = jQuery("#"+jsonStringId).val();
    //DN相關queue總數
    var queueSize = 3;
    //記錄DN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#pk1Total').text('0');
    jQuery('#pk2Total').text('0');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue3最大值為5筆
        if (i==2){
            snQueueMaxCar[i]=5;
        }
        //Queue1 - Queue2最大值為7筆
        else{
            snQueueMaxCar[i]=7;
        }
    }

    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);
    
    //處理每一個item
    jQuery.each(eval(result), function (key, val) {
        //取得在哪一個Queue
        var queueNo = getQueueNo(val.portId, carSpanIdHeader);
        if (queueNo.length==0){
            queueNo = 1;
        }
        
        //將該Queue總數+1
        var total = snQueue[queueNo-1]+1;
        //記錄該Queu筆數(+1)
        snQueue[queueNo-1] =total;        
        //處理最後一筆資料的toolTip
        if (total>snQueueMaxCar[queueNo-1]){
            lastCarToolTips[queueNo-1]  = lastCarToolTips[queueNo-1]+'&nbsp;'+val.truckno;
            //            alert('queueNo='+queueNo+';total='+total+';lastCarToolTips='+lastCarToolTips[queueNo-1]);
            return true;//continue
        }               
        
        //取得item id
        var objId = "#"+carSpanIdHeader+queueNo+"_"+total.toString();    
        //用直的圖
        var className = "carType3_1";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	     
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //包裝1期總車數(queue1 - queue4)
    var PK1CarTotal = calculateCarNum(snQueue, 1, 2);
    jQuery('#pk1Total').text(PK1CarTotal);    
    var PK2CarTotal = snQueue[2];
    jQuery('#pk2Total').text(PK2CarTotal+' 車');    
    //init toolTip
    initToolTip('pk1Block', PK1CarTotal);
    initToolTip('pk2Block', PK2CarTotal);
}            
            
            
//TK: 太空包:
function updateTK(jsonStringId){
    var carSpanIdHeader = 'TK';
    var result = jQuery("#"+jsonStringId).val();
    //DN相關queue總數
    var queueSize = 3;
    //記錄DN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#tk1Total').text('0');
    jQuery('#tk2Total').text('0');
    jQuery('#tk3Total').text('0');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue1最大值為7筆
        if (i==0){
            snQueueMaxCar[i]=7;
        }        
        //Queue2最大值為5筆
        if (i==1){
            snQueueMaxCar[i]=5;
        }
        //Queue3 最大值為6筆
        else{
            snQueueMaxCar[i]=6;
        }
    }

    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);
    
    //處理每一個item
    jQuery.each(eval(result), function (key, val) {
        //取得在哪一個Queue
        var queueNo = getQueueNo(val.portId, carSpanIdHeader);
        if (queueNo.length==0){
            queueNo = 1;
        }
        
        //將該Queue總數+1
        var total = snQueue[queueNo-1]+1;
        //記錄該Queu筆數(+1)
        snQueue[queueNo-1] =total;        
        //處理最後一筆資料的toolTip
        if (total>snQueueMaxCar[queueNo-1]){
            lastCarToolTips[queueNo-1]  = lastCarToolTips[queueNo-1]+'&nbsp;'+val.truckno;
            //            alert('queueNo='+queueNo+';total='+total+';lastCarToolTips='+lastCarToolTips[queueNo-1]);
            return true;//continue
        }               
        
        //取得item id
        var objId = "#"+carSpanIdHeader+queueNo+"_"+total.toString();    
        //用直的圖
        var className = "carType3_1";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	     
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //包裝1期總車數(queue1 - queue4)
    var TK1CarTotal =snQueue[0];
    jQuery('#tk1Total').text(TK1CarTotal);    
    var TK2CarTotal = snQueue[1];
    jQuery('#tk2Total').text(TK2CarTotal);    
    var TK3CarTotal = snQueue[2];
    jQuery('#tk3Total').text(TK3CarTotal);        
    //init toolTip
    initToolTip('tk1Block', TK1CarTotal);
    initToolTip('tk2Block', TK2CarTotal);
    initToolTip('tk3Block', TK3CarTotal);
}                        


//TK: 散泥:
function updateSL(jsonStringId){
    var carSpanIdHeader = 'SL';
    var result = jQuery("#"+jsonStringId).val();
    //相關queue總數
    var queueSize = 3;
    //記錄相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#sl1Total').text('0');
    jQuery('#sl2Total').text('0');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue3 最大值為6筆
            snQueueMaxCar[i]=6;
    }

    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);
    
    //處理每一個item
    jQuery.each(eval(result), function (key, val) {
        //取得在哪一個Queue
        var queueNo = getQueueNo(val.portId, carSpanIdHeader);
        if (queueNo.length==0){
            queueNo = 1;
        }
        
        //將該Queue總數+1
        var total = snQueue[queueNo-1]+1;
        //記錄該Queu筆數(+1)
        snQueue[queueNo-1] =total;        
        //處理最後一筆資料的toolTip
        if (total>snQueueMaxCar[queueNo-1]){
            lastCarToolTips[queueNo-1]  = lastCarToolTips[queueNo-1]+'&nbsp;'+val.truckno;
            //            alert('queueNo='+queueNo+';total='+total+';lastCarToolTips='+lastCarToolTips[queueNo-1]);
            return true;//continue
        }               
        
        //取得item id
        var objId = "#"+carSpanIdHeader+queueNo+"_"+total.toString();    
        //用橫的圖
        var className = "carType2";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	     
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //包裝1期總車數(queue1 - queue4)
    var SL1CarTotal =snQueue[0];
    jQuery('#sl1Total').text(SL1CarTotal+' 車');    
    var SL2CarTotal = snQueue[1];
    jQuery('#sl2Total').text(SL2CarTotal+' 車');    
    //init toolTip
    initToolTip('SL1Block', SL1CarTotal);
    initToolTip('SL2Block', SL2CarTotal);
}                        

//"待出廠"區塊
function updateOutputingArea(){
    var maxCarNum = 16;
    //      alert( jQuery("#jsonOutputInfos").val());
    var result = jQuery("#jsonOutputInfos").val();        

    for (var i=0; i<maxCarNum; i++){
        var index = i+1;
        jQuery("#toLeave_"+index.toString()).hide();
    }       
        
    //Reset
    jQuery('#toLeaveCarTotal').text('0 車');        

    if (result.length==0){        
        return;
    }

    var total = 0;    
    jQuery.each(eval(result), function (key, val) {
        //          alert('key='+key+';val='+val);

        var index = key+1;
        var objId = "#toLeave_"+index.toString();    
            
        //依carType決定圖示
        var className = '';
        
        if (val.carType==1){
            className = "carType1";
        }else if (val.carType==2){
            className = "carType2";
        }else{
            className = "carType3"
        }    

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);  
        
        total++;
    });	     
        
    if (total==0){
        return;
    }    
        
    jQuery('#toLeaveCarTotal').text(total+' 車');        

    //init toolTip
    initToolTip('toLeaveCarsBlock', total);

}           