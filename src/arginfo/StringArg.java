package arginfo;

public class StringArg extends ArgInfo {
	private String content;
	
	public StringArg(String strcontent) {
		content = strcontent;
		InitType(ArgType.STRING_TYPE);
	}
	
	public String getContent() {
		return content;
	}
}
