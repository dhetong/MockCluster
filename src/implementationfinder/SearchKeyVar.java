package implementationfinder;

public class SearchKeyVar {
	private String varname;
	private String methodname;
	
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
}
