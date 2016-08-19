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

var waitingArea = new WaitingArea();
function init() {
    waitingArea.top = 46;
    waitingArea.left = 919;
    waitingArea.areaSL = 0;
    waitingArea.areaDN = 0;
    waitingArea.areaSN = 1;
    waitingArea.dataSrcSL = 'jsonCar3Infos';
    waitingArea.dataSrcSN = 'jsonCar3Infos';
    waitingArea.dataSrcDN = 'jsonCar3Infos'; 
    waitingArea.titleSL = '熟料區';
    waitingArea.titleSN = '散泥區'
    waitingArea.rows = 6;
    waitingArea.cols = 4;
    waitingArea.vertical = 0;
    waitingArea.drawLayout();
    waitingArea.printLayout();
    
    areaSN1.title = '散泥';
    areaSN1.shipType = 'boat';
    areaSN1.id = 'SN1';
    areaSN1.portPrefix = 'XYTSN';
    areaSN1.portIndexDigit = 1;
    areaSN1.top = 618;
    areaSN1.left = 837;
    areaSN1.slot_title_position = 'top';
    areaSN1.cars_in_slot = 5;
    areaSN1.slot = 1;
    areaSN1.displayClassName = 'carType1';
    areaSN1.drawLayout();

    areaSN2.title = '散泥';
    areaSN2.id = 'SN2';
    areaSN2.shipType = 'boat';
    areaSN2.portPrefix = 'XYTSN';
    areaSN2.portIndexDigit = 1;
    areaSN2.top = 618;
    areaSN2.left = 941;
    areaSN2.slot_title_position = 'top';
    areaSN2.cars_in_slot = 5;
    areaSN2.start_slot = 2;
    areaSN2.slot = 1;
    areaSN2.displayClassName = 'carType1';
    areaSN2.drawLayout();
}

function updateAllData() {
    //等待區區塊 
    waitingArea.drawCar();
    
    //待出廠區塊
    updateOutputingArea();    
    
    areaSN1.drawCar('jsonSNLoadingInfos');
    areaSN2.drawCar('jsonSNLoadingInfos');
}