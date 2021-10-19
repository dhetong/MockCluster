package implementationfinder;

public class SearchKeyVar {
	private String varname;
	private String methodname;
	private String classname;
	
	private String returntype;
	
	public SearchKeyVar(String var, String method) {
		varname = var;
		methodname = method;
	}
	
	public String getVarName() {
		return varname;
	}
	
	public String getMethodName() {
		return methodname;
	}
	
	public void UpdateClassName(String cname) {
		classname = cname;
	}
	
	public String getClassName() {
		return classname;
	}
	
	public void UpdateReturnType(String type) {
		returntype = type;
	}
	
	public String getReturnType() {
		return returntype;
	}
}
