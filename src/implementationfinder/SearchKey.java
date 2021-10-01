package implementationfinder;

public class SearchKey {
	private String classname;
	private String methodname;
	private String fieldname;
	
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
}
