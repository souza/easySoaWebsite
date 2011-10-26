var AJAX = createXMLHttpRequest();
var AJAX2 = createXMLHttpRequest();
var AJAX3 = createXMLHttpRequest();

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
		tree.attachEvent("onClick", onTreeClick);
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

function onTreeClick() {
	var selectId = tree.getSelectedItemId();
	AJAX.onreadystatechange = getComponentContent;
	AJAX.open("GET", "/rest/componentContent?id=" + selectId);
	AJAX.send("");
	AJAX2.onreadystatechange = getActionMenu;
	AJAX2.open("GET", "/rest/componentMenu?id=" + selectId);
	AJAX2.send("");
}

function action(action){
	var selectId = tree.getSelectedItemId();
	AJAX3.open("POST", "/rest/addElement", true);
	AJAX3.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	AJAX3.send("id=" + selectId+"&action="+action);
	if(action == "addBinding"){
		tree.insertNewChild(selectId,selectId+"+binding+uri","uri",0,"Binding.gif","Binding.gif","Binding.gif","SELECT,CALL");
	}
	else if(action == "deleteBinding"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "addComponentService"){
		tree.insertNewChild(selectId,selectId+"+service+name","name",0,"ComponentService.gif","ComponentService.gif","ComponentService.gif","SELECT,CALL");
	}
	else if(action == "addComponentReference"){
		tree.insertNewChild(selectId,selectId+"+reference+name","name",0,"ComponentReference.gif","ComponentReference.gif","ComponentReference.gif","SELECT,CALL");
	}
	else if(action == "addComponentProperty"){
		tree.insertNewChild(selectId,selectId+"+property+name","name",0,"Property.gif","Property.gif","Property.gif","SELECT,CALL");
	}
	else if(action == "deleteComponent"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "deleteComponentService"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "deleteComponentReference"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "deleteComponentProperty"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "addComponent"){
		tree.insertNewChild(selectId,"component+name","name",0,"Component.gif","Component.gif","Component.gif","SELECT,CALL");
	}
	else if(action == "addService"){
		tree.insertNewChild(selectId,"service+name","name",0,"Service.gif","Service.gif","Service.gif","SELECT,CALL");
	}
	else if(action == "addReference"){
		tree.insertNewChild(selectId,"reference+name","name",0,"Reference.gif","Reference.gif","Reference.gif","SELECT,CALL");
	}
	else if(action == "deleteService"){
		tree.deleteItem(selectId, true);
	}
	else if(action == "deleteReference"){
		tree.deleteItem(selectId, true);
	}
	onTreeClick();
}
