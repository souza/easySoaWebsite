var AJAX = createXMLHttpRequest();
var AJAX2 = createXMLHttpRequest();
var AJAX3 = createXMLHttpRequest();
var AJAX4 = createXMLHttpRequest();
var editor;
var tree;
var popUp;

function createXMLHttpRequest() {
	if (typeof XMLHttpRequest == "undefined")
		XMLHttpRequest = function() {
			try {
				return new ActiveXObject("Msxml2.XMLHTTP.6.0")
			} catch (e) {
			}
			try {
				return new ActiveXObject("Msxml2.XMLHTTP.3.0")
			} catch (e) {
			}
			try {
				return new ActiveXObject("Msxml2.XMLHTTP")
			} catch (e) {
			}
			try {
				return new ActiveXObject("Microsoft.XMLHTTP")
			} catch (e) {
			}
			throw new Error("This browser does not support XMLHttpRequest.")
		};
	return new XMLHttpRequest();
}

function handler() {
	if (AJAX.readyState == 4 && AJAX.status == 200) {
		var res = AJAX.responseText;
		var json = eval("(" + res + ")");
		tree = new dhtmlXTreeObject("serviceView", "100%", "100%", 0);
		tree.setDataMode("json");
		tree.setImagePath("images/sca/");
		//tree.attachEvent("onClick", onTreeClick);
		tree.attachEvent("onSelect", onTreeClick);
		tree.enableKeyboardNavigation(true);
		tree.loadJSONObject(json, function() {
		});

	}
}

function show() {
	AJAX.onreadystatechange = handler;
	AJAX.open("GET", "/rest/tree");
	AJAX.send("");
};

function getComponentContent() {
	if (AJAX.readyState == 4 && AJAX.status == 200) {
		var res = AJAX.responseText;
		document.getElementById("component_frame_content").innerHTML = res;
	}
}

function getActionMenu() {
	if (AJAX2.readyState == 4 && AJAX2.status == 200) {
		var res = AJAX2.responseText;
		document.getElementById("menuObj").innerHTML = res;
	}
}

function getGlobalId(){
	var selectId = tree.getSelectedItemId();
	var globalId = "";
	while(selectId != "composite+"){
		globalId = selectId+globalId;
		selectId = tree.getParentId(selectId);
	}
	return globalId;
}

function onTreeClick() {
	var globalId = getGlobalId();
	AJAX.onreadystatechange = function(){
		if (AJAX.readyState == 4 && AJAX.status == 200) {
			var res = AJAX.responseText;
			document.getElementById("component_frame_content").innerHTML = res;
			if(globalId.indexOf("implementation") != -1){
				initEditor();
			}
		}
	};
	AJAX.open("GET", "/rest/componentContent?id=" + globalId);
	AJAX.send("");
	AJAX2.onreadystatechange = getActionMenu;
	AJAX2.open("GET", "/rest/componentMenu?id=" + globalId);
	AJAX2.send("");
	
}

function action(action){
	var id = tree.getSelectedItemId();
	var globalId = getGlobalId();
	AJAX3.open("POST", "/rest/addElement", true);
	AJAX3.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	AJAX3.send("id=" + globalId+"&action="+action);
	if(action == "addBinding"){
		tree.insertNewChild(id,"+binding+uri","uri",0,"Binding.gif","Binding.gif","Binding.gif","SELECT,CALL");
	}
	else if(action == "deleteBinding"){
		tree.deleteItem(id, true);
	}
	else if(action == "addComponentService"){
		tree.insertNewChild(id,"+service+name","name",0,"ComponentService.gif","ComponentService.gif","ComponentService.gif","SELECT,CALL");
	}
	else if(action == "addComponentReference"){
		tree.insertNewChild(id,"+reference+name","name",0,"ComponentReference.gif","ComponentReference.gif","ComponentReference.gif","SELECT,CALL");
	}
	else if(action == "addComponentProperty"){
		tree.insertNewChild(id,"+property+name","name",0,"Property.gif","Property.gif","Property.gif","SELECT,CALL");
	}
	else if(action == "deleteComponent"){
		tree.deleteItem(id, true);
	}
	else if(action == "deleteComponentService"){
		tree.deleteItem(id, true);
	}
	else if(action == "deleteComponentReference"){
		tree.deleteItem(id, true);
	}
	else if(action == "deleteComponentProperty"){
		tree.deleteItem(id, true);
	}
	else if(action == "addComponent"){
		tree.insertNewChild(id,"component+name","name",0,"Component.gif","Component.gif","Component.gif","SELECT,CALL");
	}
	else if(action == "addService"){
		tree.insertNewChild(id,"service+name","name",0,"Service.gif","Service.gif","Service.gif","SELECT,CALL");
	}
	else if(action == "addReference"){
		tree.insertNewChild(id,"reference+name","name",0,"Reference.gif","Reference.gif","Reference.gif","SELECT,CALL");
	}
	else if(action == "deleteService"){
		tree.deleteItem(id, true);
	}
	else if(action == "deleteReference"){
		tree.deleteItem(id, true);
	}
	else if(action == "deleteImplementation"){
		tree.deleteItem(id, true);
	}
	onTreeClick();
}


function changeImplementation(){
	var id = $("select[name='implementation-type'] option:selected").val();
	var globalId = getGlobalId();
	AJAX.onreadystatechange = function(){
		if (AJAX.readyState == 4 && AJAX.status == 200) {
			var res = AJAX.responseText;
			document.getElementById("implementation-panel").innerHTML = res;
			initEditor();
		}
	};
	AJAX.open("GET", "/rest/implementationContent?modelId="+globalId+"&id=" + id);
	AJAX.send("");
}

function changeInterface(){
	var id = $("select[name='interface-type'] option:selected").val();
	var globalId = getGlobalId();
	AJAX.onreadystatechange = function(){
		if (AJAX.readyState == 4 && AJAX.status == 200) {
			var res = AJAX.responseText;
			document.getElementById("interface-panel").innerHTML = res;
		}
	};
	AJAX.open("GET", "/rest/interfaceContent?modelId="+globalId+"&id=" + id);
	AJAX.send("");
}

function changeBinding(){
	var id = $("select[name='binding-type'] option:selected").val();
	var globalId = getGlobalId();
	AJAX.onreadystatechange = function(){
		if (AJAX.readyState == 4 && AJAX.status == 200) {
			var res = AJAX.responseText;
			document.getElementById("component_frame_content").innerHTML = res;
		}
	};
	AJAX.open("GET", "/rest/bindingContent?modelId="+globalId+"&id=" + id);
	AJAX.send("");
}

function getTemplateForm(){
	var templateName = $("select[name='template'] option:selected").val();
	AJAX.onreadystatechange = function(){
		if (AJAX.readyState == 4 && AJAX.status == 200) {
			var res = AJAX.responseText;
			document.getElementById("templateForm").innerHTML = res;
		}
	};
	AJAX.open("GET", "/rest/templateForm?templateName=" + templateName);
	AJAX.send("");
}

function initEditor(){
	editor = ace.edit("editor");
	AJAX4.onreadystatechange = function(){
		if (AJAX4.readyState == 4 && AJAX4.status == 200) {
			var res = AJAX4.responseText;
			editor.getSession().setValue(res);
			AJAX3.onreadystatechange = function(){
				if (AJAX3.readyState == 4 && AJAX3.status == 200) {
					var res = AJAX3.responseText;
					if(res == "javascript"){
						var JavaScriptMode = require("ace/mode/javascript").Mode;
						editor.getSession().setMode(new JavaScriptMode());
					}
					else if(res == "java"){
						var JavaMode = require("ace/mode/java").Mode;
						editor.getSession().setMode(new JavaMode());
					}
				}
			};
			AJAX3.open("GET", "/rest/editorMode");
			AJAX3.send("");
		}
	};
	AJAX4.open("GET", "/rest/implementationFileContent");
	AJAX4.send("");
}

function saveImplementation(contentText){
	$.post("/rest/saveImplementation",{content : contentText});
//	AJAX3.open("POST", "/rest/saveImplementation", true);
//	AJAX3.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
//	AJAX3.send("content="+content);
	return false;
}

function getEditorValue(){
	return editor.getSession().getValue();
}

function openPopUp(page,name,width,height,params){
	var top=(screen.height-height)/2;
	var left=(screen.width-width)/2;
	popUp = window.open(page,name,"top="+top+",left="+left+",width="+width+",height="+height+","+params);
}



function addImplementation(){
	openPopUp("addImplementation.html",'addImplem',250,100,'toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, directories=no, status=no');
}

function closePopUp(){
	popUp.close();
}

function createImplementation(choice){
	closePopUp();
	if(choice == "upload"){
		openPopUp("addImplementationUpload.html",'upload',400,100,'toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, directories=no, status=no');
	}
	else if(choice == "create"){
		openPopUp("addImplementationCreate.html",'create',250,100,'toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, directories=no, status=no');
	}
}

function createImplementationUpload(file, implementationType){
	closePopUp();
	var globalId = getGlobalId();
	var millisecondsToWait = 500;
	setTimeout(function() {
		$.post("/rest/createNewImplementation",{id : globalId, className : file, implemType : implementationType, createFile : false},function(data) {
			tree.insertNewChild(tree.getSelectedItemId(),"+implementation",file,0,"Implementation.gif","Implementation.gif","Implementation.gif","SELECT,CALL");
		});
	}, millisecondsToWait);
}

function createNewImplementation(className, implemType){
	closePopUp();
	var globalId = getGlobalId();
	$.post("/rest/createNewImplementation",{id : globalId, className : className, implemType : implemType, createFile : true},function(data) {
		tree.insertNewChild(tree.getSelectedItemId(),"+implementation",className,0,"Implementation.gif","Implementation.gif","Implementation.gif","SELECT,CALL");
	});
}

