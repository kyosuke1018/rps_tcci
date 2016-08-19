
                
jQuery(function() {
    updateAllData();

});

function updateAllData(){
       
        //等待區區塊        
    updateWatingCar('car1CarsBlock', 'jsonCar1Infos', 'car1_', 'car1Total');
    updateWatingCar('car2CarsBlock', 'jsonCar2Infos', 'car2_', 'car2Total');
    updateWatingCar('car3CarsBlock', 'jsonCar3Infos', 'car3_', 'car3Total');
    
    //裝船等待區區塊
    updateWatingCar('cementWaitingShipsBlock', 'jsonShip1Infos', 'cementShip1_', 'cementWaitingTotal');
    updateWatingCar('scatterWaitingShipsBlock', 'jsonShip2Infos', 'scatterShip1_', 'scatterWaitingTotal');
    
    //處理中區塊
    updateSN('jsonSNLoadingInfos');
    updateDN('jsonDNLoadingInfos');
    updateSLK('jsonSLKLoadingInfos');
    updateSL('jsonSLLoadingInfos');
    updateYDMT('jsonYDMTLoadingInfos');
    
    //待出廠區塊
    updateOutputingArea();
}
            
            
/*
 * 依PortId取得在哪一個Queue，若PortId包含'-'則過濾掉-後的字串
 */
function getQueueNo(portId, qName){
    //取得在哪一個Queue
        var queueNo = portId.replace(qName, "");
        //過濾掉-後的字串
        if (queueNo.indexOf("-")>0){
            queueNo = queueNo.substring(0,queueNo.indexOf("-"));
        }
        
        return queueNo;
}            
            

/*
 * 設定ToolTip: 傳入DIV的Id，此function會將該DIV下的link加入ToolTip.
 * 傳入TotalNo是避免=0時出錯。
 */
function initToolTip(blockId, total){
    if (total==0){
        return;
    }
    jQuery('#'+blockId+' a').tooltip({
        track: true,
        delay: 0,
        showURL: false,
        showBody: " - ",
        fade: 250
    });              
}            

/* 當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip:
 * lastBlockId: 最後一個Block(span)的ID
 * lastCarToolTip: 要放入的ToolTip...由原程式累積。
 */
function handleLastRedToolTip(lastBlockId, lastCarToolTip){
        var lastObjId = "#"+lastBlockId; 
//        alert('===>lastObjId='+lastObjId);
        lastCarToolTip =  jQuery(lastObjId+" a").attr('title')+lastCarToolTip;
//        alert('===>lastCarToolTip='+lastCarToolTip);
        jQuery(lastObjId+" a").attr('title', lastCarToolTip); //設定toolTip
        
        //設定為紅色
        var oldClassName = jQuery(lastObjId+" a").attr('class');
//        alert('oldClassName='+oldClassName+'r');
        jQuery(lastObjId+" a").removeClass();
        jQuery(lastObjId+" a").addClass(oldClassName+'r');
        
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

        //設定圖示
        jQuery(objId+" a").addClass(className);          

        jQuery(objId).show();
        jQuery(objId+" a").attr('title', val.toolTip); //設定toolTip
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
            
	

//隱藏所有Element
function hideMultiQueueAllElement(queueSize, queueMaxCars, carSpanIdHeader){
//    alert(queueSize+'; '+queueMaxCars+';'+carSpanIdHeader);
    //Hide All Element
    for (var i=0; i<queueSize; i++){
        var index = i+1;
        for (var j=0; j<queueMaxCars[i]; j++){
            var index2 = j+1;
            var objId = "#"+carSpanIdHeader+index.toString()+"_"+index2.toString();
            var obj =jQuery(objId);
            
            if (obj.length>0){
                obj.hide();
            }
        }                  
    }        
}

//計算指定Queue中，車(船)總數
function calculateCarNum(queue, startIdx, endIdx){
    var total = 0;
    for (idx=startIdx-1; idx<endIdx; idx++){
        total += queue[idx];
    }
    return total;
}

/**
 * 處理多個Queue時，最後一筆紅色ToolTip
 * carSpanIdHeader:  Span Header
 * queueSize: Queue的長度
 * snQueue: Array, 記錄每一個Queue中存在筆數
 * snQueueMaxCar: 記錄每一個Queue中最大筆數
 * lastCarToolTips: 記錄每一個Queue中最後一筆ToolTip
 */
function handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips){
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    for (i=0; i<queueSize; i++){
        var qNum = snQueue[i];
        
        if (qNum>snQueueMaxCar[i]){
//            alert('i='+i+';qNum='+qNum);
            var idx = i+1;
            var lastIdx = snQueueMaxCar[i];
            var lastBlockId = carSpanIdHeader+idx.toString()+'_'+lastIdx.toString();            
//            alert('lastBlockId='+lastBlockId+';'+'lastCarToolTips['+i+']='+lastCarToolTips[i]);
            handleLastRedToolTip(lastBlockId, lastCarToolTips[i]);
        }    
    }            
}

/**
 * 顯示ICON及ToolTip
 */
function handleIconAndToolTip(objId, className, toolTipStr, dnStatus){
        //若狀態為'處理中'，則加入閃爍效果'
        if (dnStatus==1){
            className = className+'a';
        }    
    
        jQuery(objId+" a").addClass(className);
        jQuery(objId).show();
        
        jQuery(objId+" a").attr('title', toolTipStr); //設定toolTip          
}


//SN: 散泥1-2期:            
function updateSN(jsonStringId){
    var carSpanIdHeader = 'SN';
    var result = jQuery("#"+jsonStringId).val();
    //Queue總筆數
    var queueSize = 10;
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
        if (i<4){//Queue1 - Queue4最大值為6筆        
            snQueueMaxCar[i]=6;
        }else{//Queue5 - Queue10最大值為5筆
            snQueueMaxCar[i]=5;
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
//        alert('objId='+objId);
        //SN1-4用橫的圖，SN5-10用縱的圖
        var className = '';
        if (queueNo<5){
            className = "carType1";
        }else{
            className = "carType1_1"
        }                    

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
    var SN2CarTotal = calculateCarNum(snQueue, 5, 10);
        jQuery('#SN2CarTotal').text(SN2CarTotal+'車');
    
   //init toolTip
    initToolTip('SN1Block', SN1CarTotal);
    initToolTip('SN2Block', SN2CarTotal);     
}


//DN: 包裝1-3期:
function updateDN(jsonStringId){
    var carSpanIdHeader = 'DN';
    var result = jQuery("#"+jsonStringId).val();
    //DN相關queue總數
    var queueSize = 12;
    //記錄DN相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄DN每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);    
    
    //Reset
     jQuery('#DN1CarTotal').text('0車');
     jQuery('#DN2CarTotal').text('0車');    
     jQuery('#DN3CarTotal').text('0車');    
    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
         //Queue1 - Queue12最大值為5筆
         snQueueMaxCar[i]=5;
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
        //DN1-4用橫的圖，SN5-10用縱的圖
        var className = '';
        if (queueNo<5){
            className = "carType3";
        }else{
            className = "carType3_1"
        }           

        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	     
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //包裝1期總車數(queue1 - queue4)
    var DN1CarTotal = calculateCarNum(snQueue, 1, 4);
    jQuery('#DN1CarTotal').text(DN1CarTotal+'車');    
    
    //包裝2期總車數(queue5 - queu8)
    var DN2CarTotal =  calculateCarNum(snQueue, 5, 8);
    jQuery('#DN2CarTotal').text(DN2CarTotal+'車');        
    
    //包裝3期總車數(queue9 - queue12)
    var DN3CarTotal = calculateCarNum(snQueue, 9, 12);
    jQuery('#DN3CarTotal').text(DN3CarTotal+'車');      
    
   //init toolTip
    initToolTip('DN1Block', DN1CarTotal);
    initToolTip('DN2Block', DN2CarTotal);
    initToolTip('DN3Block', DN3CarTotal);         
}            
            
//檢斤庫#1 - #4            
function updateSLK(jsonStringId){
    var carSpanIdHeader = 'SLK';
    var result = jQuery("#"+jsonStringId).val();
    //SLK相關queue總數
    var queueSize = 4;    
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
        //Queue1 - Queue4最大值為5筆		
         snQueueMaxCar[i]=5;
    }
    
    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);    

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
        //SLK1-4用縱的圖
        var className = 'carType2_1';                        
   
        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
        
    });	                    
    
    //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //設定各區塊車次總數
    //檢斤庫#1(queue1)
    jQuery('#weigh1Div_Total').text(snQueue[0]+'車');        
    //檢斤庫#2(queue2)
    jQuery('#weigh2Div_Total').text(snQueue[1]+'車');        
    //檢斤庫#3(queue3)
    jQuery('#weigh3Div_Total').text(snQueue[2]+'車');        
    //檢斤庫#4(queue4)
    jQuery('#weigh4Div_Total').text(snQueue[3]+'車');        
    
    
   //init toolTip
    initToolTip('SLK1Block', snQueue[0]);
    initToolTip('SLK2Block', snQueue[1]);
    initToolTip('SLK3Block', snQueue[2]);
    initToolTip('SLK4Block', snQueue[3]);
    
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

//裝船區
function updateYDMT(jsonStringId){
    var carSpanIdHeader = 'YDMT';
    var result = jQuery("#"+jsonStringId).val();
    //YDMT相關queue總數
    var queueSize = 6;
    //記錄YDMT相關Queue目前筆數
    var snQueue = new Array(queueSize);
    //記錄YDMT每個Queue的最大筆數
    var snQueueMaxCar = new Array(queueSize);    
    //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
    var lastCarToolTips =  new Array(queueSize);        
    
    //Reset
     jQuery('#YDMTCarTotal').text('0船');
                    
    //init
    for (i=0; i<queueSize; i++){
        snQueue[i] = 0;
        lastCarToolTips[i] = "<br/><br/>";
        //Queue1 - Queue4最大值為3筆		
         snQueueMaxCar[i]=3;
    }                    
    
    //Hide All Element
    hideMultiQueueAllElement(queueSize, snQueueMaxCar, carSpanIdHeader);    

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
 
        //YDMT用橫的圖
        var className = 'carType2';                        
        
        //設定圖示及toolTip    
        handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);
    });	                    

//當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
    handleMultiQueueLastRedToolTip(carSpanIdHeader, queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
    //裝船區(queue1 - queue6)
    var YDMTCarTotal = calculateCarNum(snQueue, 1, 6);
    
    jQuery('#YDMTCarTotal').text(YDMTCarTotal+'船');

   //init toolTip
    initToolTip('YDMTBlock', YDMTCarTotal);
}    


    //"待出廠"區塊
    function updateOutputingArea(){

        //      alert( jQuery("#jsonOutputInfos").val());
        var result = jQuery("#jsonOutputInfos").val();        

        for (var i=0; i<12; i++){
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

        //設定圖示
        jQuery(objId+" a").addClass(className);            
            
            jQuery(objId).show();
            jQuery(objId+" a").attr('title', val.toolTip); //設定toolTip
            total++;
        });	     
        
        if (total==0){
            return;
        }    
        
        jQuery('#toLeaveCarTotal').text(total+'車');        

       //init toolTip
        initToolTip('toLeaveCarsBlock', total);

    }           