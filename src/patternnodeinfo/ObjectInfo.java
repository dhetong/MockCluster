package patternnodeinfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;

import arginfo.ArgInfo;

public class ObjectInfo extends Info {
	private int info_type;
	
	private String varname;
	private String classname;
	
	private String field;
	
	private Statement stmt_ast;
	
	//parameter for StringLiteral
	private String content;
	
	//parameters for MethodInvocation
	private String invoked_object;
	private String invoked_method;
	
	//parameters for ClassInstanceCreation
	private String type_name;
	
	//parameter for MethodInvocation and ClassInstanceCreation
	private boolean hasargs;
	private boolean argshassimple = false;
	private List<ArgInfo> argslist = new ArrayList<>();
	
	//public
	public ObjectInfo() {
		initInfoType(InfoType.OBJECT_INIT_INFO);
	}
	
	public void InitInfoType(int type) {
		//type = 1, if the fragment is a StringLiteral
		//type = 2, if the fragment is a MethodInvocation
		//type = 3, if the fragment is a ClassInstanceCreation
		info_type = type;
	}
	
	public void InitVarName(String var) {
		varname = var;
	}
	
	public void InitClass(String cname) {
		classname = cname;
	}
	
	public int getInfoType() {
		return info_type;
	}
	
	public void InitField(String f) {
		field = f;
	}
	
	public String getField() {
		return field;
	}
	
	//for StringLiteral
	public void InitContent(String strcontent) {
		content = strcontent;
	}
	
	public String getContent() {
		return content;
	}
	
	//for MethodInvocation
	public void InitInvokedObject(String invoked) {
		invoked_object = invoked;
	}
	
	public void InitInvokedMethod(String invoked) {
		invoked_method = invoked;
	}
	
	//for ClassInstanceCreation
	public void InitTypeName(String cname) {
		//the created class type for ClassInstanceCreation
		type_name = cname;
	}
	
	//for MethodInvocation and ClassInstanceCreation
	public void InitHasArgs(boolean has) {
		hasargs = has;
	}
	
	public void InitArgs(List<ArgInfo> args) {
		argslist.addAll(args);
	}
	
	public void InitArgHasSimple(boolean hassimple) {
		argshassimple = hassimple;
	}
	
	public boolean ArgsHasSimple() {
		return argshassimple;
	}
	
	private String stmt;
	private int position;
	
	public void InitStmt(String s) {
		stmt = s;
	}
	
	public String getStmt() {
		return stmt;
	}
	
	public void InitPosition(int pos) {
		position = pos;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void InitStmt_ast(Statement s) {
		stmt_ast = s;
	}
	
	public Statement getStmt_ast() {
		return stmt_ast;
	}
}
