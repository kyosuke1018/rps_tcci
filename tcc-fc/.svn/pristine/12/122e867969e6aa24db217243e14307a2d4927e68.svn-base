/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function LoadingArea() {
    this.id = '';
    this.title = '';
    this.top = 0;
    this.left = 0;
    this.slot = 8;
    this.cars_in_slot = 6;
    this.start_slot = 1;
    this.portPrefix = '';
    this.portIndexDigit = 1;
    this.displayClassName = '';
    this.slot_title_position = 'button';
    this.shipType = 'car';
    this.showSlot = 1;
    //this.slot_order = 0; //0: ascending(left-right / top-down); 1: descending

    this.slot_space = 9;
    this.car_space = 1;
    this.car_length = 20;
    this.car_width = 9;
    this.margin = 10;
    this.title_space = 8;

    this.getHeight = function () {
        if (this.slot_title_position == 'top' || this.slot_title_position == 'button') {
            return this.margin * 2 + (this.car_length + this.car_space) * this.cars_in_slot;
        } else {
            return this.margin * 2 + (this.slot_space) * (this.slot-1);
        }
    };

    this.getWidth = function () {
        if (this.slot_title_position == 'top' || this.slot_title_position == 'button') {
            return this.margin * 2 + (this.car_width + this.slot_space) * this.slot;
        } else {
            return this.margin * 2 + (this.car_length + this.car_space) * this.cars_in_slot;
        }
    };
    
    this.getSlotTitleTop = function () {
        var tempTop;
        
        switch (this.slot_title_position) {
            case 'top': 
            case 'left':
            case 'right':
                tempTop = this.top + this.margin + 5;                
                break;
            case 'button':
                tempTop = this.top + this.getHeight() - this.margin;
                break;                
        }
        
        return tempTop;
    }
    
    this.getSlotTitleLeft = function () {
        var tempLeft;
        
        switch (this.slot_title_position) {
            case 'top': 
            case 'button':
                tempLeft = this.left + this.margin + 5;
                break;
            case 'left':
                tempLeft = this.left + this.margin;                
                break;            
            case 'right':
                tempLeft = this.left + this.getWidth() - this.margin;
                break;                
        }

        return tempLeft;
    }
    
    this.getShipType = function() {
        return this.shipType == 'car' ? '車' : '船';
    }
    
    this.createLayoutHTML = function () {
        var areaHTML = '<div id="' + this.id + '" class="Area" style="position: absolute; top:' + this.top + 'px; left:' + this.left + 'px; height:' + this.getHeight() + 'px; width: ' + this.getWidth() + 'px">'
                     + '\n  <span class="AreaTitle">' + this.title + '</span>'
                     + '\n  <span id="' + this.id + 'CarTotal" class="AreaStats" style="left:'+(this.getWidth()-15)+'px;">90 '+this.getShipType()+'</span>'
                     + '\n  <div class="AreaCars"></div>'
                     + '\n</div>';
             
        var carsHTML;
        
        if (this.slot_title_position == 'top' || this.slot_title_position == 'button') {
            carsHTML = '\n<div id="'+this.id+'Block" class="AreaCarsSN_V">';   
        }else {
            carsHTML = '\n<div id="'+this.id+'Block" class="AreaCarsSN_H">';   
        }
        

        var slotTitleTop = this.getSlotTitleTop();
        var slotTitleLeft = this.getSlotTitleLeft();
        
        for (var i = 0; i < this.slot; i++) {
            var slotNumber = this.start_slot + i;
            
            carsHTML += '\n  <span style="top:' + slotTitleTop + 'px; left:' + slotTitleLeft + 'px; font-size: 10px">' + (this.showSlot==0?'':slotNumber) + '</span> ';
            
            var carTop = 0;
            var carLeft = 0;            

            if (this.slot_title_position == 'top') {
                carTop = slotTitleTop + this.title_space;
                carLeft = slotTitleLeft - 8;
            } else if (this.slot_title_position == 'button') {
                carTop = slotTitleTop - (this.car_length);
                carLeft = slotTitleLeft - 8;
            } else if (this.slot_title_position == 'left') {
                carTop = slotTitleTop - 5;
                carLeft = slotTitleLeft + 5;
            } else if (this.slot_title_position == 'right') {
                carTop = slotTitleTop - 3;
                carLeft = slotTitleLeft - (5 + this.car_length);
            }

            for (var k = 1; k <= this.cars_in_slot; k++) {
                var portIndex = padLeft(slotNumber.toString(), this.portIndexDigit)
                
                carsHTML += '\n  <span id="' + this.portPrefix + portIndex + '_' + k + '" style="top:' + carTop + 'px;left:' + carLeft + 'px"><a href="#">&nbsp;</a></span>'; 
               
                if (this.slot_title_position == 'top') {
                    carTop += this.car_length + this.car_space;
                } else if (this.slot_title_position == 'button') {
                    carTop -= (this.car_length + this.car_space);
                } else if (this.slot_title_position == 'left') {
                    carLeft += this.car_length + this.car_space;
                } else if (this.slot_title_position == 'right') {
                    carLeft -= (this.car_length + this.car_space);
                }
            }
            
            if (this.slot_title_position == 'button' || this.slot_title_position == 'top') {
                slotTitleLeft += this.car_width + this.slot_space;
            } else if (this.slot_title_position == 'left' || this.slot_title_position == 'right') {
                slotTitleTop += this.slot_space;
            }            
        }
        
        carsHTML += '\n</div>';
        
        
        return areaHTML + carsHTML;
    };
    
    this.drawLayout = function() {
        jQuery("#dataMap").append(this.createLayoutHTML());
    }
    
    this.printLayout = function() {
        printSourceCode(this.createLayoutHTML());
    }
    
    this.drawCar = function (jsonStringId) {
        var result = jQuery("#"+jsonStringId).val();
        //Queue總筆數
        var queueSize = this.slot;
        //記錄SN相關Queue目前筆數
        var snQueue = new Array(queueSize);
        //記錄DN每個Queue的最大筆數
        var snQueueMaxCar = new Array(queueSize);      
        //記錄每個Queue最後一台車的ToolTip(超出最大車次時要累積車號)    
        var lastCarToolTips =  new Array(queueSize);    

        //Reset
         jQuery('#'+this.id+'CarTotal').text('0'+this.getShipType());

        //init
        for (i=0; i<queueSize; i++){
            snQueue[i] = 0;
            lastCarToolTips[i] = "<br/><br/>";
            snQueueMaxCar[i]=this.cars_in_slot;
        }

        //Hide All Element
        this.hideMultiQueueAllElement(queueSize, snQueueMaxCar);

        //處理每一個item
        var carSpanIdHeader = this.portPrefix;
        var startSlot = this.start_slot;
        var endSlot = this.start_slot + this.slot - 1;
        var vertical;
        var displayClassName = this.displayClassName;
        var portIndexDigit = this.portIndexDigit;
            
        if (this.slot_title_position == 'top' || this.slot_title_position == 'button') {
            vertical = 1;
        }else {
            vertical = 0;
        }
        
        jQuery.each(eval(result), function (key, val) {
            //取得在哪一個Queue
            var queueNo = getQueueNo(val.portId, carSpanIdHeader);
            
            if (queueNo < startSlot || queueNo > endSlot) return;
            
            //將該Queue總數+1
            var queueIndex = queueNo-startSlot;
            var total = snQueue[queueIndex]+1;
            //記錄該Queu筆數(+1)
            snQueue[queueIndex] =total;        
            //處理最後一筆資料的toolTip
            if (total>snQueueMaxCar[queueIndex]){
                lastCarToolTips[queueIndex]  = lastCarToolTips[queueIndex]+'&nbsp;'+val.truckno;
    //            alert('queueNo='+queueNo+';total='+total+';lastCarToolTips='+lastCarToolTips[queueNo-1]);
                return true;//continue
            }               

            //取得item id
            var portIndex = padLeft(queueNo.toString(), portIndexDigit);
            var objId = "#"+carSpanIdHeader+portIndex+"_"+total.toString();    
    //        alert('objId='+objId);
            //SN1-4用橫的圖，SN5-10用縱的圖
           
            var className = displayClassName;
            
            if (vertical == 1){
                className += "_1";
            }          
            //設定圖示及toolTip    
            handleIconAndToolTip(objId, className, val.toolTip, val.dnStatus);          
        });	
        
        //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
        this.handleMultiQueueLastRedToolTip(queueSize, snQueue, snQueueMaxCar, lastCarToolTips);
    
        //設定各區塊車次總數
        var SN1CarTotal = calculateCarNum(snQueue, 1, this.slot);
        jQuery('#'+this.id+'CarTotal').text(SN1CarTotal+this.getShipType());
    
       //init toolTip
        initToolTip(this.id+'Block', SN1CarTotal);
    }   
    
    this.hideMultiQueueAllElement = function (queueSize, queueMaxCars){
        //Hide All Element
        for (var i=0; i<queueSize; i++){
            var index = i+this.start_slot;
            for (var j=0; j<queueMaxCars[i]; j++){
                var index2 = j+1;
                var objId = "#"+this.portPrefix+padLeft(index.toString(), this.portIndexDigit)+"_"+index2.toString();
                var obj =jQuery(objId);

                if (obj.length>0){
                    obj.hide();
                }
            }                 
        }        
    }    
    
    this.handleMultiQueueLastRedToolTip = function (queueSize, snQueue, snQueueMaxCar, lastCarToolTips){
        //當車數超出屏幕最大顯示時，處理最後一筆的icon及ToolTip
        for (i=0; i<queueSize; i++){
            var qNum = snQueue[i];

            if (qNum>snQueueMaxCar[i]){
    //            alert('i='+i+';qNum='+qNum);
                var idx = i+1;
                var lastIdx = snQueueMaxCar[i];
                var lastBlockId = this.portPrefix+padLeft(idx.toString(),this.portIndexDigit)+'_'+lastIdx.toString();            
    //            alert('lastBlockId='+lastBlockId+';'+'lastCarToolTips['+i+']='+lastCarToolTips[i]);
                handleLastRedToolTip(lastBlockId, lastCarToolTips[i]);
            }    
        }            
    }    
}

function WaitingArea() {
    this.top = 0;
    this.left = 0;
    this.areaSL = 1;    
    this.areaSN = 1;
    this.areaDN = 1;
    this.rows = 3;
    this.cols = 3;
    this.vertical = 0;
    this.carBlockPrefix = 'car';
    this.carBlockPostfix = 'CarsBlock';
    this.dataSrcSL = '';
    this.dataSrcSN = '';
    this.dataSrcDN = '';
    this.titleSN = '散泥車';
    this.titleDN = '袋泥車';
    this.titleSL = '熟料車';
    
    this.getWidth = function() {
        return this.cols * 25;
    };
    
    this.getHeight = function() {
        return this.rows * 15 + 10;
    };    
    
    this.getNumOfCars = function () {
        return this.cols * this.rows;
    }
    
    this.createLayoutHTML = function() {
        var top = 0;
        var left = 0;
        var areaHeight = this.getHeight();
        var areaWidth = this.getWidth();
        
        var areaHTML = '<div class="waitingArea" style="position:absolute; top:'+this.top+'px; left:'+this.left+'px;">'
                     + '\n  <div class="carDiv" style="width:'+this.getWidth()+'px;">';
        if (this.areaSL == 1) {
            areaHTML += '\n    <span class="divTitle" style="top:'+top+'px;left:'+left+'px;width:50px;">'+this.titleSL+'</span>'
                     +  '\n    <span class="carDiv_box" style="height:'+this.getHeight()+'px;width:'+this.getWidth()+'px;"><span id="car1Total" style="top:12px;left: 5px;font-size: 14px;text-align: right;width: 20px;color: blue">0</span></span>';
            
            areaHTML += this.createCarHTML('1', top, left);
            
            if (this.vertical == 1) {
                top += areaHeight;
            }else {
                left += areaWidth;
            }
        }
        if (this.areaSN == 1) {
            areaHTML += '\n    <span class="divTitle" style="top:'+top+'px;left:'+left+'px;width:50px;">'+this.titleSN+'</span>'
                     +  '\n    <span class="carDiv_box" style="height:'+this.getHeight()+'px;width:'+this.getWidth()+'px;left:'+left+'px;top:'+top+'px;"><span id="car2Total" style="top:12px;left: 5px;font-size: 14px;text-align: right;width: 20px;color: blue">0</span></span>';
                 
            areaHTML += this.createCarHTML('2', top, left);
            
            
            if (this.vertical == 1) {
                top += areaHeight;
            }else {
                left += areaWidth;
            }
        }
        if (this.areaDN == 1) {
            areaHTML += '\n    <span class="divTitle" style="top:'+top+'px;left:'+left+'px;width:50px;">'+this.titleDN+'</span>'
                     +  '\n    <span class="carDiv_box" style="height:'+this.getHeight()+'px;width:'+this.getWidth()+'px;left:'+left+'px;top:'+top+'px;"><span id="car3Total" style="top:12px;left: 5px;font-size: 14px;text-align: right;width: 20px;color: blue">0</span></span>'            
                 
            areaHTML += this.createCarHTML('3', top, left);
        }

        areaHTML += '\n  </div>\n</div>';
                 
        return areaHTML;
    }
    
    this.drawLayout = function () {
        jQuery("#dataMap").append(this.createLayoutHTML());        
    }
    
    this.printLayout = function() {
        printSourceCode(this.createLayoutHTML());
    }
    
    this.getCarBlock = function (carBlockId) {
        return this.carBlockPrefix+carBlockId+this.carBlockPostfix;
    }
    
    this.createCarHTML = function (carBlockId, carTopbase, carLeftbase) {
        var carTop = 10;
        var carLeft = this.getWidth() - 23;
        var car_topSpace = 13;
        var car_leftSpace = 20;      
        var num_of_cars = this.rows * this.cols;
        var areaHTML = '\n<div id="'+this.getCarBlock(carBlockId)+'">';
        for (var i = 1; i <= num_of_cars; i++) {
            if ((i % this.rows) == 1) {
                carTop = 10;
                if (i > 1) {
                    carLeft -= car_leftSpace;                
                }
            }else {
                if (i > 1) {
                    carTop += car_topSpace;
                }
            }            
            areaHTML += '\n  <span id="car'+carBlockId+'_'+i+'" style="top:'+(carTop+carTopbase)+'px;left:'+(carLeft+carLeftbase)+'px"><a href="#">&nbsp;</a></span>';
        }   
        areaHTML += '\n</div>';
        return areaHTML;
    }
    
    this.drawCar = function () {
        if (this.areaSL) {
            drawCarByArea('car1CarsBlock', this.dataSrcSL, 'car1_', 'car1Total', this.getNumOfCars());
        }
        
        if (this.areaSN) {
            drawCarByArea('car2CarsBlock', this.dataSrcSN, 'car2_', 'car2Total', this.getNumOfCars());
        }
        
        if (this.areaDN) {
            drawCarByArea('car3CarsBlock', this.dataSrcDN, 'car3_', 'car3Total', this.getNumOfCars());
        }
    }
    
    var drawCarByArea = function (carsBlockId, jsonValId, carSpanIdHeader, totalId, blockMaxCar){
        //input: 'car2CarsBlock', 'jsonCar2Infos', 'car2_', 'car2Total'
         var result = jQuery("#"+jsonValId).val();

        //每個區塊最大車次
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
        
        //weird case... 'SN22' belongs to second queue 
        if (parseInt(queueNo) > 20) {
            queueNo = queueNo.substring(0,1);
        }
        
        return parseInt(queueNo);
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

//計算指定Queue中，車(船)總數
function calculateCarNum(queue, startIdx, endIdx){
    var total = 0;
    for (idx=startIdx-1; idx<endIdx; idx++){
        total += queue[idx];
    }
    return total;
}

// 左邊補0
function padLeft(str,lenght){
    if(str.length >= lenght)
        return str;
    else
        return padLeft("0" +str,lenght);
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
    
function printSourceCode (source) {
        var target = jQuery('#sourceCode').val();
        target += source;
        jQuery('#sourceCode').val(target);    
}