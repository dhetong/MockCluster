package output;

public class ObjectOutPutInfo {
	private String classname;
	private String initializertype;
	private String methodname;
	private String argstype;
	private String argscontent;	
	private String fieldname;
	
	public ObjectOutPutInfo(String cname, String inittype, String mname, String typelist, String contentlist, String fname) {
		classname = cname;
		initializertype = inittype;
		methodname = mname;
		argstype = typelist;
		argscontent = contentlist;
		fieldname = fname;
	}
	
	public String getClassName() {
		return classname;
	}
	
	public String getMethodName() {
		return methodname;
	}
	
	public String getInitType() {
		return initializertype;
	}
	
	public String getArgsType() {
		return argstype;
	}
	
	public String getArgsContent() {
		return argscontent;
	}
	
	public String getFieldName() {
		return fieldname;
	}
}
