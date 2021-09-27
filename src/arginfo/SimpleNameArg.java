package arginfo;

public class SimpleNameArg extends ArgInfo {
	private String name;
	
	public SimpleNameArg(String var) {
		name = var;
		InitType(ArgType.SIMPLE_NAME_TYPE);
	}
	
	public String getName() {
		return name;
	}
}
