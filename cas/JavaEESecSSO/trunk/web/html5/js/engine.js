/* global Notification, EventSource */

"use strict";

var urlRoot = "/JavaEESecSSO/resources/sse/";
// Add Item By Ajax POST
function addItem() {
    var itemInput = document.getElementById("name");

    var req = new XMLHttpRequest();
    req.open("POST", urlRoot+"items", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = function () {
        if (req.readyState === 4 && req.status === 204) {
            //Call a function when the state changes.
            itemInput.value = "";
            getItems();
        }
    };
    req.send("name=" + itemInput.value);
}

// Get Items By Ajax GET
function getItems() {
    var req = new XMLHttpRequest();
    req.open("GET", urlRoot+"items", true);
    req.setRequestHeader("Accept", "text/plain");
    req.onreadystatechange = function () {
        //Call a function when the state changes.
        if (req.readyState === 4 && req.status === 200) {
            document.getElementById("items").innerHTML = req.responseText;
        }
    };
    req.send();
}

// display <div class="message" ><span style="color:rgb" >{data}</span></div>
function display(data, rgb) {
    var msgSpan = document.createElement("span");
    msgSpan.style.color = rgb;
    msgSpan.innerHTML = data;
    var msgDiv = document.createElement("div");
    msgDiv.className = "message";
    msgDiv.appendChild(msgSpan);

    var messages = document.getElementById("messages");
    messages.insertBefore(msgDiv, messages.firstChild);
    
    // Notification API
    //messageNotification(data);
}

// Receive message By SSE
function receiveMessages() {
    if (typeof(EventSource) !== "undefined") {
        // Yes! Server-sent events support!
        var source = new EventSource(urlRoot+"items/events");
        // Received unnamed event
        source.onmessage = function (event) {
            console.log(event.origin);// http://localhost:8080
        
            console.log('Received unnamed event: ' + event.data);
            display("Added new item: " + event.data, "#444444");
        };
        // Received named event
        source.addEventListener("size", function(event) {
            console.log('Received event ' + event.name + ': ' + event.data);
            display("New items size: " + event.data, "#0000FF");
        }, false);

        source.onopen = function (event) {
            console.log("event source opened");
        };

        source.onerror = function (event) {
            console.log('Received error event: ' + event.data);
            display(event.data, "#FF0000");
        };
    } else {
        // Sorry! No server-sent events support..
        display('SSE not supported by browser.', "#FF0000");
    }
}

window.onload = receiveMessages;

///////////////////////////////////////////
Notification.requestPermission().then(function(permission) {
    if(permission === 'granted'){
        console.log('granted notification ...');
    }else if(permission === 'denied'){
        console.log('denied notification ...');
    }
});

var n = null;
function messageNotification(msg){
    console.log("messageNotification ...");
    //var notification = new Notification(title, options);
    n = new Notification('Message',{
        body: msg,
        tag: 'win001',
        icon: 'https://www.taiwancement.com/tw/img/logo1.png',
        requireInteraction: true,
        data: {
            url: 'https://www.taiwancement.com'
        }
    });

    n.onclick = function(e){
        e.preventDefault(); // prevent the browser from focusing the Notification's tab
        window.open(n.data.url, '_blank');
        n.close();
    };
    n.onerror = function(e) {
        // error handling
        console.log("onerror e = \n", e);
    };

    setTimeout(function() {
        n.close();
    }, 10000);// 自動關閉
}

function addOnBeforeUnload(e) {
    console.log("addOnBeforeUnload n = \n", n);
    if( n ){
        n.close();
    }
}

if(window.attachEvent){
    window.attachEvent('onbeforeunload', addOnBeforeUnload);
} else {
    window.addEventListener('beforeunload', addOnBeforeUnload, false);
}

