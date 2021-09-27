package arginfo;

public class NumberArg extends ArgInfo {
	private int number;
	
	public NumberArg(int n) {
		number = n;
		InitType(ArgType.NUMBER_TYPE);
	}
	
	public int getNumber() {
		return number;
	}
}
