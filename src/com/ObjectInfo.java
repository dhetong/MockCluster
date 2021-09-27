package com;

public class ObjectInfo extends Info {
	private int object_type;
	
	private String varname;
	private String classname;
	
	//parameter for StringLiteral
	private String content;
	//parameters for MethodInvocation
	private String invoked_object;
	private String invoked_method;
	
	public void InitType(int type) {
		//type = 1, if the fragment is a StringLiteral
		//type = 2, if the fragment is a MethodInvocation
		//type = 3, if the fragment is a ClassInstanceCreation
		object_type = type;
	}
	
	//public
	public void InitVarName(String var) {
		varname = var;
	}
	
	public void InitClass(String cname) {
		classname = cname;
	}
	
	public int getType() {
		return object_type;
	}
	
	//for StringLiteral
	public void InitContent(String strcontent) {
		content = strcontent;
	}
	
	//for MethodInvocation
	public void InitInvokedObject(String invoked) {
		invoked_object = invoked;
	}
	
	public void InitInvokedMethod(String invoked) {
		invoked_method = invoked;
	}
}
