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
var areaDN = new LoadingArea();
var waitingArea = new WaitingArea();
function init() {
    waitingArea.top = 470;
    waitingArea.left = 667;
    waitingArea.areaSL = 0;
    waitingArea.cols = 7;
    waitingArea.rows = 3;
    waitingArea.vertical = 1;
    waitingArea.dataSrcSN = 'jsonCar2Infos';
    waitingArea.dataSrcDN = 'jsonCar3Infos';   
    waitingArea.drawLayout();    
    waitingArea.printLayout();
    
    areaSN1.title = '散泥';
    areaSN1.id = 'SN1';
    areaSN1.portPrefix = '';
    areaSN1.portIndexDigit = 2;
    areaSN1.top = 385;
    areaSN1.left = 58;
    areaSN1.slot_title_position = 'button';
    areaSN1.cars_in_slot = 6;
    areaSN1.slot = 8;
    areaSN1.displayClassName = 'carType1';
    areaSN1.drawLayout();

    areaSN2.title = '待建散泥';
    areaSN2.id = 'SN2';
    areaSN2.portPrefix = '';
    areaSN2.portIndexDigit = 2;
    areaSN2.top = 303;
    areaSN2.left = 450;
    areaSN2.slot_title_position = 'left';
    areaSN2.cars_in_slot = 6;
    areaSN2.slot = 4;
    areaSN2.displayClassName = 'carType1';
    areaSN2.start_slot = 9;
    areaSN2.slot_space = 24;
    areaSN2.drawLayout();

    areaDN.title = '包裝';
    areaDN.id = 'DN';
    areaDN.portPrefix = 'P';
    areaDN.portIndexDigit = 2;
    areaDN.top = 455;
    areaDN.left = 504;
    areaDN.slot_title_position = 'left';
    areaDN.cars_in_slot = 5;
    areaDN.slot = 6;
    areaDN.slot_space = 15;
    areaDN.displayClassName = 'carType3';
    areaDN.drawLayout();
}

function updateAllData() {
    //等待區區塊 
    waitingArea.drawCar();
    
    //待出廠區塊
    updateOutputingArea();    
    
    areaSN1.drawCar('jsonSNLoadingInfos');
    areaSN2.drawCar('jsonSNLoadingInfos');
    areaDN.drawCar('jsonDNLoadingInfos');    
}

