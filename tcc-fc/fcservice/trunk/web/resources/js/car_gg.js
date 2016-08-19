jQuery(function() {
    updateAllData();
});

function updateAllData(){
       
    //等待區區塊        
    updateWatingCar('car1CarsBlock', 'jsonCar1Infos', 'car1_', 'car1Total');
    updateWatingCar('car2CarsBlock', 'jsonCar2Infos', 'car2_', 'car2Total');
    updateWatingCar('car3CarsBlock', 'jsonCar3Infos', 'car3_', 'car3Total');
    
    //處理中區塊
    updateSN('jsonSNLoadingInfos');
    updateDN('jsonDNLoadingInfos');
    updateSLK('jsonSLKLoadingInfos');
    updateSL('jsonSLLoadingInfos');
    
    //待出廠區塊
    updateOutputingArea();
}
            
            
/*
      *  更新等待區車次資料，包含熟料車、散泥車、袋泥車
      *  例: car3CarsBlock, jsonCar3Infos, car3_, car3Total
     */
function updateWatingCar(carsBlockId, jsonValId, carSpanIdHeader, totalId){
    //      alert( jQuery("#"+jsonValId).val());
    var result = jQuery("#"+jsonValId).val();

    //每個區塊最大車次
    var blockMaxCar = 21;
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
                
    jQuery('#'+totalId).text(total);                
    
    //init toolTip
    initToolTip(carsBlockId, total);

}		            
            

//SN: 散泥1-2期:            
function updateSN(jsonStringId){
    var carSpanIdHeader = 'SN';
    var result = jQuery("#"+jsonStringId).val();
    //Queue總筆數
    var queueSize = 9;
    //記錄SN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);      
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#SN1CarTotal').text('0車');
    jQuery('#SN2CarTotal').text('0車');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue1 - Queue9最大值為6筆        
        snQueueMaxCar[i]=6;
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
        if (tt=='SN9'){
            alert('objId='+objId);
        }
        //SN1-9用橫的圖
        var className = '';
            className = "carType1";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);          
    });	      
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //設定各區塊車次總數
    //散泥1期(queue1 - queue4)
    var SN1CarTotal = calculateCarNum(snQueue, 1, 4);
    jQuery('#SN1CarTotal').text(SN1CarTotal+'車');
    
    //散泥2期(queue5 - queue10)
    var SN2CarTotal = calculateCarNum(snQueue, 5, 8);
    jQuery('#SN2CarTotal').text(SN2CarTotal+'車');
    
    //init toolTip
    initToolTip('SN1Block', SN1CarTotal);
    initToolTip('SN2Block', SN2CarTotal);     
}


//DN: 包裝1期:
function updateDN(jsonStringId){
    var carSpanIdHeader = 'DN';
    var result = jQuery("#"+jsonStringId).val();
    //DN相關queue總數
    var queueSize = 4;
    //記錄DN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
    jQuery('#DN1CarTotal').text('0車');
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue1 - Queue4最大值為5筆
        snQueueMaxCar[i]=6;
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
        //DN1-4用橫的圖
        var className = "carType3";

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	     
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //包裝1期總車數(queue1 - queue4)
    var DN1CarTotal = calculateCarNum(snQueue, 1, 4);
    jQuery('#DN1CarTotal').text(DN1CarTotal+'車');    
    
    //init toolTip
    initToolTip('DN1Block', DN1CarTotal);
}            
            
//檢斤庫#1 
function updateSLK(jsonStringId){
    var carSpanIdHeader = 'SLK';
    var result = jQuery("#"+jsonStringId).val();
    //SLK相關queue總數
    var queueSize = 2;
    //記錄SLK相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄SLK每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);   
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue9 - Queue10最大值為6筆		
        snQueueMaxCar[i]=6;
    }
    
    //Hide All Element ... TODO 不確定是9, 10還是1, 
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);    
//    for (var i=0; i<queueSize; i++){
//        var index = i+1;
//        for (var j=0; j<snQueueMaxCar[i]; j++){
//            var index2 = j+1;
//            var objId = "#"+carSpanIdHeader+(index+8).toString()+"_"+index2.toString();
//            var obj =jQuery(objId);
//            
//            if (obj.length>0){
//                obj.hide();
//            }
//        }                  
//    } 

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
        //        alert('objId='+objId);
        //SLK用橫的圖
        var className = 'carType2';
   
        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
        
    });	                    
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //設定各區塊車次總數
    //檢斤庫#1(queue9)
    jQuery('#weigh1Div_Total').text(snQueue[0]+'車');        
    //檢斤庫#2(queue2)
    jQuery('#weigh2Div_Total').text(snQueue[1]+'車');        
    
    
    //init toolTip
    initToolTip('SLK1Block', snQueue[0]);
    initToolTip('SLK2Block', snQueue[1]);
    
}                    
            
//熟料1-2期            
function updateSL(jsonStringId){
    var carSpanIdHeader = 'SL';
    var result = jQuery("#"+jsonStringId).val();    
    //SL相關queue總數
    var queueSize = 8;    
    //記錄DN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);        
    
    //Reset
    jQuery('#SL1CarTotal').text('0車');
    jQuery('#SL2CarTotal').text('0車');    
     
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue1 - Queue8最大值為6筆		
        snQueueMaxCar[i]=6;
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
        //SL1-8用橫的圖
        var className = 'carType2';
        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);        
        
    });	                    
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //熟料1期(queue1 - queue4)
    var SL1CarTotal = calculateCarNum(snQueue, 1, 4);
    jQuery('#SL1CarTotal').text(SL1CarTotal+'車');    
    
    //熟料2期(queue5 - queue8)
    var SL2CarTotal = calculateCarNum(snQueue, 5, 8);
    
    jQuery('#SL2CarTotal').text(SL2CarTotal+'車');    

    //init toolTip
    initToolTip('SL1Block', SL1CarTotal);
    initToolTip('SL2Block', SL2CarTotal);
                    
}                    

//"待出廠"區塊
function updateOutputingArea(){
    var maxCarNum = 27;
    //      alert( jQuery("#jsonOutputInfos").val());
    var result = jQuery("#jsonOutputInfos").val();        

    for (var i=0; i<maxCarNum; i++){
        var index = i+1;
        jQuery("#toLeave_"+index.toString()).hide();
    }       
        
    //Reset
    jQuery('#toLeaveCarTotal').text('0車');        

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
        
    jQuery('#toLeaveCarTotal').text(total+'車');        

    //init toolTip
    initToolTip('toLeaveCarsBlock', total);

}           