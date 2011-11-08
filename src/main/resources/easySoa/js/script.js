function createApplication(){
	$.post("/rest/createApplication", $("#create").serialize());
}