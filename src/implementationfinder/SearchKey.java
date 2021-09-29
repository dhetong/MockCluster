package implementationfinder;

public class SearchKey {
	private String classname;
	private String methodname;
	
	public SearchKey(String cname, String mname) {
		classname = cname;
		methodname = mname;
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
}
