package implementationfinder;

public class SearchKey {
	private String classname;
	private String methodname;
	private String fieldname;
	
	private String returntype;
	
	public SearchKey(String cname, String mname, String field) {
		classname = cname;
		methodname = mname;
		fieldname = field;
	}
	
	public void InitClass(String name) {
		classname = name;
	}
	
	public void InitMethod(String name) {
		methodname = name;
	}
	
	public String getClassName() {
		return classname;
	}
	
	public String getMethodName() {
		return methodname;
	}
	
	public String getFieldName() {
		return fieldname;
	}
	
	public void InitReturnType(String type) {
		returntype = type;
	}
	
	public String getReturnType() {
		return returntype;
	}
}
