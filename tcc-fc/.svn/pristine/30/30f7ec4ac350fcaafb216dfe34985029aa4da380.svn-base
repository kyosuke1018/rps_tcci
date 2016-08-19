/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var IE = document.all ? true : false

// If NS -- that is, !IE -- then set up for mouse capture
if (!IE) {document.captureEvents(Event.MOUSEMOVE)}

// Temporary variables to hold mouse x-y pos.s
var tempX = 0
var tempY = 0

var xCorrect = -9;
var yCorrect = -16;

// Main function to retrieve mouse x-y pos.s

function getMouseXY(e) {
    if (IE) { // grab the x-y pos.s if browser is IE
        tempX = event.clientX + document.body.scrollLeft;
        tempY = event.clientY + document.body.scrollTop;
    } else {  // grab the x-y pos.s if browser is NS
        tempX = e.pageX;
        tempY = e.pageY;
    }
  // show the position values in the form named Show
  // in the text fields named MouseX and MouseY
    document.Show.Left.value = tempX;
    document.Show.Top.value = tempY;

    return true;
}

function showStyleHTML(e) {
    if (event.keyCode != 65) return;
    if (IE) { // grab the x-y pos.s if browser is IE
        tempX = event.clientX + document.body.scrollLeft;
        tempY = event.clientY + document.body.scrollTop;
    } else {  // grab the x-y pos.s if browser is NS
        tempX = e.pageX;
        tempY = e.pageY;
    }
  // show the position values in the form named Show
  // in the text fields named MouseX and MouseY
    document.Show.styleHTML.value = 'style="top:' + (tempY) + 'px; left:' + tempX + 'px;"';
}

//function copyToClipboard() {
//    if (IE) { // grab the x-y pos.s if browser is IE
//      tempX = event.clientX + document.body.scrollLeft
//      tempY = event.clientY + document.body.scrollTop
//    } else {  // grab the x-y pos.s if browser is NS
//      tempX = e.pageX
//      tempY = e.pageY
//    }  
//}   

// Set-up to use getMouseXY function onMouseMove
document.onmousemove = getMouseXY;
document.onkeydown = showStyleHTML;
//document.onmousedown = copyToClipboard;