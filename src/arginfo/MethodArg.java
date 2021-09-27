package arginfo;

import java.util.ArrayList;
import java.util.List;

public class MethodArg extends ArgInfo{
	private String method_name;
	private String object_name;
	private boolean hasarg;
	private List<ArgInfo> args = new ArrayList<>();
	
	public MethodArg(String object, String method) {
		method_name = method;
		object_name = object;
	}
	
	public void InitHasArg(boolean flag) {
		hasarg = flag;
	}
	
	public void addArg(ArgInfo arg) {
		args.add(arg);
	}
}
