/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(function() {
    init();
    
    updateAllData();
});

var areaSN1 = new LoadingArea();
var areaSN2 = new LoadingArea();
var areaSN3 = new LoadingArea();
var areaOther = new LoadingArea();
var areaSL1 = new LoadingArea();
var areaSL2 = new LoadingArea();
var areaSL3 = new LoadingArea();
var areaDN = new LoadingArea();
var waitingArea = new WaitingArea();
function init() {
    waitingArea.top = 73;
    waitingArea.left = 170;
    waitingArea.areaSL = 1;
    waitingArea.areaDN = 0;
    waitingArea.areaSN = 1;
    waitingArea.dataSrcSL = 'jsonCar1Infos';
    waitingArea.dataSrcSN = 'jsonCar2Infos';
    waitingArea.dataSrcDN = 'jsonCar3Infos'; 
    waitingArea.titleSL = '熟料區';
    waitingArea.titleSN = '散泥區'
    waitingArea.rows = 6;
    waitingArea.cols = 4;
    waitingArea.vertical = 0;
    waitingArea.drawLayout();
    //waitingArea.printLayout();
    
    areaSN1.title = '散泥';
    areaSN1.shipType = 'boat';
    areaSN1.id = 'SN1';
    areaSN1.portPrefix = 'BSSN';
    areaSN1.portIndexDigit = 1;
    areaSN1.top = 614;
    areaSN1.left = 74;
    areaSN1.slot_title_position = 'top';
    areaSN1.cars_in_slot = 5;
    areaSN1.slot = 1;
    areaSN1.displayClassName = 'carType1';
    areaSN1.drawLayout();
    areaSN1.printLayout();

    areaSN2.title = '散泥';
    areaSN2.id = 'SN2';
    areaSN2.shipType = 'boat';
    areaSN2.portPrefix = 'BSSN';
    areaSN2.portIndexDigit = 1;
    areaSN2.top = 614;
    areaSN2.left = 180;
    areaSN2.slot_title_position = 'top';
    areaSN2.cars_in_slot = 5;
    areaSN2.start_slot = 2;
    areaSN2.slot = 1;
    areaSN2.displayClassName = 'carType1';
    areaSN2.drawLayout();
    
    areaSN3.title = '散泥';
    areaSN3.id = 'SN3';
    areaSN3.shipType = 'boat';
    areaSN3.portPrefix = 'BSSN';
    areaSN3.portIndexDigit = 1;
    areaSN3.top = 614;
    areaSN3.left = 300;
    areaSN3.slot_title_position = 'top';
    areaSN3.cars_in_slot = 5;
    areaSN3.start_slot = 3;    
    areaSN3.slot = 1;
    areaSN3.displayClassName = 'carType1';
    areaSN3.drawLayout();  

    areaOther.title = '其它通知單';
    areaOther.id = 'SN8';
    areaOther.shipType = 'boat';
    areaOther.portPrefix = 'BSSN';
    areaOther.portIndexDigit = 2;
    areaOther.top = 323;
    areaOther.left = 90;
    areaOther.slot_title_position = 'left';
    areaOther.cars_in_slot = 5;
    areaOther.start_slot = 8;    
    areaOther.slot = 3;
    areaOther.displayClassName = 'carType1';
    areaOther.showSlot = 0;
    areaOther.drawLayout(); 
    
    areaSL1.title = '熟料';
    areaSL1.id = 'SL1';
    areaSL1.shipType = 'boat';
    areaSL1.portPrefix = 'BSSL';
    areaSL1.portIndexDigit = 1;
    areaSL1.top = 614;
    areaSL1.left = 420;
    areaSL1.slot_title_position = 'top';
    areaSL1.cars_in_slot = 5;
    areaSL1.slot = 1;
    areaSL1.displayClassName = 'carType2';
    areaSL1.drawLayout(); 
    
    areaSL2.title = '熟料';
    areaSL2.id = 'SL2';
    areaSL3.shipType = 'boat';
    areaSL2.portPrefix = 'BSSL';
    areaSL2.portIndexDigit = 1;
    areaSL2.top = 614;
    areaSL2.left = 540;
    areaSL2.slot_title_position = 'top';
    areaSL2.cars_in_slot = 5;
    areaSL2.start_slot = 2;
    areaSL2.slot = 1;
    areaSL2.displayClassName = 'carType2';
    areaSL2.drawLayout();    

    areaSL3.title = '熟料';
    areaSL3.id = 'SL3';
    areaSL3.shipType = 'boat';
    areaSL3.portPrefix = 'BSSL';
    areaSL3.portIndexDigit = 1;
    areaSL3.top = 614;
    areaSL3.left = 656;
    areaSL3.slot_title_position = 'top';
    areaSL3.cars_in_slot = 5;
    areaSL3.start_slot = 3;    
    areaSL3.slot = 1;
    areaSL3.displayClassName = 'carType2';
    areaSL3.drawLayout();    
}

function updateAllData() {
    //等待區區塊 
    waitingArea.drawCar();
    
    //待出廠區塊
    updateOutputingArea();    
    
    areaSN1.drawCar('jsonSNLoadingInfos');
    areaSN2.drawCar('jsonSNLoadingInfos');
    areaSN3.drawCar('jsonSNLoadingInfos');
    areaOther.drawCar('jsonSNLoadingInfos');
    areaSL1.drawCar('jsonSLLoadingInfos');
    areaSL2.drawCar('jsonSLLoadingInfos');
    areaSL3.drawCar('jsonSLLoadingInfos');
}