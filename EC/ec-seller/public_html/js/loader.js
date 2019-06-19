/* global initLayout, xtag */
var loadCompleted = false;
var loadTimer = 0;
var filesadded=""; //list of files already added

function loadjscssfile(filename, filetype){
    if (filetype==="js"){ //if filename is a external JavaScript file
        var fileref=document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src", filename);
        fileref.onload = function(){ filesadded+="["+filename+"]";  };
    }
    else if (filetype==="css"){ //if filename is an external CSS file
        var fileref=document.createElement("link");
        fileref.setAttribute("rel", "stylesheet");
        fileref.setAttribute("type", "text/css");
        fileref.setAttribute("href", filename);
        fileref.onload = function(){ filesadded+="["+filename+"]";  };
    }
    if (typeof fileref!=="undefined")
        document.getElementsByTagName("head")[0].appendChild(fileref);
}
 
function checkloadjscssfile(filename, filetype, dependencyCompleted){
    if( !dependencyCompleted ){
        return false;
    }
    if( filesadded.indexOf("["+filename+"]")===-1 ){
        loadjscssfile(filename, filetype);
        return false;
    }
    console.log("checkloadjscssfile false filename =", filename);
    return true;
} 

var loadResourcesFunc = loadResources;

function loadResources(){
    console.log("loadResources ...");
    clearTimeout(loadTimer);
    
    if( !loadCompleted ){
        var flag = true;
        flag = flag && checkloadjscssfile("lib/icons/css/font-awesome.min.css", "css", true);
        flag = flag && checkloadjscssfile("lib/jquery/jquery-ui.css", "css", true);
        flag = flag && checkloadjscssfile("lib/primeui/primeui-all.min.css", "css", true);
        flag = flag && checkloadjscssfile("lib/primeui/themes/hot-sneaks/theme.css", "css", true);
        flag = flag && checkloadjscssfile("css/w3.css", "css", true);
        flag = flag && checkloadjscssfile("css/style.css", "css", true);
        
        flag = flag && checkloadjscssfile("lib/primeui/x-tag-core.min.js", "js", true);
        flag = flag && checkloadjscssfile("lib/primeui/mustache.min.js", "js", true);
        flag = flag && checkloadjscssfile("lib/primeui/plugins-all.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("lib/primeui/primeui-all.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("lib/primeui/primeelements.min.js", "js", (typeof xtag!=="undefined" && typeof PUI!=="undefined"));

        flag = flag && checkloadjscssfile("js/consts.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/variables.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/tcc-utils.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/com/button.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/com/dropdown.js", "js",(typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/com/menu.js", "js", (typeof jQuery!=="undefined"));
        
        flag = flag && checkloadjscssfile("js/layout.js", "js", (typeof jQuery!=="undefined"));
        flag = flag && checkloadjscssfile("js/order-common.js", "js", (typeof jQuery!=="undefined"));

        loadCompleted = flag;
    }
    
    if( !loadCompleted ){
        loadTimer = setTimeout(loadResourcesFunc, 100);
    }else{
        start();
    }
    
    console.log("loadCompleted =", loadCompleted);
}
