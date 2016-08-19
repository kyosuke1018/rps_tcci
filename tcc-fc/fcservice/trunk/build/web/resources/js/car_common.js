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
