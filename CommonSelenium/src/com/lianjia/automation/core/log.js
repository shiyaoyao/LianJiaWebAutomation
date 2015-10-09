function loadResults(){
	var param = "results=";
	var resultsFile = location.href.substring(location.href.indexOf(param)+param.length);
	loadDoc(location.href.indexOf(param)==-1 ? "results.json" : resultsFile);
}

function loadDoc(resultsFile){
	var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
	if (xmlhttp){

		xmlhttp.onreadystatechange=function(){
			if (xmlhttp.readyState==4){
				parseData(eval("("+xmlhttp.responseText+")"));
			}
		}
		xmlhttp.open("GET",resultsFile,true);
		xmlhttp.send(null);
	}
}

var loadDone = false;
var showImage = false;
var testComplete = false;
function parseData(oData){
	
	for(var i in oData){		
		var obj = document.getElementById(i);
		if (obj)
			obj.innerHTML = oData[i];		
	}
	
	//Make pretty icons
	var osIcon = document.getElementById('osIcon');
	
	if (osIcon){
		var os = oData.platform.toLowerCase();
		osIcon.src = os.indexOf("windows")!=-1 || os.indexOf("win")!=-1 ? "icon_win.png" :
					os.indexOf("apple")!=-1 || os.indexOf("mac")!=-1 ?  "icon_apple.png" : "icon_linux.png";		
	}
	
	var browserIcon = document.getElementById('browserIcon');
	if (browserIcon){
		var browser = oData.browser.toLowerCase();
		browserIcon.src = browser.indexOf("firefox")!=-1 ? "icon_ff.png" : 
						  browser.indexOf("chrome")!=-1 ? "icon_chrome.png" : 
						  browser.indexOf("explorer")!=-1 ? "icon_ie.png" : 
					      browser.indexOf("safari")!=-1 ? "icon_safari.png" : "";
	}
	
	
	if (oData["mode"]){
		document.getElementById('modeDisplay').style.display="";
		document.getElementById('mode').className=oData["mode"];
	}
	
	var aTests = oData["tests"];
	var oTests = document.getElementById('testResults');
	if (oTests && aTests){
		for(var i=0;i<aTests.length;i++){
			var oTest = aTests[i];
			var oRow = document.createElement('tr');
			oTests.parentNode.appendChild(oRow);
			var newRow =	"<td class=\""+oTest.status.toLowerCase()+"\">"+oTest.status+"</td>"+
						"<td class=\"testname\"><a href=\"testViewer.html#results="+oTest.title+"\">"+oTest.title+"</a></td>"+
						"<td class=\"testdesc\">"+oTest.desc+"</td>"+
						"<td class=\"testtime\">"+oTest.time+"</td>";
			oRow.innerHTML = newRow;
		}
	}

	//For testViewer.html
	var aMessages = oData.messages;
	var oMessages = document.getElementById('messageResults');
	var sStartsWith;
	if (oMessages && aMessages){
		for(var i=0;i<aMessages.length;i++){
			var oMessage = aMessages[i];
			var oRow = document.createElement('tr');
      		var type = oMessage.type.toUpperCase();
			if(type =="PASSED" || type =="FAILED" || type=="SPR")
				oRow.setAttribute('title',"RESULT");
			else
				oRow.setAttribute('title',type);

			oMessages.parentNode.appendChild(oRow);
			oRow.innerHTML="<td class=\""+oMessage.type.toLowerCase()+"\">"+oMessage.type
			+"</td><td class=\""+oMessage.type.toLowerCase()+"\">"
			+oMessage.message+"</td><td name='CALLSTAMP'>"+oMessage.callstamp
			+"</td><td name='TIMESTAMP'>"+oMessage.timestamp+"</td>";
			
			if(oMessage.type.toLowerCase() == "failed"){
				showImage = true;
			}
			
			sStartsWith = oMessage.message.substring(0,5);
			if(sStartsWith == "TESTP" || sStartsWith == "TESTF" || sStartsWith == "TESTI"){
				loadDone = true;
			}
		}
	}
	
	
	//If there is a queued tests list, display it
	var oQueuedTests = oData.testsToBeRun;
	if (oQueuedTests!=null && oQueuedTests.length>0){
		var oTable = document.getElementById('queuedTestTable');
		oTable.style.display="";
		
		var oQueuedTest = document.getElementById('queuedTests');
		for(var i=0;i<oQueuedTests.length;i++){
			var oRow = document.createElement('tr');
			oQueuedTest.parentNode.appendChild(oRow);
			oRow.innerHTML="<td>"+oQueuedTests[i]+"</td>";
		}
	}
	
	
	//Retest table
	var aTests = oData["retest"];
	var oTests = document.getElementById('retestResults');
	var oTestTable = document.getElementById('retestTable');

	if (oTests && aTests){
		oTestTable.style.display="";
		for(var i=0;i<aTests.length;i++){
			var oTest = aTests[i];
			var oRow = document.createElement('tr');
			oTests.parentNode.appendChild(oRow);
			var newRow =	"<td class=\""+oTest.status.toLowerCase()+"\">"+oTest.status+"</td>"+
							"<td class=\"testname\"><a href=\"testViewer.html#results="+oTest.title+"\">"+oTest.title+"</a></td>"+
							"<td class=\"testdesc\">"+oTest.desc+"</td>"+
							"<td class=\"testtime\">"+oTest.time+"</td>";
			oRow.innerHTML = newRow;
		}
	}
		
	
	if (oData["status"]!="Completed")
		startTimer(new Date(oData["startTime"]));
}

function startTimer(sStartDate){
	var date1 = sStartDate.getTime();
	var date2 = new Date().getTime();
	var time = new Date(date2-date1);

	
	var oElapsed = document.getElementById('elapsedTime');
	if (oElapsed){
		oElapsed.innerHTML = time.getUTCHours()+"h "+time.getUTCMinutes()+"m "+time.getUTCSeconds()+"s";
	}
	
	setTimeout(function(){ startTimer(sStartDate);},1000);
}

var toggle=true;

function changeVis(level) {
    var chk = document.getElementById(level);
    if(chk){
        tbl = document.getElementById("logging");
        var len = tbl.rows.length;
        var vStyle = chk.checked ? "":"none";
        for(i=0 ; i< len; i++){
            if(tbl.rows[i].title == level){
                tbl.rows[i].style.display = vStyle;
            }
            if(level == "CALLSTAMP"){
                tbl.rows[i].cells[2].style.display = vStyle;
            }
            if(level == "TIMESTAMP"){
                tbl.rows[i].cells[3].style.display = vStyle;
            }
        }
    }
}

function load()
{
    //var hiddenlevels = new Array("WARN", "DEBUG", "INFO", "SETTING", "TESTPLAN", "CALLSTAMP", "TIMESTAMP");
    var hiddenlevels = new Array("IMAGE", "DEBUG", "INFO", "SETTING", "CALLSTAMP", "TIMESTAMP");
    for(var i=0; i < hiddenlevels.length; i++){    
        var chk = document.getElementById(hiddenlevels[i]);
        if(!chk.checked){
            chk.click();
        }
        chk.click();
    } 
}

function checkDone() {
	if(loadDone){
		if(showImage){
			var chk = document.getElementById("IMAGE");
			if(!chk.checked){
				chk.click();
			}
		}
		refreshVis();
	}
	return loadDone;
}

function checkTestComplete() {
	var oSuiteStatus = document.getElementById('status');
	if(oSuiteStatus)
		if(oSuiteStatus.textContent == "Completed")
			return true;
	
	window.location.reload();
	return testComplete;
}
function refreshVis() {
	var levels = new Array("RESULT", "ERROR", "WARN", "DEBUG", "INFO", "SETTING", "TESTPLAN", "EXCEPTION", "IMAGE", "CALLSTAMP", "TIMESTAMP");
	for(var i=0; i < levels.length; i++){    
       changeVis(levels[i]);
    }
}

function selectInitial() {
	var hiddenlevels = new Array("IMAGE", "DEBUG", "INFO", "SETTING", "CALLSTAMP", "TIMESTAMP");
    for(var i=0; i < hiddenlevels.length; i++){    
        var chk = document.getElementById(hiddenlevels[i]);
        if(!chk.checked){
            chk.click();
        }
        chk.click();
    } 
}

function selectAll(box){
    var levels = new Array("RESULT", "ERROR", "WARN", "DEBUG", "INFO", "SETTING", "TESTPLAN", "EXCEPTION", "IMAGE", "CALLSTAMP", "TIMESTAMP");

    for(var i=0; i < levels.length; i++){    
        var chk = document.getElementById(levels[i]);
        if(box.checked != chk.checked){
        	chk.click();
        }
    } 
}

function filter(fieldname, column){
    var textfield  = document.getElementsByName(fieldname);
    
    var classnames = textfield[0].value.split(";");
    var hiderow = true;
    for(i=0 ; i< tbl.rows.length; i++){
        hiderow = true;
        for(j=0; j<classnames.length;j++){
	       hiderow = hiderow && (tbl.rows[i].cells[column].innerHTML.indexOf(classnames[j]) == -1)
	       //alert(tbl.rows[i].cells[column].innerHTML);
        }
        if(hiderow){
            tbl.rows[i].style.display = "none";
        }else{
            tbl.rows[i].style.display = "";
        }
    }
    var levels = new Array("RESULT", "ERROR", "WARN", "DEBUG", "INFO", "ALWAYS", "SETTING", "TESTPLAN", "EXCEPTION", "IMAGE");
    for(var i=0; i < levels.length; i++){    
        var chk = document.getElementsByName(levels[i]);
        if(!chk[0].checked){
            changeVis(levels[i]) 
        }
    } 
}