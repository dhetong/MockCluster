package arginfo;

public class ArgInfo {
	private int arg_type;
	
	public void InitType(int type) {
		//type = 1, if arg is StringLiteral
		//type = 2, if arg is NumberLiteral
		//type = 3, if arg is SimpleName
		arg_type = type;
	}
}
